package animal.gui;

import htdptl.gui.HtDPTLWizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import translator.ResourceLocator;
import animal.exchange.AnimalASCIIImporter;
import animal.exchange.AnimationImporter;
import animal.main.Animal;
import animal.main.Animation;
import extras.animalsense.evaluate.Exercise;
import extras.animalsense.ui.StartUI;

import examples.Exercises.ExercisesHelpFunctions;

public class LoadFromCollection extends JComponent implements ActionListener,
    TreeSelectionListener {

//  public static final int   SYNTAX           = 16;
//  public static final int   RACKET           = 32;
//  public static final int   GENERATORS       = 64;
//  public static final int   ALL              = 4096;

  private static final long serialVersionUID = 1L;
  private JTree             tree;
  private JFrame            frame;
  JEditorPane       textPane;
  JButton                   okButton, cancelButton;
  String            resourceChosen   = null;
  private AnimalCollectionTypes currentMode = AnimalCollectionTypes.ALL;
  private int                   zoomCounter      = 0;
  private Font                  defaultFont      = new Font("Dialog", 0, 14);
  
  public LoadFromCollection(Animal animalInstance, AnimalCollectionTypes mode) {
	  
    if (frame == null)
      frame = new JFrame();
    currentMode = mode;
    assembleGUI(currentMode);
  }

  private void assembleGUI(AnimalCollectionTypes mode) {
    textPane = new JEditorPane();
    textPane.setContentType("text/html");
    textPane.setEditable(false);
//    textPane.addHyperlinkListener(
//        new HyperlinkListener() {
//          public void hyperlinkUpdate(HyperlinkEvent e) {
//            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
//              if (e instanceof HTMLFrameHyperlinkEvent) {
//                ((JEditorPane)textPane.getDocument()).prprocessHTMLFrameHyperlinkEvent(
//                    (HTMLFrameHyperlinkEvent)e);
//              } else {
//                try {
//                  html.setPage(e.getURL());
//                } catch (IOException ioe) {
//                  System.out.println("IOE: " + ioe);
//                }
//              }
//            }
//          }
//        });

    Object[] hierarchy = getResources(mode);

    JPanel left = new JPanel();
    left.setLayout(new BorderLayout());
    left.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    DefaultMutableTreeNode root = processHierarchy(hierarchy);
    DefaultTreeModel model = new DefaultTreeModel(root, true);
    tree = new JTree(model);
    if(currentMode == AnimalCollectionTypes.EXERCISES){
        tree.expandRow(1);
    }
    // tree = create(hierarchy);
    tree.addTreeSelectionListener(this);

    JScrollPane scrollPaneLeft = new JScrollPane(tree);
    scrollPaneLeft.setBorder(new LineBorder(Color.gray, 1));
    left.setPreferredSize(new Dimension(300, 480));
    left.add(scrollPaneLeft, BorderLayout.CENTER);

    JPanel right = new JPanel();
    right.setLayout(new BorderLayout());
    right.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    JScrollPane scrollPaneRight = new JScrollPane(textPane);
    scrollPaneRight.setBorder(new LineBorder(Color.gray, 1));
    right.add(scrollPaneRight);

    JPanel top = new JPanel();
    top.add(new JLabel(
        "<html>Please choose a program from the example collection.<br />You can select the expressions to visualize "
            + "on the next page."));

    JPanel continuePanel = new JPanel();
    okButton = new JButton("OK");
    okButton.addActionListener(this);
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this);

    continuePanel.setLayout(new FlowLayout());
    continuePanel.add(okButton);
    continuePanel.add(cancelButton);

    setLayout(new BorderLayout());
    add(top, BorderLayout.PAGE_START);
    add(left, BorderLayout.WEST);
    add(right, BorderLayout.CENTER);
    add(continuePanel, BorderLayout.SOUTH);

    frame.getContentPane().add(this);
    frame.setMinimumSize(new Dimension(740, 480));
    frame.pack();
    frame.setVisible(true);

  }

  private Object[] generators = new Object[] {
      "examples",
      new Object[] { "Generators", new Object[] { "maths", "fastBitCount.asc" } } };

  private Object[] exerciseSheets = new Object[] {
      "examples",
      ExercisesHelpFunctions.getAllExercises()
  };
