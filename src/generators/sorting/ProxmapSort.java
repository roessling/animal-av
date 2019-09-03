package generators.sorting;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.MultilineText;
import generators.helpers.OffsetCoords;
import generators.helpers.Pair;
import generators.helpers.TextUtil;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Circle;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;

/**
 * 
 * @author Dirk Kröhan, Kamil Erhard
 * 
 */

public class ProxmapSort extends AnnotatedAlgorithm implements Generator {

  private AnimalTextGenerator                      atg;

  private static final String                      DESCRIPTION = "Bucketsort (von engl. bucket „Eimer“) ist ein stabiles Sortierverfahren, das eine Liste in linearer Laufzeit sortieren kann, da es nicht auf Schlüsselvergleichen basiert.(Quelle:Wikipedia)<br>\n Die annehmbaren Werte der zu sortierenden Schlüssel müssen endlich sein, z.B. Zahlen von 0-9 oder Buchstaben von A-Z.<br>Die Sortierschlüssel werden dann durch eine Funktion auf die jeweiligen Buckets gemappt. Die Sortierung der Buckets kann durch jedes beliebige Sortierverfahren geschehen, hier Insertionsort."
                                                                   + "<br><br>\n"
                                                                   + "";
  private static final String                      SOURCE_CODE = "public ArrayList<Double> proxmapSort(Collection<Double> numbers) {\n"
                                                                   + "\tArrayList<ArrayList<Double>> buckets = new ArrayList<ArrayList<Double>>(10);\n"
                                                                   + "\tint initialBucketSize = numbers.size()/10;\n"
                                                                   + "\tfor (int i=0; i<buckets.size(); i++) {\n"
                                                                   + "\t\tbuckets.add(new ArrayList<Double>(initialBucketSize));\n"
                                                                   + "\t}\n"
                                                                   + "\tfor (double number : numbers) {\n"
                                                                   + "\t\tint bucketIndex = Math.floor(number);\n"
                                                                   + "\t\tArrayList<String> bucket = buckets.get(bucketIndex);\n"
                                                                   + "\t\tint index = 0;\n"
                                                                   + "\t\tfor (; i < bucket.size(); index++) {\n"
                                                                   + "\t\t\tif (number <= bucket.get(index)) {\n"
                                                                   + "\t\t\t\tbreak;\n"
                                                                   + "\t\t\t}\n"
                                                                   + "\t\t}\n"
                                                                   + "\t\tbucket.insert(index, number);\n"
                                                                   + "\t}\n"
                                                                   + "\tArrayList<Double> sortedList = new ArrayList<Double>();\n"
                                                                   + "\n"
                                                                   + "\tfor (ArrayList<Double> bucket : buckets) {\n"
                                                                   + "\t\tsortedList.addAll(bucket);\n"
                                                                   + "\t}\n"
                                                                   + "\treturn sortedList;\n"
                                                                   + "}";

  public boolean                                   isAnimalLoaded;
  private TextProperties                           dataTextProps;
  private Color                                    elementHighLightColor;
  private Color                                    bucketHighLightColor;
  private Color                                    compareElementHighLightColor;

  private SourceCode                               helpText;
  private SourceCodeProperties                     helpTextProps;

  private SourceCodeProperties                     sourceCodeProps;

  private int                                      lastHelpTextHighLight;

  private Group                                    lkw;
  private Coordinates                              lkwStartPoint;

  private TreeMap<Character, Pair<Integer, Group>> bucketList;
  private Map<Character, ArrayList<MultilineText>> bucketTextList;

  private static String generateRandomString(String allowedChars, Random random) {
    int max = allowedChars.length();
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < 6; i++) {
      int value = random.nextInt(max);
      buffer.append(allowedChars.charAt(value));
    }
    return buffer.toString();
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    this.dataTextProps = (TextProperties) props
        .getPropertiesByName("dataTextProperties");
    this.elementHighLightColor = (Color) primitives
        .get("elementHighLightColor");
    this.compareElementHighLightColor = (Color) primitives
        .get("compareElementHighLightColor");
    this.bucketHighLightColor = (Color) primitives.get("bucketHighLightColor");
    this.helpTextProps = (SourceCodeProperties) props
        .getPropertiesByName("helpTextProperties");
    this.sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    Integer numberOfInputDates = (Integer) primitives.get("numberOfInputDates");
    int seed = (Integer) primitives.get("seed");

