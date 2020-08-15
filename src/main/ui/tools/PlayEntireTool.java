package ui.tools;

import ui.CompositionPanel;
import model.Composition;
import ui.GraphicalEditorApp;
import ui.players.EntirePlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// this is an observer class. It observes EntirePlayer. Consider observer pattern
public class PlayEntireTool extends Tool {
    private boolean playing;
    private EntirePlayer player;
    private Timer timer;
    public static final int MILLISECONDS_PER_MINUTE = 60000;

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


    // EFFECTS: take the BPM specified in CompositionPanel and return timer delay value in milliseconds.
    private int convertBPMtoGraphicsTimerDelay() {
        int bpm =  CompositionPanel.bpm;
        float beatsPerMs = ((float) bpm / (float) MILLISECONDS_PER_MINUTE);
        float ticksPerMs = beatsPerMs * (float) Composition.resolution;
        float screenCoordinatesPerMs = ticksPerMs * CompositionPanel.tickWidth; // beatWidth
        int timerDelay = Math.round(1 / screenCoordinatesPerMs);
        return timerDelay;
    }

    private int convertBPMtoPlayerTimerDelay() {
        int bpm =  CompositionPanel.bpm;
        float beatsPerMs = ((float) bpm / (float) MILLISECONDS_PER_MINUTE);
        float ticksPerMs = beatsPerMs * (float) Composition.resolution;
        int playerTimerDelay = Math.round(1 / ticksPerMs);
        return playerTimerDelay;

    }

    // EFFECTS: plays the entire composition from the beginning.
    private void play() {
        int masterTimerDelay = convertBPMtoGraphicsTimerDelay();
        int playerTimerDelay = convertBPMtoPlayerTimerDelay();
        final Timer newMasterTimer = new Timer(masterTimerDelay, null);
        final Timer playerTimer = new Timer(playerTimerDelay, null);

        player.setTimer(playerTimer);
        editor.setMasterTimer(newMasterTimer);

        playerTimer.addActionListener(player);
        //playerTimer.addActionListener(editor.getCompositionPanel());
        newMasterTimer.addActionListener(editor.getCompositionPanel());

        playerTimer.setInitialDelay(0);
        playerTimer.start();
        newMasterTimer.setInitialDelay(0);
        newMasterTimer.start();
    }

    // EFFECTS: Stop the playing and set playing to false.
    public void stop() {
        //playing = false;
        player.stopPlaying();
        editor.getCompositionPanel().stopPlaying();
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
