/*
 * Example for an interactive Generator
 * Christian Dreger, 2018 for the Animal project at TU Darmstadt.
 */
package generators.interactive;

import generators.backtracking.CSP;
import generators.cryptography.BB84;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.InteractiveActionButton;
import generators.framework.InteractiveGenerator;
import generators.framework.InteractivityJob;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.LinkedList;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.BidirectionalDijkstra2;
import generators.graph.EdmondsAlgorithm;
import generators.graph.Tarjan;
import generators.graph.bronkerbosch.BronKerboschWithoutPivoting;
import generators.hashing.Adler32;
import generators.hashing.OpenAddressingHashing;
import generators.helpers.NodeElem;
import generators.misc.processScheduling.ProcessSchedulingEDF;
import generators.searching.interpolatedsearching.InterpolationSearchWrapper;
import generators.tree.TreeLabeling;
import interactionsupport.models.MultipleChoiceQuestionModel;
import generators.interactive.helperLists.MyListElement;
import generators.misc.processScheduling.ProcessEDF;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.bbcode.Graph;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import algorithm.search.InterpolationSearch;
import animal.animator.Move;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;

@SuppressWarnings("unused")
public class DoublyLinkedListInteractiv extends InteractiveGenerator implements ValidatingGenerator {

    public static void main(String[] args){
      Generator g = new DoublyLinkedListInteractiv();
      g.init();
//      Animal.startAnimationFromAnimalScriptCode(g.generate(null, null));
      Animal.startGeneratorWindow(g);
    }

    @Override
    public Language getAnimalScriptInstance() {
      return lang;
    }

    public void init(){
        lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
        lang.setStepMode(true);
    }

    public String getName() {
        return "DoublyLinkedList (Interactive)";
    }

    public String getAlgorithmName() {
        return "DoublyLinkedList (Interactive)";
    }

    public String getAnimationAuthor() {
        return "Christian Dreger";
    }

    public String getDescription(){
        return 
            "In computer science, a doubly linked list is a linked data structure that consists of a set of sequentially linked records called nodes.\n"+
            "Each node contains two fields, called links, that are references to the previous and to the next node in the sequence of nodes.\n"+
            "The beginning and ending nodes' previous and next links, respectively, point to some kind of terminator, typically a sentinel node or null, to facilitate traversal of the list.\n"+
            "If there is only one sentinel node, then the list is circularly linked via the sentinel node.\n"+
            "It can be conceptualized as two singly linked lists formed from the same data items, but in opposite sequential orders.\n";
    }

