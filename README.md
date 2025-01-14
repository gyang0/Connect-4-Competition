# Connect-4-Competition
- Student starter code (needs to be modified): https://github.com/gyang0/Connect-4

## Map
- **game_utils**
    - `Game.java` - general methods relating to the game&mdash;determining wins, playing out two different Player instances, checking illegal moves, etc.
    - `Player.java` - class to be extended. Has a method that determines what move to play in a certain board configuration.

- **student_submissions**: folder to put all student code
    - (All submission files extend Player.java, with their own method to decide what move to play given a board config)

- `Tournament.java` - pair entries in a round-robin tournament and rank them
- `Main.java` - Run GUI stuff

## In progress
- GUI
- Better data structure to handle scores
- Rework Student starter code & guide
- Testcases to run each entry through
- Better exception handling in Game.java `runTillCompletion()`
- Slander