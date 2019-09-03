package generators.network.helper;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Provides basic functionality around class and package names.
 */
public class ClassName {
	/**
	 * Get only the class name without any package information
	 * 
	 * @param o An object of the class to get the name for
	 * @return The class name
	 */
	public static String getClassNameOnly(Object o) {
		String withPackage = o.getClass().getName();
		return withPackage.substring(withPackage.lastIndexOf(".") + 1);
	}
	
	/**
	 * Get the path of the folder where the class resides in the file system 
	 * relative to the java project's root (the package path).<br />
	 * <b>Mind: The returned path ends with a path separator!</b>
	 * 
	 * @param o An object of the class to get the path for
	 * @return The path
	 */
	public static String getPackageAsPath(Object o) {
		return  o.getClass().getPackage().getName().replace(".", "/").concat("/");
	}
}
