package generators.helpers;

/**
 * 
 * @author Alexander Pass
 *
 */
import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class GanttDiagramm {
  /*
   * gedacht, um GanttDiagramm für verschiedene Job-Arten zu verwenden PF:
   * PermutationsFlowshop: Alle Aufträge werden in der selben Reihenfolge auf
   * den Maschinen (1,2...n) gefertigt.
   */
  private String          ShopIdentifier;
  // private Node NE;
  // private Node SW;
  // private int maxLB;
  private int             width;
  private int             hight;

  private int[][]         timematrix;                      // Bearbeitungszeiten
  private int[][]         FAmatrix;                         // Hilfsmatrix der
                                                            // frühesten
                                                            // Anfangszeitpunkte
  private int[][]         FEmatrix;                         // Hilfsmatrix der
                                                            // frühesten
                                                            // Endzeitpunkte
  private Vector<Integer> joblist;
  private Vector<Color>   colors    = new Vector<Color>();
  private Language        lang;
  // private int[] graph_yopt;
  // private int[] graph_xopt;
  private int             x_bereich;                       // pixel pro
                                                            // Zeiteinheit
  private int             y_bereich;                       // pixel pro
                                                            // Maschine
  private TextProperties  textprop  = new TextProperties();
  private TextProperties  textprop2 = new TextProperties();
  public Vector<String>   JobRect   = new Vector<String>(); // Namen der
                                                            // Job-Rechtecke
  private Vector<String>  JobText   = new Vector<String>(); // Namen der Jobs in
                                                            // einem
                                                            // Job-Rechteck

  public GanttDiagramm() {
  }

  public GanttDiagramm(Language lang) {

    Node[] vertices = new Node[2];
    vertices[0] = new Coordinates(100, 100);
    vertices[1] = new Coordinates(100, 300);
    PolylineProperties lineprop = new PolylineProperties();
    lineprop.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    lang.newPolyline(vertices, "kante", null, lineprop);

  }

  public GanttDiagramm(String ShopIdentifier, Node NE, int hight, int width,
      int[][] timematrix, Language lang) {
    // this.NE=(Coordinates)NE;//400x,400y
    // this.NE=NE;
    this.width = width;
    this.hight = hight;
    lang.newPoint(NE, "ne-point", null, new PointProperties());

    y_bereich = (hight - 10 - 20) / timematrix[1].length; // 20:oben+unten
                                                          // 10:pfeilabstand
    // this.SW=new Coordinates(800,50*timematrix.length+30);//später muss hier
    // korrigiert werden. Es wird einen maximalen
    // für Gantt reservierten y-Bereich geben. Falls 50*Jobanzahl+Bereich für
    // x-Pfeil+für Gantt-Titel > reservierter
    // Bereich-> dann eben maximaler Bereich
    this.timematrix = timematrix;
    // this.joblist=joblist; Vorerst null. Wird später aufgerufen
    this.lang = lang;
    this.ShopIdentifier = ShopIdentifier;
    // textprop.set(AnimationPropertiesKeys.FONT_PROPERTY, Font.SANSSERIF);
    // textprop2.set(AnimationPropertiesKeys.FONT_PROPERTY, Font.SANSSERIF);
    textprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("serif",
        Font.PLAIN, 10));
    textprop2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("serif",
        Font.PLAIN, 8));
    textprop2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);

    Random gen = new Random();
    for (int i = 0; i < timematrix.length; i++) {
      colors
          .add(new Color(gen.nextInt(255), gen.nextInt(255), gen.nextInt(255)));
    }

    showCoordinateSystem();
  }

  public void setMaxLB(int maxLB) {

    // this.maxLB=maxLB;
    x_bereich = (width - 10 - 20) / maxLB;

    Node[] x_help = new Node[2];
    for (int i = 1; i <= maxLB; i++) {
      x_help[0] = new Offset(i * x_bereich, 0, "nullpunkt",
          AnimalScript.DIRECTION_C);
      x_help[1] = new Offset(i * x_bereich, 3, "nullpunkt",
          AnimalScript.DIRECTION_C);
      lang.newPolyline(x_help, "x-mark_" + i, null, new PolylineProperties());
    }
    // Beschriftungsentscheidung falls zu viele Zeiteinheiten, dann eben jeden
    // 2, 5 oder 10. Strich beschriften
    if (x_bereich > 25) {
      // jeder Strich
      for (int i = 1; i <= maxLB; i++) {
        lang.newText(new Offset(i * x_bereich - 3, 3, "nullpunkt",
            AnimalScript.DIRECTION_C), "" + i, "x_" + i, null, textprop);
      }
    } else if (2 * x_bereich > 25) {
      // jeder 2.ter
      for (int i = 2; i <= maxLB; i = i + 2) {
        lang.newText(new Offset(i * x_bereich - 3, 3, "nullpunkt",
            AnimalScript.DIRECTION_C), "" + i, "x_" + i, null, textprop);
      }
    } else if (5 * x_bereich > 25) {
      // jeder 5
      for (int i = 5; i <= maxLB; i = i + 5) {
        lang.newText(new Offset(i * x_bereich - 3, 3, "nullpunkt",
            AnimalScript.DIRECTION_C), "" + i, "x_" + i, null, textprop);
      }
    } else {
      // jeder 10
      for (int i = 10; i <= maxLB; i = i + 10) {
        lang.newText(new Offset(i * x_bereich - 3, 3, "nullpunkt",
            AnimalScript.DIRECTION_C), "" + i, "x_" + i, null, textprop);
      }
    }
  }

  public void calculateJobs(Vector<Integer> joblistin) {
    if (joblistin.isEmpty())
      return;
    if (ShopIdentifier.equals("PF")) {
      /*
       * PermutationsFlowShop->alle Maschinen führen diesselbe jobfolge.
       * Annahme: Jobs müssen die Maschinen in der Reihenfolge(1,2,3...m)
       * durchlaufen
       */
      /*
       * Idee bei beiden Matrizen:
       * spaltennummer=Maschinennummer,zeilennummer=Jobnummer. Damit
       * indizes/job+maschnummern nicht durcheinanderkommen-> dimensionierung
       * mit +1. Achte: bearbeitungszeitmatrix mit normaler Dimensionierung->
       * jeweils -1
       */
      FAmatrix = new int[timematrix.length + 1][timematrix[1].length + 1];
      FEmatrix = new int[timematrix.length + 1][timematrix[1].length + 1];

      this.joblist = joblistin;
      int currentJob = joblist.firstElement();
      int lastJob = 0;

      for (int j = 1; j <= joblist.size(); j++) {
        for (int i = 1; i < FAmatrix[1].length; i++) {
          FAmatrix[currentJob][i] = Math.max(FEmatrix[currentJob][i - 1],
              FEmatrix[lastJob][i]);
          FEmatrix[currentJob][i] = FAmatrix[currentJob][i]
              + timematrix[currentJob - 1][i - 1];
        }
        lastJob = currentJob;
        if (j != joblist.size())
          currentJob = joblist.get(j);

      }

    }
  }

  public void showGantt() {
    if (joblist.isEmpty())
      return;
    emptyGraph();

    RectProperties rectprop = new RectProperties();
    rectprop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    for (int j = 0; j < joblist.size(); j++) {
      int job = joblist.get(j);
      rectprop.set(AnimationPropertiesKeys.FILL_PROPERTY, colors.get(job - 1));
      for (int i = 1; i < FAmatrix[1].length; i++) {
        JobRect.add(lang.newRect(
            new Offset(FAmatrix[job][i] * x_bereich, 0, "y_" + i,
                AnimalScript.DIRECTION_C),
            new Offset(FEmatrix[job][i] * x_bereich, 0, "y_" + (i - 1),
                AnimalScript.DIRECTION_C), "JobRect_", null, rectprop)
            .getName());
        JobText.add(lang.newText(
            new Offset(FAmatrix[job][i] * x_bereich + 3, 3, "y_" + i,
                AnimalScript.DIRECTION_C), "" + job, "Job_", null, textprop2)
            .getName());
      }

    }

  }

  private void emptyGraph() {

    lang.hideInThisStep.addAll(JobRect);
    lang.hideInThisStep.addAll(JobText);
    lang.nextStep();
    /*
     * //lösche das zuvor gezeichnete //Eleganterer Weg ist mit ausblenden !!!
     * Noch zu machen ! RectProperties killrectprop=new RectProperties();
     * killrectprop.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
     * killrectprop.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.WHITE);
     * killrectprop.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.WHITE);
     * 
     * lang.newRect(new
     * Offset(1,-1-3*y_bereich,"nullpunkt",AnimalScript.DIRECTION_C), new
     * Offset(maxLB*x_bereich+1,-1,"nullpunkt",AnimalScript.DIRECTION_C),
     * "killRect", null, killrectprop); lang.newRect(new
     * Offset(10,-10-3*y_bereich,"nullpunkt",AnimalScript.DIRECTION_C), new
     * Offset(width-10,-10,"nullpunkt",AnimalScript.DIRECTION_C), "killRect",
     * null, killrectprop);
     */
  }

  private void showCoordinateSystem() {
    PolylineProperties lineprop = new PolylineProperties();
    Node[] y_achse = new Node[2];
    Node[] x_achse = new Node[2];
    PolylineProperties achseprop = new PolylineProperties();
    achseprop.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

    lang.newPoint(new Offset(10, 100, "ne-point", AnimalScript.DIRECTION_C),
        "nullpunkt", null, new PointProperties());

    y_achse[0] = new Offset(0, 0, "nullpunkt", AnimalScript.DIRECTION_C);
    y_achse[1] = new Offset(0, -hight, "nullpunkt", AnimalScript.DIRECTION_C);
    lang.newPolyline(y_achse, "y-Achse", null, achseprop);

    x_achse[0] = new Offset(0, 0, "nullpunkt", AnimalScript.DIRECTION_C);
    x_achse[1] = new Offset(width - 10, 0, "nullpunkt",
        AnimalScript.DIRECTION_C);

    lang.newPolyline(x_achse, "x-Achse", null, achseprop);
    // Striche auf der y-Achse
    Node[] y_help = new Node[2];
    lang.newPoint(new Offset(0, 0, "nullpunkt", AnimalScript.DIRECTION_C),
        "y_0", null, new PointProperties());
    for (int i = 1; i <= timematrix[1].length; i++) {
      lang.newPoint(new Offset(0, -i * y_bereich, "nullpunkt",
          AnimalScript.DIRECTION_C), "y_" + i, null, new PointProperties());
      y_help[0] = new Offset(-3, -i * y_bereich, "nullpunkt",
          AnimalScript.DIRECTION_C);
      y_help[1] = new Offset(0, -i * y_bereich, "nullpunkt",
          AnimalScript.DIRECTION_C);
      lang.newPolyline(y_help, "y-mark_" + i, null, lineprop);
      lang.newText(new Offset(-5, -i * y_bereich, "nullpunkt",
          AnimalScript.DIRECTION_C), "" + i, "Masch" + i, null, textprop);
    }
    // Stricher auf der x-Achse werden in der Methode setMaxLB erledigt
  }
}
