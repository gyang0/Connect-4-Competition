# Connect-4-Competition
- Student starter code (needs to be modified): https://github.com/gyang0/Connect-4

## Map
- **game_utils**
    - `Game.java` - general methods relating to the game&mdash;determining wins, playing out two different Player instances, checking illegal moves, etc.
    - `Player.java` - class to be extended. Has a method that determines what move to play in a certain board configuration.
    - `Tournament.java` - pair entries in a round-robin tournament and rank them

- **student_submissions**: folder to put all student code
    - (All submission files extend Player.java, with their own method to decide what move to play given a board config)

- **swing_stuff**: folder to put all Swing-related helper classes
    - `CustomButton.java` - A JComponent that _kind of_ acts like a button (clicks must be implemented in parent JPanel)
    - `CustomEvent.java` - Used to fire events in `CustomButton.java`. Literally 5 lines.

- `Main.java` - Run GUI stuff
- `Home.java` - Swing for Home screen
- `Credits.java` - Swing for Credits screen
- `Rules.java` - Swing for Rules screen
- `Results.java` - Swing for Results screen

## In progress
- GUI
- Rework Student starter code & guide
- Testcases to run each entry through
- Slander