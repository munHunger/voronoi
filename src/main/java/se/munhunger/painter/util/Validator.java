package se.munhunger.painter.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author Marcus Münger
 */
public class Validator {
    private static final int THREADS = 4;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);

    public static float calcSimilarity(BufferedImage image1, BufferedImage image2) {
        if(image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight())
            throw new IllegalArgumentException("The images must be the same size");
        float sum = 0;
        Queue<Future<Float>> queue = new ArrayDeque<>();
        for(int x = 0; x < image1.getWidth(); x++) {
            int finalX = x;
            queue.add(threadPool.submit((Callable<Float>)() -> {
                float localSum = 0;
                for(int y = 0; y < image1.getHeight(); y++) {
                    Color c1 = new Color(image1.getRGB(finalX, y));
                    Color c2 = new Color(image2.getRGB(finalX, y));
                    float error = (Math.abs(c1.getRed() - c2.getRed()) + Math.abs(c1.getGreen() - c2.getGreen()) + Math.abs(c1.getBlue() - c2.getBlue())) / 255f / 3f;
                    localSum += error;
                }
                return localSum;
            }));
        }
        while (!queue.isEmpty())
            try {
                sum += queue.remove().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        return 1f - (sum / (image1.getWidth() * image1.getHeight()));
    }
}
