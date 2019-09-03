/*
 * RayCastingGenerator.java
 * Lukas Dietrich <lukasalexander.dietrich@stud.tu-darmstadt.de>, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.helperRayCasting.RayCasting;
import translator.Translator;

import javax.swing.JOptionPane;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.stream.Collectors;

//import animal.main.Animal;

public class RayCastingGenerator implements ValidatingGenerator {

//	public static void main(String[] args) {
//		Animal.startGeneratorWindow(new RayCastingGenerator(Locale.GERMANY));
//	}

	private Locale locale;
	private Translator strings;

	/**
	 * Construct a new generator for "Ray Casting (Point in Polygon)".
	 *
	 * Supported locales are
	 *   - Locale.US
	 *   - Locale.GERMANY
	 */
	public RayCastingGenerator(Locale locale) {
		this.locale = locale;
		this.strings = new Translator("resources/rayCasting", locale);
	}

	public void init() { }

	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		int fontSize = (Integer) primitives.get("fontSize");

		if (fontSize < 1) {
			JOptionPane.showMessageDialog(null, 
				strings.translateMessage("error.fontSize"),
				strings.translateMessage("error"),
				JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		int coordinateSystemScaling = (Integer) primitives.get("csScaling");

		if (coordinateSystemScaling < 1) {
			JOptionPane.showMessageDialog(null, 
				strings.translateMessage("error.scaling"),
				strings.translateMessage("error"),
				JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		int coordinateSystemHeight = (Integer) primitives.get("csHeight");

		if (coordinateSystemHeight < 10) {
			JOptionPane.showMessageDialog(null, 
				strings.translateMessage("error.height"),
				strings.translateMessage("error"),
				JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		int coordinateSystemWidth = (Integer) primitives.get("csWidth");

		if (coordinateSystemWidth < 10) {
			JOptionPane.showMessageDialog(null, 
				strings.translateMessage("error.width"),
				strings.translateMessage("error"),
				JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		int[] point = (int[]) primitives.get("point");

		if (point.length != 2) {
			JOptionPane.showMessageDialog(null, 
				strings.translateMessage("error.point.syntax"),
				strings.translateMessage("error"),
				JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (point[0] < 0 || point[0] > coordinateSystemWidth) {
			JOptionPane.showMessageDialog(null, 
					strings.translateMessage("error.point.coords"),
					strings.translateMessage("error"),
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (point[1] < 0 || point[1] > coordinateSystemHeight) {
			JOptionPane.showMessageDialog(null, 
					strings.translateMessage("error.point.coords"),
					strings.translateMessage("error"),
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		int[][] vertices = (int[][]) primitives.get("vertices");

		if (vertices.length < 3) {
			JOptionPane.showMessageDialog(null, 
				strings.translateMessage("error.vertices.length"),
				strings.translateMessage("error"),
				JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		for (int[] vertex : vertices) {
			if (vertex.length != 2) {
				JOptionPane.showMessageDialog(null, 
					strings.translateMessage("error.vertices.syntax"),
					strings.translateMessage("error"),
					JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

			if (vertex[0] < 0 || vertex[0] > coordinateSystemWidth) {
				JOptionPane.showMessageDialog(null, 
					strings.translateMessage("error.vertices.coords"),
					strings.translateMessage("error"),
					JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

			if (vertex[1] < 0 || vertex[1] > coordinateSystemHeight) {
				JOptionPane.showMessageDialog(null, 
					strings.translateMessage("error.vertices.coords"),
					strings.translateMessage("error"),
					JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}

		double questionProbability = (double) primitives.get("questionProbability");

		if (questionProbability < 0 || questionProbability > 1) {
			JOptionPane.showMessageDialog(null, 
				strings.translateMessage("error.probability"),
				strings.translateMessage("error"),
				JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		Language lang = new AnimalScript(
				getName(),
				getAnimationAuthor(),
				800, 600);

		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		int fontSize = (Integer) primitives.get("fontSize");

		SourceCodeProperties codeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		SourceCodeProperties textProps = (SourceCodeProperties) props.getPropertiesByName("text");

		PolygonProperties polygonProps = (PolygonProperties) props.getPropertiesByName("polygon");
		PolylineProperties rayProps = (PolylineProperties) props.getPropertiesByName("ray");
		PolylineProperties intersectionProps = (PolylineProperties) props.getPropertiesByName("intersection");
		PolylineProperties edgeProps = (PolylineProperties) props.getPropertiesByName("edge");

		int coordinateSystemHeight = (Integer) primitives.get("csHeight");
		int coordinateSystemWidth = (Integer) primitives.get("csWidth");
		int coordinateSystemScaling = (Integer) primitives.get("csScaling");

		int[][] vertices = (int[][]) primitives.get("vertices");
		int[] point = (int[]) primitives.get("point");

		double questionProbability = (double) primitives.get("questionProbability");

		RayCasting rc = new RayCasting(
			lang, strings,
			questionProbability,
			fontSize,
			codeProps, textProps,
			polygonProps, rayProps,
			edgeProps, intersectionProps,
			coordinateSystemWidth, coordinateSystemHeight, coordinateSystemScaling);

		rc.run(
			Arrays.stream(vertices)
				.map(arr -> new Point2D.Double(arr[0], arr[1]))
				.collect(Collectors.toList()),
			new Point2D.Double(point[0], point[1])
		);

		lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return "Ray Casting (Point in Polygon)";
	}

	public String getAlgorithmName() {
		return "Ray Casting (Point in Polygon)";
	}

	public String getAnimationAuthor() {
		return "Lukas Dietrich <lukasalexander.dietrich@stud.tu-darmstadt.de>";
	}

	public String getDescription() {
		return strings.translateMessage("description");
	}

	public String getCodeExample() {
		return RayCasting.SOURCECODE;
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}
