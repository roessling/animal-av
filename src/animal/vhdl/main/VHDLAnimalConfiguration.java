package animal.vhdl.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import animal.editor.Editor;
import animal.editor.graphics.meta.GraphicEditor;
import animal.exchange.animalscript.VHDLExporter;
import animal.graphics.PTGraphicObject;
import animal.handler.GraphicObjectHandler;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;
import translator.ResourceLocator;

/**
 * <br>
 * insert VHDL components to Animalconfiguration.
 * 
 * @author Lu,Zheng
 * @version 1.0
 */
public class VHDLAnimalConfiguration {
  private ArrayList<String>         VHDLcomponents = new ArrayList<String>(0);
  private Hashtable<String, Editor> VHDLEditors    = new Hashtable<String, Editor>(
                                                       0);

  public VHDLAnimalConfiguration() {
    // updatePrimitives();
  }

  private void readVHDLComponents() {
    try {
      InputStream in = ResourceLocator.getResourceLocator().getResourceStream("/VHDLcomponents.dat");
      //
      //		  
      // InputStream in = new FileInputStream("animal" + File.separator +"vhdl"
      // + File.separator + "VHDLcomponents.dat");
      if (in != null) {
        BufferedInputStream bins = new BufferedInputStream(in);
        Properties temp = new Properties();
        temp.load(bins);
        bins.close();
        in.close();
        Enumeration<?> keys = temp.propertyNames();
        while (keys.hasMoreElements()) {
          String key = (String) keys.nextElement();
          String value = temp.getProperty(key);
          if (key.startsWith("primitive")) {
            VHDLcomponents.add(value);
          }

          if (key.startsWith("animalscript"))
            AnimalConfiguration.getDefaultConfiguration().insertXProperties(
                key, value);

        }
      }

    } catch (IOException ioex) {
      System.err.println(ioex.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void updatePrimitives() {
    if (VHDLcomponents.size() == 0) {
      readVHDLComponents();
    }
    for (int i = 0; i < VHDLcomponents.size(); i++) {
      String currentComponent = VHDLcomponents.get(i);
      String editorBaseName = currentComponent.replaceFirst(
          "animal.vhdl.graphics.PT", "animal.vhdl.editor.graphics.");
      String editorName = new StringBuilder(editorBaseName).append("Editor")
          .toString();
      String componentName = editorBaseName.replaceFirst(
          "animal.vhdl.editor.graphics.", "");
      Class<Editor> c;
      XProperties props = Animal.getAnimalConfiguration().getProperties();
      try {
        c = (Class<Editor>) Class.forName(editorName);
        Editor ed = c.newInstance();
        Animal.getAnimalConfiguration().insertEditor(i, ed);
        if ((ed != null) && ed instanceof GraphicEditor) {
          GraphicObjectHandler handler = null;
          PTGraphicObject ptgo = null;

          Class<PTGraphicObject> ptgoClass = (Class<PTGraphicObject>) Class
              .forName(currentComponent);
          // if this fails, it is uncritical!
          try {
            Method targetMethod = ptgoClass
                .getDeclaredMethod("initializeDefaultProperties",
                    new Class[] { props.getClass() });
            targetMethod.invoke(null, new Object[] { props });
          } catch (NoSuchMethodException e) {
            // MessageDisplay.message("methodNotDeclaredIn", new String[] {
            // componentName, e.getMessage() });
          } catch (InvocationTargetException e) {
            MessageDisplay.message("mthdInvFail", new String[] { componentName,
                e.getMessage() });
          }

          ptgo = ptgoClass.newInstance();
          StringBuilder handlerClassName = new StringBuilder(editorName
              .length() + 10);
          handlerClassName.append("animal.vhdl.handler.").append(componentName)
              .append("Handler");
          Class<?> handlerClass = Class.forName(handlerClassName.toString());
          handler = (GraphicObjectHandler) handlerClass.newInstance();
          // AnimalTranslator.getResourceBundle().addPropertyResource("demo");

          if (ptgo != null) {
            PTGraphicObject.registeredHandlers.put(ptgo.getType(), handler);
            String[] registered = ptgo.handledKeywords();
            for (int j = 0; j < registered.length; j++) {
              PTGraphicObject.registeredTypes.put(registered[j].toLowerCase(),
                  currentComponent);
            }
          }
        }

      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

    }

    VHDLExporter.importToObjectExporters(VHDLcomponents);

  }

  @SuppressWarnings("unchecked")
  public Hashtable<String, Editor> getEditors() {
    if (VHDLcomponents.size() == 0) {
      readVHDLComponents();
    }
    Hashtable<String, Editor> animalEditors = Animal.get().getEditors();
    for (int i = 0; i < VHDLcomponents.size(); i++) {
      String currentName = VHDLcomponents.get(i);
      String editorBaseName = currentName.replaceFirst(
          "animal.vhdl.graphics.PT", "animal.vhdl.editor.graphics.");
      String editorName = new StringBuilder(editorBaseName).append("Editor")
          .toString();
      Class<Editor> c;
      try {
        c = (Class<Editor>) Class.forName(editorName);
        Editor ed = c.newInstance();
        VHDLEditors.put(ed.getName(), ed);
        animalEditors.put(ed.getName(), ed);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

    }
    return VHDLEditors;
  }
}
