package se.munhunger.painter.util;

import java.awt.image.BufferedImage;

public interface Generation {
    void initialize();

    void step(int times);

    BufferedImage getBest(int width, int height);

    int getGenerationCount();
}
