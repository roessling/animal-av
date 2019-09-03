package animal.exchange;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamCorruptedException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.net.URL;
import java.util.Hashtable;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import translator.Translator;
import animal.animator.Animator;
import animal.exchange.animalascii.AnimatorImporter;
import animal.exchange.animalascii.Importer;
import animal.exchange.animalascii.LinkImporter;
import animal.exchange.animalascii.PTGraphicObjectImporter;
import animal.graphics.PTGraphicObject;
import animal.main.Animal;
import animal.main.Animation;
import animal.main.Link;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * This class exports an animation or parts thereof as an ASCII file.
 * 
 * @author Guido R&ouml;&szlig;ling (<a
 *         href="mailto:roessling@acm.org">roessling@acm.org</a>)
 * @version 1.2 2007-09-05
 */
public class AnimalASCIIImporter extends AnimationImporter {
  /**
   * The importer hash table
   */
  private static Hashtable<String, Importer> asciiImporters = new Hashtable<String, Importer>(
      73);

  /**
   * The Animal object used for retrieving the images to export
   */
  protected Animal animal; // = Animal.get();

  /**
   * The animation currently being read in
   */
  private Animation animation;

  /**
   * Determine if file is compressed
   */
  private boolean isCompressed = true;

  /**
   * Used for mapping object IDs, as objects will be renumbered in sequence
   */
  private XProperties objectIDMapper;

  /**
   * The StreamTokenizer used for file parsing
   */
  private StreamTokenizer stok;

  public static Translator translator = new Translator("AnimalASCIIRes",
      Locale.US);

  public static StringBuilder fileContents = null;

  /**
   * creates a new Animal ASCII importer instance
   */
  public AnimalASCIIImporter() {
    // do nothing
  }

  /**
   * Set the format name requested and adjust the import capabilities
   * 
   * @param format
   *          the name of the actual format requested
   */
  public void init(String format) {
    super.init(format);
    isCompressed = format.endsWith("-compressed");
  }

  /**
   * Import the animation to a file of the given name. Note that you must set
   * the animation by calling <code>setAnimation</code> <strong>before</STRON>
   * you call this method.
   * 
   * Use <code>importAnimationFrom(System.out)</code> instead if you want the
   * output to be given on the terminal.
   * 
   * @param filename
   *          the name of the output file to import to.
   * @return the animation that was imported
   */
  public Animation importAnimationFrom(String filename) {
    Animation anim = new Animation();
    InputStream in = null;
    if ((filename == null) || filename.equals("")) {
      return new Animation();
    }

    try {
      if (filename.startsWith("http:") || filename.startsWith("https:")
          || filename.startsWith("file:")) {
        URL targetURL = new URL(filename);
        in = targetURL.openStream();
      } else
        in = new FileInputStream(filename);

      // check if the file is compressed
      try {
        in = new GZIPInputStream(in);
      } catch (IOException e) {
        // could not open the file as a compressed stream. As it could
        // be opened before, this must be an uncompressed stream.
        in.close();
        isCompressed = false;

        // so reopen it for reading.
        in = new FileInputStream(filename);
      }

      System.out.println(filename+" # "+in);
      anim = importAnimationFrom(in, filename);
      in.close();
    } catch (IOException e) {
      MessageDisplay.errorMsg(translateMessage("importException", new String[] {
          filename, e.getMessage() }), MessageDisplay.RUN_ERROR);
    }

    return anim;
  }

