/*
 * ZellersKongruenz.java
 * Florian Sunnus, Elvir Sinancevic, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import java.util.Locale;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;

public class ZellersKongruenz implements ValidatingGenerator {
		
	private Language lang;
	// Hauptvariablen
	private int day;
	private int month;
	private int year;
	private boolean forceWrongCalendertype;
	// Variablen f�r Properties
	TextProperties textPropsHeader = new TextProperties();
	TextProperties textPropsDescr = new TextProperties();
	TextProperties generalTextColor = new TextProperties();
	TextProperties weekdaysColor = new TextProperties();
	TextProperties weekdaysHighlightColor = new TextProperties();
	ArrayMarkerProperties hMarker = new ArrayMarkerProperties();
	SourceCodeProperties mainSourceCode = new SourceCodeProperties();
	SourceCodeProperties commentCode = new SourceCodeProperties();
	RectProperties hRectProperties = new RectProperties();
	RectProperties dRectProperties = new RectProperties();
	// Timing
	public final static Timing  defaultDuration = new TicksTiming(30);
	// Variablen die bei Berechnungen usw. genutzt werden
	private String[] wochentage;
	private String[] monate;
	private int wochentag; // Das Ergebnis als int
	private boolean isGregorian = false; // Gregorianisch -> true, julianisch -> false
	//Translator
	private Translator translator;
	private Locale locale;
	
    /**
     * Constructor, handles the selected language
     * @param path path of the language files
     * @param locale locale that is selected
     */
    public ZellersKongruenz(String path, Locale locale){
    	
    	this.locale = locale;
        translator = new Translator(path, locale);    	
    }
	
	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		boolean validInputs = true;
		day = (Integer) arg1.get("day");
		month = (Integer) arg1.get("month");
		year = (Integer) arg1.get("year");
		forceWrongCalendertype = (Boolean) arg1.get("calendertype");
		if(day < 0 || month < 0 || day > 31 || month > 12 || year < 0
				|| (month == 2 && istSchaltjahr(year) && day > 29)
					|| (month == 2 && !istSchaltjahr(year) && day > 28)	
				){
			validInputs = false;
			// Die Validierung der Textproperties ist an dieser Stelle nicht n�tig, "falsche" Eingaben gibt es nicht
		}
		if(day < 0 || day > 31){
			throw new IllegalArgumentException(translator.translateMessage("error1"));
		} else if(month < 0 || month > 12){
			throw new IllegalArgumentException(translator.translateMessage("error2"));
		} else if(year < 0){
			throw new IllegalArgumentException(translator.translateMessage("error3"));
		} else if(month == 2 && istSchaltjahr(year) && day > 29){
			throw new IllegalArgumentException(translator.translateMessage("error4"));
		} else if (month == 2 && !istSchaltjahr(year) && day > 28){
			throw new IllegalArgumentException(translator.translateMessage("error5"));
		}
		return validInputs;
	}

	public void init() {
		lang = new AnimalScript(translator.translateMessage("name"), "Florian Sunnus, Elvir Sinancevic", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		wochentage = new String[7];
		monate = new String[12];
		for(int i = 0; i < 7; i++){
			wochentage[i] = translator.translateMessage("wochentag" + i);
		}
		for(int i = 0; i < 12; i++){
			monate[i] = translator.translateMessage("monat" + i);
		}
		
		if(validateInput(props,primitives)){
			//Variables
			day = (Integer) primitives.get("day");
			month = (Integer) primitives.get("month");
			year = (Integer) primitives.get("year");
			forceWrongCalendertype = (Boolean) primitives.get("calendertype");
			//Properties
			textPropsHeader = (TextProperties)props.getPropertiesByName("HeaderColor");
			textPropsDescr = (TextProperties)props.getPropertiesByName("DescriptionColor");
			generalTextColor = (TextProperties)props.getPropertiesByName("GeneralCalenderTextColor");
			weekdaysColor = (TextProperties)props.getPropertiesByName("WeekdaysColor");
			weekdaysHighlightColor = (TextProperties)props.getPropertiesByName("WeekdaysHighlightColor");
			hMarker = (ArrayMarkerProperties)props.getPropertiesByName("hMarker");
			mainSourceCode = (SourceCodeProperties)props.getPropertiesByName("MainSourceCodeColor");
			commentCode = (SourceCodeProperties)props.getPropertiesByName("CommentCodeColor");
			hRectProperties = (RectProperties) props.getPropertiesByName("HeaderBackground");
			dRectProperties = (RectProperties) props.getPropertiesByName("DateHighlightBackground");
		}else{
			//...
		}
		// Beginne mit der Rechnung
		calculate(day, month, year);
		return lang.toString();
	}

	public String getName() {
		return translator.translateMessage("name");
	}

	public String getAlgorithmName() {
		return translator.translateMessage("algorithmName");
	}

	public String getAnimationAuthor() {
		return "Florian Sunnus, Elvir Sinancevic";
	}

	public String getDescription() {
		return translator.translateMessage("description");
	}

	public String getCodeExample() {
		return "public int "+translator.translateMessage("algorithmName2")+"(int d, int m, int y) {" + "\n" + "	boolean isGregorian = false;" + "\n"
				+ "	if ((y > 1582) || (y == 1582 && m > 10) || (y == 1582 && m == 10 && d >= 15)) {" + "\n"
				+ "		isGregorian = true;" + "\n" + "	}" + "\n"
				+ "	int day = d; int month = m; int year = y; int h = 0;" + "\n" + "	if (month == 1 || month == 2) {"
				+ "\n" + "		month += 12;" + "\n" + "		year -= 1;" + "\n" + "	}" + "\n"
				+ "	int k = year % 100;" + "\n" + "	int j = year / 100;" + "\n" + "	h = day;" + "\n"
				+ "	h += ((month + 1) * 13) / 5;" + "\n" + "	h += k;" + "\n" + "	h += k / 4;" + "\n"
				+ "	if (isGregorian) {" + "\n" + "		h += j / 4;" + "\n" + "		h -= 2 * j;" + "\n" + "	} else {"
				+ "\n" + "		h += 5;" + "\n" + "		h -= j;" + "\n" + "	}" + "\n" + "	h = h % 7;" + "\n"
				+ "	return h;" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
	
	public static boolean istSchaltjahr(int y) {
		// Just to check if we have a leap-year
		if (y % 4 == 0) {
			if (y % 100 == 0 && y % 400 == 0) {
				return true;
			} else {
				if (y % 100 == 0 && y % 400 != 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public void calculate(int d, int m, int y) {
		// Setze den Header
		textPropsHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		Text header = this.lang.newText(new Coordinates(20, 30), translator.translateMessage("name"), "header", null, textPropsHeader);
		
		// Rechteck um den Header
	    @SuppressWarnings("unused")
		Rect headerRect = this.lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header, "SE"), "headerRect", null, hRectProperties);
	    
	    // A small description of the generator/algorithm
	    textPropsDescr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
		@SuppressWarnings("unused")
		Text description = this.lang.newText(new Coordinates(20, 80), translator.translateMessage("description2"), "description", null, textPropsDescr);
		// Make calender highlight bold
		weekdaysHighlightColor.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 13));
		
		// Set the visual properties for the Array
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		
		// For the introduction sentence
		if((y > 1582) || (y == 1582 && m > 10) || (y == 1582 && m == 10 && d >= 15)){
			isGregorian = true;
		}else{
			isGregorian = false;
		}
		if(forceWrongCalendertype){
			isGregorian = !isGregorian;
		}
		
		// Introduction sentence
		String text;
		if(isGregorian){
			text = translator.translateMessage("greg2");
		}else{
			text = translator.translateMessage("jul2");
		}
		Text intro = lang.newText(new Coordinates(20,120), translator.translateMessage("description3") + d + "." + m + "." + y
				+" "+translator.translateMessage("e1") + " " + text + " "+ translator.translateMessage("e2"),"introsatz", null, textPropsDescr);
		Text intro2 = lang.newText(new Coordinates(20,150), "","intro2", null, textPropsHeader);
		if(forceWrongCalendertype){
			intro2.setText(translator.translateMessage("description4"), null, null);
		}
		lang.nextStep(translator.translateMessage("step1"));

		// Create StringArray linked to the properties and hide intro
		intro.hide();
		intro2.hide();
		StringArray sa = lang.newStringArray(new Coordinates(20, 170), wochentage, "intArray", null, arrayProps);

		// New step after the array was created
		lang.nextStep();

		// Set the visual properties for the source code
		mainSourceCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		
		// Create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(20, 230), "sourceCode", null, mainSourceCode);

		// Add the lines to the SourceCode object
		sc.addCodeLine("public int "+translator.translateMessage("algorithmName2")+"(int d, int m, int y) {", null, 0, null);
		sc.addCodeLine("boolean isGregorian = false;", null, 1, null);
		sc.addCodeLine("if ((y > 1582) || (y == 1582 && m > 10) || (y == 1582 && m == 10 && d >= 15)) {", null, 1, null);
		sc.addCodeLine("isGregorian = true;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("int day = d; int month = m ; int year = y; int h = 0;", null, 1, null);
		sc.addCodeLine("if(month == 1 || month == 2){", null, 1, null);
		sc.addCodeLine("month += 12;", null, 2, null);
		sc.addCodeLine("year -= 1;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("int k = year % 100;", null, 1, null);
		sc.addCodeLine("int j = year / 100;", null, 1, null);
		sc.addCodeLine("h = day;", null, 1, null);
		sc.addCodeLine("h += ((month+1)*13)/5;", null, 1, null);
		sc.addCodeLine("h += k;", null, 1, null);
		sc.addCodeLine("h += k/4;", null, 1, null);
		sc.addCodeLine("if(isGregorian){", null, 1, null);
		sc.addCodeLine("h += j/4;", null, 2, null);
		sc.addCodeLine("h -= 2*j;", null, 2, null);
		sc.addCodeLine("}else{", null, 1, null);
		sc.addCodeLine("h += 5;", null, 2, null);
		sc.addCodeLine("h -= j;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("h = h % 7;", null, 1, null);
		sc.addCodeLine("return h;", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		
		// New step after the source code is displayed
		lang.nextStep();

		// Start the day calculation
		try {
			// sa = Array , sc = Source Code, d = day, m = month, y = year
			calculateZeller(sa, sc, d, m, y);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		
		// Setting the Month, Year and Calendertype
		@SuppressWarnings("unused")
		Text month = this.lang.newText(new Coordinates(1000-15, 210), monate[m-1],"month", null, generalTextColor);
		@SuppressWarnings("unused")
		Text year = this.lang.newText(new Coordinates(1090-15, 210), Integer.toString(y),"year", null, generalTextColor);
		
		if(isGregorian){
			@SuppressWarnings("unused")
			Text type = this.lang.newText(new Coordinates(1000-15, 180), translator.translateMessage("greg"),"type", null, generalTextColor);
		}else{
			@SuppressWarnings("unused")
			Text type = this.lang.newText(new Coordinates(1000-15, 180), translator.translateMessage("jul"),"type", null, generalTextColor);
		}
		
		// First check how many days are in this month
		int totalDays = 0;
		if(m == 1 || m == 3 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12){
			totalDays = 31;
		}else if(m == 4 || m == 6 || m == 9 || m == 11){
			totalDays = 30;
		}else if(m == 2){
			if(istSchaltjahr(y)){
				totalDays = 29;
			}else{
				totalDays = 28;
			}
		}
		
		// Now fill the calender with totalDay-amount of days and save the position of the day we need to highlight
		int k = 0; // How many days did we already fill
		int xVonDatum = 0; // x-Pos of the date we need to highlight
		int yVonDatum = 0; // y-Pos of the date we need to highlight
		for(int i = 0; i < 5; i++){
			for(int j = 0 ; j < 7; j++){
				if(k <= totalDays && k > 0){
					if(k == d){
						// This is the day we need to highlight
						lang.newText(new Coordinates(1000 + 90 * j, 280 + 65 * i), Integer.toString(k),"dates", null, generalTextColor);
						xVonDatum = 1000 + 90 * j;
						yVonDatum = 280 + 65 * i;
					}
					lang.newText(new Coordinates(1000 + 90 * j, 280 + 65 * i), Integer.toString(k),"dates", null, generalTextColor);
					k++;
				}else{
					lang.newText(new Coordinates(1000 + 90 * j, 280 + 65 * i), "-","dates", null, generalTextColor);
					k++;
				}
			}
		}
		
		// Make a new label above the calender in the same column as our date with the correct weekday for our date
		lang.newText(new Coordinates(xVonDatum-10, 242), wochentage[wochentag],"dates", null, weekdaysHighlightColor);
		
		// Hightlight the day of the week
		Coordinates kord2 = new Coordinates(xVonDatum-10, 242);
		@SuppressWarnings("unused")
		Rect WochentagRect = this.lang.newRect(new Offset(-5, -5, kord2, "NW"), new Offset(75, 22, kord2, "SE"), "WochentagRect", null, dRectProperties);
		lang.nextStep();
				
		// Calculate the other weekdays according to the position of our first weekday label, which is above our calculated first date
		int anzahlLinks = 0;
		int anzahlRechts = 0;
		int temp = xVonDatum - 1000;
		while(temp > 0){
			temp -= 90;
			anzahlLinks++;
		}
		anzahlRechts = 6 - anzahlLinks;
		
		// Label the missing weekdays according to their position
		for(int i = 1 ; i <= anzahlLinks; i++){
			int stelle = wochentag - i;
			if(stelle < 0){
				stelle = wochentag + 7 - i;
			}
			lang.newText(new Coordinates(xVonDatum-10-i*90, 242), wochentage[stelle],"dates", null, weekdaysColor);
		}
		for(int i = 1 ; i <= anzahlRechts; i++){
			int stelle = wochentag + i;
			if(stelle > 6){
				stelle = wochentag - 7 + i;
			}
			lang.newText(new Coordinates(xVonDatum-10+i*90, 242), wochentage[stelle],"dates", null, weekdaysColor);
		}
		
		// Rect for the calender
		RectProperties calenderRectProperties = new RectProperties();
		calenderRectProperties.set("filled", true);
		calenderRectProperties.set("fillColor", Color.WHITE);
		calenderRectProperties.set("depth", 3);
		@SuppressWarnings("unused")
		Rect calenderRect = this.lang.newRect(new Offset(955, 200, header, "NW"), new Offset(1420, 530, header, "SE"), "datumRect", null, calenderRectProperties);
		lang.nextStep();
		
		// Hightlight the date
		Coordinates kord = new Coordinates(xVonDatum, yVonDatum);
		@SuppressWarnings("unused")
		Rect datumRect = this.lang.newRect(new Offset(-5, -5, kord, "NW"), new Offset(25, 25, kord, "SE"), "datumRect", null, dRectProperties);
		lang.nextStep(translator.translateMessage("step3"));
		
		// Finishing sentence
		if(isGregorian){
			lang.newText(new Coordinates(20,700), translator.translateMessage("summary1") + " "  + d + "." + m + "." + y + " " + translator.translateMessage("summary3") + " " + wochentage[wochentag] + "."
					,"endsatz", null, textPropsDescr);
		}else{
			lang.newText(new Coordinates(20,700), translator.translateMessage("summary2") + " " + d + "." + m + "." + y + " " + translator.translateMessage("summary3") + " " + wochentage[wochentag] + "."
					,"endsatz", null, textPropsDescr);
		}
		lang.nextStep(translator.translateMessage("step4"));

	}

	private void calculateZeller(StringArray array, SourceCode code, int d, int m, int y) {
		// Set the visual properties for the second source code object
		commentCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		
		// Create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(600, 230), "sourceCode", null, commentCode);
		
		// Arraymarker properties
		hMarker.set(AnimationPropertiesKeys.LABEL_PROPERTY, "h");
		
		// Highlight first line and add empty sourcecode line
		code.highlight(0, 0, false);
		lang.nextStep(translator.translateMessage("step2"));
		
		// Highlight line 1 and add matching comments
		code.toggleHighlight(0, 0, false, 1, 0);
		sc.addCodeLine(" ", null, 0, null);
		lang.nextStep();
		
		// Highlight line 2 and add empty sourcecode line
		code.toggleHighlight(1, 0, false, 2, 0);
		if(!isGregorian){
			sc.addCodeLine("isGregorian = false;", null, 0, null);
		}else{
			sc.addCodeLine("", null, 0, null);
		}
		lang.nextStep();

		// Highlight between line 3 and 5 and add matching comments
		if(isGregorian){
			code.toggleHighlight(2, 0, false, 3, 0);
			sc.addCodeLine(" ", null, 0, null);
			lang.nextStep();
			code.toggleHighlight(3, 0, false, 4, 0);
			isGregorian = true;
			sc.addCodeLine("isGregorian = true;", null, 0, null);
			lang.nextStep();
		}else{
			code.toggleHighlight(2, 0, false,  4, 0);
			sc.addCodeLine(" ", null, 0, null);
			sc.addCodeLine(" ", null, 0, null);
			lang.nextStep();
		}
		
		// Highlight line 5 and add matching comments
		code.toggleHighlight(4, 0, false, 5, 0);
		int day = d; int month = m; int year = y; int h = 0;
		sc.addCodeLine(" ", null, 0, null);
		lang.nextStep();

		// Highlight line 6 and set ArrayPointer and add empty sourcecode line
		code.toggleHighlight(5, 0, false, 6, 0);
		ArrayMarker arrayHMarker = lang.newArrayMarker(array, 0, "h", null, hMarker);
		sc.addCodeLine("day = " + Integer.toString(d) + ";" + " " + "month = " + Integer.toString(m) + ";" + " " + "year = " + Integer.toString(y) + ";" + " " + "h = " + Integer.toString(h) + ";", null, 0, null);
		lang.nextStep();
		
		// Highlights between line 6 and 9 and add matching comments
		if(month == 1 || month == 2){
			code.toggleHighlight(6, 0, false, 7, 0);
			sc.addCodeLine(" ", null, 0, null);
			lang.nextStep();
			code.toggleHighlight(7, 0, false, 8, 0);
			month += 12;
			sc.addCodeLine("month = " + Integer.toString(month-12) + " + " + Integer.toString(12) + " = " + Integer.toString(month) , null, 0, null);
			lang.nextStep();
			code.toggleHighlight(8, 0, false, 9, 0);
			year -= 1;
			sc.addCodeLine("year = " + Integer.toString(year-1) + " + " + Integer.toString(1) + " = " + Integer.toString(year) , null, 0, null);
			lang.nextStep();
		}else{
			code.toggleHighlight(6, 0, false, 9, 0);
			sc.addCodeLine(" " , null, 0, null);
			sc.addCodeLine(" " , null, 0, null);
			sc.addCodeLine(" " , null, 0, null);
			lang.nextStep();
		}
		
		// Highlight line 9
		code.toggleHighlight(9, 0, false, 10, 0);
		sc.addCodeLine(" " , null, 0, null);
		lang.nextStep();
		
		// Highlight line 10 and add matching comments
		code.toggleHighlight(10, 0, false, 11, 0);
		int k = year % 100;
		sc.addCodeLine("k = " + Integer.toString(year) + " % " + Integer.toString(100) + " = " + Integer.toString(k) , null, 0, null);
		lang.nextStep();
		
		// Highlight line 11 and add matching comments
		code.toggleHighlight(11, 0, false, 12, 0);
		int j = year / 100;
		sc.addCodeLine("j = " + Integer.toString(year) + " / " + Integer.toString(100) + " = " + Integer.toString(j) , null, 0, null);
		lang.nextStep();
		
		// Highlight between line 12 and 15  and add matching comments
		code.toggleHighlight(12, 0, false, 13, 0);
		h = day;
		sc.addCodeLine("h = " + Integer.toString(day), null, 0, null);
		lang.nextStep();
		
		code.toggleHighlight(13, 0, false, 14, 0);
		h += ((month + 1) * 13) / 5;
		sc.addCodeLine("h = " + Integer.toString(h - ((month + 1) * 13) / 5) + " + " + ((month + 1) * 13) / 5 + " = " + Integer.toString(h) , null, 0, null);
		lang.nextStep(); //"h = " + Integer.toString(h - ((month + 1) * 13) / 5) + " + " +  Character.toString((char)8970) +"(((" + Integer.toString(month) + " + 1)* 13) / 5)" +  Character.toString((char)8971) + " = " + Integer.toString(h) , null, 0, null
		
		code.toggleHighlight(14, 0, false, 15, 0);
		h += k;
		sc.addCodeLine("h = " + Integer.toString(h - k) + " + " + Integer.toString(k) + " = " + Integer.toString(h), null, 0, null);
		lang.nextStep();
		
		code.toggleHighlight(15, 0, false, 16, 0);
		h += k/4;
		sc.addCodeLine("h = " + Integer.toString(h - k/4) + " + "+  k/4 + " = " + Integer.toString(h), null, 0, null);
		lang.nextStep(); //"h = " + Integer.toString(h - k/4) + " + "+  Character.toString((char)8970) + "(" + Integer.toString(k) + " / 4)" +  Character.toString((char)8971) +" = " + Integer.toString(h), null, 0, null
		
		// Highlight between line 13 and 17  and add matching comments
		if(isGregorian){
			code.toggleHighlight(16, 0, false, 17, 0);
			sc.addCodeLine(" ", null, 0, null);
			lang.nextStep();
			
			code.toggleHighlight(17, 0, false, 18, 0);
			h += j / 4;
			sc.addCodeLine("h = " + Integer.toString(h - j/4) + " + " + j/4 + " = " + Integer.toString(h), null, 0, null);
			lang.nextStep(); //"h = " + Integer.toString(h - j/4) + " + " + Character.toString((char)8970)  + "(" + Integer.toString(j) + " / 4)" + Character.toString((char)8971) + " = " + Integer.toString(h), null, 0, null
			
			code.toggleHighlight(18, 0, false, 23, 0);
			h -= 2 * j;
			sc.addCodeLine("h = " + Integer.toString(h + 2*j) + " - 2 * " + Integer.toString(j) + " = " + Integer.toString(h), null, 0, null);
			lang.nextStep();
			
			sc.addCodeLine(" ", null, 0, null);
			sc.addCodeLine(" ", null, 0, null);
			sc.addCodeLine(" ", null, 0, null);
			sc.addCodeLine(" ", null, 0, null);
			
		}else{
			code.toggleHighlight(16, 0, false, 19, 0);
			sc.addCodeLine(" ", null, 0, null);
			sc.addCodeLine(" ", null, 0, null);
			sc.addCodeLine(" ", null, 0, null);
			lang.nextStep();
			
			code.toggleHighlight(19, 0, false, 20, 0);
			sc.addCodeLine(" ", null, 0, null);
			lang.nextStep();
			
			code.toggleHighlight(20, 0, false, 21, 0);
			h += 5;
			sc.addCodeLine("h = " + Integer.toString(h - 5) + " +  5 = " + Integer.toString(h), null, 0, null);
			lang.nextStep();
			
			code.toggleHighlight(21, 0, false, 22, 0);
			h -= j;
			sc.addCodeLine("h = " + Integer.toString(h + j) + " - " + Integer.toString(j) + " = " + Integer.toString(h), null, 0, null);
			lang.nextStep();
			
			code.toggleHighlight(22, 0, false, 23, 0);
			sc.addCodeLine(" ", null, 0, null);
			lang.nextStep();
		}
		
		// Highlight line 24  and add matching comments
		code.toggleHighlight(23, 0, false, 24, 0);
		int h_Old = h;
		h = h % 7;
		sc.addCodeLine("h = " + Integer.toString(h_Old) + " % 7 = " + Integer.toString(h), null, 0, null);
		lang.nextStep();
		
		// Highlight line 25
		code.toggleHighlight(24, 0, false, 25, 0);
		arrayHMarker.move(h, null, defaultDuration);
		lang.nextStep();
		
		// Save the weekday in a global variable
		wochentag = h;
	}
}