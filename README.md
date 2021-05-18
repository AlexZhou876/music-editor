<img src=media/musiceditordemo.gif alt="alt text" width="500" height="500">


# music-editor
This is a lightweight MIDI editor capable of creating, loading, modifying and saving instrumental music in the form of MIDI files. It features a GUI with a scrollable 2d piano roll 
environment, and tools that allow the user to add a note at specified position with specified length, remote a specific note, add a specific number of measures (currently to the end only),
remove specific measures, and auto-scrolling playback. 


## Instructions
Clone the repository and open in an IDE (I use IntelliJ). Run src.main.ui.Main.main. 
- to add a note: click on the button on the toolbar labelled "Add Note".
- press the mouse down within the bounds of a measure. A note appears.
- while holding, drag the mouse to the right and left to change the length.
- release the mouse to finish placing.
you can also add measures to the composition.
- to add a measure: click on the button labelled "Add Measures"
- enter a valid number
you can edit a note by using the edit note button. Click on a note to select it, drag to move it. Use the arrow 
keys to adjust the length of the note. Press the delete key (OSX: fn-delete) to delete it. 

you can save by pressing save.
to load the save, simply rerun and click the button on the dialog. Note that behaviour is only specified for files with 4/4 time signature.

## Tests
Unit tests (src.test) have been written for classes in the model and persistence packages. 

