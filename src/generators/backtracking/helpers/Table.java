package generators.backtracking.helpers;

import algoanim.animalscript.AnimalScript;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.primitives.Text;
import algoanim.util.Offset;
import algoanim.util.Coordinates;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.IntMatrix;

public class Table {

  private Text       header;
  // private TextProperties textProps, tableTextProps;
  private Text[]     names, ranking;
  private Polyline[] line;
  private Rect       rect;
  private IntMatrix  request, reject;
  private Language   lang;

  public Table(String headerName, TextProperties headerProps,
      TextProperties tableTextProps, PolylineProperties lineProps,
      Offset position, Language lang, Person[] persons, Person[] persons2) {
    this.lang = lang;
    int number = 0;
    String s = "";
    for (int i = 0; i < persons.length; i++)
      if (persons[i].getName().length() > number)
        number = persons[i].getName().length();
    for (int i = 0; i < number; i++)
      s = s + "_";

    this.names = new Text[persons.length];
    ranking = new Text[names.length * names.length];
    line = new Polyline[names.length];
//    this.tableTextProps = tableTextProps;
    header = lang.newText(position, headerName, headerName, null, headerProps);
    for (int i = 0; i < names.length; i++) {
      if (i == 0)
        this.names[0] = lang.newText(new Offset(5, 7, header,
            AnimalScript.DIRECTION_SW), persons[0].getName(), headerName + i,
            null, tableTextProps);
      else
        this.names[i] = lang.newText(new Offset(0, 4, headerName + (i - 1),
            AnimalScript.DIRECTION_SW), persons[i].getName(), headerName + i,
            null, tableTextProps);
      Text dummy = lang.newText(new Offset(0, 0, this.names[i],
          AnimalScript.DIRECTION_NW), s, "dummy", null, tableTextProps);
      Offset[] o = new Offset[] {
          new Offset(40, -3, dummy, AnimalScript.DIRECTION_NE),
          new Offset(40, 3, dummy, AnimalScript.DIRECTION_SE) };
      line[i] = lang.newPolyline(o, headerName + "line" + i, null, lineProps);
      dummy.hide();

      for (int j = 0; j < names.length; j++) {
        if (j == 0)
          ranking[i * names.length] = lang.newText(new Offset(30, 3, line[i],
              AnimalScript.DIRECTION_NE), persons[i].getPersonAt(j).getName()
              .substring(0, 1), headerName + "ranking" + (i * names.length),
              null, tableTextProps);
        else
          ranking[i * names.length + j] = lang.newText(new Offset(30, 0,
              ranking[i * names.length + j - 1], AnimalScript.DIRECTION_NE),
              persons[i].getPersonAt(j).getName().substring(0, 1), headerName
                  + "ranking" + (i * names.length + j), null, tableTextProps);
      }
    }
    rect = lang.newRect(new Offset(-3, -3, this.names[0],
        AnimalScript.DIRECTION_NW), new Offset(20, 7,
        ranking[ranking.length - 1], AnimalScript.DIRECTION_SE), headerName
        + "Rect", null);

    int[][] request = new int[names.length][names.length];
    int[][] reject = new int[names.length][names.length];
    for (int i = 0; i < names.length; i++) {
      for (int j = 0; j < names.length; j++) {
        request[i][j] = 0;
        reject[i][j] = 0;
      }
    }

    this.request = lang.newIntMatrix(new Coordinates(0, 0), request,
        "gridcell", null);
    this.reject = lang.newIntMatrix(new Coordinates(0, 0), reject, "gridcell",
        null);

    this.request.hide();
    this.reject.hide();
  }

  public Rect getRect() {
    return rect;
  }

  public Text[] getNames() {
    return names;
  }

  public IntMatrix getRequests() {
    return request;
  }

  public IntMatrix getRejects() {
    return reject;
  }

  public void setRequest(int a, int b) {
    request.put(b, a, 1, null, null);
    lang.newCircle(new Offset(0, 0, ranking[a * names.length + b],
        AnimalScript.DIRECTION_C), 12, "circle" + (a * names.length + b), null);
  }

  public void setReject(int a, int b) {
    reject.put(b, a, 1, null, null);
    lang.newPolyline(new Offset[] {
        new Offset(0, 0, ranking[a * names.length + b],
            AnimalScript.DIRECTION_NW),
        new Offset(0, 0, ranking[a * names.length + b],
            AnimalScript.DIRECTION_SE) }, "1cross" + (a * names.length + b),
        null);
    lang.newPolyline(new Offset[] {
        new Offset(0, 0, ranking[a * names.length + b],
            AnimalScript.DIRECTION_NE),
        new Offset(0, 0, ranking[a * names.length + b],
            AnimalScript.DIRECTION_SW) }, "1cross" + (a * names.length + b),
        null);
  }
}
