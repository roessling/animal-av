package generators.cryptography.caesarcipher;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class CaesarChiffreGutAnnotated extends AnnotatedAlgorithm implements generators.framework.Generator {

	private String[] sourceCodeString = {"public class caesarChiffre {", 
			"   public static void main(String args[])", 
			"   {", 
			"      String klarText = -test-;", 
			"      int key = 1;", 
			"      // parse arguments if available", 
			"      if (args.length > 0)", 
			"      {", 
			"         klarText = args[0]; / the text which is to be encrypted", 
			"      }", 
			"      if (args.length > 1)", 
			"      {", 
			"         key = Integer.parseInt(args[1]); // the key", 
			"      }", 
			"      // output every character shifted right by <key> ( modulo 255 which is max value of a character ) ", 
			"      for (int i = 0; i < klarText.length(); i++)", 
			"      {", 
			"         System.out.print((klarText.charAt(i) + key) % 255);",  
			"      }", 
			"   }", 
			"}", 
			};
	
	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		
		init();

		String cap = "Caesar-Chiffre";
		TextProperties textprop = new TextProperties();
		textprop.set(AnimationPropertiesKeys.CENTERED_PROPERTY, Boolean.FALSE);
		textprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textprop.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		textprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("ARIAL",Font.BOLD, 28));
		textprop.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, Boolean.FALSE);
		@SuppressWarnings("unused")
		Text Caption = lang.newText( new Coordinates(30, 30), cap, "caption", null, textprop );
		
		lang.setStepMode(true);
		
		Object keyObject = arg1.get( "key" );
		int key = Integer.parseInt( keyObject.toString() );
		String klartext = (String) arg1.get( "klartext" );
		klartext = klartext.toLowerCase();
		
		ArrayProperties aP = (ArrayProperties)arg0.getPropertiesByName( "arrayProps" );
		StringArray klar = generateStringArray( klartext, new Coordinates(50, 150), aP );
		String alp = "abcdefghijklmnopqrstuvwxyz";
		StringArray alphabet = generateStringArray( alp, new Coordinates(50, 200), aP );

		lang.nextStep();

		exec("ding"); //sourceCode.toggleHighlight("line12", false, "line13", new TicksTiming(200), null);

		ArrayMarker klarMark;
		ArrayMarker alpMark;
		
		String ein = "Eingabe: ";
		Text eingabe = lang.newText( new Coordinates(50, 230), ein, "eingabe", null );
		
		String keyText = "Verschiebe um " + key + " Stellen nach rechts:";
		@SuppressWarnings("unused")
		Text keyTex = lang.newText( new Coordinates(50, 250), keyText, "keytext", null );

		String aus = "Ausgabe: ";
		Text ausgabe = lang.newText( new Coordinates(50, 270), aus, "ausgabe", null );

		exec("wichtig");//code.highlight( 17 ); // die einzige Zeile in der wirklich etwas interessantes passiert!
		
		lang.nextStep();
		
		ArrayMarkerProperties aMP = (ArrayMarkerProperties)arg0.getPropertiesByName( "arrayMarkerProps" );
		
		for( int i = 0; i < klar.getLength(); i++ )
		{
			klarMark = lang.newArrayMarker(klar, i, "Klarer" + i, null, aMP );

			String klarTextItem = klar.getData( klarMark.getPosition() );
			char klarChar = (klarTextItem.charAt(0)); // offset für kleinbuchstaben... 97
			
			if( Character.isLetter(klarChar) )
			{
				// Caesar doesn't know these letters! (neither do animal-arrays)
				if( klarChar == '�' || klarChar == '�' )
					klarChar = 'o';
				if( klarChar == '�' || klarChar == '�' )
					klarChar = 'a';
				if( klarChar == '�' || klarChar == '�' )
					klarChar = 'u';
				
				
				// 97 abziehen ist hier leider n�tig um mit dem alphabet-array arbeiten zu k�nnen. 
				// um mit allen Buchstaben/sonderzeichen arbeiten zu k�nnen, m�sste man ein 255-wertiges char[] alphabet benutzen
				// da bei caesar aber nur das eigentliche alphabet benutzt wurde fand ich das a-z array als erkl�rung f�r den algorithmus passend
				// so funktioniert es jetzt auch mit sonderzeichen, .toLowerCase wird weiter oben schon gemacht :)
				klarChar = (char) (klarChar - 97);

				alpMark = lang.newArrayMarker( alphabet, ( klarChar + key ) % 26, "ergibt" + klarChar + key, null, aMP );

				aus += alphabet.getData( ( klarChar + key ) % 26 );
				ausgabe.setText( aus, null, null );

				ein += alphabet.getData( klarChar % 26);
				eingabe.setText( ein, null, null );

				lang.nextStep();

			} else 
			{
				alpMark = lang.newArrayMarker( alphabet, klarChar, "ergibt" + klarChar, null, aMP );

				aus += klarChar;
				ausgabe.setText( aus, null, null );

				ein += klarChar;
				eingabe.setText( ein, null, null );

				lang.nextStep();
			}
			
			klarMark.hide();
			alpMark.hide();
			
		}

		return lang.toString();
	}

	/**
	 * Generates the StringArray for the given String.
	 * @param array the String
	 * @return the StringArray for the visualization in animal
	 */
	private StringArray generateStringArray(String array, Coordinates coordinates, ArrayProperties arrayProps){
		String[] sAgenerated = new String[array.length()];
		for( int i = 0; i < array.length(); i++ )
		{
			sAgenerated[i] = array.substring( i, i + 1 );
		}

		// create matching IntArray
		StringArray sa = lang.newStringArray(coordinates, sAgenerated, "'char'Array", null, arrayProps);

		return sa;
	}

	
	@Override
	public String getAlgorithmName() {
		return "Caesar Cipher";
	}

	@Override
	public String getAnimationAuthor() {
		return "Annika Beissler, Martin Hess, Nando Fuchs";
	}

	@Override
	public String getCodeExample() {
		String ex = "";
		for( int i = 0; i < sourceCodeString.length; i++ ) ex += sourceCodeString[i] + "\n";
		return ex;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return "Ceasar encrypted messages by shifting the letters through the alphabet.";
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	@Override
	public String getName() {
		return "Caesar-Chiffre-Annotated";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		super.init();
		
		// Create SourceCode Properties
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.PLAIN, 12));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// create SourceCode
		sourceCode = lang.newSourceCode( new Coordinates(50, 300), "sourceCode", null, scProps );

		parse();
	}

	@Override
	public String getAnnotatedSrc() {
	    return "public class caesarChiffre { @label(\"header\")\n" + 
		"   public static void main(String args[] )@label(\"main\")\n" + 
		"   { @label(\"openMain\")\n" +
		"      String klarText = -test-; @label(\"klarText\")\n" + 
		"      int key = 1; @label(\"key\")\n"+ 
		"      // parse arguments if available @label(\"parse\")\n"+ 
		"      if (args.length > 0) @label(\"if\")\n"+ 
		"      { @label(\"openLength\")\n"+ 
		"         klarText = args[0]; // the text which is to be encrypted @label(\"set\")\n"+ 
		"      } @label(\"closeLength\")\n"+ 
		"      if (args.length > 1) @label(\"if2\")\n"+ 
		"      { @label(\"openCount\")\n"+ 
		"         key = Integer.parseInt( args[1] ); // the key @label(\"ding\")\n"+ 
		"      } @label(\"closeCount\")\n"+ 
		"      // output every character shifted right by <key> ( modulo 255 which is max value of a character ) @label(\"mod\")\n"+ 
		"      for (int i = 0; i < klarText.length(); i++) @label(\"for\")\n"+ 
		"      { @label(\"openFor\")\n"+ 
		"         System.out.print((klarText.charAt(i) + key) % 255); @label(\"wichtig\")\n"+  
		"      } @label(\"closeFor\")\n"+ 
		"   } @label(\"closeMain\"}\n"+ 
		"} @label(\"end\")\n" 
		;
	}


	
	
}
