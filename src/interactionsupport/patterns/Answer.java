package interactionsupport.patterns;
/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */

public class Answer {
	private String answerTxt = null, commentTxt = null;
	private int pnts = 0;
	private boolean state = false;

	public Answer(String answer, String comment, int points, boolean state) {
		answerTxt = answer;
		commentTxt = comment;
		pnts = points;
		this.state = state;
	}
	
	public String getAnswer() {
		return answerTxt;
	}
	
	public String getComment() {
		return commentTxt;
	}
	
	public int getPoints() {
		return pnts;
	}
	
	public boolean isState() {
		return state;
	}
}