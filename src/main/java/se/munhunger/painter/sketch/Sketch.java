package se.munhunger.painter.sketch;

import se.munhunger.painter.Painter;

import java.io.IOException;

/**
 * @author Marcus Münger
 */
public class Sketch {
    public static void main(String[] args) throws IOException {
        Painter.init("ref1.jpg", new SketchGeneration());
    }
}
