package ui.tools;

import ui.GraphicalEditorApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// a UI tool which allows the user to add measures. Different from the rest because it does not involve mouse action
// in the composition component.

public class AddMeasuresTool extends Tool {

    public AddMeasuresTool(GraphicalEditorApp editor, JComponent parent) {
        super(editor, parent);

    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Add Measures");
        button = customizeButton(button);
    }

    @Override
    protected void addListener() {
        button.addActionListener(new AddMeasuresTool.AddMeasuresToolClickHandler());
    }

    // MODIFIES: editor
    // EFFECTS: adds the specified number of measures to the composition
    private void addMeasures(int numMeasures) {
        int size = editor.getComposition().getNumMeasures();
        editor.getComposition().addMeasures(numMeasures, size, 4, 4);
    }

    private class AddMeasuresToolClickHandler implements ActionListener {
        // EFFECTS: creates and opens a dialog to prompt user for input
        //          called by the framework when the tool is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            editor.repaint();
            editor.setActiveTool(AddMeasuresTool.this);
            String input = JOptionPane.showInputDialog(editor,
                    "How many measures to add?", "1");
            try {
                int numMeasures = Integer.parseInt(input);
                addMeasures(numMeasures);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editor, "Please enter a positive integer.");
                ex.printStackTrace();
            }
            editor.repaint();
// parent should be editor or toolbar???
        }
    }
}
