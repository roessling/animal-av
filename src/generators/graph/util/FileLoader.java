/**
 * 
 */
package generators.graph.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

/**
 * @author marc
 *
 */
public class FileLoader {
	public static String loadContent(String fileName, String defaultText) {
		try (InputStream sourceCodeUrl = FileLoader.class.getClassLoader().getResourceAsStream(fileName);	
				InputStreamReader inputReader = new InputStreamReader(sourceCodeUrl);
				BufferedReader reader = new BufferedReader(inputReader)) {
			
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				if (sb.length() > 0)
					sb.append("\n");
				sb.append(line);
			}
			
			return sb.toString();
		} catch (IOException e) {			
			e.printStackTrace();
			
			return defaultText;
		}
	}
	
	public static List<Text> loadContent(String fileName, Language lang, String defaultText, String lineLabel, Function<List<Text>, Primitive> lastPrimitiveSupplier, TextProperties textProperties) {
		List<Text> result = new ArrayList<Text>();
		
		try (InputStream sourceCodeUrl = FileLoader.class.getClassLoader().getResourceAsStream(fileName);	
				InputStreamReader inputReader = new InputStreamReader(sourceCodeUrl);
				BufferedReader reader = new BufferedReader(inputReader)) {
			
			String line;
			while ((line = reader.readLine()) != null) {
				result.add(lang.newText(new Offset(0, 5, lastPrimitiveSupplier.apply(result), "SW"), line.replaceAll("\"", "\\\\\""), String.format("%s%d", lineLabel, result.size()), null, textProperties));
			}
		} catch (IOException e) {			
			e.printStackTrace();
			
			result.add(lang.newText(new Offset(0, 5, lastPrimitiveSupplier.apply(result), "SW"), defaultText, String.format("%s%d", lineLabel, result.size()), null, textProperties));
		}	
		
		return result;
	}
}
