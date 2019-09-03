package animalscript.extensions;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Hashtable;

import animal.animator.ColorChanger;
import animal.animator.Highlight;
import animal.animator.HighlightEdge;
import animal.animator.SetText;
import animal.graphics.PTGraph;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;
import animalscript.core.AnimalParseSupport;
import animalscript.core.AnimalScriptInterface;
import animalscript.core.BasicParser;

/**
 * This class provides an import filter for AnimalScript array commands
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2000-03-21
 */
public class GraphSupport extends BasicParser implements AnimalScriptInterface {
	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public GraphSupport() {
		handledKeywords = new Hashtable<String, Object>();
		rulesHash = new XProperties();
		handledKeywords.put("graph", "parseGraphInput");

		handledKeywords.put("highlightedge", "parseEdgeTransformation");
		handledKeywords.put("unhighlightedge", "parseEdgeTransformation");
		handledKeywords.put("transformedge", "parseEdgeTransformation");
		
		handledKeywords.put("highlightnode", "parseNodeTransformation");
		handledKeywords.put("unhighlightnode", "parseNodeTransformation");
		handledKeywords.put("transformnode", "parseNodeTransformation");
		
		handledKeywords.put("setedgeweight", "parseEdgeWeightTransformation");
		
		//TODO add more
		handledKeywords.put("setnodehighlightfillcolor", "parseColorSetNode");
		handledKeywords.put("setnodehighlighttextcolor", "parseColorSetNode");

		handledKeywords.put("setEdgeHighlightTextColor".toLowerCase(), "parseColorSetEdge");
		handledKeywords.put("setEdgeHighlightPolyColor".toLowerCase(), "parseColorSetEdge");

		handledKeywords.put("setNodeLabel".toLowerCase(), "parseNodeLabelTransformation");
		handledKeywords.put("setNodeRadius".toLowerCase(), "parseNodeRadiusTransformation");
	}

	// ===================================================================
	// interface methods
	// ===================================================================

	/**
	 * Determine depending on the command passed if a new step is needed Also keep
	 * in mind that we might be in a grouped step using the {...} form. Usually,
	 * every command not inside such a grouped step is contained in a new step.
	 * However, this is not the case for operations without visible effect -
	 * mostly maintenance or declaration entries.
	 * 
	 * @param command
	 *          the command used for the decision.
	 * @return true if a new step must be generated
	 */
	public boolean generateNewStep(String command) {
		return !sameStep;
	}

	// ===================================================================
	// Animator parsing routines
	// ===================================================================

