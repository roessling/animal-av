/*
 * Created on 09.02.2005
 */
package de.ahrgr.animal.kohnert.asugen;

/**
 * @author ek
 */
public class EKFont {
	public static final int DEFAULT = 0;

	public static final int SERIF = 1;

	public static final int SANSSERIF = 2;

	public static final int MONOSPACED = 3;

	public static final EKFont FT_SERIF = new EKFont(SERIF);

	public static final EKFont FT_SANSSERIF = new EKFont(SANSSERIF);

	public static final EKFont FT_MONOSPACED = new EKFont(MONOSPACED);

	public static final EKFont FT_DEFAULT = FT_MONOSPACED;

	protected static final String[] fontnames = { "Monospaced", "Serif",
			"SansSerif", "Monospaced" };

	protected int fontcode;

	protected boolean isBold;

	protected boolean isItalic;

	protected int size = 0;

	/**
	 * Creates a font of the given type
	 * 
	 * @param code
	 *          a Font type constant
	 */
	public EKFont(int code) {
		this(code, false, false, 0);
	}

	/**
	 * Creates a font of the given type
	 * 
	 * @param code
	 *          a Font type constant
	 * @param bold
	 *          should the font be bold?
	 * @param italic
	 *          should the font be italic?
	 */
	public EKFont(int code, boolean bold, boolean italic, int aSize) {
		this.fontcode = code;
		this.isBold = bold;
		this.isItalic = italic;
		this.size = aSize;
	}

	public EKFont(java.awt.Font targetFont) {
		String fontFamily = targetFont.getFamily();
		fontcode = -1;
		if ("Monospaced".equalsIgnoreCase(fontFamily))
			fontcode = EKFont.MONOSPACED;
		if ("SansSerif".equalsIgnoreCase(fontFamily))
			fontcode = EKFont.SANSSERIF;
		if ("Serif".equalsIgnoreCase(fontFamily))
			fontcode = EKFont.SERIF;
		if (fontcode == -1)
			fontcode = EKFont.SANSSERIF;
		isBold = targetFont.isBold();
		isItalic = targetFont.isItalic();
		size = targetFont.getSize();
	}

	/**
	 * 
	 * @return true if font ist bold
	 */
	public boolean getIsBold() {
		return isBold;
	}

	/**
	 * Creates a copy with a different bold value
	 * 
	 * @param bold
	 *          whether the copy should be bold
	 * @return the new font
	 */
	public EKFont deriveBold(boolean bold) {
		return new EKFont(this.fontcode, bold, this.isItalic, this.size);
	}

	/**
	 * 
	 * @return true if font is italic
	 */
	public boolean getIsItalic() {
		return isItalic;
	}

	/**
	 * Creates a copy with a different italic value
	 * 
	 * @param italic
	 *          whether the copy should be italic
	 * @return the new font
	 */
	public EKFont deriveItalic(boolean italic) {
		return new EKFont(this.fontcode, this.isBold, italic, this.size);
	}

	/**
	 * 
	 * @return font size or 0 if none is set
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Creates a copy with a different size value
	 * 
	 * @param aSize
	 *          the new size
	 * @return the new font
	 */
	public EKFont deriveSize(int aSize) {
		return new EKFont(this.fontcode, this.isBold, this.isItalic, aSize);
	}

	/**
	 * 
	 * @return the font type constant
	 */
	public int getCode() {
		return fontcode;
	}

	public String toAnimalString() {
		StringBuilder b = new StringBuilder("font ");
		b.append(getFontName());
		if (size != 0) {
			b.append(" size ");
			b.append(size);
		}
		if (isBold)
			b.append(" bold ");
		if (isItalic)
			b.append(" italic ");
		return b.toString();
	}

	public String getFontName() {
		if (fontcode >= 0 && fontcode <= 3)
			return fontnames[fontcode];
		return fontnames[0];
	}

	/**
	 * @return fontName
	 */
	public String toString() {
		return getFontName();
	}

	public static EKFont parseAnimal(String animalCode) {
		// TODO
		String[] t = animalCode.split(" ");
		String fontName = "";
		boolean bold = false;
		boolean italic = false;
		int size = 0;
		int i = 0;
		for (i = 0; i < t.length; i++) {
			String s = t[i];
			if ("font".equals(s))
				fontName = t[++i];
			if ("bold".equals(s))
				bold = true;
			if ("italic".equals(s))
				italic = true;
			if ("size".equals(s))
				size = Integer.parseInt(t[++i]);
		}
		EKFont f = createFromString(fontName);
		if (size > 0)
			f = f.deriveSize(size);
		if (bold)
			f = f.deriveBold(true);
		if (italic)
			f = f.deriveItalic(true);
		return f;
	}

	/**
	 * creates a Font from the given font name string
	 * 
	 * @param s
	 *          the font name
	 * @return the Font instance
	 */
	static public EKFont createFromString(String s) {
		for (int i = 0; i < fontnames.length; i++) {
			if (fontnames[i].equalsIgnoreCase(s))
				return new EKFont(i);
		}
		return null;
	}

	public boolean equals(Object o) {
		if (!(o instanceof EKFont))
			return false;
		if (getCode() == ((EKFont) o).getCode())
			return true;
		return false;
	}
}
