import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import game_utils.Game;
import game_utils.Player;
import swing_stuff.CustomButton;
import swing_stuff.CustomEvent;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * Credits screen with list of resources
 * @author Gene Yang
 * @version Jan 16, 2025
 */
public class Results extends JPanel implements MouseListener {
    public BufferedImage backgroundImg;
    public BufferedImage connect4_title;
    public BufferedImage confetti_img;

    private int WIDTH;
    private int HEIGHT;

    private Font pixelFont_small;
    private Font pixelFont_big;

    // Buttons on this page
    private ArrayList<CustomButton> buttons;

    // Object representation of the i-th player
    Player[] players;
    Personal_Results[] personal_results;


    // wins, losses, and draws for the i-th player
    ArrayList<ScoreBoard> scoreboard;

    // Running games?
    private boolean running = false;

    // All done?
    private boolean allDone = false;

    // Simulated board state graphics
    private int SQUARE_SIZE;
    private int ROWS = 6;
    private int COLS = 7;
    private char PLY1_CHAR = '1';
    private char PLY2_CHAR = '2';

    public char board[][];

    private boolean debug = false;

    /**
     * Constructor
     * @param cards - Parent CardLayout
     * @param homeContainer - Parent JPanel
     * @param WIDTH - Parent width
     * @param HEIGHT - Parent height
     */
    public Results(CardLayout cards, JPanel homeContainer, int WIDTH, int HEIGHT){
        // Add the mouse listener
        addMouseListener(this);

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.SQUARE_SIZE = Math.max(3, Math.min(WIDTH/(COLS + 2), HEIGHT/(ROWS + 2)));

        this.board = new char[ROWS][COLS];
        clearBoard();

        this.scoreboard = new ArrayList<ScoreBoard>();


        // Our buttons for this page
        this.buttons = new ArrayList<CustomButton>();

        // Get images
        try {
            backgroundImg = ImageIO.read(new File("assets/wood_background.png"));
            connect4_title = ImageIO.read(new File("assets/connect4_text.png"));
            confetti_img = ImageIO.read(new File("assets/confetti.png"));

            pixelFont_small = Font.createFont(Font.TRUETYPE_FONT, new File("assets/toxigenesis.otf")).deriveFont(20f);
            pixelFont_big = Font.createFont(Font.TRUETYPE_FONT, new File("assets/toxigenesis.otf")).deriveFont(30f);
            
            GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
            
            g.registerFont(pixelFont_big);

        } catch(Exception e){
            e.printStackTrace();
        }

        // Get all the student submission files
        File[] submissions = new File("student_submissions").listFiles();

        // Shuffle submissions
        Collections.shuffle(Arrays.asList(submissions));

        // Player i is stored in index i
        players = new Player[submissions.length];
        personal_results = new Personal_Results[submissions.length];

        try {
            for(int i = 0; i < submissions.length; i++){
                // Strip .java file extension
                String name = submissions[i].getName();
                name = name.substring(0, name.length() - 5);
                
                // Oh my lord why did this take so long
                players[i] = (Player) Class.forName("student_submissions." + name).getDeclaredConstructor().newInstance();
                personal_results[i] = new Personal_Results(players[i].getName());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        CustomButton launchBtn = new swing_stuff.CustomButton("Light this Candle!", WIDTH/2, 350, 350, 80, new CustomEvent(){
            @Override
            public void run(){
                running = true;
                System.out.println("Tournament started.");
                runTournament();
                repaint();
                
            }
        });

        buttons.add(launchBtn);
    }

    public void runTournament(){
        // Round-robin tournament: Each entry plays the other entries twice (one red, one yellow)
        for(int i = 0; i < players.length; i++){
            for(int j = 0; j < players.length; j++){
                if(i == j) continue;

                // Play others
                // Player i will go first.
                // By the nature of the loop, we'll get to an i-j pairing again, but for that game, player j will go first.
                Game game = new Game(players[i], players[j]);
                int result = game.runTillCompletion();

                //System.out.println(players[i].getName() + " vs. " + players[j].getName() + " | " + result + " | " + game.details);

                scoreboard.add(new ScoreBoard(i, j, game.moves, result, game.details));
            }
        }
    }

    public void clearBoard(){
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++)
                board[r][c] = ' ';
        }
    }

