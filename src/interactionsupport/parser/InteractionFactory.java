/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */

package interactionsupport.parser;

import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import interactionsupport.patterns.PatternParser;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import algoanim.primitives.generators.Language;

public class InteractionFactory {
	private Language lang;
	private PatternParser parser;
	
	public InteractionFactory(Language l, String xmlFile) {
		lang = l;
		parser = new PatternParser("InteractionPatternsSchema.xsd", xmlFile);
	}
	
	public TrueFalseQuestionModel generateTFQuestion(String patternName, String instanceName, String... placeholder) {
		String tmp = "";
		Element interaction = parser.getInteraction(patternName);
		
		if(interaction != null) {
			String name = interaction.getNodeName();
			
			if(name.equals("TFQuestion")) {
				NodeList child;
				
				TrueFalseQuestionModel tfQuestion = new TrueFalseQuestionModel(instanceName);
				// Define placeholder with {0} ... {n} in the Pattern
				child = interaction.getElementsByTagName("prompt");
				tmp = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
				tfQuestion.setPrompt(tmp);
				
				child = interaction.getElementsByTagName("pointsPossible");
			    tfQuestion.setPointsPossible(Integer.parseInt(child.item(0).getFirstChild().getNodeValue()));    
			    child = interaction.getElementsByTagName("group");
			    tfQuestion.setGroupID(child.item(0).getFirstChild().getNodeValue());
			    child = interaction.getElementsByTagName("answerState");
			    if(child.item(0).getFirstChild() != null) tfQuestion.setCorrectAnswer(child.item(0).getFirstChild().getNodeValue().equals("true"));
			    
			    // Define placeholder with {0} ... {n} in the Pattern
			    child = interaction.getElementsByTagName("commentTrue");
			    if(child.item(0).getFirstChild() != null) {
			    	tmp = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
					tfQuestion.setFeedbackForAnswer(true, tmp);
			    }
			    
			    
			    // Define placeholder with {0} ... {n} in the Pattern
			    child = interaction.getElementsByTagName("commentFalse");
			    if(child.item(0).getFirstChild() != null) {
			    	tmp = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
					tfQuestion.setFeedbackForAnswer(false, tmp);
			    }
			    
			    lang.addTFQuestion(tfQuestion);
			    lang.nextStep();
			    return tfQuestion;
			}			
			System.err.println("Pattern is not a TFQuestion");
			return null;
		}		
		System.err.println("Interaction Pattern not found");
		return null;
	}
	
	public FillInBlanksQuestionModel generateFIBQuestion(String patternName, String instanceName, String... placeholder) {
		String tmp = "";
		Element interaction = parser.getInteraction(patternName);
		
		if(interaction != null) {
			String name = interaction.getNodeName();
			
			if(name.equals("FIBQuestion")) {
				NodeList child;
				int points = 0;
				String answer = null, comment = null;
				
				FillInBlanksQuestionModel fibQuestion = new FillInBlanksQuestionModel(instanceName);
				child = interaction.getElementsByTagName("prompt");
				tmp = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
				fibQuestion.setPrompt(tmp);    
			    child = interaction.getElementsByTagName("group");
			    fibQuestion.setGroupID(child.item(0).getFirstChild().getNodeValue());
			    child = interaction.getElementsByTagName("pointsPossible");
			    if(child.item(0) != null) points = Integer.parseInt(child.item(0).getFirstChild().getNodeValue());
			    child = interaction.getElementsByTagName("answer");
			    if(child.item(0).getFirstChild() != null) {
			    	tmp = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
			    	answer = tmp;
			    }
			    child = interaction.getElementsByTagName("comment");
			    if(child.item(0).getFirstChild() != null) {
			    	tmp = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
					comment = tmp;
			    }
			    
			    if(answer != null && comment != null) fibQuestion.addAnswer(answer, points, comment);
			    
				lang.addFIBQuestion(fibQuestion);
				lang.nextStep();
				return fibQuestion;
			}		
			System.err.println("Pattern is not a FIBQuestion");
			return null;
		}	
		System.err.println("Interaction Pattern not found");
		return null;
	}
	
