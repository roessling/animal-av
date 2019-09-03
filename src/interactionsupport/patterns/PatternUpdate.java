package interactionsupport.patterns;
/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Vector;

import org.w3c.dom.Element;

public class PatternUpdate {
	private String xmlFile = null;
	private StringBuffer sb = null;
	private PatternParser parser;
	private XMLPanel xml;
	
	public PatternUpdate(String file) {
		xmlFile = file;
		parser = new PatternParser("InteractionPatternsSchema.xsd", xmlFile);
	}
	
	public void setXMLPanel(XMLPanel x) {
		xml = x;
	}
	
	private void readFile() {
		try{
	        FileInputStream fstream = new FileInputStream(xmlFile);
	        DataInputStream in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        String strLine;
	        sb = new StringBuffer();
	        
	        // Read File Line By Line
	        while ((strLine = br.readLine()) != null)   {
	          sb.append(strLine);
	        }
	        
	        in.close();
	        }catch (Exception e){
	          System.err.println("Error: " + e.getMessage());
	    }
	}
	
	private void writeFile(StringBuffer sb2) {
		// Insert in existing file
		int ende = sb.indexOf("</patterns>");
		sb.insert(ende, sb2);
		
		try{
	    FileWriter fstream = new FileWriter(xmlFile);
	    BufferedWriter out = new BufferedWriter(fstream);
	    out.write(sb.toString());
	    out.close();
	    }catch (Exception e){
	      System.err.println("Error: " + e.getMessage());
	    }
	}
	
	private void buildStandard(StringBuffer sb2, QuestionPanel question) {
		sb2.append("<prompt>" + question.getPrompt() + "</prompt>");
		sb2.append("<pointsPossible>" + question.getPointsPossible() + "</pointsPossible>");
		sb2.append("<group>" + question.getGroup() + "</group>");
	}
	
	public void update(TFPanel question) {
		parser.parse();
		
		if(!parser.isUidExisting(question.getUid())) {
			readFile();
			StringBuffer sb2 = new StringBuffer();
			
			sb2.append("<TFQuestion uid=\"" + question.getUid() + "\">");
			
			buildStandard(sb2, question);
			
			sb2.append("<answerState>" + question.isAnswerState() + "</answerState>");
			sb2.append("<commentTrue>" + question.getCommentTrue() + "</commentTrue>");
			sb2.append("<commentFalse>" + question.getCommentFalse() + "</commentFalse>");
			
			sb2.append("</TFQuestion>");
			
			writeFile(sb2);
			
			question.setWarning("");
			question.resetInteraction();
			updateXMLPanel();
		} else {
			question.setWarning("<html><font color=\"#FF0000\">Pattern \"" + question.getUid() + "\" already in use. Select another uid</font></html>");
			question.setUid("");
		}
	}
	
	public void update(FIBPanel question) {
		parser.parse();
		
		if(!parser.isUidExisting(question.getUid())) {
			readFile();
			
			StringBuffer sb2 = new StringBuffer();
			
			sb2.append("<FIBQuestion uid=\"" + question.getUid() + "\">");
			
			buildStandard(sb2, question);
			
			sb2.append("<answer>" + question.getAnswer() + "</answer>");
			sb2.append("<comment>" + question.getComment() + "</comment>");
			
			sb2.append("</FIBQuestion>");
			
			writeFile(sb2);
			
			question.setWarning("");
			question.resetInteraction();
			updateXMLPanel();
		} else {
			question.setWarning("<html><font color=\"#FF0000\">Pattern \"" + question.getUid() + "\" already in use. Select another uid</font></html>");
			question.setUid("");
		}
	}

