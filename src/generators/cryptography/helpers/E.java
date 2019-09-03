package generators.cryptography.helpers;

/**
 * 
 * @author Christian Feier & Yannick Drost
 *
 */
public interface E {
    	
    /**
     * encrypts the given block I_i
     * 
     * @param i_i
     * 			given Block to encrypt. I_i must have length of n. 
     * 
     * @return encrypted I_i aka O_i
     */
    public String encrypt(String i_i);
    
    /**
     * returns a String representation of the given function. Returns a String if it isnt a permutation else an string array
     * 
     * @return string representation of E or string Array
     */
    public Object stringRepresentation();
    
    /**
     * returns true if E is a Permutation or not
     * 
     * @return true if it is a permutation, else false
     */
    public boolean isPermutation();
}
