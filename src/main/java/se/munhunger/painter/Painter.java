package se.munhunger.painter;

import se.munhunger.painter.util.Generation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static se.munhunger.painter.util.Validator.calcSimilarity;

/**
 * @author Marcus Münger
 */
public class Painter {
    public static BufferedImage image;
    public static BufferedImage originalImage;
    public static BufferedImage paintedImage;

    private static JLabel ref;
    private static JLabel result;
    private static JLabel similarity;

    public static void init(String imagePath, Generation generation) throws IOException {
        originalImage = ImageIO.read(Painter.class.getClassLoader().getResource(imagePath));
        image = new BufferedImage(originalImage.getWidth() / 4, originalImage.getHeight() / 4, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(originalImage, 0, 0, image.getWidth(), image.getHeight(), null);
        generation.initialize();
        generation.step(1);

        paintedImage = generation.getBest(originalImage.getWidth(), originalImage.getHeight());
        JFrame frame = new JFrame("Voronoi painter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        frame.add(panel);

        JPanel images = new JPanel();
        images.setLayout(new BoxLayout(images, BoxLayout.LINE_AXIS));
        ref = new JLabel(new ImageIcon(originalImage));
        result = new JLabel(new ImageIcon(paintedImage));
        images.add(result);
        images.add(ref);

        panel.add(images);

        similarity = new JLabel(String.format("Similarity: %d%%", (int)(calcSimilarity(originalImage, paintedImage) * 100)));
        panel.add(similarity);
        new Thread(() -> {
            while(true) {
                generation.step(10);

                paintedImage = generation.getBest(originalImage.getWidth(), originalImage.getHeight());

                EventQueue.invokeLater(() ->
                                       {
                                           ref.setIcon(new ImageIcon(originalImage));
                                           result.setIcon(new ImageIcon(paintedImage));
                                           similarity.setText(String.format("Similarity: %d%% \tGenCount: %d",
                                                                            (int) (calcSimilarity(originalImage,
                                                                                                  paintedImage) * 100), generation.getGenerationCount()));
                                           frame.pack();
                                       });
            }
        }).start();

        frame.pack();
        frame.setVisible(true);
    }
}
