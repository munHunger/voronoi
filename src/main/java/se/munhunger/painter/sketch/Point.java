package se.munhunger.painter.sketch;

import se.munhunger.painter.util.Mutator;

import java.util.Random;

/**
 * @author Marcus Münger
 */
public class Point {
    private static Random random = new Random(0);
    public float x;
    public float y;

    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public static Point randomPoint() {
        return new Point(random.nextFloat(), random.nextFloat());
    }

    public Point crossover(Point other) {
        return new Point(random.nextBoolean() ? x : other.x, random.nextBoolean() ? y : other.y);
    }

    public void mutate(float limit) {
        x = Mutator.mutateFloat(x, limit);
        y = Mutator.mutateFloat(y, limit);
    }

    public Point getProgress(Point other, float progress) {
        return new Point(x + (other.x - x) * progress, y + (other.y - y) * progress);
    }
}
