package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.VariableUpdate;
import animal.misc.MessageDisplay;

public class VariableUpdateImporter extends AnimatorImporter {
	public Object importFrom(int version, int stepNr, StreamTokenizer stok) {
		int currentStep = version;
        String variableName = null, initialValue = null;
		try {
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
          token = stok.nextToken(); // should be ';'
		} catch (IOException e) {
			MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
		}
		return new VariableUpdate(currentStep, variableName, initialValue);
	}
}
