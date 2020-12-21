package persistence;

import model.Composition;
import model.Measure;
import model.Note;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Reads and parses midi files. Does not need constructor (is not instantiated).
// Assumes time signature does not change throughout composition.
public class Reader {
    public static final int NOTE_ON = 9;
    public static final int NOTE_OFF = 8;
    public static final int REST_OF_STATUS_BYTE = 4;
    public static final byte TIME_SIGNATURE_META_TYPE = 0x58;
    public static final int MIDI_A0_VALUE = 21;
    private static int ticksPerBeat;
    private static int beatNum;
    private static int beatType;



    public static Composition readFile(File file) {
        Sequence sequence = null;
        try {
            sequence = MidiSystem.getSequence(file);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ticksPerBeat = sequence.getResolution();
        List<Measure> listOfMeasure = parseSequence(sequence);
        return parseMeasures(listOfMeasure);
    }

    // EFFECTS: returns a list of measures based on a midi sequence
    private static List<Measure> parseSequence(Sequence sequence) {
        Track[] tracks = sequence.getTracks();
        List<MidiEvent> midiEvents = getMidiEvents(tracks);
        getTimeSignature(midiEvents);
        List<Note> notes = midiEventsToNotes(midiEvents);
        List<Measure> output = allocateNotes(notes);
        //convertNoteStarts(output);
        return output;
    }



    // REQUIRES: non-empty track array
    // EFFECTS: returns a list of all midi events in all tracks.
    private static List<MidiEvent> getMidiEvents(Track[] tracks) {
        List<MidiEvent> output = new ArrayList<>();
        for (Track t: tracks) {
            for (int i = 0; i < t.size(); i++) {
                output.add(t.get(i));
            }
        }
        return output;
    }

    // REQUIRES: non-empty list of midi events.
    // EFFECTS: returns list of notes corresponding to note-on and note-off events.
    // note: sometimes programs may end a note slightly before its nominal value. To deal with this, round up the value
    // to nearest int instead of casting.
    /*
    private static List<Note> midiEventsToNotes(List<MidiEvent> midiEvents) {
        List<Note> output = new ArrayList<>();
        List<MidiEvent> noteEvents = filterEvents(midiEvents);
        for (int i = 0; i < noteEvents.size(); i++) {
            MidiEvent potentialOn = noteEvents.get(i);
            int eventType = potentialOn.getMessage().getStatus() >> REST_OF_STATUS_BYTE;
            int velocity = potentialOn.getMessage().getMessage()[2];
            if (eventType == NOTE_ON && velocity != 0) { // velocity check here not technically necessary
                byte pitch = potentialOn.getMessage().getMessage()[1];
                int startBeat = 1 + (int) (potentialOn.getTick() / ticksPerBeat);
                for (MidiEvent potentialOff: noteEvents) {
                    int pairType =  potentialOff.getMessage().getStatus() >> REST_OF_STATUS_BYTE;
                    int velocity2 = potentialOff.getMessage().getMessage()[2];
                    byte pitch2 = potentialOff.getMessage().getMessage()[1];
                    int endBeat = 1 + (int) Math.round((double) potentialOff.getTick() / (double) ticksPerBeat);
                    if ((pairType == NOTE_OFF || velocity2 == 0) && pitch == pitch2 && endBeat > startBeat) {
                        int value = endBeat - startBeat;
                        int pianoKey = pitch - MIDI_A0_VALUE;
                        output.add(new Note(startBeat, value, pianoKey)); //+1: see Writer.getNoteEvents
                        // ok to use obsolete constructor here because midisynth and measure can be added later
                        break;
                    }
                }
            }
        }
        return output;
    }

     */


    private static List<Note> midiEventsToNotes(List<MidiEvent> midiEvents) {
        List<Note> output = new ArrayList<>();
        List<MidiEvent> noteEvents = filterEvents(midiEvents);
        while (!noteEvents.isEmpty()) {
            MidiEvent potentialOn = noteEvents.get(0);
            int eventType = potentialOn.getMessage().getStatus() >> REST_OF_STATUS_BYTE;
            int velocity = potentialOn.getMessage().getMessage()[2];
            if (eventType == NOTE_ON && velocity != 0) { // velocity check here not technically necessary
                byte pitch = potentialOn.getMessage().getMessage()[1];
                int startTick = (int) potentialOn.getTick() + 1; // midi tick starts at 0, model tick starts at 1
                for (MidiEvent potentialOff: noteEvents) {
                    int pairType =  potentialOff.getMessage().getStatus() >> REST_OF_STATUS_BYTE;
                    int velocity2 = potentialOff.getMessage().getMessage()[2];
                    byte pitch2 = potentialOff.getMessage().getMessage()[1];
                    int endTick = 1 + (int) potentialOff.getTick();
                    if ((pairType == NOTE_OFF || velocity2 == 0) && pitch == pitch2 && endTick > startTick) {
                        int value = endTick - startTick;
                        int pianoKey = pitch - MIDI_A0_VALUE;
                        output.add(new Note(startTick, value, pianoKey)); //+1: see Writer.getNoteEvents
                        // ok to use obsolete constructor here because midisynth and measure can be added later
                        noteEvents.remove(potentialOff);
                        noteEvents.remove(potentialOn);
                        break;
                    }
                }
            }
        }
        return output;
    }

    // REQUIRES: unfiltered not empty
    // EFFECTS: returns filtered list of midi events only containing note events
    private static List<MidiEvent> filterEvents(List<MidiEvent> unfiltered) {
        Predicate<MidiEvent> noteEvent = me -> me.getMessage().getStatus() >> REST_OF_STATUS_BYTE == NOTE_ON
                || me.getMessage().getStatus() >> REST_OF_STATUS_BYTE == NOTE_OFF;
        return unfiltered.stream().filter(noteEvent).collect(Collectors.toList());
    }

    // REQUIRES: notes not empty
    // EFFECTS: returns a list of only enough measures to properly allocate notes based on their starts.
    private static List<Measure> allocateNotes(List<Note> notes) {
        List<Measure> output = new ArrayList<>();
        int ticksPerMeasure = ticksPerBeat / beatType * 4 * beatNum;
        for (Note n: notes) {
            int quotient = n.getGlobalStart() / ticksPerMeasure;
            int remainder = n.getGlobalStart() % ticksPerMeasure;
            if (quotient > output.size() && remainder == 0) { // case for no measure, note on last tick
                int outputInitSize = output.size();
                for (int i = 0; i < quotient - outputInitSize; i++) { // used to be <=
                    output.add(new Measure(beatNum, beatType));
                }
                output.get(quotient - 1).addNote(n); // used to be quotient
            } else if (quotient >= output.size() && remainder != 0) { // case for no measure, note not on last tick
                int outputInitSize = output.size();
                for (int i = 0; i < (quotient - outputInitSize) + 1; i++) {
                    output.add(new Measure(beatNum, beatType));
                } // changed i <= to i < to prevent from adding one more measure than necessary
                output.get(quotient).addNote(n); // changed from quotient + 1 to quotient
            } else if (remainder != 0) { // case for measure exists, note not on last beat
                output.get(quotient).addNote(n); // changed from quotient + 1 to quotient
            } else { // case for measure exists, note is on last beat
                output.get(quotient - 1).addNote(n); // changed to quotient - 1
            }
        }
        return output;
    }



    // EFFECTS: returns a composition containing the list of measures in the same order.
    // update: maybe assign each measure to the composition.
    private static Composition parseMeasures(List<Measure> listOfMeasure) {
        Composition composition = new Composition(0, beatNum, beatType);
        for (Measure m: listOfMeasure) {
            composition.addMeasure(m);
        }

        composition.setResolution(ticksPerBeat);
        return composition;
    }

    // REQUIRES: there is exactly one time signature meta event in the list of events.
    // MODIFIES: this
    // EFFECTS: assigns value of time signature numbers to time signature variables
    // note: method looks for time signature meta-message, if none, then 4/4 by default
    private static void getTimeSignature(List<MidiEvent> midiEvents) {
        for (MidiEvent me: midiEvents) {
            byte[] message = me.getMessage().getMessage();
            if (message[1] == TIME_SIGNATURE_META_TYPE) {
                try {
                    beatNum = message[3]; //3
                    beatType = (int) Math.pow(2, message[4]);
                    return;
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
        beatNum = 4;
        beatType = 4;
    }

    // REQUIRES: non empty-list
    // MODIFIES: measures
    // EFFECTS: for all notes in measures, convert their start beats with respect to the measure rather than globally.
    private static void convertNoteStarts(List<Measure> measures) {
        for (int i = 0; i < measures.size(); i++) {
            for (Note n: measures.get(i).getListOfNote()) {
                if (n.getStart() % beatNum != 0) {
                    n.setGlobalStart(n.getStart() % beatNum);
                } else {
                    n.setGlobalStart(beatNum);
                }
                //n.moveTime(n.getStart() - (i - 1) * beatNum);
            } // bug fix:
        }
    }
}