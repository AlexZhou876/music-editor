package ui;

import model.Composition;
import model.Measure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// a beat refers to a quarter beat.
public class CompositionPanel extends JPanel implements ActionListener, Scrollable {
    private int playLineColumn;
    private GraphicalEditorApp editor;
    private Composition composition;

    public static final int DEFAULT_BPM = 80;

    public static final int MAXIMUM_BEAT_WIDTH = 512;
    public static final int MINIMUM_BEAT_WIDTH = 1;
    public static final int ZOOM_INTERVAL = 10;

    public static int bpm;
    //public static int resolution = 4; // initial tick is a 16th note
    public static int tickWidth = 16;
    //public static int beatWidth = tickWidth * resolution; //50, then tried 64
    public static int beatWidth = 64;
    public static final int SEMITONE_HEIGHT = 10;


    public CompositionPanel(int numMeasures, int beatNum, int beatType, GraphicalEditorApp editor) {
        super();
        setBackground(Color.black);
        composition = new Composition(numMeasures, beatNum, beatType);
        this.editor = editor;
        bpm = DEFAULT_BPM;
    }

    @Override
    // MODIFIES: this
    // EFFECTS: update this every time the timer fires by incrementing playLineColumn by one screen coordinate
    // and scroll following.
    public void actionPerformed(ActionEvent e) {
        playLineColumn += 1;
        repaint();
        followPlaying();
        //if (editor.getMasterTimer().getDelay() == )
    }

    // MODIFIES: this
    // EFFECTS: stops the timer and resets the playLineColumn to 0.
    public void stopPlaying() {
        editor.getMasterTimer().stop();
        playLineColumn = 0;
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: sets the value of beatWidth and makes sure invariant relationships between static variables
    // are maintained.
    // value writes even of public static variables should be done locally and in one place so the repercussions can be
    // handled in one place.
    public void setBeatWidth(int newBeatWidth) {
        float factor = (float) newBeatWidth / (float) beatWidth;
        beatWidth = newBeatWidth;
        tickWidth = Math.round(factor * tickWidth);
    }

    // EFFECTS: gets the end of the composition panel in screen x coordinate.
    // OUTDATED
    public int getEnd() {
        int totalTicks = composition.getNumTicks();
        return totalTicks * tickWidth;
        //int totalBeats = composition.getNumBeats();
        //return totalBeats * beatWidth;
    }

    // MODIFIES: this
    // EFFECTS: changes the dimensions of this. If the new width is greater than the starting width, assign
    // the new width. Otherwise do nothing.
    public void resize() {
        if (getEnd() > editor.WIDTH) {
            setPreferredSize(new Dimension(getEnd(), editor.HEIGHT));
        }
        revalidate();
    }

    public void setBPM(int newBPM) {
        bpm = newBPM;
    }

    public Composition getComposition() {
        return composition;
    }


    // EFFECTS: sets composition to the given one, and ensures that the ratio of tickwidth to beatwidth
    // obeys the resolution of the composition.
    public void setComposition(Composition composition) {
        this.composition = composition;
        int amount = beatWidth / Composition.resolution;
        if (amount == 0) {
            amount = 1;
        }
        tickWidth = amount;
        resize();
    }




    // EFFECTS: paints grid, playing line, notes in composition.
    // calls to repaint() get here
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawLines(graphics);
        // it may be better for measure.draw to draw bar lines.
        for (Measure measure : composition.getListOfMeasure()) {
            measure.draw(graphics);
        }
    }

    // EFFECTS: draws semitone lines and bar lines
    private void drawLines(Graphics graphics) {
        int endX = 0;
        Color save = graphics.getColor();
        graphics.setColor(new Color(100, 100, 200));
        // 100 100 200

        List<Integer> positions = composition.getPositionsOfMeasureLines();
        for (Integer x : positions) {
            graphics.drawLine(x, 0, x, getHeight());
            endX = x;
        }

        for (int y = SEMITONE_HEIGHT; y < getHeight(); y += SEMITONE_HEIGHT) {
            graphics.drawLine(0, y, endX, y);
        }

        if (playLineColumn > 0 && playLineColumn < getWidth()) {
            graphics.setColor(Color.RED);
            graphics.drawLine(playLineColumn, 0, playLineColumn, getHeight());
        }
        graphics.setColor(save);
    }

    // MODIFIES: this
    // EFFECTS: sets value of playLineColumn
    public void setPlayLineColumn(int target) {
        playLineColumn = target;
    }

    // MODIFIES: editor
    // EFFECTS: makes the editor's scroller keep the playing line in the middle of the view.
    public void followPlaying() {
        int leftEndOfView = playLineColumn - (editor.WIDTH / 2);
        editor.getScroller().getHorizontalScrollBar().setValue(leftEndOfView);
    }



    

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(GraphicalEditorApp.WIDTH, GraphicalEditorApp.HEIGHT);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }


}
