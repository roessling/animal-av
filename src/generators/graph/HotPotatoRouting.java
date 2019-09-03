/*
 * HotPotatoRouting.java
 * , 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.Hashtable;
import java.util.LinkedList;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class HotPotatoRouting implements ValidatingGenerator {
	private static Language lang;
	private static Graph graph;
	private static int seed;
	private static int[][] properties; // 0 = CurrentNode, 1 = Destination,
										// 2 = oldNode, 3 = StartTime, 4 = Hop
										// Count,5 = next Neighbor
	private static int[][] input; // 0 = StartNode, 1 = Destination, 2 =
									// StartTime
	private static Circle[] potatoes;

	public HotPotatoRouting() {
		lang = new AnimalScript("Hot Potato Routing", "", 1440, 900);
		lang.setStepMode(true);
	}

	public void init() {
		lang = new AnimalScript("Hot Potato Routing", "", 1440, 900);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		graph = (Graph) primitives.get("graph");
		input = (int[][]) primitives.get("Kartoffel/n");
		seed = (int) primitives.get("seed");
				
		properties = new int[input.length][6];
		potatoes = new Circle[input.length];
		int offsetHelp = -1;
		for (int i = 0; i < input.length; i++) {
			properties[i][0] = input[i][0];
			properties[i][1] = input[i][1];
			properties[i][2] = -1;
			properties[i][3] = input[i][2];
			properties[i][4] = 0;
			properties[i][5] = -1;
			CircleProperties cP = new CircleProperties();
			cP.set("filled", true);
			cP.set("fillColor",
					new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
			potatoes[i] = lang.newCircle(graph.getNodeForIndex(properties[i][0]), 7, "Potatoe " + i, null, cP);
			// offset and hide
			if (i % 5 == 0) {
				offsetHelp++;
			}
			if (offsetHelp == 5) {
				offsetHelp = 0;
			}
			potatoes[i].moveBy(null,
					((Coordinates) graph.getNode(properties[i][0])).getX() + i % 5 * 3
							- ((Coordinates) graph.getNode(properties[i][0])).getX(),
					((Coordinates) graph.getNode(properties[i][0])).getY() + offsetHelp * 3
							- ((Coordinates) graph.getNode(properties[i][0])).getY(),
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			potatoes[i].hide();
		}
		String[] nodeLabels = new String[graph.getSize()];
		for (int i = 0; i < graph.getSize(); i++) {
			nodeLabels[i] = graph.getNodeLabel(i);
		}
		GraphProperties graphProps = (GraphProperties) props.getPropertiesByName("graphProps");
		graph = lang.newGraph(graph.getName(), graph.getAdjacencyMatrix(), graph.getNodes(), nodeLabels,
				graph.getDisplayOptions(), graphProps);
		graph.hide();

		HotPotatoRoutingAlgo(graph, properties, potatoes, seed);

		return lang.toString();
	}

	private void HotPotatoRoutingAlgo(Graph graph, int[][] properties, Circle[] potatoes, int seed) {
		Random generator = new Random(seed);
		int yPositionForHopCount = 265;
		Text[] hopTexts = new Text[input.length];
		Circle[] hopCircles = new Circle[input.length];

		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(20, 30), "Hot Potato Routing", "header", null, headerProps);
		lang.nextStep();
		// Descriptopn
		SourceCodeProperties descriptionProps = new SourceCodeProperties();
		descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		SourceCode description = lang.newSourceCode(new Coordinates(10, 70), "Description", null, descriptionProps);
		description.addCodeLine("Hot Potato Routing ist ein simpler Routing-Algorithmus. ", null, 0, null);
		lang.nextStep();
		description.addCodeLine(
				"Bei diesem Algorithmus werden die ankommenden Pakete nicht an den Nodes gehalten,",
				null, 0, null);
		description.addCodeLine(
				"sondern versucht so schnell wie moeglich weiterzuleiten, deshalb auch der Name Hot Potato Routing. ",
				null, 0, null);
		lang.nextStep();
		description.addCodeLine(
				"Deshalb wird anstatt eine ideale Route zu berechnen fuer jedes Paket ein zufaelliger Nachbar ausgesucht,",
				null, 0, null);
		description.addCodeLine(
				"welcher im Schritt davor nicht der sendende Knoten war.",
				null, 0, null);
		lang.nextStep();
		description.addCodeLine(
				"Um nicht in eine Sackgasse zu gelangen wird das Paket zum Sender zurueckgesendet, sofern kein anderer Nachbar vorhanden ist.",
				null, 0, null);
		lang.nextStep();
		description.addCodeLine(
				"Allerdings verhindert dies nicht die Moeglichkeit, dass das Paket in eine Schleife zu geraten kann.",
				null, 0, null);
		lang.nextStep();
		description.addCodeLine(
				"Dieser Prozess wird solange wiederholt, bis das Paket sein Ziel erreicht hat.",
				null, 0, null);
		lang.nextStep();
		description.addCodeLine(
				"Der Algorithmus sorgt fuer eine optimale Leitungsauslastung, allerdings kann es durch das zufaellige Auswaehlen sehr lange dauern,",
				null, 0, null);
		description.addCodeLine(
				"bis das Paket sein Ziel erreicht.",
				null, 0, null);
		lang.nextStep();
		description.addCodeLine("Hot Potato Routing wird heute aber immer noch in spezielen Netzwerken benutzt.", null,
				0, null);
		lang.nextStep();
		description.hide();
		graph.show();

		SourceCodeProperties codeProps = new SourceCodeProperties();
		codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);

		SourceCode algorithmCode = lang.newSourceCode(new Coordinates(500, 50), "Source Code", null, codeProps);
		algorithmCode.addCodeLine(
				"public void hotPotatoAlgorithm(node comingFrom, node currentNode, node destination){", null, 0, null);
		algorithmCode.addCodeLine("int rngNeighbour;", null, 1, null);
		algorithmCode.addCodeLine("do{", null, 1, null);
		algorithmCode.addCodeLine("int rngNeighbour = (int)(Math.random()*currentNode.getNeighbourCount())", null, 2,
				null);
		algorithmCode.addCodeLine("} while(comingFrom == currentNode.getNeighbours()[rngNeighbour] && currentNode.getNeighbourCount() > 1)", null, 1, null);
		algorithmCode.addCodeLine("sendTo(currentNode.getNeighbours()[rngNeighbour];", null, 1, null);
		algorithmCode.addCodeLine("if(currentNode.getNeighbours[rngNeighbour] != Destination)", null, 1, null);
		algorithmCode.addCodeLine(
				"hotPotatoAlgorithm(currentNode, currentNode.getNeighbours()[rngNeighbour], destination);", null, 2,
				null);
		algorithmCode.addCodeLine("}", null, 0, null);

		lang.nextStep();

		TextProperties stepDescProps = new TextProperties();
		stepDescProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		Text stepDesc = lang.newText(new Coordinates(20, 370), "", "header", null, stepDescProps);
		Text stepDesc2 = lang.newText(new Coordinates(20, 390), "", "header", null, stepDescProps);
		
		for (int i = 0; i < potatoes.length; i++) {
			if (properties[i][3] == 0) {
				potatoes[i].show();
				hopCircles[i] = lang.newCircle(Node.convertToNode(new Point(500, yPositionForHopCount)), 7, "test",
						null, potatoes[i].getProperties());
				hopTexts[i] = lang
						.newText(new Coordinates(510, yPositionForHopCount - 13),
								"Start: " + (char) (65 + input[i][0]) + ", Ziel: " + (char) (65 + properties[i][1])
										+ ", Naechster Nachbar: " + "-" + ", HopCount: " + properties[i][4],
								"hopText", null, stepDescProps);
				yPositionForHopCount += 25;
			}
		}		

		boolean started = false;
		boolean finished = false;

		while (!finished) {
			for (int i = 0; i < algorithmCode.length(); i = i + 2) {
				lang.nextStep();
				if (i == 0) {
					stepDesc.setText("Methode wird fuer jedes Paket ausgefuehrt.", Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					stepDesc2.setText("", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					if (started) {
						algorithmCode.toggleHighlight(7, i);
					} else {
						algorithmCode.highlight(i);
						started = true;
					}

				} else if (i == 2) {
					stepDesc.setText("Fuer jedes Paket wird ein neuer zufaelliger Knoten",
							Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					stepDesc2.setText("gewaehlt, der nicht der vorherige Knoten ist.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					i++;
					algorithmCode.toggleHighlight(0, i);
				} else {
					algorithmCode.toggleHighlight(i - 2, i);
				}
				if (i == 3) {

					finished = true;
					for (int k = 0; k < potatoes.length; k++) {
						if (properties[k][0] != properties[k][1]) {
							if (properties[k][3] > 0) {
								properties[k][3]--;
							} else {
								do {
									properties[k][5] = generator.nextInt(getNeighbours(graph, properties[k][0]).size());
									properties[k][5] = getNeighbours(graph, properties[k][0]).get(properties[k][5]);
								} while (properties[k][5] == properties[k][2]
										&& getNeighbours(graph, properties[k][0]).size() > 1);
								hopTexts[k].setText(
										"Start: " + (char) (65 + input[k][0]) + ", Ziel: "
												+ (char) (65 + properties[k][1]) + ", Naechster Nachbar: "
												+ (char) (65 + properties[k][5]) + ", HopCount: " + properties[k][4],
										Timing.INSTANTEOUS, Timing.INSTANTEOUS);
							}
						}
					}
				} else if (i == 5) {
					stepDesc.setText("Jedes Paket wird an den vorher berechneten", Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					stepDesc2.setText("Nachbar gesendet.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					finished = true;
					for (int k = 0; k < potatoes.length; k++) {
						if (properties[k][5] == -1 && properties[k][3] <= 0) {
							potatoes[k].show();
							hopCircles[k] = lang.newCircle(Node.convertToNode(new Point(500, yPositionForHopCount)), 7,
									"test", null, potatoes[k].getProperties());
							hopTexts[k] = lang.newText(new Coordinates(510, yPositionForHopCount - 13),
									"Start: " + (char) (65 + input[k][0]) + ", Ziel: " + (char) (65 + properties[k][1])
											+ ", Naechster Nachbar: " + "-" + ", HopCount: " + properties[k][4],
									"hopText", null, stepDescProps);
							yPositionForHopCount += 25;
							finished = false;
						} else if (properties[k][0] != properties[k][1] && properties[k][3] <= 0) {
							properties[k][2] = properties[k][0];
							properties[k][0] = properties[k][5];
							potatoes[k].moveBy(null,
									((Coordinates) graph.getNode(properties[k][0])).getX()
											- ((Coordinates) graph.getNode(properties[k][2])).getX(),
									((Coordinates) graph.getNode(properties[k][0])).getY()
											- ((Coordinates) graph.getNode(properties[k][2])).getY(),
									Timing.INSTANTEOUS, Timing.MEDIUM);
							properties[k][4]++;
							hopTexts[k].setText("Start: " + (char) (65 + input[k][0]) + ", Ziel: "
									+ (char) (65 + properties[k][1]) + ", Naechster Nachbar: "
									+ (char) (65 + properties[k][5]) + ", HopCount: " + properties[k][4],
									Timing.INSTANTEOUS, Timing.INSTANTEOUS);

						}
					}
				} else if (i == 7) {
					stepDesc.setText("Fuer alle Pakete die ihr Ziel noch nicht ereicht", Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					stepDesc2.setText("haben, wird die Methode rekursiv aufgerufen.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				}
				for (int j = 0; j < potatoes.length; j++) {
					if (properties[j][0] != properties[j][1]) {
						finished = false;
					}
				}
				if (finished) {
					algorithmCode.unhighlight(5);
					algorithmCode.highlight(8);
					break;
				}
			}
		}
		lang.nextStep();
		algorithmCode.hide();
		yPositionForHopCount = 50;
		for (int i = 0; i < hopCircles.length; i++) {
			hopCircles[i].moveTo(null, null, new Coordinates(493, yPositionForHopCount - 7), Timing.MEDIUM,
					Timing.MEDIUM);
			hopTexts[i].moveTo(null, null, new Coordinates(510, yPositionForHopCount - 13), Timing.MEDIUM,
					Timing.MEDIUM);
			yPositionForHopCount += 25;
		}
		lang.nextStep();
		TextProperties conclusionProps = new TextProperties();
		conclusionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		lang.newText(new Coordinates(493, yPositionForHopCount),
				"Da der Hot Potato Algorithmus keinen konkreten Pfad berechnet, sondern die Pakete an einen zufaelligen ",
				"Text", null, conclusionProps);
		lang.newText(new Coordinates(493, yPositionForHopCount + 25),
				"Nachbar schickt, kommt es oft vor, dass die Pakete einen laengeren, nicht optimalen Weg nehmen.",
				"Text", null, conclusionProps);
	}

	private LinkedList<Integer> getNeighbours(Graph graph, int node) {
		LinkedList<Integer> neighbours = new LinkedList<>();
		for (int i = 0; i < graph.getSize(); i++) {
			if (graph.getEdgesForNode(i)[node] == 1 || graph.getEdgesForNode(node)[i] == 1) {
				neighbours.add(i);
			}
		}
		return neighbours;
	}

	public String getName() {
		return "Hot Potato Routing";
	}

	public String getAlgorithmName() {
		return "Hot Potato Routing";
	}

	public String getAnimationAuthor() {
		return "Jessey Widhalm, Kevin Trometer";
	}

	public String getDescription() {
		return "Hot Potato Routing ist ein simpler Routing-Algorithmus. "
				+ "\nBei diesem Algorithmus werden die ankommenden Packages nicht an den Nodes gehalten, sondern sofort weitergeleitet. "
				+ "\nDabei ist es egal in welche Richtung, es wird ein Zuf�lliger Nachbar gew�hlt, solange dieser nicht der Sender war. "
				+ "\nEr ist nicht sehr effizient, da es durch das zuf�llige Ausw�hlen sehr lange dauern kann, bis das Package sein Ziel erreicht. "
				+ "\nHot Potato Routing wird heute aber immer noch in spezielen Netzwerken benutzt.  ";
	}

	public String getCodeExample() {
		return "public void hotPotatoAlgorithm(node comingFrom, node currentNode, node destination){"
				+ "\n      int rngNeighbour;" + "\n      do{"
				+ "\n            int rngNeighbour = (int)(Math.random()*currentNode.getNeighbourCount())"
				+ "\n      } while(comingFrom == currentNode.getNeighbours()[rngNeighbour])"
				+ "\n      sendTo(currentNode.getNeighbours()[rngNeighbour];"
				+ "\n      if(currentNode.getNeighbours[rngNeighbour] != Destination)"
				+ "\n            hotPotatoAlgorithm(currentNode, currentNode.getNeighbours()[rngNeighbour], destination);"
				+ "\n}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		graph = (Graph) primitives.get("graph");
		input = (int[][]) primitives.get("Kartoffel/n");

		if (input[0].length != 3)
			return false;

		for (int i = 0; i < input.length; i++) {
			if (input[i][0] == input[i][1] || input[i][0] > graph.getSize() || input[i][1] > graph.getSize())
				return false;
		}

		return true;
	}

}