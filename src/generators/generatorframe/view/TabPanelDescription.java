package generators.generatorframe.view;


import generators.generatorframe.store.GetInfos;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;






import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import translator.TranslatableGUIElement;









public class TabPanelDescription extends JPanel {

  static GetInfos           algo;
  /**
   * @author Nora Wester
   * 
   */
  private static final long serialVersionUID = 1L;
  static JTextArea          text;

  public TabPanelDescription(int width, TranslatableGUIElement trans) {
    super();
    setPanel(width, trans);

  }

  static JLabel            name;
  static NamePanel         algoName;
  static JLabel            author;
  static JLabel            language;
  static JLabel            codeL;
  static JLabel            category;
  static JComboBox<String> change;
  private JLabel           gName;

  static JLabel            ghostD;
  static JLabel            ghostC;
  static int               index     = 0;

  private JPanel           animationInfo;
  private JPanel           firstRow;

  static boolean           canChange = true;
  private TranslatableGUIElement translate;

  // new global variables

  private ButtonPanel            buttonPanel;

  public void setContent() {

    String nameS = algo.getGeneratorName();

    // name.setText(nameS);
    // algoName.setMaximumSize(new Dimension(nameS.length()*20, 40));

    algoName.setLabel(nameS);
    // algoName.localChanged();

    algo.setNewLocale();
    author.setText(algo.getAuthor());
    codeL.setText(algo.getCodeLanguage());
    language.setText(algo.getLanguage());
    category.setText(algo.getCategory());
    gName.setText(algo.getName());

    change.setSelectedIndex(0);
  }

  public static void setTextArea(int i) {
    String textS = "";
    index = i;

    if (index == 0) {
      textS = algo.getDescription();
    } else {
      textS = algo.getCodeExample();
    }
    text.setText(textS);
    text.setCaretPosition(0);

  }
	
