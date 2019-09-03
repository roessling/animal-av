package generators.hardware.prefixAdderAnimation.util;

/**
 * Created by philipp on 30.07.15.
 */
public class Adder {

    private int[] inputBlockValuesP;
    private int[] inputBlockValuesG;
    private int[][] internalBlockValuesP;
    private int[][] internalBlockValuesG;
    private int[] outputBlockValues;
    private int height;
    private int width;

    public Adder(int w){
        width = w;
        height = (int) Math.ceil(Math.log(width)/Math.log(2));
        inputBlockValuesP = new int[width+1];
        inputBlockValuesG = new int[width+1];
        internalBlockValuesP = new int[height][width];
        internalBlockValuesG = new int[height][width];
        outputBlockValues = new int[width];
    }


    public void add(int[] inputA, int[] inputB, int cIn) {
        // input layer
        inputBlockValuesG[0] = cIn;
        inputBlockValuesP[0] = 0;
        for (int i = 1; i < width + 1; i++) {
            inputBlockValuesP[i] = calcInputP(inputA[i - 1], inputB[i - 1]);
            inputBlockValuesG[i] = calcInputG(inputA[i - 1], inputB[i - 1]);
        }
        //first internal layer
        for (int i = 0; i < width; i++) {
            if (i % 2 == 1) {
                internalBlockValuesP[0][i] = calcInternalP(inputBlockValuesP[i],inputBlockValuesP[i-1]);
                internalBlockValuesG[0][i] = calcInternalG(inputBlockValuesP[i],inputBlockValuesG[i],inputBlockValuesG[i-1]);
            }
            else{
                internalBlockValuesP[0][i] = inputBlockValuesP[i];
                internalBlockValuesG[0][i] = inputBlockValuesG[i];
            }
        }
        // remaining internal layers
        for (int j = 1; j < height; j++) {
            boolean block = false;
            int k = 0;
            int l = 0;
            for (int i = 0; i < width; i++){
                if(block){
                    internalBlockValuesP[j][i] = calcInternalP(internalBlockValuesP[j-1][i],internalBlockValuesP[j-1][l]);
                    internalBlockValuesG[j][i] = calcInternalG(internalBlockValuesP[j-1][i],internalBlockValuesG[j-1][i],internalBlockValuesG[j-1][l]);
                }
                else {
                    internalBlockValuesP[j][i] = internalBlockValuesP[j - 1][i];
                    internalBlockValuesG[j][i] = internalBlockValuesG[j - 1][i];
                }
                k++;
                if(k % (int)Math.pow(2,j)==0){
                    k = 0;
                    block = !block;
                    if(block) {
                        l = i;
                    }
                }
            }
        }
        // output layer
        for (int i = 0; i < width; i++){
            outputBlockValues[i] = calcOutputS(inputA[i],inputB[i],internalBlockValuesG[height-1][i]);
        }
    }


    /**************************************************************************************************************
     * Inner Block calculations
     ************************************************************************************************************+*/
    //Input block
    private int calcInputP(int a, int b){
        return a | b;
    }
    private int calcInputG(int a, int b){
        return a & b;
    }
    //Internal block
    private int calcInternalP(int pU, int pL){
        return pL & pU;
    }
    private int calcInternalG(int pU, int gU, int gL){
        return ((pU & gL) | gU);
    }
    private int calcOutputS(int a, int b, int g){
        return  (a ^ b) ^ g;
    }

    /****************************************************************************************************************
     * getter for block signals
     ****************************************************************************************************************/

    public int[] getOutputBlockValues() {
        return outputBlockValues;
    }

    public int[][] getInternalBlockValuesG() {
        return internalBlockValuesG;
    }

    public int[][] getInternalBlockValuesP() {
        return internalBlockValuesP;
    }

    public int[] getInputBlockValuesP() {
        return inputBlockValuesP;
    }

    public int[] getInputBlockValuesG() {
        return inputBlockValuesG;
    }


    /*******************
     * Debugging only
     ******************/
    public void testAdder() {
        for (int i = 0; i < 20000; i++) {
            if (i % 200 == 0) {
                System.out.println(i / 200 + "% Abgeschlossen");
            }
            for (int j = 0; j < 20000; j++) {
                this.add(Util.toBinary(i,width), Util.toBinary(j,width),0);
                int[] res = getOutputBlockValues();
                if (i + j != Util.fromBinary(res,false)) {
                    System.out.println("Error with i: " + i + " j:  " + j);
                    System.out.print("Was: ");
                    for (int k = 0; k < res.length; k++) {
                        System.out.print(res[res.length - 1 - k] + " ");
                    }
                    System.out.println(" = " + Util.fromBinary(res,false));
                    System.out.println("Should have been: " + (i + j));
                }
            }
        }
    }
}
