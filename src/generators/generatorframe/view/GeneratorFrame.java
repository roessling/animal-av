package generators.generatorframe.view;

import generators.generatorframe.controller.Starter;
import generators.generatorframe.store.FilterInfo;
import generators.generatorframe.store.GetInfos;
import generators.generatorframe.store.SaveInfos;
import generators.generatorframe.store.SearchLoader;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Locale;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import translator.TranslatableGUIElement;
import translator.Translator;

/**
 * 
 * @author Nora Wester
 *
 */
public class GeneratorFrame extends JFrame implements Observer {

  /**
   * 
   */
  private static final long   serialVersionUID   = 1L;
  private static final double MINIMUM_WIDTH      = 800;
  private static final double MINIMUM_HEIGHT     = 500;

  private JSplitPane          splitPane;
  private int                 zoomCounter        = 0;

  int                         numberofGenerators = 0;

  TranslatableGUIElement      translater;

  String[]                    category;

  SaveInfos                   infos;

  Starter                     controller;

  // unterschiedliche Panels auf der rechten Seite
  AlgoListPanel               algoList;

  AlgoListing                 algoListing;

  AlgoTabPanel                algoTab;

  Listing                     list;
  private JLabel              ghostLabelError;
  private JLabel              ghostLabelNoAnimation;
  // ----

  // public GeneratorFrame(){
  //
  // super("Animation Content Generators");
  //
  // init();
  // }

  public GeneratorFrame(int numberOfGenerators, Starter controller,
      String[] category, Locale local) {

    super("Animation Content Generators");

    this.numberofGenerators = numberOfGenerators;
    this.controller = controller;
    this.category = category;
    translater = new TranslatableGUIElement(
        new Translator("GeneratorFrame", local));

    init();
  }

  private void init() {

    setContent(super.getContentPane());
    // set size automatically
    super.pack();
    // if frame is smaller than minimum, set size on minimum
    Dimension actualSize = super.getSize();
    int widthToSet = (int) actualSize.getWidth();
    if (actualSize.getWidth() < MINIMUM_WIDTH) {
      widthToSet = (int) MINIMUM_WIDTH;
    }

    int heightToSet = (int) actualSize.getHeight();
    if (actualSize.getHeight() < MINIMUM_HEIGHT) {
      heightToSet = (int) MINIMUM_HEIGHT;
    }

    super.setSize(widthToSet, heightToSet);
    // damit der User das Fenster nicht zu klein machen kann
    super.setMinimumSize(new Dimension(widthToSet, heightToSet));

    // centers the frame on screen
    super.setLocationRelativeTo(null);

    super.setVisible(true);

    // initalisiere schon einmal die anderen Panels
    int width = algoList.getWidth();
    // System.out.println(width);
    infos = SaveInfos.getInstance();

    algoListing = new AlgoListing(width, infos.getCodeLanguageArray(),
        infos.getLanguageArray(), translater);
    algoTab = new AlgoTabPanel(width, translater);
  }

  private void setContent(Container container) {
    // TODO Auto-generated method stub
    list = new Listing(category, translater);
    algoList = new AlgoListPanel(numberofGenerators);
    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, list, algoList);
    splitPane.setOneTouchExpandable(true);

    container.add(splitPane);

    ghostLabelError = translater.generateJLabel("OutOfRange");
    ghostLabelNoAnimation = translater.generateJLabel("NoAnimation");

  }

  // public static void main(String[] args) {
  // // TODO Auto-generated method stub
  // @SuppressWarnings("unused")
  // GeneratorFrame main = new GeneratorFrame();
  // }

  public void setVisible(boolean b) {
    // TODO Auto-generated method stub
    super.setVisible(b);
    if (b) {
      splitPane.setRightComponent(algoList);
    }

  }

  public void setGeneratorLocale(Locale targetLocale) {
    // TODO Auto-generated method stub
    translater.setTranslator(new Translator("GeneratorFrame", targetLocale));
    translater.translateGUIElements();
    algoListing.changeLocale();
    list.changeLocale();
    algoList.changeLocale();
    algoTab.changeLocale();
  }

  public void setAnimation(String content) {
    controller.setAnimation(content);
  }

  public void goBack() {
    splitPane.setRightComponent(algoListing);
    algoListing.clearSelected();
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    // TODO Auto-generated method stub

    if (arg0 instanceof FilterInfo) {
      algoListing.setNoFilter();
    }

    if (arg0 instanceof SearchLoader) {

      if (arg1 == null) {
        algoListing.setCategory(((SearchLoader) arg0).getSelectedCategory());
        return;
      }
      if (((String) arg1).compareTo("selected") == 0) {

        String[][] content = infos
            .getNameLangCodelang(((SearchLoader) arg0).getSelectedIndexes());
        algoListing.changeTableContent(content);
        splitPane.setRightComponent(algoListing);
        return;
      }
      if (((String) arg1).compareTo("generator") == 0) {

        algoTab.changeInfos();
        splitPane.setRightComponent(algoTab);
        list.clear();
      }
    }

    if (arg0 instanceof GetInfos) {
      if (arg1 != null) {
        if (arg1 instanceof String && ((String) arg1).compareTo("Error") != 0) {
          if (((String) arg1).compareTo("") == 0) {
            String text = ghostLabelNoAnimation.getText() + "\n"
                + GetInfos.getErrorMessage();
            JOptionPane.showMessageDialog(null, text, text,
                JOptionPane.WARNING_MESSAGE);
          } else {
            setAnimation((String) arg1);
            setVisible(false);
          }
        } else {
          String text = ghostLabelError.getText();
          JOptionPane.showMessageDialog(null, text, text,
              JOptionPane.WARNING_MESSAGE);
        }
      } else {
        algoTab.arrayChanged();
      }
    }

  }

  /**
   * zooms the window
   * 
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Font f = this.getFont();
    Font f1 = ghostLabelError.getFont();
    Font f2 = ghostLabelNoAnimation.getFont();
    Dimension dim = this.getSize();

    if (zoomIn) {
      if (zoomCounter < 6) {
        zoomCounter++;
      } else {
        return;
      }
      if (f.getSize() < 24)
        f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      if (f1.getSize() < 24)
        f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() + 2);
      if (f2.getSize() < 24)
        f2 = new Font(f2.getName(), f2.getStyle(), f2.getSize() + 2);
      if (dim.width <= 1200) {
        dim.setSize(dim.getWidth() + 52, dim.getHeight() + 52);
      }
    } else {
      if (zoomCounter > -1) {
        zoomCounter--;
      } else {
        return;
      }
      if (f.getSize() > 10)
        f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      if (f1.getSize() > 10)
        f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() - 2);
      if (f2.getSize() > 10)
        f2 = new Font(f2.getName(), f2.getStyle(), f2.getSize() - 2);
      if (dim.width >= 500) {
        dim.setSize(dim.getWidth() - 52, dim.getHeight() - 52);
      }
    }
    if (f.getSize() >= 10 && f.getSize() <= 24)
      this.setFont(f);
    this.setSize(dim);
    algoList.zoom(zoomIn);
    algoListing.zoom(zoomIn);
    algoTab.zoom(zoomIn, false);
    list.zoom(zoomIn);
    ghostLabelError.setFont(f1);
    ghostLabelNoAnimation.setFont(f1);
    this.repaint();

  }

}