  public Animation importAnimationFrom(Reader r, String filename) {
    
    try {
      BufferedReader br = new BufferedReader(r);
      StringBuilder sb = new StringBuilder(65536);
      String currentLine = null;

      while ((currentLine = br.readLine()) != null)
        sb.append(currentLine).append(MessageDisplay.LINE_FEED);

      stok = new StreamTokenizer(new StringReader(sb.toString()));

      // stok = new StreamTokenizer(br);
      stok.eolIsSignificant(true);
      stok.quoteChar('"');
      stok.commentChar('%'); //TODO GR new 20150806
      
      if (animal == null) {
        animal = Animal.get();
      }

      animal.getEditors(); // read in mappings!

      // Parse the file header
      // Should have format "# Animal protocol v\n", where v is the
      // protocol version(currently v=1).
      objectIDMapper = new XProperties();
      // linkNrMapper = new XProperties();

      long timeTaken = System.currentTimeMillis();
      animation = importAnim();
      fileContents = sb;

      if (animation == null) {
        animation = new Animation();
      }

      timeTaken = System.currentTimeMillis() - timeTaken;

      // Link emptyStep = animation.getLink(Link.START);

      objectIDMapper = null;
      // linkNrMapper = null;

      // close the streams...
      br.close();
      r.close();

      // MessageDisplay.message("loadedInIn",
      MessageDisplay.message(translateMessage("loadedInIn",
          new String[] { String.valueOf(timeTaken) }));
    } catch (IOException e) {
      // MessageDisplay.errorMsg("importException",
      MessageDisplay.errorMsg(translateMessage("importException", new String[] {
          filename, e.getMessage() }), MessageDisplay.RUN_ERROR);
    }

    return animation;
  }
  
  
  /**
   * imports a new animation from the input stream or the file name
   * 
   * @param inStream
   *          an input stream. If not null, reads the animation from this stream
   * @param filename
   *          a file name that either describes the name of the already opened
   *          input stream, or contains the animation text itself
   * @return the imported animation
   */
  public Animation importAnimationFrom(InputStream inStream, String filename) {
    InputStream actualStream = inStream;
    try {
      if ((filename != null)
          && (filename.endsWith(".aml") || filename.endsWith(".animal"))
          && !(actualStream instanceof GZIPInputStream)) {
        try {
          actualStream = new GZIPInputStream(actualStream);
        } catch (IOException e) {
          // MessageDisplay.errorMsg("importException",
          MessageDisplay.errorMsg(translateMessage("importException", new String[] {
              filename, e.getMessage() })+" -> Try normal load!", MessageDisplay.RUN_ERROR);
          AnimationImporter importer = AnimationImporter.getImporterFor("animation/animalscript");
          return importer.importAnimationFrom(filename);
        }
      }

      InputStreamReader isr = new InputStreamReader(actualStream);
      BufferedReader br = new BufferedReader(isr);
      StringBuilder sb = new StringBuilder(65536);
      String currentLine = null;

      while ((currentLine = br.readLine()) != null)
        sb.append(currentLine).append(MessageDisplay.LINE_FEED);

      stok = new StreamTokenizer(new StringReader(sb.toString()));

      // stok = new StreamTokenizer(br);
      stok.eolIsSignificant(true);
      stok.quoteChar('"');
      stok.commentChar('%');

      if (animal == null) {
        animal = Animal.get();
      }

      animal.getEditors(); // read in mappings!

      // Parse the file header
      // Should have format "# Animal protocol v\n", where v is the
      // protocol version(currently v=1).
      objectIDMapper = new XProperties();
      // linkNrMapper = new XProperties();

      long timeTaken = System.currentTimeMillis();
      animation = importAnim();
      fileContents = sb;

      if (animation == null) {
        animation = new Animation();
      }

      timeTaken = System.currentTimeMillis() - timeTaken;

      // Link emptyStep = animation.getLink(Link.START);

      objectIDMapper = null;
      // linkNrMapper = null;

      // close the streams...
      br.close();
      isr.close();

      // MessageDisplay.message("loadedInIn",
      MessageDisplay.message(translateMessage("loadedInIn",
          new String[] { String.valueOf(timeTaken) }));
    } catch (IOException e) {
      // MessageDisplay.errorMsg("importException",
      MessageDisplay.errorMsg(translateMessage("importException", new String[] {
          filename, e.getMessage() }), MessageDisplay.RUN_ERROR);
    }

    return animation;
  }

