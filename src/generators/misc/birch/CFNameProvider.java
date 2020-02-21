package generators.misc.birch;

import java.util.LinkedList;

public class CFNameProvider {
    private static final char[] ALPHABET = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private LinkedList<Integer> nameIdx = new LinkedList<>();

    public CFNameProvider() {
        nameIdx.add(0);
    }

    public String useName() {
        // next name compute
        int idx = nameIdx.size() - 1;
        int newValue = (nameIdx.get(idx) + 1) % ALPHABET.length;
        String oldName = getName();

        while (newValue == 0) {
            nameIdx.set(idx, newValue);
            if (idx == 0) {
                nameIdx.add(0);
                break;
            } else {
                idx--;
                newValue = (nameIdx.get(idx) + 1) % ALPHABET.length;
            }
        }
        nameIdx.set(idx, newValue);

        return oldName;
    }

    public String getName() {
        return nameIdx.stream().map(x -> "" + ALPHABET[x]).reduce((x, y) -> x + y).get();
    }
}
