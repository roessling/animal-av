package generators.misc.helpers;


import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Square;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;

public class CustomerWarehouseDiagram {

  private Language           lang;

  private int                r, l;
  private TextProperties     text;
  private CircleProperties   circle;
  // private SquareProperties square;
  private RectProperties     rectClose;
  private PolylineProperties polylineProp;

  // private Color WarenhausGeschlossenDiagram;
  private Color              warenhausOffenDiagram;
  private Color              pfeilFarbeZugeordnet;
  // private Color PfeilFarbeVerbunden;

  private Circle[]           warehouseCircle;
  private connectionLine[][] connectionLines;

  public CustomerWarehouseDiagram(int x, int y, Language lang, int r, int l,
      Color warenhausOffenDiagram, Color warenhausGeschlossenDiagram,
      Color pfeilFarbeZugeordnet, Color pfeilFarbeVerbunden,
      TextProperties infoLineProp) {
    this.init(lang, r, l, warenhausOffenDiagram, warenhausGeschlossenDiagram,
        pfeilFarbeZugeordnet, pfeilFarbeVerbunden);
    int i, j;
    Node[] linePoints = new Node[2];
    Node offset;
    Polyline line;

    warehouseCircle = new Circle[l];

    Square[] customerSquare = new Square[r];

    // Ref zu "dropCost_Header" machen
    lang.newText(
        new Offset(x, y, "dropCost_Header", AnimalScript.DIRECTION_NW), "",
        "refPointCWD", null, infoLineProp);

    // Warehouses
    for (j = 0; j < l; j++) {

      offset = new Offset(0, 10 + j * 25, "refPointCWD",
          AnimalScript.DIRECTION_SW);
      lang.newText(offset, "" + (j + 1), "CWD_Warehouse_Text_" + j, null, text);

      offset = new Offset(0, (j + 1) * 25, "refPointCWD",
          AnimalScript.DIRECTION_C);
      warehouseCircle[j] = lang.newCircle(offset, 10, "CWD_Warehouse_Circle_"
          + j, null, circle);

    }

    // Customer
    for (i = 0; i < r; i++) {

      offset = new Offset(40 + i * 25, (l + 1) * 20 + 15, "refPointCWD",
          AnimalScript.DIRECTION_S);
      lang.newText(offset, "" + (i + 1), "CWD_Customer_Text_" + i, null, text);

      offset = new Offset(33 + i * 25, (l + 1) * 20 + 13, "refPointCWD",
          AnimalScript.DIRECTION_S);
      customerSquare[i] = lang.newSquare(offset, 20,
          "CWD_Customer_Square_" + i, null);

    }

    // CreateAndHide Connection Line
    for (j = 0; j < l; j++) {
      for (i = 0; i < r; i++) {
        linePoints[0] = new Offset(0, 0, "CWD_Warehouse_Circle_" + j,
            AnimalScript.DIRECTION_E);
        linePoints[1] = new Offset(0, 0, "CWD_Customer_Square_" + i,
            AnimalScript.DIRECTION_N);
        line = lang.newPolyline(linePoints, "CWD_Connection_Line_" + i + ":"
            + j, null, polylineProp);

        connectionLines[i][j] = new connectionLine(line, true);

      }
    }

  }

  private void init(Language lang, int r, int l, Color warenhausOffenDiagram,
      Color warenhausGeschlossenDiagram, Color pfeilFarbeZugeordnet,
      Color pfeilFarbeVerbunden) {
    this.lang = lang;
    this.r = r;
    this.l = l;
    // this.WarenhausGeschlossenDiagram = warenhausGeschlossenDiagram;
    this.warenhausOffenDiagram = warenhausOffenDiagram;
    this.pfeilFarbeZugeordnet = pfeilFarbeZugeordnet;
    // this.PfeilFarbeVerbunden = pfeilFarbeVerbunden;

    // Text Properties Table
    text = new TextProperties();
    text.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    text.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
        Font.PLAIN, 12));

    circle = new CircleProperties();

    circle.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    circle.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    circle.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circle.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 7);

    // circle.s

    rectClose = new RectProperties();
    rectClose.set(AnimationPropertiesKeys.FILL_PROPERTY,
        warenhausGeschlossenDiagram);
    rectClose.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        warenhausGeschlossenDiagram);
    rectClose.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectClose.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 7);

    polylineProp = new PolylineProperties();
    polylineProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        pfeilFarbeVerbunden);
    polylineProp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    // square = new SquareProperties();

    connectionLines = new connectionLine[r][l];

  }

  public void openWarehouse(int j) {
    warehouseCircle[j].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
        warenhausOffenDiagram, null, null);
  }

  public void closeWarehouse(int j) {
    int i;
    Node leftTopOffset, rightBottomOffset;
    leftTopOffset = new Offset(-13, -2, "CWD_Warehouse_Circle_" + j,
        AnimalScript.DIRECTION_C);
    rightBottomOffset = new Offset(13, 2, "CWD_Warehouse_Circle_" + j,
        AnimalScript.DIRECTION_C);

    // lang.newRect(new Coordinates(350, 110), new Coordinates(300, 100),
    // "NNNN", null);
    lang.newRect(leftTopOffset, rightBottomOffset, "CWD_Warehouse_Circle_Del2_"
        + j, null, rectClose);

    for (i = 0; i < r; i++) {
      hideConnection(j, i);
    }

  }

  public void hideConnection(int j, int i) {
    if (!connectionLines[i][j].isHidden()) {
      connectionLines[i][j].setHidden(true);
      connectionLines[i][j].getLine().hide();

    }
    ;

  }

  public void hideAllConnection() {
    int i, j;

    for (j = 0; j < l; j++) {
      for (i = 0; i < r; i++) {
        hideConnection(j, i);
      }
    }

  }

  public void showConnection(int j, int i) {
    if (connectionLines[i][j].isHidden()) {
      connectionLines[i][j].setHidden(false);
      connectionLines[i][j].getLine().show();

    }
    ;

  }

  public void markConnection(int j, int i) {
    showConnection(j, i);
    connectionLines[i][j].getLine().changeColor(null, pfeilFarbeZugeordnet,
        null, null);
  }

}
