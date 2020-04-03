# My Personal Project

## Instructions for Grader
This is a music editor. To use, run Main.main. It is mouse interactive and graphically displays your input.
- to add a note: click on the button on the toolbar labelled "Add Note".
- press the mouse down within the bounds of a measure. A note appears.
- while holding, drag the mouse to the right and left to change the length.
- release the mouse to finish placing.
you can also add measures to the composition.
- to add a measure: click on the button labelled "Add Measures" 
- enter a valid number
- see the measures appear
The visual component is clearly displayed. The audio component is triggered by hitting the Play! button.
Unfortunately, I was unable to adapt persistence for this phase. Thank you for using. 


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

## Who Will Use It? 
This application will be useful for anyone interested in music/composition, students of music, or composers. 
## Why is the project of interest to me? 
this project is of interest primarily because I am interested in music, being a musician. 
However, I've always struggled with theory and improvisation. Programming an application 
like this will increase my understanding of music, and furthermore challenge and improve my programming 
skills because of the specific demands of the domain. 

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

## Wish List/Todo
- refactor player to not need association to model
- separate out graphics responsibilities from model entirely using some sort of renderer class
- selection of notes or a group of notes (using box select). Must be able to move the selected notes
- copy and pasting of selected notes.
- ability to pan and zoom in the 2d environment where the piece is displayed.
- ability to play the composition from a starting point.
- ability to undo last action
- have text annotations, including title and credits at the beginning
- separate piece into different voices, like layers in an image editing app

## Bugs
- notes in a measure do not change position when their measure changes position due to removal of other measures (fixed)
(note: solution not most efficient, because all measures are counted while they don't all need to be. But this is low concern)
- notes touching the very end of the composition do not stop playing and are still selected (fixed)
- edit mode is still active in play mode (fixed)
- notes can be extended past end of composition (fixed)
- inappropriate error messages appear after closing or cancelling dialogs (fixed)
- note immediately following note of the same pitch fails to sound correctly
- saving and loading doesn't work properly (fixed)
MidiSynth is the class that handles sound. Only a single instance is currently used so it makes sense to make it 
Singleton to preserve this behaviour.

