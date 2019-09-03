package generators.cryptography;

import generators.cryptography.helpers.Ctr;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public class CtrGenerator implements Generator {
  private Language             lang;
  private int                  counterIncrease;
  private SourceCodeProperties sourceCodeProperties;
  private String               Message;
  private String               Blocksize;
  private String               BinaryCounterValue;
  private int[]                PermutationOrder;
  private ArrayProperties      arrayProps;

  @Override
  public void init() {
    lang = new AnimalScript("Counter Mode [EN]", "Andreas Pesak, Rolf Egert",
        800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    QuestionGroupModel groupInfo = new QuestionGroupModel("qgroup", 1);
    lang.addQuestionGroup(groupInfo);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    counterIncrease = (Integer) primitives.get("counterIncrease");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    Message = (String) primitives.get("Message");
    Blocksize = (String) primitives.get("Blocksize");
    BinaryCounterValue = (String) primitives.get("BinaryCounterValue");
    PermutationOrder = (int[]) primitives.get("PermutationOrder");
    arrayProps = (ArrayProperties) props.getPropertiesByName("array");

    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 24));

    RectProperties hRectProps = new RectProperties();
    hRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    hRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
    hRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    TextProperties normalTextProps = new TextProperties();
    normalTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SERIF, Font.PLAIN, 18));

    TextProperties monoTextProps = new TextProperties();
    monoTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));

    TextProperties monoTextBigProps = new TextProperties();
    monoTextBigProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 16));

    TextProperties monoTextGreenProps = new TextProperties();
    monoTextGreenProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    monoTextGreenProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));

    TextProperties monoBoldTextProps = new TextProperties();
    monoBoldTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.BOLD, 16));

    TextProperties boldTextProps = new TextProperties();
    boldTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 18));

    TextProperties italicRedProps = new TextProperties();
    italicRedProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.ITALIC, 12));
    italicRedProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    TextProperties italicBlackProps = new TextProperties();
    italicBlackProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.ITALIC, 12));
    italicBlackProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    RectProperties auxRectProps = new RectProperties();
    auxRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    auxRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    SourceCodeProperties descriptionProps = new SourceCodeProperties();
    descriptionProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.GREEN);
    descriptionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));

    PolylineProperties polyProps = new PolylineProperties();
    polyProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

    PolylineProperties polyProps1 = new PolylineProperties();
    polyProps1.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);

    Ctr counterMode = new Ctr(lang, Message, Blocksize, BinaryCounterValue,
        PermutationOrder, counterIncrease);
    counterMode.setProperties(headerProps, hRectProps, normalTextProps,
        auxRectProps, boldTextProps, descriptionProps, monoTextProps,
        monoTextGreenProps, arrayProps, italicBlackProps, monoBoldTextProps,
        monoTextBigProps, italicBlackProps, polyProps, polyProps1,
        sourceCodeProperties);
    try {
      counterMode.drawDescription();
    } catch (IllegalDirectionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    lang.finalizeGeneration();
    return lang.toString();
  }

  @Override
  public String getName() {
    return "Counter Mode [EN]";
  }

  @Override
  public String getAlgorithmName() {
    return "Counter Mode";
  }

  @Override
  public String getAnimationAuthor() {
    return "Andreas Pesak, Rolf Egert";
  }

  @Override
  public String getDescription() {
    return "<strong>Requirements:</strong>"
        + "\n"
        + "<ul>"
        + "\n"
        + "<li>the counter value just contains 0 and 1 as elements"
        + "<li>the number of digits the permutation uses must me equal to the number of digits of the counter value"
        + "<li>the number of digits of the blocksize must be lesser or equal to the number of digits of the counter value"
        + "\n"
        + "</ul>"
        + "\n"
        + "\n"
        + "A block cipher is an efficient algorithm that often uses a (strong,) keyed pseudorandom permutation to match blocks of a specific length on blocks of the same fixed length. A mode"
        + "\n"
        + "of operation is essentially a way of encrypting arbitrary-length messages using a block cipher. Note that arbitrary-length messages can be unambiguously padded to a total"
        + "\n"
        + "length that is a multiple of any desired block size by using specific padding techniques. Like OFB, counter mode can be viewed as a way of generating a pseudorandom stream"
        + "\n"
        + "from a block cipher."
        + "\n"
        + "    	"
        + "\n"
        + "\n"
        + "Two of the most renowned representatives as block ciphers are Data Encryption Standard (DES) and Advanced Encryption Standard (AES), whereat the former algorithm deemed"
        + "\n" + "to be broken, however the latter algorithm is in common use."
        + "\n" + "        ";
  }

  @Override
  public String getCodeExample() {
    return "in words step by step:"
        + "<ul>"
        + "     <li>choose a message m to encode"
        + "\n"
        + "     <li>Choose a blocksize n for the encryption"
        + "\n"
        + "     <li>choose a random binary value as a starting value for the counter"
        + "\n"
        + "     <li>split your message m into blocks of the length n"
        + "\n"
        + "     <li>apply the function to the counter value to get a temporary key in each encryption round"
        + "\n"
        + "     <li>use the XOR-function to combine the current message block with the corresponding temporary key (you need only the 0 to (n-1) least significant bits of the key, to macht the size of the message blocks)"
        + "\n"
        + "     <li>Increase the counter by an arbitrary but constant (for the entire encryption) value"
        + "\n"
        + "</ul>"
        + "\n"
        + "\n"
        + "counterMode(m, n){ <br>"
        + "\n<br>"
        + "\n"
        + "        <div style=\"text-indent:10px;\">  ctr = chooseRandomNumber(n); </div> <br>"
        + "\n"
        + "        <div style=\"text-indent:10px;\">  blocks = splitmessage(m,n); </div> <br>"
        + "\n"
        + " "
        + "\n"
        + "         <div style=\"text-indent:20px;\">for(i = 0; i < blocks.length; i++){ </div><br>"
        + "\n"
        + "         <div style=\"text-indent:25px;\">tmp = F(ctr); </div><br>"
        + "\n"
        + "         <div style=\"text-indent:25px;\">cipher[i] = blocks[i] XOR tmp[0:(n-1)];</div><br>"
        + "\n"
        + "         <div style=\"text-indent:20px;\">//The amount x to increase ctr is arbitrary but constant </div> <br>"
        + "\n"
        + "         <div style=\"text-indent:25px;\">ctr = ctr + x </div><br>"
        + "\n" + "        <div style=\"text-indent:20px;\">}</div> <br>" + "\n"
        + "}";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}