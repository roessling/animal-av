/*
 * MonteCarloSimulation.java
 * Sascha Zenglein,Julian Schwind, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;
import algoanim.properties.CircleProperties;
import java.util.Random;
import java.lang.Math;

public class MonteCarloSimulation implements Generator {
	private Language lang;
//	private PolylineProperties Treffer;
//	private PolylineProperties Nieten;
	private int Seed;
	private int Iterationen;
	private int PseudoCode;
    private Color Nieten;
	private Color Treffer;

	public void init() {
		lang = new AnimalScript("Monte-Carlo-Simulation", "Sascha Zenglein,Julian Schwind", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// Fuer User sichtbar
		Nieten = (Color)primitives.get("Nieten");
        Treffer = (Color)primitives.get("Treffer");
		Seed = (Integer) primitives.get("Seed");
		Iterationen = (Integer) primitives.get("Iterationen");
		PseudoCode = (Integer) primitives.get("PseudoCode");
		lang.setStepMode(true);
		
		PolylineProperties kreuzprop = new PolylineProperties();
         
		
		kreuzprop.set("fwArrow", false);

		// Properties
		TextProperties textprops = new TextProperties();
		TextProperties counterprops = new TextProperties();
		CircleProperties circleprops = new CircleProperties();
		RectProperties rectprops = new RectProperties();
		PolylineProperties achse = new PolylineProperties();

		// ueberschrift
		Font f = new Font("serif", Font.ITALIC, 50);
		textprops.set("color", Color.BLACK);
		Text headline = lang.newText(new Coordinates(20, 50), "Monte-Carlo-Simulation", "ueberschrift", null, textprops);
		Coordinates[] strich = new Coordinates[8];
		
		headline.setFont(f, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		// Stricht Unter ueberschrift

				strich[0] = new Coordinates(15, 70);
				strich[1] = new Coordinates(515, 70);
				strich[2] = new Coordinates(515, 72);
				strich[3] = new Coordinates(15, 72);
				strich[4] = new Coordinates(15, 74);
				strich[5] = new Coordinates(515, 74);
				strich[6] = new Coordinates(515, 75);
				strich[7] = new Coordinates(15, 75);
				Polyline headerLine = lang.newPolyline(strich, "strich", null);
		
		

Coordinates erkcord = new Coordinates(20,140);
		
		// BESCHREIBUNG
		
		Font besch = new Font("serif", Font.ITALIC, 26);

		Text erkl = lang.newText(erkcord,"Die Monte-Carlo-Simulation ist ein stochastisches Verfahren, welches mit Zufallsexperimenten Probleme loest.", null, null, textprops);
		Text erkl2 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+40),"Meist werden dafuer zufaellige Ereignisse per Computer mit einem geeigneten Algorithmus simuliert.", null, null, textprops);
		Text erkl3 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+80),"Wir beschraenken uns auf eine Anwendung aus dem Bereich der Mathematik, naemlich die Approximation von Pi.", null, null, textprops);
		Text erkl4 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+120),"Hierbei werden zufaellige Punkte generiert und anhand der Verteilung dieser Punkte Pi berechnet.", null, null, textprops);
		Text erkl5 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+160),"Eine genauere Erklaerung und Visualisierung des Verfahrens wird jetzt gezeigt.", null, null, textprops);


		erkl.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erkl2.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erkl3.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erkl4.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erkl5.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);



		
		lang.nextStep();
		
		erkl.hide();
		erkl2.hide();
		erkl3.hide();
		erkl4.hide();
		erkl5.hide();
		
		
		
		
		


		int incircle = 0;
		int outcircle = 0;
		int zerox = 75;
		int zeroy = 450;
		int j = 0;
		Random random = new Random(Seed);

		// Koordinaten von NullPunkt und Ende des Koordinatensystems
		Coordinates zero = new Coordinates(zerox, zeroy);
		Coordinates endofx = new Coordinates(zero.getX() + 250, zero.getY());
		Coordinates endofy = new Coordinates(zero.getX(), zero.getY() - 250);
		// Koordinaten von der X-Achse
		Node[] x = new Node[2];
		x[0] = zero;
		x[1] = endofx;
		// Koordinaten von der Y-Achse
		Node[] y = new Node[2];
		y[0] = zero;
		y[1] = endofy;

		// Anzeige der Skalierung des Koordinatensystems
		Node[] einsx = new Node[2];
		einsx[0] = new Coordinates(zero.getX() + 200, zero.getY() + 4);
		einsx[1] = new Coordinates(zero.getX() + 200, zero.getY() - 4);
		Node[] einsy = new Node[2];
		einsy[0] = new Coordinates(zero.getX() + 4, zero.getY() - 200);
		einsy[1] = new Coordinates(zero.getX() - 4, zero.getY() - 200);
		// 1,0 fuer Koordinatensystem
		lang.newText(new Coordinates(zero.getX() + 192, zero.getY() + 7), "1.0", "anX", null);
		lang.newText(new Coordinates(zero.getX() - 26, zero.getY() - 208), "1.0", "anY", null);

		// Beschriftung der Achsen
		lang.newText(new Coordinates(endofx.getX() - 32, endofx.getY() + 15), "x-Achse", "XBeschriftung", null);
		lang.newText(new Coordinates(endofy.getX() - 60, endofy.getY()), "y-Achse", "YBeschriftung", null);

		// Einheitskreis
		circleprops.set("depth", 10);
		lang.newCircle(zero, 200, "EinheitsKreis", null, circleprops);
		// Quadrat um Einheitskreis
		rectprops.set("depth", 4);
		lang.newRect(new Coordinates(zerox, zeroy - 200), new Coordinates(zerox + 200, zeroy), "Recht", null,
				rectprops);

		// Fix-Rechtecke
		rectprops.set("color", Color.WHITE);
		rectprops.set("filled", true);
		rectprops.set("fillColor", Color.WHITE);
		lang.newRect(new Coordinates(0, 0), new Coordinates(75, 600), "rettung", null, rectprops);
		lang.newRect(new Coordinates(0, zeroy), new Coordinates(zerox + 200, zeroy + 200), "rettung", null, rectprops);

		// X-und Y-Achse
		achse.set("fwArrow", true);
		achse.set("color", Color.BLACK);

		lang.newPolyline(x, "XAchse", null, achse);
		lang.newPolyline(y, "YAchse", null, achse);
		lang.newPolyline(einsx, "einsx", null);
		lang.newPolyline(einsy, "einsy", null);

		lang.nextStep();
		// TRENNSTRICHE
		Coordinates[] trennstrich = new Coordinates[2];
		trennstrich[0] = new Coordinates(zero.getX() + 433, zero.getY() - 245);
		trennstrich[1] = new Coordinates(zero.getX() + 433, zero.getY() - 15);
		Coordinates[] trennstrich2 = new Coordinates[2];
		trennstrich2[0] = new Coordinates(trennstrich[0].getX() + 420, trennstrich[0].getY());
		trennstrich2[1] = new Coordinates(trennstrich[1].getX() + 420, trennstrich[1].getY());

		lang.newPolyline(trennstrich, "trenn", null);
		lang.newPolyline(trennstrich2, "trenn", null);

		// PSEUDO-CODE
		Coordinates forpseudo = new Coordinates(zero.getX() + 455, zero.getY() - 240);
		Font fpseudo = new Font("monospace", Font.PLAIN, 16);

		Text pseudoCode1 = lang.newText(forpseudo, "for(i = 0; i<Iterationen; i++)", null, null, textprops);
		Text pseudoCode2 = lang.newText(new Coordinates(forpseudo.getX() + 13, forpseudo.getY() + 30),
				"coordinateX = random; coordinateY = random;", null, null, textprops);
		Text pseudoCode3 = lang.newText(new Coordinates(forpseudo.getX() + 13, forpseudo.getY() + 60),
				"if((coordinateX, coordinateY) inside the circle", null, null, textprops);
		Text pseudoCode4 = lang.newText(new Coordinates(forpseudo.getX() + 31, forpseudo.getY() + 90), "incircle++;",
				null, null, textprops);
		Text pseudoCode5 = lang.newText(new Coordinates(forpseudo.getX() + 13, forpseudo.getY() + 120), "repeat;", null,
				null, textprops);
		Text pseudoCode6 = lang.newText(new Coordinates(forpseudo.getX(), forpseudo.getY() + 150), "end of loop;", null,
				null, textprops);
		Text pseudoCode7 = lang.newText(new Coordinates(forpseudo.getX() - 5, forpseudo.getY() + 180),
				"return 4*(incircle/Iterationen);", null, null, textprops);

		pseudoCode1.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		pseudoCode2.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		pseudoCode3.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		pseudoCode4.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		pseudoCode5.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		pseudoCode6.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		pseudoCode7.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// ERKLaeRUNGSTEXTE

		Coordinates forexpl = new Coordinates(forpseudo.getX() + 422, forpseudo.getY() + 90);
		Font fontexpl = new Font("serif", Font.CENTER_BASELINE, 17);
		Text expl1[] = new Text[3];
		Text expl2[] = new Text[3];
		Text expl3[] = new Text[4];
		Text expl4[] = new Text[3];
		Text expl5[] = new Text[3];
		Text expl6[] = new Text[3];
		Text expl7[] = new Text[3];
		Text expl8[] = new Text[3];
		Text expl9[] = new Text[7];
		Text expl10[] = new Text[3];

		expl1[0] = lang.newText(forexpl, "Anfang der Iteration.", null, null,
				textprops);
		expl1[1] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() + 30),
				"In jeder Iteration wird ein Punkt berechnet und gesetzt.", null, null, textprops);

		expl2[0] = lang.newText( forexpl,
				"Generierung zweier zufaelliger Gleitkommazahlen zwischen 1 und 0.", null, null, textprops);
		expl2[1] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() + 30), "Diese Zahlen ergeben die Koordinate eines Punktes.", null, null, textprops);
		

		expl3[0] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() - 45),
				"Ist der Punkt innerhalb des Kreises?", null, null, textprops);
		expl3[1] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() - 15),
				"Dies wird mit Hilfe vom Satz des Pythagoras berechnet.", null, null, textprops);
		expl3[2] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() + 15),
				"Wenn coordinateX^2 + coordinateY^2 <= 1 gilt, dann", null, null, textprops);
		expl3[3] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() + 45),
		"ist der Punkt im Kreis.", null, null, textprops);

		
		expl4[0] = lang.newText(forexpl,
				"Der Punkt ist innerhalb des Kreises.", null, null, textprops);
        
		expl4[1] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY()+30),	"Der Integer incircle wird um 1 inkrementiert.", null, null, textprops);

		
		expl5[0] = lang.newText(forexpl, "Der Punkt war ausserhalb des Kreises.", null, null, textprops);

		expl6[0] = lang.newText(forexpl, "Naechste Iteration.", null, null, textprops);

		expl7[0] = lang.newText(forexpl, "Die restlichen Punkte werden eingetragen.", null, null, textprops);

		expl8[0] = lang.newText(forexpl, "Alle Punkte wurden erfolgreich eingetragen!", null, null, textprops);

		expl9[0] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() - 30),
				"Nun wird Pi mit 4*(incircle/Iterationen) berechnet.", null, null, textprops);
		expl9[1] = lang.newText(forexpl, "(incircle/Iterationen) ist der Anteil der Punkte im Kreis.", null, null,
				textprops);
		expl9[2] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() + 30),
				"Die 4 ist auf das Flaechenverhaeltnis zwischen dem Einheitskreis und einem Quadrat,", null, null, textprops);
		
		expl9[3] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() + 60),
				"welches den Kreis umschliesst, zurueckzufuehren. Denn (1^2*pi)/(2^2) = 1/4 Pi.", null, null, textprops);
		
		expl9[4] = lang.newText(new Coordinates(forexpl.getX(), forexpl.getY() + 90),
				"Dass nur ein Viertel des Einheitskreises betrachtet wird macht keinen Unteschied", null, null, textprops);
        
		
		
		
		expl10[0] = lang.newText(forexpl, "Die Schleife endet.", null, null, textprops);

		
		expl1[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl1[0].hide();

		expl1[1].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl1[1].hide();

		expl2[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl2[0].hide();

		expl2[1].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl2[1].hide();


		expl3[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl3[0].hide();

		expl3[1].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl3[1].hide();

		expl3[2].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl3[2].hide();

		expl3[3].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl3[3].hide();

		expl4[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl4[0].hide();
        
		expl4[1].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl4[1].hide();
		
		
		expl5[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl5[0].hide();

		expl6[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl6[0].hide();

		expl7[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl7[0].hide();

		expl8[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl8[0].hide();

		expl9[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl9[0].hide();

		expl9[1].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl9[1].hide();

		expl9[2].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl9[2].hide();
		
		expl9[3].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl9[3].hide();
		
		expl9[4].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl9[4].hide();
		
		
		expl10[0].setFont(fontexpl, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		expl10[0].hide();

		// Text pseudoCode6 = lang.newText(new Coordinates(forpseudo.getX(),
		// forpseudo.getY()+150), text, null, null, textprops);

		// int zerox = 75;
		// int zeroy = 425;

		// Counter der Punkte und PI AUSGABE
		Font font = new Font("serif", Font.PLAIN, 16);
		Font special = new Font("serif", Font.BOLD, 16);
		Font finale = new Font("serif", Font.CENTER_BASELINE, 24);

		counterprops.set("color", Treffer);
		Text counterin = lang.newText(new Coordinates(zero.getX() + 225, zero.getY() - 185),
				"Punkte im Kreis :" + " " + String.valueOf(incircle), "Counter", null, counterprops);
		counterprops.set("color", Nieten);
		Text counterout = lang.newText(new Coordinates(zero.getX() + 225, zero.getY() - 155),
				"Punkte ausserhalb:" + " " + String.valueOf(incircle), "Counter", null, counterprops);
		counterprops.set("color", Color.MAGENTA);
		Text counterall = lang.newText(new Coordinates(zero.getX() + 225, zero.getY() - 125),
				"Punkte insgeamt :" + " " + "0", "Counter", null, counterprops);
		textprops.set("color", Color.BLUE);
		counterin.setFont(font, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		counterout.setFont(font, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		counterall.setFont(font, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		counterprops.set("color", Color.BLACK);
		Text textpi = lang.newText(new Coordinates(zero.getX() + 225, zero.getY() - 95),
				"Pi ist derzeit  :" + " " + "-", null, null, counterprops);
		textpi.setFont(font, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		double pi;
		lang.nextStep();
		if (PseudoCode > 0) {
			pseudoCode1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			expl1[0].show();
			expl1[1].show();
			lang.nextStep();

		}
		/******************************************************* SCHLEIFE ************************************************/
		for (int i = 0; i < Iterationen; i++) {
			j++;
			boolean psbool = i < PseudoCode;
			// ES GIBT EINE CODEBEGLEITUNG
			if (psbool) {
				pseudoCode1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				pseudoCode2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);

				expl1[0].hide();
				expl1[1].hide();

				expl2[0].show();
				expl2[1].show();

				lang.nextStep();
				pseudoCode2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				pseudoCode3.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);

				expl2[0].hide();
				expl2[1].hide();

				expl3[0].show();
				expl3[1].show();
				expl3[2].show();
				expl3[3].show();

				lang.nextStep();
				
				expl3[0].hide();
				expl3[1].hide();
				expl3[2].hide();
				expl3[3].hide();


			}

			float pointx = random.nextFloat();
			float pointy = random.nextFloat();

			int rpointx = (int) Math.nextUp(pointx * 200);
			int rpointy = (int) Math.nextUp(pointy * 200);

			// PUNKT IST IM KREIS
			if (pointx * pointx + pointy * pointy <= 1f) {

				kreuzprop.set("color", Treffer);

				incircle++;

				// EINTRAGUNG DER PUNKTE
				Coordinates edge1[] = new Coordinates[2];
				Coordinates edge2[] = new Coordinates[2];

				edge1[0] = new Coordinates(zerox + rpointx - 5, zeroy - rpointy - 5);
				edge1[1] = new Coordinates(zerox + rpointx + 5, zeroy - rpointy + 5);
				edge2[0] = new Coordinates(zerox + rpointx - 5, zeroy - rpointy + 5);
				edge2[1] = new Coordinates(zerox + rpointx + 5, zeroy - rpointy - 5);

				lang.newPolyline(edge1, "cross1", null, kreuzprop);
				lang.newPolyline(edge2, "cross2", null, kreuzprop);

				// CODEBEGLEITUNG UND INCIRCLE
				if (psbool) {
					pseudoCode3.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					pseudoCode4.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);

					
					expl4[0].show();
					
					expl4[1].show();

					counterin.setFont(special, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

					counterin.setText("Punkte im Kreis:" + " " + String.valueOf(incircle), null, Timing.INSTANTEOUS);

					lang.nextStep();

					expl4[0].hide();
					expl4[1].hide();

				}

			}

			// PUNKT IST AUSSERHALB DES KREISES
			else {
				kreuzprop.set("color", Nieten);
				outcircle++;
				counterout.setText("Punkte ausserhalb:" + " " + String.valueOf(outcircle), null, Timing.INSTANTEOUS);
				Coordinates edge1[] = new Coordinates[2];
				Coordinates edge2[] = new Coordinates[2];

				edge1[0] = new Coordinates(zerox + rpointx - 5, zeroy - rpointy - 5);
				edge1[1] = new Coordinates(zerox + rpointx + 5, zeroy - rpointy + 5);
				edge2[0] = new Coordinates(zerox + rpointx - 5, zeroy - rpointy + 5);
				edge2[1] = new Coordinates(zerox + rpointx + 5, zeroy - rpointy - 5);

				lang.newPolyline(edge1, "cross1", null, kreuzprop);
				lang.newPolyline(edge2, "cross2", null, kreuzprop);

				if (psbool) {
					expl5[0].show();

					counterout.setFont(special, Timing.INSTANTEOUS, Timing.INSTANTEOUS);


					lang.nextStep();
					expl5[0].hide();

				}

			}

			// ES GIBT EINE CODEBGLEITUNG
			if (psbool) {

				// Es ist die letzte Iteration der CodeBegleitung
				if (PseudoCode == i + 1 || Iterationen - 1 == i) {
					// Es ist auch die letzte Iteration der Schleife
					if (PseudoCode >= Iterationen) {
						
						expl10[0].show();
						pseudoCode3.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
								Timing.INSTANTEOUS);
						pseudoCode4.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
								Timing.INSTANTEOUS);
						lang.nextStep();
						expl10[0].hide();

						counterin.setFont(font, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
						counterout.setFont(font, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

					}
					// Es ist nicht die letze Iteration der Schleife
					else {
						
						expl7[0].show();
						pseudoCode3.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
								Timing.INSTANTEOUS);
						pseudoCode4.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
								Timing.INSTANTEOUS);
						// REPEAT IST ROT BIS ZUM ENDE
						pseudoCode5.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS,
								Timing.INSTANTEOUS);
						lang.nextStep();

						counterin.setFont(font, Timing.MEDIUM, Timing.INSTANTEOUS);
						counterout.setFont(font, Timing.MEDIUM, Timing.INSTANTEOUS);

					}
				}

				// Es gibt weitere Iterationen mit Codebegleitung
				else {
                    
					
					expl6[0].show();
					
					pseudoCode3.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					pseudoCode4.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					pseudoCode5.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);

					lang.nextStep();
					
					expl6[0].hide();

					expl1[0].show();
					expl1[1].show();


					counterin.setFont(font, Timing.MEDIUM, Timing.INSTANTEOUS);
					counterout.setFont(font, Timing.MEDIUM, Timing.INSTANTEOUS);

					pseudoCode5.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					pseudoCode1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					lang.nextStep();
					expl1[0].hide();
					expl1[1].hide();


				}

			}
			counterall.setText("Punkte ingesamt:" + " " + String.valueOf(i + 1), null, Timing.INSTANTEOUS);

			// PI AUSGABEN COUNTer
			pi = 4 * ((double) incircle / (double) (i + 1));
			// LETZTE ITERATion keine Ausgabe
			if (i >= Iterationen - 10 && !psbool)
				textpi.setText("Pi ist derzeit:" + " " + "...", null, Timing.INSTANTEOUS);
			else
				textpi.setText("Pi ist derzeit:" + " " + String.format("%.4f", pi), null, Timing.INSTANTEOUS);

			if (j >= Iterationen / 10 && !psbool) {
				lang.nextStep();
				j = 0;
			}

		}
		/********************************************* SCHLEIFE ********************************************************/

		// ES GABE EINE CODEBEGLEITUNG
		if (PseudoCode > 0) {
			
			lang.nextStep();
			expl7[0].hide();

			expl8[0].show();
			pseudoCode5.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			pseudoCode6.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			expl8[0].hide();
			expl9[0].show();
			expl9[1].show();
			expl9[2].show();
			expl9[3].show();



			pseudoCode6.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);

			pseudoCode7.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		}

		Coordinates[] hahe = new Coordinates[2];
		hahe[0] = new Coordinates(zero.getX() , zero.getY() + 90);
		hahe[1] = new Coordinates(hahe[0].getX()+ 5, hahe[0].getY()+35);

		pi = 4d * ((double) incircle / (double) Iterationen);
		lang.nextStep();
        textprops.set("color", Color.BLACK);
		Text text = lang.newText(hahe[0], "Der berechnete Pi-Wert ist" + " " + String.format("%.4f",pi) + "!", "lul", null, textprops);
		text.setFont(finale, Timing.MEDIUM, Timing.INSTANTEOUS);
		
        Coordinates[] unter = new Coordinates[6];
        
        unter [0] = new Coordinates(hahe[0].getX()-3,  hahe[0].getY()+16);
        unter [1] = new Coordinates(hahe[0].getX()+355, hahe[0].getY()+16);
        unter [2] = new Coordinates(hahe[0].getX()+355, hahe[0].getY()+18);
        unter [3] = new Coordinates(hahe[0].getX()-3,  hahe[0].getY()+18);
        unter [4] = new Coordinates(hahe[0].getX()-3,  hahe[0].getY()+20);
        unter [5] = new Coordinates(hahe[0].getX()+355, hahe[0].getY()+20);
        
        lang.newPolyline(unter, "", null);

        lang.nextStep();
        double rpi = 3.1415;
        
		Font abschu = new Font("sanserif", Font.PLAIN, 22);
		
		Text abschluss = lang.newText(hahe[1], "Der approximierte Wert ist " + String.format("%.4f", Math.abs(pi - rpi) ) + " von Pi entfernt ", "me", null, textprops);
        Text abschluss1 = lang.newText(new Coordinates(hahe[1].getX(), hahe[1].getY()+24), 		"Als Faustregel gilt: Je hoeher die Iterationenanzahl, desto genauer ist die Apprixmation."
, "egalo", null, textprops);
        
        abschluss.setFont(abschu, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        abschluss1.setFont(abschu, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// text.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
		// Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		// lang.nextStep();
		
		
		        
        lang.nextStep();
        lang.hideAllPrimitives();
        
        headline.show();
        headerLine.show();
        
        erkl.setText("Verwendung: Die Monte-Carlo-Simulation ist nuetzlich, um analytisch schwer loesbare Probleme stochastisch zu loesen. ", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        erkl.show();
        erkl2.setText("Weitere Anwendungen: Berechnung von Pi mit der Buffonschen Nadelmethode, oder vieles Mehr", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        erkl2.show();
        erkl3.setText("Nachteile: Als stochastisches Verfahren ist die Monte-Carlo-Simulation abhaengig von den Zufallsvariablen und so unter", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        erkl3.show();
        erkl4.setText("Umstaenden weniger genau als andere Verfahren.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        erkl4.show();
		return lang.toString();
	}

	public String getName() {
		return "Monte-Carlo-Simulation";
	}

	public String getAlgorithmName() {
		return "Monte-Carlo-Simulation";
	}

	public String getAnimationAuthor() {
		return "Sascha Zenglein,Julian Schwind";
	}

	public String getDescription() {
		return "Die Monte-Carlo-Simulation ist ein stochastisches Verfahren, welches mit Zufallsexperimenten Probleme loest. \n"
				+ "Meist werden dafuer zufaellige Ereignisse per Computer mit einem geeigneten Algorithmus simuliert. \n"
				+ "Wir beschraenken uns auf eine Anwendung aus dem Bereich der Mathematik, naemlich die Approximation von Pi. \n"
				+ "Hierbei werden zufaellige Punkte generiert und anhand der Verteilung dieser Punkte Pi berechnet.";
	}

	public String getCodeExample() {
		return "for (i = 0; i < Iterationen; i++) {" + "\n" + " pointx = random;" + "\n" + " pointy = random" + "\n"
				+ " if (pointx * pointx + pointy * pointy <= 1f)" + "\n" + "  incircle++;" + "\n" + "}" + "\n"
				+ "return  4 * (incircle / Iterationen);" + "\n";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}
