package htdptl.util;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Util {

  public static Font getFont() {
    return new Font("SansSerif", Font.PLAIN, 16); // was "Monospaced"
  }

  public static Font getBoldFont() {
    return new Font("SansSerif", Font.BOLD, 16);// was "Monospaced"
  }

  public static String escape(String string) {
    return string.replaceAll("\"", "\\\\\"");
  }

  public static String getFileContents(File file) {
    String content = "";
    try {
      FileReader fr;
      fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (!line.startsWith(";;") && line.length() > 0) {
          content += line + "\n";
        }

      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return content;
  }

  public static void write(String out, String scriptCode) {
    try {
      File outFile = new File(out);
      System.out.println(outFile);
      FileWriter fw;
      fw = new FileWriter(outFile);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(scriptCode);
      bw.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void writeFile(File outFile, String scriptCode)
      throws IOException {
    FileWriter fw = new FileWriter(outFile);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(scriptCode);
    bw.close();
  }

  public static Color getHighlightColor() {
    return Color.green;
  }

}
