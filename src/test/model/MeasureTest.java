package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasureTest {
    Measure measure;
    Note note;

    @BeforeEach
    public void runBefore() {
        measure = new Measure(4, 4);
        note = new Note(1, 1, 1);
        // this note should not be associated with the composition yet.
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
    }
}