	public MultipleChoiceQuestionModel generateMCQuestion(String patternName, String instanceName, String... placeholder) {
		String tmp = "";
		Element interaction = parser.getInteraction(patternName);
		
		if(interaction != null) {
			String name = interaction.getNodeName();
			
			if(name.equals("MCQuestion")) {
				NodeList child;
				NodeList answers;
				int points;
				String text, comment;
				
				MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(instanceName);
				
				// Define placeholder with {0} ... {n} in the Pattern
				child = interaction.getElementsByTagName("prompt");
				tmp = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
				mcq.setPrompt(tmp);
				
			    child = interaction.getElementsByTagName("group");
			    mcq.setGroupID(child.item(0).getFirstChild().getNodeValue());
			    child = interaction.getElementsByTagName("pointsPossible");
			    
			    answers = interaction.getElementsByTagName("answer");
			    for(int i = 0; i < answers.getLength(); i++) {
			    	// Define placeholder with {0} ... {n} in the Pattern
			    	child = ((Element)answers.item(i)).getElementsByTagName("text");
			    	text = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
					
			    	child = ((Element)answers.item(i)).getElementsByTagName("points");
			    	points = Integer.parseInt(child.item(0).getFirstChild().getNodeValue());
			    	
			    	// Define placeholder with {0} ... {n} in the Pattern
			    	child = ((Element)answers.item(i)).getElementsByTagName("comment");
			    	comment = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
					
			    	child = ((Element)answers.item(i)).getElementsByTagName("state");
			    	mcq.addAnswer(text, points, comment);
			    }
			    
				lang.addMCQuestion(mcq);
				lang.nextStep();
			    return mcq;
			}		
			System.err.println("Pattern is not a MCQuestion");
			return null;
		}	
		System.err.println("Interaction Pattern not found");
		return null;
	}

	public MultipleSelectionQuestionModel generateMSQuestion(String patternName, String instanceName, String... placeholder) {
		String tmp = "";
		Element interaction = parser.getInteraction(patternName);
		
		if(interaction != null) {
			String name = interaction.getNodeName();
			
			if(name.equals("MSQuestion")) {
				NodeList child;
				NodeList answers;
				int points;
				String text, comment;
				
				MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel(instanceName);
				child = interaction.getElementsByTagName("prompt");
				tmp = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
				msq.setPrompt(tmp);    
				child = interaction.getElementsByTagName("group");
			    msq.setGroupID(child.item(0).getFirstChild().getNodeValue());
			    child = interaction.getElementsByTagName("pointsPossible");
			    
			    answers = interaction.getElementsByTagName("answer");
			    for(int i = 0; i < answers.getLength(); i++) {
			    	child = ((Element)answers.item(i)).getElementsByTagName("text");
			    	text = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
					child = ((Element)answers.item(i)).getElementsByTagName("points");
			    	points = Integer.parseInt(child.item(0).getFirstChild().getNodeValue());
			    	child = ((Element)answers.item(i)).getElementsByTagName("comment");
			    	comment = replacePlaceholder(child.item(0).getFirstChild().getNodeValue(), placeholder);
					
			    	child = ((Element)answers.item(i)).getElementsByTagName("state");
			    	msq.addAnswer(text, points, comment);
			    }
			    
				lang.addMSQuestion(msq);
				lang.nextStep();
			    return msq;
			}			
			System.err.println("Pattern is not a MSQuestion");
			return null;
		}		
		System.err.println("Interaction Pattern not found");
		return null;
	}
	
	public void generateDocumentation(String patternName, String instanceName, String... placeholder) {
		Element interaction = parser.getInteraction(patternName);
		
		if(interaction != null) {
			String name = interaction.getNodeName();
			
			if(name.equals("Documentation")) {
				NodeList child;
				
				HtmlDocumentationModel link = new HtmlDocumentationModel(instanceName);
				child = interaction.getElementsByTagName("URL");
				link.setLinkAddress(child.item(0).getFirstChild().getNodeValue());
			    
				lang.addDocumentationLink(link);
				lang.nextStep();
				return;
			}
			System.err.println("Pattern is not a Documentation");
			return;
		}
		System.err.println("Interaction Pattern not found");
	}
	
	private String replacePlaceholder(String tmp, String[] placeholder) {
	  String placeHolder = tmp;
		int index = -1;
		int pos = placeHolder.indexOf("{");
		String placeholderIndex = "";

		while(pos != -1) {
			placeholderIndex = placeHolder.substring(pos+1, pos+2);
			try{
				index = Integer.parseInt(placeholderIndex);
				
				if(index < placeholder.length) {
				  placeHolder = placeHolder.replace("{" + placeholderIndex + "}", placeholder[index]);
				} else {
					System.err.println("There is no parameter given for the placeholder {" + index + "}");
				}
			}catch(NumberFormatException e) {
				System.err.println("{" + placeholderIndex + "} is not a valid placeholder");
			}
			
			pos = placeHolder.indexOf("{", pos+2);
		}
		
		return placeHolder;
	}
}
