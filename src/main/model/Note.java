package model;

// class representing a music note, with starting time with respect to beats in the measure, note value
// with respect to beats in the measure, and pitch [1,88]
public class Note {
    private int start;
    private int value;
    private int pitch;
    //private Dynamic dynamic;

    // REQUIRES: start is a time value within the measure
    // EFFECTS: constructs a Note given a start time (beat), value, and pitch.
    public Note(int start, int value, int pitch) {
        this.start = start;
        this.value = value;
        this.pitch = pitch;
    }

    // EFFECTS: constructs a Note in a given measure, start time, value, and pitch
    // This may be improper!!!
    /*
    public Note(Measure measure, int start, int value, int pitch) {
        measure.addNewNote(start, value, pitch);
    }
*/

    // REQUIRES: a valid value for target (>0 and fits within time range of composition)
    // MODIFIES: this
    // EFFECTS: value is set to target.
    public void resizeNote(int target) {
        value = target;
    }
    /*
    // REQUIRES: a valid value for target (within time range of composition)
    // MODIFIES: this
    // EFFECTS: the start time of the note is set to target while the end time remains fixed.
    public void resizeStart(int target) {

    }
    // REQUIRES: a valid value for target (within time range of composition)
    // MODIFIES: this
    // EFFECTS: the end time of the note is set to target while the start time remains fixed.
    public void resizeEnd(int target) {

    }
    */

    // REQUIRES: target is within the same measure
    // MODIFIES: this
    // EFFECTS: moves the start time of the note to beat #target of the same measure
    // !!! somehow handles overflow
    public void moveTime(int target) {
        start = target;
    }

    // REQUIRES: measure exists within the composition and target beat is within that measure
    // MODIFIES: this
    // EFFECTS: moves the start time of the note to beat #target of the given measure
    public void moveTime(Measure measure, int target) {
        measure.addNote(this);
        this.moveTime(target);
    }

    // REQUIRES: a valid value for target (within pitch range of composition [1, 88])
    // MODIFIES: this
    // EFFECTS: the pitch of the note is set to target.
    public void movePitch(int target) {
        pitch = target;
    }
/*
    // REQUIRES: a valid Dynamic
    // MODIFIES: this
    // EFFECTS: the dynamic of the note is set to target.
    public void changeDynamics(Dynamic target) {

    }
*/
    // EFFECTS: returns the value of the note.
    public int getValue() {
        return value;
    }

    // EFFECTS: returns the starting beat of the note in terms of the whole composition.
    public int getStart() {
        return start;
    }

    // EFFECTS: returns the pitch of the note.
    public int getPitch() {
        return pitch;
    }


}
