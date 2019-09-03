package animal.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalSliderUI;

import animal.api.FileDrop;
import animal.exchange.AnimationImporter;
import animal.gui.AnimalMainWindow;
import animal.gui.GraphicVector;
import animal.gui.VariableView;
import animal.main.icons.LoadIcon;
import animal.main.lookandfeel.AnimalStepBasicSliderUI;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;
import animalscript.core.AnimalScriptParser;
import generators.framework.Generator;
import generators.framework.InteractiveGenerator;
import generators.generatorframe.store.GetInfos;
import generators.generatorframe.store.SearchLoader;
import generators.generatorframe.view.GeneratorFrame;
import helpers.AnimalReader;
import interactionsupport.parser.AnimalscriptParser;
import translator.Translator;

/**
 * The Window that displays the animation. Contains a Panel for control (play,
 * rewind, forward...) in its south and an AnimationCanvas that contains the
 * GraphicObjects in its center.
 */

public class AnimationWindowView extends AnimalFrame{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private Translator translator;
  
  private Container workContainer = null;
  private JPanel underContentPane = null;
  private AnimationWindow controller = null;
  private Font              defaultFont      = new Font("Dialog", 0, 14);
  private Font              buttonFont       = new Font("Dialog.bold", 1, 12);
  private Font              labelFont        = new Font("Dialog.bold", 1, 12);

  private int               height           = 25;
  private int               width            = 25;
  private int               zoomCounter      = 0;

 
  /** AnimationCanvas to display the GraphicObjects. */
  private AnimationCanvas animationCanvas;
  
  private JPanel panelNorth = null;
  
  
  /**
   * construct an AnimationWindow. Actual initialization is done in
   * <code>init</code>.
   * 
   * @param animalInstance
   *          the current Animal instance
   * @param properties
   *          the current animation properties
   * @see #init()
   */
  public AnimationWindowView(Animal animalInstance, XProperties properties, AnimationWindow controller) {
    super(animalInstance, properties);
    this.controller = controller;
    setDependentContainer(getContentPane());
    this.translator = new Translator("AnimationFrame", Animal.getCurrentLocale());
  }

  /**
   * construct an AnimationWindow. Actual initialization is done in
   * <code>init</code>.
   * 
   * @param animalInstance
   *          the current Animal instance
   * @param properties
   *          the current animation properties
   * @param aContainer
   *          the container that contains this component
   * @see #init()
   */
  public AnimationWindowView(Animal animalInstance, XProperties properties,
      Container aContainer, AnimationWindow controller) {
    super(animalInstance, properties);
    this.controller = controller;
    setDependentContainer(aContainer);
    this.translator = new Translator("AnimationFrame", Animal.getCurrentLocale());
  }
	
  private JSlider sliderSteps;
  private JScrollPane scrollPaneAnimationCanvas;
  
  private JTextField stepNumberField;
  
  private JButton buttonFirstStep;
  private JButton buttonPlayAnimationForward;
  private JButton buttonPauseAnimation;
  private JButton buttonPlayAnimationBackward;
  private JButton buttonLastStep;
  private JButton buttonOneStepBackward;
  private JButton buttonOneStepForward;
  private JButton buttonUndoInteractionTask;
  private JButton buttonCallLatestStartetGenerator;
  private JButton buttonSnapshot;
  private JButton buttonVariables;
  private JButton buttonPlayerHooking;
  private JButton       tempbutton2;
  private JButton       buttonLabels;
  private JButton       buttonTriggerSettings;

  private JButton buttonQuestionMode;
  private JButton buttonQuestionsSave;

  private static double speedMinValue = 0.1;
  private static double speedMaxValue = AnimationWindow.MAX_SPEED_FACTOR;
  private static double speedStepValue = 0.5;
  private JSlider sliderSpeed;
  
  private static double magMinValue = 0.5;
  private static double magMaxValue = 4.0;
  private static double magStepValue = 0.25;
  private JSlider sliderMag;
  
  private JPanel        otherSettings;
  private JPanel        panelSettings;
  private JPanel        othersPanel;
  private JPanel                     sliderSpeedPanel;
  private Hashtable<Integer, JLabel> table;

  private Dimension     buttonMinSize  = new Dimension(30, 40);
  
