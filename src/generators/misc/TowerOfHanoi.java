package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.Disk;
import generators.helpers.DiskProperties;
import generators.helpers.Tower;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class TowerOfHanoi implements Generator {

  private Language             lang;

  private SourceCode           sc;
  private SourceCodeProperties scProps;

  private Vector<Tower>        towers;

  private Vector<Disk>         disks;

  private DiskProperties       dps;

  private Timing               defaultTiming;

  private Text                 moveCtr;

  private Text                 validMove;
  private Text                 invalidMove;

  Color                        towerColor;

  int                          nrDisks;

  public TowerOfHanoi() {
    // System.err.println("Welcome!");
  }

  private void internInit() {
    defaultTiming = new TicksTiming(90);
  }

  private void showLabels() {

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    lang.newRect(new Coordinates(10, 10), new Coordinates(270, 55), "box",
        null, rectProps);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 30));
    lang.newText(new Coordinates(20, 30), "Towers Of Hanoi", "header", null,
        textProps);

    Text moveLbl = lang.newText(new Coordinates(640, 30), "Moves: ", "moves",
        null, textProps);

    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    moveCtr = lang.newText(
        new Offset(10, 0, moveLbl, AnimalScript.DIRECTION_NE), "0", "#moves",
        null, textProps);

    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);
    validMove = lang.newText(new Coordinates(750, 150), "Valid Destination",
        "vMove", null, textProps);
    validMove.hide();

    invalidMove = lang.newText(new Coordinates(720, 150),
        "Invalid Destination", "ivMove", null, textProps);
    invalidMove.hide();
  }

  /**
	 * 
	 */
  private void showTowers() {
    // create dummy tower
    new Tower(lang, "", -1, towerColor);

    towers.addElement(new Tower(lang, "A", 0, towerColor));
    towers.addElement(new Tower(lang, "B", 1, towerColor));
    towers.addElement(new Tower(lang, "C", 2, towerColor));

    for (int i = 0; i < nrDisks; i++)
      towers.get(0).push(nrDisks - i);
  }

  /**
	 * 
	 */
  private void showDisks() {
    for (int i = 0; i < nrDisks; i++)
      disks.addElement(new Disk(lang, dps, i + 1));

    for (int i = 0; i < nrDisks; i++)
      disks.get(i).moveBy(0, -15 * (nrDisks - (i + 1)), new TicksTiming(15));

  }

  /**
	 * 
	 */
  private void showSourceCode() {
    sc = lang.newSourceCode(new Coordinates(20, 200), "sourceCode", null,
        scProps);

    sc.addCodeLine("public void solveTowersOfHanoi(int nrDisks) {", null, 0,
        null); // 0
    sc.addCodeLine("lastMovedDisk = null;", null, 2, null); // 1
    sc.addCodeLine("allTowers = {TowerA, TowerB, TowerC};", null, 2, null); // 2
    sc.addCodeLine("putDisks(TowerA);", null, 2, null); // 3
    sc.addCodeLine(
        "While ( TowerB.nrDisks() != nrDisks && TowerC.nrDisks() != nrDisks) {",
        null, 2, null); // 4
    sc.addCodeLine("for (Tower from : allTowers)", null, 4, null); // 5
    sc.addCodeLine("if ( from.nrDisks() != 0) {", null, 6, null); // 6
    sc.addCodeLine("curDisk = from.topDisk();", null, 8, null); // 7
    sc.addCodeLine("if (curDisk != lastMovedDisk) {", null, 8, null); // 8
    sc.addCodeLine("if (curDisk.LabelIsOdd)", null, 10, null); // 9
    sc.addCodeLine("to = from.nextClockwise();", null, 12, null); // 10
    sc.addCodeLine("else", null, 10, null); // 11
    sc.addCodeLine("to = from.nextCounterClockwsie();", null, 12, null); // 12
    sc.addCodeLine(
        "if ( to.nrDisks() == 0 || curDisk.Label < to.topDisk().Label) {",
        null, 10, null); // 13
    sc.addCodeLine("moveDisk(from, to);", null, 12, null); // 14
    sc.addCodeLine("lastMovedDisk = curDisk;", null, 12, null); // 15
    sc.addCodeLine("break;", null, 12, null); // 16
    sc.addCodeLine("}", null, 10, null); // 17
    sc.addCodeLine("}", null, 8, null); // 18
    sc.addCodeLine("}", null, 6, null); // 19
    sc.addCodeLine("}", null, 2, null); // 20
    sc.addCodeLine("}", null, 0, null); // 21

  }

  /**
   * nt
   * 
   */
  public void solve() {
    internInit();
    showLabels();

    showSourceCode();
    // *************************************
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 25));

    Text nrDisksLbl = lang.newText(new Coordinates(20, 80), "nrDisks: "
        + String.valueOf(nrDisks), "#diskslbl", null, textProps);

    sc.highlight(0);
    lang.nextStep();

    // *************************************
    Text lastLbl = lang.newText(new Offset(0, 10, nrDisksLbl,
        AnimalScript.DIRECTION_SW), "lastMovedDisk: ", "lastLbl", null,
        textProps);
    Text lastMoved = lang.newText(new Offset(5, 0, lastLbl,
        AnimalScript.DIRECTION_NE), "null", "lastmoved", null, textProps);

    sc.toggleHighlight(0, 1);
    lastLbl.changeColor(null, Color.RED, null, null);
    lastMoved.changeColor(null, Color.RED, null, null);
    lang.nextStep();

    lastLbl.changeColor(null, Color.BLACK, null, null);
    lastMoved.changeColor(null, Color.BLACK, null, null);

    sc.toggleHighlight(1, 2);
    showTowers();
    lang.nextStep();

    sc.toggleHighlight(2, 3);
    showDisks();
    lang.nextStep();

    sc.toggleHighlight(3, 4);
    nrDisksLbl.changeColor(null, Color.RED, null, null);
    // towerB.changeColor(null, Color.RED, null, null);
    // towerC.changeColor(null, Color.RED, null, null);
    lang.nextStep();

    int lastMovedDisk = 0, counter = 0;

    Tower to = null;
    Disk curDisk = null;
    while (towers.get(1).size() != nrDisks && towers.get(2).size() != nrDisks) {
      // System.out.println(towers.get(0).size() + " " + towers.get(1).size() +
      // " " + towers.get(2).size());
      nrDisksLbl.changeColor(null, Color.BLACK, null, null);
      sc.toggleHighlight(4, 5);
      // lang.nextStep();

      for (Tower from : towers) {
        from.showFrom();
        lang.nextStep();

        sc.toggleHighlight(5, 6);
        lang.nextStep();

        if (from.size() != 0) {
          sc.toggleHighlight(6, 7);
          curDisk = disks.get(from.peek() - 1);
          curDisk.highlight();
          lang.nextStep();

          sc.toggleHighlight(7, 8);
          lastLbl.changeColor(null, Color.RED, null, null);
          lastMoved.changeColor(null, Color.RED, null, null);
          lang.nextStep();

          lastLbl.changeColor(null, Color.BLACK, null, null);
          lastMoved.changeColor(null, Color.BLACK, null, null);

          if (from.peek() != lastMovedDisk) {
            sc.toggleHighlight(8, 9);
            lang.nextStep();

            if (from.peek() % 2 != 0) {
              to = towers.get(from.nextClockwise());
              sc.toggleHighlight(9, 10);
              to.showTo();
              lang.nextStep();
              sc.unhighlight(10);
            } else {
              sc.toggleHighlight(9, 11);
              lang.nextStep();
              to = towers.get(from.nextCounterClockwise());
              sc.toggleHighlight(11, 12);
              to.showTo();
              lang.nextStep();
              sc.unhighlight(12);
            }

            // to.showTo();
            sc.highlight(13);
            if (to.size() == 0 || from.peek() < to.peek())
              validMove.show();
            else
              invalidMove.show();
            lang.nextStep();

            if (to.size() == 0 || from.peek() < to.peek()) {
              validMove.hide();

              sc.toggleHighlight(13, 14);
              lastMovedDisk = from.peek();
              moveDisk(from, to);
              moveCtr.setText(String.valueOf(++counter), null, null);
              // System.out.println("move disk " + lastMovedDisk + " from " +
              // from.getLabel() + " to " + to.getLabel() );
              lang.nextStep();

              sc.toggleHighlight(14, 15);
              lastMoved.setText("Disk " + String.valueOf(lastMovedDisk), null,
                  null);
              lastLbl.changeColor(null, Color.RED, null, null);
              lastMoved.changeColor(null, Color.RED, null, null);
              to.hideTo();
              from.hideFrom();
              lang.nextStep();

              lastLbl.changeColor(null, Color.BLACK, null, null);
              lastMoved.changeColor(null, Color.BLACK, null, null);
              curDisk.unhighlight();
              sc.toggleHighlight(15, 16);
              lang.nextStep();

              sc.toggleHighlight(16, 20);
              lang.nextStep();
              from.hideFrom();
              break;
            } else {
              invalidMove.hide();
              sc.toggleHighlight(13, 17);
              lang.nextStep();
              curDisk.unhighlight();
              to.hideTo();
              // from.hideFrom();

              sc.toggleHighlight(17, 18);
              lang.nextStep();

              sc.toggleHighlight(18, 19);
              lang.nextStep();

              sc.toggleHighlight(19, 5);
              from.hideFrom();
            }
          } else { // if (from.peek() != lastMovedDisk)
            curDisk.unhighlight();
            sc.toggleHighlight(8, 18);
            lang.nextStep();

            sc.toggleHighlight(18, 19);
            lang.nextStep();

            from.hideFrom();
            sc.toggleHighlight(19, 5);
          }
        } else { // if ( from.size() != 0 )

          sc.toggleHighlight(6, 19);
          lang.nextStep();
          from.hideFrom();
          sc.toggleHighlight(19, 5);
        }

      } // for (Tower from : towers)

      sc.toggleHighlight(20, 4);
      nrDisksLbl.changeColor(null, Color.RED, null, null);
      lang.nextStep();
      curDisk.unhighlight();
      to.hideTo();

    } // while

    sc.toggleHighlight(4, 21);
    nrDisksLbl.changeColor(null, Color.BLACK, null, null);
    lang.nextStep();

    sc.unhighlight(21);

  }

  /**
   * 
   * @param from
   * @param to
   */
  public void moveDisk(Tower from, Tower to) {
    // lang.nextStep();

    int nFrom = from.size();
    int nTo = to.size();

    int diff = to.getIndex() - from.getIndex();

    int diskNr = from.peek();

    Disk dsk = disks.get(diskNr - 1);

    dsk.moveBy(0, -(13 - nFrom) * 15, defaultTiming);
    dsk.moveBy((60 + 40 + 60) * diff, 0, defaultTiming);
    dsk.moveBy(0, (13 - nTo - 1) * 15, defaultTiming);

    // Primitive path = getPath(from, to);
    // Disk dsk = disks.get(from.peek() - 1);
    // dsk.moveVia(AnimalScript.DIRECTION_C, null, path, null, defaultTiming);
    to.push(from.pop());

  }

