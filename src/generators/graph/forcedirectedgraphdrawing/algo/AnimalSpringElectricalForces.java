/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.graph.forcedirectedgraphdrawing.algo;

import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.util.Coordinates;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import generators.graph.helpers.Point2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AnimalSpringElectricalForces extends SpringElectricalForces {
    Language lang;
    GUI gui;
    AnimalGraph2D graph;
    LinkedList<Primitive> firstStatePrimitives;
    Rect area;

    //sequencial animation of all forces for one node
    final static int SINGLENODE = 0;
    //sequential animation of resulting forces for all nodes of one iteration
    final static int ALLNODES = 1;

    final static int THIRDSTATE = 2;
    int state = SINGLENODE;
    int firstStateNodes;
    int firstStateIterations;
    private Color basenodeVectorColor = Color.BLUE;
    private Color baseStateHighlight = Color.YELLOW;
//    private Color repVectorColor=Color.RED;
//    private Color attrVectorColor = Color.GREEN;
//    private Color displacementVectorColor = Color.CYAN;
    private List<Point2D> partialForcesSet;
    private PolylineProperties displacementVectorProbs, attrVectorProbs, repvectorProbs;
    int questionType;
    double questionLikelihood = 0.1;


    public AnimalSpringElectricalForces(Language lang, GUI gui, double c_SPRING, double c_REP, double idealDistance, PolylineProperties displacementVectorProbs, PolylineProperties repvectorProbs, PolylineProperties attrVectorProbs, boolean withCooling) {
        super(c_SPRING, c_REP, gui.getGraph(), gui.getFrameCorner(), gui.getFrameWidth(), gui.getFrameHeight(), idealDistance, withCooling);
        this.lang = lang;
        firstStateNodes = 2;
        firstStateIterations = 2;
        this.gui = gui;
        this.graph=gui.getGraph();
        this.displacementVectorProbs = displacementVectorProbs;
        this.attrVectorProbs = attrVectorProbs;
        this.repvectorProbs = repvectorProbs;

        QuestionGroupModel calcCoords = new QuestionGroupModel("calcCoordinates",1);
        lang.addQuestionGroup(calcCoords);
        QuestionGroupModel calcAForce = new QuestionGroupModel("calcAForce",2);
        lang.addQuestionGroup(calcAForce);

    }

    private void setdoubleStepMode(boolean mode){
            graph.setDoubleStepMode(mode);
    }


    @Override
    void signalIteration(int iteration) {
        questionType = (int)(Math.random()*3);
        partialForcesSet = new ArrayList<>();
        if(iteration <= firstStateIterations){
            gui.clearForces();
            state = SINGLENODE;
            firstStatePrimitives = new LinkedList<>();
            setdoubleStepMode(false);
            if(iteration == firstStateIterations){
                setdoubleStepMode(true);
            }
        } else {
            gui.hideLowerValueBox();
            state = THIRDSTATE;
            setdoubleStepMode(true);

        }
//        gui.setInfotext("iter: "+iteration);
        gui.updateIteration(iteration);
        gui.unhighlightNewCoord();
    }

    @Override
    void signalMaxForce(int t, double maxForce) {
//        System.out.println("iter end: " + t + " " + maxForce);
        if(t==0)gui.currentMaxVal = maxForce;
        gui.updateMaxForce(maxForce);
    }

    @Override
    void signalForces(int t, List<Point2D> forces) {
        if(state == THIRDSTATE){
//            gui.updatePartialForces(partialForcesSet);
        }
        gui.updateForces(forces);
    }

    @Override
    void signalCoords(int t, List<Point2D> coords) {

        if(t>0){
            if(state!=THIRDSTATE){
                lang.nextStep("  Iteration "+t+": Phase 1&2 results");
                lang.newGroup(firstStatePrimitives, "asd").hide();
                gui.highlightNewCoord();

            }else{
                if(questionType==0 && Math.random()<questionLikelihood) {
                    FillInBlanksQuestionModel fillInBlanksQuestionModel = new FillInBlanksQuestionModel("Coordinates_" + t);
                    int node = (int) (Math.random() * graph.getSize());
                    fillInBlanksQuestionModel.setPrompt("Calculate the new position for node " + (node + 1) + " after this Iteration. Write it as 'x;y' and round it to 2 decimals.");
                    String answer = Util.roundToDecimal(coords.get(node).getX(), 2) + ";" + Util.roundToDecimal(coords.get(node).getY(), 2);
                    fillInBlanksQuestionModel.addAnswer(answer, 1, answer);
                    fillInBlanksQuestionModel.setGroupID("calcCoordinates");
                    lang.addFIBQuestion(fillInBlanksQuestionModel);
                }

            }
        }
        gui.updateCoords(coords);
    }

    @Override
    void signalSingleCoord(int iteration, int node, Point2D coord) {
        if (state != THIRDSTATE) {

//            lang.nextStep();
        }
    }

    @Override
    void signalCutVectorInbound(int iteration, int node, Point2D coord) {
    }

    @Override
    void signalDisplacementVectorForce(int iteration, int node, Point2D force) {
//        state = ALLNODES;


        if (state != THIRDSTATE) {

            Coordinates[] coords = new Coordinates[2];
            coords[0] = Util.pointToNode(graph.getPositionOfNode(node));
            coords[1] = Util.pointToNode(this.calcNewCoordinate(force, iteration, node));
            PolylineProperties probs = new PolylineProperties();
            probs.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
//            probs.set(AnimationPropertiesKeys.COLOR_PROPERTY, displacementVectorColor);
            Polyline test = lang.newPolyline(coords, "asd", null, displacementVectorProbs);
            firstStatePrimitives.add(test);

            if (node >= firstStateNodes-1 || node == graph.getSize()-1) {
                gui.highlightDisplacementLoop();
                gui.highlightFattrSum();
                gui.highlightFrepSum();
                state = ALLNODES;
                this.graph.highlightState(node, baseStateHighlight);
                lang.nextStep();
                graph.unHighlightState(node);
                gui.unhighlightDisplacementLoop();
                gui.unhighlightFattrSum();
                gui.unhighlightFrepSum();
            }
            gui.setInfotext("signDisVec "+ state+" node: "+node);
            gui.updateForce(node, force);
        }
//        else if(state == ALLNODES){
//            if(node == graph.getSize()-1) state = THIRDSTATE;
//        }
        graph.unHighlightState(node);

    }

    @Override
    void signalCalcAttractiveForce(int node1, int node2, Point2D unitVector, double distance, double idealDistance, Point2D resultForce) {
        if (state == SINGLENODE) {
            gui.updateForceValues("f_attr", node1, node2, unitVector, distance, resultForce);
//            System.out.println("f_attr(q"+node2+",q"+node1+")"+" "+ node1+" "+ node2+" "+ unitVector+" "+ distance+" "+ resultForce);
        }
    }

    @Override
    void signalCalcRepulsiveForce(int node1, int node2, Point2D unitVector, double distance, Point2D resultForce) {
        if (state == SINGLENODE) {
            gui.updateForceValues("f_rep", node1, node2, unitVector, distance, resultForce);
//            System.out.println("f_rep(q"+node2+",q"+node1+")"+" "+ node1+" "+ node2+" "+ unitVector+" "+ distance+" "+ resultForce);
        }
    }

    @Override
    void signalNormalizedAttrVector(int baseNode, int node2, Point2D totalForce, Point2D currentForce) {
        if (state == SINGLENODE) {
            Coordinates[] coords = new Coordinates[2];
            Point2D tempCoord;
            Polyline test1, test2;
            PolylineProperties probs = new PolylineProperties();
            coords[0] = Util.pointToNode(graph.getPositionOfNode(baseNode));
            tempCoord = Util.cutVectorInbound(graph.getPositionOfNode(baseNode).add(totalForce.multiply(0.1)), minX, minY, maxX, maxY);
            coords[1] = Util.pointToNode(tempCoord);
            probs.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
            probs.set(AnimationPropertiesKeys.COLOR_PROPERTY, basenodeVectorColor);
            test1 = lang.newPolyline(coords, "asd", null, probs);

            coords[0] = Util.pointToNode(graph.getPositionOfNode(node2));
            probs.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
            probs.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
//            probs.set(AnimationPropertiesKeys.COLOR_PROPERTY, attrVectorColor);
            test2 = lang.newPolyline(coords, "asd", null, attrVectorProbs);
            this.graph.highlightState(node2, (Color) attrVectorProbs.get(AnimationPropertiesKeys.COLOR_PROPERTY));
            this.graph.highlightState(baseNode, baseStateHighlight);
//            this.graph.unHighlightState((baseNode-1)%graph.getSize());
            gui.highlightFattrSum();
            gui.updateForceSum("f_attr", totalForce);
            lang.nextStep();
            gui.unhighlightFattrSum();
            graph.unHighlightState(node2);
            test1.hide();
            test2.hide();
//            System.out.println("attr for " + baseNode + " " + node2 + " " + tempCoord.toString());
            gui.setInfotext("signAttrVec "+ state+" node: "+baseNode+ " node2: "+node2);
        }else {
            if (questionType == 2 && Math.random() < questionLikelihood) {
                FillInBlanksQuestionModel fillInBlanksQuestionModel = new FillInBlanksQuestionModel("calcAForce" + Math.random());
                fillInBlanksQuestionModel.setPrompt("Calculate the attractive force between " + (baseNode + 1) + " and " + (node2 + 1) + ". Write it as 'x;y' and round it to 2 decimals.");
                String answer = Util.roundToDecimal(currentForce.getX(), 2) + ";" + Util.roundToDecimal(currentForce.getY(), 2);
                fillInBlanksQuestionModel.addAnswer(answer, 1, answer);
                fillInBlanksQuestionModel.setGroupID("calcAForce");
                lang.addFIBQuestion(fillInBlanksQuestionModel);
            }
        }
//        gui.updatePartialForce(currentForce, baseNode, node2);
//        partialForcesSet.add(currentForce);
    }

    @Override
    void signalNormalizedRepVector(int baseNode, int node2, Point2D totalForce, Point2D currentForce) {
        if (state == SINGLENODE) {
            Coordinates[] coords = new Coordinates[2];
            Point2D tempCoord;
            Polyline test1, test2;
            PolylineProperties probs = new PolylineProperties();
            coords[0] = Util.pointToNode(graph.getPositionOfNode(baseNode));
            tempCoord = Util.cutVectorInbound(graph.getPositionOfNode(baseNode).add(totalForce.multiply(0.1)), minX, minY, maxX, maxY);
            coords[1] = Util.pointToNode(tempCoord);
            probs.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
            probs.set(AnimationPropertiesKeys.COLOR_PROPERTY, basenodeVectorColor);
            test1 = lang.newPolyline(coords, "asd", null, probs);
            coords[0] = Util.pointToNode(graph.getPositionOfNode(node2));
            probs.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
            probs.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
//            probs.set(AnimationPropertiesKeys.COLOR_PROPERTY, repVectorColor);
            test2 = lang.newPolyline(coords, "asd", null, this.repvectorProbs);
            this.graph.highlightState(node2, (Color) repvectorProbs.get(AnimationPropertiesKeys.COLOR_PROPERTY));
            this.graph.highlightState(baseNode, baseStateHighlight);
//            this.graph.unHighlightState((baseNode-1)%graph.getSize());
            gui.highlightFrepSum();
            gui.updateForceSum("f_rep", totalForce);
            lang.nextStep();
            gui.unhighlightFrepSum();
            graph.unHighlightState(node2);
            test1.hide();
            test2.hide();
//            System.out.println("rep for " + baseNode + " " + node2 + " " + tempCoord.toString());
            gui.setInfotext("signRepVec "+ state+" node: "+baseNode+ " node2: "+node2);
        }else{
            if(questionType==1 && Math.random()<questionLikelihood) {
                FillInBlanksQuestionModel fillInBlanksQuestionModel = new FillInBlanksQuestionModel("calcAForce" + Math.random());
                fillInBlanksQuestionModel.setPrompt("Calculate the repulsive force between " + (baseNode + 1) + " and "+(node2+1)+". Write it as 'x;y' and round it to 2 decimals.");
                String answer = Util.roundToDecimal(currentForce.getX(), 2) + ";" + Util.roundToDecimal(currentForce.getY(), 2);
                fillInBlanksQuestionModel.addAnswer(answer, 1, answer);
                fillInBlanksQuestionModel.setGroupID("calcAForce");
                lang.addFIBQuestion(fillInBlanksQuestionModel);
            }
        }
//        gui.updatePartialForce(currentForce, baseNode, node2);
//        partialForcesSet.add(currentForce);
    }
@Override
    void signalCoolingDelta(int iteration, double delta){gui.updateDelta(delta);}

    @Override
    void signalResult(int t, int maxIterations, double maxForce, double threshold) {gui.showResult(t, maxIterations, maxForce, threshold);}


}
