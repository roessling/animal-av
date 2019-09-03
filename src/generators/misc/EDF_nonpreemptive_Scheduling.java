/*
 * TestAnimal.java
 * Christian Dreger, 2016 for the Animal project at TU Darmstadt.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.processScheduling.ProcessSchedulingEDF;
import generators.misc.processScheduling.ProcessEDF;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

@SuppressWarnings("unused")
public class EDF_nonpreemptive_Scheduling implements ValidatingGenerator {
    private Language lang;
	private String[][] stringMatrix;
	private int maxTime;
    private RectProperties rect_properties_currentTime;
    private RectProperties rect_properties_wait;
    private RectProperties rect_properties_execute;
    private SourceCodeProperties sourceCodeProperties;

    public void init(){
        lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        stringMatrix = (String[][])primitives.get("stringMatrix");
        maxTime = (int)primitives.get("maxTime");
        rect_properties_currentTime = (RectProperties)props.getPropertiesByName("rect_properties_currentTime");
        rect_properties_wait = (RectProperties)props.getPropertiesByName("rect_properties_wait");
        rect_properties_execute = (RectProperties)props.getPropertiesByName("rect_properties_execute");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");

        LinkedList<ProcessEDF> list = new LinkedList<ProcessEDF>();
        String[][] stringMatrix = (String[][])primitives.get("stringMatrix");
        for (int i = 0; i < stringMatrix.length; i++) {
        	list.add(new ProcessEDF(stringMatrix[i][0], Integer.parseInt(stringMatrix[i][1]), Integer.parseInt(stringMatrix[i][2]), Integer.parseInt(stringMatrix[i][3])));
        }
		
		ProcessSchedulingEDF ps = new ProcessSchedulingEDF(this, lang, list, maxTime,
				(Color)rect_properties_currentTime.get(AnimationPropertiesKeys.FILL_PROPERTY), (Color)rect_properties_wait.get(AnimationPropertiesKeys.FILL_PROPERTY), (Color)rect_properties_execute.get(AnimationPropertiesKeys.FILL_PROPERTY),
				sourceCodeProperties);
		ps.print_Intro();
		ps.print_EDF(false);
		
        return lang.toString();
    }
    
    private String getDescriptionLines(){
    	String temp = getDescription().replace("\n\n", " ").replace("\n", " ");
    	for(int i=50 ; i<temp.length() ; i=i+50){
    		int nextSpace = temp.indexOf(" ", i);
    		if(nextSpace>=0){
    			temp = temp.substring(0,nextSpace) + "\n" + temp.substring(nextSpace+1);
    		}
    	}
    	return temp;
    }

    public String getName() {
        return "Earliest Deadline First (EDF) - non-preemptiv [Scheduling]";
    }

    public String getAlgorithmName() {
        return "Earliest Deadline First (EDF) - non-preemptiv";
    }

    public String getAnimationAuthor() {
        return "Christian Dreger";
    }

    public String getDescription(){
        return "Earliest deadline first (EDF) or least time to go is a dynamic scheduling algorithm used in real-time operating systems"
        		+ " to place processes in a priority queue. Whenever a scheduling event occurs (task finishes, new task released, etc.)"
        		+ " the queue will be searched for the process closest to its deadline. This process is the next to be scheduled for execution.\n\n"
        		+ "If you use the EDF-preemptive version, then a running process can be interrupted from another process with an earlier deadline!\n"
        		+ "If you use the EDF-non-preemptive version, then a running process cannot be interrupted by another process until it is finished!\n\n"
        		+ "You should enter your processes in the processMatrix! One row for each process.\n"
        		+ "C: 0 -> Process-Name = The name of the process, it can only be 3 characters long\n"
        		+ "C: 1 -> Process-First-Arrived = First time process arrived[ms]\n"
        		+ "C: 2 -> Process-Work-Time = How many ms a process need to execute before deadline[ms]\n"
        		+ "C: 3 -> Process-Rhytm = Process-Deadline, until how many ms process need to be finished and new process of itself arrived[ms]";
    }

    public String getCodeExample(){
        return SOURCE_CODE.replace("<", "&lt;");
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

    private static final String SOURCE_CODE =
			"public void earliest_deadline_first(LinkedList<Process> list, boolean preemptive, int maxTime) {" // 0
		+ "\n   int currentTime = 0;" // 1
		+ "\n   Process runProcess = null;" // 2
		+ "\n   while (currentTime < maxTime) {" // 3
		+ "\n      if (list contains process with counter > 1) {" // 4
		+ "\n         throw new Exception(\"New Process arrived, but old not finished!\");" // 5
		+ "\n      }" // 6
		+ "\n      LinkedList<Process> listNeedToExecute = getAllProcessWhichNeedToExecute(list);" // 7
		+ "\n      sortListByEarliestDeadlineFirst(listNeedToExecute);" // 8
		+ "\n      if (!listNeedToExecute.isEmpty()) {" // 9
		+ "\n         if (preemptive || runProcess==null || runProcess.remainingWork()==0) {" // 10
		+ "\n            runProcess = listNeedToExecute.removeFirst();" // 11
		+ "\n         } else {" // 12
		+ "\n            listNeedToExecute.remove(runProcess);" // 13
		+ "\n         }" // 14
		+ "\n         otherProcessesWait(listNeedToExecute);" // 15
		+ "\n         runProcess.execute();" // 16
		+ "\n      }" // 17
		+ "\n      currentTime = currentTime + 1;" // 18
		+ "\n   }" // 19
		+ "\n}"; // 20
    
//    private static final String SOURCE_CODE_OLD =
//			"public void earliest_deadline_first(LinkedList<Process> list, boolean preemptive, int maxTime){" // 0
//		+ "\n	int currentTime = 0;" // 1
//		+ "\n	Process runProcess = null;" // 2
//		+ "\n	while(currentTime < maxTime){" // 3
//		+ "\n		if (list contains process with counter > 1){" // 4
//		+ "\n			throw new Exception(New Process arrived, but old not finished!);" // 5
//		+ "\n		}" // 6
//		+ "\n		LinkedList<Process> listNeedToExecute = getAllProcessWhichNeedToExecute(list);" // 7
//		+ "\n		sortListByEarliestDeadlineFirst(listNeedToExecute);" // 8
//		+ "\n		if (!listNeedToExecute.isEmpty()){" // 9
//		+ "\n			if (preemptive || runProcess==null || runProcess.remainingWork()==0){" // 10
//		+ "\n				runProcess = listNeedToExecute.removeFirst();" // 11
//		+ "\n			}else{" // 12
//		+ "\n				listNeedToExecute.remove(runProcess);" // 13
//		+ "\n			}" // 14
//		+ "\n			otherProcessesWait(listNeedToExecute);" // 15
//		+ "\n			runProcess.execute();" // 16
//		+ "\n		}" // 17
//		+ "\n		currentTime = currentTime + 1;" // 18
//		+ "\n	}" // 19
//		+ "\n}"; // 20

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		boolean correct = true;
		String errors = "";
        String[][] stringMatrix = (String[][])primitives.get("stringMatrix");
        if((int)primitives.get("maxTime")<=0){
			correct = false;
			errors = errors + "maxTime must be a positive integer!\n";
        }
        if(stringMatrix.length<=0){
			correct = false;
			errors = errors + "At least you need one process!\n";
        }
        if(correct && stringMatrix[0].length!=4){
			correct = false;
			errors = errors + "A Process only have 4 columns!\n";
        }
        if(correct){
            for (int i = 0; i < stringMatrix.length; i++) {
            	if(stringMatrix[i][0].length()>3){
        			correct = false;
        			errors = errors + "Process-name at R: "+i+" C: 0 can only be three characters long!\n";
            	}
    			for (int j = 1; j < stringMatrix[i].length; j++) {
    				if(!isNonNegativeNumeric(stringMatrix[i][j])){
    					correct = false;
    					errors = errors + "Element at R: "+i+" C: "+j+" must be a non-negative integer!\n";
    				}
    			}
            }
        }
        if(!errors.equals("") && errors.contains("\n")){
			infoBox(errors.substring(0,errors.lastIndexOf("\n")), "Incorrect parameters");
        }
		return correct;
	}
	
	public static boolean isNonNegativeNumeric(String str){  
		try{  
			Integer.parseInt(str);
		}catch(NumberFormatException nfe){  
			return false;  
		}  
		return true;  
	}
	
	public static void infoBox(String infoMessage, String titleBar){
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

}