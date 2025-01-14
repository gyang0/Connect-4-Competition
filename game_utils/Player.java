package game_utils;
/**
 * The AI used as the opponent in Connect 4. Uses minimax search to find moves.
 *
 * @author Gene Yang
 * @version Jan 14, 2025
 */
public class Player {
    public String name = "";

    public Player(){}

    // Method to be overriden
    public int returnMove(char[][] board, char yourToken, char oppToken){
        return -1;
    }
}