  protected Animation importAnim() {
    int fileID = 0;

    try {
      try {
        ParseSupport.parseMandatoryChar(stok, "#", '#');
      } catch (StreamCorruptedException scee) {
        MessageDisplay.errorMsg(translateMessage("incorrectFormat"),
        // AnimalTranslator.translateMessage("incorrectFormat"),
            MessageDisplay.RUN_ERROR);

        return null;
      }

      animation = new Animation();

      ParseSupport.parseMandatoryWord(stok, "Animal", "Animal");
      ParseSupport.parseMandatoryWord(stok, translateMessage("keyword",
          "protocol"), "protocol");

      int p_version = ParseSupport.parseInt(stok, translateMessage("protocol"));

      if (p_version > Animal.PROTOCOL_VERSION) {
        throw new StreamCorruptedException(translateMessage(
            "invalidOrMissingProtocol", new String[] { String
                .valueOf(stok.nval) }));
      }

      if (ParseSupport.parseOptionalWord(stok,
          translateMessage("animBoundingBox"), "size")) {
        try {
          animation.setWidth(ParseSupport.parseInt(stok,
              translateMessage("animW"), 0));
          ParseSupport.parseMandatoryChar(stok,
              translateMessage("animAsterisk"), '*');
          animation.setHeight(ParseSupport.parseInt(stok,
              translateMessage("animW"), 0));
        } catch (StreamCorruptedException e2) {
          MessageDisplay.errorMsg(translateMessage(
              "animBoundingBoxCorruptButNoMatter", e2.getMessage()),
              MessageDisplay.RUN_ERROR);
        }
      }

      ParseSupport.consumeIncludingEOL(stok, translateMessage("expectedEOL"));

      if (ParseSupport.parseOptionalWord(stok, translateMessage("titleKw"),
          "title")) {
        animation.setTitle(ParseSupport.parseText(stok,
            translateMessage("title")));
        ParseSupport.consumeIncludingEOL(stok, translateMessage("expectedEOL"));
      }

      if (ParseSupport.parseOptionalWord(stok, translateMessage("authorKw"),
          "author")) {
        animation.setAuthor(ParseSupport.parseText(stok,
            translateMessage("author")));
        ParseSupport.consumeIncludingEOL(stok, translateMessage("expectedEOL"));
      }

      // parse and insert the graphic objects
      parseAndInsertGraphicObjects();

      // 2. Check for keyword 'STEPS'
      while (stok.ttype == StreamTokenizer.TT_EOL)
        stok.nextToken();
      stok.pushBack();
      ParseSupport.parseMandatoryWord(stok, translateMessage("linkSteps"),
          "STEPS");

      // 3. Check for ':'
      ParseSupport.parseMandatoryChar(stok, translateMessage("linkColon"), ':');

      // 4. Check for EOL
      ParseSupport.consumeIncludingEOL(stok, translateMessage("expectedEOL"));

      int token = 0;

      // 5. Loop until keyword 'Next' is found at BOL
      while (!ParseSupport.parseOptionalWord(stok, translateMessage("end"),
          "Next")) {
        stok.pushBack();

        // 6. store the file version read in
        fileID = ParseSupport.parseInt(stok, translateMessage("cVersion"));

        // 7. if next token is 'Link', generate Link object and parse it
        if (ParseSupport.parseOptionalWord(stok, translateMessage("linkKw"),
            "Link")) {
          parseAndInsertLink(fileID);
        } else if (ParseSupport.parseOptionalWord(stok,
            translateMessage("linkKw"), "Step")) {
          parseAndInsertAnimator(fileID);
        } else {
          MessageDisplay.errorMsg(stok.sval + "/" + stok.nval + "/"
              + stok.ttype + " @ " + stok.lineno(), MessageDisplay.RUN_ERROR);
        }

        do {
          token = stok.nextToken();
        } while ((token != StreamTokenizer.TT_EOL)
            && (token != StreamTokenizer.TT_EOF));
      }

      // 9. parse ID of next graphic object
      int throwAway = ParseSupport.parseInt(stok, translateMessage("nextGOID"));
      animation.setNextGraphicObjectNum(throwAway + 1);
    } catch (IOException e) {
      MessageDisplay.errorMsg("******" + e.getMessage(),
          MessageDisplay.RUN_ERROR);
    }

    return animation;
  }

