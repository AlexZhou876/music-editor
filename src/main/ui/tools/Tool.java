package ui.tools;

import model.Composition;
import ui.GraphicalEditorApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Tool {
    protected JButton button;
    protected GraphicalEditorApp editor;
    private boolean active;

    public Tool(GraphicalEditorApp editor, JComponent parent) {
        this.editor = editor;
        createButton(parent);
        addToParent(parent);
        active = false;
        addListener();
    }

    // EFFECTS: creates button to activate tool
    protected abstract void createButton(JComponent parent);

    // EFFECTS: adds button to parent component
    public void addToParent(JComponent parent) {
        parent.add(button);
    }

    protected abstract void addListener();

    // MODIFIES: this
    // EFFECTS:  customizes the button used for this tool
    protected JButton customizeButton(JButton button) {
        button.setBorderPainted(true);
        button.setFocusPainted(true);
        button.setContentAreaFilled(true);
        return button;
    }

    // MODIFIES: editor
    // EFFECTS: changes the size of the compositionPanel. If the new width is greater than the starting width, assign
    // the new width. Otherwise do nothing.
    protected void resizeCompositionPanel() {
        //int end = editor.getCompositionPanel().getEnd();
        //if (end > editor.WIDTH) {
           // editor.getCompositionPanel().setPreferredSize(new Dimension(end, editor.HEIGHT));
            //editor.getCompositionPanel().revalidate();
        //}
        editor.getCompositionPanel().resize();
    }

    // getters
    public boolean isActive() {
        return active;
    }

    // EFFECTS: sets this Tool's active field to true
    public void activate() {
        active = true;
    }

    // EFFECTS: sets this Tool's active field to false
    public void deactivate() {
        active = false;
    }

    // EFFECTS: nothing
    public void mouseDragged(MouseEvent e) { }

    // EFFECTS: nothing
    public void mouseClicked(MouseEvent e) {}

    // EFFECTS: nothing
    public void mouseReleased(MouseEvent e) { }

    // EFFECTS: nothing
    public void mousePressed(MouseEvent e) { }

    // EFFECTS: nothing
    public void keyTyped(KeyEvent ke) {}



}
