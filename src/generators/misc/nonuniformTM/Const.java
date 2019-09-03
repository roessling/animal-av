package generators.misc.nonuniformTM;

import algoanim.util.Coordinates;

public class Const {
  /**
   * Each input bit has distance drawShift to the next input bit.
   */
  public static final int drawShiftHorizontal = 90;

  public static final int drawShiftVertical = 85;
  /**
   * Left border of animation.
   */
  public static final int leftBorderVertical = 50;

  public static final int topBorderHorizontal = 300;

  public static final Coordinates ellipseSize = new Coordinates(25, 15);
  
  /**
   * Left border TM.
   */
  public static final int leftBorderVerticalTM = 470;
  /**
   * Left border for pseudo code.
   */
  public static final int leftBorderVerticalPseudoCode = 1120;
  /**
   * Shift left = broadth of each char
   */
  public static final int shiftLeftChar = 5;
}
