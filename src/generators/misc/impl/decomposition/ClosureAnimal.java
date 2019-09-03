package generators.misc.impl.decomposition;

import java.util.ArrayList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.util.Offset;
import generators.misc.impl.Attribute;
import generators.misc.impl.FD;
import translator.Translator;

public class ClosureAnimal {

	public static Translator translator;

	public static void setTranslator(Translator translator) {
		ClosureAnimal.translator = translator;
	}

	public static List<Attribute> of(List<Attribute> base, List<FD> FDs) {
		List<Attribute> closure = new ArrayList<>();
		closure.addAll(base);

		List<FD> fds = new ArrayList<>(FDs);

		boolean changed = true;
		boolean firstTime = true;
		while (changed) {
			changed = false;

			if (!firstTime) {
				DecompositionAnimalUtil.showInfo(translator.translateMessage(I.closureAgain),
						new Offset(0, DecompositionAnimalUtil.Y_OFFSET, DecompositionAnimalUtil.closureText,
								AnimalScript.DIRECTION_SW));
			} else {
				firstTime = false;
			}

			// Walk through FDs
			for (FD fd : fds) {

				DecompositionAnimalUtil.highLight(fd, true);

				// check if subset
				if (closure.containsAll(fd.getKeys())) {
					DecompositionAnimalUtil.showInfo(translator.translateMessage(I.closureIsSubset),
							new Offset(0, DecompositionAnimalUtil.Y_OFFSET, DecompositionAnimalUtil.closureText,
									AnimalScript.DIRECTION_SW));
					String text = DecompositionAnimalUtil.closureText.getText();
					int size = closure.size();

					for (Attribute a : fd.getValues()) {
						if (!closure.contains(a)) {
							closure.add(a);
							text += ", " + a.getSymbol();
						}
					}
					DecompositionAnimalUtil.closureText.setText(text, null, null);

					changed = size != closure.size() || changed;
				} else {
					DecompositionAnimalUtil.showInfo(translator.translateMessage(I.closureIsNotSubset),
							new Offset(0, DecompositionAnimalUtil.Y_OFFSET, DecompositionAnimalUtil.closureText,
									AnimalScript.DIRECTION_SW));
				}

				DecompositionAnimalUtil.highLight(fd, false);
			}
		}
		String text = DecompositionAnimalUtil.closureText.getText() + "}";
		DecompositionAnimalUtil.closureText.setText(text, null, null);
		DecompositionAnimalUtil.showInfo(translator.translateMessage(I.closureTerminates), new Offset(0,
				DecompositionAnimalUtil.Y_OFFSET, DecompositionAnimalUtil.closureText, AnimalScript.DIRECTION_SW));

		return closure;

	}

}