	/**
	 * Create a graph from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseGraphInput() throws IOException {
		StringBuilder oids = new StringBuilder();

		// 1. parse graph type and name
		ParseSupport.parseWord(stok, "graph type");
		String graphName = AnimalParseSupport.parseText(stok, "graph name");

		// 6. parse graph size
		ParseSupport.parseMandatoryWord(stok, "graph keyword 'size'", "size");
		int nrNodes = ParseSupport.parseInt(stok,
				"nr of graph nodes [1, infinity)", 1);

		// create new object
		PTGraph newGraph = new PTGraph(nrNodes);

		// 3. optional color definitions
		// 3.1 '[color someColor]'
		Color helper = null;
		helper = AnimalParseSupport.parseAndSetColor(stok, graphName,
				"color", "black");
		newGraph.setColor(helper);
		
		// 3.2 '[bgColor someColor]'
		newGraph.setBGColor(helper = AnimalParseSupport.parseAndSetColor(stok, graphName,
				"bgColor", "white"));
		
		// 3.3 '[outlineColor someColor]'
		helper = AnimalParseSupport.parseAndSetColor(stok,
				graphName, "outlineColor", "black");
		newGraph.setOutlineColor(helper);
		
		// 3.4 '[highlightColor someColor]'
		helper = AnimalParseSupport.parseAndSetColor(stok,
				graphName, "highlightColor", "yellow");
		newGraph.setHighlightColor(helper);
		
		// 3.5 '[elemHighlightColor someColor]'
		helper = AnimalParseSupport.parseAndSetColor(stok,
				graphName, "elemHighlightColor", "red");
		newGraph.setElemHighlightColor(helper);
		
		// 3.6 '[nodeFontColor someColor]'
		helper = AnimalParseSupport.parseAndSetColor(stok,
				graphName, "nodeFontColor", "blue");
		newGraph.setNodeFontColor(helper);
		
		// 3.7 '[edgeFontColor someColor]'
		helper = AnimalParseSupport.parseAndSetColor(stok,
				graphName, "edgeFontColor", "blue");
		newGraph.setEdgeFontColor(helper);
		
		// 4. parse if the graph is directed
		newGraph.setDirection(ParseSupport.parseOptionalWord(stok, 
				"graph keyword 'directed' [optional]", "directed"));

		// 5. parse if the graph is directed
		newGraph.setWeight(ParseSupport.parseOptionalWord(stok, 
				"graph keyword 'weighted' [optional]", "weighted"));
		
		// 7. parse node definition: 'nodes { [<nodeFont>] {<edgeDefinition>} }'
		// definition of each node: '"value" [at] location', separated by ','
		// 7.1 parse keyword 'nodes'
		ParseSupport.parseMandatoryWord(stok, "graph keyword 'nodes'", "nodes");

		// 7.2 parse mandatory '{'
		ParseSupport.parseMandatoryChar(stok, "graph node keyword '{'", '{');

		// 7.3 parse optional font information '[font fontDef]'
		if (ParseSupport.parseOptionalWord(stok,
				"graph keyword 'font' [optional]", "font")) {
			stok.pushBack();
			newGraph.setNodeFont(AnimalParseSupport.parseFontInfo(stok,
					"graph node font"));
		}

		String localText = null;
		Point coordinate = null;
		for (int currentNode = 0; currentNode < nrNodes; currentNode++) {
			// 7.4 parse node value as '"value"'
			localText = AnimalParseSupport.parseText(stok, "graph node value #"
					+ currentNode, null, false, chosenLanguage);

			// 7.5 swallow optional 'at'
			ParseSupport.parseOptionalWord(stok, "graph keyword 'at' [optional]",
					"at");

			// 7.6 process location
			coordinate = AnimalParseSupport.parseNodeInfo(stok, "graph node #"
					+ currentNode + " location", null);

			// 7.7 assign node position and value
			newGraph.setPositionNode(currentNode, coordinate);
			newGraph.enterValueNode(currentNode, localText);

			// 7.8 parse mandatory ',' separator (not for last node)
			if (currentNode != nrNodes - 1)
				ParseSupport.parseMandatoryChar(stok, "graph node separator ','", ',');
		}
		// 7.9 check for node end '}'
		ParseSupport.parseMandatoryChar(stok, "graph nodes keychar '}'", '}');

		// 8. process edges 'edges { [<edgeFont>] <edgeDefinition> }'
		// definition of each edge: '(e1, e2[, weight])', separated by ','
		// 8.1 parse keyword 'edges'
		ParseSupport.parseMandatoryWord(stok, "graph keyword 'edges'", "edges");

		// 8.2 parse mandatory '{'
		ParseSupport.parseMandatoryChar(stok, "graph edge keyword '{'", '{');

		// 8.3 parse optional edge font definition '[font someFontDef]'
		if (ParseSupport.parseOptionalWord(stok,
				"graph keyword 'edgeFont' [optional", "font")) {
			stok.pushBack();
			newGraph.setEdgeFont(AnimalParseSupport.parseFontInfo(stok,
					"graph edge font"));
		}


		// 8.4 parse individual edges
		String edgeText = "index [0, " + (nrNodes-1) +"]";
		while (!ParseSupport.parseOptionalChar(stok, "graph edge terminator '}'",
				'}')) {
			// not a '{', parse!
			// 8.5 swallow opening parenthesis
			ParseSupport.parseMandatoryChar(stok, "graph edge keychar '('", '(');
			
			// 8.6 parse index of edge source [0, size - 1]
			int e1 = ParseSupport.parseInt(stok, "graph edge source " +edgeText,
					0, nrNodes - 1);
			
			// 8.7 parse mandatory separator ','
			ParseSupport.parseMandatoryChar(stok, "graph edge separator ','",
					',');

			// 8.8 parse index of edge target [0, size - 1]
			int e2 = ParseSupport.parseInt(stok, "graph edge target " +edgeText,
					0, nrNodes - 1);
			
			// 8.9 install the edge
			newGraph.setVisibleEdge(e1, e2, true);

			// 8.10 parse optional weight
			if (ParseSupport.parseOptionalChar(stok, 
					"graph edge weight separator ','", ',')) {
				String edgeWeightText = AnimalParseSupport.parseText(stok, 
						"graph edge weight (" +e1 +", " +e2 + ")");
				newGraph.enterValueEdge(e1, e2, edgeWeightText);			
			} else
				newGraph.enterValueEdge(e1, e2, PTGraph.UNDEFINED_EDGE_WEIGHT);

			// 8.11 swallow opening parenthesis
			ParseSupport.parseMandatoryChar(stok, "graph edge keychar ')'", ')');
			
			// 8.12 may use ',' between edges, but need not
			ParseSupport.parseOptionalChar(stok,
					"graph edge separator ',' [optional]", ',');
		}
		// 8.13 mandatory '}' terminator already swallowed by while loop!
		
		// 2. set origin
		if (ParseSupport.parseOptionalWord(stok, 
				"graph keyword 'origin' [optional]", "origin")) {
			Point originPoint = AnimalParseSupport.parseNodeInfo(stok,
					"graph origin node", null);
			newGraph.setOrigin(originPoint);
		}
		
		// 9. parse optional keyword 'showIndices'
		newGraph.setIndices(ParseSupport.parseOptionalWord(stok, 
				"graph keyword 'showIndices' [optional]", "showIndices"));

		// 10. check for depth information and set it, if available
		AnimalParseSupport.parseAndSetDepth(stok, newGraph, "point");

		// 11. add the object to the list of graphic objects
		BasicParser.addGraphicObject(newGraph, anim);

		// 12. append the object's ID to the list
		oids.append(newGraph.getNum(false));

		// 13. insert into object list -- necessary for lookups later on!
		getObjectIDs().put(graphName, newGraph.getNum(false));
		getObjectTypes().put(graphName, getTypeIdentifier("graph"));
		getObjectProperties().put(graphName + ".size", nrNodes);

		// 14. display the component, unless marked as 'hidden'
		AnimalParseSupport.showComponents(stok, oids.toString(), "graph", true);
	}
	
	/**
	 * Create a edge transformation animator from the description read from 
	 * the StreamTokenizer. The description is usually generated by
	 * other programs and dumped in a file or on System.out.
	 * 
	 * Example notation: highlight edges from 1->3 and 2->4 simultaneously
	 * highlightEdge on "g" [type "highlight edges"] (1, 3) (2, 4) [timing]
   * unhighlightEdge on "g" [type "unhighlight edges"] (3, 2) (1, 2) [timing]
	 */
	public void parseEdgeTransformation() throws IOException {
		// 1. parse graph type and name
		String operationType = ParseSupport.parseWord(stok, 
				"[un]highlightEdge type");
		boolean hlMode = !operationType.startsWith("un");
		StringBuilder msgBuffer = new StringBuilder(64).append(operationType);
		msgBuffer.append(" {0}");
		String baseMsg = msgBuffer.toString();
		
		ParseSupport.parseMandatoryWord(stok,	
				 baseMsg + " keyword 'on'", "on");
		String graphName = AnimalParseSupport.parseText(stok, "graph name");

		// retrieve IDs for array
		int graphID = getObjectIDs().getIntProperty(graphName);
		int nrNodes = getObjectProperties().getIntProperty(graphName + ".size",
				-1);
		if (nrNodes == -1) {
			MessageDisplay.errorMsg("could not retrieve graph called '"
					+graphName +"' in line " +stok.lineno() +", skipping rest of line.",
					MessageDisplay.RUN_ERROR);
			return;
		}

//		PTGraphicObject targetObject = animState.getCloneByNum(graphID);
		HighlightEdge animator = new HighlightEdge(nrNodes);
		animator.setObjectNums(new int[] { graphID });
		animator.setStep(currentStep);

		// 2. parse optional transformation type
		// check for method definition, if given
		String methodName = AnimalParseSupport.parseMethod(stok,
				baseMsg + "operation type", "type",
				(hlMode) ? "highlight edges" : "unhighlight edges");
		animator.setMethod(methodName);

		// continue as long as '(' is found
		while (stok.nextToken() == '(') {
			// '(' already consumed :-)
			int sourceEdgeIndex = ParseSupport.parseInt(stok,
					"index of first graph node [0, " + 1 +"]", 0, nrNodes);
			ParseSupport.parseMandatoryChar(stok, 
					baseMsg + " edge separator ','", ',');
			int targetEdgeIndex= ParseSupport.parseInt(stok,
					"index of first graph node [0, " + 1 +"]", 0, nrNodes);
			ParseSupport.parseMandatoryChar(stok, baseMsg + " edge end ')'", ')');
			animator.setHighlightState(sourceEdgeIndex, targetEdgeIndex, true);
		}
		// why not assign object ID?
		// parse optional timing - is set within the method!
		stok.pushBack();
		AnimalParseSupport.parseTiming(stok, animator, "Move");
		
		// insert into list of animators
		BasicParser.addAnimatorToAnimation(animator, anim);
	}
	
