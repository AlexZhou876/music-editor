package ui.players;

import model.Composition;
import model.Note;
import ui.EditorApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EntirePlayer implements ActionListener {

    private Composition composition;
    private Timer timer;
    private int playingColumn;
    private List<Note> lastColumnPlayed;
    private List<Note> notesInColumn;

    public EntirePlayer(Composition composition, Timer timer) {
        this.composition = composition;
        this.timer = timer;
        playingColumn = 0;
        lastColumnPlayed = new ArrayList<Note>();
        notesInColumn = new ArrayList<Note>();
    }

    // MODIFIES: this
    // EFFECTS:  plays notes in current column, repaints, increments column,  stops if done
    //           this class is the listener for the timer object, and this method is what the timer calls
    //           each time through its loop.
    @Override
    public void actionPerformed(ActionEvent e) {
        selectAndPlayNotes();
        drawRedLine();
        incrementColumn();
        stopPlayingWhenDone();
    }

    // MODIFIES: this
    // EFFECTS: selects and plays notes at current column
    private void selectAndPlayNotes() {
        notesInColumn = composition.getNotesAtColumn(playingColumn);
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
    // EFFECTS:  moves playback line to playingColumn to trigger sound and repaint
    private void drawRedLine() {
        composition.setPlayLineColumn(playingColumn);
        composition.repaint(); // the Java Graphics framework will call paintComponent
    }

    // MODIFIES: this
    // EFFECTS:  moves current x-column to next column; updates figures
    private void incrementColumn() {
        playingColumn += 1;
        lastColumnPlayed = notesInColumn;
    }

    // MODIFIES: this
    // EFFECTS:  calls Timer.stop() when playingColumn is past the edge of the frame
    private void stopPlayingWhenDone() {
        if (playingColumn > composition.getEnd()) {
            timer.stop();
        }
    }

}
