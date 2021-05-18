package ui;

import com.sun.org.apache.bcel.internal.generic.Select;
import model.Composition;
import persistence.Reader;
import ui.players.EntirePlayer;
import ui.sound.MidiSynth;
import ui.tools.*;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static ui.CompositionPanel.SEMITONE_HEIGHT;

public class GraphicalEditorApp extends JFrame {
    public static final int WIDTH = 1500;
    public static final int HEIGHT = SEMITONE_HEIGHT * 88;
    public static final String SAVE_FILE = "./data/Gurenge.mid";

    public static MidiSynth midiSynth;
    private EntirePlayer player;
    private Timer masterTimer;

    private int playingStartTick;

    private CompositionPanel compositionPanel;
    private JScrollPane scroller;

    private List<Tool> tools; // retain references to tools accessible from this class after initialization
    private Tool activeTool;

    public GraphicalEditorApp() {
        super("Music Editor");
        initFields();
        initGraphics();
        initSound();
        showInitDialog();
        initInteraction();
    }

    public Tool getActiveTool() { return activeTool; }

    public MidiSynth getMidiSynth() {
        return midiSynth;
    }

    public CompositionPanel getCompositionPanel() {
        return compositionPanel;
    }

    public int getPlayingStartTick() { return playingStartTick; }

    public void setPlayingStartTick(int target) {  playingStartTick = target; }

    private void initFields() {
        activeTool = null;
        compositionPanel = new CompositionPanel(1, 4, 4, this);
        playingStartTick = 0;
        tools = new ArrayList<Tool>();
    }

    // EFFECTS: allows the user to input init options, such as to load from file or create new.
    private void showInitDialog() {
        int closedOption = -1;
        int yes = 0;
        int no = 1;
        String[] options = {"Yes", "No"};
        int n = JOptionPane.showOptionDialog(this,
                "Would you like to load the previously saved file?",
                "Initialization",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        if (n == closedOption || n == no) {
            return;
        } else if (n == yes) {
            String input = JOptionPane.showInputDialog(this,
                    "Specify the path.", SAVE_FILE);
            loadProject(input);
        }
    }

    // MODIFIES: this
    // EFFECTS: changes composition to the one contained in the save file
    private void loadProject(String path) {
        Composition c = Reader.readFile(new File(path));

        compositionPanel.setComposition(c);
        compositionPanel.getComposition().addMidiSynthToAll(midiSynth);
        // must update Composition references in tools
        for (Tool tool: tools) {
            tool.setComposition(c);
        }

        repaint();
    }


    private void initSound() {
        midiSynth = new MidiSynth();
        midiSynth.open();
    }

    private void initGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        createTools();
        createNavigationBar();
        addComposition();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        // necessary if size of composition may change???
        setBackground(Color.black);
    }

    private void createNavigationBar() {
        //JToolBar navigationBar = new JToolBar("Navigation");
        JButton button = new JButton("Jump to");
        JTextField measureNumber = new JTextField();
        NavigationBar bar = new NavigationBar("Navigation", button, measureNumber, this);
        add(bar, BorderLayout.PAGE_START);


    }

    // MODIFIES: this
    // EFFECTS: adds a composition component to the editor
    private void addComposition() {
        //add(compositionPanel, BorderLayout.CENTER);
        compositionPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        scroller = new JScrollPane(compositionPanel);
        add(scroller, BorderLayout.CENTER);
        validate();
    }

    public JScrollPane getScroller() {
        return scroller;
    }

