package generators.hashing;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author Andreas Schaller & Daniel Fath
 * @version 1.1 2010-08-04
 */
public class DoubleHashing extends AnnotatedAlgorithm implements Generator {

//	private static final String AlgorithmName = "Double Hashing";
//	private static final String AnimationAuthor = "Andreas Schaller & Daniel Fath";
	
	private static final String deleteCommand = "delete";
	private static final String insertCommand = "insert";
	
	private int[] hashSpaceDeleted;
	private String[] hashSpace;

	
	//AnimData
	private Timing defaultTiming = new TicksTiming(50);
	private ArrayMarkerUpdater arrayMU;
	private ArrayMarker marker;

	private StringArray hashed;
	
	
	public void insert(int x) {
		exec("insertheader"); lang.nextStep();
		exec("insertpos"); lang.nextStep();
		int pos = search(x);
		vars.set("pos", String.valueOf(pos));
		this.arrayMU.setVariable(vars.getVariable("pos")); lang.nextStep();
		
		exec("insertif"); lang.nextStep();
		if(pos != -1) {
			exec("insert"); lang.nextStep();
			this.hashSpace[pos] = String.valueOf(x);
			hashed.put(pos, String.valueOf(x), null, null);
			exec("undelete"); lang.nextStep();
			this.hashSpaceDeleted[pos] = 0;
			hashed.unhighlightElem(pos, null, null);
		} else {
			exec("insertelse"); lang.nextStep();
			//else fehler ueberlauf hier unbeachtet
		}
	}
	
	public void delete(int x) {
		exec("deleteheader"); lang.nextStep();
		exec("delpos"); lang.nextStep();
		int pos = search(x);
		vars.set("pos", String.valueOf(pos));
		this.arrayMU.setVariable(vars.getVariable("pos")); lang.nextStep();
		
		exec("delif"); lang.nextStep();
		if(pos != -1 && this.hashSpace[pos] != null && this.hashSpace[pos].equals(String.valueOf(x))) {
			exec("deleteend"); lang.nextStep();
			this.hashSpaceDeleted[pos] = 1;
			hashed.highlightElem(pos, null, null);
		}
	}
	
	private int search(int x) {
		exec("searchheader"); lang.nextStep();
		exec("searchi"); lang.nextStep();
		int i = 0;
		exec("searchwhile"); lang.nextStep();
		while(i < this.hashSpace.length) {
			exec("pos"); lang.nextStep();
			int j = hash(x, i);

			exec("searchif"); lang.nextStep();
			if(this.hashSpace[j] == null || this.hashSpace[j].equals(String.valueOf(x)) || this.hashSpaceDeleted[j] == 1) {
				exec("searchendj"); lang.nextStep();
				return j;
			} else {
				exec("inci"); lang.nextStep();
				i++;
			}
			exec("searchrepeat"); lang.nextStep();
		}
		exec("searchend"); lang.nextStep();
		return -1;
	}
	
	private int hash(int x, int i) {
		exec("hashheader");
//		vars.set("x", String.valueOf(x));
//		vars.set("i", String.valueOf(i));
		lang.nextStep();
		
		exec("hash1");
		int h_one = x % this.hashSpace.length;
//		vars.set("hash1", String.valueOf(h_one));
		lang.nextStep();
		
		exec("hash2");
		int h_two = 1 + (x % (this.hashSpace.length - 1));
//		vars.set("hash2", String.valueOf(h_two));
		lang.nextStep();
		
		exec("hashend"); lang.nextStep();
		
		return (h_one + (i * h_two)) % this.hashSpace.length;
	}

	
	@Override
	public String generate(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) {
		Text topic = this.lang.newText(new Coordinates(20, 10), "Double Hashing", "Topic", null);
		topic.setFont(new Font("SansSerif", Font.PLAIN, 22), null, null);
		topic.show();
		
		ArrayProperties arrayProps = (ArrayProperties) properties.getPropertiesByName("arrayProps");
		SourceCodeProperties scProps = (SourceCodeProperties) properties.getPropertiesByName("scProps");
		ArrayMarkerProperties arrayMProps = (ArrayMarkerProperties) properties.getPropertiesByName("arrayMProps");
//		arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "pos");
		
		this.hashSpace = new String[Integer.valueOf(primitives.get("Hashgroesse").toString())];
		this.hashed = this.lang.newStringArray(new Coordinates(250, 100), this.hashSpace, "stringArray", null, arrayProps);
		this.marker = this.lang.newArrayMarker(this.hashed, 0, "pos", null, arrayMProps);
		this.arrayMU = new ArrayMarkerUpdater(marker, null, defaultTiming, this.hashed.getLength() - 1);
		
		int[] commands = (int[]) primitives.get("commands");
		
		//first value = insert / delete
		//second value = value to operate with
		String[][] cmds = new String[commands.length][2];
		
		SourceCode actions = this.lang.newSourceCode(new Coordinates(480, 240), "actionCode", null, scProps);
		
		for (int i = 0; i < commands.length; i++) {
			cmds[i][0] = (commands[i] < 0) ? deleteCommand : insertCommand;
			cmds[i][1] = String.valueOf(Math.abs(commands[i]));
			actions.addCodeLine(cmds[i][0] + "(" + cmds[i][1] + ")", null, 0, null);
		}
		vars.declare("int", "pos");
		this.hashSpaceDeleted = new int[this.hashSpace.length];
		
		this.lang.nextStep();
		
		for(int i = 0; i < commands.length; i++) {
			if(cmds[i][0] != null) {
				actions.highlight(i);
				if(cmds[i][0].startsWith(deleteCommand)) {
					lang.nextStep("delete(" + cmds[i][1] + ")");
					this.delete(Integer.valueOf(cmds[i][1]));
				} else if(cmds[i][0].startsWith(insertCommand)) {
					lang.nextStep("insert(" + cmds[i][1] + ")");
					this.insert(Integer.valueOf(cmds[i][1]));
				}
				actions.unhighlight(i);
			}
		}
		
		return this.lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Doppeltes Hashing";
	}

