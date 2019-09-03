package generators.compression;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import translator.Translator;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.compression.LZ77;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.animalscript.AnimalScript;

public class LZ77 implements Generator
{
    private Language lang;
    
    // Properties for the rechtangles
	RectProperties rectProperties_W = new RectProperties(); //Dictionary
	RectProperties rectProperties_V = new RectProperties(); //Preview Buffer
	RectProperties rectProperties_T = new RectProperties(); //Input-Text
	RectProperties rectProperties_A = new RectProperties(); //Output

    // Properties for the colors
	RectProperties rectProperties_MAGENTA = new RectProperties(); //Magenta-Rectangle
	RectProperties rectProperties_ORANGE = new RectProperties(); //Orange-Rectangle
	RectProperties rectProperties_YELLOW = new RectProperties(); //Yellow-Rectangle
	RectProperties rectProperties_WHITE = new RectProperties(); //White-Rectangle
	RectProperties rectProperties_PINK = new RectProperties(); //Pink-Rectangle
	
	// Properties for Text in Rectangles
    Color text_W = Color.BLACK;
    Color text_V = Color.BLACK;
    Color text_T = Color.BLACK;
    Color text_A = Color.BLACK;

	// Properties for labels
    TextProperties textPropertiesUeberschrift = new TextProperties(); //Headline h1
    TextProperties textPropertiesUeberschrift2 = new TextProperties(); //Headline h2
    TextProperties textPropertiesErklaerung = new TextProperties(); //Explanation
    SourceCodeProperties sonderfallProp = new SourceCodeProperties(); //Special-case Explanation

    // Properties for the Pseudocode
    SourceCodeProperties sourceCodePropertiesPseudocode = new SourceCodeProperties();
	
	Translator translator;
	Locale contentLocale = null;
	
	public LZ77()
	{
		this("resources/LZ77", Locale.GERMANY);
	}

	public LZ77(String aResourceName, Locale aLocale)
	{
		translator = new Translator(aResourceName, aLocale);
		contentLocale = aLocale;
		init();
	}
	
	/**
	 * Class of the algorithmus
	 * @author Maximilian
	 *
	 */
	private class Algorithmus
	{
		int woerterbuchGroesse;
		int vorschaupufferGroesse;
		
		/**
		 * sets the size of the dictionary and the preview buffer
		 * @param woerterbuchGroesse size of the dictionary
		 * @param vorschaupufferGroesse size of the preview buffer
		 */
		public Algorithmus(int woerterbuchGroesse, int vorschaupufferGroesse)
		{
			this.woerterbuchGroesse = woerterbuchGroesse;
			this.vorschaupufferGroesse = vorschaupufferGroesse;
		}
		
