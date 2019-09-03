package generators.graph.lindenmayer;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;

import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Node;

import generators.graphics.helpers.Coordinate;

public class LindenmayerDrawer {

	public static Coordinates ursprung = new Coordinates(900, 650); //wird auf dem stack gespeichert
	public static Coordinates spitze = new Coordinates(900, 600); //wird auf dem stack gespeichert
	public static LinkedList<Coordinates> saveCoord = new LinkedList<Coordinates>(); 
	public static LinkedList<Coordinates> saveOff = new LinkedList<Coordinates>();
	public static LinkedList<Coordinates> saveold = new LinkedList<Coordinates>();
	public static LinkedList<Coordinates> saveak = new LinkedList<Coordinates>();

	public static LinkedList<Integer> offsetCounter = new LinkedList<Integer>();
	public static int akoordX = ursprung.getX(); //wird auf dem stack gespeichert
	public static int akoordY = ursprung.getY(); //wird auf dem stack gespeichert
	private static Coordinates offN = new Coordinates(0, 50); // 0-50 //werden auf dem stack gespeichert
	private static Coordinates dirNfuss = new Coordinates(80, 330);
	private static Coordinates dirNspitze = new Coordinates(80, 280);
	private static Coordinates offNW = new Coordinates(35, 35); // -35-35
	private static Coordinates dirNWfuss = new Coordinates(80, 330);
	private static Coordinates dirNWspitze = new Coordinates(45, 295);
	private static Coordinates offNE = new Coordinates(35, 35);// 35-35
	private static Coordinates dirNEfuss = new Coordinates(80, 330);
	private static Coordinates dirNEspitze = new Coordinates(115, 295);
	private static Coordinates offE = new Coordinates(40, 0);
	private static Coordinates dirEfuss = new Coordinates(80, 330);
	private static Coordinates dirEspitze = new Coordinates(120, 330);
	private static Coordinates offW = new Coordinates(40, 0);// -40 0
	private static Coordinates dirWfuss = new Coordinates(80, 330);
	private static Coordinates dirWspitze = new Coordinates(40, 330);
	private static Coordinates offSW = new Coordinates(35, 35); // -35 35
	private static Coordinates dirSWfuss = new Coordinates(80, 330);
	private static Coordinates dirSWspitze = new Coordinates(45, 365);
	private static Coordinates offSE = new Coordinates(35, 35);
	private static Coordinates dirSEfuss = new Coordinates(80, 330);
	private static Coordinates dirSEspitze = new Coordinates(115, 365);
	private static Coordinates offS = new Coordinates(0, 50);
	private static Coordinates dirSfuss = new Coordinates(80, 330);
	private static Coordinates dirSspitze = new Coordinates(80, 380);
	private static Coordinates offset = offN; //wird auf dem stack gespeichert
	public static Coordinates oldUrsprung = new Coordinates(0, 0);
	public static Coordinates oldSpitze = new Coordinates(0, 0);
	
	private static SourceCode drawSrc;

	public LindenmayerDrawer() {
//		ursprung = new Coordinates(900, 650); //wird auf dem stack gespeichert
//		spitze = new Coordinates(900, 600); //wird auf dem stack gespeichert
//		saveCoord = new LinkedList<Coordinates>(); 
//		saveOff = new LinkedList<Coordinates>();
//		saveold = new LinkedList<Coordinates>();
//		saveak = new LinkedList<Coordinates>();
//
//		offsetCounter = new LinkedList<Integer>();
//		akoordX = ursprung.getX(); //wird auf dem stack gespeichert
//		akoordY = ursprung.getY(); //wird auf dem stack gespeichert
//		offN = new Coordinates(0, 50); // 0-50 //werden auf dem stack gespeichert
//		dirNfuss = new Coordinates(80, 330);
//		dirNspitze = new Coordinates(80, 280);
//		offNW = new Coordinates(35, 35); // -35-35
//		dirNWfuss = new Coordinates(80, 330);
//		dirNWspitze = new Coordinates(45, 295);
//		offNE = new Coordinates(35, 35);// 35-35
//		dirNEfuss = new Coordinates(80, 330);
//		dirNEspitze = new Coordinates(115, 295);
//		offE = new Coordinates(40, 0);
//		dirEfuss = new Coordinates(80, 330);
//		dirEspitze = new Coordinates(120, 330);
//		offW = new Coordinates(40, 0);// -40 0
//		dirWfuss = new Coordinates(80, 330);
//		dirWspitze = new Coordinates(40, 330);
//		offSW = new Coordinates(35, 35); // -35 35
//		dirSWfuss = new Coordinates(80, 330);
//		dirSWspitze = new Coordinates(45, 365);
//		offSE = new Coordinates(35, 35);
//		dirSEfuss = new Coordinates(80, 330);
//		dirSEspitze = new Coordinates(115, 365);
//		offS = new Coordinates(0, 50);
//		dirSfuss = new Coordinates(80, 330);
//		dirSspitze = new Coordinates(80, 380);
//		offset = offN; //wird auf dem stack gespeichert
//		oldUrsprung = new Coordinates(0, 0);
//		oldSpitze = new Coordinates(0, 0);
	}	
	
