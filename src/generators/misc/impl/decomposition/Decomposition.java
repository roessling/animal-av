package generators.misc.impl.decomposition;

import java.util.ArrayList;
import java.util.List;

import algoanim.primitives.generators.Language;
import generators.misc.impl.Attribute;
import generators.misc.impl.Closure;
import generators.misc.impl.FD;
import generators.misc.impl.FdUtil;
import generators.misc.impl.Relation;
import translator.Translator;

public class Decomposition {

	private String[] input;
	private Language lang;
	private List<FD> funcDependencies;
	public static List<Attribute> globalAttributes;
	private List<Relation> relations = new ArrayList<>();
	private List<Relation> completedRelations = new ArrayList<>();
	private String[] closureAttributes;
	private Translator t;

	public Decomposition(String[] input, String[] closureAttributes, Language lang, Translator translator) {
		this.input = input;
		this.closureAttributes = closureAttributes;
		this.lang = lang;
		this.t = translator;

		init();
		decompose();
	}

	private void init() {
		funcDependencies = FdUtil.parseDependencies(input);
		List<List<Attribute>> closureExamples = parseClosureAttributes(closureAttributes);
		initGlobalattributes();
		relations.add(new Relation("R", computeCanditateKeys(globalAttributes), globalAttributes));

		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		DecompositionAnimalUtil.init(lang, funcDependencies, t);
		DecompositionAnimalUtil.showIntro();
		DecompositionAnimalUtil.showDependencies(funcDependencies);

		// Test input
		for (List<Attribute> example : closureExamples) {
			if (globalAttributes.containsAll(example))
				DecompositionAnimalUtil.showClosureIntro(example);
		}

		DecompositionAnimalUtil.showTitle(t.translateMessage(I.algoName));
		DecompositionAnimalUtil.showRelations(relations);
		DecompositionAnimalUtil.showSteps();
		DecompositionAnimalUtil.showInfo(t.translateMessage(I.initialization));
		DecompositionAnimalUtil.hightlightStep(0, false);
	}

	private void decompose() {
		boolean hasChanged = true;
		DecompositionAnimalUtil.addBcnfQuestion();
		while (hasChanged) {
			DecompositionAnimalUtil.hightlightStep(1, true);
			hasChanged = false;
			Relation relation1 = null, relation2 = null, removedRelation = null;

			for (Relation relation : relations) {
				if (completedRelations.contains(relation)) {
					DecompositionAnimalUtil.highlight(relation, true);
					DecompositionAnimalUtil.showInfo(t.translateMessage(I.relationIsInBCNF));
					DecompositionAnimalUtil.highlight(relation, false);
					continue;
				}

				DecompositionAnimalUtil.highlight(relation, true);

				DecompositionAnimalUtil.hightlightStep(2, true);
				for (FD fd : funcDependencies) {
					DecompositionAnimalUtil.highLight(fd, true);
					if (isSubsetOfRelation(fd, relation) && !isInBcnf(fd, relation)) {
						DecompositionAnimalUtil.hightlightStep(1, false);
						DecompositionAnimalUtil.hightlightStep(2, false);
						DecompositionAnimalUtil.hightlightStep(6, true);

						List<Attribute> keys = relation.getPrimaryKey();
						List<Attribute> closure = Closure.of(fd.getKeys(), funcDependencies, fd);
						computeIntersection(closure, relation);

						relation1 = new Relation(relation.getName() + 1, computeCanditateKeys(closure), closure);

						DecompositionAnimalUtil.addR1CheckQuestion(closure, relation);
						DecompositionAnimalUtil.showDecomposedRelations(relation1);
						DecompositionAnimalUtil.showInfo(t.translateMessage(I.attributesFirstSplit));

						ArrayList<Attribute> remains = new ArrayList<>(relation.getAttributes());
						remains.removeAll(closure);
						remains.addAll(fd.getKeys());
						keys.stream().forEach(a -> {
							if (!remains.contains(a))
								remains.add(a);
						});

						relation2 = new Relation(relation.getName() + 2, keys, remains);
						DecompositionAnimalUtil.addToRightRelations(relation2);
						DecompositionAnimalUtil.showInfo(t.translateMessage(I.attributesSecondSplit));

						removedRelation = relation;
						DecompositionAnimalUtil.drawLine();
						hasChanged = true;
						DecompositionAnimalUtil.highLight(fd, false);
						break;
					} else {
						DecompositionAnimalUtil.highLight(fd, false);
					}
				}
				if (hasChanged) {
					break;
				}

				completedRelations.add(relation);
				DecompositionAnimalUtil.highlight(relation, false);
			}

			if (hasChanged) {
				DecompositionAnimalUtil.hightlightStep(6, false);
				DecompositionAnimalUtil.hightlightStep(7, true);
				DecompositionAnimalUtil.hightlightStep(8, true);
				relations.remove(removedRelation);
				relations.add(relation1);
				relations.add(relation2);
				DecompositionAnimalUtil.hideMiddleRelations();
				DecompositionAnimalUtil.showRelations(relations);
				DecompositionAnimalUtil.hideRightRelations();
				DecompositionAnimalUtil.hideLine();
				DecompositionAnimalUtil.hightlightStep(7, false);
				DecompositionAnimalUtil.hightlightStep(8, false);
			}
		}

		DecompositionAnimalUtil.hightlightStep(1, false);
		DecompositionAnimalUtil.hightlightStep(2, false);
		lang.hideAllPrimitives();
		DecompositionAnimalUtil.showFinal(completedRelations);
	}

