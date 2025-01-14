package student_submissions;
import game_utils.Player;

public class Curly_20250113 extends Player {
    public Curly_20250113(){
        this.name = "Curly";
    }

    // Curly plays on a random column
    @Override
    public int returnMove(char[][] board, char yourToken, char oppToken){
        return (int)(Math.random() * 6);
    }
}
