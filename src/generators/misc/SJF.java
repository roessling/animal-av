package generators.misc;

/*
 * genSJF.java
 * Frederik Röper, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
// package generators.misc;
import java.awt.Color;
import java.awt.Font;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.*;
import animal.main.Animal;

public class SJF implements Generator {
    private Language lang;

    public static void main(String[] args) {
        Generator generator = new SJF(); // Generator erzeugen
        Animal.startGeneratorWindow(generator); // Animal mit Generator starten
    }

    public void init() {
        lang = new AnimalScript("Shortest Job First Scheduling", "Frederik Röper", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        int[][] processes = (int[][]) primitives.get("processes");

        paint_description();
        int[][] matrix = calculate_matrix(processes);
        paint_timeline(matrix);
        paint_matrix_structure(matrix);
        lang.nextStep();
        paint_matrix(matrix);
        paint_conclusion(matrix);
        return lang.toString();
    }

    public String getName() {
        return "Shortest Job First Scheduling";
    }

    public String getAlgorithmName() {
        return "Shortest Job First Scheduling";
    }

    public String getAnimationAuthor() {
        return "Frederik Röper";
    }

    public String getDescription() {
        return "Shortest-Job-Next (SJN) oder Shortest-Job-First (SJF) ist ein nonpräemptives Scheduling-Verfahren,"
                + "das eingesetzt wird, um rechenwillige Threads oder/und Prozesse auf die physischen Prozessoren des"
                + "Rechners zu verteilen.";
    }

    public String getCodeExample() {
        return "";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    // My Code

    /**
     * default duration for swap processes
     */
    public final static Timing defaultDuration = new TicksTiming(30);
    public int graph_x = 20;
    public int graph_y = 180;
    public int step_length = 28;
    public int step_height = 15;
    public int height_offset = 26;
    public int table_column_length = 90;

    public int conclusion_container_x = 20;
    public int conclusion_container_y = 10;
    public int conclusion_x = 30;
    public int conclusion_y = 110;

    public static String repeat(String s, int times) {
        if (times <= 0)
            return "";
        else
            return s + repeat(s, times - 1);
    }

    public void paint_description() {
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
        Text textHeader = lang.newText(new Coordinates(20, 20), "Shortest Job First", "Text", null, headerProps);

        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        String[] description = new String[] {
                "Shortest-Job-Next (SJN) oder Shortest-Job-First (SJF) ist ein nonpräemptives Scheduling-Verfahren,",
                "das eingesetzt wird, um rechenwillige Threads oder/und Prozesse auf die physischen Prozessoren des",
                "Rechners zu verteilen." };
        int row_height = 17;
        for (int i = 0; i < description.length; i++) {
            Text textDescripton = lang.newText(new Coordinates(20, 50 + (row_height * i)), description[i], "Text", null,
                    textProps);
        }

        lang.nextStep();

        lang.newRect(new Coordinates(20, 110), new Coordinates(20 + this.step_length, 110 + this.step_height), "Rect",
                null);
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        lang.newRect(new Coordinates(20, 130), new Coordinates(20 + this.step_length, 130 + this.step_height), "Rect",
                null, rectProps);
        Text whiteBoxDescripton = lang.newText(new Coordinates(55, 110),
                "= Der Prozess ist angekommen und wartet auf die Ausführung", "Text", null);
        Text grayBoxDescripton = lang.newText(new Coordinates(55, 130), "= Der Prozess wird ausgeführt", "Text", null);

        Text matrixDescription = lang.newText(new Coordinates(this.graph_x, this.graph_y), "Ankunft Dauer", "Text",
                null);

    }

    public int get_max_completion(int[][] matrix) {
        int max_completion = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][2] > max_completion) {
                max_completion = matrix[i][2];
            }
        }
        return max_completion;
    }

    public void paint_timeline(int[][] matrix) {

        int max_completion = this.get_max_completion(matrix);

        String timeline = "";
        for (int i = 0; i < max_completion + 1; i++) {
            lang.newText(new Coordinates(this.graph_x + 100 + this.step_length * i, this.graph_y), String.valueOf(i),
                    "Text", null);
        }

    }

    public void paint_matrix_structure(int[][] matrix) {
        MatrixProperties matrixProps = new MatrixProperties();
        // matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        // matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

        int[][] matrix_short = new int[matrix.length][2];
        for (int i = 0; i < matrix_short.length; i++) {
            matrix_short[i][0] = matrix[i][0];
            matrix_short[i][1] = matrix[i][1];
        }

        IntMatrix matrix_structure = lang.newIntMatrix(new Coordinates(this.graph_x, this.graph_y + 20), matrix_short,
                "intMatrix", null, matrixProps);

    }

    public void paint_matrix(int[][] matrix) {
        int max_completion = this.get_max_completion(matrix);

        for (int i = 1; i < max_completion + 1; i++) {
            paint_matrix_stepwise(matrix, i);
            lang.nextStep();
        }
    }

    public void paint_matrix_stepwise(int[][] matrix, int step) {
        int left = this.graph_x + 100;
        int top = this.graph_y + 25;

        RectProperties whiteRectProps = new RectProperties();
        whiteRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        whiteRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

        RectProperties greyRectProps = new RectProperties();
        greyRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
        greyRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0] != (matrix[i][0] + matrix[i][4]) && matrix[i][0] < step) {
                lang.newRect(new Coordinates(left + this.step_length * matrix[i][0], top + this.height_offset * i),
                        new Coordinates(Math.min(left + this.step_length * (matrix[i][0] + matrix[i][4]),
                                left + this.step_length * step), top + this.height_offset * i + step_height),
                        "Rect", null, whiteRectProps);
            }
            if ((matrix[i][0] + matrix[i][4]) < step) {
                lang.newRect(
                        new Coordinates(left + this.step_length * (matrix[i][0] + matrix[i][4]),
                                top + this.height_offset * i),
                        new Coordinates(
                                Math.min(left + this.step_length * (matrix[i][2]), left + this.step_length * step),
                                top + this.height_offset * i + this.step_height),
                        "Rect", null, greyRectProps);
            }
        }
    }

    public int[][] calculate_matrix(int[][] processes) {
        int[][] matrix = new int[processes.length][6];

        for (int i = 0; i < processes.length; i++) {
            matrix[i][0] = processes[i][0];
            matrix[i][1] = processes[i][1];
            matrix[i][2] = 0;
            matrix[i][3] = 0;
            matrix[i][4] = 0;
            matrix[i][5] = 0;
        }

        int n = matrix.length;
        int st = 0, tot = 0;

        boolean a = true;
        while (true) {
            int c = n, min = 999;
            if (tot == n) // total no of process = completed process loop will be terminated
                break;

            for (int i = 0; i < n; i++) {
                /*
                 * If i'th process arrival time <= system time and its flag=0 and burst<min That
                 * process will be executed first
                 */
                if ((matrix[i][0] <= st) && (matrix[i][5] == 0) && (matrix[i][1] < min)) {
                    min = matrix[i][1];
                    c = i;
                }
            }

            /*
             * If c==n means c value can not updated because no process arrival time< system
             * time so we increase the system time
             */
            if (c == n)
                st++;
            else {
                matrix[c][2] = st + matrix[c][1];
                st += matrix[c][1];
                matrix[c][3] = matrix[c][2] - matrix[c][0];
                matrix[c][4] = matrix[c][3] - matrix[c][1];
                matrix[c][5] = 1;
                tot++;
            }
        }

        return matrix;
    }

    public void paint_conclusion(int[][] matrix) {
        System.out.println("\narrival brust  complete turn waiting");
        int n = matrix.length;
        float avgwt = 0, avgta = 0;
        RectProperties whiteRectProps = new RectProperties();
        whiteRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        whiteRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

        lang.newRect(new Coordinates(this.conclusion_container_x, 100),
                new Coordinates(this.conclusion_container_x + 1000, this.conclusion_container_y + 1000), "Rect", null,
                whiteRectProps);
        lang.newText(new Coordinates(this.conclusion_x, this.conclusion_y), "Ankunft", "Text", null);
        lang.newText(new Coordinates(this.conclusion_x + this.table_column_length, this.conclusion_y), "Dauer", "Text",
                null);
        lang.newText(new Coordinates(this.conclusion_x + this.table_column_length * 2, this.conclusion_y), "Fertig",
                "Text", null);
        lang.newText(new Coordinates(this.conclusion_x + this.table_column_length * 3, this.conclusion_y), "Turnaround",
                "Text", null);
        lang.newText(new Coordinates(this.conclusion_x + this.table_column_length * 4, this.conclusion_y), "Warten",
                "Text", null);
        
        for (int i = 0; i < n; i++) {
            avgwt += matrix[i][4];
            avgta += matrix[i][3];
            // pid[i] + "\t" +
            System.out.println(matrix[i][0] + "\t" + matrix[i][1] + "\t" + matrix[i][2] + "\t" + matrix[i][3] + "\t"
                    + matrix[i][4]);
            for (int j = 0; j < 5; j++) {
                lang.newText(new Coordinates(this.conclusion_x + this.table_column_length * j,
                        this.conclusion_y + this.height_offset * (i+1)), Integer.toString(matrix[i][j]), "Text", null);
            }
        }

        lang.newText(new Coordinates(this.conclusion_x + this.table_column_length * 5, this.conclusion_y),
                "Avrg Turnaround", "Text", null);
        lang.newText(new Coordinates(this.conclusion_x + this.table_column_length * 5, this.conclusion_y + this.height_offset),
                "Avrg Waiting", "Text", null);

        lang.newText(new Coordinates(this.conclusion_x + 30 + this.table_column_length * 6, this.conclusion_y),
                Float.toString(avgta / n), "Text", null);
        lang.newText(new Coordinates(this.conclusion_x + 30 + this.table_column_length * 6, this.conclusion_y + this.height_offset),
                Float.toString(avgwt / n), "Text", null);

        System.out.println("\naverage tat is " + (float) (avgta / n));
        System.out.println("average wt is " + (float) (avgwt / n));

    }

}