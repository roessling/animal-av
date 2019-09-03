package generators.framework.wizard;

import generators.framework.PropertiesGUI;

import java.awt.GridLayout;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class WizardPanel extends JPanel {

  BasicPropertiesPanel panel1      = new BasicPropertiesPanel();
  DescriptionPanel     panel2      = new DescriptionPanel();
  CodeExamplePanel     panel3      = new CodeExamplePanel();
  PropertiesGUI        propertiesGUI;                           // This is
                                                                 // panel 4
  AdditionalDataPanel  panel5;
  JFileChooser         fileChooser = new JFileChooser();

  // int langFlag = LanguageInterface.LANGUAGE_ENGLISH;
  // static GraphAlgController mainclass; //= new GraphAlgController(langFlag);

  // public GraphScriptPanel createGraphScriptPanel(){
  // return new GraphScriptPanel(mainclass);
  // }
  public WizardPanel() {
    super(new GridLayout(1, 1));

    propertiesGUI = new PropertiesGUI();
    // propertiesGUI.start();

    // panel4 = (Panel4) propertiesGUI.wiz.panelContent;
    panel5 = new AdditionalDataPanel(this);

    JTabbedPane tabbedPane = new JTabbedPane();
    // createGraphScriptPanel();
    // int langFlag = LanguageInterface.LANGUAGE_ENGLISH;
    // mainclass = new GraphAlgController(langFlag);
    // createGraphScriptPanel();
    tabbedPane.add("Step 1: Basic", panel1);
    tabbedPane.addTab("Step 2: Description", panel2);
    tabbedPane.addTab("Step 3: Code Example", panel3);
    tabbedPane.addTab("Step 4: XML", propertiesGUI.getWizardGUI()
        .getWizardFrame().getContentPane());
    tabbedPane.addTab("Step 5: Export", panel5);

    add(tabbedPane);
    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
  }

  /**
   * This class is used in the savefiledialog to filter only Java files
   */
  public class JavaFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
      return f.getName().toLowerCase().endsWith(".java") || f.isDirectory();
    }

    @Override
    public String getDescription() {
      return "Java files (*.java)";
    }
  }

  public void Export(final boolean overwrite) throws FileAlreadyExistsException {

    try {
      if (propertiesGUI.getXmlFile() == null) {
        JOptionPane.showMessageDialog(null,
            "Create an XML properties file first");
        return;
      }

      String dir = propertiesGUI.getXmlFile().getPath().replace("xml", "java");
      String name = propertiesGUI.getXmlFile().getName().replace("xml", "java");

      new GeneratorWriter(name, dir, panel1.GetAlgorithmName(),
          panel1.GetTitle(), panel1.GetAlgorithmType(), panel1.GetAuthorName(),
          panel2.getDescription(), panel3.getCodeExample(),
          panel5.getContentLocale(), panel5.getFileExtension(),
          panel5.getOutput(), propertiesGUI.getPrimitivesContainer(),
          panel5.ExtractPrims(), panel5.ExtractProps(), panel5.permitDisclosure(),
          overwrite);

      JOptionPane.showMessageDialog(null, "File " + dir + " successfully created!");

    } catch (FileAlreadyExistsException exception) {
      throw exception;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Unexpected Failure");
    }
    /*
     * try {
     * //http://www.java-forums.org/awt-swing/16137-save-file-based-file-extension
     * .html JFileChooser fc = new JFileChooser(); JavaFilter fJavaFilter = new
     * JavaFilter(); FileFilter filter = null;
     * fc.addChoosableFileFilter(filter); filter = fc.getFileFilter();
     * fc.setFileFilter(fJavaFilter);
     * 
     * int returnVal = fc.showSaveDialog(this); if (returnVal ==
     * JFileChooser.APPROVE_OPTION) {
     * 
     * // If the filename selected already exists if
     * (fc.getSelectedFile().exists()) { // ask the user for an overwrite
     * confirmation int response = JOptionPane.showConfirmDialog(null,
     * "Overwrite existing file?", "Confirm Overwrite",
     * JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE); if (response
     * == JOptionPane.CANCEL_OPTION) { return; } }
     * 
     * // String dir = fc.getSelectedFile().getPath(); // String name =
     * fc.getSelectedFile().getName();
     * 
     * 
     * new GeneratorWriter(name, dir, panel1.GetAlgorithmName(),
     * panel1.GetTitle(), panel1.GetAlgorithmType(), panel1.GetAuthorName(),
     * panel2.getDescription(), panel3.getCodeExample(),
     * panel5.getContentLocale(), panel5.getFileExtension(),
     * panel5.getOutput()); JOptionPane.showMessageDialog(null, "File Saved");
     * 
     * // PropertiesGUI gui = new PropertiesGUI(); // gui.start();
     * 
     * // To load an existing XML file call that //
     * gui.LoadFromFile("C:\\Users\\Jerome\\Desktop\\f.xml");
     * 
     * } } catch (Exception e) { JOptionPane.showMessageDialog(null,
     * "Save Failed"); return; }
     */
  }
}
