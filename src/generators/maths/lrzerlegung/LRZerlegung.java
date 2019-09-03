package generators.maths.lrzerlegung;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


/**
 * @author Volkan Hacimüftüoglu (volkan.hacimueftueoglu@stud.tu-darmstadt.de)
 */
public class LRZerlegung implements ValidatingGenerator  {
	private Language lang;

	public void init() {
		lang = new AnimalScript("LR-Zerlegung (Spaltenpivotsuche) [DE]",
				"Volkan Hacimüftüoglu", 640, 480);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		String[][] Eingabematrix = (String[][]) primitives.get("Eingabematrix");
		double[][] doubleMatrix = new double[Eingabematrix.length][Eingabematrix.length];

		for (int i = 0; i < doubleMatrix.length; i++) {
			for (int j = 0; j < doubleMatrix.length; j++) {
				doubleMatrix[i][j] = Double.parseDouble(Eingabematrix[i][j]);
			}

		}

		
		
        TextProperties Ueberschrift = (TextProperties)props.getPropertiesByName("Ueberschrift");
        TextProperties MatrixFarbeHervorhebung = (TextProperties)props.getPropertiesByName("Matrix Farbe Hervorhebung");
        TextProperties Texttest = (TextProperties)props.getPropertiesByName("Text");
        SourceCodeProperties Fazit = (SourceCodeProperties)props.getPropertiesByName("Fazit");
        TextProperties MatrixZeilentauschHervorhebung1 = (TextProperties)props.getPropertiesByName("Matrix Zeilentausch Hervorhebung 1");
        SourceCodeProperties Sourcecode = (SourceCodeProperties)props.getPropertiesByName("Sourcecode");
        TextProperties MatrixZeilentauschHervorhebung2 = (TextProperties)props.getPropertiesByName("Matrix Zeilentausch Hervorhebung 2");
        TextProperties MatrixFarbeHervorhebung2 = (TextProperties)props.getPropertiesByName("Matrix Farbe Hervorhebung 2");
        RectProperties Rechteck = (RectProperties)props.getPropertiesByName("Rechteck");
        
        
      
		
		
		
		
		
		MatrixProperties mp = (MatrixProperties) props
				.getPropertiesByName("matrix");

		// /\/\/\/\/\/\/\/\/\/\/\/\/\Code begin/\/\/\/\/\/\/\/\/\/\/\/\/\/\
		lang.setStepMode(true); // Schrittmodus aktivieren!
		/**
		 * the source code shown in the animation
		 */
		
		RectProperties rp = new RectProperties();
		Ueberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Arial", Font.PLAIN, 24));
		
		Rect rect = lang.newRect(new Coordinates(20,15), new Coordinates(450,65), "Rechteck", null,Rechteck);
		Text TextimRecheck = lang.newText(new Coordinates(30,40), "LR-Zerlegung mit Spaltenpivotsuche", "TextImRechteck", null, Ueberschrift);
	
		SourceCodeProperties sourceCodeProps;

		Sourcecode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));


		SourceCode src;
		src = lang.newSourceCode(new Coordinates(1020, 50), "sourceCode", null,
				Sourcecode);

		src.addCodeLine("double[][] R;", null, 1, null); // 4
		src.addCodeLine("double[][] L;", null, 1, null); // 4
		src.addCodeLine("double[][] Einheitsmatrix = 0", null, 1, null);
		src.addCodeLine("public static berechneLundR(double[][] A) {", null, 0,
				null); // 0
		src.addCodeLine("int n = A.length-1;", null, 1, null);
		src.addCodeLine("R= A.clone();", null, 1, null); // 4
		src.addCodeLine("L= 0;", null, 1, null); // 5
		src.addCodeLine("for (int j = 0; j < n; i++) {", null, 1, null); // 6
		src.addCodeLine("	double max = Math.abs(A[j][j]);", null, 1, null); // 6
		src.addCodeLine("	int imax = j;", null, 1, null); // 6
		src.addCodeLine("	for (int i = j+1; i < n; i++)", null, 2, null); // 6
		src.addCodeLine("		if (Math.abs(A[i][j]) > max)", null, 3, null); // 6
		src.addCodeLine("		{", null, 3, null); // 6
		src.addCodeLine("			max  = Math.abs(A[i][j]);", null, 4, null); // 6
		src.addCodeLine("			imax = i;", null, 4, null); // 6
		src.addCodeLine("		}", null, 3, null); // 6
		src.addCodeLine("	if(max!=Math.abs(A[j][j])) {", null, 1, null); // 6
		src.addCodeLine("		double[] h = A[j];", null, 2, null); // 6
		src.addCodeLine("		A[j] = A[imax];", null, 2, null); // 6
		src.addCodeLine("		A[imax] = h; }", null, 2, null); // 6
		src.addCodeLine("	for (int k = j+1; k <= n; k++) {", null, 1, null); // 7
		src.addCodeLine("		L[k][j]= R[k][j] / R[j][j];", null, 2, null); // 8
		src.addCodeLine("			for (int l = i; l <= n; j++) {", null, 2, null); // 9
		src.addCodeLine("				R[k][l]= R[k][l] - L[k][j] * R[j][l];", null, 3,
				null); // 10
		src.addCodeLine("			}", null, 3, null); // 11
		src.addCodeLine("	}", null, 2, null); // 12
		src.addCodeLine("}", null, 1, null); // 13
		src.addCodeLine("for (int s = 0; s < n; s++) {", null, 1, null); // 13
		src.addCodeLine("L[s][s]++;", null, 1, null); // 13
		src.addCodeLine("}", null, 1, null); // 15

		TextProperties headerProps = new TextProperties();
		TextProperties headerProps1 = new TextProperties();

		headerProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Arial", Font.PLAIN, 20));

		Integer Count = 0;
		Text Stats = lang.newText(new Coordinates(40, 515),
				"Ausgeführte Operationen:" + Count, "Statistik", null,
				headerProps1);

	
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Arial",
				Font.PLAIN, 20));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc = lang.newSourceCode(new Coordinates(20, 100),
				"sourceCode", null, scProps);

		sc.addCodeLine(
				"Will man das Lösen eines quadratischen eindeutig lösbaren Gleichungssystems Ax=b",
				"asdasdsdasd", 0, Timing.INSTANTEOUS);
		sc.addCodeLine(
				"als Computerprogramm umsetzen, bietet es sich an, den Gaußalgorithmus als LR-Zerlegung",
				"adsads", 0, Timing.INSTANTEOUS);
		sc.addCodeLine(
				"(auch LU-Zerlegung oder Dreieckszerlegung genannt) zu interpretieren.",
				"adsads", 0, Timing.INSTANTEOUS);
		sc.addCodeLine(
				"Dies ist eine Zerlegung der regulären Matrix A in das Produkt einer linken unteren Dreiecksmatrix L",
				"adsads", 0, Timing.INSTANTEOUS);
		sc.addCodeLine("und einer rechten oberen Dreiecksmatrix R", "adsads",
				0, Timing.INSTANTEOUS);
		mp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));

		lang.nextStep("Einführung");
		sc.hide();

		Text TextA = null;
		Text TextL = null;

		Text TextA1 = null;
		Text TextL1 = null;
		Text bestimmen = null;

		double[][] En = new double[doubleMatrix.length][doubleMatrix.length];
		double[][] C = new double[doubleMatrix.length][doubleMatrix.length];

		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < C.length; j++) {
				C[i][j] = 0;
			}

		}

		double[][] D = new double[doubleMatrix.length][doubleMatrix.length];

		for (int i = 0; i < D.length; i++) {
			D[i][i] = 1;

		}

		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < C.length; j++) {
				C[i][j] = doubleMatrix[i][j];

			}
		}

		for (int i = 0; i < doubleMatrix.length; i++) {
			for (int j = 0; j < doubleMatrix.length; j++) {
				En[i][j] = 0;
			}
		}

		DoubleMatrix EinMa;
		Text Plus;
		Text Gleich;
		Text Gleich2;
		Text Gleich3;

		Text TextErgebnisL;
		Text TextErgebnisR;

		switch (C.length - 1) {

		case 1: {
			EinMa = lang.newDoubleMatrix(new Coordinates(650, 320), D, "EinMa",
					null, mp);

			Plus = lang.newText(new Coordinates(615, 355), "+", "Plus", null,
					headerProps1);
			Gleich = lang.newText(new Coordinates(460, 515), "=", "Gleich",
					null, headerProps1);
			Gleich2 = lang.newText(new Coordinates(620, 515), "=", "Gleich2",
					null, headerProps1);
			Gleich3 = lang.newText(new Coordinates(220, 340), "=", "Gleich3",
					null, headerProps1);
			TextErgebnisL = lang.newText(new Coordinates(650, 515), "L",
					"TextErgebnisL", null, headerProps1);
			TextErgebnisR = lang.newText(new Coordinates(235, 340), "R",
					"TextErgebnisR", null, headerProps1);

		}

			break;

		case 2: {
			EinMa = lang.newDoubleMatrix(new Coordinates(800, 320), D, "EinMa",
					null, mp);

			Plus = lang.newText(new Coordinates(775, 355), "+", "Plus", null,
					headerProps1);
			Gleich = lang.newText(new Coordinates(460, 515), "=", "Gleich",
					null, headerProps1);
			Gleich2 = lang.newText(new Coordinates(765, 515), "=", "Gleich2",
					null, headerProps1);
			Gleich3 = lang.newText(new Coordinates(365, 355), "=", "Gleich3",
					null, headerProps1);
			TextErgebnisL = lang.newText(new Coordinates(795, 515), "L",
					"TextErgebnisL", null, headerProps1);
			TextErgebnisR = lang.newText(new Coordinates(380, 355), "R",
					"TextErgebnisR", null, headerProps1);

		}

			break;

		default: {
			EinMa = lang.newDoubleMatrix(new Coordinates(700, 320), D, "EinMa",
					null, mp);

			Plus = lang.newText(new Coordinates(680, 385), "+", "Plus", null,
					headerProps1);
			Gleich = lang.newText(new Coordinates(460, 540), "=", "Gleich",
					null, headerProps1);
			Gleich2 = lang.newText(new Coordinates(670, 540), "=", "Gleich2",
					null, headerProps1);
			Gleich3 = lang.newText(new Coordinates(275, 385), "=", "Gleich3",
					null, headerProps1);
			TextErgebnisL = lang.newText(new Coordinates(690, 540), "L",
					"TextErgebnisL", null, headerProps1);
			TextErgebnisR = lang.newText(new Coordinates(290, 385), "R",
					"TextErgebnisR", null, headerProps1);

		}
			break;
		}

		DoubleMatrix MatrixL = lang.newDoubleMatrix(new Coordinates(500, 480),
				D, "MatrixL", null, mp);
		TextErgebnisL.hide();
		Gleich2.hide();
		Gleich3.hide();
		TextErgebnisR.hide();
		MatrixL.hide();
		Gleich.hide();
		Plus.hide();
		EinMa.hide();

		DoubleMatrix Eingabe = lang.newDoubleMatrix(new Coordinates(100, 100),
				doubleMatrix, "Eingabematrix", null, mp);

		DoubleMatrix Einheitsmatrix = lang.newDoubleMatrix(new Coordinates(500,
				100), En, "Einheitsmatrix", null, mp);

		DoubleMatrix Eingabe1 = lang.newDoubleMatrix(new Coordinates(100, 320),
				C, "Eingabematrix1", null, mp);

		DoubleMatrix Einheitsmatrix1 = lang.newDoubleMatrix(new Coordinates(
				500, 320), En, "Einheitsmatrix1", null, mp);

		Text status1 = lang.newText(new Coordinates(700, 10), "Status:",
				"header2", null, headerProps1);
		Text status1x = lang.newText(new Coordinates(700, 30), "", "header3",
				null, headerProps1);
		Eingabe1.hide();
		Einheitsmatrix1.hide();

		int n = doubleMatrix.length - 1;

		Text status;

		status = lang.newText(new Coordinates(40, 70),
				"Die Eingabematrix lautet:", "header", null, headerProps1);
		src.highlight(3);

		TextA = lang.newText(new Coordinates(70, 140), "A=", "header", null,
				headerProps);
		TextL = lang.newText(new Coordinates(470, 140), "L=", "header", null,
				headerProps);

		lang.nextStep("Berechnung");

		status1.setText("Status:" + "Die Matrix hat die Dimension " + (n + 1),
				Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		src.highlight(4);
		src.unhighlight(3);
		lang.nextStep();
		Count = Count + 1;
		Stats.setText("Ausgeführte Operationen:" + Count, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);

		TextA.hide();

		TextL.hide();

		for (int i = 0; i < n; i++) {
			if (i == 0) {

				status.setText("Initialisierung:", Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);

				status1.setText(
						"Wir initialisieren A1 = A und L1 = 0 (mit der der Nullmatrix)",
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				TextA = lang.newText(new Coordinates(40, 140), "A" + (i + 1)
						+ "=A= ", "header", null, headerProps);

				TextL = lang.newText(new Coordinates(460, 140), "L" + (i + 1)
						+ "=", "header", null, headerProps);

				src.unhighlight(3);
				src.unhighlight(4);

				src.highlight(5);
				src.highlight(6);
				bestimmen = lang.newText(new Coordinates(40, 270),
						"Wir bestimmen auf A" + (i + 1) + " und L" + (i + 1)
								+ " aufbauend " + "A" + (i + 2) + " und" + " L"
								+ (i + 2) + ": ", "header", null, headerProps1);

				switch (n) {

				case 1: {

					TextA1 = lang.newText(new Coordinates(60, 340), "A2=",
							"header", null, headerProps);
					TextL1 = lang.newText(new Coordinates(460, 340), "L2=",
							"header", null, headerProps);
				}

					break;

				case 2: {

					TextA1 = lang.newText(new Coordinates(60, 355), "A2=",
							"header", null, headerProps);
					TextL1 = lang.newText(new Coordinates(460, 355), "L2=",
							"header", null, headerProps);
				}

					break;

				default: {
					TextA1 = lang.newText(new Coordinates(60, 380), "A2=",
							"header", null, headerProps);
					TextL1 = lang.newText(new Coordinates(460, 380), "L2=",
							"header", null, headerProps);
				}
					break;
				}

				bestimmen.hide();
				TextA1.hide();
				TextL1.hide();

			}

			else {
				status = lang.newText(new Coordinates(50, 70), (i + 1) + "."
						+ " Durchlauf" + " (j=" + (i) + ")", "header", null,
						headerProps1);

				lang.nextStep();
				TextA.changeColor("color", Color.red, new TicksTiming(0),
						new TicksTiming(50));
				TextL.changeColor("color", Color.red, new TicksTiming(0),
						new TicksTiming(50));
				lang.nextStep();

				TextA.setText("A" + (i + 1) + "=", new TicksTiming(50),
						new TicksTiming(50));
				TextL.setText("L" + (i + 1) + "=", new TicksTiming(50),
						new TicksTiming(50));
				lang.nextStep();
				TextA.changeColor("color", Color.black, new TicksTiming(0),
						new TicksTiming(50));
				TextL.changeColor("color", Color.black, new TicksTiming(0),
						new TicksTiming(50));

				for (int j = 0; j <= n; j++) {
					for (int j2 = 0; j2 <= n; j2++) {
						Eingabe.put(j, j2, Eingabe1.getElement(j, j2),
								Timing.INSTANTEOUS, Timing.INSTANTEOUS);

					}
				}

				for (int j = 0; j <= n; j++) {
					for (int j2 = 0; j2 <= n; j2++) {
						Einheitsmatrix.put(j, j2,
								Einheitsmatrix1.getElement(j, j2),
								Timing.INSTANTEOUS, Timing.INSTANTEOUS);

					}
				}

				TextA1.changeColor("color", Color.red, new TicksTiming(0),
						new TicksTiming(50));
				TextL1.changeColor("color", Color.red, new TicksTiming(0),
						new TicksTiming(50));
				lang.nextStep();

				TextA1.setText("A" + (i + 2) + "=", new TicksTiming(50),
						new TicksTiming(50));
				TextL1.setText("L" + (i + 2) + "=", new TicksTiming(50),
						new TicksTiming(50));

				lang.nextStep();

				TextA1.changeColor("color", Color.black, new TicksTiming(0),
						new TicksTiming(50));
				TextL1.changeColor("color", Color.black, new TicksTiming(0),
						new TicksTiming(50));

				bestimmen.setText("Wir bestimmen auf A" + (i + 1) + " und L"
						+ (i + 1) + " aufbauend " + "A" + (i + 2) + " und"
						+ " L" + (i + 2) + ": ", Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);

			}// Ende Else Zweig

			lang.nextStep();
			Count = Count + 2;
			Stats.setText("Ausgeführte Operationen:" + Count,
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			status.setText((i + 1) + ". Durchlauf" + " (j=" + (i) + ")",
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			src.unhighlight(5);
			src.unhighlight(6);
			src.highlight(7);
			Count = Count + 3;
			lang.nextStep();
			Stats.setText("Ausgeführte Operationen:" + Count,
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			src.unhighlight(7);
			src.highlight(8);
			src.highlight(9);
			src.highlight(10);
			src.highlight(11);
			src.highlight(12);
			src.highlight(13);
			src.highlight(14);
			src.highlight(15);

			status1.setText(
					"[Status] Spaltenpivotsuche: Bestimme den Wert in der "
							+ (i + 1) + ".Spalte in den Zeilen",
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			status1x.setText(
					"zwischen "
							+ (i + 1)
							+ " und "
							+ (n + 1)
							+ " der vom Betrag her maximales Element in der Spalte ist.",
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			TextA.show();
			TextL.show();
			Einheitsmatrix1.show();
			Eingabe1.show();

			bestimmen.show();
			TextA1.show();
			TextL1.show();

			lang.nextStep();

			for (int j = 0 + i; j < doubleMatrix.length; j++) {
				lang.addLine("setGridColor \"Eingabematrix[" + j + "][" + (i)
						+ "]\" TextColor ("+((Color)MatrixFarbeHervorhebung.get("color")).getRed()+","+((Color)MatrixFarbeHervorhebung.get("color")).getGreen()+","+((Color)MatrixFarbeHervorhebung.get("color")).getBlue()+")");

			}

			

			Count = Count + 2;

			int Position = 0;
			double max = 0;

			for (int f = i; f < doubleMatrix.length; f++) {
				Count = Count + 3;

				Count = Count + 2;

				if (Eingabe.getElement(f, i) > max) {

					max = Eingabe.getElement(f, i);
					Position = f;

					Count = Count + 2;

				}
			}

			// Zeilen vertauschen

			if (i == Position) {

				lang.nextStep();

				src.unhighlight(8);
				src.unhighlight(9);
				src.unhighlight(10);
				src.unhighlight(11);
				src.unhighlight(12);
				src.unhighlight(13);
				src.unhighlight(14);
				src.unhighlight(15);

				src.highlight(16);

				Stats.setText("Ausgeführte Operationen:" + Count,
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
				Count++;

				status1x.setText("", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				status1.setText(
						"[Status]: Da in der Diagonale bereits das größte Element enthalten ist",
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				status1x.setText("brauchen wir keine Zeilen umzusortieren",
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				// lang.nextStep();
				for (int j = 0 + i; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix[" + j + "]["
							+ (i) + "]\" TextColor (0,0,0)");

				}

			} else

			{ // Begin Else

				lang.nextStep();

				src.unhighlight(8);
				src.unhighlight(9);
				src.unhighlight(10);
				src.unhighlight(11);
				src.unhighlight(12);
				src.unhighlight(13);
				src.unhighlight(14);
				src.unhighlight(15);

				src.highlight(16);

				status1x.setText("", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				status1.setText("Status: In der " + (i + 1) + ".Spalte "
						+ "ist das größte Element in der Zeile "
						+ (Position + 1), Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);

				Stats.setText("Ausgeführte Operationen:" + Count,
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				lang.nextStep();

				src.unhighlight(16);
				src.highlight(17);
				src.highlight(18);
				src.highlight(19);

				status1.setText("Status: Somit müssen wir die "
						+ (Position + 1) + " Zeile mit der " + (i + 1)
						+ ". tauschen", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				// System.out.println(status1.getText());

				lang.nextStep();
				for (int j = 0 + i; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix[" + j + "]["
							+ (i) + "]\" TextColor (0,0,0)");

				}
				lang.nextStep();

				// Jetzt der eigetnliche Austausch
				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix[" + Position
							+ "][" + (j) + "]\" TextColor ("+((Color)MatrixZeilentauschHervorhebung1.get("color")).getRed()+","+((Color)MatrixZeilentauschHervorhebung1.get("color")).getGreen()+","+((Color)MatrixZeilentauschHervorhebung1.get("color")).getBlue()+")");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Einheitsmatrix[" + Position
							+ "][" + (j) + "]\" TextColor ("+((Color)MatrixZeilentauschHervorhebung1.get("color")).getRed()+","+((Color)MatrixZeilentauschHervorhebung1.get("color")).getGreen()+","+((Color)MatrixZeilentauschHervorhebung1.get("color")).getBlue()+")");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix1[" + Position
							+ "][" + (j) + "]\" TextColor ("+((Color)MatrixZeilentauschHervorhebung1.get("color")).getRed()+","+((Color)MatrixZeilentauschHervorhebung1.get("color")).getGreen()+","+((Color)MatrixZeilentauschHervorhebung1.get("color")).getBlue()+")");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Einheitsmatrix1[" + Position
							+ "][" + (j) + "]\" TextColor ("+((Color)MatrixZeilentauschHervorhebung1.get("color")).getRed()+","+((Color)MatrixZeilentauschHervorhebung1.get("color")).getGreen()+","+((Color)MatrixZeilentauschHervorhebung1.get("color")).getBlue()+")");

				}
				lang.nextStep();

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix[" + i + "]["
							+ (j) + "]\" TextColor ("+((Color)MatrixZeilentauschHervorhebung2.get("color")).getRed()+","+((Color)MatrixZeilentauschHervorhebung2.get("color")).getGreen()+","+((Color)MatrixZeilentauschHervorhebung2.get("color")).getBlue()+")");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Einheitsmatrix[" + i + "]["
							+ (j) + "]\" TextColor ("+((Color)MatrixZeilentauschHervorhebung2.get("color")).getRed()+","+((Color)MatrixZeilentauschHervorhebung2.get("color")).getGreen()+","+((Color)MatrixZeilentauschHervorhebung2.get("color")).getBlue()+")");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix1[" + i + "]["
							+ (j) + "]\" TextColor ("+((Color)MatrixZeilentauschHervorhebung2.get("color")).getRed()+","+((Color)MatrixZeilentauschHervorhebung2.get("color")).getGreen()+","+((Color)MatrixZeilentauschHervorhebung2.get("color")).getBlue()+")");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Einheitsmatrix1[" + i + "]["
							+ (j) + "]\" TextColor ("+((Color)MatrixZeilentauschHervorhebung2.get("color")).getRed()+","+((Color)MatrixZeilentauschHervorhebung2.get("color")).getGreen()+","+((Color)MatrixZeilentauschHervorhebung2.get("color")).getBlue()+")");

				}

				lang.nextStep();

				// Jetzt der eigetnliche Austausch

				lang.nextStep();

				for (int j = 0; j < doubleMatrix.length; j++) {
					Count = Count + 3;
					lang.addLine("setGridColor \"Eingabematrix[" + i + "]["
							+ (j) + "]\" TextColor (255,0,0)");

					lang.addLine("setGridColor \"Einheitsmatrix[" + i + "]["
							+ (j) + "]\" TextColor (255,0,0)");

					lang.addLine("setGridColor \"Eingabematrix1[" + i + "]["
							+ (j) + "]\" TextColor (255,0,0)");

					lang.addLine("setGridColor \"Einheitsmatrix1[" + i + "]["
							+ (j) + "]\" TextColor (255,0,0)");

					lang.nextStep();
					lang.addLine("setGridColor \"Eingabematrix[" + Position
							+ "][" + (j) + "]\" TextColor (0,0,255)");

					lang.addLine("setGridColor \"Einheitsmatrix[" + Position
							+ "][" + (j) + "]\" TextColor (0,0,255)");

					lang.addLine("setGridColor \"Eingabematrix1[" + Position
							+ "][" + (j) + "]\" TextColor (0,0,255)");

					lang.addLine("setGridColor \"Einheitsmatrix1[" + Position
							+ "][" + (j) + "]\" TextColor (0,0,255)");

					lang.nextStep();

					Eingabe.swap(i, j, Position, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					Einheitsmatrix.swap(i, j, Position, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					Einheitsmatrix1.swap(i, j, Position, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					Eingabe1.swap(i, j, Position, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					Count = Count + 6;

				}

				lang.nextStep();

				// //////////

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix[" + Position
							+ "][" + (j) + "]\" TextColor (0,0,0)");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Einheitsmatrix[" + Position
							+ "][" + (j) + "]\" TextColor (0,0,0)");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix1[" + Position
							+ "][" + (j) + "]\" TextColor (0,0,0)");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Einheitsmatrix1[" + Position
							+ "][" + (j) + "]\" TextColor (0,0,0)");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix[" + i + "]["
							+ (j) + "]\" TextColor (0,0,0)");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Einheitsmatrix[" + i + "]["
							+ (j) + "]\" TextColor (0,0,0)");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Eingabematrix1[" + i + "]["
							+ (j) + "]\" TextColor (0,0,0)");

				}

				for (int j = 0; j < doubleMatrix.length; j++) {
					lang.addLine("setGridColor \"Einheitsmatrix1[" + i + "]["
							+ (j) + "]\" TextColor (0,0,0)");

				}

				// ///////////

			}// Ende Else

			lang.nextStep();

			status1.setText("Status:", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			status1x.setText("", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			// Zeilen vertauschen Ende

			max = 0;
			Position = 0;

			// lang.nextStep();

			for (int k = i + 1; k <= n; k++) { // Zeilen

				lang.nextStep();

				Stats.setText("Ausgeführte Operationen:" + Count,
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				Count = Count + 3;
				src.unhighlight(16);
				src.unhighlight(17);
				src.unhighlight(18);
				src.unhighlight(19);

				src.highlight(20);
				lang.nextStep();
				Stats.setText("Ausgeführte Operationen:" + Count,
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				src.unhighlight(20);

				src.highlight(21);

				status1.setText("[Status] Wir bestimmen den Multiplikator L["
						+ (k + 1) + "][" + (i + 1) + "] mit L[" + (k + 1)
						+ "][" + (i + 1) + "]= A" + (i + 1) + "[" + (k + 1)
						+ "][" + (i + 1) + "] / A" + (i + 1) + "[" + (i + 1)
						+ "][" + (i + 1) + "] und fügen ", Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				status1x.setText("L[" + (k + 1) + "][" + (i + 1) + "] in "
						+ "L[" + (i + 2) + "] ein", Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				lang.nextStep();
				
				
				Eingabe.highlightElem(i, i, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				Eingabe.highlightElem(k, i, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);

				lang.nextStep();

				lang.addLine("setGridColor \"Einheitsmatrix1[" + k + "][" + i
						+ "]\" TextColor ("+((Color)MatrixFarbeHervorhebung2.get("color")).getRed()+","+((Color)MatrixFarbeHervorhebung2.get("color")).getGreen()+","+((Color)MatrixFarbeHervorhebung2.get("color")).getBlue()+")");

				lang.nextStep();
				
				
				
				
				
				status1.setText("[Status] Wir bestimmen den Multiplikator L["
						+ (k + 1) + "][" + (i + 1) + "] mit L[" + (k + 1)
						+ "][" + (i + 1) + "]="+Eingabe.getElement(k, i) +"/"+ Eingabe.getElement(i, i)+" und fügen ", Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				status1x.setText("L[" + (k + 1) + "][" + (i + 1) + "] in "
						+ "L[" + (i + 2) + "] ein", Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				
				
				
				
				lang.nextStep();

				status1.setText("[Status] Wir bestimmen den Multiplikator L["
						+ (k + 1) + "][" + (i + 1) + "] mit "+Eingabe.getElement(k, i) / Eingabe.getElement(i, i)+"="+Eingabe.getElement(k, i) +"/"+ Eingabe.getElement(i, i)+" und fügen ", Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				status1x.setText("L[" + (k + 1) + "][" + (i + 1) + "] in "
						+ "L[" + (i + 2) + "] ein", Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				
				
				
				
				lang.nextStep();
				
				
				
				
				
				
				
				

				// Warum wird zuerst das ausgeführt

				Count = Count + 2;

				Einheitsmatrix1.put(k, i,
						Eingabe.getElement(k, i) / Eingabe.getElement(i, i),
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				lang.nextStep();
				lang.addLine("setGridColor \"Einheitsmatrix1[" + k + "][" + i
						+ "]\" TextColor (0,0,0)");
				Eingabe.unhighlightElem(i, i, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				Eingabe.unhighlightElem(k, i, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				lang.nextStep();

				Stats.setText("Ausgeführte Operationen:" + Count,
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				src.unhighlight(21);
				src.highlight(22);

				lang.nextStep();

				for (int j = i; j <= n; j++) {// Spalten
					Count = Count + 3;
					src.highlight(22);
					src.unhighlight(23);

					status1.setText(
							"[Status] Wir bestimmen den Multiplikator L["
									+ (k + 1) + "][" + (i + 1) + "] mit L["
									+ (k + 1) + "][" + (i + 1) + "]= A1["
									+ (k + 1) + "][" + (i + 1) + "] / A1["
									+ (i + 1) + "][" + (i + 1) + "]",
							Timing.INSTANTEOUS, Timing.INSTANTEOUS);

					status1.setText("[Status] Wir bestimmen den Eintrag A"
							+ (i + 2) + "[" + (k + 1) + "][" + (j + 1)
							+ "] mit " + "A" + (i + 1) + "[" + (k + 1) + "]["
							+ (j + 1) + "]" + "=" + "A" + (i + 1) + "["
							+ (k + 1) + "][" + (j + 1) + "]" + "L[" + (k + 1)
							+ "][" + (i + 1) + "]*A" + (i + 1) + "[" + (i + 1)
							+ "][" + (j + 1) + "]", Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					status1x.setText("", Timing.INSTANTEOUS, Timing.INSTANTEOUS);

					lang.nextStep();
					Stats.setText("Ausgeführte Operationen:" + Count,
							Timing.INSTANTEOUS, Timing.INSTANTEOUS);

					src.unhighlight(22);
					src.highlight(23);

					lang.nextStep();

					Einheitsmatrix1.highlightElem(k, i, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					Eingabe.highlightElem(i, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);

					Eingabe.highlightElem(i, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					Eingabe.highlightElem(k, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);

					lang.nextStep();
					lang.addLine("setGridColor \"Eingabematrix1[" + k + "]["
							+ j + "]\" TextColor ("+((Color)MatrixFarbeHervorhebung2.get("color")).getRed()+","+((Color)MatrixFarbeHervorhebung2.get("color")).getGreen()+","+((Color)MatrixFarbeHervorhebung2.get("color")).getBlue()+")");

					lang.nextStep();					
					status1.setText("[Status] Wir bestimmen den Eintrag A"
							+ (i + 2) + "[" + (k + 1) + "][" + (j + 1)
							+ "] mit " + "A" + (i + 1) + "[" + (k + 1) + "]["
							+ (j + 1) + "]" + "="+Eingabe.getElement(k, j)+"-"+Einheitsmatrix.getElement(k, i)+"*"+ Eingabe.getElement(i, j), Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					status1x.setText("", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					
					
					
					lang.nextStep();
					
					status1.setText("[Status] Wir bestimmen den Eintrag A"
							+ (i + 2) + "[" + (k + 1) + "][" + (j + 1)
							+ "] mit "+ (Eingabe.getElement(k, j)
									- Einheitsmatrix.getElement(k, i)
									* Eingabe.getElement(i, j)) +"="+Eingabe.getElement(k, j)+"-"+Einheitsmatrix.getElement(k, i)+"*"+ Eingabe.getElement(i, j), Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					status1x.setText("", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					
					
					
					lang.nextStep();
					
					
					
					Count = Count + 3;

					Eingabe1.put(
							k,
							j,
							Eingabe.getElement(k, j)
									- Einheitsmatrix.getElement(k, i)
									* Eingabe.getElement(i, j),
							Timing.INSTANTEOUS, Timing.INSTANTEOUS);

					lang.nextStep();
					Stats.setText("Ausgeführte Operationen:" + Count,
							Timing.INSTANTEOUS, Timing.INSTANTEOUS);

					Eingabe.unhighlightElem(k, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					Einheitsmatrix1.unhighlightElem(k, i, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					Eingabe.unhighlightElem(i, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					Eingabe.unhighlightElem(k, j, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					lang.addLine("setGridColor \"Eingabematrix1[" + k + "]["
							+ j + "]\" TextColor (0,0,0)");

				}
				src.unhighlight(23);

			}

			if (i != n - 1)
				status.hide();

		}

		Count++;

		src.highlight(22);

		lang.nextStep();

		Stats.setText("Ausgeführte Operationen:" + Count, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);

		src.unhighlight(22);
		src.highlight(20);

		Count++;

		lang.nextStep();
		Stats.setText("Ausgeführte Operationen:" + Count, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);

		src.unhighlight(20);

		src.highlight(27);
		src.highlight(28);
		src.highlight(29);

		status1.setText("[Status] Wir addieren auf " + "L" + (n + 1) + ""
				+ " die Einheitsmatrix", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lang.nextStep();

		EinMa.show();
		Plus.show();
		Gleich.show();
		lang.nextStep();
		// Bestimme Matrix L
		Count = Count + 2;

		for (int l = 0; l < D.length; l++) {
			for (int l2 = 0; l2 < D.length; l2++) {
				Count = Count + 1;

				MatrixL.put(l, l2, Einheitsmatrix1.getElement(l, l2),
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			}
			Count = Count + 1;

		}
		Count = Count + 1;

		for (int l = 0; l < D.length; l++) {
			Count = Count + 2;
			MatrixL.put(l, l, 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		}
		MatrixL.show();
		lang.nextStep("Ergebnis");

		src.unhighlight(27);
		src.unhighlight(28);
		src.unhighlight(29);
		Stats.setText("Ausgeführte Operationen:" + Count, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);

		status1.setText("[Status] Somit bekommen wir unsere Matrix L",
				Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		TextErgebnisL.show();
		Gleich2.show();

		lang.nextStep();
		status1x.setText("und die Matrix R", Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);

		TextErgebnisR.show();
		Gleich3.show();
		lang.nextStep("Fazit");
		
		src.hide();
		
		
		SourceCodeProperties fazitprop;



		SourceCode fazit;
		fazit = lang.newSourceCode(new Coordinates(1020, 50), "sourceCode", null,
				Fazit);

		fazit.addCodeLine("Fazit:", null, 1, null);
		fazit.addCodeLine("", null, 1, null);
		fazit.addCodeLine("Mit L und R haben wir zwei Matrizen mit unterem bzw. oberem Dreiecksmatrix.", null, 1, null);
		fazit.addCodeLine("Die Multiplikation der Matrizen entspricht wieder der Ausgangsmatrix.", null, 1, null);
		fazit.addCodeLine("Mit Hilfe dieser zwei Matrizen haben das Lösen eines komplexen Gleichungssystems", null, 1, null);
		fazit.addCodeLine("auf Lösen zweier Gleichungsysteme mit unterem Dreiecksmatrix reduziert.", null, 1, null);
		fazit.addCodeLine("Dadurch lässt sich das Verfahren einfacher implementieren, da algorithmisch vorgegangen", null, 1, null);
		fazit.addCodeLine("werden.", null, 1, null);
		fazit.addCodeLine("", null, 1, null);
		fazit.addCodeLine("Komplexität des Algorithmus:", null, 1, null);
		fazit.addCodeLine("", null, 1, null);
		fazit.addCodeLine("Die Anzahl arithmetischer Operationen für die LR-Zerlegung ist bei einer", null, 1, null);
		fazit.addCodeLine("n*n-Matrix ca 2/3n^3.  Der Aufwand für das Vorwärts- und Rückwärtseinsetzen ", null, 1, null);
		fazit.addCodeLine("ist quadratisch (O(n^2)) und daher insgesamt vernachlässigbar.", null, 1, null);
		fazit.addCodeLine("", null, 1, null);
		fazit.addCodeLine("Ähnliche Verfahren:", null, 1, null);
		fazit.addCodeLine("", null, 1, null);
		fazit.addCodeLine("-LR-Zerlegung mit vollständiger Pivotsuche", null, 1, null);
		fazit.addCodeLine("-Cholesky Zerlegung", null, 1, null);

		return lang.toString();
	}

	public String getName() {
		return "LR-Zerlegung (Spaltenpivotsuche) [DE]";
	}

	public String getAlgorithmName() {
		return "LR-Zerlegung (Spaltenpivotsuche) [DE]";
	}

	public String getAnimationAuthor() {
		return "Volkan Hacimüftüoglu";
	}

	public String getDescription() {
		return "Wie der Name der Seite schon sagt, wollen wir euch mit dieser Seite die Gauß-Elimination und die LR-Zerlegung erklären. Außerdem habt ihr die Möglichkeit, auf dieser Seite alle Grundlagen zu erlernen, die man dafür beherrschen muss. Damit müsste die Seite für jeden, der schon einmal was von einem linearen Gleichungssystem gehört hat, (hoffentlich) verständlich sein J  "
				+ "\n"
				+ "Die Seite ist auf den Internet-Explorer abgestimmt, d.h. wenn ihr sie mit einem anderen Browser öffnet, könnte es an einigen Stellen zu Problemen kommen."
				+ "\n"
				+ "Entstanden ist die Seite als Projekt eines Seminars zur didaktisch animativen Aufbereitung einzelner Kapitel aus der Numerik an der Johannes Gutenberg Universtität Mainz."
				+ "\n"
				+ "Also, wenn ihr bis jetzt noch nicht wisst, wie man von einem linearen Gleichungssystem auf eine Matrix kommt, startet am besten hier, denn von da aus werdet ihr Schritt für Schritt durch die Seite geführt.";
	}

	public String getCodeExample() {
		return "double[][] R;\", null, 1, null); " + "\n"
				+ "double[][] L;\", null, 1, null); // 4" + "\n"
				+ "double[][] Einheitsmatrix = 0\", null, 1, null);" + "\n"
				+ "public static berechneLundR(double[][] A) {" + "\n"
				+ "int n = A.length-1;" + "\n" + "R= A.clone();" + "\n"
				+ "L= 0;" + "\n" + "for (int j = 0; j < n; i++) {" + "\n"
				+ "	double max = Math.abs(A[j][j]);" + "\n" + "	int imax = j;"
				+ "\n" + "	for (int i = j+1; i < n; i++)" + "\n"
				+ "		if (Math.abs(A[i][j]) > max)" + "\n" + "		{" + "\n"
				+ "			max  = Math.abs(A[i][j]);" + "\n" + "			imax = i;" + "\n"
				+ "		}" + "\n" + "	if(max!=Math.abs(A[j][j])) {" + "\n"
				+ "		double[] h = A[j];" + "\n" + "		A[j] = A[imax];" + "\n"
				+ "		A[imax] = h; }" + "\n"
				+ "	for (int k = j+1; k <= n; k++) {" + "\n"
				+ "		L[k][j]= R[k][j] / R[j][j];" + "\n"
				+ "			for (int l = i; l <= n; j++) {" + "\n"
				+ "			R[k][l]= R[k][l] - L[k][j] * R[j][l];" + "\n" + "			}"
				+ "\n" + "	}" + "\n" + "}" + "\n"
				+ "for (int s = 0; s < n; s++) {" + "\n" + "L[s][s]++;" + "\n"
				+ "}" + "\n";
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

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		Determinante berechneDeterminante = new Determinante();
		boolean result = true;

		String errormessage="";
		
		String[][] Eingabematrix = (String[][]) arg1.get("Eingabematrix");
		
		int breite = Eingabematrix[0].length;
		int hoehe = Eingabematrix.length;
		double[][] doubleMatrix = new double[hoehe][breite];

		System.out.println(breite+"  "+ hoehe);
		
		
		if(breite!=hoehe) {
			result=false;
			errormessage= errormessage+ "Die Matrix muss quadratisch sein!"+"\n";
		}
		
		for (int i = 0; i < hoehe; i++) {
			for (int j = 0; j < breite; j++) {
				
				
			     try {

						doubleMatrix[i][j] = Double.parseDouble(Eingabematrix[i][j]);

			         
			      } 
			     	catch (NumberFormatException e) {
					 result = false;
				     errormessage = errormessage + "Im Eintrag X="+i+" "+"Y="+j+" befindet sich ein ungültiges Zeichen!"+"\n";
			      }
				
			}

		}
		System.out.println(berechneDeterminante.getDecDet(doubleMatrix));
		System.out.println(((int)berechneDeterminante.getDecDet(doubleMatrix)));

		if((berechneDeterminante.getDecDet(doubleMatrix))==0){
			result = false;

			errormessage = errormessage + "Die Matrix ist nicht invertierbar"+"\n";
			errormessage = errormessage + "Bitte wählen Sie eine invertierbare Matrix aus"+"\n";

		}

		if(result == false) showErrorWindow(errormessage);

		
		
		return result;
	}
	
	
	private void showErrorWindow(String message) {
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, "Fehler", JOptionPane.ERROR_MESSAGE);
	}

}