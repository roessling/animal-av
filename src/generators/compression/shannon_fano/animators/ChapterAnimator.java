package generators.compression.shannon_fano.animators;

import generators.compression.shannon_fano.style.ShannonFanoStyle;

import java.util.ResourceBundle;

import algoanim.primitives.generators.Language;

public abstract class ChapterAnimator {

	protected Language lang;
	protected ShannonFanoStyle style;
	protected ResourceBundle messages;
	protected String chapterLabel;
	
	public ChapterAnimator(Language lang, ShannonFanoStyle shannonFanoStyle, ResourceBundle messages, String chapterLabel){
		
		this.lang = lang;
		this.style = shannonFanoStyle;
		this.messages = messages;
		this.chapterLabel = chapterLabel;
	}
	
	public void animate() {
		lang.nextStep(chapterLabel);
	}
	
	protected void doTransition() {
		
	}
}
