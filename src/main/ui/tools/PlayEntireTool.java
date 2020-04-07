package ui.tools;

import ui.GraphicalEditorApp;
import ui.players.EntirePlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// this is an observer class. It observes EntirePlayer. Consider observer pattern
public class PlayEntireTool extends Tool {
    private boolean playing;
    private EntirePlayer player;

    public PlayEntireTool(GraphicalEditorApp editor, JComponent parent, EntirePlayer player) {
        super(editor, parent);
        //player = new EntirePlayer(editor.getCompositionPanel(), null, this);
        this.player = player;
        playing = false;
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
        final Timer timer = new Timer(10, null);
        player.setTimer(timer);
        ActionListener a = player;
        timer.addActionListener(a);
        timer.setInitialDelay(0);
        timer.start();
    }

    // EFFECTS: Stop the playing and set playing to false.
    public void stop() {
        //playing = false;
        player.stopPlaying();
        player.setPlaying(false);
        button.setText("Play!");
    }

    private class PlayEntireToolClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (player.isPlaying()) {
                stop();
                return;
            }
            //playing = true;
            player.setPlaying(true);
            editor.setActiveTool(PlayEntireTool.this);
            button.setText("Stop");
            editor.repaint();
            play();
        }
    }
}
