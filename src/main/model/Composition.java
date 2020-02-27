package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// represents an entire composition, which is a collection of measures. The order of measures in the list is the order
// of measures in the composition.
public class Composition {
    private List<Measure> listOfMeasure;
    private int beatsPerMinute;
    private int beatNum;
    private int beatType;

    // REQUIRES: beatType is a power of 2
    // EFFECTS: instantiates a new composition with numMeasures measures, beatNum beats of type beatType per measure.
    public Composition(int numMeasures, int beatNum, int beatType) {
        this.beatNum = beatNum;
        this.beatType = beatType;
        listOfMeasure = new ArrayList<Measure>();
        for (int i = 0; i < numMeasures; i++) {
            Measure tempMeasure = new Measure(beatNum, beatType);
            listOfMeasure.add(tempMeasure);
        }
    }

    // REQUIRES: pos <= number of measures, beatType is a power of 2
    // MODIFIES: this
    // EFFECTS: adds n new measures to composition after the posth measure with beatNum beats of type beatType.
    public void addMeasures(int n, int pos, int beatNum, int beatType) {
        for (int i = 0; i < n; i++) {
            Measure tempMeasure = new Measure(beatNum, beatType);
            listOfMeasure.add(pos, tempMeasure);
        }
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

    // EFFECTS: return formatted content of composition !!! test
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
        listOfMeasure.add(measure);
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
