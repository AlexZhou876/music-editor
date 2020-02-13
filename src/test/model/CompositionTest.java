package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(3, piece.getMeasure(4).getNumBeats());
    }

    @Test
    public void testAddOneMeasureInMiddle() {
        piece.addMeasures(1, 2, 3, 4);
        assertEquals(4, piece.getNumMeasures());
        assertEquals(15, piece.getNumBeats());
        // tests whether the added measure is in correct position
        assertEquals(3, piece.getMeasure(3).getNumBeats());
    }

    @Test
    public void testAddMultipleMeasures() {
        piece.addMeasures(3, 3, 3, 4);
        assertEquals(6, piece.getNumMeasures());
        assertEquals(21, piece.getNumBeats());
        // tests whether the added measures are in correct position
        assertEquals(3, piece.getMeasure(4).getNumBeats());
        assertEquals(3, piece.getMeasure(5).getNumBeats());
        assertEquals(3, piece.getMeasure(6).getNumBeats());
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


}