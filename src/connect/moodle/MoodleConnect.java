package connect.moodle;

import interactionsupport.models.InteractionModel;
import interactionsupport.models.QuestionModel;
import interactionsupport.models.QuestionResult;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.JOptionPane;

import animal.exchange.AnimalZipExporter;

/**
 * Class providing support for the connection to Moodle.
 * 
 * @author Simon Sprankel <sprankel@rbg.informatik.tu-darmstadt.de>
 * @see <a href="http://moodle.org/">http://moodle.org/</a>
 */
public class MoodleConnect {

  /**
   * The ID of the course in Moodle.
   */
  private int             courseID;

  /**
   * The ID of the assignment in Moodle.
   */
  private int             assignmentID;

  /**
   * The ID of the user in Moodle.
   */
  private int             userID;

  /**
   * The URL to the Moodle import script, e.g.
   * "http://www.mymoodlesite.com/mod/assignment/type/animation/import_results.php"
   * .
   */
  private String          moodleImportURL;

  /**
   * Whether the animation code should be transmitted to Moodle.
   */
  private boolean         saveAnimation;

  /**
   * The salt for the secure hash.
   */
  private String          salt;

  /**
   * The definition type of the animation (0 for generator definition and 1 for
   * Animalscript file).
   */
  private int             definitionType;

  /**
   * The language of the generator, e.g. "en".
   */
  private String          generatorLanguage;

  /**
   * The code language of the generator, e.g. "Java".
   */
  private String          generatorCodeLanguage;

  /**
   * The type of the generator as an int as defined in
   * generators.framework.GeneratorType.
   */
  private String          generatorType;

  /**
   * The algorithm name of the generator, e.g. "MoodleConnect Presentation";
   */
  private String          generatorAlgorithm;

  /**
   * The name of the generator, e.g. "MoodleConnect Presentation";
   */
  private String          generatorName;

  /**
   * The URL to the ZIP file containing the animation definition file, the
   * interaction definition file and a manifest.
   */
  private String          animalZip;

  public static final int DEFINITION_TYPE_GENERATOR    = 0;

  public static final int DEFINITION_TYPE_ANIMALSCRIPT = 1;

  /**
   * Parses the Moodle-specific arguments out of the given arguments array. Test
   * args for loading a generator: --moodlecall --moodleCourseID=2
   * --moodleAssignmentID=3 --moodleUserID=3
   * --moodleImportURL=http://localhost/moodle22
   * /mod/assignment/type/animalanimation/import_results.php
   * --moodleSaveAnimation=true --moodleSalt=RANDOMSTRING
   * --moodleDefinitionType=0 --moodleGeneratorLanguage=en
   * --moodleGeneratorCodeLanguage=Pseudo-Code --moodleGeneratorType=Misc
   * --moodleGeneratorAlgorithm="MoodleConnect Presentation"
   * --moodleGeneratorName="MoodleConnect Presentation" args for loading an
   * animation: --moodlecall --moodleCourseID=2 --moodleAssignmentID=16
   * --moodleUserID=2
   * --moodleImportURL=http://localhost/moodle22/mod/assignment/
   * type/animalanimation/import_results.php --moodleSaveAnimation=true
   * --moodleSalt=RANDOMSTRING --moodleDefinitionType=1
   * --moodleAnimationDefinition=animation/animalscript
   * --moodleInteractionDefinition
   * =http://localhost/moodle/mod/assignment/type/animalanimation
   * /tmp/testanimation.asu
   * 
   * @param args
   *          the arguments of the main function
   * @return the arguments that are not Moodle-specific
   */
  public String[] parseArguments(String[] args) {
    ArrayList<String> remainingArgs = new ArrayList<String>();
    for (String arg : args) {
      // if argument is not a Moodle argument, ignore it in this function
      if (!arg.startsWith("--moodle")) {
        remainingArgs.add(arg);
        continue;
        // parse Moodle arguments and save them in the object's attributes
      } else if (arg.startsWith("--moodleCourseID=")) {
        this.courseID = Integer.parseInt(arg.substring(17));
      } else if (arg.startsWith("--moodleAssignmentID=")) {
        this.assignmentID = Integer.parseInt(arg.substring(21));
      } else if (arg.startsWith("--moodleUserID=")) {
        this.userID = Integer.parseInt(arg.substring(15));
      } else if (arg.startsWith("--moodleImportURL=")) {
        this.moodleImportURL = arg.substring(18);
      } else if (arg.startsWith("--moodleSaveAnimation=")) {
        this.saveAnimation = Boolean.parseBoolean(arg.substring(22));
      } else if (arg.startsWith("--moodleSalt=")) {
        this.salt = arg.substring(13);
      } else if (arg.startsWith("--moodleDefinitionType=")) {
        this.definitionType = Integer.parseInt(arg.substring(23));
      } else if (arg.startsWith("--moodleGeneratorLanguage=")) {
        this.generatorLanguage = arg.substring(26);
      } else if (arg.startsWith("--moodleGeneratorCodeLanguage=")) {
        this.generatorCodeLanguage = arg.substring(30);
      } else if (arg.startsWith("--moodleGeneratorType=")) {
        this.generatorType = arg.substring(22);
      } else if (arg.startsWith("--moodleGeneratorAlgorithm=")) {
        this.generatorAlgorithm = arg.substring(27);
      } else if (arg.startsWith("--moodleGeneratorName=")) {
        this.generatorName = arg.substring(22);
      } else if (arg.startsWith("--moodleAnimalZip=")) {
        this.animalZip = arg.substring(18);
      }
    }

    // add "normal" parameter loading the animation
    if (this.definitionType == DEFINITION_TYPE_GENERATOR) {
      // if the animation should be loaded with a generator
      remainingArgs.add("-generator");
      remainingArgs.add("language=" + this.generatorLanguage);
      remainingArgs.add("codelanguage=" + this.generatorCodeLanguage);
      remainingArgs.add("type=" + this.generatorType);
      remainingArgs.add("algorithm=" + this.generatorAlgorithm);
      remainingArgs
          .add("generatorname=" + this.generatorName.replace(' ', '_'));
    } else if (this.definitionType == DEFINITION_TYPE_ANIMALSCRIPT) {
      // if the animation should be loaded with a ZIP file, add the URL to the
      // file as a "normal" parameter
      remainingArgs.add("animation/animal-zip");
      remainingArgs.add("" + this.animalZip);
    }

    String[] result = new String[remainingArgs.size()];
    return remainingArgs.toArray(result);
  }

