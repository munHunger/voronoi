package se.munhunger.painter.sketch;

import se.munhunger.painter.Painter;
import se.munhunger.painter.util.Generation;
import se.munhunger.painter.util.Validator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Marcus Münger
 */
public class StrokeGeneration implements Generation {
    private static final int GENERATION_SIZE = 50;
    private static final int INTERPOLATION_STEPS = 3;
    private static final int ELITISM = 10;
    private static final float MUTATION = 0.1f;
    private List<Stroke> generation = new ArrayList<>();

    private int genCount = 0;

    private BufferedImage image;

    private static Random random = new Random(0);

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void initialize() {
        for(int i = 0; i < GENERATION_SIZE; i++)
            generation.add(Stroke.generateRandom());
    }

    private BufferedImage generateImage(Stroke s, float stepSize) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics g = copy.getGraphics();
        g.drawImage(image, image.getWidth(), image.getHeight(), null);
        g.setColor(new Color(s.r, s.g, s.b, s.a));
        Point prev = s.stroke[0];
        for(int i = 0; i < INTERPOLATION_STEPS; i++) {
            float progress = stepSize*((float)i);
            Point a = s.stroke[0].getProgress(s.stroke[1], progress);
            Point b = s.stroke[1].getProgress(s.stroke[2], progress);
            Point c = a.getProgress(b, progress);
            g.drawLine((int)(prev.x * image.getWidth()), ((int)prev.y * image.getHeight()), ((int)c.x * image.getWidth()), ((int)c.y * image.getHeight()));
            prev = c;
        }
        return copy;
    }

    private void orderGeneration() {
        float stepSize = 1f/(float)INTERPOLATION_STEPS;
        for(Stroke s : generation)
        {
            s.fitness = Validator.calcSimilarity(generateImage(s, stepSize), Painter.originalImage);
        }
        generation.sort((s1, s2) -> Float.compare(s2.fitness, s1.fitness));
    }

    @Override
    public void step(int times) {
        for(int i = 0; i < times; i++) {
            List<Stroke> newGen = new ArrayList<>();
            orderGeneration();
            for(int n = 0; n < ELITISM; n++)
                newGen.add(Stroke.crossover(generation.get(0), generation.get(n+1)));

            for(int n = newGen.size(); n < GENERATION_SIZE; n++)
                newGen.add(Stroke.crossover(generation.get(random.nextInt(GENERATION_SIZE - 1)), generation.get(random.nextInt(GENERATION_SIZE - 1))));

            for(Stroke s : generation)
                s.mutate(MUTATION);

            genCount++;
        }
    }

    public Stroke getBest() {
        return generation.get(0);
    }

    @Override
    public BufferedImage getBest(int width, int height) {
        return generateImage(generation.get(0), 1f/(float)INTERPOLATION_STEPS);
    }

    @Override
    public int getGenerationCount() {
        return genCount;
    }
}
