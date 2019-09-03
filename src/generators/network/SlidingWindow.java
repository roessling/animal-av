/*
 * SlidingWindow.java
 * Florian Hopp, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

class Action {
	enum Type {
		FRAME, ACKNOWLEDGEMENT;
	}

	public Action() {
		this.type = Type.FRAME;
		this.timestamp = 0;
		this.seqNo = -1;
		this.resent = false;
	}

	public Action(Type type, int timestamp, int seqNo, boolean resent) {
		this.type = type;
		this.timestamp = timestamp;
		this.seqNo = seqNo;
		this.resent = resent;
	}

	public String typeToString(Type type) {
		switch (type) {
		case FRAME:
			return "F";
		case ACKNOWLEDGEMENT:
			return "ACK";
		default:
			return "Error";
		}
	}

	public String actionToString() {
		return typeToString(this.type) + Integer.toString(this.seqNo);
	}

	public Type type;
	public int timestamp;
	public int seqNo;
	public boolean resent;
}

public class SlidingWindow implements ValidatingGenerator {
	private Language lang;
	private Translator translator;
	private Locale language;
	private ArrayMarkerProperties ReceiverUpperBound;
	private int[] errorsSender;
	private MatrixProperties WindowAndActions;
	private ArrayMarkerProperties SenderLowerBound;
	private int framesToSent;
	private int timeToTransmitSender;
	private int[] errorsReceiver;
	private int windowSizeSender;
	private ArrayProperties SenderWindow;
	private ArrayMarkerProperties ReceiverLowerBound;
	private int sequenceNumbersSender;
	private int timeoutSender;
	private int timeoutReceiver;
	private int sequenceNumbersReceiver;
	private TextProperties text;
	private ArrayProperties ReceiverWindow;
	private int windowSizeReceiver;
	private ArrayMarkerProperties SenderUpperBound;
	private int timeToTransmitReceiver;
	private SourceCodeProperties sourceCode;

	public SlidingWindow(Locale l) {
		translator = new Translator("resources/SlidingWindow", l);
		language = l;
	} // 22

	public void init() {
		lang = new AnimalScript("Sliding Window Protocol", "Florian Hopp", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		ReceiverUpperBound = (ArrayMarkerProperties) props.getPropertiesByName("ReceiverUpperBound");
		errorsSender = (int[]) primitives.get("errorsSender");
		WindowAndActions = (MatrixProperties) props.getPropertiesByName("WindowAndActions");
		SenderLowerBound = (ArrayMarkerProperties) props.getPropertiesByName("SenderLowerBound");
		framesToSent = (Integer) primitives.get("framesToSent");
		timeToTransmitSender = (Integer) primitives.get("timeToTransmitSender");
		errorsReceiver = (int[]) primitives.get("errorsReceiver");
		windowSizeSender = (Integer) primitives.get("windowSizeSender");
		SenderWindow = (ArrayProperties) props.getPropertiesByName("SenderWindow");
		ReceiverLowerBound = (ArrayMarkerProperties) props.getPropertiesByName("ReceiverLowerBound");
		sequenceNumbersSender = (Integer) primitives.get("sequenceNumbersSender");
		timeoutSender = (Integer) primitives.get("timeoutSender");
		timeoutReceiver = (Integer) primitives.get("timeoutReceiver");
		sequenceNumbersReceiver = (Integer) primitives.get("sequenceNumbersReceiver");
		text = (TextProperties) props.getPropertiesByName("text");
		ReceiverWindow = (ArrayProperties) props.getPropertiesByName("ReceiverWindow");
		windowSizeReceiver = (Integer) primitives.get("windowSizeReceiver");
		SenderUpperBound = (ArrayMarkerProperties) props.getPropertiesByName("SenderUpperBound");
		timeToTransmitReceiver = (Integer) primitives.get("timeToTransmitReceiver");
		sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");

		Set<Integer> errorsS = new HashSet<Integer>();
		for (int e : errorsSender) {
			errorsS.add(e);
		}
		Set<Integer> errorsR = new HashSet<Integer>();
		for (int e : errorsReceiver) {
			errorsR.add(e);
		}
		solve(windowSizeSender, windowSizeReceiver, framesToSent, sequenceNumbersSender, sequenceNumbersReceiver,
				timeToTransmitSender, timeToTransmitReceiver, errorsS, errorsR, timeoutSender, timeoutReceiver);

		return lang.toString();
	}

	/**
	 * default duration for swap processes
	 */
	public final static Timing defaultDuration = new TicksTiming(30);

	public int getFreeTimeSlot(Set<Integer> timeSlots, int firstPossibleSlot) {
		int freeTimeSlot = firstPossibleSlot;
		while (timeSlots.contains(freeTimeSlot)) {
			freeTimeSlot++;
		}
		return freeTimeSlot;
	}

	public int getNextAck(Set<Integer> alreadyKnown, int min) {
		boolean minFound = false;
		int nextAck = 0;
		for (int i : alreadyKnown) {
			if (i == min) {
				minFound = true;
				nextAck = min;
			}
			if (minFound && nextAck + 1 == i) {
				nextAck++;
			}
		}
		return nextAck;
	}

	/**
	 * counter for the number of pointers
	 * 
	 */
	private int pointerCounter = 0;

  @SuppressWarnings("unused")
  public void solve(int windowSizeSender, int windowSizeReceiver, int framesToSent, int sequenceNumbersSender,
			int sequenceNumbersReceiver, int timeToTransmitSender, int timeToTransmitReceiver, Set<Integer> errorsSender,
			Set<Integer> errorsReceiver, int timeoutSender, int timeoutReceiver) {
		// Vectors for every column
		Vector<Action> actionsS = new Vector<Action>();
		Vector<Action> actionsR = new Vector<Action>();
		Vector<String> times = new Vector<String>();
		Vector<String> bSender = new Vector<String>();
		Vector<String> lSender = new Vector<String>();
		Vector<String> uSender = new Vector<String>();
		Vector<String> actionsSender = new Vector<String>();
		Vector<Integer> actionsSenderA = new Vector<Integer>();
		Vector<String> actionsReceiver = new Vector<String>();
		Vector<Integer> actionsReceiverA = new Vector<Integer>();
		Vector<String> bReceiver = new Vector<String>();
		Vector<String> lReceiver = new Vector<String>();
		Vector<String> uReceiver = new Vector<String>();

		// Variables for Sender
		int actualTime = 0;
		int frameCounter = 0;
		int lBS = 0;
		int uBS = 0;
		int bSS = 0;
		Set<Integer> alreadySent = new HashSet<Integer>();
		Set<Integer> alreadyReceivedS = new HashSet<Integer>();
		Set<Integer> timeSlotsSender = new HashSet<Integer>();
		// seqnumber to timeout
		Map<Integer, Integer> timeoutsSender = new HashMap<Integer, Integer>();
		// Variables for Receiver
		int lBR = 0;
		int uBR = windowSizeReceiver - 1;
		int bSR = 0;
		Set<Integer> alreadyReceivedR = new HashSet<Integer>();
		Set<Integer> timeSlotsReceiver = new HashSet<Integer>();
		// seqnumber to timeout
		Map<Integer, Integer> timeoutsReceiver = new HashMap<Integer, Integer>();

		// initialize
		actionsS.add(new Action(Action.Type.FRAME, actualTime, frameCounter, false));
		frameCounter++;
		timeSlotsSender.add(0);
		boolean end = false;

		// solve problem
		while (!end) {
			// check for timeouts sender side
			if (timeoutsSender.containsValue(actualTime)) {
				for (Map.Entry<Integer, Integer> entry : timeoutsSender.entrySet()) {
					if (entry.getValue() == actualTime) {
						int freeslot = getFreeTimeSlot(timeSlotsSender, actualTime);
						actionsS.add(new Action(Action.Type.FRAME, freeslot, entry.getKey(), true));
						timeSlotsSender.add(freeslot);
					}
				}
			}

			// check for timeouts receiver side
			if (timeoutsReceiver.containsValue(actualTime)) {
				for (Map.Entry<Integer, Integer> entry : timeoutsReceiver.entrySet()) {
					if (entry.getValue() == actualTime) {
						int freeslot = getFreeTimeSlot(timeSlotsReceiver, actualTime);
						actionsR.add(new Action(Action.Type.ACKNOWLEDGEMENT, freeslot, entry.getKey(), true));
						timeSlotsReceiver.add(freeslot);
					}
				}
			}

			// if allowed send as many Frames as possible
			if (frameCounter < framesToSent && !timeSlotsSender.contains(actualTime) && bSS > 0) {
				actionsS.add(new Action(Action.Type.FRAME, actualTime, frameCounter, false));
				timeSlotsSender.add(actualTime);
				frameCounter++;
			}

			// Sender
			String senderActions = "";
			Vector<Action> actionsToRemoveS = new Vector<Action>();
			boolean actionAtThisTime = false;
			for (int i = 0; i < actionsS.size(); ++i) {
				Action a = actionsS.elementAt(i);
				if (actualTime == a.timestamp) {
					actionAtThisTime = true;
					switch (a.type) {
					case FRAME:
						if (uBS == a.seqNo && !alreadySent.contains(a.seqNo)) {
							senderActions = a.actionToString();
							uBS = Math.floorMod(uBS + 1, sequenceNumbersSender);
							alreadySent.add(a.seqNo);
							timeoutsSender.put(a.seqNo, actualTime + timeoutSender);
							actionsSenderA.add(1);
							if (errorsSender.contains(actualTime)) {
								senderActions = senderActions + "(error)";
							} else {
								int timeSlot = getFreeTimeSlot(timeSlotsReceiver, actualTime + timeToTransmitSender);
								actionsR.add(new Action(Action.Type.FRAME, timeSlot, a.seqNo, false));
								timeSlotsReceiver.add(timeSlot);
							}
							actionsToRemoveS.add(a);
						}
						if (a.resent) {
							actionsSenderA.add(2);
							senderActions = a.actionToString() + "(retransmit)";
							int timeSlot = getFreeTimeSlot(timeSlotsReceiver, actualTime + timeToTransmitSender);
							timeoutsSender.put(a.seqNo, actualTime + timeoutSender);
							actionsR.add(new Action(Action.Type.FRAME, timeSlot, a.seqNo, false));
							timeSlotsReceiver.add(timeSlot);
							actionsToRemoveS.add(a);
						}
						break;
					case ACKNOWLEDGEMENT:
						if (alreadyReceivedS.contains(a.seqNo)) {
							actionsSenderA.add(3);
							senderActions = a.actionToString() + "(duplicate)";
						} else {
							actionsSenderA.add(4);
							senderActions = a.actionToString();
							for (int j = lBS; j <= a.seqNo; ++j) {
								alreadyReceivedS.add(j);
								timeoutsSender.remove(j);
							}
							for (Action aRemove : actionsS) {
								if (aRemove.seqNo < a.seqNo) {
									actionsToRemoveS.add(aRemove);
								}
							}
							lBS = Math.floorMod(a.seqNo + 1, sequenceNumbersSender);
							actionsToRemoveS.add(a);

						}
						if (a.seqNo == framesToSent - 1) {
							end = true;
						}
						break;
					default:
						System.out.print("Unknown Type\n");
						break;
					}
				}
			}
			for (int i = 0; i < actionsToRemoveS.size(); ++i) {
				actionsS.remove(actionsToRemoveS.elementAt(i));
			}
			bSS = Math.floorMod(windowSizeSender - uBS + lBS, sequenceNumbersSender);

			if (!actionAtThisTime) {
				actionsSenderA.add(-1);
			}
			bSender.add(Integer.toString(bSS));
			lSender.add(Integer.toString(lBS));
			uSender.add(Integer.toString(uBS));
			actionsSender.add(senderActions);

			// Receiver
			String receiverActions = "";
			Vector<Action> actionsToRemoveR = new Vector<Action>();
			actionAtThisTime = false;
			for (int i = 0; i < actionsR.size(); ++i) {
				Action a = actionsR.elementAt(i);
				if (actualTime == a.timestamp) {
					actionAtThisTime = true;
					switch (a.type) {
					case FRAME:
						if (lBR < a.seqNo && a.seqNo <= uBR && !alreadyReceivedR.contains(a.seqNo)) {
							actionsReceiverA.add(1);
							receiverActions = a.actionToString();
							alreadyReceivedR.add(a.seqNo);
							actionsToRemoveR.add(a);
						} else if (lBR == a.seqNo && !alreadyReceivedR.contains(a.seqNo)) {
							actionsReceiverA.add(2);
							receiverActions = a.actionToString();
							lBR = Math.floorMod(lBR + 1, sequenceNumbersReceiver);
							alreadyReceivedR.add(a.seqNo);
							int nextAck = getNextAck(alreadyReceivedR, a.seqNo);
							lBR = nextAck + 1;
							boolean useOtherAck = false;
							for (Action aR : actionsR) {
								if (aR.type == Action.Type.ACKNOWLEDGEMENT && aR.seqNo < a.seqNo && aR.timestamp >= actualTime + 1) {
									aR.seqNo = nextAck;
									aR.resent = false;
									useOtherAck = true;
								}
							}
							if (!useOtherAck) {
								int timeSlot = getFreeTimeSlot(timeSlotsReceiver, actualTime + 1);
								actionsR.add(new Action(Action.Type.ACKNOWLEDGEMENT, timeSlot, nextAck, false));
								timeSlotsReceiver.add(timeSlot);
							}
							actionsToRemoveR.add(a);
						} else if (alreadyReceivedR.contains(a.seqNo)) {
							actionsReceiverA.add(3);
							receiverActions = a.actionToString() + "(duplicate)";
						}
						break;
					case ACKNOWLEDGEMENT:
						receiverActions = a.actionToString();
						if (a.resent) {
							actionsReceiverA.add(4);
							receiverActions = receiverActions + "(retransmit)";
							int timeSlot = getFreeTimeSlot(timeSlotsSender, actualTime + timeToTransmitReceiver);
							actionsS.add(new Action(Action.Type.ACKNOWLEDGEMENT, timeSlot, a.seqNo, false));
							timeSlotsSender.add(timeSlot);
							timeoutsReceiver.put(a.seqNo, actualTime + timeoutReceiver);
						} else {
							actionsReceiverA.add(5);
							timeoutsReceiver.put(a.seqNo, actualTime + timeoutReceiver);
							if (errorsReceiver.contains(actualTime)) {
								receiverActions = receiverActions + "(error)";
							} else {
								uBR = Math.floorMod(a.seqNo + windowSizeReceiver, sequenceNumbersReceiver);
								int timeSlot = getFreeTimeSlot(timeSlotsSender, actualTime + timeToTransmitReceiver);
								actionsS.add(new Action(Action.Type.ACKNOWLEDGEMENT, timeSlot, a.seqNo, false));
								timeSlotsSender.add(timeSlot);
							}
						}
						actionsToRemoveR.add(a);
						break;
					default:
						System.out.print("Unknown Type\n");
						break;
					}
				}
			}
			for (int i = 0; i < actionsToRemoveR.size(); ++i) {
				actionsR.remove(actionsToRemoveR.elementAt(i));
			}
			bSR = Math.floorMod(uBR - lBR + 1, sequenceNumbersReceiver);

			if (!actionAtThisTime) {
				actionsReceiverA.add(-1);
			}
			bReceiver.add(Integer.toString(bSR));
			lReceiver.add(Integer.toString(lBR));
			uReceiver.add(Integer.toString(uBR));
			actionsReceiver.add(receiverActions);
			times.add(Integer.toString(actualTime));
			actualTime++;

		}

		// Visualization
		TextProperties textP = new TextProperties();
		textP.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) text.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName(), Font.BOLD, 24));
		Text title = lang.newText(new Coordinates(50, 10), translator.translateMessage("title"), "textpanel2", null, textP);
		TextProperties textP1 = new TextProperties();
		textP1.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) text.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName(),
						((Font) text.get(AnimationPropertiesKeys.FONT_PROPERTY)).getStyle(), 16));
		Text description = lang.newText(new Offset(0, 20, "textpanel2", "SW"), translator.translateMessage("des") + " :",
				"textpanel8", null, textP1);
		Text description1 = lang.newText(new Offset(0, 20, "textpanel8", "SW"), translator.translateMessage("description"),
				"textpanel9", null, text);
		Text description2 = lang.newText(new Offset(0, 5, "textpanel9", "SW"), translator.translateMessage("description1"),
				"textpanel10", null, text);
		Text description3 = lang.newText(new Offset(0, 5, "textpanel10", "SW"), translator.translateMessage("description2"),
				"textpanel11", null, text);
		Text pseudo = lang.newText(new Offset(0, 20, "textpanel11", "SW"), translator.translateMessage("ps") + " :",
				"textpanel12", null, textP1);
		Text pseudo1 = lang.newText(new Offset(0, 20, "textpanel12", "SW"), translator.translateMessage("pseudo"),
				"textpanel13", null, text);
		Text pseudo2 = lang.newText(new Offset(0, 10, "textpanel13", "SW"), translator.translateMessage("pseudo1"),
				"textpanel14", null, text);
		Text pseudo3 = lang.newText(new Offset(0, 5, "textpanel14", "SW"), translator.translateMessage("pseudo2"),
				"textpanel15", null, text);
		Text pseudo4 = lang.newText(new Offset(0, 5, "textpanel15", "SW"), translator.translateMessage("pseudo3"),
				"textpanel16", null, text);
		Text pseudo5 = lang.newText(new Offset(0, 5, "textpanel16", "SW"), translator.translateMessage("pseudo4"),
				"textpanel17", null, text);
		Text pseudo6 = lang.newText(new Offset(0, 5, "textpanel17", "SW"), translator.translateMessage("pseudo5"),
				"textpanel18", null, text);
		Text pseudo7 = lang.newText(new Offset(0, 20, "textpanel18", "SW"), translator.translateMessage("pseudo6"),
				"textpanel19", null, text);
		Text pseudo8 = lang.newText(new Offset(0, 10, "textpanel19", "SW"), translator.translateMessage("pseudo7"),
				"textpanel20", null, text);
		Text pseudo9 = lang.newText(new Offset(0, 5, "textpanel20", "SW"), translator.translateMessage("pseudo8"),
				"textpanel21", null, text);
		Text pseudo10 = lang.newText(new Offset(0, 5, "textpanel21", "SW"), translator.translateMessage("pseudo9"),
				"textpanel22", null, text);
		Text pseudo11 = lang.newText(new Offset(0, 5, "textpanel22", "SW"), translator.translateMessage("pseudo10"),
				"textpanel23", null, text);
		lang.nextStep(translator.translateMessage("intro"));
		description.hide();
		description1.hide();
		description2.hide();
		description3.hide();
		pseudo.hide();
		pseudo1.hide();
		pseudo2.hide();
		pseudo3.hide();
		pseudo4.hide();
		pseudo5.hide();
		pseudo6.hide();
		pseudo7.hide();
		pseudo8.hide();
		pseudo9.hide();
		pseudo10.hide();
		pseudo11.hide();

		// Sender/Receiver Buffer Visualization
		int[] allNumbersS = new int[sequenceNumbersSender + 1];
		for (int i = 0; i <= sequenceNumbersSender; ++i) {
			allNumbersS[i] = i;
		}
		int[] allNumbersR = new int[sequenceNumbersReceiver + 1];
		for (int i = 0; i <= sequenceNumbersReceiver; ++i) {
			allNumbersR[i] = i;
		}
		Text txtSender = lang.newText(new Offset(0, 70, "textpanel2", "SW"), translator.translateMessage("sender"),
				"textpanel", null, text);
		IntArray sender = lang.newIntArray(new Offset(10, 0, "textpanel", "NE"), allNumbersS, "intArray", null,
				SenderWindow);
		Text txtReceiver = lang.newText(new Offset(10, 0, "intArray", "NE"), translator.translateMessage("receiver"),
				"textpanel1", null, text);
		IntArray receiver = lang.newIntArray(new Offset(10, 0, "textpanel1", "NE"), allNumbersR, "intArray1", null,
				ReceiverWindow);

		ArrayMarker lSMarker = lang.newArrayMarker(sender, 0, "l" + pointerCounter, null, SenderLowerBound);
		pointerCounter++;
		ArrayMarker uSMarker = lang.newArrayMarker(sender, 0, "u" + pointerCounter, null, SenderUpperBound);
		pointerCounter++;
		ArrayMarker lRMarker = lang.newArrayMarker(receiver, 0, "l" + pointerCounter, null, ReceiverLowerBound);
		pointerCounter++;
		ArrayMarker uRMarker = lang.newArrayMarker(receiver, 0, "u" + pointerCounter, null, ReceiverUpperBound);

		// create the StringMatrix object
		// Time B(Sender) L(Sender) U(Sender) Actions(Sender) Action(Receiver)
		// L(Receiver) U(Receiver) B(Receiver)
		String[][] solution = new String[times.size() + 1][9];
		solution[0][0] = "      " + translator.translateMessage("time") + "      ";
		solution[0][1] = "   B(" + translator.translateMessage("sender") + ")    ";
		solution[0][2] = "   L(" + translator.translateMessage("sender") + ")    ";
		solution[0][3] = "   U(" + translator.translateMessage("sender") + ")    ";
		solution[0][4] = "Actions(" + translator.translateMessage("sender") + ") ";
		solution[0][5] = "Action(" + translator.translateMessage("receiver") + ")";
		solution[0][6] = "  L(" + translator.translateMessage("receiver") + ")   ";
		solution[0][7] = "  U(" + translator.translateMessage("receiver") + ")   ";
		solution[0][8] = "  B(" + translator.translateMessage("receiver") + ")   ";
		for (int i = 1; i < times.size() + 1; ++i) {
			for (int j = 0; j < 9; ++j) {
				if (j == 0) {
					solution[i][j] = Integer.toString(i - 1);
				} else {
					solution[i][j] = "";
				}

			}
		}
		StringMatrix m = lang.newStringMatrix(new Offset(0, 40, "textpanel", "SW"), solution, "stringMatrix", null,
				WindowAndActions);

		// additional information
		Text infos = lang.newText(new Offset(10, 10, "stringMatrix", "NE"), "U = " + translator.translateMessage("upper"),
				"textpanel3", null, text);
		Text infos1 = lang.newText(new Offset(0, 5, "textpanel3", "SW"), "L = " + translator.translateMessage("lower"),
				"textpanel4", null, text);
		Text infos2 = lang.newText(new Offset(0, 5, "textpanel4", "SW"), "B = " + translator.translateMessage("buffer"),
				"textpanel5", null, text);
		Text infos3 = lang.newText(new Offset(0, 5, "textpanel5", "SW"), "F = " + translator.translateMessage("frame"),
				"textpanel6", null, text);
		Text infos4 = lang.newText(new Offset(0, 5, "textpanel6", "SW"), "ACK =" + translator.translateMessage("ack"),
				"textpanel7", null, text);
		Text infos5 = lang.newText(new Offset(0, 5, "textpanel7", "SW"), "error =" + translator.translateMessage("error"),
				"textpanel24", null, text);

		TextProperties textP2 = new TextProperties();
		textP2.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) text.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName(), Font.BOLD, 14));
		Text infos6 = lang.newText(new Offset(0, 30, "textpanel24", "SW"),
				translator.translateMessage("title") + " " + translator.translateMessage("ps"), "textpanel25", null, textP2);

		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Offset(0, 5, "textpanel25", "SW"), "sourceCode", null, sourceCode);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine(translator.translateMessage("sender")+":", null, 0, null);
		sc.addCodeLine("if(B > 0)", null, 1, null);
		sc.addCodeLine("{", null, 1, null);
		sc.addCodeLine(translator.translateMessage("sourcecode"), null, 2, null);
		sc.addCodeLine(translator.translateMessage("sourcecode1"), null, 2, null);
		sc.addCodeLine(translator.translateMessage("sourcecode2"), null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine(translator.translateMessage("sourcecode3"), null, 1, null);
		sc.addCodeLine("{", null, 1, null);
		sc.addCodeLine(translator.translateMessage("sourcecode4"), null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine(translator.translateMessage("sourcecode5"), null, 1, null);
		sc.addCodeLine("{", null, 1, null);
		sc.addCodeLine(translator.translateMessage("sourcecode6"), null, 2, null);
		sc.addCodeLine("{", null, 2, null);
		sc.addCodeLine(translator.translateMessage("sourcecode7"), null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine(translator.translateMessage("sourcecode8"), null, 1, null);

		// now, create the source code entity
		SourceCode scR = lang.newSourceCode(new Offset(10, -13, "sourceCode", "NE"), "sourceCode1", null, sourceCode);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		scR.addCodeLine(translator.translateMessage("receiver")+":", null, 0, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR"), null, 1, null);
		scR.addCodeLine("{", null, 1, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR1"), null, 2, null);
		scR.addCodeLine("{", null, 2, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR2"), null, 3, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR3"), null, 3, null);
		scR.addCodeLine("}", null, 2, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR4"), null, 2, null);
		scR.addCodeLine("{", null, 2, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR5"), null, 3, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR6"), null, 3,
				null);
		scR.addCodeLine("}", null, 2, null);
		scR.addCodeLine("}", null, 1, null);
		scR.addCodeLine(translator.translateMessage("sourcecode3"), null, 1, null);
		scR.addCodeLine("{", null, 1, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR7"), null, 2, null);
		scR.addCodeLine("}", null, 1, null);
		scR.addCodeLine("else", null, 1, null);
		scR.addCodeLine("{", null, 1, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR8"), null, 2, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR9"), null, 2, null);
		scR.addCodeLine("}", null, 1, null);
		scR.addCodeLine(translator.translateMessage("sourcecodeR10"), null, 1, null);

		lang.nextStep();

		// step by step Visualization of the matrix
		int ackReceived = 1;
		for (int i = 0; i < times.size(); ++i) {
			int time = Integer.parseInt(times.elementAt(i)) + 1;
			m.put(time, 1, bSender.elementAt(i), null, null);
			m.put(time, 2, lSender.elementAt(i), null, null);
			m.put(time, 3, uSender.elementAt(i), null, null);
			m.put(time, 4, actionsSender.elementAt(i), null, null);
			if (actionsSenderA.elementAt(i) == 1) {
				sc.highlight(1, 0, false);
				sc.highlight(3, 0, false);
				sc.highlight(4, 0, false);
				sc.highlight(5, 0, false);
				sc.highlight(18, 0, false);
			}
			if (actionsSenderA.elementAt(i) == 2) {
				sc.highlight(7, 0, false);
				sc.highlight(9, 0, false);
			}
			if (actionsSenderA.elementAt(i) == 4) {
				sc.highlight(11, 0, false);
				sc.highlight(13, 0, false);
				sc.highlight(15, 0, false);
				sc.highlight(18, 0, false);
			}
			if (actionsSenderA.elementAt(i) == 3) {
				sc.highlight(11, 0, false);
			}
			m.highlightCell(time, 1, null, null);
			m.highlightCell(time, 2, null, null);
			m.highlightCell(time, 3, null, null);
			lSMarker.move(Integer.parseInt(lSender.elementAt(i)), null, defaultDuration);
			uSMarker.move(Integer.parseInt(bSender.elementAt(i)), null, defaultDuration);
			if (actionsSenderA.elementAt(i) == 4) {
				lang.nextStep("ACK" + ackReceived + " " + translator.translateMessage("received"));
				ackReceived++;
			} else {
				lang.nextStep();
			}
			for (int j = 0; j < 19; ++j) {
				sc.unhighlight(j, 0, false);
			}
			sc.unhighlight(0, 0, false);
			m.unhighlightCell(time, 1, null, null);
			m.unhighlightCell(time, 2, null, null);
			m.unhighlightCell(time, 3, null, null);

			m.put(time, 6, lReceiver.elementAt(i), null, null);
			m.put(time, 7, uReceiver.elementAt(i), null, null);
			m.put(time, 8, bReceiver.elementAt(i), null, null);
			m.put(time, 5, actionsReceiver.elementAt(i), null, null);
			m.highlightCell(time, 6, null, null);
			m.highlightCell(time, 7, null, null);
			m.highlightCell(time, 8, null, null);
			if (actionsReceiverA.elementAt(i) == 1) {
				scR.highlight(1, 0, false);
				scR.highlight(3, 0, false);
				scR.highlight(5, 0, false);
				scR.highlight(6, 0, false);
			}
			if (actionsReceiverA.elementAt(i) == 2) {
				scR.highlight(1, 0, false);
				scR.highlight(8, 0, false);
				scR.highlight(10, 0, false);
				scR.highlight(11, 0, false);
				scR.highlight(23, 0, false);
			}
			if (actionsReceiverA.elementAt(i) == 3) {
				scR.highlight(1, 0, false);
			}
			if (actionsReceiverA.elementAt(i) == 4) {
				scR.highlight(14, 0, false);
				scR.highlight(16, 0, false);
			}
			if (actionsReceiverA.elementAt(i) == 5) {
				scR.highlight(18, 0, false);
				scR.highlight(20, 0, false);
				scR.highlight(21, 0, false);
				scR.highlight(23, 0, false);
			}
			lRMarker.move(Integer.parseInt(lReceiver.elementAt(i)), null, defaultDuration);
			uRMarker.move(Integer.parseInt(uReceiver.elementAt(i)), null, defaultDuration);
			lang.nextStep();
			for (int j = 0; j < 24; ++j) {
				scR.unhighlight(j, 0, false);
			}
			m.unhighlightCell(time, 6, null, null);
			m.unhighlightCell(time, 7, null, null);
			m.unhighlightCell(time, 8, null, null);
		}
		lang.nextStep(translator.translateMessage("conclusion"));
		Text conclusion = lang.newText(new Offset(0, 30, "stringMatrix", "SW"), translator.translateMessage("conclusion1")
				+ " " + times.size() + " " + translator.translateMessage("conclusion2"), "conclusion", null, text);
	}

	public String getName() {
		return "Sliding Window Protocol";
	}

	public String getAlgorithmName() {
		return "Sliding Window Protocol";
	}

	public String getAnimationAuthor() {
		return "Florian Hopp";
	}

	public String getDescription() {
		return translator.translateMessage("description") + "\n" + translator.translateMessage("description1") + "\n"
				+ translator.translateMessage("description2") + "\n";
	}

	public String getCodeExample() {
		return translator.translateMessage("pseudo") + "\n" + translator.translateMessage("pseudo1") + "\n"
				+ translator.translateMessage("pseudo2") + "\n" + translator.translateMessage("pseudo3") + "\n    "
				+ translator.translateMessage("pseudo4") + "\n" + translator.translateMessage("pseudo5") + "\n\n"
				+ translator.translateMessage("pseudo6") + "\n" + translator.translateMessage("pseudo7") + "\n"
				+ translator.translateMessage("pseudo8") + "\n" + translator.translateMessage("pseudo9") + "\n"
				+ translator.translateMessage("pseudo10") + "\n";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return language;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		if((Integer)primitives.get("timeToTransmitSender") <= 0){
			throw new IllegalArgumentException("timeToTransmitSender "+translator.translateMessage("validate")+ " 0");
		}
		if((Integer)primitives.get("timeToTransmitReceiver") <= 0){
			throw new IllegalArgumentException("timeToTransmitReceiver "+translator.translateMessage("validate")+ " 0");
		}
		if((Integer)primitives.get("windowSizeReceiver") <= 0){
			throw new IllegalArgumentException("windowSizeReceiver "+translator.translateMessage("validate")+ " 0");
		}
		if((Integer)primitives.get("sequenceNumbersReceiver") <= 0){
			throw new IllegalArgumentException("sequenceNumbersReceiver "+translator.translateMessage("validate")+ " 0");
		}
		if((Integer)primitives.get("sequenceNumbersReceiver") <= (Integer)primitives.get("windowSizeReceiver")){
			throw new IllegalArgumentException("sequenceNumbersReceiver "+translator.translateMessage("validate")+ " windowSizeReceiver");
		}
		if((Integer)primitives.get("windowSizeSender") <= 0){
			throw new IllegalArgumentException("windowSizeSender "+translator.translateMessage("validate")+ " 0");
		}
		if((Integer)primitives.get("sequenceNumbersSender") <= 0){
			throw new IllegalArgumentException("sequenceNumbersSender "+translator.translateMessage("validate")+ " 0");
		}
		if((Integer)primitives.get("sequenceNumbersSender") <= (Integer)primitives.get("windowSizeSender")){
			throw new IllegalArgumentException("sequenceNumbersSender "+translator.translateMessage("validate")+ " windowSizeSender");
		}
		if((Integer)primitives.get("timeoutSender") <= 0){
			throw new IllegalArgumentException("timeoutSender "+translator.translateMessage("validate")+ " 0");
		}
		if((Integer)primitives.get("timeoutSender") < (((Integer)primitives.get("timeToTransmitSender") + (Integer)primitives.get("timeToTransmitReceiver")) * 1.5)){
			throw new IllegalArgumentException("timeoutSender "+translator.translateMessage("validate")+ " (timeToTransmitSender + timeToTransmitReceiver)*1.5");
		}
		if((Integer)primitives.get("timeoutReceiver") <= 0){
			throw new IllegalArgumentException("timeoutReceiver "+translator.translateMessage("validate")+ " 0");
		}
		if((Integer)primitives.get("timeoutReceiver") < (((Integer)primitives.get("timeToTransmitSender") + (Integer)primitives.get("timeToTransmitReceiver")) * 1.5)){
			throw new IllegalArgumentException("timeoutReceiver "+translator.translateMessage("validate")+ " (timeToTransmitSender + timeToTransmitReceiver)*1.5");
		}
		if((Integer)primitives.get("framesToSent") <= 0){
			throw new IllegalArgumentException("framesToSent "+translator.translateMessage("validate")+ " 0");
		}
		else{
			return true;
		}
	}

}