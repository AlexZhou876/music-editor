package model;

import ui.CompositionPanel;
import ui.sound.MidiSynth;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// represents a measure (bar) with a collection of notes which start in the measure, a number of beats, a type (value)
// of beats, and its position within the entire composition.
// simple and compound meter is handled, but not complex meter.

public class Measure {
    private static final int PULSES_PER_COMPOUND_BEAT = 3;
    private List<Note> listOfNote;
    private int beatNumber;
    private int beatType;
    private Composition composition;
    private int measureNumber;


    // REQUIRES: beatType is a power of 2
    // EFFECTS: instantiate an empty Measure with time signature beatNumber/beatType
    public Measure(int beatNumber, int beatType, Composition composition, int measureNumber) {
        this.composition = composition;
        listOfNote = new ArrayList<Note>();
        if (compound(beatNumber)) {
            this.beatNumber = beatNumber / PULSES_PER_COMPOUND_BEAT;
        } else {
            this.beatNumber = beatNumber;
        }
        this.beatType = beatType;
        this.measureNumber = measureNumber;
    }

    // EFFECTS: return true if the time signature of this is compound, false otherwise.
    private boolean compound(int beatNumber) {
        return beatNumber % 3 == 0 && beatNumber != 3;
    }

    // same but allows an unassigned measure
    public Measure(int beatNumber, int beatType) {
        listOfNote = new ArrayList<Note>();
        if (beatNumber % 3 == 0 && beatNumber != 3) {
            this.beatNumber = beatNumber / PULSES_PER_COMPOUND_BEAT;
        } else {
            this.beatNumber = beatNumber;
        }
        this.beatType = beatType;
    }

    // EFFECTS: adds a midisynth to all notes.
    public void addMidiSynthToAll(MidiSynth midiSynth) {
        for (Note note : listOfNote) {
            note.setMidiSynth(midiSynth);
        }
    }

    // EFFECTS: set the measure number of this measure and update note beats accordingly. If the measure number is 0,
    // this is a special case used in tests or Reader and should not trigger the same behaviour.
    // Measure numbers are never 0 in normal runtime.
    public void setMeasureNumber(int measureNumber) {
        if (this.measureNumber != measureNumber && this.measureNumber != 0) {
            //int oldMeasureNumber = this.measureNumber;
            this.measureNumber = measureNumber;
            //int measureDiff = this.measureNumber - oldMeasureNumber;
            //updateNotes(measureDiff);
        } else { // case when measure has just been created and is assigned a number for the first time
            this.measureNumber = measureNumber;
        }
    }

    public int getMeasureNumber() {
        return measureNumber;
    }

    /*
    // MODIFIES: this
    // EFFECTS: gives notes their new global positions after measure number changes.
    private void updateNotes(int measureDiff) {
        int beatDiff = measureDiff * beatNumber;
        for (Note note: listOfNote) {
            note.setGlobalStart(note.getGlobalStart() + beatDiff);
        }
    }

     */

    // MODIIES: this
    // EFFECTS: gives notes their new global positions according to the tick difference.
    public void adjustNotes(int tickDiff) {
        for (Note note: listOfNote) {
            note.setGlobalStart(note.getGlobalStart() + tickDiff);
        }
    }



    // EFFECTS: returns the global start tick of this measure.
    public int getGlobalStartTick() {
        return composition.getGlobalStartOf(this);
    }



    // EFFECTS: returns the measure before this one in the comp.
    public Measure getLast() {
        return composition.getMeasure(composition.getListOfMeasure().indexOf(this));
    }

    // EFFECTS: returns the measure after this one in the comp.
    public Measure getNext() {
        return composition.getMeasure(composition.getListOfMeasure().indexOf(this) + 2);
    }

    // MODIFIES: this
    // EFFECTS: assigns this to given composition
    public void assignToComposition(Composition composition) {
        this.composition = composition;
    }

    public Composition getComposition() {
        return composition;
    }

    // EFFECTS: draws all notes in this measure.
    public void draw(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.drawString(Integer.toString(measureNumber), getGlobalStartTick() * CompositionPanel.tickWidth, 30);
        for (Note note : listOfNote) {
            note.draw(graphics);
        }
    }

    // this method may be obsolete
    // REQUIRES: beat is within length of measure, pitch within pitch range of composition
    // MODIFIES: this
    // EFFECTS: creates a new note, adding it to this measure at the given beat with value and pitch
    public void addNewNote(int beat, int value, int pitch) {
        Note newNote = new Note(beat, value, pitch);
        listOfNote.add(newNote);

    }


    // MODIFIES: this, note
    // EFFECTS: adds the note passed to this measure and makes sure this is assigned to note.
    public void addNote(Note note) {
        if (!listOfNote.contains(note)) {
            listOfNote.add(note);
            note.assignToMeasure(this);
        }
    }

    // MODIFIES: this, note
    // EFFECTS: removes the note passed from this measure and makes sure this is unassigned from note.
    public void removeNote(Note note) {
        if (listOfNote.contains(note)) {
            listOfNote.remove(note);
            note.unassignFromMeasure();
        }
    }

    // EFFECTS: returns the number of beats in this measure
    public int getBeatNumber() {
        return beatNumber;
    }

    // EFFECTS: returns the number of ticks in this measure
    public int getNumTicks() {

        return Composition.resolution / beatType * 4 * beatNumber;
    }

    // EFFECTS: returns the type of beats in this measure
    public int getBeatType() {
        return beatType;
    }

    // EFFECTS: returns the note at the given global beat and pitch in the measure. If none, return null.
    public Note getNote(int beat, int pitch) {
        for (Note note : listOfNote) {
            if (beat == note.getGlobalStart() && pitch == note.getPitch()) {
                return note;
            }
        }
        return null;
    }

    // EFFECTS: returns string contents of the measure
    public String getContents() {
        String tempString = "";
        for (Note n : listOfNote) {
            tempString = tempString + n.getGlobalStart() + " " + n.getValue() + " " + n.getPitch() + "\n";
        }
        return tempString;
    }



    public List<Note> getListOfNote() {
        return listOfNote;
    }



}
