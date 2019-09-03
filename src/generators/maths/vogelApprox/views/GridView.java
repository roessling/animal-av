package generators.maths.vogelApprox.views;

import generators.maths.grid.Grid;
import generators.maths.grid.GridProperty;
import generators.maths.vogelApprox.DrawingUtils;
import generators.maths.vogelApprox.MatrixElement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class GridView {

  private Language           animationScript;
  private Grid               costGrid;
  private Grid               costDifRowGrid;
  private Grid               costDifColumnGrid;

  private Grid               tableauGrid;

  private Grid               demandGrid;
  private Grid               supplyGrid;

  private MatrixElement[]    supply;                                          // Angebot
  private MatrixElement[]    demand;                                          // Nachfrage

  private MatrixElement[][]  tableau;                                         // Transporttableau
  private MatrixElement[][]  cost;                                            // Kostenmatrix

  private DrawingUtils       myDrawingUtils;

  private static Coordinates CO_HEADER           = new Coordinates(520, 20);
  private static int         CO_GRID_LEFT        = 600;

  private static String      HEADER              = "Kostenmatrix und Tableau";
  private static String      HEADER1             = "Kostenmatrix:";
  private static String      HEADER2             = "Tableau:";
  private static String      ROW_CAPTION_OVERALL = "Stadt ";
  private static String      COL_CAPTION_OVERALL = "Werk ";
  private static String      ROW_CAPTION_BOTTOM  = "Nachfrage";
  private static String      COL_CAPTION_BOTTOM  = "Angebot";
  private static String      COL_CAPTION_DIF     = "Differenz";
  private static int         CELL_SIZE           = 40;

  public GridView(Language animationScript, DrawingUtils myDrawingUtils2) {
    this.animationScript = animationScript;
    this.myDrawingUtils = myDrawingUtils2;
  }

  public void setupView(MatrixElement[] supply, MatrixElement[] demand,
      MatrixElement[][] tableau, MatrixElement[][] cost) {

    this.supply = supply;
    this.demand = demand;
    this.tableau = tableau;
    this.cost = cost;

    myDrawingUtils.drawHeader(CO_HEADER, HEADER);

    costDifColumnGrid = createCostDifColumnGrid(demand.length, 1);
    costDifRowGrid = createCostDifRowGrid(1, supply.length);
    costGrid = createCostGrid(demand.length, supply.length);

    supplyGrid = createSupplyGrid(1, supply.length);
    demandGrid = createDemandGrid(demand.length, 1);
    tableauGrid = createTableauGrid(demand.length, supply.length);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    animationScript.newText(new Coordinates(CO_GRID_LEFT - 50, 70), HEADER1,
        "description1", null, textProps);
    animationScript.newText(new Coordinates(CO_GRID_LEFT - 50,
        calcTableauTop() - 70), HEADER2, "description1", null, textProps);

    refreshValues();
  }

  private Grid createCostGrid(int width, int height) {

    // Design of GRID
    GridProperty gp = new GridProperty();
    gp.set(GridProperty.GRID_COLOR, Color.gray);
    gp.set(GridProperty.BORDER, true);
    gp.set(GridProperty.BORDER_COLOR, Color.blue);
    gp.set(GridProperty.CAPTION_OFFSET_TOP, new Point(0, 5));
    gp.set(GridProperty.CAPTION_OFFSET_LEFT, new Point(-10, 15));
    gp.set(GridProperty.CAPTION_COLOR, Color.gray);

    // Create Grid
    Grid x = new Grid(new Coordinates(CO_GRID_LEFT, 140), width, height,
        CELL_SIZE, animationScript, gp);

    // Create Caption
    String captionTop[] = new String[width];
    for (int i = 1; i <= width; i++) {
      captionTop[i - 1] = ROW_CAPTION_OVERALL + i;
    }
    x.setCaptionTop(captionTop);

    String captionLeft[] = new String[height];
    for (int i = 1; i <= height; i++) {
      captionLeft[i - 1] = COL_CAPTION_OVERALL + i;
    }
    x.setCaptionLeft(captionLeft);

    return x;

  }

  private Grid createCostDifColumnGrid(int width, int height) {

    // Design of GRID
    GridProperty gp = new GridProperty();
    gp.set(GridProperty.GRID_COLOR, Color.gray);
    gp.set(GridProperty.BORDER, true);
    gp.set(GridProperty.TEXT_COLOR, Color.red);
    gp.set(GridProperty.CAPTION_OFFSET_LEFT, new Point(-10, 0));
    gp.set(GridProperty.CAPTION_COLOR, Color.gray);

    // Create Grid
    Grid x = new Grid(new Coordinates(CO_GRID_LEFT, 140 + CELL_SIZE
        * supply.length), width, height, CELL_SIZE, animationScript, gp);

    // Create Caption
    String captionLeft[] = new String[height];
    for (int i = 1; i <= height; i++) {
      captionLeft[i - 1] = COL_CAPTION_DIF;
    }
    x.setCaptionLeft(captionLeft);

    return x;

  }

  private Grid createCostDifRowGrid(int width, int height) {

    // Design of GRID
    GridProperty gp = new GridProperty();
    gp.set(GridProperty.GRID_COLOR, Color.gray);
    gp.set(GridProperty.BORDER, true);
    gp.set(GridProperty.TEXT_COLOR, Color.red);
    // gp.set(gp.BORDER_COLOR, Color.blue);
    gp.set(GridProperty.CAPTION_OFFSET_LEFT, new Point(-10, 15));
    // gp.set(gp.CAPTION_TEXT_SIZE, 26);
    gp.set(GridProperty.CAPTION_COLOR, Color.gray);

    // Create Grid
    Grid x = new Grid(new Coordinates(CO_GRID_LEFT + CELL_SIZE * demand.length,
        140), width, height, CELL_SIZE, animationScript, gp);

    // Create Caption
    String captionTop[] = new String[width];
    for (int i = 1; i <= width; i++) {
      captionTop[i - 1] = COL_CAPTION_DIF;
    }
    x.setCaptionTop(captionTop);

    return x;

  }

  private Grid createTableauGrid(int width, int height) {

    // Design of GRID
    GridProperty gp = new GridProperty();
    gp.set(GridProperty.GRID_COLOR, Color.gray);
    gp.set(GridProperty.BORDER, true);
    gp.set(GridProperty.BORDER_COLOR, Color.blue);
    gp.set(GridProperty.CAPTION_OFFSET_TOP, new Point(0, 5));
    gp.set(GridProperty.CAPTION_OFFSET_LEFT, new Point(-10, 15));
    // gp.set(gp.CAPTION_TEXT_SIZE, 26);
    gp.set(GridProperty.CAPTION_COLOR, Color.gray);

    // Create Grid
    Grid x = new Grid(new Coordinates(CO_GRID_LEFT, calcTableauTop()), width,
        height, CELL_SIZE, animationScript, gp);

    // Create Caption
    String captionTop[] = new String[width];
    for (int i = 1; i <= width; i++) {
      captionTop[i - 1] = ROW_CAPTION_OVERALL + i;
    }
    x.setCaptionTop(captionTop);

    String captionLeft[] = new String[height];
    for (int i = 1; i <= height; i++) {
      captionLeft[i - 1] = COL_CAPTION_OVERALL + i;
    }
    x.setCaptionLeft(captionLeft);

    return x;

  }

  private Grid createSupplyGrid(int width, int height) {

    // Design of GRID
    GridProperty gp = new GridProperty();
    gp.set(GridProperty.GRID_COLOR, Color.gray);
    gp.set(GridProperty.BORDER, true);
    // gp.set(gp.BORDER_COLOR, Color.blue);
    gp.set(GridProperty.CAPTION_OFFSET_LEFT, new Point(-10, 15));
    // gp.set(gp.CAPTION_TEXT_SIZE, 26);
    gp.set(GridProperty.CAPTION_COLOR, Color.gray);

    // Create Grid
    Grid x = new Grid(new Coordinates(CO_GRID_LEFT + CELL_SIZE * demand.length,
        calcTableauTop()), width, height, CELL_SIZE, animationScript, gp);

    // Create Caption
    String captionTop[] = new String[width];
    for (int i = 1; i <= width; i++) {
      captionTop[i - 1] = COL_CAPTION_BOTTOM;
    }
    x.setCaptionTop(captionTop);

    return x;

  }

  private Grid createDemandGrid(int width, int height) {

    // Design of GRID
    GridProperty gp = new GridProperty();
    gp.set(GridProperty.GRID_COLOR, Color.gray);
    gp.set(GridProperty.BORDER, true);
    // gp.set(gp.BORDER_COLOR, Color.blue);
    gp.set(GridProperty.CAPTION_OFFSET_LEFT, new Point(-10, 15));
    // gp.set(gp.CAPTION_TEXT_SIZE, 26);
    gp.set(GridProperty.CAPTION_COLOR, Color.gray);

    // Create Grid
    Grid x = new Grid(new Coordinates(CO_GRID_LEFT, calcTableauTop()
        + CELL_SIZE * supply.length), width, height, CELL_SIZE,
        animationScript, gp);

    // Create Caption
    String captionLeft[] = new String[height];
    for (int i = 1; i <= height; i++) {
      captionLeft[i - 1] = ROW_CAPTION_BOTTOM;
    }
    x.setCaptionLeft(captionLeft);

    return x;

  }

  private int calcTableauTop() {
    return 140 + CELL_SIZE * (supply.length + 1) + 100;
  }

  public void refreshSupply() {
    for (int i = 0; i < supply.length; i++) {
      if (supply[i].isActive() && supply[i].getValue() != null) {
        supplyGrid.setLabel(0, i, Integer.toString(supply[i].getValue()));
      } else {
        supplyGrid.setLabel(0, i, "");
      }
    }
  }

  public void refreshDemand() {
    for (int i = 0; i < demand.length; i++) {
      if (demand[i].isActive() && demand[i].getValue() != null) {
        demandGrid.setLabel(i, 0, Integer.toString(demand[i].getValue()));
      } else {
        demandGrid.setLabel(i, 0, "");
      }
    }
  }

  public void refreshValues() {
    for (int i = 0; i < cost.length; i++) {
      for (int j = 0; j < cost[i].length; j++) {
        if (cost[i][j].isActive()) {
          costGrid.setLabel(j, i, Integer.toString(cost[i][j].getValue()));
        } else {
          costGrid.setLabel(j, i, Integer.toString(cost[i][j].getValue()));
        }
      }
    }

    for (int i = 0; i < tableau.length; i++) {
      for (int j = 0; j < tableau[i].length; j++) {
        if (tableau[i][j].isActive()) {
          tableauGrid
              .setLabel(j, i, Integer.toString(tableau[i][j].getValue()));
        } else {
          tableauGrid.setLabel(j, i, "");
        }
      }
    }

    for (int i = 0; i < supply.length; i++) {
      if (supply[i].isActive() && supply[i].getValue() != null) {
        supplyGrid.setLabel(0, i, Integer.toString(supply[i].getValue()));
      } else {
        supplyGrid.setLabel(0, i, "");
      }
    }

    for (int i = 0; i < demand.length; i++) {
      if (demand[i].isActive() && demand[i].getValue() != null) {
        demandGrid.setLabel(i, 0, Integer.toString(demand[i].getValue()));
      } else {
        demandGrid.setLabel(i, 0, "");
      }
    }

  }

  public Grid getCostGrid() {
    return costGrid;
  }

  public Grid getCostDifRowGrid() {
    return costDifRowGrid;
  }

  public Grid getCostDifColumnGrid() {
    return costDifColumnGrid;
  }

  public Grid getTableauGrid() {
    return tableauGrid;
  }

  public Grid getDemandGrid() {
    return demandGrid;
  }

  public Grid getSupplyGrid() {
    return supplyGrid;
  }

}
