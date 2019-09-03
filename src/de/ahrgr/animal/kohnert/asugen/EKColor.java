/*
 * Created on 17.12.2004
 */
package de.ahrgr.animal.kohnert.asugen;

import java.util.Hashtable;

/**
 * @author ek
 */
public class EKColor {
	/*
	 * public static final int BLACK = 0; public static final int BLUE = 1; public
	 * static final int BLUE2 = 2; public static final int BLUE3 = 3; public
	 * static final int BLUE4 = 4; public static final int BROWN2 = 5; public
	 * static final int BROWN3 = 6; public static final int BROWN4 = 7; public
	 * static final int CYAN = 8; public static final int CYAN2 = 9; public static
	 * final int CYAN3 = 10; public static final int CYAN4 = 11; public static
	 * final int DARK_GRAY = 12; public static final int GOLD = 13; public static
	 * final int GREEN = 14; public static final int GREEN2 = 15; public static
	 * final int GREEN3 = 16; public static final int GREEN4 = 17; public static
	 * final int LIGHT_GREEN = 18; public static final int LIGHT_BLUE = 19; public
	 * static final int MAGENTA = 20; public static final int MAGENTA2 = 21;
	 * public static final int MAGENTA3 = 22; public static final int MAGENTA4 =
	 * 23; public static final int ORANGE = 24; public static final int PINK = 25;
	 * public static final int PINK2 = 26; public static final int PINK3 = 27;
	 * public static final int PINK4 = 28; public static final int RED = 29;
	 * public static final int RED2 = 30; public static final int RED3 = 31;
	 * public static final int RED4 = 32; public static final int WHITE = 33;
	 * public static final int YELLOW = 34;
	 */

	public static final EKColor BLACK = new EKColor(0);

	public static final EKColor BLUE = new EKColor(1);

	public static final EKColor BLUE2 = new EKColor(2);

	public static final EKColor BLUE3 = new EKColor(3);

	public static final EKColor BLUE4 = new EKColor(4);

	public static final EKColor BROWN2 = new EKColor(5);

	public static final EKColor BROWN3 = new EKColor(6);

	public static final EKColor BROWN4 = new EKColor(7);

	public static final EKColor CYAN = new EKColor(8);

	public static final EKColor CYAN2 = new EKColor(9);

	public static final EKColor CYAN3 = new EKColor(10);

	public static final EKColor CYAN4 = new EKColor(11);

	public static final EKColor DARK_GRAY = new EKColor(12);

	public static final EKColor GOLD = new EKColor(13);

	public static final EKColor GRAY = new EKColor(14);

	public static final EKColor GREEN = new EKColor(15);

	public static final EKColor GREEN2 = new EKColor(16);

	public static final EKColor GREEN3 = new EKColor(17);

	public static final EKColor GREEN4 = new EKColor(18);

	public static final EKColor LIGHT_GRAY = new EKColor(19);

	public static final EKColor LIGHT_BLUE = new EKColor(20);

	public static final EKColor MAGENTA = new EKColor(21);

	public static final EKColor MAGENTA2 = new EKColor(22);

	public static final EKColor MAGENTA3 = new EKColor(23);

	public static final EKColor MAGENTA4 = new EKColor(24);

	public static final EKColor ORANGE = new EKColor(25);

	public static final EKColor PINK = new EKColor(26);

	public static final EKColor PINK2 = new EKColor(27);

	public static final EKColor PINK3 = new EKColor(28);

	public static final EKColor PINK4 = new EKColor(29);

	public static final EKColor RED = new EKColor(30);

	public static final EKColor RED2 = new EKColor(31);

	public static final EKColor RED3 = new EKColor(32);

	public static final EKColor RED4 = new EKColor(33);

	public static final EKColor WHITE = new EKColor(34);

	public static final EKColor YELLOW = new EKColor(35);

	public static final EKColor DEFAULT_FILLCOLOR = WHITE;

	private static Hashtable<Object, Integer> CLUT = new Hashtable<Object, Integer>(
			59);

