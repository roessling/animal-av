package animal.vhdl.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import animal.misc.ColorChoice;
import animal.misc.MessageDisplay;
import animal.vhdl.graphics.PTD;
import animal.vhdl.graphics.PTEntity;
import animal.vhdl.graphics.PTNot;
import animal.vhdl.graphics.PTVHDLElement;
/**
 * 
 * @author lu,zheng
 *
 */

public class VHDLAnalyzer extends VHDLBaseAnalyzer{
	/** store all VHDL'Elenment 
	 *  Example: VHDL expression: c=a AND b  
	 *  create a PTAnd object to ArrayList
	 */
	private static ArrayList<PTVHDLElement> VHDLElements;
	
	/**
	 * store code from ENTITY to END ENTITY
	 */
	private ArrayList<String> entityCode;
	/**
	 * 	store code from ARCHITECTURE to END ARCHITECTURE 
	 */
	private ArrayList<String> architectureCode;
	
	public static ArrayList<PTVHDLElement> getElements() {
		return VHDLElements;
	}

	public void setElements() {
		VHDLElements=new ArrayList<PTVHDLElement>(0);
		int baseCodeNumber=Integer.parseInt(entityCode.get(0));
		if (entityCode!=null)
			entityAnalyse(entityCode,(byte)0,baseCodeNumber,baseCodeNumber+entityCode.size()-1);
		if (architectureCode!=null)
			architectureCodeAnalyse(architectureCode);
		setElementAttribute();
	}
	private void setElementAttribute() {
		Properties config = VHDLPropertiesImporter.PropertiesImporter();
		for(PTVHDLElement ele:VHDLElements){
			String className=ele.getClass().getName().substring(ele.getClass().getName().lastIndexOf(".")+1);
			String color=(String) config.get(className+".highlightColor");
			if (color!=null){
				ele.setColor(ColorChoice.getColor(color));
			}
			String fillColor=(String) config.get(className+".highlightFillColor");
			if (fillColor!=null){
				ele.setFillColor(ColorChoice.getColor(fillColor));
			}
			color=(String) config.get(className+".color");
			if (color!=null){
				ele.setDefaultColor(ColorChoice.getColor(color));
			}
			fillColor=(String) config.get(className+".fillColor");
			if (fillColor!=null){
				ele.setDefaultFillColor(ColorChoice.getColor(fillColor));
			}
			String isFilled=(String) config.get(className+".isFilled");
			if(isFilled!=null)
				ele.setFilled(Boolean.valueOf(isFilled));
		}
	}

