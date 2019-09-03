package generators.cryptography.blowfish;

/**
 * Blowfish base class.
 *
 * @author Dani El-Soufi (dani.el-soufi@stud.tu-darmstadt.de)
 * @author Deniz Can Franz Ertan (deniz_can_franz.ertan@stud.tu-darmstadt.de)
 */
abstract class BaseBlowfish implements BlowfishBoxes {

    final static String TAG = Blowfish.class.getSimpleName();

    public final static int BLOCKSIZE = 8; // 64bit

    /**
     * Handles NPEs.
     */
    BlowfishAnimatorCallback callback = new BlowfishAnimatorCallback() {
        public void ready() {
        }

        public void text(int hi, int lo) {
        }

        public void lo(int lo) {
        }

        public void hi(int hi) {
        }

        public void cipher(int hi, int lo) {
        }

        public void pbox(int val, int pos) {
        }

        public void f(int result, int val) {
        }

        public void sbox(int result, int val) {
        }
    };

    private final static int PBOX_ENTRIES = 18;
    private final static int SBOX_ENTRIES = 256;

    private int[] m_pbox;
    private int[] m_sbox1;
    private int[] m_sbox2;
    private int[] m_sbox3;
    private int[] m_sbox4;

    /**
     * Constructor.
     *
     * @param bsecret
     *                              Secret key as byte array
     * @throws BlowfishException
     *                              Throws when the given secret is not a multiple of 32bit
     */
    BaseBlowfish(byte[] bsecret) throws BlowfishException {
        check(bsecret);
        init(bsecret);
    }

    /**
     * Constructor.
     *
     * @param bsecret
     *                              Secret key as byte array
     * @param callback
     *                              Callback interface
     * @throws BlowfishException
     *                              Throws when the given secret is not a multiple of 32bit
     */
    BaseBlowfish(byte[] bsecret, BlowfishAnimatorCallback callback) throws BlowfishException {
        this.callback = callback;

        check(bsecret);
        init(bsecret);
    }

    /**
     * Initialize boxes properly.
     *
     * @param bsecret
     *                  Secret key as byte array
     */
    private void init(byte[] bsecret) {
        m_pbox = new int[PBOX_ENTRIES];
        System.arraycopy(pbox_init, 0, m_pbox, 0, m_pbox.length);

        m_sbox1 = new int[SBOX_ENTRIES];
        m_sbox2 = new int[SBOX_ENTRIES];
        m_sbox3 = new int[SBOX_ENTRIES];
        m_sbox4 = new int[SBOX_ENTRIES];

        System.arraycopy(sbox_init_1, 0, m_sbox1, 0, m_sbox1.length);
        System.arraycopy(sbox_init_2, 0, m_sbox2, 0, m_sbox2.length);
        System.arraycopy(sbox_init_3, 0, m_sbox3, 0, m_sbox3.length);
        System.arraycopy(sbox_init_4, 0, m_sbox4, 0, m_sbox4.length);

        int pos = 0;
        int len = bsecret.length;

        // XOR key over p-box
        int block = 0; // 32bit
        for (int i = 0; i < PBOX_ENTRIES; i++) {
            for (int j = 0; j < 4; j++) {
                block = (block << 8) | (((int)bsecret[pos++ % len]) & 0x0ff);
            }
            m_pbox[i] ^= block;
        }

        // encrypt all boxes with zero string
        long zero = 0; // 64bit
        for (int i = 0; i < PBOX_ENTRIES; i += 2) {
            zero = encryptBlock(zero);
            m_pbox[i] = (int)(zero >>> 32);
            m_pbox[i + 1] = (int)(zero & 0x0ffffffffL);
        }

        for (int i = 0; i < SBOX_ENTRIES; i += 2) {
            zero = encryptBlock(zero);
            m_sbox1[i] = (int)(zero >>> 32);
            m_sbox1[i + 1] = (int)(zero & 0x0ffffffffL);
        }

        for (int i = 0; i < SBOX_ENTRIES; i += 2) {
            zero = encryptBlock(zero);
            m_sbox2[i] = (int)(zero >>> 32);
            m_sbox2[i + 1] = (int)(zero & 0x0ffffffffL);
        }

        for (int i = 0; i < SBOX_ENTRIES; i += 2) {
            zero = encryptBlock(zero);
            m_sbox3[i] = (int)(zero >>> 32);
            m_sbox3[i + 1] = (int)(zero & 0x0ffffffffL);
        }

        for (int i = 0; i < SBOX_ENTRIES; i += 2) {
            zero = encryptBlock(zero);
            m_sbox4[i] = (int)(zero >>> 32);
            m_sbox4[i + 1] = (int)(zero & 0x0ffffffffL);
        }
    }

