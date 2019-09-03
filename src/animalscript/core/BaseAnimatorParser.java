package animalscript.core;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import animal.animator.ColorChanger;
import animal.animator.Move;
import animal.animator.MoveBase;
import animal.animator.Rotate;
import animal.animator.TimedShow;
import animal.graphics.PTArc;
import animal.graphics.PTBoxPointer;
import animal.graphics.PTCircle;
import animal.graphics.PTGraph;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * This class provides an import filter for program output to Animal.
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 1999-06-05
 */
public class BaseAnimatorParser extends BasicParser implements
		AnimalScriptInterface {
	// ========================= attributes =========================

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public BaseAnimatorParser() {
		handledKeywords = new Hashtable<String, Object>();
		rulesHash = new XProperties();

		handledKeywords.put("clone", "parseCloning");
		rulesHash
				.put(
						"clone",
						"# object 'id' is cloned as 'newID' at the chosen position\n# (default: point of origin) and using the timing given.");

		handledKeywords.put("color", "parseColorChange");
		handledKeywords.put("fillcolor", "parseColorChange");
		rulesHash
				.put(
						"color",
						"# change the color of the object(s) to 'colorname'\n# using the timing info and the optional type");

		handledKeywords.put("setlink", "parseListOperation");
		rulesHash.put("setlink",
				"# set the link of the object to the target list element");
		handledKeywords.put("clearlink", "parseListOperation");
		rulesHash.put("clearlink",
				"# reset the pointer of the target object to null");

		handledKeywords.put("move", "parseMove");
		handledKeywords.put("translate", "parseMove");
		handledKeywords.put("jump", "parseMove");
		rulesHash
				.put(
						"move",
						"# move the object(s) by move method 'subtype' using the timing...: \n# * move along the 'via' object, must be arc / polyline\n# * move along a line of the given nodes\n# * move along arc nodes: center, radius x, radius y, start / end angle\n# * move along circle: center, radius, start / end angle\n# * set target position to given node\n# * move to target position");
		rulesHash.put("translate", "# see rule for 'move'");
		rulesHash.put("jump", "# see rule for 'move'");

		handledKeywords.put("rotate", "parseRotate");
		rulesHash
				.put(
						"rotate",
						"# rotate object(s) around the given center; must be a point,\n# * or rotate object(s) around center node by given degrees");

		handledKeywords.put("show", "parseShowHide");
		rulesHash.put("show", "# show object(s) using timing");

		handledKeywords.put("hide", "parseShowHide");
		rulesHash.put("hide", "# hide object(s) using timing");

		handledKeywords.put("hideall", "parseShowHide");
		rulesHash.put("hideall", "# hide all objects using timing");

		handledKeywords.put("hideallbut", "parseShowHide");
		rulesHash.put("hideallbut",
				"# hide all objects except the given ID(s)using timing");

		// handledKeywords.put("zoom", "parseZoom");

		rulesHash
				.put("timing",
						"# operate after the given offset; operation takes time given after 'within'");
		
//		handledKeywords.put("settext", "parseSetText");
//		rulesHash.put("settext", 
//				"# set the text of an object\n#  setText \"oid\" \"newText\"");
//		
//		handledKeywords.put("setfont", "parseSetFont");
//		rulesHash.put("setfont",
//				"# sets the font of the selected object\n#  setFont \"oid\" <fontDef>");
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
	 * Note that this method will return <code>false</code> only if the command
	 * is inside a grouped step!
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
	 * Create a Clone operation from the description read from the
	 * StreamTokenizer. The description is usually generated by other programs and
	 * dumped in a file or on System.out.
	 */
	public void parseCloning() throws IOException {
		// parse exact command
		ParseSupport.parseMandatoryWord(stok, "clone keyword 'clone'", "clone");

		// parse the object concerned
		String originalObject = AnimalParseSupport.parseText(stok,
				"Clone base object name");
		int[] targetOIDs = getObjectIDs().getIntArrayProperty(originalObject);
		StringBuilder cloneIDs = new StringBuilder(targetOIDs.length << 3);

		ParseSupport.parseMandatoryWord(stok, "clone keyword 'as'", "as");
		String targetName = AnimalParseSupport.parseText(stok, "clone target name");
		PTGraphicObject orig = animState.getCloneByNum(targetOIDs[0]);
		PTGraphicObject go = null;
		Point basePoint = orig.getBoundingBox().getLocation();
		getObjectProperties().put("Polyline.lastNode", basePoint);
		ParseSupport.parseOptionalWord(stok, "clone target location keyword 'at'",
				"at");
		Point targetPoint = AnimalParseSupport.parseNodeInfo(stok,
				"target location for cloned objects", null);
		if (currentNodeMode != ParseSupport.NODETYPE_OFFSET_MOVE)
			targetPoint = new Point(targetPoint.x - basePoint.x, targetPoint.y
					- basePoint.y);
		String localTargetName = targetName;
		for (int i = 0; i < targetOIDs.length; i++) {
			orig = animState.getCloneByNum(targetOIDs[i]);
			go = (PTGraphicObject) orig.clone();

			go.clonePropertiesFrom(orig.getProperties(), true);
			if (go != null) {
				go.resetNum();
				go.translate(targetPoint.x, targetPoint.y);
				cloneIDs.append(go.getNum(true)).append(" ");
				localTargetName = (i > 0) ? orig.getObjectName() + ".clone"
						: targetName;
				go.setObjectName(localTargetName);
				getObjectIDs().put(localTargetName, go.getNum(false));
				BasicParser.addGraphicObject(go, anim);
			}
		}
		getObjectIDs().put(targetName, cloneIDs.toString());
		AnimalParseSupport.showComponents(stok, cloneIDs.toString(), "clone", true);
	}

	/**
	 * Create a ColorChange from the description read from the StreamTokenizer.
	 * The description is usually generated by other programs and dumped in a file
	 * or on System.out.
	 */
	public void parseColorChange() throws IOException {
		// parse exact command
		String colorType = ParseSupport.parseWord(stok, "color change operation");
		ColorChanger colChanger = null;
		Color c = null;

		// parse the objects concerned
		int[] targetOIDs = ParseSupport.parseOIDNames(stok, getObjectIDs(), anim
				.getNextGraphicObjectNum());

		if (colorType.equals("color"))
			colorType = AnimalParseSupport.parseMethod(stok, "color change", "type",
					"color");

		// determine the target color
		c = AnimalParseSupport.parseColor(stok, colorType + " animator");

		// generate a generic color changer move(instanteous change)
		colChanger = new ColorChanger(currentStep, targetOIDs, 0, colorType, c);

		// parse optional timing - is set within the method!
		AnimalParseSupport.parseTiming(stok, colChanger, "Color");

		// insert into list of animators
		BasicParser.addAnimatorToAnimation(colChanger, anim);
	}

	public void parseListOperation() throws IOException {
		String localType = ParseSupport.parseWord(stok, "list operation keyword");

		String targetIDName = AnimalParseSupport.parseText(stok,
				"index marker name");
		int[] targetOIDs = getObjectIDs().getIntArrayProperty(targetIDName);
		PTPolyline moveLine = null;

		PTGraphicObject ptgo = animState.getCloneByNum(targetOIDs[0]);
		// String baseElementName = getObjectIDs().getProperty(targetIDName);

		// check for link number
		int ptrNr = 1;
		boolean pointerNumberGiven = false;
		if (ParseSupport.parseOptionalWord(stok, "link nr.", "link")) {
			ptrNr = ParseSupport.parseInt(stok, "link nr", 1, ((PTBoxPointer) ptgo)
					.getPointerCount());
			pointerNumberGiven = true;
		}

		// calculate target coordinates
		Point currentPoint = ((PTBoxPointer) ptgo).getTip(ptrNr - 1);
		Point[] movePoints = new Point[2];
		int[] oid = { ptgo.getNum(false) };
		movePoints[0] = currentPoint;
		if (localType.equalsIgnoreCase("setLink")) {
			if (ParseSupport.parseOptionalWord(stok, "index marker keyword 'to'",
					"to")) {
				String targetElement = AnimalParseSupport.parseText(stok,
						"target list element");
				int targetID = getObjectIDs().getIntProperty(targetElement);
				PTGraphicObject targetObject = animState.getCloneByNum(targetID);
				Rectangle boundingBox = ((PTBoxPointer) targetObject)
						.getBoundingBoxWithoutPointers();
				Rectangle firstBBox = ((PTBoxPointer) ptgo)
						.getBoundingBoxWithoutPointers();
				if (firstBBox.x < boundingBox.x)
					movePoints[1] = new Point(boundingBox.x,
							((PTBoxPointer) targetObject).getTipOrigin(ptrNr - 1).y);
				else
					movePoints[1] = new Point(boundingBox.x + boundingBox.width,
							((PTBoxPointer) targetObject).getTipOrigin(ptrNr - 1).y);
			} else
				movePoints[1] = AnimalParseSupport.parseNodeInfo(stok,
						"list pointer target location", null);
		} else if (localType.equalsIgnoreCase("clearLink")) {
			Rectangle boundingBox = ((PTBoxPointer) ptgo)
					.getBoundingBoxWithoutPointers();
			movePoints[1] = new Point(boundingBox.x
					+ ((ptrNr % 2 == 1) ? (boundingBox.width - 5) : 5),
					((PTBoxPointer) ptgo).getTipOrigin(0).y);
		}
		moveLine = new PTPolyline(movePoints);
		moveLine.setObjectName("moveLine4");
		getObjectTypes().put(moveLine, getTypeIdentifier("polyline"));

		// insert target point into list of graphic objects
		BasicParser.addGraphicObject(moveLine, anim);

		// set the numeric ID of the move target
		int moveBaseNum = moveLine.getNum(false);

		// generate a generic move(instanteous jump)
		String methodName = (pointerNumberGiven) ? "setTip #" + ptrNr : "setTip";
		Move move = new Move(currentStep, oid, 0, methodName, //"setTip #" + ptrNr, 
				moveBaseNum);
		// parse optional timing - is set within the method!
		AnimalParseSupport.parseTiming(stok, move, "List element link");

		// insert into list of animators
		BasicParser.addAnimatorToAnimation(move, anim);
	}

	/**
	 * Create a Move from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseMove() throws IOException {
		String localType = ParseSupport.parseWord(stok, "move subtype");
		int[] targetOIDs = ParseSupport.parseOIDNames(stok, getObjectIDs(), anim
				.getNextGraphicObjectNum());
		int moveBaseNum = -1, i = 0;
		// boolean moveTo = false;
		// boolean moveAlongLine = false;
		boolean moveAlongArc = false;
		boolean moveViaMode = false;

		PTGraphicObject ptgo = null;
		PTGraphicObject moveLine = null;

		// parse the alignment, if any is given
		if (ParseSupport.parseOptionalWord(stok, "Move keyword 'corner'", "corner")) {
			String direction = ParseSupport.parseWord(stok, "move border coordinate")
					.toLowerCase();

			int dir = compass.getIntProperty(direction);

			if (dir == -1) {
				MessageDisplay.errorMsg("Invalid offset direction in line "
						+ stok.lineno() + " changed to 'CENTER'", MessageDisplay.RUN_ERROR);
				dir = 8;
			}
		}

		// check for method definition, if given
		String methodName = AnimalParseSupport.parseMethod(stok, "move type",
				"type", "translate");

		// check for move type, one of 'via', 'along', 'to', 'near'
		// 1. check for type 'via "OID"'

		if (ParseSupport.parseOptionalWord(stok, "Move keyword 'via'", "via")) {
			moveViaMode = true;
			// expect a OID to move along
			String s = AnimalParseSupport.parseText(stok, "OID to move along");
			moveBaseNum = getObjectIDs().getIntProperty(s, -1);
			if (moveBaseNum > -1)
				ptgo = animState.getCloneByNum(moveBaseNum);

			if (moveBaseNum == -1 || ptgo == null || !(ptgo instanceof MoveBase))
				throw new IOException("OID for move path not found in line "
						+ stok.lineno());
		} else {
			// 2. check for type 'along' [line, arc]
			if (ParseSupport.parseOptionalWord(stok, "Move keyword 'along'", "along")) {
				// check if "along" target object is supposed to be an arc
				moveAlongArc = ParseSupport.parseOptionalWord(stok,
						"Move keyword 'along arc'", "arc")
						|| ParseSupport.parseOptionalWord(stok,
								"Move keyword 'along ellipse'", "ellipse")
						|| ParseSupport.parseOptionalWord(stok,
								"Move keyword 'along ellipseseg'", "ellipseseg")
						|| ParseSupport.parseOptionalWord(stok,
								"Move keyword 'along ellipseseg'", "ellipsesegment")
						|| ParseSupport.parseOptionalWord(stok,
								"Move keyword 'along circle'", "circle")
						|| ParseSupport.parseOptionalWord(stok,
								"Move keyword 'along circleseg'", "circleseg")
						|| ParseSupport.parseOptionalWord(stok,
								"Move keyword 'along circlesegment'", "circlesegment");
				String localtype = null;
				if (!moveAlongArc && stok.ttype == '(')
					localType = "line";
				else
					localtype = stok.sval.toLowerCase();
				if (moveAlongArc) {

					// parse three nodes
					int xRadius = 0, yRadius = 0;
					Point center = AnimalParseSupport.parseNodeInfo(stok,
							"first arc move point", null);
					xRadius = ParseSupport.parseInt(stok, localType + " x radius <int>",
							1);
					moveLine = new PTArc();
					((PTArc) moveLine).setCenter(center);
					if (localtype.equals("circle") || localtype.equals("circleseg")) {
						((PTArc) moveLine).setRadius(xRadius);
						((PTArc) moveLine).setCircle(true);
					} else {
						((PTArc) moveLine).setXRadius(xRadius);
						yRadius = ParseSupport
								.parseInt(stok, localType + " y radius <int>");
						((PTArc) moveLine).setYRadius(yRadius);
						((PTArc) moveLine).setCircle(false);
					}
					int startAngle = ParseSupport.parseInt(stok, localType
							+ " start angle <int>");
					((PTArc) moveLine).setStartAngle(startAngle);
					int arcAngle = ParseSupport.parseInt(stok, localType
							+ " end angle <int>");
					((PTArc) moveLine).setTotalAngle(arcAngle);
				} else { // must be along a polyline!

					// parse keyword 'polyline' or 'line' -- optional!
					ParseSupport.parseOptionalWord(stok, "move keyword 'line/polyline'",
							"line");
					ParseSupport.parseOptionalWord(stok, "move keyword 'line/polyline'",
							"polyline");

					// generate an(empty!) polyline
					moveLine = new PTPolyline();

					// parse the nodes of the line!
					while (stok.nextToken() == '(') {
						i++;
						stok.pushBack();
						((PTPolyline) moveLine).addNode(new PTPoint(AnimalParseSupport
								.parseNodeInfo(stok, localType + " node " + i, null)));
					}
					// push back unwanted token(not node paren)
					stok.pushBack();
				}
			} else if (ParseSupport
					.parseOptionalWord(stok, "Move keyword 'to'", "to")) {
				// read in target point
				Point targetPoint = AnimalParseSupport.parseNodeInfo(stok,
						"move destination", null);
				// AnimationState animState = new AnimationState(anim);
				// FIXME touch this!
				animState.setStep(currentStep - 1, false);
				PTGraphicObject targetObject = animState.getCloneByNum(targetOIDs[0]);

				// calculate target coordinates
				Rectangle boundingBox = targetObject.getBoundingBox();
				int[] targetXCoords = { boundingBox.x, targetPoint.x };
				int[] targetYCoords = { boundingBox.y, targetPoint.y };
				int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
				// determine least ULB
				for (i = 0; i < targetOIDs.length; i++) {
					ptgo = animState.getCloneByNum(targetOIDs[i]);
					if (ptgo != null) {
						Rectangle r = ptgo.getBoundingBox();
						if (minX > r.x)
							minX = r.x;
						if (minY > r.y)
							minY = r.y;
					}
					targetXCoords[0] = minX;
					targetYCoords[0] = minY;
				}
				if(targetObject instanceof PTGraph) {
				  PTGraph graph = (PTGraph) targetObject;
				  if(methodName.startsWith("translate #")) {
	          int num = Integer.parseInt(methodName.substring(11));
	          PTCircle circle = graph.getNode(num);
	          targetXCoords[0] = circle.getCenter().x;
	          targetYCoords[0] = circle.getCenter().y;
				  }else if(methodName.startsWith("translateNodes") || methodName.startsWith("translateWithFixedNodes")) {
            targetXCoords[0] = 0;
            targetYCoords[0] = 0;
				  }
				}
        if(targetObject instanceof PTGraph) {
          PTGraph graph = (PTGraph) targetObject;
          if(methodName.startsWith("translate #")) {
            int num = Integer.parseInt(methodName.substring(11));
            PTCircle circle = graph.getNode(num);
            targetXCoords[0] = circle.getCenter().x;
            targetYCoords[0] = circle.getCenter().y;
          }else if(methodName.startsWith("translateNodes") || methodName.startsWith("translateWithFixedNodes")) {
            targetXCoords[0] = 0;
            targetYCoords[0] = 0;
          }
        }
				moveLine = new PTPolyline(targetXCoords, targetYCoords);
			} else // no special method
			{
				Point targetPoint = AnimalParseSupport.parseStartPosition(stok, "move");
				Rectangle currentBB = animState.getCloneByNum(targetOIDs[0])
						.getBoundingBox();
				int[] xCoords = { currentBB.x, targetPoint.x };
				int[] yCoords = { currentBB.y, targetPoint.y };
				moveLine = new PTPolyline(xCoords, yCoords);
			}

			if (!moveViaMode) {
				moveLine.setObjectName("moveLine");
				// insert target point into list of graphic objects
				BasicParser.addGraphicObject(moveLine, anim);

				// set the numeric ID of the move target
				moveBaseNum = moveLine.getNum(false);
			}
			// insert the new(!) node into the object list
			getObjectIDs().put("tmpPoint" + moveBaseNum, moveBaseNum);
			getObjectTypes().put("tmpPoint" + moveBaseNum,
					getTypeIdentifier("polyline"));
		}

		// generate a generic move(instanteous jump)
		Move move = new Move(currentStep, targetOIDs, 0, methodName, moveBaseNum);

		// parse optional timing - is set within the method!
		AnimalParseSupport.parseTiming(stok, move, "Move");

		// if "jump", erase possible mention of 'duration'
		if (localType.equalsIgnoreCase("jump"))
			if (move.getDuration() != 0) {
				MessageDisplay.message("'jump' can not have a duration - use 'move'"
						+ " instead in line " + stok.lineno());
				move.setDuration(0);
			}

		// insert into list of animators
		BasicParser.addAnimatorToAnimation(move, anim);
	}

	/**
	 * Create a Rotation from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseRotate() throws IOException {
		// parse exact command
		String rotationType = ParseSupport.parseWord(stok, "rotation operation");
		int centerID = 0, degrees = 360;
		// parse the objects concerned
		int[] targetOIDs = ParseSupport.parseOIDNames(stok, getObjectIDs(), anim
				.getNextGraphicObjectNum());

		if (ParseSupport.parseOptionalWord(stok,
				rotationType + " keyword 'around'", "around")) {
			String centerPointName = AnimalParseSupport.parseText(stok, rotationType
					+ " center point");

			// retrieve the numeric object IDs
			centerID = getObjectIDs().getIntProperty(centerPointName, -1);
			if (centerID == -1
					|| !(animState.getCloneByNum(centerID) instanceof PTPoint))
				ParseSupport.formatException("Rotation center point '"
						+ centerPointName + "' "
						+ ((centerID == -1) ? "does not exist" : "is no point"), stok);
		} else if (ParseSupport.parseOptionalWord(stok, rotationType
				+ " keyword 'center'", "center")) {
			PTPoint p = new PTPoint(AnimalParseSupport.parseNodeInfo(stok,
					rotationType + " center node", null));
			BasicParser.addGraphicObject(p, anim);
			centerID = p.getNum(false);
		} else
			ParseSupport.formatException("Unknow command for rotation.", stok);

		if (ParseSupport.parseOptionalWord(stok, rotationType
				+ " keyword 'degrees'", "degrees"))
			degrees = ParseSupport.parseInt(stok, rotationType + " degrees");

		// generate a generic color changer move(instanteous change)
		Rotate rotation = new Rotate(currentStep, targetOIDs, 0, centerID, degrees);
		// parse optional timing - is set within the method!
		AnimalParseSupport.parseTiming(stok, rotation, rotationType);

		// insert into list of animators
		BasicParser.addAnimatorToAnimation(rotation, anim);
	}

	/**
	 * Show or hide the selected object IDs, possibly with delay
	 * 
	 * The description is usually generated by other programs and dumped in a file
	 * or on System.out.
	 */
	public void parseShowHide() throws IOException {
		// Read in the command
		String localType = ParseSupport.parseWord(stok, "show/hide type");
		boolean isShow = localType.equalsIgnoreCase("show");
		int[] ids = null;
		if (localType.equalsIgnoreCase("hideAll")) {
			StringBuilder sb = new StringBuilder(300);
			Enumeration<String> e = getCurrentlyVisible().keys();
			while (e.hasMoreElements()) {
				String value = e.nextElement();
				//System.out.println(value);
				if ("true".equalsIgnoreCase(getCurrentlyVisible().get(value)))
					sb.append(' ').append(value);
			}
			ids = ParseSupport.parseOIDsFromString(sb.toString());
			sb = null;
		} else
			ids = ParseSupport.parseOIDNames(stok, getObjectIDs(), anim
					.getNextGraphicObjectNum());
		if (localType.equalsIgnoreCase("hideAllBut")) {
			StringBuilder sb = new StringBuilder(300);
			Enumeration<String> e = getCurrentlyVisible().keys();
			while (e.hasMoreElements()) {
				String value = e.nextElement();
				int elemNr = new Integer(value).intValue();
				if ("true".equalsIgnoreCase(getCurrentlyVisible().get(value))
						&& !includedIn(elemNr, ids))
					sb.append(' ').append(value);
			}
			ids = ParseSupport.parseOIDsFromString(sb.toString());
			sb = null;
		}

		// check for method definition, if given
		String methodName = AnimalParseSupport.parseMethod(stok, localType +" type",
				"type", localType.toLowerCase());
		if (localType.equalsIgnoreCase("hideAll") && localType.equalsIgnoreCase(methodName))
			methodName = "hide";
		if (localType.equalsIgnoreCase("hideAllBut") && methodName.equalsIgnoreCase("hideAllBut"))
			methodName = "hide";
		
		// check for keyword 'after'
		if (ParseSupport.parseOptionalWord(stok, localType + " 'after' keyword",
				"after")) {
			// push back the token for the parsing method!
			stok.pushBack();

			// generate the TimedShow animator
			TimedShow ts = new TimedShow(currentStep, ids, 0,
					methodName, isShow);
//					(isShow) ? "show" : "hide", isShow);

			// now parse the timing information
			AnimalParseSupport.parseTiming(stok, ts, "Timed Show");

			// insert the animator
			// anim.insertAnimator(ts);
			BasicParser.addAnimatorToAnimation(ts, anim);
		} else {
			// insert a 'normal', i.e. untimed, show
			TimedShow showAnimator = new TimedShow(currentStep, ids, 0,
					methodName, isShow);
//					(isShow) ? "show" : "hide", isShow);
			BasicParser.addAnimatorToAnimation(showAnimator, anim);
		}
		for (int i = 0; i < ids.length; i++)
			getCurrentlyVisible().put(String.valueOf(ids[i]), String.valueOf(isShow));
	}

//	public void parseSetText() throws IOException {
//		// Read in the command
//		String localType = ParseSupport.parseWord(stok, "setText");
//		int[] targetOIDs = ParseSupport.parseOIDNames(stok, getObjectIDs(), anim
//				.getNextGraphicObjectNum());
//		// parse new text
//		// parse the text component
//		String text = AnimalParseSupport.parseText(stok, "Text for setText", 
//				"text", false, chosenLanguage);
//		
//		SetText setText = new SetText()
//	}
	
	
	public boolean includedIn(int value, int[] array) {
		if (array == null || array.length == 0)
			return false;
		int i = 0;
		while (i < array.length && value != array[i])
			i++;
		return (i < array.length);
	}
}