	public void update(MCPanel question) {
		parser.parse();
		
		if(!parser.isUidExisting(question.getUid())) {
			readFile();
			StringBuffer sb2 = new StringBuffer();
			
			sb2.append("<MCQuestion uid=\"" + question.getUid() + "\">");
			
			buildStandard(sb2, question);
			
			Vector<Answer> answerSet = question.getAnswerSet();
			Answer answer = null;
			
			for(int i = 0; i < answerSet.size(); i++) {
				answer = answerSet.elementAt(i);
				
				sb2.append("<answer>");
				sb2.append("<text>" + answer.getAnswer() + "</text>");
				sb2.append("<points>" + answer.getPoints() + "</points>");
				sb2.append("<comment>" + answer.getComment() + "</comment>");
				sb2.append("<state>" + answer.isState() + "</state>");
				sb2.append("</answer>");
			}
			
			sb2.append("</MCQuestion>");
			
			writeFile(sb2);

			question.setWarning("");
			question.resetInteraction();
			updateXMLPanel();
		} else {
			question.setWarning("<html><font color=\"#FF0000\">Pattern \"" + question.getUid() + "\" already in use. Select another uid</font></html>");
			question.setUid("");
		}
	}
	
	public void update(MSPanel question) {
		parser.parse();
		
		if(!parser.isUidExisting(question.getUid())) {
			readFile();
			StringBuffer sb2 = new StringBuffer();
			
			sb2.append("<MSQuestion uid=\"" + question.getUid() + "\">");
			
			buildStandard(sb2, question);
			
			Vector<Answer> answerSet = question.getAnswerSet();
			Answer answer = null;
			
			for(int i = 0; i < answerSet.size(); i++) {
				answer = answerSet.elementAt(i);
				
				sb2.append("<answer>");
				sb2.append("<text>" + answer.getAnswer() + "</text>");
				sb2.append("<points>" + answer.getPoints() + "</points>");
				sb2.append("<comment>" + answer.getComment() + "</comment>");
				sb2.append("<state>" + answer.isState() + "</state>");
				sb2.append("</answer>");
			}
			
			sb2.append("</MSQuestion>");
			
			writeFile(sb2);

			question.setWarning("");
			question.resetInteraction();
			updateXMLPanel();
		} else {
			question.setWarning("<html><font color=\"#FF0000\">Pattern \"" + question.getUid() + "\" already in use. Select another uid</font></html>");
			question.setUid("");
		}
	}
	
	public void update(DocuPanel question) {
		parser.parse();
		
		if(!parser.isUidExisting(question.getUid())) {
			readFile();
			StringBuffer sb2 = new StringBuffer();
			
			sb2.append("<Documentation uid=\"" + question.getUid() + "\">");
			sb2.append("<URL>" + question.getURL() + "</URL>");
			sb2.append("</Documentation>");
			
			writeFile(sb2);

			question.setWarning("");
			question.resetInteraction();
			updateXMLPanel();
		} else {
			question.setWarning("<html><font color=\"#FF0000\">Pattern \"" + question.getUid() + "\" already in use. Select another uid</font></html>");
			question.setUid("");
		}
	}
	
	private void updateXMLPanel() {
		xml.update();
	}
	
	public String[] getUids() {
		parser.parse();
		
		Vector<String> tmp = parser.getUids();
		String[] uids = new String[tmp.size()];
		
		for(int i=0; i < tmp.size(); i++) {
			uids[i] = tmp.get(i);
		}
		
		return uids;
	}
	
	public Element getRoot() {
		parser.parse();
		return parser.getRoot();
	}
	
	public void delete(String uid) {
		parser.parse();
		
		if(parser.isUidExisting(uid)) {
			readFile();
			
			// Insert in existing file
			int start = sb.indexOf("<" + parser.getType(uid) + " uid=\"" + uid + "\">");
			int end = sb.indexOf("</" + parser.getType(uid) + ">", start);
			end = sb.indexOf(">", end);
			
			StringBuffer sb2 = new StringBuffer();
			sb2.append(sb.substring(0, start));
			sb2.append(sb.substring(end+1, sb.length()));
			
			try{
		    FileWriter fstream = new FileWriter(xmlFile);
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(sb2.toString());
		    out.close();
		    }catch (Exception e){
		      System.err.println("Error: " + e.getMessage());
		    }
		}
	}
}
