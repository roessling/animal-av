package generators.misc.impl.synthese;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
import generators.misc.impl.decomposition.I;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

public class SyntheseAnimalUtil {
	private static Translator translator;

	private static Language lang;
	private static SourceCode rightSc;
	private static SourceCode leftSc;
	private static SourceCode relationSc;
	private static SourceCode stepsSc;
	private static SourceCode info;
	private static SourceCodeProperties scProps;
	private static Text title;
	private static Text closure;
	private static int stepIndex = 0;
	private static List<Polyline> lines = new ArrayList<>();
	public static int X_OFFSET = 30;
	public static int Y_OFFSET = 100;
	public static int X_SIDE_OFFSET = 25 * X_OFFSET;
	public static final String TRANSLATE = "translate";
	public static final String TRANSLATE_HEAD = "translate #2";

	public static void init(Language lang, Translator translator) {
		SyntheseAnimalUtil.lang = lang;
		SyntheseAnimalUtil.translator = translator;
		initializeSc();
		showIntro();
	}

	public static void showIntro() {

		rightSc = lang.newSourceCode(new Coordinates(X_OFFSET, Y_OFFSET), "firstPage", null, scProps);
		showTitle(translator.translateMessage(I18n.algoName));

		String text = translator.translateMessage(I18n.textBegin);

		rightSc.addMultilineCode(text, null, null);
		lang.nextStep();
		rightSc.hide();

		rightSc = lang.newSourceCode(new Coordinates(X_OFFSET, Y_OFFSET), "steps", null, scProps);
		showTitle(translator.translateMessage(I18n.requiredSteps));

		text = translator.translateMessage(I18n.textSteps);
		rightSc.addMultilineCode(text, null, null);
		lang.nextStep();
		rightSc.hide();
	}

	public static void showFinal(List<Relation> relations) {
		lang.hideAllPrimitivesExcept(relationSc);
		showTitle(translator.translateMessage(I18n.titleFinal));
		showFinalRelations(relations);
		showInfo(translator.translateMessage(I18n.finalInfo));
	}

	private static void hideLines() {
		for (Polyline line : lines) {
			line.hide();
		}
	}

