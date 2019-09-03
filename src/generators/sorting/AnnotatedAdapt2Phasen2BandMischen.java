package generators.sorting;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.ListElement;
import generators.helpers.Pointer;
import generators.helpers.PolylineHandler;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class AnnotatedAdapt2Phasen2BandMischen extends AnnotatedAlgorithm
    implements Generator {
  private Language            lang;

  static final String         ALGORITHM_NAME = "Adaptiertes 2-Phasen-2-Band Mischen";

  private static final String AUTHOR         = "Andreas Franek"; // <andy-held@web.de>";

  private static final String DESCRIPTION    = "(von Wikipedia)"
                                                 + "\nIn den Anfängen der elektronischen Datenverarbeitung wurden Massendaten auf Magnetbändern abgelegt. Diese Bänder wurden mit einem Magnetkopf sequentiell gelesen und geschrieben. Es war also kein wahlfreier Zugriff möglich. Man behalf sich mit Algorithmen, die ein Band mithilfe eines zweiten (u.U. auch eines dritten) sequentiell sortierten."
                                                 + "\nDieses Verfahren wurde Ende der 90er Jahre für doppelt verkettete Listen adaptiert (Markus v. Brevern und Dirk Sorges)."
                                                 + "\nDoppelt verkettete Listen bestehen aus Listenenelementen, die einen Zeiger auf den Nachfolger und auf den Vorgänger besitzen. Man kann also vom Anfang vorwärts oder vom Ende rückwärts durch die Listen laufen. Einfügen und Löschen ist sehr einfach. Eine Liste ist einem Array bei diesen beiden Operationen deutlich überlegen."
                                                 + "\nIm 2-Phasen-2-Band-Mischen werden in einer Phase Läufe (runs) sortierter Elemente bestimmt. Diese Läufe werden dann in der zweiten Phase ineinander gemischt, so dass aus 2 Läufen einer wird. Der Algorithmus endet, wenn nur noch ein (jetzt vollständig sortierter) Lauf übrig bleibt."
                                                 + "\nBei doppelt verketteten Listen verwendet man die Vorwärtszeiger als Zeiger auf das nächste Element und die Rückwärtszeiger als Zeiger auf den nächsten Lauf. Man initialisiert die Liste, indem man für jedes Element die Rückwärtszeiger wie die Vorwärtszeiger auf das nächste Element zeigen lässt. Jetzt hat man n Läufe der Länge 1. Daraufhin sortiert man so lange zwei Läufe zusammen, bis nur noch ein Lauf übrig ist. Das Sortieren erreicht man mit zwei zusätzlichen Zeigern, die jeweils auf einen Lauf zeigen. Das kleinere referenzierte Element wird in den Mischlauf übernommen, der entsprechende Zeiger auf das nächste Element des Laufs gesetzt. Ist ein Lauf ganz abgearbeitet, wird der Rest des anderen Laufs an den Mischlauf angehängt. Anhängen und Einsortieren wird einfach durch 'Umzeigern' erreicht. Abschließend werden alle Rückwärtszeiger auf das jeweils vorige Element gesetzt."
                                                 + "\nDieses Verfahren arbeitet In-place, verbraucht also keinen weiteren Speicherplatz. Kopiert wird nichts, lediglich die Zeiger werden verändert. Der Aufwand liegt immer in O( n * log n). Es ist also das perfekte Sortierverfahren für unbestimmte Datenlagen.";

  private static final String SOURCE_CODE    = "Pseudo Code:																		@label(\"schd\")"
                                                 + "\n1. Erstelle n Läufe															@label(\"createn\")"
                                                 + "\n2. Finde schon sortierte Läufe													@label(\"findSorted\")"
                                                 + "\n3. Solange es mehr als einen Lauf gibt:										@label(\"whileRuns\")"
                                                 + "\n    Betrachte die 2 nächsten Läufe.											@label(\"lookAt\")"
                                                 + "\n    4.1 Bis beide Läufe leer sind:												@label(\"untilEmpty\")"
                                                 + "\n        Füge das kleinere der vorderen Elemente der beiden Läufe in die Liste	@label(\"move\")"
                                                 + "\n5. Lasse prev Zeiger wieder auf vorheriges Element Zeigen						@label(\"prev\")";

  int                         elemCount;

  int                         radius         = 150;
  int                         circleMidX     = 200;
  int                         cricleMidY     = 450;
  double                      circleStep;
  int                         RowX           = 400;
  int                         topRowY        = 500;
  int                         downRowY       = 550;
  int                         legX           = 600;
  int                         legY           = 300;
  SourceCodeProperties        scProps;

  public AnnotatedAdapt2Phasen2BandMischen() {
  }

  private Group makeHeader() {
    LinkedList<Primitive> header = new LinkedList<Primitive>();
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 32));
    Text hdTxt = lang.newText(new Coordinates(20, 30),
        "Adaptiertes 2-Phasen-2-Band Mischen", "header", null, headerProps);
    Rect hdRect = lang.newRect(new Coordinates(12, 15),
        new Coordinates(695, 52), "hdrect", null);
    header.add(hdTxt);
    header.add(hdRect);
    return lang.newGroup(header, "header");
  }

  private SourceCode makeDescr() {
    SourceCode descr = lang.newSourceCode(new Coordinates(20, 50), "descr",
        null, scProps);
    descr.addCodeLine("Beschreibung:", null, 0, null);
    descr.addCodeLine(
        "Der Algorithmus arbeitet auf doppelt verketteten Listen.", null, 1,
        null);
    descr.addCodeLine("Er erzeugt sogenannte Läufe.", null, 1, null);
    descr
        .addCodeLine(
            "Ein Lauf ist eine Folge von sortierten Elementen, bei denen je ein Zeiger auf den nächsten Lauf zeigt.",
            null, 1, null);
    descr
        .addCodeLine(
            "Zunächst werden alle Rückwärtszeiger auf das nächste Element gesetzt.",
            null, 1, null);
    descr.addCodeLine("So erhält man zu Beginn n Läufe.", null, 1, null);
    descr
        .addCodeLine(
            "Dann geht man einmal durch die Liste und sucht bereits sortierte Läufe.",
            null, 1, null);
    return descr;
  }

  private Group makeLegend() {
    LinkedList<Primitive> legend = new LinkedList<Primitive>();
    Rect legRect = lang.newRect(new Coordinates(legX, legY), new Coordinates(
        legX + 305, legY + 85), "legende", null);
    PolylineHandler legNxPtr = new PolylineHandler(lang, new Coordinates(
        legX + 300, legY + 28), new Coordinates(legX + 200, legY + 28),
        "legNxPtr");
    PolylineHandler legPrPtr = new PolylineHandler(lang, new Coordinates(
        legX + 300, legY + 48), new Coordinates(legX + 200, legY + 48),
        "legPrPtr");
    PolylineHandler legBtPtr = new PolylineHandler(lang, new Coordinates(
        legX + 300, legY + 68), new Coordinates(legX + 200, legY + 68),
        "legBtPtr");
    legPrPtr.changeColor();
    legBtPtr.same();
    SourceCode legText = lang.newSourceCode(
        new Coordinates(legX + 4, legY - 16), "legText", null, scProps);
    legText.addCodeLine("Legende", null, 0, null);
    legText.addCodeLine("next-Pointer", null, 1, null);
    legText.addCodeLine("prev-Pointer", null, 1, null);
    legText.addCodeLine("prev & next-Pointer", null, 1, null);
    legend.add(legRect);
    legend.add(legNxPtr.getPl());
    legend.add(legPrPtr.getPl());
    legend.add(legBtPtr.getPl());
    legend.add(legText);
    return lang.newGroup(legend, "header");
  }

  private void mischeln(int[] arr) {
    init();
    sourceCode.hide();
    elemCount = arr.length;
    @SuppressWarnings("unused")
    Group header = makeHeader();
    SourceCode descr = makeDescr();

    lang.nextStep();
    descr.hide();
    sourceCode.show();
    exec("createn");
    // create legend
    Group legend = makeLegend();

    circleStep = 2 * Math.PI / arr.length;
    double circlePos = 0;
    ListElement last = null;
    ListElement first = null;
    ListElement neu = null;
    for (int i = 0; i < elemCount; i++) {
      int newx = circleMidX - (int) (Math.sin(circlePos) * radius);
      int newy = cricleMidY - (int) (Math.cos(circlePos) * radius);
      neu = new ListElement(lang, new Coordinates(newx, newy), arr[i],
          String.valueOf(i));
      if (last != null) {
        last.setNext(neu);
        last.setNextRun(neu);
      } else
        first = neu;
      neu.setIndex(i);
      circlePos += circleStep;
      last = neu;
    }
    last.setNext(first);
    last.setNextRun(first);
    lang.nextStep();
    exec("findSorted");
    // suche sortierte runs
    ListElement lastRun = first;
    ListElement lookAt = first;
    int runs = 0;
    for (int i = 0; i < elemCount; i++) {
      if (lookAt.num > lookAt.getNext().num) {
        lookAt = lookAt.getNext();
        if (lookAt == lastRun) {
          // array ist schon sortiert
          lastRun.setNextRun(lookAt);
          lastRun = lastRun.getNext();
        }
        while (lastRun != lookAt) {
          lastRun.setNextRun(lookAt);
          lastRun = lastRun.getNext();
        }
        runs++;
      } else
        lookAt = lookAt.getNext();
    }
    lookAt = lastRun;
    if (lookAt != first)
      runs++;
    while (lookAt != first) {
      lookAt.setNextRun(first);
      lookAt = lookAt.getNext();
    }
    lang.nextStep();
    ListElement run1;
    ListElement run2;
    ListElement lookAt2;
    ListElement nextRun;
    Pointer upper = null;
    Pointer lower = null;
    Text runsTxt = lang.newText(new Coordinates(40, 280), "runs = " + runs,
        "runsTxt", null);
    runsTxt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16), null, null);
    upper = new Pointer(lang, first, true);
    upper.hide();
    lower = new Pointer(lang, first, false);
    lower.hide();
    exec("whileRuns");
    while (runs > 1) {
      lang.nextStep();
      exec("lookAt");

      run1 = lastRun.getNextRun();
      run2 = run1.getNextRun();
      if (run2 == first) {
        lastRun = run1;
        run1 = first;
        run2 = first.getNextRun();
      }
      nextRun = run2.getNextRun();
      lookAt = run1;
      lookAt2 = run2;

      moveRun(run1, run2, true);
      lang.nextStep();
      moveRun(run2, nextRun, false);
      lang.nextStep();
      exec("untilEmpty");

      upper.pointTo(lookAt);
      lower.pointTo(lookAt2);
      upper.show();
      lower.show();

      int index = run1.getIndex();
      ListElement lastSorted = null;

      lookAt.highlight();
      lookAt2.highlight();

      while (lookAt != run2) {
        lang.nextStep();
        exec("move");
        if (lookAt2 != nextRun && lookAt2.num < lookAt.num) {
          // verschiebe lookAt2
          lookAt2.setIndex(index);
          moveBack(lookAt2);
          if (lastSorted == null) {
            if (run1 == first)
              first = run2;
            if (runs == 2)
              nextRun = run2;
            while (lastRun.getNext() != lookAt) {
              lastRun.setNextRun(lookAt2);
              lastRun = lastRun.getNext();
            }
            lastRun.setNext(lookAt2);
            lastRun.setNextRun(lookAt2);
            lastRun = lookAt2;
          } else {
            lastSorted.setNext(lookAt2);
          }
          lookAt2.setNextRun(nextRun);
          lastSorted = lookAt2;
          lookAt2 = lookAt2.getNext();
          if (lookAt2 != nextRun) {
            lower.pointTo(lookAt2);
            lookAt2.highlight();
          } else
            lower.hide();
        } else {
          // verschiebe lookAt
          lookAt.setIndex(index);
          moveBack(lookAt);
          if (lastSorted == null) {
            lastRun = lookAt;
          } else {
            lastSorted.setNext(lookAt);
          }
          lookAt.setNextRun(nextRun);
          lastSorted = lookAt;
          lookAt = lookAt.getNext();
          if (lookAt != run2) {
            upper.pointTo(lookAt);
            lookAt.highlight();
          } else
            upper.hide();
        }
        index++;
        lastSorted.unhighlight();
      }
      while (lookAt2 != nextRun) {
        lang.nextStep();
        // verschiebe uebrige aus run2
        lookAt2.setIndex(index);
        moveBack(lookAt2);
        lastSorted.setNext(lookAt2);
        lookAt2.setNextRun(nextRun);
        lastSorted = lookAt2;
        lookAt2 = lookAt2.getNext();
        if (lookAt2 != nextRun && lookAt2 != run2) {
          lower.pointTo(lookAt2);
          lookAt2.highlight();
        } else
          lower.hide();
        lastSorted.unhighlight();
        index++;
      }
      if (lastSorted.getNext() != nextRun) {
        lastSorted.setNext(nextRun);
      }
      lang.nextStep();
      runs--;
      runsTxt.setText("runs = " + runs, null, null);
      exec("whileRuns");
    }
    lang.nextStep();
    exec("prev");
    lookAt = first;
    while (lookAt.getNext() != first) {

      lookAt.getNext().setNextRun(lookAt);
      lookAt = lookAt.getNext();
    }
    first.setNextRun(lookAt);
    lang.nextStep();
    lookAt = first;
    while (lookAt.getNext() != first) {

      lookAt.hide();
      lookAt = lookAt.getNext();
    }
    lookAt.hide();
    sourceCode.hide();
    runsTxt.hide();
    legend.hide();
    SourceCode conclusion = lang.newSourceCode(new Coordinates(20, 50),
        "conclusion", null, scProps);
    conclusion.addCodeLine("Fazit:", null, 0, null);
    conclusion
        .addCodeLine(
            "Der Algorithmus hantiert nur mit Pointern, er arbeitet also in-place.",
            null, 1, null);
    conclusion
        .addCodeLine(
            "In einer Iteration durchläuft der Algorithmus alle Elemente der Liste.",
            null, 1, null);
    conclusion.addCodeLine(
        "Dabei halbiert er die Anzahl der sortierten Läufe.", null, 1, null);
    conclusion.addCodeLine("Die Laufzeit liegt also in O(n*log(n)).", null, 1,
        null);
  }

  private void moveBack(ListElement le) {
    int newx = circleMidX
        - (int) (Math.sin(circleStep * le.getIndex()) * radius);
    int newy = cricleMidY
        - (int) (Math.cos(circleStep * le.getIndex()) * radius);
    le.moveTo(newx - le.getWidth() / 2, newy - le.getHeight() / 2);
  }

  private void moveRun(ListElement runStart, ListElement runEnd, boolean top) {
    ListElement lookAt = runStart;
    int elemXPos = RowX;
    int elemYPos = (top) ? topRowY : downRowY;
    while (lookAt != runEnd) {
      // System.out.println(lookAt.num);
      lookAt.moveTo(elemXPos, elemYPos);
      elemXPos += lookAt.getWidth() + 10;
      lookAt = lookAt.getNext();
    }
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    int arr[] = (int[]) arg1.get("list");
    mischeln(arr);
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  @Override
  public String getAlgorithmName() {
    return ALGORITHM_NAME;
  }

  @Override
  public String getAnimationAuthor() {
    return AUTHOR;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getName() {
    return ALGORITHM_NAME + "[annotations]";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    super.init();
    lang = new AnimalScript(ALGORITHM_NAME, AUTHOR, 640, 480);
    lang.setStepMode(true);

    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.BOLD, 14));

    sourceCode = lang.newSourceCode(new Coordinates(20, 50), "bssc", null,
        scProps);
    parse();
  }

  @Override
  public String getAnnotatedSrc() {
    return SOURCE_CODE;
  }
}
