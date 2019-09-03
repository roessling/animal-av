package generators.misc.processScheduling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.ValidatingGenerator;

public class ProcessSchedulingEDF {

	private ValidatingGenerator edf_scheduling;
	private Language lang;

	private LinkedList<ProcessEDF> processes;
	private int currentTime = 0;
	private int maxTime = 30;
	private Color color_CurrentTime = Color.CYAN;
	private Color color_Wait = new Color(137, 207, 240);
	private Color color_Execute = Color.GREEN;
	private SourceCodeProperties sourceCodeProperties;

	/**
	 * Here you can create a scheduling-process which will help you creating an EDF scheduling-plan of the given processes
	 * @param edf_scheduling
	 * @param lang Animal-Language object
	 * @param processes list of processes to scheduling
	 * @param maxTime maximum time to scheduling
	 * @param color_CurrentTime Color of the CurrentTime-Line. If null then default value.
	 * @param color_Wait Color when a process is waiting. If null then default value.
	 * @param color_Execute Color when a process is executing. If null then default value.
	 * @param sourceCodeProperties Properties from the Sourcecode. If null then default value.
	 */
	public ProcessSchedulingEDF(ValidatingGenerator edf_scheduling, Language lang, LinkedList<ProcessEDF> processes, int maxTime,
			Color color_CurrentTime, Color color_Wait, Color color_Execute,
			SourceCodeProperties sourceCodeProperties){
		this.edf_scheduling = edf_scheduling;
		this.processes = new LinkedList<ProcessEDF>();
		if(processes!=null){
			this.processes.addAll(processes);
		}
		this.lang = lang;
		
		this.maxTime = maxTime;
		if(color_CurrentTime!=null){
			this.color_CurrentTime = color_CurrentTime;
		}
		if(color_Wait!=null){
			this.color_Wait = color_Wait;
		}
		if(color_Execute!=null){
			this.color_Execute = color_Execute;
		}
		if(sourceCodeProperties==null){
			sourceCodeProperties = new SourceCodeProperties();
			sourceCodeProperties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
			sourceCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
			sourceCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		}
		this.sourceCodeProperties = sourceCodeProperties;
	}
	
