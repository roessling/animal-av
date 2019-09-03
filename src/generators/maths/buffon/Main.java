package generators.maths.buffon;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import util.Brush;

public class Main {

  public static void main(String args[]) {

    int needles1[][] = new int[3][5];
    needles1[0][0] = 1;
    needles1[1][0] = 2;
    needles1[1][1] = 30;
    needles1[1][2] = 1;
    needles1[1][3] = -30;
    needles1[1][4] = 2;
    needles1[2][0] = 1;
    needles1[2][1] = 120;
    needles1[2][2] = 1;
    needles1[2][3] = 120;
    needles1[2][4] = 1;

    int needles2[][] = new int[2][3];
    needles2[0][0] = 1;
    needles2[1][0] = 1;
    needles2[1][1] = 90;
    needles2[1][2] = 1;

    BuffonAlgo algo = new BuffonAlgo(20, 0.4, 6, needles1, needles2, new Brush(
        Color.RED), new Brush(Color.BLUE), new Brush(Color.YELLOW), true, false);

    try {
      PrintWriter pWriter = new PrintWriter(new FileWriter("BuffonTest.asu"));
      pWriter.println(algo.toString());
      pWriter.flush();
      pWriter.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

  }

}
