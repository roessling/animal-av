/*
 * newLinkedList.java
 * Sascha Zenglein,Julian Schwind, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.datastructures;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.ListElement;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;

import algoanim.animalscript.AnimalScript;
import algoanim.properties.ListElementProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;

public class NewLinkedList implements Generator {
	private Language lang;
	private String[] list;
	private Coordinates nullCoords;
	private Text nullText;
	private int nullFontSize = 20;
	private Color farbeFirst;
	private Color farbeTmp;
	private int hinzufuegen;
	private int loeschen;
	private String name;

	public void init() {
		lang = new AnimalScript("LinkedList", "Sascha Zenglein,Julian Schwind", 800, 600);
	}

	private ListElement[] createListElements(Coordinates startPosition, String[] listContent) {
		ListElementProperties listprops = new ListElementProperties();
		listprops.set("boxFillColor", Color.WHITE);
		listprops.set("pointerAreaColor", Color.BLACK);
		listprops.set("pointerAreaFillColor", Color.WHITE);
		listprops.set("textColor", Color.RED);
		listprops.set("position", 1);

		ListElement[] listElements = new ListElement[listContent.length];

		Coordinates pos = startPosition;

		for (int i = 0; i < listContent.length; i++) {
			listprops.set("text", listContent[i]);
			listElements[i] = lang.newListElement(pos, 1, null, null, list[i], null, listprops);

			if (i > 0)
				listElements[i - 1].link(listElements[i], 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			int offset = listContent[i].length() * 10;
			pos = new Coordinates(pos.getX() + offset + 70, pos.getY());
		}

		Coordinates textPos = new Coordinates(pos.getX() + 20, pos.getY() + (int) (nullFontSize * 0.4f));
		nullCoords = new Coordinates(pos.getX() + 20, pos.getY());
		nullText = lang.newText(textPos, "NULL", "nulltext", null);

		Font f = new Font("serif", Font.PLAIN, nullFontSize);

		nullText.setFont(f, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		nullText.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		linkToNull(listElements[listElements.length - 1], nullText);
		return listElements;
	}

	public void linkToNull(ListElement last, Text nullText) {
		last.moveTo(AnimalScript.DIRECTION_N, "setTip", nullText.getUpperLeft(), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		// For some reason upperleft is definitely NOT the upper left of the
		// text, so some tweaking is necessary
		last.moveBy("setTip", -45, -(int) (nullFontSize * 0.4f), Timing.INSTANTEOUS, Timing.INSTANTEOUS);

	}

	private Node getFirstNode(Polyline line) {
	  Node[] nodes = line.getNodes();
	  if (nodes != null && nodes.length > 0)
	    return nodes[0];
	  return new Coordinates(0, 0);
	}
	public class ListElementPointer {
		int height;
		String label;
		Text textPrimitive;
		Rect rectBackground;
		Polyline line;

		public ListElementPointer(ListElement position, int height, String label, Color color) {
			this.label = label;
			this.height = height;

			PolylineProperties props = new PolylineProperties();
			props.set("fwArrow", true);
			props.set("depth", 5);
			props.set("color", color);

			Coordinates polyCoords[] = new Coordinates[2];

			polyCoords[0] = new Coordinates(0, 0);
			polyCoords[1] = new Coordinates(0, height);

			this.line = lang.newPolyline(polyCoords, "pointer", null, props);

			TextProperties textprops = new TextProperties();
			textprops.set("depth", 3);
			textprops.set("color", color);

			this.textPrimitive = lang.newText(getFirstNode(this.line), label, label, null, textprops);

			RectProperties rectprops = new RectProperties();
			rectprops.set("depth", 4);
			rectprops.set("color", Color.WHITE);
			rectprops.set("filled", true);
			rectprops.set("fillColor", Color.WHITE);

			int offset = textPrimitive.getText().length() * 6;
			rectBackground = lang.newRect(new Coordinates(0, 0), new Coordinates(offset + 10, 16), "rettung", null,
					rectprops);

			this.moveToWithoutSteps(position);
		}

		public int getHeight() {
			return height;
		}

		public Text getText() {
			return textPrimitive;
		}

		public Polyline getLine() {
			return line;
		}

		public void moveTo(ListElement le) {
			moveToWithoutSteps(le);
			lang.nextStep();

			lang.nextStep();
		}

		public void moveToWithoutSteps(ListElement le) {
			line.moveTo(AnimalScript.DIRECTION_N, "translate", le.getUpperLeft(), Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			line.moveBy("translate", 0, -getHeight(), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			textPrimitive.moveTo(AnimalScript.DIRECTION_N, "translate", le.getUpperLeft(), Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			int offset = textPrimitive.getText().length() * 6;
			textPrimitive.moveBy("translate", -offset / 2, -(20 + getHeight()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			// workaround because AnImAl
			rectBackground.moveTo(AnimalScript.DIRECTION_C, "translate", le.getUpperLeft(), Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			rectBackground.moveBy("translate", -offset / 2 - 5, -(20 + getHeight()), Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);

		}

		public void moveToNull() {
			line.moveTo(AnimalScript.DIRECTION_N, "translate", nullCoords, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			line.moveBy("translate", 0, -getHeight(), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			textPrimitive.moveTo(AnimalScript.DIRECTION_N, "translate", nullCoords, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			int offset = textPrimitive.getText().length() * 6;
			textPrimitive.moveBy("translate", -offset / 2, -(20 + getHeight()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			// workaround because AnImAl
			rectBackground.moveTo(AnimalScript.DIRECTION_C, "translate", nullCoords, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			rectBackground.moveBy("translate", -offset / 2 - 5, -(20 + getHeight()), Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			lang.nextStep();
			lang.nextStep();
		}

		public void hide() {
			textPrimitive.hide();
			line.hide();
			rectBackground.hide();
		}

		public void unhide() {
			textPrimitive.show();
			line.show();
			rectBackground.show();
		}

		public void moveBy(int offsetX, int offsetY) {
			textPrimitive.moveBy("translate", offsetX, offsetY, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			line.moveBy("translate", offsetX, offsetY, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rectBackground.moveBy("translate", offsetX, offsetY, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}

	}

	public void setColor(Text text, Color color) {
		text.changeColor(AnimalScript.COLORCHANGE_COLOR, color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		farbeFirst = (Color) primitives.get("FarbeFirst");
		farbeTmp = (Color) primitives.get("FarbeTmp");
		hinzufuegen = (int) primitives.get("Hinzufuegen");
		loeschen = (int) primitives.get("Loeschen");
		name = (String) primitives.get("Name");

		list = (String[]) primitives.get("Liste");
		TextProperties textprops = new TextProperties();

		lang.setStepMode(true);

		Coordinates startCoord = new Coordinates(100, 300);
		// ********************** �BERSCHRIFT *************************//
				Font f = new Font("serif", Font.ITALIC, 50);
				textprops.set("color", Color.BLACK);
				Text headline = lang.newText(new Coordinates(20, 50), "LinkedList", "�berschrift", null, textprops);
				Coordinates[] strich = new Coordinates[8];

				headline.setFont(f, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				// Stricht Unter �berschrift

				strich[0] = new Coordinates(15, 70);
				strich[1] = new Coordinates(230, 70);
				strich[2] = new Coordinates(230, 72);
				strich[3] = new Coordinates(15, 72);
				strich[4] = new Coordinates(15, 74);
				strich[5] = new Coordinates(230, 74);
				strich[6] = new Coordinates(230, 75);
				strich[7] = new Coordinates(15, 75);
				Polyline schnell = lang.newPolyline(strich, "strich", null);

				// ********************** �BERSCHRIFT *************************//
		
				
				// EINLEITUNGSTEXT
				
				
				Coordinates erkcord = new Coordinates(20,140);

				Font besch = new Font("serif", Font.ITALIC, 26);

				
				Text erkl = lang.newText(erkcord,"Eine Linked List ist eine Datenstruktur, die dazu verwendet wird , um eine beliebig grosse Menge an Elemente in einer kettenartigen Liste zu speichern.", null, null, textprops);
				Text erkl2 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+40),"Dazu hat jedes einzelne Listenelement einen Verweis auf das folgende Element, wobei das Letze auf nichts zeigt (null).", null, null, textprops);
				Text erkl3 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+80),"Um auf die Liste zuzugreifen wird nur ein Pointer auf das erste Listenelement benoetigt.", null, null, textprops);
				Text erkl4 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+120),"Die Liste ist eine rekursive Datenstruktur; das heisst, jedes ListenElement kann wiederum eine komplette Liste enthalten. ", null, null, textprops);
				Text erkl5 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+160),"Im Folgenden wird visualisiert, wie man durch so eine Liste durchlaeuft und wie Elemente hinzugefuegt sowie geloescht werden.", null, null, textprops);


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
				
				
				
		/*		"Eine Linked List ist eine Datenstruktur, die dazu verwendet wird , um eine beliebig grosse Menge an Elemente in einer kettenartigen Liste zu speichern."
				+ "\n"
				+ "Dazu hat jedes einzelne Listenelement einen Verweis auf das folgende Element, wobei das Letze auf nichts zeigt (null)."
				+ "\n" + "Um auf die Liste zuzugreifen wird nur ein Pointer auf das erste Listenelement benoetigt. "
				+ "\n" + "\n" + "Im Folgenden wird visualisiert, wie man durch so eine Liste durchlaeuft"
				+ "und wie Elemente hinzugefuegt sowie geloescht werden." + "\n";
				
			*/	
				
				
				
				
				
			

		ListElement[] listElements = createListElements(startCoord, list);
		lang.nextStep();


		

		int pseudox = 110;
		int pseudoy = 430;
		Font fo = new Font("serif", Font.BOLD, 24);

		
		Coordinates unter = new Coordinates(20,105);
		
		Text traverseunter = lang.newText(unter, "DURCHLAUFEN", name, null);
		
		
		traverseunter.setFont(fo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		
		Coordinates pseudo = new Coordinates(pseudox, pseudoy);
		Coordinates pseudo1 = new Coordinates(pseudo.getX(), pseudo.getY() + 30);
		Coordinates pseudo2 = new Coordinates(pseudo.getX(), pseudo1.getY() + 30);
		Coordinates pseudo3 = new Coordinates(pseudo.getX(), pseudo2.getY() + 30);
		Coordinates pseudo4 = new Coordinates(pseudo.getX(), pseudo3.getY() + 30);
		Coordinates pseudo5 = new Coordinates(pseudo.getX(), pseudo4.getY() + 30);
		Coordinates pseudo6 = new Coordinates(pseudo.getX(), pseudo5.getY() + 30);
		Coordinates pseudo7 = new Coordinates(pseudo.getX(), pseudo6.getY() + 30);
		Coordinates pseudo8 = new Coordinates(pseudo.getX(), pseudo7.getY() + 30);
		Coordinates pseudo9 = new Coordinates(pseudo.getX(), pseudo8.getY() + 30);
		Coordinates pseudo10 = new Coordinates(pseudo.getX(), pseudo9.getY() + 30);
		Coordinates pseudo11 = new Coordinates(pseudo.getX(), pseudo10.getY() + 30);
		Coordinates pseudo12 = new Coordinates(pseudo.getX(), pseudo11.getY() + 30);

		Font fpseudo = new Font("monospace", Font.PLAIN, 20);

		TextProperties pseudoprops = new TextProperties();
		Text traverse = lang.newText(pseudo, "listElement e = first;", "", null, pseudoprops);
		Text traverse1 = lang.newText(pseudo1, "while(e.hasNext())", "", null, pseudoprops);
		Text traverse2 = lang.newText(pseudo2, "    e = e.next;", "", null, pseudoprops);
		Text[] loesch = new Text[10];
		loesch[0] = lang.newText(pseudo, "if(s == 0){", "", null, pseudoprops);
		loesch[1] = lang.newText(pseudo1, "    first = first.next();", "", null, pseudoprops);
		loesch[2] = lang.newText(pseudo2, "} else {", "", null, pseudoprops);
		loesch[3] = lang.newText(pseudo3, "    for(listElement e = first; e.hasNext(); e = e.next());", "", null,
				pseudoprops);
		loesch[4] = lang.newText(pseudo4, "        i++;", "", null, pseudoprops);
		loesch[5] = lang.newText(pseudo5, "        if(i == s-1){", "", null, pseudoprops);
		loesch[6] = lang.newText(pseudo6, "            e.setNext(e.next().next());", "", null, pseudoprops);
		loesch[7] = lang.newText(pseudo7, "	       }", "", null, pseudoprops);
		loesch[8] = lang.newText(pseudo8, "    }", "", null, pseudoprops);
		loesch[9] = lang.newText(pseudo9, "}", "", null, pseudoprops);

		Text[] hinzufueg = new Text[13];
		hinzufueg[0] = lang.newText(pseudo, "if(i == 0){", "", null, pseudoprops);
		hinzufueg[1] = lang.newText(pseudo1, "    element.setNext(first);", "", null, pseudoprops);
		hinzufueg[2] = lang.newText(pseudo2, "    first = element;", "", null, pseudoprops);
		hinzufueg[3] = lang.newText(pseudo3, "} else {", "", null, pseudoprops);
		hinzufueg[4] = lang.newText(pseudo4, "    int i = 0;", "", null, pseudoprops);
		hinzufueg[5] = lang.newText(pseudo5, "    for(listElement e; e.hasNext(); e = e.next()){", "", null,
				pseudoprops);
		hinzufueg[6] = lang.newText(pseudo6, "        i++;", "", null, pseudoprops);
		hinzufueg[7] = lang.newText(pseudo7, "        if(i == s-1){", "", null, pseudoprops);
		hinzufueg[8] = lang.newText(pseudo8, "            element.setNext(e.next());", "", null, pseudoprops);
		hinzufueg[9] = lang.newText(pseudo9, "            e.setNext(element);", "", null, pseudoprops);
		hinzufueg[10] = lang.newText(pseudo10, "        }", "", null, pseudoprops);
		hinzufueg[11] = lang.newText(pseudo11, "    }", "", null, pseudoprops);
		hinzufueg[12] = lang.newText(pseudo12, "}", "", null, pseudoprops);

		for (int k = 0; k < loesch.length; k++) {
			loesch[k].hide();

		}

		for (int i = 0; i < hinzufueg.length; i++) {
			hinzufueg[i].hide();
		}
		
		
		Coordinates cord = new Coordinates(pseudox + 500, pseudoy);
		
		Coordinates cord1 = new Coordinates(pseudox + 500, pseudoy+30);

		Coordinates cord2 = new Coordinates(pseudox + 500, pseudoy+60);

		Coordinates cord3 = new Coordinates(pseudox + 500, pseudoy+90);

		
		Text travepxl = lang.newText(cord,"Dies ist die simple Schleife um die Liste zu durchlaufen," , name, null);
		Text travepx2 = lang.newText(cord1,"die im nachfolgenden verwendete Schreibweise ist aber folgende:  for(listElement e; e.hasNext(); e = e.next()){}", "wtf", null);
		

		
		
		
		

		traverse.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		traverse1.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		traverse2.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		travepxl.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		travepx2.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		ListElementPointer first = new ListElementPointer(listElements[0], 100, "first", farbeFirst);
		lang.nextStep();
		ListElementPointer pointer = new ListElementPointer(listElements[0], 50, "e", farbeTmp);
		setColor(traverse, Color.RED);
		lang.nextStep();
		setColor(traverse, Color.BLACK);
		

		

		// traverse
		for (int i = 1; i < listElements.length; i++) {
			setColor(traverse1, Color.RED);

			lang.nextStep();
			setColor(traverse1, Color.BLACK);

			setColor(traverse2, Color.RED);

			pointer.moveTo(listElements[i]);

			setColor(traverse2, Color.BLACK);

		}
		traverse.hide();
		traverse1.hide();
		traverse2.hide();

		for (int k = 0; k < loesch.length; k++) {
			loesch[k].setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			loesch[k].show();

		}
		
		traverseunter.hide();
		travepxl.hide();
		travepx2.hide();
		
		
		Text los = lang.newText(cord,"Um ein Element an der Stelle s zu loeschen, muss man die Liste wie bereits gezeigt durchlaufen und " , name, null);
		Text los1 = lang.newText(cord1,"den Pointer auf das vorhergehende Element speichern. Dann kann man das Element einfach 'ueberspringen'.", "wtf", null);
		Text los2 = lang.newText(cord2,"Fuer den Sonderfall s=0 ist eine spezielle Abfrage noetig, die den first-Pointer entsprechend setzt.", "wtf", null);

		los.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		los1.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		los2.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		
		
        Text loeschenunter = lang.newText(unter, "ELEMENT LOESCHEN", name, null);
		
		
		loeschenunter.setFont(fo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// delete
		lang.nextStep();
		int deletePos = loeschen;

		pointer.hide();

		// lang.nextStep();
		setColor(loesch[0], Color.RED);
		lang.nextStep();
		setColor(loesch[0], Color.BLACK);

		if (deletePos != 0) {
			pointer.unhide();

			for (int i = 0; i < listElements.length; i++) {
				setColor(loesch[3], Color.RED);
				pointer.moveTo(listElements[i]);
				setColor(loesch[3], Color.BLACK);
				setColor(loesch[4], Color.RED);
				lang.nextStep();
				setColor(loesch[4], Color.BLACK);
				setColor(loesch[5], Color.RED);
				lang.nextStep();
				setColor(loesch[5], Color.BLACK);

				if (i >= deletePos - 1) {
					break;
				}
			}
			setColor(loesch[6], Color.RED);

		}

		if (deletePos > listElements.length - 1)
			deletePos = listElements.length - 1;

		listElements[deletePos].moveBy("translateWithFixedTip", 0, 80, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		if (deletePos == listElements.length - 1) {
			// delete last Element
			listElements[deletePos - 1].moveBy("setTip", 0, 80, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			listElements[deletePos - 1].moveBy("setTip", 0, -80, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			listElements[deletePos - 1].moveTo(AnimalScript.DIRECTION_C, "setTip",
					new Coordinates(nullCoords.getX() - 80, nullCoords.getY()), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		} else if (deletePos == 0) {

			// delete first Element
			setColor(loesch[1], Color.RED);
			first.moveTo(listElements[1]);
			lang.nextStep();
			setColor(loesch[1], Color.BLACK);

		} else {
			listElements[deletePos - 1].moveBy("setTip", 0, 80, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			listElements[deletePos - 1].moveBy("setTip", 0, -80, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			listElements[deletePos - 1].link(listElements[deletePos + 1], 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}

		lang.nextStep();
		setColor(loesch[6], Color.BLACK);

		lang.nextStep();
		listElements[deletePos].hide();
		lang.nextStep();

		// cleanup
		for (int i = 0; i < listElements.length; i++) {
			listElements[i].hide();
		}
		nullText.hide();

		first.moveToWithoutSteps(listElements[0]);
		listElements = createListElements(startCoord, list);
		pointer.moveToWithoutSteps(listElements[0]);		

		for (int k = 0; k < loesch.length; k++) {
			loesch[k].hide();
		}

		loeschenunter.hide();
		los.hide();
		los1.hide();
		los2.hide();
		
		
		Text lo = lang.newText(cord,"Um ein Element element an der Stelle s einzufuegen, muss man die Liste erneut durchlaufen und" , name, null);
		Text lo1 = lang.newText(cord1,"wieder das den Pointer auf das vorhergehende Element speichern. Dann kann man element", "wtf", null);
		Text lo2 = lang.newText(cord2,"als Nachfolger den Rest der Liste anhaengen und element als Nachfolger des vorhergehenden Elements setzen.", "wtf", null);
		Text lo3 = lang.newText(cord3,"Fuer s=0 ist wieder eine spezielle Abfrage noetig, da der first-Pointer entsprechend gesetzt werden muss.", "wtf", null);

		
		lo.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lo1.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lo2.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lo3.setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		
		 Text hinzuunter = lang.newText(unter, "ELEMENT HINZUFUEGEN", name, null);
			
			
			hinzuunter.setFont(fo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		
		
		// insert
		String newElement = name;
		int newElementPos = hinzufuegen;

		int offsetX = newElement.length() * 10 + 70;

		for (int i = 0; i < hinzufueg.length; i++) {
			hinzufueg[i].setFont(fpseudo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			hinzufueg[i].show();
		}

		if (newElementPos <= 0) {
			newElementPos = 0;
			// pointer is unnecessary if we delete the first element
			pointer.hide();
		} else
			pointer.unhide();

		if (newElementPos >= listElements.length) {
			newElementPos = listElements.length;
		}
		
		lang.nextStep();

		setColor(hinzufueg[0], Color.RED);
		lang.nextStep();
		setColor(hinzufueg[0], Color.BLACK);
		if (newElementPos != 0) {
			setColor(hinzufueg[4], Color.RED);
             lang.nextStep();
 			setColor(hinzufueg[4], Color.BLACK);
			for (int i = 0; i < listElements.length; i++) {
				setColor(hinzufueg[5], Color.RED);
				pointer.moveTo(listElements[i]);
				setColor(hinzufueg[5], Color.BLACK);
				setColor(hinzufueg[6], Color.RED);
				lang.nextStep();
				setColor(hinzufueg[6], Color.BLACK);
				setColor(hinzufueg[7], Color.RED);
				lang.nextStep();
				setColor(hinzufueg[7], Color.BLACK);

				if (i >= newElementPos - 1) {
					break;
				}
			}
		}

		
		// move all listElements to the right
		for (int i = newElementPos; i < listElements.length; i++) {
			listElements[i].moveBy("translate", offsetX, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}
		nullText.moveBy("translate", offsetX, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		if (newElementPos > 0) {
			listElements[newElementPos - 1].moveBy("setTip", offsetX, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		} else {
			first.moveBy(offsetX, 0);
		}

		lang.nextStep();

		ListElementProperties listprops = new ListElementProperties();
		listprops.set("boxFillColor", Color.WHITE);
		listprops.set("pointerAreaColor", Color.BLACK);
		listprops.set("pointerAreaFillColor", Color.WHITE);
		listprops.set("textColor", Color.RED);
		listprops.set("position", 1);
		listprops.set("text", newElement);

		Node newElemPosition = startCoord;
		if (newElementPos > 0) {
			newElemPosition = listElements[newElementPos - 1].getUpperLeft();
		}

		ListElement newElem = lang.newListElement(newElemPosition, 1, null, null, "newElement", null, listprops);

		int lastOffset = 0;
		if (newElementPos > 0) {
			lastOffset = list[newElementPos - 1].length() * 10 + 70;
		}
		
		newElem.moveBy("translate", lastOffset, 80, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		lang.nextStep();
		lang.nextStep();
		

		if (newElementPos == listElements.length) {
			setColor(hinzufueg[8], Color.RED);
			lang.nextStep();
			setColor(hinzufueg[8], Color.BLACK);
			newElem.moveTo(AnimalScript.DIRECTION_C, "setTip", nullText.getUpperLeft(), Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			newElem.moveBy("setTip", 35, -(int) (nullFontSize * 0.4f), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			// linkToNull(newElem, nullText);
			// newElem.moveBy("setTip", 110, 0, Timing.INSTANTEOUS,
			// Timing.INSTANTEOUS);
		} else if (newElementPos > 0){
			newElem.link(listElements[newElementPos], 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			setColor(hinzufueg[8], Color.RED);
			lang.nextStep();
			setColor(hinzufueg[8], Color.BLACK);
		} else {
			newElem.link(listElements[newElementPos], 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			setColor(hinzufueg[1], Color.RED);
			lang.nextStep();
			setColor(hinzufueg[1], Color.BLACK);
		}
		
		lang.nextStep();

		newElem.moveBy("translatewithfixedtip", 0, -80, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		if (newElementPos == 0) {
			setColor(hinzufueg[2], Color.RED);
			first.moveTo(newElem);
			lang.nextStep();
			first.moveTo(newElem);
			setColor(hinzufueg[2], Color.BLACK);
		} else {
			setColor(hinzufueg[9], Color.RED);
			listElements[newElementPos - 1].link(newElem, 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			listElements[newElementPos - 1].moveBy("setTip", 0, -80, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			setColor(hinzufueg[9], Color.BLACK);
		}


		lang.nextStep();
		
		lang.hideAllPrimitives();
		headline.show();
		schnell.show();
		pointer.hide();
		
		
		
		Text erl = lang.newText(erkcord,"Es wurde gezeigt, wie gaengige Listenoperationen auf einer LinkedList ausgefuehrt werden koennen. Es wurde deutlich,", null, null, textprops);
		Text erl2 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+40)," dass einzelne ListenElemente als komplette Liste betrachtet werden koennen.", null, null, textprops);
		Text erl3 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+80)," Vorteil einer LinkedList:", null, null, textprops);
		Text erl4 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+120),"     Die LinkedList kann im Gegensatz zu Arrays beliebig grosse Datenmengen speichern und ist sehr flexibel", null, null, textprops);
		Text erl5 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+160)," Nachteil:", null, null, textprops);
		Text erl6 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+200),"     Da die LinkedList immer durchlaufen werden muss, ist sie sehr langsam.", null, null, textprops);
		Text erl7 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+240),"     Arrays mit direktem Speicherzugriff sind um ein vielfaches schneller.", null, null, textprops);
		Text erl8 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+280)," Ausblick:", null, null, textprops);
		Text erl9 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+320),"     Es existieren Kompromisse zwischen Arrays und Listen, so zum Beispiel die ArrayList.", null, null, textprops);
		Text erl10 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+360),"     Sie vereint den Komfort einer LinkedList mit der Geschwindigkeit eines Arrays.", null, null, textprops);
		Text erl11 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+400),"     Andere rekursive Datenstrukturen funktionieren nach dem selben Prinzip wie die Liste.", null, null, textprops);
		Text erl12 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+440),"     So koennen zum Beispiel Baumstrukturen aehnlich dargestellt werden.", null, null, textprops);
		Text erl13 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+480),"     Je nach Anwendung kann die LinkedList angepasst werden. Muss man oft auf Elemente am Ende der Liste zugreifen,", null, null, textprops);
		Text erl14 = lang.newText(new Coordinates(erkcord.getX(), erkcord.getY()+520),"     bietet sich eine Doubly Linked List an, bei der jedes Element zusaetzlich einen Zeiger auf seinen Vorgaenger besitzt.", null, null, textprops);



		erl.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl2.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl3.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl4.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl5.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl6.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl7.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl8.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl9.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl10.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl11.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl12.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl13.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		erl14.setFont(besch, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		
		
		
		
		
		
		
		
		
		lang.nextStep();

		return lang.toString();
	}

	public String getName() {
		return "LinkedList";
	}

	public String getAlgorithmName() {
		return "Traversieren, Element hinzuf�gen und l�schen";
	}

	public String getAnimationAuthor() {
		return "Sascha Zenglein,Julian Schwind";
	}

	public String getDescription() {
		return "Eine Linked List ist eine Datenstruktur, die dazu verwendet wird , um eine beliebig grosse Menge an Elemente in einer kettenartigen Liste zu speichern."
				+ "\n"
				+ "Dazu hat jedes einzelne Listenelement einen Verweis auf das folgende Element, wobei das Letze auf nichts zeigt (null)."
				+ "\n" + "Um auf die Liste zuzugreifen wird nur ein Pointer auf das erste Listenelement benoetigt. "
				+ "\n" + "\n" + "Im Folgenden wird visualisiert, wie man durch so eine Liste durchlaeuft"
				+ "und wie Elemente hinzugefuegt sowie geloescht werden." + "\n";
	}

	public String getCodeExample(){
        return "Durchlaufen der Liste:"
 +"\n"
 +"\n"
 +"listElement e = first;"
 +"\n"
 +"while(e.hasNext())"
 +"\n"
 +" e = e.next;"
 +"\n"
 +"\n"
 +"Einf�gen von Element element an Position s"
 +"\n"
 +"\n"
 +"int i = 0;"
 +"\n"
 +"if(i == 0){"
 +"\n"
 +"    element.setNext(first);"
 +"\n"
 +"    first = element;"
 +"\n"
 +"} else {"
 +"\n"
 +"    for(listElement e; e.hasNext(); e = e.next()){"
 +"\n"
 +"        i++;"
 +"\n"
 +"        if(i == s-1){"
 +"\n"
 +"            element.setNext(e.next());"
 +"\n"
 +"            e.setNext(element);"
 +"\n"
 +"        }"
 +"\n"
 +"    }"
 +"\n"
 +"}"
 +"\n"
 +"\n"
 +"L�schen Position s"
 +"\n"
 +"\n"
 +"Stelle: s"
 +"\n"
 +"if(s == 0){"
 +"\n"
 +"    first = first.next();"
 +"\n"
 +"} else {"
 +"\n"
 +"    for(listElement e = first; e.hasNext(); e = e.next()){"
 +"\n"
 +"        i++;"
 +"\n"
 +"        if(i == s-1){"
 +"\n"
 +"            e.setNext(e.next().next());"
 +"\n"
 +"        }"
 +"\n"
 +"    }"
 +"\n"
 +"}"
 +"\n";
    }

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

}
