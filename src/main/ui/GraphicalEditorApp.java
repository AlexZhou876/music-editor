package ui;

import model.Composition;
import persistence.Reader;
import ui.players.EntirePlayer;
import ui.sound.MidiSynth;
import ui.tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static model.Composition.SEMITONE_HEIGHT;

public class GraphicalEditorApp extends JFrame {
    public static final int WIDTH = 1500;
    public static final int HEIGHT = SEMITONE_HEIGHT * 88;
    //public static final int HEIGHT = SEMITONE_HEIGHT * 70;
    public static final String SAVE_FILE = "./data/saveFile.mid";

    private MidiSynth midiSynth;
    private EntirePlayer player;

    private CompositionPanel compositionPanel;
    private JScrollPane scroller;

    private List<Tool> tools; // is this even useful??
    private Tool activeTool;

    public GraphicalEditorApp() {
        super("Music Editor");
        initFields();
        initGraphics();
        initSound();
        showInitDialog();
        initInteraction();
    }

    public MidiSynth getMidiSynth() {
        return midiSynth;
    }

    public CompositionPanel getCompositionPanel() {
        return compositionPanel;
    }

    private void initFields() {
        activeTool = null;
        compositionPanel = new CompositionPanel(1, 4, 4, this);
        //composition.addMeasures(1, 1, 4, 4);
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
        if (n == closedOption) {
            return;
        } else if (n == yes) {
            loadProject();
        } else {
            return;
        }
    }

    // MODIFIES: this
    // EFFECTS: changes composition to the one contained in the save file
    private void loadProject() {
        compositionPanel.setComposition(Reader.readFile(new File(SAVE_FILE)));
        compositionPanel.getComposition().addMidiSynthToAll(midiSynth);
        //compositionPanel = Reader.readFile(new File(SAVE_FILE));
        //compositionPanel.addMidiSynthToAll(midiSynth);
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

        AddNoteTool addNoteTool = new AddNoteTool(this, toolbar);
        tools.add(addNoteTool);
// should I add the others to the list?? What is the point of the list?
        AddMeasuresTool addMeasuresTool = new AddMeasuresTool(this, toolbar);

        EditNoteTool editNoteTool = new EditNoteTool(this, toolbar);

        RemoveMeasuresTool removeMeasuresTool = new RemoveMeasuresTool(this, toolbar);


        player = new EntirePlayer(compositionPanel, new Timer(0,null), null);
        PlayEntireTool playEntireTool = new PlayEntireTool(this, toolbar, player);
        player.setTool(playEntireTool);

        SaveTool saveTool = new SaveTool(this, toolbar);

        setActiveTool(addNoteTool);
    }

    public EntirePlayer getPlayer() {
        return player;
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
