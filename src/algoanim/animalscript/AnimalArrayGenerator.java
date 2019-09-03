package algoanim.animalscript;

import java.awt.Color;

import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.generators.Language;
import algoanim.util.Timing;

public abstract class AnimalArrayGenerator extends AnimalGenerator {
  public AnimalArrayGenerator(Language as) {
    super(as);
  }

  protected void createEntry(ArrayPrimitive array, String keyword,
      int position, Timing offset, Timing duration) {
    StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    def.append(keyword).append(" on \"").append(array.getName());
    def.append("\" position ").append(position);
    addWithTiming(def, offset, duration);
  }

  protected void createEntry(ArrayPrimitive array, String keyword, int from,
      int to, Timing offset, Timing duration) {
    StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    def.append(keyword).append(" on \"").append(array.getName());
    def.append("\" from ").append(from).append(" to ").append(to);
    addWithTiming(def, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.GenericArrayGenerator
   *      #highlightCell(ArrayPrimitive, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void highlightCell(ArrayPrimitive ia, int from, int to, Timing offset,
      Timing duration) {
    createEntry(ia, "highlightArrayCell", from, to, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.GenericArrayGenerator
   *      #highlightCell(ArrayPrimitive, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void highlightCell(ArrayPrimitive ia, int position, Timing offset,
      Timing duration) {
    createEntry(ia, "highlightArrayCell", position, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.GenericArrayGenerator
   *      #highlightElem(ArrayPrimitive, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void highlightElem(ArrayPrimitive ia, int from, int to, Timing offset,
      Timing duration) {
    createEntry(ia, "highlightArrayElem", from, to, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.GenericArrayGenerator
   *      #highlightElem(ArrayPrimitive, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void highlightElem(ArrayPrimitive ia, int position, Timing offset,
      Timing duration) {
    createEntry(ia, "highlightArrayElem", position, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.GenericArrayGenerator #swap(
   *      algoanim.primitives.ArrayPrimitive, int, int,
   *      algoanim.util.Timing, algoanim.util.Timing)
   */
  public void swap(ArrayPrimitive iap, int what, int with, Timing delay,
      Timing duration) {
    //    if (what != with) {
    StringBuilder sb = new StringBuilder(256);
    sb.append("arraySwap on \"").append(iap.getName());
    sb.append("\" position ").append(what).append(" with ");
    sb.append(with);
    addWithTiming(sb, delay, duration);
  }

  /**
   * @see algoanim.primitives.generators.GenericArrayGenerator
   *      #unhighlightCell(ArrayPrimitive, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void unhighlightCell(ArrayPrimitive ia, int from, int to, Timing offset,
      Timing duration) {
    createEntry(ia, "unhighlightArrayCell", from, to, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.GenericArrayGenerator
   *      #unhighlightCell(ArrayPrimitive, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void unhighlightCell(ArrayPrimitive ia, int position, Timing offset,
      Timing duration) {
    createEntry(ia, "unhighlightArrayCell", position, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.GenericArrayGenerator
   *      #unhighlightElem(ArrayPrimitive, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void unhighlightElem(ArrayPrimitive ia, int from, int to, Timing offset,
      Timing duration) {
    createEntry(ia, "unhighlightArrayElem", from, to, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.GenericArrayGenerator
   *      #unhighlightElem(ArrayPrimitive, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void unhighlightElem(ArrayPrimitive ia, int position, Timing offset,
      Timing duration) {
    createEntry(ia, "unhighlightArrayElem", position, offset, duration);
  }
  
  public void setColorTyp(ArrayPrimitive ia, String typ, int from, int to, Color c, Timing offset,
	      Timing duration){
	    createEntry(ia, typ, from, to, c, offset, duration);
  }
  
  public void setColorTyp(ArrayPrimitive ia, String typ, int position, Color c, Timing offset,
	      Timing duration){
	    createEntry(ia, typ, position, c, offset, duration);
  }

  protected void createEntry(ArrayPrimitive array, String keyword,
      int position, Color c, Timing offset, Timing duration) {
    StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    def.append(keyword).append(" on \"").append(array.getName());
    def.append("\" position ").append(position).append(" with color (").append(c.getRed()+","+c.getGreen()+","+c.getBlue()).append(")");
    addWithTiming(def, offset, duration);
  }

  protected void createEntry(ArrayPrimitive array, String keyword, int from,
      int to, Color c, Timing offset, Timing duration) {
    StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    def.append(keyword).append(" on \"").append(array.getName());
    def.append("\" from ").append(from).append(" to ").append(to).append(" with color (").append(c.getRed()+","+c.getGreen()+","+c.getBlue()).append(")");
    addWithTiming(def, offset, duration);
  }
}
