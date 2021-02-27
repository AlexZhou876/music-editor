package ui.tools;

import exceptions.InvalidTargetValue;
import model.*;
import ui.GraphicalEditorApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static ui.CompositionPanel.SEMITONE_HEIGHT;
import static ui.CompositionPanel.tickWidth;


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
    }

    // MODIFIES: this
    // EFFECTS: sets bounds of this note to where mouse is dragged to
    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getPoint().getX() < editor.getCompositionPanel().getEnd()) {
            try {
                Point modelP = compositionPanel.graphicsPointToModelPoint(e.getPoint());
                note.setBounds((int)modelP.getX());
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
        Point p = compositionPanel.graphicsPointToModelPoint(e.getPoint());
        note = composition.addNoteAtPoint(p, 1);






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
