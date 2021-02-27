package model;

import ui.sound.MidiSynth;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
//import static ui.CompositionPanel.*;
import static ui.GraphicalEditorApp.*;
// consider splitting graphical responsibilities into new class

// represents an entire composition, which is a collection of measures. The order of measures in the list is the order
// of measures in the composition.
public class Composition  { // used to extend JPanel
    private List<Measure> listOfMeasure;
    private int beatNum;
    private int beatType;
    private int barWidth; // implement later
    public static int resolution = 4; //4
    // the resolution is the number of ticks per quarter beat.

    private Map<Integer, List<Note>> noteMap;
    private Map<Integer, Measure> measureMap;


    // REQUIRES: beatType is a power of 2
    // EFFECTS: instantiates a new composition with numMeasures measures, beatNum beats of type beatType per measure.
    public Composition(int numMeasures, int beatNum, int beatType) {
        this.beatNum = beatNum;
        this.beatType = beatType;
        listOfMeasure = new ArrayList<Measure>();
        for (int i = 0; i < numMeasures; i++) {
            Measure tempMeasure = new Measure(beatNum, beatType, this, i + 1);
            listOfMeasure.add(tempMeasure);
        }
    }

    public void setResolution(int resolution) {
        Composition.resolution = resolution;
    }

    // OBSOLETE
    // REQUIRES: beat > 0
    // EFFECTS: returns the measure which has the given global beat within it. If none have the given beat, return null.
    public Measure getMeasureAtBeat(int beat) {
        int beatCount = 0;
        for (Measure measure: listOfMeasure) {
            beatCount += measure.getBeatNumber();
            if (beatCount >= beat) {
                return measure;
            }
        }
        return null;
    }

    // REQUIRES: tick > 0
    // EFFECTS: returns the measure which has the given tick within it. If none, return null.
    public Measure getMeasureAtTick(int tick) {
        int tickCount = 0;
        for (Measure m: listOfMeasure) {
            tickCount += m.getNumTicks();
            if (tickCount >= tick) {
                return m;
            }
        }
        return null;
    }

    // EFFECTS: adds a midisynth to all notes.
    public void addMidiSynthToAll(MidiSynth midiSynth) {
        for (Measure measure : listOfMeasure) {
            measure.addMidiSynthToAll(midiSynth);
        }
    }

    // EFFECTS: returns list containing the x screen coordinates of all measure lines in the composition.
    public List<Integer> getTickPositionsOfMeasureLines() {
        List<Integer> output = new ArrayList<Integer>();
        output.add(0);
        int tickCount = 0;
        for (Measure m: listOfMeasure) {
            tickCount += m.getNumTicks();
            output.add(tickCount);
        }
        return output;
    }


    // EFFECTS: returns the global start tick of the given measure.
    public int getGlobalStartOf(Measure measure) {
        int output = 0;
        for (Measure m: listOfMeasure) {
            if (!m.equals(measure)) {
                output += m.getNumTicks();
            } else {
                output++;
                return output;
            }
        }
        return 0; //this should throw an exception probably
    }

    // EFFECTS: returns the global start tick of the given measure number.
    public int getGlobalStartOf(int measure) throws Exception {
        if (measure < 1 || measure > getNumMeasures()) {
            throw new Exception("tried to get start tick of measure out of bounds");
        }
        int output = 0;
        for (int i = 1; i < measure; i++) {
            output += getMeasure(i).getNumTicks();
        }
        return output; //this should throw an exception probably
    }

