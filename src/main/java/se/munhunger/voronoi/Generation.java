package se.munhunger.voronoi;

/**
 * @author Marcus Münger
 */
public class Generation {
    public static final int GENERATION_SIZE = 15;
    public Picture generation[] = new Picture[GENERATION_SIZE];

    public void initialize(){
        for(int i = 0; i < generation.length; i++)
            generation[i] = Picture.generateRandom();
    }

    public Picture getBest() {
        float best = 0f;
        int bestIndex = 0;
        for(int i = 0; i < generation.length; i++) {
            float similarity = Voronoi.calcSimilarity(generation[i].toImage(Voronoi.image.getWidth(), Voronoi.image.getHeight()), Voronoi.image);
            if(similarity > best) {
                best = similarity;
                bestIndex = i;
            }
        }
        return generation[bestIndex];
    }
}
