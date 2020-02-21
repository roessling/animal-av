/*
 * PotentialField.java
 * Jasper Suess, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.datastructures;

import generators.datastructures.Cell.Type;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;
import algoanim.animalscript.AnimalScript;

public class PotentialField implements ValidatingGenerator {
	private Language lang;
	private int[] startingPoint;
	private double a;
	private double b;
	private int[] goalPoint;
	private int[][] obstacles;
	private double b0;
	private CellGrid cells;
	private SourceCode sc;
	private Color obstacleColor;
	private Color goalColor;
	private Color startColor;
	private Color pathColor;
	private Color highlight1;
	private Color highlight2;
	private Coordinates base;
	private Coordinates paramBase;
	private Locale language;
	private Translator translator;
	private boolean colorCoded;
	private boolean fastMode;

	public PotentialField(Locale language){
		this.language = language;
		translator = new Translator("resources/PotentialField" , language ) ;
	}

	public static void main(String[] args) {
		Generator generator = new PotentialField(Locale.US); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}

	public void init(){
		lang = new AnimalScript("Potential Field", "Jasper Suess", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		startingPoint = (int[])primitives.get("startingPoint");
		a = (double)primitives.get("a");
		b = (double)primitives.get("b");
		goalPoint = (int[])primitives.get("goalPoint");
		obstacles = (int[][])primitives.get("obstacles");
		b0 = (double)primitives.get("b0");
		colorCoded = (boolean)primitives.get("grayscaling");
		fastMode = (boolean)primitives.get("fastMode");


		obstacleColor = (Color) ((SquareProperties)props.getPropertiesByName("obstacleColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
		goalColor = (Color) ((SquareProperties)props.getPropertiesByName("goalColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
		startColor = (Color) ((SquareProperties)props.getPropertiesByName("startColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
		pathColor = (Color) ((SquareProperties)props.getPropertiesByName("pathColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
		highlight1 = (Color) ((SquareProperties)props.getPropertiesByName("sourceCodeHighlightColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
		highlight2 = (Color) ((SquareProperties)props.getPropertiesByName("cellHighlightColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);


		cells = new CellGrid();
		base = new Coordinates(600, 40);
		paramBase = new Coordinates(50, 330);


		generateDescriptionStart();
		lang.nextStep();
		lang.hideAllPrimitives();
		generateStaticElements();
		generateSourceCode();
		runPF();
		generateDescriptionEnd();
		return lang.toString();
	}

	public void generateStaticElements() {
		SquareProperties sp = new SquareProperties();
		sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 18));



		Square obstacleLegendSq = lang.newSquare(new Offset(429, 39, base, null), 40, "obstacleLegend", null, sp);
		obstacleLegendSq.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, obstacleColor, null, null);
		Text obstacleT = lang.newText(new Offset(479, 46, base, AnimalScript.DIRECTION_C), translator.translateMessage("obstacleDescription"), "obstacleT", null, tp);

		Square goalLegendSq = lang.newSquare(new Offset(429, 39*3, base, null), 40, "goalLegend", null, sp);
		goalLegendSq.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColor, null, null);
		Text goalT = lang.newText(new Offset(479, 46 + 39*2, base, AnimalScript.DIRECTION_C), translator.translateMessage("goalDescription"), "obstacleT", null, tp);

		Square startLegendSq = lang.newSquare(new Offset(429, 39*5, base, null), 40, "startLegend", null, sp);
		startLegendSq.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, startColor, null, null);
		Text startT = lang.newText(new Offset(479, 46 + 39*4, base, AnimalScript.DIRECTION_C), translator.translateMessage("startDescription"), "obstacleT", null, tp);

		Square normalLegendSq = lang.newSquare(new Offset(429, 39*7, base, null), 40, "normalLegend", null, sp);
		normalLegendSq.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.WHITE, null, null);
		Text normalT = lang.newText(new Offset(479, 46 + 39*6, base, AnimalScript.DIRECTION_C), translator.translateMessage("normalDescription"), "obstacleT", null, tp);

		Square pathLegendSq = lang.newSquare(new Offset(429, 39*9, base, null), 40, "pathLegend", null, sp);
		pathLegendSq.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, pathColor, null, null);
		Text pathT = lang.newText(new Offset(479, 46 + 39*8, base, AnimalScript.DIRECTION_C), translator.translateMessage("pathDescription"), "obstacleT", null, tp);


		TextProperties tpBold = new TextProperties();
		tpBold.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 18));
		Text alphaT = lang.newText(new Offset(0, 0, paramBase, AnimalScript.DIRECTION_C), "a = " + a, "alphaT", null, tpBold);
		Text betaT = lang.newText(new Offset(190, 0, paramBase, AnimalScript.DIRECTION_C), "b = " + b, "betaT", null, tpBold);
		Text beta0T = lang.newText(new Offset(340, 0, paramBase, AnimalScript.DIRECTION_C), "b0 = " + b0, "beta0T", null, tpBold);
	}    	

	public void generateSourceCode() {

		TextProperties tpHeader = new TextProperties();
		tpHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(25, 10), "Potential Field", "header", null, tpHeader);

		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED,
				Font.PLAIN, 16));
		scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlight1);
		scp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);

		sc = lang.newSourceCode(new Coordinates(40, 50), "sourceCode", null, scp);
		int i = 1;

		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 0, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 0, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 1, null);
		sc.addCodeLine(translator.translateMessage("sc" + i), "sdc" + i++, 2, null);
	}

	public void generateDescriptionStart() {
		lang.nextStep(translator.translateMessage("description"));
		TextProperties tpHeader = new TextProperties();
		tpHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF,Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(25, 10), "Potential Field", "header", null, tpHeader);

		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF, Font.ROMAN_BASELINE, 16));

		SourceCode sdc = lang.newSourceCode(new Coordinates(40, 40), "sourceCode", null, scp);
		for(int i = 1; i < 17; i++) {
			sdc.addCodeLine(translator.translateMessage("ds" + i), "sdc" + i, 0, null);
		}
	}

	public void generateDescriptionEnd() {
		lang.hideAllPrimitives();
		lang.nextStep(translator.translateMessage("summary"));
		TextProperties tpHeader = new TextProperties();
		tpHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF,Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(25, 10), "Potential Field", "header", null, tpHeader);

		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF, Font.ROMAN_BASELINE, 16));

		SourceCode sdc = lang.newSourceCode(new Coordinates(40, 40), "sourceCode", null, scp);
		for(int i = 1; i < 25; i++) {
			sdc.addCodeLine(translator.translateMessage("de" + i), "sdc" + i, 0, null);
		}
	}

	public void runPF() {
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 16));
		SquareProperties sp = new SquareProperties();
		sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		sp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		sp.set(AnimalScript.COLORCHANGE_COLOR, Color.BLACK);
		for(int x = 0; x < 10; x++) {
			for(int y = 0; y < 10; y++) {
				if(x == goalPoint[0] && y == goalPoint[1])
					cells.add(x, y, new Cell(x, y, 0, lang.newSquare(new Offset(0 + 39*x, 351 - 39*y, base, null), 40, "grid_" + x + "_" + y, null, sp), Type.GOAL));
				else if(x == startingPoint[0] && y == startingPoint[1])
					cells.add(x, y, new Cell(x, y, 0, lang.newSquare(new Offset(0 + 39*x, 351 - 39*y, base, null), 40, "grid_" + x + "_" + y, null, sp), Type.START));
				else if(isObstacle(x, y))
					cells.add(x, y, new Cell(x, y, Integer.MAX_VALUE, lang.newSquare(new Offset(0 + 39*x, 351 - 39*y, base, null), 40, "grid_" + x + "_" + y, null, sp), Type.OBSTACLE));
				else
					cells.add(x, y, new Cell(x, y, 0, lang.newSquare(new Offset(0 + 39*x, 351 - 39*y, base, null), 40, "grid_" + x + "_" + y, null, sp), Type.NORMAL));

			}
		}

		Cell goal = cells.getSingleType(Type.GOAL);
		goal.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColor, null, null);
		goal.color = goalColor;

		Cell start = cells.getSingleType(Type.START);
		start.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, startColor, null, null);
		start.color = startColor;

		for(Cell c : cells.getMultipleType(Type.OBSTACLE)) {
			c.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, obstacleColor, null, null);
			c.color = obstacleColor;
		}

		for(Cell c : cells.getMultipleType(Type.NORMAL)) {
			c.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.WHITE, null, null);
			c.color = Color.WHITE;
		}

		TextProperties tpBold = new TextProperties();
		tpBold.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 18));

		lang.nextStep();
		boolean fastModeStop = false;
		for(int y = 0; y < 10 && !fastModeStop; y++) {
			for(int x = 0; x < 10; x++) {
				if(cells.get(x, y).type != Type.OBSTACLE) {
					sc.highlight(0);
					cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, highlight2, null, null);


					lang.nextStep(translator.translateMessage("calculation") + " (" + x + "," + y + ")");


					sc.toggleHighlight(0, 1);
					double uObstacle = 0;

					Cell closestObstacle = findClosestObstacle(x, y);
					double distanceObstacle = Math.sqrt(Math.pow(closestObstacle.x - x, 2) + Math.pow(closestObstacle.y - y, 2));
					distanceObstacle = (double)((int)(distanceObstacle*100)/100.0);
					PolylineProperties pp = new PolylineProperties();
					pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
					pp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
					Polyline arrow = lang.newPolyline(new Node[]{new Offset(39*x+19, 351 - 39*y+19, base, null), new Offset(closestObstacle.x*39 + 19, 351 - closestObstacle.y*39 + 19, base, null)}, "arrow", null, pp);
					Text distanceT = lang.newText(new Offset(0, 40, paramBase, AnimalScript.DIRECTION_C), translator.translateMessage("distance") + " = " + distanceObstacle, "distanceT", null, tpBold);
					distanceT.changeColor(AnimalScript.COLORCHANGE_COLOR, highlight1, null, null);


					lang.nextStep();


					arrow.hide();
					distanceT.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
					sc.toggleHighlight(1, 2);
					if(closestObstacle != null)
						uObstacle = b / (b0 + distanceObstacle);
					uObstacle = (double)((int)(uObstacle*100)/100.0);

					Text uObstacleT = lang.newText(new Offset(0, 80, paramBase, AnimalScript.DIRECTION_C), translator.translateMessage("uobstacle") + " = " + uObstacle, "uObstacleT", null, tpBold);
					uObstacleT.changeColor(AnimalScript.COLORCHANGE_COLOR, highlight1, null, null);


					lang.nextStep();


					distanceT.hide();
					uObstacleT.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
					sc.toggleHighlight(2,3);
					double distanceGoal = Math.sqrt(Math.pow(goalPoint[0] - x, 2) + Math.pow(goalPoint[1] - y, 2));
					distanceGoal = (double)((int)(distanceGoal*100)/100.0);

					arrow = lang.newPolyline(new Node[]{new Offset(39*x+19, 351 - 39*y+19, base, null), new Offset(goalPoint[0]*39 + 19, 351 - goalPoint[1]*39 + 19, base, null)}, "arrow", null, pp);
					distanceT = lang.newText(new Offset(0, 40, paramBase, AnimalScript.DIRECTION_C), translator.translateMessage("distance") + " = " + distanceGoal, "distanceT", null, tpBold);
					distanceT.changeColor(AnimalScript.COLORCHANGE_COLOR, highlight1, null, null);


					lang.nextStep();


					arrow.hide();
					sc.toggleHighlight(3, 4);
					distanceT.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);

					double uGoal = a * distanceGoal;
					uGoal = (double)((int)(uGoal*100)/100.0);

					Text uGoalT = lang.newText(new Offset(190, 80, paramBase, AnimalScript.DIRECTION_C),translator.translateMessage("ugoal") + " = " + uGoal, "uGoalT", null, tpBold);
					uGoalT.changeColor(AnimalScript.COLORCHANGE_COLOR, highlight1, null, null);


					lang.nextStep();


					uGoalT.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
					sc.toggleHighlight(4, 5);

					int potential = (int) Math.round(uGoal + uObstacle);
					int xco;
					if(potential >= 1000)
						xco = 1;
					else if(potential >= 100) 
						xco = 7;
					else 
						xco = 12;
					Text uT = lang.newText(new Offset(340, 80, paramBase, AnimalScript.DIRECTION_C), "U = " + potential, "uT", null, tpBold);
					uT.changeColor(AnimalScript.COLORCHANGE_COLOR, highlight1, null, null);

					lang.nextStep();

					Text p = lang.newText(new Offset(xco + 39*x, 360 - 39*y, base, AnimalScript.DIRECTION_C), "" + potential, "p_" + x + "_" + y, null, tp);

					uT.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
					sc.toggleHighlight(5, 6);

					cells.get(x, y).setPotential(potential);


					lang.nextStep();


					uGoalT.hide();
					uObstacleT.hide();
					uT.hide();
					distanceT.hide();
					sc.unhighlight(6);
					if(cells.get(x, y).type == Type.NORMAL)
						if(colorCoded) {
							int max = (int) (12.7 * a + b/(b0+1));
							int min = (int) (b/(b0+12.7));
							int brightness = 255 - (int)(255*(potential - min) / (max - min));
							Color c = new Color(brightness, brightness, brightness);

							cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, c, null, null);
							cells.get(x, y).color = c;

						} else
							cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.WHITE, null, null);
					else if(cells.get(x, y).type == Type.GOAL)
						cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColor, null, null);
					else if(cells.get(x, y).type == Type.START)
						cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, startColor, null, null);

					if(fastMode && x == 4) {
						fastModeStop = true;
						fillAllCells();
						break;
					}



				}
			}
		}

		sc.highlight(7);


		lang.nextStep(translator.translateMessage("pathFinding"));



		Cell current = cells.get(startingPoint[0], startingPoint[1]);
		Cell former = new Cell(-1, -1, 0, null, null);
		Cell goa = cells.get(goalPoint[0], goalPoint[1]);
		int min = Integer.MAX_VALUE;
		Cell minCell = null;
		start.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, pathColor, null, null);
		while(!current.equals(goa) && current != former) {
			sc.toggleHighlight(9, 8);
			sc.toggleHighlight(7, 8);
			int s = 100;
			int x = current.x-1;
			int y = current.y+1;
			int c = 0;
			for(int i = 0; i < 8; i++) {
				if(x < 10 && y < 10 && x >= 0 && y >= 0 && (former.x != x || former.y != y) && cells.get(x, y).type != Type.OBSTACLE) {
					cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, highlight2, new MsTiming(c*s), null);
					if(cells.get(x, y).type == Type.GOAL)
						cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColor, new MsTiming(c*s+s), null);
					else 
						cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, cells.get(x, y).color, new MsTiming(c*s+s), null);
					c++;
				}

				if(i < 2)
					x++;
				else if(i < 4)
					y--;
				else if(i < 6)
					x--;
				else if(i < 8)
					y++;				
			}

			for(int i = current.x-1; i <= current.x + 1; i++) {
				for(int j = current.y-1; j <= current.y + 1; j++) {
					if(i >= 0 && i < 10 && j >= 0 && j < 10 && (i != current.x || j != current.y)) {    					
						if(cells.get(i, j).getPotential() < min) {
							min = cells.get(i, j).getPotential();
							minCell = cells.get(i, j);
						}	
					}

				}
			}
			former = current;
			current = minCell;
			minCell.type = Type.PATH;
			minCell.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, pathColor, new MsTiming(c*s + 200), null);
			minCell.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, minCell.color, new MsTiming(c*s + 600), null);
			minCell.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, pathColor, new MsTiming(c*s+1000), null);
			minCell.color = pathColor;

			lang.nextStep();


			sc.toggleHighlight(8,9);


			lang.nextStep();
		}

		start.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, startColor, null, null);
		if(current == former) {
			sc.toggleHighlight(9, 10);
			lang.nextStep(translator.translateMessage("negativeEnd"));
		}else {
			goal.square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColor, null, null);
			lang.nextStep(translator.translateMessage("positiveEnd"));
		}
	}

	public Cell findClosestObstacle(int x, int y) {
		double minDistance = Double.MAX_VALUE;
		Cell closest = null;
		for(int i = 0; i < obstacles.length; i++) {
			Cell o = new Cell(obstacles[i][0], obstacles[i][1], 0, null, null);
			double distance = Math.sqrt(Math.pow(o.x - x, 2) + Math.pow(o.y - y, 2));
			if(distance < minDistance) {
				minDistance = distance;
				closest = o;
			}
		}
		return closest;
	}

	public boolean isObstacle(int x, int y) {
		for(int[] coords : obstacles) 
			if(coords[0] == x && coords[1] == y)
				return true;
		return false;
	}

	public void fillAllCells() {
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				if(cells.get(x, y).type != Type.OBSTACLE) {
					double uObstacle = 0;

					Cell closestObstacle = findClosestObstacle(x, y);
					double distanceObstacle = Math.sqrt(Math.pow(closestObstacle.x - x, 2) + Math.pow(closestObstacle.y - y, 2));
					distanceObstacle = (double)((int)(distanceObstacle*100)/100.0);

					if(closestObstacle != null)
						uObstacle = b / (b0 + distanceObstacle);
					uObstacle = (double)((int)(uObstacle*100)/100.0);

					double distanceGoal = Math.sqrt(Math.pow(goalPoint[0] - x, 2) + Math.pow(goalPoint[1] - y, 2));
					distanceGoal = (double)((int)(distanceGoal*100)/100.0);

					double uGoal = a * distanceGoal;
					uGoal = (double)((int)(uGoal*100)/100.0);

					int potential = (int) Math.round(uGoal + uObstacle);
					int xco;
					if(potential >= 1000)
						xco = 1;
					else if(potential >= 100) 
						xco = 7;
					else 
						xco = 12;

					TextProperties tp = new TextProperties();
					tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 16));
					Text p = lang.newText(new Offset(xco + 39*x, 360 - 39*y, base, AnimalScript.DIRECTION_C), "" + potential, "p_" + x + "_" + y, null, tp);

					cells.get(x, y).setPotential(potential);

					if(cells.get(x, y).type == Type.NORMAL)
						if(colorCoded) {
							int max = (int) (12.7 * a + b/(b0+1));
							int min = (int) (b/(b0+12.7));
							int brightness = 255 - (int)(255*(potential - min) / (max - min));
							Color c = new Color(brightness, brightness, brightness);

							cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, c, null, null);
							cells.get(x, y).color = c;

						} else
							cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, Color.WHITE, null, null);
					else if(cells.get(x, y).type == Type.GOAL)
						cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, goalColor, null, null);
					else if(cells.get(x, y).type == Type.START)
						cells.get(x, y).square.changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, startColor, null, null);

				}
			}

		}
	}

	public String getName() {
		return "Potential Field";
	}

	public String getAlgorithmName() {
		return "Potential Field";
	}

	public String getAnimationAuthor() {
		return "Jasper Suess";
	}

	public String getDescription(){
		String ret = "";

		for(int i = 1; i < 17; i++) {
			ret = ret + translator.translateMessage("ds" + i) + "\n";
		}
		return ret;
	}

	public String getCodeExample(){
		String ret = "";
		for(int i = 1; i < 12; i++) {
			ret = ret + translator.translateMessage("sc" + i) + "\n";
		}
		return ret;
	}

	public String getFileExtension(){
		return "asu";
	}

	public Locale getContentLocale() {
		return language;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	public boolean validPoint(int x) {
		return x >= 0 && x < 10;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		startingPoint = (int[])primitives.get("startingPoint");
		a = (double)primitives.get("a");
		b = (double)primitives.get("b");
		goalPoint = (int[])primitives.get("goalPoint");
		obstacles = (int[][])primitives.get("obstacles");
		b0 = (double)primitives.get("b0");


		if(startingPoint.length != 2 || !validPoint(startingPoint[0]) || !validPoint(startingPoint[1]))
			return false;
		System.out.println("start");
		if(goalPoint.length != 2 || !validPoint(goalPoint[0]) || !validPoint(goalPoint[1]))
			return false;
		System.out.println("goal");
		if(startingPoint[0] == goalPoint[0] && startingPoint[1] == goalPoint[1])
			return false;
		
		if(a < 0 || b < 0 || b0 < 0)
			return false;

		for(int[] o : obstacles)
			if(o.length != 2 || !validPoint(o[0]) || !validPoint(o[1]) || (o[0] == goalPoint[0] && o[1] == goalPoint[1]) || (o[0] == startingPoint[0] && o[1] == startingPoint[1]))
				return false;
			
		return true;
		
	}

}