	@SuppressWarnings("unchecked")
  private void architectureCodeAnalyse(ArrayList<String> architectureCode) {
		int start=0,end=0;//start and end number in array,which contains component sentence 
		ArrayList<String> component=new ArrayList<String>();
		//architectureCode.get(0) is codeLineNumber
		int baseCodeNumber=Integer.parseInt(architectureCode.get(0));
		for(int i=1;i<architectureCode.size();i++){
			if (architectureCode.get(i).trim().toLowerCase().startsWith("component")){
				start=i;
			}
			if (architectureCode.get(i).trim().toLowerCase().startsWith("end component")){
				end=i;
			}
			if (start!=0 && end !=0 && start<end){
				for(int j=start;j<=end;j++){
					component.add(architectureCode.get(j));
				}
				entityAnalyse(component,(byte)1,baseCodeNumber+start-1,baseCodeNumber+end-2);
				start=0;
				end=0;
				component.clear();
			}
		}
		ArrayList <String> functionDescribeSentences=findExpressions(architectureCode,"begin","end architecture");
		String[] subExpression=VHDLExpressionAnalyzer.splitlogicExpressions(functionDescribeSentences);
		int codeNrBegin=0;
		int codeNrEnd=0;
		for (int i=0;i<subExpression.length;i++){
			String temp=subExpression[i].replaceAll("(\\(|\\))", "");
			String [] tempPorts=temp.split(" ");
			ArrayList<String> portsWithoutSpace=new ArrayList<String>();
			for (int y=0;y<tempPorts.length;y++){
				if (!tempPorts[y].equals(""))
					portsWithoutSpace.add(tempPorts[y]);
			}
			Class<PTVHDLElement> c;
			PTVHDLElement element=null;
			boolean mat = temp.matches("\\d+");
			if (mat){
				codeNrBegin=Integer.parseInt(temp);
				codeNrEnd=Integer.parseInt(temp);
			}
			if (portsWithoutSpace.size()==2){
				mat = portsWithoutSpace.get(0).matches("\\d+");
				if (mat)
					codeNrBegin=Integer.parseInt(portsWithoutSpace.get(0));
				mat = portsWithoutSpace.get(1).matches("\\d+");
				if (mat)
					codeNrEnd=Integer.parseInt(portsWithoutSpace.get(1));
			}
			if (portsWithoutSpace.size()>2){
				if (portsWithoutSpace.get(2).equalsIgnoreCase("not")){
					element=new PTNot();
					element.getInputPins().get(0).setPinName(portsWithoutSpace.get(3));
					element.getOutputPins().get(0).setPinName(portsWithoutSpace.get(0));
					element.setCodeLineNumberBegin(codeNrBegin);
					element.setCodeLineNumberEnd(codeNrEnd);
					//element.setObjectName("NOT");
					element.setEntityType((byte)2);
					insertElements(element);
				}
				else{
					if (portsWithoutSpace.size()>3){
						String className=isKnownElement(portsWithoutSpace.get(3).toLowerCase());
							if (className!=null){
								try {
									c=(Class<PTVHDLElement>) Class.forName(className);
									element = c.newInstance();
									element.getInputPins().get(0).setPinName(portsWithoutSpace.get(2));
									element.getInputPins().get(1).setPinName(portsWithoutSpace.get(4));
									element.getOutputPins().get(0).setPinName(portsWithoutSpace.get(0));
									element.setEntityType((byte)2);
									element.setCodeLineNumberBegin(codeNrBegin);
									element.setCodeLineNumberEnd(codeNrEnd);
									//element.setObjectName(ports[3].toUpperCase());
									insertElements(element);
								} catch (InstantiationException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								} 
							}
						}
					else{
						element=new PTNot();
						element.getInputPins().get(0).setPinName(portsWithoutSpace.get(2));
						element.getOutputPins().get(0).setPinName(portsWithoutSpace.get(0));
						element.setEntityType((byte)3);  // only for wire, z.B out=c
						element.setObjectName("WIRE");
						insertElements(element);
					}
					if (portsWithoutSpace.get(2).equalsIgnoreCase("DFF")){
						PTD element1=new PTD();
						element1.setAsynSR(true);
						element1.setSynControl(true);
						element1.getInputPins().get(0).setPinName(portsWithoutSpace.get(3));
						element1.getOutputPins().get(0).setPinName(portsWithoutSpace.get(0));
						element1.setCodeLineNumberBegin(codeNrBegin);
						element1.setCodeLineNumberEnd(codeNrEnd);
						//element.setObjectName("NOT");
						if (!portsWithoutSpace.get(4).equals("NIL"))
							element1.getControlPins().get(0).setPinName(portsWithoutSpace.get(4));
						else
							element1.setSynControl(false);
						if (!portsWithoutSpace.get(5).equals("NIL"))
							element1.getControlPins().get(1).setPinName(portsWithoutSpace.get(5));
						else
							element1.setSynControl(false);
						if (!portsWithoutSpace.get(6).equals("NIL"))
							element1.getControlPins().get(2).setPinName(portsWithoutSpace.get(6));
						else
							element1.setAsynSR(false);
						if (!portsWithoutSpace.get(7).equals("NIL"))
							element1.getControlPins().get(3).setPinName(portsWithoutSpace.get(7));
						else
							element1.setAsynSR(false);
						element1.setEntityType((byte)2);
						insertElements(element1);
					}
					if (portsWithoutSpace.size()>3 && portsWithoutSpace.get(3).equalsIgnoreCase("mux")){
	//					PTMux element1=new PTMux();
						
	//					element1.getInputPin().get(0).setPinName(ports[2]);
	//					element1.getInputPin().get(1).setPinName(ports[4]);
	//					element1.getOutputPin().get(0).setPinName(ports[0]);
	//					element1.getControlPin().get(0).setPinName(ports[6]);
	//					element1.setEntityType((byte)2);
	//					insertElements(element1);
					}
				}
		}
			}
		codeNrBegin=Integer.parseInt(functionDescribeSentences.get(0));
		for(int i=1;i<functionDescribeSentences.size();i++){
			if (functionDescribeSentences.get(i).toLowerCase().contains("port map")){ // check PORT MAP
				PTVHDLElement gate=VHDLPortMapAnalyzer.insertPortMapEntity(functionDescribeSentences.get(i),codeNrBegin+i-1);
				if (gate!=null)
					VHDLElements.add(gate);
			}
		}

	}
	
/**
 * 
 * @param entityCode
 * @param entityType 0:main entity 1:entity or component 2: base element 
 * @param codeLineNumberEnd 
 * @param codeLineNumberBegin 
 */
	@SuppressWarnings("unchecked")
  private void entityAnalyse(ArrayList<String> entityCode,byte entityType, int codeLineNumberBegin, int codeLineNumberEnd) {
		String entityName="";
		String temp="";
		try{
		if (entityType==0)
			//entityCode.get(0) is codelinenubmer.
			entityName=entityCode.get(1).trim().substring(7,entityCode.get(1).length()-3);//ENTITY entityName IS
		else if(entityType==1)
			entityName=entityCode.get(0).trim().substring(10);//COMPONENT entityName
		for (int i=1;i<entityCode.size();i++){
				temp=temp+entityCode.get(i).trim();
		}
		}catch (IndexOutOfBoundsException e){
			MessageDisplay.errorMsg("Entity part of VHDL Date have error",MessageDisplay.RUN_ERROR);
		}
		int start=temp.indexOf("(");
		int end=temp.indexOf(")");
		temp=temp.substring(start+1, end); //ain,bin,cin : IN STD_LOGIC; cout,sum : OUT STD_LOGIC
		setPins(temp);
		Class<PTVHDLElement> c;
		PTVHDLElement pe;
		String className=isKnownElement(entityName.toLowerCase());
		if (className!=null)
			try {
			c=(Class<PTVHDLElement>) Class.forName(className);
			pe = c.newInstance();
			for(int i=0;i<getInputPins().size();i++)
				pe.setInputPins(getInputPins());
			//pe.setInputPin(getInputPin());
			for(int i=0;i<getOutputPins().size();i++)
				pe.setOutputPins(getOutputPins());
			//pe.setOutputPin(getOutputPin());
			pe.setEntityType(entityType);
			pe.setObjectName(entityName);
			pe.setCodeLineNumberBegin(codeLineNumberBegin);
			pe.setCodeLineNumberEnd(codeLineNumberEnd);
			insertElements(pe);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
		else{
			//System.out.print("entity");
			PTEntity x=new PTEntity();
			x.setInputPins(getInputPins());
			x.setOutputPins(getOutputPins());
			x.setInoutPins(getInoutputPins());
			x.setObjectName(entityName);
			x.setElementSymbol(entityName);
			x.setEntityType(entityType);
			x.setCodeLineNumberBegin(codeLineNumberBegin);
			x.setCodeLineNumberEnd(codeLineNumberEnd);
			insertElements(x);
		}
	}


	public void removeall(){
		VHDLElements=new ArrayList<PTVHDLElement>(0);
	}
	public void removeElements(byte type){
		for (int i=0;i<VHDLElements.size();i++){
			if (VHDLElements.get(i).getEntityType()==type)
				removeElements(i);
		}
			
	}
	public void removeElements(int index){
		VHDLElements.remove(index);
	}
	private void insertElements(PTVHDLElement e){
		VHDLElements.add(e);
	}
	public void importVHDLFrom (InputStream in){
		VHDLImporter vi=new VHDLImporter();
		vi.importVHDLFrom(in);
		//vi.getCodeLine("sss");
		entityCode=vi.getEntityCode();
		architectureCode=vi.getArchitectureCode();
		setElements();
	}
	public void importVHDLFrom (String filename){
		InputStream in = null;
		try{
			in= new FileInputStream(filename);
			importVHDLFrom(in);
		}catch (IOException e) {
		      MessageDisplay.errorMsg("importException " +filename,MessageDisplay.RUN_ERROR);
		        }
	}
	public static void  main(String []args){
		VHDLAnalyzer test=new VHDLAnalyzer();
		test.importVHDLFrom("VHDLBeispiel"+File.separator+"hald_add.txt");
//		VHDLImporter t=new VHDLImporter();
//		t.importVHDLFrom("VHDLBeispiel\\D.txt");
//		String[] x=t.getCodeLine("sss");
//		for(int i=0;i<x.length;i++)
//			System.out.println(x[i]);
		new VHDLElementsToXML(VHDLAnalyzer.getElements());
	}

}
