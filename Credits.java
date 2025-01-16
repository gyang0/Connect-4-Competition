import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

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
public class Credits extends JPanel implements MouseListener {
    public BufferedImage backgroundImg;
    public BufferedImage connect4_title;

    private int WIDTH;
    private int HEIGHT;

    private Font pixelFont_big;
    private Font pixelFont_small;

    // Buttons on this page
    private ArrayList<CustomButton> buttons;

    /**
     * Constructor
     * @param cards - Parent CardLayout
     * @param homeContainer - Parent JPanel
     * @param WIDTH - Parent width
     * @param HEIGHT - Parent height
     */
    public Credits(CardLayout cards, JPanel homeContainer, int WIDTH, int HEIGHT){
        // Add the mouse listener
        addMouseListener(this);

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        // Our buttons for this page
        this.buttons = new ArrayList<CustomButton>();
        CustomButton backBtn = new swing_stuff.CustomButton("Back", WIDTH/2, 650, 150, 80, new CustomEvent(){
            @Override
            public void run(){
                cards.show(homeContainer, "Home");
            }
        });

        buttons.add(backBtn);

        // Get images
        try {
            backgroundImg = ImageIO.read(new File("assets/wood_background.png"));
            connect4_title = ImageIO.read(new File("assets/connect4_text.png"));

            pixelFont_small = Font.createFont(Font.TRUETYPE_FONT, new File("assets/toxigenesis.otf")).deriveFont(20f);
            pixelFont_big = Font.createFont(Font.TRUETYPE_FONT, new File("assets/toxigenesis.otf")).deriveFont(30f);
            
            GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
            
            g.registerFont(pixelFont_big);
            g.registerFont(pixelFont_small);

        } catch(Exception e){
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
        g.setFont(pixelFont_small);
        centerText(g, "Oct. 2024 - Jan. 2025", WIDTH/2, 250);
        centerText(g, "Gene Yang", WIDTH/2, 280);
        centerText(g, "Shiv Sitaram", WIDTH/2, 310);
        centerText(g, "Maxwell Palance", WIDTH/2, 340);
        centerText(g, "Aanya Gupta", WIDTH/2, 370);

        centerText(g, "Toxigenesis font from www.1001fonts.com", WIDTH/2, 430);
        centerText(g, "Wood background from www.craiyon.com", WIDTH/2, 460);

        centerText(g, "Lots & lots of coffee + Stack Overflow", WIDTH/2, 520);

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