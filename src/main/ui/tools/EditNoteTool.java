package ui.tools;

import model.Note;
import static model.Composition.*;
import ui.GraphicalEditorApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

// Tool which allows user to move, extend, and delete notes.
public class EditNoteTool extends Tool {
    private Note note;
    private Point startingPoint;

    public EditNoteTool(GraphicalEditorApp editor, JComponent parent) {
        super(editor, parent);
        editor.getComposition().getInputMap()
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0), "incrementValue");
        editor.getComposition().getActionMap().put("incrementValue", new IncrementValue());
        editor.getComposition().getInputMap().put(KeyStroke.getKeyStroke("VK_LEFT"), "decrementValue");
        editor.getComposition().getActionMap().put("decrementValue", new DecrementValue());
        editor.getComposition().getInputMap().put(KeyStroke.getKeyStroke("VK_DELETE"), "delete");
        editor.getComposition().getActionMap().put("delete", new Delete());
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Edit Note");
        button = customizeButton(button);
    }

    @Override
    protected void addListener() {
        button.addActionListener(new EditNoteTool.EditNoteToolClickHandler());
    }

    // MODIFIES: note (this)
    // EFFECTS: if the click is on a note, the note is selected and the click location is remembered.
    // Otherwise do nothing.
    @Override
    public void mousePressed(MouseEvent e) {
        Note newNote = editor.getComposition().getNoteAtPoint(e.getPoint());
        /*
        note = editor.getComposition().getNoteAtPoint(e.getPoint());
        if (note != null) {
            this.note.selectAndPlay();
            startingPoint = e.getPoint();
        }

         */
        if (newNote != null) {
            if (note != null) {
                note.unselectAndStopPlaying();
            }
            note = newNote;
            note.selectAndPlay();
            startingPoint = e.getPoint();
        }
    }

    // MODIFIES: note (this)
    // EFFECTS: The note is moved to the semitone where the mouse is dragged, and the note's start is changed to the
    // beat where the mouse is dragged.
    @Override
    public void mouseDragged(MouseEvent e) {
        if (note != null) {
            note.move(e.getPoint());
        }
    }

    // MODIFIES: note (this)
    // EFFECTS: unselects note and causes this to forget the note.
    @Override
    public void mouseReleased(MouseEvent e) {

        /*
        if (note != null) {
            note.unselectAndStopPlaying();
            note = null;
        }

         */
    }

    // EFFECTS: unselects the note; called when tool switches.
    @Override
    public void deactivate() {
        if (note != null) {
            note.unselectAndStopPlaying();
            note = null;
        }
        super.deactivate();
    }
/*
    // MODIFIES: note (this)
    // EFFECTS: Right arrow presses increment the value of the note, and left arrow presses decrement.
    // Delete key deletes the note.
    @Override
    public void keyTyped(KeyEvent ke) {
        int currentWidth = note.getGlobalStart() + note.getValue();
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            note.setBounds(currentWidth + BEAT_WIDTH);
        } else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
            note.setBounds(currentWidth - BEAT_WIDTH);
        } else if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
            note.getMeasure().removeNote(note);
            note.unselectAndStopPlaying();
        }
    }
*/

    // for the tool button
    private class EditNoteToolClickHandler implements ActionListener {
        // EFFECTS: sets active tool to the edit tool
        //          called by the framework when the tool is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            editor.setActiveTool(EditNoteTool.this);
        }
    }

    // extends note value when right arrow pressed
    private class IncrementValue extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (EditNoteTool.this.isActive()) {
                int currentWidth = note.getGlobalStart() + note.getValue();
                note.setBounds(currentWidth + BEAT_WIDTH);
                editor.repaint();
            }
        }
    }

    // decrease note value when left arrow pressed
    private class DecrementValue extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (EditNoteTool.this.isActive()) {
                int currentWidth = note.getGlobalStart() + note.getValue();
                if (currentWidth > 1) {
                    note.setBounds(currentWidth - BEAT_WIDTH);
                    editor.repaint();
                }
            }
        }
    }

    // delete note when delete pressed
    private class Delete extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (EditNoteTool.this.isActive()) {
                note.getMeasure().removeNote(note);
                note.unselectAndStopPlaying();
                editor.repaint();
            }
        }
    }
}
