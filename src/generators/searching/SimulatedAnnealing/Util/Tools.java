package generators.searching.SimulatedAnnealing.Util;

/**
 * Created by philipp on 04.08.15.
 */
public class Tools {
    /**
     *  Min and Max are needed to normalize data visualisation
     */
    public static int getMax(double[] data){
        double currentBestVal = -Double.MAX_VALUE;
        int best = -1;
        for (int i  = 0; i<data.length; i++){
            if(data[i] > currentBestVal){
                currentBestVal = data[i];
                best = i;
            }
        }
        return best;
    }

    public static int getMin(double[] data){
        double currentBestVal = Double.MAX_VALUE;
        int best = -1;
        for (int i  = 0; i < data.length; i++){
            if(data[i] < currentBestVal){
                currentBestVal = data[i];
                best = i;
            }
        }
        return best;
    }

    public static void printData(double[] data){
        for(int i = 0; i< data.length; i++){
            System.out.println(data[i]);
        }
    }
}
