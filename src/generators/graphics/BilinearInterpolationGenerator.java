package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class BilinearInterpolationGenerator implements Generator {
  private Language             lang;
  private TextProperties       normalText;
  private int[][]              intMatrix;
  private SourceCodeProperties sourceCode;
  private MatrixProperties     matrix;
  private TextProperties       headlines;
  private TextProperties       header;

  // private boolean error = false;

  public void init() {
    lang = new AnimalScript("Bilinear Interpolation in Image Processing [EN]",
        "Natalie Faber <faber@d120.de>, Sascha Weiss <sascha@d120.de>", 800,
        600);
  }

  public boolean check_matrix(int[][] m) {
    int notzero = 0;
    for (int i = 0; i < m.length; i++)
      for (int j = 0; j < m[0].length; j++) {
        if (m[i][j] != 0)
          notzero++;
        if (notzero >= 4)
          return true;
      }
    if (notzero >= 4)
      return true;
    return false;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    normalText = (TextProperties) props.getPropertiesByName("normalText"); //
    intMatrix = (int[][]) primitives.get("intMatrix"); //
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode"); //
    matrix = (MatrixProperties) props.getPropertiesByName("matrix"); //
    headlines = (TextProperties) props.getPropertiesByName("headlines"); //
    header = (TextProperties) props.getPropertiesByName("header"); //

    lang.setStepMode(true);
    bilinear_interpolation(intMatrix);

    return lang.toString();
  }

  // ALGO

  private void bilinear_interpolation(int[][] m) {
    boolean init_distance = false;
    int hh = 0;

    // Header
    // Eigenschaften unseres Headers definieren
    // TextProperties headerTextProperties = new TextProperties();
    // headerTextProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    // headerTextProperties.set(AnimationPropertiesKeys.NAME, "header");
    // headerTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("SansSerif",
    // Font.BOLD, 24));
    // headerTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
    // Color.BLACK);

    // Text des Headers erstellen und setzen
    Text header = lang.newText(new Coordinates(20, 30),
        "Bilinear Interpolation in Image Processing", "header", null,
        this.header);

    // Umrandung des Headers
    // Umrandungs Eigenschaften
    RectProperties rectHeaderTextProperties = new RectProperties();
    rectHeaderTextProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectHeaderTextProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.WHITE);
    rectHeaderTextProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    // Rechteck um den Header setzen
    lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5, 5, header,
        "SE"), "rectheader", null, rectHeaderTextProperties);

    lang.nextStep();

    if (!check_matrix(m)) {
      TextProperties errorProp = new TextProperties();

      errorProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
      errorProp.set(AnimationPropertiesKeys.NAME, "header");
      errorProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "SansSerif", Font.BOLD, 24));
      errorProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

      // error.
      lang.newText(new Offset(-10, 90, header, "SW"),
          "Error: The algorithm needs an minimum of 4 known", "error", null,
          errorProp);
      lang.newText(new Offset(-10, 130, header, "SW"), "values in the matrix",
          "error2", null, errorProp);
      lang.nextStep("Error");
      return;
    }

    // Headline
    // Eigenschaften unseres Headlines definieren
    TextProperties headlineTextProperties = headlines;

    // TextProperties headlineTextProperties = new TextProperties();
    // headlineTextProperties.set(AnimationPropertiesKeys.NAME, "headline");
    // headlineTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("SansSerif",
    // Font.BOLD, 18));
    // headlineTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
    // Color.BLACK);

    // Normal
    // Eigenschaften unseres normalen Textes definieren

    TextProperties normalTextProperties = normalText;

    /*
     * TextProperties normalTextProperties = new TextProperties();
     * normalTextProperties.set(AnimationPropertiesKeys.NAME, "normal");
     * normalTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new
     * Font("SansSerif", Font.PLAIN, 17));
     * normalTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
     * Color.BLACK);
     */
    // Text der Headline description0 erstellen und setzen
    Text description0 = lang.newText(new Offset(-10, 60, header, "SW"),
        "Description:", "description0", null, headlineTextProperties);

    // Normaltext description1 bis description6 erstellen und setzen
    Text description1 = lang
        .newText(
            new Offset(-10, 90, header, "SW"),
            "Bilinear interpolation is used in image processing to reduce some of the visual distortion.",
            "description1", null, normalTextProperties);
    Text description2 = lang
        .newText(
            new Offset(-10, 115, header, "SW"),
            "If we have a picture with unknown values, bilinear interpolation takes the nearest 2x2 known values in the picture",
            "description2", null, normalTextProperties);
    Text description3 = lang
        .newText(
            new Offset(-10, 140, header, "SW"),
            "and multiplies each of them with 1 - the distance to the unknown value.",
            "description3", null, normalTextProperties);
    Text description4 = lang
        .newText(
            new Offset(-10, 165, header, "SW"),
            "The resulted values will be summarized, devided by 4 and this resulted value is the new value where before the unknown value was.",
            "description4", null, normalTextProperties);
    Text description5 = lang
        .newText(
            new Offset(-10, 190, header, "SW"),
            "If all the nearest neighbors are exactly next to the unknown value, bilinear interpolation just summarize, devide them by 4",
            "description5", null, normalTextProperties);
    Text description6 = lang.newText(new Offset(-10, 215, header, "SW"),
        "and refresh the unknown value with this new computed value.",
        "description6", null, normalTextProperties);

    lang.nextStep("Description of the Algorithm");

    // Text Ueberschrift: Example
    lang.newText(new Offset(-10, 60, header, "SW"), "Example:", "example",
        null, headlineTextProperties);

    // MATRIX
    int zeile = m.length;
    int spalte = m[0].length;

    // Obere Linke Ecke unterhalb der Matrix!
    int u_matrix = (zeile + 1) * 30 + 90;

    // Matrix erstellen
    MatrixProperties matrixProperties = matrix;
    /*
     * MatrixProperties matrixProperties = new MatrixProperties();
     * matrixProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.WHITE);
     */
    matrixProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

    IntMatrix matrix = lang.newIntMatrix(new Offset(0, 90, header, "SW"), m,
        "matrix", null, matrixProperties);

    // SourceCode Block

    SourceCodeProperties InterpolationSourceProperties = sourceCode;

    // SourceCodeProperties InterpolationSourceProperties = new
    // SourceCodeProperties();
    // InterpolationSourceProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.RED);
    // InterpolationSourceProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
    // Color.BLACK);

    SourceCode InterpolationSource = lang.newSourceCode(new Offset(0, u_matrix,
        header, "SW"), "InterpolationSource", null,
        InterpolationSourceProperties);
    InterpolationSource.addCodeLine(
        "1. To show how the algorithm works, we generate a " + zeile + "x"
            + spalte + " matrix and fill it with some values", null, 0, null);
    InterpolationSource.addCodeLine(
        "2. Move throw the matrix until we find an unknown value (0)", null, 0,
        null);
    InterpolationSource.addCodeLine(
        "3. Find the four nearest known neighbors (!= 0)", null, 0, null);
    InterpolationSource.addCodeLine(
        "a.1. Compute the distance of the nearest neighbors", null, 1, null);
    InterpolationSource
        .addCodeLine(
            "a.2. Multiply the four values with 1 - their distance to the unknown point",
            null, 1, null);
    InterpolationSource.addCodeLine(
        "4. Summarize the four values and devide them by 4", null, 0, null);
    InterpolationSource
        .addCodeLine(
            "5. Refresh the matrix and proceed with the second step until the whole matrix has been cycled",
            null, 0, null);

    InterpolationSource.highlight(0);
    // Text Ueberschrift: Calculations & Explanition
    lang.newText(new Offset(250, u_matrix, header, "SE"),
        "Calculations & Explanations:", "calculations", null,
        headlineTextProperties);

    // Explanition fuer diesen Schritt
    Text explain1 = lang.newText(new Offset(250, u_matrix + 20, header, "SE"),
        "Our matrix contains the values of a picture in the RGB color model.",
        "explain1", null, normalTextProperties);
    Text explain2 = lang.newText(new Offset(250, u_matrix + 40, header, "SE"),
        "To display a whole RGB picture, we need 3 matrices.", "explain2",
        null, normalTextProperties);
    Text explain3 = lang.newText(new Offset(250, u_matrix + 60, header, "SE"),
        "One for the red values, second  for the blue values", "explain3",
        null, normalTextProperties);
    Text explain4 = lang.newText(new Offset(250, u_matrix + 80, header, "SE"),
        "and the third one for the green values.", "explain4", null,
        normalTextProperties);
    Text explain5 = lang.newText(new Offset(250, u_matrix + 100, header, "SE"),
        "The color values red/green/blue can be between 0 and 255.",
        "explain5", null, normalTextProperties);
    Text explain6 = lang.newText(new Offset(250, u_matrix + 120, header, "SE"),
        "For our example we just need one matrix to show how one of the 3",
        "explain6", null, normalTextProperties);
    Text explain7 = lang.newText(new Offset(250, u_matrix + 140, header, "SE"),
        "colors in a picture can be interpolated.", "explain7", null,
        normalTextProperties);

    description0.hide();
    description1.hide();
    description2.hide();
    description3.hide();
    description4.hide();
    description5.hide();
    description6.hide();

    lang.nextStep("Initialize");

    explain1.hide();
    explain2.hide();
    explain3.hide();
    explain4.hide();
    explain5.hide();
    explain6.hide();
    explain7.hide();

    InterpolationSource.unhighlight(0);
    InterpolationSource.highlight(1);

    // float d = 0;
    String z1 = "", z2 = "", z3 = "";
    int erg, i = 0;

    float v1, v2, v3, v4;

    // Alle Texte für den Algo setzen
    Text note1 = lang.newText(new Offset(250, u_matrix + 20, header, "SE"),
        "The values " + z1 + " should be marked orange.", "note1", null,
        normalTextProperties);
    Text note3 = lang.newText(new Offset(250, u_matrix + 40, header, "SE"),
        "Since all our neighbors are exactly next to the unknown value, ",
        "note3", null, normalTextProperties);
    Text note4 = lang.newText(new Offset(250, u_matrix + 60, header, "SE"),
        "we can go to step 4.", "note4", null, normalTextProperties);
    Text note5 = lang.newText(new Offset(250, u_matrix + 20, header, "SE"), "",
        "note5", null, normalTextProperties);
    Text note6 = lang.newText(new Offset(250, u_matrix + 40, header, "SE"),
        "To use bilinear interpolation, we need 4 neighbors. So we choose",
        "note6", null, normalTextProperties);
    Text note7 = lang.newText(new Offset(250, u_matrix + 60, header, "SE"),
        "randomly neighbors of the next nearest neighbors. To get 4 neighbors",
        "note7", null, normalTextProperties);
    Text note8 = lang.newText(new Offset(250, u_matrix + 80, header, "SE"), "",
        "note8", null, normalTextProperties);
    Text note9 = lang.newText(new Offset(250, u_matrix + 100, header, "SE"),
        "", "note9", null, normalTextProperties);

    Text addieren = lang.newText(new Offset(250, u_matrix + 20, header, "SE"),
        "", "addieren", null, normalTextProperties);

    Text distance1 = lang
        .newText(
            new Offset(250, u_matrix + 20, header, "SE"),
            "Measuring the distance within a matrix is fairly simple: by evaluating the",
            "distance1", null, normalTextProperties);
    Text distance2 = lang
        .newText(
            new Offset(250, u_matrix + 40, header, "SE"),
            "distance between a row or a column within the matrix reveals the farthest range (1).",
            "distance2", null, normalTextProperties);
    Text distance3 = lang
        .newText(
            new Offset(250, u_matrix + 60, header, "SE"),
            "This means, that the maximum distance within the matrix has to be divided by the",
            "distance3", null, normalTextProperties);
    Text distance4 = lang
        .newText(
            new Offset(250, u_matrix + 80, header, "SE"),
            "values of the rows and columns. The resulted value is the distance from value to value.",
            "distance4", null, normalTextProperties);
    Text distance5 = lang.newText(
        new Offset(250, u_matrix + 100, header, "SE"), "", "distance5", null,
        normalTextProperties);
    Text distance6 = lang.newText(
        new Offset(250, u_matrix + 120, header, "SE"), "", "distance6", null,
        normalTextProperties);

    Text value1 = lang.newText(new Offset(250, u_matrix + 20, header, "SE"),
        "", "value1", null, normalTextProperties);
    Text value2 = lang.newText(new Offset(250, u_matrix + 40, header, "SE"),
        "", "value2", null, normalTextProperties);
    Text value3 = lang.newText(new Offset(250, u_matrix + 60, header, "SE"),
        "", "value3", null, normalTextProperties);
    Text value4 = lang.newText(new Offset(250, u_matrix + 80, header, "SE"),
        "", "value4", null, normalTextProperties);

    Text init_distance1 = lang.newText(new Offset(250, u_matrix + 20, header,
        "SE"), "We know the farthest range is 1.", "init_distance1", null,
        normalTextProperties);
    Text init_distance2 = lang.newText(new Offset(250, u_matrix + 40, header,
        "SE"), "Now the distance from one value to another can be determined.",
        "init_distance2", null, normalTextProperties);
    Text init_distance3 = lang.newText(new Offset(250, u_matrix + 60, header,
        "SE"), "As previously stated, we have " + zeile + " (row) and "
        + spalte + " (column).", "init_distance3", null, normalTextProperties);
    Text init_distance4 = lang.newText(new Offset(250, u_matrix + 80, header,
        "SE"),
        "Which means that the farthest range of the higher number is 1.",
        "init_distance4", null, normalTextProperties);

    Text init_distance5 = lang.newText(new Offset(250, u_matrix + 20, header,
        "SE"), "Init 5", "init_distance5", null, normalTextProperties);
    Text init_distance6 = lang.newText(new Offset(250, u_matrix + 40, header,
        "SE"), "Init 6", "init_distance6", null, normalTextProperties);
    Text init_distance7 = lang.newText(new Offset(250, u_matrix + 60, header,
        "SE"), "Init 7", "init_distance7", null, normalTextProperties);
    Text init_distance8 = lang.newText(new Offset(250, u_matrix + 80, header,
        "SE"), "Init 8", "init_distance8", null, normalTextProperties);
    Text init_distance9 = lang.newText(new Offset(250, u_matrix + 100, header,
        "SE"), "Init 9", "init_distance9", null, normalTextProperties);

    Text new_value = lang.newText(new Offset(250, u_matrix + 20, header, "SE"),
        "", "new_value", null, normalTextProperties);

    ArrayList<MatrixElement> new_values = new ArrayList<MatrixElement>();
    ArrayList<MatrixElement> four_n = new ArrayList<MatrixElement>();

    addieren.hide();
    note1.hide();
    note3.hide();
    note4.hide();
    note5.hide();
    note6.hide();
    note7.hide();
    note8.hide();
    note9.hide();

    distance1.hide();
    distance2.hide();
    distance3.hide();
    distance4.hide();
    distance5.hide();
    distance6.hide();

    init_distance1.hide();
    init_distance2.hide();
    init_distance3.hide();
    init_distance4.hide();
    init_distance5.hide();
    init_distance6.hide();
    init_distance7.hide();
    init_distance8.hide();
    init_distance9.hide();

    value1.hide();
    value2.hide();
    value3.hide();
    value4.hide();

    new_value.hide();

    // if (zeile < spalte)
    // d = 1 / spalte;
    // else
    // d = 1 / zeile;

    for (int y = 0; y < zeile; y++) {
      for (int x = 0; x < spalte; x++) {
        if (m[y][x] == 0) {
          matrix.highlightCell(y, x, null, null);
          lang.nextStep("Search a unknown Value");
          matrix.unhighlightCell(y, x, null, null);
          InterpolationSource.unhighlight(1);
          InterpolationSource.highlight(2);
          Neighbours n = new Neighbours(m);

          ArrayList<MatrixElement> points = n.getExactNeighboursOf(x, y);
          if (points.size() == 4) {
            z1 = "";
            z2 = "";
            // Falls es 4 exakte Nachbarn gibt!

            for (MatrixElement item : points) {
              z1 += " " + m[item.getY()][item.getX()] + ",";
              matrix.highlightCell(item.getY(), item.getX(), null, null);
            }
            note1.setText("The values " + z1.substring(0, z1.length() - 1)
                + " should be marked yellow.", null, null);
            note1.show();
            note3.show();
            note4.show();
            lang.nextStep("-> Search 4 Neighbours for value (" + x + "," + y
                + ")");
            InterpolationSource.unhighlight(2);
            InterpolationSource.highlight(5);
            note1.hide();
            note3.hide();
            note4.hide();

            // addieren berrechnen :)
            erg = (points.get(0).getValue() + points.get(1).getValue()
                + points.get(2).getValue() + points.get(3).getValue()) / 4;
            z2 = "(" + points.get(0).getValue() + " + "
                + points.get(1).getValue() + " + " + points.get(2).getValue()
                + " + " + points.get(3).getValue() + ")/4 = " + erg;
            addieren.setText(z2, null, null);
            addieren.show();

            lang.nextStep("-> Calculate new value");
            InterpolationSource.unhighlight(5);
            InterpolationSource.highlight(6);
            addieren.hide();
            for (MatrixElement item : points) {
              matrix.unhighlightCell(item.getY(), item.getX(), null, null);
            }
            new_values.add(new MatrixElement(x, y, 0, 0, 1));
            matrix.put(y, x, erg, null, null);
            matrix.highlightCell(y, x, null, null);
          } else {
            z1 = "";
            z2 = "";
            z3 = "";
            // Falls es keine 4 Nachbarn gibt
            four_n.clear();
            ArrayList<MatrixElement> points2 = new ArrayList<MatrixElement>();
            points2 = n.getZNeighboursOf(x, y, points2, 2, points.size());
            for (MatrixElement item : points) {
              z1 += " " + m[item.getY()][item.getX()] + ",";
              four_n.add(item);
              matrix.highlightCell(item.getY(), item.getX(), null, null);
            }
            i = 0;
            for (MatrixElement item : points2) {
              z2 += " " + m[item.getY()][item.getX()] + ",";
              if ((4 - points.size()) > i) {
                matrix.highlightCell(item.getY(), item.getX(), null, null);
                z3 += " " + m[item.getY()][item.getX()] + ",";
                four_n.add(item);
              }
              i++;
            }

            if (z1.length() != 0) {
              note5.setText("The values " + z1.substring(0, z1.length() - 1)
                  + " are the nearest neighbors.", null, null);
              note5.show();
            }
            note8.setText("These are: " + z2.substring(0, z2.length() - 1)
                + ". We take the value " + z3.substring(0, z3.length() - 1)
                + ".", null, null);
            if (z1.length() != 0)
              note9.setText("The values " + z1.substring(0, z1.length() - 1)
                  + z3.substring(0, z3.length() - 1)
                  + " should be marked yellow.", null, null);
            else
              note9.setText("The values " + z3.substring(0, z3.length() - 1)
                  + " should be marked yellow.", null, null);

            note6.show();
            note7.show();
            note8.show();
            note9.show();

            lang.nextStep("-> Search Neighbours for (" + x + "," + y + ")");
            // Wie wir auf die Werte Pro Feld kommen
            for (MatrixElement item : four_n) {
              matrix.unhighlightCell(item.getY(), item.getX(), null, null);
            }

            note5.hide();
            note6.hide();
            note7.hide();
            note8.hide();
            note9.hide();

            int bigger;
            float bigger_f;
            if (zeile < spalte)
              bigger = spalte;
            else
              bigger = zeile;

            bigger_f = (float) 1 / bigger;

            if (!init_distance) {
              init_distance1.show();
              init_distance2.show();
              init_distance3.show();
              init_distance4.show();
              // We know the farthest range is 1.
              // Now the distance from one value to another can be determined.
              // As previously stated, we have "+Zeile+" (row) and "+Spalte+"
              // (column).
              // Which means that the farthest range of the higher number is 1.

              lang.nextStep("-> Initialize the base distance");
              init_distance1.hide();
              init_distance2.hide();
              init_distance3.hide();
              init_distance4.hide();
              init_distance5.show();
              init_distance6.show();
              init_distance7.show();
              init_distance8.show();
              init_distance9.show();
              if (zeile > spalte) {
                init_distance5.setText("We determined that the rows with "
                    + bigger + " are larger than the columns,", null, null);
                init_distance6
                    .setText(
                        "which indicates that the widest range 1 belongs to the rows, which proves that one ",
                        null, null);
                init_distance7.setText(
                    "field from value to value has the distance 1 divide through "
                        + bigger + ". ", null, null);
                init_distance8
                    .setText(
                        "This is how we receive the distance from one field to the next.",
                        null, null);
              } else {
                // spalte >= zeile
                init_distance5.setText("We determined that the columns with "
                    + bigger + " are larger or the same than the rows,", null,
                    null);
                init_distance6
                    .setText(
                        "which indicates that the widest range 1 belongs to the columns, which proves that one ",
                        null, null);
                init_distance7.setText(
                    "field from value to value has the distance 1 divide through "
                        + bigger + ". ", null, null);
                init_distance8
                    .setText(
                        "This is how we receive the distance from one field to the next.",
                        null, null);
              }
              init_distance9.setText("1 / " + bigger + " = " + bigger_f
                  + " for one field", null, null);

              // We determined that the columns/rows with "+Zeile/Spalte+" are
              // larger than the columns/rows,
              // which indicates that the widest range 1 belongs to the
              // columns/rows, which proves that one
              // field from value to value has the distance 1 divide through
              // (columns/rows).
              // This is how we receive the distance from one field to the next.
              // zeile/spalte : 1 = y

              for (int k = 0; k < bigger; k++) {
                if (zeile < spalte)
                  matrix.highlightCell(0, k, null, null);
                else
                  matrix.highlightCell(k, 0, null, null);
              }

              lang.nextStep();
              init_distance5.hide();
              init_distance6.hide();
              init_distance7.hide();
              init_distance8.hide();
              init_distance9.hide();
              init_distance = true;
              for (int k = 0; k < bigger; k++) {
                if (zeile < spalte)
                  matrix.unhighlightCell(0, k, null, null);
                else
                  matrix.unhighlightCell(k, 0, null, null);
              }
            }

            // Looking at the value X, the count of fields from X to the unknown
            // value is Y.
            // This indicates that the distance between these two fields are:
            // Count* (distance of one field). The count is marked yellow in the
            // matrix.
            hh = 0;
            for (MatrixElement item : four_n) {

              hh++;

              // Jede der 4 Elemente durchgehen für das Festlegen der Distance
              matrix.highlightCell(item.getY(), item.getX(), null, null);
              matrix.highlightCell(y, x, null, null);
              kennzeichnen(matrix, x, y, item.getX(), item.getY(), 0);

              init_distance5.setText("Looking at the value " + item.getValue()
                  + ", the count of fields from " + item.getValue()
                  + " to the unknown value is " + item.getCount() + ".", null,
                  null);
              init_distance6
                  .setText(
                      "This indicates that the distance between these two fields are:",
                      null, null);
              init_distance7.setText("Count*" + bigger_f
                  + ". The count is marked yellow in the matrix.", null, null);

              init_distance5.show();
              init_distance6.show();
              init_distance7.show();

              if (hh < 4)
                lang.nextStep();
              else
                lang.nextStep("-> Calculate distance");
              kennzeichnen(matrix, x, y, item.getX(), item.getY(), 1);
              matrix.unhighlightCell(item.getY(), item.getX(), null, null);
              matrix.unhighlightCell(y, x, null, null);

              init_distance5.hide();
              init_distance6.hide();
              init_distance7.hide();

            }

            for (MatrixElement item : four_n) {
              matrix.highlightCell(item.getY(), item.getX(), null, null);
            }

            z1 = "";
            z2 = "";
            InterpolationSource.unhighlight(2);
            InterpolationSource.highlight(3);

            i = 0;
            for (MatrixElement item : points2) {
              if ((4 - points.size()) > i) {
                matrix.highlightCell(item.getY(), item.getX(), null, null);
                z1 += "Unknown -> " + m[item.getY()][item.getX()] + ": "
                    + item.getDistance() + " ";
              }
              i++;
            }

            distance5.setText("Distance From: " + z1, null, null);
            if (points.size() > 0)
              distance6
                  .setText("and the distance to the other " + points.size()
                      + " values is " + points.get(0).getDistance(), null, null);
            else
              distance6.setText("", null, null);

            distance1.show();
            distance2.show();
            distance3.show();
            distance4.show();
            distance5.show();
            distance6.show();

            lang.nextStep();
            InterpolationSource.unhighlight(3);
            InterpolationSource.highlight(4);
            distance1.hide();
            distance2.hide();
            distance3.hide();
            distance4.hide();
            distance5.hide();
            distance6.hide();

            v1 = four_n.get(0).getValue() * (1 - four_n.get(0).getDistance());
            v2 = four_n.get(1).getValue() * (1 - four_n.get(1).getDistance());
            v3 = four_n.get(2).getValue() * (1 - four_n.get(2).getDistance());
            v4 = four_n.get(3).getValue() * (1 - four_n.get(3).getDistance());

            value1.setText(four_n.get(0).getValue() + "* (1 - "
                + four_n.get(0).getDistance() + ") = " + v1, null, null);
            value2.setText(four_n.get(1).getValue() + "* (1 - "
                + four_n.get(1).getDistance() + ") = " + v2, null, null);
            value3.setText(four_n.get(2).getValue() + "* (1 - "
                + four_n.get(2).getDistance() + ") = " + v3, null, null);
            value4.setText(four_n.get(3).getValue() + "* (1 - "
                + four_n.get(3).getDistance() + ") = " + v4, null, null);

            value1.show();
            value2.show();
            value3.show();
            value4.show();

            lang.nextStep();
            InterpolationSource.unhighlight(4);
            InterpolationSource.highlight(5);
            value1.hide();
            value2.hide();
            value3.hide();
            value4.hide();
            erg = Math.round((v1 + v2 + v3 + v4) / 4);
            new_value.setText("(" + v1 + " + " + v2 + " + " + v3 + " + " + v4
                + ")/4 = " + erg, null, null);
            new_value.show();

            lang.nextStep("-> Calculate new value");
            InterpolationSource.unhighlight(5);
            InterpolationSource.highlight(6);
            new_value.hide();
            for (MatrixElement item : four_n) {
              matrix.unhighlightCell(item.getY(), item.getX(), null, null);
            }
            matrix.put(y, x, erg, null, null);
            matrix.highlightCell(y, x, null, null);
            new_values.add(new MatrixElement(x, y, 0, 0, 1));
          }

          lang.nextStep("-> Set new value");
          InterpolationSource.unhighlight(6);
          InterpolationSource.highlight(1);
          matrix.unhighlightCell(y, x, null, null);

        }
      }
    }

    InterpolationSource.unhighlight(1);
    InterpolationSource.hide();
    for (MatrixElement item : new_values) {
      matrix.highlightCell(item.getY(), item.getX(), null, null);
    }

    lang.newText(new Offset(250, u_matrix + 20, header, "SE"),
        "The yellow marked values are the new values, which are acquired by ",
        "end1", null, normalTextProperties);
    lang.newText(new Offset(250, u_matrix + 40, header, "SE"),
        "bi-linear interpolation.", "end2", null, normalTextProperties);
    lang.newText(
        new Offset(250, u_matrix + 60, header, "SE"),
        "TBi-linear interpolation results in much smoother pictures in comparison",
        "end3", null, normalTextProperties);
    lang.newText(
        new Offset(250, u_matrix + 80, header, "SE"),
        "to simply using the nearest neighbor without considering the distance.",
        "end4", null, normalTextProperties);
    lang.newText(new Offset(250, u_matrix + 100, header, "SE"), "", "end5",
        null, normalTextProperties);

    lang.newText(new Offset(0, u_matrix + 20, header, "SW"),
        "The complexity of the algorithm is linear. Therefore, the whole ",
        "com1", null, normalTextProperties);
    lang.newText(new Offset(0, u_matrix + 40, header, "SW"),
        "matrix needs to be searched only once for unknown", "com2", null,
        normalTextProperties);
    lang.newText(new Offset(0, u_matrix + 60, header, "SW"),
        "values. The computings have all fixed values, consequently", "com3",
        null, normalTextProperties);
    lang.newText(new Offset(0, u_matrix + 80, header, "SW"),
        "the complexity has to be O(n), with n = number of", "com4", null,
        normalTextProperties);
    lang.newText(new Offset(0, u_matrix + 100, header, "SW"),
        "pixels in the picture.", "com5", null, normalTextProperties);
    lang.nextStep("Complexity");
  }

  // ALGO

  // Funktion für das Kennzeichnen der Wege
  private void kennzeichnen(IntMatrix m, int x_s, int y_s, int xZ, int yZ,
      int status) {
    int x_z = xZ, y_z = yZ;
    // status = 0 --> Higlight
    // status = 1 --> UnHiglight
    if (x_s == x_z && y_s == y_z)
      return;

    if (x_s != x_z) {
      if (x_s > x_z)
        x_z = x_z + 1;
      else
        x_z = x_z - 1;
    }

    if (y_s != y_z) {
      if (y_s > y_z)
        y_z = y_z + 1;
      else
        y_z = y_z - 1;
    }

    if (status == 0)
      m.highlightCell(y_z, x_z, null, null);
    else
      m.unhighlightCell(y_z, x_z, null, null);
    kennzeichnen(m, x_s, y_s, x_z, y_z, status);
  }

  public String getName() {
    return "Bilinear Interpolation in Image Processing";
  }

  public String getAlgorithmName() {
    return "Bilinear Interpolation in Image Processing";
  }

  public String getAnimationAuthor() {
    // "Natalie Faber <faber@d120.de>, Sascha Weiss <sascha@d120.de>";
    return "Natalie Faber>, Sascha Weiß";
  }

  public String getDescription() {
    return "Bilinear interpolation is used in image processing to reduce some of the visual distortion."
        + "\n"
        + "If we have a picture with unknown values, bilinear interpolation takes the nearest 2x2 known values in the picture"
        + "\n"
        + "and multiplies each of them with 1 - the distance to the unknown value."
        + "\n"
        + "The resulted values will be summarized, devided by 4 and this resulted value is the new value where before the unknown value was."
        + "\n"
        + "If all the nearest neighbors are exactly next to the unknown value, bilinear interpolation just summarize, devide them by 4"
        + "\n" + "and refresh the unknown value with this new computed value.";
  }

  public String getCodeExample() {
    return "1. Move throw the matrix until we find an unknown value (0)"
        + "\n"
        + "2. Find the four nearest known neighbors (!= 0)"
        + "\n"
        + "    a.1. Compute the distance of the nearest neighbors"
        + "\n"
        + "    a.2. Multiply the four values with (1 - their distance to the unknown point)"
        + "\n"
        + "3. Summarize the four values and devide them by 4"
        + "\n"
        + "4. Refresh the matrix and proceed with the second step until the whole matrix has been cycled";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  class Neighbours {
    int[][] matrix;
    int     zeile, spalte;
    float   d_zeile, d_spalte, d;

    public Neighbours(int[][] matrix) {
      this.matrix = matrix;
      this.zeile = matrix.length;
      this.spalte = matrix[0].length;
      this.d_zeile = (float) 1 / this.zeile;
      this.d_spalte = (float) 1 / this.spalte;
      if (this.d_zeile > this.d_spalte)
        this.d = this.d_zeile;
      else
        this.d = this.d_spalte;

    }

    public ArrayList<MatrixElement> getExactNeighboursOf(int x, int y) {
      ArrayList<MatrixElement> n = new ArrayList<MatrixElement>();
      if (zeile > y + 1 && spalte > x + 1)
        if (!is0(x + 1, y + 1))
          n.add(new MatrixElement(x + 1, y + 1, this.matrix[y + 1][x + 1], d, 1));
      if (zeile > y + 1 && -1 < x - 1)
        if (!is0(x - 1, y + 1))
          n.add(new MatrixElement(x - 1, y + 1, this.matrix[y + 1][x - 1], d, 1));
      if (zeile > y + 1)
        if (!is0(x, y + 1))
          n.add(new MatrixElement(x, y + 1, this.matrix[y + 1][x], d, 1));
      if (spalte > x + 1)
        if (!is0(x + 1, y))
          n.add(new MatrixElement(x + 1, y, this.matrix[y][x + 1], d, 1));
      if (n.size() == 4)
        return n;
      if (-1 < x - 1)
        if (!is0(x - 1, y))
          n.add(new MatrixElement(x - 1, y, this.matrix[y][x - 1], d, 1));
      if (n.size() == 4)
        return n;
      if (-1 < y - 1 && spalte > x + 1)
        if (!is0(x + 1, y - 1))
          n.add(new MatrixElement(x + 1, y - 1, this.matrix[y - 1][x + 1], d, 1));
      if (n.size() == 4)
        return n;
      if (-1 < y - 1 && -1 < x - 1)
        if (!is0(x - 1, y - 1))
          n.add(new MatrixElement(x - 1, y - 1, this.matrix[y - 1][x - 1], d, 1));
      if (n.size() == 4)
        return n;
      if (-1 < y - 1)
        if (!is0(x, y - 1))
          n.add(new MatrixElement(x, y - 1, this.matrix[y - 1][x], d, 1));
      if (n.size() == 4)
        return n;
      else
        return n;
    }

    public ArrayList<MatrixElement> getZNeighboursOf(int x, int y,
        ArrayList<MatrixElement> n, int z, int togo) {
      /*
       * Beispiel mit Z = 2
       * 
       * O O O O O O X X X O O X P X O O X X X O O O O O O
       * 
       * P = aktueller Punkt von dem aus gesucht wird X = Felder ignorieren O =
       * Ring den wir abgehen S = Aktuelle Suchposition der Suche F = Abgesuchte
       * Felder
       */

      // Ring der Z Felder entfernt ist berechnen und abgehen!
      int new_x = 0, new_y = 0;
      new_x = x - z;
      new_y = y - z;
      /*
       * O O O O O O X X X O O X P X O O X X X O S O O O O
       */
      int i;
      for (i = new_x; i < x + z; i++) {
        if (zeile > new_y && new_y > -1 && spalte > i && i > -1)
          if (!is0(i, new_y))
            n.add(new MatrixElement(i, new_y, this.matrix[new_y][i], d * z, z));

      }

      // new_x auf unser neues x setzen, was aktuell i ist
      new_x = i;
      /*
       * O O O O O O X X X O O X P X O O X X X O F F F F S
       */
      for (i = new_y; i < y + z; i++) {
        if (zeile > i && i > -1 && spalte > new_x && new_x > -1)
          if (!is0(new_x, i))
            n.add(new MatrixElement(new_x, i, this.matrix[i][new_x], d * z, z));
      }
      // new_y auf unser neues y setzen, was aktuell i ist
      new_y = i;
      /*
       * O O O O S O X X X F O X P X F O X X X F F F F F F
       */
      for (i = new_x; i > x - z; i--) {
        if (zeile > new_y && new_y > -1 && spalte > i && i > -1)
          if (!is0(i, new_y))
            n.add(new MatrixElement(i, new_y, this.matrix[new_y][i], d * z, z));
      }
      // new_x auf unser neues x setzen, was aktuell i ist
      new_x = i;
      /*
       * S F F F F O X X X F O X P X F O X X X F F F F F F
       */
      for (i = new_y; i > y - z; i--) {
        if (zeile > i && i > -1 && spalte > new_x && new_x > -1)
          if (!is0(new_x, i))
            n.add(new MatrixElement(new_x, i, this.matrix[i][new_x], d * z, z));
      }
      if (n.size() + togo >= 4)
        return n;
      else
        return getZNeighboursOf(x, y, n, z + 1, togo);
    }

    private boolean is0(int x, int y) {
      if (matrix[y][x] == 0)
        return true;
      else
        return false;
    }
  }

  class MatrixElement {
    int   x, y, Value = 0;
    float distance = 0;
    int   count    = 0;

    public MatrixElement(int x, int y, int Value, float distance, int count) {
      this.x = x;
      this.y = y;
      this.Value = Value;
      this.distance = distance;
      this.count = count;
    }

    public int getX() {
      return x;
    }

    public void setX(int x) {
      this.x = x;
    }

    public int getY() {
      return y;
    }

    public void setY(int y) {
      this.y = y;
    }

    public float getDistance() {
      return distance;
    }

    public int getCount() {
      return count;
    }

    public int getValue() {
      return Value;
    }
  }

}