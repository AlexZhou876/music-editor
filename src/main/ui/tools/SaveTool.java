package ui.tools;

import persistence.Writer;
import ui.GraphicalEditorApp;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static ui.GraphicalEditorApp.*;

public class SaveTool extends Tool {
    private String path;

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


    private class SaveToolClickHandler implements ActionListener {

        // EFFECTS: saves the current state of the composition to file and shows a dialog to the user.
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = JOptionPane.showInputDialog(editor,
                    "Specify the path.", SAVE_FILE);
            if (input == null) {
                return;
            }
            try {
                path = input;
                Writer writer = new Writer();
                writer.writeFile(editor.getCompositionPanel().getComposition(), path);
                JOptionPane.showMessageDialog(editor, "Success!");
            } catch (InvalidMidiDataException imde) {
                JOptionPane.showMessageDialog(editor, "Invalid Midi Data: " + imde.getMessage());
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(editor, "IO Error: " + ioe.getMessage());
            }
        }
    }
}
