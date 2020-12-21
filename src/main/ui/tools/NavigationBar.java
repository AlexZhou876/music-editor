package ui.tools;

import ui.GraphicalEditorApp;

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
    private JButton setTempo;
    private JTextField tempo;
    private GraphicalEditorApp parent;

    public NavigationBar(String label, JButton button, JTextField field, GraphicalEditorApp parent) {
        super(label);
        jumpTo = button;
        this.measureNum = field;
        zoomIn = new JButton("+");
        zoomOut = new JButton("-");
        setZoom = new JButton("Set Zoom");
        playFrom = new JButton("Set Playback Start Measure");
        playFromNum = new JTextField();
        tempo = new JTextField();
        setTempo = new JButton("Set Tempo (QBPM)");
        this.parent = parent;
        add(button);
        add(field);
        addUIElements();

    }

    private void initializeFieldsAndUIElements() {

    }

    private void addUIElements() {
        addSeparator();
        add(zoomIn);
        add(zoomOut);
        add(setZoom);
        addSeparator();
        add(playFrom);
        add(playFromNum);
        addSeparator();
        add(setTempo);
        add(tempo);
        addListeners();
    }

    private void addListeners() {
        jumpTo.addActionListener(new JumpToClickHandler());
        zoomIn.addActionListener(new ZoomInClickHandler());
        zoomOut.addActionListener(new ZoomOutClickHandler());
        playFrom.addActionListener(new PlayFromClickHandler());
        setTempo.addActionListener(new SetTempoClickHandler());
        //setZoom.addActionListener();
    }

    private class JumpToClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int numToJumpTo = Integer.parseInt(measureNum.getText());
                //int coordToJumpTo = (parent.getCompositionPanel()
                  //      .getComposition().getMeasure(numToJumpTo).getGlobalStartTick() - 1) * beatWidth;
                int coordToJumpTo = parent.getCompositionPanel().getComposition().getMeasure(numToJumpTo)
                        .getGlobalStartTick() * tickWidth;
                parent.getScroller().getHorizontalScrollBar().setValue(coordToJumpTo);
            } catch (NumberFormatException exc) {
                exc.printStackTrace();
            }
        }
    }

    private class ZoomInClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (beatWidth < MAXIMUM_BEAT_WIDTH) {
                //beatWidth += ZOOM_INTERVAL;
                parent.getCompositionPanel().setBeatWidth(beatWidth + ZOOM_INTERVAL);
                parent.getCompositionPanel().resize();
                parent.repaint();
            }
        }
    }

    private class ZoomOutClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (beatWidth > MINIMUM_BEAT_WIDTH) {
                //beatWidth -= ZOOM_INTERVAL;
                parent.getCompositionPanel().setBeatWidth(beatWidth - ZOOM_INTERVAL);
                parent.getCompositionPanel().resize();
                parent.repaint();
            }
        }
    }

    private class PlayFromClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int measure = Integer.parseInt(playFromNum.getText());
                int startTick = parent.getCompositionPanel().getComposition().getGlobalStartOf(measure);
                parent.getCompositionPanel().setPlayLineColumnMeasure(measure);
                parent.setPlayingStartTick(startTick);


            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(parent, "Invalid Number Entry!");
                nfe.printStackTrace();
            } catch (Exception exc) {
                // this one is supposed to be for the measure out of bounds
                JOptionPane.showMessageDialog(parent, "Invalid Measure Number!");
                exc.printStackTrace();
            }
        }
    }

    private class SetTempoClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int newBPM = Integer.parseInt(tempo.getText());
                parent.getCompositionPanel().setBPM(newBPM);
            } catch (NumberFormatException exc) {
                exc.printStackTrace();
            }

        }
    }
}
