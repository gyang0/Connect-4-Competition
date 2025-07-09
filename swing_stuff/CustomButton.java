package swing_stuff;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * A pseudo-button with better styling but no click functionality
 * Clicks must be implemented in parent JPanel
 * @author Gene Yang
 * @version 1/16/2025
 */
public class CustomButton extends JComponent {
    private String text;
    private int x;
    private int y;
    private int w;
    private int h;
    private CustomEvent evt;
    
    public CustomButton(String text, int x, int y, int w, int h, CustomEvent evt){
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.evt = evt;
    }

    public void paint(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(50, 50, 50));
        g.fillRoundRect(this.x - this.w/2 - 4, this.y - this.h/2 - 4, this.w + 8, this.h + 8, 5, 5);
        
        g.setColor(new Color(135, 74, 4));
        g.fillRoundRect(this.x - this.w/2, this.y - this.h/2, this.w, this.h, 5, 5);


        g.setColor(Color.BLACK);
        centerText(g, this.text, this.x, this.y);
    }

    public void run(){
        this.evt.run();
    }

    /**
     * Center a piece of text on the screen
     * @param g - Graphics obj.
     */
    public void centerText(Graphics g, String str, int x, int y){
        int width = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
        int height = (int) g.getFontMetrics().getStringBounds(str, g).getHeight();

        g.drawString(str, x - width/2, y + height/4);
    }

    // Check if mouse is inside button
    public boolean collided(int mouseX, int mouseY){
        return (
            mouseX > this.x - this.w/2 && mouseX < this.x + this.w/2 &&
            mouseY > this.y - this.h/2 && mouseY < this.y + this.h/2
        );
    }
}
