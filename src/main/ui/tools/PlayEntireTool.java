package ui.tools;

import ui.GraphicalEditorApp;
import ui.players.EntirePlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayEntireTool extends Tool {

    public PlayEntireTool(GraphicalEditorApp editor, JComponent parent) {
        super(editor, parent);
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Play!");
        button = customizeButton(button);
    }

    @Override
    protected void addListener() {
        button.addActionListener(new PlayEntireToolClickHandler());

    }

    // EFFECTS: plays the entire composition from the beginning.
    private void play() {
        final Timer t = new Timer(10, null);
        ActionListener a = new EntirePlayer(editor.getCompositionPanel(), t);
        t.addActionListener(a);
        t.setInitialDelay(0);
        t.start();

    }

    private class PlayEntireToolClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            editor.setActiveTool(PlayEntireTool.this);
            editor.repaint();
            play();
        }
    }
}
