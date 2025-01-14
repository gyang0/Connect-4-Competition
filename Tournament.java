import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JComponent;

import game_utils.Player;

public class Tournament extends JComponent {
    // Object representation of the i-th player
    ArrayList<Player> players;

    // wins, losses, and draws for the i-th player
    // scoreboard[i] = { wins for player[i], losses for player[i], draws for player[i] }
    ArrayList<Integer[]> scoreboard;

    // Constructor
    public Tournament(){
        // Player i is stored in index i
        players = new ArrayList<>();
        scoreboard = new ArrayList<>();

        // Get all the student submission files
        File[] submissions = new File("student_submissions").listFiles();
        try {
            for(int i = 0; i < submissions.length; i++){
                // Strip .java file extension
                String name = submissions[i].getName();
                name = name.substring(0, name.length() - 5);
                
                // Oh my lord why did this take so long
                players.add((Player) Class.forName("student_submissions." + name).getDeclaredConstructor().newInstance());
                scoreboard.add(new Integer[]{0, 0, 0});

                System.out.println(players.get(i).name);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Everything meant to be painted on the window should go here.
     */
    @Override
    public void paintComponent(Graphics g) {
        // Round-robin tournament: Each entry plays the other entries twice (one red, one yellow)
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < players.size(); j++){
                if(i == j) continue;

                // Play others
                // Player i will go first.
                // By the nature of the loop, we'll get to an i-j pairing again, but for that game, player j will go first.
                game_utils.Game game = new game_utils.Game(players.get(i), players.get(j));
                int result = game.runTillCompletion();

                System.out.print(players.get(i).name + " v. " + players.get(j).name + ": ");
                if(result == 1){
                    System.out.println(players.get(i).name + " won");
                    scoreboard.get(i)[0]++;
                    scoreboard.get(j)[1]++;
                    
                } else if(result == 0){
                    System.out.println("draw");
                    scoreboard.get(i)[2]++;
                    scoreboard.get(j)[2]++;

                } else {
                    System.out.println(players.get(j).name + " won");
                    scoreboard.get(i)[1]++;
                    scoreboard.get(j)[0]++;
                }
            }
        }

        System.out.println();
        for(int i = 0; i < scoreboard.size(); i++){
            System.out.println(players.get(i).name + ": +" + scoreboard.get(i)[0] + "-" + scoreboard.get(i)[1] + "=" + scoreboard.get(i)[2]);
        }
        
        // 3. Winners displayed
    }
}