	static {
		CLUT.put(BLACK, new Integer(0));
		CLUT.put(BLUE, new Integer(1));
		CLUT.put(BLUE2, new Integer(2));
		CLUT.put(BLUE3, new Integer(3));
		CLUT.put(BLUE4, new Integer(4));
		CLUT.put(BROWN2, new Integer(5));
		CLUT.put(BROWN3, new Integer(6));
		CLUT.put(BROWN4, new Integer(7));
		CLUT.put(CYAN, new Integer(8));
		CLUT.put(CYAN2, new Integer(9));
		CLUT.put(CYAN3, new Integer(10));
		CLUT.put(CYAN4, new Integer(11));
		CLUT.put(DARK_GRAY, new Integer(12));
		CLUT.put(GOLD, new Integer(13));
		CLUT.put(GRAY, new Integer(14));
		CLUT.put(GREEN, new Integer(15));
		CLUT.put(GREEN2, new Integer(16));
		CLUT.put(GREEN3, new Integer(17));
		CLUT.put(GREEN4, new Integer(18));
		CLUT.put(LIGHT_GRAY, new Integer(19));
		CLUT.put(LIGHT_BLUE, new Integer(20));
		CLUT.put(MAGENTA, new Integer(21));
		CLUT.put(MAGENTA2, new Integer(22));
		CLUT.put(MAGENTA3, new Integer(23));
		CLUT.put(MAGENTA4, new Integer(24));
		CLUT.put(ORANGE, new Integer(25));
		CLUT.put(PINK, new Integer(26));
		CLUT.put(PINK2, new Integer(27));
		CLUT.put(PINK3, new Integer(28));
		CLUT.put(PINK4, new Integer(29));
		CLUT.put(RED, new Integer(30));
		CLUT.put(RED2, new Integer(31));
		CLUT.put(RED3, new Integer(32));
		CLUT.put(RED4, new Integer(33));
		CLUT.put(WHITE, new Integer(34));
		CLUT.put(YELLOW, new Integer(35));

		CLUT.put(java.awt.Color.BLACK, new Integer(0));
		CLUT.put(java.awt.Color.BLUE, new Integer(1));
		CLUT.put(new java.awt.Color(0, 0, 0xd0), new Integer(2));
		CLUT.put(new java.awt.Color(0, 0, 0xb0), new Integer(3));
		CLUT.put(new java.awt.Color(0, 0, 0x90), new Integer(4));
		CLUT.put(new java.awt.Color(0xc0, 0x60, 0), new Integer(5));
		CLUT.put(new java.awt.Color(0xa0, 0x40, 0), new Integer(6));
		CLUT.put(new java.awt.Color(0x80, 0x30, 0), new Integer(7));
		CLUT.put(java.awt.Color.CYAN, new Integer(8));
		CLUT.put(new java.awt.Color(0x0, 0xd0, 0xd0), new Integer(9));
		CLUT.put(new java.awt.Color(0x0, 0xb0, 0xb0), new Integer(10));
		CLUT.put(new java.awt.Color(0x0, 0x90, 0x90), new Integer(11));
		CLUT.put(java.awt.Color.DARK_GRAY, new Integer(12));
		CLUT.put(new java.awt.Color(0xff, 0xd7, 0xd0), new Integer(13));
		CLUT.put(java.awt.Color.GRAY, new Integer(14));
		CLUT.put(java.awt.Color.GREEN, new Integer(15));
		CLUT.put(new java.awt.Color(0x0, 0xd0, 0x0), new Integer(16));
		CLUT.put(new java.awt.Color(0x0, 0xb0, 0x0), new Integer(17));
		CLUT.put(new java.awt.Color(0x0, 0x90, 0x0), new Integer(18));
		CLUT.put(java.awt.Color.LIGHT_GRAY, new Integer(19));
		CLUT.put(new java.awt.Color(0x87, 0xce, 0xff), new Integer(20));
		CLUT.put(java.awt.Color.MAGENTA, new Integer(21));
		CLUT.put(new java.awt.Color(0xd0, 0x0, 0xd0), new Integer(22));
		CLUT.put(new java.awt.Color(0xb0, 0x0, 0xb0), new Integer(23));
		CLUT.put(new java.awt.Color(0x90, 0x0, 0x90), new Integer(24));
		CLUT.put(java.awt.Color.ORANGE, new Integer(25));
		CLUT.put(new java.awt.Color(0xff, 0xe0, 0xe0), new Integer(26));
		CLUT.put(new java.awt.Color(0xff, 0xc0, 0xc0), new Integer(27));
		CLUT.put(new java.awt.Color(0xff, 0xa0, 0xa0), new Integer(28));
		CLUT.put(new java.awt.Color(0xff, 0x80, 0x80), new Integer(29));
		CLUT.put(java.awt.Color.RED, new Integer(30));
		CLUT.put(new java.awt.Color(0xd0, 0x0, 0x0), new Integer(31));
		CLUT.put(new java.awt.Color(0xb0, 0x0, 0x0), new Integer(32));
		CLUT.put(new java.awt.Color(0x90, 0x0, 0x0), new Integer(33));
		CLUT.put(java.awt.Color.WHITE, new Integer(34));
		CLUT.put(java.awt.Color.YELLOW, new Integer(35));
	}

	protected static final String[] colornames = { "black", "blue", "blue2",
			"blue3", "blue4", "brown2", "brown3", "brown4", "cyan", "cyan2", "cyan3",
			"cyan4", "dark Gray", "gold", "gray", "green", "green2", "green3",
			"green4", "light Gray", "light_blue", "magenta", "magenta2", "magenta3",
			"magenta4", "orange", "pink", "pink2", "pink3", "pink4", "red", "red2",
			"red3", "red4", "white", "yellow" };

	protected int colorCode;

	public EKColor(int code) {
		colorCode = code;
	}

	public EKColor(java.awt.Color chosenColor) {
		Integer code = CLUT.get(chosenColor);
		if (code != null)
			colorCode = code.intValue();
		else
			colorCode = 0;
	}

	public String getColorString() {
		return colornames[colorCode];
	}

	public String toString() {
		return getColorString();
	}

	public int getColorCode() {
		return colorCode;
	}

	static public EKColor createFromString(String s) {
		for (int i = 0; i < colornames.length; i++) {
			if (colornames[i].equalsIgnoreCase(s))
				return new EKColor(i);
		}
		System.err.println("color not found: " + s);
		return null;
	}

	public boolean equals(Object o) {
		if (!(o instanceof EKColor))
			return false;
		if (getColorCode() == ((EKColor) o).getColorCode())
			return true;
		return false;
	}

}
