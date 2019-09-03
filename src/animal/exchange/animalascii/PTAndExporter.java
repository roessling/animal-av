package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTAnd;

public class PTAndExporter extends PTVHDLExporter {
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    if (ptgo instanceof PTAnd)
      pw.println(exportCommonFeatures((PTAnd) ptgo, "And"));
    else
      System.err.println("Sorry, but this is not possible");
  }
}
