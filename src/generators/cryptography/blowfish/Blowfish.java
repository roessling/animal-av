package generators.cryptography.blowfish;

/**
 * Blowfish encryption/decryption class.
 *
 * @author Dani El-Soufi (dani.el-soufi@stud.tu-darmstadt.de)
 * @author Deniz Can Franz Ertan (deniz_can_franz.ertan@stud.tu-darmstadt.de)
 */
class Blowfish extends BaseBlowfish {

    /**
     * Constructor.
     *
     * @param secret
     *                              A secret key, has to be a multiple of 32bit till 448bit
     * @throws BlowfishException
     *                              Throws when the given secret is not a multiple of 32bit
     */
    public Blowfish(final String secret) throws BlowfishException {
        this(secret.getBytes());
    }

    /**
     * Constructor.
     *
     * @param secret
     *                              A secret key, has to be a multiple of 32bit till 448bit
     * @param callback
     *                              Callback interface
     * @throws BlowfishException
     *                              Throws when the given secret is not a multiple of 32bit
     */
    public Blowfish(final String secret, final BlowfishAnimatorCallback callback) throws BlowfishException {
        this(secret.getBytes(), callback);
    }

    /**
     * Constructor.
     *
     * @param bsecret
     *                              A secret key as byte array, has to be a multiple of 32bit till 448bit
     * @throws BlowfishException
     *                              Throws when the given secret is not a multiple of 32bit
     */
    public Blowfish(final byte[] bsecret) throws BlowfishException {
        super(bsecret);
    }

    /**
     * Constructor.
     *
     * @param bsecret
     *                              A secret key as byte array, has to be a multiple of 32bit till 448bit
     * @param callback
     *                              Callback interface
     * @throws BlowfishException
     *                              Throws when the given secret is not a multiple of 32bit
     */
    public Blowfish(final byte[] bsecret, BlowfishAnimatorCallback callback) throws BlowfishException {
        super(bsecret, callback);
    }

    public String encrypt(final String plainText) throws BlowfishException {
        int len = plainText.length();
        if ((len % Blowfish.BLOCKSIZE) != 0) {
            throw new BlowfishException("error during " + TAG + "#encrypt(): given message is not a multiple of 64bit");
        }

        callback.ready();

        byte[] bcipher = Util.stringToByteArray(plainText);
        encrypt(bcipher);
        return Util.bytesToHexadecimal(bcipher, 0 , bcipher.length);
    }

    public String decrypt(final String cipher) throws BlowfishException {
        // get the number of estimated bytes in the string (cut off broken blocks)
        int len = (cipher.length() >> 1) & ~7;
        if (len < Blowfish.BLOCKSIZE) {
            throw new BlowfishException("error during " + TAG + "#decrypt(): the given cipher is too short");
        }

        // anything to decrypt?
        len >>= 1;
        if (len == 0) {
            return "";
        }

        callback.ready();

        byte[] bplain = Util.hexStringToByteArray(cipher);
        decrypt(bplain);
        return Util.byteArrayToUTF8String(bplain);
    }

}