    public String getCodeExample(){
        return getSourcecodeAdd()+"\n\n\n"+getSourcecodeRemove()+"\n\n\n"+getSourcecodeSet();
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
      throws IllegalArgumentException {
        return true;
    }
  
    private Language lang;
    
    private String[] stringArray;
    private Vector<MyListElement> vectorElements;

    private SourceCode sc_add;
    private SourceCode sc_remove;
    private SourceCode sc_set;

    private Text infoText;

    private Text textGlobalVariable;
    private Text textGlobalVariable_first;
    private Text textGlobalVariable_last;
    private Text textGlobalVariable_size;

    private PolylineProperties ppF;
    private PolylineProperties ppL;
    private TextProperties textProps;
    private TextProperties textProps2;

    private MyListElement firstElement;
    private Polyline pl_first;
    private Polyline pl_first_v;

    private MyListElement lastElement;
    private Polyline pl_last;
    private Polyline pl_last_v;
    
    private int size = 0;

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
      
      // startInteractivity: Needed in every InteractiveGenerator. See function-javadoc!
      startInteractivity(new Runnable() {
        @Override
        public void run() {
          lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
          lang.setStepMode(true);
          stringArray = (String[]) primitives.get("startDoublyLinkedList");
          
          createIntro(getName(), getDescription(), true);
          
          InteractiveActionButton iab = createInteractiveActionButton(new Coordinates(200, 300), "Website Information", 20, null);
          InteractivityJob iJob = new InteractivityJob(iab, lang.getStep(), "Open", "Wikipedia: Doubly linked list") {
            @Override
            public void doAction() {
              try {
                java.awt.Desktop.getDesktop().browse(new URI("https://en.wikipedia.org/wiki/Doubly_linked_list"));
              } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
              }
            }
          };
          iJob.setInteractivityOnlyAtAnimationEnd(false);
          addInteractivityJob(iJob);
          iJob = new InteractivityJob(iab, lang.getStep(), "Open", "geeksforgeeks: Doubly linked list") {
            @Override
            public void doAction() {
              try {
                java.awt.Desktop.getDesktop().browse(new URI("https://www.geeksforgeeks.org/doubly-linked-list/"));
              } catch (IOException | URISyntaxException e) {}
            }
          };
          iJob.setInteractivityOnlyAtAnimationEnd(false);
          addInteractivityJob(iJob);
          
          lang.nextStep();
          iab.hide();
          SourceCodeProperties sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
          sc_add = lang.newSourceCode(new Coordinates(50, 400), "SourceCode", null, sourceCodeProperties);
          sc_add.addMultilineCode(getSourcecodeAdd(), "SourcecodeAdd", null);
          sc_remove = lang.newSourceCode(new Coordinates(50, 400), "SourceCode", null, sourceCodeProperties);
          sc_remove.addMultilineCode(getSourcecodeRemove(), "SourcecodeAdd", null);
          sc_set = lang.newSourceCode(new Coordinates(50, 400), "SourceCode", null, sourceCodeProperties);
          sc_set.addMultilineCode(getSourcecodeSet(), "SourcecodeAdd", null);
          sc_add.hide();
          sc_remove.hide();
          sc_set.hide();

          textProps = new TextProperties();
          textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
          textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
          infoText = lang.newText(new Coordinates(30, 360), "Info:", "infoText", null, textProps);
          infoText.hide();

          textGlobalVariable = lang.newText(new Coordinates(600, 50), "Global Variable:", "globalVariable", null, textProps);

          textProps2 = new TextProperties();
          textProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
          textGlobalVariable_first = lang.newText(new Offset(50, 0, textGlobalVariable, AnimalScript.DIRECTION_NE), "first = null", "globalVariable", null, textProps2);
          textGlobalVariable_last = lang.newText(new Offset(60, 0, textGlobalVariable_first, AnimalScript.DIRECTION_NE), "last = null", "globalVariable", null, textProps2);
          textGlobalVariable_size = lang.newText(new Offset(60, 0, textGlobalVariable_last, AnimalScript.DIRECTION_NE), "size = 0", "globalVariable", null, textProps2);

          firstElement = null;
          lastElement = null;
          ppF = new PolylineProperties();
          ppF.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
          ppF.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
          ppF.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(218,165,32));
          pl_first_v = lang.newPolyline(new Node[]{new Offset(-20, 0, textGlobalVariable_first, AnimalScript.DIRECTION_E), new Offset(20, 0, textGlobalVariable_first, AnimalScript.DIRECTION_E)}, null, null, ppF);
          ppL = new PolylineProperties();
          ppL.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
          ppL.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
          ppL.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(85,107,47));
          pl_last_v = lang.newPolyline(new Node[]{new Offset(-20, 0, textGlobalVariable_last, AnimalScript.DIRECTION_E), new Offset(20, 0, textGlobalVariable_last, AnimalScript.DIRECTION_E)}, null, null, ppL);
          pl_first_v.hide();
          pl_last_v.hide();
          pl_first = null;
          pl_last = null;
          
          size = 0;
          
          hideIntroDescription();
          
          createStartDoublyLinkedList(stringArray);
        }
      });
      
      return lang.toString();
    }
    
    /**
     * Set First Element and all Objects that have to do with it!
     * @param e
     */
    private void setFirst(MyListElement e) {
      if(e != null) {
        if(firstElement==null) {
          pl_first_v.show();
        }
        if(pl_first!=null) {
          pl_first.hide();
        }
        pl_first = lang.newPolyline(new Node[]{new Offset(-20, -40, e.getTextRect(), AnimalScript.DIRECTION_NW), new Offset(0, 0, e.getTextRect(), AnimalScript.DIRECTION_NW)}, null, null, ppF);
        textGlobalVariable_first.setText("first = ", null, null);
      } else {
        if(firstElement!=null) {
          pl_first_v.hide();
          pl_first.hide();
        }
        textGlobalVariable_first.setText("first = null", null, null);
      }
      firstElement = e;
    }

    /**
     * Set Last Element and all Objects that have to do with it!
     * @param e
     */
    private void setLast(MyListElement e) {
      if(e != null) {
        if(lastElement==null) {
          pl_last_v.show();
        }
        if(pl_last!=null) {
          pl_last.hide();
        }
        pl_last = lang.newPolyline(new Node[]{new Offset(20, -40, e.getTextRect(), AnimalScript.DIRECTION_NE), new Offset(0, 0, e.getTextRect(), AnimalScript.DIRECTION_NE)}, null, null, ppL);
        textGlobalVariable_last.setText("last = ", null, null);
      } else {
        if(lastElement!=null) {
          pl_last_v.hide();
          pl_last.hide();
        }
        textGlobalVariable_last.setText("last = null", null, null);
      }
      lastElement = e;
    }

    /**
     * Set size and all Objects that have to do with it!
     * @param size
     */
    private void setSize(int size) {
      this.size = size;
      textGlobalVariable_size.setText("size = "+size, null, null);
    }
    
    /**
     * Create a doublylinked list from String[]
     * @param stringArray
     */
    private void createStartDoublyLinkedList(String[] stringArray) {
      vectorElements = new Vector<MyListElement>();
      if(stringArray!=null && stringArray.length>0) {
        for(int i=0 ; i<stringArray.length ; i++) {
          vectorElements.add(new MyListElement(new Coordinates(100, 100), stringArray[i], lang));
        }
      }
      createStartDoublyLinkedList(vectorElements);
    }

    /**
     * Create a doublylinked list from Vector<MyListElement>. <br>
     * And call makeGoodStyleList()
     * @param vectorElements
     */
    private void createStartDoublyLinkedList(Vector<MyListElement> vectorElements) {
      for(int i=0 ; i<vectorElements.size() ; i++) {
        MyListElement elementCurrent = vectorElements.get(i);
        MyListElement elementBefore = i==0 ? null : vectorElements.get(i-1);
        MyListElement elementAfter = i+1==vectorElements.size() ? null : vectorElements.get(i+1);
        elementCurrent.setLastElementTo(elementBefore);
        elementCurrent.setNextElementTo(elementAfter);
      }
      makeGoodStyleList(new Coordinates(150, 175));
    }
    
    /**
     * Set all ListElements to their right position and update all objects.<br>
     * Add interactiveJobs to all Elements
     * @param startCoordinate Start Coordinate from the first listelement
     */
    private void makeGoodStyleList(Coordinates startCoordinate) {
      for(int i=0 ; i<vectorElements.size() ; i++) {
        MyListElement le = vectorElements.get(i);
        if(i==0) {
          le.setTextTo(startCoordinate);
        } else {
          MyListElement leB = vectorElements.get(i-1);
          le.setTextTo(new Offset(125, 10, leB.getTextRect(), AnimalScript.DIRECTION_NE));
        }
      }
      for(int i=0 ; i<vectorElements.size() ; i++) {
        MyListElement elementCurrent = vectorElements.get(i);
        MyListElement elementBefore = i==0 ? null : vectorElements.get(i-1);
        MyListElement elementAfter = i+1==vectorElements.size() ? null : vectorElements.get(i+1);
        
        elementCurrent.updatePointers();
        setFirst(vectorElements.firstElement());
        setLast(vectorElements.lastElement());
        setSize(vectorElements.size());

        final int index = i;
        
        String objectNameForAction = "Element(Index="+i+",Item="+elementCurrent.getItem()+")";
        
        ///////////////////////////////////////////////
        // add InteractivityJobs for current Element //
        ///////////////////////////////////////////////
        
        InteractivityJob iJob = new InteractivityJob(elementCurrent.getTextRect(), lang.getStep(), objectNameForAction, "Insert new Element at this Index") {
          int indexOfElement = vectorElements.indexOf(elementCurrent);
          @Override
          public void doAction() {
            String item = JOptionPane.showInputDialog(getParentComponent(), "Enter Item of new Element at Index "+index,"Item"+(int)(Math.random() * 1000 + 10));
            runTask(new Runnable() {
              @Override
              public void run() {
//                System.out.println("Insert Element at Index "+indexOfElement);
                addElementAtIndex(indexOfElement, item);
              }
            });
          }
        };
        addInteractivityJob(iJob);
        
        iJob = new InteractivityJob(elementCurrent.getTextRect(), lang.getStep(), "Insert new Element", "at Index "+i) {
          int indexOfElement = vectorElements.indexOf(elementCurrent);
          @Override
          public void doAction() {
            String item = JOptionPane.showInputDialog(getParentComponent(), "Enter Item of new Element at Index "+index,"Item"+(int)(Math.random() * 1000 + 10));
            runTask(new Runnable() {
              @Override
              public void run() {
//                System.out.println("Insert Element at Index "+indexOfElement);
                addElementAtIndex(indexOfElement, item);
              }
            });
          }
        };
        addInteractivityJob(iJob);
        
        if(i+1==vectorElements.size()) {
          iJob = new InteractivityJob(elementCurrent.getTextRect(), lang.getStep(), "Insert new Element", "at Index "+vectorElements.size()) {
            int indexOfElement = vectorElements.size();
            @Override
            public void doAction() {
              String item = JOptionPane.showInputDialog(getParentComponent(), "Enter Item of new Element at Index "+index,"Item"+(int)(Math.random() * 1000 + 10));
              runTask(new Runnable() {
                @Override
                public void run() {
//                  System.out.println("Insert Element at Index "+indexOfElement);
                  addElementAtIndex(indexOfElement, item);
                }
              });
            }
          };
          addInteractivityJob(iJob);
        }
        
        iJob = new InteractivityJob(elementCurrent.getTextRect(), lang.getStep(), objectNameForAction, "Remove this Element") {
          int indexOfElement = vectorElements.indexOf(elementCurrent);
          @Override
          public void doAction() {
            runTask(new Runnable() {
              @Override
              public void run() {
//                System.out.println("Remove Element at Index "+indexOfElement);
                removeElementAtIndex(indexOfElement);
              }
            });
          }
        };
        addInteractivityJob(iJob);

        iJob = new InteractivityJob(elementCurrent.getTextRect(), lang.getStep(), objectNameForAction, "Set Item for this Element") {
          int indexOfElement = vectorElements.indexOf(elementCurrent);
          @Override
          public void doAction() {
            String item = JOptionPane.showInputDialog(getParentComponent(), "Enter Item to set for Element at Index "+index,"Item"+(int)(Math.random() * 1000 + 10));
            runTask(new Runnable() {
              @Override
              public void run() {
//                System.out.println("Set Element at Index "+indexOfElement);
                setElementAtIndex(indexOfElement, item);
              }
            });
          }
        };
        addInteractivityJob(iJob);
      }
      infoText.setText("Info: Right-Click at a listelement to do a action", null, null);
      infoText.show();
    }
    
    /**
     * Add an Element at an index.<br>
     * And create all Animations.
     * @param index
     * @param element
     */
    private void addElementAtIndex(int index, String element) {
      Text textLocalVariable = lang.newText(new Offset(100, 0, sc_add, AnimalScript.DIRECTION_NE), "Local Variable:", "textLocalVariable", null, textProps);
      Text textLocalVariableNewElement = lang.newText(new Offset(50, 0, textLocalVariable, AnimalScript.DIRECTION_NE), "newElement = null", "textLocalVariableNewElement", null, textProps2);
      Text textLocalVariableLastElement = lang.newText(new Offset(60, 0, textLocalVariableNewElement, AnimalScript.DIRECTION_NE), "lastElement = null", "textLocalVariableLastElement", null, textProps2);
      Text textLocalVariableNextElement = lang.newText(new Offset(60, 0, textLocalVariableLastElement, AnimalScript.DIRECTION_NE), "nextElement = null", "textLocalVariableNextElement", null, textProps2);
      textLocalVariable.hide();
      textLocalVariableNewElement.hide();
      textLocalVariableLastElement.hide();
      textLocalVariableNextElement.hide();
      infoText.setText("Info: Call add("+index+", "+element+")", null, null);
      infoText.show();
      lang.nextStep();
      textLocalVariable.show();
      sc_add.show();
      sc_add.highlight(0);
      lang.nextStep("Call add("+index+", "+element+")");
      sc_add.unhighlight(0);
      sc_add.highlight(1);
      lang.nextStep();
      sc_add.unhighlight(1);
      sc_add.highlight(4);
      MyListElement v_lastElement = index==0 ? null : vectorElements.get(index-1);
      MyListElement v_nextElement = index+2>=vectorElements.size() ? null : vectorElements.get(index);
      Node c = new Coordinates(150, 270);
      if(v_lastElement!=null) {
        c = new Offset(50, 70, v_lastElement.getTextRect(), AnimalScript.DIRECTION_SE);
      }
      MyListElement newElement = new MyListElement(c, element, lang);
      textLocalVariableNewElement.show();
      textLocalVariableNewElement.setText("newElement = ", null, null);
      PolylineProperties ppNewElement = new PolylineProperties();
      ppNewElement.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
      ppNewElement.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
      ppNewElement.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0,0,255));
      Polyline plNewElement = lang.newPolyline(new Node[]{new Offset(0, 30, newElement.getTextRect(), AnimalScript.DIRECTION_S), new Offset(0, 0, newElement.getTextRect(), AnimalScript.DIRECTION_S)}, null, null, ppNewElement);
      Polyline plnewElementV = lang.newPolyline(new Node[]{new Offset(-20, 0, textLocalVariableNewElement, AnimalScript.DIRECTION_E), new Offset(20, 0, textLocalVariableNewElement, AnimalScript.DIRECTION_E)}, null, null, ppNewElement);
      lang.nextStep();
      sc_add.unhighlight(4);
      sc_add.highlight(5);
      textLocalVariableLastElement.show();
      Polyline plLastElement = null;
      Polyline plLastElementV = null;
      if(v_lastElement!=null) {
        PolylineProperties ppLastElement = new PolylineProperties();
        ppLastElement.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        ppLastElement.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        ppLastElement.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(139,0,0));
        plLastElement = lang.newPolyline(new Node[]{new Offset(0, 30, v_lastElement.getTextRect(), AnimalScript.DIRECTION_S), new Offset(0, 0, v_lastElement.getTextRect(), AnimalScript.DIRECTION_S)}, null, null, ppLastElement);
        plLastElementV = lang.newPolyline(new Node[]{new Offset(-20, 0, textLocalVariableLastElement, AnimalScript.DIRECTION_E), new Offset(20, 0, textLocalVariableLastElement, AnimalScript.DIRECTION_E)}, null, null, ppLastElement);
        textLocalVariableLastElement.setText("lastElement = ", null, null);
      }
      lang.nextStep();
      sc_add.unhighlight(5);
      sc_add.highlight(6);
      textLocalVariableNextElement.show();
      Polyline plNextElement = null;
      Polyline plNextElementV = null;
      if(v_nextElement!=null) {
        PolylineProperties ppNextElement = new PolylineProperties();
        ppNextElement.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        ppNextElement.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        ppNextElement.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255,20,147));
        plNextElement = lang.newPolyline(new Node[]{new Offset(0, 30, v_nextElement.getTextRect(), AnimalScript.DIRECTION_S), new Offset(0, 0, v_nextElement.getTextRect(), AnimalScript.DIRECTION_S)}, null, null, ppNextElement);
        plNextElementV = lang.newPolyline(new Node[]{new Offset(-20, 0, textLocalVariableNextElement, AnimalScript.DIRECTION_E), new Offset(20, 0, textLocalVariableNextElement, AnimalScript.DIRECTION_E)}, null, null, ppNextElement);
        textLocalVariableNextElement.setText("nextElement = ", null, null);
      }
      lang.nextStep();
      sc_add.unhighlight(6);
      sc_add.highlight(7);
      lang.nextStep();
      sc_add.unhighlight(7);
      if(v_nextElement!=null) {
        sc_add.highlight(8);
        newElement.setNextElementTo(v_nextElement);
        lang.nextStep();
        sc_add.unhighlight(8);
        sc_add.highlight(9);
        v_nextElement.setLastElementTo(newElement);
        lang.nextStep();
        sc_add.unhighlight(9);
      } else {
        sc_add.highlight(11);
        setLast(newElement);
        lang.nextStep();
        sc_add.unhighlight(11);
      }
      sc_add.highlight(13);
      lang.nextStep();
      sc_add.unhighlight(13);
      if(v_lastElement!=null) {
        sc_add.highlight(14);
        newElement.setLastElementTo(v_lastElement);
        lang.nextStep();
        sc_add.unhighlight(14);
        sc_add.highlight(15);
        v_lastElement.setNextElementTo(newElement);
        lang.nextStep();
        sc_add.unhighlight(15);
      } else {
        sc_add.highlight(17);
        setFirst(newElement);
        lang.nextStep();
        sc_add.unhighlight(17);
      }
      sc_add.highlight(19);
      setSize(size+1);
      lang.nextStep();
      sc_add.unhighlight(19);
      sc_add.hide();
      infoText.hide();
      textLocalVariableNewElement.hide();
      textLocalVariableLastElement.hide();
      textLocalVariableNextElement.hide();
      plNewElement.hide();
      plnewElementV.hide();
      if(v_lastElement!=null) {
        plLastElement.hide();
        plLastElementV.hide();
      }
      if(v_nextElement!=null) {
        plNextElement.hide();
        plNextElementV.hide();
      }
      textLocalVariable.hide();
      vectorElements.add(index, newElement);
      createStartDoublyLinkedList(vectorElements);
    }

    /**
     * Remove Element at an index.<br>
     * And create all Animations.
     * @param index
     */
    private void removeElementAtIndex(int index) {
      Text textLocalVariable = lang.newText(new Offset(100, 0, sc_remove, AnimalScript.DIRECTION_NE), "Local Variable:", "textLocalVariable", null, textProps);
      Text textReturnValue = lang.newText(new Offset(100, 50, sc_remove, AnimalScript.DIRECTION_NE), "Return Value:", "textReturnValue", null, textProps);
      Text textLocalVariableRemoveElement = lang.newText(new Offset(50, 0, textLocalVariable, AnimalScript.DIRECTION_NE), "removeElement = null", "textLocalVariableRemoveElement", null, textProps2);
      Text textLocalVariableLastElement = lang.newText(new Offset(60, 0, textLocalVariableRemoveElement, AnimalScript.DIRECTION_NE), "lastElement = null", "textLocalVariableLastElement", null, textProps2);
      Text textLocalVariableNextElement = lang.newText(new Offset(60, 0, textLocalVariableLastElement, AnimalScript.DIRECTION_NE), "nextElement = null", "textLocalVariableNextElement", null, textProps2);
      Text textLocalVariableRetValue = lang.newText(new Offset(60, 0, textLocalVariableNextElement, AnimalScript.DIRECTION_NE), "retValue = null", "textLocalVariableRetValue", null, textProps2);
      textLocalVariable.hide();
      textReturnValue.hide();
      textLocalVariableRemoveElement.hide();
      textLocalVariableLastElement.hide();
      textLocalVariableNextElement.hide();
      textLocalVariableRetValue.hide();
      infoText.setText("Info: Call remove("+index+")", null, null);
      infoText.show();
      lang.nextStep();
      sc_remove.show();
      textLocalVariable.show();
      textReturnValue.show();
      sc_remove.highlight(0);
      lang.nextStep("Call remove("+index+")");
      sc_remove.unhighlight(0);
      sc_remove.highlight(1);
      lang.nextStep();
      sc_remove.unhighlight(1);
      sc_remove.highlight(4);
      MyListElement removeElement = vectorElements.get(index);
      textLocalVariableRemoveElement.show();
      textLocalVariableRemoveElement.setText("removeElement = ", null, null);
      PolylineProperties ppRemoveElement = new PolylineProperties();
      ppRemoveElement.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
      ppRemoveElement.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
      ppRemoveElement.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0,0,255));
      Polyline plRemoveElement = lang.newPolyline(new Node[]{new Offset(0, 30, removeElement.getTextRect(), AnimalScript.DIRECTION_S), new Offset(0, 0, removeElement.getTextRect(), AnimalScript.DIRECTION_S)}, null, null, ppRemoveElement);
      Polyline plRemoveElementV = lang.newPolyline(new Node[]{new Offset(-20, 0, textLocalVariableRemoveElement, AnimalScript.DIRECTION_E), new Offset(20, 0, textLocalVariableRemoveElement, AnimalScript.DIRECTION_E)}, null, null, ppRemoveElement);
      lang.nextStep();
      sc_remove.unhighlight(4);
      sc_remove.highlight(5);
      MyListElement v_lastElement = index==0 ? null : vectorElements.get(index-1);
      textLocalVariableLastElement.show();
      Polyline plLastElement = null;
      Polyline plLastElementV = null;
      if(v_lastElement!=null) {
        PolylineProperties ppLastElement = new PolylineProperties();
        ppLastElement.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        ppLastElement.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        ppLastElement.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(139,0,0));
        plLastElement = lang.newPolyline(new Node[]{new Offset(0, 30, v_lastElement.getTextRect(), AnimalScript.DIRECTION_S), new Offset(0, 0, v_lastElement.getTextRect(), AnimalScript.DIRECTION_S)}, null, null, ppLastElement);
        plLastElementV = lang.newPolyline(new Node[]{new Offset(-20, 0, textLocalVariableLastElement, AnimalScript.DIRECTION_E), new Offset(20, 0, textLocalVariableLastElement, AnimalScript.DIRECTION_E)}, null, null, ppLastElement);
        textLocalVariableLastElement.setText("lastElement = ", null, null);
      }
      lang.nextStep();
      sc_remove.unhighlight(5);
      sc_remove.highlight(6);
      MyListElement v_nextElement = index+1==vectorElements.size() ? null : vectorElements.get(index+1);
      textLocalVariableNextElement.show();
      Polyline plNextElement = null;
      Polyline plNextElementV = null;
      if(v_nextElement!=null) {
        PolylineProperties ppNextElement = new PolylineProperties();
        ppNextElement.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        ppNextElement.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        ppNextElement.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255,20,147));
        plNextElement = lang.newPolyline(new Node[]{new Offset(0, 30, v_nextElement.getTextRect(), AnimalScript.DIRECTION_S), new Offset(0, 0, v_nextElement.getTextRect(), AnimalScript.DIRECTION_S)}, null, null, ppNextElement);
        plNextElementV = lang.newPolyline(new Node[]{new Offset(-20, 0, textLocalVariableNextElement, AnimalScript.DIRECTION_E), new Offset(20, 0, textLocalVariableNextElement, AnimalScript.DIRECTION_E)}, null, null, ppNextElement);
        textLocalVariableNextElement.setText("nextElement = ", null, null);
      }
      lang.nextStep();
      sc_remove.unhighlight(6);
      sc_remove.highlight(7);
      String returnValue = removeElement.getItem();
      textLocalVariableRetValue.show();
      textLocalVariableRetValue.setText("retValue = "+returnValue, null, null);
      lang.nextStep();
      sc_remove.unhighlight(7);
      sc_remove.highlight(8);
      removeElement.setItem("null");
      plRemoveElement.hide();
      plRemoveElement = lang.newPolyline(new Node[]{new Offset(0, 30, removeElement.getTextRect(), AnimalScript.DIRECTION_S), new Offset(0, 0, removeElement.getTextRect(), AnimalScript.DIRECTION_S)}, null, null, ppRemoveElement);
      lang.nextStep();
      sc_remove.unhighlight(8);
      sc_remove.highlight(9);
      removeElement.setLastElementTo(null);
      lang.nextStep();
      sc_remove.unhighlight(9);
      sc_remove.highlight(10);
      removeElement.setNextElementTo(null);
      lang.nextStep();
      sc_remove.unhighlight(10);
      sc_remove.highlight(11);
      lang.nextStep();
      sc_remove.unhighlight(11);
      if(v_lastElement==null && v_nextElement==null) {
        sc_remove.highlight(12);
        setLast(null);
        lang.nextStep();
        sc_remove.unhighlight(12);
        sc_remove.highlight(13);
        setFirst(null);
        lang.nextStep();
        sc_remove.unhighlight(13);
      } else {
        sc_remove.highlight(14);
        lang.nextStep();
        sc_remove.unhighlight(14);
        sc_remove.highlight(15);
        lang.nextStep();
        sc_remove.unhighlight(15);
        if(v_nextElement==null) {
          sc_remove.highlight(16);
          setLast(v_lastElement);
          lang.nextStep();
          sc_remove.unhighlight(16);
          sc_remove.highlight(17);
          v_lastElement.setNextElementTo(null);
          lang.nextStep();
          sc_remove.unhighlight(17);
        }else if(v_lastElement==null) {
          sc_remove.highlight(18);
          lang.nextStep();
          sc_remove.unhighlight(18);
          sc_remove.highlight(19);
          setFirst(v_nextElement);
          lang.nextStep();
          sc_remove.unhighlight(19);
          sc_remove.highlight(20);
          v_nextElement.setLastElementTo(null);
          lang.nextStep();
          sc_remove.unhighlight(20);
        } else {
          sc_remove.highlight(18);
          lang.nextStep();
          sc_remove.unhighlight(18);
          sc_remove.highlight(21);
          lang.nextStep();
          sc_remove.unhighlight(21);
          sc_remove.highlight(22);
          v_lastElement.setNextElementTo(v_nextElement);
          lang.nextStep();
          sc_remove.unhighlight(22);
          sc_remove.highlight(23);
          v_nextElement.setLastElementTo(v_lastElement);
          lang.nextStep();
          sc_remove.unhighlight(23);
        }
      }
      removeElement.hide();
      plRemoveElement.hide();
      sc_remove.highlight(26);
      setSize(size-1);
      lang.nextStep();
      sc_remove.unhighlight(26);
      sc_remove.highlight(27);
      Text textReturnValueValue = lang.newText(new Offset(50, 0, textReturnValue, AnimalScript.DIRECTION_NE), returnValue, "textReturnValueValue", null, textProps2);
      lang.nextStep();
      sc_remove.unhighlight(27);
      sc_remove.hide();
      infoText.hide();
      textLocalVariableRemoveElement.hide();
      textLocalVariableLastElement.hide();
      textLocalVariableNextElement.hide();
      textLocalVariableRetValue.hide();
      //plRemoveElement.hide();
      plRemoveElementV.hide();
      if(v_lastElement!=null) {
        plLastElement.hide();
        plLastElementV.hide();
      }
      if(v_nextElement!=null) {
        plNextElement.hide();
        plNextElementV.hide();
      }
      textReturnValueValue.hide();
      textLocalVariable.hide();
      textReturnValue.hide();
      vectorElements.remove(index);
      createStartDoublyLinkedList(vectorElements);
    }

    /**
     * Set an Element at an index.<br>
     * And create all Animations.
     * @param index
     * @param element
     */
    private void setElementAtIndex(int index, String element) {
      Text textLocalVariable = lang.newText(new Offset(100, 0, sc_set, AnimalScript.DIRECTION_NE), "Local Variable:", "textLocalVariable", null, textProps);
      Text textReturnValue = lang.newText(new Offset(100, 50, sc_set, AnimalScript.DIRECTION_NE), "Return Value:", "textReturnValue", null, textProps);
      Text textLocalVariableReplaceElement = lang.newText(new Offset(50, 0, textLocalVariable, AnimalScript.DIRECTION_NE), "replaceElement = null", "textLocalVariableReplaceElement", null, textProps2);
      Text textLocalVariableRetValue = lang.newText(new Offset(60, 0, textLocalVariableReplaceElement, AnimalScript.DIRECTION_NE), "retValue = null", "textLocalVariableRetValue", null, textProps2);
      textLocalVariable.hide();
      textReturnValue.hide();
      textLocalVariableReplaceElement.hide();
      textLocalVariableRetValue.hide();
      infoText.setText("Info: Call set("+index+", "+element+")", null, null);
      infoText.show();
      lang.nextStep();
      sc_set.show();
      textLocalVariable.show();
      textReturnValue.show();
      sc_set.highlight(0);
      lang.nextStep("Call set("+index+", "+element+")");
      sc_set.unhighlight(0);
      sc_set.highlight(1);
      lang.nextStep();
      sc_set.unhighlight(1);
      sc_set.highlight(4);
      MyListElement replaceElement = vectorElements.get(index);
      textLocalVariableReplaceElement.show();
      textLocalVariableReplaceElement.setText("replaceElement = ", null, null);
      PolylineProperties ppReplaceElement = new PolylineProperties();
      ppReplaceElement.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
      ppReplaceElement.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
      ppReplaceElement.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0,0,255));
      Polyline plReplaceElement = lang.newPolyline(new Node[]{new Offset(0, 30, replaceElement.getTextRect(), AnimalScript.DIRECTION_S), new Offset(0, 0, replaceElement.getTextRect(), AnimalScript.DIRECTION_S)}, null, null, ppReplaceElement);
      Polyline plReplaceElementV = lang.newPolyline(new Node[]{new Offset(-20, 0, textLocalVariableReplaceElement, AnimalScript.DIRECTION_E), new Offset(20, 0, textLocalVariableReplaceElement, AnimalScript.DIRECTION_E)}, null, null, ppReplaceElement);
      lang.nextStep();
      sc_set.unhighlight(4);
      sc_set.highlight(5);
      String returnValue = replaceElement.getItem();
      textLocalVariableRetValue.show();
      textLocalVariableRetValue.setText("retValue = "+returnValue, null, null);
      lang.nextStep();
      sc_set.unhighlight(5);
      sc_set.highlight(6);
      replaceElement.setItem(element);
      if(returnValue.length()<element.length()) {
        createStartDoublyLinkedList(vectorElements);
      }
      plReplaceElement.hide();
      plReplaceElement = lang.newPolyline(new Node[]{new Offset(0, 30, replaceElement.getTextRect(), AnimalScript.DIRECTION_S), new Offset(0, 0, replaceElement.getTextRect(), AnimalScript.DIRECTION_S)}, null, null, ppReplaceElement);
      lang.nextStep();
      sc_set.unhighlight(6);
      sc_set.highlight(7);
      Text textReturnValueValue = lang.newText(new Offset(50, 0, textReturnValue, AnimalScript.DIRECTION_NE), returnValue, "textReturnValueValue", null, textProps2);
      lang.nextStep();
      sc_set.unhighlight(7);
      sc_set.hide();
      infoText.hide();
      textLocalVariableReplaceElement.hide();
      textLocalVariableRetValue.hide();
      plReplaceElement.hide();
      plReplaceElementV.hide();
      textReturnValueValue.hide();
      textLocalVariable.hide();
      textReturnValue.hide();
      createStartDoublyLinkedList(vectorElements);
    }

    public String getSourcecodeAdd() {
      return
          /* 00 */"    public void add(int index, E element) {\n"+
          /* 01 */"        if (index > size || index < 0){\n"+
          /* 02 */"            throw new IndexOutOfBoundsException();\n"+
          /* 03 */"        }\n"+
          /* 04 */"        DoublyLinkedListElement<E> newElement = new DoublyLinkedListElement<E>(element);\n"+
          /* 05 */"        DoublyLinkedListElement<E> lastElement = (index > 0) ? getElementAt(index-1) : null;\n"+
          /* 06 */"        DoublyLinkedListElement<E> nextElement = lastElement != null ? lastElement.next : (index < size ? getElementAt(index) : null);\n"+
          /* 07 */"        if (nextElement != null){\n"+
          /* 08 */"            newElement.next = nextElement;\n"+
          /* 09 */"            nextElement.last = newElement;\n"+
          /* 10 */"        }else{\n"+
          /* 11 */"            last = newElement;\n"+
          /* 12 */"        }\n"+
          /* 13 */"        if (lastElement != null){\n"+
          /* 14 */"            newElement.last = lastElement;\n"+
          /* 15 */"            lastElement.next = newElement;\n"+
          /* 16 */"        }else{\n"+
          /* 17 */"            first = newElement;\n"+
          /* 18 */"        }\n"+
          /* 19 */"        size++;\n"+
          /* 20 */"    }"
          ;
    }

    public String getSourcecodeRemove() {
      return
          /* 00 */"    public E remove(int index) {\n"+
          /* 01 */"        if (index >= size || index < 0){\n"+
          /* 02 */"            throw new IndexOutOfBoundsException();\n"+
          /* 03 */"        }\n"+
          /* 04 */"        DoublyLinkedListElement<E> removeElement = getElementAt(index);\n"+
          /* 05 */"        DoublyLinkedListElement<E> lastElement = removeElement.last;\n"+
          /* 06 */"        DoublyLinkedListElement<E> nextElement = removeElement.next;\n"+
          /* 07 */"        E retValue = removeElement.item\n"+
          /* 08 */"        removeElement.item = null;\n"+
          /* 09 */"        removeElement.last = null;\n"+
          /* 10 */"        removeElement.next = null;\n"+
          /* 11 */"        if (lastElement == null && nextElement == null){\n"+
          /* 12 */"            last = null;\n"+
          /* 13 */"            first = null;\n"+
          /* 14 */"        }else{\n"+
          /* 15 */"            if (nextElement == null){\n"+
          /* 16 */"                last = lastElement;\n"+
          /* 17 */"                lastElement.next = null;\n"+
          /* 18 */"            }else if (lastElement == null){\n"+
          /* 19 */"                first = nextElement;\n"+
          /* 20 */"                nextElement.last = null;\n"+
          /* 21 */"            }else{\n"+
          /* 22 */"                lastElement.next = nextElement;\n"+
          /* 23 */"                nextElement.last = lastElement;\n"+
          /* 24 */"            }\n"+
          /* 25 */"        }\n"+
          /* 26 */"        size--;\n"+
          /* 27 */"        return retValue;\n"+
          /* 28 */"    }"
          ;
    }

    public String getSourcecodeSet() {
      return
          /* 00 */"    public E set(int index, E element) {\n"+
          /* 01 */"        if (index >= size || index < 0){\n"+
          /* 02 */"            throw new IndexOutOfBoundsException();\n"+
          /* 03 */"        }\n"+
          /* 04 */"        DoublyLinkedListElement<E> replaceElement = getElementAt(index);\n"+
          /* 05 */"        E retValue = replaceElement.item;\n"+
          /* 06 */"        replaceElement.item = element;\n"+
          /* 07 */"        return retValue;\n"+
          /* 08 */"    }"
          ;
    }

}