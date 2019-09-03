package generators.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * 
 * @author Alexander Pass
 * 
 */

public class AdvancedText {
  private Language       lang;
  Timing                 delay;                                                    // vorerst
                                                                                    // auf
                                                                                    // null
                                                                                    // lassen
  Timing                 duration;                                                  // vorerst
                                                                                    // auf
                                                                                    // null
                                                                                    // lassen
  String                 pattern       = "([^$]*)(\\$[a-zA-Z]+)\\(([^).]+)\\)(.*)";
  String                 sum_until     = "(.*)([\\$sum\\(]+)(.*)[;]+(.+)\\)(.*)";
  String                 index_up      = "(.*)([\\$index\\(]+)(.*)[;]+(.+)\\)(.*)";
  Font                   font_upper;
  Font                   font_tp;
  // String innen_pattern="([^$.]*)(\\$[a-zA-Z]+)\\<([^).]+)\\>(.*)";
  private Offset         UpperLeft;
  private Offset         TextUL;
  private Offset         actUpperLeft;
  private TextProperties tp;
  private TextProperties u_prop        = new TextProperties();
  private Vector<Text>   text          = new Vector<Text>();
  private Vector<Text>   indizes       = new Vector<Text>();
  private Vector<Rect>   help          = new Vector<Rect>();

  private Vector<Text>   sigmas        = new Vector<Text>();
  // private boolean scal_up_flag=false; //eventuelle Korrektur nach oben(falls
  // oberer index/oder a)
  private boolean        sum_down_flag = false;                                    // eventuelle
                                                                                    // Korrektur
                                                                                    // nach
                                                                                    // unten
                                                                                    // (falls
                                                                                    // unterer
                                                                                    // index/oder
                                                                                    // untere
                                                                                    // summengrenze
                                                                                    // angegeben
                                                                                    // sind)
  private boolean        ind_down_flag = false;
  String                 sum           = "" + '\u2211';

  /*
   * char no_elem='\u2209'; char not_equal='\u2260'; char elem='\u2208'; char
   * oLine='\u0305'; //muss nach dem Buchstaben stehen
   */
  /**
   * advancedText: bietet eine Möglichkeit Text mit Indizies, Summenzeichen,...
   * darzustellen. Die Kennzeichnung erfolgt mittels besonderer Schlagwörter,
   * wie String$sum(lowlimit;uplimit)String3 String$index(low;upper)String
   * 
   * @param lang
   *          Language
   * @param input
   *          String, das zerlegt werden muss
   */
  public AdvancedText(Language lang, Offset UpperLeft, String input,
      TextProperties tp, Timing delay, Timing duration) {

    this.lang = lang;
    this.tp = tp;
    u_prop = new TextProperties();
    font_tp = (Font) tp.get("font");
    Color color = (Color) tp.get("color");
    u_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
    u_prop.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(font_tp.getName(), font_tp.getStyle(), font_tp.getSize() - 2));
    font_upper = (Font) u_prop.get("font");
    /*
     * In Java verfügbare Fonts anzeigen for ( String fonts :
     * GraphicsEnvironment.getLocalGraphicsEnvironment().
     * getAvailableFontFamilyNames() ) System.out.println( "availableFont"+fonts
     * );
     */

    this.delay = delay;
    this.duration = duration;
    this.UpperLeft = UpperLeft;
    text.add(lang.newText(this.UpperLeft, "", "hilfe", null));
    help.add(lang.newRect(UpperLeft, new Offset(0, 0, text.lastElement()
        .getName(), AnimalScript.DIRECTION_SE), "help", null));
    // überprüft, ob Summenzeichen

    if (input.matches(sum_until) || input.matches(index_up)
        && !input.equals("")) {
      // System.out.println("oberer Grenzwert vorhanden:"+input);
      TextUL = new Offset(0, font_upper.getSize(),
          help.lastElement().getName(), AnimalScript.DIRECTION_NW);
      actUpperLeft = new Offset(0, font_upper.getSize(), help.lastElement()
          .getName(), AnimalScript.DIRECTION_NW);
      // später testen, ob offset=offset doch nciht 2 objekte sind
    } else {
      // System.out.println(" KEIN oberer Grenzwert vorhanden:"+input);
      TextUL = new Offset(0, 0, help.lastElement().getName(),
          AnimalScript.DIRECTION_NW);
      actUpperLeft = new Offset(0, 0, help.lastElement().getName(),
          AnimalScript.DIRECTION_NW);
      // später testen, ob offset=offset doch nciht 2 objekte sind
    }

