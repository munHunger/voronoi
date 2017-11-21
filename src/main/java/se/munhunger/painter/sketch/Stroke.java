package se.munhunger.painter.sketch;

import se.munhunger.painter.util.Mutator;
import se.munhunger.painter.voronoi.Picture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Marcus Münger
 */
public class Stroke implements Comparable<Stroke> {
    private static Random random = new Random(0);
    public static final int STROKE_POINTS = 3;
    public Point[] stroke = new Point[STROKE_POINTS];
    public float r;
    public float g;
    public float b;
    public float a;
    public float size;

    public float fitness = -1f;

    public Stroke clone() {
        Stroke clone = new Stroke();
        for(int i = 0; i < STROKE_POINTS; i++)
            clone.stroke[i] = stroke[i].clone();
        clone.r = r;
        clone.g = g;
        clone.b = b;
        clone.a = a;
        clone.size = size;
        return clone;
    }

    public static Stroke generateRandom() {
        Stroke newStroke = new Stroke();
        for(int i = 0; i < STROKE_POINTS; i++)
            newStroke.stroke[i] = Point.randomPoint();
        newStroke.r = random.nextFloat();
        newStroke.g = random.nextFloat();
        newStroke.b = random.nextFloat();
        newStroke.a = random.nextFloat();
        newStroke.size = random.nextFloat();
        return newStroke;
    }

    public static Stroke crossover(Stroke s1, Stroke s2) {
        Stroke newStroke = new Stroke();
        for(int i = 0; i < STROKE_POINTS; i++)
            newStroke.stroke[i] = s1.stroke[i].crossover(s2.stroke[i]);
        newStroke.r = random.nextBoolean() ? s1.r : s2.r;
        newStroke.g = random.nextBoolean() ? s1.g : s2.g;
        newStroke.b = random.nextBoolean() ? s1.b : s2.b;
        newStroke.a = random.nextBoolean() ? s1.a : s2.a;
        newStroke.size = random.nextBoolean() ? s1.size : s2.size;
        return newStroke;
    }

    public void mutate(float limit) {
        for(int i = 0; i < STROKE_POINTS; i++)
            stroke[i].mutate(limit);
        r = Mutator.mutateFloat(r, limit);
        g = Mutator.mutateFloat(g, limit);
        b = Mutator.mutateFloat(b, limit);
        a = Mutator.mutateFloat(a, limit);
        size = Mutator.mutateFloat(size, limit);
    }

    public void drawStroke(Graphics g, int width, int height, int steps) {
        float stepSize = 1f/(float)steps;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(width/10 * size));
        g2d.setColor(new Color(r, this.g, b, a));
        Point prev = stroke[0];
        for(int i = 0; i < steps; i++) {
            float progress = stepSize*((float)i);
            Point a = stroke[0].getProgress(stroke[1], progress);
            Point b = stroke[1].getProgress(stroke[2], progress);
            Point c = a.getProgress(b, progress);
            g2d.drawLine((int)(prev.x * width), (int)(prev.y * height), (int)(c.x * width), (int)(c.y * height));
            prev = c;
        }
    }

    @Override
    public int compareTo(Stroke o) {
        return Float.compare(fitness, o.fitness);
    }
}
