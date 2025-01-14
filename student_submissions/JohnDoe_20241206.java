package student_submissions;
import game_utils.Player;

public class JohnDoe_20241206 extends Player {
    public JohnDoe_20241206(){
        this.name = "John Doe";
    }

    // John only plays on the 0-th column
    @Override
    public int returnMove(char[][] board, char yourToken, char oppToken){
        return 0;
    }
}
