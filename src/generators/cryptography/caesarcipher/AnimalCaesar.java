package generators.cryptography.caesarcipher;
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
import algoanim.util.Offset;


public class AnimalCaesar implements generators.framework.Generator
{
	private String[] alphabet ={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}; 
	private Language lang;
	private int shift=3;
	private SourceCode sc;
	private String[] stringContent={"A"}; 
	private String[] cipherContent;
	private StringArray plain;
	private StringArray cipher;
	private SourceCode titel;
	private SourceCodeProperties titelProps;
	private SourceCodeProperties sourceProps;
	private ArrayMarker markerPlain;
	private ArrayMarker markerCipher;
	private ArrayMarkerProperties markerPlainProps , markerCipherProps;
	public ArrayProperties plainProps;
	public ArrayProperties cipherProps;

	public AnimalCaesar() 
	{
		lang = new AnimalScript("Caesar","Naseri & Parisay & Gonen" , 0 ,0);
		lang.setStepMode(true);
	//	my_init();
	}




	/**
	 * 
	 */
	private void setProps(){

		plainProps = new ArrayProperties();
		plainProps.set(AnimationPropertiesKeys.FILLED_PROPERTY , true);
		plainProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		plainProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		cipherProps = new ArrayProperties();
		cipherProps.set(AnimationPropertiesKeys.FILLED_PROPERTY , true);
		cipherProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		titelProps= new SourceCodeProperties();
		titelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font ("Serif", Font.BOLD, 18));

