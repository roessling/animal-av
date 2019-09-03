/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.graph.forcedirectedgraphdrawing.algo;

import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import generators.graph.helpers.Point2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    final static int FONTSIZE = 18;
    Language lang;
    Text infotext, title, infoBox;
    StringMatrix forces, upperInfoBox, lowerValueBox;
    DoubleMatrix partialForces;
    AnimalGraph2D graph;
    Rect frame, algoBG, funcBG;
    int frameWidth, frameHeight;
    Point2D frameCorner, upperleftCorner;
    double currentMaxVal;
    int currentMaximumIndex=-1;
    SourceCode algoCode, funcCode;
    private double[][] partialForcesValues;
    boolean visibleLowerValueBox=true;

    public GUI(Language lang, AnimalGraph2D graph){
        this.lang = lang;
        infotext = lang.newText(new Coordinates(5,5), "init", "txt",null);
        this.graph = graph;
        MatrixProperties matrProbs = new MatrixProperties();
        matrProbs.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
//        matrProbs.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        initForceMatrix(matrProbs);
//        partialForcesValues = new double[graph.getSize()][graph.getSize()];
//        partialForces = lang.newDoubleMatrix(new Coordinates(700, 10), partialForcesValues, "asd", null, matrProbs);
//        partialForces.hide();
    }

    public GUI(Language lang){

        this.lang = lang;
        infotext = lang.newText(new Coordinates(5,45), "", "txt",null);
//        infoBox = lang.newText(new Coordinates(5,45), "", "txt",null, new TextProperties());
        upperleftCorner = new Point2D(5,120);
    }

    public void initTitle(TextProperties titleProps){
         title = lang.newText(new Coordinates(8,2), "FDGD: Spring-Embedder", "title", null, titleProps);
    }

    public void setInfotext(String txt){
//        infotext.setText(txt, null, null);
    }

    public void setInfoBox(int phase){

    }

    public void setupFrame(int width, int height) {
        RectProperties rectProbs = new RectProperties();
        frameWidth = width;
        frameHeight = height;
        Coordinates tmp = (Coordinates) algoBG.getLowerRight();
        this.frameCorner = new Point2D(tmp.getX()+10,upperleftCorner.getY());
        rectProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        rectProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 15);
        frame = lang.newRect(Util.pointToNode(frameCorner), new Coordinates((int) frameCorner.getX()+width, (int) frameCorner.getY()+height), "area", null, rectProbs);
    }

    public void initStructure(GraphProperties properties, String[] labels, Coordinates[] startingCoords, int[][] adjMatrix){
        Coordinates[] coords = transformStructureCoordinates(startingCoords);
        this.graph = new AnimalGraph2D(Util.nodesToPoints(coords), adjMatrix, labels, this.lang, properties);
    }

    public void initAlgoCode(SourceCodeProperties sourceCodeProperties, RectProperties rectProperties){
        int width = 250;

        Coordinates algoCodeBase = new Coordinates((int) upperleftCorner.getX(), (int) upperleftCorner.getY());
        algoCode = lang.newSourceCode(algoCodeBase, "mainAlgo", null, sourceCodeProperties);
        algoCode.addCodeLine("t <- 1", "codeLine", 0, null);
        algoCode.addCodeLine("while( t < K && max ||F_n(t)|| > threshold ) do", "codeLine", 0, null);
        algoCode.addCodeLine("foreach Node n do", "codeLine", 1, null);
        algoCode.addCodeLine("F_n(t) <- ", "codeLine", 2, null);
        algoCode.addCodeLine("//for all neighbours", "codeLine", 6, null);
        algoCode.addCodeLine("sum f_attr(p_u, p_v) +", "codeLine", 6, null);
        algoCode.addCodeLine("//for all non-neighbours", "codeLine", 6, null);
        algoCode.addCodeLine("sum f_rep(p_u, p_v)", "codeLine", 6, null);
        algoCode.addCodeLine("foreach Node n do", "codeLine", 1, null);
        algoCode.addCodeLine("pos_n <- pos_n + delta(t)*F_n(t)", "codeLine", 2, null);
        algoCode.addCodeLine("t++", "codeline", 1, null);

        algoBG = lang.newRect(new Coordinates(3, (int) upperleftCorner.getY()), new Coordinates(algoCodeBase.getX()+width, algoCodeBase.getY()+algoCode.length()*FONTSIZE+5), "bg", null, rectProperties);
        Coordinates tmp = (Coordinates) algoBG.getLowerRight();

        funcCode = lang.newSourceCode(new Coordinates((int) upperleftCorner.getX(), tmp.getY()+5), "mainAlgo", null, sourceCodeProperties);
        funcCode.addCodeLine("f_attr(p_u, p_v) =", "codeLine", 0, null);
        funcCode.addCodeLine(TextDatabase.CONSTANT_ATTR+" * log( dist / "+TextDatabase.LENGTH+" ) * uv(p_u,p_v)", "codeLine", 1, null);
        funcCode.addCodeLine("", "codeLine", 0, null);
        funcCode.addCodeLine("f_rep(p_u, p_v) =", "codeLine", 0, null);
        funcCode.addCodeLine(TextDatabase.CONSTANT_REP+" / dist²  * uv(p_u,p_v)", "codeLine", 1, null);
        funcCode.addCodeLine("", "codeLine", 0, null);
        funcCode.addCodeLine("//dist = ||p_u - p_v||  and", "codeLine", 0, null);
        funcCode.addCodeLine("//uv being the unitvector of two points", "codeLine", 0, null);

        algoCodeBase = new Coordinates(3, ((Coordinates) funcCode.getUpperLeft()).getY());

        funcBG = lang.newRect(algoCodeBase, new Coordinates(algoCodeBase.getX()+width, algoCodeBase.getY()+ funcCode.length()*FONTSIZE+5), "bg", null, rectProperties);
    }

    public void initInfoBox(int maxIter, double forceThreshold, double cRep, double cAttr, double length, MatrixProperties matrixProperties){
        Coordinates tmp = (Coordinates) frame.getUpperLeft();
        tmp = new Coordinates(tmp.getX(), (int) upperleftCorner.getY()-60);

        String[][] tmpBox = {{"K: "+maxIter, TextDatabase.CONSTANT_REP+": "+cRep,TextDatabase.CONSTANT_ATTR+": "+cAttr,TextDatabase.LENGTH+": "+length, "threshold: "+forceThreshold}, {"t: ","delta(t):","","", "max ||F_n(t)||:"}};

        upperInfoBox = lang.newStringMatrix(tmp, tmpBox, "upperInfoBox", null, matrixProperties);

    }

    public void initForceMatrix(MatrixProperties matrProbs){
        String[][] stringTempMatr = new String[graph.getSize()+1][4];
        stringTempMatr[0][0]="qn";
        stringTempMatr[0][1]="||F_n(t)||";
        stringTempMatr[0][2]="";
        stringTempMatr[0][3]="p_n";
        for (int i = 1; i <= graph.getSize(); i++) {
            stringTempMatr[i][0]=graph.getLabel(i-1);
            stringTempMatr[i][1]="";
            stringTempMatr[i][2]="";
            stringTempMatr[i][3]="";
        }
        forces = lang.newStringMatrix(new Coordinates(((Coordinates)frame.getLowerRight()).getX()+10, ((Coordinates)frame.getUpperLeft()).getY()), stringTempMatr, "forcesMatrix", null, matrProbs);
//        forces.hide();
    }

    public void updateForces(List<Point2D> forces){
//        this.forces.show();
        double maxForce = Util.getMaxForce(forces);
        for (int i = 0; i < forces.size(); i++) {
            Point2D point2D = forces.get(i);
            double force = updateForce(i, point2D);


            paintTemperature(this.forces, maxForce, i+1, 1, force);
            if(point2D.magnitude()==maxForce && i!=currentMaximumIndex) {
                if(currentMaximumIndex!=-1)this.forces.put(currentMaximumIndex+1, 2, "", null, null);
                updateStringMatrixDeglitcher(this.forces, i+1, 2, "<--max");
                currentMaximumIndex=i;
            }
        }
    }

    public double updateForce(int i, Point2D point2D) {
        double force = Util.roundToDecimal(point2D.magnitude(), 2);
        updateStringMatrixDeglitcher(this.forces,i+1, 1, ""+force);
        return force;
    }

    public void clearForces(){
        for (int i = 0; i < graph.getSize(); i++) {
            updateStringMatrixDeglitcher(this.forces, i+1, 1, "");
            updateStringMatrixDeglitcher(this.forces, i+1, 2, "");
            forces.setGridHighlightFillColor(i+1, 1, Color.WHITE, null, null);
        }
    }


    private void paintTemperature(StringMatrix matrix,  double maxForce, int i, int j, double force) {
        float temperature = 0.7f-((float) ((force)/(maxForce))*0.7f);
//            temperature = ((float)i)/((float)forces.size());
        Color tempColor;
        tempColor = Color.getHSBColor(temperature, 0.8f, 0.9f);
        matrix.setGridHighlightFillColor(i, j, tempColor, null, null);
        matrix.highlightCell(i,j,null,null);
    }

    public void updateCoords(List<Point2D> coords){
//        this.forces.show();
        for (int i = 0; i < coords.size(); i++) {
            this.forces.put(i+1, 3, printNormalizedPosition(coords.get(i)), null, null);


        }
    }



    private String printNormalizedPosition(Point2D point2D){
        Point2D tmpPoint = removeFrame(point2D);
        return printPosition(tmpPoint);
    }

    private String printPosition(Point2D tmpPoint) {
        return "["+Util.roundToDecimal(tmpPoint.getX(),4)+" / "
                +Util.roundToDecimal(tmpPoint.getY(),4)+"]";
    }

    private Point2D removeFrame(Point2D value){
        Coordinates tmp = (Coordinates) frame.getUpperLeft();
        return new Point2D(value.getX()-tmp.getX(), value.getY()-tmp.getY());
    }

     Coordinates[] transformStructureCoordinates(Coordinates[] input) {
        Coordinates[] result = new Coordinates[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = new Coordinates(input[i].getX()+(int) frameCorner.getX(), input[i].getY()+(int) frameCorner.getY());
        }
        return result;
    }

    public void initLowerValueBox(MatrixProperties matrixProperties){
        String[][] tempMatr = {{"p_u:", "unitvector:"},{"p_v:","distance"},{"force:","forceSum"}};
        Coordinates tmp = (Coordinates) frame.getLowerRight();
        tmp = new Coordinates(((Coordinates)frame.getUpperLeft()).getX(), tmp.getY()+10);
        lowerValueBox = lang.newStringMatrix(tmp, tempMatr, "upperInfoBox", null, matrixProperties);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                lowerValueBox.setGridHighlightFillColor(i,j,Color.LIGHT_GRAY,null,null);
                lowerValueBox.highlightCell(i,j,null,null);
            }
        }