    // MODIFIES: this
    // EFFECTS: declare and instantiate all tools
    private void createTools() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(0, 1));
        toolbar.setSize((new Dimension(0, 0)));
        add(toolbar, BorderLayout.EAST);

        SelectNoteTool snt = new SelectNoteTool(this, toolbar);
        tools.add(snt);

        AddNoteTool addNoteTool = new AddNoteTool(this, toolbar);
        tools.add(addNoteTool);

        AddMeasuresTool addMeasuresTool = new AddMeasuresTool(this, toolbar);
        tools.add(addMeasuresTool);

        EditNoteTool editNoteTool = new EditNoteTool(this, toolbar);
        tools.add(editNoteTool);

        RemoveMeasuresTool removeMeasuresTool = new RemoveMeasuresTool(this, toolbar);
        tools.add(removeMeasuresTool);

        masterTimer = new Timer(0, null);
        //player = new EntirePlayer(compositionPanel.getComposition(), null, null);
        PlayEntireTool playEntireTool = new PlayEntireTool(this, toolbar, player);
        tools.add(playEntireTool);



        SaveTool saveTool = new SaveTool(this, toolbar);
        tools.add(saveTool);

        setActiveTool(addNoteTool);
    }

    public EntirePlayer getPlayer() {
        return player;
    }

    public Timer getMasterTimer() {
        return masterTimer;
    }

    public void setMasterTimer(Timer masterTimer) {
        this.masterTimer = masterTimer;
    }

    // MODIFIES: this
    // EFFECTS: initializes EditorMouseListener and EKL for JFrame
    private void initInteraction() {
        EditorMouseListener eml = new EditorMouseListener();
        compositionPanel.addMouseListener(eml);
        compositionPanel.addMouseMotionListener(eml);

        //EditorKeyListener ekl = new EditorKeyListener();
        //addKeyListener(ekl);
        //compositionPanel.addKeyListener(ekl);
        //compositionPanel.setFocusable(true);
        //compositionPanel.requestFocusInWindow();


    }

    // MODIFIES: this
    // EFFECTS:  sets the given tool as the activeTool
    public void setActiveTool(Tool tool) {
        if (activeTool != null) {
            activeTool.deactivate();
        }
        tool.activate();
        activeTool = tool;
    }

    // EFFECTS: if activeTool != null, then mousePressedInDrawingArea is invoked on activeTool, depends on the
    //          type of the tool which is currently activeTool
    private void handleMousePressed(MouseEvent e) {
        if (activeTool != null) {
            activeTool.mousePressed(e);
        }
        repaint();
    }

    // EFFECTS: if activeTool != null, then mouseReleasedInDrawingArea is invoked on activeTool, depends on the
    //          type of the tool which is currently activeTool
    private void handleMouseReleased(MouseEvent e) {
        if (activeTool != null) {
            activeTool.mouseReleased(e);
        }
        repaint();
    }

    // EFFECTS: if activeTool != null, then mouseClickedInDrawingArea is invoked on activeTool, depends on the
    //          type of the tool which is currently activeTool
    private void handleMouseClicked(MouseEvent e) {
        if (activeTool != null) {
            activeTool.mouseClicked(e);
        }
        repaint();
    }

    // EFFECTS: if activeTool != null, then mouseDraggedInDrawingArea is invoked on activeTool, depends on the
    //          type of the tool which is currently activeTool
    private void handleMouseDragged(MouseEvent e) {
        if (activeTool != null) {
            activeTool.mouseDragged(e);
        }
        repaint();
    }

    // EFFECTS: if activeTool != null, then keytyped is invoked on activeTool.
    private void handleKeyTyped(KeyEvent ke) {
        if (activeTool != null) {
            activeTool.keyTyped(ke);
        }
        repaint();
    }



    // from SimpleDrawingPlayer (modified)
    private class EditorMouseListener extends MouseAdapter {
        // EFFECTS: Forward mouse pressed event to the active tool
        public void mousePressed(MouseEvent e) {
            handleMousePressed(translateEvent(e));
        }

        // EFFECTS: Forward mouse released event to the active tool
        public void mouseReleased(MouseEvent e) {
            handleMouseReleased(translateEvent(e));
        }

        // EFFECTS:Forward mouse clicked event to the active tool
        public void mouseClicked(MouseEvent e) {
            handleMouseClicked(translateEvent(e));
        }

        // EFFECTS:Forward mouse dragged event to the active tool
        public void mouseDragged(MouseEvent e) {
            handleMouseDragged(translateEvent(e));
        }

        // EFFECTS: translates the mouse event to current drawing's coordinate system
        private MouseEvent translateEvent(MouseEvent e) {
            return SwingUtilities.convertMouseEvent(e.getComponent(), e, compositionPanel);
        }
    }



}
