package ui.players;

import model.Composition;
import model.Note;
import ui.CompositionPanel;
import ui.tools.PlayEntireTool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

// this is a subject (because scrolling follows the progress of the playing line)
public class EntirePlayer implements ActionListener {

    private CompositionPanel compositionPanel;
    private Composition composition;
    private PlayEntireTool tool;
    private Timer timer;
    private int playingTick;
    private int counter;

    private List<Note> lastTickPlayed;
    private List<Note> notesAtTick;
    public static final int BUFFER = 1;
    private boolean playing;

    public EntirePlayer(Composition composition, Timer timer, PlayEntireTool tool) {
        //this.compositionPanel = compositionPanel;
        this.composition = composition;
        this.tool = tool;
        this.timer = timer;
        playingTick = 1;
        counter = 0;
        lastTickPlayed = new ArrayList<Note>();
        notesAtTick = new ArrayList<Note>();
        playing = false;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setTool(PlayEntireTool tool) {
        this.tool = tool;
    }



    public void setPlayingTick(int playingTick) {
        this.playingTick = playingTick;
        //compositionPanel.setPlayLineColumn(playingTick);
    }

    // MODIFIES: this
    // EFFECTS:  plays notes in current column, repaints, increments column,  stops if done
    //           this class is the listener for the timer object, and this method is what the timer calls
    //           each time through its loop.
    @Override
    public void actionPerformed(ActionEvent e) {
       // if (counter % CompositionPanel.beatWidth == 0) {
        selectAndPlayNotes();
        incrementTick();
            //drawRedLine();
            //scrollFollowingColumn();
        //correctProgressLine();
        stopPlayingWhenDone();
        //}
        //counter++;
    }

    // MODIFIES: CompositionPanel
    // EFFECTS: sets the x coordinate of the displayed playing progress line to ensure it is in sync with the audio.
    public void correctProgressLine() {
        int playLineColumn = playingTick * CompositionPanel.beatWidth;
        compositionPanel.setPlayLineColumn(playLineColumn);
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    /*

    // MODIFIES: this
    // EFFECTS: sets the scroll location of scroller to follow the playing line.
    private void scrollFollowingColumn() {
        //compositionPanel.followPlaying();
    }

     */

    // MODIFIES: this
    // EFFECTS: selects and plays notes at current tick
    private void selectAndPlayNotes() {
        /*
        notesInColumn = compositionPanel.getComposition().getNotesAtColumn(playingTick);
        for (Note note : lastColumnPlayed) {
            if (!notesInColumn.contains(note)) {
                note.unselectAndStopPlaying();
            }
        }
        for (Note note : notesInColumn) {
            if (!lastColumnPlayed.contains(note)) {
                note.selectAndPlay();
            }
        }

         */
        notesAtTick = composition.getNotesAtTick(playingTick);
        for (Note note : lastTickPlayed) {
            if (!notesAtTick.contains(note)) {
                note.unselectAndStopPlaying();
            }
        }
        for (Note note : notesAtTick) {
            if (!lastTickPlayed.contains(note)) {
                note.selectAndPlay();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: unselects and stops playing all notes at current column
    private void unselectAndStopPlayingAll() {
        for (Note note: notesAtTick) {
            note.unselectAndStopPlaying();
        }
    }
/*
    // MODIFIES: this
    // EFFECTS:  moves playback line to playingColumn to trigger sound and repaint
    private void drawRedLine() {

        //compositionPanel.repaint(); // the Java Graphics framework will call paintComponent
    }

 */

    // MODIFIES: this
    // EFFECTS:  moves current x-column to next column; updates figures
    private void incrementTick() {
        playingTick += 1;
        lastTickPlayed = notesAtTick;
    }

    // MODIFIES: this
    // EFFECTS:  calls Timer.stop() when playingColumn is past the edge of the composition.
    private void stopPlayingWhenDone() {
        if (playingTick > composition.getNumTicks() + BUFFER) {
            tool.stop(); // calls stopPlaying
        }
    }

    // MODIFIES: this
    // EFFECTS: stops the timer to stop playing.
    public void stopPlaying() {
        timer.stop();
        unselectAndStopPlayingAll();
        playingTick = 1;



    }

}
