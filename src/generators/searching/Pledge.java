package generators.searching;/*
 * Pledge.java
 * Oliver Käfer, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class Pledge implements Generator {
    private Language lang;
    private MatrixProperties NumberMatrix;
    private MatrixProperties LabyrinthVisProps;
    private int[][] labyrinth;
    private int actX = -1,actY = -1;
    private int degree;
    private List<Consumer<Integer>> directions;
    private int oldX,oldY;
    private Variables v;

    public void init(){
        lang = new AnimalScript("Der Pledge-Algorithmus", "Oliver Käfer", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        NumberMatrix = (MatrixProperties)props.getPropertiesByName("NumberMatrix");
        labyrinth = (int[][])primitives.get("labyrinth");
        LabyrinthVisProps = (MatrixProperties)props.getPropertiesByName("LabyrinthVisProps");
        v = lang.newVariables();
        actY = -1;
        actX = -1;
        int i,j;
        for (i = 0; i < labyrinth.length;i++){
            for (j = 0;j < labyrinth[0].length;j++){
                if (labyrinth[i][j] == 3){
                    this.actX = i;
                    this.actY = j;
                }
            }
        }

        TextProperties txtProps = new TextProperties();
        txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,24));
        Text header = lang.newText(new Coordinates(10,20),"Pledge-Algorithmus","header",null,txtProps);
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        Rect headRect = lang.newRect(new Offset(-5, -5, "header",
                        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, rectProps);

        oldX = actX;
        oldY = actY;
        if (actX == -1 && actY == -1){
            txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,24));
            lang.addItem(lang.newText(new Coordinates(300,300),"No start point found","error",null,txtProps));
            return lang.toString();
        }
        IntMatrix matrix = lang.newIntMatrix(new Coordinates(50,150),labyrinth,"labyrinth",null,NumberMatrix);
        IntMatrix visLaby = lang.newIntMatrix(new Offset(50,0,"labyrinth","NE"),labyrinth,"vislaby",null,LabyrinthVisProps);
        initLabyrinth(visLaby);
        lang.addItem(lang.newText(new Offset(0,-50,"labyrinth","NW"),"Was der Algo sieht.","infoAlgo",null,txtProps));
        lang.addItem(lang.newText(new Offset(0,-50,"vislaby","NW"),"Das Labyrinth.","infoLaby",null,txtProps));
        this.degree = 0;
        directions = new ArrayList<>();
        directions.add((x) ->{if(this.actX > 0){this.actX--;}});                            // Up
        directions.add((x) ->{if(this.actY < this.labyrinth[0].length-1){this.actY++;}});   // Right
        directions.add((x) ->{if(this.actX < labyrinth.length-1){this.actX++;}});           // Down
        directions.add((x) ->{if(this.actY > 0){this.actY--;}});                            // Left
        directions.add((x) ->{if(this.actX > 0){this.actX--;}});                            // Up
        lang.nextStep();
        pledge(matrix,visLaby);

        return lang.toString();
    }

    private void initLabyrinth(IntMatrix visLaby) {
        for (int i = 0; i < labyrinth.length; i++) {
            for (int j = 0; j < labyrinth[0].length; j++) {
                switch (labyrinth[i][j]){
                    case 1: visLaby.highlightCell(i,j,null,null);break;
                    case 3: visLaby.highlightElem(i,j,null,null);break;
                }
            }
        }
    }

    private void pledge(IntMatrix matrix, IntMatrix visLaby) {
        Consumer<Integer> direction = directions.get(0);
        v.declare("int","degree","0");
        degree = 0;
        do {
            do {
                direction.accept(0);
                highlight(matrix,visLaby);
                lang.nextStep();
            } while (labyrinth[actX][actY] != 1 && labyrinth[actX][actY] != 2);
            directions.get(degreeInv()).accept(0);
            highlight(matrix,visLaby);
            lang.nextStep();
            direction = directions.get((++degree)%4);
            v.set("degree",String.valueOf(degree*90));
            do {
                if (nearWall()){
                    direction.accept(0);
                    highlight(matrix,visLaby);
                    lang.nextStep();
                } else if (labyrinth[actX][actY] == 1){
                    directions.get(degreeInv()).accept(0);
                    highlight(matrix,visLaby);
                    lang.nextStep();
                    direction = directions.get((++degree)%4);
                    v.set("degree",String.valueOf(degree*90));
                } else if (labyrinth[actX][actY] == 2){
                    break;
                }
                else {
                    direction = directions.get((--degree)%4);
                    v.set("degree",String.valueOf(degree*90));
                    direction.accept(0);
                    highlight(matrix,visLaby);
                    lang.nextStep();
                }
            }while (degree > 0 && degree < 6);
        }while (labyrinth[actX][actY] != 2 && degree != 6);
        String out;
        if (degree == 6){
            out = "Kein Ausgang";
        } else {
            out = "Ausgang gefunden bei (" + actX + " , " + actY + ")";
        }
        TextProperties txtProps = new TextProperties();
        txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.PLAIN,24));
        lang.addItem(lang.newText(new Offset(-50,50,"vislaby","S"),out,"info",null,txtProps));
        lang.nextStep();
    }

    private void highlight(IntMatrix matrix, IntMatrix visLaby) {
        matrix.unhighlightCell(oldX,oldY,null,null);
        matrix.highlightCell(actX,actY,null,null);
        if (labyrinth[oldX][oldY] != 1 && labyrinth[actX][actY] != 1) {
            visLaby.unhighlightElem(oldX, oldY, null, null);
        }
        if (labyrinth[actX][actY] != 1) {
            visLaby.highlightElem(actX,actY,null,null);
        }
        oldY = actY;
        oldX = actX;
    }

    private boolean nearWall(){
        if (actX == 0 || actY == 0){
            return false;
        }
        if (actX == labyrinth.length-1 || actY == labyrinth[0].length-1){
            return false;
        }
        switch (degree % 4){
            case 0: return labyrinth[actX][actY-1] == 1;
            case 1: return labyrinth[actX-1][actY] == 1;
            case 2: return labyrinth[actX][actY+1] == 1;
            case 3: return labyrinth[actX+1][actY] == 1;
        }
        return false;
    }

    private int degreeInv(){
        switch (degree % 4){
            case 0: return 2;
            case 1: return 3;
            case 2: return 0;
            case 3: return 1;
            default: return 0;
        }
    }
    
    public String getName() {
        return "Der Pledge-Algorithmus";
    }

    public String getAlgorithmName() {
        return "Pledge-Algorithmus";
    }

    public String getAnimationAuthor() {
        return "Oliver Käfer";
    }

    public String getDescription(){
        return "Der Pledge-Algorithmus ist ein Algorithmus mit dem aus einem Planaren Labyrinth entkommen kann."
 +"\n"
 +"Dabei ist keine Information über das Labyrinth nötig, ausser zu wissen Links von einem eine Wand ist."
 +"\n"
 +"\n"
 +"Die Strategie ist die verfolgt wird ist einfach. "
 +"\n"
 +"Man läuft in dem  Labyrinth so, dass immer Links von einem eine Wand ist. Kommt man eine Wand vor einem dreht man sich nach Rechts und addiert die 90 Grad drehung auf die gesamt drehungen darauf."
 +"\n"
 +"Wenn Links von einem keine Wand mehr ist und man mehr als 0 Grad auf dem drehungszähler hat, dreht man sich nach Links und zieht 90 Grad ab."
 +"\n"
 +"Hat der Zähler 540 Grad erreicht und man hat keinen Ausgang gefunden gibt es auch keinen, wenn das Labyrinth Planar ist. "
 +"\n"
 +"\n"
 +"Hinweis zu dem eigenen Input:"
 +"\n"
 +"Die bedeutung der Zahlen in der Matrix:"
 +"\n"
 +"0 = Weg"
 +"\n"
 +"1 = Wand"
 +"\n"
 +"2 = Ausgang"
 +"\n"
 +"3 = Start";
    }

    public String getCodeExample(){
        return "Setze Umdrehungszähler auf 0;"
 +"\n"
 +"repeat"
 +"\n"
 +"      repeat"
 +"\n"
 +"            Gehe geradeaus;"
 +"\n"
 +"      until Wand erreicht;"
 +"\n"
 +"\n"
 +"      Drehe nach rechts;"
 +"\n"
 +"\n"
 +"      repeat"
 +"\n"
 +"            Folge dem Hindernis;"
 +"\n"
 +"      until Umdrehungszäler = 0;"
 +"\n"
 +"\n"
 +"until ins Helle gelangt;";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}