  /**
   * Determines whether JAR has been called by Moodle.
   * 
   * @param args
   *          the arguments passed to the main function
   * @return whether JAR has been called by Moodle
   */
  public static boolean calledByMoodle(String[] args) {
    // if no arguments are given, JAR is not called by Moodle
    if (args == null || args.length == 0)
      return false;
    List<String> argsList = Arrays.asList(args);
    // if the --moodlecall flag is set, JAR has been called by Moodle
    if (argsList.contains("--moodlecall"))
      return true;
    return false;
  }

  /**
   * Generates the hash out of the attributes that has to be transmitted to
   * Moodle.
   * 
   * @return the generated hash
   */
  public String generateHash() {
    String toHash = this.courseID + "" + this.assignmentID + "" + this.userID
        + this.salt;
    MessageDigest md = null;
    byte[] hash = null;
    try {
      md = MessageDigest.getInstance("SHA-512");
      hash = md.digest(toHash.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return convertToHex(hash);
  }

  /**
   * Converts the given byte[] to a hex string.
   * 
   * @param raw
   *          the byte[] to convert
   * @return the string the given byte[] represents
   */
  private String convertToHex(byte[] raw) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < raw.length; i++) {
      sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  /**
   * Sends the results to Moodle.
   * 
   * @param questions
   *          The map mapping the question IDs to the corresponding questions.
   */
  public void sendResults(Map<String, InteractionModel> questions) {
    Map<String, QuestionResult> questionGroupResults = questionGroupResults(questions);

    try {
      // open a connection to the Moodle site and prepare it for sending
      // multipart data
      URL url = new URL(moodleImportURL);
      URLConnection con = url.openConnection();
      // create a boundary - the boundary must not be contained in the content!
      // the boundary is used to delimit the different messages
      String boundary = (UUID.randomUUID().toString() + UUID.randomUUID()
          .toString()).substring(0, 65);
      con.setRequestProperty("Content-Type", "multipart/form-data; boundary=\""
          + boundary + "\"");
      con.setDoOutput(true);
      con.setUseCaches(false);
      // use a BufferedOutputStream for performance reasons
      BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
      // use a PrintStream to comfortably write strings to the output stream
      PrintStream ps = new PrintStream(bos, false, "UTF-8");
      // send the courseID, the assignmentID, the userID and the calculated hash
      printFormElement(ps, boundary, "courseID", courseID + "");
      printFormElement(ps, boundary, "assignmentID", assignmentID + "");
      printFormElement(ps, boundary, "userID", userID + "");
      printFormElement(ps, boundary, "hash", generateHash());
      // send the results as a JSON encoded string, e.g.
      // {"questionGroup1":[1,2]}
      String jsonResult = "{";
      for (String questionGroupID : questionGroupResults.keySet()) {
        QuestionResult result = questionGroupResults.get(questionGroupID);
        jsonResult += "\"" + questionGroupID + "\":["
            + result.getAchievedPoints() + "," + result.getPossiblePoints()
            + "],";
      }
      jsonResult = jsonResult.substring(0, jsonResult.length() - 1) + "}";
      printFormElement(ps, boundary, "results", jsonResult);
      ps.flush();

      // if the generated animation should be transferred, send it
      DataOutputStream dos = new DataOutputStream(ps);
      if (saveAnimation) {
        ps.println("--" + boundary);
        ps.println("Content-Disposition: form-data; name=\"animalZIP\"; filename=\"animal.zip\"");
        ps.println("Content-Type: application/zip");
        ps.println();
        ps.flush();
        // write the Animal ZIP in a temporary file
        AnimalZipExporter exporter = new AnimalZipExporter();
        String filename = UUID.randomUUID().toString() + ".zip";
        exporter.exportAnimationTo(filename);
        FileInputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[0xFFFF];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
          dos.write(buffer, 0, bytesRead);
        }
        dos.flush();
        fis.close();
        // delete the temporary file
        File file = new File(filename);
        file.delete();
      }
      ps.println();
      ps.println("--" + boundary + "--");
      // this flushes and closes all streams
      dos.close();

      // we have to get the input stream in order to actually send the request
      InputStream is = con.getInputStream();
      // get the Moodle result message and show it in a popup
      @SuppressWarnings("resource")
      Scanner scanner = new Scanner(is).useDelimiter("\\A");
      if (scanner.hasNext()) {
        String result = scanner.next();
        JOptionPane.showMessageDialog(null, result);
      }
      scanner.close();
//      is.close();

    } catch (MalformedURLException e) {
      System.err
          .println("The URL to the moodle site specified by the JNLP file is not a valid URL.");
      e.printStackTrace();
    } catch (IOException e) {
      System.err
          .println("The connection to the moodle site could not be established.");
      e.printStackTrace();
    }
  }

  /**
   * Computes the results in each question group based on the results of the
   * single questions.
   * 
   * @param questions
   *          a map mapping the question ID to the corresponding question result
   * @return a map mapping the question group IDs to the results achieved in
   *         this question group
   */
  private Map<String, QuestionResult> questionGroupResults(
      Map<String, InteractionModel> questions) {
    Map<String, QuestionResult> questionGroupResults = new HashMap<String, QuestionResult>();
    if (questions == null) {
      return questionGroupResults;
    }
    QuestionModel question;
    // go through all questions
    for (String questionID : questions.keySet()) {
      InteractionModel interactiveElement = questions.get(questionID);
      // ignore interactive elements that are no questions
      if (!(interactiveElement instanceof QuestionModel)) {
        break;
      }
      question = (QuestionModel) questions.get(questionID);
      String questionGroupID = question.getGroupID();
      if (questionGroupResults.containsKey(questionGroupID)) {
        // if question group ID already exists, increment achieved and possible
        // points
        QuestionResult questionGroupResult = questionGroupResults
            .get(questionGroupID);
        questionGroupResult.setAchievedPoints(questionGroupResult
            .getAchievedPoints() + question.getPointsAchieved());
        questionGroupResult.setPossiblePoints(questionGroupResult
            .getPossiblePoints() + question.getPointsPossible());
        questionGroupResults.put(questionGroupID, questionGroupResult);
      } else {
        // if question group ID does not already exist, just put the single
        // result into the map
        questionGroupResults.put(
            questionGroupID,
            new QuestionResult(question.getPointsAchieved(), question
                .getPointsPossible()));
      }
    }
    return questionGroupResults;
  }

  /**
   * Writes a simple key-value pair as a MIME multipart message to the given
   * PrintStream.
   * 
   * @param ps the {@link PrintStream} to write to
   * @param boundary the boundary of the MIME multipart message
   * @param name the name of the parameter
   * @param value the value of the parameter
   */
  private void printFormElement(PrintStream ps, String boundary, String name,
      String value) {
    ps.println("--" + boundary);
    ps.println("Content-Disposition: form-data; name=\"" + name + "\"");
    ps.println();
    ps.println(value);
  }

}
