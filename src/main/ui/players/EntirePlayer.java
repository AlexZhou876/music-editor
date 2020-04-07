package ui.players;

import model.Note;
import ui.CompositionPanel;
import ui.tools.PlayEntireTool;
import ui.tools.Tool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

// this is a subject (because scrolling follows the progress of the playing line)
public class EntirePlayer implements ActionListener {

    private CompositionPanel compositionPanel;
    private PlayEntireTool tool;
    private Timer timer;
    private int playingColumn;
    private List<Note> lastColumnPlayed;
    private List<Note> notesInColumn;
    public static final int BUFFER = 5;
    private boolean playing;

    public EntirePlayer(CompositionPanel compositionPanel, Timer timer, PlayEntireTool tool) {
        this.compositionPanel = compositionPanel;
        this.tool = tool;
        this.timer = timer;
        playingColumn = 0;
        lastColumnPlayed = new ArrayList<Note>();
        notesInColumn = new ArrayList<Note>();
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

    public void setPlayingColumn(int playingColumn) {
        this.playingColumn = playingColumn;
        compositionPanel.setPlayLineColumn(playingColumn);
    }

    // MODIFIES: this
    // EFFECTS:  plays notes in current column, repaints, increments column,  stops if done
    //           this class is the listener for the timer object, and this method is what the timer calls
    //           each time through its loop.
    @Override
    public void actionPerformed(ActionEvent e) {
        selectAndPlayNotes();
        incrementColumn();
        drawRedLine();
        scrollFollowingColumn();
        stopPlayingWhenDone();
    }

    // MODIFIES: this
    // EFFECTS: sets the scroll location of scroller to follow the playing line.
    private void scrollFollowingColumn() {
        compositionPanel.followPlaying();
    }

    // MODIFIES: this
    // EFFECTS: selects and plays notes at current column
    private void selectAndPlayNotes() {
        notesInColumn = compositionPanel.getComposition().getNotesAtColumn(playingColumn);
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
    }

    // MODIFIES: this
    // EFFECTS: unselects and stops playing all notes at current column
    private void unselectAndStopPlayingAll() {
        for (Note note: notesInColumn) {
            note.unselectAndStopPlaying();
        }
    }

    // MODIFIES: this
    // EFFECTS:  moves playback line to playingColumn to trigger sound and repaint
    private void drawRedLine() {
        compositionPanel.repaint(); // the Java Graphics framework will call paintComponent
    }

    // MODIFIES: this
    // EFFECTS:  moves current x-column to next column; updates figures
    private void incrementColumn() {
        playingColumn += 1;
        compositionPanel.setPlayLineColumn(playingColumn);
        lastColumnPlayed = notesInColumn;
    }

    // MODIFIES: this
    // EFFECTS:  calls Timer.stop() when playingColumn is past the edge of the composition.
    private void stopPlayingWhenDone() {
        if (playingColumn > compositionPanel.getComposition().getEnd() + BUFFER) {
            tool.stop(); // calls stopPlaying
        }
    }

    // MODIFIES: this
    // EFFECTS: stops the timer to stop playing.
    public void stopPlaying() {
        timer.stop();
        unselectAndStopPlayingAll();
        playingColumn = 0;
        compositionPanel.setPlayLineColumn(playingColumn);
        compositionPanel.repaint();
    }

}
