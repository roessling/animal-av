package generators.misc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FdUtil {
	

	private static final String ATTR_SEPARATOR = ";";
	public static final String DEPENDENCY_ARROW = "->";

	/**
	 * primitive parsing variant
	 * 
	 * @param dependencies
	 *            The functional dependencies left hand side have to be separated by
	 *            an arrow "->" from the right hand side and the attributes have to
	 *            be separated by commas. Example: "A,B,C->D,E,F"
	 */
	public static List<FD> parseDependencies(String[] dependencies) {
		List<FD> deps = new ArrayList<>(dependencies.length);

		for (int i = 0; i < dependencies.length; i++) {
			String[] sides = dependencies[i].split(DEPENDENCY_ARROW);
			deps.add(new FD(FdUtil.convertToAttribute(sides[0].split(ATTR_SEPARATOR)),
					FdUtil.convertToAttribute(sides[1].split(ATTR_SEPARATOR))));
		}

		return deps;
	}

	public static Attribute[] convertToAttribute(String[] attributes) {
		Attribute[] result = new Attribute[attributes.length];

		for (int i = 0; i < attributes.length; i++) {
			result[i] = new Attribute(attributes[i]);
		}

		return result;
	}

	public static String printAttributes(List<Attribute> attributes) {
		return attributes.stream().map(n -> n.getSymbol()).collect(Collectors.joining(","));
	}

}
