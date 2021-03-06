package ui.tools;

import exceptions.InvalidTargetValue;
import model.*;
import static ui.CompositionPanel.*;
import ui.GraphicalEditorApp;

import javax.swing.*;
import java.awt.event.*;


public class AddNoteTool extends Tool {
    private Note note;
    private Measure measure;

    public AddNoteTool(GraphicalEditorApp editor, JComponent parent) {
        super(editor, parent);

    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton(getLabel());
        button = customizeButton(button);

    }

    @Override
    protected void addListener() {
        button.addActionListener(new AddNoteToolClickHandler());
    }

    // EFFECTS: returns string for button label.
    private String getLabel() {
        return "Add Note";
    }

    // MODIFIES: this
    // EFFECTS: a note is instantiated, played, and added to editor's composition
    @Override
    public void mousePressed(MouseEvent e) {
        createNote(e);
        note.selectAndPlay();
        /*
        try {
            note.setBounds(e.getPoint().getX());
        } catch (InvalidTargetValue invalidTargetValue) {
            invalidTargetValue.printStackTrace();
        }
        // editor.getComposition().addMeasure(measure);

         */


    }

    // MODIFIES: this
    // EFFECTS: sets bounds of this note to where mouse is dragged to
    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getPoint().getX() < editor.getCompositionPanel().getEnd()) {
            try {
                note.setBounds(e.getPoint().getX());
            } catch (InvalidTargetValue invalidTargetValue) {
                invalidTargetValue.printStackTrace();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: unselects note and causes this to forget the note.
    @Override
    public void mouseReleased(MouseEvent e) {
        note.unselectAndStopPlaying();
        note = null;
    }

    // EFFECTS: instantiates the new note
    // pitch counts from bottom while y coordinate counts from top
    private void createNote(MouseEvent e) {
        int pitch =  88 - ((int) e.getPoint().getY()) / SEMITONE_HEIGHT;
        measure = getMeasureWithX((int) e.getPoint().getX());
        int globalStart = 1 + ((int) e.getPoint().getX()) / tickWidth;
        note = new Note(measure, globalStart, 1, pitch, editor.getMidiSynth());
    }

    /*
    // EFFECTS: takes the x screen coordinate of the click and returns the measure it falls into.
    // Warning: assumes beats have uniform number of ticks.
    private Measure getMeasureWithX(int x) {
        int beat = 1 + x / beatWidth;
        return editor.getCompositionPanel().getComposition().getMeasureAtBeat(beat);
    }

     */

    // EFFECTS: takes x screen coordinate of the click and returns the measure it falls into.
    private Measure getMeasureWithX(int x) {
        int tick = 1 + x / tickWidth;
        return editor.getCompositionPanel().getComposition().getMeasureAtTick(tick);
    }

    private class AddNoteToolClickHandler implements ActionListener {

        // from SimpleDrawingPlayer (modified)
        // EFFECTS: sets active tool to the shape tool
        //          called by the framework when the tool is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            editor.setActiveTool(AddNoteTool.this);
            editor.repaint();
        }
    }
}