  /**
   * initializes the AnimationWindow by adding the control panel and the
   * AnimationCanvas.
   */
  public void init() {
    super.init();
    super.pack();
    super.setLocationRelativeTo(null);
    
    setTitle("Animal Animation");
    workContainer().setLayout(new GridBagLayout());

    if (animationCanvas == null) {
      animationCanvas = new AnimationCanvas();
    }
 
    animationCanvas.setProperties(props);

    GridBagConstraints c = new GridBagConstraints();
    c.weightx = c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;

//    JPanel contentPane = (JPanel) workContainer();
    underContentPane = new JPanel(new BorderLayout(0, 0));
    addPanelFullScreenToWorkContainer(underContentPane);
//  setContentPane(contentPane);

    c.fill = GridBagConstraints.HORIZONTAL;
    
    Dimension dimFrame = new Dimension(1000, 750);
    setBounds(100, 100, dimFrame.width, dimFrame.height);
    setMinimumSize(new Dimension(800, 600));
    setPreferredSize(dimFrame);
    
    underContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    underContentPane.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Player", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    underContentPane.setLayout(new BorderLayout(0, 0));
    
    scrollPaneAnimationCanvas = new JScrollPane(animationCanvas);
    scrollPaneAnimationCanvas.getViewport().setPreferredSize(new Dimension(300, 300));
    scrollPaneAnimationCanvas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPaneAnimationCanvas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    animationCanvas.setPreferredSize(new Dimension(300, 300));
    
    JPanel panelSouth = new JPanel();
    panelSouth.setLayout(new BorderLayout(0, 0));
    panelSouth.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Animation Control", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));

    panelNorth = new JPanel();
    panelNorth.setLayout(new BorderLayout(0, 0));

    c.fill = GridBagConstraints.BOTH;

    panelSettings = new JPanel();
    panelSettings.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Animation Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    panelSettings.setLayout(new GridBagLayout());
    
    sliderMag = new JSlider();
    sliderMag.setMaximum((int)(magMaxValue*100));
    sliderMag.setMinimum((int)(magMinValue*100));
    sliderMag.setValue(100);
    sliderMag.setMajorTickSpacing(50);
    sliderMag.setMinorTickSpacing((int)(magStepValue*100));
    sliderMag.setPaintTicks(true);
    sliderMag.setPaintLabels(true);
    sliderMag.setFont(labelFont);
    sliderMag.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        int value = sliderMag.getValue();
        double magValue = (double)value/100;
        controller.setMagnification(magValue);
      }
    });
    JPanel sliderMagPanel = new JPanel();
    sliderMagPanel.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Zoom in %", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    sliderMagPanel.setLayout(new GridBagLayout());
    sliderMagPanel.add(sliderMag,c);
    
    sliderSpeed = new JSlider();
    sliderSpeed.setMaximum((int)(speedMaxValue*100));
    sliderSpeed.setMinimum(0);
    sliderSpeed.setValue(100);
    sliderSpeed.setMajorTickSpacing(100);
    sliderSpeed.setMinorTickSpacing((int)(speedStepValue*100));
    sliderSpeed.setPaintTicks(true);
    sliderSpeed.setPaintLabels(true);
    final double[] speedScale = {speedMinValue, 1.0, 2.0, 5.0, 10.0, speedMaxValue};
    table = new Hashtable<Integer, JLabel>();
    for(double labelValue : speedScale) {
      String labelName = String.valueOf(labelValue);
      if(labelName.endsWith(".0")) {
        labelName = labelName.substring(0, labelName.indexOf(".0"));
      }
      if(speedMinValue==labelValue) {
        labelName = " " + labelName + "      ";
        JLabel l = new JLabel(labelName);
        l.setFont(labelFont);
        table.put((int) (labelValue * 100), l);
      }else if(speedMinValue<labelValue && labelValue<=speedMaxValue) {
        JLabel l = new JLabel(labelName + "    ");
        l.setFont(labelFont);
        table.put((int) (labelValue * 100), l);
      } else {
        System.out.println(labelValue);
      }
    }
    sliderSpeed.setLabelTable(table);
    sliderSpeed.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        int value = sliderSpeed.getValue();
        double speedValue = (double)value/100;
        if(speedValue<=speedMinValue) {
          speedValue = speedMinValue;
        }
        controller.setSpeed(speedValue);
      }
    });
    sliderSpeedPanel = new JPanel();
    sliderSpeedPanel.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Speed (1=Normal)", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    sliderSpeedPanel.setLayout(new GridBagLayout());
    sliderSpeedPanel.add(sliderSpeed,c);
    


    buttonPlayerHooking = new JButton("Unhook Player");
    buttonPlayerHooking.setFocusPainted(false);
    buttonPlayerHooking.setFont(new Font(buttonPlayerHooking.getFont().getFontName(), Font.PLAIN, 10));
    buttonPlayerHooking.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.setAnimationPlayerHooked(!controller.isAnimationPlayerHooked());
        sliderSteps.requestFocus();
      }
    });
    
    buttonLabels = new JButton("Show Labels");
    buttonLabels.setFocusPainted(false);
    buttonLabels.setFont(new Font(buttonLabels.getFont().getFontName(), Font.PLAIN, 10));
    TimeLineWindow labelview = AnimalConfiguration.getDefaultConfiguration().getWindowCoordinator().getTimeLineWindow(false);
    labelview.setVisible(false);
    JList<Object> labellist = labelview.getLabelListCopy();
    JScrollPane scrollPaneLabelList = new JScrollPane(labellist);
    scrollPaneLabelList.setPreferredSize(new Dimension(250, -1));
    buttonLabels.addActionListener(new ActionListener() {
      boolean showLabelViewByPlayer = false;
      @Override
      public void actionPerformed(ActionEvent e) {
        if(showLabelViewByPlayer) {
          underContentPane.remove(scrollPaneLabelList);
          buttonLabels.setText("Show Labels");
        } else {
          underContentPane.add(scrollPaneLabelList, BorderLayout.EAST);
          buttonLabels.setText("Hide Labels");
        }
        showLabelViewByPlayer = !showLabelViewByPlayer;
        underContentPane.updateUI();
        underContentPane.repaint();
      }
    });

    buttonVariables = new JButton("Show Variables");
    buttonVariables.setFocusPainted(false);
    buttonVariables.setFont(new Font(buttonVariables.getFont().getFontName(), Font.PLAIN, 10));
    buttonVariables.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        VariableView vview = AnimalConfiguration.getDefaultConfiguration().getWindowCoordinator().getVariableView();
        vview.setVisible(!vview.isVisible());
        buttonVariables.setText((vview.isVisible() ? "Hide" :"Show")+" Variables");
      }
    });

    buttonQuestionMode = new JButton("Set Questions Off");
    buttonQuestionMode.setFocusPainted(false);
    buttonQuestionMode.setFont(new Font(buttonQuestionMode.getFont().getFontName(), Font.PLAIN, 10));
    buttonQuestionMode.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(controller.isShowQuestions()) {
          buttonQuestionMode.setText("Set Questions On");
        } else {
          buttonQuestionMode.setText("Set Questions Off");
        }
        controller.setShowQuestions(!controller.isShowQuestions());
      }
    });
    buttonQuestionMode.setEnabled(AnimalscriptParser.getLastInteractionFile()!=null);

    buttonQuestionsSave = new JButton("Save Question-File");
    buttonQuestionsSave.setFocusPainted(false);
    buttonQuestionsSave.setFont(new Font(buttonQuestionMode.getFont().getFontName(), Font.PLAIN, 10));
    buttonQuestionsSave.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        File iFile = AnimalscriptParser.getLastInteractionFile();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(new File("").getAbsolutePath()+File.separator+iFile.getName()));
        fileChooser.setDialogTitle("Save Question-File to");  
        FileFilter ft = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.addChoosableFileFilter(ft); 
        ft = new FileFilter() {
          @Override
          public String getDescription() {
            return null;
          }
          @Override
          public boolean accept(File f) {
            return f.getName().equals(iFile.getName());
          }
        };
        fileChooser.addChoosableFileFilter(ft);
        if (fileChooser.showSaveDialog(AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).getAnimationWindowView()) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(Files.readAllBytes(iFile.toPath()));
            out.close();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }//TODO
      }
    });
    buttonQuestionsSave.setEnabled(AnimalscriptParser.getLastInteractionFile()!=null);

    otherSettings = new JPanel();
    otherSettings.setMinimumSize(new Dimension(300, 70));
    otherSettings.setMaximumSize(new Dimension(300, 70));
    otherSettings.setPreferredSize(new Dimension(300, 70));
    otherSettings.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Others", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    otherSettings.setLayout(new GridLayout(3, 0));

    otherSettings.add(buttonPlayerHooking);
    otherSettings.add(buttonLabels);
    otherSettings.add(buttonVariables);
    otherSettings.add(buttonQuestionMode);
    otherSettings.add(buttonQuestionsSave);
    
    tempbutton2 = new JButton();
    tempbutton2.setFont(new Font(tempbutton2.getFont().getFontName(), Font.PLAIN, 10));
    tempbutton2.setEnabled(false);
    otherSettings.add(tempbutton2);
    
    panelSettings.add(sliderMagPanel,c);
    panelSettings.add(sliderSpeedPanel,c);
    panelSettings.add(otherSettings);
    
    c.fill = GridBagConstraints.HORIZONTAL;
    
    panelNorth.add(panelSettings, BorderLayout.CENTER);
    
