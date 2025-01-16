package student_submissions;
import game_utils.Player;

public class BuggyCode_20250116 extends Player {
    public BuggyCode_20250116(){
        this.name = "Bugs Bunny";
    }

    // Bugs always makes illegal moves
    @Override
    public int returnMove(char[][] board, char yourToken, char oppToken){
        return -1;
    }
}
