package generators.maths.buffon;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import util.Brush;

public class Buffon implements Generator {

	private BuffonAlgo buffon;

    public void init(){
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        boolean withExplanation = (Boolean)primitives.get("withExplanation");
        int maxSteps = (Integer)primitives.get("maxSteps");
        double abortDist = (Double)primitives.get("abortDist");
        int[][] needles1 = (int[][])primitives.get("needles1");
        Color colorError = (Color)primitives.get("colorError");
        int[][] needles2 = (int[][])primitives.get("needles2");
        int abortSteps = (Integer)primitives.get("abortSteps");
        Color color2 = (Color)primitives.get("color2");
        boolean stepwise = (Boolean)primitives.get("stepwise");
        Color color1 = (Color)primitives.get("color1");
        
        buffon = new BuffonAlgo(maxSteps, abortDist, abortSteps, needles1, needles2,
        		                new Brush(color1), new Brush(color2), new Brush(colorError), withExplanation, stepwise);
        
        return buffon.toString();
    }

    public String getName() {
        return "Buffonsche Nadelmethode";
    }

    public String getAlgorithmName() {
        return "Monte Carlo Methods";
    }

    public String getAnimationAuthor() {
        return "Christian Ritter";
    }

    public String getDescription(){
        return "<h3>Beschreibung</h3>"
 +"\n"
 +"Bei der Buffoschen Nadelmethode handelt es sich um ein Monte-Carlo-Verfahren zur Bestimmung"
 +"\n"
 +"der Kreiszahl &pi;. Im Allgemeinen wird bei Monte-Carlo-Verfahren ein Erwartungswert durch"
 +"\n"
 +"Wiederholen eines Zufallsexperimentes und mittels des Gesetzes der gro&szlig;en Zahlen"
 +"\n"
 +"n&auml;herungsweise bestimmt. In unserem speziellen Fall f&uuml;hren wir folgendes Zufallsexperiment"
 +"\n"
 +"durch: Wir werfen eine Nadel der L&auml;nge <i>d</i>/2 auf eine Wurffl&auml;che, auf der parallele Linien"
 +"\n"
 +"im Abstand <i>d</i> gezeichnet sind. Die Wahrscheinlichkeit, dass die Nadel eine Linie schneidet,"
 +"\n"
 +"betr&auml;gt dann 1/&pi;."
 +"\n"
 +"\n"
 +"<h3>Durchf&uuml;hrung</h3>"
 +"\n"
 +"In unserem Algorithmus f&uuml;hren wir zwei Monte-Carlo-Simulationen parallel durch. Dies erm&ouml;glicht"
 +"\n"
 +"es uns, den Fehler zu &pi; absch&auml;tzen zu k&ouml;nnen. Au&szlig;erdem k&ouml;nnen wir in jedem Schritt gleich"
 +"\n"
 +"mehrere Nadeln werfen, was uns Zeit spart. Die Nadeln k&ouml;nnen dabei auch geknickt oder gebogen"
 +"\n"
 +"sein. Wir z&auml;hlen nach jedem Schritt die Schnitte zwischen Nadeln und Linien. Bei verbogenen Nadeln"
 +"\n"
 +"kann es auch vorkommen, dass wir f&uuml;r eine Nadel mehrere Schnittpunkte erhalten. Diese werden"
 +"\n"
 +"dann auch entsprechend mehrfach gez&auml;hlt. Unsere Sch&auml;tzung f&uuml;r &pi; erhalten wir als das Verh&auml;ltnis"
 +"\n"
 +"Anzahl der W&uuml;rfe zu Anzahl der Schnittpunkte."
 +"\n"
 +"\n"
 +"<h3>Einstellbare Parameter</h3>"
 +"\n"
 +"<tt>maxSteps</tt> - Die maximale Anzahl der Simulationsschritte.<br />"
 +"\n"
 +"<tt>abortDist</tt> und <tt>abortNum</tt> - Eine Abbruchbedingung. Die Simulation bricht ab, sobald"
 +"\n"
 +"der gesch&auml;tze Fehler <tt>abortNum</tt> Schritte in Folge unter <tt>abortDist</tt> liegt.<br />"
 +"\n"
 +"<tt>needles1</tt> und <tt>needles2</tt> - Die Nadeln f&uuml;r Simulation 1 und Simulation 2. Jede der"
 +"\n"
 +"beiden Matrizen enth&auml;lt pro Zeile genau eine Nadel. Aus der Zahlenfolge heraus wird eine geknickte Nadel"
 +"\n"
 +"konstruiert. Dabei steht jeder ungerade Eintrag f&uuml;r eine L&auml;nge und jeder gerade Eintrag f&uuml;r einen"
 +"\n"
 +"Winkel in Grad. Zum Beispiel liefert 1 - 45 - 1 eine Nadel, die in der Mitte um 45&deg; geknickt ist. Enth&auml;lt"
 +"\n"
 +"die Folge nur eine L&auml;nge, wird eine gerade Nadel konstruiert. Die Nadel wird automatisch auf die"
 +"\n"
 +"L&auml;nge d/2 normiert.<br />"
 +"\n"
 +"<tt>color1</tt> und <tt>color2</tt> - Farben zur Unterscheidung von Simulation 1 und 2.<br />"
 +"\n"
 +"<tt>colorError</tt> - Farbe zum Hervorheben des Fehler-Unterschreitungs-Z&auml;hlers.<br />"
 +"\n"
 +"<tt>withExplanation</tt> - Boolscher Wert, der angibt, ob die Simulation mit einer Einf&uuml;hrung zum"
 +"\n"
 +"Thema Monte-Carlo-Verfahren und Buffonsches Nadelwerfen beginnen soll.<br />"
 +"\n"
 +"<tt>stepwise</tt> - Boolscher Wert, der angibt, ob in jedem Schritt alle Nadeln auf einmal oder"
 +"\n"
 +"nacheinander geworfen werden sollen."
 +"\n";
    }