		/**
		 * the main-algorithmus of the lz77-algorithmus
		 * @param text text
		 * @return array of triples
		 */
		public List<Triple> Kompression(String text)
		{
			String woerterbuch = "";
			String vorschaupuffer = "";
			List<Triple> array = new ArrayList<Triple>();
			int laenge = text.length();
			
			for (int i = 0; i < woerterbuchGroesse; i++)
				woerterbuch += "$";
			for (int i = 0; i < vorschaupufferGroesse; i++)
				woerterbuch += " ";
			text = text.replace("$", "") + "$";
			

	    	SourceCode pseudo = lang.newSourceCode(new Coordinates(20, 130), "pseudo", null, sourceCodePropertiesPseudocode);
	    	for (String line : translator.translateMessage("pseudoText").split("\n")) {
	    		pseudo.addCodeLine(line, null, 0, null);
			}
	    	
			lang.newRect(Node.convertToNode(new Point(20, 50)), Node.convertToNode(new Point(20 + woerterbuchGroesse * 20, 70)), "rechteckUW", null, rectProperties_W);
			lang.newRect(Node.convertToNode(new Point(20 + woerterbuchGroesse * 20, 50)), Node.convertToNode(new Point(20 + woerterbuchGroesse * 20 + vorschaupufferGroesse * 20, 70)), "rechteckUW", null, rectProperties_V);
			lang.newRect(Node.convertToNode(new Point(20 + woerterbuchGroesse * 20 + vorschaupufferGroesse * 20, 50)), Node.convertToNode(new Point(20 + woerterbuchGroesse * 20 + vorschaupufferGroesse * 20 + (text.length()-1) * 20, 70)), "rechteckUW", null, rectProperties_T);
	    	lang.newText(new Coordinates(25, 53), translator.translateMessage("woerterbuch"), "textUeberschrift2", null, textPropertiesUeberschrift2);
	    	lang.newText(new Coordinates(25 + woerterbuchGroesse * 20, 49), translator.translateMessage("vorschaupuffer"), "textUeberschrift2", null, textPropertiesUeberschrift2);
	    	lang.newText(new Coordinates(25 + woerterbuchGroesse * 20 + vorschaupufferGroesse * 20, 49), translator.translateMessage("text"), "textUeberschrift2", null, textPropertiesUeberschrift2);
	    	lang.newText(new Coordinates(30, 310), translator.translateMessage("ausgabe"), "textUeberschrift2", null, textPropertiesUeberschrift2);
			
	    	createRectangles(woerterbuch, vorschaupuffer, text, laenge, -1, -1);
			lang.nextStep();
			
			int i = -1;
			int length = -1;
			while (!text.equals(""))
			{
				i++;
				if (text.equals("$"))
					break;
				
				int index = -1;
				int altIndex = woerterbuchGroesse;

				if (woerterbuch.length() > woerterbuchGroesse)
					woerterbuch = woerterbuch.substring(woerterbuch.length() - woerterbuchGroesse, woerterbuch.length());
				
				if (text.length() <= vorschaupufferGroesse)
					createRectangles(woerterbuch, text, "", laenge, -1, -1);
				else
					createRectangles(woerterbuch, text.substring(0, vorschaupufferGroesse), text.substring(vorschaupufferGroesse), laenge, -1, -1);
				createBowAndArrow(-1, -1, "");
				if (length != -1)
				{
					if (length == 1)
						showSourceCode(pseudo, 8);
					else
						showSourceCode(pseudo, 5);
					addExplanation(translator.translateMessage("beschreibung3") + " " + length + " " + translator.translateMessage("beschreibung4"), Color.GREEN);
					lang.nextStep();
				}
				else
				{
					addExplanation(translator.translateMessage("beschreibung1"), Color.BLACK);
				}
				showSourceCode(pseudo, 2);
				lang.nextStep();
				length = 1;
				while (length < vorschaupufferGroesse)
				{
					vorschaupuffer = text.substring(0, length);
					index = (woerterbuch + vorschaupuffer).indexOf(vorschaupuffer);
					if (index >= woerterbuchGroesse || text.length() == length)
						break;
					length++;
					altIndex = index;
				}

				if (length == 1)
				{
					addExplanation(translator.translateMessage("beschreibung2"), Color.MAGENTA);
					showSourceCode(pseudo, 6);
				}
				else
				{
					addExplanation(translator.translateMessage("beschreibung5") + text.substring(0, length - 1), Color.RED);
					showSourceCode(pseudo, 3);
				}
				

				sonderfallProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
				sonderfallProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		    	SourceCode sonderfall = lang.newSourceCode(new Coordinates(20 + 20 * woerterbuchGroesse + 20 * vorschaupufferGroesse, 111), "sonderfall", null, sonderfallProp);
				if ((length-1) > (woerterbuchGroesse - altIndex))
				{
					for (String line : translator.translateMessage("beschreibung6").split("\n")) {
						sonderfall.addCodeLine(line, null, 0, null);
					}
				}
				createBowAndArrow(altIndex, length, text.substring(length - 1, length));
				
				if (text.length() <= vorschaupufferGroesse)
					createRectangles(woerterbuch, text, "", laenge, length, altIndex);
				else
					createRectangles(woerterbuch, text.substring(0, vorschaupufferGroesse), text.substring(vorschaupufferGroesse), laenge, length, altIndex);
				lang.nextStep("String " + text.substring(0, length - 1) + " " + translator.translateMessage("inhalt"));
				
				woerterbuch += text.substring(0, length);

				text = text.substring(length);
				
				Triple t = new Triple(woerterbuchGroesse - altIndex, woerterbuch.length() - woerterbuchGroesse - 1, woerterbuch.charAt(woerterbuch.length() - 1));
				array.add(new Triple(woerterbuchGroesse - altIndex, woerterbuch.length() - woerterbuchGroesse - 1, woerterbuch.charAt(woerterbuch.length() - 1)));
				outputTriple(t, i);
				if (length == 1)
					showSourceCode(pseudo, 7);
				else
					showSourceCode(pseudo, 4);
				lang.nextStep();
				if (sonderfall != null)
					sonderfall.hide();
			}
			createBowAndArrow(-1, -1, "");
			if (woerterbuch.length() > woerterbuchGroesse)
				woerterbuch = woerterbuch.replace("$",  "").substring(woerterbuch.replace("$",  "").length() - woerterbuchGroesse, woerterbuch.replace("$",  "").length());
			createRectangles(woerterbuch, text, "", laenge, -1, -1);
			if (length == 1)
				showSourceCode(pseudo, 8);
			else
				showSourceCode(pseudo, 5);
			addExplanation(translator.translateMessage("beschreibung3") + " " + length + " " + translator.translateMessage("beschreibung4"), Color.GREEN);
			lang.nextStep();
			showSourceCode(pseudo, 1);
			addExplanation("", Color.BLACK);
			lang.nextStep("");
			return array;
		}
		