	/**
	 * Create a node transformation animator from the description read from 
	 * the StreamTokenizer. The description is usually generated by
	 * other programs and dumped in a file or on System.out.
	 *
	 * Example notation: set weight of edge from node 2 to 4 to value 7
	 * setEdgeWeight [of] "g" [edge] (2, 4) to 7 [timing]
	 */
	public void parseEdgeWeightTransformation() throws IOException {
		// 1. parse graph type and name
//     String operationType = 
      ParseSupport.parseWord(stok, "setEdgeWeight type");
		ParseSupport.parseOptionalWord(stok, "optional setEdgeWeight keyword 'of'", 
				"of");
		String graphName = AnimalParseSupport.parseText(stok, "graph name");
		// retrieve IDs for array
		int graphID = getObjectIDs().getIntProperty(graphName);
		int nrNodes = getObjectProperties().getIntProperty(graphName + ".size",
				-1);
		if (nrNodes == -1) {
			MessageDisplay.errorMsg("could not retrieve graph called '"
					+graphName +"' in line " +stok.lineno() +", skipping rest of line.",
					MessageDisplay.RUN_ERROR);
			return;
		}
		
		// parse edge definition
		ParseSupport.parseOptionalWord(stok, 
				"optional setEdgeWeight keyword 'edge'", "edge");
		ParseSupport.parseMandatoryChar(stok, "setEdgeWeight char '('", '(');
		int sourceEdgeIndex = ParseSupport.parseInt(stok,
				"index of first graph node [0, " + 1 +"]", 0, nrNodes);
		ParseSupport.parseMandatoryChar(stok, 
				"setEdgeWeight edge separator ','", ',');
		int targetEdgeIndex= ParseSupport.parseInt(stok,
				"index of first graph node [0, " + 1 +"]", 0, nrNodes);
		ParseSupport.parseMandatoryChar(stok, "setEdgeWeight edge end ')'", ')');

		ParseSupport.parseMandatoryWord(stok, "setEdgeWeight keyword 'to'",
				"to");
		String newWeight = AnimalParseSupport.parseText(stok, "new edge weight");
		System.err.println("adapt edge weight ("+sourceEdgeIndex +", " 
				+targetEdgeIndex +") to value " +newWeight);
		System.err.println("*** animator missing! ***");
		PTGraph graph = (PTGraph)animState.getCloneByNum(graphID);
		graph.enterValueEdge(sourceEdgeIndex, targetEdgeIndex, newWeight);
	}
		
