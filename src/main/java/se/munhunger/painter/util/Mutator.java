package se.munhunger.painter.util;

import java.util.Random;

/**
 * @author Marcus Münger
 */
public class Mutator {
    private static Random random = new Random(0);
    public static float mutateFloat(float f, float limit) {
        f += (random.nextFloat() * limit * 2) - limit;
        f = Math.min(1, Math.max(f, 0));
        return f;
    }
}
