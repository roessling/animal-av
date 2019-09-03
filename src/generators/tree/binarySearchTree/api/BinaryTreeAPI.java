package generators.tree.binarySearchTree.api;

import generators.network.helper.ClassName;
import generators.tree.binarySearchTree.binaryTree.Knoten;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import translator.Translator;
import algoanim.animalscript.AnimalPolylineGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalSourceCodeGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.animalscript.addons.InfoBox;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.DefaultStyle;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class BinaryTreeAPI {

	private Language lang;
	private Knoten root;
	private TextProperties circTextProp;
	private TextProperties textProp;
	private TextProperties headerProp;
	private RectProperties headerBoxProp;
	private CircleProperties circProp;
	private SourceCodeProperties sourceProp;
	private PolylineProperties lineProp;
	private TextProperties infoBoxTextProps;
	private SourceCodeProperties slideTextProps;
	private TextProperties slideHeaderProps;
	private Color circNormal;
	private Color circHighlight = Color.RED;
	private Color nextCircHighlight = Color.MAGENTA;
	private Color textHighlight = Color.RED;
	private Color textNormal;
	private Color circThirdHighlight = Color.BLUE;
	private int lineCount;
	private Coordinates rootCoords;
	public static int textOffset_X = 0;
	public static int textOffset_Y = -8;
	private int startWidth = 300;
	private int maxDepth = 0;
	private final int minXCoordinate = 600;
	private final int minLineWidth = 11;
	private QuestionGroupModel deleteQuestions;
	private Translator translator;
	private BinaryTreeStyle mySlideStyle;
	
	//statistical values
	private int accessesRead = 0;
	private int accessesWrite = 0;
	private int numNodes = 0;
	
	
	
	private static String insertSource = "public void insert(int key){"+
			"\n\tif(this.root == null)"+
			"\n\t\tthis.root = new Node(key);"+
			"\n\telse"+
			"\n\t\tinsert(key, this.root)"+
			"\n}\n "+
			"\npublic void insert(int key, Node currentNode) {"+
			"\n\tif (key <= currentNode.getKey()) {"+
			"\n\t\tif (currentNode.getLeft() != null)"+
			"\n\t\t\tinsert(key, currentNode.getLeft());"+
			"\n\t\telse"+
			"\n\t\t\tcurrentNode.setLeft(new Node(key));"+
			"\n\t} else {"+
			"\n\t\tif (currentNode.getRight() != null)"+
			"\n\t\t\tinsert(key, currentNode.getRight());"+
			"\n\t\telse"+
			"\n\t\t\tcurrentNode.setRight(new Node(key));"+
			"\n\t}"+
			"\n}";
	
	private static String delCallSource = "public boolean delete(int key){"+ 
			"\n\tif(root != null && key == root.getKey()){"+
			"\n\t\tKnoten left = root.getLeft();"+
			"\n\t\tKnoten right = root.getRight();"+
			"\n\t\tif(left == null && right == null){"+
			"\n\t\t\tthis.root = null;"+
			"\n\t\t}"+
			"\n\t\telse{"+
			"\n\t\t\tif (left == null) {"+
			"\n\t\t\t\tthis.root = right;"+
			"\n\t\t\t} else if (right == null) {"+
			"\n\t\t\t\tthis.root = left;"+
			"\n\t\t\t} else {"+
			"\n\t\t\t\tKnoten rightmostParent = this.searchRightmostParent(root, left);"+
			"\n\t\t\t\tif(rightmostParent.equals(root)){"+
			"\n\t\t\t\t\tleft.setRight(right);"+
			"\n\t\t\t\t\troot = left;"+
			"\n\t\t\t\t}"+
			"\n\t\t\t\telse{"+
			"\n\t\t\t\t\tKnoten rightmostChild = rightmostParent.getRight();"+
			"\n\t\t\t\t\tif (rightmostChild.getLeft() == null) {"+
			"\n\t\t\t\t\t\trightmostParent.setRight(null);"+
			"\n\t\t\t\t\t} else {"+
			"\n\t\t\t\t\t\trightmostParent.setRight(rightmostChild.getLeft());"+
			"\n\t\t\t\t\t}"+
			"\n\t\t\t\t\trightmostChild.setLeft(left);"+
			"\n\t\t\t\t\trightmostChild.setRight(right);"+
			"\n\t\t\t\t\tthis.root = rightmostChild;"+
			"\n\t\t\t\t}"+
			"\n\t\t\t}"+
			"\n\t\t}"+
			"\n\t\treturn true"+
			"\n\t}"+
			"\n\telse if(this.root != null)"+
			"\n\t\treturn this.delete(key, root);"+
			"\n\telse return false;"+
			"\n}";
	
	private static String delSource = "private boolean delete(int key, Knoten currentNode){"+
			"\n\tKnoten left = currentNode.getLeft();"+
			"\n\tKnoten right = currentNode.getRight();"+
			"\n\tif(left != null && left.getKey() == key){"+
				"\n\t\treturn this.deleteNode(currentNode, left, false);"+
			"\n\t}"+
			"\n\telse if(right != null && right.getKey() == key){"+
				"\n\t\treturn this.deleteNode(currentNode, right, true);"+
			"\n\t}"+
			"\n\telse if(left != null && key < currentNode.getKey())"+
				"\n\t\treturn delete(key, left);"+
			"\n\telse if(right != null && key > currentNode.getKey())"+
				"\n\t\treturn delete(key, right);"+
			"\n\telse"+
				"\n\t\treturn false;"+
		"\n}";
	
	private static String deleteNodeSource = "private boolean deleteNode(Knoten parent, Knoten toDelete, boolean leftRight){"+
			"\n\tKnoten left = toDelete.getLeft();"+
			"\n\tKnoten right = toDelete.getRight();"+
			"\n\tif(left == null && right == null){"+
				"\n\t\tif(leftRight)"+
					"\n\t\t\tparent.setRight(null);"+
				"\n\t\telse"+
					"\n\t\t\tparent.setLeft(null);"+
			"\n\t}"+
			"\n\telse {"+
				"\n\t\tif (left == null) {"+
					"\n\t\t\tif (leftRight)"+
						"\n\t\t\t\tparent.setRight(right);"+
					"\n\t\t\telse"+
						"\n\t\t\t\tparent.setLeft(right);"+
				"\n\t\t} else if (right == null) {"+
					"\n\t\t\tif (leftRight)"+
						"\n\t\t\t\tparent.setRight(left);"+
					"\n\t\t\telse"+
						"\n\t\t\t\tparent.setLeft(left);"+
				"\n\t\t} else {"+
					"\n\t\t\tKnoten rightmostParent = "
					+ "\n\t\t\t\t\tsearchRightmostParent(toDelete, toDelete.getLeft());"+
					"\n\t\t\tif (rightmostParent.equals(toDelete)) {"+
						"\n\t\t\t\ttoDelete.getLeft().setRight(toDelete.getRight());"+
						"\n\t\t\t\tif (leftRight)"+
							"\n\t\t\t\t\tparent.setRight(toDelete.getLeft());"+
						"\n\t\t\t\telse"+
							"\n\t\t\t\t\tparent.setLeft(toDelete.getLeft());"+
					"\n\t\t\t} else {"+
						"\n\t\t\t\tKnoten rightmostChild = rightmostParent.getRight();"+
						"\n\t\t\t\tif (rightmostChild.getLeft() == null)"+
							"\n\t\t\t\t\trightmostParent.setRight(null);"+
						"\n\t\t\t\telse"+
							"\n\t\t\t\t\trightmostParent.setRight(rightmostChild.getLeft());"+
						"\n\t\t\t\trightmostChild.setLeft(left);"+
						"\n\t\t\t\trightmostChild.setRight(right);"+
						"\n\t\t\t\tif (leftRight)"+
							"\n\t\t\t\t\tparent.setRight(rightmostChild);"+
						"\n\t\t\t\telse"+
							"\n\t\t\t\t\tparent.setLeft(rightmostChild);"+
					"\n\t\t\t}"+
				"\n\t\t}"+
			"\n\t}"+
			"\n\treturn true;"+
		"\n}";
	
	private static String findRightmostSource = "private Knoten searchRightmostParent(Knoten parent, Knoten child){"+
			"\n\tif(child.getRight() == null){"+
			"\n\t\treturn parent;"+
			"\n\t}"+
			"\n\telse"+
			"\n\t\treturn searchRightmostParent(child, child.getRight());"+
			"\n}";
	
	public BinaryTreeAPI(Language lang, SourceCodeProperties scProps,
			TextProperties headerProps, TextProperties textProps,
			Color textHighlight, TextProperties nodeTextProps,
			CircleProperties circProps, Color nodeHighlight,
			Color nodeHighlightNext, Color nodeHighlightThird,
			RectProperties headerboxProps, PolylineProperties lineProps,
			SourceCodeProperties slideTextProps, TextProperties slideHeaderProps,
			TextProperties infoBoxTextProps, Locale locale) {
		this.lang = lang;
		this.lang.setStepMode(true);
		circProp = circProps;
		circNormal = (Color)circProp.get(AnimationPropertiesKeys.FILL_PROPERTY);
		this.circHighlight = nodeHighlight;
		this.nextCircHighlight = nodeHighlightNext;
		this.circThirdHighlight = nodeHighlightThird;
		
		circTextProp = nodeTextProps;
		Font f = (Font)circTextProp.get(AnimationPropertiesKeys.FONT_PROPERTY);
		circTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(f.getFamily(), Font.BOLD, 14));
		//circTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
		
		textProp = textProps;
		f = (Font)textProp.get(AnimationPropertiesKeys.FONT_PROPERTY);
		textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(f.getFamily(), Font.BOLD, 14));
		this.textNormal = (Color)textProp.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		this.infoBoxTextProps = infoBoxTextProps;
		f = (Font)this.infoBoxTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		this.infoBoxTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(f.getFamily(), Font.PLAIN, 14));
		
		this.slideTextProps = slideTextProps;
		f = (Font)this.slideTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		this.slideTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(f.getFamily(), Font.PLAIN, (Integer)slideTextProps.get(AnimationPropertiesKeys.SIZE_PROPERTY)));
		
		this.slideHeaderProps = slideHeaderProps;
		f = (Font)this.slideHeaderProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		this.slideHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(f.getFamily(), Font.BOLD, 18));
		
		this.textHighlight = textHighlight;
		this.sourceProp = scProps;
		
		lineProp = lineProps;
		
		this.headerBoxProp = headerboxProps;
		
		this.headerProp = headerProps;
		f = (Font)headerProp.get(AnimationPropertiesKeys.FONT_PROPERTY);
		headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(f.getFamily(), Font.BOLD, 24));
		
		this.lineCount = 0;
		this.rootCoords = new Coordinates(this.minXCoordinate, 200);
		
		this.deleteQuestions = new QuestionGroupModel("deleteQuestions", 4);
		lang.addQuestionGroup(deleteQuestions);
		
		this.translator = new Translator(ClassName.getPackageAsPath(this) + "translatorFiles/BinaryTree", locale);
		
		this.mySlideStyle = new BinaryTreeStyle(this.slideHeaderProps, this.slideTextProps);
	}
	
	public void runNegDel(int[] keys){
		accessesRead = 0;
		int accessesReadOverall = 0;
		accessesWrite = 0;
		int accessesWriteOverall = 0;
		numNodes = 0;
		int numInserts = 0, numDeletes = 0, prevDepth = 0, prevNumNodes = 0;
		float accInsertRelHeight = 0, accDelRelHeight = 0, accInsertRelNodes = 0, accDelRelNodes = 0, meanHeightNodes = 0;
		Text header = new Text(new AnimalTextGenerator(lang), new Coordinates(20, 30), translator.translateMessage("title"), "header", null, headerProp);
		Rect headerBox = new Rect(new AnimalRectGenerator(lang), new Offset(-5, -5, header, AnimalScript.DIRECTION_NW), new Offset(20, 5, header, AnimalScript.DIRECTION_SE), "headerbox", null, headerBoxProp);
		Slide slide = new Slide(lang, ClassName.getPackageAsPath(this) + translator.translateMessage("pathDescriptionSlide"), "headerbox", mySlideStyle);
		slide.hide();
		
		
		SourceCode sc = new SourceCode(new AnimalSourceCodeGenerator(lang), new Coordinates(10, 200), "insertCode", null, sourceProp);
		sc.addMultilineCode(insertSource, null, null);
		sc.hide();
		
		SourceCode delCallCode = new SourceCode(new AnimalSourceCodeGenerator(lang), new Coordinates(10, 200), "deleteCallCode", null, sourceProp);
		delCallCode.addMultilineCode(delCallSource, null, null);
		delCallCode.hide();
		
		SourceCode deleteCode = new SourceCode(new AnimalSourceCodeGenerator(lang), new Coordinates(10, 200), "deleteCode", null, sourceProp);
		deleteCode.addMultilineCode(delSource, null, null);
		deleteCode.hide();
		
		SourceCode deleteNodeCode = new SourceCode(new AnimalSourceCodeGenerator(lang), new Coordinates(10, 200), "deleteNodeCode", null, sourceProp);
		deleteNodeCode.addMultilineCode(deleteNodeSource, null, null);
		deleteNodeCode.hide();
		
		SourceCode searchRightmost = new SourceCode(new AnimalSourceCodeGenerator(lang), new Coordinates(10, 200), "searchRightmost", null, sourceProp);
		searchRightmost.addMultilineCode(findRightmostSource, null, null);
		searchRightmost.hide();
		lang.nextStep();
		
		
		Text currentKey = new Text(new AnimalTextGenerator(lang), new Coordinates(10, 150), translator.translateMessage("toInsert"), "currentKey", null, textProp);
		Text keyNumber = new Text(new AnimalTextGenerator(lang), new Offset(4, 0, currentKey, AnimalScript.DIRECTION_NE), "", "keyNr", null, textProp);
		Text accWrite = lang.newText(new Offset(0, -50, currentKey, AnimalScript.DIRECTION_NW), translator.translateMessage("writeAcc"), "accWrite", null, textProp);
		Text accRead = lang.newText(new Offset(0, -20, accWrite, AnimalScript.DIRECTION_NW), translator.translateMessage("readAcc"), "accRead", null, textProp);
		Text accReadVal = lang.newText(new Offset(4, 0, accRead, AnimalScript.DIRECTION_NE), Integer.toString(this.accessesRead), "accReadVal", null, textProp);
		Text accWriteVal = lang.newText(new Offset(4, 0, accWrite, AnimalScript.DIRECTION_NE), Integer.toString(this.accessesWrite), "accWriteVal", null, textProp);
		
		Circle circ = lang.newCircle(new Offset(200, 0, "accReadVal", AnimalScript.DIRECTION_NE), 6, "legendHighlight", null, this.circProp);
		circ.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, this.circHighlight, null, null);
		circ = lang.newCircle(new Offset(0, 25, "legendHighlight", AnimalScript.DIRECTION_C), 6, "legendHighlightNext", null, this.circProp);
		circ.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, this.nextCircHighlight, null, null);
		circ = lang.newCircle(new Offset(0, 25, "legendHighlightNext", AnimalScript.DIRECTION_C), 6, "legendHighlightThird", null, this.circProp);
		circ.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, this.circThirdHighlight, null, null);
		lang.newText(new Offset(20, -10, "legendHighlight", AnimalScript.DIRECTION_C), translator.translateMessage("legend1"), "legendHighlightDesc", null, textProp);
		lang.newText(new Offset(20, -10, "legendHighlightNext", AnimalScript.DIRECTION_C), translator.translateMessage("legend2"), "legendHighlightNextDesc", null, textProp);
		lang.newText(new Offset(20, -10, "legendHighlightThird", AnimalScript.DIRECTION_C), translator.translateMessage("legend3"), "legendHighlightThirdDesc", null, textProp);
		
		SourceCode[] codeSupportArray = new SourceCode[4];
		codeSupportArray[0] = delCallCode;
		codeSupportArray[1] = deleteCode;
		codeSupportArray[2] = deleteNodeCode;
		codeSupportArray[3] = searchRightmost;
		
		MyInfoBox statistical = new MyInfoBox(lang, new Coordinates(10, 200), 8, "", infoBoxTextProps);
		statistical.hide();
		ArrayList<String> statisticalText = new ArrayList<String>(8);
		MyInfoBox box = new MyInfoBox(lang, new Coordinates(600, 50), 10, "", infoBoxTextProps);
		MultipleChoiceQuestionModel question;
		Random rand = new Random();
		for (int key:keys){
			accReadVal.setText(Integer.toString(this.accessesRead), null, null);
			accWriteVal.setText(Integer.toString(this.accessesWrite), null, null);
			if(this.numNodes == 0)
				prevDepth = -1;
			else
				prevDepth = this.maxDepth;
			prevNumNodes = this.numNodes;
			if(prevNumNodes != 0 && (prevDepth+1) != 0)
				meanHeightNodes += (float)(prevDepth+1)/(float)prevNumNodes;
			if(key >= 0){
				currentKey.setText(translator.translateMessage("toInsert"), null, null);
				keyNumber.setText(Integer.toString(key), null, null);
				lang.nextStep(translator.translateMessage("insert", Integer.toString(key)));
				this.insert(key, sc, keyNumber, accReadVal, accWriteVal);
				numInserts++;
				if(prevDepth > 0)
					accInsertRelHeight+=(float)(accessesRead + accessesWrite)/(float)prevDepth;
				else
					accInsertRelHeight+=(float)(accessesRead + accessesWrite);
				accInsertRelNodes+=(float)(accessesRead + accessesWrite)/(float)this.numNodes;
				lang.nextStep();
			}
			else{
				int toDelete = -key;
				currentKey.setText(translator.translateMessage("toDelete"), null, null);
				keyNumber.setText(Integer.toString(toDelete), null, null);
				lang.nextStep(translator.translateMessage("delete", Integer.toString(toDelete)));
				Knoten node = this.getNode(toDelete, root);
				if(node != null && this.numNodes >= 4){
					question = new MultipleChoiceQuestionModel("deleteQuestion" + rand.nextInt());
					question.setGroupID("deleteQuestions");
					question.setPrompt(translator.translateMessage("question", Integer.toString(toDelete)));
					question.setNumberOfTries(1);
					int correctAnswer;
					String wrongAnswer;
					int neededAnswers;
					if(node.getLeft() == null && node.getRight() == null){
						wrongAnswer = "wrongAnswerLeaf";
						correctAnswer = toDelete;
						question.addAnswer(
								translator.translateMessage(
										"answerNotReplaced",
										Integer.toString(toDelete)), 5,
								translator.translateMessage(
										"correctAnswerLeaf",
										Integer.toString(toDelete)));
						neededAnswers = 3;
					}
					else{ 
						if(node.getLeft() == null){
							correctAnswer = node.getRight().getKey();
						}
						else if(node.getRight() == null){
							correctAnswer = node.getLeft().getKey();
						}
						else
							correctAnswer = this.findRightmostKey(node.getLeft());
						wrongAnswer = "wrongAnswerOther";
						question.addAnswer(Integer.toString(correctAnswer), 5,
								translator.translateMessage(
										"correctAnswerOther",
										Integer.toString(toDelete),
										Integer.toString(correctAnswer)));
						question.addAnswer(
								translator.translateMessage(
										"answerNotReplaced", Integer.toString(toDelete)), 0,
								translator.translateMessage(wrongAnswer,
										Integer.toString(correctAnswer)));
						neededAnswers = 2;
					}
					List<Integer> answers = this.getRandomNodes(neededAnswers+1, toDelete);
					if(answers != null){
						int answerCount = 0;
						for(int answer:answers){
							if(answer != correctAnswer && answerCount < neededAnswers){
								question.addAnswer(Integer.toString(answer), 0, 
										translator.translateMessage(wrongAnswer, Integer.toString(correctAnswer)));
								answerCount++;
							}
						}
						lang.addMCQuestion(question);
					}
				}
				this.delete(toDelete, codeSupportArray, keyNumber, box, accReadVal, accWriteVal);
				if(prevDepth > 0)
					accDelRelHeight+=(float)(accessesRead + accessesWrite)/(float)prevDepth;
				else
					accDelRelHeight+=(float)(accessesRead + accessesWrite);
				accDelRelNodes+=(float)(accessesRead + accessesWrite)/(float)this.numNodes;
				numDeletes++;
				lang.nextStep();
			}
			accessesReadOverall += accessesRead;
			accessesWriteOverall += accessesWrite;
			statisticalText.clear();
			statisticalText.add(translator.translateMessage("readAccLast", Integer.toString(accessesRead)));
			statisticalText.add(translator.translateMessage("writeAccLast", Integer.toString(accessesWrite)));
			statisticalText.add(translator.translateMessage("readAccSum", Integer.toString(accessesReadOverall)));
			statisticalText.add(translator.translateMessage("writeAccSum", Integer.toString(accessesWriteOverall)));
			statisticalText.add(translator.translateMessage("depth", Integer.toString(prevDepth+1)));
			statisticalText.add(translator.translateMessage("numNodes", Integer.toString(prevNumNodes)));
			statisticalText.add(translator.translateMessage("accRelHeight", Float.toString(((float)(accessesRead+accessesWrite))/(float)(prevDepth+1))));
			statisticalText.add(translator.translateMessage("accRelNodes", Float.toString(((float)(accessesRead+accessesWrite))/(float)prevNumNodes)));
			statistical.setText(statisticalText);
			statistical.show();
			accessesWrite = 0;
			accessesRead = 0;
			lang.nextStep();
			statistical.hide();
		}
		lang.hideAllPrimitives();
		header.show();
		headerBox.show();
		if(numInserts > 0){
			accInsertRelHeight=accInsertRelHeight/(float)numInserts;
			accInsertRelNodes=accInsertRelNodes/(float)numInserts;
		}
		if(numDeletes > 0){
			accDelRelHeight=accDelRelHeight/(float)numDeletes;
			accDelRelNodes=accDelRelNodes/(float)numDeletes;
		}
		meanHeightNodes = meanHeightNodes/(float)(numInserts+numDeletes);
		new Slide(lang, ClassName.getPackageAsPath(this) + translator.translateMessage("pathConclusion"),
				"headerbox", mySlideStyle,
				Integer.toString(accessesReadOverall),
				Integer.toString(accessesWriteOverall),
				Integer.toString(numInserts), Integer.toString(numDeletes),
				Float.toString(accInsertRelHeight),
				Float.toString(accInsertRelNodes),
				Float.toString(accDelRelHeight),
				Float.toString(accDelRelHeight),
				Float.toString(meanHeightNodes));
	}
	
	/**
	 * Fügt den gegebenen Schlüssel in den Baum ein.
	 * @param key Der einzufügende Schlüssel.
	 * @param codeSupport Das SourceCode-Objekt für Highlighting.
	 * @param currentKey Das Text-Objekt, das den einzufügenden Schlüssel repräsentiert.
	 */
	private void insert(int key, SourceCode codeSupport, Text currentKey, Text readAcc, Text writeAcc){
		codeSupport.show();
		codeSupport.highlight(1);
		lang.nextStep();
		codeSupport.unhighlight(1);
		if(this.root == null){
			codeSupport.highlight(2);
			lang.nextStep();
			this.root = this.createNode(rootCoords, key);
			this.accessesWrite++;
			writeAcc.setText(Integer.toString(accessesWrite), null, null);
			this.numNodes++;
			codeSupport.unhighlight(2);
		}
		else{
			codeSupport.highlight(4);
			lang.nextStep();
			codeSupport.unhighlight(4);
			codeSupport.highlight(7);
			lang.nextStep();
			codeSupport.unhighlight(7);
			this.insert(key, root, 1, codeSupport, currentKey, readAcc, writeAcc);
		}
		codeSupport.hide();
	}
	
	/**
	 * Fügt den gegebenen Schlüssel in den Baum ein.
	 * @param key Der einzufügende Schlüssel.
	 * @param depth Die aktuelle Ebene im Baum.
	 * @param codeSupport Das SourceCode-Objekt für Highlighting.
	 * @param currentKey Das Text-Objekt, das den einzufügenden Schlüssel repräsentiert.
	 */
	private void insert(int key, Knoten currentNode, int depth, SourceCode codeSupport, Text currentKey, Text readAcc, Text writeAcc){
		codeSupport.highlight(8);
		currentNode.changeColor(circHighlight);
		currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textHighlight, null, null);
		this.accessesRead++;
		readAcc.setText(Integer.toString(accessesRead), null, null);
		lang.nextStep();
		currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
		codeSupport.unhighlight(8);
		this.accessesRead++;
		readAcc.setText(Integer.toString(accessesRead), null, null);
		if (key <= currentNode.getKey()) {
			codeSupport.highlight(9);
			if (currentNode.getLeft() != null){
				currentNode.getLeft().changeColor(circThirdHighlight);
				lang.nextStep();
				codeSupport.unhighlight(9);
				codeSupport.highlight(10);
				lang.nextStep();
				currentNode.changeColor(circNormal);
				codeSupport.unhighlight(10);
				codeSupport.highlight(7);
				lang.nextStep();
				codeSupport.unhighlight(7);
				insert(key, currentNode.getLeft(), depth+1, codeSupport, currentKey, readAcc, writeAcc);
			}
			else{
				lang.nextStep();
				codeSupport.unhighlight(9);
				codeSupport.highlight(12);
				lang.nextStep();
				this.accessesWrite++;
				writeAcc.setText(Integer.toString(accessesWrite), null, null);
				Knoten newNode = createNode(currentNode, key, false, depth);
				currentNode.setLeft(newNode, this.connect(currentNode, newNode));
				this.numNodes++;
				currentNode.changeColor(circNormal);
				codeSupport.unhighlight(12);
			}
				
		} else {
			codeSupport.highlight(14);
			if (currentNode.getRight() != null){
				currentNode.getRight().changeColor(circThirdHighlight);
				lang.nextStep();
				codeSupport.unhighlight(14);
				codeSupport.highlight(15);
				lang.nextStep();
				currentNode.changeColor(circNormal);
				codeSupport.unhighlight(15);
				codeSupport.highlight(7);
				lang.nextStep();
				codeSupport.unhighlight(7);
				insert(key, currentNode.getRight(), depth+1, codeSupport, currentKey, readAcc, writeAcc);
			}
			else{
				lang.nextStep();
				codeSupport.unhighlight(14);
				codeSupport.highlight(17);
				lang.nextStep();
				this.accessesWrite++;
				writeAcc.setText(Integer.toString(accessesWrite), null, null);
				Knoten newNode = createNode(currentNode, key, true, depth);
				currentNode.setRight(newNode, this.connect(currentNode, newNode));
				this.numNodes++;
				currentNode.changeColor(circNormal);
				codeSupport.unhighlight(17);
			}
		}
	}
	
	/**
	 * Löscht den ersten auftretenden Knoten mit dem angegebenen Schlüssel aus dem Baum.
	 * @param key Der Schlüssel des zu löschenden Knotens.
	 * @param codeSupportArray Ein Array, das die SourceCode-Objekte für Highlighting beinhaltet.
	 * @param currentKey Das Text-Objekt, das den zu löschenden Schlüssel repräsentiert.
	 * @param box Eine Infobox für weitere Erläuterungen.
	 */
	private void delete(int key, SourceCode[] codeSupportArray, Text currentKey, MyInfoBox box, Text readAcc, Text writeAcc){
		//Sonderbehandlung für root-Knoten notwendig
		ArrayList<String> boxText = new ArrayList<String>();
		box.hide();
		SourceCode codeSupport = codeSupportArray[0];
		codeSupport.show();
		codeSupport.highlight(1);
		currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textHighlight, null, null);
		if(this.root != null && key == this.root.getKey()){
			this.accessesRead++;
			readAcc.setText(Integer.toString(accessesRead), null, null);
			root.changeColor(circHighlight);
			lang.nextStep();
			codeSupport.unhighlight(1);
			currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
			Knoten left = root.getLeft();
			Knoten right = root.getRight();
			codeSupport.highlight(4);
			this.accessesRead+=2;
			readAcc.setText(Integer.toString(accessesRead), null, null);
			lang.nextStep();
			if(left == null && right == null){
				codeSupport.unhighlight(4);
				codeSupport.highlight(5);
				boxText.add(translator.translateMessage("rootNoChildren"));
				boxText.add(translator.translateMessage("rootNoChildrenCont"));
				box.setText(boxText);
				box.show();
				this.accessesWrite++;
				writeAcc.setText(Integer.toString(accessesWrite), null, null);
				this.numNodes--;
				lang.nextStep();
				box.hide();
				codeSupport.unhighlight(5);
				this.root.hide();
				this.root = null;
			}
			else{
				codeSupport.toggleHighlight(4, 8);
				if (left == null) {
					lang.nextStep();
					codeSupport.toggleHighlight(8, 9);
					boxText.add(translator.translateMessage("rootLeftEmpty"));
					boxText.add(translator.translateMessage("rootLeftEmptyCont"));
					box.setText(boxText);
					box.show();
					this.root.getRightLine().hide(new MsTiming(200));
					this.root.hide(new MsTiming(200));
					this.root = right;
					this.numNodes--;
					this.accessesWrite++;
					writeAcc.setText(Integer.toString(accessesWrite), null, null);
					lang.nextStep();
					box.hide();
					codeSupport.unhighlight(9);
					int maxDepth = this.searchMaxDepth();
					if(maxDepth < this.maxDepth)
						this.maxDepth = maxDepth;
					this.rearrangeTree();
					//this.rearrangeLines();
					lang.nextStep();
				} else if (right == null) {
					left.changeColor(nextCircHighlight);
					lang.nextStep();
					codeSupport.toggleHighlight(8, 10);
					left.changeColor(circNormal);
					lang.nextStep();
					codeSupport.toggleHighlight(10, 11);
					boxText.add(translator.translateMessage("rootRightEmpty"));
					boxText.add(translator.translateMessage("rootRightEmptyCont"));
					box.setText(boxText);
					box.show();
					this.root.getLeftLine().hide(new MsTiming(200));
					this.root.hide(new MsTiming(200));
					this.root = left;
					this.numNodes--;
					this.accessesWrite++;
					writeAcc.setText(Integer.toString(accessesWrite), null, null);
					lang.nextStep();
					box.hide();
					codeSupport.unhighlight(11);
					int maxDepth = this.searchMaxDepth();
					if(maxDepth < this.maxDepth)
						this.maxDepth = maxDepth;
					this.rearrangeTree();
					//this.rearrangeLines();
					lang.nextStep();
				} else {
					left.changeColor(nextCircHighlight);
					lang.nextStep();
					codeSupport.toggleHighlight(8, 10);
					left.changeColor(circNormal);
					right.changeColor(nextCircHighlight);
					lang.nextStep();
					codeSupport.toggleHighlight(10, 13);
					right.changeColor(circNormal);
					boxText.add(translator.translateMessage("deleteBothChildren"));
					boxText.add(translator.translateMessage("rootBothChildrenCont"));
					box.setText(boxText);
					box.show();
					lang.nextStep();
					box.hide();
					codeSupport.unhighlight(13);
					codeSupport.hide();
					Knoten rightmostParent = this.searchRightmostParent(root,
							left, codeSupportArray[3], readAcc, writeAcc);
					codeSupport.show();
					codeSupport.highlight(14);
					root.changeColor(circHighlight);
					boxText.clear();
					lang.nextStep();
					if(rightmostParent.equals(root)){
						boxText.add(translator.translateMessage("rootLeftRightmost"));
						boxText.add(translator.translateMessage("rootLeftRightmostCont"));
						box.setText(boxText);
						box.show();
						codeSupport.toggleHighlight(14, 15);
						codeSupport.highlight(16);
						root.getLeftLine().hide(new MsTiming(200));
						left.setRight(right, root.getRightLine());
						root.hide(new MsTiming(200));
						root = left;
						this.numNodes--;
						this.accessesWrite+=2;
						writeAcc.setText(Integer.toString(accessesWrite), null, null);
						int maxDepth = this.searchMaxDepth();
						if(maxDepth < this.maxDepth)
							this.maxDepth = maxDepth;
						this.rearrangeTree();
						//this.rearrangeLines();
						lang.nextStep();
					}
					else{
						codeSupport.toggleHighlight(14, 19);
						Knoten rightmostChild = rightmostParent.getRight();
						this.accessesRead++;
						readAcc.setText(Integer.toString(accessesRead), null, null);
						rightmostChild.changeColor(circThirdHighlight);
						lang.nextStep();
						codeSupport.toggleHighlight(19, 20);
						this.accessesRead++;
						readAcc.setText(Integer.toString(accessesRead), null, null);
						if (rightmostChild.getLeft() == null) { //rightmostChild.getRight existiert nicht
							lang.nextStep();
							boxText.add(translator.translateMessage("searchRightmostLeaf"));
							box.setText(boxText);
							box.show();
							codeSupport.toggleHighlight(20, 21);
							rightmostParent.getRightLine().hide();
							rightmostParent.setRight(null, null);
							this.accessesWrite++;
							writeAcc.setText(Integer.toString(accessesWrite), null, null);
						} else {
							lang.nextStep();
							boxText.add(translator.translateMessage("searchRightmostNoLeaf"));
							boxText.add(translator.translateMessage("searchRightmostNoLeafCont"));
							box.setText(boxText);
							box.show();
							codeSupport.toggleHighlight(20, 23);
							rightmostParent.setRight(rightmostChild.getLeft(),
									rightmostParent.getRightLine());
							rightmostChild.getLeftLine().hide();
							this.accessesWrite++;
							writeAcc.setText(Integer.toString(accessesWrite), null, null);
						}
						codeSupport.highlight(25);
						codeSupport.highlight(26);
						codeSupport.highlight(27);
						rightmostChild.setLeft(root.getLeft(),
								root.getLeftLine());
						rightmostChild.setRight(root.getRight(),
								root.getRightLine());
						this.root.hide();
						this.root = rightmostChild;
						this.numNodes--;
						int maxDepth = this.searchMaxDepth();
						if(maxDepth < this.maxDepth)
							this.maxDepth = maxDepth;
						this.rearrangeTree();
						this.accessesWrite+=3;
						writeAcc.setText(Integer.toString(accessesWrite), null, null);
						//this.rearrangeLines();
						lang.nextStep();
						codeSupport.unhighlight(21);
						codeSupport.unhighlight(23);
						codeSupport.unhighlight(25);
						codeSupport.unhighlight(26);
						codeSupport.unhighlight(27);
						rightmostChild.changeColor(circNormal);
						rightmostParent.changeColor(circNormal);
						box.hide();
					}
					
				}
			}
			codeSupport.highlight(31);
			lang.nextStep();
			codeSupport.unhighlight(31);
		}
		else if(this.root != null){
			root.changeColor(circHighlight);
			lang.nextStep();
			codeSupport.toggleHighlight(1, 33);
			currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
			lang.nextStep();
			codeSupport.toggleHighlight(33, 34);
			lang.nextStep();
			codeSupport.unhighlight(34);
			codeSupport.hide();
			codeSupportArray[1].show();
			this.delete(key, root, codeSupportArray, currentKey, box, readAcc, writeAcc);
			codeSupport.show();
		}
		else{
			codeSupport.highlight(35);
			lang.nextStep();
			codeSupport.unhighlight(35);
		}
		codeSupport.hide();
	}
	
	private boolean delete(int key, Knoten currentNode, SourceCode codeSupportArray[], Text currentKey, MyInfoBox box, Text readAcc, Text writeAcc){
		SourceCode codeSupport = codeSupportArray[1];
		codeSupport.highlight(0);
		lang.nextStep();
		codeSupport.toggleHighlight(0, 3);
		ArrayList<String> boxText = new ArrayList<String>();
		boxText.add(translator.translateMessage("deleteCheck"));
		box.setText(boxText);
		box.show();
		Knoten left = currentNode.getLeft();
		Knoten right = currentNode.getRight();
		this.accessesRead+=2;
		readAcc.setText(Integer.toString(accessesRead), null, null);
		if(left != null){
			left.changeColor(circHighlight);
			currentNode.changeColor(nextCircHighlight);
			currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textHighlight, null, null);
			this.accessesRead++;
			readAcc.setText(Integer.toString(accessesRead), null, null);
			lang.nextStep();
			if(left.getKey() == key){
				boxText.add(translator.translateMessage("deleteNode"));
				box.setText(boxText);
				codeSupport.toggleHighlight(3, 4);
				lang.nextStep();
				codeSupport.unhighlight(4);
				codeSupport.hide();
				box.hide();
				codeSupportArray[2].show();
				currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
				return this.deleteNode(currentNode, left, false, box, codeSupportArray, readAcc, writeAcc);
			}
			left.changeColor(circNormal);
			currentNode.changeColor(circHighlight);
			currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
		}
		else
			lang.nextStep();
		codeSupport.toggleHighlight(3, 6);
		if(right != null){
			right.changeColor(circHighlight);
			currentNode.changeColor(nextCircHighlight);
			currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textHighlight, null, null);
			this.accessesRead++;
			readAcc.setText(Integer.toString(accessesRead), null, null);
			lang.nextStep();
			if(right.getKey() == key){
				boxText.add(translator.translateMessage("deleteNode"));
				box.setText(boxText);
				codeSupport.toggleHighlight(6, 7);
				lang.nextStep();
				codeSupport.unhighlight(7);
				codeSupport.hide();
				box.hide();
				codeSupportArray[2].show();
				currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
				return this.deleteNode(currentNode, right, true, box, codeSupportArray, readAcc, writeAcc);
			}
			right.changeColor(circNormal);
			currentNode.changeColor(circHighlight);
			currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
		}
		else
			lang.nextStep();
		codeSupport.toggleHighlight(6, 9);
		boxText.add(translator.translateMessage("deleteContinue"));
		box.setText(boxText);
		currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textHighlight, null, null);
		this.accessesRead++;
		readAcc.setText(Integer.toString(accessesRead), null, null);
		lang.nextStep();
		if(left != null && key < currentNode.getKey()){
			currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
			codeSupport.toggleHighlight(9, 10);
			currentNode.changeColor(circNormal);
			left.changeColor(circHighlight);
			lang.nextStep();
			codeSupport.unhighlight(10);
			box.hide();
			return delete(key, left, codeSupportArray, currentKey, box, readAcc, writeAcc);
		}
		codeSupport.toggleHighlight(9, 11);
		if(right != null && key > currentNode.getKey()){
			currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
			codeSupport.toggleHighlight(11, 12);
			currentNode.changeColor(circNormal);
			right.changeColor(circHighlight);
			lang.nextStep();
			codeSupport.unhighlight(12);
			box.hide();
			return delete(key, right, codeSupportArray, currentKey, box, readAcc, writeAcc);
		}
		lang.nextStep();
		//Gesuchter Key nicht enthalten
		boxText.add(translator.translateMessage("deleteNoNode"));
		box.setText(boxText);
		currentKey.changeColor(AnimalScript.COLORCHANGE_COLOR, textNormal, null, null);
		currentNode.changeColor(circNormal);
		codeSupport.toggleHighlight(11, 14);
		lang.nextStep();
		codeSupport.hide();
		box.hide();
		return false;
	}
	
	private boolean deleteNode(Knoten parent, Knoten toDelete, boolean leftRight, MyInfoBox box, SourceCode[] codeSupportArray, Text readAcc, Text writeAcc){
		SourceCode codeSupport = codeSupportArray[2];
		codeSupport.highlight(0);
		ArrayList<String> boxText = new ArrayList<String>();
		boxText.add("leftRight: " + Boolean.toString(leftRight));
		box.setText(boxText);
		box.show();
		parent.changeColor(nextCircHighlight);
		lang.nextStep();
		codeSupport.toggleHighlight(0, 3);
		Knoten left = toDelete.getLeft();
		Knoten right = toDelete.getRight();
		this.accessesRead+=2;
		readAcc.setText(Integer.toString(accessesRead), null, null);
		if(left == null && right == null){
			lang.nextStep();
			boxText.add("deleteNoChild");
			box.setText(boxText);
			codeSupport.toggleHighlight(3, 4);
			lang.nextStep();
			if(leftRight){
				codeSupport.toggleHighlight(4, 5);
				parent.getRightLine().hide(new MsTiming(200));
				parent.setRight(null, null);
				this.accessesWrite++;
				writeAcc.setText(Integer.toString(accessesWrite), null, null);
			}
			else{
				codeSupport.toggleHighlight(4, 7);
				parent.getLeftLine().hide(new MsTiming(200));
				parent.setLeft(null, null);
				this.accessesWrite++;
				writeAcc.setText(Integer.toString(accessesWrite), null, null);
			}
			toDelete.hide(new MsTiming(200));
			this.numNodes--;
			int maxDepth = this.searchMaxDepth(); 
			if(maxDepth < this.maxDepth){
				this.maxDepth = maxDepth;
				this.rearrangeTree();
			}
			lang.nextStep();
			codeSupport.unhighlight(5);
			codeSupport.unhighlight(7);
		}
		else {
			if(left != null)
				left.changeColor(circThirdHighlight);
			if(right != null)
				right.changeColor(circThirdHighlight);
			lang.nextStep();
			boxText.add(translator.translateMessage("deleteAtLeastOne"));
			box.setText(boxText);
			codeSupport.toggleHighlight(3, 10);
			if (left == null) {
				if(right != null)
					right.changeColor(circNormal);
				lang.nextStep();
				boxText.add(translator.translateMessage("deleteNoLeft"));
				box.setText(boxText);
				toDelete.getRightLine().hide();
				if (leftRight) {
					codeSupport.toggleHighlight(10, 12);
					parent.setRight(right, parent.getRightLine());
				} else {
					codeSupport.toggleHighlight(10, 13);
					parent.setLeft(right, parent.getLeftLine());
				}
				this.accessesWrite++;
				writeAcc.setText(Integer.toString(accessesWrite), null, null);
			} else if (right == null) {
				lang.nextStep();
				left.changeColor(circNormal);
				codeSupport.toggleHighlight(10, 15);
				lang.nextStep();
				boxText.add(translator.translateMessage("deleteNoRight"));
				box.setText(boxText);
				toDelete.getLeftLine().hide();
				if (leftRight) {
					codeSupport.toggleHighlight(15, 17);
					parent.setRight(left, parent.getRightLine());
				} else {
					codeSupport.toggleHighlight(15, 19);
					parent.setLeft(left, parent.getLeftLine());
				}
				this.accessesWrite++;
				writeAcc.setText(Integer.toString(accessesWrite), null, null);
			} else {
				right.changeColor(circNormal);
				lang.nextStep();
				codeSupport.toggleHighlight(10, 15);
				left.changeColor(circNormal);
				right.changeColor(circThirdHighlight);
				lang.nextStep();
				right.changeColor(circNormal);
				codeSupport.toggleHighlight(15, 21);
				codeSupport.highlight(22);
				boxText.add(translator.translateMessage("deleteBothChildren"));
				box.setText(boxText);
				lang.nextStep();
				codeSupport.unhighlight(22);
				codeSupport.unhighlight(21);
				codeSupport.hide();
				box.hide();
				Knoten rightmostParent = this.searchRightmostParent(toDelete,
						toDelete.getLeft(), codeSupportArray[3], readAcc, writeAcc);
				codeSupport.show();
				box.show();
				toDelete.changeColor(circHighlight);
				codeSupport.highlight(23);
				boxText.add(translator.translateMessage("searchRightmostCheckLeft"));
				box.setText(boxText);
				lang.nextStep();
				if (rightmostParent.equals(toDelete)) {
					boxText.add(translator.translateMessage("searchRightmostCheckLeftTrue"));
					box.setText(boxText);
					codeSupport.toggleHighlight(23, 24);
					toDelete.getLeftLine().hide();
					toDelete.getLeft().setRight(toDelete.getRight(),
							toDelete.getRightLine());
					if (leftRight) {
						parent.setRight(toDelete.getLeft(),
								parent.getRightLine());
						codeSupport.highlight(26);
					} else {
						parent.setLeft(toDelete.getLeft(), parent.getLeftLine());
						codeSupport.highlight(28);
					}
					this.accessesWrite+=2;
					writeAcc.setText(Integer.toString(accessesWrite), null, null);
					toDelete.hide(new MsTiming(200));
				} else {
					boxText.add(translator.translateMessage("searchRightmostCheckLeftFalse"));
					box.setText(boxText);
					codeSupport.toggleHighlight(23, 31);
					Knoten rightmostChild = rightmostParent.getRight();
					this.accessesRead++;
					readAcc.setText(Integer.toString(accessesRead), null, null);
					this.accessesRead++;
					readAcc.setText(Integer.toString(accessesRead), null, null);
					if (rightmostChild.getLeft() == null) { // ist ein Blatt
						lang.nextStep();
						boxText.add(translator.translateMessage("searchRightmostLeaf"));
						box.setText(boxText);
						codeSupport.toggleHighlight(31, 32);
						rightmostParent.getRightLine().hide();
						rightmostParent.setRight(null, null);
						this.accessesWrite++;
						writeAcc.setText(Integer.toString(accessesWrite), null, null);
						lang.nextStep();
						codeSupport.unhighlight(32);
					} else {
						rightmostChild.getLeft().changeColor(circThirdHighlight);
						lang.nextStep();
						rightmostChild.getLeft().changeColor(circNormal);
						boxText.add(translator.translateMessage("searchRightmostNoLeaf"));
						boxText.add(translator.translateMessage("searchRightmostNoLeafCont"));
						box.setText(boxText);
						codeSupport.toggleHighlight(31, 34);
						rightmostChild.getLeftLine().hide();
						rightmostParent.setRight(rightmostChild.getLeft(), rightmostParent.getRightLine());
						this.accessesWrite++;
						writeAcc.setText(Integer.toString(accessesWrite), null, null);
					}
					codeSupport.highlight(35);
					codeSupport.highlight(36);
					rightmostChild.setLeft(toDelete.getLeft(),
							toDelete.getLeftLine());
					rightmostChild.setRight(toDelete.getRight(),
							toDelete.getRightLine());
					if (leftRight){
						codeSupport.highlight(38);
						parent.setRight(rightmostChild, parent.getRightLine());
					}
					else{
						codeSupport.highlight(39);
						parent.setLeft(rightmostChild, parent.getLeftLine());
					}
					this.accessesWrite+=3;
					writeAcc.setText(Integer.toString(accessesWrite), null, null);
				}
				rightmostParent.changeColor(circNormal);
			}
			toDelete.hide(new MsTiming(200));
			this.numNodes--;
			int maxDepth = this.searchMaxDepth(); 
			if(maxDepth < this.maxDepth)
				this.maxDepth = maxDepth;
			this.rearrangeTree();
			//this.rearrangeLines();
			lang.nextStep();
			codeSupport.unhighlight(24);
			codeSupport.unhighlight(26);
			codeSupport.unhighlight(28);
			codeSupport.unhighlight(34);
			codeSupport.unhighlight(35);
			codeSupport.unhighlight(36);
			codeSupport.unhighlight(39);
			codeSupport.unhighlight(38);
		}
		codeSupport.unhighlight(17);
		codeSupport.unhighlight(19);
		parent.changeColor(circNormal);
		codeSupport.highlight(44);
		lang.nextStep();
		codeSupport.unhighlight(44);
		box.hide();
		codeSupport.hide();
		return true;
	}
	
	private Knoten createNode(Coordinates coords, int key){
		return new Knoten(lang, coords, key, 20, circProp, circTextProp);
	}
	
	private Knoten createNode(Knoten currentNode, int key, boolean leftRight, int depth){
		if(depth > maxDepth){
			maxDepth = depth;
			this.rearrangeTree();
		}
		Coordinates currentCoords = currentNode.getCoordinates();
		int xFactor = startWidth;
		for(int i=1; i<depth; i++){
			xFactor = xFactor / 2;
		}
		if(!leftRight)
			xFactor = 0-xFactor;
		Coordinates newCoords = new Coordinates(currentCoords.getX()+xFactor, currentCoords.getY()+100);
		return new Knoten(lang, newCoords, key, 20, circProp, circTextProp);
	}
	
	private Polyline connect(Knoten parent, Knoten child){
		String name = "line" + this.lineCount++;
		Coordinates coord = parent.getCoordinates();
		Node[] offsets = new Node[2];
		offsets[0] = new Coordinates(coord.getX(), coord.getY()+20);
		coord = child.getCoordinates();
		offsets[1] = new Coordinates(coord.getX(), coord.getY()-20);
		return new Polyline(new AnimalPolylineGenerator(lang), offsets, name, new MsTiming(500), this.lineProp);
	}
	
	private Knoten searchRightmostParent(Knoten parent, Knoten child, SourceCode codeSupport, Text readAcc, Text writeAcc){
		codeSupport.show();
		parent.changeColor(nextCircHighlight);
		child.changeColor(circHighlight);
		codeSupport.highlight(0);
		lang.nextStep();
		this.accessesRead++;
		readAcc.setText(Integer.toString(accessesRead), null, null);
		codeSupport.toggleHighlight(0,1);
		if(child.getRight() == null){
			lang.nextStep();
			codeSupport.toggleHighlight(1, 2);
			child.changeColor(circNormal);
			lang.nextStep();
			codeSupport.unhighlight(2);
			codeSupport.hide();
			return parent;
		}
		else{
			child.changeColor(circNormal);
			child.getRight().changeColor(circHighlight);
			lang.nextStep();
			codeSupport.toggleHighlight(1, 5);
			child.changeColor(nextCircHighlight);
			parent.changeColor(circNormal);
			lang.nextStep();
			codeSupport.unhighlight(5);
			codeSupport.hide();
			return searchRightmostParent(child, child.getRight(), codeSupport, readAcc, writeAcc);
		}
	}
	
	private void rearrangeTree(){
		this.startWidth = this.minLineWidth *  (int) (Math.floor(Math.pow(2, maxDepth)));
		Coordinates newRootCoords = new Coordinates(this.minXCoordinate+this.getLeftWidth(), rootCoords.getY());
		this.rootCoords = newRootCoords;
		root.move(rootCoords);
		this.rearrangeTree(root, 1, rootCoords);
		this.rearrangeLines();
	}
	
	private void rearrangeTree(Knoten current, int depth, Coordinates currentCoords){
		if(current.getLeft() != null){
			Coordinates newCoords = this.rearrangeChild(current.getLeft(), currentCoords, depth, false);
			rearrangeTree(current.getLeft(), depth+1, newCoords);
		}
		if(current.getRight() != null){
			Coordinates newCoords = this.rearrangeChild(current.getRight(), currentCoords, depth, true);
			rearrangeTree(current.getRight(), depth+1, newCoords);
		}
	}
	
	private Coordinates rearrangeChild(Knoten child, Coordinates parentCoords, int depth, boolean leftRight){
		int xFactor = startWidth;
		for(int i=1; i<depth; i++){
			xFactor = xFactor / 2;
		}
		if(!leftRight)
			xFactor = -xFactor;
		Coordinates newCoords = new Coordinates(parentCoords.getX()+xFactor, parentCoords.getY()+100);
		child.move(newCoords);
		return newCoords;
	}
	
	private void rearrangeLines(){
		if(root != null){
			this.hideAllLines(root);
			this.rearrangeLines(root);
		}
	}
	
	private void rearrangeLines(Knoten current){
		if(current.getLeft() != null){
			Polyline line = this.connect(current, current.getLeft());
			current.setLeft(current.getLeft(), line);
			rearrangeLines(current.getLeft());
		}
		if(current.getRight() != null){
			Polyline line = this.connect(current, current.getRight());
			current.setRight(current.getRight(), line);
			rearrangeLines(current.getRight());
		}
	}
	
	private void hideAllLines(Knoten current){
		if(current != null){
			Polyline line = current.getLeftLine();
			if(line != null){
				line.hide();
				hideAllLines(current.getLeft());
			}
			line = current.getRightLine();
			if(line != null){
				line.hide();
				hideAllLines(current.getRight());
			}
		}
	}	
	
	private int findRightmostKey(Knoten current){
		if(current.getRight() == null)
			return current.getKey();
		else
			return findRightmostKey(current.getRight());
	}
	
	private Knoten getNode(int key, Knoten current){
		if(current == null)
			return null;
		if(current.getKey() == key)
			return current;
		else if(current.getKey() < key)
			return getNode(key, current.getRight());
		else
			return getNode(key, current.getLeft());
	}
	
	private List<Integer> getRandomNodes(int count, int except){
		int key = 0, stopCounter = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<count; i++){
			stopCounter = 0;
			do{
				key = getRandomNode(root);
				stopCounter++;
			}while((list.contains(key) || key == except) && stopCounter < 100);
			if(stopCounter >= 100)
				return null;
			list.add(key);
		}
		return list;
	}
	
	private int getRandomNode(Knoten node){
		Random r = new Random();
		if(r.nextBoolean())
			return node.getKey();
		if(r.nextBoolean() && node.getLeft() != null)
			return getRandomNode(node.getLeft());
		else if(node.getRight() != null)
			return getRandomNode(node.getRight());
		else
			return node.getKey();
	}
	
	/**
	 * Durchläuft rekursiv alle Baumteile und macht sie sichtbar.
	 * @param current Der aktuelle Knoten
	 */
	private void showTree(Knoten current){
		if(current != null){
			current.show();
			if(current.getLeftLine() != null)
				current.getLeftLine().show();
			if(current.getRightLine() != null)
				current.getRightLine().show();
			showTree(current.getLeft());
			showTree(current.getRight());
		}
	}
	
	private int searchMaxDepth(){
		return searchMaxDepth(root, -1);
	}
	
	private int searchMaxDepth(Knoten currentNode, int currentDepth){
		if(currentNode == null)
			return currentDepth;
		int left, right;
		left = this.searchMaxDepth(currentNode.getLeft(), currentDepth+1);
		right = this.searchMaxDepth(currentNode.getRight(), currentDepth+1);
		if(left > right)
			return left;
		else
			return right;
	}
	
	private int getLeftWidth(){
		int currentWidth = this.startWidth;
		int result = 0;
		Knoten current = this.root;
		while(current.getLeft() != null){
			result += currentWidth;
			currentWidth = currentWidth/2;
			current = current.getLeft();
		}
		result += currentWidth;
		return result;
	}
}
