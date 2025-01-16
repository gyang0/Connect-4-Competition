import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import swing_stuff.CustomButton;
import swing_stuff.CustomEvent;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * Home screen with options for the tournament
 * @author Gene Yang
 * @version Jan 16, 2025
 */
public class Home extends JPanel implements MouseListener {
    public BufferedImage backgroundImg;
    public BufferedImage connect4_title;

    private int WIDTH;
    private int HEIGHT;

    private Font pixelFont;

    // Buttons on this page
    private ArrayList<CustomButton> buttons;


    public Home(CardLayout cards, JPanel homeContainer, int WIDTH, int HEIGHT){
        // Add the mouse listener
        addMouseListener(this);

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        // Our buttons for this page
        this.buttons = new ArrayList<CustomButton>();
        CustomButton showCredits = new swing_stuff.CustomButton("Credits", WIDTH/2, 550, 150, 80, new CustomEvent(){
            @Override
            public void run(){
                cards.show(homeContainer, "Credits");
            }
        });
        CustomButton startBtn = new swing_stuff.CustomButton("Start", WIDTH/2, 650, 150, 80, new CustomEvent(){
            @Override
            public void run(){
                cards.show(homeContainer, "Results");
            }
        });

        buttons.add(showCredits);
        buttons.add(startBtn);

        // Get images
        try {
            backgroundImg = ImageIO.read(new File("assets/wood_background.png"));
            connect4_title = ImageIO.read(new File("assets/connect4_text.png"));

            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/toxigenesis.otf")).deriveFont(30f);
            GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
            g.registerFont(pixelFont);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, -(backgroundImg.getWidth() - WIDTH)/2, 0, null);

        g.drawImage(connect4_title, WIDTH/2 - connect4_title.getWidth()/2, 50, null);

        g.setColor(Color.BLACK);
        g.setFont(pixelFont);
        centerText(g, "Made by the OCS20 TAs", WIDTH/2, 250);
        centerText(g, "A round-robin style tournament", WIDTH/2, 350);
        centerText(g, "of Connect-4", WIDTH/2, 400);

        // Display buttons
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