	public static boolean equals(Coordinates a, Coordinates b){
		
		return(a.getX() == b.getX() && a.getY() == b.getY());
		
	}
	
	public static void drawArrows(String res) {
		System.out.println("RES=" + res);
		Text cdopText = LindenmayerAPIGenerator.lang.newText(new Coordinates(20, 380), "Aktuelle Richtung des Pfeils",
				"cs", null, LindenmayerAPIGenerator.shownTextProperties);
		drawSrc = LindenmayerAPIGenerator.lang.newSourceCode(new Coordinates(700, 360), "drawSource", null,
				LindenmayerAPIGenerator.sourceCodeProperties);
		Text templang = LindenmayerAPIGenerator.lang.newText(new Coordinates(870, 680), "", "t1", null,
				LindenmayerAPIGenerator.shownTextProperties);

		int cP = 0;
		Polyline dirpoly = LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNfuss, dirNspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);

		for (int i = 0; i < res.length(); i++) {

			switch (res.charAt(i)) {
			
			case 'F':
				dirpoly.hide();
						if (equals(offset,offN) && ((cP + 8) % 8) == 0){
							spitze = new Coordinates (akoordX + offset.getX(), akoordY - offset.getY());
					LindenmayerAPIGenerator.lang.newPolyline(new Node[] { ursprung, spitze, }, "f1", null,
							LindenmayerAPIGenerator.currentPolyline);
					
					dirpoly = LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNfuss, dirNspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					LindenmayerAPIGenerator.lang.newCircle(spitze,
							5, "currentPosition" + i, null,	LindenmayerAPIGenerator.currentCirclePosition); // 1
					akoordX += offset.getX();
					akoordY -= offset.getY();
//					spitze = new Coordinates (akoordX, akoordY);
					oldUrsprung = ursprung;
					oldSpitze = spitze;
					ursprung = new Coordinates(akoordX, akoordY);
					//f�r Pfeil
					System.out.println("Ou" + i + oldUrsprung.getX() + "y" + oldUrsprung.getY());
					System.out.println("Os" + i + oldSpitze.getX() + "y" + oldSpitze.getY());				
				} else if (equals(offset,offNW) && ((cP + 8) % 8) == 1) {
					// -35-35
					spitze = new Coordinates (akoordX - offset.getX(), akoordY - offset.getY());					

					LindenmayerAPIGenerator.lang.newPolyline(new Node[] {
							ursprung, spitze, }, "f1", null, LindenmayerAPIGenerator.currentPolyline);
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNWfuss, dirNWspitze}, "d1",
							null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					LindenmayerAPIGenerator.lang.newCircle(spitze,
							5, "currentPosition" + i, null, LindenmayerAPIGenerator.currentCirclePosition); // 1
					akoordX -= offset.getX();
					akoordY -= offset.getY();
					oldUrsprung = ursprung;
					oldSpitze = spitze;
					ursprung = new Coordinates(akoordX, akoordY);

				} else if (equals(offset,offNE) && ((cP + 8) % 8) == 7) {
					// 35-35
					spitze = new Coordinates (akoordX + offset.getX(), akoordY - offset.getY());

					LindenmayerAPIGenerator.lang.newPolyline(new Node[] {
							ursprung, spitze, }, "f1", null, LindenmayerAPIGenerator.currentPolyline);
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNEfuss, dirNEspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					LindenmayerAPIGenerator.lang.newCircle(spitze,
							5, "currentPosition" + i, null, LindenmayerAPIGenerator.currentCirclePosition); // 1
					akoordX += offset.getX();
					akoordY -= offset.getY();
					oldUrsprung = ursprung;
					oldSpitze = spitze;
					ursprung = new Coordinates(akoordX, akoordY);

				} else if (equals(offset,offE) && ((cP + 8) % 8) == 6) {
					spitze = new Coordinates (akoordX + offset.getX(), akoordY + offset.getY());

					LindenmayerAPIGenerator.lang.newPolyline(new Node[] {
							ursprung, spitze, }, "f1", null, LindenmayerAPIGenerator.currentPolyline);
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirEfuss, dirEspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					LindenmayerAPIGenerator.lang.newCircle(spitze,
							5, "currentPosition" + i, null, LindenmayerAPIGenerator.currentCirclePosition); // 1
					akoordX += offset.getX();
					akoordY += offset.getY();
					oldUrsprung = ursprung;
					oldSpitze = spitze;
					ursprung = new Coordinates(akoordX, akoordY);

				} else if (equals(offset,offW) && ((cP + 8) % 8) == 2) {
					// -40 0
					spitze = new Coordinates (akoordX - offset.getX(), akoordY - offset.getY());

					LindenmayerAPIGenerator.lang.newPolyline(new Node[] {
							ursprung, spitze, }, "f1", null, LindenmayerAPIGenerator.currentPolyline);
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirWfuss, dirWspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					LindenmayerAPIGenerator.lang.newCircle(spitze,
							5, "currentPosition" + i, null, LindenmayerAPIGenerator.currentCirclePosition); // 1
					akoordX -= offset.getX();
					akoordY -= offset.getY();
					oldUrsprung = ursprung;
					oldSpitze = spitze;
					ursprung = new Coordinates(akoordX, akoordY);
			
				} else if (equals(offset,offSW) && ((cP + 8) % 8) == 3) {
					spitze = new Coordinates (akoordX - offset.getX(), akoordY + offset.getY());

					LindenmayerAPIGenerator.lang.newPolyline(new Node[] {
							ursprung, spitze, }, "f1", null, LindenmayerAPIGenerator.currentPolyline);
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirSWfuss, dirSWspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					LindenmayerAPIGenerator.lang.newCircle(spitze,
							5, "currentPosition" + i, null, LindenmayerAPIGenerator.currentCirclePosition); // 1
					akoordX -= offset.getX();
					akoordY += offset.getY();
					oldUrsprung = ursprung;
					oldSpitze = spitze;
					ursprung = new Coordinates(akoordX, akoordY);

				} else if (equals(offset,offSE) && ((cP + 8) % 8) == 5) {
					spitze = new Coordinates (akoordX + offset.getX(), akoordY + offset.getY());

					LindenmayerAPIGenerator.lang.newPolyline(new Node[] {
							ursprung, spitze, }, "f1", null, LindenmayerAPIGenerator.currentPolyline);
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirSEfuss, dirSEspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					LindenmayerAPIGenerator.lang.newCircle(spitze,
							5, "currentPosition" + i, null, LindenmayerAPIGenerator.currentCirclePosition); // 1
					akoordX += offset.getX();
					akoordY += offset.getY();
					oldUrsprung = ursprung;
					oldSpitze = spitze;
					ursprung = new Coordinates(akoordX, akoordY);
		
				} else if (equals(offset,offS) && ((cP + 8) % 8) == 4) {
					spitze = new Coordinates (akoordX + offset.getX(), akoordY + offset.getY());

					LindenmayerAPIGenerator.lang.newPolyline(new Node[] {
							ursprung, spitze, }, "f1", null, LindenmayerAPIGenerator.currentPolyline);
					dirpoly = LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirSfuss, dirSspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					LindenmayerAPIGenerator.lang.newCircle(spitze,
							5, "currentPosition" + i, null, LindenmayerAPIGenerator.currentCirclePosition); // 1
					akoordX += offset.getX();
					akoordY += offset.getY();
					oldUrsprung = ursprung;
					oldSpitze = spitze;
					ursprung = new Coordinates(akoordX, akoordY);
			
				}
				templang.setText(templang.getText()+"F", null, null);
				LindenmayerAPIGenerator.src.highlight("f");
				LindenmayerAPIGenerator.lang.nextStep();
				LindenmayerAPIGenerator.src.unhighlight("f");
				LindenmayerAPIGenerator.lang.newPolyline(new Node[] { oldUrsprung, 
						oldSpitze, }, "f1", null,
						LindenmayerAPIGenerator.drawnPolyline);

				LindenmayerAPIGenerator.lang.newCircle(oldSpitze,
						5, "currentPosition" + i, null, LindenmayerAPIGenerator.cpDel); // 1
				
			//	dirpoly.show();
				break;
			case '+':
				dirpoly.hide();
				if (((cP + 8) % 8) == 0) { //N
					offset = offNW;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNWfuss, dirNWspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP++;
				} else if (((cP + 8) % 8) == 1) { //NW
					offset = offW;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirWfuss, dirWspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP++;
				} else if (((cP + 8) % 8) == 2) { //W
					offset = offSW;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirSWfuss, dirSWspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP++;
				} else if (((cP + 8) % 8) == 3) {//SW
					offset = offS;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirSfuss, dirSspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP++;
				} else if (((cP + 8) % 8) == 4) {//S
					offset = offSE;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirSEfuss, dirSEspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP++;
				} else if (((cP + 8) % 8) == 5) {//SE
					offset = offE;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirEfuss, dirEspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP++;
				} else if (((cP + 8) % 8) == 6) {//E
					offset = offNE;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNEfuss, dirNEspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP++;
				} else if (((cP + 8) % 8) == 7) {//NE
					offset = offN;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNfuss, dirNspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP++;
				}
				templang.setText(templang.getText()+"+", null, null);
				LindenmayerAPIGenerator.src.highlight("+");
				LindenmayerAPIGenerator.lang.nextStep();
				LindenmayerAPIGenerator.src.unhighlight("+");
//				LindenmayerAPIGenerator.lang.newPolyline(new Node[] { oldUrsprung, 
//						oldSpitze, }, "f1", null,
//						LindenmayerAPIGenerator.drawnPolyline);
				break;
			case '-':
				dirpoly.hide();
				if (((cP + 80) % 8) == 0) {//N
					offset = offNE;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNEfuss, dirNEspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP--;
				} else if (((cP + 80) % 8)== 7) {//NE
					offset = offE;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirEfuss, dirEspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP--;
				} else if (((cP + 80) % 8) == 6) {//E
					offset = offSE;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirSEfuss, dirSEspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP--;
				} else if (((cP + 80) % 8)   == 5) {//SE
					offset = offS;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirSfuss, dirSspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP--;
				} else if (((cP + 80) % 8) == 4) {//S
					offset = offSW;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirSWfuss, dirSWspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP--;
				} else if (((cP + 80) % 8) == 3) {//SW
					offset = offW;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirWfuss, dirWspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP--;
				} else if (((cP + 80) % 8) == 2) {//W
					offset = offNW;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNWfuss, dirNWspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP--;
				} else if (((cP + 80) % 8) == 1) {//NW
					offset = offN;
					dirpoly =	LindenmayerAPIGenerator.lang.newPolyline(new Node[] {dirNfuss, dirNspitze}, "d1", null, LindenmayerAPIGenerator.currentDirectionOfPolyline);
					cP--;
				}
				templang.setText(templang.getText()+"-", null, null);
				LindenmayerAPIGenerator.src.highlight("-");
				LindenmayerAPIGenerator.lang.nextStep();
				LindenmayerAPIGenerator.src.unhighlight("-");
//				LindenmayerAPIGenerator.lang.newPolyline(new Node[] { oldUrsprung, 
//						oldSpitze, }, "f1", null,
//						LindenmayerAPIGenerator.drawnPolyline);
				break;
			case '[':
//				LindenmayerAPIGenerator.lang.newPolyline(new Node[] {ursprung,
//						new Coordinates(akoordX + offset.getX(), akoordY
//								- offset.getY()), }, "f1", null,
//						LindenmayerAPIGenerator.currentPolyline);
				
				saveCoord.push(spitze); //1
				saveCoord.push(ursprung); //2
     			saveOff.push(offset); //3
				saveold.push(oldSpitze); //5
				saveold.push(oldUrsprung); //6
				saveak.push(new Coordinates(akoordX, akoordY)); //4
				offsetCounter.add(((cP + 8) % 8));//2 | 2,5
				
				System.out.println(i+"akX " + akoordX);
				System.out.println(i+"akY " + akoordY);
				System.out.println(i+"oldUrsp " + oldUrsprung.getX() + " y " + oldUrsprung.getY());
				System.out.println(i+"oldSpit " + oldSpitze + " y " + oldSpitze.getY());
				System.out.println(i+"offset " + offset.getX() + " y " + offset.getY());
				System.out.println(i+"ursprung " + ursprung.getX() + " y " + ursprung.getY());
				System.out.println(i+"spitze " + spitze.getX() + " y " + spitze.getY());
				System.out.println(i+"offsetCounter "+offsetCounter);
//				System.out.println("sa" + i + spitze.getX() + "y" + spitze.getY());
//				System.out.println("ua" + i + ursprung.getX() + "y" + ursprung.getY());
				templang.setText(templang.getText()+"[", null, null);
				LindenmayerAPIGenerator.src.highlight("[");
				LindenmayerAPIGenerator.lang.newCircle(oldSpitze,
						5, "currentPosition" + i, null, LindenmayerAPIGenerator.savedCirclePosition); // 1
				LindenmayerAPIGenerator.lang.nextStep();
//				LindenmayerAPIGenerator.lang.newPolyline(new Node[] { oldUrsprung, 
//						oldSpitze, }, "f1", null,
//						LindenmayerAPIGenerator.drawnPolyline);
				break;
			case ']':
				akoordX = saveak.getLast().getX(); //bevor 4 gel�scht wird
				akoordY = saveak.pop().getY();//4
//				saveCoord.pop();
				oldUrsprung = saveold.pop(); //6
				oldSpitze = saveold.pop(); //5
				offset = saveOff.pop(); //3
				ursprung = saveCoord.pop(); //2
				spitze = saveCoord.pop(); //1
//				oldUrsprung = ursprung;
//				oldSpitze = spitze;
				cP = offsetCounter.getLast();
				offsetCounter.removeLast();
				System.out.println(i+"akX " + akoordX);
				System.out.println(i+"akY " + akoordY);
				System.out.println(i+"oldUrsp " + oldUrsprung.getX() + " y " + oldUrsprung.getY());
				System.out.println(i+"oldSpit " + oldSpitze.getX() + " y " + oldSpitze.getY());
				System.out.println(i+"offset " + offset.getX() + " y " + offset.getY());
				System.out.println(i+"ursprung " + ursprung.getX() + " y " + ursprung.getY());
				System.out.println(i+"spitze " + spitze.getX() + " y " + spitze.getY());
				System.out.println(i+"offsetCounter "+offsetCounter);
				templang.setText(templang.getText()+"]", null, null);
				LindenmayerAPIGenerator.src.highlight("]");
				LindenmayerAPIGenerator.lang.newCircle(oldSpitze,
						5, "currentPosition" + i, null, LindenmayerAPIGenerator.currentCirclePosition); // 1
				LindenmayerAPIGenerator.lang.nextStep();
				LindenmayerAPIGenerator.src.unhighlight("[");
				LindenmayerAPIGenerator.src.unhighlight("]");
				break;
			default:
				break;
			}	
		}
//		LindenmayerAPIGenerator.src.unhighlight("f");
//		LindenmayerAPIGenerator.src.unhighlight("+");
//		LindenmayerAPIGenerator.src.unhighlight("-");
//		LindenmayerAPIGenerator.src.unhighlight("[");
//		LindenmayerAPIGenerator.src.unhighlight("]");

	}

	
	
}