		Text textErklaerung;
		/**
		 * adds the explanation of the current action to the array
		 * @param text text
		 * @param c color
		 */
		public void addExplanation(String text, Color c)
		{
			if (textErklaerung != null)
				textErklaerung.hide();
	        textPropertiesErklaerung.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
	        textPropertiesErklaerung.set(AnimationPropertiesKeys.COLOR_PROPERTY, c);
	    	textErklaerung = lang.newText(new Coordinates(20 + 20 * woerterbuchGroesse + 20 * vorschaupufferGroesse, 91), text, "textErklaerung", null, textPropertiesErklaerung);
		}
		
		Rect RechteckAusgabe;
		/**
		 * outputs the triple
		 * @param t triple
		 * @param i number
		 */
		public void outputTriple(Triple t, int i)
		{
			if (RechteckAusgabe != null)
				RechteckAusgabe.hide();
			RechteckAusgabe = lang.newRect(Node.convertToNode(new Point(23, 340)), Node.convertToNode(new Point(140, 377 + 25 * i)), "rechteckAusgabe", null, rectProperties_A);
			
			TextProperties textPropertiesTriple = new TextProperties();
	    	textPropertiesTriple.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
	    	textPropertiesTriple.set(AnimationPropertiesKeys.COLOR_PROPERTY, text_A);
			lang.newText(new Coordinates(30, 350 + 25 * i), t.toString(), "Ueberschrift", null, textPropertiesTriple);
		}
		
