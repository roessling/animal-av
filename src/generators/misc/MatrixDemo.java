package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.util.Coordinates;

public class MatrixDemo implements Generator {

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		final Language l = new AnimalScript("Testbed", "Dominik Fischer", 800, 300);
		l.setStepMode(true);
		MatrixProperties p = new MatrixProperties();
		p.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		p.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		p.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		p.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		p.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);
		p.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 20);
		p.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 20);
		IntMatrix m = l.newIntMatrix(new Coordinates(150, 150), new int[][] {{1, 2}, {4, 5}}, "array", null, p);
		l.nextStep();
		m.highlightCell(0, 1, null, null);
		l.nextStep();
		m.unhighlightCell(0, 1, null, null);
		m.highlightElem(1, 0, null, null);
		return l.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Demo";
	}

	@Override
	public String getAnimationAuthor() {
		return "Dominik Fischer";
	}

	@Override
	public String getCodeExample() {
		return "m = {{1, 2}, {4, 5}}\n"
				+ "m.highlightCell(0, 1);\n"
				+ "m.highlightElem(1, 0);";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	@Override
	public String getDescription() {
		return "This generator demonstrates matrix highlight for cells and content.";
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getName() {
		return "Matrix Demo";
	}

	@Override
	public String getOutputLanguage() {
		return "Pseudocode";
	}

	@Override
	public void init() {
	}

}
