package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.VariableDeclaration;
import animal.misc.MessageDisplay;

public class VariableDeclarationImporter extends AnimatorImporter {
	public Object importFrom(int version, int stepNr, StreamTokenizer stok) {
		int currentStep = version;
//		String type=null;
		String variableName = null, initialValue = null;
//		String activationFrame = null;
		try {
			stok.nextToken();
//			type = stok.sval;

			stok.nextToken();
			variableName = stok.sval;
			initialValue = null;
			int token = stok.nextToken();
			if (token == '=') {
				token = stok.nextToken();
				if (token == StreamTokenizer.TT_WORD)
					initialValue = stok.sval;
				else
					initialValue = String.valueOf(stok.nval);
			} else
				stok.pushBack();
//			activationFrame = null;
			token = stok.nextToken();
			if (token == '/') {
				token = stok.nextToken();
//				activationFrame = stok.sval;
			} else
				stok.pushBack();
			token = stok.nextToken(); // should be ';'
		} catch (IOException e) {
			MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
		}
		return new VariableDeclaration(currentStep, variableName, initialValue);
	}
}