		Polyline poly1;
		Polyline poly2;
		Rect rect;
		Rect rectWhite;
		Text pos;
		Text len;
		Text cha;
		/**
		 * creates the additional bows an arrows below the array
		 * @param index index of the first agreement
		 * @param length length of the agreement
		 * @param buchstabe character
		 */
		public void createBowAndArrow(int index, int length, String buchstabe)
		{
			
			if (poly1 != null)
				poly1.hide();
			if (poly2 != null)
				poly2.hide();
			if (rect != null)
				rect.hide();
			if (rectWhite != null)
				rectWhite.hide();
			if (pos != null)
				pos.hide();
			if (len != null)
				len.hide();
			if (cha != null)
				cha.hide();
			
			PolylineProperties polyProps = new PolylineProperties();
	        polyProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
			if (index != -1)
			{
				Node[] vertices = new Node[2];
				vertices[0] = Node.convertToNode(new Point(30 + index * 20, 120));
				vertices[1] = Node.convertToNode(new Point(30 + index * 20, 100));
		        poly1 = lang.newPolyline(vertices, "poly1", null, polyProps);
			}

			if (index != -1)
			{
				Node[] vertices = new Node[2];
				vertices[0] = Node.convertToNode(new Point(30 + 20 * woerterbuchGroesse + (length-1) * 20, 120));
				vertices[1] = Node.convertToNode(new Point(30 + 20 * woerterbuchGroesse + (length-1) * 20, 100));
		        poly2 = lang.newPolyline(vertices, "poly2", null, polyProps);
			}

			if (index != -1 && length > 1)
			{
				rect = lang.newRect(new Coordinates(20 + 20 * woerterbuchGroesse, 95), new Coordinates(20 + 20 * woerterbuchGroesse + 20 * (length-1), 120), "rect", null);
				rectWhite = lang.newRect(new Coordinates(20 + 20 * woerterbuchGroesse, 95), new Coordinates(20 + 20 * woerterbuchGroesse + 20 * (length-1), 100), "rectWhite", null, rectProperties_WHITE);
			}
			
			if (index != -1 && length > 1)
			{
				pos = lang.newText(new Coordinates(27 + index * 20, 120), String.valueOf(woerterbuchGroesse - index), "pos", null, textPropertiesUeberschrift2);
				len = lang.newText(new Coordinates(17  + (10 * (length-1)) + 20 * woerterbuchGroesse, 120), String.valueOf(length-1), "len", null, textPropertiesUeberschrift2);
				cha = lang.newText(new Coordinates(27 + 20 * woerterbuchGroesse + (length-1) * 20, 120), buchstabe.replace("$", translator.translateMessage("ende")), "cha", null, textPropertiesUeberschrift2);
			}
			else if (index != -1 && length <= 1)
			{
				cha = lang.newText(new Coordinates(17 + 20 * woerterbuchGroesse + (length-1) * 20, 120), "0, 0, " + buchstabe, "cha", null, textPropertiesUeberschrift2);
			}
		}
		
