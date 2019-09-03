package generators.misc.impl.synthese;

import java.util.ArrayList;
import java.util.List;

import algoanim.primitives.generators.Language;
import generators.misc.impl.Attribute;
import generators.misc.impl.Closure;
import generators.misc.impl.FD;
import generators.misc.impl.FdUtil;
import generators.misc.impl.Lists;
import generators.misc.impl.Relation;
import translator.Translator;

public class SyntheseImpl {

	Language lang;
	List<Attribute> globalAttributes;
	Relation R;
	String currentStep = "init";

	List<FD> FDset = new ArrayList<>();
	List<FD> reducedLeft = new ArrayList<>();
	List<FD> reducedRight = new ArrayList<>();
	List<FD> emptyRemoved = new ArrayList<>();
	List<FD> assembled = new ArrayList<>();

	// phases for encoding of fd labels
	final String INIT = I18n.init, LEFT = I18n.left, RIGHT = I18n.right, EMPTY = I18n.empty, ASSEMBLED = I18n.assembled;

	List<Relation> compiled = new ArrayList<>();
	private Translator translator;

	public SyntheseImpl(String[] dependencies, Language lang, Translator translator) {
		this.lang = lang;
		this.translator = translator;
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		SyntheseAnimalUtil.init(lang, translator);

		init(dependencies);

		SyntheseAnimalUtil.addStepsQuestion();
		reduceLeft();
		reduceRight();
		removeEmpty();
		assembleMatching();
		createSchema();
		bindRelations();
		SyntheseAnimalUtil.showFinal(compiled);
	}

	private void init(String[] dependencies) {
		FDset = FdUtil.parseDependencies(dependencies);
		initGlobalattributes();
	}

	private void initGlobalattributes() {
		if (globalAttributes == null || globalAttributes.isEmpty()) {
			globalAttributes = new ArrayList<>();
			for (FD fd : FDset) {
				for (Attribute att : fd.getKeys()) {
					if (!globalAttributes.contains(att))
						globalAttributes.add(att);
				}

				for (Attribute att : fd.getValues()) {
					if (!globalAttributes.contains(att))
						globalAttributes.add(att);
				}
			}
		}
	}

