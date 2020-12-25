package model;

import exceptions.InvalidTargetValue;
import ui.sound.MidiSynth;

import java.awt.*;

import static ui.CompositionPanel.*;

// class representing a music note, with starting time with respect to global ticks, note value
// with respect to global ticks, and pitch [1,88]
public class Note {
    public static final int PLACEHOLDER_VOLUME = 100;
    public static final int PLACEHOLDER_INSTRUMENT = 1; // midi program change values
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
    // EFFECTS: constructs a Note given a start time (tick), value, and pitch.
    public Note(int start, int value, int pitch) {
        this.globalStart = start;
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

    public void setMidiSynth(MidiSynth midiSynth) {
        this.midiSynth = midiSynth;
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

    // EFFECTS: returns true if start tick <= given tick < end tick, false otherwise.
    public boolean containsTick(int tick) {
        return globalStart <= tick && tick < (globalStart + value);
    }



    // MODIFIES: this
    // EFFECTS:  If the point dragged to indicates a different location for the note selected than where it already is,
    //           move the note to that location. Manage the playing of the note during the transition.
    // assume for now that notes lock to ticks rather than any larger beats or nudge widths.
    public void move(Point draggedTo) {
        boolean noteChanges;
        int newGlobalStart = (int)draggedTo.getX();
        int newGlobalEnd = (int)draggedTo.getX() + value;
        int newPitch = (int)draggedTo.getY();

        noteChanges = newGlobalStart != globalStart || newPitch != pitch;
        if (noteChanges && inBounds(newGlobalEnd, newPitch)) {
            changeAssignedMeasure(newGlobalStart);
            globalStart = newGlobalStart;
            if (newPitch != pitch) {
                stopPlaying();
                pitch = newPitch;
                play();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if the note has been moved to a different measure, assign this note to that measure.
    private void changeAssignedMeasure(int newGlobalStart) {
        int leftEndPoint = measure.getGlobalStartTick();
        int rightEndPoint = leftEndPoint + measure.getNumTicks();
        if (newGlobalStart > rightEndPoint) {
            assignToMeasure(measure.getNext());
        } else if (newGlobalStart < leftEndPoint) {
            assignToMeasure(measure.getLast());
        }
    }


    // EFFECTS: returns true if the end time and pitch given are in the composition.
    private boolean inBounds(int end, int pitch) {
        int totalTicks = measure.getComposition().getNumTicks();
        //int totalBeats = measure.getComposition().getNumBeats();
        return end - 1 <= totalTicks && end >= 1 && pitch <= 88 && pitch >= 1;
    }

    // EFFECTS: converts model pitch values to y screen coordinate
    private int pitchToYCoord(int pitch) {
        return (88 - pitch) * SEMITONE_HEIGHT + SEMITONE_HEIGHT;
    }



    private int modelTimeToXCoord(int tick) {
        return (tick - 1) * tickWidth;
    }



    // EFFECTS: draws this note on the composition, if selected, note is filled in,
    // otherwise it is white.
    public void draw(Graphics graphics) {
        int x = (globalStart - 1) * tickWidth;
        int y = (88 - pitch) * SEMITONE_HEIGHT;
        int height = SEMITONE_HEIGHT;
        int width = value * tickWidth;
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

    // MODIFIES: this, measure
    // EFFECTS: assigns given measure to this note and makes sure this is added to measure
    public void assignToMeasure(Measure measure) {
        if (this.measure == null) {
            this.measure = measure;
            this.measure.addNote(this);
        } else if (!this.measure.equals(measure)) {
            unassignFromMeasure();
            this.measure = measure;
            this.measure.addNote(this);
        }
    }

    // MODIFIES: this, measure
    // EFFECTS: unassigns this note from its measure and makes sure this is removed from measure
    public void unassignFromMeasure() {
        measure.removeNote(this);
        measure = null;
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


    // MODIFIES: this
    // EFFECTS: changes the value of this note depending on tick dragged to. If dragged left of start or right of
    // composition end, throw InvalidTargetValue.
    public void setBounds(int draggedToX) throws InvalidTargetValue {
        int globalEnd = 1 + draggedToX;
        if (globalEnd - 1 < globalStart || globalEnd - 1 > measure.getComposition().getNumTicks()) {
            throw new InvalidTargetValue();
        }
        value = globalEnd - globalStart;
    }


    // MODIFIES: this
    // EFFECTS: value is set to target in ticks. If target is invalid, throws InvalidTargetValue.
    public void resizeNote(int target) throws InvalidTargetValue {
        int globalEnd = globalStart + target;
        if (globalEnd - 1 > measure.getComposition().getNumTicks() || globalEnd <= globalStart) {
            throw new InvalidTargetValue();
        }
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

    // MODIFIES: this
    // EFFECTS: moves the start time of the note to beat #target
    public void setGlobalStart(int target) {
        globalStart = target;
    }

    /*
    // REQUIRES: measure exists within the composition and target beat is within that measure
    // MODIFIES: this
    // EFFECTS: moves the start time of the note to beat #target of the given measure
    public void moveTime(Measure measure, int target) {
        measure.addNote(this);
        this.moveTime(target);
    }

     */

    // REQUIRES: a valid value for target (within pitch range of composition [1, 88])
    // MODIFIES: this
    // EFFECTS: the pitch of the note is set to target.
    public void setPitch(int target) {
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
