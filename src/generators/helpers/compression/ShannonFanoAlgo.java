package generators.helpers.compression;

import java.util.Hashtable;
import java.util.Vector;

public class ShannonFanoAlgo {

  public static class Partition {

    protected Partition left;
    protected Partition right;
    protected String    value;

    public Partition(Vector<Letter> letters) {
      int index;
      if (letters.size() == 1)
        this.value = letters.elementAt(0).letter;
      else {
        // get the sum of frequencys
        int sum = 0;
        for (int i = 0; i < letters.size(); i++) {
          sum += letters.elementAt(i).frequency;
        }
        // Partion the elements
        float half = (float) sum / 2;
        int count = 0;
        index = 0;
        float difference = half;
        for (int i = 0; i < letters.size(); i++) {
          if (Math
              .abs(half
                  - (float) ((float) count + (float) letters.elementAt(i).frequency)) < difference) {
            count += letters.elementAt(i).frequency;
            difference = Math.abs(half - count);
            index = i;
          } else
            break;
        }

        // create the 2 partitions
        Vector<Letter> l = new Vector<Letter>(0, 1);
        Vector<Letter> r = new Vector<Letter>(0, 1);
        for (int i = 0; i < letters.size(); i++) {
          if (i <= index)
            l.add(letters.elementAt(i));
          else
            r.add(letters.elementAt(i));
        }
        this.left = new Partition(l);
        this.right = new Partition(r);
      }
    }
  }

  public static class Letter {

    protected String letter;
    protected int    frequency;

    public Letter(String letter, int frequency) {
      this.letter = letter;
      this.frequency = frequency;
    }
  }

  public static void compress(String[] text) {
    // extract the input text
    // String input = "";
    // for (int i=0;i<text.length;i++) {
    // input += text[i];
    // }

    // get the frequencies of each letter
    int[] list = new int[256];
    for (int i = 0; i < text.length; i++) {
      list[new Integer(text[i].charAt(0))]++;
    }

    // sort all the letters into the initial partition (decreasing size!)
    Vector<Letter> letters = new Vector<Letter>(0, 1);
    int most = -1;
    int mostIndex = -1;

    // get the number of different letters
    int numberOfLetters = 0;
    for (int i = 0; i < 256; i++) {
      if (list[i] != 0)
        numberOfLetters++;
    }

    // create the partition
    for (int i = 0; i < numberOfLetters; i++) {
      most = -1;
      mostIndex = -1;
      for (int j = 0; j < list.length; j++) {
        if (list[j] > 0 && list[j] > most) {
          most = list[j];
          mostIndex = j;
        }
      }
      letters.add(new Letter("" + (char) mostIndex, most));
      list[mostIndex] = 0;
    }
    Partition partition = new Partition(letters);

    // encode

    // get the encoding for every letter by traversing the tree
    Hashtable<String, String> hash = new Hashtable<String, String>();
    fillHash(hash, partition, "");

    // Encode the input
    String result = "";
    for (int i = 0; i < text.length; i++) {
      result += hash.get(text[i]) + " ";
    }
    System.out.println(result);
  }

  public static Hashtable<String, String> fillHash(
      Hashtable<String, String> hash, Partition p, String currentBits) {
    if (p.value != null) {
      hash.put(p.value, currentBits);
    } else {
      // hash =
      fillHash(hash, p.left, currentBits + "0");
      // hash =
      fillHash(hash, p.right, currentBits + "1");
    }
    return hash;
  }
}
