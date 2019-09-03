package generators.cryptography.blowfish;

/**
 * Utility class.
 *
 * @author Dani El-Soufi (dani.el-soufi@stud.tu-darmstadt.de)
 * @author Deniz Can Franz Ertan (deniz_can_franz.ertan@stud.tu-darmstadt.de)
 */
final class Util {

    private final static char[] HEXTAB = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Convert a byte array to a hexadecimal string
     *
     * @param data
     *                      Byte array
     * @param start
     *                      Start index where to get the bytes
     * @param nbytes
     *                      Number of bytes to convert
     * @return
     *                      Hexadecimal string
     */
    static String bytesToHexadecimal(byte[] data, int start, int nbytes) {
        StringBuilder sb = new StringBuilder();
        sb.setLength(nbytes << 1);

        int pos = 0;
        for (int i = 0; i < nbytes; i++) {
            sb.setCharAt(pos++, HEXTAB[(data[i + start] >> 4) & 0x0f]);
            sb.setCharAt(pos++, HEXTAB[data[i + start] & 0x0f]);
        }

        return sb.toString();
    }

    /**
     * Get bytes from an array into a long value
     *
     * @param buffer
     *                  Byte array
     * @param start
     *                  Start index from where to start read
     * @return
     *                  64bit long integer
     */
    static long byteArrayToLong(byte[] buffer, int start) {
        return (((long)buffer[start]) << 56) |
                (((long)buffer[start + 1] & 0x0ffL) << 48) |
                (((long)buffer[start + 2] & 0x0ffL) << 40) |
                (((long)buffer[start + 3] & 0x0ffL) << 32) |
                (((long)buffer[start + 4] & 0x0ffL) << 24) |
                (((long)buffer[start + 5] & 0x0ffL) << 16) |
                (((long)buffer[start + 6] & 0x0ffL) << 8) |
                ((long)buffer[start + 7] & 0x0ff);
    }

    /**
     * Convert given plain text into a byte array suitable to Blowfish.
     *
     * @param plainText
     *                      Plain text
     * @return
     *                      Plain text as byte array
     */
    static byte[] stringToByteArray(String plainText) {
        int len = plainText.length();
        // allocate the buffer (align to the next 8 byte)
        byte[] bcipher = new byte [((len << 1) & 0xfffffff8)];

        // copy all bytes of the string into the buffer
        int pos = 0;
        for (int i = 0; i < len; i++) {
            char c = plainText.charAt(i);
            bcipher[pos++] = (byte) ((c >> 8) & 0x0ff);
            bcipher[pos++] = (byte) (c & 0x0ff) ;
        }

        return bcipher;
    }

    /**
     * Convert the decrypted byte array into UTF-8 string.
     * Reconstructs the plain text as being given during encryption.
     *
     * @param bplain
     *                  Byte array
     * @return
     *                  UTF-8 string
     */
    static String byteArrayToUTF8String(byte[] bplain) {
        byte[] plain = new byte[bplain.length >> 1];
        int pos = 0;
        for (int i = 0; i < bplain.length; i++) {
            if ((i & 0x01) == 1) {
                plain[pos++] = bplain[i];
            }
        }

        return new String(plain);
    }

    /**
     * Convert long value to bytes which are put into a given array.
     *
     * @param val
     *                  Long value
     * @param buffer
     *                  Output buffer
     * @param start
     *                  Start index to where to place the bytes in the buffer
     */
    static void longToByteArray(long val, byte[] buffer, int start) {
        buffer[start] = (byte) (val >>> 56);
        buffer[start + 1] = (byte) ((val >>> 48) & 0x0ff);
        buffer[start + 2] = (byte) ((val >>> 40) & 0x0ff);
        buffer[start + 3] = (byte) ((val >>> 32) & 0x0ff);
        buffer[start + 4] = (byte) ((val >>> 24) & 0x0ff);
        buffer[start + 5] = (byte) ((val >>> 16) & 0x0ff);
        buffer[start + 6] = (byte) ((val >>> 8) & 0x0ff);
        buffer[start + 7] = (byte) val;
    }

    /**
     * Convert hexadecimal to byte array.
     *
     * @param s
     *          Hexadecimal string
     * @return
     *          Byte array
     */
    static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Convert integer to string array.
     *
     * @param val
     *              Integer
     * @return
     *              String array
     */
    static String[] intToStringArray(int val) {
        String[] s = new String[32];
        for (int i = 0; i < 32; i++) {
            s[i] = Integer.toString(val & 0x1);
            val = val >>> 1;
        }

        return s;
    }

}
