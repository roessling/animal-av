package htdptl.gui;

import htdptl.exceptions.NoExpressionsException;
import htdptl.exceptions.TraceTooLargeException;
import htdptl.facade.Facade;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import animal.gui.AnimalMainWindow;
import animal.gui.WindowCoordinator;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.Animation;
import animalscript.core.AnimalScriptParser;

public class HtDPTLWizard {

  public static HtDPTLWizard      instance          = new HtDPTLWizard();

  private Facade                  facade            = new Facade();

  private JComponent              currentPage;
  @SuppressWarnings("unused")
  private JComponent              previousPage;

  private StartPage               startPage;
  private EnterHtDPTLCode         enterHtDPTLCode;
  private ExampleCollection       exampleCollection = new ExampleCollection();
  private ChooseExpressionsPage   chooseExpressionsPage;
  private FilterPage              filterPage        = new FilterPage(facade);
  private BatchPage               batchPage         = new BatchPage();

  private Container               pane;
  private JFrame                  frame;
  private ButtonPane              buttonPane;

  public static void main(String[] args) {
    HtDPTLWizard.instance.show();
  }
  
  public HtDPTLWizard() {

    enterHtDPTLCode = new EnterHtDPTLCode();

    // Create and set up the window.
    this.frame = new JFrame("HtDP-TL Visualization Wizard");
    this.pane = frame.getContentPane();

    // Set up the content pane.
    addComponents();

    // 
    FilterTableModel.getInstance().setFacade(facade);

    // Display the window.
    frame.pack();

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int w = frame.getSize().width;
    int h = frame.getSize().height;
    int x = (dim.width - w) / 2;

    // center vertically if there is enough room
    int y = (dim.height - frame.getSize().height < 100) ? 0
        : (dim.height - h) / 2;

    frame.setLocation(x, y);

  }

  public void show() {
    frame.setVisible(true);
  }

