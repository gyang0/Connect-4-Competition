package game_utils;

import java.util.ArrayList;

public class Game {
    // Dimensions of Connect-4 board
    public final int ROWS = 6;
    public final int COLUMNS = 7;

    public final long MAX_TIME = 2000; // Maximum 2 seconds per move

    // Players
    Player player1;
    Player player2;

    private final char PLY1_TOKEN = '1';
    private final char PLY2_TOKEN = '2';
    private final char EMPTY_TOKEN = '-';
    
    // Board representation (6x7 char array)
    private char[][] board;

    // Times per move
    private ArrayList<Long> times_1;
    private ArrayList<Long> times_2;

    // Details
    // "exc" = game forfeited by exception
    // "ill" = game forfeited by illegal move
    // "tim" = game forfeited by too much time taken
    public String details = "";
    
    // Moves (what columns the players dropped in)
    public ArrayList<Integer> moves = new ArrayList<Integer>();

    public Game(Player player1, Player player2){
        // player1 goes first.
        // We represent player1 as '1' and player2 as '2' in the board (' ' for none occupied)
        this.player1 = player1;
        this.player2 = player2;

        this.times_1 = new ArrayList<Long>();
        this.times_2 = new ArrayList<Long>();

        board = new char[ROWS][COLUMNS];
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLUMNS; c++)
                board[r][c] = EMPTY_TOKEN;
        }
    }

    /**
     * Simulate running a game until it's over (draw or win/loss)
     * @return 1 if player1 won. 0 if draw. -1 if player1 lost.
     */
    public int runTillCompletion(){
        // player1 ('1') goes first, then player2 ('2')

        // bad code but it works
        this.player1.MY_TOKEN = PLY1_TOKEN;
        this.player1.OPP_TOKEN = PLY2_TOKEN;
        this.player1.EMPTY_TOKEN = EMPTY_TOKEN;
        
        this.player2.MY_TOKEN = PLY2_TOKEN;
        this.player2.OPP_TOKEN = PLY1_TOKEN;
        this.player2.EMPTY_TOKEN = EMPTY_TOKEN;

        while(anyMovesLeft()){
            // Get player1's move
            int choice1;
            long time1;
            try {
                this.player1.updateState(board, PLY1_TOKEN, PLY2_TOKEN, EMPTY_TOKEN);

                long cur = System.currentTimeMillis();
                choice1 = this.player1.returnMove();
                time1 = System.currentTimeMillis() - cur;

            } catch(Exception e){
                details = "exc";
                System.out.println(this.player2.getName() + " won by " + this.player1.getName() + "\'s exception");
                return -1;
            }

            // Illegal move attempt
            if(!canMakeMove(choice1)){
                details = "ill";
                System.out.println(this.player2.getName() + " won by " + this.player1.getName() + "\'s illegal move");
                System.out.println(" - wanted to move to " + choice1);
                return -1;
            }

            // Took too long
            if(time1 > MAX_TIME){
                details = "tim";
                System.out.println(this.player2.getName() + " won by " + this.player1.getName() + "\'s timeout");
                return -1;
            }

            // Check if player1 won
            playMove(choice1, PLY1_TOKEN);
            this.moves.add(choice1);
            this.times_1.add(time1);


            if(checkWin() == '1'){
                System.out.println(this.player1.getName() + " won");
                return 1;
            }

            if(!anyMovesLeft()){
                System.out.println("draw");
                return 0;
            }


            // Then player2 ('2') goes
            int choice2;
            long time2;
            try {
                this.player2.updateState(board, PLY2_TOKEN, PLY1_TOKEN, EMPTY_TOKEN);

                long cur = System.currentTimeMillis();
                choice2 = this.player2.returnMove();
                time2 = System.currentTimeMillis() - cur;

            } catch(Exception e){
                details = "exc";
                System.out.println(this.player1.getName() + " won by " + this.player2.getName() + "\'s exception");
                return 1;
            }
            
            // Illegal move attempt
            if(!canMakeMove(choice2)){
                details = "ill";
                System.out.println(this.player1.getName() + " won by " + this.player2.getName() + "\'s illegal move");
                return 1;
            }

            // Took too long
            if(time2 > MAX_TIME){
                details = "tim";
                System.out.println(this.player1.getName() + " won by " + this.player2.getName() + "\'s timeout");
                return 1;
            }

            // Check if player2 won
            playMove(choice2, PLY2_TOKEN);
            this.moves.add(choice2);
            this.times_2.add(time2);

            if(checkWin() == '2'){
                System.out.println(this.player2.getName() + " won");
                return -1;
            }
        }

        // Draw
        System.out.println("draw");
        return 0;
    }


    //-----------------------------------------
    // Utility methods for checking board states
    
    /**
     * Check if it's legal to drop a token in a given column
     * @param col - Column in which we want to make a move
     * @return - True if move is legal
     */
    private boolean canMakeMove(int col){
        if(col < 0 || col >= COLUMNS) return false;

        return (this.board[0][col] == EMPTY_TOKEN);
    }

    /**
     * Check whether any moves are left
     */
    private boolean anyMovesLeft(){
        for(int i = 0; i < this.COLUMNS; i++){
            if(this.canMakeMove(i))
                return true;
        }
        return false;
    }

    /**
     * Checks whether a player has gotten 4 tokens in a row
     * @return - character used to represent player that just won. If no one won, return '-' (empty space)
     */
    private char checkWin(){
        for(int r = 0; r < this.ROWS; r++){
            for(int c = 0; c < this.COLUMNS; c++){
                // Only check squares with tokens
                if(board[r][c] == EMPTY_TOKEN)
                    continue;
                
                // Horizontal
                if(c + 3 < this.COLUMNS &&
                   board[r][c] == board[r][c + 1] &&
                   board[r][c] == board[r][c + 2] &&
                   board[r][c] == board[r][c + 3])
                    return board[r][c];

                // Vertical
                if(r + 3 < this.ROWS &&
                   board[r][c] == board[r + 1][c] &&
                   board[r][c] == board[r + 2][c] &&
                   board[r][c] == board[r + 3][c])
                    return board[r][c];
            
                // (Diagonal) Left top to right bottom
                if(r + 3 < this.ROWS && c + 3 < this.COLUMNS &&
                   board[r][c] == board[r + 1][c + 1] &&
                   board[r][c] == board[r + 2][c + 2] &&
                   board[r][c] == board[r + 3][c + 3])
                    return board[r][c];
                
                // (Diagonal) Right top to left bottom
                if(r + 3 < this.ROWS && c - 3 >= 0 &&
                   board[r][c] == board[r + 1][c - 1] &&
                   board[r][c] == board[r + 2][c - 2] &&
                   board[r][c] == board[r + 3][c - 3])
                    return board[r][c];
            }
        }

        return EMPTY_TOKEN;
    }

    /**
     * Plays a move on the board, dropping the token into the specified column.
     * @param col - column in which to drop the token
     * @param ch - token representing player
     */
    private void playMove(int col, char ch){
        // Simulate dropping 'ch' in column 'col'
        for(int r = 0; r < this.ROWS; r++){
            if(this.board[r][col] != EMPTY_TOKEN){
                this.board[r - 1][col] = ch;
                return;
            }
        }

        this.board[this.ROWS - 1][col] = ch;
    }

    private void print(){
        for(int r = 0; r < this.ROWS; r++){
            for(int c = 0; c < this.COLUMNS; c++){
                System.out.print(this.board[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