//    contentPane.add(panelNorth, BorderLayout.NORTH);
    underContentPane.add(scrollPaneAnimationCanvas, BorderLayout.CENTER);
    underContentPane.add(panelSouth, BorderLayout.SOUTH);
    
    JPanel panelSliderButton = new JPanel(new BorderLayout(0, 0));

    sliderSteps = new JSlider(JSlider.HORIZONTAL);
    sliderSteps.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
          if(buttonPauseAnimation.isEnabled()) {
            buttonPauseAnimation.doClick();
          } else if(e.isControlDown() && buttonPlayAnimationBackward.isEnabled()) {
            buttonPlayAnimationBackward.doClick();
          } else if(buttonPlayAnimationForward.isEnabled()) {
            buttonPlayAnimationForward.doClick();
          }
        }
      }
    });
    Dimension dimProgress = new Dimension(dimFrame.width, 50);
    sliderSteps.setSize(dimProgress);
    sliderSteps.setPreferredSize(dimProgress);
    AnimalStepBasicSliderUI sui = new AnimalStepBasicSliderUI(sliderSteps) {
      protected void scrollDueToClickInTrack(int direction) {
        try {
          if(slider!=null) {
            int value = slider.getValue(); 
            if (slider.getOrientation() == JSlider.HORIZONTAL) {
                value = this.valueForXPosition(slider.getMousePosition().x);
            } else if (slider.getOrientation() == JSlider.VERTICAL) {
                value = this.valueForYPosition(slider.getMousePosition().y);
            }
            slider.setValue(value);
          }
        } catch (Exception e) {
        }
      }
    };
    sliderSteps.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