    parse(input);

  }

  private void parse(String input) {
    fkt(input.replaceAll(pattern, "$1"), input.replaceAll(pattern, "$2"),
        input.replaceAll(pattern, "$3"), input.replaceAll(pattern, "$4"));

  }

  private void fkt(String vor, String fkt, String param, String rest) {
    // ersten String(..bis zum $) direkt anzeigen lassen
    text.add(lang.newText(actUpperLeft, vor, "Text", null, tp));
    help.add(lang.newRect(TextUL, new Offset(0, 0,
        text.lastElement().getName(), AnimalScript.DIRECTION_SE), "help", null));
    actUpperLeft = new Offset(0, 0, text.lastElement().getName(),
        AnimalScript.DIRECTION_NE);

    if (fkt.equals("$index")) {
      String index_reg = "(.*)[;](.*)";
      String[] index_params = new String[2];
      index_params[0] = param.replaceAll(index_reg, "$1");
      index_params[1] = param.replaceAll(index_reg, "$2");

      // vorerst nur den unteren Index anzeigen lassen
      if (!index_params[0].equals("")) {
        actUpperLeft = new Offset(0, 0, help.lastElement().getName(),
            AnimalScript.DIRECTION_E);
        indizes.add(lang.newText(actUpperLeft, index_params[0], "Text", null,
            u_prop));

        help.add(lang.newRect(TextUL, new Offset(0, 0, indizes.lastElement()
            .getName(), AnimalScript.DIRECTION_SE), "help", null));
        actUpperLeft = new Offset(0, 0, help.lastElement().getName(),
            AnimalScript.DIRECTION_NE);
        ind_down_flag = true;

      }
      // oberer index noch zu machen
      if (!index_params[1].equals("")) {
        // System.out.println("Oh, oberen Index gefunden");
      }

    } else if (fkt.equals("$sum")) {
      String sum_reg = "(.*)[;](.*)";
      String[] sum_params = new String[2];
      sum_params[0] = param.replaceAll(sum_reg, "$1");
      sum_params[1] = param.replaceAll(sum_reg, "$2");
      Font tp_font = (Font) tp.get("font");
      int size = tp_font.getSize();

      sigmas.add(lang.newText(actUpperLeft, sum, "Text", null, tp));
      // lang.newRect(new
      // Offset(0,0,sigmas.lastElement().getName(),AnimalScript.DIRECTION_NW),
      // new
      // Offset(0,0,sigmas.lastElement().getName(),AnimalScript.DIRECTION_SE),
      // "sigma-rec", null);
      // text unter/oberhalb des summenzeichens
      // obere
      if (!sum_params[1].equals("")) {
        text.add(lang.newText(new Offset(0, -size, sigmas.lastElement()
            .getName(), AnimalScript.DIRECTION_NW), sum_params[1], "sum_ober",
            null, u_prop));
      }
      // untere grenze
      if (!sum_params[0].equals("")) {
        text.add(lang.newText(new Offset(0, -4, sigmas.lastElement().getName(),
            AnimalScript.DIRECTION_SW), sum_params[0], "sum_unter", null,
            u_prop));
        sum_down_flag = true;
      }
      // es müsste aber noch eine anpassung an eventuell einzig oberer grenze
      // geben
      help.add(lang.newRect(TextUL, new Offset(0, 0, text.lastElement()
          .getName(), AnimalScript.DIRECTION_SE), "help", null));
      actUpperLeft = new Offset(0, 0, help.lastElement().getName(),
          AnimalScript.DIRECTION_NE);
    } else {
      // hier könnte man weitere "schlagwörter" hinzufügen
    }

    if (rest.matches(pattern)) {
      // System.out.println("selben kram im rest gefunden:"+rest);
      parse(rest);
    } else if (!rest.equals(vor)) {
      text.add(lang.newText(actUpperLeft, rest, "Text", null, tp));
      // Endstation:
      int offset = 0;
      if (sum_down_flag) {
        offset = font_upper.getSize();
      } else if (ind_down_flag) {
        offset = font_upper.getSize() - font_tp.getSize() / 2;
      }

      help.add(lang.newRect(UpperLeft, new Offset(0, 0 + offset, text
          .lastElement().getName(), AnimalScript.DIRECTION_SE), "help", null));
      // actUpperLeft=new
      // Offset(0,0,help.lastElement().getName(),AnimalScript.DIRECTION_NE);

    }

    for (int i = 0; i < help.size(); i++)
      lang.hideInThisStep.add(help.get(i).getName());

  }

  /**
   * 
   * @return Gibt den Namen des Objekts zurück.
   */
  public String getName() {
    return help.lastElement().getName();
  }

  public Offset getSW() {
    return (new Offset(0, 0, help.lastElement().getName(),
        AnimalScript.DIRECTION_SW));
  }

  public Offset getNW() {
    return (new Offset(0, 0, help.lastElement().getName(),
        AnimalScript.DIRECTION_NW));
  }

  public Offset getSE() {
    return (new Offset(0, 0, help.lastElement().getName(),
        AnimalScript.DIRECTION_SE));
  }

  public Offset getNE() {
    return (new Offset(0, 0, help.lastElement().getName(),
        AnimalScript.DIRECTION_NE));
  }

  public Offset getW() {
    return (new Offset(0, 0, help.lastElement().getName(),
        AnimalScript.DIRECTION_W));
  }

  public void set(String input) {
    hide();
    // this.UpperLeft=newUpperLeft;
    // löschen aller "Hilfselemente"
    help.clear();
    indizes.clear();
    sigmas.clear();
    text.clear();
    ind_down_flag = sum_down_flag = false;
    // Wiederherstellung der AnfangsPosition
    text.add(lang.newText(this.UpperLeft, "", "hilfe", null));
    help.add(lang.newRect(UpperLeft, new Offset(0, 0, text.lastElement()
        .getName(), AnimalScript.DIRECTION_SE), "help", null));
    // überprüft, ob Summenzeichen

    if (input.matches(sum_until) || input.matches(index_up)
        && !input.equals("")) {
      // System.out.println("oberer Grenzwert vorhanden(set):"+input);
      TextUL = new Offset(0, font_upper.getSize(),
          help.lastElement().getName(), AnimalScript.DIRECTION_NW);
      actUpperLeft = new Offset(0, font_upper.getSize(), help.lastElement()
          .getName(), AnimalScript.DIRECTION_NW);
      // später testen, ob offset=offset doch nciht 2 objekte sind
    } else {
      // System.out.println(" KEIN oberer Grenzwert vorhanden(set):"+input);
      TextUL = new Offset(0, 0, help.lastElement().getName(),
          AnimalScript.DIRECTION_NW);
      actUpperLeft = new Offset(0, 0, help.lastElement().getName(),
          AnimalScript.DIRECTION_NW);
      // später testen, ob offset=offset doch nciht 2 objekte sind
    }

    parse(input);
    // show();
  }

  public void hide() {
    for (int i = 0; i < text.size(); i++)
      lang.hideInThisStep.add(text.get(i).getName());
    for (int i = 0; i < indizes.size(); i++)
      lang.hideInThisStep.add(indizes.get(i).getName());
    for (int i = 0; i < sigmas.size(); i++)
      lang.hideInThisStep.add(sigmas.get(i).getName());

  }

  public void show() {
    for (int i = 0; i < text.size(); i++)
      lang.showInThisStep.add(text.get(i).getName());
    for (int i = 0; i < indizes.size(); i++)
      lang.showInThisStep.add(indizes.get(i).getName());
    for (int i = 0; i < sigmas.size(); i++)
      lang.showInThisStep.add(sigmas.get(i).getName());

  }
}
