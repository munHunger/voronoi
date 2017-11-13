package se.munhunger.voronoi;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author Marcus Münger
 */
public class Generation {
    public static final int GENERATION_SIZE = 500;
    public static final float MUTATION_FACTOR = 0.2f;
    public static final int ELITISM = 200;
    public static final int RANDOM_SAMPLES = 2;

    private static final int THREADS = 6;
    public Picture generation[] = new Picture[GENERATION_SIZE];

    int genCount = 0;

    public void initialize(){
        for(int i = 0; i < generation.length; i++)
            generation[i] = Picture.generateRandom();
    }

    private void order() {
        Arrays.sort(generation);
    }

    private static Random random = new Random(0);

    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);

    public void step(int times){
        for(int i = 0; i < times; i++) {
            genCount++;
            System.out.println("Generating on step " + i);
            Picture newGen[] = new Picture[GENERATION_SIZE];
            order();
            newGen[0] = generation[0];
            Semaphore semaphore = new Semaphore(THREADS);
            for(int n = 1; n < newGen.length/THREADS; n+= THREADS) {
                int finalN = n;
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                threadPool.submit(() -> {
                    for(int x = finalN; x < Math.max(newGen.length, finalN +THREADS); x++) {
                        newGen[x] = Picture.splice(generation[random.nextInt(ELITISM)],
                                                   generation[random.nextInt(ELITISM)]);
                        newGen[x].mutate(MUTATION_FACTOR);
                    }
                    semaphore.release();
                });
            }
            for(int n = 0; n < RANDOM_SAMPLES; n++) {
                newGen[newGen.length - n - 1] = Picture.splice(generation[random.nextInt(generation.length)], generation[random.nextInt(generation.length)]);
                newGen[newGen.length - n - 1].mutate(MUTATION_FACTOR);
            }
            try {
                semaphore.acquire(THREADS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            generation = newGen;
        }
    }

    public Picture getBest() {
        order();
        return generation[0];
    }
}
