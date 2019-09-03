package generators.misc;

//package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class HPFgenerator implements Generator {
  public Language             lang;
  public TextProperties       textProp;
  public int[][]              inputProcesses;
  public ArrayProperties      arrayFillinProp;
  public SourceCodeProperties sourceCodeProp;
  public ArrayProperties      arrayProp;

  double                      waitingTime = 0.0;
  int                         execTime    = 0;

  int                         xx          = 0;
  int                         yy          = 0;

  boolean                     interaction = false;

  public HPFgenerator() {

  }

  public HPFgenerator(Language language, HPFgenerator hp) {
    this.lang = language;

    textProp = hp.textProp;
    inputProcesses = hp.inputProcesses;
    arrayFillinProp = hp.arrayFillinProp;
    sourceCodeProp = hp.sourceCodeProp;
    arrayProp = hp.arrayProp;
    waitingTime = hp.waitingTime;
    execTime = hp.execTime;
    xx = hp.xx;
    yy = hp.yy;
    interaction = hp.interaction;
  }

  public void init() {
    lang = new AnimalScript("Highest-Priority-First Sheduling with Interrupts",
        "Denis Neumann", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    textProp = (TextProperties) props.getPropertiesByName("textProp");
    inputProcesses = (int[][]) primitives.get("inputProcesses");
    arrayFillinProp = (ArrayProperties) props
        .getPropertiesByName("arrayFillinProp");
    sourceCodeProp = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProp");
    arrayProp = (ArrayProperties) props.getPropertiesByName("arrayProp");
    interaction = (Boolean) primitives.get("interaction");

    // System.out.println(inputProcesses[1][2]);//zeile-spalte=28

    // int[][] a = {{1,0,8,4},
    // {2,3,28,1},
    // {3,7,12,0},
    // {4,9,3,2},
    // {5,15,4,3}};

    for (int i = 0; i < inputProcesses.length; i++) {
      if (inputProcesses[i][2] != 0) {
        if (inputProcesses[i][1] > xx)
          xx = xx + inputProcesses[i][2] + inputProcesses[i][1] - xx;
        else
          xx = xx + inputProcesses[i][2];
        yy++;
      }
    }

    // System.out.println(x);
    // System.out.println(y);
    HPFgenerator pr = new HPFgenerator(lang, this);

    pr.startScheduling(inputProcesses, xx, yy);
    // //////////////////////////////////////////////////////////
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Highest-Priority-First Scheduling with Interrupts";
  }

  public String getAlgorithmName() {
    return "Scheduling";
  }

  public String getAnimationAuthor() {
    return "Denis Neumann";
  }

  public String getDescription() {
    return "	Highest-Priority-First Sheduling with Interrupts is used to "
        + "\n" + "	manage different non-parralel processes." + "\n"
        + "	This algorithm selects, which process is allowed to be " + "\n"
        + "	executed in a specific point of time." + "\n"
        + "	If a process with higher priority arrives,it is " + "\n"
        + "	immediately executed,while other processes should wait." + "\n"
        + "	As input the algorithm expects for each process its" + "\n"
        + "	arrival,duration and priority." + "\n" + "\n" + "	Note!" + "\n"
        + "\n In the next step you can select the property \"interaction\","
        + "\n and set its value to true, so that you can prove your"
        + "\n understanding of the algorithm by predicting its next moves."
        + "\n"
        + "	You can also chose the properties of the incomeing processes."
        + "\n" + "	Fill the matrix \"inputProcesses\" as follows:" + "\n"
        + "	    Column 0 = Number of the Process;" + "\n"
        + "	    Column 1 = Arrival of the Process;" + "\n"
        + "	    Column 2 = Duration of the Process;" + "\n"
        + "	    Column 3 = Priority of the Process;" + "\n" + "\n";
  }

  public String getCodeExample() {
    return "1	HPF-Sheduling(){" + "\n" + "2		while(true){" + "\n"
        + "3			select all active processes" + "\n"
        + "4			if(no active processes){" + "\n"
        + "5   	    	set the status of all processes on 'empty'" + "\n"
        + "6			   	wait one timestep" + "\n" + "7			}" + "\n" + "8   	  	else{"
        + "\n" + "9	  	   	chose the process with highest priority" + "\n"
        + "10		  		set the status of the chosen process on 'working'" + "\n"
        + "11			  	set the status of all other active processes on 'waiting'"
        + "\n" + "12		   		wait one timestep" + "\n" + "13			}" + "\n"
        + "14		}" + "\n" + "15	}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * Sort the int array passed in
   * 
   * @param a
   *          the array to be sorted
   * @param x
   * @param y
   */
  public void startScheduling(int[][] a, int x, int y) {
    if (interaction)
      lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    // show the header with a heading surrounded by a rectangle
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    lang.newText(new Coordinates(20, 30),
        "Highest-Priority-First Sheduling with Interrupts", "header", null,
        headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);

    TextProperties textProps3 = new TextProperties();
    // Color c =Color.BLACK;//
    // textProps3.get(AnimationPropertiesKeys.COLOR_PROPERTY);
    textProps3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    textProps3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    Text descr1 = lang.newText(new Coordinates(10, 150),
        "Highest-Priority-First Sheduling with Interrupts is used to ",
        "description1", null, textProps3);
    Text descr2 = lang.newText(new Offset(0, 25, "description1",
        AnimalScript.DIRECTION_NW), "manage different non-parralel processes.",
        "description2", null, textProps3);

    Text descr3 = lang.newText(new Offset(0, 25, "description2",
        AnimalScript.DIRECTION_NW),
        "This algorithm selects, which process is allowed to be ",
        "description3", null, textProps3);

    Text descr4 = lang.newText(new Offset(0, 25, "description3",
        AnimalScript.DIRECTION_NW), "executed in a specific point of time.",
        "description4", null, textProps3);

    Text descr5 = lang.newText(new Offset(0, 25, "description4",
        AnimalScript.DIRECTION_NW),
        "If a process with higher priority arrives,it is ", "description5",
        null, textProps3);

    Text descr6 = lang.newText(new Offset(0, 25, "description5",
        AnimalScript.DIRECTION_NW),
        "immediately executed,while other processes should wait.",
        "description6", null, textProps3);

    Text descr7 = lang.newText(new Offset(0, 25, "description6",
        AnimalScript.DIRECTION_NW),
        "As input the algorithm expects for each process its", "description7",
        null, textProps3);

    Text descr8 = lang.newText(new Offset(0, 25, "description7",
        AnimalScript.DIRECTION_NW), "arrival,duration and priority.",
        "description8", null, textProps3);

    lang.nextStep();
    descr1.hide();
    descr2.hide();
    descr3.hide();
    descr4.hide();
    descr5.hide();
    descr6.hide();
    descr7.hide();
    descr8.hide();

    // Create Array: coordinates, data, name, display options,
    // default properties

    // first, set the visual properties (somewhat similar to CSS)

    // ArrayList<ArrayProperties> arrayProps = new ArrayList<ArrayProperties>();
    ArrayList<StringArray> sa = new ArrayList<StringArray>();

    // ArrayList<ArrayProperties> arrayProps2 = new
    // ArrayList<ArrayProperties>();
    ArrayList<StringArray> na = new ArrayList<StringArray>();

    ArrayList<ArrayMarkerProperties> arrayJMProps = new ArrayList<ArrayMarkerProperties>();
    ArrayList<ArrayMarker> timeMarker = new ArrayList<ArrayMarker>();

//    ArrayList<Text> te = new ArrayList<Text>();

    String strArr2[][] = new String[y][x];
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {
        strArr2[j][i] = " X ";
      }

    }
    String strArr3[][] = new String[y][1];
    for (int i = 0; i < 1; i++) {
      for (int j = 0; j < y; j++) {
        strArr3[j][i] = "P" + (j + 1);
      }
    }

    for (int i = 0; i < y; i++) {
      {
        // coord(x,y)

        na.add(lang.newStringArray(new Coordinates(20, 175 + 25 * i),
            strArr3[i], "stringArray" + i,
            // null,arrayProps2.get(i)));
            null, arrayProp));

        // now, create the IntArray object, linked to the properties
        // coord(x,y)
        sa.add(lang.newStringArray(new Coordinates(50, 175 + 25 * i),
            strArr2[i], "stringArray" + i,
            // null,arrayProps.get(i)));
            null, arrayFillinProp));
      }

    }

    // set time markers for the first string array
    for (int i = 0; i < x; i++) {
      arrayJMProps.add(new ArrayMarkerProperties());
      arrayJMProps.get(i).set(AnimationPropertiesKeys.LABEL_PROPERTY,
          (i + 1) + "");
      arrayJMProps.get(i).set(AnimationPropertiesKeys.COLOR_PROPERTY,
          Color.GRAY);
      timeMarker.add(lang.newArrayMarker(sa.get(0), i, "time" + i, null,
          arrayJMProps.get(i)));
    }

    //
    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Offset(0, 60, "stringArray"
        + (y - 1), AnimalScript.DIRECTION_NW), "sourceCode", null,
        sourceCodeProp);
    // lang.newSourceCode(new Coordinates(40, 25*(y-5)+325), "sourceCode",
    // null, sourceCodeProp);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    // "1	HPF-Sheduling(){"
    // +"\n2		1while(true){"
    // +"\n3		2	select all active processes"
    // +"\n4		2	if(no active processes){"
    // +"\n5   	3    	set the status of all processes on 'empty'"
    // +"\n6		3	   	wait one timestep"
    // +"\n7		2	}"
    // +"\n8   	2  	else{"
    // +"\n9	  	3   	chose the process with highest priority"
    // +"\n10		3 		set the status of the chosen process on 'working'"
    // +"\n11		3	  	set the status of all other active processes on 'waiting'"
    // +"\n12		3  		wait one timestep"
    // +"\n13		2	}"
    // +"\n14		1}"
    // +"\n15	}";
    sc.addCodeLine("HPF-Sheduling(){", null, 0, null); // 0
    sc.addCodeLine("while(true){", null, 1, null); // 1
    sc.addCodeLine("select all active processes", null, 2, null); // 2
    sc.addCodeLine("if(no active processes){", null, 2, null); // 3
    sc.addCodeLine("set the status of all processes on 'empty'", null, 3, null); // 4
    sc.addCodeLine("wait one timestep", null, 3, null); // 5
    sc.addCodeLine("}", null, 2, null); // 6
    sc.addCodeLine("else{", null, 2, null); // 7
    sc.addCodeLine("chose the process with highest priority", null, 3, null); // 8
    sc.addCodeLine("set the status of the chosen process on 'working'", null,
        3, null); // 9
    sc.addCodeLine("set the status of all other active processes on 'waiting'",
        null, 3, null); // 10
    sc.addCodeLine("wait one timestep", null, 3, null); // 11
    sc.addCodeLine("}", null, 2, null); // 12
    sc.addCodeLine("}", null, 1, null); // 13
    sc.addCodeLine("}", null, 0, null); // 14

    // show task table
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    // lang.newText(new Coordinates(600, (y-5)*25+350),
    lang.newText(new Offset(550, 60, "stringArray" + (y - 1),
        AnimalScript.DIRECTION_NW),
        "Process\t  Arrival\t  Duration\t  Priority", "taskLine0", null,
        textProps);
    TextProperties textProps2 = new TextProperties();
    textProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 14));

    ArrayList<StringArray> na2 = new ArrayList<StringArray>();

    for (int i = 0; i < y; i++) {
      // na2.add(lang.newStringArray(new Coordinates(600, (y-5)*25+390+40*i),
      // strArr3[i], "stringArray2"+i,
      na2.add(lang.newStringArray(new Offset(0, 30 + 30 * i, "taskLine0",
          AnimalScript.DIRECTION_NW), strArr3[i], "stringArray2" + i,
      // null,arrayProps2.get(i)));
          null, arrayProp));
      // lang.newText(new Coordinates(670, (y-5)*25+390+40*i),
      lang.newText(new Offset(70, 30 + 30 * i, "taskLine0",
          AnimalScript.DIRECTION_NW), "" + a[i][1], "taskCell0" + (i + 1)
          + "_0", null, textProp);
      // lang.newText(new Coordinates(600+70*2, (y-5)*25+390+40*i),
      lang.newText(new Offset(140, 30 + 30 * i, "taskLine0",
          AnimalScript.DIRECTION_NW), "" + a[i][2], "taskCell0" + (i + 1)
          + "_0", null, textProp);
      // lang.newText(new Coordinates(600+70*2, (y-5)*25+390+40*i),
      lang.newText(new Offset(210, 30 + 30 * i, "taskLine0",
          AnimalScript.DIRECTION_NW), "" + a[i][3], "taskCell0" + (i + 1)
          + "_0", null, textProp);

    }

    lang.nextStep();

    //
    // sa.get(0).highlightCell(0, 0, null, null);
    try {
      // Start

      sheduling(na, na2, sa, sc, 1, a, x, y);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
    // sc.hide();
    // ia.get(0).hide();
    lang.nextStep();
    sc.hide();

    // lang.newText(new Coordinates(40, (y-10)+435)
    lang.newText(new Offset(30, 70, "stringArray" + (y - 1),
        AnimalScript.DIRECTION_NW), "Above you can see the resault of the",
        "summary1", null, textProp);
    lang.newText(new Offset(0, 25, "summary1", AnimalScript.DIRECTION_NW),
        "'Highest-Priority-First Sheduling with Interrupts'.", "summary2",
        null, textProp);

    lang.newText(new Offset(0, 25, "summary2", AnimalScript.DIRECTION_NW),
        "Number of executed processes is " + y, "summary3", null, textProp);

    lang.newText(new Offset(0, 25, "summary3", AnimalScript.DIRECTION_NW),
        "The total executing time is " + execTime + " timesteps.", "summary4",
        null, textProp);

    lang.newText(new Offset(0, 25, "summary4", AnimalScript.DIRECTION_NW),
        "The average waiting-time for one process is " + (waitingTime / y),
        "summary5", null, textProp);

  }

  private void sheduling(ArrayList<StringArray> na, ArrayList<StringArray> na2,
      ArrayList<StringArray> sa, SourceCode codeSupport, int step, int[][] a,
      int x, int y) {
    // "1	HPF-Sheduling(){"
    // +"\n2		1while(true){"

    boolean active = false;
    int maxPriority = -1;
    int maxPrioInd = -1;
    for (int i = 0; i < y; i++) {
      if (a[i][1] < step && a[i][2] > 0) {
        active = true;
        if (a[i][3] > maxPriority) {
          maxPriority = a[i][3];
          maxPrioInd = i;

        }

      }

    }
    if (interaction) {
      String str[] = { "No, not really.", "No, not really.", "No, not really.",
          "No, not really.", "No, not really.", "No, not really.",
          "No, not really.", "No, not really.", "No, not really.",
          "No, not really." };
      if (maxPrioInd > -1)
        str[maxPrioInd] = "Absolutely right!";
      QuestionGroupModel groupInfo = new QuestionGroupModel(
          "First question group " + step, 1);
      lang.addQuestionGroup(groupInfo);
      MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
          "multipleChoiceQuestion" + step);
      mcq.setPrompt("Which Process will be executed next?");
      if (!active)
        mcq.addAnswer("None", 200, "Absolutely right!");
      else
        mcq.addAnswer("None", 0, "No, not really.");
      for (int i = 0; i < y; i++) {
        mcq.addAnswer("P" + (i + 1), i + 1, str[i]);
      }
      mcq.setGroupID("First question group " + step);
      lang.addMCQuestion(mcq);
      lang.nextStep();
    }

    // +"\n3		2	select all active processes"
    codeSupport.highlight(2, 0, false);
    for (int i = 0; i < y; i++) {
      // has arrived, not executed completely yet
      if (a[i][1] < step && a[i][2] > 0) {
        na.get(i).highlightCell(0, null, null);
        na2.get(i).highlightCell(0, null, null);
      }

    }

    // +"\n4		2	if(no active processes){"

    if (active == false) {

      // +"\n5   	3    	set the status of all processes on 'empty'"
      lang.nextStep();
      codeSupport.unhighlight(2, 0, false);
      codeSupport.highlight(4, 0, false);
      // +"\n6		3	   	wait one time-step"
      lang.nextStep();
      codeSupport.unhighlight(4, 0, false);
      codeSupport.highlight(5, 0, false);

      // +"\n7		2	}"
    }
    // +"\n8   	2  	else{"
    else {
      // +"\n9	  	3   	chose the process with highest priority"

      lang.nextStep();
      codeSupport.unhighlight(2, 0, false);
      codeSupport.highlight(8, 0, false);

      for (int i = 0; i < y; i++) {
        na.get(i).unhighlightCell(0, null, null);
        na2.get(i).unhighlightCell(0, null, null);
      }
      na.get(maxPrioInd).highlightCell(0, null, null);
      na2.get(maxPrioInd).highlightCell(0, null, null);
      // +"\n10		3 		set the status of the chosen process on 'working'"
      lang.nextStep();
      codeSupport.unhighlight(8, 0, false);
      codeSupport.highlight(9, 0, false);
      sa.get(maxPrioInd).highlightCell(step - 1, null, null);
      execTime++;

      a[maxPrioInd][2]--;
      // +"\n11		3	  	set the status of all other active processes on 'waiting'"
      boolean checked = false;
      for (int i = 0; i < y; i++) {
        if (i != maxPrioInd && a[i][1] < step && a[i][2] > 0) {
          if (!checked) {
            lang.nextStep();
            codeSupport.unhighlight(9, 0, false);
            codeSupport.highlight(10, 0, false);
            checked = true;
          }
        }
      }
      for (int i = 0; i < y; i++) {
        if (i != maxPrioInd && a[i][1] < step && a[i][2] > 0) {

          sa.get(i).highlightElem(step - 1, null, null);
          na.get(i).highlightElem(0, null, null);
          na2.get(i).highlightElem(0, null, null);
          waitingTime = waitingTime + 1.0;

        }
      }
      // +"\n12		3  		wait one timestep"
      lang.nextStep();
      codeSupport.unhighlight(9, 0, false);
      codeSupport.unhighlight(10, 0, false);
      codeSupport.highlight(11, 0, false);
      // +"\n13		2	}"
    }
    // +"\n14		1}"
    // +"\n15	}";
    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "" + (step));
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    lang.newArrayMarker(sa.get(0), step - 1, "blueMarker", null, arrayIMProps);
    lang.nextStep("t = " + step);
    for (int i = 0; i < y; i++) {
      na.get(i).unhighlightCell(0, null, null);
      na.get(i).unhighlightElem(0, null, null);
      na2.get(i).unhighlightCell(0, null, null);
      na2.get(i).unhighlightElem(0, null, null);
    }
    codeSupport.unhighlight(5, 0, false);
    codeSupport.unhighlight(11, 0, false);
    if (step < x) {
      try {

        sheduling(na, na2, sa, codeSupport, step + 1, a, x, y);
      } catch (LineNotExistsException e) {
        e.printStackTrace();
      }
    }

  }

}
