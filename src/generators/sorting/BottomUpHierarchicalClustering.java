package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Point;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class BottomUpHierarchicalClustering implements Generator {
  private Language lang;
  private int      anzahlVonVektoren;
  private boolean  zeigeIntro;

  /*
   * Umgebauter K-Meanspunkt aus 5.1 da sich die Strukturen aehneln.
   */
  private class BottomUpCluster {
    // Umgebaut vom K-Meanspunkt aus Aufgabe 5.1
    private int                             coord_x;
    private int                             coord_y;
    private int                             knotennummer;
//    int                                     birth;
    public int                              retired           = 9999;

    private String                          punktName;
    private Circle                          animalCircle      = null;
    // private Triangle animalTriangle = null;
    Text                                    animalText        = null;
    Color                                   animalActualColor = null;
    // private Color animalLastColor = null;
    // private BottomUpCluster actualCentroid = null;
    boolean                         isSubCluster      = false;
    public boolean                          isStartCluster    = false;
//    BottomUpCluster                         parentCluster     = null;
    public HashMap<String, BottomUpCluster> childClusters;
//    BottomUpCluster                         subClusterA       = null;
//    BottomUpCluster                         subClusterB       = null;
    ArrayList<Polyline>                     listClusterPfeile = new ArrayList<Polyline>();

    public BottomUpCluster(int x, int y, String knotenname, int knotennummer) {
      this.punktName = knotenname;
      this.knotennummer = knotennummer;
      this.coord_x = x;
      this.coord_y = y;
      this.setSubCluster(false);

      this.childClusters = new HashMap<String, BottomUpCluster>();
      // System.out.println("Punkt erzeugt: " + this.coord_x + "," +
      // this.coord_y);
    }

    public void setAnimalText(Text derText) {
      this.animalText = derText;
    }

    public Circle getAnimalCircle() {
      return this.animalCircle;
    }

    public void setAnimalCircle(Circle derCircle) {
      this.animalCircle = derCircle;
    }

    /*
     * public void initAnimalScript(Point nullpunkt) { // Properties fuer die
     * unkategorisierten Vektoren setzen //
     * unkategorisierterCircle.set(AnimationPropertiesKeys.COLOR_PROPERTY,
     * Color.darkGray); //
     * unkategorisierterCircle.set(AnimationPropertiesKeys.FILLED_PROPERTY,
     * true); //
     * unkategorisierterCircle.set(AnimationPropertiesKeys.FILL_PROPERTY,
     * Color.gray);
     * 
     * /* this.animalCircle = lang.newCircle((new
     * Offset(this.coord_x,this.coord_y,new Coordinates(0, 0)
     * ,AnimalScript.DIRECTION_NW)), radius, this.punktName, null,
     * unkategorisierterCircle); // this.animalCircle = lang.newCircle((new
     * Offset(0, 0, nullpunkt, AnimalScript.DIRECTION_NW)), radius, "links1",
     * null, unkategorisierterCircle); }
     */

    // Generate some coordinates to be used to display representations
    // private int generateCoords(int min, int max)
    // {
    // Random rnd = new Random();
    // // int difference = max - min;
    // return (rnd.nextInt(max)); //liefert zahlen zwischen 0 (inklusive) und 4
    // (exklusive)
    // }

    public int getCoordX() {
      return this.coord_x;
    }

    public String getNodename() {
      return this.punktName;
    }

    public int getCoordY() {
      return this.coord_y;
    }

    public int getKnotennummer() {
      return this.knotennummer;
    }

    // public void setX(int X)
    // {
    // this.coord_x = X;
    // }
    //
    // public void setY(int Y)
    // {
    // this.coord_y = Y;
    // }

    // public void setXY(int X, int Y) {
    // this.coord_x = X;
    // this.coord_y = Y;
    // }

    /**
     * Hilfsmethode um Koordinaten zu erhalten.
     */
    public Coordinates getCoordinates() {
      return (new Coordinates(this.coord_x, this.coord_y));
    }

    // /**
    // * Hilfsmethode die euklidische Distanz auf der 2 Dimens. Karte erhalten.
    // */
    // public double getEuklideanDistanceTo(BottomUpCluster centroidToCompare) {
    // int centroid_x = centroidToCompare.getCoordX();
    // int centroid_y = centroidToCompare.getCoordY();
    //
    // int distance_x = (this.coord_x - centroid_x);
    // int distance_y = (this.coord_y - centroid_y);
    //
    // double dist = Math.sqrt((distance_x * distance_x)
    // + (distance_y * distance_y));
    //
    // // System.out.println("Abstandende von '" + this.punktName + "' zu '" +
    // // centroidToCompare.getNodename() + "|" +
    // // centroidToCompare.getAnimalColor() + "' : (" + distance_x + "," +
    // // distance_y + ") -> " + dist);
    // return dist;
    // }
    //
    // /**
    // * Hilfsmethode um einen KMeanspunkt graphisch als Centroiden zu markieren
    // */
    // public void setAnimalTriangle(Triangle animalTriangle) {
    // this.animalTriangle = animalTriangle;
    // }
    //
    // /**
    // * Hilfsmethode
    // */
    // public Triangle getAnimalTriangle() {
    // return animalTriangle;
    // }
    //
    // /**
    // * Hilfsmethode
    // */
    // public void setAnimalColor(Color animalColor) {
    // this.animalActualColor = animalColor;
    // }

    // /**
    // * Hilfsmethode
    // */
    // public Color getAnimalColor() {
    // return animalActualColor;
    // }
    //
    // /**
    // * Hilfsmethode
    // */
    // public void setActualCentroid(BottomUpCluster actualCentroid) {
    // this.actualCentroid = actualCentroid;
    // }
    //
    // /**
    // * Hilfsmethode
    // */
    // public BottomUpCluster getActualCentroid() {
    // return actualCentroid;
    // }
    //
    // /**
    // * Hilfsmethode
    // */
    // public void setAnimalLastColor(Color animalLastColor) {
    // this.animalLastColor = animalLastColor;
    // }
    //
    // /**
    // * Hilfsmethode
    // */
    // public Color getAnimalLastColor() {
    // return animalLastColor;
    // }

    public boolean isSubCluster() {
      return isSubCluster;
    }

    public void setSubCluster(boolean isSubCluster) {
      this.isSubCluster = isSubCluster;
    }

  }

  private HashMap<String, BottomUpCluster> mapOfClusters;

  /**
   * Ein paar Variablen
   */
  // private Language lang;

  // Angabe fuer das Textoffset neben den Punkten
  private int                              textoffsetX             = 10;
  private int                              textoffsetY             = -9;

  // Iterationenzaehler, Iterationslimit usw...
  private int                              overallRadius           = 7;
  private int                              overallIterations       = 0;
  private int                              overallDistanceChecks   = 0;
  // private int overallCenterCalculations = 0;

  // Variablenfenster

  // private Variables vars;

  // Properties innerhalb KMeans zugaenglich machen
  private CircleProperties                 unkategorisierterCircle = new CircleProperties();
  private CircleProperties                 roterCircle             = new CircleProperties();
  private PolylineProperties               pfeilProps              = new PolylineProperties();
  private PolylineProperties               pfeilTempProps          = new PolylineProperties();
  // private EllipseProperties ellipseProps = new EllipseProperties();

  // Farben definieren, damit der Generator etwas aussuchen kann (sofern wir das
  // brauchen, wenn wir bei Aufgabe 4 sind)
  // private ArrayList<Color> listOfColors = new ArrayList<Color>();

  // Liste der Pfeile, die sich mit jeder Iteration aendern.
  private ArrayList<Polyline>              listOfPfeile            = new ArrayList<Polyline>();
  private ArrayList<Polyline>              listOfTempPfeile        = new ArrayList<Polyline>();

  // Hieran wird die Animation mit den Kreisen und Triangeln ausgerichtet.
  private Point                            offsetPunkt;
  private Point                            introPoint;

  // Soll aenderbar sein
  private Text                             titelText, descriptionText,
      baumTextText, statusText;

  // Soll auch von allen Methoden nutzbar sein.
  private SourceCode                       einleitungsText, pseudocodeText,
      abschlussText, baumText;

  private Polyline                         baumTextTextUnderscore;
  private Text                             pseudoTextText;
  private Polyline                         pseudoTextTextUnderline;
  private Polyline                         statusbalken;
  private Text                             statusTextCap;

  /**
   * Initialisiere Animalanbindung.
   */
  // public KMeans(Language l) {
  // Store the language object
  // lang = l;
  // This initializes the step mode. Each pair of subsequent steps has to
  // be divdided by a call of lang.nextStep();
  // lang.setStepMode(true);
  // }

  /*
   * Initialisiert ein paar Werte.
   */
  public void initializeStuff() {

    /* Beschreibungstext zum Beispiel generieren */
    TextProperties desc1Props = new TextProperties();
    desc1Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 18));
    descriptionText = lang.newText(new Coordinates(22, 80),
        "Beschreibung des Algorithmus", "descriptionText", null, desc1Props);
    descriptionText.hide();

    mapOfClusters = new HashMap<String, BottomUpCluster>();

    // Properties fuer die unkategorisierten Vektoren setzen
    unkategorisierterCircle.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.darkGray);
    unkategorisierterCircle.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    unkategorisierterCircle.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.gray);

    // Properties fuer die roten Vektoren setzen
    roterCircle.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.darkGray);
    roterCircle.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    roterCircle.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.red);

    // Properties fuer Pfeil setzen
    pfeilProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
    pfeilProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
    pfeilTempProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    pfeilTempProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);

    // Den Nullpunkt initialisieren, an dem spaeter die zufaelligen Punkte
    // ausgerichtet sind
    PointProperties nullPointProps = new PointProperties();
    offsetPunkt = lang.newPoint(new Coordinates(50, 100), "offsetPunkt", null,
        nullPointProps);
    offsetPunkt.changeColor("Color", Color.white, null, null); // weiss auf
                                                               // weiss =
                                                               // unsichtbar :)

    /*
     * Den Punkt initialisieren, an dem die Punkte NUR IM INTRO ausgerichtet
     * sind
     */
    PointProperties introPointProps = new PointProperties();
    introPoint = lang.newPoint(new Coordinates(200, 500), "introPoint", null,
        introPointProps);

  }

  /*
   * Erzeugt ein zufaelliges Feld ohne sich ueberschneidende Knoten per
   * Mindestabstand
   */
  public void generateRandomField(int areaMaxX, int areaMaxY, int mindestabstand) {
    // Ersten Randomknoten setzen, daran spaeter die anderen mit mindestabstand
    // generieren
    Random rnd = new Random();
    boolean redo = false;

    mapOfClusters.put("cluster1", new BottomUpCluster(rnd.nextInt(areaMaxX),
        rnd.nextInt(areaMaxY), "cluster1", 1));

    for (int i = 1; i <= anzahlVonVektoren; i++) {

      // System.out.println("I="+i);

      while (redo == true) {
        redo = false;
        int neuesX = rnd.nextInt(areaMaxX);
        int neuesY = rnd.nextInt(areaMaxY);

        Set<String> alleCluster = mapOfClusters.keySet();
        for (String clusterInDerMap : alleCluster) {
          double distanz = getEuklideanDistanceBetweenCoords(
              mapOfClusters.get(clusterInDerMap).getCoordinates(),
              new Coordinates(neuesX, neuesY));
          if (distanz < mindestabstand) {
            redo = true;
          }

          // System.out.println("Distanz: " + distanz + " | redo -> " + redo);
        }

        mapOfClusters.put("cluster" + i, new BottomUpCluster(neuesX, neuesY,
            "cluster" + i, i));
      }

      redo = true;

    }

    Text erzeugeBeispieltext = lang.newText(new Coordinates(150, 250),
        "Es folgt ein Beispiel mit " + anzahlVonVektoren
            + " zufaelligen Vektoren.", "generateExample", null);

    /* Zeige Legende */
    Text legendeText = lang.newText(new Coordinates(143, 300), "Legende:",
        "legende", null);
    Circle anonymKreis = lang.newCircle(new Coordinates(150, 330),
        overallRadius, "anonymKreis", null, unkategorisierterCircle);
    Text anonymText = lang.newText(new Offset(10, -7, anonymKreis,
        AnimalScript.DIRECTION_E), "Cluster aus nur einem Knoten bestehend",
        "anonymText", null);

    Circle gruenKreis = lang.newCircle(new Coordinates(150, 350),
        overallRadius, "rotKreis", null, unkategorisierterCircle);
    gruenKreis.changeColor("fillColor", Color.green, null, null);
    Text gruenText = lang.newText(new Offset(10, -7, gruenKreis,
        AnimalScript.DIRECTION_E), "Cluster innerhalb eines Parentclusters",
        "rotText", null);

    Circle rotKreis = lang.newCircle(new Coordinates(150, 380), overallRadius,
        "rotKreis", null, unkategorisierterCircle);
    rotKreis.changeColor("fillColor", Color.red, null, null);
    Text rotText = lang
        .newText(new Offset(10, -7, rotKreis, AnimalScript.DIRECTION_E),
            "Cluster in Teil A bei der AverageDistance-Berechnung", "rotText",
            null);

    Circle blauKreis = lang.newCircle(new Coordinates(150, 400), overallRadius,
        "blauKreis", null, unkategorisierterCircle);
    blauKreis.changeColor("fillColor", Color.blue, null, null);
    Text blauText = lang.newText(new Offset(10, -7, blauKreis,
        AnimalScript.DIRECTION_E),
        "Cluster in Teil B bei der AverageDistance-Berechnung", "blauText",
        null);

    lang.nextStep("Algorithmus START");

    erzeugeBeispieltext.hide();
    legendeText.hide();
    anonymKreis.hide();
    anonymText.hide();
    gruenKreis.hide();
    gruenText.hide();
    blauKreis.hide();
    blauText.hide();
    rotKreis.hide();
    rotText.hide();

  }

  /* Malt alle Punkte ein erstes mal */
  public void malePunkte() {
    overallIterations = 0;
    Set<String> alleCluster = mapOfClusters.keySet();
    for (String clusterInDerMap : alleCluster) {
      BottomUpCluster aktuellerCluster = mapOfClusters.get(clusterInDerMap);
      // aktuellerCluster.birth = 0;
      aktuellerCluster.retired = 9999;
      aktuellerCluster.isStartCluster = true;
      aktuellerCluster.animalActualColor = Color.gray;
      aktuellerCluster.setAnimalCircle(lang.newCircle(new Offset(
          aktuellerCluster.getCoordX(), aktuellerCluster.getCoordY(),
          offsetPunkt, AnimalScript.DIRECTION_C), overallRadius,
          aktuellerCluster.getNodename(), null, unkategorisierterCircle));

      aktuellerCluster.setAnimalText(lang.newText(
          new Offset(aktuellerCluster.getCoordX() + textoffsetX,
              aktuellerCluster.getCoordY() + textoffsetY, offsetPunkt,
              AnimalScript.DIRECTION_NW),
          "" + aktuellerCluster.getKnotennummer(), "beschriftung"
              + aktuellerCluster.getKnotennummer(), null));
    }

  }

  /*
   * ARC ist kaputt... Idee leider verworfen, das haette wirklich cool aussehen
   * koennen ;)
   * 
   * 
   * public void erzeugeEllipseVonAnachB(BottomUpCluster neuerCluster,
   * BottomUpCluster teilClusterA, BottomUpCluster teilClusterB) {
   * 
   * //new Ellipse(eg, aCenter, aRadius, name, display, ep) //Ellipse ellipse1 =
   * new Ellipse(null, new Coordinates(500, 284),new Coordinates(20,
   * 20),"ellipse1",null,ellipseProps); int offsetX = neuerCluster.getCoordX() -
   * teilClusterA.getCoordX(); int offsetY = neuerCluster.getCoordY() -
   * teilClusterA.getCoordY();
   * 
   * int absOffsetX = Math.abs(offsetX); int absOffsetY = Math.abs(offsetY);
   * 
   * if (teilClusterA == null) { System.out.println("TeilCluster A ist NULL"); }
   * 
   * if (teilClusterB == null) { System.out.println("TeilCluster A ist NULL"); }
   * 
   * System.out.println(teilClusterA.getAnimalCircle().getRadius());
   * System.out.println(teilClusterB.getAnimalCircle().getRadius());
   * 
   * System.out.println("OffsetX = " + neuerCluster.getCoordX() + " - " +
   * teilClusterA.getCoordX() + " => " + offsetX);
   * System.out.println("OffsetY = " + neuerCluster.getCoordY() + " - " +
   * teilClusterA.getCoordY() + " => " + offsetY);
   * 
   * Offset radiusOffset = new Offset(offsetX, offsetY, offsetPunkt,
   * AnimalScript.DIRECTION_C);
   * 
   * 
   * System.out.println("CenterNormal (" + neuerCluster.getCoordinates().getX()
   * + "," + neuerCluster.getCoordinates().getY() +")");
   * System.out.println("CenterNormal (" +
   * neuerCluster.getAnimalCircle().getCenter().toString() + "," +
   * neuerCluster.getCoordinates().getX());
   * 
   * // lang.newEllipse(neuerCluster.getAnimalCircle().getCenter(), new
   * Coordinates(offsetX,offsetY), "ellipomane", null); //
   * lang.newEllipse(neuerCluster.getAnimalCircle(), radius, name, display)
   * 
   * // arc "arc2" (100,100) radius ( 20,40) angle 180 starts 45
   * counterclockwise closed filled fillColor blue ArcProperties arcprops = new
   * ArcProperties(); arcprops.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 180);
   * arcprops.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 45);
   * 
   * arcprops.set(AnimationPropertiesKeys.CLOSED_PROPERTY, true);
   * 
   * arcprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
   * arcprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.blue);
   * 
   * // arcprops.set(AnimationPropertiesKeys.CLOSED_PROPERTY, false); //
   * arcprops.set(AnimationPropertiesKeys., value) //
   * lang.newArc(neuerCluster.getCoordinates(), new
   * Coordinates(offsetX,offsetY), "theARC", arcprops);
   * 
   * lang.newArc(new OffsetCoords(offsetPunkt.getCoords(),
   * neuerCluster.getCoordX(), neuerCluster.getCoordY()), new Coordinates(20,
   * 90), "theARC", null, arcprops); // arc "ar2" (100, 100) radius (20, 40)
   * angle 180 starts 45 counterclockwise closed filled fillColor blue //
   * lang.newEllipse(neuerCluster.getCoordinates(),new Offset(20,20,new
   * Coordinates(0,0), AnimalScript.DIRECTION_C),"ellipse1",null,ellipseProps);
   * lang.nextStep(); // System.out.println("Elipse von (" + A.getX() + "," +
   * A.getY()+ ") nach (" + B.getX() + "," + B.getY() + ")"); }
   */

  /*
   * Malt einen temporaeren Pfeil von A nach B
   */
  public void erzeugeTempPfeilVonAnachB(Coordinates A, Coordinates B) {

    // Pfeil Anfangs- und Endpunkt
    Node[] offsetpfeilnode = {
        new Offset(A.getX(), A.getY(), offsetPunkt, AnimalScript.DIRECTION_C),
        new Offset(B.getX(), B.getY(), offsetPunkt, AnimalScript.DIRECTION_C), };
    listOfTempPfeile.add(lang.newPolyline(offsetpfeilnode, "pfeil", null,
        pfeilTempProps));

  }

  /*
   * Versteckt alle als temporaer gezeichneten Pfeile
   */
  public void killTempPfeile() {
    if (listOfTempPfeile.size() > 0) {
      for (int i = 0; i < listOfTempPfeile.size(); i++) {
        listOfTempPfeile.get(i).hide();
      }
    }

  }

  /*
   * Malt einen Pfeil von A nach B
   */
  public void erzeugePfeilVonAnachB(Coordinates A, Coordinates B,
      PolylineProperties props) {

    // Pfeil Anfangs- und Endpunkt
    Node[] offsetpfeilnode = {
        new Offset(A.getX(), A.getY(), offsetPunkt, AnimalScript.DIRECTION_C),
        new Offset(B.getX(), B.getY(), offsetPunkt, AnimalScript.DIRECTION_C), };
    listOfPfeile.add(lang.newPolyline(offsetpfeilnode, "pfeil", null, props));

  }

  /*
   * Faerbe alte pfeile hellgrau.
   */
  public void maleAltePfeileSanft() {
    ArrayList<Polyline> pfeileListe = listOfPfeile;

    for (Polyline polyline : pfeileListe) {
      polyline.changeColor("Color", Color.gray, null, null);
    }

  }

  /*
   * Zeichnet Pfeile von allen Knoten innerhalb eines Clusters in die
   * Clustermitte.
   */
  public void maleClusterPfeileInsZentrum(BottomUpCluster clusterZentrum) {
    // ArrayList<Polyline> pfeileListe = clusterZentrum.listClusterPfeile;
    HashMap<String, BottomUpCluster> clusterChilds = clusterZentrum.childClusters;
    Set<String> alleClusters = clusterChilds.keySet();

    for (String cluster : alleClusters) {
      if (clusterChilds.get(cluster).isStartCluster) {
        clusterChilds.get(cluster).getAnimalCircle()
            .changeColor("fillColor", Color.green, null, null);
        // Pfeil Anfangs- und Endpunkt
        Node[] offsetpfeilnode = {
            new Offset(clusterZentrum.getCoordX(), clusterZentrum.getCoordY(),
                offsetPunkt, AnimalScript.DIRECTION_C),
            new Offset(clusterChilds.get(cluster).getCoordX(), clusterChilds
                .get(cluster).getCoordY(), offsetPunkt,
                AnimalScript.DIRECTION_C) };
        clusterZentrum.listClusterPfeile.add(lang.newPolyline(offsetpfeilnode,
            "pfeil", null, pfeilProps));
      }
    }

  }

  /*
   * Setzt die Farbe aller Cluster auf die im Cluster gespeicherte AnimalColor
   * zurueck.
   */
  public void resetClusterFarbe(BottomUpCluster clusterZentrum) {

    Set<String> alleClusters = mapOfClusters.keySet();

    for (String cluster : alleClusters) {
      if (mapOfClusters.get(cluster).isStartCluster) {
        mapOfClusters
            .get(cluster)
            .getAnimalCircle()
            .changeColor("fillColor",
                mapOfClusters.get(cluster).animalActualColor, null, null);
      }
    }
  }

  /*
   * Fuere .hide() auf alle Pfeile aus, die in die Clustermitte zeigen.
   */
  public void versteckeClusterPfeileInsZentrum(BottomUpCluster clusterZentrum) {
    ArrayList<Polyline> pfeilliste = clusterZentrum.listClusterPfeile;
    if (pfeilliste.size() > 0) {
      for (int i = 0; i < pfeilliste.size(); i++) {
        pfeilliste.get(i).hide();
      }
    }
  }

  /*
   * Finde das naechste Clusterpaar und erzeuge gleich einen neuen Cluster mit
   * linkem und rechtem Sohn.
   * 
   * Die Methode ruft auch die visualisierenden Methoden auf.
   */
  public void findNextClusterPair() {
    overallIterations++;
    double kleinsteDistanz = 100000;
    BottomUpCluster nextClusterA = null;
    BottomUpCluster nextClusterB = null;
    boolean foundone = false;

    Set<String> alleCluster = mapOfClusters.keySet();
    for (String clusterInDerMapA : alleCluster) {
      BottomUpCluster clusterZumVergleichA = mapOfClusters
          .get(clusterInDerMapA);

      for (String clusterInDerMapB : alleCluster) {
        BottomUpCluster clusterZumVergleichB = mapOfClusters
            .get(clusterInDerMapB);

        if (clusterZumVergleichA != clusterZumVergleichB
            && (!clusterZumVergleichA.isSubCluster && !clusterZumVergleichB.isSubCluster)) {
          overallDistanceChecks++;
          // System.out.println("Vergleiche " +
          // clusterZumVergleichA.getNodename() + " mit " +
          // clusterZumVergleichB.getNodename());
          if (getAverageLinkClusterDistance(clusterZumVergleichA,
              clusterZumVergleichB) < kleinsteDistanz) {

            kleinsteDistanz = getAverageLinkClusterDistance(
                clusterZumVergleichA, clusterZumVergleichB);
            nextClusterA = clusterZumVergleichA;
            nextClusterB = clusterZumVergleichB;
            foundone = true;

          }

        }

      }

    }
    pseudocodeText.unhighlight(0);
    pseudocodeText.highlight(1);
    updateStatusText();

    if (foundone) {
      /* Neuen Cluster zusammebauen */
      Coordinates neueClusterCoords = getAverageLinkClusterMiddle(nextClusterA,
          nextClusterB);

      // mapOfClusters.put("Cluster"+anzahlVonVektoren+1, new
      // BottomUpCluster(neueClusterCoords.getX(), neueClusterCoords.getY(),
      // "Cluster"+anzahlVonVektoren+1, anzahlVonVektoren+1));

      // Der neu gebildete Cluster erhaelt als Key und Nodename die
      // naechsthoehere freie Nummer.
      String neuerClusterKey = "Cluster" + anzahlVonVektoren + 1;
      BottomUpCluster neuerCluster = new BottomUpCluster(
          neueClusterCoords.getX(), neueClusterCoords.getY(), "Cluster"
              + anzahlVonVektoren + 1, anzahlVonVektoren + 1);
      mapOfClusters.put(neuerClusterKey, neuerCluster);

      nextClusterA.setSubCluster(true);
      nextClusterA.retired = overallIterations;
//      nextClusterA.parentCluster = neuerCluster;

      if (nextClusterA.isStartCluster) {
        /*
         * nextClusterA.setAnimalCircle( lang.newCircle(new
         * Offset(nextClusterA.getCoordX(), nextClusterA.getCoordY(),
         * offsetPunkt, AnimalScript.DIRECTION_C), overallRadius,
         * nextClusterA.getNodename(), null, unkategorisierterCircle) );
         * nextClusterA.setAnimalText( lang.newText(new
         * Offset(nextClusterA.getCoordX
         * ()+textoffsetX,nextClusterA.getCoordY()+textoffsetY
         * ,offsetPunkt,AnimalScript.DIRECTION_NW),
         * ""+nextClusterA.getKnotennummer(),
         * "beschriftung"+nextClusterA.getKnotennummer(), null));
         * nextClusterA.animalActualColor = Color.blue;
         */
        nextClusterA.getAnimalCircle().changeColor("fillColor",
            nextClusterA.animalActualColor, null, null);

      }

      nextClusterB.setSubCluster(true);
      nextClusterB.retired = overallIterations;
//      nextClusterB.parentCluster = neuerCluster;

      if (nextClusterB.isStartCluster) {
        /*
         * nextClusterB.setAnimalCircle( lang.newCircle(new
         * Offset(nextClusterB.getCoordX(), nextClusterB.getCoordY(),
         * offsetPunkt, AnimalScript.DIRECTION_C), overallRadius,
         * nextClusterB.getNodename(), null, unkategorisierterCircle) );
         * nextClusterB.setAnimalText( lang.newText(new
         * Offset(nextClusterB.getCoordX
         * ()+textoffsetX,nextClusterB.getCoordY()+textoffsetY
         * ,offsetPunkt,AnimalScript.DIRECTION_NW),
         * ""+nextClusterB.getKnotennummer(),
         * "beschriftung"+nextClusterB.getKnotennummer(), null));
         */
        nextClusterB.animalActualColor = Color.blue;
        nextClusterB.getAnimalCircle().changeColor("fillColor",
            nextClusterB.animalActualColor, null, null);
      }

      // Hier noch die Childs der Childs anhaengen... Methode vielleicht noetig
      // System.out.println("A childs size " +
      // nextClusterA.childClusters.size());
      // System.out.println("B childs size " +
      // nextClusterB.childClusters.size());
      neuerCluster.childClusters.putAll(nextClusterA.childClusters);
      neuerCluster.childClusters.putAll(nextClusterB.childClusters);
      // neuerCluster.birth = overallIterations;

//      neuerCluster.subClusterA = nextClusterA;
//      neuerCluster.subClusterB = nextClusterB;
      neuerCluster.childClusters.put(nextClusterA.getNodename(), nextClusterA);
      neuerCluster.childClusters.put(nextClusterB.getNodename(), nextClusterB);
      anzahlVonVektoren++;
      // System.out.println("Neuer Cluster ist " +
      // mapOfClusters.get(neuerClusterKey).getNodename() + " (" +
      // nextClusterA.getNodename() + "," + nextClusterB.getNodename() +
      // ") mit " + kleinsteDistanz + " und mitte bei (" +
      // neueClusterCoords.getX() + "," + neueClusterCoords.getY() + ")");
      // erzeugePfeilVonAnachB(nextClusterA.getCoordinates(),
      // nextClusterB.getCoordinates());

      // Clustermitte einzeichen
      /*
       * neuerCluster.setAnimalCircle( lang.newCircle(new
       * Offset(neuerCluster.getCoordX(), neuerCluster.getCoordY(), offsetPunkt,
       * AnimalScript.DIRECTION_C), overallRadius, neuerCluster.getNodename(),
       * null, roterCircle) );
       */
      neuerCluster.setAnimalText(lang.newText(
          new Offset(neuerCluster.getCoordX() + textoffsetX, neuerCluster
              .getCoordY() + textoffsetY, offsetPunkt,
              AnimalScript.DIRECTION_NW), "" + neuerCluster.getKnotennummer(),
          neuerCluster.getNodename(), null));

      maleAverageLinkClusterConnections(nextClusterA, nextClusterB);
      versteckeClusterPfeileInsZentrum(nextClusterA);
      versteckeClusterPfeileInsZentrum(nextClusterB);
      // erzeugePfeilVonAnachB(neuerCluster.getCoordinates(),
      // nextClusterA.getCoordinates());
      // erzeugePfeilVonAnachB(neuerCluster.getCoordinates(),
      // nextClusterB.getCoordinates(), pfeilProps);
      // erzeugeEllipseVonAnachB(neuerCluster, nextClusterA, nextClusterB);
      // malePfeileZuNeuemCluster(neuerCluster);

      lang.nextStep();
      pseudocodeText.unhighlight(1);
      pseudocodeText.highlight(2);
      updateStatusText();
      killTempPfeile();
      versteckeClusterPfeileInsZentrum(neuerCluster);
      maleClusterPfeileInsZentrum(neuerCluster);

      updateHirarchiefenster();

      lang.nextStep();
      pseudocodeText.unhighlight(2);
      pseudocodeText.highlight(0);
      updateStatusText();
      // killTempPfeile();
    } else {
      System.out.println("none found");
    }
  }

  /*
   * Berechne die Average Link Distance
   * 
   * Wurde umgestellt auf Centroiden - gleiches Ergebnis. Siehe Methode tiefer.
   */
  public double getAverageLinkClusterDistance(BottomUpCluster clusterA,
      BottomUpCluster clusterB) {
    double returnwert = 0;
    int counter = 0;

    Set<String> alleClusterA = clusterA.childClusters.keySet();
    Set<String> alleClusterB = clusterB.childClusters.keySet();

    // int anzahlClusterInA = clusterA.childClusters.size();
    // int anzahlClusterInB = clusterB.childClusters.size();

    BottomUpCluster aktuellerClusterInA = clusterA;
    BottomUpCluster aktuellerClusterInB = clusterB;

    if (clusterA.childClusters.isEmpty() && clusterB.childClusters.isEmpty()) {
      returnwert = getEuklideanDistanceBetweenCoords(clusterA.getCoordinates(),
          clusterB.getCoordinates());
      counter = 1;
    } else if (clusterA.childClusters.isEmpty()
        && !clusterB.childClusters.isEmpty()) {
      // System.out.println("Cluster A is a Node, Cluster B is a REAL CLUSTER");
      for (String clusterInB : alleClusterB) {

        aktuellerClusterInB = clusterB.childClusters.get(clusterInB);
        returnwert = returnwert
            + getEuklideanDistanceBetweenCoords(clusterA.getCoordinates(),
                aktuellerClusterInB.getCoordinates());
        counter++;
      }
    } else if (!clusterA.childClusters.isEmpty()
        && clusterB.childClusters.isEmpty()) {
      // System.out.println("Cluster A is a REAL CLUSTER, Cluster B is Node");

      for (String clusterInA : alleClusterA) {

        aktuellerClusterInA = clusterA.childClusters.get(clusterInA);
        returnwert = returnwert
            + getEuklideanDistanceBetweenCoords(
                aktuellerClusterInA.getCoordinates(), clusterB.getCoordinates());
        counter++;
      }
    } else {
      for (String clusterInA : alleClusterA) {
        // System.out.println("Ich bin in Cluster A");

        aktuellerClusterInA = clusterA.childClusters.get(clusterInA);

        for (String clusterInB : alleClusterB) {

          aktuellerClusterInB = clusterB.childClusters.get(clusterInB);
          returnwert = returnwert
              + getEuklideanDistanceBetweenCoords(
                  aktuellerClusterInA.getCoordinates(),
                  aktuellerClusterInB.getCoordinates());
          counter++;
        }

      }
    }

    // System.out.println("Sum of all Distances is " + returnwert);

    returnwert = returnwert / counter;

    // System.out.println("Average Link Distance between " +
    // clusterA.getNodename() + " and " + clusterB.getNodename() + " = " +
    // returnwert);
    return returnwert;

  }

  /*
   * Stelle die Average Link Distance als Pfeile auf der Karte dar.
   */
  public void maleAverageLinkClusterConnections(BottomUpCluster clusterA,
      BottomUpCluster clusterB) {
    Set<String> alleClusterA = clusterA.childClusters.keySet();
    Set<String> alleClusterB = clusterB.childClusters.keySet();

    if (clusterA.childClusters.size() > 0 && clusterB.childClusters.size() > 0) {
      for (String clusterInA : alleClusterA) {

        for (String clusterInB : alleClusterB) {
          if (clusterA.childClusters.get(clusterInA).isStartCluster
              && clusterB.childClusters.get(clusterInB).isStartCluster) {
            clusterA.childClusters.get(clusterInA).getAnimalCircle()
                .changeColor("fillColor", Color.blue, null, null);
            clusterB.childClusters.get(clusterInB).getAnimalCircle()
                .changeColor("fillColor", Color.red, null, null);
            erzeugeTempPfeilVonAnachB(clusterA.childClusters.get(clusterInA)
                .getCoordinates(), clusterB.childClusters.get(clusterInB)
                .getCoordinates());
          }
        }
      }
    }
    // Wenn Cluster A keine Childs hat
    else if (clusterA.childClusters.size() == 0
        && clusterB.childClusters.size() > 0) {
      for (String clusterInB : alleClusterB) {
        if (clusterB.childClusters.get(clusterInB).isStartCluster) {
          clusterA.getAnimalCircle().changeColor("fillColor", Color.blue, null,
              null);
          clusterB.childClusters.get(clusterInB).getAnimalCircle()
              .changeColor("fillColor", Color.red, null, null);
          erzeugeTempPfeilVonAnachB(clusterA.getCoordinates(),
              clusterB.childClusters.get(clusterInB).getCoordinates());
        }
      }
    }
    // Wenn Cluster B keine Childs hat
    else if (clusterA.childClusters.size() > 0
        && clusterB.childClusters.size() == 0) {
      for (String clusterInA : alleClusterA) {
        if (clusterA.childClusters.get(clusterInA).isStartCluster) {

          clusterB.getAnimalCircle().changeColor("fillColor", Color.red, null,
              null);
          clusterA.childClusters.get(clusterInA).getAnimalCircle()
              .changeColor("fillColor", Color.blue, null, null);
          erzeugeTempPfeilVonAnachB(clusterA.childClusters.get(clusterInA)
              .getCoordinates(), clusterB.getCoordinates());
        }
      }
    }
    // Wenn Cluster A und B Starcluster sind
    else if (clusterA.childClusters.size() == 0
        && clusterB.childClusters.size() == 0) {
      erzeugeTempPfeilVonAnachB(clusterA.getCoordinates(),
          clusterB.getCoordinates());
      clusterA.getAnimalCircle().changeColor("fillColor", Color.blue, null,
          null);
      clusterB.getAnimalCircle()
          .changeColor("fillColor", Color.red, null, null);

    }

  }

  /*
   * Gibt den Mittelpunkt der des neuen Clusters mit AverageDistanceLink aus
   * 
   * Die Methode wurde umgestellt, da der Vergleich zweier Clustermitten (die
   * urspruenglich mal fuer die Mitte der geplanten Ellipsen gedacht war) das
   * gleiche Ergebnis bringt.
   */
  public Coordinates getAverageLinkClusterMiddle(BottomUpCluster clusterA,
      BottomUpCluster clusterB) {
    // System.out.println("START Mittebestimmung fuer: " +
    // clusterA.getNodename() + " + " + clusterB.getNodename());
    int counter = 0;

    int neuesX = 0;
    int neuesY = 0;
    // Set<String> alleClusterA = clusterA.childClusters.keySet();
    // Set<String> alleClusterB = clusterB.childClusters.keySet();

    // int anzahlClusterInA = clusterA.childClusters.size();
    // int anzahlClusterInB = clusterB.childClusters.size();

    neuesX = (clusterA.getCoordX() + clusterB.getCoordX());
    neuesY = (clusterA.getCoordY() + clusterB.getCoordY());
    counter = 2;

    /*
     * BottomUpCluster aktuellerClusterInA = clusterA; BottomUpCluster
     * aktuellerClusterInB = clusterB;
     * 
     * if(clusterA.childClusters.isEmpty() && clusterB.childClusters.isEmpty())
     * { neuesX = (clusterA.getCoordX() + clusterB.getCoordX()); neuesY =
     * (clusterA.getCoordY() + clusterB.getCoordY()); counter = 2; } else if
     * (clusterA.childClusters.isEmpty() && !clusterB.childClusters.isEmpty()) {
     * System
     * .out.println("Middle: Cluster A is a Node, Cluster B is a REAL CLUSTER");
     * 
     * neuesX = (clusterA.getCoordX()); neuesY = (clusterA.getCoordY());
     * counter++;
     * 
     * for (String clusterInB : alleClusterB) {
     * 
     * aktuellerClusterInB = clusterB.childClusters.get(clusterInB);
     * 
     * neuesX = (neuesX + aktuellerClusterInB.getCoordX()); neuesY = (neuesY +
     * aktuellerClusterInB.getCoordY());
     * 
     * counter++; } } else if (!clusterA.childClusters.isEmpty() &&
     * clusterB.childClusters.isEmpty()) {
     * System.out.println("Middle: Cluster A is a REAL CLUSTER, Cluster B is Node"
     * );
     * 
     * neuesX = (clusterB.getCoordX()); neuesY = (clusterB.getCoordY());
     * counter++;
     * 
     * 
     * for (String clusterInA : alleClusterA) {
     * 
     * aktuellerClusterInA = clusterA.childClusters.get(clusterInA);
     * 
     * neuesX = (neuesX + aktuellerClusterInA.getCoordX()); neuesY = (neuesY +
     * aktuellerClusterInA.getCoordY());
     * 
     * counter++; } } else { for (String clusterInA : alleClusterA) {
     * 
     * aktuellerClusterInA = clusterA.childClusters.get(clusterInA); neuesX =
     * (neuesX + aktuellerClusterInA.getCoordX()); neuesY = (neuesY +
     * aktuellerClusterInA.getCoordY());
     * 
     * 
     * for (String clusterInB : alleClusterB) {
     * 
     * aktuellerClusterInB = clusterB.childClusters.get(clusterInB);
     * 
     * neuesX = (neuesX + aktuellerClusterInB.getCoordX()); neuesY = (neuesY +
     * aktuellerClusterInB.getCoordY());
     * 
     * counter++; }
     * 
     * } } // Okay, gekuerzt geht das einfacher :D
     */
    // System.out.println("Sum of all Distances is " + returnwert);

    // System.out.println("Alle X waeren " + neuesX);
    // System.out.println("Alle Y waeren " + neuesY);
    // System.out.println("Counter ist " + counter);

    neuesX = neuesX / counter;
    neuesY = neuesY / counter;

    // System.out.println("Neue Coords (" + neuesX + "," + neuesY + ")");
    // System.out.println("END Average Link Distance between (" +
    // clusterA.getCoordX() + "," + clusterA.getCoordY() + ") " + " and (" +
    // clusterB.getCoordX() + "," + clusterB.getCoordY() + ") ");

    return (new Coordinates(neuesX, neuesY));

  }

  /*
   * Erstellt den Pseudocode rechts oben
   */
  private void erzeugePseudeocodeText() {
    SourceCodeProperties pseudoeProps = new SourceCodeProperties();
    pseudoeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    pseudoeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    pseudoeProps
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    pseudoeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    pseudocodeText = lang.newSourceCode(new Offset(500, 0, offsetPunkt,
        AnimalScript.DIRECTION_SW), "baumText", null, pseudoeProps);

    pseudocodeText.addCodeLine("Solange noch X > 1 Cluster vorhanden sind:",
        null, 0, null);
    pseudocodeText
        .addCodeLine(
            "1. Finde das Clusterpaar mit der kleinsten Distanz (average Distance)",
            null, 1, null);
    pseudocodeText.addCodeLine(
        "2. Erzeuge neuen Cluster durch Vereinigung des Clusterpaares", null,
        1, null);
    // pseudocodeText.addCodeLine("Solange noch X > 2 Cluster vorhanden sind",
    // null, 1, null);

  }

  /*
   * Erstellt das Feld, in dem der Hierarchiebaum als Text pro Iteration
   * ausgegeben werden soll.
   */
  private void erzeugeBaumText() {

    SourceCodeProperties baumtextProps = new SourceCodeProperties();
    baumtextProps
        .set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    baumtextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    baumtextProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    baumtextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    baumText = lang.newSourceCode(new Offset(500, 110, offsetPunkt,
        AnimalScript.DIRECTION_SW), "baumText", null, baumtextProps);
  }

  /*
   * Erzeuge eine Trennline ueber dem Pseudocode
   */
  private void erzeugePseudeoTextRahmen() {
    TextProperties pseudoTextTextProps = new TextProperties();
    pseudoTextTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 12));

    // historyArray.add(zeigeHirarchieZeileDieserIteration());

    pseudoTextText = lang.newText(new Offset(0, -20, pseudocodeText,
        AnimalScript.DIRECTION_NW), "Pseudocode", "pesudoCode", null,
        pseudoTextTextProps);

    Node[] offsetpseudo1 = {
        new Offset(0, 3, pseudoTextText, AnimalScript.DIRECTION_SW),
        new Offset(400, 3, pseudoTextText, AnimalScript.DIRECTION_SW) };

    pseudoTextTextUnderline = lang.newPolyline(offsetpseudo1,
        "TrennliniePseudoText", null, pfeilProps);

  }

  /*
   * Male den Rahmen um das Feld, in dem die Baumhierarchie gezeigt wird.
   */
  private void erzeugeBaumTextRahmen() {
    TextProperties baumTextProps = new TextProperties();
    baumTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 12));

    // historyArray.add(zeigeHirarchieZeileDieserIteration());

    baumTextText = lang.newText(new Offset(500, 100, offsetPunkt,
        AnimalScript.DIRECTION_C), "Erzeugte Hierarchie (der Iteration)",
        "baumTextText", null, baumTextProps);

    Node[] offsetbaum1 = {
        new Offset(0, 3, baumTextText, AnimalScript.DIRECTION_SW),
        new Offset(400, 3, baumTextText, AnimalScript.DIRECTION_SW) };

    baumTextTextUnderscore = lang.newPolyline(offsetbaum1,
        "TrennlinieBaumText", null, pfeilProps);
    // lang.newPolyline(offsetbaum2, "TrennlinieBaumText", null, pfeilProps);
    // listOfTempPfeile.add(lang.newPolyline(offsetpfeilnode, "pfeil", null,
    // pfeilTempProps));
    // Offset rectangleoffset_obbelinks = new
    // Offset(490,48,offsetPunkt,AnimalScript.DIRECTION_C);
    // Offset rectangleoffest_unnerechts = new
    // Offset(790,400,offsetPunkt,AnimalScript.DIRECTION_C);
    // lang.newPolyline(vertices, name, display)
    // lang.newRect(rectangleoffset_obbelinks,rectangleoffest_unnerechts,
    // "hirarchRect", null);
  }

  /*
   * Kreiert das TextObjekt fue den Titel
   */
  public void erzeugeTitel() {
    /* Ueberschrift generieren */
    TextProperties titleProps = new TextProperties();
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    titelText = lang.newText(new Coordinates(50, 35),
        "Hierarchical Agglomerative (Bottom-Up) Clustering", "titletext", null,
        titleProps);
    Offset rectangleoffset_obbelinks = new Offset(-30, -10, titelText,
        AnimalScript.DIRECTION_NW);
    Offset rectangleoffest_unnerechts = new Offset(30, 10, titelText,
        AnimalScript.DIRECTION_SE);
    lang.newRect(rectangleoffset_obbelinks, rectangleoffest_unnerechts,
        "headerRect", null);
  }

  /**
   * Erzeuge Einleitungsfolien in Animalscript.
   */
  public void generateIntro() {

    descriptionText.show();

    SourceCodeProperties einleitungProps = new SourceCodeProperties();
    einleitungProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    einleitungProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    einleitungProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    einleitungProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    einleitungsText = lang.newSourceCode(new Offset(0, 10, descriptionText,
        AnimalScript.DIRECTION_SW), "einleitungsText", null, einleitungProps);

    einleitungsText.addCodeLine("Die Idee des Algorithmus", null, 0, null); // 0
    einleitungsText.addCodeLine("", null, 0, null);
    einleitungsText
        .addCodeLine(
            "Der Algorithmus versucht aus einer Menge ungeordneter Klassen eine Hierarchie zu erstellen.",
            null, 0, null);
    einleitungsText.addCodeLine("", null, 0, null); // 3
    einleitungsText
        .addCodeLine(
            "Es wird so versucht, die ungeordnete Klassen nach 'Verwandschaft' hierarchisch zu sortieren.",
            null, 0, null); // 4
    einleitungsText
        .addCodeLine(
            "Die Aehnlichkeit der Klassen wird ueber die Distanz bestimmt. Zwei Cluster mit der geringsten Distanz",
            null, 0, null); // 5
    einleitungsText
        .addCodeLine(
            "weisen demnach die groesste Aehnlichkeit zueinander auf. Aufgrund dieser Aehnlichkeit werden diese",
            null, 0, null); // 6
    einleitungsText.addCodeLine(
        "beiden dann in einem uebergeordneten Cluster zusammengefasst.", null,
        0, null); // 7
    einleitungsText.addCodeLine("", null, 0, null); // 7
    einleitungsText
        .addCodeLine(
            "Auf diese Weise kann man mit einem Querschnitt im Hierarchiebaum entweder viele feingranulierte Clusterkategorien bilden",
            null, 0, null); // 8
    einleitungsText
        .addCodeLine(
            "(kleines i / tiefes Level) oder einen groben Ueberblick ueber die zu erwartende Klassenhierarchie (grosses i / hohes level) bekommen. ",
            null, 0, null); // 9
    // einleitungsText.addCodeLine("", null, 0, null); // 9

    /* Die Initiale Struktur malen */
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

    String[] arrayContents0 = new String[] { " A ", " B ", " C ", " D ", " E ",
        " F " };
    String[] arrayContents1 = new String[] { " (A, B) ", " C ", " D ", " E ",
        " F " };
    String[] arrayContents2 = new String[] { " (A, B) ", " C ", " (D, E ) ",
        " F " };
    String[] arrayContents3 = new String[] { " (A, B) ", " (C, D, E) ", " F " };
    String[] arrayContents4 = new String[] { " (A, B, C, D, E) ", " F " };
    String[] arrayContents5 = new String[] { " (A, B, C, D, E, F) " };

    /* Punkte zeichnen */
    Circle cA = lang.newCircle((new Offset(-100, 80, introPoint,
        AnimalScript.DIRECTION_NW)), overallRadius, "cA", null,
        unkategorisierterCircle);
    Circle cB = lang.newCircle((new Offset(-70, 80, introPoint,
        AnimalScript.DIRECTION_NW)), overallRadius, "cB", null,
        unkategorisierterCircle);
    Circle cC = lang.newCircle((new Offset(15, 80, introPoint,
        AnimalScript.DIRECTION_NW)), overallRadius, "cC", null,
        unkategorisierterCircle);
    Circle cD = lang.newCircle((new Offset(60, 65, introPoint,
        AnimalScript.DIRECTION_NW)), overallRadius, "cD", null,
        unkategorisierterCircle);
    Circle cE = lang.newCircle((new Offset(60, 95, introPoint,
        AnimalScript.DIRECTION_NW)), overallRadius, "cE", null,
        unkategorisierterCircle);
    Circle cF = lang.newCircle((new Offset(300, 80, introPoint,
        AnimalScript.DIRECTION_NW)), overallRadius, "cF", null,
        unkategorisierterCircle);

    Text tA = lang.newText((new Offset(4, -4, cA, AnimalScript.DIRECTION_SW)),
        "A", "textA", null);
    Text tB = lang.newText((new Offset(4, 0, cB, AnimalScript.DIRECTION_SW)),
        "B", "textB", null);
    Text tC = lang.newText((new Offset(4, 0, cC, AnimalScript.DIRECTION_SW)),
        "C", "textC", null);
    Text tD = lang.newText((new Offset(4, 0, cD, AnimalScript.DIRECTION_SW)),
        "D", "textD", null);
    Text tE = lang.newText((new Offset(4, 0, cE, AnimalScript.DIRECTION_SW)),
        "E", "textE", null);
    Text tF = lang.newText((new Offset(4, 0, cF, AnimalScript.DIRECTION_SW)),
        "F", "textF", null);

    algoanim.primitives.StringArray dasArray0 = lang.newStringArray(
        new Coordinates(650, 600), arrayContents0, "array0", null, arrayProps);
    lang.nextStep("Einleitung");

    /*
     * Male Ellipsen, um die Cluster kenntlich zu machen. Leider dynamisch nicht
     * moeglich, weil ARC nicht geht. Wenn noch Zeit, pruefen ob aussere Huelle
     * manuell bestimmt werden kann. Erstmal mit Clustermitte darstellen.
     */

    // descriptionText.setText("Funktionsweise", null, null);

    Ellipse elAB = lang.newEllipse((new Offset(-85, 80, introPoint,
        AnimalScript.DIRECTION_NW)), new Coordinates(27, 15), "elAB", null);
    elAB.changeColor("Color", Color.blue, null, null);
    algoanim.primitives.StringArray dasArray1 = lang.newStringArray(
        new Coordinates(645, 570), arrayContents1, "array1", null, arrayProps);
    lang.nextStep();

    Ellipse elDE = lang.newEllipse((new Offset(60, 80, introPoint,
        AnimalScript.DIRECTION_NW)), new Coordinates(15, 30), "elDE", null);
    elDE.changeColor("Color", Color.blue, null, null);
    algoanim.primitives.StringArray dasArray2 = lang.newStringArray(
        new Coordinates(645, 540), arrayContents2, "array2", null, arrayProps);
    lang.nextStep();

    Ellipse elCDE = lang.newEllipse((new Offset(40, 80, introPoint,
        AnimalScript.DIRECTION_NW)), new Coordinates(50, 40), "elCDE", null);
    elCDE.changeColor("Color", Color.green, null, null);
    algoanim.primitives.StringArray dasArray3 = lang.newStringArray(
        new Coordinates(648, 510), arrayContents3, "array3", null, arrayProps);
    lang.nextStep();

    Ellipse elABCDE = lang.newEllipse((new Offset(0, 80, introPoint,
        AnimalScript.DIRECTION_NW)), new Coordinates(120, 50), "elABCDE", null);
    elABCDE.changeColor("Color", Color.orange, null, null);
    algoanim.primitives.StringArray dasArray4 = lang.newStringArray(
        new Coordinates(652, 480), arrayContents4, "array4", null, arrayProps);
    lang.nextStep();

    Ellipse elABCDEF = lang.newEllipse((new Offset(100, 80, introPoint,
        AnimalScript.DIRECTION_NW)), new Coordinates(230, 100), "elABCDDEF",
        null);
    elABCDEF.changeColor("Color", Color.red, null, null);
    algoanim.primitives.StringArray dasArray5 = lang.newStringArray(
        new Coordinates(654, 450), arrayContents5, "array5", null, arrayProps);

    // StringMatrix theMatrix = lang.newStringMatrix(new Coordinates(30, 30),
    // arrayContents, "zeile1", null); ... Matrix doch ungeeignet.

    lang.nextStep();

    // Verstecke das von Hand gezeichnete Beispiel

    cA.hide();
    cB.hide();
    cC.hide();
    cD.hide();
    cE.hide();
    cF.hide();

    tA.hide();
    tB.hide();
    tC.hide();
    tD.hide();
    tE.hide();
    tF.hide();

    dasArray0.hide();
    dasArray1.hide();
    dasArray2.hide();
    dasArray3.hide();
    dasArray4.hide();
    dasArray5.hide();

    elAB.hide();
    elDE.hide();
    elCDE.hide();
    elABCDE.hide();
    elABCDEF.hide();

    descriptionText.hide();
    einleitungsText.hide();

  }

  /*
   * Restbestand aus 5.1 Farbe als Text ausgeben, um in der Variablenlist
   * kenntlicher zu machen welcher Centroid welche Farbe hat. ... Workaround.
   * 
   * 
   * Solle im Forum eine weitere Loesung auftauchen, dann waere es cooler die
   * entsprechende Zeile in der Liste einzufaerben
   */
  public String translateColorToText(Color farbe) {
    /*
     * 
     * listOfColors.add(Color.gray); // 0 listOfColors.add(Color.red); // 1
     * listOfColors.add(Color.blue); // 2 listOfColors.add(Color.green); // 3
     * listOfColors.add(Color.black); // 4 listOfColors.add(Color.cyan); // 5
     * listOfColors.add(Color.pink); // 6 listOfColors.add(Color.yellow); // 7
     * listOfColors.add(Color.magenta); // 8 listOfColors.add(Color.white); // 9
     * listOfColors.add(Color.darkGray); // 10
     */

    if (farbe.equals(Color.gray)) {
      return "grau";
    }

    if (farbe.equals(Color.red)) {
      return "rot";
    }

    if (farbe.equals(Color.blue)) {
      return "blau";
    }

    if (farbe.equals(Color.green)) {
      return "gruen";
    }

    if (farbe.equals(Color.black)) {
      return "schwarz";
    }

    if (farbe.equals(Color.cyan)) {
      return "cyan";
    }

    if (farbe.equals(Color.pink)) {
      return "pink";
    }

    if (farbe.equals(Color.yellow)) {
      return "gelb";
    }

    if (farbe.equals(Color.magenta)) {
      return "magenta";
    }
    if (farbe.equals(Color.white)) {
      return "weiss";
    }
    if (farbe.equals(Color.darkGray)) {
      return "dunkelgrau";
    }

    // Ansonsten... aber sollte nicht vorkommen.
    return "unbekannt";

  }

  /*
   * Ungenutzt: Variablenliste wird derzeit nicht verwendet
   * 
   * public void updateVariables(HashMap<String,BottomUpCluster> knotenMap,
   * HashMap<String,BottomUpCluster> centroidenMap) { if(variablenListeAnzeigen)
   * { Set<String> alleKnoten = knotenMap.keySet(); Set<String> alleCentroiden =
   * centroidenMap.keySet(); for (String knoten : alleKnoten) {
   * 
   * vars.set(knotenMap.get(knoten).getNodename()+"-xy", "(" +
   * knotenMap.get(knoten).getCoordX()
   * +","+knotenMap.get(knoten).getCoordY()+")"); //
   * vars.setRole(knotenMap.get(knoten).getNodename(), "(" +
   * knotenMap.get(knoten).getCoordX()
   * +","+knotenMap.get(knoten).getCoordY()+")");
   * 
   * }
   * 
   * for (String centroid : alleCentroiden) {
   * 
   * 
   * vars.set(centroidenMap.get(centroid).getNodename()+"-xy", "(" +
   * centroidenMap.get(centroid).getCoordX()
   * +","+centroidenMap.get(centroid).getCoordY()+")"); // double
   * averageDistance = getAverageDistance(centroidenMap.get(centroid),
   * knotenMap); // averageDistance = Math.round( averageDistance * 100. ) /
   * 100.; // DecimalFormat df = new DecimalFormat( "0.00" ); // double myDouble
   * = Math.round( averageDistance * 100. ) / 100.;
   * 
   * // String s = df.format( myDouble ); // System.out.println(myDouble); //
   * System
   * .out.println(translateColorToText(centroidenMap.get(centroid).getAnimalColor
   * ())); // vars.set(centroidenMap.get(centroid).getNodename()+"-avgDistance",
   * ""+ averageDistance);
   * 
   * } }
   * 
   * }
   */

  /*
   * Pruefe, ob mehr als ein Cluster uebrig ist.
   */
  public boolean areThereTwoOrMoreClustersLeft() {

    int counter = 0;
    Set<String> alleCluster = mapOfClusters.keySet();
    for (String einCluster : alleCluster) {

      if (!mapOfClusters.get(einCluster).isSubCluster) {
        counter++;
      }

    }

    if (counter > 1) {
      return true;
    } else {
      return false;
    }
  }

  /*
   * Ungenutzt: Deklariere Variablen initial, nicht sicher, ob das in diesem
   * Algorithmus gut eingesetzt werden kann, da keine Zentren gebildet werden
   * und der Hierarchiebaum bereits ueber die Verknuepfungen informiert.
   * 
   * public void declareVariables(HashMap<String,BottomUpCluster> knotenMap,
   * HashMap<String,BottomUpCluster> centroidenMap) { if(variablenListeAnzeigen)
   * { // Vars anlegen vars = lang.newVariables();
   * 
   * Set<String> alleKnoten = knotenMap.keySet(); Set<String> alleCentroiden =
   * centroidenMap.keySet(); for (String knoten : alleKnoten) {
   * vars.declare("String", (knotenMap.get(knoten).getNodename())+"-xy");
   * vars.set(knotenMap.get(knoten).getNodename()+"-xy", "(" +
   * knotenMap.get(knoten).getCoordX()
   * +","+knotenMap.get(knoten).getCoordY()+")"); }
   * 
   * for (String centroid : alleCentroiden) { vars.declare("String",
   * centroidenMap.get(centroid).getNodename()+"-xy"); vars.declare("String",
   * centroidenMap.get(centroid).getNodename()+"-avgDistance");
   * vars.declare("String", centroidenMap.get(centroid).getNodename()+"-color");
   * vars.set(centroidenMap.get(centroid).getNodename()+"-xy", "(" +
   * centroidenMap.get(centroid).getCoordX()
   * +","+centroidenMap.get(centroid).getCoordY()+")");
   * vars.set(centroidenMap.get(centroid).getNodename()+"-avgDistance", "0");
   * vars.set(centroidenMap.get(centroid).getNodename()+"-color",
   * translateColorToText(centroidenMap.get(centroid).getAnimalColor()));
   * 
   * } }
   * 
   * }
   */

  /*
   * Gibt die euklidische Distanz zweier Punkte zurueck
   */
  public double getEuklideanDistanceBetweenCoords(Coordinates punktA,
      Coordinates punktB) {

    int distance_x = (punktB.getX() - punktA.getX());
    int distance_y = (punktB.getY() - punktA.getY());

    double dist = Math.sqrt((distance_x * distance_x)
        + (distance_y * distance_y));

    // System.out.println("Abstandende von '" + this.punktName + "' zu '" +
    // centroidToCompare.getNodename() + "|" +
    // centroidToCompare.getAnimalColor() + "' : (" + distance_x + "," +
    // distance_y + ") -> " + dist);
    return dist;
  }

  /**
   * Aus dem Beispiel kopiert, wird noch angepasst.
   */
  private static final String DESCRIPTION = "Hierarchical Agglomerative (Bottom Up) Clustering.\n"
                                              + "Der Algorithmus erstellt eine auf Distanzvergleichen basierende Hierarchie "
                                              + "unterschiedlicher Vektoren bzw. Klassen. \n "
                                              + "Um eine Hierarchie zu erhalten betrachtet man nach Beendigung des Algorithmus "
                                              + "eine Ebene im gebildeten Hierarchiebaum. \n "
                                              + "Bei der Distanzberechnung koennen unterschiedliche Berechnungsmethoden in Betracht kommen \n "
                                              + "Single Link -> Minimale Distanz zwischen Knoten innerhalb zweier Clustern \n "
                                              + "Complete Link -> Maximale Distanz zwischen Knoten innerhalb zweier Cluster \n "
                                              + "Average Link -> Durchschnittliche Distanz aller Knoten innerhalb zweier Cluster";

  private static final String SOURCE_CODE = "PSEUDOCODE:" // 0
                                              + "BetrachteJedesElementAlsCluster();"
                                              + "\n"
                                              + "\n"
                                              + "2. Berechne Distanz D zwischen allen Clustern"
                                              + "\n"
                                              + "3. Erzeuge neuen Parentcluster aus den zwei am naehesten beieinanderliegenen Clustern"
                                              + "\n"
                                              + "4. Berechne Distanz des neuen Clusters zu allen anderen Clustern"
                                              + "\n"
                                              + "5. Existiert mehr als ein Cluster, gehe zu 3.";

  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

  /**
   * Erzeuge AbschlussfolienAnimalscript
   */
  public void generateOutro() {

    // lang.hideAllPrimitivesExcept(wurstliste);

    descriptionText.show();
    descriptionText.setText("Zum Abschluss...", null, null);

    SourceCodeProperties outroProps = new SourceCodeProperties();
    outroProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    outroProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    outroProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    outroProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    abschlussText = lang.newSourceCode(new Coordinates(22, 90),
        "einleitungsText", null, outroProps);

    abschlussText
        .addCodeLine(
            "Um eine Hierarchie zu erhalten, betrachtet man lediglich eine Ebene im Hierarchiebaum - also alle Cluster",
            null, 0, null); // 0
    abschlussText.addCodeLine(
        "die die gleiche Anzahl von Unterclustern beinhalten.", null, 0, null); // 0
    abschlussText.addCodeLine("", null, 0, null);
    abschlussText
        .addCodeLine(
            "Je nach Anwendungsfall lohnt es sich, zu wissen in wieviele Kategorien man am Ende einteilen will",
            null, 0, null);
    abschlussText
        .addCodeLine(
            "und wie aehnlich diese sich sind. So waeren beispielsweise 3 Kategorien (also i-2) fuer die Objekte:",
            null, 0, null);
    abschlussText
        .addCodeLine(
            "'Audi' , 'Opel' , 'Spielkonsole' , 'Gamepad' , 'Champignon' , 'Pfifferling'",
            null, 0, null);
    abschlussText
        .addCodeLine(
            "am sinnvollsten, bevor im naechsten Schritt evtl. die Automarken mit Pilzen zusammgenfasst werden.",
            null, 0, null);
    abschlussText.addCodeLine("", null, 0, null);
    abschlussText
        .addCodeLine(
            "Man kann auch je nach Anwendungsfall die Abstaende besser ausjustieren. So macht es einen Unterschied",
            null, 0, null); // 3
    abschlussText
        .addCodeLine(
            "welche Distanzfunktion verwendet wird. In dieser Animation wird die 'Average Cluster Distance' genutzt,",
            null, 0, null); // 4
    abschlussText
        .addCodeLine(
            "welche jeden Punkt innerhalb eines Clusters mit allen Punkten des Nachbarclusters vergleicht und daraus",
            null, 0, null);
    abschlussText.addCodeLine("", null, 0, null);
    // abschlussText.addCodeLine("Komplexitaet:", null, 0, null);
    abschlussText
        .addCodeLine(
            "Die einfache naive Implementierung hat durch stets erneut erforderlichen Distanzvergleiche ",
            null, 0, null);
    abschlussText.addCodeLine("eine Komplexitaet von O(n^3).", null, 0, null);
    abschlussText
        .addCodeLine(
            "Speichert man unveraenderte Clustermitten zwischen (sog. Centroiden) kann man eine Komplexitaet",
            null, 0, null);
    abschlussText.addCodeLine("von von O(n^2) einhalten.", null, 0, null);

    // abschlussText.addCodeLine("Die Anzahl der Iterationen Dieses Beispiel benoetigte z.B. "
    // + overallIterations + " Iterationen mit insgesamt " +
    // overallDistanceChecks +
    // " Distanzvergleichen zu den Clusterzentren (Centroiden)", null, 0, null);
    // // 5
    // abschlussText.addCodeLine("welche wiederum insgesamt " +
    // overallCenterCalculations + " mal neu berechnet wurden.", null, 0, null);
    // // 7

    lang.nextStep("Abschluss");

  }

  private String gibAlleChildCluster(BottomUpCluster cluster) {
    HashMap<String, BottomUpCluster> clusterKinder = cluster.childClusters;
    Set<String> alleKids = cluster.childClusters.keySet();
    String returnstring = new String();

    if (!alleKids.isEmpty()) {
      for (String kindCluster : alleKids) {
        returnstring = returnstring
            + clusterKinder.get(kindCluster).getKnotennummer() + ",";
      }
      returnstring = "{"
          + returnstring.subSequence(0, returnstring.length() - 1) + "}";
    } else {
      returnstring = "{" + (cluster.getKnotennummer() + "}");
    }

    return returnstring;
  }

  /*
   * Schreibt die neuste Iteration in das Fenster und entfernt bei Bedarf die
   * alten Werte. So soll vermieden werden, dass die History unten unanscrollbar
   * aus dem Bild laeuft.
   */
  private void updateHirarchiefenster() {
    boolean eigenesScrolling = true;

    if (eigenesScrolling == true) {
      // Bei 20 Eintraegen neues Feld beschreiben
      if (0 == (overallIterations % 20)) {
        baumText.hide();
        erzeugeBaumText();
      }

      baumText.addCodeLine(zeigeHirarchieZeileDieserIteration(), null, 0, null);
    } else {
      baumText.addCodeLine(zeigeHirarchieZeileDieserIteration(), null, 0, null);
    }
  }

  /*
   * Erstellt den Statustext, der ueber die aktuellen Statistiken des
   * Algorithmus informiert
   */
  private void erzeugeStatusText() {

    TextProperties statusTextCapProps = new TextProperties();
    statusTextCapProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 12));

    TextProperties statusTextProps = new TextProperties();
    statusTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));

    // historyArray.add(zeigeHirarchieZeileDieserIteration());

    statusText = lang.newText(new Offset(500, 470, offsetPunkt,
        AnimalScript.DIRECTION_SW), "Algorithmus Start", "statusText", null,
        statusTextProps);

    Node[] offsetbaum2 = {
        new Offset(000, -20, statusText, AnimalScript.DIRECTION_SW),
        new Offset(400, -20, statusText, AnimalScript.DIRECTION_SW) };

    statusbalken = lang.newPolyline(offsetbaum2, "TextStatustext", null,
        pfeilProps);

    statusTextCap = lang.newText(new Offset(0, -20, statusbalken,
        AnimalScript.DIRECTION_NW), "Statistik:", "statusText", null,
        statusTextCapProps);

  }

  /*
   * Schreibt die Informationen der aktuellen Iteration in den Statustext
   */
  private void updateStatusText() {
    String neuerText = "Iterationen: " + overallIterations
        + " | Distanzvergleiche: " + overallDistanceChecks
        + " | Gebildete Cluster gesamt: " + anzahlVonVektoren;
    statusText.setText(neuerText, null, null);
  }

  /*
   * Die Methode gibt die derzeitige 'Verclusterung' der aktuellen Iteration als
   * String in einer Zeile zuerueck
   */
  private String zeigeHirarchieZeileDieserIteration() {
    // Finde letzten Cluster, der kein Child ist.
    Set<String> clusterKeys = mapOfClusters.keySet();
    String returnstring = "" + overallIterations + ". -> ";

    for (String aktuellerCluster : clusterKeys) {
      if (mapOfClusters.get(aktuellerCluster).retired > overallIterations) {
        returnstring = returnstring
            + gibAlleChildCluster(mapOfClusters.get(aktuellerCluster));
      }
    }

    return returnstring;

  }

  /**
   * Main Methode - auskommentieren fuers kompilieren bitte!
   * 
   * public static void main(String[] args) {
   * 
   * // Lege Instanz von Animal an
   * 
   * // Erzeuge eine KMeans Demo, mit Einleitungstext, dem eigentlichen
   * Algorithmus und einer Endfolie BottomUpHirachicalClustering bottomUpAlgo =
   * new BottomUpHirachicalClustering(); bottomUpAlgo.init();
   * 
   * // AnimationPropertiesContainer aniProps = new
   * AnimationPropertiesContainer(500);
   * 
   * // System.out.println("Main Methode hier!"); File dateizuschreiben = new
   * File("C:\\Users\\Dennis\\Documents\\animalHirarchTest.asu");
   * 
   * try { FileWriter schreibmich = new FileWriter(dateizuschreiben);
   * schreibmich.write(bottomUpAlgo.generate(null, null)); schreibmich.flush();
   * schreibmich.close(); } catch (IOException e) { block e.printStackTrace(); }
   * 
   * 
   * 
   * 
   * 
   * // Aus dem Forum -> mit ausgepacktem Animal laufen lassen: java -cp
   * /opt/Animal animal.main.Animal }
   */

  /*
   * Laesst alle dynamisch generierten Elemetne verschwinden.
   */
  public void hideAllStuffForOutro() {
    Set<String> alleCluster = mapOfClusters.keySet();
    for (String string : alleCluster) {
      if (mapOfClusters.get(string).isStartCluster) {
        mapOfClusters.get(string).getAnimalCircle().hide();
        mapOfClusters.get(string).animalText.hide();
      } else {
        mapOfClusters.get(string).animalText.hide();
      }
      if (!mapOfClusters.get(string).isSubCluster()) {
        versteckeClusterPfeileInsZentrum(mapOfClusters.get(string));
      }
    }

    pseudocodeText.hide();
    pseudoTextText.hide();
    pseudoTextTextUnderline.hide();

    baumText.hide();
    baumTextText.hide();
    baumTextTextUnderscore.hide();

    statusbalken.hide();
    statusText.hide();
    statusTextCap.hide();
  }

  public void init() {
    lang = new AnimalScript(
        "Hierarchical Agglomerative (Bottom Up) Clustering",
        "Dennis Werner, Hamed Samadzai", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // System.out.println("Alle Primitives: " + primitives.keySet());

    /* Diesen Part nur zum testen nutzen */
    // initialize();
    // anzahlVonVektoren = 30;

    // nurEinEinfachesBeispielAnzeigen = false;
    // variablenListeAnzeigen = false;

    /* Diesen Part einkompilieren */
    // initialize();
    anzahlVonVektoren = (Integer) primitives.get("anzahlVektoren");
    zeigeIntro = (Boolean) primitives.get("zeigeIntro");
    // nurEinEinfachesBeispielAnzeigen =
    // (Boolean)primitives.get("nurEinEinfachesBeispielAnzeigen");
    // variablenListeAnzeigen =
    // (Boolean)primitives.get("variablenListeAnzeigen");

    /* Ab hier wieder kann alles wieder bleiben */

    // Bei mehr als 80 Vektoren dreht Animal.jar am Rad!
    if (anzahlVonVektoren > 80) {
      lang.newText(
          new Coordinates(50, 100),
          "Fehler: Es tut uns leid, aber derzeit sind maximal 80 Vektoren/Punkte moeglich. Bitte generieren Sie ein neues Beispiel.",
          "SorryCentroids", null);
    }
    ;

    if (anzahlVonVektoren < 81) {
      /*
       * if(nurEinEinfachesBeispielAnzeigen == true) { runExampleField(); } else
       */
      {
        initializeStuff();
        erzeugeTitel();
        if (zeigeIntro) {
          generateIntro();
        }

        generateRandomField(400, 400, 15);
        erzeugePseudeocodeText();
        erzeugePseudeoTextRahmen();
        // erzeugePseudeocodeText();
        // erzeugePseudeoTextRahmen();

        pseudocodeText.highlight(0);
        erzeugeBaumText();
        erzeugeBaumTextRahmen();
        malePunkte();
        erzeugeStatusText();
        lang.nextStep();

        while (areThereTwoOrMoreClustersLeft()) {
          findNextClusterPair();

          lang.nextStep("Iteration " + overallIterations);
        }

        pseudocodeText.addCodeLine("", null, 0, null);
        pseudocodeText.addCodeLine("Fertig.", null, 0, null);
        pseudocodeText.unhighlight(0);
        pseudocodeText.highlight(4);

        lang.nextStep("Algorithmus Ende");
        hideAllStuffForOutro();
        generateOutro();

        // runRandomField(anzahlVonVektoren, nurEinEinfachesBeispielAnzeigen,
        // 550, 300);
      }
    }
    ;

    return lang.toString();
  }

  public String getName() {
    return "Hierarchical Agglomerative Clustering";
  }

  public String getAlgorithmName() {
    return "Hierarchical Agglomerative Clustering";
  }

  public String getAnimationAuthor() {
    return "Dennis Werner, Hamed Samadzai";
  }

  public String getDescription() {
    return "Hierarchical Agglomerative (Bottom Up) Clustering.\n"
        + "Der Algorithmus erstellt eine auf Distanzvergleichen basierende Hierarchie "
        + "unterschiedlicher Vektoren bzw. Klassen. \n "
        + "Um eine Hierarchie zu erhalten betrachtet man nach Beendigung des Algorithmus "
        + "eine Ebene im gebildeten Hierarchiebaum. \n "
        + "Bei der Distanzberechnung koennen unterschiedliche Berechnungsmethoden in Betracht kommen \n "
        + "Single Link -> Minimale Distanz zwischen Knoten innerhalb zweier Clustern \n "
        + "Complete Link -> Maximale Distanz zwischen Knoten innerhalb zweier Cluster \n "
        + "Average Link -> Durchschnittliche Distanz aller Knoten innerhalb zweier Cluster";
  }

  public String getCodeExample() {
    return "BetrachteJedesElementAlsCluster();"
        + "\n"
        + "\n"
        + "2. Berechne Distanz D zwischen allen Clustern"
        + "\n"
        + "3. Erzeuge neuen Parentcluster aus den zwei am naehesten beieinanderliegenen Clustern"
        + "\n"
        + "4. Berechne Distanz des neuen Clusters zu allen anderen Clustern"
        + "\n" + "5. Existiert mehr als ein Cluster, gehe zu 3.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}