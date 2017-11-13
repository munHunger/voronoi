package se.munhunger.voronoi;

import javax.swing.*;
import java.awt.*;

/**
 * @author Marcus Münger
 */
public class Voronoi {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Voronoi painter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        frame.add(panel);

        JPanel images = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                super.setBackground(Color.PINK);
            }
        };

        panel.add(images);

        frame.setVisible(true);
    }
}