    public void dropToken(char c, int col){
        int toDrop = 0;
        for(int r = 1; r < ROWS; r++){
            if(board[r][col] != ' ') break;
            else toDrop++;
        }

        board[toDrop][col] = c;
    }

    /**
     * Pauses for a specified amount of milliseconds, used for animation.
     * @param ms The milliseconds to pause
     */
    public void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 1. Get list of games - along with game data
     * 2. Play through each game, keep track of index
     * 3. When all done, display data.
     */
    int game = 0; // game iteration
    int k = 0; // move iteration (within game)
    
    /**
     * Paint all the stuff on the screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, -(backgroundImg.getWidth() - WIDTH)/2, 0, null);
        g.setFont(pixelFont_big);

        if(allDone){
            g.drawImage(connect4_title, WIDTH/2 - connect4_title.getWidth()/2, 50, null);
            g.drawImage(confetti_img, WIDTH/2 - confetti_img.getWidth()/2, 0, null);

            Arrays.sort(personal_results, (obj1, obj2) -> {
                if(obj1.wins != obj2.wins) return obj2.wins - obj1.wins; // primarily wins
                else if(obj1.losses != obj2.losses) return obj1.losses - obj2.losses; // then losses
                else if(obj1.numExceptions + obj1.numTimeExceeded + obj1.illegalMoves != 
                        obj2.numExceptions + obj2.numTimeExceeded + obj2.illegalMoves)
                    return (obj1.numExceptions + obj1.numTimeExceeded + obj1.illegalMoves) - 
                            (obj2.numExceptions + obj2.numTimeExceeded + obj2.illegalMoves); // Then by illegal moves
                
                else return 0;
            });

            g.setColor(Color.BLACK);
            for(int i = 0; i < Math.min(5, personal_results.length); i++){
                g.setFont(pixelFont_big);
                centerText(g, (i+1) + ". " + personal_results[i].name, WIDTH/2, 250 + 100*i);
                
                g.setFont(pixelFont_small);
                centerText(g, personal_results[i].toString(), WIDTH/2, 250 + 100*i + 30);
            }
            
            return;
        }

        if(running){
            g.setColor(Color.BLACK);

            // Display result (slightly in advance)
            if(k >= scoreboard.get(game).moves.size() - 2){
                g.setFont(pixelFont_big);

                if(scoreboard.get(game).result == 1){
                    g.setColor(Color.RED);
                    centerText(g, personal_results[scoreboard.get(game).player1].name + " won", WIDTH/2, 200);
                }
                else if(scoreboard.get(game).result == -1){
                    g.setColor(Color.YELLOW);
                    centerText(g, personal_results[scoreboard.get(game).player2].name + " won", WIDTH/2, 200);

                }
                else {
                    g.setColor(Color.GRAY);
                    centerText(g, "Draw", WIDTH/2, 200);
                }
            }
            
            if(k >= scoreboard.get(game).moves.size()){
                // current game over
                ScoreBoard score_game = scoreboard.get(game);

                if(score_game.result == 1){
                    if(score_game.details.equals("exc")) personal_results[score_game.player2].numExceptions++;
                    else if(score_game.details.equals("ill")) personal_results[score_game.player2].illegalMoves++;
                    else if(score_game.details.equals("tim")) personal_results[score_game.player2].numTimeExceeded++;
                    
                    personal_results[score_game.player2].losses++;
                    personal_results[score_game.player1].wins++;

                } else if(score_game.result == -1){
                    if(score_game.details.equals("exc")) personal_results[score_game.player1].numExceptions++;
                    else if(score_game.details.equals("ill")) personal_results[score_game.player1].illegalMoves++;
                    else if(score_game.details.equals("tim")) personal_results[score_game.player1].numTimeExceeded++;
                    
                    personal_results[score_game.player1].losses++;
                    personal_results[score_game.player2].wins++;
                    
                } else {
                    // draw
                    personal_results[score_game.player2].draws++;
                    personal_results[score_game.player1].draws++;
                }

                game++;
                k = 0;

                if(!debug) pause(1000);

                if(game >= scoreboard.size()){
                    allDone = true;
                } else {
                    clearBoard();
                }
                
            } else {
                dropToken(k%2==0 ? PLY1_CHAR : PLY2_CHAR, scoreboard.get(game).moves.get(k));
                k++;
            }


            
            // Display the board here.
            for(int r = 0; r < ROWS; r++){
                for(int c = 0; c < COLS; c++){
                    if(board[r][c] == PLY1_CHAR) g.setColor(Color.RED);
                    else if(board[r][c] == PLY2_CHAR) g.setColor(Color.YELLOW);
                    else g.setColor(Color.GRAY);

                    g.fillOval(SQUARE_SIZE + c*SQUARE_SIZE, 250 + r*SQUARE_SIZE, SQUARE_SIZE - 2, SQUARE_SIZE - 2);
                }
            }

            g.setColor(Color.BLACK);
            if(game < scoreboard.size())
               centerText(g, players[scoreboard.get(game).player1].getName() + " vs. " + players[scoreboard.get(game).player2].getName(), WIDTH/2, 100);

            // Animation
            if(!debug) pause(50);
            repaint();

        } else {
            g.drawImage(backgroundImg, -(backgroundImg.getWidth() - WIDTH)/2, 0, null);
            g.drawImage(connect4_title, WIDTH/2 - connect4_title.getWidth()/2, 50, null);

            centerText(g, "This may take a minute or two.", WIDTH/2, HEIGHT/2 + 100);

            // Display buttons
            g.setFont(pixelFont_big);
            for(int i = 0; i < buttons.size(); i++){
                buttons.get(i).paint(g);
            }
        }
    }

    /**
     * Center a piece of text on the screen
     * @param g - Graphics obj.
     */
    public void centerText(Graphics g, String str, int x, int y){
        int width = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
        g.drawString(str, x - width/2, y);
    }

