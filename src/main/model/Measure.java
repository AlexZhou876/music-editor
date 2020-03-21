package model;

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


    // REQUIRES: beatType is a power of 2
    // EFFECTS: instantiate an empty Measure with time signature beatNumber/beatType
    public Measure(int beatNumber, int beatType) {
        listOfNote = new ArrayList<Note>();
        if (beatNumber % 3 == 0 && beatNumber != 3) {
            this.beatNumber = beatNumber / PULSES_PER_COMPOUND_BEAT;
        } else {
            this.beatNumber = beatNumber;
        }
        this.beatType = beatType;
    }

    // EFFECTS: draws all notes in this measure.
    public void draw(Graphics graphics) {
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


    // MODIFIES: this
    // EFFECTS: adds existing note to the measure //not at given beat because methods in Note class cover this.
    public void addNote(Note note) {
        if (!listOfNote.contains(note)) {
            listOfNote.add(note);
            note.assignToMeasure(this);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes the note passed
    // this remove method has no partner in Note because there cannot be
    // a note with no measure. This method is for note deletion, after which the note obj
    // should become inactive.
    public void removeNote(Note note) {
        if (listOfNote.contains(note)) {
            listOfNote.remove(note);
        }
    }

    // EFFECTS: returns the number of beats in this measure
    public int getNumBeats() {
        return beatNumber;
    }

    // EFFECTS: returns the type of beats in this measure
    public int getBeatType() {
        return beatType;
    }

    // EFFECTS: returns the note at the given beat and pitch in the measure. If none, return null.
    public Note getNote(int beat, int pitch) {
        for (Note note : listOfNote) {
            if (beat == note.getStart() && pitch == note.getPitch()) {
                return note;
            }
        }
        return null;
    }

    // EFFECTS: returns string contents of the measure
    public String getContents() {
        String tempString = "";
        for (Note n : listOfNote) {
            tempString = tempString + n.getStart() + " " + n.getValue() + " " + n.getPitch() + "\n";
        }
        return tempString;
    }



    public List<Note> getListOfNote() {
        return listOfNote;
    }


}
