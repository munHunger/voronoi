package se.munhunger.painter.sketch;

import se.munhunger.painter.Painter;
import se.munhunger.painter.util.Generation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcus Münger
 */
public class SketchGeneration implements Generation {
    private List<Stroke> strokes = new ArrayList<>();
    private StrokeGeneration generation = new StrokeGeneration();
    private BufferedImage image = null;

    private int genCount = 0;

    @Override
    public void initialize() {
        image = new BufferedImage(Painter.originalImage.getWidth() / 1, Painter.originalImage.getHeight() / 1, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void step(int times) {
        for(int i = 0; i < times; i++) {
            BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            copy.getGraphics().drawImage(image, image.getWidth(), image.getHeight(), null);
            generation.setImage(copy);
            generation.initialize();
            generation.step(10);
            generation.getBest().drawStroke(image.getGraphics(), image.getWidth(), image.getHeight(), 100);
            strokes.add(generation.getBest().clone());
            genCount++;
        }
    }

    @Override
    public BufferedImage getBest(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(Stroke s : strokes)
            s.drawStroke(image.getGraphics(), width, height, 10);
        try {
            ImageIO.write(image, "jpg",
                          new File("C:\\Users\\mamun1\\Desktop\\paintResult\\" + getGenerationCount() + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public int getGenerationCount() {
        return genCount;
    }
}