  private Importer getLocalImporterFor(String className) {
    try { // try adding exporter

      if (!asciiImporters.containsKey(className)) {
        StringBuilder handlerName = new StringBuilder(
            "animal.exchange.animalascii.");
        handlerName.append(className.substring(className.lastIndexOf('.') + 1));
        handlerName.append("Importer");

        String subName = handlerName.toString();
        @SuppressWarnings("unchecked")
        Class<Importer> c = (Class<Importer>) Class.forName(subName);
        Importer handler = c.newInstance();
        asciiImporters.put(className, handler);
      } // if no handler registered yet
    } // try
    catch (Exception e) {
      MessageDisplay.errorMsg(e.getClass().getName() + "**********"
          + e.getMessage(), MessageDisplay.RUN_ERROR);
    } // try...catch

    return asciiImporters.get(className);
  }

  /**
   * parses and inserts an animator command
   * 
   * @param version
   *          the version information for the animator, important if the format
   *          has changed in newer versions.
   */
  public void parseAndInsertAnimator(int version) {
    // int token;
    int stepNr;
    AnimatorImporter importer = null;
    Animator currentAnimator = null;
    Hashtable<String, String> registeredTypes = Animator.registeredHandlers;
    String objectName = null;

    try {
      while (stok.ttype == StreamTokenizer.TT_EOL)
        stok.nextToken();
     stok.pushBack();

      // 3. next token must be keyword 'object'
      ParseSupport
          .parseMandatoryWord(stok, translateMessage("aStepKw"), "Step");

      // 4. read in animation step (>= 1)
      stepNr = ParseSupport.parseInt(stok, translateMessage("stepNr"));

      // 5. Retrieve the object name
      objectName = ParseSupport.parseWord(stok, translateMessage("objectType"));

      // 6. 'switch' over the type name - actual switch not possible, so use
      // cascading ifs...
      if (registeredTypes.containsKey(objectName.toLowerCase())) {
        String targetType = registeredTypes.get(objectName.toLowerCase());

        try {
          importer = (AnimatorImporter) getLocalImporterFor(targetType);

          Object dummyObject = importer.importFrom(version, stepNr, stok);
          currentAnimator = (dummyObject instanceof Animator) ? (Animator) dummyObject
              : null;
        } catch (Exception e) {
          MessageDisplay.errorMsg("****" + e.getMessage() + " / "
              + e.toString(), MessageDisplay.RUN_ERROR);
        }
      } else {
        MessageDisplay.errorMsg(translateMessage("noSuchHandler", new Object[] {
            objectName.toLowerCase(), String.valueOf(stok.lineno()) }),
            MessageDisplay.RUN_ERROR);
      }

      if (currentAnimator != null) {
        animation.insertAnimator(currentAnimator); // will have next number
        // now!
      }

      // push back the token superfluously read in
      stok.pushBack();
    } catch (IOException e) {
      MessageDisplay.errorMsg("******" + e.getMessage(),
          MessageDisplay.RUN_ERROR);
    }
  }