	/**
	 * Create a node transformation animator from the description read from 
	 * the StreamTokenizer. The description is usually generated by
	 * other programs and dumped in a file or on System.out.
	 * 
	 * Example (if "g" is a graph ;-))
	 * highlightNode on "g" [type "highlight nodes"] nodes 1 3 5 6 after 10 ms within 20 ms
   * unhighlightNode on "g" [type "unhighlight nodes"] nodes 1 within 20 ms 
	 */
	public void parseNodeTransformation() throws IOException {
		// 1. parse graph type and name
		String operationType = ParseSupport.parseWord(stok, 
				"[un]highlightNode type");
		boolean hlMode = !operationType.startsWith("un");
		StringBuilder msgBuffer = new StringBuilder(64).append(operationType);
		String baseMsg = msgBuffer.toString();
		
		ParseSupport.parseMandatoryWord(stok,	
				 baseMsg + " keyword 'on'", "on");
		String graphName = AnimalParseSupport.parseText(stok, "graph name");

		// retrieve IDs for array
		int graphID = getObjectIDs().getIntProperty(graphName);
		int nrNodes = getObjectProperties().getIntProperty(graphName + ".size",
				-1);
		if (nrNodes == -1) {
			MessageDisplay.errorMsg("could not retrieve graph called '"
					+graphName +"' in line " +stok.lineno() +", skipping rest of line.",
					MessageDisplay.RUN_ERROR);
			return;
		}

//		PTGraphicObject targetObject = animState.getCloneByNum(graphID);
		Highlight animator = new Highlight(nrNodes);
		animator.setObjectNums(new int[] { graphID });
		animator.setStep(currentStep);

		// 2. parse optional transformation type
		// check for method definition, if given
		String methodName = AnimalParseSupport.parseMethod(stok,
				baseMsg + "operation type", "type",
				(hlMode) ? "highlight nodes" : "unhighlight nodes");
		animator.setMethod(methodName);

		ParseSupport.parseMandatoryWord(stok, baseMsg +" keyword 'nodes'",
				"nodes");
		@SuppressWarnings("unused")
		int token = Integer.MAX_VALUE;
		// continue as long as a number (for a node) is found
		while ((token = stok.nextToken()) == StreamTokenizer.TT_NUMBER) {
//				|| token == StreamTokenizer.TT_WORD 
//				|| token == '"') {
			
			int nodeIndex = (int)stok.nval;
			animator.setHighlightState(nodeIndex, true);
		}
		// why not assign object ID?
		// parse optional timing - is set within the method!
		stok.pushBack();
		AnimalParseSupport.parseTiming(stok, animator, "Move");
		
		// insert into list of animators
		BasicParser.addAnimatorToAnimation(animator, anim);
	}
	
	
	public void parseColorSetNode() throws IOException {
		String colorType = ParseSupport.parseWord(stok, "color set operation");
		String baseMsg = colorType;
		
		ParseSupport.parseMandatoryWord(stok,	
				 baseMsg + " keyword 'on'", "on");
		String graphName = AnimalParseSupport.parseText(stok, "graph name");

		// retrieve IDs for array
		int graphID = getObjectIDs().getIntProperty(graphName);
		int nrNodes = getObjectProperties().getIntProperty(graphName + ".size",
				-1);
		if (nrNodes == -1) {
			MessageDisplay.errorMsg("could not retrieve graph called '"
					+graphName +"' in line " +stok.lineno() +", skipping rest of line.",
					MessageDisplay.RUN_ERROR);
			return;
		}

//		PTGraphicObject targetObject = animState.getCloneByNum(graphID);

		ParseSupport.parseMandatoryWord(stok, baseMsg +" keyword 'nodes'",
				"nodes");
		
		int fromRange = ParseSupport.parseInt(stok, baseMsg +" nodes indices",
				0, nrNodes);
		int toRange = fromRange;	// TODO multiple nodes
		

		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'with'", "with");
		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'color'", "color");
		
		Color color = AnimalParseSupport.parseColor(stok, colorType + " color");
		
		// why not assign object ID?
		// parse optional timing - is set within the method!
		stok.pushBack();
		
		String method = colorType+" "+fromRange+" "+toRange;
		ColorChanger colChanger = new ColorChanger(currentStep, graphID, 0,
				method, color);
		AnimalParseSupport.parseTiming(stok, colChanger, "Color");
		BasicParser.addAnimatorToAnimation(colChanger, anim);
	}
	
	
	public void parseColorSetEdge() throws IOException {
		String colorType = ParseSupport.parseWord(stok, "color set operation");
		String baseMsg = colorType;
		
		ParseSupport.parseMandatoryWord(stok,	
				 baseMsg + " keyword 'on'", "on");
		String graphName = AnimalParseSupport.parseText(stok, "graph name");

		// retrieve IDs for array
		int graphID = getObjectIDs().getIntProperty(graphName);
		int nrNodes = getObjectProperties().getIntProperty(graphName + ".size",
				-1);
		if (nrNodes == -1) {
			MessageDisplay.errorMsg("could not retrieve graph called '"
					+graphName +"' in line " +stok.lineno() +", skipping rest of line.",
					MessageDisplay.RUN_ERROR);
			return;
		}

//		PTGraphicObject targetObject = animState.getCloneByNum(graphID);

		ParseSupport.parseMandatoryWord(stok, baseMsg +" keyword 'nodes'",
				"edge");
		
		int fromRange = ParseSupport.parseInt(stok, baseMsg +" nodes indices",
				0, nrNodes);
		int toRange = ParseSupport.parseInt(stok, baseMsg +" nodes indices",
				0, nrNodes);
		

		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'with'", "with");
		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'color'", "color");
		
		Color color = AnimalParseSupport.parseColor(stok, colorType + " color");
		
		// why not assign object ID?
		// parse optional timing - is set within the method!
		stok.pushBack();
		
		String method = colorType+" "+fromRange+" "+toRange;
		ColorChanger colChanger = new ColorChanger(currentStep, graphID, 0,
				method, color);
		AnimalParseSupport.parseTiming(stok, colChanger, "Color");
		BasicParser.addAnimatorToAnimation(colChanger, anim);
	}


