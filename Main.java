import javax.swing.*;

import java.awt.*;
import java.io.File;

/**
 * Runnable Main class that does the JFrame stuff
 *
 * @author Gene Yang
 * @version Nov 28 2024
 */
public class Main {
    public static int WIDTH = 800;
    public static int HEIGHT = 800;

    public void run() {
        JFrame window = new JFrame("Connect 4");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        // Card Layout
        CardLayout cards = new CardLayout(5, 5);
        JPanel homeContainer = new JPanel(cards);
        homeContainer.setBackground(Color.black);

        // JPanel screens
        Home home = new Home(cards, homeContainer, WIDTH, HEIGHT);
        Credits credits = new Credits(cards, homeContainer, WIDTH, HEIGHT);
        Results results = new Results(cards, homeContainer);

        // Add to main JPanel (homeContainer)
        homeContainer.add(home, "Home");
        homeContainer.add(credits, "Credits");
        homeContainer.add(results, "Results");

        window.add(homeContainer, BorderLayout.CENTER);
        cards.show(homeContainer, "Home");
        window.setVisible(true);
    }

    public static void main(String[] args) {
        new Main().run();
    }
}