//  private Primitive getPath(Tower from, Tower to) {
//    Node[] vertices = new Node[4];
//    int nFrom = from.size();
//    int nTo = to.size();
//
//    int diff = to.getIndex() - from.getIndex();
//
//    int diskNr = from.peek();
//
//    Disk dsk = disks.get(diskNr - 1);
//    dsk.hide();
//
//    vertices[0] = dsk.getUpperLeft();
//    dsk.moveBy(0, -(13 - nFrom) * 15, null);
//    vertices[1] = dsk.getUpperLeft();
//    dsk.moveBy((60 + 40 + 60) * diff, 0, null);
//    vertices[2] = dsk.getUpperLeft();
//    dsk.moveBy(0, (13 - nTo - 1) * 15, null);
//    vertices[3] = dsk.getUpperLeft();
//
//    dsk.moveBy(0, -(13 - nTo - 1) * 15, null);
//    dsk.moveBy(-(60 + 40 + 60) * diff, 0, null);
//    dsk.moveBy(0, (13 - nFrom) * 15, null);
//
//    dsk.show();
//
//    Polyline pl = lang.newPolyline(vertices, "path", null);
//    pl.hide();
//
//    return pl;
//  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    // init();

    nrDisks = (Integer) arg1.get("Number of Disks");
    if (nrDisks > 10)
      nrDisks = 10;

    if (nrDisks < 1)
      nrDisks = 1;

    towerColor = (Color) ((RectProperties) arg0.getPropertiesByName("Tower"))
        .get(AnimationPropertiesKeys.FILL_PROPERTY);

    dps = new DiskProperties((RectProperties) arg0.getPropertiesByName("Disk"));

    scProps = (SourceCodeProperties) arg0.getPropertiesByName("Source Code");

    solve();

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Towers of Hanoi";
  }

  @Override
  public String getAnimationAuthor() {
    return "Amir Naseri, Morteza Emamgholi";
  }

  @Override
  public String getCodeExample() {
    return "public void solveTowersOfHanoi(int nrDisks) {\n"
        + "	lastMovedDisk = null;\n"
        + "	allTowers = {TowerA, TowerB, TowerC};\n"
        + "	putDisks(TowerA);\n"
        + "	While ( TowerB.nrDisks() != nrDisks && TowerC.nrDisks() != nrDisks) {\n"
        + "		for (Tower from : allTowers)\n"
        + "			if ( from.nrDisks() != 0) {\n"
        + "				curDisk = from.topDisk();\n"
        + "				if (curDisk != lastMovedDisk) {\n"
        + "					if (curDisk.LabelIsOdd)\n"
        + "						to = from.nextClockwise();\n"
        + "					else\n"
        + "						to = from.nextCounterClockwsie();\n"
        + "					if ( to.nrDisks() == 0 || curDisk.Label < to.topDisk().Label) {\n"
        + "						moveDisk(from, to);\n" + "						lastMovedDisk = curDisk;\n"
        + "						break;\n" + "					}\n" + "				}\n" + "			}\n" + "	}\n" + "}";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "This iterative algorithm would provide an excellent validation problem\n"
        + "for putative program simplification systems. To see this simple solution,\n"
        + "first establish these standards :\n"
        + "1. Number the disks from 1 (the smallest) to N (the largest).\n"
        + "2. The three posts are ordered so that the concepts of moving a disk\n"
        + "clockwise and counterclockwise are meaningful.\n"
        + "Now the whole solution derives from these three principles :\n"
        + "1. Move odd-numbered disks only clockwise and even-numbered disks only counterclockwise.\n"
        + "2. Do not move the same disk twice in succession.\n"
        + "3. Do not place a larger disk on top of a smaller one.\n\n"
        + "Wm. Randolph Franklin, \"A SIMPLER ITERATIVE SOLUTION TO THE TOWERS OF HANOI PROBLEM\"\n"
        + "		Electrical, Computer, and Systems Engineering Department\n"
        + "			Rensselaer Polytechnic Institute\n"
        + "                              Troy, NY 12181";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getName() {
    return "TowersOfHanoi";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Towers of Hanoi",
        "Amir Naseri, Morteza Emamgholi", 640, 480);
    lang.setStepMode(true);

    towers = new Vector<Tower>();
    disks = new Vector<Disk>();

  }

}
