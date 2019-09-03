package generators.hardware.prefixAdderAnimation.util;

/**
 * Created by philipp on 30.07.15.
 */
public class Util {
    private static final double LOG2 = Math.log(2);

    public static int[] toBinary(int in, int length){
        int[] res = new int[length];
        for (int i = 0; i < res.length; i++) {
            res[i] = (in % 2 == 0) ? 0 : 1;
            in = in / 2;
        }
        return res;
    }
    public static int fromBinary(int[] in, boolean signed){
        int res = 0;
        for (int i = 0; i < in.length - 1; i++) {
            res = res + in[i] * (int) Math.pow(2, i);
        }
        res = res + (signed ? - 1 : 1) * in[in.length-1] * (int) Math.pow(2,in.length-1);
        return res;
    }

    public static int[] invert(int[] in){
        int[] out = new int[in.length];
        for(int i = 0; i < out.length;i++){
            if (in[i] == 1){
                out[i] = 0;
            }
            if (in[i] == 0){
                out[i] = 1;
            }
        }
        return out;
    }

}
