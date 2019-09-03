package animal.exchange.animalscript;

import animal.main.Link;
import animal.misc.MessageDisplay;

public class LinkExporter implements Exporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param link
	 *          the current Link object
	 */
	public String getExportString(Link link) {
		if (link.getStep() == 1)
			return "{";
		StringBuilder sb = new StringBuilder(200);
		sb.append("}");
		if (link.getMode() == Link.WAIT_TIME)
			sb.append(" after ").append(link.getTime()).append(" ms");
		if (link.getStep() != Link.END)
			sb.append(MessageDisplay.LINE_FEED).append("{  ");
        if (link.getLinkLabel() != null) {
          sb.append(MessageDisplay.LINE_FEED).append("  label \"");
          sb.append(link.getLinkLabel()).append("\"");
        }
		return sb.toString();
	}
}
