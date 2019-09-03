package generators.searching.SimulatedAnnealing.Util;

import generators.searching.SimulatedAnnealing.Algorithm.TemperatureFunction;

/**
 * Created by philipp on 04.08.15.
 */
public class Txt {

    public static final String SOURCECODE =
    "do{\n"+
    "   temperature = temperatureFunction(k);\n"+
    "   chosen = randomSuccessor(current);\n" +
    "   delta = data[chosen] - data[current];\n" +
    "   if(delta > 0){\n" +
    "       current = chosen;\n" +
    "   }\n" +
    "   else{\n" +
    "       probability = Math.exp(delta/temperature);\n" +
    "       rand = random.nextDouble();\n" +
    "       if(rand < probability){\n" +
    "           current = chosen;\n" +
    "       }\n" +
    "   }\n" +
    "   k++;\n" +
    "}while(temperature != 0);";

    public static final String DESCRIPTION =
        "Simulated Annealing is a probabilistic searching algorithm. The algorithm is \n" +
        "inspired by classical 'annealing', a method from metallurgy and material \n" +
        "science whereby a material is heated up and then slowly cooled down in order \n"+
        "to change the properties of that material. \n \n"+
        "Simulated Annealing is used to find some kind of optimal solution (approximately)\n"+
        "for a given problem, where the searching space is non-convex and/or too complex to\n"+
        "optimize with numerical methods. This particular implementation of the algorithm \n+" +
        "maximizes the objective function, however it is easy to modify the algorithm (or the \n"+
        "objective function) such that a minimum can be found\n \n"+
        "In each step a successor is picked by the randomSuccessor function. In this example the\n" +
        "successor is a direct neighbour of the current point (the objective function is  \n" +
        "discretized). Of course this function can be adapted to arbitrary spaces. If the value of \n" +
        "the picked successor is better it is taken, if not it is taken with a certain probability. \n"+
        "The probability that a bad move is allowed depends on the 'badness' of that move and on the \n" +
        "current 'temperature'. The temperature in each step is given by a monotone decreasing \n" +
        "function. The algorithm terminates after a certain number of iterations or when the temperature \n " +
        "is 0. \n \n"+
        "For sufficiently slow lowering of the temperature it can be proven that the \n" +
        "probability to converge to a global optimum converges to 1. However in practice it \n"+
        "is non trivial to find a temperature function and a start temperature t_0 that \n" +
        "achieves good results for a given problem (as you will see in this animation). \n \n"+
        "In general tree cases are possible: \n"+
        "   - a better value is picked and taken \n" +
        "   - a worse value is picked but nevertheless accepted \n "+
        "   - a worse value is picked and rejected \n"+
        "For each case one example will be shown in detail, all the other iterations will be \n"+
        "shown briefly";

    public static final String ADD_FOR_GEN =
        "This generator lets you choose from 3 different temperature functions: \n \n"+
        "       (1) Exponential: t_0 * 0.95^n \n"+
        "       (2) Fast: t_0 / n \n"+
        "       (3) Boltzmann: t_0 / ln(n) \n"+
        "The Boltzmann function is inspired by the work of Ludwig Boltzmann (on Entropy).\n"+
        "In the beginning it decreases faster than the exponential function, asymptotically \n" +
        "however it decreases much slower \n"+
        "Also the initial temperature t_0, the maximum number of iterations and the \n"+
        "number of points with which the objective function will be generated is up to you. \n"+
        "The function itself will be generated randomly.";

    public static final String CONCLUSION =
        "The algorithm may not have found the optimal solution. This may be due to badly \n" +
        "chosen parameters and/or bad luck. However in practice simulated annealing works \n"+
        "quite well. In fact it is used to solve various real world tasks like VLSI - design\n"+
        "or for airline scheduling.";

    public static String getTempFuncString(TemperatureFunction func){
        switch (func){
            case exponential:
                return "Exponential Function: t_0*0.95^k";
            case boltzmann:
                return "Boltzmann Function: t_0/ln(k)";
            case fast:
                return "Fast (decreasing) Function: t_0/k";
        }
        return "Exponential Function: t_0*0.95^k";

    }
}

