package algoanim.annotations;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class Annotation {
  // TODO als enum ablegen und keine String speichern
  public final static String LABEL         = "label";
  public final static String DECLARE       = "declare";
  public final static String SET           = "set";
  public final static String DISCARD       = "discard";
  public final static String INC           = "inc";
  public final static String DEC           = "dec";
  public final static String EVAL          = "eval";
  public final static String HIGHLIGHT     = "highlight";
  public final static String GLOBAL        = "global";
  // TODO
  public final static String OPENCONTEXT   = "openContext";
  public final static String CLOSECONTEXT  = "closeContext";

  public final static String VARIABLE_ROLE = "role";

  private String             name;
  private Vector<String>     parameters    = new Vector<String>();

  public Annotation(String name) {
    this.name = name;
  }

  public void addParameter(String param) {
    parameters.add(param);
  }

  public String toString() {
    StringBuilder result = new StringBuilder(256);
    result.append("@").append(name);

    if (parameters.size() > 0) {
      result.append("(");

      for (int i = 0; i < parameters.size(); i++) {
        result.append("\"").append(parameters.get(i)).append("\"");
        if (i != parameters.size() - 1)
          result.append(", ");
      }

      result.append(")");
    }

    return result.toString();
  }

  public static Vector<Annotation> parse(String line) {
    // TODO nur annotations in kommentaren parsen
    // System.out.println("######################");
    Vector<Annotation> result = new Vector<Annotation>();

    // alle annotations finden
    String value = "[a-zA-Z0-9,.-_ ]+";
    String regexp = "@([a-zA-Z]+)(?:\\((\"" + value + "\"(, \"" + value
        + "\")*)?\\))?";

    Pattern p = Pattern.compile(regexp);
    Matcher m = p.matcher(line);

    while (m.find()) {
      String name = m.group(1);
      String options = m.group(2);
      // System.out.println("found: @" + name + "("+options+")");

      Annotation props = new Annotation(name);

      if (options != null) {
        String par_re = "\"(" + value + ")\"";
        Pattern par_p = Pattern.compile(par_re);
        Matcher par_m = par_p.matcher(options);

        while (par_m.find()) {
          props.addParameter(par_m.group(1));
        }
      }
      // System.out.println(props);
      result.add(props);
    }

    return result;
  }

  public String getName() {
    return name;
  }

  public Vector<String> getParameters() {
    return parameters;
  }

  public Executor getExecutor(Variables vars, SourceCode src) {
    return null;
  }
}
