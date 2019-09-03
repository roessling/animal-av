/**
 * 
 */
package animal.vhdl.logic;

/**
 * @author p_li
 * 
 */
public abstract class LogicVHDL {
	public static final Character LOGIC_UNINITIALIZED = 'U';
	public static final Character LOGIC_STRONG_UNKNOWN = 'X';
	public static final Character LOGIC_STRONG_LOW = '0';
	public static final Character LOGIC_STRONG_HIGH = '1';
	public static final Character LOGIC_HIGH_IMPEDANCE = 'Z';
	public static final Character LOGIC_WEAK_UNKNOWN = 'W';
	public static final Character LOGIC_WEAK_LOW = 'L';
	public static final Character LOGIC_WEAK_HIGH = 'H';
	public static final Character LOGIC_DO_NOT_CARE = '-';

	public static final Character[] LOGIC_VALUES = new Character[] {
			LOGIC_UNINITIALIZED, LOGIC_STRONG_UNKNOWN, LOGIC_STRONG_LOW,
			LOGIC_STRONG_HIGH, LOGIC_HIGH_IMPEDANCE, LOGIC_WEAK_UNKNOWN,
			LOGIC_WEAK_LOW, LOGIC_WEAK_HIGH, LOGIC_DO_NOT_CARE };

}
