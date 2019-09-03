package generators.searching.SimulatedAnnealing.Algorithm;

import generators.searching.SimulatedAnnealing.Util.Cases;

import java.util.Random;

/**
 * Created by philipp on 03.08.15.
 */
public class SimulatedAnnealing {
    private Random random;
    private boolean terminated = true;
    private int iterations;
    private int radius;
    private double[] data;
    private TemperatureFunction func;
    //Each iteration
    private int current;
    private int chosen;
    private double delta;
    private double prop;
    private double temp;
    private double rand;
    private int old;
    private Cases currentCase;

    private double tZero;

    private int size;
    private int k;

    /**
     * For debugging only
     * @param seed
     */
    public SimulatedAnnealing(int seed){
        random = new Random(seed);
    }

    public SimulatedAnnealing(){
        random = new Random();
    }


    public void initialize(double[] data, int iterations, int rad, double tZero, TemperatureFunction func){
        this.radius = rad;
        this.data = data;
        this.iterations = iterations;
        this.k = 0;
        this.size = data.length;
        this.terminated = false;
        this.current = size/2;
        this.func = func;
        this.tZero = tZero;
    }

    public void doStep(){
        old = current;
        k++;
        if(!terminated){
            switch (func){
                case fast: temp = tempFast(k);
                    break;
                case boltzmann: temp = temperatureBoltzmann(k);
                    break;
                default: temp = temperatureExp(k);
            }
            if(temp == 0){
                terminated = true;
            }
            else{
                chosen = randomSuccessor(current);
                //System.out.println("Iteration "+ k +" Current:" +data[current] +" at " + current + ". Chosen " +chosen+ " with "+data[chosen]);
                delta = data[chosen] - data[current];
                if(delta > 0){
                    current = chosen;
                    currentCase = Cases.BETTER;
                }
                else{
                    prop = Math.exp(delta/temp);
                    rand = random.nextDouble();
                  //  System.out.println("Delta: " + delta + ", Temp: "+temp +" -> " + prop + " Random: " +rand);
                    if(rand < prop){
                        current = chosen;
                        currentCase = Cases.WORSE_ACCEPTED;
                    }
                    else{
                        currentCase = Cases.WORSE_REJECTED;
                    }
                }

            }
        }
    }




    public boolean isTerminated(){
        return terminated;
    }

    public int returnCurrentResult(){
        return current;
    }


    private int randomSuccessor(int current){
        int succ;
        do {
            int val = random.nextInt(2 * radius + 1);
            succ = current + val - radius;
            if (succ < 0) {
                succ = 0;
            }
            if (succ >= size) {
                succ = size - 1;
            }
        } while (succ == current);
        return succ;
    }

    /**
     * Different temp TemperatureFunction. Basically the default function implemented by the matlab reference implementation
     */

    private double temperatureExp(int n){
        //terminate
        if(n == iterations) {
            return 0;
        }
        double nD = n;
        return tZero*Math.pow(0.95, nD);
    }
    private double temperatureBoltzmann (int n){
        if(n == iterations){
            return 0;
        }
        double nD = n;
        return tZero/Math.log(nD);
    }
    private double tempFast (int n){
        if(n == iterations){
            return 0;
        }
        double nD = n;
        return tZero/nD;
    }

    /**
     *  Getter for significant Values during each iteration - can be used to display them
     */
    public double getProp(){
        return prop;
    }

    public int getCurrent(){
        return current;
    }

    public double getTemp(){
        return temp;
    }

    public double getRand(){
        return rand;
    }

    public Cases getCurrentCase(){
        return currentCase;
    }


    public int getIteration(){
        return k;
    }
    public int getOld() {
        return old;
    }
    public int getChosen(){
        return chosen;
    }
    public double getDelta(){
        return delta;
    }

    public boolean isLastStep(){
        return k == iterations;
    }

    public TemperatureFunction getFunction(){
        return func;
    }
}
