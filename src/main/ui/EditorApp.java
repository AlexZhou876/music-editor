package ui;

import model.Composition;
import model.Measure;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Music editor application
public class EditorApp {
    private Scanner input;
    private Composition piece;

    // EFFECTS: runs the editor app.
    public EditorApp() {
        runEditor();
    }
    // MODIFIES: this
    // EFFECTS: processes user input
    private void runEditor() {
        boolean running = true;
        String command = null;
        input = new Scanner(System.in);
        init();
        while (running) {
            displayOptions();
            command = input.next();
            if (command.equals("q")) {
                running = false;
            } else {
                doCommand(command);
            }
        }
        System.out.println("You have quit the editor.");
    }
    // MODIFIES: this
    // EFFECTS: initializes composition
    private void init() {
        int numMeasures;
        int beatNum;
        int beatType;
        System.out.println("Enter the number of measures in your composition.");
        numMeasures = input.nextInt();
        System.out.println("Enter the number of beats/pulses in each measure.");
        beatNum = input.nextInt();
        System.out.println("Enter the value of beats/pulses in each measure");
        beatType = input.nextInt();
        piece = new Composition(numMeasures, beatNum, beatType);
        System.out.println("Your composition has been created.");
    }
    // EFFECTS: displays menu of command options
    private void displayOptions() {
        System.out.println("Input: Feature");
        System.out.println("1: Add Measures");
        System.out.println("2: Remove Measures");
        System.out.println("3: Add a Note to a Measure");
        System.out.println("4: Edit or Delete an Existing Note");
        System.out.println("5: See Entire Composition");
        System.out.println("q: Quit");
    }

    // REQUIRES: command is a valid command
    // MODIFIES: this
    // EFFECTS: makes changes to the model based on user commands
    private void doCommand(String command) {
        if (command.equals("1")) {
            addMeasures();
        } else if (command.equals("2")) {
            removeMeasures();
        } else if (command.equals("3")) {
            addNote();
        } else if (command.equals("4")) {
            modifyNote();
        } else if (command.equals("5")) {
            printComposition();
        } else {
            System.out.println("Select a valid input.");
        }
    }
    // EFFECTS: allows the user to add measures.
    private void addMeasures() {
        int num = 0;
        int pos = 0;
        int beatNum = 0;
        int beatType = 0;
        System.out.println("Enter, with spaces, the # of measures to add, the measure # after which to add them,"
                + "the # of beats per each, and the value of the beat.");
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                num = input.nextInt();
            } else if (i == 1) {
                pos = input.nextInt();
            } else if (i == 2) {
                beatNum = input.nextInt();
            } else if (i == 3) {
                beatType = input.nextInt();
            }
        }
        piece.addMeasures(num, pos, beatNum, beatType);
        System.out.println("Measures added.");
    }
    // EFFECTS: allows the user to remove measures.
    private void removeMeasures() {
        List<Integer> listOfPos = new ArrayList<>();
        System.out.println("Enter, with spaces, the measure #s of the measures to remove.");
        while (input.hasNext()) {
            listOfPos.add(input.nextInt());
        }
        piece.removeMeasures(listOfPos);
        System.out.println("Measures removed.");
    }
    // EFFECTS: allows the user to add a note.
    private void addNote() {
        int start = 0;
        int value = 0;
        int pitch = 0;
        Measure measure = null;
        System.out.println("Enter, with spaces, the measure # to add this note to, the starting beat of the note,"
                + "the value of the note, and the pitch of the note.");
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                measure = piece.getMeasure(input.nextInt());
            } else if (i == 1) {
                start = input.nextInt();
            } else if (i == 2) {
                value = input.nextInt();
            } else if (i == 3) {
                pitch = input.nextInt();
            }
        }
        measure.addNewNote(start, value, pitch); //or could call Note constructor
        System.out.println("Note added.");
    }
    // EFFECTS: allows the user to modify a note.
    private void modifyNote() {

    }
    // EFFECTS: allows the user to print the entire composition.
    private void printComposition() {
        System.out.println(piece.getContents());
    }
}
