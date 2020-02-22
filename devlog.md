19/2/2019: found that removeMeasures removes the correct number 
of measures, but not the correct measure instances. 
Fix: reverse listOfPos to remove last first. Also refactored
input handling in EditorApp to use the much shorter string 
split method. Wrote cases for the Dynamic enum. 