	private boolean isInBcnf(FD fd, Relation relation) {
		if (isTrivial(fd) || isSuperKey(fd, relation)) {
			return true;
		} else {
			DecompositionAnimalUtil.showInfo(t.translateMessage(I.redIsInNotBcnf));
			return false;
		}
	}

	private boolean isTrivial(FD fd) {
		if (fd.getValues().containsAll(fd.getKeys())) {
			DecompositionAnimalUtil.showInfo(t.translateMessage(I.redIsTrivial));
			return true;
		} else {
			return false;
		}
	}

	private boolean isSuperKey(FD fd, Relation relation) {
		if (Closure.of(fd.getKeys(), funcDependencies, fd).containsAll(relation.getAttributes())) {
			DecompositionAnimalUtil.showInfo(t.translateMessage(I.markedFdDeterminant));
			return true;
		} else {
			return false;
		}
	}

	private boolean isSubsetOfRelation(FD fd, Relation rel) {
		ArrayList<Attribute> fdAttributes = new ArrayList<>();
		fdAttributes.addAll(fd.getKeys());
		fdAttributes.addAll(fd.getValues());

		ArrayList<Attribute> relAttributes = new ArrayList<>();
		relAttributes.addAll(rel.getAttributes());

		if (relAttributes.containsAll(fdAttributes)) {
			return true;
		} else {
			DecompositionAnimalUtil.showInfo(t.translateMessage(I.markedNotSubsetRelation));
			return false;
		}
	}

	private boolean isOnRhs(Attribute att) {
		for (FD fd : funcDependencies) {
			if (fd.getValues().contains(att)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Computes the intersection of the list attributes and the attributes of the
	 * given relation and mutate the given list
	 * 
	 * @param attributes
	 *            The attributes which get filtered
	 * @param rel
	 *            The relation, containing the valid attributes for the given list
	 */
	private void computeIntersection(List<Attribute> attributes, Relation rel) {
		attributes.removeIf(a -> !rel.getAttributes().contains(a));
	}

	private List<Attribute> computeCanditateKeys(List<Attribute> attributes) {
		List<Attribute> left = findLeft();
		List<Attribute> candidates = new ArrayList<>();
		List<Attribute> lastClosure = new ArrayList<>();

		for (Attribute attribute : left) {

			if (attributes.contains(attribute)) {
				candidates.add(attribute);

				List<Attribute> newClosure = Closure.of(candidates, funcDependencies);

				if (newClosure.containsAll(attributes)) {
					return candidates;
				} else if (newClosure.size() <= lastClosure.size()) {
					candidates.remove(candidates.size());
				} else {
					lastClosure = newClosure;
				}
			}
		}

		List<Attribute> middle = findMiddle();

		for (Attribute attribute : middle) {
			if (attributes.contains(attribute)) {
				candidates.add(attribute);

				if (Closure.of(candidates, funcDependencies).containsAll(attributes)) {
					return candidates;
				}
			}
		}

		System.out.println("No candidate key found!");
		return new ArrayList<>();
	}

	private List<Attribute> findLeft() {
		List<Attribute> left = new ArrayList<>();
		for (FD fd : funcDependencies) {
			for (Attribute key : fd.getKeys()) {
				if (!isOnRhs(key) && !left.contains(key)) {
					left.add(key);
				}
			}
		}
		return left;
	}

	private List<Attribute> findMiddle() {
		List<Attribute> middle = new ArrayList<>();
		List<Attribute> left = findLeft();
		for (FD fd : funcDependencies) {
			for (Attribute key : fd.getKeys()) {
				if (!left.contains(key) && !middle.contains(key)) {
					middle.add(key);
				}
			}
		}
		return middle;
	}

	private void initGlobalattributes() {
		if (globalAttributes == null || globalAttributes.isEmpty()) {
			globalAttributes = new ArrayList<>();
			for (FD fd : funcDependencies) {
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

	private List<List<Attribute>> parseClosureAttributes(String[] closureAttributes) {
		List<List<Attribute>> closureExamples = new ArrayList<>();

		if (closureAttributes != null)
			for (String atts : closureAttributes) {
				ArrayList<Attribute> example = new ArrayList<>();
				for (String a : atts.split(";")) {
					example.add(new Attribute(a));
				}
				closureExamples.add(example);
			}

		return closureExamples;
	}
}
