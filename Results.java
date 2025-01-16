import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

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

    private int WIDTH;
    private int HEIGHT;

    private Font pixelFont_big;

    // Buttons on this page
    private ArrayList<CustomButton> buttons;
    
    // Results to display
    private ArrayList<String> results = new ArrayList<String>();

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

        // Our buttons for this page
        this.buttons = new ArrayList<CustomButton>();
        CustomButton awardsBtn = new swing_stuff.CustomButton("Awards", WIDTH/2, 650, 150, 80, new CustomEvent(){
            @Override
            public void run(){
                // Change page
                cards.show(homeContainer, "Awards");
            }
        });

        buttons.add(awardsBtn);

        // Get images
        try {
            backgroundImg = ImageIO.read(new File("assets/wood_background.png"));
            connect4_title = ImageIO.read(new File("assets/connect4_text.png"));

            pixelFont_big = Font.createFont(Font.TRUETYPE_FONT, new File("assets/toxigenesis.otf")).deriveFont(30f);
            
            GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
            
            g.registerFont(pixelFont_big);

        } catch(Exception e){
            e.printStackTrace();
        }

        // Read results.txt for results
        try {
            File file = new File("results.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                List<String> data = Arrays.asList(str.split(","));

                // NAME (+wins-losses=draws, ERR=num_errors)
                results.add(data.get(0) + " (+" + data.get(1) + "-" + data.get(2) + "=" + data.get(3) + ", " + (Integer.valueOf(data.get(4)) + Integer.valueOf(data.get(5))) + " forfeits)");
            }
            
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Paint all the stuff on the screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, -(backgroundImg.getWidth() - WIDTH)/2, 0, null);
        g.drawImage(connect4_title, WIDTH/2 - connect4_title.getWidth()/2, 50, null);

        g.setColor(Color.BLACK);
        g.setFont(pixelFont_big);

        for(int i = 0; i < Math.min(5, results.size()); i++){
            centerText(g, (i + 1) + ". " + results.get(i), WIDTH/2, 250 + i*70);
        }

        // Display buttons
        g.setFont(pixelFont_big);
        for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).paint(g);
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