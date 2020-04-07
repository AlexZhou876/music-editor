package ui;

import model.Composition;
import model.Measure;

import javax.swing.*;
import java.awt.*;

// does this even need to implement scrollable?
public class CompositionPanel extends JPanel implements Scrollable {
    private int playLineColumn;
    private GraphicalEditorApp editor;
    private Composition composition;
    public static final int MAXIMUM_BEAT_WIDTH = 512;
    public static final int MINIMUM_BEAT_WIDTH = 16;
    public static int BEAT_WIDTH = 64; //50
    public static final int BAR_WIDTH = 4 * BEAT_WIDTH; // note: reimplement the usage of BAR_WIDTH so that this const
    // it is no longer needed.
    public static final int SEMITONE_HEIGHT = 10;


    public CompositionPanel(int numMeasures, int beatNum, int beatType, GraphicalEditorApp editor) {
        super();
        setBackground(Color.black);
        composition = new Composition(numMeasures, beatNum, beatType);
        this.editor = editor;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
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
        /* this implementation depends on constant bar width
        for (int measureNum = 0; measureNum < composition.getListOfMeasure().size(); measureNum++) {
            int x = BAR_WIDTH * (measureNum + 1);
            graphics.drawLine(x, 0, x, getHeight());
            endX = x;
        }

         */
        int x = 0;
        for (int measureNum = 0; measureNum < composition.getListOfMeasure().size(); measureNum++) {
            x = x + (composition.getListOfMeasure().get(measureNum).getNumBeats() * BEAT_WIDTH);
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
