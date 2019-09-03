import java.awt.Font;
import java.awt.FontMetrics;

import animal.main.Animal;

public class JarTest {

	public static void main(String[] args) {
//		try {
//		JarFile jar = new JarFile("c:\\Temp\\Silke.jar");
//		Enumeration entries = jar.entries();
//		while (entries.hasMoreElements()) {
//			JarEntry entry = (JarEntry)entries.nextElement();
//			System.err.println(entry.toString());
//		}
//		} catch(Exception e) {
//			System.err.println("EXC: " +e.getMessage());
//		}
		
		String[] strings = new String[] {"Hi", "all small", "ALL LARGE", "LaTeX",
				"queerLongY", "gfqpiLYA"};
		Font f = new Font("SansSerif", 12, Font.PLAIN);
		FontMetrics fm = Animal.getConcreteFontMetrics(f);
		for (int i=0; i<strings.length; i++) {
			System.err.println(strings[i] +" -> " +fm.stringWidth(strings[i]));
			
		}
	}
}
