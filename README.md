# Music Editor

## Instructions 
This is a music editor. It is created using the IntelliJ IDEA IDE. To use, run Main.main. It is mouse interactive and graphically displays your input.
- to add a note: click on the button on the toolbar labelled "Add Note".
- press the mouse down within the bounds of a measure. A note appears.
- while holding, drag the mouse to the right and left to change the length.
- release the mouse to finish placing.
you can also add measures to the composition.
- to add a measure: click on the button labelled "Add Measures" 
- enter a valid number
- see the measures appear
you can edit a note by using the edit note button. Click on a note to select it, drag to move it. Use the arrow 
keys to adjust the length of the note. Press the delete key (OSX: fn-delete) to delete it. 

you can save by pressing save. It will save to whatever the current path is set to in GraphicalEditorApp.
to load the save, simply rerun and click the button on the dialog. For now, you can load external midi files by putting them in the
same root directory and changing the path in GraphicalEditorApp. Note that behaviour is only specified for files with 4/4 time signature.


## What Will the Application Do? 
My project will be a music visualization and composition application. 
It will allow users to quickly sketch and further develop musical compositions in a 2d environment 
where the vertical axis represents pitch and the horizontal axis represents time. 
The application will also include helper features such as suggestions, templates, 
and ways to automate manual user input (refactoring). 
The following is a list of specific planned functionality:
- placing, moving, and resizing notes by mouse or keyboard
- note placement constrained to discrete positions in time and pitch
- quick manipulation such as group selection of notes, copying/pasting
- ability for user to define a group of notes as a template
- automatic, viewable analysis of music metacharacteristics, such as chord progression
- ability for user to change a metacharacterstic and have the music change automatically



## User Stories
- As a user, I want to be able to create a new note(s) with a start time, time value, pitch, and volume, 
adding it to the collection of existing notes in a given measure.
- As a user, I must be able to view on demand the entire content of my composition.
- As a user, I want to be able to select a note and change its characteristics, or possibly delete it.
- As a user, I must be able to specify the number of measures in my composition and add or remove measures. 

## Phase 2 Persistence User Stories
- as a user, I must be able to save the current composition as a .mid file before quitting the program.
- as a user, I must be able to load any previously saved composition after running the program (but not necessarily
any given .mid file from anywhere).

## Phase 3 GUI User Stories
- be able to play back the whole composition
- no others except preserving the functionality of earlier user stories


## Phase 4: Task 2
- I have chosen to implement a bidirectional association between model.Measure and model.Note. 

## Phase 4: Task 3
- there was low cohesion in model.Composition because it had ui and graphics responsibilities as well
 as model responsibilities. It used to extend JPanel. This can be seen in previous commits. Fixed by separating ui
 responsibilities into a new class ui.CompositionPanel. 
-  PlayEntireTool separated audio and playing responsibilities into EntirePlayer, PlayEntireTool deals only with ui 
responsibilities. This improves cohesion.

## Continuation

## Wish List/Todo: 
- disable other tools while playing is active
- refactor player to not need association to model
- separate out graphics responsibilities from model entirely using some sort of renderer class
- selection of notes or a group of notes (using box select). Must be able to move the selected notes
- copy and pasting of selected notes.
- change the tempo of playback
- ability to pan and zoom in the 2d environment where the piece is displayed. (pan: done. Zoom: done horizontally, need vertically)
- ability to play the composition from a starting point (implemented)
- ability to undo last action (weird one, maybe ignore for now)
- have text annotations, including title and credits at the beginning
- separate piece into different voices, like layers in an image editing app (ignore on first pass)
- different instruments
- import and correctly interpret midi files (fractional note values) export correct midi files

## Bugs

- notes in a measure do not change position when their measure changes position due to removal of other measures (fixed)
- notes touching the very end of the composition do not stop playing and are still selected (fixed)
- edit mode is still active in play mode (fixed)
- notes can be extended past end of composition (fixed)
- inappropriate error messages appear after closing or cancelling dialogs (fixed)
- note immediately following note of the same pitch fails to sound correctly (fixed)
- saving and loading doesn't work properly (there may be problems with saving)
- when adding note, able to drag it to negative value (fixed)
- user specified tempo changes have little effect when the resolution is too high as the timer delay is at its minimum value.
- timer controlling playback progress line/auto scrolling and timer controlling playback are not in sync.