    // Each individual game
    public class ScoreBoard {
        public int player1;
        public int player2;
        public ArrayList<Integer> moves;
        public int result;
        public String details;

        public ScoreBoard(int player1, int player2, ArrayList<Integer> moves, int result, String details){
            this.player1 = player1;
            this.player2 = player2;
            this.moves = moves;
            this.result = result;
            this.details = details;
        }
    }

    // Statistics for each player
    public class Personal_Results {
        public String name = "";
        public int wins = 0;
        public int draws = 0;
        public int losses = 0;
        public int illegalMoves = 0;
        public int numExceptions = 0;
        public int numTimeExceeded = 0;

        public Personal_Results(String name){
            this.name = name;
        }

        public Personal_Results(String name, int wins, int draws, int losses, int illMoves, int numExc, int numTime){
            this.name = name;
            this.wins = wins;
            this.draws = draws;
            this.losses = losses;
            this.illegalMoves = illMoves;
            this.numExceptions = numExc;
            this.numTimeExceeded = numTime;
        }

        @Override
        public String toString(){
            return this.wins + " wins " + this.draws + " draws " + this.losses + " losses | " + (this.numExceptions + this.numTimeExceeded + this.illegalMoves) + " forfeits";
        }
    }



    /**
     * Handle mouse clicks (for buttons)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        for(int i = 0; i < buttons.size(); i++){
            if(buttons.get(i).collided(e.getX(), e.getY())){
                buttons.get(i).run();
            }
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}