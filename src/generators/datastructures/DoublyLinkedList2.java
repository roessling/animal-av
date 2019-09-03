package generators.datastructures;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * Doubly linked list implementation.
 * 
 * @author Uwe Breidenbach <hi01ebub@rbg.informatik.tu-darmstadt.de>
 */
public class DoublyLinkedList2 implements generators.framework.Generator {
	
	/**
	 * Internal node structure of DoublyLinkedList2
	 */
	private class Node {
		Node next = null;
		Node prev = null;
		int value;
		int x, y;
		Rect box;
		Text txt;
		Polyline n, p;
		

		Node(int value, int x, int y) {
			this.value = value;
			this.x = x;
			this.y = y;
			this.txt = lang.newText(new Coordinates(this.x + 10, this.y + 10), Integer.toString(this.value), "Text" + nameCounter++, null, textProps);
			this.box = lang.newRect(new Coordinates(this.x, this.y), new Offset(10, 10, this.txt, "se") , "Box" + nameCounter++, null, rectProps);
			this.n = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, this.box, "ne"), new Offset(30, 0, this.box, "ne")}, "Ptr" + nameCounter++, null, arrowProps);
			this.p = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, this.box, "sw"), new Offset(-30, 0, this.box, "sw")}, "Ptr" + nameCounter++, null, arrowProps);
		}
		
		void move(int x, int y) {
			if (this.next != null)
				this.next.move(x, y);
			this.x += x;
			this.y += y;
			this.box.hide();
			this.txt.hide();
			this.txt = lang.newText(new Coordinates(this.x + 10, this.y + 10), Integer.toString(this.value), "Text" + nameCounter++, null, textProps);
			this.box = lang.newRect(new Coordinates(this.x, this.y), new Offset(10, 10, this.txt, "se") , "Box" + nameCounter++, null, rectProps);
			this.n.moveBy(null, x, y, zero, zero);
			this.p.moveBy(null, x, y, zero, zero);
		}
		
		void setN(Node r) {
			this.n.hide();
			if (r != null)
				this.n = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, this.box, "ne"), new Offset(0, 0, r.box, "nw")}, "Ptr" + nameCounter++, null, arrowProps);
			else
				this.n = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, this.box, "ne"), new Offset(30, 0, this.box, "ne")}, "Ptr" + nameCounter++, null, arrowProps);
		}
		
		void setP(Node r) {
			this.p.hide();
			if (r != null)
				this.p = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, this.box, "sw"), new Offset(0, 0, r.box, "se")}, "Ptr" + nameCounter++, null, arrowProps);
			else
				this.p = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, this.box, "sw"), new Offset(-30, 0, this.box, "sw")}, "Ptr" + nameCounter++, null, arrowProps);
		}
	}
	
	/** 
	 * List variables.
	 */
	private Node head = null;
	private Node tail = null;
	private int size = 0;
	
	/**
	 * The concrete language object used for creating output.
	 */
	private Language lang;
	
	/**
	 * The array to add to the list.
	 */
    private int[] array;
    
    /**
     * Add/Remove last instead of first.
     */
    private boolean last = false;
    
    /**
     * Ask Questions?
     */
    private boolean askQuestion = true;
    
    /**
     * Random number generator
     */
    private Random rand;

	/**
	 * Timing.
	 */
	private Timing defaultTiming;
	private Timing zero;
	
	/**
	 * Animal primitives
	 */
	private Text header;
	private Text arrayText;
	private Polyline headPtr;
	private Polyline tailPtr;
	private Text headText;
	private Text tailText;
	private Text sizeText;
	private SourceCode srcAddLast;
	private SourceCode srcAddFirst;
	private SourceCode srcRemoveLast;
	private SourceCode srcRemoveFirst;
	private SourceCode srcFindLast;
	private SourceCode srcFindFirst;
	private SourceCode srcRemoveNode;
	private Polyline arrow;
	
	/**
	 * Locale 
	 */
	private Locale locale;
	private HashMap<String, String> text;
	
	/**
	 * Properties
	 */
	private TextProperties headerProps;
	private TextProperties textProps;
	private ArrayProperties arrayProps;
	private SourceCodeProperties srcProps;
	private PolylineProperties arrowProps;
	private RectProperties rectProps;
	
	private String author = "Uwe Breidenbach <hi01ebub@rbg.informatik.tu-darmstadt.de>";
	private String algorithmName = "DoublyLinkedList";
	
	/* Counter for unique names for replaced (and hidden) objects in AnimalScript. */
	private int nameCounter = 0;
	
	/**
	 * Localized constructor
	 */
	public DoublyLinkedList2(Locale locale) {
		this.rand = new Random();
		this.locale = locale;
		text = new HashMap<>();
		
		if (this.locale == Locale.GERMANY) {
			text.put("description", "Eine (azyklische) doppelt verkettete Liste ist eine dynamische Datenstruktur, die aus sequenziell verketteten Elementen, den sogenannten Knoten, besteht. Jeder Knoten besitzt zwei Zeiger, die auf den vorherige und den nächsten Knoten referenzieren. Der Zeiger auf den vorherigen Knoten des ersten und der Zeiger auf den nächsten Knoten des letzten Knotens verweisen auf ein Terminierungszeichen, typischerweise einen speziellen Knoten oder auf null, um das Traversieren über die Liste zu ermöglichen.");
			text.put("description2", "Verwendung des Eingabearrays: Jede positive Zahl (und die Null) aus dem Eingabearray wird zur Liste hinzugefügt. Für jede negative Zahl aus dem Eingabearray wird ein Knoten mit dem Betrag der Zahl in der Liste gesucht und entfernt.");
//			text.put("algorithmName", "Operationen auf doppelt verketteten Listen");
			text.put("inputArray", "Eingabearray");
			text.put("introduction", "Einleitung");
			text.put("initialization", "Initialisierung");
			text.put("epilogue", "Es wurden einige grundlegende Operationen, wie das Hinzufügen und das Entfernen von Knoten, auf (azyklischen) doppelt verketteten Listen eingeführt.");
			text.put("epilogue2", "Das Einfügen von Knoten am Anfang oder am Ende einer doppelt verketteten Liste mit n Knoten hat ein Laufzeitverhalten von O(1), da die Liste dazu nicht traversiert werden muss. Für das Einfügen von Knoten innerhalb der Liste müsste die Liste von vorne bzw. hinten durchlaufen werden, um die Position des einzufügenden Knotens zu bestimmen, was einem Laufzeitverhalten von O(n) entsprechen würde. Dieses Vorgehen wird in dieser Animation beim Löschen eines Knotens demonstriert. Dabei wird die Liste auf der Suche nach dem ersten Vorkommen des zu entfernenden Knotens durchlaufen [O(n)] und der Knoten entfernt [O(1)].");
			text.put("epilogue-tag", "Epilog");
			text.put("Add1", "Füge ");
			text.put("Add2", " ein");
			text.put("Remove", "Entferne ");
			text.put("question0", "Welches Laufzeitverhalten hat die funktion, die ein Element zur Liste hinzufügt?");
			text.put("answere0_0", "O(1)");
			text.put("answere0_1", "O(log(n))");
			text.put("answere0_2", "O(n)");
			text.put("answere0_3", "O(n²)");
			text.put("answereYes", "Ja");
			text.put("answereNo", "Nein");
			text.put("feedbackT", "Richtig!");
			text.put("feedbackF0", "Falsch! Richtig wäre O(1) gewesen.");
			text.put("feedbackF1", "Falsch! Richtig wäre O(n) gewesen.");
			text.put("feedbackF2", "Falsch! Es ist möglich.");
			text.put("question1", "Welches Laufzeitverhalten hat die funktion, die ein Element in der Liste sucht und entfernt?");
			text.put("question2", "Ist es mit doppelt verketteten Listen möglich eine Warteschlange zu implementieren, die ein Laufzeitverhalten von O(1) hat?");
			text.put("question3", "Welches Laufzeitverhalten hätte eine Funktion, die zwei doppelt verkettete Listen konkateniert?");
//			text.put("question1", "");
//			text.put("answere1_0", "");
//			text.put("feedback1_0", "");
//			text.put("answere1_1", "");
//			text.put("feedback1_1", "");
//			text.put("answere1_2", "");
//			text.put("feedback1_2", "");
//			text.put("answere1_3", "");
//			text.put("feedback1_3", "");
		} else {
			text.put("description", "In computer science, a (acyclic) doubly linked list is a linked data structure that consists of a set of sequentially linked records called nodes. Each node contains two fields, called links, that are references to the previous and to the next node in the sequence of nodes. The beginning and ending nodes' previous and next links, respectively, point to some kind of terminator, typically a sentinel node or null, to facilitate traversal of the list.");
			text.put("description2", "Usage of the input array: Each positiv integer (and zero) from the input array is added to the list. For each negativ integer from the input array a node with the absolut value of the integer is searched and removed from the list.");
//			text.put("algorithmName", "Doubly linked list operations");
			text.put("inputArray", "input array");
			text.put("introduction", "Introduction");
			text.put("initialization", "Initialization");
			text.put("epilogue", "We introduced a few basic operations, like adding and removing of nodes, on (acyclic) doubly linked lists.");
			text.put("epilogue2", "The insertion of a node in the front or the back of a doubly linked list with n nodes is in O(1), because a traversal of the list is therefor not necessary. For an insertion of a node in between the list, the list ought to be traversed from the front to the back or the other way around in order to determine the position of the node to be inserted, which lead to a time complexity of O(n). This approach is demonstrated in this animation by removing a node. Thereby the list is traversed in search of the first approach of the node to be removed [O(n)] and afterwards the node is removed [O(1)].");
			text.put("epilogue-tag", "Epilogue");
			text.put("Add1", "Add ");
			text.put("Add2", "");
			text.put("Remove", "Remove ");
			text.put("question0", "Which time complexity has the function that adds a value to the list?");
			text.put("answere0_0", "O(1)");
			text.put("answere0_1", "O(log(n))");
			text.put("answere0_2", "O(n)");
			text.put("answere0_3", "O(n²)");
			text.put("answereYes", "Yes");
			text.put("answereNo", "No");
			text.put("feedbackT", "Correct!");
			text.put("feedbackF0", "Wrong! O(1) would have been correct.");
			text.put("feedbackF0", "Wrong! O(n) would have been correct.");
			text.put("feedbackF2", "Wrong! It is possible.");
			text.put("question1", "Which time complexity has the function that searches and removes a value from the list?");
			text.put("question2", "Is it possible to implement a queue with doubly linked lists with a time complexity of O(1)?");
			text.put("question3", "Which time complexity would have a function that concatenates two doubly linked lists?");
		}
		
		text.put("src", "public void addFirst(int value) {\n"
				+ "   Node node = new Node(value);\n"
				+ "   if (head != null) {\n"
				+ "      node.next = head;\n"
				+ "      head.prev = node;\n"
				+ "   } else {\n"
				+ "      tail = node;\n"
				+ "   }\n"
				+ "   head = node;\n"
				+ "   ++size;\n"
				+ "}\n"
				);
		
		text.put("srcAddFirst0", "public void addFirst(int value) {");
		text.put("srcAddFirst1", "Node node = new Node(value);");
		text.put("srcAddFirst2", "if (head != null) {");
		text.put("srcAddFirst3", "node.next = head;");
		text.put("srcAddFirst4", "head.prev = node;");
		text.put("srcAddFirst5", "} else {");
		text.put("srcAddFirst6", "tail = node;");
		text.put("srcAddFirst7", "}");
		text.put("srcAddFirst8", "head = node;");
		text.put("srcAddFirst9", "++size;");
		text.put("srcAddFirst10", "}");
		
		text.put("srcRemoveFirst0", "public void removeFirst(int value) {");
		text.put("srcRemoveFirst1", "remove(findFirst(value));");
		text.put("srcRemoveFirst2", "}");
		
		text.put("srcFindFirst0", "private Node findFirst(int value) {");
		text.put("srcFindFirst1", "Node node = head;");
		text.put("srcFindFirst2", "while (node != null && node.value != value)");
		text.put("srcFindFirst3", "node = node.next;");
		text.put("srcFindFirst4", "return node;");
		text.put("srcFindFirst5", "}");
		
		text.put("srcRemoveNode0", "private void remove(Node node) {");
		text.put("srcRemoveNode1", "if (node == null)");
		text.put("srcRemoveNode2", "return;");
		text.put("srcRemoveNode3", "if (node.prev != null)");
		text.put("srcRemoveNode4", "node.prev.next = node.next;");
		text.put("srcRemoveNode5", "else");
		text.put("srcRemoveNode6", "head = node.next;");
		text.put("srcRemoveNode7", "if (node.next != null)");
		text.put("srcRemoveNode8", "node.next.prev = node.prev;");
		text.put("srcRemoveNode9", "else");
		text.put("srcRemoveNode10", "tail = node.prev;");
		text.put("srcRemoveNode11", "--size;");
		text.put("srcRemoveNode12", "}");
		
		text.put("srcAddLast0", "public void addLast(int value) {");
		text.put("srcAddLast1", "Node node = new Node(value);");
		text.put("srcAddLast2", "if (tail != null) {");
		text.put("srcAddLast3", "node.prev = tail;");
		text.put("srcAddLast4", "tail.next = node;");
		text.put("srcAddLast5", "} else {");
		text.put("srcAddLast6", "head = node;");
		text.put("srcAddLast7", "}");
		text.put("srcAddLast8", "tail = node;");
		text.put("srcAddLast9", "++size;");
		text.put("srcAddLast10", "}");
		
		text.put("srcRemoveLast0", "public void removeLast(int value) {");
		text.put("srcRemoveLast1", "remove(findLast(value));");
		text.put("srcRemoveLast2", "}");
		
		text.put("srcFindLast0", "private Node findLast(int value) {");
		text.put("srcFindLast1", "Node node = tail;");
		text.put("srcFindLast2", "while (node != null && node.value != value)");
		text.put("srcFindLast3", "node = node.prev;");
		text.put("srcFindLast4", "return node;");
		text.put("srcFindLast5", "}");
		
		init();
	}

	/**
	 * Inserts the specified element at the beginning of this list.
	 * 
	 * @param value
	 *            the element to add
	 */
	protected void addFirst(int value) { // 0
		srcAddFirst.show();
		srcAddFirst.highlight(0);
		//move
		if (head != null) {
			headPtr.hide();
			tailPtr.hide();
			head.move(80, 0);
			headPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, headText, "n"), new Offset(0, 0, head.box, "s")}, "HeadPtr" + nameCounter++, null, arrowProps);
			tailPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, tailText, "n"), new Offset(0, 0, tail.box, "s")}, "TailPtr" + nameCounter++, null, arrowProps);
		}
		lang.nextStep(text.get("Add1") + value + text.get("Add2"));
		
		srcAddFirst.toggleHighlight(0, 1);
		Node node = new Node(value, 100, 150); // 1
		lang.nextStep();
		
		srcAddFirst.toggleHighlight(1, 2);
		lang.nextStep();
		if (head != null) { // 2
			srcAddFirst.toggleHighlight(2, 3);
			node.setN(head);
			node.next = head; // 3
			lang.nextStep();
			
			srcAddFirst.toggleHighlight(3, 4);
			head.setP(node);
			head.prev = node; // 4
			lang.nextStep();
			
			srcAddFirst.toggleHighlight(4, 7);
			lang.nextStep();
		} else { // 5
			srcAddFirst.toggleHighlight(2, 5);
			lang.nextStep();
			
			srcAddFirst.toggleHighlight(5, 6);
			tailPtr.hide();
			tailPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, tailText, "n"), new Offset(0, 0, node.box, "s")}, "TailPtr" + nameCounter++, null, arrowProps);
			tail = node; // 6
			lang.nextStep();
			
			srcAddFirst.toggleHighlight(6, 7);
			lang.nextStep();
		} // 7

		srcAddFirst.toggleHighlight(7, 8);
		headPtr.hide();
		headPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, headText, "n"), new Offset(0, 0, node.box, "s")}, "HeadPtr" + nameCounter++, null, arrowProps);
		head = node; // 8
		lang.nextStep();
		
		srcAddFirst.toggleHighlight(8, 9);
		++size; // 9
		sizeText.setText("Size = " + this.size, zero, zero);
		lang.nextStep();
		
		srcAddFirst.toggleHighlight(9, 10);
		lang.nextStep();
		srcAddFirst.unhighlight(10);
		srcAddFirst.hide();
	} // 10

	/**
	 * Appends the specified element to the end of this list.
	 * 
	 * @param value
	 *            the element to add
	 */
	protected void addLast(int value) { // 0
		srcAddLast.show();
		srcAddLast.highlight(0);
		lang.nextStep(text.get("Add1") + value + text.get("Add2"));
		
		srcAddLast.toggleHighlight(0, 1);
		Node node;
		if (tail != null)
			node = new Node(value, tail.x + 80, tail.y); // 1
		else
			node = new Node(value, 100, 150); // 1
		lang.nextStep();
		
		srcAddLast.toggleHighlight(1, 2);
		lang.nextStep();
		if (tail != null) { // 2
			srcAddLast.toggleHighlight(2, 3);
			node.setP(tail);
			node.prev = tail; // 3
			lang.nextStep();
			
			srcAddLast.toggleHighlight(3, 4);
			tail.setN(node);
			tail.next = node; // 4
			lang.nextStep();
			
			srcAddLast.toggleHighlight(4, 7);
			lang.nextStep();
		} else { // 5
			srcAddLast.toggleHighlight(2, 5);
			lang.nextStep();
			
			srcAddLast.toggleHighlight(5, 6);
			headPtr.hide();
			headPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, headText, "n"), new Offset(0, 0, node.box, "s")}, "HeadPtr" + nameCounter++, null, arrowProps);
			head = node; // 6
			lang.nextStep();
			
			srcAddLast.toggleHighlight(6, 7);
			lang.nextStep();
		} // 7

		srcAddLast.toggleHighlight(7, 8);
		tailPtr.hide();
		tailPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, tailText, "n"), new Offset(0, 0, node.box, "s")}, "TailPtr" + nameCounter++, null, arrowProps);
		tail = node; // 8
		lang.nextStep();
		
		srcAddLast.toggleHighlight(8, 9);
		++size; // 9
		sizeText.setText("Size = " + this.size, zero, zero);
		lang.nextStep();
		
		srcAddLast.toggleHighlight(9, 10);
		lang.nextStep();
		srcAddLast.unhighlight(10);
		srcAddLast.hide();
	} // 10

	/**
	 * Inserts the specified element at the beginning of this list.
	 * 
	 * @param value
	 *            the element to add
	 */
	protected void add(int value) {
		if (last)
			addLast(value);
		else
			addFirst(value);
	}

	/**
	 * Removes the first occurrence of the specified element in this list (when
	 * traversing the list from head to tail). If the list does not contain the
	 * element, it is unchanged.
	 * 
	 * @param value
	 *            element to be removed from this list, if present
	 */
	protected void removeFirst(int value) { // 0
		srcRemoveFirst.show();
		srcRemoveFirst.highlight(0);
		lang.nextStep(text.get("Remove") + value);
		
		srcRemoveFirst.toggleHighlight(0, 1);
		remove(findFirst(value)); // 1
		
		srcRemoveFirst.toggleHighlight(1, 2);
		lang.nextStep();

		srcRemoveFirst.unhighlight(2);
		srcRemoveFirst.hide();
	} // 2

	/**
	 * Removes the last occurrence of the specified element in this list (when
	 * traversing the list from head to tail). If the list does not contain the
	 * element, it is unchanged.
	 * 
	 * @param value
	 *            element to be removed from this list, if present
	 */
	protected void removeLast(int value) { // 0
		srcRemoveLast.show();
		srcRemoveLast.highlight(0);
		lang.nextStep(text.get("Remove") + value);
		
		srcRemoveLast.toggleHighlight(0, 1);
		remove(findLast(value)); // 1
		
		srcRemoveLast.toggleHighlight(1, 2);
		lang.nextStep();

		srcRemoveLast.unhighlight(2);
		srcRemoveLast.hide();
	} // 2

	/**
	 * Removes the first occurrence of the specified element in this list (when
	 * traversing the list from head to tail). If the list does not contain the
	 * element, it is unchanged.
	 * 
	 * @param value
	 *            element to be removed from this list, if present
	 */
	protected void remove(int value) {
		if (last)
			removeLast(value);
		else
			removeFirst(value);
	}

	/**
	 * Removes the node from list.
	 * 
	 * @param value
	 *            element to be removed from this list, if present
	 */
	private void remove(Node node) { // 0
		srcRemoveNode.show();
		srcRemoveNode.highlight(0);
		lang.nextStep();
		
		srcRemoveNode.toggleHighlight(0, 1);
		lang.nextStep();
		
		if (node == null) { // 1
			srcRemoveNode.toggleHighlight(1, 2);
			lang.nextStep();
			
			arrow.hide();
			srcRemoveNode.unhighlight(2);
			srcRemoveNode.hide();
			return; // 2
		}
		
		srcRemoveNode.toggleHighlight(1, 3);
		lang.nextStep();
		
		if (node.prev != null) { // 3
			srcRemoveNode.toggleHighlight(3, 4);
			node.prev.setN(node.next);
			node.prev.next = node.next; // 4
			lang.nextStep();
			
			srcRemoveNode.unhighlight(4);
		} else { // 5
			srcRemoveNode.toggleHighlight(3, 6);
			headPtr.hide();
			if (node.next != null)
				headPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, headText, "n"), new Offset(0, 0, node.next.box, "s")}, "HeadPtr" + nameCounter++, null, arrowProps);
			else
				headPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, headText, "n"), new Offset(0, -30, headText, "n")}, "HeadPtr" + nameCounter++, null, arrowProps);
			head = node.next; // 6
			lang.nextStep();

			srcRemoveNode.unhighlight(6);
		}
		
		srcRemoveNode.highlight(7);
		lang.nextStep();
		if (node.next != null) { // 7
			srcRemoveNode.toggleHighlight(7, 8);
			node.next.setP(node.prev);
			node.next.prev = node.prev; // 8
			lang.nextStep();

			srcRemoveNode.unhighlight(8);
		} else { // 9
			srcRemoveNode.toggleHighlight(7, 10);
			tailPtr.hide();
			if (node.prev != null)
				tailPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, tailText, "n"), new Offset(0, 0, node.prev.box, "s")}, "TailPtr" + nameCounter++, null, arrowProps);
			else
				tailPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, tailText, "n"), new Offset(0, -30, tailText, "n")}, "TailPtr" + nameCounter++, null, arrowProps);
			tail = node.prev; // 10
			lang.nextStep();
			
			srcRemoveNode.unhighlight(10);
		}
		
		node.box.hide();
		node.n.hide();
		node.p.hide();
		node.txt.hide();
		arrow.hide();
		srcRemoveNode.highlight(11);
		--size; // 11
		sizeText.setText("Size = " + this.size, zero, zero);
		lang.nextStep();
		
		if (node.next != null) {
			headPtr.hide();
			tailPtr.hide();
			node.next.move(-80, 0);
			if (node.prev != null)
				node.prev.setN(node.next);
			node.next.setP(node.prev);
			headPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, headText, "n"), new Offset(0, 0, head.box, "s")}, "HeadPtr" + nameCounter++, null, arrowProps);
			tailPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, tailText, "n"), new Offset(0, 0, tail.box, "s")}, "TailPtr" + nameCounter++, null, arrowProps);
		}
		srcRemoveNode.toggleHighlight(11, 12);
		lang.nextStep();
		
		srcRemoveNode.unhighlight(12);
		srcRemoveNode.hide();
	} // 12

	/**
	 * Returns the first occurrence of the specified element in this list (when
	 * traversing the list from head to tail), null otherwise.
	 * 
	 * @param value
	 *            element to be found in this list, if present
	 * @return the first occurrence of the specified element in this list, null
	 *         otherwise.
	 */
	private Node findFirst(int value) { // 0
		srcFindFirst.show();
		srcFindFirst.highlight(0);
		lang.nextStep();
		
		srcFindFirst.toggleHighlight(0, 1);
		Node node = head; // 1
		if (node != null)
			arrow = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, -25, node.box, "n"), new Offset(0, 0, node.box, "n")}, "Ptr" + nameCounter++, null, arrowProps);
		else
			arrow = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, -30, headPtr, "n"), new Offset(0, -5, headPtr, "n")}, "Ptr" + nameCounter++, null, arrowProps);
		arrow.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, zero, zero);
		lang.nextStep();

		srcFindFirst.toggleHighlight(1, 3);
		while (node != null && node.value != value) { // 2
			srcFindFirst.toggleHighlight(3, 2);
			lang.nextStep();
			
			srcFindFirst.toggleHighlight(2, 3);
			if (node.next != null)
				arrow.moveTo(null, null, new Offset(0, -25, node.next.box, "n"), zero, zero);
			else
				arrow.moveTo(null, null, new Offset(30, -25, node.box, "ne"), zero, zero);
			node = node.next; // 3
			lang.nextStep();
		}
		srcFindFirst.toggleHighlight(3, 2);
		lang.nextStep();
		
		srcFindFirst.toggleHighlight(2, 4);
		lang.nextStep();
		
		srcFindFirst.unhighlight(4);
		srcFindFirst.hide();
		return node; // 4
	} // 5

	/**
	 * Returns the last occurrence of the specified element in this list (when
	 * traversing the list from head to tail), null otherwise.
	 * 
	 * @param value
	 *            element to be found in this list, if present
	 * @return the last occurrence of the specified element in this list, null
	 *         otherwise.
	 */
	private Node findLast(int value) { // 0
		srcFindLast.show();
		srcFindLast.highlight(0);
		lang.nextStep();
		
		srcFindLast.toggleHighlight(0, 1);
		Node node = tail; // 1
		if (node != null)
			arrow = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, -25, node.box, "n"), new Offset(0, 0, node.box, "n")}, "Ptr" + nameCounter++, null, arrowProps);
		else
			arrow = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, -30, tailPtr, "n"), new Offset(0, -5, tailPtr, "n")}, "Ptr" + nameCounter++, null, arrowProps);
		arrow.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED, zero, zero);
		lang.nextStep();

		srcFindLast.toggleHighlight(1, 3);
		while (node != null && node.value != value) { // 2
			srcFindLast.toggleHighlight(3, 2);
			lang.nextStep();
			
			srcFindLast.toggleHighlight(2, 3);
			if (node.prev != null)
				arrow.moveTo(null, null, new Offset(0, -25, node.prev.box, "n"), zero, zero);
			else
				arrow.moveTo(null, null, new Offset(-30, -25, node.box, "nw"), zero, zero);
			node = node.prev; // 3
		lang.nextStep();
		}
		srcFindLast.toggleHighlight(3, 2);
		lang.nextStep();
		
		srcFindLast.toggleHighlight(2, 4);
		lang.nextStep();
		
		srcFindLast.unhighlight(4);
		srcFindLast.hide();
		return node; // 4
	} // 5

	/**
	 * Returns the first occurrence of the specified element in this list, null
	 * otherwise.
	 * 
	 * @param value
	 *            element to be found in this list, if present
	 * @return the first occurrence of the specified element in this list, null
	 *         otherwise.
	 */
	private Node find(int value) {
		return last ? findLast(value) : findFirst(value);
	}

	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list
	 */
	protected int size() {
		return size;
	}

	/**
	 * Removes all of the elements from this list. The list will be empty after
	 * this call returns.
	 */
	protected void clear() {
		head = null;
		tail = null;
		size = 0;
	}

	public String toString() {
		StringBuffer result = new StringBuffer(size() * 10);
		result.append('[');
		Node node = head;
		while (node != null) {
			result.append(node.value).append(" - ");
			node = node.next;
		}
		if (result.length() > 1)
			result.delete(result.length() - 3, result.length());
		result.append(']');
		return result.toString();
	}
	
	private void run() {
		/* Print header and introduction */
	    header = lang.newText(new Coordinates(25, 25), getAlgorithmName(), "header", null, headerProps);
	    //TODO The first line is not printed on the proper place when font size is changed from default, but the resulting AnimalSkript code seems to be okay.
	    int tmp = showText(text.get("description"), 50, 100, 80, "intro");
	    showText(text.get("description2"), 50, 125 + tmp * 25, 80, "intro2");
		lang.nextStep(text.get("introduction"));
		
		lang.hideAllPrimitives();
		
		/* Print header, IntArray and list. Prepare src. */
		header.show();
		arrayText = lang.newText(new Offset(25, 25, header, "sw"), text.get("inputArray") + ":", "ArrayText", null, textProps);
		IntArray array = lang.newIntArray(new Offset(25, 0, arrayText, "ne"), this.array, "array", null, arrayProps);
		headText = lang.newText(new Coordinates(100, 250), "Head", "HeadText", null, textProps);
		tailText = lang.newText(new Coordinates(250, 250), "Tail", "TailText", null, textProps);
		headPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, headText, "n"), new Offset(0, -30, headText, "n")}, "HeadPtr" + nameCounter++, null, arrowProps);
		tailPtr = lang.newPolyline(new algoanim.util.Node[]{new Offset(0, 0, tailText, "n"), new Offset(0, -30, tailText, "n")}, "TailPtr" + nameCounter++, null, arrowProps);
		sizeText = lang.newText(new Coordinates(400, 250), "Size = " + this.size, "Size", null, textProps);
		
		initSrc();
		lang.nextStep(text.get("initialization"));
		
		
		for (int i = 0; i < array.getLength(); ++i) {
			if (askQuestion && (i == 3 || (i < 3 && i == (array.getLength() - 1))))
				ask(rand.nextInt(4));
			array.highlightCell(i, zero, defaultTiming);
			int j = array.getData(i);
			if (j >= 0)
				add(j);
			else if (j < 0)
				remove(-j);
			array.unhighlightCell(i, zero, defaultTiming);
		}
		
		lang.hideAllPrimitives();
		
		/* Print header and epilogue. */
		header.show();
		tmp = showText(text.get("epilogue"), 50, 100, 80, "epilogue");
	    showText(text.get("epilogue2"), 50, 125 + tmp * 25, 80, "epilogue2");
		lang.nextStep(text.get("epilogue-tag"));
	}
	
	/**
	 * Initializes the source code.
	 */
	private void initSrc() {
		srcAddFirst = lang.newSourceCode(new Coordinates(50, 300), "srcAddFirst", null, srcProps);		
		srcAddFirst.addCodeLine(text.get("srcAddFirst0"), null, 0, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst1"), null, 1, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst2"), null, 1, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst3"), null, 2, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst4"), null, 2, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst5"), null, 1, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst6"), null, 2, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst7"), null, 1, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst8"), null, 1, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst9"), null, 1, null);
		srcAddFirst.addCodeLine(text.get("srcAddFirst10"), null, 0, null);
		srcAddFirst.hide();
		
		srcRemoveFirst = lang.newSourceCode(new Coordinates(50, 300), "srcRemoveFirst", null, srcProps);
		srcRemoveFirst.addCodeLine(text.get("srcRemoveFirst0"), null, 0, null);
		srcRemoveFirst.addCodeLine(text.get("srcRemoveFirst1"), null, 1, null);
		srcRemoveFirst.addCodeLine(text.get("srcRemoveFirst2"), null, 0, null);
		srcRemoveFirst.hide();
		
		srcFindFirst = lang.newSourceCode(new Offset(100, 0, srcRemoveFirst, "ne"), "srcFindFirst", null, srcProps);
		srcFindFirst.addCodeLine(text.get("srcFindFirst0"), null, 0, null);
		srcFindFirst.addCodeLine(text.get("srcFindFirst1"), null, 1, null);
		srcFindFirst.addCodeLine(text.get("srcFindFirst2"), null, 1, null);
		srcFindFirst.addCodeLine(text.get("srcFindFirst3"), null, 2, null);
		srcFindFirst.addCodeLine(text.get("srcFindFirst4"), null, 1, null);
		srcFindFirst.addCodeLine(text.get("srcFindFirst5"), null, 0, null);
		srcFindFirst.hide();
		
		srcRemoveNode = lang.newSourceCode(new Offset(100, 0, srcRemoveFirst, "ne"), "srcRemoveNode", null, srcProps);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode0"), null, 0, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode1"), null, 1, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode2"), null, 2, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode3"), null, 1, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode4"), null, 2, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode5"), null, 1, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode6"), null, 2, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode7"), null, 1, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode8"), null, 2, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode9"), null, 1, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode10"), null, 2, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode11"), null, 1, null);
		srcRemoveNode.addCodeLine(text.get("srcRemoveNode12"), null, 0, null);
		srcRemoveNode.hide();
		
		srcAddLast = lang.newSourceCode(new Coordinates(50, 300), "srcAddLast", null, srcProps);		
		srcAddLast.addCodeLine(text.get("srcAddLast0"), null, 0, null);
		srcAddLast.addCodeLine(text.get("srcAddLast1"), null, 1, null);
		srcAddLast.addCodeLine(text.get("srcAddLast2"), null, 1, null);
		srcAddLast.addCodeLine(text.get("srcAddLast3"), null, 2, null);
		srcAddLast.addCodeLine(text.get("srcAddLast4"), null, 2, null);
		srcAddLast.addCodeLine(text.get("srcAddLast5"), null, 1, null);
		srcAddLast.addCodeLine(text.get("srcAddLast6"), null, 2, null);
		srcAddLast.addCodeLine(text.get("srcAddLast7"), null, 1, null);
		srcAddLast.addCodeLine(text.get("srcAddLast8"), null, 1, null);
		srcAddLast.addCodeLine(text.get("srcAddLast9"), null, 1, null);
		srcAddLast.addCodeLine(text.get("srcAddLast10"), null, 0, null);
		srcAddLast.hide();
		
		srcRemoveLast = lang.newSourceCode(new Coordinates(50, 300), "srcRemoveLast", null, srcProps);
		srcRemoveLast.addCodeLine(text.get("srcRemoveLast0"), null, 0, null);
		srcRemoveLast.addCodeLine(text.get("srcRemoveLast1"), null, 1, null);
		srcRemoveLast.addCodeLine(text.get("srcRemoveLast2"), null, 0, null);
		srcRemoveLast.hide();
		
		srcFindLast = lang.newSourceCode(new Offset(100, 0, srcRemoveLast, "ne"), "srcFindLast", null, srcProps);
		srcFindLast.addCodeLine(text.get("srcFindLast0"), null, 0, null);
		srcFindLast.addCodeLine(text.get("srcFindLast1"), null, 1, null);
		srcFindLast.addCodeLine(text.get("srcFindLast2"), null, 1, null);
		srcFindLast.addCodeLine(text.get("srcFindLast3"), null, 2, null);
		srcFindLast.addCodeLine(text.get("srcFindLast4"), null, 1, null);
		srcFindLast.addCodeLine(text.get("srcFindLast5"), null, 0, null);
		srcFindLast.hide();
	}
	
	/**
	 * Prints a string in multiple lines of maximum <code>width</code> characters.
	 * Works only with coordinates, because this is a workaround for the missing
	 * feature of Animal to print _one_ String in _multiple_ lines.
	 * 
	 * @param input The string to print.
	 * @param x X-axis coordinate.
	 * @param y Y-axis coordinate.
	 * @param width The maximum number of characters in a line.
	 * @param name The name of the new text objects followed by the line number starting with 0.
	 * @return The number of lines used.
	 */
	private int showText(String input, int x, int y, int width, String name) {
		int index = 0;
		int yAxis = y;
		int lineNo = 0;
		while (index < input.length()) {
			if (index + width < input.length()) {
				String sub = input.substring(index, index + width);
				int end = Math.max(sub.lastIndexOf(' '), sub.lastIndexOf('.'));
				end = Math.max(end, sub.lastIndexOf('!'));
				end = Math.max(end, sub.lastIndexOf('?'));
				lang.newText(new Coordinates(x, yAxis), input.substring(index, index + end + 1), name + lineNo, null, textProps);
				index += end + 1;
			} else {
				lang.newText(new Coordinates(x, yAxis), input.substring(index), name + lineNo, null, textProps);
				index = input.length();
			}
			yAxis += 25;
			++lineNo;
		}
		return lineNo;
	}
	
