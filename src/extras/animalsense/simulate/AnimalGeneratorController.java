package extras.animalsense.simulate;

import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.beanutils.DynaProperty;

import extras.lifecycle.common.PropertiesBean;
import extras.lifecycle.common.Record;
import extras.lifecycle.monitor.CheckpointUtils;
import extras.lifecycle.monitor.Monitor;
import extras.lifecycle.monitor.MonitorException;
import extras.lifecycle.monitor.MonitorListener;
import generators.framework.Generator;
import generators.framework.GeneratorManager;
import generators.framework.GeneratorTreeNode;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.properties.tree.PropertiesTreeModel;
import generators.generatorframe.store.SaveInfos;

/**
 * This class is based on <code>generator.GeneratorInterceptor</code> in Animal.
 * 
 * @author Mihail Mihaylov
 * @see generators.framework.GeneratorInterceptor
 * 
 */
public class AnimalGeneratorController {

  private String runGenerator(String generatorName, PropertiesBean variables,
      Generator generator) {

    PropertiesTreeModel model = loadPropertiesFor(generator, generatorName);

    AnimationPropertiesContainer container = model.getPropertiesContainer();
    Hashtable<String, Object> primitives = model.getPrimitivesContainer();

    // Fill all variables into the primitives of the algorithm
    for (DynaProperty prop : variables.getDynaProperties()) {
      String name = prop.getName();
      Object value = variables.get(name);

//      System.err.println("GeneratorUtils: Variable: " + name + " = " + value);
      primitives.put(name, value);
    }

    generator.init(); // Ensure all properties are set up.

    // primitives.put(arrayName, array);
    String content = generator.generate(container, primitives);
    assert content != null;

    return content;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * extras.animalsense.evaluate.Evaluator#executeSimulation(java.lang.String,
   * extras.lifecycle.common.PropertiesBean,
   * extras.lifecycle.common.MonitorListener)
   */
  public GeneratorSimulationBean executeSimulation(String generatorName,
      String chainPath, PropertiesBean variables,
      MonitorListener monitorListener) throws SimulationException {

    // First we load the generator object
    // Generator generator = createGenerator(generatorName);
    SaveInfos sI = SaveInfos.getInstance();
    Generator generator = sI.getGeneratorOutOfPath(chainPath);
//    System.out.println(chainPath);//TODO
 //   Generator generator = getGenerator(chainPath, generatorName);

    // Ensure that the object can be monitored
    // if (generator == null || !(generator instanceof Checkpointed))
    if (generator == null)
      throw new SimulationException("The generator could not be loaded: unknow error (generator is null).");

    // Register a monitor for this object
    Monitor monitor = null;
    try {
      monitor = CheckpointUtils.getMonitor(generator);
      // Start monitoring
      monitor.startRecording();
    } catch (MonitorException e) {
      e.printStackTrace();
      return null;
    }

    if (monitorListener != null)
      monitor.addListener(monitorListener);

    String script = runGenerator(generatorName, variables, generator);

    // Stop recording
    monitor.stopRecording();

    // Get the recorded data
    Record record = monitor.getRecord();

    return new GeneratorSimulationBean(record, script);
  }

  private PropertiesTreeModel loadPropertiesFor(Generator generator,
      String generatorName) {
    if (generator == null)
      return null;
    PropertiesTreeModel ptm = new PropertiesTreeModel();
    StringBuilder buf = new StringBuilder(64);
    buf.append(generator.getClass().getPackage().getName());
    buf.append("/");
    buf.append(generator.getClass().getSimpleName());
    String basename = buf.toString();
    try {
      String fname = PropertiesTreeModel.cleanString(basename + ".xml", "xml");// basename.replace('.',
                                                                               // '/');
      ptm.loadFromXMLFile(fname, true);
    } catch (Exception e2) {
      try {
        String fname = PropertiesTreeModel
            .cleanString(basename + ".ptm", "ptm");// basename.replace('.',
                                                   // '/');
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

  @SuppressWarnings("unused")
  private Generator createGenerator(String className) {
    Object object = null;
    try {
      Class<?> genClass = Class.forName(className);
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

  @SuppressWarnings("unused")
private Generator getGenerator(String chainPath, String generatorName) throws SimulationException {
    GeneratorManager.checkGenerators();
    Generator chosenGenerator = null;
    HashMap<String, Generator> n2g = GeneratorManager.name2Generator;
    chosenGenerator = n2g.get(generatorName);
    if (chosenGenerator != null)
      return chosenGenerator;
    //GR
//    Generator g = GeneratorManager.getGenerator("x");
    HashMap<String, DefaultMutableTreeNode> gens = GeneratorManager.getGenerators();
    DefaultMutableTreeNode gtn = gens.get(chainPath);
    if (gtn == null) {
      // first try if this is a class name...
      if (!chainPath.startsWith("/")) {
        try {
          Class<?> myClass = Class.forName(chainPath);
          if (myClass != null) {
            Object o;
            try {
              o = myClass.newInstance();
            } catch (Exception e) {
              throw new SimulationException("Generator not found " + chainPath +" / " +generatorName);
            }
            if (o instanceof Generator)
              return (Generator)o;
          }    
        }
        catch (ClassNotFoundException cnfe) {
          throw new SimulationException("Generator not found " + chainPath +" / " +generatorName);
        }
        throw new SimulationException("Generator not found " + chainPath +" / " +generatorName);
      }
      throw new SimulationException("Generator not found " + chainPath +" / " +generatorName);
    }

    if (!(gtn instanceof GeneratorTreeNode))
      throw new SimulationException("Generator " + chainPath + " is not an instance of " + GeneratorTreeNode.class.getSimpleName() + ".");

    return ((GeneratorTreeNode) gtn).getGenerator();
  }

}
