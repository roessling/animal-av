/*
 * RittersBoundingSphereGenerator.java
 * Lukas Dietrich <lukasalexander.dietrich@stud.tu-darmstadt.de>, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
//import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.helperRittersBoundingSphere.RittersBoundingSphere;
import translator.Translator;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class RittersBoundingSphereGenerator implements ValidatingGenerator {

//	public static void main(String[] args) {
//		Animal.startGeneratorWindow(new RittersBoundingSphereGenerator(Locale.GERMANY));
//	}

	private Locale locale;
	private Translator strings;

	/**
	 * Construct a new generator for "Ritter's Bounding Spheres".
	 *
	 * Supported locales are
	 *   - Locale.US
	 *   - Locale.GERMANY
	 */
	public RittersBoundingSphereGenerator(Locale locale) {
		this.locale = locale;
		this.strings = new Translator("resources/rittersBoundingSphere", locale);
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

		int[][] points = (int[][]) primitives.get("points");

		if (points.length < 2) {
			JOptionPane.showMessageDialog(null, 
				strings.translateMessage("error.points.length"),
				strings.translateMessage("error"),
				JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		for (int[] point : points) {
			if (point.length != 2) {
				JOptionPane.showMessageDialog(null, 
					strings.translateMessage("error.points.syntax"),
					strings.translateMessage("error"),
					JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

			if (point[0] < 0 || point[0] > coordinateSystemWidth) {
				JOptionPane.showMessageDialog(null, 
					strings.translateMessage("error.points.coords"),
					strings.translateMessage("error"),
					JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

			if (point[1] < 0 || point[1] > coordinateSystemHeight) {
				JOptionPane.showMessageDialog(null, 
					strings.translateMessage("error.points.coords"),
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
		TextProperties stepProps = (TextProperties) props.getPropertiesByName("step");
		PolylineProperties candidateProps = (PolylineProperties) props.getPropertiesByName("distanceCandidate");
		PolylineProperties bestProps = (PolylineProperties) props.getPropertiesByName("distanceBest");

		int coordinateSystemHeight = (Integer) primitives.get("csHeight");
		int coordinateSystemWidth = (Integer) primitives.get("csWidth");
		int coordinateSystemScaling = (Integer) primitives.get("csScaling");

		int[][] points = (int[][]) primitives.get("points");

		double questionProbability = (double) primitives.get("questionProbability");

		RittersBoundingSphere rbs = new RittersBoundingSphere(
			lang, strings,
			questionProbability,
			fontSize,
			codeProps, textProps,
			stepProps,
			candidateProps, bestProps,
			coordinateSystemWidth, coordinateSystemHeight, coordinateSystemScaling);

		rbs.run(
			Arrays.stream(points)
				.map(arr -> new Point2D.Double(arr[0], arr[1]))
				.collect(Collectors.toList())
		);

		lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return "Ritter's bounding sphere";
	}

	public String getAlgorithmName() {
		return "Ritter's bounding sphere";
	}

	public String getAnimationAuthor() {
		return "Lukas Dietrich <lukasalexander.dietrich@stud.tu-darmstadt.de>";
	}

	public String getDescription() {
		return strings.translateMessage("description");
	}

	public String getCodeExample() {
		return RittersBoundingSphere.SOURCECODE;
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