    // EFFECTS: returns the note at a given point in composition, if none, return null.
    public Note getNoteAtPoint(Point point) {
        for (Measure measure : listOfMeasure) {
            for (Note note : measure.getListOfNote()) {
                if (note.contains(point)) {
                    return note;
                }
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: adds a note at specified pitch, tick, with specified value, and returns it
    public Note addNoteAtPoint(Point p, int value) {
        return new Note(getMeasureAtTick(p.x), p.x, value, p.y, midiSynth);
    }



    // EFFECTS: returns list of notes at a given tick in the composition.
    public List<Note> getNotesAtTick(int tick) {
        List<Note> notesAtTick = new ArrayList<>();
        /*
        for (Measure m: listOfMeasure) {
            for (Note note : m.getListOfNote()) {
                if (note.containsTick(tick)) {
                    notesAtTick.add(note);
                }
            }
        }

         */

        int ticksPerMeasure = listOfMeasure.get(0).getNumTicks();
        int index = (tick - 1) / ticksPerMeasure;
        Measure measure = listOfMeasure.get(index);
        for (Note note : measure.getListOfNote()) {
            if (note.containsTick(tick)) {
                notesAtTick.add(note);
            }
        }
        return notesAtTick;

    }

    // REQUIRES: pos <= number of measures, beatType is a power of 2
    // MODIFIES: this
    // EFFECTS: adds n new measures to composition after the posth measure with beatNum beats of type beatType.
    public void addMeasures(int n, int pos, int beatNum, int beatType) {
        //int prevSize = listOfMeasure.size();
        for (int i = 0; i < n; i++) {
            Measure tempMeasure = new Measure(beatNum, beatType, this, pos + i + 1);
            listOfMeasure.add(pos + i, tempMeasure);
            countAndNotifyMeasures();
            assert checkMeasureNums();
            //.add adds the element at the index and shifts everything afterwards to the right by one.
        }
    }

    // EFFECTS: checks the class invariant that the measures have correct measure numbers for their order in
    // listOfMeasure
    private boolean checkMeasureNums() {
        int count = 0;
        for (Measure measure : listOfMeasure) {
            count++;
            if (count != measure.getMeasureNumber()) {
                return false;
            }
        }
        return true;
    }

    // REQUIRES: there is a measure at every position specified and there are no duplicates.
    // MODIFIES: this
    // EFFECTS: removes the specified measures from composition, and makes sure note positions and measure numbers
    // resulting are updated.
    public void removeMeasures(List<Integer> listOfPos) {
        // reverse list to remove last first
        Collections.sort(listOfPos); // have to sort first
        Collections.reverse(listOfPos);
        for (Integer pos : listOfPos) {
            int tickDiff = -1 * listOfMeasure.get(pos - 1).getNumTicks();
            updateNotePositions(tickDiff, pos); // pos - 1 + 1
            listOfMeasure.remove(pos - 1);
        }
        countAndNotifyMeasures();
    }

    // MODIFIES: this
    // EFFECTS: for all measures from the one after the one to be removed until the end, adjust note starts by
    // tickDiff.
    private void updateNotePositions(int tickDiff, int index) {
        for (int i = index; i < listOfMeasure.size(); i++) {
            listOfMeasure.get(i).adjustNotes(tickDiff);
        }
    }

    // EFFECTS: notifies all measures of their measure numbers.
    private void countAndNotifyMeasures() {
        int count = 0;
        for (Measure measure: listOfMeasure) {
            count++;
            measure.setMeasureNumber(count);
        }
    }

    // EFFECTS: returns the number of measures in the composition.
    public int getNumMeasures() {
        return listOfMeasure.size();
    }

    // EFFECTS: returns the total number of beats in the composition.
    public int getNumBeats() {
        int numBeats = 0;
        for (Measure measure : listOfMeasure) {
            numBeats = numBeats + measure.getBeatNumber();
        }
        return numBeats;
    }

    // EFFECTS: returns the total number of ticks in the composition.
    public int getNumTicks() {
        int numTicks = 0;
        for (Measure measure : listOfMeasure) {
            numTicks += measure.getNumTicks();
        }
        return numTicks;
    }

    // EFFECTS: returns the measure at pos
    public Measure getMeasure(int pos) {
        return listOfMeasure.get(pos - 1);
    }
/*
    public int getPosOfMeasure(Measure measure) {
        return 0;
    }
*/

    // EFFECTS: return formatted content of composition
    public String getContents() {
        String tempString = "";
        for (int i = 0; i < listOfMeasure.size(); i++) {
            int n = i + 1;
            tempString = tempString + "\n" + "Measure" + n + "\n" + listOfMeasure.get(i).getContents();
        }
        return tempString;
    }

    // REQUIRES: measure not null
    // MODIFIES: this
    // EFFECTS: add measure to end of composition
    public void addMeasure(Measure measure) {
        measure.assignToComposition(this);
        listOfMeasure.add(measure);
        measure.setMeasureNumber(listOfMeasure.size());
    }

    /*
    // remove param note from this. Return true if found and removed, false otherwise.
    public boolean removeNote(Note note) {
        note.unassignFromMeasure();

    }
    */


    public int getBeatNum() {
        return beatNum;
    }

    public int getBeatType() {
        return beatType;
    }

    public List<Measure> getListOfMeasure() {
        return listOfMeasure;
    }


    public int getResolution() {
        return resolution;
    }
}
