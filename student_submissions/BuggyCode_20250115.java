package student_submissions;
import game_utils.Player;

public class BuggyCode_20250115 extends Player {
    public BuggyCode_20250115(){
        this.name = "Patrick Starfish";
    }

    // Patrick always raises exceptions
    @Override
    public int returnMove(char[][] board, char yourToken, char oppToken){
        return 6/0;
    }
}
