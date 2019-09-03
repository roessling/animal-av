package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.io.StreamTokenizer;

import animal.exchange.AnimalASCIIImporter;
import animal.main.Link;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;
import animalscript.core.AnimalParseSupport;

/**
 * This class imports link objects from Animal's ASCII format
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 0.7 05.09.2007
 */
public class LinkImporter implements Importer {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param version
   *          the version of the Link to be parsed
   * @param stok
   *          the StreamTokenizer used for parsing the link
   */
  public Object importFrom(int version, StreamTokenizer stok) {
    XProperties props = new XProperties();
    try {
      // 1. Check if file ID is OK
      if (version > Link.getFileVersion())
        throw new StreamCorruptedException(
            AnimalASCIIImporter.translateMessage("wrongLinkVersion",
                new Integer[]{version, Link.getFileVersion()}
        ));

      if (!ParseSupport.parseOptionalWord(stok, 
          AnimalASCIIImporter.translateMessage("otKw", 
              new String[] { "Link", "end" }), "END")) { // check
        // for
        // END
        // 2. "Link" was already read in; read in step number
        props.put(Link.STEP_LABEL, ParseSupport.parseInt(stok, "Link number"));

        // 3. Parse words 'next' and 'step'
        ParseSupport.parseMandatoryWord(stok,  
            AnimalASCIIImporter.translateMessage("otKw", 
                new String[] { "Link", "next"}), "next");
        ParseSupport.parseMandatoryWord(stok,  
            AnimalASCIIImporter.translateMessage("otKw", 
                new String[] { "Link", "step"}), "step");

        // 4. Parse next link number
        props.put(Link.NEXT_STEP_LABEL, ParseSupport.parseInt(stok,
            AnimalASCIIImporter.translateMessage("otKw", 
                new String[] { "Link", "next link number"})));

        // 5. Parse 'after'
        ParseSupport.parseMandatoryWord(stok,  
            AnimalASCIIImporter.translateMessage("otKw", 
                new String[] { "Link", "after"}), "after");

        // 6. Check which switching animator to use
        if (ParseSupport.parseOptionalWord(stok,  
            AnimalASCIIImporter.translateMessage("otKw", 
                new String[] { "Link", "time"}), "time")) {
          // 7. set mode
          props.put(Link.LINK_MODE, Link.WAIT_TIME);

          // 8. parse waiting time
          props.put(Link.TIME_LABEL, ParseSupport.parseInt(stok,
              AnimalASCIIImporter.translateMessage("linkTimedWaitPeriod")));

          // 9. parse keyword 'ms'
          ParseSupport.parseMandatoryWord(stok, 
              AnimalASCIIImporter.translateMessage("otKw", 
                  new String[] { "Link", "ms"}),
              "ms");
        } else if (ParseSupport.parseOptionalWord(stok, 
            AnimalASCIIImporter.translateMessage("otKw", 
                new String[] { "Link", "key"}),
            "key")) {
          // 10. set animation mode
          props.put(Link.LINK_MODE, Link.WAIT_KEY);

          // 11. parse keyword 'press'
          ParseSupport
              .parseMandatoryWord(stok, 
                  AnimalASCIIImporter.translateMessage("otKw", 
                      new String[] { "Link", "press"}), "press");
        } // space left for other effects...
        else if (ParseSupport.parseOptionalWord(stok, 
            AnimalASCIIImporter.translateMessage("otKw", 
                new String[] { "Link", "click"}),
            "click")) {
          // 10. set animation mode
          props.put(Link.LINK_MODE, Link.WAIT_CLICK);

          ParseSupport.parseMandatoryWord(stok, 
              AnimalASCIIImporter.translateMessage("otKw", 
                  new String[] { "Link", "on"}), "on");
          ParseSupport.parseMandatoryWord(stok, 
              AnimalASCIIImporter.translateMessage("otKw", 
                  new String[] { "Link", "ID"}), "id");
          props.put(Link.TARGET_OBJECT_ID, ParseSupport.parseInt(stok,
              AnimalASCIIImporter.translateMessage("linkTargetID")));
        } else
          ParseSupport.formatException(
              AnimalASCIIImporter.translateMessage("unknownLinkTransition", stok.sval), 
              stok);
      } else {
        // OK, set to "nextStep == END"
        props.put(Link.STEP_LABEL, Link.END);
        props.put(Link.NEXT_STEP_LABEL, Link.END);
      }

      if (ParseSupport.parseOptionalWord(stok, 
          AnimalASCIIImporter.translateMessage("otKw", 
              new String[] { "Link", "label"}), "label"))
        props.put(Link.LINK_NAME, AnimalParseSupport.parseText(stok,
            "Link label"));

      // ParseSupport.parseMandatoryWord(stok, "Link keyword 'click'", "click");
      // ParseSupport.parseMandatoryWord(stok, "Link keyword 'on'", "on");
      // ParseSupport.parseMandatoryWord(stok, "Link keyword 'ID'", "id");
      // props.put(Link.TARGET_OBJECT_ID, ParseSupport.parseInt(stok, "Link
      // target ID"));
      // }

    } catch (IOException e) {
      MessageDisplay.errorMsg("ZZZ" + e.getMessage(), MessageDisplay.RUN_ERROR);
    }
    return new Link(props);
  }
}