//        if(sliderSteps.getValueIsAdjusting()) {}
        int value = sliderSteps.getValue();
        controller.setStep(value, true);
      }
    });
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e1) {}
        sliderSteps.setUI(sui);
        setJSlider(1,false);
        if(System.getProperty("os.name").toLowerCase().contains("windows")) {
          sliderSpeed.setUI(new MetalSliderUI() {
            protected void scrollDueToClickInTrack(int direction) {
              try {
                if(slider!=null) {
                  int value = slider.getValue(); 
                  if (slider.getOrientation() == JSlider.HORIZONTAL) {
                      value = this.valueForXPosition(slider.getMousePosition().x);
                  } else if (slider.getOrientation() == JSlider.VERTICAL) {
                      value = this.valueForYPosition(slider.getMousePosition().y);
                  }
                  slider.setValue(value);
                }
              } catch (Exception e) {
              }
            }
          });
          sliderMag.setUI(new MetalSliderUI() {
            protected void scrollDueToClickInTrack(int direction) {
              try {
                if(slider!=null) {
                  int value = slider.getValue(); 
                  if (slider.getOrientation() == JSlider.HORIZONTAL) {
                      value = this.valueForXPosition(slider.getMousePosition().x);
                  } else if (slider.getOrientation() == JSlider.VERTICAL) {
                      value = this.valueForYPosition(slider.getMousePosition().y);
                  }
                  slider.setValue(value);
                }
              } catch (Exception e) {
              }
            }
          });
        }
      }
   });
    

    
    buttonSnapshot = new JButton();
    buttonSnapshot.setIcon(getImageIconPlayer("Button_Snapshot.png"));
    buttonSnapshot.setMinimumSize(buttonMinSize);
    buttonSnapshot.setPreferredSize(buttonMinSize);
    buttonSnapshot.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.setSnapshot();
        sliderSteps.requestFocus();
      }
    });

    buttonFirstStep = new JButton();
    buttonFirstStep.setIcon(getImageIconPlayer("Button_Start.png"));
    buttonFirstStep.setMinimumSize(buttonMinSize);
    buttonFirstStep.setPreferredSize(buttonMinSize);
    buttonFirstStep.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.startOfAnimation();
        sliderSteps.requestFocus();
      }
    });

    buttonPlayAnimationForward = new JButton();
    buttonPlayAnimationForward.setIcon(getImageIconPlayer("Button_PlayF.png"));
    buttonPlayAnimationForward.setMinimumSize(buttonMinSize);
    buttonPlayAnimationForward.setPreferredSize(buttonMinSize);
    buttonPlayAnimationForward.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.slideShow();
        sliderSteps.requestFocus();
      }
    });

    buttonPauseAnimation = new JButton();
    buttonPauseAnimation.setIcon(getImageIconPlayer("Button_Pause.png"));
    buttonPauseAnimation.setMinimumSize(buttonMinSize);
    buttonPauseAnimation.setPreferredSize(buttonMinSize);
    buttonPauseAnimation.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.pauseAnimation();
        sliderSteps.requestFocus();
      }
    });

    buttonPlayAnimationBackward = new JButton();
    buttonPlayAnimationBackward.setIcon(getImageIconPlayer("Button_PlayB.png"));
    buttonPlayAnimationBackward.setMinimumSize(buttonMinSize);
    buttonPlayAnimationBackward.setPreferredSize(buttonMinSize);
    buttonPlayAnimationBackward.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.reverseSlideShow();
        sliderSteps.requestFocus();
      }
    });

    buttonLastStep = new JButton();
    buttonLastStep.setIcon(getImageIconPlayer("Button_End.png"));
    buttonLastStep.setMinimumSize(buttonMinSize);
    buttonLastStep.setPreferredSize(buttonMinSize);
    buttonLastStep.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        controller.endOfAnimation();
        sliderSteps.requestFocus();
      }
    });
    

    buttonOneStepBackward = new JButton();
    buttonOneStepBackward.setIcon(getImageIconPlayer("Button_StepB.png"));
    buttonOneStepBackward.setMinimumSize(buttonMinSize);
    buttonOneStepBackward.setPreferredSize(buttonMinSize);
    buttonOneStepBackward.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.backwardAnimation();
        sliderSteps.requestFocus();
      }});
    
    stepNumberField = new JTextField();
    stepNumberField.setHorizontalAlignment(JTextField.CENTER);
    stepNumberField.setColumns(3);
    stepNumberField.setMinimumSize(new Dimension(30, buttonMinSize.height+1));
    stepNumberField.setPreferredSize(new Dimension(30, buttonMinSize.height+1));
    stepNumberField.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          controller.setStep(getNearestStepFrom(Integer.parseInt(stepNumberField.getText())), true);
        } catch (NumberFormatException e2) {
          stepNumberField.setText(String.valueOf(ani.getStep()));
        }
      }});

    buttonOneStepForward = new JButton();
    buttonOneStepForward.setIcon(getImageIconPlayer("Button_StepF.png"));
    buttonOneStepForward.setMinimumSize(buttonMinSize);
    buttonOneStepForward.setPreferredSize(buttonMinSize);
    buttonOneStepForward.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.forwardAnimation();
        sliderSteps.requestFocus();
      }});
    
    buttonUndoInteractionTask = new JButton();
    buttonUndoInteractionTask.setIcon(getImageIconPlayer("Button_Undo.png"));
    buttonUndoInteractionTask.setMinimumSize(buttonMinSize);
    buttonUndoInteractionTask.setPreferredSize(buttonMinSize);
    buttonUndoInteractionTask.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        Generator generator = GetInfos.getInstance().getLatestStartetGenerator();
        InteractiveGenerator iGenerator = null;
        if(generator!=null && generator instanceof InteractiveGenerator) {
          iGenerator = (InteractiveGenerator) generator;
        }
        if(iGenerator!=null && iGenerator.isInteractive() && iGenerator.getInteractivityJobList()!=null) {
          boolean removeLastTaskSuccess = iGenerator.removeLastTask();
          if(!removeLastTaskSuccess) {
            System.out.println("RemoveLastTaski was not Successfull!");
          }
        }
      }});
    buttonUndoInteractionTask.setEnabled(false);
    
    buttonCallLatestStartetGenerator = new JButton();
    buttonCallLatestStartetGenerator.setIcon(getImageIconPlayer("Button_LatestGenerator.png"));
    buttonCallLatestStartetGenerator.setMinimumSize(buttonMinSize);
    buttonCallLatestStartetGenerator.setPreferredSize(buttonMinSize);
    buttonCallLatestStartetGenerator.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if(GetInfos.getInstance().getGlobalIndex()!=-1) {
          Animal.get().openGenerator();
          GeneratorFrame generatorFrame = (GeneratorFrame)Animal.get().getStarter().getFrame();
          SearchLoader.getInstance().setSelectedGenerator(SearchLoader.getInstance().getSelectedGeneratorIndex());
          generatorFrame.update(SearchLoader.getInstance(), null);
          generatorFrame.update(SearchLoader.getInstance(), "selected");
          generatorFrame.update(SearchLoader.getInstance(), "generator");
        }
      }});
    buttonCallLatestStartetGenerator.setEnabled(false);
    
    
    
    JPanel slideshowPanel = new JPanel(new GridBagLayout());
    slideshowPanel.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Slideshow", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    
    slideshowPanel.add(buttonPlayAnimationForward,c);
    slideshowPanel.add(buttonPauseAnimation,c);
    slideshowPanel.add(buttonPlayAnimationBackward,c);

    JPanel navigationPanel = new JPanel(new GridBagLayout());
    navigationPanel.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Navigation", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    
    navigationPanel.add(buttonFirstStep,c);
    navigationPanel.add(buttonOneStepBackward,c);
    navigationPanel.add(stepNumberField,c);
    navigationPanel.add(buttonOneStepForward,c);
    navigationPanel.add(buttonLastStep,c);
    navigationPanel.add(buttonUndoInteractionTask,c);

    othersPanel = new JPanel(new GridBagLayout());
    othersPanel.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Others", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
    
    buttonTriggerSettings = new JButton(
        (panelNorth.isValid() ? "Hide" : "Show") + " Settings");
    buttonTriggerSettings.setFocusPainted(false);
    buttonTriggerSettings.setMinimumSize(new Dimension(120, buttonMinSize.height));
    buttonTriggerSettings.setPreferredSize(new Dimension(120, buttonMinSize.height));
    buttonTriggerSettings.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if(panelNorth.isValid()) {
          underContentPane.remove(panelNorth);
          buttonTriggerSettings.setText("Show Settings");
        } else {
          underContentPane.add(panelNorth, BorderLayout.NORTH);
          buttonTriggerSettings.setText("Hide Settings");
        }
        underContentPane.updateUI();
      }});
    

    othersPanel.add(buttonSnapshot,c);
    othersPanel.add(buttonCallLatestStartetGenerator,c);
    othersPanel.add(buttonTriggerSettings,c);

    
    JPanel panelButtons = new JPanel();
    panelButtons.setLayout(new GridBagLayout());
    
    panelButtons.add(slideshowPanel,c);
    panelButtons.add(navigationPanel,c);
    panelButtons.add(othersPanel,c);

    panelSliderButton.add(sliderSteps, BorderLayout.CENTER);
    panelSliderButton.add(createEmptyJPanelWithSize(new Dimension(10, sliderSteps.getHeight())), BorderLayout.WEST);
    panelSliderButton.add(createEmptyJPanelWithSize(new Dimension(10, sliderSteps.getHeight())), BorderLayout.EAST);
