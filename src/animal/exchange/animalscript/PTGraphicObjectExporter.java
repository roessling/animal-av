package animal.exchange.animalscript;

import java.util.Hashtable;

import animal.graphics.PTGraphicObject;

public class PTGraphicObjectExporter implements Exporter {
	protected static Hashtable<PTGraphicObject, String> hasBeenExported =
		new Hashtable<PTGraphicObject, String>(403);

	public static boolean getExportStatus(PTGraphicObject ptgo) {
		return hasBeenExported.containsKey(ptgo);
	}

	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param ptgo the graphical object to export; will cause an error in this
	 * class (must be used in subclasses!)
	 */
	public String getExportString(PTGraphicObject ptgo) {
		return ("Undefined primitive: " + getClass().getName() + " for " +ptgo);
	}
}
