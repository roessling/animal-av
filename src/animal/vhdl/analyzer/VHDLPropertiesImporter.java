package animal.vhdl.analyzer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import animal.vhdl.VHDLAnimalDummy;

public class VHDLPropertiesImporter {
  public static Properties PropertiesImporter() {
    Properties config = null;
    try {
      VHDLAnimalDummy dummy = new VHDLAnimalDummy();
      InputStream in = dummy.getClass().getClassLoader().getResourceAsStream(
          "animal/vhdl/VHDL.properties");
//      InputStream in = new FileInputStream("animal" + File.separator + "vhdl"
//          + File.separator + "VHDL.properties");
      if (in != null) {
        BufferedInputStream bins = new BufferedInputStream(in);
        config = new Properties();
        config.load(bins);
        bins.close();
        in.close();
      }
    } catch (IOException ioex) {
      System.err.println(ioex.getMessage());
    }
    return config;
  }
}