	public void parseNodeLabelTransformation() throws IOException {
		// 1. parse graph type and name
//     String operationType = 
      ParseSupport.parseWord(stok, "setNodeLabel type");
		ParseSupport.parseOptionalWord(stok, "optional setNodeLabel keyword 'of'", 
				"of");
		String graphName = AnimalParseSupport.parseText(stok, "graph name");
		// retrieve IDs for array
		int graphID = getObjectIDs().getIntProperty(graphName);
		int nrNodes = getObjectProperties().getIntProperty(graphName + ".size",
				-1);
		if (nrNodes == -1) {
			MessageDisplay.errorMsg("could not retrieve graph called '"
					+graphName +"' in line " +stok.lineno() +", skipping rest of line.",
					MessageDisplay.RUN_ERROR);
			return;
		}
		
		ParseSupport.parseMandatoryWord(stok, "node", "node");
		int nodeIndex = ParseSupport.parseInt(stok,
				"index of graph node [0, " + 1 +"]", 0, nrNodes);

		ParseSupport.parseMandatoryWord(stok, "setNodeLabel keyword 'to'",
				"to");
		String newLabel = AnimalParseSupport.parseText(stok, "new node label");
		
		PTGraph graph = (PTGraph)animState.getCloneByNum(graphID);
		graph.enterValueNode(nodeIndex, newLabel);
		
		SetText setter = new SetText(currentStep, graphID, 0, 0, "ticks", newLabel);
		setter.setMethod(setter.getMethod()+" Node "+nodeIndex);
		BasicParser.addAnimatorToAnimation(setter, anim);
		
	}
	
