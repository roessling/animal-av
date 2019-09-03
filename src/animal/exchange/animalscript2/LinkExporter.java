package animal.exchange.animalscript2;

import algoanim.primitives.generators.Language;
import animal.main.Link;

public class LinkExporter implements Exporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param link
	 *          the current Link object
	 */
//	public String getExportString(Link link) {
	public void export(Language l, Link link) {
		String label = link.getLinkLabel();
		boolean useLabel = (label != null);
		if (useLabel) {
			if (link.getMode() == Link.WAIT_TIME)
				l.nextStep(link.getTime(), label);
			else
				l.nextStep(label);
		} else if (link.getMode() == Link.WAIT_TIME)
			l.nextStep(link.getTime());
		else l.nextStep();
		
//		switch(link.getMode()) {
//			case Link.WAIT_TIME:
//				l.nextStep(link.getTime());
//				break;
//			case Link.WAIT_KEY:
//				l.nextStep();
//		}
////		if (link.getStep() == 1)
////			return "{";
//		StringBuilder sb = new StringBuilder(200);
//		sb.append("}");
//		if (link.getMode() == Link.WAIT_TIME)
//			sb.append(" after ").append(link.getTime()).append(" ms");
//		if (link.getStep() != Link.END)
//			sb.append(MessageDisplay.LINE_FEED).append("{  ");
//        if (link.getLinkLabel() != null) {
//          sb.append(MessageDisplay.LINE_FEED).append("  label \"");
//          sb.append(link.getLinkLabel()).append("\"");
//        }
//		return sb.toString();
	}
}
