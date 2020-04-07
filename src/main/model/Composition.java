package model;

import ui.CompositionPanel;
import ui.sound.MidiSynth;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// consider splitting graphical responsibilities into new class

// represents an entire composition, which is a collection of measures. The order of measures in the list is the order
// of measures in the composition.
public class Composition  { // used to extend JPanel
    private List<Measure> listOfMeasure;
    private int beatsPerMinute;
    private int beatNum;
    private int beatType;
    private int barWidth; // implement later

    //private int playLineColumn; graphics responsibilities
    public static int BEAT_WIDTH = CompositionPanel.BEAT_WIDTH;
    public static int SEMITONE_HEIGHT = CompositionPanel.SEMITONE_HEIGHT;

    // REQUIRES: beatType is a power of 2
    // EFFECTS: instantiates a new composition with numMeasures measures, beatNum beats of type beatType per measure.
    public Composition(int numMeasures, int beatNum, int beatType) {
        //super();
        //setBackground(Color.black);
        this.beatNum = beatNum;
        this.beatType = beatType;
        listOfMeasure = new ArrayList<Measure>();
        for (int i = 0; i < numMeasures; i++) {
            Measure tempMeasure = new Measure(beatNum, beatType, this, i + 1);
            listOfMeasure.add(tempMeasure);
        }
    }

    // EFFECTS: gets the end of the composition in screen x coordinate.
    public int getEnd() {
        int totalBeats = this.getNumBeats();
        return totalBeats * BEAT_WIDTH;
    }

    // EFFECTS: adds a midisynth to all notes.
    public void addMidiSynthToAll(MidiSynth midiSynth) {
        for (Measure measure : listOfMeasure) {
            measure.addMidiSynthToAll(midiSynth);
        }
    }

    /*
    // EFFECTS: gets a list of all notes in the composition.
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        for (Measure measure : listOfMeasure) {
            measure.getListOfNote();
            List<String> newList = Stream.concat(listOne.stream(), listTwo.stream())
                    .collect(Collectors.toList());
        }
    }



    public void setPlayLineColumn(int target) {
        playLineColumn = target;
    }
    */


    // EFFECTS: returns the global start beat of the given measure.
    public int getGlobalStartOf(Measure measure) {
        int output = 0;
        for (Measure m: listOfMeasure) {
            if (!m.equals(measure)) {
                output = output + m.getNumBeats();
            } else {
                output = output + 1;
                return output;
            }
        }
        return 0; //this should throw an exception probably
    }

    // EFFECTS: returns the note at a given point in composition, if any.
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

    // EFFECTS: returns list of notes at a given column in the composition.
    public List<Note> getNotesAtColumn(int x) {
        List<Note> notesAtColumn = new ArrayList<Note>();
        for (Measure m: listOfMeasure) {
            for (Note note : m.getListOfNote()) {
                if (note.containsX(x)) {
                    notesAtColumn.add(note);
                }
            }
        }
        return notesAtColumn;
    }
/* graphics responsibilities extracted
    // EFFECTS: paints grid, playing line, notes in composition.
    // calls to repaint() get here
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawLines(graphics);
        // it may be better for measure.draw to draw bar lines.
        for (Measure measure : listOfMeasure) {
            measure.draw(graphics);
        }
    }


    // EFFECTS: draws semitone lines and bar lines
    private void drawLines(Graphics graphics) {
        int endX = 0;
        Color save = graphics.getColor();
        graphics.setColor(new Color(100, 100, 200));
        for (int measureNum = 0; measureNum < listOfMeasure.size(); measureNum++) {
            int x = BAR_WIDTH * (measureNum + 1);
            graphics.drawLine(x, 0, x, getHeight());
            endX = x;
        }
        for (int y = SEMITONE_HEIGHT; y < getHeight(); y += SEMITONE_HEIGHT) {
            graphics.drawLine(0, y, endX, y);
        }
        if (playLineColumn > 0 && playLineColumn < getWidth()) {
            graphics.setColor(Color.RED);
            graphics.drawLine(playLineColumn, 0, playLineColumn, getHeight());
        }
        graphics.setColor(save);
    }

 */

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

    // REQUIRES: there is a measure at every position specified.
    // MODIFIES: this
    // EFFECTS: removes the specified measures from composition.
    public void removeMeasures(List<Integer> listOfPos) {
        // reverse list to remove last first
        Collections.reverse(listOfPos);
        for (Integer pos : listOfPos) {
            listOfMeasure.remove(pos - 1);
        }
        countAndNotifyMeasures();
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
            numBeats = numBeats + measure.getNumBeats();
        }
        return numBeats;
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

    public int getBeatNum() {
        return beatNum;
    }

    public int getBeatType() {
        return beatType;
    }

    public List<Measure> getListOfMeasure() {
        return listOfMeasure;
    }


}
