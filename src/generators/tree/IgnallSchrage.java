package generators.tree;

/**
 * 
 * @author Alexander Pass
 *
 */
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.GanttDiagramm;
import generators.helpers.AdvancedText;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class IgnallSchrage implements Generator {
  private Language      lang;
  int                   anzahl_jobs;
  int                   anzahl_masch;
  // private int[][] sMatrix;
  private StringMatrix  matrix          = null;
  private int[][]       table           = null;
  private GanttDiagramm gantt;
  Vector<Integer>       joblist;
  Vector<String>        LBStepElements  = new Vector<String>();
  Vector<AdvancedText>  LBStepSchranken = new Vector<AdvancedText>();
  Vector<String>        tempHide        = new Vector<String>();
  Vector<String>        tempShow        = new Vector<String>();
  Vector<Integer>       joblist_quer;
  // noch zu checken, ob es nicht irgendwo im Zwischenschritt gelöscht wird->
  // sonst wieder in Konstruktor
  SourceCodeProperties  sourceProps;
  SourceCode            source;
  MatrixProperties      matrixProps;
  boolean               firststatus     = true;
  Text                  infoBoxJobList;
  Text                  infoBoxJobListQuer;
  Text                  infoBoxMinLB;
  Text                  infoBoxAktLB;
  Vector<AdvancedText>  F_Text          = new Vector<AdvancedText>();
  Vector<AdvancedText>  FA_Text         = new Vector<AdvancedText>();
  Vector<AdvancedText>  SM_Text         = new Vector<AdvancedText>();
  Vector<AdvancedText>  SA_Text         = new Vector<AdvancedText>();
  PolylineProperties    pfeil           = new PolylineProperties();
  Vector<Polyline>      F_Pfeil         = new Vector<Polyline>();
  AdvancedText          Formel;
  AdvancedText          Formel_Descr;
  AdvancedText          Formel_Rech;
  Vector<String>        loesungen       = new Vector<String>();
  // ----------------------------------
  char                  sum             = '\u2211';
  char                  no_elem         = '\u2209';
  char                  unequal         = '\u2260';
  char                  elem            = '\u2208';
  char                  iota            = '\u03B9';
  char                  oLine           = '\u0305';                  // muss
                                                                      // nach
                                                                      // dem
                                                                      // Buchstaben
                                                                      // stehen
  String                LDach           = "L" + oLine;
  // -----------------------------------

  String                FA_Formel;
  String                SM_Formel;
  String                SA_Formel;

  // String helpMinMaschFA;
  // String helpMinMaschSM;
  String                helpMinMasch;
  String                helpSumMasch;

  public IgnallSchrage() {
  }

  public IgnallSchrage(Language l) {
  }

  public void init() {
    lang = new AnimalScript("IgnallSchrage", "Alexander Pass", 800, 600);
    FA_Formel = "FA$index(" + LDach + "i;) =max{ F$index(Li;), max{ FA$index("
        + LDach + iota + ")+min{ $sum(h=" + iota + ";i-1)t$index(jh;) | j"
        + elem + LDach + "} | " + iota + "=1,..,i-1}} für i=1..m"; // Summenzeichen-Problem
    SM_Formel = "SM$index(i;) =FA$index(" + LDach + "i;)+ $sum(j" + elem
        + LDach + ";)t$index(ji;) +min { $sum(h=i+1;m)t$index(jh;) | j" + elem
        + LDach + "} für i=1..m"; // Summenzeichen-Problem
    SA_Formel = "SA$index(j;) =max{ FA$index(" + LDach + "i;) + $sum(" + iota
        + "=i;m) t$index(j" + iota + ") + $sum(k" + elem + LDach + " k"
        + unequal
        + "j;) min{t$index(ki;) , t$index(km;) } | i=1..m } für j=1..n"; // Summenzeichen-Problem

    lang.setStepMode(true);
  }

  private void generateSourceCode() {
    sourceProps = new SourceCodeProperties();
    sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("serif",
        Font.PLAIN, 12));
    sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.green);
    sourceProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

    pfeil.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

    // SourceCode.Y am besten auf das max(Matrix.SE.Y,Gantt.SE.Y
    source = lang.newSourceCode(new Offset(100, 0, matrix,
        AnimalScript.DIRECTION_NE), "sourceCode", null, sourceProps); // muss 20
                                                                      // von
                                                                      // matrix
                                                                      // sein

    source.addCodeLine("Branch_and_Bound(minLB,L-List,Lquer-List)", null, 0,
        null); // 0
    source.addCodeLine("{", null, 0, null);// 1
    source.addCodeLine(
        "1.Berechne die aktuelle Schranke aktLB für das aktuelle Problem",
        null, 0, null); // 2
    source.addCodeLine("2.IF(aktLB>minLB  || Lquer-List ist leer)", null, 0,
        null); // 3
    source.addCodeLine("THEN: return", null, 1, null); // 4 //in der HilfsBox:
                                                       // in dem restlichen Baum
                                                       // wird es keine bessere
                                                       // Lösung zu finden sein
    source.addCodeLine("3.IF(Lquer-List ist leer)", null, 0, null); // 5
    source.addCodeLine("THEN: Eine Lösung gefunden", null, 2, null); // 6
    source.addCodeLine(" ELSE IF(Lquer-List hat genau 2 Elemente)", null, 1,
        null); // 7
    source.addCodeLine("THEN:", null, 2, null); // 8
    source.addCodeLine("füge beide Elemente von Lquer-List in L-List ein ",
        null, 3, null); // 9
    source.addCodeLine(
        "vertausche beide Elemente und füge diese in L-List ein ", null, 3,
        null); // 10
    source.addCodeLine("ELSE", null, 1, null); // 11
    source.addCodeLine("WHILE(Lquer-List nicht leer)", null, 2, null);// 12
    source.addCodeLine("{", null, 2, null);// 13
    source.addCodeLine(
        "entnehme nächsten Job aus Lquer-List und füge diesen in L-List ein",
        null, 3, null);// 14

    source.addCodeLine("}", null, 3, null);
    source.addCodeLine("}", null, 0, null);
  }

  private void generateBoxes() {

    RectProperties infoBoxProp = new RectProperties();
    infoBoxProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    infoBoxProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    infoBoxProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    Offset infoBoxNW = new Offset(400, 10, "sourceCode",
        AnimalScript.DIRECTION_NW);
    Coordinates infoBoxSE = new Coordinates(800, 170);
    lang.newRect(infoBoxNW, infoBoxSE, "infoBox", null, infoBoxProp);
    lang.newText(new Offset(10, 0, "infoBox", AnimalScript.DIRECTION_NW),
        "Info-Box", "infoBox-Titel", null);
    infoBoxJobList = lang.newText(new Offset(-5, 10, "infoBox-Titel",
        AnimalScript.DIRECTION_SW), "", "infoBoxJobList", null);
    infoBoxJobListQuer = lang.newText(new Offset(0, 5, "infoBoxJobList",
        AnimalScript.DIRECTION_SW), "", "infoBoxJobListQuer", null);
    infoBoxMinLB = lang.newText(new Offset(0, 5, "infoBoxJobListQuer",
        AnimalScript.DIRECTION_SW), "", "infoBoxMinLB", null);
    infoBoxAktLB = lang.newText(new Offset(0, 5, "infoBoxMinLB",
        AnimalScript.DIRECTION_SW), "", "infoBoxAktLB", null);

    LBStepElements.add("LB-Step");
    LBStepElements.add("LB-Step-Titel");
    LBStepElements.add("helpBoxBig");
    RectProperties helpBoxProp = new RectProperties();
    helpBoxProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    helpBoxProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    helpBoxProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);

    lang.newRect(new Offset(0, 0, "infoBox", AnimalScript.DIRECTION_SE),
        new Offset(0, 0, "sourceCode", AnimalScript.DIRECTION_SW), "help_rec2",
        null);
    tempHide.add("help_rec2"); // x-Coord von infoBox und y- Coord von
                               // source-Box

    lang.newRect(new Offset(0, 5, "infoBox", AnimalScript.DIRECTION_SW),
        new Offset(0, 0, "help_rec2", AnimalScript.DIRECTION_SE),
        "helpBoxSmall", null, helpBoxProp);
    tempHide.add("helpBoxSmall");

    RectProperties descrBoxProp = new RectProperties();
    descrBoxProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    descrBoxProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
    descrBoxProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    // Erzeugung der LB-Step- Box siehe unten (wegen der SE-Koordinate)
    lang.newText(new Offset(10, 15, "sourceCode", AnimalScript.DIRECTION_NW),
        "aktLB-Berechnung:", "LB-Step-Titel", null);
    /*
     * F_Text.add(lang.newText(new
     * Offset(10,5,"LB-Step-Titel",AnimalScript.DIRECTION_SW), "F_1=", "F_1",
     * null)); FA_Text.add(lang.newText(new
     * Offset((infoBoxNW.getX()-30)/4,5,"LB-Step-Titel"
     * ,AnimalScript.DIRECTION_SW), "FA_1=", "FA_1", null));
     * SM_Text.add(lang.newText(new
     * Offset((infoBoxNW.getX()-30)/2,5,"LB-Step-Titel"
     * ,AnimalScript.DIRECTION_SW), "SM_1=", "SM_1", null));
     * SA_Text.add(lang.newText(new
     * Offset((infoBoxNW.getX()-30)*3/4,5,"LB-Step-Titel"
     * ,AnimalScript.DIRECTION_SW), "SA_1=", "SA_1", null));
     */

    TextProperties testp = new TextProperties();
    testp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    testp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("serif",
        Font.PLAIN, 14));
    // advancedText test=new advancedText(lang, new
    // Offset(0,10,"title_rect",AnimalScript.DIRECTION_SW), input, testp,null,
    // null);

    F_Text.add(new AdvancedText(lang, new Offset(10, 5, "LB-Step-Titel",
        AnimalScript.DIRECTION_SW), "F$index(L1;)=", testp, null, null));
    FA_Text.add(new AdvancedText(lang, new Offset((infoBoxNW.getX() - 30) / 4,
        5, "LB-Step-Titel", AnimalScript.DIRECTION_SW), "FA$index(" + LDach
        + "1;)=", testp, null, null));
    SM_Text.add(new AdvancedText(lang, new Offset((infoBoxNW.getX() - 30) / 2,
        5, "LB-Step-Titel", AnimalScript.DIRECTION_SW), "SM$index(1;)=", testp,
        null, null));
    SA_Text.add(new AdvancedText(lang, new Offset(
        (infoBoxNW.getX() - 30) * 3 / 4, 5, "LB-Step-Titel",
        AnimalScript.DIRECTION_SW), "SA$index(1;)=", testp, null, null));

    F_Text.lastElement().hide();
    FA_Text.lastElement().hide();
    SM_Text.lastElement().hide();
    SA_Text.lastElement().hide();
    /*
     * LBStepSchranken.add( "F_1"); LBStepSchranken.add( "FA_1");
     * LBStepSchranken.add( "SM_1"); LBStepSchranken.add( "SA_1");
     */
    for (int i = 2; i <= table[1].length; i++) {
      F_Text.add(new AdvancedText(lang, new Offset(F_Text.lastElement().getSW()
          .getX(), F_Text.lastElement().getSW().getY(), F_Text.lastElement()
          .getSW().getBaseID(), F_Text.lastElement().getSW().getDirection()),
          "F$index(L" + i + ";)=", testp, null, null));
      FA_Text
          .add(new AdvancedText(lang, new Offset(FA_Text.lastElement().getSW()
              .getX(), FA_Text.lastElement().getSW().getY(), FA_Text
              .lastElement().getSW().getBaseID(), FA_Text.lastElement().getSW()
              .getDirection()), "FA$index(" + LDach + i + ";)=", testp, null,
              null));
      SM_Text.add(new AdvancedText(lang, new Offset(SM_Text.lastElement()
          .getSW().getX(), SM_Text.lastElement().getSW().getY(), SM_Text
          .lastElement().getSW().getBaseID(), SM_Text.lastElement().getSW()
          .getDirection()), "SM$index(" + i + ";)=", testp, null, null));

      F_Text.lastElement().hide();
      FA_Text.lastElement().hide();
      SM_Text.lastElement().hide();
      // LBStepSchranken.add( "F_"+i);
      // LBStepSchranken.add( "FA_"+i);
      // LBStepSchranken.add( "SM_"+i);
    }
    for (int i = 2; i <= table.length; i++) {
      // SA_Text.add(lang.newText(new
      // Offset(0,5,"SA_"+(i-1),AnimalScript.DIRECTION_SW), "SA_"+i+"=",
      // "SA_"+i, null));
      SA_Text.add(new AdvancedText(lang, new Offset(SA_Text.lastElement()
          .getSW().getX(), SA_Text.lastElement().getSW().getY(), SA_Text
          .lastElement().getSW().getBaseID(), SA_Text.lastElement().getSW()
          .getDirection()), "SA$index(" + i + ";)=", testp, null, null));

      SA_Text.lastElement().hide();
      // LBStepSchranken.add( "SA_"+i);
    }

    lang.newRect(new Offset(-5, 0, "infoBox", AnimalScript.DIRECTION_NW),
        SA_Text.lastElement().getSE(), "help_rec3", null);
    tempHide.add("help_rec3");

    lang.newRect(new Offset(10, 10, "sourceCode", AnimalScript.DIRECTION_NW),
        new Offset(0, 0, "help_rec3", AnimalScript.DIRECTION_SE), "LB-Step",
        null, descrBoxProp);

    lang.newRect(new Offset(0, 5, "LB-Step", AnimalScript.DIRECTION_SW),
        new Offset(0, 0, "helpBoxSmall", AnimalScript.DIRECTION_SE),
        "helpBoxBig", null, helpBoxProp);
    // Formel=lang.newText(new
    // Offset(5,5,"helpBoxBig",AnimalScript.DIRECTION_NW), "", "Formel", null);
    Formel = new AdvancedText(lang, new Offset(5, 5, "helpBoxBig",
        AnimalScript.DIRECTION_NW), "init$sum(a;b)", testp, null, null);
    Formel_Descr = new AdvancedText(lang, Formel.getSW(), "inti$index(a;b)",
        testp, null, null);
    // Formel_Rech=lang.newText(new
    // Offset(0,5,"Formel_Descr",AnimalScript.DIRECTION_SW), "", "Formel_Rech",
    // null);
    Formel_Rech = new AdvancedText(lang, Formel_Descr.getSW(), "init$sum(a;b)",
        testp, null, null);

    Formel.hide();
    Formel_Rech.hide();
    // tempHide.add("Formel");
    Formel_Descr.hide();
    // tempHide.add("Formel_Rech");

    tempHide.addAll(LBStepElements);
    // tempHide.addAll(LBStepSchranken);

  }

  private void nextStep() {
    if (!tempHide.isEmpty()) {
      lang.hideInThisStep.addAll(tempHide);
      tempHide.clear();
    }
    if (!tempShow.isEmpty()) {
      lang.showInThisStep.addAll(tempShow);
      tempShow.clear();
    }
    lang.nextStep();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    init();
    table = (int[][]) primitives.get("Bearbeitungszeiten");
    anzahl_jobs = table.length;
    anzahl_masch = table[1].length;
    matrixProps = (MatrixProperties) props.getPropertiesByName("matrix");

    joblist = new Vector<Integer>();
    joblist_quer = new Vector<Integer>();

    addTitle();
    nextStep();
    addDescriptionStep();
    nextStep();
    // Vector<String> toHide=new Vector<String>();
    tempHide.add("descr");

    printTable();
    nextStep();
    generateSourceCode();
    // aktuelles Problem-Box

    lang.newRect(new Offset(0, 12, matrix, AnimalScript.DIRECTION_SE),
        new Offset(0, 0, "sourceCode", AnimalScript.DIRECTION_SW), "help_rec",
        null);
    tempHide.add("help_rec"); // help_rec ist nur dafür da, die südlichste
                              // Koordinate von matrix oder sourceCode zu
                              // bestimmen

    gantt = new GanttDiagramm("PF", new Offset(-30 - 28
        * (matrix.getNrCols() - 1), 20, "help_rec", AnimalScript.DIRECTION_SW),
        100, 400, table, lang);
    generateBoxes();

    for (int i = 1; i <= table.length; i++)
      joblist_quer.add(i);

    source.highlight(0);
    nextStep();
    branch_and_bound(Integer.MAX_VALUE, joblist, joblist_quer);
    // toHide Add ALL
    // Lösungsbox
    // jede Lösung aufzählen

    return lang.toString();
  }

  private void addTitle() {
    // hier am Besten: Rect mit Text
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.pink);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.pink);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("serif", Font.BOLD,
        16));

    lang.newText(new Coordinates(350, 20), "Ignall-Schrage Algorithmus",
        "titel", null, tp);
    lang.newRect(new Offset(-20, 0, "titel", AnimalScript.DIRECTION_NW),
        new Offset(20, 0, "titel", AnimalScript.DIRECTION_SE), "title_rect",
        null, rp);

  }

  private void addDescriptionStep() {
    SourceCode text;
    SourceCodeProperties descProps = new SourceCodeProperties();
    descProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("monospaced",
        Font.PLAIN, 12));
    descProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.green);

    text = lang.newSourceCode(new Offset(-150, 50, "title_rect",
        AnimalScript.DIRECTION_SW), "descr", null, descProps);
    text.addCodeLine(
        "Ignall-Schrage Algorithmus ist eine auf dem Branch & Bound basierte Methode,das exakte Lösung",
        null, 0, null);
    text.addCodeLine(
        "(je nach Implementierung auch mehrere Lösungen) von Permutations-Flow-Shop-Problemen mit dem Ziel,",
        null, 0, null);
    text.addCodeLine(
        "die Zykluszeit zu minimieren[PF| |Z],liefert.Ebenso werden gleichzeitig auch zulässige Lösungen",
        null, 0, null);
    text.addCodeLine(
        "für allgemeine Flow-Shop-Probleme[F| |Z] ermittelt. Bei einem Flow-Shop-Problem sind n Aufträge",
        null, 0, null);
    text.addCodeLine(
        "auf m Maschinen zu bearbeiten. Jeder Auftrag besteht aus genau m Arbeitsgängen, die die Maschinen",
        null, 0, null);
    text.addCodeLine(
        "jeweils in der selben Maschienfolge zu durchlaufen haben.Eine Besonderheit der",
        null, 0, null);
    text.addCodeLine(
        "Permutations-Flow-Shop-Probleme ist die Bedingung, dass die Auftragsfolge auf allen Maschinen gleich",
        null, 0, null);
    text.addCodeLine(
        "sein muss. Diese Probleme sind in der Regel einfacher zu lösen.",
        null, 0, null);
    text.addCodeLine("", null, 0, null);
    text.addCodeLine("Branch & Bound Methode:", null, 0, null);
    text.addCodeLine(
        "Es eine in dem Bereich Operation Research eine sehr häufig verwendete Methode, um für",
        null, 0, null);
    text.addCodeLine("ein Optimierungsproblem beste Lösung zu finden.", null,
        0, null);
    text.addCodeLine(
        "In dem Branch-Schritt wird das jeweilige Teilpromlem in mehrere Teilprobleme aufgeteilt. Durch die",
        null, 0, null);
    text.addCodeLine(
        "rekursive Ausführung ensteht eine Baumstruktur der Teilprobleme.",
        null, 0, null);
    text.addCodeLine(
        "Um relativ früh eine richtige Lösung zu erkennen und nicht alle Teilprobleme zu untersuchen ",
        null, 0, null);
    text.addCodeLine(
        "(Laufzeitoptimierung), wird in dem Bound-Schritt eine Schranke berechnet, anhang derer ein Teilbaum ",
        null, 0, null);
    text.addCodeLine(
        "von der Lösungsmenge abgeschnitten wird oder nicht. In einem Minimierungsproblem iefert die aktuell ",
        null, 0, null);
    text.addCodeLine(
        "beste zulässige Lösung die aktuelle obere Schranke UP(UpperBound) für den optimalen Zielfunktionswert.",
        null, 0, null);
    text.addCodeLine(
        "Alle Teilprobleme, deren untere Schranke LB(LowerBound) größer als aktuelle UP, können verworfen werden.",
        null, 0, null);

  }

  @SuppressWarnings("unchecked")
  private void branch_and_bound(int minLB, Vector<Integer> joblist,
      Vector<Integer> joblist_quer) {
    source.unhighlight(12);
    source.unhighlight(14);

    setInfoBox(minLB, joblist, joblist_quer);

    int aktLB = LB(joblist, joblist_quer);
    updateInfoBox(aktLB);
    nextStep();
    // System.out.println("aktLB:"+aktLB);
    if (firststatus) {
      gantt.setMaxLB(aktLB);
      firststatus = false;
    }
    if (aktLB > minLB) {
      source.toggleHighlight(3, 4);
      nextStep();
      source.unhighlight(4);
      return;
    } else if (joblist_quer.isEmpty()) {
      loesungen.add(printjob(joblist));
      nextStep();
      return;
    } else if (joblist_quer.size() == 2) {
      Vector<Integer> joblist_local2 = new Vector<Integer>();
      Vector<Integer> joblist_quer_local2 = new Vector<Integer>();

      joblist_local2 = (Vector<Integer>) joblist.clone();
      joblist_quer_local2 = (Vector<Integer>) joblist_quer.clone();

      int firstjob = joblist_quer_local2.firstElement();
      int lastjob = joblist_quer_local2.lastElement();
      joblist_quer_local2.clear();
      joblist_local2.add(firstjob);
      joblist_local2.add(lastjob);
      // System.out.println("aktLB:"+aktLB+" joblist:"+printjob(joblist_local2)+" joblist_quer:"+printjob(joblist_quer_local2));
      source.highlight(9);
      nextStep();
      source.unhighlight(9);
      gantt.calculateJobs(joblist_local2);
      gantt.showGantt();
      source.highlight(0);

      branch_and_bound(aktLB, joblist_local2, joblist_quer_local2);

      joblist_local2.removeElement(firstjob); //
      joblist_local2.add(firstjob);
      // System.out.println("aktLB:"+aktLB+" joblist:"+printjob(joblist_local2)+" joblist_quer:"+printjob(joblist_quer_local2));
      source.highlight(10);
      nextStep();
      source.unhighlight(10);

      gantt.calculateJobs(joblist_local2);
      gantt.showGantt();
      source.highlight(0);

      branch_and_bound(aktLB, joblist_local2, joblist_quer_local2);
    } else {

      Vector<Integer> to_visit = new Vector<Integer>();
      to_visit = (Vector<Integer>) joblist_quer.clone(); // VERWEIS oder
      Vector<Integer> joblist_local = new Vector<Integer>();
      Vector<Integer> joblist_quer_local = new Vector<Integer>();
      joblist_local = (Vector<Integer>) joblist.clone();
      joblist_quer_local = (Vector<Integer>) joblist_quer.clone();
      int job2visit;
      while (!to_visit.isEmpty()) {
        source.highlight(12);
        source.highlight(14);

        job2visit = to_visit.elementAt(0);
        joblist_local.add(job2visit); // joblist: Elemente, die besucht wurden
        joblist_quer_local.removeElement(job2visit); // noch zu besuchen .
        nextStep();

        gantt.calculateJobs(joblist_local);
        gantt.showGantt();
        source.highlight(0);

        branch_and_bound(aktLB, joblist_local, joblist_quer_local);
        source.unhighlight(0);

        joblist_local.removeElement(job2visit); // Wieder den Zustand vor der
                                                // Rekursion einnehmen, damit
                                                // nächster Job in joblist_quer
                                                // abgearbeitet werden kann
        joblist_quer_local.add(job2visit);
        to_visit.removeElement(job2visit); // Weil der Knoten schon besucht
                                           // wurde, diesen aus to_visit
                                           // entfernen
      }
    }

  }

  private void updateInfoBox(int aktLB) {
    // hier muss noch der Sprung von descr-rec aktiviert werden. Erst danach
    // descr ausblenden lassen

    infoBoxAktLB.setText("aktLB:" + aktLB, null, null);

  }

  private void setInfoBox(int minLB, Vector<Integer> info_joblist,
      Vector<Integer> info_joblist_quer) {

    infoBoxJobList.setText("List:" + printjob(info_joblist), null, null);
    infoBoxJobListQuer.setText("List_quer:" + printjob(info_joblist_quer),
        null, null);

    infoBoxMinLB.setText("MinLB:" + minLB, null, null);
    infoBoxAktLB.setText("", null, null);

  }

  private String printjob(Vector<Integer> joblist) {
    String out = "[";
    for (int i = 0; i < joblist.size(); i++)
      out = out + joblist.get(i) + ",";
    out = out + "]";
    out = out.replaceAll(",]", "]");
    return out;

  }

  private int LB(Vector<Integer> joblist, Vector<Integer> joblist_quer) {
    Node[] pfeil_coord = new Node[2];

    int[] F = new int[anzahl_masch];// Frühester Endzeitpunkt nach Abarbeitung
                                    // der Aufträge aus joblist (für jede
                                    // Maschine)
    int[] FA = new int[anzahl_masch]; // Frühester Beginn der Abarbeitung der
                                      // Jobs aus joblist_quer
    int[] SM = new int[anzahl_masch];
    int[] SA = new int[anzahl_jobs];
    Vector<Integer> for_max = new Vector<Integer>();
    int[][] FAmatrix;// Hilfsmatrix der frühesten Anfangszeitpunkte
    int[][] FEmatrix;// Hilfsmatrix der frühesten Endzeitpunkte

    source.toggleHighlight(0, 2);

    tempShow.addAll(LBStepElements);
    tempHide.add("helpBoxSmall");
    nextStep();

    // F-Bestimmung /ähnlich wie in Gantt
    Formel_Descr
        .set("F$index(Li;): effektiver Fertigstellungszeitpunkt bereits eingeplanter Aufträge aus L-Liste für jede Maschine i");
    // tempShow.add("Formel_Descr");
    FAmatrix = new int[anzahl_jobs + 1][anzahl_masch + 1];
    FEmatrix = new int[anzahl_jobs + 1][anzahl_masch + 1];
    int currentJob = 0;
    if (!joblist.isEmpty()) {
      currentJob = joblist.get(0);
      int lastJob = 0;
      for (int j = 1; j <= joblist.size(); j++) {
        for (int i = 1; i < FAmatrix[1].length; i++) {
          FAmatrix[currentJob][i] = Math.max(FEmatrix[currentJob][i - 1],
              FEmatrix[lastJob][i]);
          FEmatrix[currentJob][i] = FAmatrix[currentJob][i]
              + table[currentJob - 1][i - 1];
        }
        lastJob = currentJob;
        if (j != joblist.size())
          currentJob = joblist.get(j);
      }

      for (int i = 1; i < FEmatrix[1].length; i++) {
        F[i - 1] = FEmatrix[joblist.lastElement()][i];

        Formel_Rech.set("F$index(L" + (i) + ";)=" + F[i - 1]);
        pfeil_coord[0] = new Offset(0, 0, gantt.JobRect.get(gantt.JobRect
            .size() - 1 - anzahl_masch + i), AnimalScript.DIRECTION_E);
        pfeil_coord[1] = new Offset(0, 0, Formel_Rech.getW().getBaseID(),
            Formel_Rech.getW().getDirection());
        F_Pfeil.add(lang.newPolyline(pfeil_coord, "F_pfeil_" + i, null, pfeil));
        // MsTiming ausprobieren

        F_Text.get(i - 1).set("F$index(L" + (i) + ";)=" + F[i - 1]);
        nextStep();
        tempHide.add(F_Pfeil.lastElement().getName());
        Formel_Rech.hide();
      }
    } else {
      Formel_Rech
          .set("Weil es noch keine Jobs eingeplant sind, ist Fertigstellungszeitpunkt auf allen Maschinen gleich 0 ");

      for (int i = 0; i < anzahl_masch; i++) {
        F[i] = 0;
        // überlegen, wie msTiming sich auswirkt
        F_Text.get(i).set("F$index(L" + (i + 1) + ";)=" + F[i]);
      }
      nextStep();
      Formel_Rech.hide();
    }

    // FA-Bestimmung
    Formel.set(FA_Formel);
    Formel_Descr
        .set("FA$index("
            + LDach
            + "i;): Frühester Bearbeitungszeitpunkt der noch nciht eingeplanten Aufträge aus "
            + LDach + "-Liste auf jeder Maschine i");

    FA[0] = F[0];
    String testFA = "FA$index(" + LDach + "1;)= F$index(L1;)";
    Formel_Rech.set(testFA);
    // System.out.println(testFA);
    nextStep();
    FA_Text.firstElement().set("FA$index(" + LDach + "1;)=" + FA[0]);

    nextStep();

    for (int i = 1; i < FA.length; i++) {
      for_max.add(F[i]);
      testFA = "FA$index(" + LDach + (i + 1) + ";)=max{ F$index(L" + (i + 1)
          + ";),";
      for (int j = 0; j < i; j++) {

        for_max.add(FA[j] + minMaschFA(j, i, joblist_quer));// Überlegung:
                                                            // testFA in min
                                                            // maschfa zu
                                                            // übergeben und
                                                            // dort nextSteps
                                                            // ausrufen
        testFA = testFA + "FA$index(" + LDach + (j + 1) + ";)+" + helpMinMasch;
        if ((i - j) > 1)
          testFA = testFA + ",";
      }
      FA[i] = rekmax(for_max);
      for_max.clear();
      Formel_Rech.set(testFA + "}");
      // System.out.println("L:"+printjob(joblist)+"L_quer:"+printjob(joblist_quer)+testFA+"}");
      nextStep();
      FA_Text.get(i).set("FA$index(" + LDach + (i + 1) + ";)=" + FA[i]);

      nextStep();

    }

    // SM-Bestimmung
    Formel.set(SM_Formel);
    Formel_Descr
        .set("SM$index(i;): Maschinenorientierte Schranken für jede Maschine i");
    String testSM = "";

    for (int i = 0; i < SM.length; i++) {
      SM[i] = FA[i] + sumMasch(i, joblist_quer)
          + minMasch(i + 1, SM.length - 1, joblist_quer);

      testSM = "SM$index(" + (i + 1) + ";)=FA$index(" + LDach + (i + 1) + ";)"
          + helpSumMasch + helpMinMasch;
      Formel_Rech.set(testSM);
      nextStep();
      SM_Text.get(i).set("SM$index(" + (i + 1) + ";)=" + SM[i]);
      nextStep();
    }

    // SA-Bestimmung
    Formel.set(SA_Formel);
    Formel_Descr
        .set("SA$index(j;): Auftragsorientierte Schranken für jeden Auftrag j");
    for (int i = 0; i < joblist.size(); i++) { // da SAs nur für jobs aus
                                               // Liste_quer bestimmt werden
                                               // sollten, setzte für
                                               // jobliste-jobs alles auf 0 bzw.
                                               // auf Integer.min
      SA[joblist.get(i) - 1] = 0;
    }
    // jetzt für querliste
    int[] for_max_a = new int[table[1].length];
    String testSA;
    for (int j = 0; j < joblist_quer.size(); j++) {
      testSA = "SA$index(" + joblist_quer.get(j) + ";)=max {";
      for (int i = 0; i < table[1].length; i++) {
        for_max_a[i] = FA[i]
            + sumMasch(i, table[1].length - 1, joblist_quer.get(j) - 1)
            + minjob(i, table[1].length - 1, joblist_quer);
        if ((joblist_quer.size() - j) > 2)
          testSA = testSA + "FA$index(" + LDach + (i + 1) + ";)+"
              + helpSumMasch + "+" + helpMinMasch + ",";
        else
          testSA = testSA + "FA$index(" + LDach + (i + 1) + ";)+"
              + helpSumMasch + "+" + helpMinMasch;
      }
      testSA = testSA + "}";
      SA[joblist_quer.get(j) - 1] = rekmax(for_max_a);
      Formel_Rech.set(testSA);
      nextStep();
      SA_Text.get(joblist_quer.get(j) - 1).set(
          "SA$index(" + joblist_quer.get(j) + ";)="
              + SA[joblist_quer.get(j) - 1]);
      nextStep();
    }

    source.toggleHighlight(2, 3);
    tempHide.addAll(LBStepElements);
    tempShow.add("helpBoxSmall");
    // tempHide.add("Formel_Descr");
    // tempHide.add("Formel");
    // tempHide.add("Formel_Rech");
    // tempHide.addAll(LBStepSchranken);
    Formel.hide();
    Formel_Descr.hide();
    Formel_Rech.hide();
    for (int i = 0; i < table[1].length; i++) {
      F_Text.get(i).hide();
      FA_Text.get(i).hide();
      SM_Text.get(i).hide();
    }
    for (int i = 0; i < table.length; i++) {
      SA_Text.get(i).hide();
    }

    return Math.max(rekmax(SM), rekmax(SA));
  }

  private int minMaschFA(int fromM, int untilM, Vector<Integer> joblist_quer) {
    if (joblist_quer.isEmpty())
      return 0;
    Vector<Integer> for_min = new Vector<Integer>();
    helpMinMasch = "min{";
    // schnellster job auf masch oder summer der masch 0-masch
    for (int j = 0; j < joblist_quer.size(); j++) {
      for_min.add(0);
      for (int i = fromM; i < untilM; i++) {
        for_min.set(j, for_min.get(j) + table[joblist_quer.get(j) - 1][i]);
        String plus = "";
        if ((untilM - i) > 1)
          plus = "+";
        helpMinMasch = helpMinMasch + "t$index(" + (joblist_quer.get(j))
            + (i + 1) + ";)" + plus;

      }
      if ((joblist_quer.size() - j) > 1) {
        helpMinMasch = helpMinMasch + ",";
      }
    }
    helpMinMasch = helpMinMasch + "}";
    // System.out.println("fromM="+fromM+" untilM:"+untilM+" rekmin:"+rekmin(for_min));
    return rekmin(for_min);
  }

  private int minjob(int fromM, int untilM, Vector<Integer> joblist_quer) {
    if (joblist_quer.isEmpty())
      return 0;
    int min = 0;
    helpMinMasch = "";
    for (int j = 0; j < joblist_quer.size(); j++) {
      min = min
          + Math.min(table[joblist_quer.get(j) - 1][fromM],
              table[joblist_quer.get(j) - 1][untilM]);
      helpMinMasch = helpMinMasch + "+min{t$index(" + joblist_quer.get(j)
          + fromM + ";),t$index(" + joblist_quer.get(j) + untilM + ";)}";
    }
    helpMinMasch = helpMinMasch + "";
    return min;
  }

  // private void printarr(String id, int[] arr) {
  // String id2 = id;
  // for (int i = 0; i < arr.length; i++) {
  // id2 = id2 + "[" + i + "]=" + arr[i];
  // }
  // System.out.println(id2);
  // }

  private int sumMasch(int masch, Vector<Integer> joblist_quer) {
    if (joblist_quer.isEmpty())
      return 0;
    int sum = 0;
    helpSumMasch = "+{";
    for (int j = 0; j < joblist_quer.size(); j++) {
      sum = sum + table[joblist_quer.get(j) - 1][masch];
      if (j == 0)
        helpSumMasch = helpSumMasch + "t$index(" + joblist_quer.get(j)
            + (masch + 1) + ";)";
      else
        helpSumMasch = helpSumMasch + "+t$index(" + joblist_quer.get(j)
            + (masch + 1) + ";)";
    }
    helpSumMasch = helpSumMasch + "}";
    return sum;
  }

  private int sumMasch(int fromM, int untilM, int job) {
    int sum = 0;
    helpSumMasch = "{";
    for (int i = fromM; i < untilM; i++) {
      sum = sum + table[job][i];
      if (i == fromM)
        helpSumMasch = helpSumMasch + "t$index(" + job + (i + 1) + ";)";
      else
        helpSumMasch = helpSumMasch + "+t$index(" + job + (i + 1) + ";)";
    }
    helpSumMasch = helpSumMasch + "}";
    return sum;
  }

  private int minMasch(int fromM, int untilM, Vector<Integer> joblist_quer) {
    if (fromM > untilM)
      return 0;
    Vector<Integer> for_min = new Vector<Integer>();
    helpMinMasch = "+{";
    for (int j = 0; j < joblist_quer.size(); j++) {
      for_min.add(0);
      for (int i = fromM; i <= untilM; i++) {
        for_min.set(j, for_min.get(j) + table[joblist_quer.get(j) - 1][i]);
        if (j == 0)
          helpMinMasch = helpMinMasch + "t$index(" + joblist_quer.get(j) + (i)
              + ";)";
        else
          helpMinMasch = helpMinMasch + "+t$index(" + joblist_quer.get(j) + (i)
              + ";)";

      }
    }
    helpMinMasch = helpMinMasch + "}";
    return rekmin(for_min);
  }

  private int rekmin(Vector<Integer> for_min) {
    if (for_min.isEmpty())
      return 0;
    int min = Integer.MAX_VALUE;
    for (int i = 0; i < for_min.size(); i++) {
      min = Math.min(min, for_min.get(i));
    }
    return min;
  }

  private int rekmax(Vector<Integer> for_max) {
    if (for_max.isEmpty())
      return 0;
    int max = 0;
    for (int i = 0; i < for_max.size(); i++) {
      max = Math.max(max, for_max.get(i));
    }
    return max;
  }

  private int rekmax(int[] arr) {
    int max = 0;
    for (int i = 0; i < arr.length; i++) {
      max = Math.max(max, arr[i]);
    }
    return max;
  }

  // private int rekmin(int[] arr) {
  // int min = Integer.MAX_VALUE;
  // for (int i = 0; i < arr.length; i++) {
  // min = Math.min(min, arr[i]);
  // }
  // return min;
  // }

  private void printTable() {
    String[][] strTable = new String[anzahl_jobs + 1][table[1].length + 1];
    strTable[0][0] = "J" + "\\" + "M";
    for (int i = 1; i < strTable.length; i++)
      strTable[i][0] = "J_" + i;
    for (int i = 1; i < strTable[1].length; i++)
      strTable[0][i] = "M_" + i;

    for (int i = 1; i < strTable.length; i++) {
      for (int j = 1; j < strTable[1].length; j++) {
        strTable[i][j] = "" + table[i - 1][j - 1];
      }
    }
    matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");// scheint
                                                                           // nicht
                                                                           // zu
                                                                           // funktionieren
    matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");// scheint
                                                                          // nicht
                                                                          // zu
                                                                          // funktionieren
    matrix = lang.newStringMatrix(new Coordinates(10, 50), strTable,
        "Bearbeitungszeiten", null, matrixProps);
  }

  public String getName() {
    return "Ignall und Schrage";
  }

  public String getAlgorithmName() {
    return "IgnallSchrage";
  }

  public String getAnimationAuthor() {
    return "Alexander Pass";
  }

  public String getDescription() {
    return "Exaktes Verfahren zur Minimierung der Zykluszeit "
        + "Exaktes Lösen für Permutations-Flow-Shop-Problemen [PF| |Z]. Gleichzeitig zulässige Lösung für "
        + "allgemeine Flow-Shop-Probleme [F| |Z]";
  }

  public String getCodeExample() {
    return "genauer Algorithm kommt später";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}