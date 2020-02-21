package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

public class ShortestRemainingTimeFirst implements Generator{
	
		private Language lang;
		
		private Locale locale = Locale.US;
		private Translator translator;
		private MatrixProperties matrixProps;
		private Color highlightShortestColor;
		
		private static final String SOURCE_CODE     = "1. determine current shortest job (that has already arrived)\n"
				+ "2. while shortest job has remaining runtime:\n"
				+ "\t a. execute it for one time step\n"
				+ "\t b. determine new shortest job";
		
		/**
		  * default duration for swap processes
		  */
		public final static Timing  defaultDuration = new TicksTiming(30);
		
		
		private int turnaroundSum;
		private int waitingSum;
		private int responseSum;
		
		private Text preemptsTotalText;
		private Text finishedText;
		private Text infoJobText;
		private Text executionTimeJobText;
		private Text waitingTimeJobText;
		private Text preemptsJobText;
	
		private int numPreempts;
		private int numFinishedJobs;
		private int currentJob;
		
		private ArrayList<int[]> state = new ArrayList<int[]>();
		private ArrayList<Integer> executionOrder;
		private Task[] origJobs;
		
		
		/**
		  * Default constructor
		  */
		public ShortestRemainingTimeFirst(String path, Locale locale) {
			this.locale = locale;
			translator = new Translator(path, locale); 
			init();
		}
		
		public void init() {
			// Create a new language object for generating animation code
		    // this requires type, name, author, screen width, screen height
			lang = new AnimalScript("Shortest Remaining Time First Scheduling", "Simon Althaus, Florian Giger", 800, 600);
			
			// This initializes the step mode. Each pair of subsequent steps has to
		    // be divided by a call of langintroSrcProps.nextStep();
			lang.setStepMode(true);
			numPreempts = 0;
			turnaroundSum = 0;
			waitingSum = 0;
			responseSum = 0;			
		}
		
		public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		    
			SourceCodeProperties scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
			matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
			MatrixProperties shortestJobProps = (MatrixProperties) props.getPropertiesByName("shortestJob");
			highlightShortestColor = (Color) shortestJobProps.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
			int[][] arr = (int[][])primitives.get("tasks");
			
			int numberTasks = arr.length;
			origJobs = createTasksArray(arr);
			Task[] tasks = createTasksArray(arr);
			executionOrder = srtf(tasks);
			int timestepsNeeded = executionOrder.size();
	        
	        TextProperties titleProps = new TextProperties();
			titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
			
			Text title = lang.newText(new Coordinates(20,30), "Shortest Remaining Time First Scheduling", "title", null, titleProps);
			
			//Intro
			SourceCodeProperties introScProps = new SourceCodeProperties();
			introScProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
			SourceCode introSC = lang.newSourceCode(new Offset(0, 20, title, AnimalScript.DIRECTION_SW), "introSourceCode", null, introScProps);
			introSC.addMultilineCode(translator.translateMessage("introduction"), null, null);		
			
			lang.nextStep(translator.translateMessage("introductionStep"));
			
			introSC.hide();
			
			int[][] emptyMatrix = new int[numberTasks+1][timestepsNeeded+1];
			for(int i=1; i < timestepsNeeded+1; i++) {
				emptyMatrix[0][i] = i-1;
			}
			
			for (int i=1; i < numberTasks+1; i++) {
				emptyMatrix[i][0] = i-1;
			}
			
			TextProperties titleMatricesProps = new TextProperties();
			titleMatricesProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
			
			//Create matrix for Gantt diagram of outstanding tasks
		    IntMatrix im1 = lang.newIntMatrix(new Coordinates(20, 150), emptyMatrix, "intMatrix1", null, matrixProps);
		    Text titleMatrix1 = lang.newText(new Coordinates(20,120), translator.translateMessage("matrix1Title"), "matrix1Title", null, titleMatricesProps);
		    titleMatrix1.show();
		    
			//Create matrix for Gantt diagram of processed tasks
		    IntMatrix im2 = lang.newIntMatrix(new Offset(0, 75, im1, AnimalScript.DIRECTION_SW), emptyMatrix, "intMatrix2", null, matrixProps);
		    Text titleMatrix2 = lang.newText(new Offset(0, 45, im1, AnimalScript.DIRECTION_SW), translator.translateMessage("matrix2Title"), "matrix1Title", null, titleMatricesProps);
		    titleMatrix2.show();
		    
