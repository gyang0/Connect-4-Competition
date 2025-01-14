import javax.swing.*;
import java.awt.*;

/**
 * Runnable Main class that does the JFrame stuff
 *
 * @author Gene Yang
 * @version Nov 28 2024
 */
public class Main {
    public static int WIDTH = 600;
    public static int HEIGHT = 600;
    private Tournament tournament;

    public void run() {
        JFrame window = new JFrame("Connect 4");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        tournament = new Tournament();
        window.add(tournament, BorderLayout.CENTER);

        window.setVisible(true);
    }

    public static void main(String[] args) {
        new Main().run();
    }
}