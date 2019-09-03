package extras.animalsense.ui.show;

import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import extras.animalsense.evaluate.Question;

public class BasicTextPane extends JTextPane {

	static final String COMMENT_END_TAG = "</COMMENT>";
	static final String COMMENT_BEGIN_TAG1 = "<COMMENT id='";
	static final String COMMENT_BEGIN_TAG2 = "'>";
	
	/**
	 * default serial Nr
	 */
	
	static String NEWLINE = "\n";
	
	private String title;
	private String subtitle;
	private String description;
	private List<Question> questions;
	private List<Component> components;

	
	public BasicTextPane() {
		super();
		this.setEditable(false);
		questions = new LinkedList<Question>();
		components = new LinkedList<Component>(); 
		addBasicStylesToDocument();
	}
	
	protected void addBasicStylesToDocument() {
		StyledDocument doc = getStyledDocument();
		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle(BasicStyle.DESCRIPTION.name(), def);
		StyleConstants.setFontFamily(def, "SansSerif");
		
		Style s;
		s = doc.addStyle(BasicStyle.REGULAR.name(), regular);
		s = doc.addStyle(BasicStyle.QUESTION.name(), regular);
		
		s = doc.addStyle(BasicStyle.HIDDEN.name(), regular);
		StyleConstants.setFontSize(s, 0);
		
		s = doc.addStyle(BasicStyle.SUBTITLE.name(), regular);
		StyleConstants.setItalic(s, true);
		
		s = doc.addStyle(BasicStyle.COMMENT.name(), regular);
		StyleConstants.setItalic(s, true);
		
		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = doc.addStyle("small", regular);
		StyleConstants.setFontSize(s, 10);

		s = doc.addStyle(BasicStyle.TITLE.name(), regular);
		StyleConstants.setFontSize(s, 16);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
	}
	
	/**
	 * @return the questions
	 */
	public List<Question> getQuestions() {
		return questions;
	}

	/**
	 * @param questions the questions to set
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public void addQuestion(Question qi) {
		questions.add(qi);
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
	public void dump() {
		// Get paragraph element
		Element paragraph = getDocument().getDefaultRootElement();

		// Get number of content elements
		int contentCount = paragraph.getElementCount();

		// Get index ranges for each content element.
		// Each content element represents one line.
		// Each line includes the terminating newline.
		for (int i=0; i<contentCount; i++) {
		    Element e = paragraph.getElement(i);
		    System.err.println("E: " + e.getName() + ", -->" + e);
		    if (e.getElementCount() > 0) {
		    	Element content = e.getElement(0);
		    	System.err.println("C: " + content.getName() + ", -->" + content);
		    }
		    
		    int rangeStart = e.getStartOffset();
		    int rangeEnd = e.getEndOffset();
		    try {
		        String line = getText(rangeStart, rangeEnd-rangeStart);
		        System.err.println(i + ">> " + line);
		    } catch (BadLocationException ex) {
		    }
		}

	}
	/*
	public boolean updateComment_old(String questionId, String comment) {
		int commentStartOffset = 0;
		int commentEndOffset;
		boolean commentStartFound = false;
		
		// Get paragraph element
		Element paragraph = getDocument().getDefaultRootElement();
		// Get number of content elements
		int contentCount = paragraph.getElementCount();

		// Get index ranges for each content element.
		// Each content element represents one line.
		// Each line includes the terminating newline.
		for (int i=0; i<contentCount; i++) {
		    Element e = paragraph.getElement(i);
		    System.err.println("E: " + e.getName() + ", -->" + e);
		    
		    if (e.getElementCount() > 0) {
		    	Element content = e.getElement(0);
		    	System.err.println("C: " + content.getName() + ", -->" + content);
		    }
		    
		    int rangeStart = e.getStartOffset();
		    int rangeEnd = e.getEndOffset();
		    try {
		        String line = getText(rangeStart, rangeEnd-rangeStart);
		        System.err.println(i + "|" + line+ "|");
		        
		        if (line != null && line.startsWith(COMMENT_BEGIN_TAG1)) {
		        	int beginIndex = COMMENT_BEGIN_TAG1.length();
		        	int endIndex = line.lastIndexOf(COMMENT_BEGIN_TAG2);
		        	String tempQuestionId = line.substring(beginIndex, endIndex);
		        	
		        	if (questionId.equals(tempQuestionId)) {
		        		commentStartOffset = rangeEnd;
		        		commentStartFound = true;		        		
		        	}
		        } else
		        if (COMMENT_END_TAG.equals(line)) {
		        	if (commentStartFound) {		// We insure that we've got the begin mark before the end mark of the comment
		        		commentEndOffset = rangeStart;
		        		
		        		// We change the comment
		        		String oldComment = getText(rangeStart, rangeEnd-rangeStart);
		        		System.err.println("Old Content was: " + oldComment);
		        		
		        		StyledDocument doc = getStyledDocument();
		        		// Delete the old comment
		        		doc.remove(commentStartOffset, commentEndOffset-commentStartOffset);
		        		
		        		// Insert the new one
		        		doc.insertString(commentStartOffset, comment, doc.getStyle(BasicStyle.COMMENT.name()));
		        		
		        		commentStartFound = false;
		        	}
		        	 
		        }
		        
		    } catch (BadLocationException ex) {
		    }
		}
		
		return false;
	}
	*/

