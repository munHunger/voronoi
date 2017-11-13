package se.munhunger.voronoi;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Marcus Münger
 */
public class Voronoi {
    public static BufferedImage image;
    public static BufferedImage paintedImage;

    public static void main(String[] args) throws IOException {
        image = ImageIO.read(Voronoi.class.getClassLoader().getResource("ref1.jpg"));
        paintedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        JFrame frame = new JFrame("Voronoi painter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        frame.add(panel);

        JPanel images = new JPanel();
        images.setLayout(new BoxLayout(images, BoxLayout.LINE_AXIS));
        JLabel ref = new JLabel(new ImageIcon(image));
        JLabel result = new JLabel(new ImageIcon(paintedImage));
        images.add(result);
        images.add(ref);

        panel.add(images);

        frame.pack();
        frame.setVisible(true);
    }
}