	public static void showTitle(String text) {
		if (title != null) {
			title.hide();
		}

		Node node = new Coordinates(X_OFFSET, 20);
		title = lang.newText(node, text, null, null);
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30), null, null);
	}

	public static void showClosure(Attribute att, List<FD> fds, FD current) {
		if (closure != null) {
			closure.hide();
		}

		List<Attribute> of = Closure.of(att, fds, current);
		StringBuilder message = new StringBuilder();
		message.append(translator.translateMessage(I18n.closureOf)).append(" ").append(att.getSymbol()).append("⁺ = {");

		for (Attribute a : of) {
			message.append(a.getSymbol()).append(", ");
		}

		message.setLength(message.length() - 2);
		message.append("}");

		int fontSize = 20;
		closure = lang.newText(info.getUpperLeft(), message.toString(), null, null);
		closure.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize), null, null);
	}

	public static void showClosure(List<Attribute> att, List<FD> fds, FD current) {
		if (closure != null) {
			closure.hide();
		}

		List<Attribute> of = Closure.of(att, fds, current);
		StringBuilder message = new StringBuilder();
		message.append(translator.translateMessage(I18n.closureOf)).append(" (");

		for (Attribute a : current.getKeys()) {
			message.append(a.getSymbol()).append(",");
		}
		message.replace(message.length() - 1, message.length(), ")");
		message.append("⁺ = {");

		for (Attribute a : of) {
			message.append(a.getSymbol()).append(", ");
		}

		message.setLength(message.length() - 2);
		message.append("}");

		int fontSize = 20;
		closure = lang.newText(info.getUpperLeft(), message.toString(), null, null);
		closure.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize), null, null);
	}

	public static void hideClosure() {
		closure.hide();
	}

	public static void hideDependencies(SourceCode sc) {
		sc.hide();
	}

	/**
	 * 
	 * @param fds
	 * @param step
	 *            values = "init","left","right","empty","assembled"
	 */
	public static void showDependencies(List<FD> fds, String step) {
		rightSc = lang.newSourceCode(new Offset(X_SIDE_OFFSET, Y_OFFSET, stepsSc, AnimalScript.DIRECTION_SW), step,
				null, scProps);

		rightSc.addCodeLine(translator.translateMessage(step), null, 0, null);
		emptyLine(rightSc);
		emptyLine(rightSc);

		for (FD fd : fds) {
			createFdLine(fd, step);
		}
	}

	public static void showRelations(List<FD> fds) {
		relationSc = lang.newSourceCode(new Offset(0, -20, rightSc, AnimalScript.DIRECTION_NW), "relationsSc", null,
				scProps);

		relationSc.addCodeLine(translator.translateMessage(I18n.relations) + " ", null, 0, null);
		lang.nextStep();
		emptyLine(relationSc);
		emptyLine(relationSc);

		for (int i = 0; i < fds.size(); i++) {
			relationSc.addCodeElement("R" + (i + 1), "R" + (i + 1), 0, null);
			relationSc.addCodeElement("(", null, 0, null);

			for (Iterator<Attribute> j = fds.get(i).getKeys().iterator(); j.hasNext();) {
				Attribute next = j.next();
				String label = "R" + fds.get(i).getId() + next.getSymbol();
				relationSc.addCodeElement(next.getSymbol(), label, 0, null);
				relationSc.addCodeElement(",", null, 0, null);
				relationSc.highlight(label);
			}

			for (Iterator<Attribute> j = fds.get(i).getValues().iterator(); j.hasNext();) {
				Attribute next = j.next();
				relationSc.addCodeElement(next.getSymbol(), fds.get(i).getId() + next.getSymbol(), 0, null);

				if (j.hasNext())
					relationSc.addCodeElement(",", null, 0, null);
				else
					relationSc.addCodeElement(")", null, 0, null);
			}

			PolylineProperties lineProp = new PolylineProperties();
			lineProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

			int yStartOffset = Y_OFFSET / 2 + 14;
			int lineOffset = 24;

			// Move type “translate #2”  move only node #2 of the object(s)
			Offset first = new Offset(10, yStartOffset + i * lineOffset, rightSc, AnimalScript.DIRECTION_NE);
			Offset second = new Offset(-X_OFFSET * 2, yStartOffset + i * lineOffset, relationSc,
					AnimalScript.DIRECTION_NW);
			Polyline line = lang.newPolyline(new Node[] { first, first }, "line" + i, null, lineProp);
			line.moveTo(AnimalScript.DIRECTION_NE, TRANSLATE_HEAD, second, null, new MsTiming(750));
			lines.add(line);

			emptyLine(relationSc);
			lang.nextStep();
		}
	}

	public static void showFinalRelations(List<Relation> relations) {
		Offset offset = new Offset(0, Y_OFFSET, title, AnimalScript.DIRECTION_SW);
//		relationSc = lang.newSourceCode(offset, null, null, scProps);
//
//		for (Relation relation : relations) {
//			createRelationLine(relation);
//		}
		relationSc.moveTo(AnimalScript.DIRECTION_NE, TRANSLATE, offset, null, null);
	}

	private static void createRelationLine(Relation relation) {
		relationSc.addCodeElement(relation.getName(), null, 0, null);
		relationSc.addCodeElement("(", null, 0, null);

		for (Iterator<Attribute> iterator = relation.getPrimaryKey().iterator(); iterator.hasNext();) {
			Attribute attribute = iterator.next();

			String label = "final" + relation.getName() + attribute.getSymbol();
			relationSc.addCodeElement(attribute.getSymbol(), label, 0, null);
			relationSc.highlight(label);

			if (iterator.hasNext())
				relationSc.addCodeElement(",", null, 0, null);
		}

		for (Iterator<Attribute> iterator2 = relation.getAttributes().iterator(); iterator2.hasNext();) {
			Attribute attribute2 = iterator2.next();

			relationSc.addCodeElement(attribute2.getSymbol(), null, 0, null);

			if (iterator2.hasNext())
				relationSc.addCodeElement(",", null, 0, null);
			else
				relationSc.addCodeElement(")", null, 0, null);
		}
	}

	public static void highLightRelation(int i, boolean highlighted) {
		if (highlighted)
			relationSc.highlight("R" + (i + 1));
		else
			relationSc.unhighlight("R" + (i + 1));
	}

	public static void addBinderRelation(Relation r) {
		relationSc.addCodeElement(r.getName(), null, 0, null);
		relationSc.addCodeElement("(", null, 0, null);

		for (Iterator<Attribute> j = r.getPrimaryKey().iterator(); j.hasNext();) {
			Attribute next = j.next();
			String label = "Binder" + next.getSymbol();
			relationSc.addCodeElement(next.getSymbol(), label, 0, null);
			relationSc.highlight(label);

			if (j.hasNext())
				relationSc.addCodeElement(",", null, 0, null);
			else
				relationSc.addCodeElement(")", null, 0, null);
		}

	}

	// Shows an info below the FDs and does one nextStep
	public static void showInfo(String message, SourceCode sc) {
		Node node = new Offset(0, 20, leftSc, AnimalScript.DIRECTION_SW);

		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.ITALIC, 20));
		scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		info = lang.newSourceCode(node, null, null, scp);

		for (String string : message.split("\n")) {
			info.addCodeLine(string, null, 0, null);
		}
		lang.nextStep();
		info.hide();
	}

	// Shows an info below the FDs and does one nextStep
	public static void showInfo(String message) {
		showInfo(message, leftSc);
	}

	public static void showSteps() {
		stepsSc = lang.newSourceCode(new Offset(0, Y_OFFSET / 2, title, AnimalScript.DIRECTION_SW), "steps", null,
				scProps);
		stepsSc.addMultilineCode(translator.translateMessage(I18n.allSteps), "stepsSc", null);
	}

	public static void highlightStep(int line) {
		stepsSc.unhighlight(stepIndex);
		stepIndex = line;
		stepsSc.highlight(stepIndex);
	}

	public static void unhighlightStep() {
		stepsSc.unhighlight(stepIndex);
	}

	private static void initializeSc() {
		scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}

	public static void moveRightSc() {
		MsTiming delay = new MsTiming(500);
		MsTiming duration = new MsTiming(1000);

		rightSc.moveBy(TRANSLATE, -X_SIDE_OFFSET, 0, delay, duration);

		if (leftSc != null)
			leftSc.hide();
		leftSc = rightSc;

		lang.nextStep();
	}

	public static void createFdLine(FD fd, String step) {
		addAttributeElements(fd, fd.getKeys(), step);
		rightSc.addCodeElement(" -> ", null, 0, null);
		addAttributeElements(fd, fd.getValues(), step);

		emptyLine(rightSc);
	}

	private static void addAttributeElements(FD fd, List<Attribute> atts, String step) {
		for (Iterator<Attribute> i = atts.iterator(); i.hasNext();) {
			Attribute next = i.next();
			String label = step + fd.getId() + next.getSymbol();
			rightSc.addCodeElement(next.getSymbol(), label, 0, null);

			if (i.hasNext())
				rightSc.addCodeElement(",", null, 0, null);
		}
	}

	public static void highLight(String label, boolean highLight) {
		if (highLight)
			leftSc.highlight(label);
		else
			leftSc.unhighlight(label);
	}

	public static void highLight(FD fd, boolean highLight, String step) {
		for (Attribute a : fd.getKeys()) {
			if (highLight)
				leftSc.highlight(step + fd.getId() + a.getSymbol());
			else
				leftSc.unhighlight(step + fd.getId() + a.getSymbol());
		}

		for (Attribute a : fd.getValues()) {
			if (highLight)
				leftSc.highlight(step + fd.getId() + a.getSymbol());
			else
				leftSc.unhighlight(step + fd.getId() + a.getSymbol());
		}
	}

	public static void highLight(List<String> labels, boolean highLight) {
		for (String label : labels) {
			if (highLight)
				leftSc.highlight(label);
			else
				leftSc.unhighlight(label);
		}
	}

	public static void emptyLine(SourceCode sc) {
		sc.addCodeLine(" ", null, 0, null);
	}

	public static SourceCode getFdSc() {
		return rightSc;
	}

	public static SourceCode getRelationSc() {
		return leftSc;
	}

	public static void addEmptyClauseQuestion(List<FD> fds) {

		FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("emptyQ");
		question.setPrompt(translator.translateMessage(I18n.emptyQPrompt));

		int i = 0;

		for (FD fd : fds) {
			if (fd.getValues().isEmpty())
				i++;
		}

		question.addAnswer(Integer.toString(i), 2, translator.translateMessage(I18n.rightAnswer));

		lang.addFIBQuestion(question);
	}

	public static void addAssembledQuestion(List<FD> fds) {

		FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("assembledQ");
		question.setPrompt(translator.translateMessage(I18n.assembledQPrompt));

		List<FD> copy = new ArrayList<>(fds);
		int count = 0;

		while (!copy.isEmpty()) {
			FD fd = copy.get(0);
			List<FD> toRemove = new ArrayList<>();

			for (int j = 1; j < copy.size(); j++) {
				if (hasSameDeterminant(fd, copy.get(j))) {
					count++;
					toRemove.add(copy.get(j));
				}
			}

			copy.remove(fd);
			for (FD fd2 : toRemove) {
				copy.remove(fd2);
			}
		}

		int answer = fds.size() - count;

		question.addAnswer(Integer.toString(answer), 2, translator.translateMessage(I18n.rightAnswer));

		lang.addFIBQuestion(question);
	}

	private static boolean hasSameDeterminant(FD fd1, FD fd2) {
		if (fd1.getKeys().size() == fd2.getKeys().size()) {
			for (Iterator<Attribute> iterator = fd1.getKeys().iterator(); iterator.hasNext();) {
				Attribute attribute = iterator.next();
				if (!fd2.getKeys().contains(attribute))
					return false;
			}
		} else
			return false;
		return true;
	}

	public static void addStepsQuestion() {

		MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("stepQ");
		question.setPrompt(translator.translateMessage(I18n.stepQPrompt));

		question.addAnswer(translator.translateMessage(I18n.leftReduction), -2,
				translator.translateMessage(I18n.wrongAnswer));
		question.addAnswer(translator.translateMessage(I18n.rightReduction), -2,
				translator.translateMessage(I18n.wrongAnswer));
		question.addAnswer(translator.translateMessage(I18n.emptyReduction), -2,
				translator.translateMessage(I18n.wrongAnswer));
		question.addAnswer(translator.translateMessage(I18n.assembledReduction), -2,
				translator.translateMessage(I18n.wrongAnswer));
		question.addAnswer(translator.translateMessage(I18n.wrongReduction), 2,
				translator.translateMessage(I18n.rightAnswer));

		lang.addMCQuestion(question);
	}
}