    public String getCodeExample(){
        return "wuerfeSimu1 = 0"
 +"\n"
 +"wuerfeSimu2 = 0"
 +"\n"
 +"schnitteSimu1 = 0"
 +"\n"
 +"schnitteSimu2 = 0"
 +"\n"
 +"abort = 0"
 +"\n"
 +"\n"
 +"<b>F&uuml;r jedes</b> 0 &le; i &le; maxSteps :"
 +"\n"
 +"	Werfe n1 Nadeln f&uuml;r Simulation 1 auf die Wurffl&auml;che."
 +"\n"
 +"	wuerfeSimu1 += n1"
 +"\n"
 +"	Werfe n2 Nadeln f&uuml;r Simulation 2 auf die Wurffl&auml;che."
 +"\n"
 +"	wuerfeSimu2 += n2"
 +"\n"
 +"	Ermittele die Anzahl k1 der Schnittpunkte zwischen Nadeln und parallelen Linien."
 +"\n"
 +"	schnitteSimu1 += k1"
 +"\n"
 +"	Ermittele die Anzahl k2 der Schnittpunkte zwischen Nadeln und parallelen Linien."
 +"\n"
 +"	schnitteSimu1 += k2"
 +"\n"
 +"	Sch&auml;tze Pi f&uuml;r Simulation 1: pi1 = wuerfeSimu1 / schnitteSimu1"
 +"\n"
 +"	Sch&auml;tze Pi f&uuml;r Simulation 2: pi2 = wuerfeSimu2 / schnitteSimu2"
 +"\n"
 +"	<b>Falls</b> |pi1-pi2| &le; abortDist <b>dann</b> abort += 1"
 +"\n"
 +"	<b>Falls</b> abort &ge; abortSteps <b>dann</b> Bereche den Algorithmus ab.";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}