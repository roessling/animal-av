package generators.network.chandylamport;

import algoanim.primitives.Polyline;
import algoanim.primitives.Text;

public class Message {

	private Text messageText;
	private Polyline messageLine;
	private int messageMatrixRowIndex;
	private int messageMatrixColIndex;
	private boolean wasSaved = false;
	
	public boolean isWasSaved() {
		return wasSaved;
	}

	public void setWasSaved(boolean wasSaved) {
		this.wasSaved = wasSaved;
	}

	public Message(Text messageText, Polyline messageLine,int messageMatrixRowIndex,int messageMatrixColIndex) {
		this.messageText = messageText;
		this.messageLine = messageLine;
		this.messageMatrixRowIndex = messageMatrixRowIndex;
		this.messageMatrixColIndex = messageMatrixColIndex;
	}

	public int getMessageMatrixRowIndex() {
		return messageMatrixRowIndex;
	}

	public int getMessageMatrixColIndex() {
		return messageMatrixColIndex;
	}

	public Text getMessageText() {
		return messageText;
	}

	public void setMessageText(Text messageText) {
		this.messageText = messageText;
	}

	public Polyline getMessageLine() {
		return messageLine;
	}

	public void setMessageLine(Polyline messageLine) {
		this.messageLine = messageLine;
	}

}
