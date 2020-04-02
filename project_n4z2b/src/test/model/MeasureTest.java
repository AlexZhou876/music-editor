package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasureTest {
    Measure measure;
    Note note;
    Composition composition;

    @BeforeEach
    public void runBefore() {
        measure = new Measure(4, 4);
        note = new Note(1, 1, 1);
        // this note should not be associated with the composition yet.
        composition = new Composition(0, 4, 4);
    }

    @Test
    public void testConstructor() {
        assertEquals(4, measure.getNumBeats());
        assertEquals(4, measure.getBeatType());
    }

    @Test
    public void testAddOneNewNote() {
        measure.addNewNote(1, 1, 30);
        assertEquals(1, measure.getNote(1, 30).getValue());
    }

    @Test
    public void testAddMultipleNewNotes() {
        measure.addNewNote(1, 1, 30);
        measure.addNewNote(2, 1, 40);
        assertEquals(1, measure.getNote(1, 30).getValue());
        assertEquals(1, measure.getNote(2, 40).getValue());
    }

    @Test
    public void testAddExistingNote() {
        measure.addNote(note);
        // check there is a note at beat 1
        assertNotEquals(null, measure.getNote(1, 1));
        // check note has right value
        assertEquals(1, measure.getNote(1, 1).getValue());
    }
    @Test
    public void testGetContentsEmpty() {
        assertTrue(measure.getContents().equals(""));
    }

    @Test
    public void testGetContentsNotEmpty() {
        measure.addNewNote(1,1,1);
        measure.addNewNote(1, 1, 2);
        assertEquals( "1 1 1\n1 1 2\n", measure.getContents());
    }

    @Test
    public void testRemoveNote() {
        measure.addNote(note);
        measure.removeNote(note);
        assertEquals("", measure.getContents());
        assertNull(note.getMeasure());
    }

    @Test
    public void testSetMeasureNumber() {
        measure.addNote(note);
        Note note2 = new Note(2, 1, 1);
        measure.addNote(note2);
        measure.setMeasureNumber(1);
        measure.setMeasureNumber(2);
        assertEquals(2, measure.getMeasureNumber());
        assertEquals(5, note.getGlobalStart());
        assertEquals(6, note2.getGlobalStart());
    }

    @Test
    public void testGetGlobalStart() {
        composition.addMeasure(measure);
        assertEquals(1, measure.getGlobalStart());
        composition.addMeasures(1, 0, 4, 4);
        assertEquals(5, measure.getGlobalStart());
    }

    @Test
    public void testGetLastGetNext() {
        composition.addMeasure(new Measure(3, 4));
        composition.addMeasure(measure);
        composition.addMeasure(new Measure(2, 2));
        assertEquals(3, measure.getLast().getNumBeats());
        assertEquals(2, measure.getNext().getNumBeats());
    }

    @Test
    public void testGetNote() {
        Note returnNote = measure.getNote(1,1);
        assertNull(returnNote);
        measure.addNote(note);
        returnNote = measure.getNote(1,1);
        assertEquals(note, returnNote);


    }

}
