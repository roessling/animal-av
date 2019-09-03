package generators.backtracking;

import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ListElement;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.ListElementProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;
import algoanim.util.TicksTiming;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class DavisPutnam implements ValidatingGenerator {
	public static class Literal {
		private char id;
		private boolean negated = false;

		private Literal(char id, boolean neg) {
			this.id = id;
			this.negated = neg;
		}

		/* create negated literal */
		public static Literal Neg(char id) {
			return new Literal(id, true);
		}

		/* create negated literal from existing one */
		public static Literal Neg(Literal lit) {
			Literal lit2 = Neg(lit.id);
			lit2.negated = !lit.negated;
			return lit2;
		}

		public static boolean CanParse(String s) {
		  String myS = s.replace(" ", "");
			return myS.matches("~?[a-zA-Z]");

		}

		public static Literal FromString(String s) {
			String myS = s.replace(" ", "");
			if (myS.matches("[a-zA-Z]"))
				return Pos(myS.charAt(0));
			else if (myS.matches("~[a-zA-Z]"))
				return Neg(myS.charAt(1));
			else
				throw new IllegalArgumentException(
						"could not parse to literal: " + myS);
		}

		/* create positive literal */
		public static Literal Pos(char id) {
			return new Literal(id, false);
		}

		@Override
		public String toString() {
			return (negated ? "~" : "") + id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			result = prime * result + (negated ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Literal other = (Literal) obj;
			if (id != other.id)
				return false;
			if (negated != other.negated)
				return false;
			return true;
		}

	}

	private static String clauseToString(Set<Literal> clause) {
		StringBuilder cVisual = new StringBuilder();
		int litCnt = 0;
		for (Literal l : clause)
			cVisual.append(l.toString()).append(
					litCnt++ < clause.size() - 1 ? ", " : "");/*
															 * don't append
															 * comma for last
															 * element
															 */
		return cVisual.toString();
	}


	/* updates string array */
	private void clauseSetUpdated(Set<Set<Literal>> csReal) {
		clauseSet = new String[csReal.size()];

		int cCnt = 0;
		for (Set<Literal> cReal : csReal) {
			/* update visual array */
			clauseSet[cCnt++] = clauseToString(cReal);

		}

		if (clauseSetV != null)
			clauseSetV.hide();
		/* bug: this will only be displayed if there is a final call to next step */

		if (clauseSet.length > 0)
			clauseSetV = lang.newStringArray(new Offset(700, 60, subHeader4,
					"NW"), clauseSet, "clauseSet", null, clausetSetVProps);

		

		lang.nextStep();
	}

	private int oldLineNumber;

	/* highlights only line number and inserts a step */
	private void breakHl(int lno) {
		codeV.unhighlight(oldLineNumber);

		codeV.highlight(lno);

		oldLineNumber = lno;

		lang.nextStep();
	}

	private void markerTo(Set<Literal> clause) {
		if (arrMarker == null)
			throw new NullPointerException(
					"no marker but trying to move marker.");
		if (arrMarker.getArray() != clauseSetV) {
			/* array has been recreated -> recreate marker */
			createArrayMarker();
		}

		/* sets marker to position of clause in clauseSet */
		String clauseAsString = clauseToString(clause);
		for (int i = 0; i < clauseSet.length; ++i)
			if (clauseSet[i].equals(clauseAsString))
				arrMarker.move(i, null, null);

	}

	private enum LocalVariable {
		FlatLen, UnitClauses, Lit, RndLit, SplitCS1, SplitCS2
	}

	/* updates value in loc. variable block. set to null to clear */
	@SuppressWarnings("unchecked")
	private void dspLocVar(LocalVariable lv, Object value) {

		if (value == null) {
			/* clear and return */
			locVars.put(lv, null);
			return;
		}

		/* convert value to string */
		String valueAsString;
		switch (lv) {
		case FlatLen:
			if (!(value instanceof Integer))
				throw new IllegalArgumentException("not an integer");
			valueAsString = value.toString();
			break;
		case UnitClauses:
			if (!(value instanceof Set<?>))/*
											 * problem: value could also be for
											 * splitcs1/2
											 */
				throw new IllegalArgumentException("not a set<?>");
			valueAsString = "{" + clauseToString((Set<Literal>) value) + "}";
			break;
		case Lit:/* same as rndlit */
		case RndLit:
			if (!(value instanceof Literal))
				throw new IllegalArgumentException("not a literal");
			valueAsString = value.toString();
			break;
		case SplitCS1:
		case SplitCS2:
			/*
			 * problem: value could also be for unitclauses
			 */
			if (!(value instanceof Set<?>))
				throw new IllegalArgumentException("not a set<?>");
			valueAsString = clauseSetToString((Set<Set<Literal>>) value);
			break;
		default:
			throw new IllegalArgumentException();
		}

		locVars.put(lv, valueAsString);

		if (locVarBlock != null)
			locVarBlock.hide();
		locVarBlock = lang.newSourceCode(new Offset(700, 200, subHeader4, "NW"),
				"locVarBlock", null, locVarBlockProps);

		locVarBlock.addCodeLine("Lokale Variablen:", "l0", 0, null);
		for (LocalVariable var : locVars.keySet()) {
			String val = locVars.get(var);
			if (val != null)
				locVarBlock.addCodeLine(var + " = " + val, var.toString(), 0,
						null);
		}

	}

	private boolean dp(Set<Set<Literal>> clauseSet) {
		clauseSetUpdated(clauseSet);/* AS */

		breakHl(1);
		int flatLen;
		breakHl(2);
		do {
			breakHl(3);
			flatLen = flatLen(clauseSet);
			dspLocVar(LocalVariable.FlatLen, flatLen);
			breakHl(4);
			Set<Literal> unitClauses = new HashSet<Literal>();
			dspLocVar(LocalVariable.UnitClauses, unitClauses);
			breakHl(5);
			for (Set<Literal> clause : clauseSet) {
				markerTo(clause);
				breakHl(6);
				if (clause.size() == 1) {/* unit clause */
					breakHl(7);
					unitClauses.add(clause.iterator().next());
					dspLocVar(LocalVariable.UnitClauses, unitClauses);
					breakHl(8);
				}
			}
			breakHl(9);
			for (Literal lit : unitClauses) {
				dspLocVar(LocalVariable.Lit, lit);
				breakHl(10);
				for (Iterator<Set<Literal>> clauseIt = clauseSet.iterator(); clauseIt
						.hasNext();) {
					breakHl(11);
					Set<Literal> clause = clauseIt.next();
					markerTo(clause);
					breakHl(12);
					if (clause.contains(lit)) {
						breakHl(14);
						addQuestionAnswer(2);
						clauseIt.remove();/* unit subsumption */
						clauseSetUpdated(clauseSet);/* AS */
						
						breakHl(15);
					} else {
						breakHl(16);
						for (Iterator<Literal> litIt = clause.iterator(); litIt
								.hasNext();) {
							breakHl(17);

							if (litIt.next().equals(Literal.Neg(lit))) {
								breakHl(19);
								litIt.remove();/* unit resolution */
								addQuestionAnswer(3);
								clauseSetUpdated(clauseSet); /* AS */
								breakHl(20);
							}

						}
					}
				}
			}

			dspLocVar(LocalVariable.Lit, null);

			breakHl(23);
			if (clauseSet.isEmpty()) {
				breakHl(24);
				return true;
			}

			else {
				breakHl(25);
				if (containsEmptyClause(clauseSet)) {
					breakHl(26);
					return false;
				}
				breakHl(27);
			}
			breakHl(29);
		} while (flatLen(clauseSet) != flatLen);/* fixpoint iteration */

		dspLocVar(LocalVariable.FlatLen, null);/* clear */
		dspLocVar(LocalVariable.UnitClauses, null);

		breakHl(32);
		/* naive splitting to create a new unit clause */
		Literal rndLit = chooseLiteral(clauseSet);
		dspLocVar(LocalVariable.RndLit, rndLit);
		breakHl(33);

		Set<Set<Literal>> splitCS1 = new HashSet<Set<Literal>>(clauseSet);
		dspLocVar(LocalVariable.SplitCS1, splitCS1);
		breakHl(34);
		splitCS1.add(createClause(rndLit));
		dspLocVar(LocalVariable.SplitCS1, splitCS1);

		breakHl(35);
		Set<Set<Literal>> splitCS2 = new HashSet<Set<Literal>>(clauseSet);
		dspLocVar(LocalVariable.SplitCS2, splitCS2);
		breakHl(36);
		splitCS2.add(createClause(Literal.Neg(rndLit)));
		dspLocVar(LocalVariable.SplitCS2, splitCS2);
		
		addQuestionAnswer(4);

		breakHl(38);
		if (dp(splitCS1)) {
			breakHl(39);
			return true;
		} else {
			breakHl(40);
			if (dp(splitCS2)) {
				breakHl(41);
				return true;
			} else {
				breakHl(43);
				return false;
			}

		}

	}

	private static String clauseSetToString(Set<Set<Literal>> cs) {
		StringBuilder str = new StringBuilder("{");
		int clCnt = 0;
		for (Set<Literal> cl : cs) {
			str.append("{").append(clauseToString(cl))
					.append(clCnt++ < cs.size() - 1 ? "}, " : "}");
		}
		str.append("}");
		return str.toString();
	}

	private static Set<Literal> createClause(Literal... literals) {
		return new HashSet<Literal>(Arrays.asList(literals));
	}

	private static Literal chooseLiteral(Set<Set<Literal>> clauseSet) {
		if (!clauseSet.isEmpty() && !clauseSet.iterator().next().isEmpty())
			return clauseSet.iterator().next().iterator().next();
		throw new IllegalArgumentException(
				"clause set does not contain any literals");
	}

	private static int flatLen(Set<Set<Literal>> clauseSet) {
		int ctr = 0;
		for (Set<Literal> cl : clauseSet)
			ctr += cl.size();
		return ctr;
	}

	private static boolean containsEmptyClause(Set<Set<Literal>> clauseSet) {
		for (Set<Literal> cl : clauseSet)
			if (cl.isEmpty())
				return true;
		return false;
	}

	/* string representation of real clause set */
	private StringArray clauseSetV; /* v for visualization */
	private String[] clauseSet;

	/* display local variables */
	SourceCode locVarBlock;
	Map<LocalVariable, String> locVars = new TreeMap<LocalVariable, String>();/* treemap for ordering */

	/* code */
	private SourceCode codeV;
	private SourceCodeProperties locVarBlockProps;

	private void genDavisPutnamASCode(Set<Set<Literal>> clSet,
			ArrayProperties clausetSetVProps, SourceCodeProperties codeProps,
			SourceCodeProperties locVarProps, TextProperties headerProps, RectProperties headerRectProps) {
		locVarBlockProps = locVarProps;
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		/* general text properties */
		SourceCodeProperties textProps = new SourceCodeProperties();

		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 15));
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		TextProperties textProps2 = new TextProperties();

		textProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 15));
		textProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		/* STEP 1 STARTS */
		/* header */
		
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(20, 30),
				"Davis-Putnam-Verfahren", "header", null, headerProps);
		headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		Rect headerRect = lang.newRect(new Offset(-5, -5, header, "NW"),
				new Offset(5, 5, header, "SE"), "headerRect", null,
				headerRectProps);

		/* sub header */
		TextProperties subheaderProps = new TextProperties();
		subheaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 18));
		subheaderProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		Text subHeader = lang.newText(new Offset(0, 60, headerRect, "NW"),
				"Problem und Algorithmus", "subheader", null, subheaderProps);

		/* show introduction */
		
		introductionV = lang.newSourceCode(new OffsetFromLastPosition(0, 60),
				"sourceCode", null, textProps);
		for (String l : introduction)
			introductionV.addCodeLine(l, null, 0, null);
		
		
		/* STEP 1 ENDS */

		/* STEP 2 STARTS */
		lang.nextStep("Problem und Algorithmus");
		
		addQuestionAnswer(0);
		
		
		subHeader.hide();
		introductionV.hide();

		Text subHeader2 = lang.newText(new Offset(0, 60, headerRect, "NW"),
				"Transformation einer Formel", "subheader2", null,
				subheaderProps);

		/* OR is displayed correctly */
		Text formula = lang.newText(new OffsetFromLastPosition(0, 60),
				"(P | Q) & (~Q | R) & R", "formula", null, textProps2);
		Text cs = lang.newText(new OffsetFromLastPosition(250, 0),
				"{{P,Q}, {~Q,R}, R}", "clauseSet", null, textProps2);

		PolylineProperties arrowProps = new PolylineProperties();
		arrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		/* 11 works for some reason, 8 not */
		Polyline arrow = lang.newPolyline(
				new Node[] { new Offset(20, 8, formula, "NE"),
						new Offset(-20, 11, cs, "NW") }, "arrow0", null,
				arrowProps);
		Text desc1 = lang
				.newText(
						new Offset(0, 50, formula, "NW"),
						"Die Formel in dieser Form ist die Eingabe fuer den Algorithmus.",
						"desc1", null, textProps2);
		
		
		/* STEP 2 ENDS */

		/* STEP 3 STARTS */
		lang.nextStep("Transformation einer Formel");
		
		addQuestionAnswer(1);
		
		
		subHeader2.hide();
		formula.hide();
		cs.hide();
		desc1.hide();
		arrow.hide();

		Text subheader3 = lang.newText(new Offset(0, 60, headerRect, "NW"),
				"Abstrakte Vorgehensweise und Teilschritte", "subheader3",
				null, subheaderProps);
		SourceCode abstractAlgo = lang.newSourceCode(
				new OffsetFromLastPosition(0, 60), "abstractAlgo", null,
				textProps);
		for (String l : abstractAlgoDesc)
			abstractAlgo.addCodeLine(l, null, 0, null);

		/* STEP 3 ENDS */

		/* STEP 4 BEGINS */
		lang.nextStep("Abstrakte Vorgehensweise und Teilschritte");
		subheader3.hide();
		abstractAlgo.hide();

		/* subheader */
		subHeader4 = lang.newText(new Offset(0, 60, headerRect, "NW"),
				"Beispiel", "subheader4", null, subheaderProps);

		codeV = lang.newSourceCode(new Offset(0, 60, subHeader4, "NW"),
				"sourceCode", null, codeProps);
		for (String l : code)
			codeV.addCodeLine(l, null, 0, null);

		this.clausetSetVProps = clausetSetVProps;

		/* initialize clauseSetV */
		clauseSetUpdated(clSet);

		/* STEP 4 ENDS */

		lang.nextStep();

		createArrayMarker();

		/* initialize display for local variables */
		for (LocalVariable lv : LocalVariable.values())
			locVars.put(lv, null);

		boolean result = dp(clSet);

		
		
		/* ending */
		lang.nextStep("Beispiel");
		
		

		

		codeV.hide();
		locVarBlock.hide();
		clauseSetV.hide();
		subHeader4.hide();
		Text subheader5 = lang.newText(new Offset(0, 60, headerRect, "NW"),
				"Abschliessende Bemerkungen", "subheader5", null,
				subheaderProps);
		SourceCode ending = lang.newSourceCode(new Offset(0, 60, subheader5,
				"NW"), "ending", null);
		for (String l : result ? ending1True : ending1False) {
			ending.addCodeLine(l, l, 0, null);
		}
		for (String l : ending2) {
			ending.addCodeLine(l, l, 0, null);
		}

		lang.nextStep("Abschliessende Bemerkungen");
		lang.finalizeGeneration();
	}

	private void createArrayMarker() {
		if (clauseSetV.getLength() > 0) {
			ArrayMarkerProperties arrProps = new ArrayMarkerProperties();
			arrProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "clause");
			arrProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
			arrMarker = lang.newArrayMarker(clauseSetV, 0, "arrMarker", null,
					arrProps);
		} else
			throw new IllegalStateException(
					"clause set array has zero elements. cannot create a marker for it.");
	}
	
	private void addQuestionAnswer(int n) {
		MultipleChoiceQuestionModel[] qas = new MultipleChoiceQuestionModel[5];
		
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel(
				"Einheitsklauseln");
		q1.setPrompt("Warum sind Einheitsklauseln so wichtig?");
		q1.addAnswer(
				"Weil sie immer mit 'wahr' belegt werden müssen und deshalb nicht 'wahr' und 'falsch' durchprobiert werden muss.",
				1,
				"Das ist richtig.");
		q1.addAnswer("Weil sie immer mit 'falsch' belegt werden müssen und deshalb nicht 'wahr' und 'falsch' durchprobiert werden muss.", 0,
				"Leider falsch. Wäre eine Einheitsklausel 'falsch', wäre die komplette Klauselmenge sofort 'falsch'.");
		qas[0] = q1;
		
		MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel(
				"Transformation");
		q2.setPrompt("Zu was wird P & (Q | R) & R?");
		q2.addAnswer(
				"{{P,Q,R}}",
				0,
				"Leider falsch. Jeder 'Bestandteil' der Konjunktion liegt in seiner eigenen Menge.");
		q2.addAnswer("{{P},{Q},{R}}", 0,
				"Leider falsch. Q und R gelten als ein 'Bestandteil' der Konjunktion und liegen daher in einer Menge.");
		q2.addAnswer("{{P},{Q,R},{R}}", 1,
				"Das ist richtig. Jeder 'Bestandteil' der Konjunktion liegt in seiner eigenen Menge.");
		qas[1] = q2;
		
		
		MultipleChoiceQuestionModel q3 = new MultipleChoiceQuestionModel(
				"Klausel entfernen");
		q3.setPrompt("Warum kann hier die ganze Klausel entfernt werden?");
		q3.addAnswer(
				"Weil die Klausel (eine Disjunktion) eine 'wahre' Variable (die Einheitsklausel) enthält und daher 'wahr' wird.",
				1,
				"Das ist richtig. Hat eine Disjunktion nur einen 'wahren' Bestandteil, wird sie komplett 'wahr'.");
		q3.addAnswer("Weil die Klausel 'falsch' geworden ist und daher keine Rolle mehr in der Klauselmenge spielt.", 0,
				"Leider falsch. Die Klausel ist 'wahr' geworden, denn sie enthält eine 'wahre' Einheitsklausel.");
		qas[2] = q3;
		
		
		MultipleChoiceQuestionModel q4 = new MultipleChoiceQuestionModel(
				"Negierte Einheitsklausel entfernen");
		q4.setPrompt("Warum kann die negierte Einheitsklausel gelöscht werden?");
		q4.addAnswer(
				"Weil sie falsch ist und daher keine Rolle in der Klausel spielt",
				1,
				"Das ist richtig. Eine Disjunktion mit 'falsch' hat keinen Einfluss (Idempotenz).");
		q4.addAnswer("Weil sie 'wahr' ist und daher keine Rolle in der Klausel spielt.", 0,
				"Leider falsch. Die Einheitsklausel ist 'wahr', also ist die negierte EK 'falsch'.");
		qas[3] = q4;
		
		
		MultipleChoiceQuestionModel q5 = new MultipleChoiceQuestionModel(
				"Splitting");
		q5.setPrompt("Wieso hilft das Splitting hier?");
		q5.addAnswer(
				"Die Mengen werden kleiner und können so schneller bearbeitet werden.",
				0,
				"Leider falsch. Die Splitting macht die Klauselmenge wider Erwarten nicht kleiner.");
		q5.addAnswer("Man gewinnt jeweils eine neue Einheitsklausel.", 1,
				"Das ist wahr. Im nächsten Aufruf kann so die neue Einheitsklausel mit 'wahr' belegt und die Klauselmenge weiter reduziert werden.");
		
		qas[4] = q5;
		
		
		/*attach*/
		lang.addMCQuestion(qas[n]);
		
		
	}

	/* used to draw clause set array */
	private ArrayProperties clausetSetVProps;
	private Text subHeader4;
	private ArrayMarker arrMarker;

	/* introduction */
	private SourceCode introductionV;

	private final String[] introduction = {
			"Das Davis-Putnam-Verfahren kann das Erfuellbarkeitsproblem (SAT)",
			"einer aussagenlogischen Formel in konjunktiver Normalform",
			"entscheiden. Das bedeutet, dass der Algorithmus 'true'",
			"liefert, falls eine Variablenbelegung gefunden werden kann,",
			"fuer die die Formel zu wahr auswertet. Andernfalls liefert",
			"er 'false'.",

			"Die Formel selbst wird als Klauselmenge dargestellt.",
			"Eine Klausel ist dabei eine Disjunktion von Literalen",
			"(Variable oder ihr Negat) und wird ebenfalls als Menge abgebildet.",
			"Die Eingabe des Algorithmus' ist also eine Menge von Mengen.",

			"Der Algorithmus wendet dabei wenige, einfache Regeln",
			"an, um Variablen mit nur einer moeglichen Belegung zu finden (Literale aus sog. Einheitsklauseln).",
			"Gelingt ihm das nicht, probiert er beide moeglichen",
			"Belegungen aus." };

	private final String[] abstractAlgoDesc = {
			"1.  'Einheitsklauseln' in Klauselmenge K finden (Klausel mit nur 1 Literal).",
			"     (Diese muessen wahr sein, da K Konjunktion ueber Klauseln ist.)",

			"2.  Fuer alle gefundenen Einheitsklauseln:",
			"2.1 Entferne Klauseln aus K, in denen Literal aus einer gefundenen Einheitsklausel vorkommt",
			"      (Also auch Einheitsklausel selbst. Denn das wahre Literal macht ganze Klauseln wahr.)",
			"2.2 Entferne Negate von Literal aus Einheitsklausel aus anderen Klauseln",
			"     (Da Literal aus Einheitsklausel wahr -> Negat falsch -> ueberfluessig in Klausel)",

			"3.  ueberpruefe Klauselmenge K. 2 Faelle sind moeglich:",
			"3.1 K ist leer, d. h. Fall 2.1 ist 'oft genug' eingetreten.",
			"    (Die Einheitsklauseln haben also ganz K wahr gemacht.) -> Ende mit 'true'",

			"3.2 K selbst ist nicht leer, enthaelt aber eine leere Klausel",
			"    D. h. Fall 2.2 ist 'zu oft' eingetreten. (Es blieb in der Klausel nichts uebrig,",
			"    was sie noch wahr machen koennte und Disjunktion ueber die leere Menge ist falsch.)",
			"    -> Ende mit 'false'",

			"4.  ueberpruefe, ob sich K seit Schritt 1. veraendert hat",
			"    (weniger Klauseln oder weniger Literale in Klauseln):",

			"4.1  veraendert: Wiederhole ab Schritt 1.",
			"4.2  gleich: gehe zu Schritt 5",

			"5.  Zerlege K in zwei neue Klauselmengen K1 und K2 wie folgt:",
			"5.1  Waehle zufaellig ein Literal L aus beliebiger Klausel in K",
			"5.2  Erzeuge K1 durch Hinzufuegen von einer Einheitsklausel, die aus L besteht",
			"     Analog fuer K2, aber hier mit dem Negat von L",

			"5.3 Rufe Verfahren mit K1 und K2 auf",
			"5.4 Ergibt mindestens 1 Aufruf 'true', ende mit 'true', sonst mit 'false'.",
			"   (Es reicht, wenn eine Belegung von L zum Erfolg fuehrt.)" };

	private final String[] code = {
			"public static boolean dp(Set<Set<Literal>> clauseSet) {", // 0
			"  int flatLen;", // 1
			"  do {", // 2
			"    flatLen = flatLen(clauseSet);", // 3
			"    Set<Literal> unitClauses = new HashSet<>();", // 4
			"    for(Set<Literal> clause : clauseSet ) {", // 5
			"      if(clause.size() == 1)", // 6
			"        unitClauses.add(clause.iterator().next());/*Einheitsklausel (EK) merken*/", // 7
			"    }", // 8
			"    for(Literal lit : unitClauses) {", // 9
			"      for(Iterator<Set<Literal>> clauseIt = clauseSet.iterator(); clauseIt.hasNext(); ) {", // 10
			"        Set<Literal> clause = clauseIt.next();", // 11
			"        if(clause.contains(lit))", // 12
			"          /*Klausel, die Literal von EK enthaelt, loeschen*/", // 13
			"          clauseIt.remove();", // 14
			"        else", // 15
			"          for(Iterator<Literal> litIt = clause.iterator(); litIt.hasNext(); )", // 16
			"            if(litIt.next().equals(Literal.Neg(lit)))", // 17
			"              /*Negiertes Literal aus EK loeschen*/", // 18
			"              litIt.remove();", // 19
			"      }", // 20
			"    }", // 21
			"      ", // 22
			"    if(clauseSet.isEmpty())", // 23
			"      return true;", // 24
			"    else if(containsEmptyClause(clauseSet))", // 25
			"      return false;", // 26
			"  }", // 27
			"  /*ueberpruefe Veraenderung der Gesamtzahl der Literale*/", // 28
			"  while(flatLen(clauseSet) != flatLen);", // 29
			"  ", // 30
			"  /*zufaellige neue EK generieren und aufteilen*/", // 31
			"  Literal rndLit = chooseLiteral(clauseSet);", // 32
			"  Set<Set<Literal>> splitCS1 = new HashSet<>(clauseSet);", // 33
			"  splitCS1.add(createClause(rndLit));", // 34
			"  Set<Set<Literal>> splitCS2 = new HashSet<>(clauseSet);", // 35
			"  splitCS2.add(createClause(Literal.Neg(rndLit)));", // 36
			"  ", // 37
			"  if(dp(splitCS1))", // 38
			"    return true;", // 39
			"  else if(dp(splitCS2))", // 40
			"    return true;", // 41
			"  else", // 42
			"    return false;", // 43
			"}", // 44

	};

	private final String[] ending1True = {
			"Der Algorithmus terminiert mit 'true', d. h.",
			"es existiert also eine Variablenbelegung, die",
			"die ganze Formel wahr macht.", "" };

	private final String[] ending1False = {
			"Der Algorithmus terminiert mit 'false', d. h.",
			"es existiert also keine Variablenbelegung, die",
			"die ganze Formel wahr macht.", "" };

	private final String[] ending2 = {

			"Man koennte den Algorithmus beispielsweise noch so modifizieren,",
			"dass er ggf. die Belegung am Ende mit ausgibt.",
			"",
			"Da SAT zur Komplexitaetsklasse NP gehoert, laesst sich das Problem (auf herkoemmlichen Rechnern) nur in exponentieller Zeit entscheiden.",
			"Das bedeutet in diesem Fall, dass Variablenbelegungen einfach",
			"'durchprobiert' werden muessen. Zwar nicht ganz blindlings (denn zunaechst werden ja immer Variablen",
			"mit nur einer Belegungsmoeglichkeit gesucht (Einheitsklauseln),",
			"jedoch wird bei der Generierung der neuen",
			"Klauselmengen splitCS1 und splitCS2 willkuerlich ein Literal gewaehlt wird, und fuer dieses dann einfach beide",
			"binaere Belegungsmoeglichkeiten durchprobiert.",
			"",
			"Eine optimiertere Version des Verfahrens ist der Davis-Putnam-Logemann-Loveland-Algorithmus (DPLL)." };

