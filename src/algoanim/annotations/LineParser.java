package algoanim.annotations;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser {
  private String             line;
  private String             labelChar;

  private int                indent;
  private String             code;
  private Vector<Annotation> props;

  public LineParser(String line) {
    this(line, "@");
  }

  public LineParser(String line, String labelChar) {
    this.line = line.replaceAll("\\s", " ");
    this.labelChar = labelChar;

    parseIndent();
    parseCode();
    parseProperties();
  }

  private void parseIndent() {
    indent = 0;
    while (indent < line.length() && line.charAt(indent) == ' ')
      indent++;
  }

  private void parseCode() {
    Pattern p = Pattern.compile("([^" + labelChar + "]+)(" + labelChar + ".*)");
    Matcher m = p.matcher(line);

    if (m.find()) {
      code = m.group(1).trim();
    } else
      code = "";
  }

  private void parseProperties() {
    props = new Vector<Annotation>();

    // alle annotations finden
    String value = "[a-zA-Z0-9,.\\-_ +*/()]+";
    String regexp = labelChar + "([a-zA-Z]+)(?:\\((\"" + value + "\"(, \""
        + value + "\")*)?\\))?";

    Pattern p = Pattern.compile(regexp);
    Matcher m = p.matcher(line);

    // alle gefundenen annotations parsen
    while (m.find()) {
      String name = m.group(1);
      String options = m.group(2);

      Annotation prop = new Annotation(name);

      if (options != null) {
        String par_re = "\"(" + value + ")\"";
        Pattern par_p = Pattern.compile(par_re);
        Matcher par_m = par_p.matcher(options);

        while (par_m.find()) {
          prop.addParameter(par_m.group(1));
        }
      }
      props.add(prop);
    }
  }

  /**
   * @return identation level for this line
   */
  public int getIndent() {
    return indent;
  }

  /**
   * @return code part of this line
   */
  public String getCode() {
    return code;
  }

  /**
   * @return all parsed annotations of this line
   */
  public Vector<Annotation> getProperties() {
    return props;
  }

  public String getLabel() {
    for (int i = 0; i < props.size(); i++) {
      Annotation anno = props.get(i);
      if (anno.getName().equals("label")) {
        if (anno.getParameters() != null && anno.getParameters().size() > 0)
          return props.get(i).getParameters().get(0);
        else 
          return "null";
      }
    }

    return null;
  }

  public boolean isContinue() {
    for (int i = 0; i < props.size(); i++)
      if (props.get(i).getName().equals("continue"))
        return true;

    return false;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder(256);
    sb.append("indentation: ").append((Integer.valueOf(indent)));
    sb.append("\ncode: ").append(code).append("\nprops: ").append(props);
    sb.append("\ncontinued: ").append(isContinue()).append("\nlabel: ");
    sb.append(getLabel()).append("\n");
    return sb.toString();
  }
}
