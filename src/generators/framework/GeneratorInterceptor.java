package generators.framework;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.properties.tree.PropertiesTreeModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class GeneratorInterceptor {
  public GeneratorInterceptor() {
  }
  
  public void runFromArguments(String[] args) {
    if (args == null || args.length < 3)
      return;
    String generatorName = args[0];
    Generator generator = createGenerator(generatorName);
    if (generator == null)
      return;
    PropertiesTreeModel model = loadPropertiesFor(generator, generatorName);
    String arrayName = args[1];
    int[] array = createArrayFromString(args[2]);
    AnimationPropertiesContainer container = model.getPropertiesContainer();
    Hashtable<String, Object> primitives = model.getPrimitivesContainer();
    primitives.put(arrayName, array);
    String content = generator.generate(container, primitives);
    FileWriter out;
    BufferedWriter buf = null;
    try {
      out = new FileWriter("demo.asu");
      buf = new BufferedWriter(out);
      // PropertiesTreeModel aModel = (PropertiesTreeModel) editor
      // .getPropertiesPanel().getTree().getModel();
      // buf.write(currentGenerator.generate(aModel.getPropertiesContainer(),
      // aModel.getPrimitivesContainer()));
      buf.write(content);
      buf.flush();
      buf.close();
    } catch (IOException e1) {
      JOptionPane.showMessageDialog(null, "Error writing",
          null, JOptionPane.ERROR_MESSAGE);
     } finally {
      try {
        if (buf != null)
          buf.close();
      } catch (Exception e2) {
        // do nothing
      }
    }

  }
  
  private int[] createArrayFromString(String arrayString) {
    if (arrayString == null)
      return null;
    StringTokenizer stok = new StringTokenizer(arrayString, ", ");
    int[] tmp = new int[arrayString.length()];
    int pos = 0;
    while (stok.hasMoreTokens()) {
      String token = stok.nextToken();
      try {
        Integer i = Integer.parseInt(token);
        tmp[pos++] = i.intValue();
      } catch(NumberFormatException nfe) {
        System.err.println(token +" could not be coded as an int");
      }
    }
    int[] result = new int[pos];
    System.arraycopy(tmp, 0, result, 0, pos);
    return result;
  }
  
  private PropertiesTreeModel loadPropertiesFor(Generator generator, String generatorName) {
    if (generator == null)
      return null;
    PropertiesTreeModel ptm = new PropertiesTreeModel();
    StringBuilder buf = new StringBuilder(64);
    buf.append(generator.getClass().getPackage().getName());
    buf.append("/");
    buf.append(generator.getClass().getSimpleName());
    String basename = buf.toString();
    try {
      String fname = PropertiesTreeModel.cleanString(basename + ".xml", "xml");//basename.replace('.', '/');
      ptm.loadFromXMLFile(fname, true);
    } catch (Exception e2) {
      try {
        String fname = PropertiesTreeModel.cleanString(basename +".ptm", "ptm");//basename.replace('.', '/');
        ptm.loadFromXMLFile(fname + ".ptm", true);
      } catch (Exception e3) {
        System.err.println(e3.getMessage());
        // the file wasn't found or it is invalid
      }
      System.err.println(e2.getMessage());
      // the file wasn't found or it is invalid
    }
    return ptm;
  }
  
  private Generator createGenerator(String className) {
    Object object = null;
    try {
      Class<?> genClass = Class.forName(className);
      // Class[] interfaces = genClass.getInterfaces();
      // for (int i = 0; i < interfaces.length; i++)
      // if (interfaces[i].getName().equals(genInterface)) {
      // interfaceFound = true;
      // break;
      // }
      //
      // // is Generator implemented?
      // if (!interfaceFound)
      // return null;
      object = genClass.newInstance();
      if (object instanceof Generator)
        return (Generator) object;
      return null;
    } catch (InstantiationException e) {
      // maybe the Class is abstract
      return null;
    } catch (IllegalAccessException e) {
      // we are not allowed to access the Class
      return null;
    } catch (ClassNotFoundException e) {
      // the Class does not exist
      return null;
    }
  }

  public static void main(String[] args) {
    new GeneratorInterceptor().runFromArguments(args);
  }

}
