package generators.framework;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import translator.TranslatableGUIElement;
import translator.Translator;

public class GenGUI implements ListSelectionListener {
  private boolean notifyMode = true;
  
  Vector<Generator> generators = new Vector<Generator>(500,500);
  
  private HashMap<String, Generator> name2gen = new HashMap<String, Generator>(3991, 340);
//  private int lastFirst = 0, lastLast = 0;
  private String[] algoNames;
  private JEditorPane infoPane;
  private  JList<?> list;
  private Vector<String> allNames = new Vector<String>(500, 500);
  Vector<String> locales = new Vector<String>(20);
  Vector<String> output = new Vector<String>(20, 20);
  Vector<String> algoName = new Vector<String>(200, 200);
  Vector<String> type = new Vector<String>(200, 200);
  HashMap<String, Vector<Generator>> seen = new HashMap<String, Vector<Generator>>(3991, 340);

  private String[] packages = new String[]{"generators.backtracking", "generators.compression", "generators.cryptography",
      "generators.datastructures", "generators.graph", "generators.graphics", "generators.hardware", "generators.maths",
      "generators.misc", "generators.network", "generators.searching", "generators.sorting", "generators.tree"};
 
  private void notify(String s) {
    if (notifyMode)
      System.err.println(s);
  }
  protected void readAll() {
    for (String packageName : packages) {
      readGenerators(packageName);
    }
    notify(generators.size() + " content generators loaded.");
  }

  protected void updateChoice(String type, Object value) {
    System.err.println("Update list to contain only items with "+type +"=" +value);
  }
  
  protected void showFrame() {
    Translator t = new Translator("guigen", Locale.US);
    TranslatableGUIElement tgui = t.getGenerator();
    JFrame f = tgui.generateJFrame("title");
    f.setSize(800, 640);
    f.setMaximumSize(new Dimension(1024, 768));
    f.setResizable(false);
    
    Container cp = f.getContentPane();
    cp.setLayout(new BorderLayout());
    JPanel jp = tgui.generateBorderedJPanel("choices");
    jp.setLayout(new GridLayout(2, 5));
    jp.setVisible(true);
    cp.add(jp, BorderLayout.NORTH);
    
    // row 1: labels
    jp.add(tgui.generateJLabel("lang"));
    jp.add(tgui.generateJLabel("out"));
    jp.add(tgui.generateJLabel("type"));
    jp.add(tgui.generateJLabel("algo"));
    jp.add(tgui.generateJLabel("search"));
    
    String[] localeInfos = extractEntries(locales);
    JComboBox<String> langCB = tgui.generateJComboBox("lang", null, localeInfos, "*", true);
    langCB.addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        System.err.println("language changed to " +e.getItem());
        updateChoice("locale", e.getItem());        
      }
      
    });
    jp.add(langCB);

    String[] outInfos = extractEntries(output);
    JComboBox<String> outCB = tgui.generateJComboBox("out", null, outInfos, "*", true);
    outCB.addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        System.err.println("output changed to " +e.getItem());
        updateChoice("output", e.getItem());        
      }
      
    });
    jp.add(outCB);

    String[] typeInfos = extractEntries(type);
    JComboBox<String> typeCB = tgui.generateJComboBox("type", null, typeInfos, "*", true);
    typeCB.addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        System.err.println("type changed to " +e.getItem());
        updateChoice("type", e.getItem());        
      }
      
    });
    jp.add(typeCB);

    String[] algoInfo = extractEntries(algoName);
    JComboBox<String> algoCB = tgui.generateJComboBox("algo", null, algoInfo, "*", true);
    algoCB.addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        System.err.println("algoName changed to " +e.getItem());
        updateChoice("algoName", e.getItem());        
      }
      
    });
    jp.add(algoCB);
    

    JTextField jtf = tgui.generateJTextField("searchMe", null, 16, "");
    jp.add(jtf);
    
    list = tgui.generateJList("list", null, algoNames, ListSelectionModel.SINGLE_SELECTION, this, 0);
    JScrollPane jScrollPane = new JScrollPane(list);

    infoPane = new JEditorPane("text/html", "<html><body><h1>Nothing chosen</h1><p>Please choose an element first.</p></body></html>");
    JSplitPane jsp = new JSplitPane();
    jsp.add(jScrollPane, JSplitPane.LEFT, 1);
