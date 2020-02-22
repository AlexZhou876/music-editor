# My Personal Project

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