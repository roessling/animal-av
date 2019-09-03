package generators.misc.impl.decomposition;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.misc.impl.Attribute;
import generators.misc.impl.Closure;
import generators.misc.impl.FD;
import generators.misc.impl.Relation;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import translator.Translator;

public class DecompositionAnimalUtil {

	private static Language lang;
	private static SourceCode topLeftSc;
	private static SourceCode bottomLeftSc;
	private static SourceCode bottomRightSc;
	private static SourceCode topRightSc;
	private static SourceCode info;
	private static Text title;
	public static Text closureText;
	private static SourceCodeProperties scProps;
	private static int defaultFontSize = 20;
	private static Polyline line;
	public static int X_OFFSET = 40;
	public static int Y_OFFSET = 60;
	private static List<FD> funcDependencies;
	private static Translator translator;
	private static boolean firstR1 = true;
	private static boolean firstR2;

	private static final String TRANSLATE_HEAD = "translate #2";

	public static void init(Language lang, List<FD> funcDependencies, Translator translator) {
		DecompositionAnimalUtil.lang = lang;
		DecompositionAnimalUtil.funcDependencies = funcDependencies;
		DecompositionAnimalUtil.translator = translator;
		initializeSc();
	}

	public static void showIntro() {

		topLeftSc = lang.newSourceCode(new Coordinates(X_OFFSET, Y_OFFSET), "bcnfIntro", null, scProps);
		// showTitle(translator.translateMessage(I.name));
		// lang.nextStep();
		showTitle(translator.translateMessage(I.algoName));

		String text = translator.translateMessage(I.introTextFirst);

		topLeftSc.addMultilineCode(text, null, null);
		lang.nextStep();
		topLeftSc.hide();

		topLeftSc = lang.newSourceCode(new Coordinates(X_OFFSET, Y_OFFSET), "decompositionIntro", null, scProps);
		showTitle(translator.translateMessage(I.name));

		text = translator.translateMessage(I.introTextSecond);

		topLeftSc.addMultilineCode(text, null, null);
		lang.nextStep();
		topLeftSc.hide();

		topLeftSc = lang.newSourceCode(new Coordinates(X_OFFSET, Y_OFFSET), "stepsIntro", null, scProps);
		showTitle(translator.translateMessage(I.algoName) + ": " + translator.translateMessage(I.mathHeader));

		text = translator.translateMessageWithoutParameterExpansion(I.instructions)
				+ translator.translateMessage(I.symbolTable);

		topLeftSc.addMultilineCode(text, null, null);
		lang.nextStep();
		topLeftSc.hide();
		showTitle(translator.translateMessage(I.algoName));
	}

