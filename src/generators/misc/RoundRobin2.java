package generators.misc;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.roundRobinUtil.RRProcess;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class RoundRobin2 extends AnnotatedAlgorithm implements Generator {

  // private static final String AlgorithmName = "RoundRobin";
  // private static final String AnimationAuthor =
  // "Andreas Schaller, Daniel Fath";

  private int                maxProcessTime;
  private int                firstProcessStart;
  private int                processTime   = 0;
  private int                schedule      = 0;
  private int                maxID;
  private int                quota;

  private RRProcess[]        processes;

  // AnimData
  private Timing             defaultTiming = new TicksTiming(50);
  private ArrayMarkerUpdater arrayMU;
  private ArrayMarker        marker;

  private StringArray[]      sas;

  private SourceCode         processNames, processesCode;

  private void RoundRobinCalculator() {
    exec("header");
    lang.nextStep();
    this.arrayMU.setVariable(vars.getVariable("processTime"));
    while (processTime < maxProcessTime + firstProcessStart) {
      exec("while1");
      lang.nextStep();
      RRProcess p = processes[schedule];
      exec("scheduleProcess");
      lang.nextStep();
      int i = 0;
      exec("vari");
      lang.nextStep();
      while (processTime >= p.startTime && !p.isFinished() && i < quota) {
        exec("while2");
        lang.nextStep();
        p.process(processTime, false);
        sas[schedule].put(processTime, "x", null, null);
        exec("process");
        lang.nextStep();

        exec("forS1");
        lang.nextStep();
        exec("forS2");
        lang.nextStep();
        int j;
        while ((j = Integer.parseInt(vars.get("j"))) < processes.length) {
          RRProcess tmp = processes[j];
          exec("tmpProcess");
          lang.nextStep();
          exec("if1");
          lang.nextStep();
          if (processTime >= tmp.startTime && !tmp.isFinished() && p != tmp) {
            tmp.process(processTime, true);
            sas[j].put(processTime, "::", null, null);
            exec("tmpTrue");
            lang.nextStep();
          }
          exec("if1End");
          lang.nextStep();
          exec("for");
          lang.nextStep();
        }
        exec("forEnd");
        lang.nextStep();
        processTime++;
        exec("incTime");
        lang.nextStep();
        i = (i + 1) % (quota + 1);
        exec("iQuota");
        lang.nextStep();
      }
      exec("while2End");
      lang.nextStep();
      schedule = (schedule + 1) % maxID;
      exec("scheduler");
      lang.nextStep();
      // processTime = processTime < firstProcessStart ? processTime++ :
      // processTime;
      exec("processTimeInit1");
      lang.nextStep();
      if (processTime < firstProcessStart) {
        processTime++;
        exec("processTimeInit2");
        lang.nextStep();
      }
      exec("processTimeInit");
      lang.nextStep();
    }
    exec("while1End");
    lang.nextStep();
  }

  @Override
  public String generate(AnimationPropertiesContainer properties,
      Hashtable<String, Object> primitives) {
    Text topic = this.lang.newText(new Coordinates(20, 10), "Round Robin",
        "Topic", null);
    topic.setFont(new Font("SansSerif", Font.PLAIN, 22), null, null);
    topic.show();

    ArrayProperties arrayProps = (ArrayProperties) properties
        .getPropertiesByName("arrayProps");
    SourceCodeProperties scProps = (SourceCodeProperties) properties
        .getPropertiesByName("scProps");
    ArrayMarkerProperties arrayMProps = (ArrayMarkerProperties) properties
        .getPropertiesByName("arrayMProps");
    arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "processTime");

    quota = (Integer) primitives.get("quota");
    int[][] procs = (int[][]) primitives.get("startTime - duration");
    processes = new RRProcess[procs.length];
    sas = new StringArray[procs.length];

    maxID = procs.length;
    maxProcessTime = 0;
    firstProcessStart = Integer.MAX_VALUE;

    SourceCodeProperties processNamesProps = new SourceCodeProperties();
    for (String prop : scProps.getAllPropertyNames()) {
      processNamesProps.set(prop, scProps.get(prop));
    }
    processNamesProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));

    this.processesCode = this.lang.newSourceCode(new Coordinates(650, 370),
        "Processes", null, processNamesProps);
    this.processesCode.addCodeLine("Quota: " + this.quota, null, 0, null);
    for (int i = 0; i < procs.length; i++) {
      int startTime = procs[i][0];
      int processTime = procs[i][1];

      firstProcessStart = firstProcessStart > startTime ? startTime
          : firstProcessStart;
      maxProcessTime += processTime;

      processes[i] = new RRProcess(i, startTime, processTime);

      this.processesCode.addCodeLine("P" + i + ": startTime: " + startTime
          + ", processTime: " + processTime, null, 0, null);
      this.processesCode.highlight(i + 1);
    }

    this.processNames = this.lang.newSourceCode(new Coordinates(18, 390),
        "Processnames", null, processNamesProps);

    for (int i = 0; i < procs.length; i++) {
      processes[i].init(maxProcessTime + firstProcessStart);
      sas[i] = this.lang.newStringArray(new Coordinates(40, 400 + (24 * i)),
          processes[i].line, "p" + i, null, arrayProps);
      this.processNames.addCodeLine("P" + i, null, 0, null);
    }

    this.marker = this.lang.newArrayMarker(this.sas[0], 0, "time", null,
        arrayMProps);
    this.arrayMU = new ArrayMarkerUpdater(marker, null, defaultTiming,
        this.sas[0].getLength() - 1);

    RoundRobinCalculator();

    return this.lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Round Robin";
  }

  @Override
  public String getAnimationAuthor() {
    return "Andreas Schaller, Daniel Fath";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Der Begriff Rundlauf-Verfahren oder englisch Round-Robin bezeichnet ein Scheduling-Verfahren, "
        + "d.h. es ordnet mehreren konkurrierenden Prozessen begrenzte Ressourcen zu. \n"
        + "Das Round-Robin-Verfahren gew&auml;hrt allen Prozessen nacheinander f&uuml;r jeweils einen kurzen Zeitraum – "
        + "einen sogenannten Zeitschlitz – Zugang zu den ben&ouml;tigten Ressourcen; man nennt dies auch Arbitrierung. "
        + "<p>Round-Robin wird auch zur Lastverteilung (load balancing) verwendet. Das Ziel der Lastverteilung ist es, "
        + "mehrere gleichartige Ressourcen m&ouml;glichst gleichm&auml;&szlig;ig zu beanspruchen."
        + "</p><p>Quelle: http://de.wikipedia.org/wiki/Round_Robin_(Informatik)"
        + "<p>"
        + "Bitte bei Property \"startTime - duration\" nur Nx2 Matrizen angeben, beispielsweise"
        + "<table><tr><td>0</td><td>2</td></tr>"
        + "<tr><td>1</td><td>3</td></tr>"
        + "<tr><td>4</td><td>6</td></tr>"
        + "<tr><td>4</td><td>3)</td></tr></table>";
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getName() {
    return "Round Robin";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public String getAnnotatedSrc() {
    String codeExample = "private void RoundRobinCalculator() {													@label(\"header\") @highlight(\"end\") @openContext @declare(\"int\", \"processTime\")\n"
        + "	while (processTime < maxProcessTime + firstProcessStart) {							@label(\"while1\")\n"
        + "		RRProcess p = processes[schedule];												@label(\"scheduleProcess\")\n"
        + "		int i = 0;																		@label(\"vari\")  @declare(\"int\", \"i\")\n"
        + "		while (processTime >= p.startTime && !p.isFinished() && i < quota) {				@label(\"while2\")\n"
        + "			p.process(processTime, false);												@label(\"process\")\n"
        + "			for (int j = 0; 															@label(\"forS1\") @declare(\"int\", \"j\") @set(\"j\", \"0\") \n"
        + "			 j < processes.length;														@label(\"forS2\") @continue \n"
        + "			 j++) {																		@label(\"for\") @continue @inc(\"j\")\n"
        + "				RRProcess tmp = processes[j];											@label(\"tmpProcess\")\n"
        + "				if (processTime >= tmp.startTime && !tmp.isFinished() && p != tmp) {		@label(\"if1\")\n"
        + "					tmp.process(processTime, true);										@label(\"tmpTrue\")\n"
        + "				}																		@label(\"if1End\")\n"
        + "			}																			@label(\"forEnd\")\n"
        + "			processTime++;																@label(\"incTime\") @inc(\"processTime\")\n"
        + "			i = (i+1) % (quota+1);														@label(\"iQuota\")	@inc(\"i\")\n"
        + "		}																				@label(\"while2End\")\n"
        + "		schedule = (schedule+1) % maxID;												@label(\"scheduler\")\n"
        + "		processTime = processTime < firstProcessStart ? 								@label(\"processTimeInit1\")\n"
        + "			processTime++ 																@label(\"processTimeInit2\") @continue @inc(\"processTime\")\n"
        + "			: processTime;																@label(\"processTimeInit\") @continue \n"
        + "	}																					@label(\"while1End\")\n"
        + "}																						@label(\"end\") @closeContext\n";
    return codeExample;
  }

  @Override
  public void init() {
    super.init();

    SourceCodeProperties props = new SourceCodeProperties();
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);

    // instantiate source code primitive to work with
    sourceCode = lang.newSourceCode(new Coordinates(20, 25), "sumupCode", null,
        props);

    // parsing anwerfen
    parse();
  }
}
