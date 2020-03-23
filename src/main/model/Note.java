package model;

import ui.sound.MidiSynth;

import java.awt.*;
import static model.Composition.*;

// class representing a music note, with starting time with respect to beats in the measure, note value
// with respect to beats in the measure, and pitch [1,88]
public class Note {
    public static final int PLACEHOLDER_VOLUME = 100;
    public static final int PLACEHOLDER_INSTRUMENT = 1;
    public static final int MIDI_A0_VALUE = 21;
    public static final Color PLAYING = new Color(230, 158, 60);
    private Measure measure; //bidirectional association
    private int start;
    private int value;
    private int pitch;
    private int globalStart;
    private boolean selected;
    private MidiSynth midiSynth;
    //private Dynamic dynamic;

    // this constructor is obsolete
    // REQUIRES: start is a time value within the measure
    // EFFECTS: constructs a Note given a start time (beat), value, and pitch.
    public Note(int start, int value, int pitch) {
        this.start = start;
        this.value = value;
        this.pitch = pitch;
    }

    // EFFECTS: constructs a Note in a given measure, global start time, value, and pitch
    public Note(Measure measure, int start, int value, int pitch, MidiSynth midiSynth) {
        this.globalStart = start;
        this.value = value;
        this.pitch = pitch;
        selected = false;
        this.midiSynth = midiSynth;
        this.measure = measure;
        measure.addNote(this);
    }

    // EFFECTS: returns true if the note contains the given point, false otherwise.
    // !!! code duplication with draw
    public boolean contains(Point point) {
        int pointX = point.x;
        int pointY = point.y;
        int leftEdge = modelTimeToXCoord(globalStart);
        int topEdge = (88 - pitch) * SEMITONE_HEIGHT;
        int bottomEdge = pitchToYCoord(pitch);
        int rightEdge = modelTimeToXCoord(globalStart + value);
        return pointX >= leftEdge && pointX <= rightEdge && pointY >= topEdge && pointY <= bottomEdge;
    }

    // EFFECTS: returns true if the x coordinate is within bounds of note; false otherwise
    public boolean containsX(int x) {
        return x >= modelTimeToXCoord(globalStart) && x <= modelTimeToXCoord(globalStart + value);
    }

    // MODIFIES: this
    // EFFECTS:  If the point dragged to indicates a different location for the note selected than where it already is,
    //           move the note to that location. Manage the playing of the note during the transition.
    public void move(Point draggedTo) {
        boolean noteChanges;
        /*
        noteChanges = Math.abs(draggedTo.getY() - pitchToYCoord(pitch)) > SEMITONE_HEIGHT
                || Math.abs(draggedTo.getX() - modelTimeToXCoord(globalStart)) > BEAT_WIDTH;

         */
        int newGlobalStart = 1 + ((int) draggedTo.getX()) / BEAT_WIDTH;
        int newPitch = 88 - ((int) draggedTo.getY()) / SEMITONE_HEIGHT;
        noteChanges = newGlobalStart != globalStart || newPitch != pitch;
        if (noteChanges && inBounds(newGlobalStart, newPitch)) {
            changeAssignedMeasure(newGlobalStart);
            stopPlaying();
            globalStart = newGlobalStart;
            pitch = newPitch;
            play();
        }
    }

    // MODIFIES: this
    // EFFECTS: if the note has been moved to a different measure, assign this note to that measure.
    private void changeAssignedMeasure(int newGlobalStart) {
        int leftEndPoint = measure.getGlobalStart();
        int rightEndPoint = leftEndPoint + measure.getNumBeats() - 1;
        if (newGlobalStart > rightEndPoint) {
            assignToMeasure(measure.getNext());
        } else if (newGlobalStart < leftEndPoint) {
            assignToMeasure(measure.getLast());
        }
    }


    // EFFECTS: returns true if the starting time and pitch given are in the composition.
    private boolean inBounds(int start, int pitch) {
        int totalBeats = measure.getComposition().getNumBeats();
        return start <= totalBeats && start >= 1 && pitch <= 88 && pitch >= 1;
    }

    // EFFECTS: converts model pitch values to y screen coordinate
    private int pitchToYCoord(int pitch) {
        return (88 - pitch) * SEMITONE_HEIGHT + SEMITONE_HEIGHT;
    }

    // EFFECTS: converts model beat values to x screen coordinate
    private int modelTimeToXCoord(int beat) {
        return (beat - 1) * BEAT_WIDTH;
    }

    // EFFECTS: draws this note on the composition, if selected, note is filled in,
    // otherwise it is white.
    public void draw(Graphics graphics) {
        int x = (globalStart - 1) * BEAT_WIDTH;
        int y = (88 - pitch) * SEMITONE_HEIGHT;
        int height = SEMITONE_HEIGHT;
        int width = value * BEAT_WIDTH;
        Color save = graphics.getColor();
        if (selected) {
            graphics.setColor(PLAYING);
        } else {
            graphics.setColor(Color.white);
        }
        graphics.fillRect(x, y, width, height);
        graphics.setColor(save);
        graphics.drawRect(x, y, width, height);
    }

    // MODIFIES: this
    // EFFECTS: assigns given measure to this note
    public void assignToMeasure(Measure measure) {
        if (!this.measure.equals(measure)) {
            this.measure = measure;
        }
    }

    // MODIFIES: this
    // EFFECTS:  selects this Note, plays associated sound
    // from SimpleDrawingPlayer (modified)
    public void selectAndPlay() {
        if (!selected) {
            selected = true;
            play();
        }
    }

    // MODIFIES: this
    // EFFECTS:  unselects this Note, stops playing associated sound
    // from SimpleDrawingPlayer (modified)
    public void unselectAndStopPlaying() {
        if (selected) {
            selected = false;
            stopPlaying();
        }
    }

    // EFFECTS: starts playing the shape, sound depends on coordinates
    private void play() {
        midiSynth.play(PLACEHOLDER_INSTRUMENT, modelPitchToMidiPitch(pitch), PLACEHOLDER_VOLUME);
    }

    // EFFECTS: stops playing this note
    private void stopPlaying() {
        midiSynth.stop(PLACEHOLDER_INSTRUMENT, modelPitchToMidiPitch(pitch));
    }

    // EFFECTS: convert model pitch to midi pitch.
    private int modelPitchToMidiPitch(int pitch) {
        return MIDI_A0_VALUE + pitch;
    }

    // REQUIRES: x coordinate of point > x coordinate of the note (remove)
    // MODIFIES: this
    // EFFECTS: sets the right edge of note to the indicated value
    public void setBounds(double x) {
        value = ((int) (x - globalStart * BEAT_WIDTH)) / BEAT_WIDTH + 1;
    }

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

    public int getGlobalStart() {
        return globalStart;
    }

    // EFFECTS: returns the pitch of the note.
    public int getPitch() {
        return pitch;
    }

    public Measure getMeasure() {
        return measure;
    }


}
