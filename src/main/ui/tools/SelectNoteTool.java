package ui.tools;

import ui.GraphicalEditorApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class SelectNoteTool extends Tool {
    private Point boxInit;
    private Point cursorLoc;

    public SelectNoteTool(GraphicalEditorApp editor, JComponent parent) {
        super(editor, parent);
        cursorLoc = null;
        boxInit = null;
    }

    public Point getBoxInit() {
        return boxInit;
    }

    public Point getCursorLoc() {
        return cursorLoc;
    }

    @Override
    public void render(Graphics graphics) {
        if (boxInit != null) {
            graphics.drawRect(boxInit.x, boxInit.y,
                    cursorLoc.x - boxInit.x,
                    cursorLoc.y - boxInit.y);
        }
    }


    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Select Notes");
        button = customizeButton(button);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        boxInit = e.getPoint();
        cursorLoc = e.getPoint();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        cursorLoc = e.getPoint();

    }

    // MODIFIES: note (this)
    // EFFECTS: unselects note and causes this to forget the note.
    @Override
    public void mouseReleased(MouseEvent e) {
        cursorLoc = null;
        boxInit = null;
    }

    @Override
    protected void addListener() {
        button.addActionListener(new SelectNoteToolClickHandler());
    }

    private class SelectNoteToolClickHandler implements ActionListener {
        // EFFECTS: sets active tool to the this tool
        //          called by the framework when the tool is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            editor.setActiveTool(SelectNoteTool.this);
            //compositionPanel.requestFocus();
        }
    }
}
