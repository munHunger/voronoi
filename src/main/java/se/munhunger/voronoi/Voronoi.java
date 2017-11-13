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

    private static JLabel ref;
    private static JLabel result;
    private static JLabel similarity;

    private static Generation generation;

    public static void main(String[] args) throws IOException {
        BufferedImage original = ImageIO.read(Voronoi.class.getClassLoader().getResource("ref1.jpg"));
        image = new BufferedImage(original.getWidth() / 40, original.getHeight() / 40, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(original, 0, 0, image.getWidth(), image.getHeight(), null);
        generation = new Generation();
        generation.initialize();
        generation.step(100);
        Picture best = generation.getBest();

        image = original;
        paintedImage = best.toImage(image.getWidth(), image.getHeight());
        JFrame frame = new JFrame("Voronoi painter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        frame.add(panel);

        JPanel images = new JPanel();
        images.setLayout(new BoxLayout(images, BoxLayout.LINE_AXIS));
        ref = new JLabel(new ImageIcon(image));
        result = new JLabel(new ImageIcon(paintedImage));
        images.add(result);
        images.add(ref);

        panel.add(images);

        similarity = new JLabel(String.format("Similarity: %d%%", (int)(calcSimilarity(image, paintedImage) * 100)));
        panel.add(similarity);

        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(e -> {
            for(int i = 0; i < 100; i++) {
                BufferedImage org = image;
                image = new BufferedImage(original.getWidth() / 40, original.getHeight() / 40,
                                          BufferedImage.TYPE_INT_RGB);
                image.getGraphics().drawImage(original, 0, 0, image.getWidth(), image.getHeight(), null);
                generation.step(100);

                Picture bestInGen = generation.getBest();

                image = org;
                paintedImage = bestInGen.toImage(image.getWidth(), image.getHeight());

                EventQueue.invokeLater(() ->
                                       {
                                           ref.setIcon(new ImageIcon(image));
                                           result.setIcon(new ImageIcon(paintedImage));
                                           similarity.setText(String.format("Similarity and stuff: %d%%",
                                                                            (int) (calcSimilarity(image,
                                                                                                  paintedImage) * 100)));

                                       });
            }
        });
        panel.add(stepButton);

        frame.pack();
        frame.setVisible(true);
    }

    public static float calcSimilarity(BufferedImage image1, BufferedImage image2) {
        if(image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight())
            throw new IllegalArgumentException("The images must be the same size");
        float sum = 0;
        for(int x = 0; x < image1.getWidth(); x++) {
            for(int y = 0; y < image1.getHeight(); y++) {
                Color c1 = new Color(image1.getRGB(x,y));
                Color c2 = new Color(image2.getRGB(x,y));
                float error = (Math.abs(c1.getRed() - c2.getRed()) + Math.abs(c1.getGreen() - c2.getGreen()) + Math.abs(c1.getBlue() - c2.getBlue())) / 255f / 3f;
                sum += error;
            }
        }
        return sum / (image1.getWidth() * image1.getHeight());
    }
}
