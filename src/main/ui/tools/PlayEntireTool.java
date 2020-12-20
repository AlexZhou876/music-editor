package ui.tools;



import ui.CompositionPanel;
import model.Composition;
import ui.GraphicalEditorApp;
import ui.players.EntirePlayer;
import persistence.ToSequence;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// observer pattern?
public class PlayEntireTool extends Tool implements MetaEventListener {
    private Sequencer sequencer;
    private Synthesizer synthesizer;
    private boolean playing;
    private EntirePlayer player;
    private Timer timer;
    public static final int MILLISECONDS_PER_MINUTE = 60000;
    public static final int GRAPHICS_TIMER_DELAY = 10;
    public static final int END_OF_TRACK = 0x2F;

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
        final Timer newMasterTimer = new Timer(GRAPHICS_TIMER_DELAY, null);

        editor.setMasterTimer(newMasterTimer);
        /*

        player.setTimer(playerTimer);
        editor.setMasterTimer(newMasterTimer);

        playerTimer.addActionListener(player);
        //playerTimer.addActionListener(editor.getCompositionPanel());
        newMasterTimer.addActionListener(editor.getCompositionPanel());

        playerTimer.setInitialDelay(0);
        playerTimer.start();
        newMasterTimer.setInitialDelay(0);
        newMasterTimer.start();
         */
        newMasterTimer.addActionListener(editor.getCompositionPanel());
        try {
            Sequence s = ToSequence.toSequence(editor.getCompositionPanel().getComposition());
            sequencer = MidiSystem.getSequencer();
            editor.getCompositionPanel().setSequencer(sequencer);
            sequencer.addMetaEventListener(this);
            sequencer.open();
            //synthesizer = (Synthesizer)sequencer;
            sequencer.setTempoInBPM(CompositionPanel.bpm);
            sequencer.setSequence(s);
            sequencer.start();

            newMasterTimer.setInitialDelay(0);
            newMasterTimer.start();

        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            e.printStackTrace();
        }
        // note: busy waiting for sequence end will not work with only one thread.

    }


    // EFFECTS: Stop the playing and set playing to false.
    public void stop() {
        //playing = false;
        /*
        player.stopPlaying();
        editor.getCompositionPanel().stopPlaying();
        player.setPlaying(false);
        button.setText("Play!");

         */
        sequencer.stop();
        editor.getCompositionPanel().stopPlaying();
        playing = false;
        button.setText("Play!");

    }

    @Override
    // event handler triggered by sequencer when MetaMessage encountered
    public void meta(MetaMessage meta) {
        if (meta.getType() == END_OF_TRACK) {
            stop();
        }
    }

    private class PlayEntireToolClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (playing) {
                stop();
                return;
            }
            playing = true;
            editor.setActiveTool(PlayEntireTool.this);
            button.setText("Stop");
            editor.repaint();
            play();
        }
    }

    private class FinishedPlayingHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            stop();
        }
    }
}
