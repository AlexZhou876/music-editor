package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.sound.MidiSynth;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CompositionTest {
    Composition piece;


    @BeforeEach
    public void runBefore() {
        piece = new Composition(3, 4, 4);
    }

    @Test
    public void testConstructor() {
        assertEquals(12, piece.getNumBeats());
        assertEquals(3, piece.getNumMeasures());

    }

    @Test
    public void testConstructorCompound() {
        piece = new Composition(2, 6, 8);
        assertEquals(4, piece.getNumBeats());
        assertEquals(2, piece.getNumMeasures());

    }

    @Test
    public void testAddOneMeasureToEnd() {
        piece.addMeasures(1, 3, 3, 4);
        assertEquals(4, piece.getNumMeasures());
        assertEquals(15, piece.getNumBeats());
        // tests whether the added measure is in correct position
        assertEquals(3, piece.getMeasure(4).getBeatNumber());
    }

    @Test
    public void testAddOneMeasureInMiddle() {
        piece.addMeasures(1, 2, 3, 4);
        assertEquals(4, piece.getNumMeasures());
        assertEquals(15, piece.getNumBeats());
        // tests whether the added measure is in correct position
        assertEquals(3, piece.getMeasure(3).getBeatNumber());
    }

    @Test
    public void testAddMultipleMeasures() {
        piece.addMeasures(3, 3, 3, 4);
        assertEquals(6, piece.getNumMeasures());
        assertEquals(21, piece.getNumBeats());
        // tests whether the added measures are in correct position
        assertEquals(3, piece.getMeasure(4).getBeatNumber());
        assertEquals(3, piece.getMeasure(5).getBeatNumber());
        assertEquals(3, piece.getMeasure(6).getBeatNumber());
    }

    @Test
    public void testRemoveOneMeasure() {
        // setup
        List<Integer> positions = new ArrayList<Integer>();
        positions.add(1);
        // call
        piece.removeMeasures(positions);
        // check
        assertEquals(2, piece.getNumMeasures());
    }

    @Test
    public void testRemoveMultipleMeasures() {
        // setup
        List<Integer> positions = new ArrayList<Integer>();
        positions.add(1);
        positions.add(2);
        // call
        piece.removeMeasures(positions);
        // check
        assertEquals(1, piece.getNumMeasures());
    }

    @Test
    public void testGetContents() {
        String output = piece.getContents();
        assertEquals("" + "\n" + "Measure" + 1 + "\n" + "\n" + "Measure" + 2 + "\n"
                + "\n" + "Measure" + 3 + "\n", output);

        piece.addMeasures(1, 3, 4, 4);
        output = piece.getContents();
        assertEquals("" + "\n" + "Measure" + 1 + "\n" + "\n" + "Measure" + 2 + "\n"
                + "\n" + "Measure" + 3 + "\n" + "\n" + "Measure" + 4 + "\n", output);

    }

    @Test
    public void testGetMeasureAtBeat() {
        assertEquals(piece.getMeasure(1), piece.getMeasureAtBeat(1));
        assertEquals(piece.getMeasure(2), piece.getMeasureAtBeat(5));
        assertEquals(piece.getMeasure(3), piece.getMeasureAtBeat(9));
        assertNull(piece.getMeasureAtBeat(13));
    }

    @Test
    public void testGetNotesAtTick() {
        MidiSynth ms = new MidiSynth();
       Measure m = piece.getMeasure(1);
       Note n = new Note(m, 16, 1, 50, ms);
       List<Note> expected = new ArrayList<Note>();
       expected.add(n);
       assertEquals(expected, piece.getNotesAtTick(16));
    }


}