  public void setPanel(int width, TranslatableGUIElement trans) {
    // TODO Auto-generated constructor stub
    if (translate == null) {
      translate = trans;
    }
    algo = GetInfos.getInstance();

    // JPanel pane = new JPanel();
    super.setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    if (width < 800) {

    }
    mainPanel.setMinimumSize(new Dimension(width, 200));
    mainPanel.setBackground(Color.white);

    // Name des Algorithmuses.
    algoName = new NamePanel(algo.getGeneratorName());

    JPanel test = new JPanel();
    test.setLayout(new BoxLayout(test, BoxLayout.Y_AXIS));

    test.add(algoName);
    // test.add(new JPanel());
    super.add(test, BorderLayout.NORTH);

    //
    // String nameS = algo.getName();
    // name = new JLabel(nameS);
    // name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
    // algoName.add(name);
    // algoName.setBackground(Color.white);
    // //gr��e des Panels in Bezug auf die Namensl�nge berechnen
    // algoName.setMaximumSize(new Dimension(nameS.length()*20, 40));
    // mainPanel.add(algoName);
    // mainPanel.add(Box.createVerticalGlue());
    // some space between name and infos
    mainPanel.add(Box.createVerticalStrut(5));

    // eigenes Panel Informationen �ber die Animation
    animationInfo = new JPanel(new BorderLayout());
    animationInfo.setMaximumSize(new Dimension(width, 30));
    animationInfo.setBackground(Color.white);

    // author
    algo.setNewLocale();
    String authorS = algo.getAuthor();

    author = new JLabel(authorS);

    author.setText(authorS);
    // JTextPane authorP = new JTextPane();
    // authorP.setText(authorS);
    //
    // authorP.setContentType("text/html;charset=UTF-8");
    //
    // authorP.setEditorKit(new HTMLEditorKit());
    firstRow = new JPanel();
    firstRow.setLayout(new BorderLayout());
    if (width < 800)
      width = 800;
    firstRow.setMaximumSize(new Dimension(width, 30));
    firstRow.setMinimumSize(new Dimension(width, 30));
    
    firstRow.setBackground(Color.white);

    firstRow.add(author, BorderLayout.WEST);

    category = new JLabel(algo.getCategory());

    firstRow.add(category, BorderLayout.EAST);

    animationInfo.add(firstRow, BorderLayout.PAGE_START);

    // Panel for languages
    JPanel lPane = new JPanel(new BorderLayout());
    lPane.setMaximumSize(new Dimension(width, 30));
    lPane.setMinimumSize(new Dimension(width, 30));
    lPane.setBackground(Color.white);

    // code language
    codeL = new JLabel(algo.getCodeLanguage());
    lPane.add(codeL, BorderLayout.WEST);

    // language
    language = new JLabel(algo.getLanguage());
    lPane.add(language, BorderLayout.EAST);

    animationInfo.add(lPane, BorderLayout.CENTER);

    JPanel name = new JPanel();
    name.setLayout(new BorderLayout());
    name.setBackground(Color.WHITE);

    // Name
    gName = new JLabel(algo.getName());
    name.add(gName, BorderLayout.WEST);

    animationInfo.add(name, BorderLayout.SOUTH);

    mainPanel.add(animationInfo);

    // some space between info about the animation and info about the algorithm
    mainPanel.add(Box.createVerticalStrut(10));

    // Panel f�r den inhalt mit mehr Text
    JPanel infoPane = new JPanel(new BorderLayout());
    // infoPane.setMinimumSize(new Dimension(width, 200));
    infoPane.setBackground(Color.white);

    JPanel combo = new JPanel(new BorderLayout());
    combo.setBackground(Color.white);
    combo.setMaximumSize(new Dimension(100, 30));
    combo.setMinimumSize(new Dimension(100, 30));

    ghostD = trans.generateJLabel("description");
    ghostC = trans.generateJLabel("codeExample");

    change = trans.generateJComboBox("changeText", null,
        new String[] { ghostD.getText(), ghostC.getText() });

    combo.add(change, BorderLayout.WEST);
    change.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        int selected = change.getSelectedIndex();
        // System.out.println("hier"+ selected);
        if (selected != -1 && canChange) {
          setTextArea(selected);
        }
      }
    });

    infoPane.add(combo, BorderLayout.NORTH);

    String textS = algo.getDescription();
    text = new JTextArea();
    text.setEditable(false);
    // text.setContentType("text/html");
    text.setText(textS);
    text.setWrapStyleWord(true);
    text.setLineWrap(true);
    text.setCaretPosition(0);
    text.setMargin(new Insets(10, 10, 10, 10));
    // text.setMinimumSize(new Dimension(width, 200));
    JScrollPane scroll = new JScrollPane(text,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    infoPane.add(scroll, BorderLayout.CENTER);

    mainPanel.add(infoPane);
    mainPanel.add(Box.createVerticalGlue());

    super.add(mainPanel, BorderLayout.CENTER);
    // return pane;

    buttonPanel = new ButtonPanel("", "primitives", trans);
    super.add(buttonPanel, BorderLayout.SOUTH);
  }

  public void changeLocale() {

    // Unterstützung für den ActionListener
    canChange = false;

    change.removeItemAt(0);
    change.removeItemAt(0);

    change.addItem(ghostD.getText());
    change.addItem(ghostC.getText());
    change.setSelectedIndex(index);

    algo.setNewLocale();
    author.setText(algo.getAuthor());
    codeL.setText(algo.getCodeLanguage());
    language.setText(algo.getLanguage());

    canChange = true;
    // algoName.localChanged();
  }

  /**
   * zooms the tab in
   * 
   * @param zoomIn
   *          if true zooms in, if flase zooms out
   */

  public void zoom(boolean zoomIn, int zoomCounter) {

    Font f;

    if (name != null) {
      f = name.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      name.setFont(f);
    }

    if (text != null) {
      f = text.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      text.setFont(f);
    }

    if (author != null) {
      f = author.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      author.setFont(f);
    }

    if (language != null) {
      f = language.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      language.setFont(f);
    }
    if (codeL != null) {
      f = codeL.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      codeL.setFont(f);
    }

    if (category != null) {
      f = category.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      category.setFont(f);
    }

    if (gName != null) {
      f = gName.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      gName.setFont(f);
    }

    if (gName != null) {
      f = gName.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      gName.setFont(f);
    }

    if (ghostD != null) {
      f = ghostD.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      ghostD.setFont(f);
    }

    if (ghostC != null) {
      f = ghostC.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      ghostC.setFont(f);
    }


    if (algoName != null) {
      algoName.zoom(zoomIn);
    }


    // static JComboBox<String> change;

    if (change != null) {
      f = change.getFont();
      Dimension dimC = change.getSize();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
        if (dimC.getWidth() <= 1000) {
          dimC.setSize(dimC.getWidth() + 20, dimC.getHeight() + 20);
        }
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
        if (dimC.getWidth() >= 100) {
          dimC.setSize(dimC.getWidth() - 20, dimC.getHeight() - 20);
        }

      }
      change.setFont(f);
      change.setSize(dimC);
    }

    if (animationInfo != null) {

      Dimension dimC = animationInfo.getMaximumSize();
      if (zoomIn) {
        if (dimC.getWidth() <= 800) {
          dimC.setSize(dimC.getWidth() + 40, dimC.getHeight() + 5);
        }
      } else {

        if (dimC.getWidth() >= 260) {
          dimC.setSize(dimC.getWidth() - 40, dimC.getHeight() - 5);
        }

      }

      animationInfo.setMaximumSize(dimC);
      animationInfo.setMinimumSize(dimC);
      animationInfo.setSize(dimC);
    }

    if (firstRow != null) {

      Dimension dimC = firstRow.getMaximumSize();
      if (zoomIn) {
        if (dimC.getWidth() <= 980) {
          dimC.setSize(dimC.getWidth() + 40, dimC.getHeight() + 5);
        }
      } else {

        if (dimC.getWidth() >= 800) {
          dimC.setSize(dimC.getWidth() - 40, dimC.getHeight() - 5);
        }

      }

      if (this.getWidth() > 800) {
        dimC.setSize(this.WIDTH, dimC.getHeight());
      } else {
        dimC.setSize(800, dimC.getHeight());
      }

      firstRow.setMaximumSize(dimC);
      firstRow.setMinimumSize(dimC);
      firstRow.setSize(dimC);

    }

    Dimension dim = this.getSize();


    if (zoomIn) {

      if (dim.getWidth() <= 980) {
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      }
    } else {

      if (dim.getWidth() >= 700) {
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      }

    }
    this.setSize(dim);

    if (animationInfo != null) {
      Dimension dimC = this.getSize();
      dimC.setSize(dimC.width - 10, animationInfo.getHeight());
      if (zoomIn) {
        if (dimC.getHeight() <= 80) {
          dimC.setSize(dimC.getWidth() , dimC.getHeight() + 5);
        }
      } else {

        if (dimC.getWidth() >= 20) {
          dimC.setSize(dimC.getWidth() , dimC.getHeight() - 5);
        }
      }
      if (dimC.getWidth() < 800) {
        dimC.setSize(800, 30);
      }
        animationInfo.setMaximumSize(dimC);
        animationInfo.setMinimumSize(dimC);
        animationInfo.setSize(dimC);
      
    }

    if (buttonPanel != null) {

      buttonPanel.zoom(zoomIn, zoomCounter);
    }

    this.repaint();

  }
	
}