//    jsp.add(jScrollPane, 1);
    jsp.add(new JScrollPane(infoPane), JSplitPane.RIGHT, 2);
    cp.add(jsp);
    f.setVisible(true);
  }
  
  private void readGenerators(String packageName) {
    try {
      Class<?> dummyClass = Class.forName(packageName +".DummyGenerator");
      GeneratorBundle instance = (GeneratorBundle)dummyClass.newInstance();
      Vector<Generator> generatorsInInstance = instance.getGenerators();
      generators.addAll(generatorsInInstance);
    } catch (Exception e) {
      System.err.println("problem@" + packageName +": " +e.getClass().getName() + " /" + e.getMessage());
      e.printStackTrace();
    }
  }
  
  private void updateForEntry(Generator g, HashMap<String, Vector<Generator>> seen, Vector<String> entries, String field, String prefix) {
    String help = prefix + field;
    if (!seen.containsKey(help)) {
      Vector<Generator> vc = new Vector<Generator>(20, 15);
      vc.add(g);
      seen.put(help, vc);
      // ensure field entry exists
      entries.add(field);
    }
    else {
      seen.get(help).add(g);
    }
  }

  private String[] extractEntries(Vector<String> entries) {
    String[] result = new String[entries.size() + 1];
    result[0] = "*";
    for (int i = 0; i < entries.size(); i++)
      result[i + 1] = entries.elementAt(i);
    Arrays.sort(result);
    return result;
  }

//  private void showStats(String tag, Vector<String> entries, HashMap<String, Vector<Generator>> seen, String prefix, boolean showUniques) {
//    notify(tag +": " +entries.size());
//    if (showUniques)
//      for (String s : entries)
//        notify("  " +s +": " +seen.get(prefix + s).size() +" times");
//  }
  
  protected void processGenerators(Vector<Generator> gen) {
    for (Generator g : gen) {
      if (g == null)
        break;
      
      // add name to list
      String name = g.getName();
      allNames.add(name);
      if (!name2gen.containsKey(name))
        name2gen.put(name, g);
      else
        name2gen.put(name +" a", g);
      
      // update locales
      updateForEntry(g, seen, locales, g.getContentLocale().getDisplayName(), "locale.");
    
      // update output formats
      updateForEntry(g, seen, output, g.getOutputLanguage(), "output.");
      
      // update algo names
      updateForEntry(g, seen, algoName, g.getAlgorithmName(), "algo.");

      // update topic types
      updateForEntry(g, seen, type, GeneratorType.getStringForType(g.getGeneratorType().getType()), "type.");

//      String loc = g.getContentLocale().getDisplayName();
//      String help = "locale." +loc;
//      if (!seen.containsKey(help)) {
//        seen.put(help, 1);
//        // ensure locale exists
//        locales.add(loc);
//        System.err.println(help);
//      }
//      else {
//        seen.put(help, (Integer)seen.get(help) + 1);
//      }
      
//      help = "output." +g.getOutputLanguage();
      
    }
//    showStats("Locales", locales, seen, "locale.", true);
//    showStats("Output formats", output, seen, "output.", true);
//    showStats("Algo Names", algoName, seen, "algo.", false);
//    showStats("Algo Types", type, seen, "type.", true);
    algoNames = new String[allNames.size()];
    allNames.copyInto(algoNames);
    Arrays.sort(algoNames);
  }

  protected void init() {
    // read in all definitions
    readAll();
    
//    // summarize results
//    summarize(generators);
  }
  
  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      int chosen = list.getSelectedIndex();
//      int first = e.getFirstIndex();
//      int last = e.getLastIndex();
//      int target = 0;
//      System.err.print("first: " +first +" (lF=" + lastFirst +"), last: " +last + " (lL=" +lastLast);
//      if (first != lastFirst && first != lastLast)
//        target = lastFirst = first;
//      else
//        lastLast = target = last;
//      System.err.println("), target=" +target);
      Generator chosenGenerator = name2gen.get(algoNames[chosen]);
      infoPane.setText("<html><body><h1>" +chosenGenerator.getName() +"</h1>"+"<h2>Description</h2>"+chosenGenerator.getDescription()
          +"<h2>Code Example</h2><pre>" +chosenGenerator.getCodeExample()+"</pre></body></html>");
      infoPane.setCaretPosition(0);
    }
  }  

  
  public static void main(String[] args) {
    GenGUI g = new GenGUI();
//    System.err.println("Processing, this make take a moment...");
    g.init();
//    g.readAll();
    g.processGenerators(g.generators);
//    Vector<Generator> vc = g.seen.get("type.Network");
//    for (Generator gen : vc)
//      System.err.println("**" +gen.toString());
    if (args.length>0 && "-dump".equals(args[0]))
      for (Generator gen : g.generators) {
        System.out.println(GeneratorType.getStringForType(gen.getGeneratorType().getType()) +";" +gen.getGeneratorType().getType() +";"
            + gen.getAlgorithmName() +";"+gen.getContentLocale()+";"+gen.getOutputLanguage()
            +";"+gen.getAnimationAuthor() +";" +gen.getClass().getName());
      }
    g.showFrame();
  }
}
