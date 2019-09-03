package generators.cryptography.blowfish;

/**
 * A blowfish animator callback for returning step-by-step algorithm execution.
 *
 * @author Dani El-Soufi (dani.el-soufi@stud.tu-darmstadt.de)
 * @author Deniz Can Franz Ertan (deniz_can_franz.ertan@stud.tu-darmstadt.de)
 */
interface BlowfishAnimatorCallback {

    /**
     * Called when encryption/decryption method has been called.
     */
    void ready();

    /**
     * Plain text as high and low int values.
     *
     * @param hi
     *              32 higher bits
     * @param lo
     *              32 lower bits
     */
    void text(int hi, int lo);

    /**
     * Low value.
     *
     * @param lo
     *              32 lower bits
     */
    void lo(int lo);

    /**
     * High value.
     *
     * @param hi
     *              32 higher bits
     */
    void hi(int hi);

    /**
     * Cipher text as high and low int values.
     *
     * @param hi
     *              32 higher bits
     * @param lo
     *              32 lower bits
     */
    void cipher(int hi, int lo);

    /**
     * P-Box value at specific position.
     *
     * @param val
     *              Value
     * @param pos
     *              Position
     */
    void pbox(int val, int pos);

    /**
     * Result of the F-Function of the algorithm for high or low result.
     *
     * @param result
     *                  Result of the F-Function
     * @param val
     *                  0 for low and 1 for high
     */
    void f(int result, int val);

    /**
     * S-Box value for high or low result.
     *
     * @param result
     *                  Result of sbox
     * @param val
     *                  0 for low and 1 for high
     */
    void sbox(int result, int val);

}
