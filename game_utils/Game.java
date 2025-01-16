package game_utils;

public class Game {
    // Dimensions of Connect-4 board
    public final int ROWS = 6;
    public final int COLUMNS = 7;

    // Players
    Player player1;
    Player player2;
    
    // Board representation (6x7 char array)
    private char[][] board;

    // Details
    public boolean exception = false; // Game forfeited by exception?
    public boolean illegalMove = false; // Game forfeited by illegal move?

    public Game(Player player1, Player player2){
        // player1 goes first.
        // We represent player1 as '1' and player2 as '2' in the board (' ' for none occupied)
        this.player1 = player1;
        this.player2 = player2;

        board = new char[ROWS][COLUMNS];
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLUMNS; c++)
                board[r][c] = '-';
        }
    }

    /**
     * Simulate running a game until it's over (draw or win/loss)
     * @return 1 if player1 won. 0 if draw. -1 if player1 lost.
     */
    public int runTillCompletion(){
        // player1 ('1') goes first, then player2 ('2')
        while(anyMovesLeft()){
            // Get player1's move
            int choice1;
            try {
                choice1 = this.player1.returnMove(board, '1', '2');
            } catch(Exception e){
                exception = true;
                return -1;
            }

            // Illegal move attempt
            if(!canMakeMove(choice1)){
                illegalMove = true;
                return -1;
            }

            // Check if player1 won
            playMove(choice1, '1');
            if(checkWin() == '1'){
                return 1;
            }


            // Then player2 ('2') goes
            int choice2;
            try {
                choice2 = this.player2.returnMove(board, '2', '1');
            } catch(Exception e){
                exception = true;
                return 1;
            }
            
            // Illegal move attempt
            if(!canMakeMove(choice2)){
                illegalMove = true;
                return 1;
            }

            // Check if player2 won
            playMove(choice2, '2');
            if(checkWin() == '2'){
                return -1;
            }
        }

        // Draw
        System.out.println("'Twas but a draw.");
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

        return (this.board[0][col] == '-');
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
                if(board[r][c] == '-')
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

        return '-';
    }

    /**
     * Plays a move on the board, dropping the token into the specified column.
     * @param col - column in which to drop the token
     * @param ch - token representing player
     */
    private void playMove(int col, char ch){
        // Simulate dropping 'ch' in column 'col'
        for(int r = 0; r < this.ROWS; r++){
            if(this.board[r][col] != '-'){
                this.board[r - 1][col] = ch;
                return;
            }
        }

        this.board[this.ROWS - 1][col] = ch;
    }

    /*private void print(){
        for(int r = 0; r < this.ROWS; r++){
            for(int c = 0; c < this.COLUMNS; c++){
                System.out.print(this.board[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }*/
}
