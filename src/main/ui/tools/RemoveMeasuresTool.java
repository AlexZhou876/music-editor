package ui.tools;

import ui.GraphicalEditorApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RemoveMeasuresTool extends Tool {

    public RemoveMeasuresTool(GraphicalEditorApp editor, JComponent parent) {
        super(editor, parent);
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Remove Measures");
        button = customizeButton(button);
    }

    @Override
    protected void addListener() {
        button.addActionListener(new RemoveMeasuresToolClickHandler());
    }

    // MODIFIES: editor
    // EFFECTS: removes the specified measure from the composition.
    private void removeMeasures(List<Integer> listOfPos) {
        editor.getCompositionPanel().getComposition().removeMeasures(listOfPos);
    }

    private class RemoveMeasuresToolClickHandler implements ActionListener {
        // EFFECTS: creates and opens a dialog to prompt user for input
        //          called by the framework when the tool is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            editor.repaint();
            editor.setActiveTool(RemoveMeasuresTool.this);
            List<Integer> listOfPos = new ArrayList<>();
            String input = JOptionPane.showInputDialog(editor,
                    "Enter, with spaces, the measure #s of the measures to remove.", null);
            if (input == null) {
                return;
            }
            String[] tempArray = input.split(" ");
            try {
                for (int i = 0; i < tempArray.length; i++) {
                    listOfPos.add(Integer.parseInt(tempArray[i]));
                }
                removeMeasures(listOfPos);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editor, "Please enter positive integers only.");
                ex.printStackTrace();
            } catch (IndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(editor, "Please enter existing measure numbers only.");
                ex.printStackTrace();
            }
            editor.repaint();
        }
    }
}
