package generators.maths.resolutionCalculus;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
import animal.graphics.PTStringArray;
import animal.main.Animal;
import generators.maths.ResolutionGenerator;
import generators.maths.resolutionCalculus.parser.Parser;
import generators.maths.resolutionCalculus.parser.Token;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

public class ResolutionGeneratorMain {

	private static Color colorHIGHLIGHT1;
	private static Color colorHIGHLIGHT2;
	private static Color colorCHECK;
	private static boolean isDebug;

	public static final int BACK = 16;
	public static final int FRONT = 0;

	public static final int RESOLUTION_LEFT = 400;
	public static final int CLAUSE_HEIGHT_FIXED = new PTStringArray(new String[] { "0" }).getBoundingBox(0).height + 10;
	public static final int CLAUSE_SPACING = 10;
	public static final int LEFT = 10;
	public static final int TOP = 10;

	public static Language lang;

	/**
	 * Used for early debugging, resort to {@link ResolutionGenerator} now
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		isDebug = true;

		// "{p, q}, {!p, s}, {!s}, {!q}"
		// "{!p,!q},{p,q},{p}"
		// "{p, q}, {!p, q}, {p, !q}, {!p, !q}"
		// "{!p,s,t,q},{!q,p},{!s,!t,q,p},{s,!t,!p},{p,q},{!p},{s,t}"
		// "{!s, t}, {s, t, q}, {!q, p}, {!q, !p}, {!s, !q}"

		Color PALE_BLUE = new Color(0, 127, 255);
		Color NICE_YELLOW = new Color(255, 216, 31);
		Color PEACEFUL_RED = new Color(255, 66, 31);
		Language lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Resolutionmocktest", "TestTest", 800,
				600);
		main(new Translator("resources/Resolution-Calculus", Locale.ENGLISH), lang,
				new Parser(Token.toTokens("{p, q}, {!p, s}, {!s}, {!q}")).getClauses(), PALE_BLUE, NICE_YELLOW,
				PEACEFUL_RED, true);
	}

	public static void main(Translator translator, Language lang, List<Clause> inputClauses, Color clauseHighlight1,
			Color clauseHighlight2, Color clauseCheck, boolean showQuestions) {
		ResolutionGeneratorMain.lang = lang;
		colorHIGHLIGHT1 = clauseHighlight1;
		colorHIGHLIGHT2 = clauseHighlight2;
		colorCHECK = clauseCheck;
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		TextProperties titleProp = new TextProperties();
		titleProp.set("font", new Font(Font.SANS_SERIF, Font.BOLD, 20));
		Text title = lang.newText(new Coordinates(LEFT, TOP), translator.translateMessage("name"), "null", null,
				titleProp);
		RectProperties rectProp = new RectProperties();
		rectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, BACK);
		rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 216, 31));
		Rect titleRect = lang.newRect(new Offset(-5, -5, title, "NW"), new Offset(5, 5, title, "SE"), "null", null,
				rectProp);

		InfoBox explanationBox = new InfoBox(lang, new Offset(0, 20, title, "SW"), 22, "");
		explanationBox.setText(Arrays.asList(translator.translateMessage("expl1"), translator.translateMessage("expl2"),
				translator.translateMessage("expl3"), "", translator.translateMessage("expl4"),
				translator.translateMessage("expl5"), translator.translateMessage("expl6"), "",
				translator.translateMessage("expl7"), translator.translateMessage("expl8"), "",
				translator.translateMessage("expl9"), translator.translateMessage("expl10"),
				translator.translateMessage("expl11"), translator.translateMessage("expl12"), "",
				translator.translateMessage("expl13"), translator.translateMessage("expl14"), "",
				translator.translateMessage("expl15"), translator.translateMessage("expl16"),
				translator.translateMessage("expl17")));

		// Offset not possible, InfoBox is not a Primitive

		Text exampleText = lang.newText(new Coordinates(800, 150), translator.translateMessage("exmplRes"), "null",
				null);
		AnimationDecoratedClause example1 = new AnimationDecoratedClause(new Clause("!s", "q"), lang,
				new Offset(-25, 25, exampleText, "SW"));
		AnimationDecoratedClause example2 = new AnimationDecoratedClause(new Clause("s", "p"), lang,
				new Offset(25, 25, exampleText, "SE"));
		AnimationDecoratedClause exampleResolvent = new AnimationDecoratedClause(new Clause("q", "p"), lang,
				new Offset(0, 100, exampleText, "S"));
		PolylineProperties exampleLineProps = new PolylineProperties();
		exampleLineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		exampleLineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, BACK);
		Polyline exampleL1 = lang.newPolyline(new Node[] { example1.getPosition(), exampleResolvent.getPosition() },
				"null", null, exampleLineProps);
		Polyline exampleL2 = lang.newPolyline(new Node[] { example2.getPosition(), exampleResolvent.getPosition() },
				"null", null, exampleLineProps);
		example1.highlight(colorHIGHLIGHT1);
		example1.highlightCell(colorHIGHLIGHT2, "!s");
		example2.highlight(colorHIGHLIGHT1);
		example2.highlightCell(colorHIGHLIGHT2, "s");
		exampleResolvent.highlight(colorHIGHLIGHT1);

		lang.nextStep(-1, "Resolving first generation");

		if (showQuestions) {
			MultipleChoiceQuestionModel emptyClauseQuestion = new MultipleChoiceQuestionModel("1");
			emptyClauseQuestion.setPrompt(translator.translateMessage("emptyClauseQ0"));
			emptyClauseQuestion.addAnswer(translator.translateMessage("emptyClauseQ1"), 0,
					translator.translateMessage("emptyClauseQ2"));
			emptyClauseQuestion.addAnswer(translator.translateMessage("emptyClauseQ3"), 0,
					translator.translateMessage("emptyClauseQ4"));
			emptyClauseQuestion.addAnswer(translator.translateMessage("emptyClauseQ5"), 1,
					translator.translateMessage("emptyClauseQ6"));
			emptyClauseQuestion.addAnswer(translator.translateMessage("emptyClauseQ7"), 0,
					translator.translateMessage("emptyClauseQ8"));
			lang.addMCQuestion(emptyClauseQuestion);
		}

		explanationBox.hide();
		exampleText.hide();
		example1.destroy();
		example2.destroy();
		exampleL1.hide();
		exampleL2.hide();
		exampleResolvent.destroy();

		try {
			List<AnimationDecoratedClause> allClauses = generateResolution(title, inputClauses);

			allClauses.sort((c1, c2) -> c1.literalCount() - c2.literalCount());

			lang.nextStep("Resolution calculus shows a proof for satisfiability");

			InfoBox satisfiabilityBox = new InfoBox(lang, new Offset(0, 20, title, "SW"), 15,
					translator.translateMessage("satExpl0"));
			satisfiabilityBox.setText(Arrays.asList(translator.translateMessage("satExpl1"),
					translator.translateMessage("satExpl2"), translator.translateMessage("satExpl3"),
					translator.translateMessage("satExpl4"), translator.translateMessage("satExpl5"), "",
					translator.translateMessage("satExpl6"), translator.translateMessage("satExpl7"),
					translator.translateMessage("satExpl8"), "", translator.translateMessage("satExpl9")));

			// Not possible because InfoBox is no primitive
			// new Offset(20, 0, satisfiabilityBox, "E").getX();

			AtomicInteger x = new AtomicInteger(RESOLUTION_LEFT);
			AtomicInteger y = new AtomicInteger(TOP);
			int currentLiteralCount = allClauses.get(0).literalCount();
			for (AnimationDecoratedClause clause : allClauses) {
				if (currentLiteralCount != clause.literalCount()) {
					nextLine(x, y);
					currentLiteralCount = clause.literalCount();
				}
				clause.setPosition(new Coordinates(x.get(), y.get()));
				if (inputClauses.contains(clause.getEmbeddedClause()))
					clause.highlight(colorHIGHLIGHT2);
				x.addAndGet(clause.getWidth() + CLAUSE_SPACING);
			}

			// TextProperties properties = new TextProperties();
			// properties.set(AnimationPropertiesKeys.SIZE_PROPERTY, 16);
			// lang.newText(new Coordinates(RESOLUTION_LEFT, y.get() + CLAUSE_HEIGHT_FIXED),
			// "-> Satisfiable", "null",
			// null, properties);
		} catch (UnsatisfiabilityException e) {
			lang.nextStep("Resolution calculus shows a proof for unsatisfiability");

			lang.hideAllPrimitivesExcept(Arrays.asList(title, titleRect, e.getCause1().getPrimitive(),
					e.getCause2().getPrimitive(), e.getEmptyClauseProve().getPrimitive()));

			InfoBox unsatisfiabilityBox = new InfoBox(lang, new Offset(0, 20, title, "SW"), 10,
					translator.translateMessage("unsatExpl0"));
			unsatisfiabilityBox.setText(
					Arrays.asList(translator.translateMessage("unsatExpl1"), translator.translateMessage("unsatExpl2"),
							translator.translateMessage("unsatExpl3"), "", translator.translateMessage("unsatExpl4")));

			// Offset not possible from InfoBox

			e.getCause1().setPosition(new Coordinates(400, TOP));
			e.getCause2().setPosition(new Coordinates(550, TOP));
			e.getEmptyClauseProve().setPosition(new Coordinates(475, TOP + 100));
			Polyline u1 = lang.newPolyline(
					new Node[] { e.getCause1().getPosition(), e.getEmptyClauseProve().getPosition() }, "null", null,
					exampleLineProps);
			Polyline u2 = lang.newPolyline(
					new Node[] { e.getCause2().getPosition(), e.getEmptyClauseProve().getPosition() }, "null", null,
					exampleLineProps);
			u1.hide();
			u2.hide();
			u1.show(Timing.MEDIUM);
			u2.show(Timing.MEDIUM);
		}

		if (showQuestions) {
			MultipleChoiceQuestionModel parallelResolutionQuestion = new MultipleChoiceQuestionModel("2");
			parallelResolutionQuestion.setPrompt(translator.translateMessage("parallelResQ0"));
			parallelResolutionQuestion.addAnswer(translator.translateMessage("parallelResQ1"), 1,
					translator.translateMessage("correct"));
			parallelResolutionQuestion.addAnswer(translator.translateMessage("parallelResQ2"), 0,
					translator.translateMessage("parallelResQ3"));
			parallelResolutionQuestion.addAnswer(translator.translateMessage("parallelResQ4"), 0,
					translator.translateMessage("parallelResWrong"));
			parallelResolutionQuestion.addAnswer(translator.translateMessage("parallelResQ5"), 0,
					translator.translateMessage("parallelResWrong"));
			lang.addMCQuestion(parallelResolutionQuestion);

			MultipleChoiceQuestionModel runtimeQuestion = new MultipleChoiceQuestionModel("3");
			runtimeQuestion.setPrompt(translator.translateMessage("runtimeQ0"));
			runtimeQuestion.addAnswer(translator.translateMessage("runtimeQ1"), 0,
					translator.translateMessage("runtimeQ2"));
			runtimeQuestion.addAnswer(translator.translateMessage("runtimeQ3"), 1,
					translator.translateMessage("runtimeQ4"));
			runtimeQuestion.addAnswer(translator.translateMessage("runtimeQ5"), 0,
					translator.translateMessage("runtimeQ6"));
			runtimeQuestion.addAnswer(translator.translateMessage("runtimeQ7"), 1,
					translator.translateMessage("runtimeQ8"));
			lang.addMCQuestion(runtimeQuestion);
		}

		lang.finalizeGeneration();

		if (isDebug) {
			String code = lang.getAnimationCode();

			PrintStream systemOutStream = System.out;
			PrintStream fileStream = null;
			try {
				fileStream = new PrintStream(new File("resolutionTest.asu"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			System.setOut(fileStream);
			System.out.println(code);
			System.setOut(systemOutStream);
			System.out.println("Script generated");

			Animal.startAnimationFromAnimalScriptCode(code);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<AnimationDecoratedClause> generateResolution(Text title, List<Clause> clauses)
			throws UnsatisfiabilityException {
		SourceCodeProperties prop = new SourceCodeProperties();
		prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);

		SourceCode firstGenerationPseudoCode = lang.newSourceCode(new Offset(0, 20, title, "SW"), "null", null, prop);
		firstGenerationPseudoCode.addCodeLine("resolveFirstGen(oldGen, newGen)", "1", 0, null);
		firstGenerationPseudoCode.addCodeLine("for i = 0 to oldGen.length", "2", 1, null);
		firstGenerationPseudoCode.addCodeLine("for j = i to oldGen.length", "3", 2, null);
		firstGenerationPseudoCode.addCodeLine("newGen.add(resolve(oldGen[i], oldGen[j]))", "4", 3, null);

		SourceCode laterGenerationPseudoCode = lang.newSourceCode(new Offset(0, 20, title, "SW"), "null", null, prop);
		laterGenerationPseudoCode.addMultilineCode(
				"// Called until nextGen is empty (-> all clauses seen, satisfiable)\n// or unsatisfiability is detected (checkForUnsatisfiability)",
				"0", null);
		laterGenerationPseudoCode.addCodeLine("resolveLaterGens(oldGen, newGen, nextGen)", "1", 0, null);
		laterGenerationPseudoCode.addCodeLine("foreach clause1 : newGen", "2", 1, null);
		laterGenerationPseudoCode.addCodeLine("foreach clause2 : oldGen UNION newGen", "3", 2, null);
		laterGenerationPseudoCode.addCodeLine("if (clause1 == clause2) continue", "4", 3, null);
		laterGenerationPseudoCode.addCodeLine("nextGen.add(resolve(clause1, clause2))", "5", 3, null);
		laterGenerationPseudoCode.addCodeLine("add newGen to oldGen, clear newGen", "6", 0, null);
		laterGenerationPseudoCode.addCodeLine("add nextGen to newGen, clear nextGen", "7", 0, null);

		laterGenerationPseudoCode.hide();

		SourceCode resolvePseudoCode = lang.newSourceCode(new Offset(0, 20, firstGenerationPseudoCode, "SW"), "null",
				null, prop);
		resolvePseudoCode.addCodeLine("resolve(clause1, clause2)", "1", 0, null);
		resolvePseudoCode.addCodeLine("res = new List<Clause>", "2", 1, null);
		resolvePseudoCode.addCodeLine("foreach literal : clause1.literals", "3", 1, null);
		resolvePseudoCode.addCodeLine("negation = clause2.getNegatedLiteral(literal)", "4", 2, null);
		resolvePseudoCode.addCodeLine("if negation != null", "6", 2, null);
		resolvePseudoCode.addMultilineCode(
				"\t\t\tres.add(new Clause((clause1.literals UNION \n\t\t\tclause2.literals) EXCEPT (literal UNION negation)))",
				"7", null);
		resolvePseudoCode.addCodeLine("checkForUnsatisfiability(res)", "8", 1, null);
		resolvePseudoCode.addCodeLine("removeAlreadyExisting(res)", "9", 1, null);
		resolvePseudoCode.addCodeLine("return res", "10", 1, null);

		// Initialization: Parse input and show all initial clauses

		List<AnimationDecoratedClause> oldGens = new ArrayList<>();
		List<AnimationDecoratedClause> newGen = new ArrayList<>();
		List[] oldNewContainer = new List[] { oldGens, newGen };
		List<AnimationDecoratedClause> nextGen = new ArrayList<>();

		TripleListContainer<AnimationDecoratedClause> container = new TripleListContainer<>(oldGens, newGen, nextGen);

		AtomicInteger currentX = new AtomicInteger(RESOLUTION_LEFT);
		AtomicInteger currentY = new AtomicInteger(TOP);

		clausesToAnimationLine(clauses, oldGens, currentX, currentY);

		nextLine(currentX, currentY);
		lang.nextStep();

		// 1. step: Each with each and populate the newGen cache with that

		for (int i = 0; i < oldGens.size(); ++i) {
			AnimationDecoratedClause c1 = oldGens.get(i);

			c1.highlight(colorHIGHLIGHT1);
			firstGenerationPseudoCode.highlight(1);
			lang.nextStep();

			for (int j = i + 1; j < oldGens.size(); ++j) {
				AnimationDecoratedClause c2 = oldGens.get(j);

				c2.highlight(colorHIGHLIGHT2);
				firstGenerationPseudoCode.toggleHighlight(1, 2);
				lang.nextStep();

				firstGenerationPseudoCode.toggleHighlight(2, 3);
				List<AnimationDecoratedClause> resolvents = generateAndShowResolutionOfTwoClauses(resolvePseudoCode, c1,
						c2);
				firstGenerationPseudoCode.unhighlight(3);

				resolvePseudoCode.highlight(8);
				resolvePseudoCode.highlight(9);
				addAllToLineAndReposition(resolvents, newGen, container, currentX, currentY);
				resolvePseudoCode.unhighlight(8);
				resolvePseudoCode.unhighlight(9);

				c2.dehighlight();
			}

			c1.dehighlight();
		}

		nextLine(currentX, currentY);
		firstGenerationPseudoCode.hide();
		laterGenerationPseudoCode.show(Timing.MEDIUM);
		resolvePseudoCode.moveTo(AnimalScript.DIRECTION_S, null, new Offset(0, 20, laterGenerationPseudoCode, "SW"),
				Timing.INSTANTEOUS, Timing.FAST);
		lang.nextStep("Resolving new generation with older generations and itself");

		// Actual algorithm: Resolve newGen with both current gens to nextGen and ripple
		// push those registers afterwards

		// We already have generation 1 generated at this spot
		int generationCounter = 2;

		for (;;) {
			for (AnimationDecoratedClause clause1 : newGen) {
				clause1.highlight(colorHIGHLIGHT1);
				laterGenerationPseudoCode.highlight(3);
				lang.nextStep();

				for (List<AnimationDecoratedClause> list : oldNewContainer) {
					for (AnimationDecoratedClause clause2 : list) {
						if (clause1.equals(clause2))
							continue;

						clause2.highlight(colorHIGHLIGHT2);
						laterGenerationPseudoCode.toggleHighlight(3, 4);
						lang.nextStep();

						laterGenerationPseudoCode.toggleHighlight(4, 5);
						laterGenerationPseudoCode.highlight(6);
						List<AnimationDecoratedClause> resolvents = generateAndShowResolutionOfTwoClauses(
								resolvePseudoCode, clause1, clause2);
						laterGenerationPseudoCode.unhighlight(5);
						laterGenerationPseudoCode.unhighlight(6);

						resolvePseudoCode.highlight(8);
						resolvePseudoCode.highlight(9);
						addAllToLineAndReposition(resolvents, nextGen, container, currentX, currentY);
						resolvePseudoCode.unhighlight(8);
						resolvePseudoCode.unhighlight(9);

						clause2.dehighlight();
					}
				}

				clause1.dehighlight();
			}

			nextLine(currentX, currentY);

			if (nextGen.isEmpty())
				break;

			laterGenerationPseudoCode.unhighlight(5);
			laterGenerationPseudoCode.unhighlight(6);
			laterGenerationPseudoCode.highlight(7);
			laterGenerationPseudoCode.highlight(8);
			lang.nextStep(String.format("Resolving generation %d", ++generationCounter));
			oldGens.addAll(newGen);
			newGen.clear();
			newGen.addAll(nextGen);
			nextGen.clear();
			laterGenerationPseudoCode.unhighlight(7);
			laterGenerationPseudoCode.unhighlight(8);
		}

		firstGenerationPseudoCode.hide();
		laterGenerationPseudoCode.hide();
		resolvePseudoCode.hide();

		// Just returning that to show the finishing slide
		oldGens.addAll(newGen);
		return oldGens;
	}

	private static void clausesToAnimationLine(List<? extends Clause> clauses,
			List<AnimationDecoratedClause> animationLine, AtomicInteger x, AtomicInteger y) {
		for (Clause clause : clauses) {
			AnimationDecoratedClause decorated = new AnimationDecoratedClause(clause, lang,
					new Coordinates(x.get(), y.get()));
			x.addAndGet(decorated.getWidth() + CLAUSE_SPACING);
			animationLine.add(decorated);
		}
	}

	private static void addAllToLineAndReposition(List<AnimationDecoratedClause> resolvents,
			List<AnimationDecoratedClause> animationLine, TripleListContainer<AnimationDecoratedClause> container,
			AtomicInteger x, AtomicInteger y) {
		for (AnimationDecoratedClause clause : resolvents) {
			if (container.contains(clause))
				clause.destroy();
			else {
				clause.setPosition(new Coordinates(x.get(), y.get()));
				x.addAndGet(clause.getWidth() + CLAUSE_SPACING);
				animationLine.add(clause);
			}
		}
		lang.nextStep();
	}

	private static List<AnimationDecoratedClause> generateAndShowResolutionOfTwoClauses(SourceCode resolvePseudoCode,
			AnimationDecoratedClause c1, AnimationDecoratedClause c2) throws UnsatisfiabilityException {
		AnimationDecoratedClause copyOfC1 = new AnimationDecoratedClause(c1.getEmbeddedClause(), lang,
				new Coordinates(10, 400));
		AnimationDecoratedClause copyOfC2 = new AnimationDecoratedClause(c2.getEmbeddedClause(), lang,
				new Coordinates(160, 400));

		resolvePseudoCode.highlight(1);
		List<AnimationDecoratedClause> animatedClauses = new ArrayList<>();

		lang.nextStep(String.format("Resolving %s with %s", c1.toString(), c2.toString()));

		resolvePseudoCode.unhighlight(1);

		resolvePseudoCode.highlight(2);
		resolvePseudoCode.highlight(3);
		resolvePseudoCode.highlight(4);
		resolvePseudoCode.highlight(5);
		resolvePseudoCode.highlight(6);

		c1.resolveWith(c2.getEmbeddedClause(), new IResolveListener() {
			AnimationDecoratedClause leftClause = null;

			@Override
			public void onResolve(String causing1, String causing2, Clause resolvent) {
				copyOfC1.highlight(colorHIGHLIGHT1);
				copyOfC2.highlight(colorHIGHLIGHT1);
				copyOfC1.highlightCell(colorHIGHLIGHT2, causing1);
				copyOfC2.highlightCell(colorHIGHLIGHT2, causing2);

				Coordinates coords = new Coordinates(leftClause == null ? 10
						: ((Coordinates) leftClause.getPosition()).getX() + leftClause.getWidth() + CLAUSE_SPACING,
						500);

				AnimationDecoratedClause colored = new AnimationDecoratedClause(resolvent, lang, coords);
				leftClause = colored;
				colored.highlight(colorHIGHLIGHT1);

				PolylineProperties properties = new PolylineProperties();
				properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
				properties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, BACK);
				Polyline line = lang.newPolyline(new Node[] { new Coordinates(10, 400), coords }, "null", null,
						properties);

				Polyline line2 = lang.newPolyline(new Node[] { new Coordinates(160, 400), coords }, "null", null,
						properties);

				lang.nextStep();

				line.hide();
				line2.hide();
				colored.dehighlight();
				animatedClauses.add(colored);
			}
		});

		resolvePseudoCode.unhighlight(2);
		resolvePseudoCode.unhighlight(3);
		resolvePseudoCode.unhighlight(4);
		resolvePseudoCode.unhighlight(5);
		resolvePseudoCode.unhighlight(6);

		resolvePseudoCode.highlight(7);

		for (AnimationDecoratedClause clause : animatedClauses) {
			clause.highlight(colorCHECK);
			if (clause.isUnsatisfiable()) {
				throw new UnsatisfiabilityException(copyOfC1, copyOfC2, clause);
			}
			lang.nextStep();
			clause.dehighlight();
		}

		if (animatedClauses.isEmpty())
			lang.nextStep();

		resolvePseudoCode.unhighlight(7);

		copyOfC1.destroy();
		copyOfC2.destroy();

		return animatedClauses;
	}

	public static void nextLine(AtomicInteger x, AtomicInteger y) {
		x.set(RESOLUTION_LEFT);
		y.addAndGet(CLAUSE_HEIGHT_FIXED);
	}

}
