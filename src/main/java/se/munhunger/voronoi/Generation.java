package se.munhunger.voronoi;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Marcus Münger
 */
public class Generation {
    public static final int GENERATION_SIZE = 15;
    public static final float MUTATION_FACTOR = 0.05f;
    public Picture generation[] = new Picture[GENERATION_SIZE];

    public void initialize(){
        for(int i = 0; i < generation.length; i++)
            generation[i] = Picture.generateRandom();
    }

    private void order() {
        Arrays.sort(generation);
    }

    private static Random random = new Random(0);

    public void step(int times){
        for(int i = 0; i < times; i++) {
            System.out.println("Generating on step " + i);
            Picture newGen[] = new Picture[GENERATION_SIZE];
            order();
            newGen[0] = generation[0];
            for(int n = 1; n < newGen.length; n++) {
                newGen[n] = Picture.splice(generation[random.nextInt(5)], generation[random.nextInt(5)]);
                newGen[n].mutate(MUTATION_FACTOR);
            }
            generation = newGen;
        }
    }

    public Picture getBest() {
        order();
        return generation[0];
    }
}