//        lowerValueBox.hide();
    }

    public void updateForceValues(String forceName, int node1, int node2, Point2D unitVector, double distance, Point2D resultForce){
//        lowerValueBox.show();
        updateStringMatrixDeglitcher(lowerValueBox, 0,0,"p_q"+(node2+1)+": "+ printNormalizedPosition(graph.getPositionOfNode(node2)));
        updateStringMatrixDeglitcher(lowerValueBox, 1,0,"p_q"+(node1+1)+": "+ printNormalizedPosition(graph.getPositionOfNode(node1)));
        updateStringMatrixDeglitcher(lowerValueBox, 0,1,"unitvector: "+ printPosition(unitVector));
        updateStringMatrixDeglitcher(lowerValueBox, 1,1,"distance: "+Util.roundToDecimal(distance,2));
        updateStringMatrixDeglitcher(lowerValueBox, 2,0,forceName+"(q"+(node2+1)+",q"+(node1+1)+"): "+ printPosition(resultForce));
    }

    public void updateForceSum(String forceName, Point2D sum){
//        lowerValueBox.show();
        updateStringMatrixDeglitcher(lowerValueBox, 2,1,"current force sum: "+ printPosition(sum));
    }

    public void hideLowerValueBox(){
        if(visibleLowerValueBox){
            lowerValueBox.hide();
            visibleLowerValueBox = false;
        }
    }

    private void updateStringMatrixDeglitcher(StringMatrix matrix, int row, int column, String string){
        if(!matrix.getElement(row, column).equals(string)) matrix.put(row, column, string, null,null);
    }

    public void updateIteration(int t){
        upperInfoBox.put(1,0, "t: "+t, null, null);
    }

    public void updateDelta(double delta){
        upperInfoBox.put(1,1, "delta(t): "+Util.roundToDecimal(delta, 4), null, null);
    }
    public void updateMaxForce(double maxForce){
        upperInfoBox.put(1,4, "max ||F_n(t)||: "+ Util.roundToDecimal(maxForce, 2), null, null);
    }

    public void highlightDisplacementLoop(){
        algoCode.highlight(2);
        algoCode.highlight(3);
    }

    public void unhighlightDisplacementLoop(){
        algoCode.unhighlight(2);
        algoCode.unhighlight(3);
    }

    public void highlightFrepSum(){
        algoCode.highlight(6);
        algoCode.highlight(7);
        funcCode.highlight(3);
        funcCode.highlight(4);
    }

    public void unhighlightFrepSum(){
        algoCode.unhighlight(6);
        algoCode.unhighlight(7);
        funcCode.unhighlight(3);
        funcCode.unhighlight(4);
    }

    public void highlightFattrSum(){
        algoCode.highlight(4);
        algoCode.highlight(5);
        funcCode.highlight(0);
        funcCode.highlight(1);
    }

    public void unhighlightFattrSum(){
        algoCode.unhighlight(4);
        algoCode.unhighlight(5);
        funcCode.unhighlight(0);
        funcCode.unhighlight(1);
    }

    public void highlightNewCoord(){
        algoCode.highlight(8);
        algoCode.highlight(9);
    }

    public void unhighlightNewCoord(){
        algoCode.unhighlight(8);
        algoCode.unhighlight(9);
    }

    public AnimalGraph2D getGraph() {
        return graph;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public Point2D getFrameCorner() {
        return frameCorner;
    }

    public void showFirstPage(SourceCodeProperties sourceCodeProperties){
        Coordinates coord = new Coordinates((int)(upperleftCorner.getX())+10, (int)upperleftCorner.getY()-65);
        SourceCode txt = lang.newSourceCode(coord, "result",null, sourceCodeProperties);
        List<String> texts = new ArrayList<>();
        List<String> texts2 = new ArrayList<>();
        texts.add("Force Directed Graph Drawing algorithms are used to calculate aesthetically-\n" +
                "pleasing layouts for graphs.\n" +
                "Graphs drawn by these algorithms should e.g. have edges with nearly equal length,\n" +
                "and as few crossing edges as possible.\n \n");
        texts.add("A group of algorithms, like the presented 'Spring Embedder'(Eades 1984), assign forces\n" +
                "to the edges and nodes and simulate motion, which should converge to a minimal energy state.\n \n");
        texts.add("For this, 'Spring Embedder' uses logarithmic springs as attractive force between two adjacent nodes\n" +
                "\t\t\tf_spring(p_u,p_v) = c_spring * log( (||p_u - p_v||)/length ) * uv(p_v,p_u)\n" +
                "and repulsive force between two non-adjacent nodes\n" +
                "\t\t\tf_rep(p_u,p_v) = c_rep / (||p_u - p_v||²) * uv(p_v,p_u)\n" +
                "which are used to calculate the total force applied to a node.\n \n");
        texts.add( "C-attr and c-attr are given constants and 'length' denotes the ideal distance \n" +
                "between two adjacent nodes (spring-length).\n" +
                "delta(t) > 0 is multiplied with the total force, and either constant, or decreases\n" +
                "as a 'Temperature' over time in a linear fashion, to weaken the influence of later changes.\n" +
                "The algorithm terminates after a finite number of steps, or if the \n" +
                "greatest of all forces falls below a specific threshold.\n \n");
        texts.add(
                "According to Eades, experience has shown that\n" +
                "c_attr = 2,  c_rep = 1,  delta = 0.1 (constant or starting value)\n" +
                "are appropriate for most graphs, and that the algorithm should terminate\n" +
                "in less than 100 steps for most graphs.");


        texts2.add("This animation describes the algorithm in three phases.\n" +
                "first, for a specific number of nodes we will see how each attracting and\n" +
                "repulsive force is applied and thus the vector for the new position is calculated.\n \n"
        );
        texts2.add("For the remaining nodes of an iteration (e.g. if we have 18 nodes and don't want\n" +
                "to visualize the calculation of the forces for all of them), only the displacement vectors are shown.\n \n"
        );
        texts2.add("Finally, the remaining iterations show how the nodes move to their new layout over time."
        );


        txt.addMultilineCode(texts.get(0),
                "intro",null);
        lang.nextStep("Introduction");
        txt.addMultilineCode(texts.get(1),
                "intro",null);
        lang.nextStep();
        txt.addMultilineCode(texts.get(2),
                "intro",null);
        txt.highlight(9);
        txt.highlight(11);
        lang.nextStep();
        txt.addMultilineCode(texts.get(3),
                "intro",null);
        lang.nextStep();
        txt.addMultilineCode(texts.get(4),
                "intro",null);
        lang.nextStep();
        txt.hide();
        txt = lang.newSourceCode(coord, "result",null, sourceCodeProperties);
        for(int i = 0; i< texts2.size(); i++){
            txt.addMultilineCode(texts2.get(i),
                    "intro",null);
                lang.nextStep();
        }
        txt.hide();
    }

    public void showResult(int t, int maxIterations, double maxForce, double threshold){
        lang.hideAllPrimitivesExcept(title);
//        title = lang.newText(new Coordinates(8,2), "FDGD: Spring-Embedder", "result", null, new TextProperties());
        SourceCode result = lang.newSourceCode(Util.pointToNode(upperleftCorner), "result",null, new SourceCodeProperties());
        result.addCodeLine("The algorithm finished in "+t+" of "+maxIterations+" iterations.","asd",0,null);
        result.addCodeLine("","asd",0,null);
//        maxForce = Util.roundToDecimal(maxForce, 2);
        if(maxForce>threshold){
            result.addCodeLine("Since the final max ||F_n(t)|| = "+Util.roundToDecimal(maxForce,2)+" was greater than threshold "+threshold+",","asd",0,null);
            result.addCodeLine("there is probably a better solution.","asd",0,null);
            result.addCodeLine("","asd",0,null);
            result.addCodeLine("You can increase the threshold, so it becomes more likely ","asd",0,null);
            result.addCodeLine("that a better layout will be found earlier.","asd",0,null);
            result.addCodeLine("","asd",0,null);
            result.addCodeLine("Alternatively, you can adjust the individual constants, delta-value,","asd",0,null);
            result.addCodeLine("the frame-width and -height, or increase the number of iterations.","asd",0,null);
            result.addCodeLine("(Still, following the paper, a good solution will most likely be found in 100 steps)","asd",0,null);
        }else{
            result.addCodeLine("The final max ||F_n(t)|| = "+Util.roundToDecimal(maxForce,2)+" was less than threshold "+threshold+",","asd",0,null);
            result.addCodeLine("and, in this context, the layout is a good solution.","asd",0,null);
            result.addCodeLine("","asd",0,null);
            result.addCodeLine("You can adjust the individual constants and the delta-value or the frame-width and -height","asd",0,null);
            result.addCodeLine("to alter the algorithm progression and possibly get an even better layout.","asd",0,null);
        }
        lang.nextStep("Results");

    }
}
