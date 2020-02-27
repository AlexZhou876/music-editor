package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {
    Note note;
    Composition composition;

    @BeforeEach
    public void runBefore() {
        composition = new Composition(2, 4, 4);
        note = new Note(1, 1, 1);
        composition.getMeasure(1).addNote(note); //probably in note constructor???
    }

    @Test
    public void test3ParamConstructor() {
        assertEquals(1, note.getValue());
        assertEquals(1, note.getStart());
        assertEquals(1, note.getPitch());
    }

    /*
    @Test
    public void test4ParamConstructor() {
        // setup: remove the note object instantiated in runBefore?
        note = new Note(composition.getMeasure(2),1,1,20);
        // check
        // problem w/ assertEquals(1, note.getValue()); is that note contains reference to a note instance without
        // instantiated fields, a separate instance from the one that gets added to the measure.
        assertEquals(1, note.getValue());
        assertEquals(5, note.getStart());
        assertEquals(1, note.getPitch());
    }
*/
    @Test
    public void testResizeNote() {
        note.resizeNote(4);
        assertEquals(4, note.getValue());
        assertEquals(4, composition.getMeasure(1).getNote(1, 1).getValue());
    }

    @Test
    public void testMoveTime1Param() {
        note.moveTime(3);
        assertEquals(3, note.getStart());
        assertEquals(1, composition.getMeasure(1).getNote(3, 1).getValue());
    }

    @Test
    public void testMoveTime2Param() {
        note.moveTime(composition.getMeasure(2), 3);
        assertEquals(3, note.getStart());
        assertEquals(1, composition.getMeasure(2).getNote(3, 1).getValue());
    }

    @Test
    public void testMovePitch() {
        note.movePitch(70);
        assertEquals(70, note.getPitch());
    }

    @Test
    public void testGetNoteReturnsNull() {
        Note note = composition.getMeasure(1).getNote(2, 2);
        assertNull(note);
    }


}