  public void addComponents() {

    enterHtDPTLCode.setAlignmentX(Component.CENTER_ALIGNMENT);
    enterHtDPTLCode.setBorder(BorderFactory.createLineBorder(Color.gray, 1));

    buttonPane = new ButtonPane(this);
    buttonPane.setAlignmentX(Component.RIGHT_ALIGNMENT);

    startPage = new StartPage();
    currentPage = startPage;

    pane.add(startPage, BorderLayout.NORTH);
    pane.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.PAGE_END);
    pane.add(buttonPane, BorderLayout.PAGE_END);
    pane.setPreferredSize(new Dimension(650, 650));

  }

  public void back() {
    if (currentPage == enterHtDPTLCode) {
      pane.remove(enterHtDPTLCode);
      pane.add(startPage, BorderLayout.CENTER);
      pane.validate();
      pane.repaint();
      currentPage = startPage;
    } else if (currentPage == exampleCollection) {
      pane.remove(exampleCollection);
      pane.add(startPage, BorderLayout.CENTER);
      pane.validate();
      pane.repaint();
      currentPage = startPage;
    } else if (currentPage == chooseExpressionsPage) {
      frame.setVisible(false);
//      pane.remove(chooseExpressionsPage);
//      pane.add(previousPage, BorderLayout.CENTER);
//      pane.validate();
//      pane.repaint();
//      currentPage = previousPage;
    } else if (currentPage == filterPage) {
      pane.remove(filterPage);
      pane.add(chooseExpressionsPage, BorderLayout.CENTER);
      pane.validate();
      pane.repaint();
      currentPage = chooseExpressionsPage;
    } else if (currentPage == batchPage) {
      pane.remove(batchPage);
      pane.add(startPage, BorderLayout.CENTER);
      pane.validate();
      pane.repaint();
      currentPage = startPage;
    }

    updateButtons();
  }

  public void next() {

    if (currentPage == startPage) {

      String choice = startPage.getChoice();
      if (choice.equals("enter")) {
        pane.remove(startPage);
        pane.add(enterHtDPTLCode);
        currentPage = enterHtDPTLCode;
      }
      if (choice.equals("load")) {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(pane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          FileReader fr;
          try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String content = "";
            String line;
            while ((line = br.readLine()) != null) {
              content += line + "\n";
            }
            br.close();
            toChooseExpressionPage(content);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        previousPage = startPage;
      }
      if (choice.equals("example")) {
        pane.remove(startPage);
        pane.add(exampleCollection);
        currentPage = exampleCollection;
      }
      if (choice.equals("batch")) {
        pane.remove(startPage);
        pane.add(batchPage);
        currentPage = batchPage;
      }
    } else if (currentPage == enterHtDPTLCode) {
      toChooseExpressionPage(enterHtDPTLCode.getProgram());
      previousPage = enterHtDPTLCode;
    } else if (currentPage == exampleCollection) {
      toChooseExpressionPage(exampleCollection.getProgram());
      previousPage = exampleCollection;

    } else if (currentPage == chooseExpressionsPage) {
      chooseExpressionsPage.doSelection();
      pane.remove(chooseExpressionsPage);
      currentPage = filterPage;
      pane.add(filterPage);
    }
    pane.validate();
    pane.repaint();
    updateButtons();
  }

  private void updateButtons() {
    if (facade.getExpressions().size() > 0) {
      buttonPane.finish.setEnabled(true);
    }
    buttonPane.back.setEnabled(currentPage != startPage);
    buttonPane.next.setEnabled(currentPage != filterPage && currentPage != batchPage);

  }

  public void parseProgram(String program) {
    toChooseExpressionPage(program);
  }
  private void toChooseExpressionPage(String program) {

    try {
      facade.input(program);
      pane.remove(currentPage);
      chooseExpressionsPage = new ChooseExpressionsPage(facade);
      pane.add(chooseExpressionsPage);
      currentPage = chooseExpressionsPage;
      buttonPane.setBackEnabled(true);
      pane.add(currentPage);
    } catch (NoExpressionsException e) {
      JOptionPane.showMessageDialog(frame,
          "No expressions found in the given program!");
    } catch (Exception e) {
      JOptionPane.showMessageDialog(frame,
          "The Program seems to contain errors!");
      e.printStackTrace();
    }

  }

  public void finish() {

    if (currentPage == chooseExpressionsPage) {
      chooseExpressionsPage.doSelection();
    }

    try {

      facade.animate();
      String script = facade.getScriptCode();

      AnimalScriptParser scriptParser = new AnimalScriptParser();

      scriptParser.generateStreamTokenizer(script, false);
      Animation tmpAnim = scriptParser.importAnimationFrom(new StringReader(
          script), true);

      Animal animalInstance = Animal.get();
      WindowCoordinator coord = AnimalMainWindow.getWindowCoordinator();
      if (animalInstance.setAnimation(tmpAnim)) {
        animalInstance.getAnimation().resetChange();
        animalInstance.setFilename("localBuffer");
        coord.getDrawWindow(false).setTitle(
            "Draw Window - "
                + AnimalConfiguration.getDefaultConfiguration()
                    .getCurrentFilename());
      }
      coord.getTimeLineWindow(false)
          .setVisible(true);
      coord.getAnimationWindow(false)
          .setVisible(true);
      frame.setVisible(false);
      animalInstance.getScriptInputWindow().setScriptingContent(script);
    } catch (TraceTooLargeException e) {
      System.out.println(e.getLog());
      JOptionPane.showMessageDialog(frame,
          "The visualisation contains to much steps! \n" + e.getLog());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(frame, "The Program contain errors!");
      e.printStackTrace();
      facade = new Facade();
      updateButtons();
    }

  }

  public void cancel() {
    frame.setVisible(false);
  }

  public void next(String mode) {
    startPage.setChoice(mode);
    next();
  }

  public JComponent getCurrentPage() {
    return currentPage;
  }

  public void reset() {
    instance = new HtDPTLWizard();
  }

}
