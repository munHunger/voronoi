package se.munhunger.voronoi;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Marcus Münger
 */
public class Picture implements Comparable<Picture> {
    public static final int REGION_COUNT = 15;

    public float regions[] = new float[REGION_COUNT*5];

    public static Random random = new Random(0l);

    public static Picture generateRandom(){
        Picture picture = new Picture();
        for(int i = 0; i < picture.regions.length; i++) {
            picture.regions[i] = random.nextFloat();
        }
        return picture;
    }

    public BufferedImage toImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++)
            {
                double closeDistance = Double.MAX_VALUE;
                int closeIndex = 0;
                for(int i = 0; i < regions.length; i+=5)
                {
                    double dist = Math.sqrt(Math.pow(x - (regions[i] * width),2) + Math.pow(y - (regions[i + 1] * height), 2));
                    if(dist < closeDistance) {
                        closeIndex = i;
                        closeDistance = dist;
                    }
                }
                g.setColor(new Color(regions[closeIndex+2], regions[closeIndex+3], regions[closeIndex+4]));
                g.fillRect(x,y,1,1);
            }
        }
        return image;
    }

    public void mutate(float factor) {
        for(int i = 0; i < regions.length; i++) {
            float effect = (random.nextFloat() * factor) - factor/2;
            regions[i] = Math.min(Math.max(regions[i] + effect, 0), 1);
        }
    }

    public static Picture splice(Picture a, Picture b) {
        Picture n = new Picture();
        for(int i = 0; i < a.regions.length; i++)
            n.regions[i] = random.nextBoolean() ? a.regions[i] : b.regions[i];
        return n;
    }

    @Override
    public int compareTo(Picture o) {
        float thisSimilarity = Voronoi.calcSimilarity(toImage(Voronoi.image.getWidth(), Voronoi.image.getHeight()), Voronoi.image);
        float otherSimilarity = Voronoi.calcSimilarity(o.toImage(Voronoi.image.getWidth(), Voronoi.image.getHeight()), Voronoi.image);
        return Float.compare(thisSimilarity, otherSimilarity) * -1;
    }
}
