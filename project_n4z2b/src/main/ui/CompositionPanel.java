package ui;

import model.Composition;
import model.Measure;

import javax.swing.*;
import java.awt.*;

public class CompositionPanel extends JPanel {
    private int playLineColumn;
    private Composition composition;
    public static final int BAR_WIDTH = 200;
    public static final int BEAT_WIDTH = 50;
    public static final int SEMITONE_HEIGHT = 10;


    public CompositionPanel(int numMeasures, int beatNum, int beatType) {
        super();
        setBackground(Color.black);
        composition = new Composition(numMeasures, beatNum, beatType);

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
        for (int measureNum = 0; measureNum < composition.getListOfMeasure().size(); measureNum++) {
            int x = BAR_WIDTH * (measureNum + 1);
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

    public void setPlayLineColumn(int target) {
        playLineColumn = target;
    }


}