//    panelSouth.add(createEmptyJPanelWithSize(new Dimension(-1, 5)), BorderLayout.NORTH);
    panelSouth.add(panelSliderButton, BorderLayout.CENTER);
    panelSouth.add(panelButtons, BorderLayout.SOUTH);
    
    changeLocale(getLocale());

    //setProperties(props);
    //this.setExtendedState(Frame.MAXIMIZED_BOTH);
    //this.setMaximumSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-300));
    
    sliderSteps.requestFocus();

    
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    WindowListener exitListener = new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        controller.setAnimationPlayerHooked(true);
      }
    };
    addWindowListener(exitListener);
    
    new FileDrop(this, new FileDrop.Listener(){
      @Override
      public void filesDropped(File[] files) {
//        for(File file : files) {
//          System.out.println(file.getAbsolutePath());
//        }
        if(files.length==1) {
//        if(AnimationImporter.importAnimation(files[0].getAbsolutePath())) {
//          System.out.println("LoadSuccsess");
//        }else{
//          System.out.println("Not LoadSuccsess");
//        }
          String filePath = files[0].getAbsolutePath();
          AnimalScriptParser animalScriptParser = Animal.getAnimalScriptParser(true);
          Animation newAnim = animalScriptParser.importAnimationFrom(filePath, true);
          if(AnimalScriptParser.fileContents.toString().startsWith("%Animal")) {
            String extension = "";
            int i = filePath.lastIndexOf('.');
            if (i > 0) {
                extension = filePath.substring(i+1);
            }
            if(AnimationImporter.finalizeAnimationLoading(newAnim, filePath, extension)) {
              Animal.get().setAnimalScriptCode(AnimalScriptParser.fileContents.toString());
            }
          } else {
            MessageDisplay.errorMsg("You can only drag&drop a file with AnimalScript-Code!", MessageDisplay.RUN_ERROR);
          }
        } else {
          MessageDisplay.errorMsg("You can only drag&drop ONE file!", MessageDisplay.RUN_ERROR);
        }
      }
    });
    
  }
  
  public void addPanelFullScreenToWorkContainer(JPanel panel) {
    GridBagConstraints c = new GridBagConstraints();
    c.weightx = c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    workContainer().add(panel,c);
  }
  
  private void setJSlider(int steps, boolean paintLabels) {
    if(sliderSteps!=null) {
      int maxLabels = 8;
      sliderSteps.setMaximum(steps);
      sliderSteps.setMinimum(1);
      sliderSteps.setValue(1);
      int mats = (int) Math.max(1,Math.floor(steps/Double.valueOf(maxLabels-1)));
      int mits = (int) Math.ceil(steps/100.0);
      sliderSteps.setMajorTickSpacing(mats);
      sliderSteps.setMinorTickSpacing(mits);
      sliderSteps.setPaintTicks(paintLabels);
      sliderSteps.setPaintLabels(paintLabels);
      sliderSteps.setLabelTable(sliderSteps.createStandardLabels(mats));
      @SuppressWarnings("unchecked")
      Dictionary<Integer, JLabel> dicLabelTable = sliderSteps.getLabelTable();
      HashMap<Integer, JLabel> labelTable = dictionaryToHashMap(dicLabelTable);
      boolean needFirstLabel = true;
      int highestLabel = -1;
      for (Map.Entry<Integer, JLabel> entry : labelTable.entrySet()) {
        Integer key = entry.getKey();
        @SuppressWarnings("unused")
        JLabel val = entry.getValue();
        if(key.intValue()==1) {
          needFirstLabel = false;
        }
        if(key.intValue()>=highestLabel) {
          highestLabel = key.intValue();
        }
      }
      if(needFirstLabel) {
        dicLabelTable.put(Integer.valueOf(1), new JLabel(String.valueOf(1)));
      }
      int checkstep = steps-mats/2;
      if(checkstep<=highestLabel) {
        dicLabelTable.remove(Integer.valueOf(highestLabel));
      }
      dicLabelTable.put(Integer.valueOf(steps), new JLabel(String.valueOf(steps)));
      sliderSteps.setLabelTable(dicLabelTable);
    }else {
      System.err.println("JSlider was null, but should not be");
    }
  }

    public void changeLocale(Locale targetLocale) {
      translator.setTranslatorLocale(Animal.getCurrentLocale());
      stepNumberField.setToolTipText(translator.translateMessage("number"));
      buttonOneStepBackward.setToolTipText(translator.translateMessage("backwards"));
      buttonFirstStep.setToolTipText(translator.translateMessage("first"));
      buttonOneStepForward.setToolTipText(translator.translateMessage("forwards"));
      buttonLastStep.setToolTipText(translator.translateMessage("last"));
      sliderMag.setToolTipText(translator.translateMessage("zoom"));
      buttonSnapshot.setToolTipText(translator.translateMessage("snapshot"));
      buttonPauseAnimation.setToolTipText(translator.translateMessage("stop"));
      buttonPlayAnimationForward.setToolTipText(translator.translateMessage("play"));
      buttonPlayAnimationBackward.setToolTipText(translator.translateMessage("reverse"));
      sliderSpeed.setToolTipText(translator.translateMessage("speedSlider"));
      buttonUndoInteractionTask.setToolTipText(translator.translateMessage("undoInteractionTask"));
      buttonCallLatestStartetGenerator.setToolTipText(translator.translateMessage("callLatestStartedGenerator"));
    }
    
    private <K, V> HashMap<K, V> dictionaryToHashMap(Dictionary<K, V> source) {
      HashMap<K, V> sink = new HashMap<K, V>();
        for (Enumeration<K> keys = source.keys(); keys.hasMoreElements();) {
            K key = keys.nextElement();
            sink.put(key, source.get(key));
        }
        return sink;
    }

    public JScrollPane getScrollPane() {
      return scrollPaneAnimationCanvas;
    }
    

    
    /**
     * initializes the AnimationWindow's bounds.
     * 
     * @param props
     *          the properties to set (concerns the window coordinates and
     *          bounds).
     */
    void setProperties(XProperties properties) {
      setBounds(properties.getIntProperty("animationWindow.x", 50), 
          properties.getIntProperty("animationWindow.y", 50), 
          properties.getIntProperty("animationWindow.width", 400),
          properties.getIntProperty("animationWindow.height", 200));
    }

    /**
     * stores the window location into the properties passed in
     * 
     * @param props
     *          the properties in which the window location is to be stored
     */
      void getProperties(XProperties properties) {
      Rectangle b = getBounds();

      if (((b.width - 10) == properties.getIntProperty("animationWindow.width", 320))
          && ((b.height - 22) == properties.getIntProperty("animationWindow.height",
              200))) {
        b.width -= 10;
        b.height -= 22;
      }

      properties.put("animationWindow.x", b.x);
      properties.put("animationWindow.y", b.y);
      properties.put("animationWindow.width", b.width);
      properties.put("animationWindow.height", b.height);
      }

    /**
     * Sets the container that contains this component
     * 
     * @param aContainer
     *          the container for this component
     */
    public void setDependentContainer(Container aContainer) {
      workContainer = aContainer;
    }
    
    public JPanel getUnderContentPane() {
      return underContentPane;
    }
    
    public AnimationCanvas getAnimationCanvas(){
        return animationCanvas;
    }
    
    public void setCanvasObject(GraphicVector g){
        animationCanvas.setObjects(g);
    }
    
    /**
     * returns the work container for this component
     * 
     * @return the work container of this component
     */
    public Container workContainer() {
      return workContainer;
    }
    
    public void setMagnification(double scale){
        animationCanvas.setMagnification(scale);
        sliderMag.setValue((int)(scale*100));
    }
    
    public double getMagnification(){
        return animationCanvas.getMagnification();
    }

    public double getMagnificationMaxValue() {
      return magMaxValue;
    }

    public double getMagnificationMinValue() {
      return magMinValue;
    }

    public double getMagnificationStepValue() {
      return magStepValue;
    }

    public JSlider getMagnificationSlider() {
      return sliderMag;
    }
    
    public void updateCanvas(){
      try {
        animationCanvas.repaintNow();
      } catch (Exception e) {}
    }
    
    protected Image getCurrentImage(){
        return animationCanvas.getCurrentImage();
    }
    
    public JButton getUHookPlayerButton() {
      return buttonPlayerHooking;
    }

    private AnimationState ani;
    public void setAnimationtate(AnimationState ani) {
      this.ani = ani;
      Animation animation = ani.getAnimation();
      try {
        setJSlider(animation.getMaxStepNum(),true);
      } catch (Exception e) {}
      setTitle("Animal Animation: "+animation.getTitle());
      updateAllToCurStep();
    }
    
    public void updateAllToCurStep() {
      if(ani!=null) {
        int firstStep = ani.getFirstRealStep();
        int curStep = ani.getStep();
        int lastStep = ani.getLastStep();
        sliderSteps.setValue(curStep);
        stepNumberField.setText(String.valueOf(curStep));
        
        buttonOneStepForward.setEnabled(curStep<lastStep);
        buttonLastStep.setEnabled(curStep<lastStep);
        buttonPlayAnimationForward.setEnabled(curStep<lastStep && !controller.getSlideShowMode());
        buttonOneStepBackward.setEnabled(curStep>firstStep);
        buttonFirstStep.setEnabled(curStep>firstStep);
        buttonPlayAnimationBackward.setEnabled(curStep>firstStep && !controller.getSlideShowMode());
        buttonPauseAnimation.setEnabled(controller.getSlideShowMode());
        
        buttonQuestionMode.setEnabled(AnimalscriptParser.getLastInteractionFile()!=null);
        buttonQuestionsSave.setEnabled(AnimalscriptParser.getLastInteractionFile()!=null);

        VariableView vview = AnimalConfiguration.getDefaultConfiguration().getWindowCoordinator().getVariableView();
        buttonVariables.setText((vview.isVisible() ? "Hide" :"Show")+" Variables");
        
        Generator generator = GetInfos.getInstance().getLatestStartetGenerator();
        InteractiveGenerator iGenerator = null;
        if(generator!=null && generator instanceof InteractiveGenerator) {
          iGenerator = (InteractiveGenerator) generator;
        }
        if(iGenerator!=null && iGenerator.isInteractive() && iGenerator.getInteractivityJobList()!=null) {
          buttonUndoInteractionTask.setEnabled(iGenerator.canRemoveLastTask());
        }
        buttonCallLatestStartetGenerator.setEnabled(GetInfos.getInstance().getGlobalIndex()!=-1);

        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            JPanel mainWindowContentPanel = (JPanel)workContainer;
            mainWindowContentPanel.updateUI();
          }
        });
      }
    }
    
    private JPanel createEmptyJPanelWithSize(Dimension dim) {
      JPanel panel = new JPanel();
      panel.setPreferredSize(dim);
      panel.setSize(dim);
      panel.setMinimumSize(dim);
      panel.setMaximumSize(dim);
      return panel;
    }
    
    private int getNearestStepFrom(int step) {
      int maxStep = ani.getAnimation().getMaxStepNum();
      int s = step;
      if(s <= 0){
        s = 1;
      }
      if(s > maxStep){
        s = maxStep;
      }
      return s;
    }
    
    private ImageIcon getImageIconPlayer(String name) {
      try {
        return new ImageIcon(AnimalReader.getImageIcon(AnimalReader.getInputStreamOnLayer(LoadIcon.class, name)).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
      } catch (Exception e) {
        return null;
      }
    }
    
    public boolean isSettingsVisible() {
      return panelNorth!=null && panelNorth.isValid();
    }

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Dimension dim = new Dimension(0, 0);
    @SuppressWarnings("unused")
    Dimension dimS = new Dimension(0, 0);

    if (otherSettings != null)
      dim = otherSettings.getSize();


    if (sliderSpeedPanel != null)
      dimS = sliderSpeedPanel.getSize();

    if (zoomIn) {
      if (zoomCounter < 6)
        zoomCounter++;
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }

      if (labelFont.getSize() < 16) {
        labelFont = new Font(labelFont.getFontName(), labelFont.getStyle(),
            labelFont.getSize() + 1);
      }


      if (dim.getHeight() < 116) {
        dim = new Dimension((int) dim.getWidth() + 40,
            (int) dim.getHeight() + 8);
      }

      if (height < 45) {
        height = height + 5;
        width = width + 5;
      }

    } else {
      if (zoomCounter > -1)
        zoomCounter--;
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }

      if (dim.getHeight() > 62) {
        dim = new Dimension((int) dim.getWidth() - 40,
            (int) dim.getHeight() - 8);
      }

      /*
       * if (dimS.getHeight() > 62) { dimS = new Dimension((int) dimS.getWidth()
       * - 40, (int) dimS.getHeight() - 8); }
       */
      if (height > 20) {
        height = height - 5;
        width = width - 5;
      }
      if (labelFont.getSize() > 9) {
        labelFont = new Font(labelFont.getFontName(), labelFont.getStyle(),
            labelFont.getSize() - 1);
      }

    }
    
    buttonFont = new Font(buttonFont.getFontName(), buttonFont.getStyle(),
        defaultFont.getSize() - 2);

    if (otherSettings != null) {

      if (dim.getHeight() < 70 + zoomCounter * 8)
        dim = new Dimension(300 + 40 * zoomCounter, 70 + 8 * zoomCounter);
      otherSettings.setMinimumSize(dim);
      otherSettings.setMaximumSize(dim);
      otherSettings.setPreferredSize(dim);
    }

    if (stepNumberField != null) {
      stepNumberField.setFont(defaultFont);
    }
    
    if (buttonFirstStep != null) {
      zoomButton(buttonFirstStep,buttonFont);
    }

    if (buttonPlayAnimationForward != null) {
      zoomButton(buttonPlayAnimationForward,buttonFont);
    }

    if (buttonPauseAnimation != null) {
      zoomButton(buttonPauseAnimation,buttonFont);
    }

    if (buttonPlayAnimationBackward != null) {
      zoomButton(buttonPlayAnimationBackward,buttonFont);
    }

    if (buttonLastStep != null) {
      zoomButton(buttonLastStep,buttonFont);
    }

    if (buttonOneStepBackward != null) {
      zoomButton(buttonOneStepBackward,buttonFont);
    }

    if (buttonOneStepForward != null) {
      zoomButton(buttonOneStepForward,buttonFont);
    }

    if (buttonUndoInteractionTask != null) {
      zoomButton(buttonUndoInteractionTask,buttonFont);
    }

    if (buttonCallLatestStartetGenerator != null) {
      zoomButton(buttonCallLatestStartetGenerator,buttonFont);
    }

    if (buttonSnapshot != null) {
      zoomButton(buttonSnapshot,buttonFont);
    }

    if (buttonVariables != null) {
      zoomButton(buttonVariables,buttonFont);
    }

    if (buttonPlayerHooking != null) {
      zoomButton(buttonPlayerHooking,buttonFont);
    }

    if (buttonQuestionMode != null) {
      zoomButton(buttonQuestionMode,buttonFont);
    }

    if (buttonQuestionsSave != null) {
      zoomButton(buttonQuestionsSave,buttonFont);
    }

    if (tempbutton2 != null) {
      zoomButton(tempbutton2,buttonFont);
    }

    if (buttonLabels != null) {
      zoomButton(buttonLabels,buttonFont);
    }

    if (buttonTriggerSettings != null) {
      zoomButton(buttonTriggerSettings, buttonFont);
    }

    if (sliderSpeed != null) {

      sliderSpeed.setFont(defaultFont);
    }
    
    if (sliderSpeed != null) {
      Enumeration<JLabel> it = table.elements();
      while (it.hasMoreElements()) {
        JLabel label = it.nextElement();
        label.setFont(labelFont);
      }


    }

    if (sliderMag != null) {
      sliderMag.setFont(labelFont);
    }

  }

  private void zoomButton(AbstractButton but, Font f) {

    if (but.getIcon() != null) {
      Image img = ((ImageIcon) but.getIcon()).getImage();
      Image newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      ImageIcon icon = new ImageIcon(newimg);
      but.setIcon(icon);
    }
  but.setFont(f);
  
  }

}
