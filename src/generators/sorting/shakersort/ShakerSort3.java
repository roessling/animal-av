package generators.sorting.shakersort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class ShakerSort3 implements generators.framework.Generator {

	
	// Pointer einstellungen
	ArrayMarkerProperties linksP = new ArrayMarkerProperties();
	ArrayMarkerProperties rechtsP = new ArrayMarkerProperties();
	ArrayMarkerProperties fertigP = new ArrayMarkerProperties();
	ArrayMarkerProperties iP = new ArrayMarkerProperties();
	
	
	//Private Variablen fuer Algo Ausfuehrung
	private Language lang;
	
	private ArrayMarker linkerpointer;
	private ArrayMarker rechterPointer;
	private ArrayMarker fertigPointer;
	private ArrayMarker iPointer;
	
	private SourceCode sc;
	
	private algoanim.primitives.Text tausche;
	
	private String[] a;
	
	
	private StringArray init(String[] a) {
	    // now, create the source code entity
	    SourceCodeProperties scProps = new SourceCodeProperties();
	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
	        Font.PLAIN, 12));

	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
	        Color.RED);   
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		this.sc = lang.newSourceCode(new Coordinates(30, 40), "sourceCode",
        null, scProps);
		sc.addCodeLine("static <E extends Comparable<? super E>> void shakerSort(E[] sammlung) {", "label", 0, null);
		sc.addCodeLine("    boolean austausch;", null, 0, null);
		sc.addCodeLine("    int links = 1;", null, 0, null);
		sc.addCodeLine("    int rechts = sammlung.length-1;", null, 0, null);
		sc.addCodeLine("    int fertig = rechts;", null, 0, null);
		sc.addCodeLine("    do {", null, 0, null);
		sc.addCodeLine("        austausch = false;", null, 0, null);
		sc.addCodeLine("        for (int i = rechts; i >= links; i--) {", null, 0, null);
		sc.addCodeLine("            if (sammlung[i].compareTo(sammlung[i-1]) < 0) {", null, 0, null);
		sc.addCodeLine("                austausch = true; ", null, 0, null);
		sc.addCodeLine("                fertig = i;", null, 0, null);
		sc.addCodeLine("                final E temp = sammlung[i-1];", null, 0, null);
		sc.addCodeLine("                sammlung[i-1] = sammlung[i];", null, 0, null);
		sc.addCodeLine("                sammlung[i] = temp;", null, 0, null);
		sc.addCodeLine("            }", null, 0, null);
		sc.addCodeLine("        }", null, 0, null);
		sc.addCodeLine("        links = fertig + 1;", null, 0, null);
		sc.addCodeLine("        for (int i = links; i <= rechts; i++) {", null, 0, null);
		sc.addCodeLine("            if (sammlung[i].compareTo(sammlung[i-1]) < 0) {", null, 0, null);
		sc.addCodeLine("                austausch = true;", null, 0, null);
		sc.addCodeLine("                fertig = i;", null, 0, null);
		sc.addCodeLine("                final E temp = sammlung[i-1];", null, 0, null);
		sc.addCodeLine("                sammlung[i-1] = sammlung[i];", null, 0, null);
		sc.addCodeLine("                sammlung[i] = temp;", null, 0, null);
		sc.addCodeLine("            }", null, 0, null);
		sc.addCodeLine("        }", null, 0, null);
		sc.addCodeLine("        rechts = fertig - 1;", null, 0, null);
		sc.addCodeLine("    } while (austausch);", null, 0, null);
		sc.addCodeLine(" }", null, 0, null);

		
		
		
		algoanim.primitives.Text header = lang.newText(new Coordinates(20, 30), "ShakerSort", "ShakerSort", null);
		header.setFont(new Font("SansSerif",1,22), null, null);
		
		this.tausche = lang.newText(new Coordinates(450,250), "austausch = ", "austausch = ", null);
		tausche.setFont(new Font("SansSerif",1,16), null, null);
		
		// Properties fuer das Array
	    ArrayProperties arrayProps = new ArrayProperties();
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
	        Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
	        Color.BLUE);
	    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
	        Color.YELLOW);
	    
		StringArray arr = lang.newStringArray(new Coordinates(450,220), a, "strArray", null, arrayProps);
		

	    this.linkerpointer = lang.newArrayMarker(arr, 0, "l", null, linksP);
	    this.linkerpointer.hide();
	    
	    // Pointer rechts erstellen
	    this.rechterPointer = lang.newArrayMarker(arr, a.length-1, "r", null, rechtsP);
	    this.rechterPointer.hide();
	    
	    // Pointer fertig erstellen
	    this.fertigPointer = lang.newArrayMarker(arr, a.length-1, "f", null, fertigP);
	    this.fertigPointer.hide();
	    
	    // Fertig Pointer
	    this.iPointer = lang.newArrayMarker(arr, a.length-1, "i", null, iP);
	    this.iPointer.hide();
	    
		return arr;
	}
	
	public void sort(String[] a) {
		int i = 1;
		int help = 0; //hilfsvariable fuer animation
		StringArray arr = this.init(a);	
		sc.highlight(0,0,false);
		lang.nextStep("Initialisierung help = 0");
		sc.unhighlight(0);
		boolean austausch;
		int links = 1;
		this.linkerpointer.show();
		this.linkerpointer.move(links, null, new TicksTiming(30));
		sc.highlight(1);
		sc.highlight(2);
		lang.nextStep();
		sc.unhighlight(1);
		sc.unhighlight(2);
		sc.highlight(3);
	    int rechts = a.length-1;
	    this.rechterPointer.show();
	    this.rechterPointer.move(rechts, null, new TicksTiming(30));
	    lang.nextStep();
	    sc.unhighlight(3);
	    sc.highlight(4);
	    int fertig = rechts;
	    this.fertigPointer.show();
	    this.fertigPointer.move(fertig, null, new TicksTiming(30));
	    lang.nextStep();
	    sc.unhighlight(4);
	    sc.highlight(5);
	    lang.nextStep();
	    sc.unhighlight(5);
	    do {
	        austausch = false;
	        this.tausche.setText("austausch = false", null, null);
	        sc.highlight(6);
	        lang.nextStep("Step " + i);
	        i++;
	        for (int ab = rechts; ab >= links; ab--) {
	        	sc.highlight(7);
	        	sc.highlight(8);
	        	lang.nextStep();
	        	sc.unhighlight(6);
	        	sc.unhighlight(7);
	        	sc.highlight(9);
	        	sc.highlight(10);
	        	this.iPointer.show();
	        	this.iPointer.move(ab, null, new TicksTiming(30));
	        	lang.nextStep();
	        	sc.unhighlight(8);
	        	sc.unhighlight(9);
	        	sc.unhighlight(10);
	            if (a[ab].compareTo(a[ab-1]) < 0) {
	                austausch = true; 
	                this.tausche.setText("austausch = true", null, null);
	                fertig = ab;
	                this.fertigPointer.move(fertig, null, new TicksTiming(30));
	                lang.nextStep();
	                //String temp = a[ab-1];
	                //a[ab-1] = a[ab];
	                //a[ab] = temp;
	                arr.swap(ab-1, ab, null, new TicksTiming(100));
	                sc.highlight(11);
	                sc.highlight(12);
	                sc.highlight(13);
	                lang.nextStep();
	                sc.unhighlight(11);
	                sc.unhighlight(12);
	                sc.unhighlight(13);
	            }
	            help = ab;
	        }
	        //arr.highlightCell(help, null, null);
	        //System.out.println(help);
	        arr.highlightCell(0, help-1, null, null);
	        lang.nextStep();
	        links = fertig + 1;
	        sc.highlight(16);
	        this.linkerpointer.move(links-1, null, new TicksTiming(100));
	        lang.nextStep();
	        sc.unhighlight(16);
	        
	        for (int auf = links; auf <= rechts; auf++) {
	            lang.nextStep();
	            sc.highlight(17);
	            this.iPointer.move(auf, null, new TicksTiming(30));
	            lang.nextStep();
	            sc.unhighlight(17);
	            sc.highlight(18);
	            sc.highlight(19);
	            // aufwärts
	            if (a[auf].compareTo(a[auf-1]) < 0) {
	                austausch = true;
	                this.tausche.setText("austausch = true", null, null);
	                fertig = auf;
	                lang.nextStep();
		            sc.unhighlight(18);
		            sc.unhighlight(19);
	                sc.highlight(20);
	                this.fertigPointer.move(fertig, null, new TicksTiming(30));
	                lang.nextStep();
	                sc.unhighlight(20);
	                sc.highlight(21);
	                sc.highlight(22);
	                sc.highlight(23);
	                //String temp = a[auf-1];
	                //a[auf-1] = a[auf];
	                //a[auf] = temp;
	                arr.swap(auf-1, auf, null, new TicksTiming(100));
	                lang.nextStep();
	                sc.unhighlight(21);
	                sc.unhighlight(22);
	                sc.unhighlight(23);
	            }
	            lang.nextStep();
	            sc.unhighlight(18);
	            sc.unhighlight(19);
	            help = auf;
	        }
	        arr.highlightCell(help, arr.getLength()-1, null, null);
	        //System.out.println(help);
	        sc.highlight(26);
	        rechts = fertig - 1;
	        this.rechterPointer.move(rechts, null, new TicksTiming(30));
	        lang.nextStep();
	        sc.unhighlight(26);
	    } while (austausch);
	    sc.highlight(28);
	    sc.unhighlight(6);
	    sc.unhighlight(7);
	    sc.unhighlight(8);
	    //for(int i = 0; i < a.length; i++) System.out.println(a[i]);
	}
	
	
	public ShakerSort3() {
	}
	
	public ShakerSort3(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
	}
	
	
	@Override
	public String generate(AnimationPropertiesContainer arg0,Hashtable<String, Object> arg1) {
			init();
			if(arg1.get("arr") != null) {
				this.a = (String[]) arg1.get("arr");
				this.linksP = (ArrayMarkerProperties) arg0.getPropertiesByName("linksP");
				this.rechtsP = (ArrayMarkerProperties) arg0.getPropertiesByName("rechtsP");
				this.iP = (ArrayMarkerProperties) arg0.getPropertiesByName("iP");
				this.fertigP = (ArrayMarkerProperties) arg0.getPropertiesByName("fertigP");	
			} else {
				this.a = new String[]{"D","H", "K", "G","J" , "B", "I", "A", "F", "E", "C"};

				// Pointer links erstellen
				this.linksP = new ArrayMarkerProperties();
				linksP.setName("linksP");
			    linksP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "l");   
			    linksP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
			    
			    // Pointer rechts erstellen
			    this.rechtsP = new ArrayMarkerProperties();
			    rechtsP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "r");   
			    rechtsP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
			    
				// Pointer fertig erstellen
				this.fertigP = new ArrayMarkerProperties();
			    fertigP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "f");   
			    fertigP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			    
			    // i Pointer erstellen
			    this.iP = new ArrayMarkerProperties();
			    iP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");   
			    iP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
			}
				
			
		this.sort(this.a);
		
		return this.lang.toString();
	}
		
	@Override
	public String getAlgorithmName() {
		return "Shaker Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Tarik Tahiri, Philipp Sowinski";
	}

	@Override
	public String getCodeExample() {
		String code = "static &lt;E extends Comparable&lt;? super E&gt;&gt; void shakerSort(E[] sammlung) {\n" +
						"    boolean austausch;\n" +
						"    int links = 1;\n" +
						"    int rechts = sammlung.length-1;\n" +
						"    int fertig = rechts;\n" +
						"    do {\n" +
						"        austausch = false;\n" +
						"        for (int i = rechts; i &gt;= links; i--) {\n" +
						"            if (sammlung[i].compareTo(sammlung[i-1]) &lt; 0) {\n" +
						"                austausch = true; \n" +
						"                fertig = i;\n" +
						"                final E temp = sammlung[i-1];\n" +
						"                sammlung[i-1] = sammlung[i];\n" +
						"                sammlung[i] = temp;\n" +
						"            }\n" +
						"        }\n" +
						"        links = fertig + 1;\n" +
						"        for (int i = links; i &lt;= rechts; i++) {\n" +
						"            if (sammlung[i].compareTo(sammlung[i-1]) &lt; 0) {\n" +
						"                austausch = true;\n" +
						"                fertig = i;\n" +
						"                final E temp = sammlung[i-1];\n" +
						"                sammlung[i-1] = sammlung[i];\n" +
						"                sammlung[i] = temp;\n" +
						"            }\n" +
						"        }\n" +
						"        rechts = fertig - 1;\n" +
						"    } while (austausch);\n" +
						" }\n";
		return code;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Shaker Sort animation mit Pointern auf Array und Java Code.";
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return "Shaker Sort";
	}

	@Override
	public String getOutputLanguage() {
		return JAVA_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript("ShakerSort", "Philipp Sowinski; Tarik Tahiri", 800, 600);

		lang.setStepMode(true);

	}

}
