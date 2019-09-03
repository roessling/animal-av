package animal.exchange.animalscript2;

import java.util.Hashtable;
import java.util.Vector;

import algoanim.primitives.generators.Language;
import animal.animator.Animator;
import animal.graphics.PTGraphicObject;

public abstract class AnimatorExporter implements Exporter {
	static PTGraphicObject[] graphicObjects = null;
	
	public AnimatorExporter() {
		// Do nothing here
	}

	static Hashtable<String, Exporter> objectExporters = 
		new Hashtable<String, Exporter>(53);

	public static void setGraphicObjects(Vector<PTGraphicObject> objects) {
		int nrOfGraphicObjects = objects.size(); // , pos = 0;
		PTGraphicObject lastGO = objects.lastElement();
		int lastObjectNumber = -1;
		for (int i = 0; i < nrOfGraphicObjects; i++) {
			lastGO = objects.elementAt(i);
			if (lastGO.getNum(false) > lastObjectNumber) {
				lastObjectNumber = lastGO.getNum(false);
			}
		}
		graphicObjects = new PTGraphicObject[lastObjectNumber + 1];
		PTGraphicObject currentObject = null;
		for (int i = 0; i < nrOfGraphicObjects; i++) {
			currentObject = objects.elementAt(i);
			if (currentObject != null) {
				graphicObjects[currentObject.getNum(false)] = currentObject;
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(2048);
		for (int i = 0; i < graphicObjects.length; i++) {
			sb.append("@").append(i).append(": ").append(
					graphicObjects[i].getNum(false));
		}
		return sb.toString();
	}

	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param animator
	 *          the current Animator object
	 */
	public abstract void export(Language lang, Animator animator);

	/**
	 * Export the requested object IDs Note: the animator is responsible for
	 * exporting any not yet exported objects!
	 * 
	 * @param animator
	 *          the current animator, used for retrieving the object IDs
	 */
	public void exportObjectIDs(Language lang, Animator animator) {
		int[] objectNums = animator.getObjectNums();
		exportObjectIDs(lang, objectNums);
	}

	public void exportObjectIDs(Language lang, int objectNum) {
		exportObjectIDs(lang, new int[] { objectNum });
	}

	public void exportUsedObjects(Language lang, int objectNum) {
		exportUsedObjects(lang, new int[] { objectNum }, true, 0, 0, true);
	}

	public void exportUsedObjects(Language lang, int[] objectNums) {
		exportUsedObjects(lang, objectNums, true, 0, 0, true);
	}

	public void exportUsedObjects(Language lang, int[] objectNums, boolean isVisible) {
		exportUsedObjects(lang, objectNums, isVisible, 0, 0, true);
	}

	@SuppressWarnings("unchecked")
  public void exportUsedObjects(Language lang, int[] objectNums, boolean isVisible, 
			int offset, int duration, boolean timeUnitIsTicks) {
		PTGraphicObject ptgo = null;
		// String currentName = null;
		String className = null, subName = null;
		int i;
//		StringBuilder sb = new StringBuilder();
		for (i = 0; i < objectNums.length; i++) { // for (i=0;)
			ptgo = graphicObjects[objectNums[i]];
			if (ptgo != null) { // if (ptgo != null)
				if (!PTGraphicObjectExporter.getExportStatus(ptgo)) { // if not exported
																															// yet
					className = ptgo.getClass().getName();
					try { // try adding exporter
						if (!objectExporters.containsKey(className)) { // object exporter
																														// not registered
							StringBuilder handlerName = new StringBuilder(
									"animal.exchange.animalscript2.");
							handlerName.append(className
									.substring(className.lastIndexOf('.') + 1));
							handlerName.append("Exporter");
							subName = handlerName.toString();
							Class<PTGraphicObjectExporter> c = 
								(Class<PTGraphicObjectExporter>)Class.forName(subName);
							PTGraphicObjectExporter handler = c.newInstance();
							objectExporters.put(className, handler);
						} // if (!registered
						PTGraphicObjectExporter localHandler =
							(PTGraphicObjectExporter)objectExporters.get(className);
						localHandler.export(lang, ptgo, isVisible, offset, 
								duration, timeUnitIsTicks);
//						sb.append(localHandler.getExportString(ptgo));
//						if (!isVisible)
//							sb.append(" hidden");
//						sb.append(MessageDisplay.LINE_FEED).append("  ");
					} // try adding exporter
					catch (Exception e) {
		    		// do nothing
					} // catch
				} // not yet exported
			} // not null?
		} // end loop
//		return sb.toString();
	}

	public String exportObjectIDs(Language lang, int[] objectNums) {
		PTGraphicObject ptgo = null;
		String currentName = null; // , className = null, subName = null;;
		int i;
		StringBuilder sb = new StringBuilder();

		// 3. write out the numbers of objects worked on
		for (i = 0; objectNums != null && i < objectNums.length; i++) {
			sb.append(" \"");
			ptgo = graphicObjects[objectNums[i]];
			currentName = ptgo.getObjectName();
			if (currentName == null)
				currentName = String.valueOf(ptgo.getNum(false));
			sb.append(currentName);
			sb.append("\"");
		}
		return sb.toString();
	}
}