		    //Make first row and column with numbers for times and jobs visible
		    IntMatrix[] ims = {im1, im2};
		    for (IntMatrix im : ims) {
		    	im.highlightElemColumnRange(0, 1, timestepsNeeded, null, null);
			    im.highlightElem(0, timestepsNeeded, null, null);
			    im.highlightElemRowRange(1, numberTasks, 0, null, null);
			    im.highlightElem(numberTasks, 0, null, null);
		    }
		    		    
		    // Create SourceCode: coordinates, name, display options,
		    SourceCode sc = lang.newSourceCode(new Offset(50, 0, im1, AnimalScript.DIRECTION_NE), "sourceCode", null, scProps);
		    
		    //Add the lines to the SourceCode object.
		    //Line, name, indentation, display dealy
		    sc.addCodeLine(translator.translateMessage("sc1"), null, 0, null); //0
		    sc.addCodeLine(translator.translateMessage("sc2"), null, 0, null);
		    sc.addCodeLine(translator.translateMessage("sc2a"), null, 1, null);
		    sc.addCodeLine(translator.translateMessage("sc2b"), null, 1, null);
		    
		    //TODO Werte einsetzten / berechnen
		    preemptsTotalText = lang.newText(new Offset(50, 0, im2, AnimalScript.DIRECTION_NE), String.format(translator.translateMessage("noPreempts"), 0), null, null);
		    finishedText = lang.newText(new Offset(0, 10, preemptsTotalText, AnimalScript.DIRECTION_SW), String.format(translator.translateMessage("noFinishedJobs"), 0), null, null);
		    TextProperties headingProps = new TextProperties();
		    headingProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		    infoJobText = lang.newText(new Offset(0, 20, finishedText, AnimalScript.DIRECTION_SW), String.format(translator.translateMessage("infoJob"), 0), null, null, headingProps);
			executionTimeJobText = lang.newText(new Offset(10, 10, infoJobText, AnimalScript.DIRECTION_SW), String.format(translator.translateMessage("turnaroundTime"), 0), null, null);
			waitingTimeJobText = lang.newText(new Offset(0, 10, executionTimeJobText, AnimalScript.DIRECTION_SW), String.format(translator.translateMessage("waitTime"), 0), null, null);
			preemptsJobText = lang.newText(new Offset(0, 10, waitingTimeJobText, AnimalScript.DIRECTION_SW), String.format(translator.translateMessage("noPreempts"), 0), null, null);
		    
		    
	        lang.nextStep("SRTF");
			
			try { 
				tasks = createTasksArray(arr);
				animate_srtf(tasks, executionOrder, im1, im2, sc);
			} catch (LineNotExistsException e) {
				e.printStackTrace();
			}

			//Summary
			lang.nextStep();
			lang.hideAllPrimitives();
			titleMatrix1.hide();
			titleMatrix2.hide();
			title.show();
			SourceCodeProperties summaryScProps = new SourceCodeProperties();
			summaryScProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
			SourceCode summarySC = lang.newSourceCode(new Offset(0, 20, title, AnimalScript.DIRECTION_SW), "summarySourceCode", null, summaryScProps);
			//TODO calculate values for summary
			double throughput = 1.0*numberTasks/timestepsNeeded;
			double turnaroundTime = 1.0*turnaroundSum/numberTasks;
			double waitingTime = 1.0*waitingSum/numberTasks;
			double responseTime = 1.0*responseSum/numberTasks;
			summarySC.addMultilineCode(String.format(translator.translateMessage("summary"), numPreempts, throughput, turnaroundTime, waitingTime, responseTime), null, null);
			lang.nextStep(translator.translateMessage("summaryStep"));
	        