    TextUtil.hashMap.clear();

    this.bucketList = new TreeMap<Character, Pair<Integer, Group>>();
    this.bucketTextList = new TreeMap<Character, ArrayList<MultilineText>>();

    this.init();

    TextUtil.setFont((Font) this.dataTextProps.get("font"));

    this.atg = new AnimalTextGenerator(this.lang);
    this.lang.setStepMode(true);

    String allowedChars = "0123456789";
    char[] charArray = allowedChars.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      this.buildBucket(new Coordinates(100 + i * 40, 100), 25, 30,
          charArray[i], i);
    }
    this.buildHelpTextAndSourceCode(this.helpTextProps, this.sourceCodeProps);

    this.lkwStartPoint = new Coordinates(100, 10);
    this.buildLKW(20, 80);

    String[] test = new String[numberOfInputDates];

    Random random = new Random(seed);

    for (int i = 0; i < numberOfInputDates; i++) {
      String randomString = generateRandomString(allowedChars, random);
      test[i] = randomString;
    }

    LinkedList<MultilineText> inputTexts = new LinkedList<MultilineText>();

    Coordinates c = new Coordinates(10, 10);
    for (String element : test) {
      MultilineText multiLineLkwText = new MultilineText(this.atg, c, element,
          "lkwText", this.dataTextProps, 10);
      inputTexts.add(multiLineLkwText);
      c = new OffsetCoords(c, 0, 15);
    }

    this.vars.declare("int", "numbers.size",
        Integer.toString(numberOfInputDates));

    this.highlightSourceCode(0);
    this.lang.nextStep();
    this.highlightSourceCode(1);
    this.helpText.highlight(0);
    this.lastHelpTextHighLight = 0;
    this.lang.nextStep();
    this.highlightSourceCode(2);
    this.vars.set("initialBucketSize",
        Integer.toString(numberOfInputDates / 10));
    this.lang.nextStep();
    for (char element : charArray) {
      this.highlightSourceCode(3);

      this.lang.nextStep();
      this.bucketList.get(element).getY().show();
      this.highlightSourceCode(4);
      this.lang.nextStep();
      this.highlightSourceCode(5);
      this.lang.nextStep();
    }
    for (String string : test) {
      this.highlightSourceCode(6);
      this.vars.set("number", string);

      this.highlightHelpText(1);
      MultilineText m = inputTexts.pop();
      m.changeColor(AnimalScript.COLORCHANGE_COLOR, this.elementHighLightColor,
          null, null);
      this.lang.nextStep();

      this.highlightSourceCode(7);
      this.vars.set("bucketIndex", Character.toString(string.charAt(0)));

      for (MultilineText mt : inputTexts) {
        mt.moveBy("translate", 0, -15, null, new MsTiming(200));
      }

      this.moveLkw(string.charAt(0), m);
      m.changeColor(AnimalScript.COLORCHANGE_COLOR,
          (Color) this.dataTextProps.get("color"), null, null);
    }
    this.highlightSourceCode(6);
    this.lang.nextStep();
    this.highlightSourceCode(16);
    this.lang.nextStep();
    this.highlightSourceCode(17);
    this.lang.nextStep();
    this.highlightHelpText(7);
    c = new Coordinates(10, 10);
    Group bucket;
    for (Character ch : this.bucketTextList.keySet()) {
      this.highlightSourceCode(18);
      bucket = this.bucketList.get(ch).getY();
      bucket.changeColor(AnimalScript.COLORCHANGE_COLOR,
          this.bucketHighLightColor, null, null);
      this.lang.nextStep();
      this.highlightSourceCode(19);
      // inputTexts.addAll(list);
      for (MultilineText mt : this.bucketTextList.get(ch)) {
        mt.normalizeText(null, null);
        mt.moveTo(c, null, new MsTiming(1000));
        c = new OffsetCoords(c, 0, 15);
      }
      this.lang.nextStep();
      bucket.changeColor(AnimalScript.COLORCHANGE_COLOR,
          (Color) this.dataTextProps.get("color"), null, null);
    }
    this.highlightSourceCode(18);
    this.lang.nextStep();
    this.highlightSourceCode(20);
    this.lang.nextStep();
    this.highlightSourceCode(21);
    return this.lang.toString();
  }

  private void buildBucket(Coordinates c, int height, int width,
      char character, int index) {
    int x = c.getX();
    int y = c.getY();
    Coordinates textCoordinates = new Coordinates(x + height / 2, y + width / 4);
    Node[] bucketPolyline = new Node[4];
    bucketPolyline[0] = new Coordinates(x, y);
    bucketPolyline[1] = new Coordinates(x, y + height);
    bucketPolyline[2] = new Coordinates(x + width, y + height);
    bucketPolyline[3] = new Coordinates(x + width, y);

    Polyline p = this.lang.newPolyline(bucketPolyline, "bucketPolyline-"
        + character, null);
    Text t = this.lang.newText(textCoordinates, String.valueOf(character),
        "bucketText-" + character, null, new TextProperties());

    LinkedList<Primitive> bucketGroupList = new LinkedList<Primitive>();
    bucketGroupList.add(p);
    bucketGroupList.add(t);
    Group g = this.lang.newGroup(bucketGroupList, "bucket-" + character);
    g.hide();
    this.bucketList.put(character, new Pair<Integer, Group>(index, g));
  }

  private void buildLKW(int height, int width) {
    int x = this.lkwStartPoint.getX();
    int y = this.lkwStartPoint.getY();
    Node[] bucketPolyline = new Node[8];
    bucketPolyline[0] = new Coordinates(x, y);
    bucketPolyline[1] = new Coordinates(x, y + height);
    bucketPolyline[2] = new Coordinates(x + width + (int) (height / 1.5), y
        + height);
    bucketPolyline[3] = new Coordinates(x + width + (int) (height / 1.5), y
        + height / 3);
    bucketPolyline[4] = new Coordinates(x + width, y + height / 3);
    bucketPolyline[5] = new Coordinates(x + width, y + height);
    bucketPolyline[6] = new Coordinates(x + width, y);
    bucketPolyline[7] = new Coordinates(x, y);
    Polyline p = this.lang.newPolyline(bucketPolyline, "lkw", null);

    Circle c1 = this.lang.newCircle(new Coordinates(x + width / 5, y + height
        + height / 5), height / 5, "tire1", null);
    Circle c2 = this.lang.newCircle(new Coordinates(x + width / 5 + height / 5
        * 2, y + height + height / 5), height / 5, "tire1", null);
    Circle c3 = this.lang.newCircle(new Coordinates(x + width + height / 4, y
        + height + height / 5), height / 5, "tire2", null);
    LinkedList<Primitive> lkwPrimitveList = new LinkedList<Primitive>();
    lkwPrimitveList.add(p);
    lkwPrimitveList.add(c1);
    lkwPrimitveList.add(c2);
    lkwPrimitveList.add(c3);

    this.lkw = this.lang.newGroup(lkwPrimitveList, "lkw");
  }

  private void moveLkw(char bucketNumber, MultilineText multiLineLkwText) {
    Pair<Integer, Group> bucket = this.bucketList.get(bucketNumber);
    int x = ((Coordinates) ((Polyline) bucket.getY().getPrimitives().getFirst())
        .getNodes()[3]).getX();

    multiLineLkwText.moveTo(new OffsetCoords(this.lkwStartPoint, 5, 0), null,
        null);

    this.lang.nextStep();
    this.highlightSourceCode(8);

    this.highlightHelpText(2);
    Polyline line = TextUtil.getVia(this.lang, this.lkwStartPoint.getX(),
        this.lkwStartPoint.getY(), x, this.lkwStartPoint.getY());

    try {
      this.lkw.moveVia(AnimalScript.DIRECTION_W, "translate", line, null,
          new MsTiming((bucket.getX() + 1) * 100));
    } catch (IllegalDirectionException e1) {
      e1.printStackTrace();
    }

    multiLineLkwText.moveBy("translate", x - this.lkwStartPoint.getX(), 0,
        null, new MsTiming((bucket.getX() + 1) * 100));
    bucket.getY().changeColor(AnimalScript.COLORCHANGE_COLOR,
        this.bucketHighLightColor, null, null);
    this.lang.nextStep();
    this.highlightSourceCode(9);
    this.lang.nextStep();

    multiLineLkwText.moveBy("translate", -45, 0, null, new MsTiming(200));

    multiLineLkwText.rotateText(new MsTiming(200), null);

    multiLineLkwText.moveTo(new OffsetCoords(((Polyline) bucket.getY()
        .getPrimitives().getFirst()).getNodes()[0], 10, 30), new MsTiming(200),
        new MsTiming(200));

    this.insertIntoBucket(bucketNumber, multiLineLkwText);
    this.lang.nextStep();
    bucket.getY().changeColor(AnimalScript.COLORCHANGE_COLOR,
        (Color) this.dataTextProps.get("color"), null, null);

    line = TextUtil.getVia(this.lang, x, this.lkwStartPoint.getY(),
        this.lkwStartPoint.getX(), this.lkwStartPoint.getY());

    try {
      this.lkw.moveVia(AnimalScript.DIRECTION_W, "translate", line, null, null);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
  }

  private void insertIntoBucket(char bucketNumber, MultilineText text) {
    ArrayList<MultilineText> texts = this.bucketTextList.get(bucketNumber);

    int i = 0;

    if (texts == null) {
      this.highlightSourceCode(10);
      this.vars.set("bucket.size", "0");

      this.lang.nextStep();
      this.highlightSourceCode(14);
      this.lang.nextStep();
      this.highlightSourceCode(15);
      texts = new ArrayList<MultilineText>();
      this.bucketTextList.put(bucketNumber, texts);
      this.highlightHelpText(3);
      text.moveBy("translate", 10, 0, new MsTiming(200), new MsTiming(200));
    } else {
      boolean insert = false;
      this.highlightSourceCode(10);
      this.vars.set("bucket.size", Integer.toString(texts.size()));

      this.highlightHelpText(4);
      this.lang.nextStep();
      for (i = 0; i < texts.size(); i++) {
        this.highlightSourceCode(11);
        this.vars.set("bucket.get", texts.get(i).getText());

        texts.get(i).changeColor(AnimalScript.COLORCHANGE_COLOR,
            this.compareElementHighLightColor, null, null);
        this.lang.nextStep();
        if (text.getText().compareTo(texts.get(i).getText()) <= 0) {
          this.highlightSourceCode(12);
          this.lang.nextStep();
          this.highlightSourceCode(14);
          this.lang.nextStep();
          this.highlightSourceCode(15);
          this.highlightHelpText(5);
          for (int j = i; j < texts.size(); j++) {
            texts.get(j).moveBy("translate", 0, text.getHeight() + 20, null,
                new MsTiming(200));
          }
          texts.get(i).changeColor(AnimalScript.COLORCHANGE_COLOR,
              (Color) this.dataTextProps.get("color"), null, null);
          text.moveBy("translate", 10, 0, new MsTiming(200), new MsTiming(200));
          insert = true;
          break;
        } else {
          this.highlightSourceCode(13);
          this.lang.nextStep();
          this.highlightHelpText(4);
          text.moveTo(new OffsetCoords(texts.get(i).getUpperLeft(), -10, texts
              .get(i).getHeight() + 20), null, new MsTiming(200));
          texts.get(i).changeColor(AnimalScript.COLORCHANGE_COLOR,
              (Color) this.dataTextProps.get("color"), null, null);
          this.highlightSourceCode(10);
          this.lang.nextStep();
        }
      }
      if (!insert) {
        this.highlightSourceCode(14);
        this.lang.nextStep();
        this.highlightSourceCode(15);
        text.moveBy("translate", 10, 0, new MsTiming(200), new MsTiming(200));
        this.highlightHelpText(6);
      }

    }
    texts.add(i, text);
  }

  private void buildHelpTextAndSourceCode(SourceCodeProperties helpTextprops,
      SourceCodeProperties sourceCodeProps) {
    Coordinates c = ((Coordinates) ((Polyline) this.bucketList.lastEntry()
        .getValue().getY().getPrimitives().getFirst()).getNodes()[3]);
    // now, create the source code entity
    Node oc = new OffsetCoords(c, 100, -100);
    this.helpText = this.lang.newSourceCode(oc, "helpText", null,
        this.helpTextProps);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display delay
    this.helpText.addCodeLine("0. Bucketerzeugung", null, 0, null);
    this.helpText.addCodeLine("1. Neues Element auswählen", null, 0, null);
    this.helpText.addCodeLine("2. Bucket berechnen", null, 0, null);
    this.helpText.addCodeLine("3.a Falls Bucketliste leer, Element einfügen",
        null, 0, null); // 0
    this.helpText.addCodeLine("3.b Sonst Vergleich mit nächstem Listenelement",
        null, 0, null); // 1
    this.helpText.addCodeLine("3.b.1 Falls kleiner, davor einfügen", null, 1,
        null); // 2
    this.helpText.addCodeLine(
        "3.b.2 Falls Listenende erreicht, Element anhängen", null, 1, null);
    this.helpText.addCodeLine(
        "4. Inhalt der Buckets in Ergebnisliste verschieben.", null, 0, null);

    this.sourceCode = this.lang.newSourceCode(new OffsetCoords(oc, 0, 150),
        "sourceCode", null, sourceCodeProps);
    this.parse();
  }

  private void highlightHelpText(int line) {
    this.helpText.unhighlight(this.lastHelpTextHighLight);
    this.helpText.highlight(line);
    this.lastHelpTextHighLight = line;
  }

  private void highlightSourceCode(int line) {
    // this.sourceCode.unhighlight(this.lastSourceCodeHighLight);
    // this.sourceCode.highlight(line);
    // this.lastSourceCodeHighLight = line;
    this.exec("LINE" + (line + 1));
  }

  @Override
  public void init() {
    super.init();
  }

  @Override
  public String getAlgorithmName() {
    return "Proxmap-Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Kamil Erhard, Dirk Kröhan";
  }

  @Override
  public String getCodeExample() {
    return SOURCE_CODE;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
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
    return "ProxmapSort";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public String getAnnotatedSrc() {
    return "public ArrayList<Double> proxmapSort(Collection<Double> numbers) { @label(\"LINE1\") \n"
        + " ArrayList<ArrayList<Double>> buckets = new ArrayList<ArrayList<Double>>(10); @label(\"LINE2\") \n"
        + " int initialBucketSize = numbers.size()/10; @label(\"LINE3\") @declare(\"int\", \"initialBucketSize\") @declare(\"int\", \"i\", \"-1\") \n"
        + " for (int i=0; i<10; i++) { @label(\"LINE4\") @inc(\"i\") \n"
        + "  buckets.add(new ArrayList<Double>(initialBucketSize)); @label(\"LINE5\") \n"
        + " } @label(\"LINE6\") \n"
        + " for (double number : numbers) { @label(\"LINE7\") @discard(\"i\") @declare(\"String\", \"number\") \n"
        + "  int bucketIndex = Math.floor(number); @label(\"LINE8\") @declare(\"String\", \"bucketIndex\") \n"
        + "  ArrayList<String> bucket = buckets.get(bucketIndex); @label(\"LINE9\") \n"
        + "  int index = 0; @label(\"LINE10\") @declare(\"int\", \"index\", \"0\") @declare(\"int\", \"bucket.size\") \n"
        + "  for (; index < bucket.size(); index++) { @label(\"LINE11\") \n"
        + "   if (number <= bucket.get(index)) { @label(\"LINE12\") @inc(\"index\") @declare(\"String\", \"bucket.get\") \n"
        + "    break; @label(\"LINE13\") \n"
        + "   } @label(\"LINE14\") \n"
        + "  } @label(\"LINE15\") @discard(\"bucket.get\") @discard(\"bucket.size\") \n"
        + "  bucket.insert(index, number); @label(\"LINE16\") @discard(\"index\") @discard(\"bucketIndex\") \n"
        + " } @label(\"LINE17\") @discard(\"number\") \n"
        + " ArrayList<Double> sortedList = new ArrayList<Double>(); @label(\"LINE18\") \n"
        +

        " for (ArrayList<Double> bucket : buckets) { @label(\"LINE19\") \n"
        + "  sortedList.addAll(bucket); @label(\"LINE20\") \n"
        + " } @label(\"LINE21\") \n"
        + " return sortedList; @label(\"LINE22\") \n" + "} @label(\"LINE23\") ";
  }
}
