package model;

import exceptions.InvalidTargetValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {
    Note note;
    Note note2;
    Composition composition;
    Measure measure;

    @BeforeEach
    public void runBefore() {
        composition = new Composition(1, 4, 4);
        note = new Note(1, 1, 1);
        composition.getMeasure(1).addNote(note); //probably in note constructor???
        measure = new Measure(4,4);
        composition.addMeasure(measure);
        note2 = new Note(measure, 2, 2, 2, null);
    }

    @Test
    public void test3ParamConstructor() {
        assertEquals(1, note.getValue());
        assertEquals(1, note.getGlobalStart());
        assertEquals(1, note.getPitch());
    }

    @Test
    public void test5ParamConstructor() {
        assertEquals(2, note2.getValue());
        assertEquals(2, note2.getGlobalStart());
        assertEquals(2, note2.getPitch());
        assertEquals(measure, note2.getMeasure());
    }

    @Test
    public void testResizeNote() {
        try {
            note.resizeNote(4);
        } catch (InvalidTargetValue invalidTargetValue) {
            invalidTargetValue.printStackTrace();
        }
        assertEquals(4, note.getValue());
        assertEquals(4, composition.getMeasure(1).getNote(1, 1).getValue());
    }

    @Test
    public void testMoveTime1Param() {
        note.setGlobalStart(3);
        assertEquals(3, note.getGlobalStart());
        assertEquals(1, composition.getMeasure(1).getNote(3, 1).getValue());
    }
/*
    @Test
    public void testMoveTime2Param() {
        note.moveTime(composition.getMeasure(2), 3);
        assertEquals(3, note.getStart());
        assertEquals(1, composition.getMeasure(2).getNote(3, 1).getValue());
    }

 */

    @Test
    public void testMovePitch() {
        note.setPitch(70);
        assertEquals(70, note.getPitch());
    }

    @Test
    public void testGetNoteReturnsNull() {
        Note note = composition.getMeasure(1).getNote(2, 2);
        assertNull(note);
    }

    @Test
    public void testAssignToMeasure() {
        note.assignToMeasure(measure);
        // test that note and measure have correct bidirectional association
        assertEquals(measure, note.getMeasure());
        assertEquals(note, measure.getNote(1,1));
        // test that old measure no longer has note
        assertNull(composition.getMeasure(1).getNote(1,1));
    }

    @Test
    public void testUnassignFromMeasure() {
        note.unassignFromMeasure();
        assertNull(note.getMeasure());
        assertEquals(0, composition.getMeasure(1).getListOfNote().size());
    }








}
