package ui.tools;

import persistence.Writer;
import ui.GraphicalEditorApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static ui.GraphicalEditorApp.*;

public class SaveTool extends Tool {

    public SaveTool(GraphicalEditorApp editor, JComponent parent) {
        super(editor, parent);
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Save");
        button = customizeButton(button);
    }

    @Override
    protected void addListener() {
        button.addActionListener(new SaveTool.SaveToolClickHandler());
    }

    // EFFECTS: saves the composition to file.
    private void saveComposition() {
        Writer writer = new Writer();
        writer.writeFile(editor.getCompositionPanel().getComposition(), SAVE_FILE);
    }


    private class SaveToolClickHandler implements ActionListener {

        // EFFECTS: saves the current state of the composition to file and shows a dialog to the user.
        @Override
        public void actionPerformed(ActionEvent e) {
            saveComposition();
            JOptionPane.showMessageDialog(editor, "Success!");

        }
    }
}
