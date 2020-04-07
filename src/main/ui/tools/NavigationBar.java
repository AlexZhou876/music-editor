package ui.tools;

import ui.GraphicalEditorApp;
import ui.players.EntirePlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.CompositionPanel.*;

public class NavigationBar extends JToolBar {
    private JButton jumpTo;
    private JButton zoomIn;
    private JButton zoomOut;
    private JButton setZoom;
    private JTextField measureNum;
    private JButton playFrom;
    private JTextField playFromNum;
    private GraphicalEditorApp parent;

    public NavigationBar(String label, JButton button, JTextField field, GraphicalEditorApp parent) {
        super(label);
        jumpTo = button;
        this.measureNum = field;
        add(button);
        add(field);

        zoomIn = new JButton("+");
        zoomOut = new JButton("-");
        setZoom = new JButton("Set Zoom");
        playFrom = new JButton("Play From");
        playFromNum = new JTextField();

        addSeparator();
        add(zoomIn);
        add(zoomOut);
        add(setZoom);
        addSeparator();
        add(playFrom);
        add(playFromNum);




        addListeners();
        this.parent = parent;
    }

    private void addListeners() {
        jumpTo.addActionListener(new JumpToClickHandler());
        zoomIn.addActionListener(new ZoomInClickHandler());
        zoomOut.addActionListener(new ZoomOutClickHandler());
        playFrom.addActionListener(new PlayFromClickHandler());
        //setZoom.addActionListener();
    }

    private class JumpToClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int numToJumpTo = Integer.parseInt(measureNum.getText());
                int coordToJumpTo = (parent.getCompositionPanel()
                        .getComposition().getMeasure(numToJumpTo).getGlobalStart() - 1) * BEAT_WIDTH;
                parent.getScroller().getHorizontalScrollBar().setValue(coordToJumpTo);
            } catch (NumberFormatException exc) {
                exc.printStackTrace();
            }
        }
    }

    private class ZoomInClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (BEAT_WIDTH < MAXIMUM_BEAT_WIDTH) {
                BEAT_WIDTH *= 2;
                parent.repaint();
            }
        }
    }

    private class ZoomOutClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (BEAT_WIDTH > MINIMUM_BEAT_WIDTH) {
                BEAT_WIDTH /= 2;
                parent.repaint();
            }
        }
    }

    private class PlayFromClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int measure = Integer.parseInt(playFromNum.getText());
                int start = BEAT_WIDTH
                        * (parent.getCompositionPanel().getComposition().getMeasure(measure).getGlobalStart() - 1);
                parent.getCompositionPanel().setPlayLineColumn(start);
                parent.getPlayer().setPlayingColumn(start);


            } catch (NumberFormatException exc) {
                exc.printStackTrace();
            }
        }
    }
}