    /**
     * Encrypt the given buffer and write the result back to it.
     *
     * @param bplain
     *                  Byte array to encrypt
     */
    void encrypt(byte[] bplain) {
        long val;
        for (int i = 0; i < bplain.length; i += 8) {
            // encrypt 64bit block
            val = Util.byteArrayToLong(bplain, i);
            val = encryptBlock(val);
            callback.lo(longLo32(val));
            Util.longToByteArray(val, bplain, i);
        }
    }

    /**
     * Decrypt the given buffer and write the result back to it.
     *
     * @param bcipher
     *                  Byte array to decrypt
     */
    void decrypt(byte[] bcipher) {
        long val;
        for (int i = 0; i < bcipher.length; i += 8) {
            // decrypt over 64bit block
            val = Util.byteArrayToLong(bcipher, i);
            val = decryptBlock(val);
            Util.longToByteArray(val, bcipher, i);
        }
    }

    /**
     * Internal routine to encrypt a 64bit plain text block.
     *
     * @param plainTextBlock
     *                          64bit plain text block
     * @return
     *                          64bit encrypted block
     */
    private long encryptBlock(long plainTextBlock) {
        int hi = longHi32(plainTextBlock);
        int lo = longLo32(plainTextBlock);

        callback.text(hi, lo);

        int[] sbox1 = m_sbox1;
        int[] sbox2 = m_sbox2;
        int[] sbox3 = m_sbox3;
        int[] sbox4 = m_sbox4;

        int[] pbox = m_pbox;

        callback.pbox(pbox[0], 0);
        callback.pbox(pbox[1], 1);

        callback.hi(hi);
        callback.lo(lo);

        hi ^= pbox[0];
        callback.hi(hi);
        callback.sbox(sbox1[hi >>> 24], 1);
        callback.sbox(sbox2[(hi >>> 16) & 0x0ff], 1);
        callback.sbox(sbox3[(hi >>> 8) & 0x0ff], 1);
        callback.sbox(sbox4[hi & 0x0ff], 1);
        callback.f((((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]), 1);
        lo ^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox[1];
        callback.lo(lo);
        callback.f((((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]), 0);
        hi ^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox[2];
        callback.sbox(sbox1[lo >>> 24], 0);
        callback.sbox(sbox2[(lo >>> 16) & 0x0ff], 0);
        callback.sbox(sbox3[(lo >>> 8) & 0x0ff], 0);
        callback.sbox(sbox4[lo & 0x0ff], 0);
        callback.hi(hi);
        lo ^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox[3];
        hi ^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox[4];
        lo ^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox[5];
        hi ^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox[6];
        lo ^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox[7];
        hi ^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox[8];
        lo ^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox[9];
        hi ^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox[10];
        lo ^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox[11];
        hi ^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox[12];
        lo ^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox[13];
        hi ^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox[14];
        lo ^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox[15];
        callback.hi(hi);
        hi ^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox[16];
        callback.hi(hi);
        callback.lo(lo);

        callback.pbox(pbox[16], 16);
        callback.pbox(pbox[17], 17);

        return makeLong(hi, lo ^ pbox[17]);
    }

    /**
     * Internal routine to decrypt a 64bit cipher block.
     *
     * @param cipherBlock
     *                      64bit plain text block
     * @return
     *                      64bit encrypted block
     */
    private long decryptBlock(long cipherBlock) {
        int hi = longHi32(cipherBlock);
        int lo = longLo32(cipherBlock);


        callback.text(hi, lo);

        int[] sbox1 = m_sbox1;
        int[] sbox2 = m_sbox2;
        int[] sbox3 = m_sbox3;
        int[] sbox4 = m_sbox4;

        int[] pbox = m_pbox;

        callback.pbox(pbox[17], 17);
        callback.pbox(pbox[16], 16);

        callback.hi(hi);
        callback.lo(lo);


        hi ^= m_pbox[17];
        callback.hi(hi);
        callback.sbox(sbox1[hi >>> 24], 1);
        callback.sbox(sbox2[(hi >>> 16) & 0x0ff], 1);
        callback.sbox(sbox3[(hi >>> 8) & 0x0ff], 1);
        callback.sbox(sbox4[hi & 0x0ff], 1);
        callback.f((((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]), 1);
        lo ^= (((m_sbox1[hi >>> 24] + m_sbox2[(hi >>> 16) & 0x0ff]) ^ m_sbox3[(hi >>> 8) & 0x0ff]) + m_sbox4[hi & 0x0ff]) ^ m_pbox[16];
        callback.lo(lo);
        callback.f((((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]), 0);
        hi ^= (((m_sbox1[lo >>> 24] + m_sbox2[(lo >>> 16) & 0x0ff]) ^ m_sbox3[(lo >>> 8) & 0x0ff]) + m_sbox4[lo & 0x0ff]) ^ m_pbox[15];
        callback.sbox(sbox1[lo >>> 24], 0);
        callback.sbox(sbox2[(lo >>> 16) & 0x0ff], 0);
        callback.sbox(sbox3[(lo >>> 8) & 0x0ff], 0);
        callback.sbox(sbox4[lo & 0x0ff], 0);
        callback.hi(hi);
        lo ^= (((m_sbox1[hi >>> 24] + m_sbox2[(hi >>> 16) & 0x0ff]) ^ m_sbox3[(hi >>> 8) & 0x0ff]) + m_sbox4[hi & 0x0ff]) ^ m_pbox[14];
        hi ^= (((m_sbox1[lo >>> 24] + m_sbox2[(lo >>> 16) & 0x0ff]) ^ m_sbox3[(lo >>> 8) & 0x0ff]) + m_sbox4[lo & 0x0ff]) ^ m_pbox[13];
        lo ^= (((m_sbox1[hi >>> 24] + m_sbox2[(hi >>> 16) & 0x0ff]) ^ m_sbox3[(hi >>> 8) & 0x0ff]) + m_sbox4[hi & 0x0ff]) ^ m_pbox[12];
        hi ^= (((m_sbox1[lo >>> 24] + m_sbox2[(lo >>> 16) & 0x0ff]) ^ m_sbox3[(lo >>> 8) & 0x0ff]) + m_sbox4[lo & 0x0ff]) ^ m_pbox[11];
        lo ^= (((m_sbox1[hi >>> 24] + m_sbox2[(hi >>> 16) & 0x0ff]) ^ m_sbox3[(hi >>> 8) & 0x0ff]) + m_sbox4[hi & 0x0ff]) ^ m_pbox[10];
        hi ^= (((m_sbox1[lo >>> 24] + m_sbox2[(lo >>> 16) & 0x0ff]) ^ m_sbox3[(lo >>> 8) & 0x0ff]) + m_sbox4[lo & 0x0ff]) ^ m_pbox[9];
        lo ^= (((m_sbox1[hi >>> 24] + m_sbox2[(hi >>> 16) & 0x0ff]) ^ m_sbox3[(hi >>> 8) & 0x0ff]) + m_sbox4[hi & 0x0ff]) ^ m_pbox[8];
        hi ^= (((m_sbox1[lo >>> 24] + m_sbox2[(lo >>> 16) & 0x0ff]) ^ m_sbox3[(lo >>> 8) & 0x0ff]) + m_sbox4[lo & 0x0ff]) ^ m_pbox[7];
        lo ^= (((m_sbox1[hi >>> 24] + m_sbox2[(hi >>> 16) & 0x0ff]) ^ m_sbox3[(hi >>> 8) & 0x0ff]) + m_sbox4[hi & 0x0ff]) ^ m_pbox[6];
        hi ^= (((m_sbox1[lo >>> 24] + m_sbox2[(lo >>> 16) & 0x0ff]) ^ m_sbox3[(lo >>> 8) & 0x0ff]) + m_sbox4[lo & 0x0ff]) ^ m_pbox[5];
        lo ^= (((m_sbox1[hi >>> 24] + m_sbox2[(hi >>> 16) & 0x0ff]) ^ m_sbox3[(hi >>> 8) & 0x0ff]) + m_sbox4[hi & 0x0ff]) ^ m_pbox[4];
        hi ^= (((m_sbox1[lo >>> 24] + m_sbox2[(lo >>> 16) & 0x0ff]) ^ m_sbox3[(lo >>> 8) & 0x0ff]) + m_sbox4[lo & 0x0ff]) ^ m_pbox[3];
        lo ^= (((m_sbox1[hi >>> 24] + m_sbox2[(hi >>> 16) & 0x0ff]) ^ m_sbox3[(hi >>> 8) & 0x0ff]) + m_sbox4[hi & 0x0ff]) ^ m_pbox[2];
        callback.hi(hi);
        hi ^= (((m_sbox1[lo >>> 24] + m_sbox2[(lo >>> 16) & 0x0ff]) ^ m_sbox3[(lo >>> 8) & 0x0ff]) + m_sbox4[lo & 0x0ff]) ^ m_pbox[1];

        callback.hi(hi);
        callback.lo(lo);

        callback.pbox(pbox[1], 1);
        callback.pbox(pbox[0], 0);

        return makeLong(hi, lo ^ m_pbox[0]);
    }

    /**
     * Encrypt the given plain text.
     *
     * @param plainText
     *                              plain text
     * @return
     *                              Encrypted cipher as byte array
     * @throws BlowfishException
     *                              Throws when an unexpected error occurs
     */
    abstract String encrypt(String plainText) throws BlowfishException;

    /**
     * Decrypt the given cipher.
     *
     * @param cipher
     *                              Cipher
     * @return
     *                              Decrypted cipher as byte array
     * @throws BlowfishException
     *                              Throws when an unexpected error occurs
     */
    abstract String decrypt(String cipher) throws BlowfishException;

    /**
     * Get higher 32bits of the long integer.
     *
     * @param val
     *              Long integer
     * @return
     *              Higher 32bits of the integer
     */
    private int longHi32(long val) {
        return (int)(val >>> 32);
    }

    /**
     * Get lower 32bits of the given long integer.
     *
     * @param val
     *              Long integer
     * @return
     *              Lower 32bits of the integer
     */
    private int longLo32(long val) {
        return (int)val;
    }

    /**
     * Make a long (64bit) value out of two integers (treated unsigned)
     *
     * @param lo
     *              Lower 32bits
     * @param hi
     *              Higher 32bits
     * @return
     *              Long value out of the two integers
     */
    private long makeLong(int lo, int hi) {
        return (((long)hi << 32) | ((long)lo & 0x00000000ffffffffL));
    }

    /**
     * Check if the secret size is a multiple of 32bit.
     *
     * @param bsecret
     *                              Secret as byte array
     * @throws BlowfishException
     *                              Throws if secret is not a multiple of 32bit
     */
    private void check(final byte[] bsecret) throws BlowfishException {
        if ((bsecret.length % 4) != 0) {
            throw new BlowfishException("error during " + TAG + "#init(): given secret is not a multiple of 32bit");
        }
    }

}
