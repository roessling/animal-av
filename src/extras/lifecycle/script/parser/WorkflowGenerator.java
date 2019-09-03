package extras.lifecycle.script.parser;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;

import extras.lifecycle.script.parser.Expression;
import extras.lifecycle.script.parser.FunctionExpression;
import extras.lifecycle.script.parser.SimpleExpression;
import extras.lifecycle.script.parser.WorkflowGenerator;
import extras.lifecycle.script.parser.WorkflowGeneratorException;

import extras.lifecycle.query.workflow.AbstractBox;
import extras.lifecycle.query.workflow.AssignBox;
import extras.lifecycle.query.workflow.Box;
import extras.lifecycle.query.workflow.Calculator;
import extras.lifecycle.query.workflow.FunctionBox;
import extras.lifecycle.query.workflow.ScriptBox;
import extras.lifecycle.script.generated.analysis.DepthFirstAdapter;
import extras.lifecycle.script.generated.lexer.Lexer;
import extras.lifecycle.script.generated.lexer.LexerException;
import extras.lifecycle.script.generated.node.AAssignStatement;
import extras.lifecycle.script.generated.node.ADecimalIntegerLiteral;
import extras.lifecycle.script.generated.node.ADecimalIntegerLiteralValueExpression;
import extras.lifecycle.script.generated.node.AExpressiononlyStatement;
import extras.lifecycle.script.generated.node.AFunctionExpression;
import extras.lifecycle.script.generated.node.AIdentifierValueExpression;
import extras.lifecycle.script.generated.node.AScript;
import extras.lifecycle.script.generated.node.ASingleExpression;
import extras.lifecycle.script.generated.node.AStringLiteralValueExpression;
import extras.lifecycle.script.generated.node.Start;
import extras.lifecycle.script.generated.node.TIdentifier;
import extras.lifecycle.script.generated.parser.Parser;
import extras.lifecycle.script.generated.parser.ParserException;

public class WorkflowGenerator extends DepthFirstAdapter {
	//public static final String SampleScript = "A=B" + "\n" + "C=D";

	private ScriptBox scriptBox;
	private Stack<Box> boxes;

