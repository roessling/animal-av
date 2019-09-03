package generators.compression.huffman.animators;

import generators.compression.huffman.style.HuffmanStyle;

import java.util.ResourceBundle;

import algoanim.primitives.generators.Language;

public abstract class ChapterAnimator {

	protected Language lang;
	protected HuffmanStyle huffmanStyle;
	protected ResourceBundle messages;
	protected String chapterLabel;
	
	public ChapterAnimator(Language lang, HuffmanStyle huffmanStyle, ResourceBundle messages, String chapterLabel){
		
		this.lang = lang;
		this.huffmanStyle = huffmanStyle;
		this.messages = messages;
		this.chapterLabel = chapterLabel;
	}
	
	public void animate() {
		lang.nextStep(chapterLabel);
	}
	
	protected void doTransition() {
		
	}
}