//	private void updateSizeText() {
//		sizeText.hide();
//		sizeText = lang.newText(new Coordinates(400, 250), "Size = " + this.size, "Size", null, textProps);
//	}
	
	private void ask(int no) {
		MultipleChoiceQuestionModel mcQuestion = new MultipleChoiceQuestionModel(Integer.toString(no));
		switch (no) {
		case 0:
			mcQuestion.setPrompt(text.get("question0"));
			mcQuestion.addAnswer(text.get("answere0_0"), 1, text.get("feedbackT"));
			mcQuestion.addAnswer(text.get("answere0_1"), 0, text.get("feedbackF0"));
			mcQuestion.addAnswer(text.get("answere0_2"), 0, text.get("feedbackF0"));
			mcQuestion.addAnswer(text.get("answere0_3"), 0, text.get("feedbackF0"));
			break;
		case 1:
			mcQuestion.setPrompt(text.get("question1"));
			mcQuestion.addAnswer(text.get("answere0_0"), 0, text.get("feedbackF1"));
			mcQuestion.addAnswer(text.get("answere0_1"), 0, text.get("feedbackF1"));
			mcQuestion.addAnswer(text.get("answere0_2"), 1, text.get("feedbackT"));
			mcQuestion.addAnswer(text.get("answere0_3"), 0, text.get("feedbackF1"));
			break;
		case 2:
			mcQuestion.setPrompt(text.get("question2"));
			mcQuestion.addAnswer(text.get("answereYes"), 1, text.get("feedbackT"));
			mcQuestion.addAnswer(text.get("answereNo"), 0, text.get("feedbackF2"));
			break;
		case 3:
			mcQuestion.setPrompt(text.get("question3"));
			mcQuestion.addAnswer(text.get("answere0_0"), 1, text.get("feedbackT"));
			mcQuestion.addAnswer(text.get("answere0_1"), 0, text.get("feedbackF0"));
			mcQuestion.addAnswer(text.get("answere0_2"), 0, text.get("feedbackF0"));
			mcQuestion.addAnswer(text.get("answere0_3"), 0, text.get("feedbackF0"));
			break;
		}
		lang.addMCQuestion(mcQuestion);
//		MultipleChoiceQuestionModel mcQuestion = new MultipleChoiceQuestionModel("0");
//		mcQuestion.setPrompt(text.get("question0"));
//		mcQuestion.addAnswer(text.get("answere0_0"), 1, text.get("feedback0_0"));
//		mcQuestion.addAnswer(text.get("answere0_1"), 1, text.get("feedback0_1"));
//		mcQuestion.addAnswer(text.get("answere0_2"), 0, text.get("feedback0_2"));
//		mcQuestion.addAnswer(text.get("answere0_3"), 2, text.get("feedback0_3"));
//		lang.addMCQuestion(mcQuestion);
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		/* Set properties. */
		headerProps = (TextProperties) props.getPropertiesByName("headerProps");
		textProps = (TextProperties) props.getPropertiesByName("textProps");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		srcProps = (SourceCodeProperties) props.getPropertiesByName("srcProps");
		arrowProps = (PolylineProperties) props.getPropertiesByName("arrowProps");
		rectProps = (RectProperties) props.getPropertiesByName("rectProps");
		

		/* Change some properties, because they can't be specified in the XML. */
		//TODO Some properties can't be specified in the XML.
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		
		/* Set variables. */
		array = (int[]) primitives.get("array");
		last = (boolean) primitives.get("last");
		askQuestion = (boolean) primitives.get("askQuestion");
		
		/* Set a name counter for unique names for replaced (and hidden) objects in AnimalScript. */
		nameCounter = 0;
		
		/* Reset variables, because the execution after generating
		 * in Animal otherwise fails */
		this.head = null;
		this.tail = null;
		this.size = 0;
		
		/* Generate the AnimalSkript code. */
		run();
        lang.finalizeGeneration();
        return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return algorithmName;
	}

	@Override
	public String getAnimationAuthor() {
		return author;
	}

	@Override
	public String getCodeExample() {
		return text.get("src");
	}

	@Override
	public Locale getContentLocale() {
		return locale;
	}

	@Override
	public String getDescription() {
		return text.get("description") + "\n\n" + text.get("description2");
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getName() {
		return getAlgorithmName();
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript(getAlgorithmName(), getAnimationAuthor(), 1024, 768);
		
		/* This initializes the step mode. Each pair of subsequent steps has to be divided by a call of lang.nextStep(); */
		lang.setStepMode(true);
		
		/* Declare timings. */
		defaultTiming = new TicksTiming(100);
		zero = new TicksTiming(0);
		
		/* Initialize properties. */
		headerProps = new TextProperties();
		textProps = new TextProperties();
		arrayProps = new ArrayProperties();
		srcProps = new SourceCodeProperties();
		arrowProps = new PolylineProperties();
		rectProps = new RectProperties();
		
		/* Prepare interaction support. */
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public static void main(String[] args) {
		DoublyLinkedList2 list = new DoublyLinkedList2(Locale.GERMANY);
//		int[] array = {4, 3, 2, 1, 0, 1, 2, 3, 4, -2, 5, -4,  -42};
//		int[] array = {3, 2, 1, -2, -5, 4};
//		int[] array = {3, 2, 1, -1, -3};
		int[] array = {-3, 3, 2, 1, -1, -5, -3, -2 };
//		int[] array = {3, 2, 1, -2 };
		AnimationPropertiesContainer props = new AnimationPropertiesContainer();
		Hashtable<String, Object> primitives = new Hashtable<>();
		
		props.add(new TextProperties("headerProps"));
		props.add(new TextProperties("textProps"));
		props.add(new ArrayProperties("arrayProps"));
		props.add(new SourceCodeProperties("srcProps"));
		props.add(new PolylineProperties("arrowProps"));
		props.add(new RectProperties("rectProps"));
		
		props.getPropertiesByName("arrayProps").set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);
		props.getPropertiesByName("arrayProps").set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		props.getPropertiesByName("srcProps").set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		props.getPropertiesByName("arrowProps").set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		
		primitives.put("array", array);
		primitives.put("last", false);
		
		String as = list.generate(props, primitives);
		System.out.println(as);
	}
}
