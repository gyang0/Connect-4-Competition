package game_utils;
/**
 * The AI used as the opponent in Connect 4. Uses minimax search to find moves.
 *
 * @author Gene Yang
 * @version June 19, 2025
 */
public class Player {
    private char[][] board; // current board state
    private final int ROWS = 6;
    private final int COLS = 7;

    public char MY_TOKEN = ' ';
    public char OPP_TOKEN = ' ';
    public char EMPTY_TOKEN = ' ';
    
    public Player(){
        
    }

    public String getName(){
        return "";
    }

    // Method to be overriden
    public int returnMove(){
        return -1;
    }


    // You can disregard anything down here
    // ================================================================================
    // ================================================================================
    
    // Updates board after move
    public void updateState(char[][] board, char my_token, char opp_token, char empty_token){
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++)
                this.board[r][c] = board[r][c];
        }

        MY_TOKEN = my_token;
        OPP_TOKEN = opp_token;
        EMPTY_TOKEN = empty_token;
    }
    
    // For debugging
    public void printBoard(){
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++){
                System.out.print("[" + board[r][c] + "]");
            }
            System.out.println();
        }
    }
}