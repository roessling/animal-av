package animal.vhdl.analyzer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import animal.misc.MessageDisplay;

public class VHDLImporter {
  private ArrayList<String> entityCode;
  private ArrayList<String> ArchitectureCode;
  private ArrayList<String> codes;

  public void importVHDLFrom(String filename) {
    InputStream in = null;
    try {
      in = new FileInputStream(filename);
      importVHDLFrom(in);
    } catch (IOException e) {
      MessageDisplay.errorMsg("importException " + filename,
          MessageDisplay.RUN_ERROR);
    }
  }

  public void importVHDLFrom(InputStream in) {
    codes = new ArrayList<String>(0);
    try {
      InputStreamReader isr = new InputStreamReader(in);
      BufferedReader br = new BufferedReader(isr);
      String currentLine = null;
      while ((currentLine = br.readLine()) != null)
        codes.add(currentLine);
      br.close();
      in.close();
      setEntityCode();
    } catch (IOException e) {
      MessageDisplay.errorMsg("importException", MessageDisplay.RUN_ERROR);
    }

  }

  private void setEntityCode() {
    int i, j = 0, k = 0, l = 0;
    for (i = 0; i < codes.size(); i++) {
      if (codes.get(i).toLowerCase().startsWith("entity"))
        j = i;
      if (codes.get(i).toLowerCase().startsWith("end entity"))
        k = i;
      if (codes.get(i).toLowerCase().startsWith("architecture"))
        l = i;
    }
    entityCode = new ArrayList<String>();
    entityCode.add(j + "");
    for (int c = j; c <= k; c++) {
      entityCode.add(codes.get(c));
    }
    ArchitectureCode = new ArrayList<String>();
    ArchitectureCode.add(l + "");
    for (int c = l; c < codes.size(); c++) {
      ArchitectureCode.add(codes.get(c));
    }

  }

  public ArrayList<String> getEntityCode() {
    return entityCode;
  }

  public ArrayList<String> getArchitectureCode() {
    return ArchitectureCode;
  }

  public String[] getCodeLine(String sourceCodeName) {
    String VHDLcodes[];
    ArrayList<String> codeLine = new ArrayList<String>(0);
    int indentation = 0;
    for (int i = 0; i < codes.size(); i++) {

      if (i > 1
          && (codes.get(i - 1).toLowerCase().matches(".*begin.*")
              || codes.get(i - 1).toLowerCase().matches(".*entity.*")
              || codes.get(i - 1).toLowerCase().matches(".*architecture.*")
              || codes.get(i - 1).toLowerCase().matches(".*component.*")
              || codes.get(i - 1).toLowerCase().matches(".*process.*")
              || codes.get(i - 1).toLowerCase().matches(".*if.*")
              || codes.get(i - 1).toLowerCase().matches(".*else.*") || codes
              .get(i - 1).toLowerCase().matches(".*elsif.*"))
          && !codes.get(i - 1).toLowerCase().matches(".*end.*")
          && !codes.get(i - 1).toLowerCase().matches(".*elsif.*"))
        indentation = indentation + 1;
      if (codes.get(i).toLowerCase().matches(".*end.*")
          || codes.get(i).toLowerCase().matches(".*else.*")
          || codes.get(i).toLowerCase().matches(".*elsif.*")) {
        if (codes.get(i).toLowerCase().matches(".*end architecture.*"))
          indentation = 0;
        indentation = indentation - 1;
      }
      if (indentation > 0) {
        codeLine.add("addCodeLine \"" + codes.get(i).trim() + "\" to \""
            + sourceCodeName + "\" indentation " + indentation);
      } else
        codeLine.add("addCodeLine \"" + codes.get(i).trim() + "\" to \""
            + sourceCodeName + "\"");
    }

    VHDLcodes = new String[codeLine.size()];
    codeLine.toArray(VHDLcodes);
    // for (int i=0;i<VHDLcodes.length;i++){
    // System.out.println(VHDLcodes[i]);
    // }
    return VHDLcodes;
  }
}