	@Override
	public String getAnimationAuthor() {
		return "Andreas Schaller, Daniel Fath";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Beim Doppelstreuwertverfahren oder Doppel-Hashing (engl. double hashing) " +
			"handelt es sich um eine Methode zur Realisierung eines geschlossenen Hash-Verfahrens.\n" +
			"In geschlossenen Hash-Verfahren wird versucht, " +
			"Überläufer in der Hash-Tabelle unterzubringen anstatt sie innerhalb der Zelle (z. B. als Liste) " +
			"zu speichern.\n" + 
			"\n\nIn Diesem Beispiel werden positive Zahlen als Eingabe betrachtet.\n" +
			"Negative Zahlen werden als delete-Befehl betrachtet.";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}

	@Override
	public String getName() {
		return "Double Hashing";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public String getAnnotatedSrc() {
		return 	"function search(T, k)								@label(\"searchheader\") @highlight(\"searchend\") @highlight(\"searchendj\") @declare(\"int\", \"k\")\n" +
				"	i <- 0											@label(\"searchi\") @declare(\"int\", \"i\") @set(\"i\", \"0\")\n" +
				"	repeat											@label(\"searchwhile\") @declare(\"int\", \"j\")\n" +
				"			j <- hash(T, k, i)						@label(\"pos\")\n" +
				"			if T[j] = NIL or T[j] = k				@label(\"searchif\")\n" +
				"				then return j						@label(\"searchendj\")\n" +
				"			else i <- i + 1							@label(\"inci\") @inc(\"i\")\n" +
				"		until T[j] = NIL or i = m					@label(\"searchrepeat\")\n" +
				"	return NIL										@label(\"searchend\")\n" +
				"\n" +
				"function insert(T, k)								@label(\"insertheader\") @openContext @declare(\"int\", \"k\")\n" +
				"	pos <- search(T, k)								@label(\"insertpos\")\n" + //@declare(\"int\", \"pos\")
				"	if T[pos] = NIL or T[pos] is marked as deleted	@label(\"insertif\")\n" +
				"		then T[pos] <- k							@label(\"insert\")\n" +
				"			mark T[pos] as undeleted				@label(\"undelete\") @closeContext\n" +
				"	else error 'Überlauf der Hashtabelle'			@label(\"insertelse\") @closeContext\n" +
				"\n" +
				"function delete(T, k)								@label(\"deleteheader\") @highlight(\"deleteend\") @openContext @declare(\"int\", \"k\")\n" +
				"	pos <- search(T, k)								@label(\"delpos\")\n" + //@declare(\"int\", \"pos\")
				"	if T[pos] = k									@label(\"delif\")\n" +
				"		then mark T[pos] as deleted					@label(\"deleteend\") @closeContext\n" +
				"\n" +
				"function hash(T, k, i)								@label(\"hashheader\") @highlight(\"hashend\") @declare(\"int\", \"k\") @declare(\"int\", \"i\")\n" +
				"	h_one <- k % length(T)							@label(\"hash1\") @declare(\"int\", \"h_one\")\n" +
				"	h_two <- 1 + (k % (length(T) - 1))				@label(\"hash2\") @declare(\"int\", \"h_two\")\n" +
				"	return (h_one + (i * h_two)) % length(T)		@label(\"hashend\")";
	}

	@Override
	public void init() {
		super.init();
		
		SourceCodeProperties props = new SourceCodeProperties();
		props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
		props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);

		// instantiate source code primitive to work with
		sourceCode = lang.newSourceCode(new Coordinates(20, 25), "sumupCode", null, props);
		
		// parsing anwerfen
		parse();
	}
}
