package animal.misc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import translator.AnimalTranslator;
import animal.exchange.AnimationImporter;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import animal.main.AnimationWindow;

public class AnimalRepositoryConnector implements ActionListener {
  public static final String RESOURCE_FILENAME = "http://www.animal.ahrgr.de/Anims/animLister.php3";

  private XProperties properties = null;

  private JTable theTable = null;

  private String[] labels = null;

  private String[][] entries = null;

  private boolean refresh = true;

  private int urlPos = 0;

  private AbstractButton acceptButton, cancelButton, refreshButton;

  private JFrame theFrame;

  public AnimalRepositoryConnector() {
    // do nothing
  }

  public boolean readTableEntries() {
    if (properties == null)
      properties = new XProperties();
    InputStream resourceStream = getResourceStream();
    if (resourceStream == null)
      return false;
    try {
      properties.load(resourceStream);
    } catch (IOException ioException) {
      MessageDisplay.errorMsg("ioExcRepo", RESOURCE_FILENAME,
          MessageDisplay.RUN_ERROR);
    }

    int currentLine, nrEntries = properties.getIntProperty("repository.size");
    // generate the new table
    entries = new String[nrEntries][6];
    labels = new String[] { AnimalTranslator.translateMessage("repExTitle"),
        AnimalTranslator.translateMessage("repExAuthor"),
        AnimalTranslator.translateMessage("repExSize"),
        AnimalTranslator.translateMessage("repExDate"),
        AnimalTranslator.translateMessage("repExURL"),
        AnimalTranslator.translateMessage("repExFormat") };

    // and then populate the table!
    for (currentLine = 0; currentLine < nrEntries; currentLine++) {
      entries[currentLine][0] = convert(properties.getProperty(currentLine
          + ".title"));
      entries[currentLine][1] = properties.getProperty(currentLine + ".author");
      entries[currentLine][2] = properties.getProperty(currentLine
          + ".fileSize");
      entries[currentLine][3] = properties.getProperty(currentLine + ".date");
      entries[currentLine][4] = properties.getProperty(currentLine + ".URL");
      entries[currentLine][5] = properties.getProperty(currentLine
          + ".animationFormat");
    }
    urlPos = 4;

    return true;
  }

  public String convert(String element) {
    return "<html><body>" + element + "</body></html>";
  }

  public JTable createTable() {
    if (labels == null || entries == null || refresh)
      readTableEntries();
    refresh = false;

    theTable = new JTable(entries, labels);
    theTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    return theTable;
  }

  public InputStream getResourceStream() {
    try {
//      if (!InetAddress.getByName(RESOURCE_FILENAME).isReachable(20000))
//        throw new IOException("time out");
      URL theURL = new URL(RESOURCE_FILENAME);
      if (theURL != null)
        return theURL.openStream();
    } catch (IOException e) {
      MessageDisplay.errorMsg(AnimalTranslator
          .translateMessage("repositoryConnectFailed"),
          MessageDisplay.RUN_ERROR);
    }
    return null;
  }

  public void actionPerformed(ActionEvent event) {
    if (event.getSource() == cancelButton) {
      theFrame.setVisible(false);
    } else if (event.getSource() == refreshButton) {
      readTableEntries();
      createTable();
    } else if (event.getSource() == acceptButton) {
      int selectedRow = theTable.getSelectedRow();
      if (selectedRow >= 0) {
        Animal.get();
        try {
          URL connector = new URL(entries[selectedRow][urlPos]);
          InputStream in = connector.openStream();
          AnimationImporter.importAnimation(in, entries[selectedRow][urlPos]);
          // now also show the animation!!!
          AnimationWindow animWin = AnimalMainWindow.getWindowCoordinator()
              .getAnimationWindow(true);
          animWin.setVisible(true);
          animWin.startOfAnimation();
          animWin.setTitle("Animal Animation: " + entries[selectedRow][0]);
        } catch (IOException e) {
          MessageDisplay.errorMsg("repConnFailed",
              entries[selectedRow][urlPos], MessageDisplay.RUN_ERROR);
        }
      }
    }
  }

  public boolean useRefresh() {
    return refresh;
  }

  public void buildGUI(String frameTitle) {
    if (theFrame != null)
      theFrame.setVisible(true);
    else {
      theFrame = new JFrame(frameTitle);
      theFrame.getContentPane().setLayout(new BorderLayout());
      theTable = createTable(); // better with params?!?
      JScrollPane scrollPane = new JScrollPane(theTable);

      theFrame.getContentPane().add(BorderLayout.CENTER, scrollPane);

      JPanel buttonPanel = new JPanel();
      acceptButton = AnimalTranslator.getGUIBuilder().generateJButton(
          "repAccept");
      refreshButton = AnimalTranslator.getGUIBuilder().generateJButton(
          "repRefresh");
      cancelButton = AnimalTranslator.getGUIBuilder().generateJButton(
          "repCancel");
      buttonPanel.add(acceptButton);
      buttonPanel.add(refreshButton);
      buttonPanel.add(cancelButton);
      acceptButton.addActionListener(this);
      refreshButton.addActionListener(this);
      cancelButton.addActionListener(this);

      theFrame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);

      theFrame.pack();
    }
  }

  /**
   * determines if the frame is supposed to be visible or not
   * 
   * @param isVisible
   *          if true, show the frame, else hide it
   */
  public void setVisible(boolean isVisible) {
    if (theFrame != null)
      theFrame.setVisible(isVisible);
  }
}
