package game_utils;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;

public class Tournament {
    // Object representation of the i-th player
    Player[] players;

    // wins, losses, and draws for the i-th player
    ScoreBoard[] scoreboard;

    // Constructor
    public Tournament(){
        // Get all the student submission files
        File[] submissions = new File("student_submissions").listFiles();

        // Player i is stored in index i
        players = new Player[submissions.length];
        scoreboard = new ScoreBoard[submissions.length];

        try {
            for(int i = 0; i < submissions.length; i++){
                // Strip .java file extension
                String name = submissions[i].getName();
                name = name.substring(0, name.length() - 5);
                
                // Oh my lord why did this take so long
                players[i] = (Player) Class.forName("student_submissions." + name).getDeclaredConstructor().newInstance();
                scoreboard[i] = new ScoreBoard(players[i].name);

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Simulate a tournament and fill in the scoreboard ArrayList
     */
    public void runTournament() {
        // Round-robin tournament: Each entry plays the other entries twice (one red, one yellow)
        for(int i = 0; i < players.length; i++){
            for(int j = 0; j < players.length; j++){
                if(i == j) continue;

                // Play others
                // Player i will go first.
                // By the nature of the loop, we'll get to an i-j pairing again, but for that game, player j will go first.
                Game game = new Game(players[i], players[j]);
                int result = game.runTillCompletion();

                if(result == 1){
                    // player[i] won
                    scoreboard[i].wins++;
                    scoreboard[j].losses++;

                    // How was the game decided?
                    if(game.illegalMove) scoreboard[j].illegalMoves++;
                    if(game.exception) scoreboard[j].numExceptions++;
                    
                } else if(result == 0){
                    // Draw
                    scoreboard[i].draws++;
                    scoreboard[j].draws++;

                } else {
                    // player[j] won
                    scoreboard[i].losses++;
                    scoreboard[j].wins++;
                    
                    // How was the game decided?
                    if(game.illegalMove) scoreboard[i].illegalMoves++;
                    if(game.exception) scoreboard[i].numExceptions++;
                }
            }
        }

        // Sort scoreboard
        Arrays.sort(scoreboard, (obj1, obj2) -> {
            if(obj1.wins != obj2.wins) return obj2.wins - obj1.wins; // Primary: # of wins
            if(obj1.losses != obj2.losses) return obj1.losses - obj2.losses; // Minimize # of losses
            
            // Number of illegal moves / exceptions are tiebreaks
            return (obj1.illegalMoves + obj1.numExceptions) - (obj2.illegalMoves + obj2.numExceptions);
        });
    }

    /**
     * Record results in the file specified
     */
    public void recordResults(String file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            
            // name, wins, losses, draws, # of exceptions
            // comma-separated values
            for(int i = 0; i < scoreboard.length; i++){
                writer.println(scoreboard[i].toString());
            }

            writer.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }


    public class ScoreBoard {
        public String name;
        public int wins = 0;
        public int losses = 0;
        public int draws = 0;
        public int illegalMoves = 0;
        public int numExceptions = 0;

        public ScoreBoard(String name){
            this.name = name;
        }

        // Return comma-spliced values
        @Override
        public String toString(){
            return name + "," + wins + "," + losses + "," + draws + "," + illegalMoves + "," + numExceptions;
        }
    }
}