		sourceProps = new SourceCodeProperties();
		sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font (Font.MONOSPACED, Font.PLAIN ,14));

		markerPlainProps = new ArrayMarkerProperties();
		markerPlainProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "plain text");
		markerCipherProps = new ArrayMarkerProperties();
		markerCipherProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "cipher text");

	}

	/**
	 * initialize the properties of elements which should be shown in animation
	 */
	private void my_init()
	{
		initCipher();
	//	setProps();

		titel = lang.newSourceCode(new Coordinates(5,10),"title" , null,titelProps);
		titel.addCodeLine("CAESAR CIPHER", "first", 0, null);
		sc = lang.newSourceCode(new Offset(5,5,"title","south"), "sourceCode", null,sourceProps);
		setSource();
		plain =lang.newStringArray(new Offset(70,20,"sourceCode","southeast"),stringContent,"plain" , null, plainProps);
		cipher = lang.newStringArray(new Offset(70,120,"sourceCode","southeast"),cipherContent, "output",null,cipherProps); 

	}

	/**
	 * 
	 */
	private void setSource()
	{
		sc.addCodeLine("FOR EACH ELEMENT OF ARRAY ", "first", 0, null);
		sc.addCodeLine("FOR I = 0 TO SHIFT", "second", 1, null);
		sc.addCodeLine("CHOOSE THE NEXT LETTER", "third", 2, null);
		sc.addCodeLine("SET NEW ELEMENT ", "fourth", 1, null);
		sc.addCodeLine("RETURN CIPHER TEXT", "fifth", 0, null);
		sc.addCodeLine("", "sixth", 0, null);
		sc.addCodeLine("", "7.", 0, null);
		sc.addCodeLine("", "7.1", 0, null);
		sc.addCodeLine("shift = ", "8.", 0,null );
		sc.addCodeElement(Integer.toString(shift), "9.", 0, null);


	}

	/**
	 * 
	 */
	private void initCipher()
	{
		cipherContent = new String[stringContent.length];
		for (int i = 0 ;i< stringContent.length; i++)
			cipherContent[i]= stringContent[i];

	}

	/**
	 * encrypts the text passed in
	 * 
	 * @param text the text to encrypt
	 * @return the encrypted text as a String array
	 */
	public String[]  encrypt(String[] text)
	{
		boolean doesNotExist=true; 
		stringContent=text;
		my_init();
		lang.nextStep();
		sc.highlight(0);
		markerPlain = lang.newArrayMarker(plain,0, "markerplain", null,markerPlainProps);
		markerCipher = lang.newArrayMarker(cipher,0, "markercipher", null,markerCipherProps);
		lang.nextStep();
		sc.unhighlight(0);
		int indexOfChar=0;
		for (int i=0 ;i<text.length ; i++){
			markerPlain.move(i, null, null);
			markerCipher.move(i, null, null);

			for (int  j=0; j<alphabet.length;j++){
				if( alphabet[j].toUpperCase().compareTo(text[i].toUpperCase())==0 ){
					indexOfChar=j;
					doesNotExist=false;
					break;
				}
			}
			sc.unhighlight(0);
			if (doesNotExist) {
				lang.nextStep();
				sc.highlight(1);
				sc.unhighlight(2);
				lang.nextStep();
				sc.unhighlight(1);
				sc.highlight(2);
				cipher.put(i," !" , null, null);
				cipherContent[i] = " !";

			}
			else{
				for (int k = 0; k<shift ; k++){
					lang.nextStep();
					sc.highlight(1);
					sc.unhighlight(2);
					lang.nextStep();
					sc.unhighlight(1);
					sc.highlight(2);

					if (indexOfChar>= alphabet.length){ 
						indexOfChar=0;
						cipher.put(i,alphabet[indexOfChar] , null, null);
					}
					else{
						indexOfChar++;
						if (indexOfChar >=alphabet.length)
							indexOfChar=0;
						cipher.put(i,alphabet[indexOfChar] , null, null);

					}
					cipherContent[i] = alphabet[indexOfChar];
				}

			}

			sc.unhighlight(2);
			sc.highlight(3);
			lang.nextStep();
			plain.highlightCell(i, null, null);
			sc.unhighlight(3);
			lang.nextStep();
		}

		sc.highlight(4);
		lang.nextStep();
		sc.unhighlight(4);
		return cipherContent;
	}


	/**
	 * 
	 * @param cipher
	 */
	public void changeCipherArray(StringArray cipher)
	{
		for (int i =0;i<cipher.getLength() ; i++)
			this.cipher.put(i, cipher.getData(i), null, null);
	}

	/**
	 * 
	 * @param str
	 */
	public void setStringToEncrypt(String[] str)
	{
		stringContent=str;
	}

	/**
	 * set the amount of shift
	 * @param i
	 */
	public void setShift(int i){
		shift=i;
	}

	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1)
	{
		AnimalCaesar caesar = new AnimalCaesar();		
		String[] _plain= (String[]) arg1.get("plain");
		caesar.setShift((Integer) arg1.get("shift"));
		caesar.setProps();
		caesar.plainProps =  (ArrayProperties) arg0.getPropertiesByName("plainProps");
		caesar.cipherProps =(ArrayProperties) arg0.getPropertiesByName("cipherProps");
		caesar.encrypt(_plain);

		return caesar.lang.toString();
	}


	
	public String getAlgorithmName() {
    return "Caesar-Verschlüsselung";
	}


	
	public String getAnimationAuthor() {
		return "Ardalan Naseri, Mohsen Parisay, Yanai Gonen";
	}



	
	public String getCodeExample() {
		StringBuffer buff = new StringBuffer();
		buff.append("FOR EACH ELEMENT OF ARRAY \n");
		buff.append(" FOR I = 0 TO SHIFT\n");
		buff.append("  CHOOSE THE NEXT LETTER\n");
		buff.append(" SET NEW ELEMENT\n");
		buff.append("RETURN CIPHER TEXT");
		return 	buff.toString();

	}


	
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}


	
	public String getDescription() {
		return "Zum Zwecke der Verschlsselung wird dabei jeder Buchstabe des lateinischen Standardalphabets um eine bestimmte Anzahl von Positionen\n" +
		" zyklisch verschoben (rotiert).";
	}


	
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}


	
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}


	
	public String getName() {
		return "Caesar Cipher";
	}


	
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}


	
	
	public void init() {

		plain.unhighlightElem(0, plain.getLength()-1, null, null);
		initCipher();
		markerCipher.move(0, null, null);
		markerPlain.move(0, null, null);
		
	}

}