	public void print_Intro() {
    	lang.addLine("label \"EDF-Scheduling Informations!\"");
		TextProperties prop = new TextProperties();
		prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 25));
        Text ueberschrift = lang.newText(new Coordinates(50,50), getName(this), "HeaderText", null, prop);

		RectProperties propRec = new RectProperties();
		propRec.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		propRec.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		propRec.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect rec_ArrowRec = lang.newRect(new Offset(-5, -5, ueberschrift.getName(), AnimalScript.DIRECTION_NW),
				new Offset(+5, +5, ueberschrift.getName(), AnimalScript.DIRECTION_SE), "HeaderRec", null, propRec);

		SourceCodeProperties scProps = sourceCodeProperties;
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc = lang.newSourceCode(new Offset(0, +75, rec_ArrowRec.getName(), AnimalScript.DIRECTION_SW), "description", null, scProps);
		String[] descL;
		if(edf_scheduling!=null){
			descL = getLines(edf_scheduling.getDescription()).substring(0, edf_scheduling.getDescription().indexOf("!\n\n")+1).split("\n");
		}else{
			descL = getLines(getDescription()).split("\n");
		}
		for (int i = 0; i < descL.length; i++) {
			sc.addCodeLine(descL[i], null, 0, null);
		}

		lang.nextStep();
		sc.hide();
	}

	public void print_OutputArray_InGUI(String[][] outputArray) {
		JFrame frame = new JFrame("Process Scheduling");
		frame.setVisible(true);
		frame.setResizable(false);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		frame.add(panel);
		String[] columnNames = new String[outputArray[0].length+1];
		columnNames[0] = "Process";
		for(int i=0 ; i<columnNames.length-1 ; i++){
			columnNames[i+1] = String.valueOf(i);
		}
		String[][] data = new String[outputArray.length][outputArray[0].length+1];
		for(int i=0 ; i<outputArray.length ; i++){
			data[i][0] = processes.get(i).getName();
			for(int j=0 ; j<outputArray[0].length ; j++){
				data[i][j+1] = outputArray[i][j];
			}
		}
		final JTable table = new JTable(data, columnNames);
		panel.add(table);
        table.setPreferredScrollableViewportSize(new Dimension(Math.min(1500, outputArray[0].length*40+100), 500));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
		table.setEnabled(false);
		table.getTableHeader().setReorderingAllowed(false); 
		JLabel renderer = ((JLabel)table.getDefaultRenderer(Object.class));
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setMinWidth(100);
		col.setMaxWidth(100);
		for(int j=1 ; j<outputArray[0].length+1 ; j++){
			col = table.getColumnModel().getColumn(j);
			col.setMinWidth(40);
			col.setMaxWidth(40);
		}
        frame.pack();
        frame.setLocationRelativeTo(null);
	}

	@SuppressWarnings("unused")
	private void print_OutputArray(String[][] outputArray) {
		for(String[] p : outputArray){
			for(String z : p){
				System.out.print(" "+z);
			}
			System.out.println();
		}
		System.out.println();
	}

	private StringRec[][] srArray;
	private Text[] tprArray;
	private Text[] tmsArray;
	private Coordinates recStart = new Coordinates(100, 150);
	private int recSize = 30;
	private void print_OutputArray_Animal() {
		srArray = new StringRec[processes.size()][maxTime];
		tprArray = new Text[processes.size()];
		tmsArray = new Text[maxTime+1];
		for(int i=0 ; i<processes.size() ; i++){
			for(int j=0 ; j<maxTime ; j++){
				StringRec stringRec = new StringRec(i+"_"+j,lang);
				srArray[i][j] = stringRec;
				if(j==0){
					if(i==0){//Start
						stringRec.createRec(recStart, new Coordinates(recStart.getX()+recSize, recStart.getY()+recSize));
					}else{//Untereinander
						stringRec.createRec(new Offset(0, 5, srArray[i-1][j].getRec().getName(), AnimalScript.DIRECTION_SW), new Offset(recSize, recSize+5, srArray[i-1][j].getRec().getName(), AnimalScript.DIRECTION_SW));
					}
				}else{//Nebeneinander
					stringRec.createRec(new Offset(0, 0, srArray[i][j-1].getRec().getName(), AnimalScript.DIRECTION_NE), new Offset(recSize, recSize, srArray[i][j-1].getRec().getName(), AnimalScript.DIRECTION_NE));
				}
				stringRec.createText("");
				
				if(i==0){//MS-String
					TextProperties prop = new TextProperties();
					prop.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
					prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
					tmsArray[j] = lang.newText(new Offset(0, -35, srArray[i][j].getRec().getName(), AnimalScript.DIRECTION_NW), ""+j, "MS_String_"+j, null, prop);
				}
			}
			TextProperties prop = new TextProperties();
			prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
			lang.newText(new Coordinates(0, 0), "", "TEMP", null, prop);
			tprArray[i] = lang.newText(new Offset(-60, 0, srArray[i][0].getText().getName(), AnimalScript.DIRECTION_NW), processes.get(i).getName(), "Process_String_"+i, null, prop);
		}
		TextProperties prop2 = new TextProperties();
		prop2.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		prop2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
		tmsArray[maxTime] = lang.newText(new Offset(0, -35, srArray[0][maxTime-1].getRec().getName(), AnimalScript.DIRECTION_NE), ""+maxTime, "MS_String_"+maxTime, null, prop2);

		TextProperties prop = new TextProperties();
		prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
		lang.newText(new Offset(-30, -30, srArray[0][0].getRec().getName(), AnimalScript.DIRECTION_NW), "ms:", "MS_String", null, prop);
		
		for(int i=0 ; i<processes.size() ; i++){
			for(Integer j : processes.get(i).getAllNewProcessTimes(maxTime)){
				srArray[i][j].createNewProcessLine();
			}
		}
	}

	private Rect rec_ArrowRec = null;
	private Text text_ArrowRec = null;
	private void setArrowRecTo(){
		if(rec_ArrowRec==null){
			RectProperties propArrowRec = new RectProperties();
			propArrowRec.set(AnimationPropertiesKeys.FILL_PROPERTY, color_CurrentTime);
			propArrowRec.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			rec_ArrowRec = lang.newRect(new Offset(-2, -15, srArray[0][currentTime].getRec().getName(), AnimalScript.DIRECTION_NW),
					new Offset(+2, +15, srArray[srArray.length-1][currentTime].getRec().getName(), AnimalScript.DIRECTION_SW), "ArrowRec", null, propArrowRec);
			TextProperties prop = new TextProperties();
			prop.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
			prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
			text_ArrowRec = lang.newText(new Offset(0, +20, srArray[srArray.length-1][currentTime].getRec().getName(), AnimalScript.DIRECTION_SW), "currentTime", "ArrowRecText", null, prop);
		}else if(currentTime!=0){
			text_ArrowRec.hide();
			TextProperties prop = new TextProperties();
			prop.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
			prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
			if(currentTime<srArray[0].length){
				rec_ArrowRec.moveTo(AnimalScript.DIRECTION_NW, "translate #1", new Offset(-2, -15, srArray[0][currentTime].getRec().getName(), AnimalScript.DIRECTION_NW), null, null);
				text_ArrowRec = lang.newText(new Offset(0, +15, srArray[srArray.length-1][currentTime].getRec().getName(), AnimalScript.DIRECTION_SW), "currentTime", "ArrowRecText", null, prop);
			}else{
				rec_ArrowRec.moveTo(AnimalScript.DIRECTION_NE, "translate #1", new Offset(-2, -15, srArray[0][srArray[0].length-1].getRec().getName(), AnimalScript.DIRECTION_NE), null, null);
				text_ArrowRec = lang.newText(new Offset(0, +15, srArray[srArray.length-1][srArray[0].length-1].getRec().getName(), AnimalScript.DIRECTION_SE), "currentTime", "ArrowRecText", null, prop);
			}
		}
	}

	
	Comparator<ProcessEDF> comp_earliestDeadlineFirst = new Comparator<ProcessEDF>() {
		@Override
		public int compare(ProcessEDF p1, ProcessEDF p2) {
			return p1.getRemainingTimeToDeadline(currentTime)-p2.getRemainingTimeToDeadline(currentTime);
		}
	};
	
	StringMatrix sm;
	private void print_Matrix(String[][] outputMatrix){
		for (int i = 0; i < processes.size(); i++) {
			outputMatrix[i] = processes.get(i).getCurrentInfos(currentTime);
		}
		String[][] outputMatrixNamed = new String[outputMatrix.length+1][outputMatrix[0].length];
		for (int i = 0; i < outputMatrix.length; i++) {
			for (int j = 0; j < outputMatrix[0].length; j++) {
				outputMatrixNamed[i+1][j] = outputMatrix[i][j];
			}
			outputMatrixNamed[i+1][0] = "";
		}
		outputMatrixNamed[0][0] = "Name";
		outputMatrixNamed[0][1] = "First-Arrived";
		outputMatrixNamed[0][2] = "Work-Time";
		outputMatrixNamed[0][3] = "Rhythm";
		outputMatrixNamed[0][4] = "Counter";
		outputMatrixNamed[0][5] = "Remaining-Work";
		outputMatrixNamed[0][6] = "Deadline";

		TextProperties prop = new TextProperties();
		prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
		text_runProcess = lang.newText(new Offset(100, 0, sc.getName(), AnimalScript.DIRECTION_NE), "runProcess = null", "runProcess", null, prop);
		text_listNeedToExecute = lang.newText(new Offset(0, 20, text_runProcess.getName(), AnimalScript.DIRECTION_SW), "neededExecutionList: ", "listNeedToExecute", null, prop);
		text_runProcess.hide();
		text_listNeedToExecute.hide();
		
		MatrixProperties smp = new MatrixProperties();
		smp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");
		smp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		sm = lang.newStringMatrix(new Offset(0, 50, text_listNeedToExecute.getName(), AnimalScript.DIRECTION_SW), outputMatrixNamed, "Matrix", null, smp);
	}

	private Text text_listNeedToExecute;
	private Text text_runProcess;
	public void print_EDF(boolean preemptive) {
		Variables vars = lang.newVariables();
		vars.declare("string", "processes", getStringFromList(processes));
		vars.declare("int", "maxTime", String.valueOf(maxTime));
		
    	lang.addLine("label \"Run EDF-Scheduling on the processes!\"");
		String[][] outputMatrix = new String[processes.size()][7];
		
		// Set all Designs and Matrix Layer
		print_OutputArray_Animal();
		makeSourceCode();
		print_Matrix(outputMatrix);

		TextProperties prop_Call = new TextProperties();
		prop_Call.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 15));
		prop_Call.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		lang.newText(new Offset(-30, -30, sc.getName(), AnimalScript.DIRECTION_NW), "CALL: earliest_deadline_first(LinkedList<Process> list, boolean preemptive="+preemptive+", int maxTime="+maxTime+")", "Call_Msg", null, prop_Call);

		for (int i = 0; i < processes.size(); i++) {
			ProcessEDF p = processes.get(i);
			p.updateInfos(currentTime);
			outputMatrix[i] = p.getCurrentInfos(currentTime);
			updateMatrixRow(sm,i,outputMatrix[i]);
		}
		
		// Set local Variables before loop
		sc.highlight(0);
		lang.nextStep();
		sc.unhighlight(0);
		sc.highlight(1);
		vars.declare("int", "currentTime", "0");
		currentTime = 0;
		setArrowRecTo();
		lang.nextStep();
		sc.unhighlight(1);
		sc.highlight(2);
		ProcessEDF runProcess = null;
		vars.declare("string", "runProcess", "null");
		text_runProcess.show();
		lang.nextStep();
		sc.unhighlight(2);
		
		boolean error = false;
		LinkedList<ProcessEDF> list_errorProcesses = new LinkedList<ProcessEDF>();

		sc.highlight(3);
		lang.nextStep();
		while(currentTime<maxTime){
	    	lang.addLine("label \"Run EDF-Scheduling on the processes! -> CurrentTime="+currentTime+"\"");
			sc.unhighlight(3);
			setArrowRecTo();
			LinkedList<ProcessEDF> neededExecutionList = new LinkedList<ProcessEDF>();
			LinkedList<ProcessEDF> notneededExecutionList = new LinkedList<ProcessEDF>();
			
			// Get All processes and there informations and sort them in list(neededExecution/notneededExecution)
			for (int i = 0; i < processes.size(); i++) {
				ProcessEDF p = processes.get(i);
				if(currentTime>0){p.updateInfos(currentTime);}
				outputMatrix[i] = p.getCurrentInfos(currentTime);
				updateMatrixRow(sm,i,outputMatrix[i]);
				
				if(outputMatrix[i][4].equals("1")){
					neededExecutionList.add(p);
				}else if(outputMatrix[i][4].equals("0")){
					notneededExecutionList.add(p);
				}else{	// Show Error
					error = true;
					list_errorProcesses.add(p);
					CircleProperties cp = new CircleProperties();
					cp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
					cp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
					Circle circle = lang.newCircle(new Offset(0, 0, srArray[i][currentTime].getRec().getName(), AnimalScript.DIRECTION_W), 18, "Error_Circle_"+i+"_1", null, cp);
					lang.newCircle(new Offset(0, 0, srArray[i][currentTime].getRec().getName(), AnimalScript.DIRECTION_W), 17, "Error_Circle_"+i+"_2", null, cp);
					lang.newCircle(new Offset(0, 0, srArray[i][currentTime].getRec().getName(), AnimalScript.DIRECTION_W), 16, "Error_Circle_"+i+"_3", null, cp);

					PolylineProperties pp = new PolylineProperties();
					pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
					pp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
					lang.newPolyline(new Offset[]{new Offset(0, 0, circle.getName(), AnimalScript.DIRECTION_E),
							new Offset(recSize*2, -50, srArray[0][currentTime].getRec().getName(), AnimalScript.DIRECTION_NE)
							}, "Error_Arrow_"+i, null, pp);
				}
			}
			sc.highlight(4);
			lang.nextStep();
			sc.unhighlight(4);
			// Check if New Process arrived, but old not finished
			if(error){
				sc.highlight(5);
				TextProperties prop = new TextProperties();
				prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 15));
				prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
				lang.newText(new Offset(recSize*2+5, -50-12, srArray[0][currentTime].getRec().getName(), AnimalScript.DIRECTION_NE), "New Process arrived, but old not finished!", "Error_Message", null, prop);
				break;
			}
			sc.highlight(7);
			vars.declare("string", "listNeedToExecute", getStringFromList(neededExecutionList));
			text_listNeedToExecute.show();
			text_listNeedToExecute.setText("neededExecutionList: "+getStringFromList(neededExecutionList), null, null);	// Show neededExecutionList
			for (ProcessEDF p : notneededExecutionList) {
				srArray[processes.indexOf(p)][currentTime].createText("-");
			}
			lang.nextStep();
			sc.unhighlight(7);
			sc.highlight(8);
			
			Collections.sort(neededExecutionList, comp_earliestDeadlineFirst);
			vars.set("listNeedToExecute", getStringFromList(neededExecutionList));
			text_listNeedToExecute.setText("neededExecutionList: "+getStringFromList(neededExecutionList), null, null);	// Show sorted neededExecutionList
			
			lang.nextStep();
			sc.unhighlight(8);
			sc.highlight(9);

			lang.nextStep();
			// Get the processes which should be executed and execute
			if(!neededExecutionList.isEmpty()){
				sc.unhighlight(9);
				sc.highlight(10);
				lang.nextStep();
				sc.unhighlight(10);
				if(preemptive || runProcess==null || !neededExecutionList.contains(runProcess)){
					sc.highlight(11);
					runProcess = neededExecutionList.removeFirst();
					vars.set("runProcess",runProcess.getName());
					text_runProcess.setText("runProcess = "+runProcess.getName(), null, null);
					vars.set("listNeedToExecute", getStringFromList(neededExecutionList));
					text_listNeedToExecute.setText("neededExecutionList: "+getStringFromList(neededExecutionList), null, null);
					lang.nextStep();
					sc.unhighlight(11);
				}else{
					sc.highlight(13);
					neededExecutionList.remove(runProcess);
					vars.set("listNeedToExecute", getStringFromList(neededExecutionList));
					text_listNeedToExecute.setText("neededExecutionList: "+getStringFromList(neededExecutionList), null, null);
					lang.nextStep();
					sc.unhighlight(13);
				}
				sc.highlight(15);
				for(ProcessEDF p : neededExecutionList){
					srArray[processes.indexOf(p)][currentTime].createText("W");
					srArray[processes.indexOf(p)][currentTime].changeColor_RectFill(color_Wait);
				}
				lang.nextStep();
				sc.unhighlight(15);
				sc.highlight(16);
				
				srArray[processes.indexOf(runProcess)][currentTime].createText("E");
				srArray[processes.indexOf(runProcess)][currentTime].changeColor_RectFill(color_Execute);
				runProcess.do_one_execution();
				
				lang.nextStep();
				sc.unhighlight(16);
			}else{
				sc.unhighlight(9);
			}
			sc.highlight(18);
			currentTime++;
			vars.set("currentTime",String.valueOf(currentTime));
			for (int i = 0; i < processes.size(); i++) {	// Update Table after execution
				ProcessEDF p = processes.get(i);
				outputMatrix[i] = p.getCurrentInfos(currentTime);
				updateMatrixRow(sm,i,outputMatrix[i]);
			}
			setArrowRecTo();
			lang.nextStep();
			sc.unhighlight(18);
			text_listNeedToExecute.hide();
			vars.discard("listNeedToExecute");
		}
		
		if(!error){
			setArrowRecTo();
			sc.highlight(20);
			text_runProcess.hide();
			vars.discard("processes");
			vars.discard("maxTime");
			vars.discard("currentTime");
			vars.discard("runProcess");
		}

		lang.nextStep();
		// Show last page of animation
    	lang.addLine("label \"Finished EDF-Scheduling!\"");
		if(error){
			text_runProcess.hide();
			vars.discard("processes");
			vars.discard("maxTime");
			vars.discard("currentTime");
			vars.discard("runProcess");
		}
		sc.hide();
		sm.hide();
		
		SourceCodeProperties scProps = sourceCodeProperties;
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc_finalText = lang.newSourceCode(new Offset(0, +50, "Call_Msg", AnimalScript.DIRECTION_SW), "finalText", null, scProps);
		String finalString = "";
		if(error){
			finalString = "Your call was not successfully scheduled!";
			if(list_errorProcesses.size()>1){
				finalString = finalString + "\nThis is because at "+currentTime+"ms an error appear at "+list_errorProcesses.size()+" processes:";
			}else{
				finalString = finalString + "\nThis is because at "+currentTime+"ms an error appear at "+list_errorProcesses.size()+" process:";
			}
			for (int i = 0; i < list_errorProcesses.size(); i++) {
				ProcessEDF p = list_errorProcesses.get(i);
				finalString = finalString + "\n"+ (i+1) + ") Process "+p.getName()+" still have to execute some ms in his old process, but a new process of him arrived!";
			}
		}else{
			finalString = "Your call was successfully scheduled!"
					+ "\nUntil "+maxTime+"ms all of your processes executed before their deadline."
					+ "\nSo you will not get an error with your processes until "+maxTime+"ms!"
					+ "\nBut be aware, that your processes CAN trigger an error in near future.";
		}
		String[] descL = finalString.split("\n");
		for (int i = 0; i < descL.length; i++) {
			sc_finalText.addCodeLine(descL[i], null, 0, null);
		}
		RectProperties propRec = new RectProperties();
		propRec.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		propRec.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		propRec.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, sc_finalText.getName(), AnimalScript.DIRECTION_NW),
				new Offset(+5, +5, sc_finalText.getName(), AnimalScript.DIRECTION_SE), "FinalRec", null, propRec);
	}
	
	private void updateMatrixRow(StringMatrix smatrix, int row, String[] newRow){
		for (int i = 0; i < newRow.length; i++) {
			smatrix.put(row+1, i, newRow[i], null, null);
		}
	}
	
	@SuppressWarnings("unused")
	private String[][] makeOutputArray(StringRec[][] srArray){
		String[][] outputArray = new String[srArray.length][srArray[0].length];
		for (int i = 0; i < srArray.length; i++) {
			for (int j = 0; j < srArray[i].length; j++) {
				outputArray[i][j] = srArray[i][j].getText().getText();
			}
		}
		return outputArray;
	}

	SourceCode sc;
	private void makeSourceCode(){
		SourceCodeProperties scProps = sourceCodeProperties;
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
//		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
//		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
//		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		int i = 2;
		sc = lang.newSourceCode(new Offset(0, 100, srArray[srArray.length-1][0].getRec().getName(), AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
		String[] sourcecode = null;
		if(edf_scheduling!=null){
			sourcecode = edf_scheduling.getCodeExample().replace("&lt;", "<").replace("\"", "\\\"").split("\n");
		}else{
			sourcecode = SOURCE_CODE.split("\n");
		}
		sc.addCodeLine(sourcecode[0], null, 0*i, null);
		sc.addCodeLine(sourcecode[1], null, 1*i, null);
		sc.addCodeLine(sourcecode[2], null, 1*i, null);
		sc.addCodeLine(sourcecode[3], null, 1*i, null);
		sc.addCodeLine(sourcecode[4], null, 2*i, null);
		sc.addCodeLine(sourcecode[5], null, 3*i, null);
		sc.addCodeLine(sourcecode[6], null, 2*i, null);
		sc.addCodeLine(sourcecode[7], null, 2*i, null);
		sc.addCodeLine(sourcecode[8], null, 2*i, null);
		sc.addCodeLine(sourcecode[9], null, 2*i, null);
		sc.addCodeLine(sourcecode[10], null, 3*i, null);
		sc.addCodeLine(sourcecode[11], null, 4*i, null);
		sc.addCodeLine(sourcecode[12], null, 3*i, null);
		sc.addCodeLine(sourcecode[13], null, 4*i, null);
		sc.addCodeLine(sourcecode[14], null, 3*i, null);
		sc.addCodeLine(sourcecode[15], null, 3*i, null);
		sc.addCodeLine(sourcecode[16], null, 3*i, null);
		sc.addCodeLine(sourcecode[17], null, 2*i, null);
		sc.addCodeLine(sourcecode[18], null, 2*i, null);
		sc.addCodeLine(sourcecode[19], null, 1*i, null);
		sc.addCodeLine(sourcecode[20], null, 0*i, null);
	}
	
	private String getStringFromList(LinkedList<ProcessEDF> list){
		String temp = "";
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				temp = temp + list.get(i).getName();
				if(i+1<list.size()){
					temp = temp + ", ";
				}
			}
		}
		return temp;
	}
	
    
    /**
     * You can change the processes in the list.
     * Add new processes to list.
     * Remove processes to list.
     * Change the colors.
     * Change preemptive value.
     * Change maxTime.
     * 
     * But you must give at least one process and maxTime must be at least 1.
     */
    public static void main(String[] args){//TODO: Change Values
    	Language lang = new AnimalScript(getName(null), getAnimationAuthor(null), 800, 600);
    	lang.setStepMode(true);
    	boolean preemptive = false;
    	int maxTime = 50;
    	LinkedList<ProcessEDF> list = new LinkedList<ProcessEDF>();
    	list.add(new ProcessEDF("P1", 0, 1, 13));
    	list.add(new ProcessEDF("P2", 0, 4, 55));
    	list.add(new ProcessEDF("P3", 0, 5, 86));
    	list.add(new ProcessEDF("P4", 0, 4, 16));
    	list.add(new ProcessEDF("P5", 0, 15, 63));
    	list.add(new ProcessEDF("P6", 0, 3, 88));
		ProcessSchedulingEDF ps = new ProcessSchedulingEDF(null, lang, list, maxTime, Color.CYAN, new Color(137, 207, 240), Color.GREEN, null);
		ps.print_Intro();
		ps.print_EDF(preemptive);
		System.out.println(lang.toString());
    }

    /**
     * Not necessary when it implements Generator
     */
    public static String getName(ProcessSchedulingEDF ps) {
    	if(ps!=null && ps.edf_scheduling!=null){
            return ps.edf_scheduling.getName();
    	}else{
            return "Earliest Deadline First (EDF)";
    	}
    }


    /**
     * Not necessary when it implements Generator
     */
    public static String getAnimationAuthor(ProcessSchedulingEDF ps) {
    	if(ps!=null && ps.edf_scheduling!=null){
            return ps.edf_scheduling.getAnimationAuthor();
    	}else{
            return "Christian Dreger";
    	}
    }


    /**
     * Not necessary when it implements Generator
     */
    public static String getDescription(){
        return "Earliest deadline first (EDF) or least time to go is a dynamic scheduling algorithm used in real-time operating systems"
        		+ " to place processes in a priority queue. Whenever a scheduling event occurs (task finishes, new task released, etc.)"
        		+ " the queue will be searched for the process closest to its deadline. This process is the next to be scheduled for execution.\n\n"
        		+ "If you use the EDF-preemptive version, then a running process can be interrupted from another process with an earlier deadline!\n"
        		+ "If you use the EDF-non-preemptive version, then a running process cannot be interrupted by another process until it is finished!";
    }


    /**
     * Not necessary when it implements Generator
     */
    private String getLines(String text){
    	String temp = text.replace("\n\n", " ").replace("\n", " ");
    	for(int i=50 ; i<temp.length() ; i=i+50){
    		int nextSpace = temp.indexOf(" ", i);
    		if(nextSpace>=0){
    			temp = temp.substring(0,nextSpace) + "\n" + temp.substring(nextSpace+1);
    		}
    	}
    	return temp;
    }

    /**
     * Not necessary when it implements Generator
     */
    private static final String SOURCE_CODE =
			"public void earliest_deadline_first(LinkedList<Process> list, boolean preemptive, int maxTime) {" // 0
		+ "\n   int currentTime = 0;" // 1
		+ "\n   Process runProcess = null;" // 2
		+ "\n   while (currentTime < maxTime) {" // 3
		+ "\n      if (list contains process with counter > 1) {" // 4
		+ "\n         throw new Exception(\\\"New Process arrived, but old not finished!\\\");" // 5
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
}