		/**
		 * creates a new Rectangle of the Array
		 * @param woerterbuch dictionary
		 * @param vorschaupuffer preview buffer
		 * @param text text
		 * @param laengeText length of the text
		 * @param uebereinstimmung agreement
		 * @param index index of the first agreement
		 */
		public void createRectangles(String woerterbuch, String vorschaupuffer, String text, int laengeText, int uebereinstimmung, int index)
		{
			for (int i = 0; i < woerterbuchGroesse; i++)
			{
				if (index != -1 && i >= index && i < (index + uebereinstimmung - 1))
					lang.newRect(Node.convertToNode(new Point(20 + i * 20, 70)), Node.convertToNode(new Point(40 + i * 20, 90)), "rechteckW" + i, null, rectProperties_PINK);
				else
					lang.newRect(Node.convertToNode(new Point(20 + i * 20, 70)), Node.convertToNode(new Point(40 + i * 20, 90)), "rechteckW" + i, null, rectProperties_W);
				String Buchstabe = String.valueOf(woerterbuch.charAt(i)).replace('$', ' ');
				
				TextProperties textProperties = new TextProperties();
		    	textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, text_W);
				lang.newText(Node.convertToNode(new Point(26 + i * 20, 72)), Buchstabe, "nameW" + i, null, textProperties);
			}
			for (int i = 0; i < vorschaupufferGroesse; i++)
			{
				if (i < uebereinstimmung)
					if (i == uebereinstimmung-1)
						lang.newRect(Node.convertToNode(new Point(20 + (woerterbuchGroesse * 20) + i * 20, 70)), Node.convertToNode(new Point(40 + (woerterbuchGroesse * 20) + i * 20, 90)), "rechteckV" + i, null, rectProperties_MAGENTA);
					else
						lang.newRect(Node.convertToNode(new Point(20 + (woerterbuchGroesse * 20) + i * 20, 70)), Node.convertToNode(new Point(40 + (woerterbuchGroesse * 20) + i * 20, 90)), "rechteckV" + i, null, rectProperties_ORANGE);
				else
					lang.newRect(Node.convertToNode(new Point(20 + (woerterbuchGroesse * 20) + i * 20, 70)), Node.convertToNode(new Point(40 + (woerterbuchGroesse * 20) + i * 20, 90)), "rechteckV" + i, null, rectProperties_V);
				String Buchstabe = " ";
				if (i < vorschaupuffer.length())
					Buchstabe = String.valueOf(vorschaupuffer.charAt(i)).replace('$', ' ');
				
				TextProperties textProperties = new TextProperties();
		    	textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, text_V);
				lang.newText(Node.convertToNode(new Point(26 + (woerterbuchGroesse * 20) + i * 20, 72)), Buchstabe, "nameV" + i, null, textProperties);
			}
			for (int i = 0; i < laengeText; i++)
			{
				String Buchstabe = " ";
				if (i < text.length())
				{
					Buchstabe = String.valueOf(text.charAt(i)).replace('$', ' ');
					if (Buchstabe.equals(" "))
						lang.newRect(Node.convertToNode(new Point(20 + ((woerterbuchGroesse + vorschaupufferGroesse) * 20) + i * 20, 70)), Node.convertToNode(new Point(40 + ((woerterbuchGroesse + vorschaupufferGroesse) * 20) + i * 20, 90)), "rechteckT" + i, null, rectProperties_T);
					else
						lang.newRect(Node.convertToNode(new Point(20 + ((woerterbuchGroesse + vorschaupufferGroesse) * 20) + i * 20, 70)), Node.convertToNode(new Point(40 + ((woerterbuchGroesse + vorschaupufferGroesse) * 20) + i * 20, 90)), "rechteckT" + i, null, rectProperties_T);
				}
				else
					lang.newRect(Node.convertToNode(new Point(21 + ((woerterbuchGroesse + vorschaupufferGroesse) * 20) + i * 20, 71)), Node.convertToNode(new Point(40 + ((woerterbuchGroesse + vorschaupufferGroesse) * 20) + i * 20, 90)), "rechteckT" + i, null, rectProperties_WHITE);
				
				TextProperties textProperties = new TextProperties();
		    	textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, text_T);
				lang.newText(Node.convertToNode(new Point(26 + ((woerterbuchGroesse + vorschaupufferGroesse) * 20) + i * 20, 72)), Buchstabe, "nameT" + i, null, textProperties);
			}
		}
		
		/**
		 * highlights the corresponding line 
		 * @param sourceCode pseudocode
		 * @param lineNr number of the line
		 */
		public void showSourceCode(SourceCode sourceCode, int lineNr)
		{
			for (int i = 0; i < 9; i++)
			{
				sourceCode.unhighlight(i);
			}
			sourceCode.highlight(lineNr);
		}
	}
	
	/**
	 * Class for the Output-Triple-Format
	 * @author Maximilian
	 *
	 */
	private class Triple
	{
		int position;
		int laenge;
		char buchstabe;

		/**
		 * Creates a new Triple with Position and length within the dictionary and the next character
		 * 
		 * @param position
		 *            position
		 * @param laenge
		 *            length
		 * @param buchstabe
		 *            character
		 */
		public Triple(int position, int laenge, char buchstabe) {
			this.position = position;
			this.laenge = laenge;
			this.buchstabe = buchstabe;
		}

		@Override
		public String toString() {
			return "(" + position + ", " + laenge + ", " + ((buchstabe == '$') ? translator.translateMessage("ende") : buchstabe) + ")";
		}
	}
	
    public void init()
    {
        lang = new AnimalScript("Lempel Ziv 77", "Maximilian Müller, Tobias Neidig", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
    {
        int Vorschaupuffer = (Integer)primitives.get("Vorschaupuffer");
        String Zeichenkette = (String)primitives.get("Zeichenkette");
        int Woerterbuchgroesse = (Integer)primitives.get("Woerterbuchgroesse");
		lang.setStepMode(true);
        
		rectProperties_W = (RectProperties) props.getPropertiesByName("Wörterbuch");
		rectProperties_V = (RectProperties) props.getPropertiesByName("Vorschaupuffer");
		rectProperties_T = (RectProperties) props.getPropertiesByName("Text");
		rectProperties_A = (RectProperties) props.getPropertiesByName("Ausgabe");
		sourceCodePropertiesPseudocode = (SourceCodeProperties) props.getPropertiesByName("Pseudocode");
		
		text_W = (Color) rectProperties_W.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		text_V = (Color) rectProperties_V.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		text_T = (Color) rectProperties_T.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		text_A = (Color) rectProperties_A.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		rectProperties_W.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProperties_V.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProperties_T.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProperties_A.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		
        rectProperties_MAGENTA.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProperties_MAGENTA.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.MAGENTA);
        rectProperties_ORANGE.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProperties_ORANGE.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
        rectProperties_YELLOW.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProperties_YELLOW.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        rectProperties_YELLOW.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        rectProperties_WHITE.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProperties_WHITE.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectProperties_WHITE.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
        rectProperties_PINK.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProperties_PINK.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
                
        textPropertiesUeberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
        textPropertiesUeberschrift2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        
        
        Algorithmus lz77 = new Algorithmus(Woerterbuchgroesse, Vorschaupuffer);
        Intro(props);
        lz77.Kompression(Zeichenkette);
        Outro(props);
        
        return lang.toString();
    }
    
    /**
     * creates the Intro-Text at the first page
     * @param props AnimationPropertiesContainer
     */
    public void Intro(AnimationPropertiesContainer props)
    {
    	lang.newText(new Coordinates(20, 17), "Lempel Ziv 77", "Ueberschrift", null, textPropertiesUeberschrift);
    	lang.newRect(new Coordinates(0, 40), new Coordinates(800, 40), "rectHeadline", null);
    	SourceCodeProperties scProps = (SourceCodeProperties) props.getPropertiesByName("Intro");

    	SourceCode intro = lang.newSourceCode(new Coordinates(20, 80), "intro", null, scProps);
    	for (String line : translator.translateMessage("introText").split("\n")) {
    		intro.addCodeLine(line, null, 0, null);
		}
		lang.nextStep("Intro");
		intro.hide();
    }
    
    /**
     * creates the Outro-Text at the last page
     * @param props AnimationPropertiesContainer
     */
    public void Outro(AnimationPropertiesContainer props)
    {
    	lang.newRect(new Coordinates(0, 41), new Coordinates(100000, 100000), "rectHeadline", null, rectProperties_WHITE);
    	SourceCodeProperties scProps = (SourceCodeProperties) props.getPropertiesByName("Outro");
    	SourceCode outro = lang.newSourceCode(new Coordinates(20, 80), "outro", null, scProps);
    	for (String line : translator.translateMessage("outroText").split("\n")) {
    		outro.addCodeLine(line, null, 0, null);
		}
		lang.nextStep("Outro");
    }

    public String getName()
    {
        return "LZ77";
    }
    
    public String getAlgorithmName()
    {
        return "Lempel Ziv 77";
    }

    public String getAnimationAuthor()
    {
        return "Maximilian Müller, Tobias Neidig";
    }

	public String getDescription()
	{
		return translator.translateMessage("animDescription");
	}

	public String getCodeExample()
	{
		return translator.translateMessage("animCodeExample");
	}

    public String getFileExtension()
    {
        return "asu";
    }

    public Locale getContentLocale()
    {
        return contentLocale;
    }

    public GeneratorType getGeneratorType()
    {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
    }

    public String getOutputLanguage()
    {
        return Generator.JAVA_OUTPUT;
    }

}
