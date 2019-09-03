package animal.misc;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jhave.support.InfoFrameInterface;
import translator.AnimalTranslator;

public class AnimalInfoFrame implements InfoFrameInterface {
	private JFrame frame = null;

	private JEditorPane editorPane = null;

	public AnimalInfoFrame() {
		frame = new JFrame(AnimalTranslator.translateMessage("infoFrame"));
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(editorPane);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		frame.setSize(300, 300);
	}

	public void setQuizmode(boolean mode) {
		// do nothing
	}

	public void setQuestionPanel(JPanel questionPanel) {
		// do nothing
	}

	public void setHtmlStr(String htmlString) {
		try {
			editorPane.setPage(htmlString);
		} catch (Exception e) {
			MessageDisplay.errorMsg("offlineExc", new String[] { htmlString,
					e.getMessage() }, MessageDisplay.RUN_ERROR);
		}
		if (!frame.isVisible())
			frame.setVisible(true);
	}
}