//  private Object[] exerciseSheets = new Object[] {
//	      "examples",
//	      new Object[] {
//	          "Exercises", 
//	          new Object[] {
//	              "Crypto", 
//	                "BinaerExponent1.xml", 
//	                "BinaerExponent2.xml",
//	                "Caesar1.xml", 
//	                "Caesar2.xml", 
//	                "CFB1.xml", 
//	                "CFB2.xml",
//	                "DiffieHellman1.xml", 
//	                "DiffieHellman2.xml", 
//	                "ElGamal1.xml", 
//	                "ElGamal2.xml",
//	                "OneTimePad1.xml",
//	                "OneTimePad2.xml",
//	                "OFB1.xml",
//	                "OFB2.xml", 
//	                "RSA1.xml",
//	                "RSA2.xml"
//	          },
//	          new Object[] {
//	              "Graphs",
//	              "BellmanFord.xml", "BreadthFirstSearch.xml", "DepthFirstSearch.xml",
//	              "DijkstraAlgorithm.xml", "DistanceVectorRouting.xml",
//	              "FloydWarshallAlgorithm.xml", "KruskalAlgorithm.xml",
//	              "RingElectionAlgorithm.xml"
//	          },
//	          new Object[] {
//	              "Searching",
//	              "BinarySearch.xml", "BinarySearch2.xml", "IterativeInterpolatedSearch.xml",
//	              "RecursiveInterpolatedSearch.xml"
//	          },
//	          new Object[] {
//	              "Sorting",
//	              "BucketSort.xml", "BucketSortDE.xml", "CocktailSortDE.xml", "CombSortDE.xml", 
//	              "CountingSortDE.xml", "EvenOddSortDE.xml", "GnomeSort.xml", "GnomeSortDE.xml",
//	              "Heapsort.xml", "InsertionSortDE.xml", "Quicksort.xml", "Quicksort2.xml", 
//	              "QuicksortDE.xml", "SelectionSort.xml", "SelectionSortDE.xml",
//	              "ShakerSort.xml", "ShakerSort2.xml", "ShakersortDE.xml", "Shellsort.xml",
//	              "ShellSortDE.xml", "StoogeSortDE.xml", "StrandSortDE.xml", "SwapSortDE.xml",
//	              "InsertionSort.xml", "SimpleSortDE.xml", "BubbleSort.xml"
//	          }
//
//	      }
//	  };

  private Object[] syntax     = new Object[] {
      "examples",
      new Object[] {
      "Syntaxdiagramme", "Dijkstra_BSP.aml", "do-while.aml", "for-Schleife.aml",
      "forSchleife.aml", "Funktionen.aml",
      "HTML-Elemente.aml", "HTML-Elemente1.aml", "HTML-Elemente2.aml",
      "if-Anweisung.aml", "Methoden.aml", "Prozedur.aml", "while.aml",
      new Object[] { "Q-Phase", new Object[] { "Methoden.aml" } }
      }
  };

  private Object[] htDPTL     = new Object[] {
      "examples",
      new Object[] {
      "HtDP",
      "T0.19.rkt", "T1.9.rkt","T1.13.rkt", "T1.21.rkt", "T1.22.rkt", "T1.33.rkt", 
      "T1.41.rkt", "T1.48.rkt", "T1.57.rkt", "T2.6.rkt", "T2.14-29.rkt", "T2.32-34.rkt", 
      "T2.35-37.rkt", "T3.4.rkt", "T3.6.rkt", "T3.19.rkt", "T3.22.rkt", "T3.23.rkt", 
      "T3.26.rkt", "T3.27.rkt", "T3.32-33.rkt", "T3.36.rkt", "T3.40-45.rkt",
      "T3.64-71.rkt", "T3.74-82.rkt", "T5.5-7.rkt", "T5.9-12.rkt", "T5.13.rkt", 
      "T5.16-18.rkt", "T5.20.rkt", "T5.39-50.rkt", "T5.54.rkt", "T5.57-59.rkt",
      "T5.64-65.rkt", "T6.21-22.rkt", "T7.32-33.rkt",
      "T7.38.rkt", "T7.65-70.rkt",
      new Object[] { "fold", "foldl.rkt", "foldr.rkt" },
      new Object[] { "map", "map.rkt" } 
      }
  };

  private Object[] getResources(AnimalCollectionTypes key) {
    Object[] result = null;
    if (key == AnimalCollectionTypes.SYNTAX)
      return syntax;
    else if (key == AnimalCollectionTypes.HTDP_TL)
      return htDPTL;
    else if (key == AnimalCollectionTypes.GENERATORS)
      return generators;
    else if (key == AnimalCollectionTypes.EXERCISES)
      return exerciseSheets;
    else {
      result = new Object[generators.length + htDPTL.length + syntax.length + exerciseSheets.length - 3];
      int pos = 0;
      System.arraycopy(generators, 0, result, 0, generators.length);
      pos = generators.length;
      System.arraycopy(htDPTL, 1, result, pos, htDPTL.length - 1);
      pos += htDPTL.length - 1;
      System.arraycopy(syntax, 1, result, pos, syntax.length - 1);
      pos += syntax.length - 1;
      System.arraycopy(exerciseSheets, 1, result, pos, exerciseSheets.length - 1);
      return result;
    }
  }

  private Reader getReader(String resource) {
    Reader in = null;
    try {
      InputStream is = ResourceLocator.getResourceLocator().getResourceStream(
          resource);
      if (resource.endsWith(".aml"))
        is = new GZIPInputStream(is);
      if(is==null){
      	return null;
      }
      in = new InputStreamReader(is, "UTF-8");
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return in;
  }
  
  private String readFrom(String resource) {
    String s = null;
    Reader in = null;
    try {
//      InputStream is = ResourceLocator.getResourceLocator().getResourceStream(
//          resource);
//      if (resource.endsWith(".aml"))
//        is = new GZIPInputStream(is);
//      in = new InputStreamReader(is, "UTF-8");
      in = getReader(resource);
      if(in==null){
    	  return "Class, ClassLoader and local file IO cannot allocate file for "+resource;
      }
      final char[] buffer = new char[0x10000];
      StringBuilder out = new StringBuilder();
      int read;
      do {
        read = in.read(buffer, 0, buffer.length);
        if (read > 0) {
          out.append(buffer, 0, read);
        }
      } while (read >= 0);
      s = out.toString();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return s;
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    TreeNode node = (DefaultMutableTreeNode) tree
        .getLastSelectedPathComponent();

    if (node == null) {
      resourceChosen = null;
      // Nothing is selected.
      return;
    }
    if (node.isLeaf() && !node.getAllowsChildren()) {

      String resource = node.toString();
      while (node.getParent() != null) {
        resource = node.getParent().toString() + "/" + resource;
        node = node.getParent();
      }
      final String finalResource = resource;
//      String descFileName = resource.substring(0, resource.lastIndexOf("."))
//          + ".txt";
//      contents = readFrom(descFileName);
//      if (contents == null)
//        contents = readFrom(resource);
      //TODO Add description from xml
      resourceChosen = null;
      if(finalResource.endsWith(".xml")) {
        textPane.setText("ExerciseInfo is loading..");
        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        String contents = null;
                      Exercise ex = StartUI.getExerciseInfos(finalResource);
                        if(ex!=null){
                          contents = ex.getDescription();
                          contents = contents.replaceAll("\n", "<br>");
                        }
                        
                        if (contents != null)
                          resourceChosen = finalResource;
                        textPane.setText(contents);
                        textPane.setCaretPosition(0);
                    }
                }, 
                5 
        );
      } else {
        textPane.setText("ResourceContent is loading..");
        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                      try {
                        ResourceLocator rl = ResourceLocator.getResourceLocator();
                        InputStreamReader readerTxt = new InputStreamReader(rl.getResourceStream(finalResource.substring(0, finalResource.lastIndexOf('.'))+".txt"), "UTF-8");
                        String contentTxt = null;
                        if(readerTxt!=null) {
                          contentTxt = new BufferedReader(readerTxt).lines()
                              .parallel().collect(Collectors.joining("\n"));
                        }
                        InputStreamReader readerOriginalFile = new InputStreamReader(rl.getResourceStream(finalResource), "UTF-8");
                        String contentOriginalFile = new BufferedReader(readerOriginalFile).lines()
                            .parallel().collect(Collectors.joining("\n"));
                        if(contentOriginalFile != null) {
                          resourceChosen = finalResource;
                          textPane.setText(contentTxt!=null ? contentTxt : contentOriginalFile);
                          textPane.setCaretPosition(0);
                        }
                      } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                      }
                    }
                }, 
                5 
        );
      }
      
    }
  }

  private static DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
    DefaultMutableTreeNode child;
    for (int i = 1; i < hierarchy.length; i++) {
      Object nodeSpecifier = hierarchy[i];
      if (nodeSpecifier instanceof Object[]){
        child = processHierarchy((Object[]) nodeSpecifier);
        if(child.getChildCount()==0){
        	child = new DefaultMutableTreeNode(child.toString());
        }
      } else {
        child = new DefaultMutableTreeNode(nodeSpecifier,false);
      }
      node.add(child);
    }
    return (node);
  }

  public String getProgram() {
    return textPane.getText();
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getSource() == okButton && resourceChosen!=null) {
//      System.err.println("load file " + resourceChosen);

      String format = "animation/";
      if (resourceChosen.endsWith(".asu"))
        format += "animalscript";
      else if (resourceChosen.endsWith(".asc"))
        format += "animalscript-compressed";
      else if (resourceChosen.endsWith(".aml"))
        format += "animal-ascii-compressed";
      else if (resourceChosen.endsWith(".ama"))
        format += "animal-ascii";
      else if (resourceChosen.endsWith(".rkt"))
        format += "racket";
      else if (resourceChosen.endsWith(".xml")) // && currentMode == AnimalCollectionTypes.EXERCISES)
        format += "exercise";
      if (format.indexOf("racket") != -1 ) {
        String program = readFrom(resourceChosen);
        HtDPTLWizard.instance.parseProgram(program);
        HtDPTLWizard.instance.show();
      } 
      else if (format.indexOf("exercise") != -1) {
//        System.err.println("Exercise sheet..." + resourceChosen);
        StartUI.showExercise(resourceChosen, Animal.get());
      }
      else 
      {
        AnimationImporter importer = AnimationImporter.getImporterFor(format);
        boolean success = false;
        if (importer instanceof AnimalASCIIImporter) {
          Animation anim = ((AnimalASCIIImporter)importer).importAnimationFrom(getReader(resourceChosen),
            format);
          success = AnimationImporter.finalizeAnimationLoading(anim, resourceChosen, format);
          success = (anim != null);
        }
        else {
          ResourceLocator rl = ResourceLocator.getResourceLocator();
          InputStream is = rl.getResourceStream(resourceChosen);
          success = AnimationImporter.importAnimation(is,
            format);
        }
        System.err.println(success);
      }
    } else if (event.getSource() == cancelButton) {
      System.err.println("Nothing to be done, bye");
      frame.setVisible(false);
      frame.dispose();
    }
  }

  /**
   * 
   * @param zoomIn
   *          if true zooms window in else zooms out
   */
  public void zoom(boolean zoomIn) {

    if (zoomIn) {
      if (zoomCounter < 6)
        zoomCounter++;
      if (defaultFont.getSize() < 24)
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);

    } else {
      if (zoomCounter > -1)
        zoomCounter--;
      if (defaultFont.getSize() > 10)
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);

    }

    if (okButton != null) {
      Font f = new Font(okButton.getFont().getFontName(),
          okButton.getFont().getStyle(), defaultFont.getSize());
      okButton.setFont(f);

    }

    if (cancelButton != null) {
      Font f = new Font(cancelButton.getFont().getFontName(),
          cancelButton.getFont().getStyle(), defaultFont.getSize());
      cancelButton.setFont(f);

    }

    if (tree != null) {
      Font f = new Font(tree.getFont().getFontName(), tree.getFont().getStyle(),
          defaultFont.getSize());
      tree.setFont(f);
    }

    if (textPane != null) {
      Font f = new Font(textPane.getFont().getFontName(),
          textPane.getFont().getStyle(), defaultFont.getSize());
      textPane.setFont(f);
    }

  }

}
