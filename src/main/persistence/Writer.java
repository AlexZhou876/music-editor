package persistence;

import model.Composition;
import model.Measure;
import model.Note;
import static persistence.Reader.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// writes a midi file based on program data.
public class Writer {
    private int ticksPerBeat = 24;
    private static int ARBITRARY_BUFFER = 10;

    public void writeFile(Composition composition, String path) {
        int beatNum = composition.getBeatNum();
        int beatType = composition.getBeatType();
        try {
            Sequence sequence = new Sequence(Sequence.PPQ,ticksPerBeat);
            Track track = sequence.createTrack();
            setTempo(track);
            setTimeSignature(track, beatNum, beatType);
            List<MidiEvent> noteEvents = getNoteEvents(composition, beatNum);
            sortEvents(noteEvents);
            addEvents(track, noteEvents);
            addEndOfTrackEvent(track);
            File file = new File(path);
            MidiSystem.write(sequence, 1, file);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setTempo(Track track) throws InvalidMidiDataException {
        MidiEvent me;
        MetaMessage mt = new MetaMessage();
        byte[] bt = {0x02, (byte)0x00, 0x00};
        mt.setMessage(0x51, bt, 3);
        me = new MidiEvent(mt,(long)0);
        track.add(me);
    }

    private void setTimeSignature(Track track, int beatNum, int beatType) throws InvalidMidiDataException {
        MidiEvent me;
        MetaMessage mt = new MetaMessage();
        byte power = getPower(beatType);
        byte[] message = {(byte) beatNum, power, 0x18, 0x08};
        mt.setMessage(0x58, message, 4);
        me = new MidiEvent(mt, 0);
        track.add(me);
    }

    private byte getPower(int beatType) {
        switch (beatType) {
            case 2: return 1;
            case 4: return 2;
            case 8: return 3;
            case 16: return 4;
            case 32: return 5;
            case 64: return 6;
            default: return 1;
        }
    }

    private List<MidiEvent> getNoteEvents(Composition composition, int beatNum) throws InvalidMidiDataException {
        List<MidiEvent> output = new ArrayList<>();
        List<Measure> listOfMeasure = composition.getListOfMeasure();
        for (int i = 0; i < listOfMeasure.size(); i++) {
            for (Note n: listOfMeasure.get(i).getListOfNote()) {
                ShortMessage noteOn = new ShortMessage();
                int pitch = n.getPitch() + MIDI_A0_VALUE;
                noteOn.setMessage(0x90, pitch, 0x60);
                ShortMessage noteOff = new ShortMessage();
                noteOff.setMessage(0x80, pitch, 0);
                //long startTime = (i  * beatNum + n.getStart()) * ticksPerBeat - ticksPerBeat;
                long startTime = n.getGlobalStart() * ticksPerBeat;
                long endTime = (n.getGlobalStart() + n.getValue()) * ticksPerBeat;
                //long endTime = (i * beatNum + n.getStart() + n.getValue()) * ticksPerBeat - ticksPerBeat;
                // - ticksPerBeat to adjust since beat 1 in midi is tick 0, not tick ticksPerBeat
                output.add(new MidiEvent(noteOn, startTime));
                output.add(new MidiEvent(noteOff, endTime));
            } // changed to i from (i-1)
        }
        return output;
    }

    private void sortEvents(List<MidiEvent> noteEvents) {
        Collections.sort(noteEvents, Comparator.comparing(MidiEvent::getTick));
    }

    private void addEvents(Track track, List<MidiEvent> noteEvents) {
        for (MidiEvent me: noteEvents) {
            track.add(me);
        }
    }

    private void addEndOfTrackEvent(Track track) throws InvalidMidiDataException {
        MetaMessage mt = new MetaMessage();
        byte[] bet = {};
        mt.setMessage(0x2F,bet,0);
        MidiEvent me = new MidiEvent(mt,track.ticks() + ARBITRARY_BUFFER);
        track.add(me);
    }


}
