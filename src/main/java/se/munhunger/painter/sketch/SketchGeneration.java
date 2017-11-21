package se.munhunger.painter.sketch;

import se.munhunger.painter.Painter;
import se.munhunger.painter.util.Generation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcus Münger
 */
public class SketchGeneration implements Generation {
    private List<Stroke> strokes = new ArrayList<>();
    private StrokeGeneration generation = new StrokeGeneration();
    private BufferedImage image = new BufferedImage(Painter.image.getWidth() / 10, Painter.image.getHeight() / 10, BufferedImage.TYPE_INT_ARGB);

    private int genCount = 0;

    @Override
    public void initialize() {
        generation.initialize();
    }

    @Override
    public void step(int times) {
        generation.setImage(image);
        for(int i = 0; i < times; i++) {
            generation.step(100);
            generation.getBest().drawStroke(image.getGraphics(), image.getWidth(), image.getHeight(), 5);
            strokes.add(generation.getBest());
            genCount++;
        }
    }

    @Override
    public BufferedImage getBest(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(Stroke s : strokes)
            s.drawStroke(image.getGraphics(), width, height, 10);
        return null;
    }

    @Override
    public int getGenerationCount() {
        return genCount;
    }
}
