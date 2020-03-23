package ui.tools;

import model.*;
import static model.Composition.*;
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
        note.setBounds(e.getPoint().getX());
       // editor.getComposition().addMeasure(measure);
    }

    // MODIFIES: this
    // EFFECTS: sets bounds of this note to where mouse is dragged to
    @Override
    public void mouseDragged(MouseEvent e) {
        note.setBounds(e.getPoint().getX());
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
        int measureNum = 1 + ((int) e.getPoint().getX()) / BAR_WIDTH;
        int globalStart = 1 + ((int) e.getPoint().getX()) / BEAT_WIDTH;
        measure = editor.getComposition().getMeasure(measureNum);
        note = new Note(measure, globalStart, 0, pitch, editor.getMidiSynth());
        // maybe value should be 1
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