//	@Override
//	public String toString() {
//		return lang.getAnimationCode();
//	}

	private static Set<Set<Literal>> stringToClauseSet(String[][] cs) {
		Set<Set<Literal>> clauseSet = new HashSet<Set<Literal>>();
		for (String[] cl : cs) {
			Set<Literal> clause = new HashSet<Literal>();
			for (String l : cl) {
				if (l == null || l.isEmpty())
					continue;
				Literal lit = Literal.FromString(l);
				clause.add(lit);
			}
			clauseSet.add(clause);
		}
		return clauseSet;
	}

	/* GENERATED CODE BEGINS HERE */

	private Language lang;
	private String[][] Klauselmenge;
	private ArrayProperties KlauselmengeFarben;
	private SourceCodeProperties CodeFarben;
	private SourceCodeProperties LokaleVariablenFarben;
	private TextProperties UeberschriftFarben;
	private RectProperties UeberschriftKasten;

	public void init() {
		lang = new AnimalScript("Davis-Putnam-Verfahren [DE]",
				"Magnus Brand,Bastian de Groot", 800, 600);
	}

	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		Klauselmenge = (String[][]) arg1.get("Klauselmenge");
		KlauselmengeFarben = (ArrayProperties) arg0
				.getPropertiesByName("KlauselmengeFarben");
		CodeFarben = (SourceCodeProperties) arg0
				.getPropertiesByName("CodeFarben");
		LokaleVariablenFarben = (SourceCodeProperties) arg0
				.getPropertiesByName("LokaleVariablenFarben");
		UeberschriftFarben = (TextProperties)arg0.getPropertiesByName("UeberschriftFarben");
		UeberschriftKasten = (RectProperties)arg0.getPropertiesByName("UeberschriftKasten");
		/* set colors */
    
		/* parse clause set, make settings, compute */
		genDavisPutnamASCode(stringToClauseSet(Klauselmenge),
				KlauselmengeFarben, CodeFarben, LokaleVariablenFarben, UeberschriftFarben, UeberschriftKasten);

		/*LOG FOR DEBUGGING*/
		FileWriter fw = null;
		try {
			fw = new FileWriter("dp.asu");
			fw.write(lang.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lang.toString();
	}

	public String getName() {
		return "Davis-Putnam-Verfahren [DE]";
	}

	public String getAlgorithmName() {
		return "Davis-Putnam-Verfahren";
	}

	public String getAnimationAuthor() {
		return "Magnus Brand, Bastian de Groot";
	}

	public String getDescription() {
		return "Das Davis-Putnam-Verfahren kann das Erf&uuml;llbarkeitsproblem (SAT) einer aussagenlogischen Formel in konjunktiver Normalform entscheiden. Das bedeutet, dass der Algorithmus 'true' liefert, falls eine Variablenbelegung gefunden werden kann, f&uuml;r die die Formel zu wahr auswertet. Andernfalls liefert er 'false'. "
				+ "<br/><br/>"
				+ "Die Formel selbst wird als Klauselmenge dargestellt. Eine Klausel ist dabei eine Disjunktion von Literalen (Variable oder ihr Negat) und wird ebenfalls als Menge abgebildet. Die Eingabe des Algorithmus' ist also eine Menge von Mengen. <b>In Animal wird die Eingabe ueber ein zweidimensionales Array von Strings realisiert. In der Anzeige von Animal bildet eine Zeile jeweils eine Klausel. In einer Zelle steht ein Literal, ausgedr&uuml;ckt durch einen einzelnen Buchstaben; zur Negation verwendet man '~' als Praefix.</b>"
				+ "<br/><br/>"
				+ "Der Algorithmus wendet wenige, einfache Regeln an, um Variablen mit nur einer m&ouml;glichen Belegung zu finden (Literale aus sog. Einheitsklauseln). Gelingt ihm das nicht, probiert er beide m&ouml;glichen Belegungen aus.";
	}

	public String getCodeExample() {
		return "public static boolean dp(Set<Set<Literal>> clauseSet) {"
				+ "\n"
				+ "  int flatLen;"
				+ "\n"
				+ "  do {"
				+ "\n"
				+ "    flatLen = flatLen(clauseSet);"
				+ "\n"
				+ "    Set<Literal> unitClauses = new HashSet<>();"
				+ "\n"
				+ "    for(Set<Literal> clause : clauseSet ) {"
				+ "\n"
				+ "      if(clause.size() == 1)"
				+ "\n"
				+ "        unitClauses.add(clause.iterator().next());/*Einheitsklausel (EK) merken*/"
				+ "\n"
				+ "    }"
				+ "\n"
				+ "    for(Literal lit : unitClauses) {"
				+ "\n"
				+ "      for(Iterator<Set<Literal>> clauseIt = clauseSet.iterator(); clauseIt.hasNext(); ) {"
				+ "\n"
				+ "        Set<Literal> clause = clauseIt.next();"
				+ "\n"
				+ "        if(clause.contains(lit))"
				+ "\n"
				+ "          /*Klausel, die Literal von EK enthaelt, loeschen*/"
				+ "\n"
				+ "          clauseIt.remove();"
				+ "\n"
				+ "        else"
				+ "\n"
				+ "          for(Iterator<Literal> litIt = clause.iterator(); litIt.hasNext(); )"
				+ "\n"
				+ "            if(litIt.next().equals(Literal.Neg(lit)))"
				+ "\n" + "              /*Negiertes Literal aus EK loeschen*/"
				+ "\n" + "              litIt.remove();" + "\n" + "      }"
				+ "\n" + "    }" + "\n" + "      " + "\n"
				+ "    if(clauseSet.isEmpty())" + "\n" + "      return true;"
				+ "\n" + "    else if(containsEmptyClause(clauseSet))" + "\n"
				+ "      return false;" + "\n" + "  }" + "\n"
				+ "  /*ueberpruefe Veraenderung der Gesamtzahl der Literale*/"
				+ "\n" + "  while(flatLen(clauseSet) != flatLen);" + "\n"
				+ "  " + "\n"
				+ "  /*zufaellige neue EK generieren und aufteilen*/" + "\n"
				+ "  Literal rndLit = chooseLiteral(clauseSet);" + "\n"
				+ "  Set<Set<Literal>> splitCS1 = new HashSet<>(clauseSet);"
				+ "\n" + "  splitCS1.add(createClause(rndLit));" + "\n"
				+ "  Set<Set<Literal>> splitCS2 = new HashSet<>(clauseSet);"
				+ "\n" + "  splitCS2.add(createClause(Literal.Neg(rndLit)));"
				+ "\n" + "  " + "\n" + "  if(dp(splitCS1))" + "\n"
				+ "    return true;" + "\n" + "  else if(dp(splitCS2))" + "\n"
				+ "    return true;" + "\n" + "  else" + "\n"
				+ "    return false;" + "\n" + "}";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_BACKTRACKING);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	public static void main(String[] args) {
		/* test input */
		// Set<Set<Literal>> clauseSet = new HashSet<Set<Literal>>();
		// clauseSet.add(createClause(Literal.Pos('P'), Literal.Pos('Q')));
		// clauseSet.add(createClause(Literal.Neg('Q'), Literal.Pos('R')));
		// clauseSet.add(createClause(Literal.Pos('R')));

		DavisPutnam gen = new DavisPutnam();

		/* clause set */
		String[][] clauseSetStr = { { "a", "b" }, { "~c", "d" }, { "e" } };
		Hashtable<String, Object> clauseSet = new Hashtable<String, Object>();
		clauseSet.put("Klauselmenge", clauseSetStr);
		/* clause set property */
		ArrayProperties clausetSetVProps = new ArrayProperties(
				"KlauselmengeFarben");
		clausetSetVProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);
		// clausetSetVProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
		// Color.YELLOW);
		clausetSetVProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		clausetSetVProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		clausetSetVProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		clausetSetVProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.RED);
		/* code props */
		SourceCodeProperties codeProps = new SourceCodeProperties("CodeFarben");

		codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.RED);
		codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		/* local variables props */
		SourceCodeProperties locVarProps = new SourceCodeProperties(
				"LokaleVariablenFarben");

		locVarProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 12));
		locVarProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		AnimationPropertiesContainer propsCont = new AnimationPropertiesContainer();
		propsCont.add(clausetSetVProps);
		propsCont.add(codeProps);
		propsCont.add(locVarProps);

		gen.init();

		try{
		  Writer fw = new OutputStreamWriter(new FileOutputStream(
				"DPGOutput.asu"), Charset.forName("UTF-8"));
			fw.write(gen.generate(propsCont, clauseSet));
			fw.close();
		} catch (IOException e) {
			System.err.println("could not write to file");
		}
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		/* check if all literals can be parsed */
		int col = 0, row = 0;
		String[][] clauseSetAsString = (String[][]) arg1.get("Klauselmenge");
		for (String[] clauseAsString : clauseSetAsString) {
			col = 0;
			for (String litAsString : clauseAsString) {
				if (!litAsString.isEmpty() && !Literal.CanParse(litAsString)) {/*skip empty strings*/
					JOptionPane
							.showMessageDialog(
									null,
									String.format(
											"Klauselmenge hat ungültigen Eintrag (%s) in Zeile %d, Spalte %d. Siehe Beschreibung für korrektes Format.",
											litAsString, row, col));
					return false;
				}
				col++;
			}
			row++;
		}
		return true;
	}
}