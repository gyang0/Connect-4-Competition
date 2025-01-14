package student_submissions;
import game_utils.Player;

public class JaneDoe_20250108 extends Player {
    public JaneDoe_20250108(){
        this.name = "Jane Doe";
    }

    // Jane plays on a random column
    @Override
    public int returnMove(char[][] board, char yourToken, char oppToken){
        return (int)(Math.random() * 6);
    }
}