	private void reduceLeft() {
		System.out.println("Reduce left");

		// Refresh dependencies
		SyntheseAnimalUtil.showTitle(translator.translateMessage(I18n.titleReduceLeft));
		SyntheseAnimalUtil.showSteps();
		SyntheseAnimalUtil.highlightStep(0);
		SyntheseAnimalUtil.showDependencies(FDset, INIT);
		SyntheseAnimalUtil.moveRightSc();
		SyntheseAnimalUtil.showDependencies(reducedLeft, LEFT);
		lang.nextStep();
		SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.explanationLeft));

		List<String> removedAttributes = new ArrayList<>();

		for (int i = 0; i < FDset.size(); i++) {
			// Make copy before reducing
			FD fd = FDset.get(i);
			FD leftFD = new FD(fd);
			List<Attribute> removedKeys = new ArrayList<>();
			reducedLeft.add(leftFD);

			// If more than one
			if (leftFD.getKeys().size() > 1) {

				// Check unnecessary keys
				for (Attribute key : leftFD.getKeys()) {
					if (!removedKeys.contains(key)) {
						SyntheseAnimalUtil.highLight(INIT + leftFD.getId() + key.getSymbol(), true);
						SyntheseAnimalUtil.showClosure(key, FDset, leftFD);

						List<Attribute> others = new ArrayList<>(leftFD.getKeys());
						List<Attribute> closure = Closure.of(key, FDset, leftFD);
						others.remove(key);

						for (Attribute otherKey : others) {
							if (closure.contains(otherKey)) {
								String label = leftFD.getId() + otherKey.getSymbol();
								if (!removedAttributes.contains(label)) {
									removedAttributes.add(INIT + leftFD.getId() + otherKey.getSymbol());
									removedKeys.add(otherKey);
									SyntheseAnimalUtil.showInfo(
											translator.translateMessage(I18n.key) + " " + otherKey.getSymbol() + " "
													+ translator.translateMessage(I18n.removedLeftNotInClosure) + " "
													+ key.getSymbol() + " " + translator.translateMessage(I18n.isdot));
								}
							} else {
								SyntheseAnimalUtil
										.showInfo(translator.translateMessage(I18n.key) + " " + otherKey.getSymbol()
												+ " " + translator.translateMessage(I18n.remainsDueNotInClosure));
							}
						}
					}
					SyntheseAnimalUtil.highLight(INIT + leftFD.getId() + key.getSymbol(), false);
				}
			} else {
				// Not enough keys, print a message
				Attribute attribute = leftFD.getKeys().get(0);
				SyntheseAnimalUtil.showClosure(attribute, FDset, leftFD);
				SyntheseAnimalUtil.highLight(INIT + leftFD.getId() + attribute.getSymbol(), true);
				SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.onlyOneCanSkip));
				SyntheseAnimalUtil.highLight(INIT + leftFD.getId() + attribute.getSymbol(), false);
			}

			reducedLeft.get(i).getKeys().removeAll(removedKeys);
			SyntheseAnimalUtil.createFdLine(reducedLeft.get(i), LEFT);
		}

		SyntheseAnimalUtil.hideClosure();
		SyntheseAnimalUtil.highLight(removedAttributes, true);
		SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.redMarkedWereRemoved));
		SyntheseAnimalUtil.highLight(removedAttributes, false);
	}

	private void reduceRight() {
		System.out.println("\nReduce right");

		// Refresh dependencies
		SyntheseAnimalUtil.showTitle(translator.translateMessage(I18n.titleReduceRight));
		SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.explanationRight));
		SyntheseAnimalUtil.moveRightSc();
		SyntheseAnimalUtil.showDependencies(reducedRight, RIGHT);
		List<String> removedAttributes = new ArrayList<>();

		reducedRight = reducedLeft;

		boolean firstRight = true;
		for (FD fd : reducedRight) {
			// Copy for concurrent modification
			FD iterableCopy = new FD(fd);
			for (Attribute value : iterableCopy.getValues()) {
				SyntheseAnimalUtil.highLight(LEFT + fd.getId() + value.getSymbol(), true);

				// Set of modified FDs
				FD copy = new FD(iterableCopy);
				copy.getValues().remove(value);
				List<FD> fdValueSubset = Lists.subsetForValue(value, fd, reducedRight);
				SyntheseAnimalUtil.showClosure(iterableCopy.getKeys(), fdValueSubset, copy);

				List<Attribute> closure = Closure.of(fd.getKeys(), fdValueSubset, copy);

				if (firstRight) {
					lang.nextStep();
					firstRight = false;
				}

				if (closure.contains(value)) {

					System.out.println(fd);
					System.out.println(translator.translateMessage(I18n.unnecessaryValue) + " " + value
							+ " with closure " + closure);
					fd.getValues().remove(value);
					System.out.println(fd);

					removedAttributes.add(LEFT + iterableCopy.getId() + value.getSymbol());
					SyntheseAnimalUtil.showInfo(
							value.getSymbol() + " " + translator.translateMessage(I18n.willBeRemovedBecauseKeyClosure));
				} else {
					SyntheseAnimalUtil.showInfo(value.getSymbol() + " "
							+ translator.translateMessage(I18n.willRemainBecauseNotPartKeyClosure));
				}

				SyntheseAnimalUtil.highLight(LEFT + fd.getId() + value.getSymbol(), false);
			}

			SyntheseAnimalUtil.createFdLine(fd, RIGHT);
			firstRight = true;
		}

		SyntheseAnimalUtil.hideClosure();
		SyntheseAnimalUtil.highLight(removedAttributes, true);
		SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.redMarkedRemovedFromRight));
		SyntheseAnimalUtil.highLight(removedAttributes, false);
	}

	private void removeEmpty() {

		System.out.println("\nRemove empty");

		SyntheseAnimalUtil.showTitle(translator.translateMessage(I18n.titleRemoveEmpty));
		SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.explanationEmpty));
		SyntheseAnimalUtil.moveRightSc();
		SyntheseAnimalUtil.showDependencies(emptyRemoved, EMPTY);
		SyntheseAnimalUtil.addEmptyClauseQuestion(reducedRight);

		boolean firstEmpty = true;
		for (int i = 0; i < reducedRight.size(); i++) {
			if (firstEmpty) {
				lang.nextStep();
				firstEmpty = false;
			}

			FD fd = reducedRight.get(i);
			SyntheseAnimalUtil.highLight(fd, true, RIGHT);

			if (!fd.getValues().isEmpty()) {
				emptyRemoved.add(fd);
				SyntheseAnimalUtil.createFdLine(fd, EMPTY);
				SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.mayRemainRightNotEmpty));
			} else {
				System.out.println("Dumping " + fd);
				SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.removeBecauseRightEmpty));
			}
			SyntheseAnimalUtil.highLight(fd, false, RIGHT);
		}
		SyntheseAnimalUtil.hideClosure();
	}

	private void assembleMatching() {
		System.out.println("\nAssemble matching");

		// Refresh dependencies
		SyntheseAnimalUtil.showTitle(translator.translateMessage(I18n.titleAssembleRelations));
		SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.explanationAssembled));
		SyntheseAnimalUtil.moveRightSc();
		SyntheseAnimalUtil.addAssembledQuestion(emptyRemoved);
		lang.nextStep();
		SyntheseAnimalUtil.showDependencies(assembled, ASSEMBLED);

		List<FD> handled = new ArrayList<>();
		for (FD fd : emptyRemoved) {
			System.out.println(fd);
			if (!handled.contains(fd)) {
				List<FD> matching = Lists.subsetForKeyMatch(fd, emptyRemoved);

				FD fdCopy = new FD(fd);
				assembled.add(fdCopy);
				for (FD match : matching) {
					fdCopy.getValues().addAll(match.getValues());
					handled.add(match);
				}

				handled.add(fd);
			}
		}

		int first = 0;
		int assembleCounter = 0;
		for (int i = 0; i < handled.size(); i++) {
			FD current = handled.get(i);

			SyntheseAnimalUtil.highLight(current, true, EMPTY);

			if (i == 0 || !current.getKeys().equals(handled.get(i - 1).getKeys())) {
				// Unhighlights previous matching fds
				for (int j = first; j < i; j++) {
					SyntheseAnimalUtil.highLight(handled.get(j), false, EMPTY);
				}
				first = i;

				if ((i + 1 < handled.size() && !current.getKeys().equals(handled.get(i + 1).getKeys()))
						|| i + 1 >= handled.size()) {
					SyntheseAnimalUtil.createFdLine(assembled.get(assembleCounter++), ASSEMBLED);

					if (first == 0)
						lang.nextStep();

					SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.redDependencyIsAlreadyAssembled));
				}
			} else {
				if (first == 0)
					lang.nextStep();

				SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.redDependenciesAreBeingAssembled));
				SyntheseAnimalUtil.createFdLine(assembled.get(assembleCounter++), ASSEMBLED);
			}
		}

		System.out.println("\nAfter assembly");
		for (FD fd : assembled) {
			System.out.println(fd);
		}
		SyntheseAnimalUtil.hideClosure();
	}

	private void createSchema() {
		SyntheseAnimalUtil.showTitle(translator.translateMessage(I18n.titleCreateNewRelations));
		SyntheseAnimalUtil.highlightStep(1);
		SyntheseAnimalUtil.moveRightSc();
		SyntheseAnimalUtil.showRelations(assembled);

		int i = 1;
		for (FD fd : assembled) {
			compiled.add(new Relation("R" + i++, fd.getKeys(), fd.getValues()));
		}
	}

	private void bindRelations() {
		// Bind old PK

		SyntheseAnimalUtil.showTitle(translator.translateMessage(I18n.titleAddBinderRelation));
		SyntheseAnimalUtil.highlightStep(2);
		SyntheseAnimalUtil.showInfo(translator.translateMessage(I18n.iterateUntilPkCovered),
				SyntheseAnimalUtil.getRelationSc());

		if (R == null) {
			List<Attribute> att = new ArrayList<>();
			List<Attribute> pks = new ArrayList<>();
			String message;

			for (int i = 0; i < compiled.size(); i++) {
				Relation r = compiled.get(i);

				for (Attribute a : r.getPrimaryKey()) {
					if (!pks.contains(a) && !att.contains(a))
						pks.add(a);
				}
				att = Closure.of(pks, assembled, FD.convertToFD(r));
				SyntheseAnimalUtil.highLightRelation(i, true);
				if (att.containsAll(globalAttributes)) {
					message = translator.translateMessage(I18n.theClosureOf) + " (" + FdUtil.printAttributes(pks)
							+ ")⁺ " + translator.translateMessage(I18n.coversPkCreatingBinderRelation);
					
					SyntheseAnimalUtil.showInfo(message, SyntheseAnimalUtil.getRelationSc());
					SyntheseAnimalUtil.highLightRelation(i, false);
					break;
				}

				message = translator.translateMessage(I18n.theClosureOf) + " (" + FdUtil.printAttributes(pks) + ")⁺ = {"
						+ FdUtil.printAttributes(Closure.of(pks, assembled, FD.convertToFD(compiled.get(0)))) + "} "
						+ translator.translateMessage(I18n.doesntCoverNeedMorePk) + " " + compiled.get(i + 1).getName()
						+ " " + translator.translateMessage(I18n.bringInNotPartClosure);
				SyntheseAnimalUtil.showInfo(message, SyntheseAnimalUtil.getRelationSc());
				SyntheseAnimalUtil.highLightRelation(i, false);
			}
			compiled.add(new Relation("R" + (compiled.size() + 1), pks, pks));
		} else {
			compiled.add(new Relation("R" + (compiled.size() + 1), R.getPrimaryKey(), R.getPrimaryKey()));
		}

		SyntheseAnimalUtil.addBinderRelation(compiled.get(compiled.size() - 1));
		lang.nextStep();

		for (Relation rel : compiled) {
			System.out.println(rel);
		}
	}
}