  /**
   * parses and inserts a graphical object
   */
  public void parseAndInsertGraphicObjects() {
    int objectID;
    int fileID;
    PTGraphicObjectImporter importer = null;
    PTGraphicObject currentObject = null;
    Hashtable<String, String> registeredTypes = PTGraphicObject.registeredTypes;
    String objectName = null;
    // int ptgoCounter = 0;

    try {
      // 1. loop over all components until a word is reached
      while (stok.nextToken() != StreamTokenizer.TT_WORD) {
        while (stok.ttype == StreamTokenizer.TT_EOL)
          stok.nextToken();
//        stok.pushBack();
        // 1a. push back the token read in...
        stok.pushBack();

        // 2. first token should be FILE_VERSION
        fileID = ParseSupport.parseInt(stok, translateMessage("goVersion"), 1);

        // 3. next token must be keyword 'object'
        ParseSupport.parseMandatoryWord(stok, translateMessage("goObjectKw"),
            "object");

        // 4. read in file number(>= 1)
        objectID = ParseSupport.parseInt(stok, translateMessage("goid"), 1);

        // 5. Retrieve the object name
        objectName = ParseSupport.parseWord(stok, translateMessage("goType"));

        // 6. 'switch' over the type name - actual switch not possible,
        // so use cascading ifs...
        if (registeredTypes.containsKey(objectName.toLowerCase())) {
          String targetType = registeredTypes.get(objectName.toLowerCase());

          try {
            importer = (PTGraphicObjectImporter) getLocalImporterFor(targetType);

            Object dummyObject = importer.importFrom(fileID, stok);
            currentObject = (dummyObject instanceof PTGraphicObject) ? (PTGraphicObject) dummyObject
                : null;
            ParseSupport.consumeIncludingEOL(stok, "++");
          } catch (Exception e) {
            MessageDisplay.errorMsg("****" + e.getMessage() + " / "
                + e.toString(), MessageDisplay.RUN_ERROR);
          }
        } else {
          MessageDisplay.errorMsg(translateMessage("noSuchHandler",
              new Object[] { objectName.toLowerCase(),
                  String.valueOf(stok.lineno()) }), MessageDisplay.RUN_ERROR);

          int token = -21;
          do {
            token = stok.nextToken();
          } while (token != StreamTokenizer.TT_EOL);
        }

        if (currentObject != null) {
          currentObject.resetNum();
          currentObject.setNum(objectID);
          // will have next number now!
          animation.insertGraphicObject(currentObject);
          objectIDMapper.put(String.valueOf(objectID), currentObject
              .getNum(false));
        }
      } // while != WORD

      // push back the token superfluously read in
      stok.pushBack();
    } catch (IOException e) {
      MessageDisplay.errorMsg("******" + e.getMessage(),
          MessageDisplay.RUN_ERROR);
    }
  }

  /**
   * parses and inserts a new animation step link
   * 
   * @param version
   *          the version of the link. Important if the format has changed
   *          between versions.
   */
  public void parseAndInsertLink(int version) {
    Importer importer = getLocalImporterFor("animal.main.Link");

    if ((importer != null) && importer instanceof LinkImporter) {
      LinkImporter linkImporter = (LinkImporter) importer;
      Object linkObject = linkImporter.importFrom(version, stok);

      if ((linkObject != null) && linkObject instanceof Link
          && (((Link) linkObject).getStep() != Link.END)) {
        animation.insertLink((Link) linkObject);
      }
    }
  }

  /**
   * returns the default file extension
   * 
   * @return the default file extension, either "aml" for compressed input or
   *         "ama" for uncompressed input
   */
  public String getDefaultExtension() {
    return (isCompressed) ? "aml" : "ama";
  }

  /**
   * returns the MIME type describing the input format handled by this parser
   * 
   * @return either "animation/animal-ascii-compressed" for compressed input, or
   *         "animation/animal-ascii" for uncompressed input
   */
  public String getMIMEType() {
    return "animation/animal-ascii" + ((isCompressed) ? "-compressed" : "");
  }

  /**
   * returns the format description String
   * 
   * @return the format description String, in this case the result of invoking
   *         toString()
   * @see #toString()
   */
  public String getFormatDescription() {
    return toString();
  }

  /**
   * Use this method to provide a short(single line) description of the importer
   * 
   * @return a String describing this importer
   */
  public String toString() {
    return translateMessage("asciiImportDescription");
  }

  public static String translateMessage(String key) {
    return translator.translateMessage(key);
  }

  public static String translateMessage(String key, Object[] params) {
    return translator.translateMessage(key, params);
  }

  public static String translateMessage(String key, String... params) {
    return translator.translateMessage(key, params);
  }

  public static String translateMessage(String key, Object param) {
    return translator.translateMessage(key, new Object[] { param });
  }
}
