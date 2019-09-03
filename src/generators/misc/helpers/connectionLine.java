package generators.misc.helpers;

import algoanim.primitives.Polyline;

public class connectionLine {
  private boolean  hidden;
  private Polyline line;

  public connectionLine(Polyline line, boolean hidden) {
    super();
    this.hidden = hidden;
    this.line = line;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public Polyline getLine() {
    return line;
  }

  public void setLine(Polyline line) {
    this.line = line;
  }

}