	public WorkflowGenerator() {
		super();
		boxes = new Stack<Box>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeextras.lifecylcle.script.generated.analysis.DepthFirstAdapter#
	 * inAAssignStatement
	 * (extras.lifecylcle.script.generated.node.AAssignStatement)
	 */
	@Override
	public void inAAssignStatement(AAssignStatement node) {
		// This comes when there is an assignment
		AssignBox anAssignBox = new AssignBox();

		String varName = node.getId().getText();
		assert varName != null && !varName.isEmpty();
		anAssignBox.setVariableName(varName);

		boxes.push(anAssignBox);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeextras.lifecylcle.script.generated.analysis.DepthFirstAdapter#
	 * outAAssignStatement
	 * (extras.lifecylcle.script.generated.node.AAssignStatement)
	 */
	@Override
	public void outAAssignStatement(AAssignStatement node) {
		Expression seb = (Expression) boxes.pop();
		Calculator expression = seb.generateExpressionCalculator();

		AssignBox anAssignBoxAfterProcessing = (AssignBox) boxes.pop();

		anAssignBoxAfterProcessing.setExpression(expression);
		// This is a component, which is always attached to the main script box
		scriptBox.append(anAssignBoxAfterProcessing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeextras.lifecylcle.script.generated.analysis.DepthFirstAdapter#
	 * outAExpressiononlyStatement
	 * (extras.lifecylcle.script.generated.node.AExpressiononlyStatement)
	 */
	@Override
	public void outAExpressiononlyStatement(AExpressiononlyStatement node) {
		FunctionExpression functionExpression = (FunctionExpression) boxes
				.pop();

		assert functionExpression instanceof FunctionExpression;

		FunctionBox functionBox = functionExpression.generateFunctionBox();

		// This is a component, which is always attached to the main script box
		scriptBox.append(functionBox);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * extras.lifecylcle.script.generated.analysis.DepthFirstAdapter#inAScript
	 * (extras.lifecylcle.script.generated.node.AScript)
	 */
	@Override
	public void inAScript(AScript node) {
		// Here starts our script. We generate one script box as a holder
		// the other boxes will be attached to this one.

		scriptBox = new ScriptBox();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * extras.lifecylcle.script.generated.analysis.DepthFirstAdapter#outAScript
	 * (extras.lifecylcle.script.generated.node.AScript)
	 */
	@Override
	public void outAScript(AScript node) {
		assert isTopBoxReached();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeextras.lifecylcle.script.generated.analysis.DepthFirstAdapter#
	 * inASingleExpression
	 * (extras.lifecylcle.script.generated.node.ASingleExpression)
	 */
	@Override
	public void inASingleExpression(ASingleExpression node) {
		boxes.push(new SimpleExpression());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeextras.lifecylcle.script.generated.analysis.DepthFirstAdapter#
	 * inAFunctionExpression
	 * (extras.lifecylcle.script.generated.node.AFunctionExpression)
	 */
	@Override
	public void inAFunctionExpression(AFunctionExpression node) {

		String functionName = node.getId().getText();
		boxes.push(new FunctionExpression(functionName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeextras.lifecylcle.script.generated.analysis.DepthFirstAdapter#
	 * outAIdentifierValueExpression
	 * (extras.lifecylcle.script.generated.node.AIdentifierValueExpression)
	 */
	@Override
	public void outAIdentifierValueExpression(AIdentifierValueExpression node) {
		TIdentifier identifier = node.getIdentifier();
		String identifierName = identifier.getText();

		Expression eb = (Expression) boxes.peek();
		eb.putIdentifier(identifierName);
		// eb.setExpressionCalculator(new ValueOfIdentifier(identifierName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeextras.lifecylcle.script.generated.analysis.DepthFirstAdapter#
	 * outADecimalIntegerLiteralValueExpression
	 * (extras.lifecylcle.script.generated
	 * .node.ADecimalIntegerLiteralValueExpression)
	 */
	@Override
	public void outADecimalIntegerLiteralValueExpression(
			ADecimalIntegerLiteralValueExpression node) {
		ADecimalIntegerLiteral decimalIntegerLiteral = (ADecimalIntegerLiteral) node
				.getDecimalIntegerLiteral();
		String value = decimalIntegerLiteral.getDecimalNumeral().getText();

		Expression eb = (Expression) boxes.peek();
		eb.putDecimalIntegerValue(value);
		// eb.setExpressionCalculator(new ConstantValue(value));
	}

	public Box getRootBox() {
		return scriptBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeextras.lifecylcle.script.generated.analysis.DepthFirstAdapter#
	 * outAStringLiteralValueExpression
	 * (extras.lifecylcle.script.generated.node.AStringLiteralValueExpression)
	 */
	@Override
	public void outAStringLiteralValueExpression(
			AStringLiteralValueExpression node) {
		String value = node.getStringLiteral().getText();
		if (value == null)
			value = "";
		else {
			// The minimal string is ""
			// we remove the trailing " and the starting "
			value = value.substring(1, value.length() - 1);
		}

		Expression eb = (Expression) boxes.peek();
		eb.putStringValue(value);

		// eb.setExpressionCalculator(new ConstantValue(value));
	}
//
//	public static void main(String[] args) throws FileNotFoundException {
//		// String expr = args.length == 0 ? SampleScript : args[0];
//		// Reader in = new StringReader(expr);
//		//        
//		String fileName = "questionscriptsample.txt";
//		Reader in = new FileReader(fileName);
//
//		Parser parser = new Parser(new Lexer(new PushbackReader(in)));
//		try {
//			Start start = parser.parse();
//			WorkflowGenerator workflowGenerator = new WorkflowGenerator();
//
//			start.getPScript().apply(workflowGenerator);
//
//			System.out.println(workflowGenerator.getRootBox());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//	}

	private boolean isTopBoxReached() {
		// Box b = boxes.peek();
		// return b != null && b instanceof ScriptBox;

		return boxes.isEmpty();
	}

	public AbstractBox generate(String workflowScript) throws WorkflowGeneratorException {
		AbstractBox result = null;
		Reader in = new StringReader(workflowScript);
		Parser parser = new Parser(new Lexer(new PushbackReader(in)));
		Start start;
		WorkflowGenerator workflowGenerator = new WorkflowGenerator();
		try {
			start = parser.parse();

			start.getPScript().apply(workflowGenerator);

			result = (AbstractBox) workflowGenerator.getRootBox();
		} catch (ParserException e) {
			throw new WorkflowGeneratorException(e);
		} catch (LexerException e) {
			throw new WorkflowGeneratorException(e);
		} catch (IOException e) {
			throw new WorkflowGeneratorException(e);
		}

		result = (AbstractBox) workflowGenerator.getRootBox();
		
	//s	WorkflowGenerator workflowGenerator = new WorkflowGenerator();

		//result = (AbstractBox) this.getRootBox();
		return result;
	}
}
