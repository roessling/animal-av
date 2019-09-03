package generators.compression.huffman.animators;

import generators.compression.huffman.guielements.priorityqueue.PQInsertCounter;
import generators.compression.huffman.style.HuffmanStyle;

import java.util.ResourceBundle;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class ComplexityAnimator extends ChapterAnimator {

	private static final String CHAPTER_LABEL = "Complexity";

	private Text headline;
	private SourceCode complexityText1;
	private SourceCode complexityText2;
	private PQInsertCounter insertCounter;
	private SourceCode complexityText3;
	private SourceCode complexityText4;
	private SourceCode complexityText5;
	private SourceCode complexityText6;
	private SourceCode complexityText7;
	private SourceCode complexityText8;

	public ComplexityAnimator(Language lang, HuffmanStyle huffmanStyle,
			ResourceBundle messages, Text headline, PQInsertCounter insertCounter) {
		super(lang, huffmanStyle, messages, CHAPTER_LABEL);

		this.headline = headline;
		this.insertCounter = insertCounter;
	}

	@Override
	public void animate() {

		super.animate();

		SourceCodeProperties textProps = (SourceCodeProperties) huffmanStyle
				.getProperties(HuffmanStyle.SOURCECODE);

		Offset offset = new Offset(0, 35, headline, AnimalScript.DIRECTION_SW);
		complexityText1 = lang.newSourceCode(offset, "complexityText1", null,
				textProps);
		String text = messages.getString("complexityText1");
		complexityText1.addCodeLine(text, "", 0, null);

		lang.nextStep();

		offset = new Offset(0, 5, complexityText1, AnimalScript.DIRECTION_SW);
		complexityText2 = lang.newSourceCode(offset, "complexityText2", null,
				textProps);
		text = messages.getString("complexityText2");
		complexityText2.addCodeLine(text, "", 0, null);

		lang.nextStep();

		insertCounter.show();
		insertCounter.moveTo(new Offset(0, 10, complexityText2, AnimalScript.DIRECTION_SW), new MsTiming(1000));
		
		lang.nextStep();
		
		offset = new Offset(0, 35, complexityText2, AnimalScript.DIRECTION_SW);
		complexityText3 = lang.newSourceCode(offset, "complexityText3", null,
				textProps);
		text = messages.getString("complexityText3");
		complexityText3.addCodeLine(text, "", 0, null);

		lang.nextStep();

		offset = new Offset(0, 5, complexityText3, AnimalScript.DIRECTION_SW);
		complexityText4 = lang.newSourceCode(offset, "complexityText4", null,
				textProps);
		text = messages.getString("complexityText4");
		complexityText4.addCodeLine(text, "", 0, null);

		lang.nextStep();

		offset = new Offset(0, 5, complexityText4, AnimalScript.DIRECTION_SW);
		complexityText5 = lang.newSourceCode(offset, "complexityText5", null,
				textProps);
		text = messages.getString("complexityText5");
		complexityText5.addCodeLine(text, "", 0, null);

		lang.nextStep();

		offset = new Offset(0, 5, complexityText5, AnimalScript.DIRECTION_SW);
		complexityText6 = lang.newSourceCode(offset, "complexityText6", null,
				textProps);
		text = messages.getString("complexityText6part1");
		complexityText6.addCodeLine(text, "", 0, null);
		text = messages.getString("complexityText6part2");
		complexityText6.addCodeLine(text, "", 0, null);

		lang.nextStep();

		offset = new Offset(0, 5, complexityText6, AnimalScript.DIRECTION_SW);
		complexityText7 = lang.newSourceCode(offset, "complexityText7", null,
				textProps);
		text = messages.getString("complexityText7");
		complexityText7.addCodeLine(text, "", 0, null);

		lang.nextStep();

		offset = new Offset(0, 5, complexityText7, AnimalScript.DIRECTION_SW);
		complexityText8 = lang.newSourceCode(offset, "complexityText8", null,
				textProps);
		text = messages.getString("complexityText8");
		complexityText8.addCodeLine(text, "", 0, null);
		
		lang.nextStep();
		
		doTransition();
	}
	
	@Override
	protected void doTransition() {
		
		complexityText1.hide();
		complexityText2.hide();
		insertCounter.hide();
		complexityText3.hide();
		complexityText4.hide();
		complexityText5.hide();
		complexityText6.hide();
		complexityText7.hide();
		complexityText8.hide();
	}
}