	/**
	 * @param q
	 * @param doc
	 * @throws BadLocationException
	 */
	void addComment(String id) throws BadLocationException {
		StyledDocument doc = getStyledDocument();
		doc.insertString(doc.getLength(), COMMENT_BEGIN_TAG1 + id  + COMMENT_BEGIN_TAG2, doc.getStyle(BasicStyle.HIDDEN.name()));
		doc.insertString(doc.getLength(), NEWLINE, doc.getStyle(BasicStyle.REGULAR.name()));
		doc.insertString(doc.getLength(), COMMENT_END_TAG, doc.getStyle(BasicStyle.HIDDEN.name()));
		doc.insertString(doc.getLength(), NEWLINE + NEWLINE, doc.getStyle(BasicStyle.REGULAR.name()));
	}
	
	public void setComment(String id, String comment) {
		assert id != null;
		
		// Control variables to find the location of the comment
		int commentStartOffset = 0;
		int commentEndOffset;
		boolean commentStartFound = false;
		
		// Get the text area's document
		StyledDocument doc = getStyledDocument();
		// Create an iterator using the root element
		ElementIterator it = new ElementIterator(doc.getDefaultRootElement());
		// Iterate all content elements (which are leaves)
		Element e;
		while ((e = it.next()) != null) {
			if (e.isLeaf()) {
				int rangeStart = e.getStartOffset();
				int rangeEnd = e.getEndOffset();
				try {
					String line = getText(rangeStart, rangeEnd - rangeStart);
					if (line == null)
						continue;

//					System.err.println(">>" + line+ "<<");
					
			        if (!commentStartFound && line.startsWith(COMMENT_BEGIN_TAG1)) {
			        	int beginIndex = COMMENT_BEGIN_TAG1.length();
			        	int endIndex = line.lastIndexOf(COMMENT_BEGIN_TAG2);
			        	String tempQuestionId = line.substring(beginIndex, endIndex);
			        	
			        	if (id.equals(tempQuestionId)) {
			        		commentStartOffset = rangeEnd;
			        		commentStartFound = true;		        		
			        	}
			        } else
			        if (commentStartFound && COMMENT_END_TAG.equals(line)) {	// We insure that we've got the begin mark before the end mark of the comment
			        		commentEndOffset = rangeStart;
			        		
//			        		System.err.println("Old Content was: >>" +  getText(rangeStart, rangeEnd-rangeStart + "<<");
			        		// We change the comment
			        		
			        		// Delete the old comment
			        		doc.remove(commentStartOffset, commentEndOffset-commentStartOffset);
			        		
			        		// Insert the new one
			        		doc.insertString(commentStartOffset, comment, doc.getStyle(BasicStyle.COMMENT.name()));
			        		
			        		// Scroll to the right position
			        		setCaretPosition(commentStartOffset);
			        		
//			        		commentStartFound = false;
			        		// We break the loop, no more cycles are needed
			        		break;
			        	}
					
				} catch (BadLocationException ex) {
				}
			}
		}
	}

	

	public void addComponentToDoc(Component component) {
		String id = component.getClass().getSimpleName() + "_"
				+ component.getName() + "_" + component.hashCode();
		addComponentToDoc(id, component);
	}

	public void addComponentToDoc(String id, Component component) {
		StyledDocument doc = getStyledDocument();

		// Add it as a style
		Style regular = doc.getStyle(BasicStyle.REGULAR.name());
		Style componentStyle = doc.addStyle(id, regular);
		StyleConstants
				.setAlignment(componentStyle, StyleConstants.ALIGN_CENTER);

		StyleConstants.setComponent(componentStyle, component);

		try {
			doc.insertString(doc.getLength(), " ", componentStyle);
			doc.insertString(doc.getLength(), NEWLINE, doc
					.getStyle(BasicStyle.REGULAR.name()));
			components.add(component);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (Component comp : components) {
			comp.setEnabled(enabled);
		}
	}
	
	public void clear() {
		StyledDocument doc = getStyledDocument();
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		components.clear();
		questions.clear();
	}
	
}