	public static void showClosureIntro(List<Attribute> attributes) {
		ClosureAnimal.setTranslator(translator);
		DecompositionAnimalUtil.showTitle(translator.translateMessage(I.closureHeader));
		String attributeString = Arrays.deepToString(attributes.toArray());
		attributeString = attributeString.substring(1, attributeString.length() - 1);

		DecompositionAnimalUtil.addClosureQuestion(attributes);

		Offset offset = new Offset(0, Y_OFFSET, topLeftSc, AnimalScript.DIRECTION_SW);

		String beginning = translator.translateMessage(I.closureOf) + " (" + attributeString + ")⁺ = {"
				+ attributeString;
		closureText = lang.newText(offset, beginning, "closure", null);
		closureText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20), null, null);
		DecompositionAnimalUtil.showInfo(translator.translateMessage(I.closureInfo) + "\n{" + attributeString + "}",
				new Offset(0, Y_OFFSET, closureText, AnimalScript.DIRECTION_SW));

		ClosureAnimal.of(attributes, funcDependencies);

		lang.nextStep();
		closureText.hide();
	}

	public static void showTitle(String text) {
		if (title != null) {
			title.hide();
		}

		Node node = new Coordinates(X_OFFSET, 20);
		title = lang.newText(node, text, null, null);
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30), null, null);
	}

	public static void showDependencies(List<FD> fds) {
		topLeftSc = lang.newSourceCode(new Coordinates(X_OFFSET, Y_OFFSET), null, null, scProps);

		topLeftSc.addCodeLine(translator.translateMessage(I.funcDependencies) + ": ", null, 0, null);
		emptyLine(topLeftSc);

		for (FD fd : fds) {
			createFdLine(fd);
		}
	}

	public static void showSteps() {
		Offset offset = new Offset(X_OFFSET * 4, -defaultFontSize, topLeftSc, AnimalScript.DIRECTION_NE);
		topRightSc = lang.newSourceCode(offset, null, null, scProps);
		topRightSc.addMultilineCode(translator.translateMessageWithoutParameterExpansion(I.instructions),
				"instructions", null);
		topRightSc.highlight(0);
	}

	public static void hightlightStep(int i, boolean highlighted) {
		if (highlighted) {
			topRightSc.highlight(i);
		} else {
			topRightSc.unhighlight(i);
		}
	}

	public static void showRelations(List<Relation> relations) {
		lang.nextStep();
		Offset offset = new Offset(0, Y_OFFSET, topLeftSc, AnimalScript.DIRECTION_SW);
		bottomLeftSc = lang.newSourceCode(offset, null, null, scProps);
		bottomLeftSc.addCodeLine(translator.translateMessage(I.currentRelations) + ": ", null, 0, null);

		for (Relation relation : relations) {
			bottomLeftSc.addCodeLine(createRelationLine(relation), relation.getName(), 0, null);
		}
	}

	public static void showFinal(List<Relation> relations) {
		showTitle(translator.translateMessage(I.finalTitle));

		Offset offset = new Offset(0, Y_OFFSET, title, AnimalScript.DIRECTION_SW);
		bottomLeftSc = lang.newSourceCode(offset, null, null, scProps);

		for (Relation relation : relations) {
			bottomLeftSc.addCodeLine(createRelationLine(relation), relation.getName(), 0, null);
		}
		DecompositionAnimalUtil.showInfo(translator.translateMessage(I.finalRelationsColon));
	}

	public static void showDecomposedRelations(Relation relation) {
		Offset offset = new Offset(X_OFFSET, -defaultFontSize, bottomLeftSc, AnimalScript.DIRECTION_NE);
		bottomRightSc = lang.newSourceCode(offset, null, null, scProps);
		bottomRightSc.addCodeLine(translator.translateMessage(I.decomposedRelation) + ": ", null, 0, null);
		lang.nextStep();
		bottomRightSc.addCodeLine(createRelationLine(relation), "rightSc" + relation.getName(), 0, null);
	}

	public static void addToRightRelations(Relation relation) {
		bottomRightSc.addCodeLine(createRelationLine(relation), "rightSc" + relation.getName(), 0, null);
	}

	public static void hideRightRelations() {
		lang.nextStep();
		bottomRightSc.hide();
	}

	public static void hideMiddleRelations() {
		lang.nextStep();
		bottomLeftSc.hide();
	}

	private static String createRelationLine(Relation relation) {
		StringBuilder sb = new StringBuilder();
		sb.append(relation.getName()).append(" ");
		sb.append(translator.translateMessage(I.wordKey) + ": ")
				.append(Arrays.deepToString(relation.getPrimaryKey().toArray())).append(" ");
		sb.append(translator.translateMessage(I.attributes) + ": ")
				.append(Arrays.deepToString(relation.getAttributes().toArray()));

		return sb.toString();
	}

	public static void createFdLine(FD fd) {
		addAttributeElements(fd, fd.getKeys());
		topLeftSc.addCodeElement(" -> ", null, 0, null);
		addAttributeElements(fd, fd.getValues());

		emptyLine(topLeftSc);
	}

	public static void showInfo(String message) {
		Node node = new Offset(0, Y_OFFSET, bottomLeftSc, AnimalScript.DIRECTION_SW);
		showInfo(message, node);
	}

	// Shows an info below the FDs and does one nextStep
	public static void showInfo(String message, Node node) {
		info = lang.newSourceCode(node, null, null, scProps);
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.ITALIC, defaultFontSize));
		scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		for (String string : message.split("\n")) {
			info.addCodeLine(string, null, 0, null);
		}
		lang.nextStep();
		info.hide();
	}

	private static void addAttributeElements(FD fd, List<Attribute> atts) {
		for (Iterator<Attribute> i = atts.iterator(); i.hasNext();) {
			Attribute next = i.next();
			String label = fd.getId() + next.getSymbol();
			topLeftSc.addCodeElement(next.getSymbol(), label, 0, null);

			if (i.hasNext())
				topLeftSc.addCodeElement(",", null, 0, null);
		}
	}

	private static void initializeSc() {
		scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, defaultFontSize));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}

	public static void drawLine() {
		PolylineProperties lineProp = new PolylineProperties();
		lineProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		// Move type “translate #2”  move only node #2 of the object(s)
		Offset first = new Offset(-X_OFFSET / 2, defaultFontSize / 2 + 5, bottomRightSc, AnimalScript.DIRECTION_NW);
		Offset second = new Offset(X_OFFSET * 7, defaultFontSize / 2 + 5, bottomLeftSc, AnimalScript.DIRECTION_NW);
		line = lang.newPolyline(new Node[] { first, first }, "line", null, lineProp);
		line.moveTo(AnimalScript.DIRECTION_NE, TRANSLATE_HEAD, second, null, new MsTiming(750));
	}

	public static void hideLine() {
		line.hide();
	}

	public static void emptyLine(SourceCode sc) {
		sc.addCodeLine(" ", null, 0, null);
	}

	public static void highlight(Relation relation, boolean highLighted) {
		if (highLighted)
			bottomLeftSc.highlight(relation.getName());
		else
			bottomLeftSc.unhighlight(relation.getName());
	}

	public static void highLight(String label, boolean highLighted) {
		if (highLighted)
			topLeftSc.highlight(label);
		else
			topLeftSc.unhighlight(label);
	}

	public static void highLight(FD fd, boolean highLight) {
		for (Attribute a : fd.getKeys()) {
			if (highLight)
				topLeftSc.highlight(fd.getId() + a.getSymbol());
			else
				topLeftSc.unhighlight(fd.getId() + a.getSymbol());
		}

		for (Attribute a : fd.getValues()) {
			if (highLight)
				topLeftSc.highlight(fd.getId() + a.getSymbol());
			else
				topLeftSc.unhighlight(fd.getId() + a.getSymbol());
		}
	}

	public static void addBcnfQuestion() {
		MultipleSelectionQuestionModel question = new MultipleSelectionQuestionModel("bcnfQ");
		question.setPrompt(translator.translateMessage(I.bcnfQPrompt));
		question.addAnswer(translator.translateMessage(I.bcnfAnswer1), 1, translator.translateMessage(I.bcnfFeedback1));
		question.addAnswer(translator.translateMessage(I.bcnfAnswer2), -1, "");
		question.addAnswer(translator.translateMessage(I.bcnfAnswer3), 1, translator.translateMessage(I.bcnfFeedback3));
		question.addAnswer(translator.translateMessage(I.bcnfAnswer4), -1, "");

		lang.addMSQuestion(question);
	}

	public static void addR1CheckQuestion(List<Attribute> atts, Relation r) {
		if (firstR1) {

			FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("R1Q");
			question.setPrompt(translator.translateMessage(String.format(I.r1QPrompt, r.getName(), r.getName())));

			ArrayList<String> copy = new ArrayList<>();
			atts.stream().forEach(a -> copy.add(a.getSymbol()));
			Collections.sort(copy);
			String answerText = "";

			for (String s : copy) {
				answerText += s.trim() + ",";
			}
			answerText = answerText.substring(0, answerText.length() - 1);

			question.addAnswer(answerText, 2, translator.translateMessage(I.rightAnswer));

			lang.addFIBQuestion(question);
			firstR1 = false;
		}
	}

	public static void addR2CheckQuestion(List<Attribute> atts, Relation r) {
		if (firstR2) {

			FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("R2Q");
			question.setPrompt(translator.translateMessage(String.format(I.r2QPrompt, r.getName(), r.getName())));

			ArrayList<String> copy = new ArrayList<>();
			atts.stream().forEach(a -> copy.add(a.getSymbol()));
			Collections.sort(copy);
			String answerText = "";

			for (String s : copy) {
				answerText += s.trim() + ",";
			}
			answerText = answerText.substring(0, answerText.length() - 1);

			question.addAnswer(answerText, 2, translator.translateMessage(I.rightAnswer));

			lang.addFIBQuestion(question);
			firstR2 = false;
		}
	}

	public static void addClosureQuestion(List<Attribute> atts) {
		MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("closureQ");

		String attString = attributesToSortedString(atts);
		question.setPrompt(translator.translateMessage(I.closureQPrompt) + " (" + attString + ")⁺ ?");

		List<Attribute> closure = Closure.of(atts, funcDependencies);

		question.addAnswer(attributesToSortedString(closure), 2, translator.translateMessage(I.rightAnswer));

		// Wrong answer #1
		closure.remove(new Random().nextInt(closure.size()));
		question.addAnswer(attributesToSortedString(closure), -2, translator.translateMessage(I.wrongAnswer));

		// Wrong answer #2
		int i = 0;
		for (Attribute a : Decomposition.globalAttributes) {
			if (!closure.contains(a) && i++ < 3)
				closure.add(a);
		}
		question.addAnswer(attributesToSortedString(closure), -2, translator.translateMessage(I.wrongAnswer));

		lang.addMCQuestion(question);
	}

	private static String attributesToSortedString(List<Attribute> attributes) {
		ArrayList<String> copy = new ArrayList<>();
		attributes.stream().forEach(a -> copy.add(a.getSymbol()));
		Collections.sort(copy);

		StringBuilder sb = new StringBuilder();

		for (String s : copy) {
			sb.append(s.trim()).append(",");
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();
	}

}