	public void parseNodeRadiusTransformation() throws IOException {
		// 1. parse graph type and name
//     String operationType = 
      ParseSupport.parseWord(stok, "setNodeRadius type");
		ParseSupport.parseOptionalWord(stok, "optional setNodeRadius keyword 'of'", 
				"of");
		String graphName = AnimalParseSupport.parseText(stok, "graph name");
		// retrieve IDs for array
		int graphID = getObjectIDs().getIntProperty(graphName);
		int nrNodes = getObjectProperties().getIntProperty(graphName + ".size",
				-1);
		if (nrNodes == -1) {
			MessageDisplay.errorMsg("could not retrieve graph called '"
					+graphName +"' in line " +stok.lineno() +", skipping rest of line.",
					MessageDisplay.RUN_ERROR);
			return;
		}
		
		ParseSupport.parseMandatoryWord(stok, "node", "node");
		int nodeIndex = ParseSupport.parseInt(stok,
				"index of graph node [0, " + 1 +"]", 0, nrNodes);

		ParseSupport.parseMandatoryWord(stok, "setNodeRadius keyword 'to'",
				"to");
		String newContent = AnimalParseSupport.parseText(stok, "new node radius");
		Integer newRadius;
		if(newContent.equals("null")){
			newRadius = null;
		}else{
			newRadius = Integer.valueOf(newContent);
		}
		
		PTGraph graph = (PTGraph)animState.getCloneByNum(graphID);
		graph.enterNodeRadius(nodeIndex, newRadius);
		
		SetText setter = new SetText(currentStep, graphID, 0, 0, "ticks", newContent);
		setter.setMethod("setRadius Node "+nodeIndex);
		BasicParser.addAnimatorToAnimation(setter, anim);
		
	}
	
}