	        return lang.toString();
	    }
		
		private void animate_srtf(Task[] jobs, ArrayList<Integer> executionOrder, IntMatrix im1, IntMatrix im2, SourceCode sc) {
			visualizeRemainingJobs(jobs, im1);
			lang.nextStep();
			sc.highlight(0);
			visualizeShortestJob(im1, executionOrder.get(0), jobs[executionOrder.get(0)]);
			for(int t=0; t < executionOrder.size(); t++) {
				if(t == 0) {
					lang.nextStep();
					sc.toggleHighlight(0, 1);
					lang.nextStep();
					sc.toggleHighlight(1,2);
				}
				
				int shortest = executionOrder.get(t);
				currentJob = shortest;
				
				if (t != 0) {
					visualizeShortestJob(im1, shortest, jobs[shortest]);
					lang.nextStep();
					sc.toggleHighlight(3,1);
					lang.nextStep();
					sc.toggleHighlight(1,2);
				}
				jobs[shortest].duration--;
				
				for(int i=0; i < jobs.length; i++) {
					if(jobs[i].arrival > 0)
						jobs[i].arrival--;
				}
				
				visualizeRemainingJobs(jobs, im1);
				visualizeExecution(shortest, t, im2);
				updateText(jobs, t);
				lang.nextStep();
				sc.toggleHighlight(2,3);
			}
			lang.nextStep();
			sc.toggleHighlight(3,1);
			lang.nextStep();
			sc.unhighlight(1);
		}
		
		private void visualizeShortestJob(IntMatrix im1, int shortest, Task job) {
			//Reset highlight fill color for all jobs
			for(int i=1; i < im1.getNrRows(); i++) {
				for(int j=1; j < im1.getNrCols(); j++) {
					im1.setGridHighlightFillColor(i, j, (Color) matrixProps.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), null, null);
				}
			}
			
			//highlight shortest job
			for(int i=0; i < job.duration; i++) {
				im1.setGridHighlightFillColor(shortest+1, job.arrival+1+i, highlightShortestColor, null, null);
			}
			im1.highlightCellColumnRange(shortest+1, job.arrival+1, job.arrival+job.duration, null, null);
			
			
		}
		
		private void visualizeRemainingJobs(Task[] jobs, IntMatrix im1) {
			for(int i=0; i < jobs.length; i++) {
				Task job = jobs[i];
				im1.unhighlightCellColumnRange(i+1, 1, im1.getNrCols()-1, null, null);
				im1.highlightCellColumnRange(i+1, job.arrival+1, job.arrival+job.duration, null, null);
			}
		}
		
		private void visualizeExecution(int shortest, int t, IntMatrix im2) {
			im2.highlightCell(shortest+1, t+1, null, null);
		}
		
		private void updateText(Task[] jobs, int t) {
			//calculate number of finished jobs until now
			int tmp = 0;
			for (Task job : jobs) {
				if(job.duration == 0)
					tmp++;
			}
			numFinishedJobs=tmp;
			
			
			//calculate total preempts until now
			int totalPreempts = 0;
			if(t != 0)
				for(int x : state.get(t-1))
					totalPreempts += x;
		
			
			int timeInSystem = t-origJobs[currentJob].arrival+1;
			int numberPreemptsJob = (t == 0 ? 0 : state.get(t-1)[currentJob]);
			int executedTime = 0; //(int) executionOrder.stream().filter(x -> (x == currentJob)).count();
			for(int i=0; i <= t; i++) {
				if(executionOrder.get(i) == currentJob)
					executedTime++;
			}
			int waitTime = timeInSystem - executedTime;
			
			preemptsTotalText.setText(String.format(translator.translateMessage("noPreempts"), totalPreempts), null, null);
			finishedText.setText(String.format(translator.translateMessage("noFinishedJobs"), numFinishedJobs), null, null); 
		    infoJobText.setText(String.format(translator.translateMessage("infoJob"), currentJob), null, null);
			executionTimeJobText.setText(String.format(translator.translateMessage("turnaroundTime"), timeInSystem), null, null);
			waitingTimeJobText.setText(String.format(translator.translateMessage("waitTime"), waitTime), null, null);
			preemptsJobText.setText(String.format(translator.translateMessage("noPreempts"), numberPreemptsJob), null, null);
		}
		
		private ArrayList<Integer> srtf(Task[] _jobs){
			Task[] jobs = _jobs;
			
			ArrayList<Integer> executed = new ArrayList<Integer>();
			
			//Determine current shortest job (that has already arrived)
			int shortest = shortestIndex(jobs, executed.size());
			
			//While shortest job has remaining runtime
			while(jobs[shortest].duration != 0){
				//execute it
				jobs[shortest].duration--;
				
				//add to response sum
				if(!executed.contains(shortest) && executed.size() != 0 ) {
					responseSum += executed.size() - jobs[shortest].arrival;
				}
				
				//sum up waiting time
				for(int i=0; i < jobs.length; i++) {
					if(jobs[i].duration > 0 && jobs[i].arrival <= executed.size() && i != shortest) {
						waitingSum += 1;
					}
				}
				
				//sum up time in system
				if(jobs[shortest].duration == 0) {
					turnaroundSum += executed.size()+1-jobs[shortest].arrival;
				}
				
				executed.add(shortest);
				
				//determine new shortest job
				int newShortest = shortestIndex(jobs, executed.size());
				
				//record preempt for this time step
				int[] preempts = new int[jobs.length];
				if(state.size() > 0) {
					for(int i=0; i < jobs.length; i++) {
						preempts[i] = state.get(state.size()-1)[i];
					}
				}
				//count number of preempts:
				if(jobs[shortest].duration > 0 && jobs[shortest].arrival < executed.size() && newShortest != shortest) {
					numPreempts++;
					preempts[shortest]++;
				}
				state.add(preempts);
				
				shortest = newShortest;
			}
			
			return executed;
		}
		
		private static int shortestIndex(Task[] jobs, int timeStep){
			
			int shortest = 0;
			
			for(int i = 0; i<jobs.length; i++){
				if((jobs[i].duration < jobs[shortest].duration && jobs[i].duration!=0 && jobs[i].arrival <= timeStep) || jobs[shortest].duration==0){
					shortest = i;
				}
			}
			return shortest;
		}
		
		private Task[] createTasksArray(int[][] arr) {
			Task[] tasks = new Task[arr.length];
			for (int i = 0; i < arr.length; i++) {
				if(arr[i] != null && arr[i].length == 2) {
					tasks[i] = new Task(arr[i][0], arr[i][1]);
				} else {
					System.out.println("Skipping task entry at position " + i);
				}
			}
			return tasks;
		}
		
		public String getName() {
	        return "Shortest Remaining Time First Scheduling";
	    }

	    public String getAlgorithmName() {
	        return "Shortest Remaining Time First Scheduling";
	    }

	    public String getAnimationAuthor() {
	        return "Simon Althaus, Florian Giger";
	    }
	    
	    public String getDescription() {
	    	return translator.translateMessage("description");
	    }
	    
	    public String getCodeExample() {
	    	return SOURCE_CODE;
	    }
	    
	    public String getFileExtension(){
	        return "asu";
	    }

	    public Locale getContentLocale() {
	        return locale;
	    }
	    
	    public void setLanguage(Locale lang) {
			this.locale = lang;
			this.translator.setTranslatorLocale(this.locale);
		}

	    public GeneratorType getGeneratorType() {
	        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	    }

	    public String getOutputLanguage() {
	        return Generator.PSEUDO_CODE_OUTPUT;
	    }

		/*public static void main(String[] args) {
			
		    Generator s = new ShortestRemainingTimeFirst("resources/ShortestRemainingTimeFirst", Locale.US);
		    //Animal.startGeneratorWindow(s);
		    
		    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
		    int[][] a = { {6,0}, {4,0}, {2,1}, {1,4}, {3,7} };
		    primitives.put("tasks", a);
		    
		    AnimationPropertiesContainer props = new AnimationPropertiesContainer();
		    
		    SourceCodeProperties scProps = new SourceCodeProperties();
		    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
		        new Font("Monospaced", Font.PLAIN, 12));
		    
		    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		    scProps.setName("sourceCode");
		    props.add(scProps);

		    MatrixProperties matrixProps = new MatrixProperties();
		    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
		    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
		    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.BLUE);
		    matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		    matrixProps.setName("matrixProps");
		    props.add(matrixProps);
		    
		    MatrixProperties shortestJobProps = new MatrixProperties();
		    shortestJobProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		    shortestJobProps.setName("shortestJob");
		    props.add(shortestJobProps);
		    
		    String code = s.generate(props, primitives);
		    
		    try (PrintWriter out = new PrintWriter("SRTF.asu")) {
				out.println(code);
				System.out.println("Written to SRTF.asu. Please open or reload Animal");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println(code);
			    Animal.startAnimationFromAnimalScriptCode(code);
			}
		}*/
		
		static class Task {
			
			int duration, arrival;
			
			Task(int duration, int arrival){
				this.duration = duration;
				this.arrival = arrival;
			}	
			
		}
	
}
