package generators.misc.id3_chi_squared;

/** DoubleToString
 * create a String of a double with two decimal places
 */
public class DoubleToString {
    public static String doubleToString(double x){
        return String.format("%.2f",x);
    }
}
