package generators.searching.SimulatedAnnealing.Util;

import java.util.Random;

/**
 * Created by philipp on 03.08.15.
 */
public class DataGenerator {

    Random rand = new Random();

    public DataGenerator(){
        rand = new Random();
    }

    /**
     * For debugging only
     * @param seed
     */
    public DataGenerator(int seed){
        rand = new Random(seed);
    }

    public double[] generateData(int size){
        double[] data = new double[size];
        data[0] = 0;
        for(int i = 1; i < size; i++) {
            data[i] = data[i - 1] + rand.nextGaussian();
        }
        return data;
    }

}
