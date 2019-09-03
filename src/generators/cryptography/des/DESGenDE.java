package generators.cryptography.des;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public class DESGenDE implements ValidatingGenerator {
  private String               input;
  private ArrayProperties      arrayProps;
  private SourceCodeProperties sourceCodeProps;
  private String               key;
  private TextProperties       textProps;

  public void init() {

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    input = (String) primitives.get("input");
    arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");
    key = (String) primitives.get("key");
    textProps = (TextProperties) props.getPropertiesByName("textProps");
    DES des = new DES(Locale.GERMAN);
    des.init(arrayProps, sourceCodeProps, textProps);
    des.start(input, key);
    return des.lang.toString();
  }

  public String getName() {
    return "DES";
  }

  public String getAlgorithmName() {
    return "Data Encryption Standard (DES)";
  }

  public String getAnimationAuthor() {
    return "Andre Daube, Kokilan Kanesalingam";
    // Andre Daube <andredaube@yahoo.de>, Kokilan Kanesalingam
    // <kajan1@hotmail.de>";
  }

  public String getDescription() {
    return "<html><head><body><p class=\"MsoNormal\"><span class=\"MsoSubtleEmphasis\"><span lang=\"DE-DE\" style=\"font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;mso-ansi-language:EN-GB\">Bei <b>Data"
        + "\n"
        + "Encryption Standard (DES)</b> handelt es sich um einen symmetrischen Algorithmus, das heisst zur Ver- und Entschluesselung"
        + "\n"
        + "wird derselbe Schluessel verwendet. DES funktioniert als Blockchiffre, jeder Block wird also unter Verwendung des Schluessels"
        + "\n"
        + "einzeln chiffriert, wobei die Daten in 16 Iterationen beziehungsweise Runden von Substitutionen und Transpositionen (Permutation)"
        + "\n"
        + "nach dem Schema von Feistel 'verwuerfelt' werden. Die Blockgroesse betraegt 64 Bits, das heisst ein 64-Bit-Block Klartext wird"
        + "\n"
        + "in einen 64-Bit-Block Chiffretext transformiert. Auch der Schluessel, der diese Transformation kontrolliert, besitzt 64 Bits."
        + "\n"
        + "Jedoch stehen dem Benutzer von diesen 64 Bits nur 56 Bits zur Verfuegung, die uebrigen 8 Bits (jeweils ein Bit aus jedem Byte)"
        + "\n"
        + "werden zum Paritaets-Check benoetigt. Die effektive Schluessellaenge betraegt daher nur 56 Bits."
        + "\n"
        + "Die Entschluesselung wird mit dem gleichen Algorithmus durchgefuehrt, wobei die einzelnen Rundenschluessel in umgekehrter Reihenfolge"
        + "\n" + "verwendet werden.<o:p></o:p></span></span></p></body></html>";
  }

  public String getCodeExample() {
    return "#include <stdio.h>"
        + "\n"
        + "#include <math.h>"
        + "\n"
        + "int l[32],r[32],keys[16][48],ct[64];"
        + "\n"
        + "void sbox(int row,int col,int i,int p[],int loc)"
        + "\n"
        + "{"
        + "\n"
        + "	int sop,j,sbox[8][4][16]={14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7,0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8,4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0,15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13, //64 values - SBox1"
        + "\n"
        + "									 15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10,3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5,0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15,13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9, //SBox2"
        + "\n"
        + "									 10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8,13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1,13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7,1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12, //SBox3"
        + "\n"
        + "									 7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15,13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9,10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4,3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14, //SBox4"
        + "\n"
        + "									 2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9,14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6,4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14,11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3, //SBox5"
        + "\n"
        + "									 12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11,10,15,4,2,7,12,9,5,6,1,12,14,0,11,3,8,9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6,4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13, //SBox6"
        + "\n"
        + "									 4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1,13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6,1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2,6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12, //SBox7"
        + "\n"
        + "									 13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7,1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2,7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8,2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}; //SBox8"
        + "\n"
        + "	sop = sbox[i][row][col]; // i value gives SBox Number"
        + "\n"
        + "	for(;sop!=0;sop/=2)"
        + "\n"
        + "		p[loc--]=sop%2;"
        + "\n"
        + "}"
        + "\n"
        + "void cmp_fun(int round) // Complex Function . ."
        + "\n"
        + "{"
        + "\n"
        + " int E[]={32,1,2,3,4,5,4,5,6,7,8,9,8,9,10,11,12,13,12,13,14,15,16,17,16,17,18,19,20,21,20,21,22,23,24,25,24,25,26,27,28,29,28,29,30,31,32,1}; //Expansion Permutation (E)"
        + "\n"
        + " int Per[]={16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25}; //Permutation Function (P)"
        + "\n"
        + " int eo[48],i,j,k,p[32]={0},np[32],sip[8][6],row,col;"
        + "\n"
        + " for(i=0;i<48;i++)//Expansion Permutation (E)"
        + "\n"
        + "	 eo[i] = r[E[i]-1];"
        + "\n"
        + " j=0,k=0;"
        + "\n"
        + " for(i=0;i<48;i++) // Performing Xor with Keys"
        + "\n"
        + " {"
        + "\n"
        + "		sip[j][k] = eo[i]^keys[round][i];//round value gives Key Number"
        + "\n"
        + "		k++;"
        + "\n"
        + "		if(k==6)// 6 coz, using 6 bits as S-Box input"
        + "\n"
        + "			k=0,j++;"
        + "\n"
        + " }"
        + "\n"
        + " for(i=0;i<8;i++) // Calling 8 SBoxes"
        + "\n"
        + " {"
        + "\n"
        + "		row = sip[i][0]*2 + sip[i][5];"
        + "\n"
        + "		for(j=0;j<4;j++)"
        + "\n"
        + "		col += sip[i][j+1]*pow(2,(3-j));"
        + "\n"
        + "		sbox(row,col,i,p,(4*(i+1)-1)); //p[] stores Sbox Output ;; (4*(i+1)-1) gives the index in p[], to store the SBox Output"
        + "\n"
        + " }"
        + "\n"
        + " for(i=0;i<32;i++) // Permutation Function P"
        + "\n"
        + " 	np[i]=p[Per[i]-1];"
        + "\n"
        + " for(i=0;i<32;i++) // Xor with left bits"
        + "\n"
        + "	l[i] = l[i]^np[i];"
        + "\n"
        + "}"
        + "\n"
        + "void left_shift(int keyip[],int nob) //Left shift nob bits.. (nob - No. of Bits)"
        + "\n"
        + "{"
        + "\n"
        + "	int t1,t2,i;"
        + "\n"
        + "	while(nob>0)"
        + "\n"
        + "	{"
        + "\n"
        + "	 t1=keyip[0],t2=keyip[28];"
        + "\n"
        + "	 for(i=0;i<55;i++)"
        + "\n"
        + "	 if(i==27) continue;"
        + "\n"
        + "	 else"
        + "\n"
        + "		keyip[i]=keyip[i+1];"
        + "\n"
        + "	 keyip[27]=t1,keyip[55]=t2;"
        + "\n"
        + "	 nob--;"
        + "\n"
        + "	}"
        + "\n"
        + "}"
        + "\n"
        + "void toBin(int text[])"
        + "\n"
        + "{"
        + "\n"
        + "	int i,j,k,temp[64]={0};"
        + "\n"
        + "	int hex[16];"
        + "\n"
        + "	for(i=0;i<16;i++)"
        + "\n"
        + "		scanf(\"%x\",&hex[i]);"
        + "\n"
        + "	k=0;"
        + "\n"
        + "	for(i=0;i<16;i++)"
        + "\n"
        + "	{"
        + "\n"
        + "		j=0;"
        + "\n"
        + "		for(;hex[i]!=0;hex[i]/=2)"
        + "\n"
        + "			temp[j++]=hex[i]%2;"
        + "\n"
        + "		for(j=3;j>=0;j--)"
        + "\n"
        + "			text[k++]=temp[j];"
        + "\n"
        + "	}"
        + "\n"
        + "}"
        + "\n"
        + "void gen_keys()//Generating keys. . ."
        + "\n"
        + "{"
        + "\n"
        + " int key[64],keyip[56],i,j;"
        + "\n"
        + " int pc1[]={57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36,63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4}; //Permuted Choice 1"
        + "\n"
        + " int pc2[]={14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,13,2,41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32}; //Permuted Choice 2"
        + "\n"
        + " int left_shift_s[]={1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1}; //Schedule of Lift Shifts"
        + "\n"
        + " printf(\"Enter Key(0-F) (16 - HexCode):\");"
        + "\n"
        + " toBin(key);"
        + "\n"
        + " for(i=0;i<56;i++) // Permuted Choice 1"
        + "\n"
        + "	 keyip[i] = key[pc1[i]-1];"
        + "\n"
        + " for(j=0;j<16;j++)"
        + "\n"
        + " {"
        + "\n"
        + "	left_shift(keyip,left_shift_s[j]);"
        + "\n"
        + "	for(i=0;i<48;i++)"
        + "\n"
        + "		keys[j][i]=keyip[pc2[i]-1]; //Permuted Choice 2"
        + "\n"
        + " }"
        + "\n"
        + "}"
        + "\n"
        + "void main()"
        + "\n"
        + "{"
        + "\n"
        + "	int pt[64],i,t[64],j;"
        + "\n"
        + "	int ip[] ={58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7}; //Initail Permutation (IP)"
        + "\n"
        + "	int iip[]={40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25};//Inverse Initial Permutation (IP^-1)"
        + "\n"
        + "	gen_keys();"
        + "\n"
        + "	printf(\"Enter Plain Text(0-F) (16 - HexCode) :\");"
        + "\n"
        + "	toBin(pt);"
        + "\n"
        + "	printf(\"\nPlain Text\n\");"
        + "\n"
        + "	for(i=0;i<64;i++)"
        + "\n"
        + "	 printf(\"%d\",pt[i]);"
        + "\n"
        + "	for(i=0;i<64;i++) //Initial Permutatiion on Plain text"
        + "\n"
        + "	 if(i<32)"
        + "\n"
        + "		 l[i] = pt[ip[i]-1];"
        + "\n"
        + "	 else"
        + "\n"
        + "		 r[i-32] = pt[ip[i]-1];"
        + "\n"
        + "	for(i=0;i<16;i++)"
        + "\n"
        + "	{"
        + "\n"
        + "		cmp_fun(i); //Complex Function Round i"
        + "\n"
        + "		if(i<15) //Not Swapping for Round 16"
        + "\n"
        + "			for(j=0;j<32;j++) //Swapping left & right"
        + "\n"
        + "				r[j]=l[j]+r[j],l[j]=r[j]-l[j],r[j]=r[j]-l[j];"
        + "\n"
        + "	}"
        + "\n"
        + "	for(i=0;i<64;i++)"
        + "\n"
        + "		if(i<32)	t[i]=l[i];"
        + "\n"
        + "		else	t[i]=r[i-32];"
        + "\n"
        + "	printf(\"\n\nEncrypting . . . \nFinal Cipher Text after 16 rounds\n\");"
        + "\n"
        + "	for(i=0;i<64;i++)// Inverse Intial Permutation, ct -> Cipher Text"
        + "\n"
        + "		printf(\"%d\",ct[i]=t[iip[i]-1]);"
        + "\n"
        + "	//Decrypting . . ."
        + "\n"
        + "	for(i=0;i<64;i++) //Initial Permutatiion on Cipher text"
        + "\n"
        + "	 if(i<32)"
        + "\n"
        + "		 l[i] = ct[ip[i]-1];"
        + "\n"
        + "	 else"
        + "\n"
        + "		 r[i-32] = ct[ip[i]-1];"
        + "\n"
        + "	for(i=15;i>=0;i--)"
        + "\n"
        + "	{"
        + "\n"
        + "		cmp_fun(i); //Complex Function Round i"
        + "\n"
        + "		if(i>0) //Not Swapping for Round 0"
        + "\n"
        + "			for(j=0;j<32;j++) //Swapping left & right"
        + "\n"
        + "				r[j]=l[j]+r[j],l[j]=r[j]-l[j],r[j]=r[j]-l[j];"
        + "\n"
        + "	}"
        + "\n"
        + "	for(i=0;i<64;i++)"
        + "\n"
        + "		if(i<32)	t[i]=l[i];"
        + "\n"
        + "		else	t[i]=r[i-32];"
        + "\n"
        + " printf(\"\n\nDecrypting . . . \nFinal Plain Text after 16 rounds\n\");"
        + "\n"
        + "	for(i=0;i<64;i++)// Inverse Intial Permutation, ct -> Cipher Text"
        + "\n" + "		printf(\"%d\",ct[i]=t[iip[i]-1]);" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    input = (String) arg1.get("input");
    key = (String) arg1.get("key");
    if (input.length() != 64 || key.length() != 64)
      return false;
    for (int i = 0; i < input.length(); i++)
      if ((input.charAt(i) != '0' && input.charAt(i) != '1')
          || (key.charAt(i) != '0' && key.charAt(i) != '1'))
        return false;
    return true;
  }

}