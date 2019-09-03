/*
 * Created on 02.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.ahrgr.animal.kohnert.asugen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author ek
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EmbeddedPropertiesFilter extends Reader {

	BufferedReader br;

	boolean startTagFound = false;

	boolean eof = false;

	StringBuilder buffer = new StringBuilder();

	int bufferpos;

	public EmbeddedPropertiesFilter(Reader r) {
		br = new BufferedReader(r);
	}

	/**
	 * sucht die naechste Kommentarzeile
	 * 
	 * @return die Zeile ohne Kommentarzeichen
	 * @throws IOException
	 */
	public String getNextCommentLine() throws IOException {
		String s = null;
		while ((s = br.readLine()) != null) {
			if (s.startsWith("#"))
				return s.substring(1);
		}
		return null;
	}

	/**
	 * Finden des Start Tags, falls noch nicht gefunden
	 * 
	 * @return if a start tag was found
	 * @throws IOException
	 */
	public boolean findStartTag() throws IOException {
		String s = null;
		if (startTagFound)
			return true;
		while ((s = getNextCommentLine()) != null) {
			if (s.startsWith("!!!EMBEDDED_PROPERTIES")) {
				startTagFound = true;
				return true;
			}
		}
		return false;
	}

	public int read() throws IOException {
		if (!findStartTag() || eof)
			return -1;
		if (buffer != null && bufferpos < buffer.length()) {
			return buffer.charAt(bufferpos++);
		}
		String s = getNextCommentLine();
		if (s == null)
			return -1;
		if (s.equals("!!!END_EMBEDDED_PROPERTIES")) {
			eof = true;
			return -1;
		}
		buffer = new StringBuilder(s);
		buffer.append("\n");
		bufferpos = 0;
		if (buffer.length() > 0)
			return buffer.charAt(bufferpos++);

		return -1; // END OF STREAM
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		if (!findStartTag())
			return -1;
		if (len <= 0)
			return 0;
		// ineffizient - nur ein zeichen kopieren
		int i = read();
		if (i < 0)
			return i;
		cbuf[off] = (char) i;
		return 1;
	}

	public void close() throws IOException {
		br.close();
	}

	public static void test(File f) {
		try {
			EmbeddedPropertiesFilter af = new EmbeddedPropertiesFilter(
					new FileReader(f));
			BufferedReader r = new BufferedReader(af);
			String s;
			while ((s = r.readLine()) != null)
				System.out.println(s);
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
