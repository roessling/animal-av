/*
 * SweepAndPruneAABB2D.java
 * Johannes Alef, Rebecca Schieren, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc.sweepandpruneaabb2d;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import translator.Translator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.exceptions.LineNotExistsException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class SweepAndPruneAABB2D implements ValidatingGenerator {


	private Language lang;
	private SourceCodeProperties scProps;
	private ArrayProperties arrayProps;
	private int[][] intMatrix;

	public final static Timing  defaultDuration = new TicksTiming(30);

	private Locale localLang = Locale.GERMANY;

	private Translator translator;

	private int maxCollisionQuestions = 3;
	private int collisionQuestionProbability = 100;
	private int nextAxisQuestionProbability = 100;
	Random r = new Random();

	public SweepAndPruneAABB2D(String languageResource, Locale targetLocale) {
		super();
		localLang = targetLocale;
		translator  = new Translator(languageResource, targetLocale);

	}

	public void init(){
		lang = new AnimalScript(translator.translateMessage("name"), "Johannes Alef, Rebecca Schieren", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		arrayProps = (ArrayProperties)props.getPropertiesByName("array");
		intMatrix = (int[][])primitives.get("AABBS (minX, maxX, minY, maxY)");
		maxCollisionQuestions = (int)primitives.get("maximum number of collision Questions");
		collisionQuestionProbability = (int)primitives.get("probability of collision Questions");
		nextAxisQuestionProbability = (int)primitives.get("probability of next axis Questions");

		List<AABB> input = new ArrayList<AABB>();

		for (int i = 0; i < intMatrix.length; i++) {
			input.add(new AABB(intMatrix[i][0], intMatrix[i][1], intMatrix[i][2], intMatrix[i][3]));
		}

		new QuestionGroupModel("collision", maxCollisionQuestions);



		sweep(input);

		lang.finalizeGeneration();
		
		System.out.println("f\u00FCr");

		return lang.toString();
	}


	//check that we have exactly four columns and can thus create the AABBs
	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {

		int[][] testMatrix = (int[][])arg1.get("AABBS (minX, maxX, minY, maxY)");

		for (int i = 0; i < testMatrix.length;i++) {
			if (testMatrix[i].length != 4) {
				throw new IllegalArgumentException(translator.translateMessage("illegalCollumns"));
			}
		}

		return true;
	}


	public String getName() {
		return "Sweep and Prune für Axis-aligned Bounding Boxes in 2D";
	}

	public String getAlgorithmName() {
		return "Sweep and Prune";
	}

	public String getAnimationAuthor() {
		return "Johannes Alef, Rebecca Schieren";
	}

	public String getDescription(){
		return translator.translateMessage("descriptionText");
	}

	public String getCodeExample(){
		return translator.translateMessage("pseudeCode");
	}

	public String getFileExtension(){
		return "asu";
	}

	public Locale getContentLocale() {
		return localLang;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}


	ArrayMarker activeMarker;

	/**
	 * Check for possible Collisions
	 * 
	 * @param input
	 *          the list of the AABBs of the objects in the scene
	 */
	public void sweep(List<AABB> input) {

		possibleCollisionsY = new ArrayList<AABBPair>();
		possibleCollisionsX = new ArrayList<AABBPair>();

		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24));

		lang.newText(new Coordinates(20, 30), translator.translateMessage("name"),
				"header", null, tp);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "headerRect", null, rectProps);

		lang.nextStep(translator.translateMessage("introduction"));

		// now, create the source code entity
		SourceCode description = lang.newSourceCode(new Offset(0, 50, "headerRect", "SW"), "description",
				null, scProps);

		// Add the lines to the SourceCode object.
		description.addCodeLine(translator.translateMessage("descriptionline0"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline1"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline2"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline3"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline4"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline5"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline6"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline7"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline8"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline9"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline10"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline11"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline12"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline13"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline14"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline15"), null, 0, null);
		description.addCodeLine(translator.translateMessage("descriptionline16"), null, 0, null);


		lang.nextStep();

		description.hide();

		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Offset(0, 50, "headerRect", "SW"), "sourceCode",
				null, scProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine(translator.translateMessage("pseudocode0"), null, 0, null);
		sc.addCodeLine(translator.translateMessage("pseudocode1"), null, 0, null);
		sc.addCodeLine(translator.translateMessage("pseudocode2"), null, 0, null);
		sc.addCodeLine(translator.translateMessage("pseudocode3"), null, 1, null);
		sc.addCodeLine(translator.translateMessage("pseudocode4"), null, 1, null);
		sc.addCodeLine(translator.translateMessage("pseudocode5"), null, 1, null);
		sc.addCodeLine(translator.translateMessage("pseudocode6"), null, 1, null);
		sc.addCodeLine(translator.translateMessage("pseudocode7"), null, 2, null);
		sc.addCodeLine(translator.translateMessage("pseudocode8"), null, 3, null);
		sc.addCodeLine(translator.translateMessage("pseudocode9"), null, 4, null);
		sc.addCodeLine(translator.translateMessage("pseudocode10"), null, 4, null);
		sc.addCodeLine(translator.translateMessage("pseudocode11"), null, 3, null);
		sc.addCodeLine(translator.translateMessage("pseudocode12"), null, 2, null);
		sc.addCodeLine(translator.translateMessage("pseudocode13"), null, 1, null);
		sc.addCodeLine(translator.translateMessage("pseudocode14"), null, 0, null);

		lang.nextStep();


		//get scalefactor
		int maxX = 10;
		int maxY = 10;
		int minX = 0;
		int minY = 0;

		for (AABB i : input) {
			if (maxX < i.getMaxX()){
				maxX = i.getMaxX();
			}
			if (maxY < i.getMaxY()){
				maxY = i.getMaxY();
			}
			if (minX > i.getMinX()){
				minX = i.getMinX();
			}
			if (minY > i.getMinY()){
				minY = i.getMinY();
			}
		}

		int absoluteLength = 200;
		float scaleX = (float) absoluteLength / (float)(maxX-minX);
		float scaleY = (float) absoluteLength / (float)(maxY-minY);

		float scale = Math.min(scaleX, scaleY);

		//coordinate system
		PolylineProperties plProps = new PolylineProperties();
		plProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
		plProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		algoanim.util.Node[] xAxisCoords = {new Offset(50, absoluteLength, "sourceCode", "NE"), new Offset(50 + absoluteLength, absoluteLength, "sourceCode", "NE")};
		Polyline xAxis = lang.newPolyline(xAxisCoords, "xAxis", null, plProps);

		algoanim.util.Node[] yAxisCoords = {new Offset(50, absoluteLength, "sourceCode", "NE"), new Offset(50, 0, "sourceCode", "NE")};
		Polyline yAxis = lang.newPolyline(yAxisCoords, "yAxis", null, plProps);

		//AABBS
		String[] names = new String[input.size()];
		new RectProperties();
		int i = 0;
		for (AABB box : input) {
			lang.newRect(new Offset((int) scale * box.getMinX(), -(int) scale * box.getMaxY(), "xAxis", "SW"),
					new Offset((int) scale * box.getMaxX(), -(int) scale * box.getMinY(), "xAxis", "SW"),
					Integer.toString(box.getMyID()),
					null);
			lang.newText(new Offset((int) (box.getMaxX() - box.getMinX())/2 - 3,
					(int) - (box.getMaxY() - box.getMinY())/2 - 3, Integer.toString(box.getMyID()), "SW"),
					Integer.toString(box.getMyID()), Integer.toString(box.getMyID()) + "name", null);
			names[i] = Integer.toString(box.getMyID());
			i++;
		}


		StringArray consideredList = lang.newStringArray(new Offset(0, -nameYOffset * 2, "sourceCode", "SW"), names, "consideredList",
				null, arrayProps);
		lang.newText(new Offset(0, -nameYOffset/2, "consideredList", "SW"),
				translator.translateMessage("consideredList"), "consideredListName", null);

		sc.highlight(0);
		lang.nextStep();

		String[] possibleCollisions = new String[input.size()*(input.size()-1)/2];
		for (i = 0; i < possibleCollisions.length; i++) {
			possibleCollisions[i] = "   ";
		}


		StringArray possibleCollisionListsX = lang.newStringArray(new Offset(0, -nameYOffset * 2, "consideredListName", "SW"), 
				possibleCollisions, "possibleCollisionListsX",
				null, arrayProps);
		lang.newText(new Offset(0, -nameYOffset/2, "possibleCollisionListsX", "SW"),
				translator.translateMessage("possibleCollisionsList")+"X", "possibleCollisionListXName", null);


		StringArray possibleCollisionListY = lang.newStringArray(new Offset(0, -nameYOffset * 2, "possibleCollisionListXName", "SW"), 
				possibleCollisions, "possibleCollisionListY",
				null, arrayProps);
		lang.newText(new Offset(0, -nameYOffset/2, "possibleCollisionListY", "SW"),
				translator.translateMessage("possibleCollisionsList")+"Y", "possibleCollisionListYName", null);


		sc.highlight(1);
		sc.unhighlight(0);
		lang.nextStep();

		String[] emptyStrings = new String[input.size()];
		for (int j = 0; j < emptyStrings.length; j++) {
			emptyStrings[j] = "  ";
		}


		StringArray activeListVisual = lang.newStringArray(new Offset(150, 0, "consideredList", "NE"),
				emptyStrings, "activeList",
				null, arrayProps);
		lang.newText(new Offset(0, -nameYOffset/2, "activeList", "SW"),
				translator.translateMessage("activeList"), "activeListName", null);

		activeListVisual.hide();

		ArrayMarkerProperties arrayPMProps = new ArrayMarkerProperties();
		arrayPMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "active");
		arrayPMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		arrayPMProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);

		activeMarker = lang.newArrayMarker(activeListVisual, 0,
				"activeMarker", null, arrayPMProps);

		activeMarker.hide();


		TwoValueCounter counter = lang.newCounter(activeListVisual); // Zaehler anlegen
		CounterProperties cp = new CounterProperties(); // Zaehler-Properties anlegen
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau
		lang.newCounterView(counter, new Offset(200, 0, "activeList", "NE"), cp, true, true);

		try {
			// Start sweepandprune
			sweepAndPrune(input, true, sc, xAxis, yAxis, possibleCollisionListsX,
					possibleCollisionListY, consideredList, activeListVisual, counter);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		sc.hide();

		//highlight identified collisions in each list
		for (int k = 0; k < possibleCollisionsY.size(); k++) {
			possibleCollisionListY.highlightElem(k, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			for (int l = 0; l < possibleCollisionsX.size(); l++){
				if (possibleCollisionsX.get(l).equals(possibleCollisionsY.get(k))){
					possibleCollisionListsX.highlightElem(l, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				}
			}
		}

		SourceCode conclusion = lang.newSourceCode(new Offset(0, 50, "headerRect", "SW"), "conclusion",
				null, scProps);



		conclusion.addCodeLine(translator.translateMessage("conclusionLine0"), null, 0, null);
		conclusion.addCodeLine(translator.translateMessage("conclusionLine1"), null, 0, null);
		conclusion.addCodeLine(translator.translateMessage("conclusionLine2a")
				+ ((input.size()*(input.size()-1))/2 - possibleCollisionsY.size())
				+ " "
				+ translator.translateMessage("conclusionLine2b")
				+ (input.size()*(input.size()-1)/2)
				+ " "
				+ translator.translateMessage("conclusionLine2c"), null, 0, null);
		conclusion.addCodeLine(translator.translateMessage("conclusionLine3a")
				+ counter.getAccess()
				+ " "
				+ translator.translateMessage("conclusionLine3b"), null, 0, null);

		String toBeTested = "";
		for (i = 0; i < possibleCollisionsY.size() - 1; i++) {
			toBeTested += " " 
					+ Integer.toString(possibleCollisionsY.get(i).first.getMyID())
					+ ":" + Integer.toString(possibleCollisionsY.get(i).second.getMyID());
		}
		if (possibleCollisionsY.size() > 1) {
			toBeTested += translator.translateMessage("and");
		}
		toBeTested += Integer.toString(possibleCollisionsY.get(possibleCollisionsY.size()-1).first.getMyID())
				+ ":" + Integer.toString(possibleCollisionsY.get(possibleCollisionsY.size()-1).second.getMyID());

		conclusion.addCodeLine(translator.translateMessage("conclusionLine4a")
				+ toBeTested
				+ " "
				+ translator.translateMessage("conclusionLine4b"), null, 0, null);


		lang.nextStep(translator.translateMessage("conclusion"));



	}

	List<AABBPair> possibleCollisionsY = new ArrayList<AABBPair>();
	List<AABBPair> possibleCollisionsX = new ArrayList<AABBPair>();

	int nameYOffset = -20;

	/**
	 * go through the axis identifying possible collisions
	 * @param consideredList - AABBs to consider on the current axis
	 * @param xAxisFlag - is the x-axis the current axis?
	 * @param sc - sourceCode for highlighting lines
	 * @param xAxis - for highlighting the x-axis
	 * @param yAxis - for highlighting the y-axis
	 * @param possibleCollisionListsX - for adding the possible collisions along the x-axis
	 * @param possibleCollisionListY - for adding the possible collisions along the x-axis
	 * @param consideredList2 - the visual consideredList 
	 * @param activeListVisual - the visual list of currently active AABBs
	 * @param counter - counter for the activeList for de-/activation pruposes
	 */
	private void sweepAndPrune(List<AABB> consideredList, boolean xAxisFlag, SourceCode sc,
			Polyline xAxis, Polyline yAxis, StringArray possibleCollisionListsX,
			StringArray possibleCollisionListY, StringArray consideredList2, StringArray activeListVisual, TwoValueCounter counter){

		sc.highlight(2);
		sc.unhighlight(1);

		String sectionHeadline = translator.translateMessage("xAxis");

		//highlight the right axis
		if (xAxisFlag) {
			xAxis.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		} else {
			xAxis.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			yAxis.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			sectionHeadline = translator.translateMessage("yAxis");
		}

		lang.nextStep(sectionHeadline);

		List<AABB> nextConsideredList = new ArrayList<AABB>();

		String[] emptyStrings = new String[consideredList.size()];
		for (int i = 0; i < emptyStrings.length; i++) {
			emptyStrings[i] = "  ";
		}

		StringArray nextConsideredListVisual = lang.newStringArray(new Offset(0, -nameYOffset * 2, "activeListName", "SW"),
				emptyStrings, "nextConsideredList",
				null, arrayProps);
		lang.newText(new Offset(0, -nameYOffset/2, "nextConsideredList", "SW"),
				translator.translateMessage("nextConsideredList"), "nextConsideredListName", null);

		List<AABB> activeList = new ArrayList<AABB>();

		activeListVisual.show();
		activeMarker.hide();

		sc.highlight(3);
		sc.unhighlight(2);
		lang.nextStep();

		//gather the corner points
		List<CornerPoint> cornerList = new ArrayList<CornerPoint>();
		for (AABB aabb : consideredList) {

			CornerPoint minPoint = new CornerPoint();
			minPoint.setOwner(aabb);
			minPoint.setType(MinMax.MIN);
			if (xAxisFlag) {
				minPoint.setValue(aabb.getMinX());
			} else {
				minPoint.setValue(aabb.getMinY());
			}

			cornerList.add(minPoint);

			CornerPoint maxPoint = new CornerPoint();
			maxPoint.setOwner(aabb);
			maxPoint.setType(MinMax.MAX);
			if (xAxisFlag) {
				maxPoint.setValue(aabb.getMaxX());
			} else {
				maxPoint.setValue(aabb.getMaxY());
			}

			cornerList.add(maxPoint);
		}

		String[] cornerListStrings = new String[cornerList.size()];
		for (int i = 0; i < cornerListStrings.length; i++){
			cornerListStrings[i] = Integer.toString(cornerList.get(i).getOwner().getMyID()) + cornerList.get(i).getType();
		}


		StringArray cornerListVisual = lang.newStringArray(new Offset(0, -nameYOffset * 2, "nextConsideredListName", "SW"),
				cornerListStrings, "cornerList",
				null, arrayProps);
		lang.newText(new Offset(0, -nameYOffset/2, "cornerList", "SW"),
				translator.translateMessage("cornerList"), "cornerListName", null);

		sc.highlight(4);
		sc.unhighlight(3);
		lang.nextStep();

		Collections.sort(cornerList);


		for (int i = 0; i < cornerListStrings.length; i++){
			cornerListStrings[i] = Integer.toString(cornerList.get(i).getOwner().getMyID()) + cornerList.get(i).getType();
			cornerListVisual.put(i, cornerListStrings[i], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}

		sc.highlight(5);
		sc.unhighlight(4);
		lang.nextStep();


		sc.highlight(6);
		sc.unhighlight(5);
		lang.nextStep();

		int activeCounter = 0;
		int nexConsideredCounter = 0;
		int possibleCollisionsCounter = 0;

		ArrayMarkerProperties arrayPMProps = new ArrayMarkerProperties();
		arrayPMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "corner");
		arrayPMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		arrayPMProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);

		ArrayMarker cornerMarker = lang.newArrayMarker(cornerListVisual, 0,
				"cornerMarker", null, arrayPMProps);

		int j = 0;

		for (CornerPoint cp : cornerList) {

			cornerMarker.move(cornerList.indexOf(cp), null, defaultDuration);

			for (int k = 0; k < cornerListVisual.getLength(); k++) {
				if (k != j) {
					cornerListVisual.unhighlightElem(k, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				}
			}
			cornerListVisual.highlightElem(j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			j++;

			sc.highlight(7);
			sc.unhighlight(6);
			sc.unhighlight(11);
			lang.nextStep();
			if (cp.getType().equals(MinMax.MIN)) {

				sc.highlight(8);
				sc.unhighlight(7);
				lang.nextStep();

				int m = 0;



				for (AABB active : activeList) {
					activeListVisual.getData(m);
					activeMarker.show();
					activeMarker.move(activeList.indexOf(active), null, defaultDuration);

					counter.deactivateCounting();
					for (int l = 0; l < activeList.size(); l++) {
						if (l != m)
							activeListVisual.unhighlightElem(l, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					}
					activeListVisual.highlightElem(m, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					counter.activateCounting();
					m++;

					lang.nextStep();



					int randomNumberForQuestion = r.nextInt(100);

					if (randomNumberForQuestion < collisionQuestionProbability) {

						MultipleChoiceQuestionModel collisionQuestion = new MultipleChoiceQuestionModel("coll");
						collisionQuestion.setPrompt(translator.translateMessage("collisionQuestion"));
						collisionQuestion.addAnswer(cp.getOwner().getMyID() + translator.translateMessage("and") + active.getMyID() 
								+ translator.translateMessage("collisionAnswer1"), 1, translator.translateMessage("correct"));
						collisionQuestion.addAnswer(translator.translateMessage("collisionAnswer2a") + cp.getOwner().getMyID() 
								+ translator.translateMessage("and") + active.getMyID() + translator.translateMessage("collisionAnswer2b"), 0, 
								translator.translateMessage("collisionResult2"));
						collisionQuestion.addAnswer(cp.getOwner().getMyID() + translator.translateMessage("and") + active.getMyID() 
								+ translator.translateMessage("collisionAnswer3"), 0,
								translator.translateMessage("collisionResult3"));
						collisionQuestion.setGroupID("collision");

						lang.addMCQuestion(collisionQuestion);
					}

					if (!nextConsideredList.contains(active)) {
						nextConsideredList.add(active);
						nextConsideredListVisual.put(nexConsideredCounter, Integer.toString(active.getMyID()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						nexConsideredCounter++;
					}
					if (!nextConsideredList.contains(cp.getOwner())) {
						nextConsideredList.add(cp.getOwner());
						nextConsideredListVisual.put(nexConsideredCounter, Integer.toString(cp.getOwner().getMyID()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						nexConsideredCounter++;
					}

					sc.highlight(9);
					sc.unhighlight(8);
					lang.nextStep();   



					AABBPair possibleCollision = new AABBPair();
					possibleCollision.first = active;
					possibleCollision.second = cp.getOwner();
					if (xAxisFlag) {
						possibleCollisionsX.add(possibleCollision);
					} else  {
						possibleCollisionsY.add(possibleCollision);
					}

					if (xAxisFlag) {
						possibleCollisionListsX.put(possibleCollisionsCounter, 
								Integer.toString(active.getMyID()) + ":" + Integer.toString(cp.getOwner().getMyID()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					} else {
						possibleCollisionListY.put(possibleCollisionsCounter, 
								Integer.toString(active.getMyID()) + ":" + Integer.toString(cp.getOwner().getMyID()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					}
					possibleCollisionsCounter++;


					activeMarker.hide();

					sc.highlight(10);
					sc.unhighlight(9);
					lang.nextStep();
				}
				activeListVisual.unhighlightElem(m-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				activeList.add(cp.getOwner());

				activeListVisual.put(activeCounter, Integer.toString(cp.getOwner().getMyID()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				activeCounter++;

				sc.highlight(11);
				sc.unhighlight(10);
				sc.unhighlight(8);
				lang.nextStep();
			}

			sc.unhighlight(7);


			if (cp.getType().equals(MinMax.MAX)) {
				activeList.remove(activeList.indexOf(cp.getOwner()));
				counter.deactivateCounting();
				for (int i = 0; i < activeList.size(); i++) {
					activeListVisual.put(i, Integer.toString(activeList.get(i).getMyID()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				}
				for (int i = activeList.size(); i < activeListVisual.getLength(); i++) {
					activeListVisual.put(i, "  ", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				}
				counter.activateCounting();
				activeCounter--;
				sc.highlight(12);
				lang.nextStep();
			}
			sc.unhighlight(12);
		}

		activeListVisual.hide();

		for (int i = 0; i < nextConsideredList.size(); i++) {
			consideredList2.put(i, Integer.toString(nextConsideredList.get(i).getMyID()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}
		for (int i = nextConsideredList.size(); i < activeListVisual.getLength(); i++) {
			consideredList2.put(i, " ", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}

		sc.highlight(13);

		lang.nextStep();
		sc.unhighlight(13);
		nextConsideredListVisual.hide();
		activeListVisual.hide();
		cornerListVisual.hide();


		if (xAxisFlag) {	

			int randomNumberForQuestion = r.nextInt(100);

			if (randomNumberForQuestion < nextAxisQuestionProbability) {

				//get the list of uncosnidered objects
				List<AABB> unconsidered = new ArrayList<AABB>();
				for (int i = 0; i < consideredList.size(); i++) {
					if (!nextConsideredList.contains(consideredList.get(i))){
						unconsidered.add(consideredList.get(i));
					}
				}

				//create string out them
				if (unconsidered.size() > 0) {
					String unconsideredString = "";
					for (int i = 0; i < unconsidered.size() - 1; i++) {
						unconsideredString += " " + (unconsidered.get(i).getMyID());
					}
					if (unconsidered.size() > 1) {
						unconsideredString += translator.translateMessage("and");
					}
					unconsideredString += unconsidered.get(unconsidered.size()-1).getMyID();

					MultipleChoiceQuestionModel unconsideredQuestion = new MultipleChoiceQuestionModel("unconsidered");
					unconsideredQuestion.setPrompt(translator.translateMessage("whyunconsidered1")
							+ unconsideredString + translator.translateMessage("whyunconsidered2"));
					unconsideredQuestion.addAnswer(translator.translateMessage("unconsideredAnswer1"),
							0, translator.translateMessage("unconsideredResult1"));
					unconsideredQuestion.addAnswer(translator.translateMessage("unconsideredAnswer2"), 1, 
							translator.translateMessage("correct"));
					unconsideredQuestion.addAnswer(translator.translateMessage("unconsideredAnswer3"), 0,
							translator.translateMessage("unconsideredResult3"));

					lang.addMCQuestion(unconsideredQuestion);
				}
			}

			sweepAndPrune(nextConsideredList, false, sc, xAxis, yAxis,
					possibleCollisionListsX, possibleCollisionListY, consideredList2, activeListVisual, counter);
		}
		yAxis.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}


}