package generators.graph.lindenmayer;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import algoanim.util.Coordinates;
import algoanim.util.Node;

public class ParserAnimal {
	
	public static String solution = "";
	public static String result = "";
	public static int open = 0;
	public static int close = 0;
	public static String parse(String productionsString, int iterations, String startSymbol){
		LinkedList<Production> productions = new LinkedList<Production>();
		productionsString = productionsString.replaceAll("\\s+","");
		String errorMessage;
		while(productionsString.length() > 0){
			String rule;
			if(productionsString.indexOf(",") == -1) rule = productionsString;
			else rule = productionsString.substring(0, productionsString.indexOf(","));
			productionsString = productionsString.substring(rule.length());
			productionsString = productionsString.replaceFirst(",","");
			
			if(!rule.matches("[A-Za-z]->[\\[\\]\\+\\-a-zA-Z]*")){ 
				errorMessage = (rule + " is not a valid rule. "); 
				errorMessage = errorMessage.concat("Please correct your Rule coordinate and use only Letters from 'A-Z' and no Symbols and Numbers.");
				JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), errorMessage, "Fehler", JOptionPane.ERROR_MESSAGE);
				throw new IllegalArgumentException(errorMessage);
			//	LindenmayerAPIGenerator.showErrorWindow(errorMessage);
//				
			
				}
			String leftSide = rule.substring(0, rule.indexOf("->"));
			String rightSide = rule.substring(rule.indexOf("->")+2, rule.length());
			productions.addLast(new Production(leftSide, rightSide));
		}
	
		solution = startSymbol;
		LindenmayerAPIGenerator.lang.newText(new Coordinates(20, 335),
				"Iteration: 0 | Ergebnis: " + solution, "rulex", null, LindenmayerAPIGenerator.shownTextProperties);
//	    LindenmayerAPIGenerator.rsrc.highlight("regel");
		LindenmayerAPIGenerator.lang.nextStep("Iteration 0");
		int i = 0;
		while(i < iterations){
			boolean found = false;
			for(Production p : productions){
				if(!found && solution.contains(p.getLeftSide())){
					found = true;
					solution = solution.replaceFirst(p.getLeftSide(), p.getRightSide());
//					 LindenmayerAPIGenerator.rsrc.unhighlight("regel");
					LindenmayerAPIGenerator.lang.newText(new Coordinates(20, 360 + (i)*25),
							"Iteration: " + (i+1) + " | Ergebnis: " + solution, "rule" + i+3, null, LindenmayerAPIGenerator.shownTextProperties);
					LindenmayerAPIGenerator.lang.nextStep("Iteration " + (i+1));
		
				}
				
			}
			System.out.println("iteration = " + i + " " + solution);
			i++;
		}

		for (int j = 0; j < solution.length(); j++) {
			switch (solution.charAt(j)) {
			case 'F':
				result += "F";
				break;
			case '+':
				if(j == solution.length()-1){ result += "";}
				else {result += "+";}
				break;
			case '-':
				if(j == solution.length()-1){ result += "";} //[+F-+-[-++-]}
				else {result += "-";}
				break;
			case '[':
				if(j == solution.length()-1){ result += "";}
//				else if(solution.lastIndexOf("F") < j){
//					
//				}
				else { result += "[";}
				break;
			case ']':
				if(j == 0){ result += "";}
				else {result += "]";}
				break;
			default:
				result += "";
				break;
			}

	}
	

		for (int k = 0; k < result.length(); k++) {
			if(result.charAt(k) == '[') open++;
			if(result.charAt(k) == ']') close++;
		} if(open!= close) {
			open = 0;
			close = 0;
			errorMessage = (result + " kein g�ltiger String. "); 
			errorMessage = errorMessage.concat("Zu viele geschlossene oder offene Klammer.");
			JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), errorMessage, "Fehler", JOptionPane.ERROR_MESSAGE);
			result = "";
			throw new IllegalArgumentException(errorMessage);
		
		}

		System.out.println("result " + result);
	
		return solution;
		//return result;
	}

//	public static void main(String[] args){
//      ParserAnimal.parse("S->FA,A->[+F]A,", 3,"S");
//	}
}
