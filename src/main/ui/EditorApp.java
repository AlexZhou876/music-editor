/*
package ui;

import model.Composition;
import model.Measure;
import model.Note;
import persistence.Reader;
import persistence.Writer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Music editor console application
public class EditorApp {
    private Scanner input;
    private Composition piece;
    private static final String SAVE_FILE = "./data/saveFile.mid";

    // EFFECTS: runs the editor app.
    public EditorApp() {
        runEditor();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runEditor() {
        boolean running = true;
        String command = null;
        System.out.println("Enter 'l' to load or 'n' to start new file.");
        input = new Scanner(System.in);
        command = input.next();
        if (command.equals("l")) {
            loadComposition();
        } else {
            init();
        }
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

    // MODIFIES: this
    // EFFECTS: loads the content from existing file.
    private void loadComposition() {
        piece = Reader.readFile(new File(SAVE_FILE));
        System.out.println("Loaded.");
    }

    private void saveComposition() {
        Writer writer = new Writer();
        writer.writeFile(piece, SAVE_FILE);
        System.out.println("Saved.");
    }

    // EFFECTS: displays menu of command options
    private void displayOptions() {
        System.out.println("Input: Feature");
        System.out.println("1: Add Measures");
        System.out.println("2: Remove Measures");
        System.out.println("3: Add a Note to a Measure");
        System.out.println("4: Edit an Existing Note");
        System.out.println("5: Delete an Existing Note");
        System.out.println("6: See Entire Composition");
        System.out.println("7: Save Composition to File");
        System.out.println("q: Quit");
    }

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
            editNote();
        } else if (command.equals("5")) {
            deleteNote();
        } else if (command.equals("6")) {
            printComposition();
        } else if (command.equals("7")) {
            saveComposition();
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
            } else {
                beatType = input.nextInt();
            }
        }
        piece.addMeasures(num, pos, beatNum, beatType);
        System.out.println("Measures added.");
    }
    /*
    Scanner tempInput = new Scanner(System.in);
        List<Integer> inputList = new ArrayList<>();
        String command = tempInput.nextLine();
        String[] tempArray = command.split(" ");
        for (String s: tempArray) {
            inputList.add(Integer.parseInt(s));
        }


    // EFFECTS: allows the user to remove measures.
    private void removeMeasures() {
        Scanner tempInput = new Scanner(System.in);
        List<Integer> listOfPos = new ArrayList<>();
        System.out.println("Enter, with spaces, the measure #s of the measures to remove.");
        String command = tempInput.nextLine();
        String[] tempArray = command.split(" ");
        for (int i = 0; i < tempArray.length; i++) {
            try {
                listOfPos.add(Integer.parseInt(tempArray[i]));
            } catch (NumberFormatException e) {
                System.out.println("Enter only valid numbers.");
            }
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
            } else {
                pitch = input.nextInt();
            }
        }
        measure.addNewNote(start, value, pitch); //or could call Note constructor
        System.out.println("Note added.");
    }

    // EFFECTS: allows the user to delete a note.
    private void deleteNote() {
        Measure measure = null;
        int beat = 0;
        int pitch = 0;
        System.out.println("Enter, with spaces, the measure# where this note starts, the beat where the note starts"
                + ", and the pitch of the note.");
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                measure = piece.getMeasure(input.nextInt());
            } else if (i == 1) {
                beat = input.nextInt();
            } else {
                pitch = input.nextInt();
            }
        }
        measure.removeNote(measure.getNote(beat, pitch));
        System.out.println("Note removed.");
    }

    // EFFECTS: allows user to edit a note
    private void editNote() {
        Measure measure = null;
        int beat = 0;
        int pitch = 0;
        System.out.println("Enter, with spaces, the measure# where this note starts, the beat where the note starts"
                + ", and the pitch of the note.");
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                measure = piece.getMeasure(input.nextInt());
            } else if (i == 1) {
                beat = input.nextInt();
            } else {
                pitch = input.nextInt();
            }
        }
        Note tempNote = measure.getNote(beat, pitch);
        System.out.println("Enter the target pitch (from 0 to 88).");
        tempNote.movePitch(input.nextInt());
        System.out.println("Enter the target beat (within the measure)");
        tempNote.moveTime(input.nextInt());
    }

    // EFFECTS: allows the user to print the entire composition.
    private void printComposition() {
        System.out.println(piece.getContents());
    }
}
*/