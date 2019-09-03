package generators.misc.knapsackProblem.main;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.knapsackProblem.algorithm.Basket;
import generators.misc.knapsackProblem.algorithm.Item;
import generators.misc.knapsackProblem.algorithm.KnapsackAlgorithm;
import generators.misc.knapsackProblem.view.Accumulator;
import generators.misc.knapsackProblem.view.GraphBuilder;
import generators.misc.knapsackProblem.view.SimpleNode;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class KnapsackProblem implements ValidatingGenerator {
	
	private Language lang;
	private SourceCode src;
	
	// config input
    private String[][] items;
    private int capacity;
	
    // GUI
    private StringArray optionBasket;
    private StringArray currentBasket;
    private Text currentLabel;
    private Coordinates finishedBasketsAnchor;
    
    // questions
    private ArrayList<String> addedQuestions;
    
	/**
	 * Prepare algorithm input and compute results
	 */
	public void prepare(AnimationPropertiesContainer props) {
		// prepare items
		Item[] items = new Item[4];
		// max row count is 5, ignore first
		for (int i = 1; i < 5; i++) {
			// max column count is 3
			items[i - 1] = new Item(this.items[0][i], Integer.parseInt(this.items[1][i]), Integer.parseInt(this.items[2][i]));
		}
		
		// prepare root for graph
		SimpleNode root = new SimpleNode(new Basket());
		
		// compute graph
		Basket options = new Basket(new ArrayList<>(Arrays.asList(items)));
		System.out.println(options);
		System.out.println("Starting with max capacity: " + this.capacity);
		Basket result = KnapsackAlgorithm.computeRecGraph(root, options.copy(), new Basket(), this.capacity);
		System.out.println("Result: " + result);
		int remaining = this.capacity - result.getBasketWeight();
		System.out.println("Remaining Cap: " + remaining);
		
		this.visualize(props, root, options, result);
	}
	
	public void prepareSrc(AnimationPropertiesContainer props) {
		SourceCodeProperties sProps = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
		sProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 0, 16));
		
		this.src = this.lang.newSourceCode(new Coordinates(800, 300), "sourceCode", null, sProps);
		this.src.hide();
		
		this.src.addCodeLine("public int recKnapsack(int maxWeight, int value, List options) {", null, 1, null); // 0
		this.src.addCodeLine("if options are empty {", null, 2, null); // 1
		this.src.addCodeLine("return value", null, 3, null); // 2
		this.src.addCodeLine("} else {", null, 2, null); // 3
		this.src.addCodeLine("choose first item in options", null, 3, null); // 4
		this.src.addCodeLine("if maxWeight <= item.weight {", null, 3, null); // 5
		this.src.addCodeLine("with = recKnapsack(maxWeight - item.weight, value + item.value, options)", null, 4, null); // 6
		this.src.addCodeLine("without = recKnapsack(maxWeight, value, options)", null, 4, null); // 7
		this.src.addCodeLine("return max(with.value, without.value)", null, 4, null); // 8
		this.src.addCodeLine("} else {", null, 3, null); // 9
		this.src.addCodeLine("skip the item", null, 4, null); // 10
		this.src.addCodeLine("return recKnapsack(maxWeight, value, options)", null, 4, null); // 11
		this.src.addCodeLine("}", null, 3, null); // 12
		this.src.addCodeLine("}", null, 2, null); // 13
		this.src.addCodeLine("}", null, 1, null); // 14
		
	}
	
	public void visualize(AnimationPropertiesContainer props, SimpleNode root, Basket options, Basket result) {
		
		// properties
		ArrayProperties aProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
	    TextProperties tProps = (TextProperties) props.getPropertiesByName("textProps");
	    TextProperties hProps = (TextProperties) props.getPropertiesByName("titleProps");
	    hProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 1, 24));
	    SourceCodeProperties sProps = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
	    sProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 0, 16));
		
		// intro text
		Text headline = this.lang.newText(new Coordinates(10, 10), this.getAlgorithmName(), "headline", null, hProps);
		SourceCode intro1 = this.lang.newSourceCode(new Coordinates(10, 30), "intro1", null, sProps);
		intro1.addMultilineCode(this.getIntro1(), null, null);
		StringArray example = lang.newStringArray(new Coordinates(10, 135), options.toArray(), "options", null, aProps);
		example.showIndices(false, null, null);
		SourceCode intro2 = this.lang.newSourceCode(new Coordinates(10, 155), "intro2", null, sProps);
		intro2.addMultilineCode(this.getIntro2(), null, null);
		this.lang.nextStep();
		
		// GUI Init
	    headline.hide();
	    intro1.hide();
	    example.hide();
	    intro2.hide();
	    lang.newText(new Coordinates(10, 10), "Knapsack Options (w = weight, v = value) MAX-WEIGHT: " + this.capacity, "options", null, tProps);
		this.optionBasket = lang.newStringArray(new Coordinates(10, 30), options.toArray(), "options", null, aProps);
		this.optionBasket.showIndices(false, null, null);
		this.currentLabel = lang.newText(new Coordinates(10, 80), "Current Knapsack", "current", null, tProps);
		this.currentBasket = lang.newStringArray(new Coordinates(10, 100), root.getBasket().toArray(), "current", null, aProps);
		this.currentBasket.showIndices(false, null, null);
		lang.newText(new Coordinates(10, 150), "Finished Knapsack", "finished", null, tProps);
		this.finishedBasketsAnchor = new Coordinates(10, 170);
		

		Graph g = GraphBuilder.buildGraph(this.lang, root, (GraphProperties) props.getPropertiesByName("graphProps"));
		g.show();
		this.src.show();
		this.src.highlight(0);
		this.lang.nextStep();
		
		int depth = GraphBuilder.recDepth(root);
		int size = GraphBuilder.recCountNodes(root, new Accumulator(0)).getValue();
		int leafCount = GraphBuilder.recCountLeafs(root, new Accumulator(0)).getValue();
		MultipleChoiceQuestionModel knapsackCountQuestion = new MultipleChoiceQuestionModel("knapsackCountQuestion");
		knapsackCountQuestion.setPrompt("Before we start: How many knapsack configurations do you think we will get while running this algorithm?"
				+ "(Hint: Look at the recursion anchor of the pseudo-code and the binary tree on the right)");
		String wrongAnswerText = "Wrong. The number of knapsack configurations equals the number of leafs of the binary tree."
				+ "Each selection is finished once the recursion anchor is reached, which is always represented as a leaf.";
		knapsackCountQuestion.addAnswer("0", 0, wrongAnswerText);
		knapsackCountQuestion.addAnswer(String.valueOf(leafCount), 1, "Correct. The number of knapsack configurations equals the number of leafs of the binary tree.");
		knapsackCountQuestion.addAnswer(String.valueOf(size), 0, wrongAnswerText);
		knapsackCountQuestion.addAnswer(String.valueOf(depth), 0, wrongAnswerText);
		this.lang.addMCQuestion(knapsackCountQuestion);
		this.lang.nextStep();	
		
		this.src.unhighlight(0);
		this.visualizeBasketBuilding(root, options, options.copy(), this.capacity, aProps, g);
		g.highlightNode(root.id, null, null);
		
		// show result explanation
		this.src.hide();
		SourceCode resultText = this.lang.newSourceCode(new Coordinates(800, 300), "resultDescription",
				null, (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps"));
			resultText.addMultilineCode(this.getResultDescription(), null, null);
	}
	
	public void visualizeBasketBuilding(SimpleNode node, Basket options, Basket optionsIndex, int capacity, ArrayProperties aProps, Graph g) {
		g.highlightNode(node.id, null, null);
		this.currentBasket.hide();
		this.currentBasket = lang.newStringArray(new Coordinates(10, 100), node.getBasket().toArray(), "current", null, aProps);
		this.currentBasket.showIndices(false, null, null);
		this.currentLabel.setText("Current Knapsack - REMAINING-WEIGHT: " + (this.capacity - node.getWeight()), null, null);
		this.src.highlight(1);
		this.lang.nextStep();
		if (options.getBasketSize() == 0) {
			this.src.unhighlight(1);
			this.src.highlight(2);
			this.lang.nextStep();
			
			g.unhighlightNode(node.id, null, null);
			this.src.unhighlight(2);
			node.basketViz = lang.newStringArray(this.finishedBasketsAnchor,  node.getBasket().toArray(), "result_" + node.id, null, aProps);
			node.basketViz.showIndices(false, null, null);
			this.finishedBasketsAnchor = new Coordinates(this.finishedBasketsAnchor.getX(), this.finishedBasketsAnchor.getY() + 50);
			return;
		} else {
			this.src.unhighlight(1);
			this.src.highlight(3);
			this.lang.nextStep();
			
			this.src.unhighlight(3);
			this.src.highlight(4);
			Item current = options.grabFirstItem();
			this.optionBasket.highlightCell(optionsIndex.getIndex(current), null, null);
			this.lang.nextStep();
			
			this.src.unhighlight(4);
			this.src.highlight(5);
			this.lang.nextStep();
			
			if (current.getWeight() <= capacity) {				
				this.src.unhighlight(5);
				this.src.highlight(6);
				this.lang.nextStep();
				
				SimpleNode left = node.getLeft();
				SimpleNode right = node.getRight();
				
				// with
				this.src.unhighlight(6);
				g.unhighlightNode(node.id, null, null);
				this.visualizeBasketBuilding(left, options.copy(), optionsIndex, capacity - current.getWeight(), aProps, g);
				g.highlightNode(node.id, null, null);
				// display current basket again when jumping back up
				this.currentBasket.hide();
				this.currentBasket = lang.newStringArray(new Coordinates(10, 100), node.getBasket().toArray(), "current", null, aProps);
				this.currentBasket.showIndices(false, null, null);
				this.lang.nextStep();
				
				this.src.highlight(7);
				this.lang.nextStep();
				
				// without
				this.optionBasket.unhighlightCell(optionsIndex.getIndex(current), null, null);
				this.src.unhighlight(7);
				g.unhighlightNode(node.id, null, null);
				this.visualizeBasketBuilding(right, options.copy(), optionsIndex, capacity, aProps, g);
				this.src.highlight(8);
				g.highlightNode(node.id, null, null);
				this.lang.nextStep();
				
				// compare
				g.unhighlightNode(node.id, null, null);
				left.basketViz.highlightCell(0, left.basketViz.getLength()-1, null, null);
				right.basketViz.highlightCell(0, right.basketViz.getLength()-1, null, null);
				g.highlightNode(left.id, null, null);
				g.highlightNode(right.id, null, null);
				this.lang.nextStep();
				
				Basket leftBasket = left.getBasket();
				Basket rightBasket = right.getBasket();
				SimpleNode max = leftBasket.getBasketValue() >= rightBasket.getBasketValue() ? left : right;
				
				// only add question once
				if (!this.addedQuestions.contains("maxQuestion")) {
					MultipleChoiceQuestionModel maxQuestion = new MultipleChoiceQuestionModel("maxQuestion");
					maxQuestion.setPrompt("Which of the two selections will bubble up the tree?\n"
							+ "1: " + leftBasket.toString() + "\n" + "2: " + rightBasket.toString());
					if (left.id != max.id) {
						maxQuestion.addAnswer("1", 0, "Wrong. The value of knapsack 2 (" + rightBasket.getBasketValue() + ") is"
								+ " bigger than that of knapsack 1 (" + leftBasket.getBasketValue() + ")");
						maxQuestion.addAnswer("2", 1, "Correct.");
					} else {
						maxQuestion.addAnswer("1", 1, "Correct.");
						maxQuestion.addAnswer("2", 0, "Wrong. The value of knapsack 1 (" + leftBasket.getBasketValue() + ") is"
								+ " bigger than that of knapsack 2 (" + rightBasket.getBasketValue() + ")");
					}
					this.addedQuestions.add("maxQuestion");
					this.lang.addMCQuestion(maxQuestion);
					this.lang.nextStep();	
				}
				
				left.basketViz.unhighlightCell(0, left.basketViz.getLength()-1, null, null);
				right.basketViz.unhighlightCell(0, right.basketViz.getLength()-1, null, null);
				g.unhighlightNode(left.id, null, null);
				g.unhighlightNode(right.id, null, null);
				
				// unhighlight subtree of not chosen node
				if (left.id != max.id) {
					this.unHighlightSubTree(left, g);
				} else {
					this.unHighlightSubTree(right, g);
				}
				
				g.highlightNode(max.id, null, null);
				node.basketViz = max.basketViz;
				node.setBasket(max.getBasket());
				node.basketViz.highlightCell(0, node.basketViz.getLength()-1, null, null);
				this.lang.nextStep();
				
				g.highlightNode(node.id, null, null);
				this.lang.nextStep();
				
				this.src.unhighlight(8);
				g.unhighlightNode(node.id, null, null);
				
				return;
			} else {
				if (!this.addedQuestions.contains("skipQuestion")) {
					MultipleChoiceQuestionModel skipQuestion = new MultipleChoiceQuestionModel("skipQuestion");
					skipQuestion.setPrompt("Will the current item option (" + current.getName() + ") be included in the current knapsack selection?");
					skipQuestion.addAnswer("Yes", 0, "Wrong. If we would add the item to the current knapsack, it would exceed the remaining max weight of " + capacity + ".");
					skipQuestion.addAnswer("No", 1, "Correct. As you can see, the current items weight ("
							+ current.getWeight() + ") would exceed the current selections max weight (" + capacity + ")");
					this.addedQuestions.add("skipQuestion");
					this.lang.addMCQuestion(skipQuestion);
					this.lang.nextStep();	
				}
				// skip item
				this.src.unhighlight(5);
				this.src.highlight(9);
				this.lang.nextStep();
				
				this.src.unhighlight(9);
				this.src.highlight(10);
				this.lang.nextStep();
				
				this.optionBasket.unhighlightCell(optionsIndex.getIndex(current), null, null);
				this.src.unhighlight(10);
				this.src.highlight(11);
				this.lang.nextStep();
				
				this.src.unhighlight(11);
				this.visualizeBasketBuilding(node, options, optionsIndex, capacity, aProps, g);
			}
		}
	}
	
	private void unHighlightSubTree(SimpleNode node, Graph g) {
		g.unhighlightNode(node.id, null, null);
		if (node.getLeft() != null) {
			this.unHighlightSubTree(node.getLeft(), g);
		}
		if (node.getRight() != null) {
			this.unHighlightSubTree(node.getRight(), g);
		}
		return;
	}
	
	public final static Timing defaultDuration = new TicksTiming(30);
	
    public void init(){
        this.lang = new AnimalScript("Knapsack Problem (recursive)", "Authors", 800, 600);
        this.lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        this.items = (String[][])primitives.get("Items");
        this.capacity = (Integer)primitives.get("Weight");
        
        this.addedQuestions = new ArrayList<String>();
        
        this.lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        this.prepareSrc(props);
		this.prepare(props);
		this.lang.finalizeGeneration();
        
        return lang.toString();
    }

    public String getName() {
        return "Knapsack Problem";
    }

    public String getAlgorithmName() {
        return "Knapsack Problem (recursive)";
    }

    public String getAnimationAuthor() {
        return "Alexander Appel, Seynab Mohammadkia";
    }
    
    private String getIntro1() {
    	return "The following visualization shows a knapsack problem solving algorithm step by step from the starting recursion to the finished\n"
    			+ "optimal item selection.\n"
    			+ "On the left part of the screen you will see the item options above the 'current' selection and 'finished' selections of the algorithm.\n"
    			+ "Item selection example (w = weight, v = value) - REMAINING WEIGHT: 6";
    }
    
    private String getIntro2() {
    	return "As you can see above, each selection contains a set of items with respective weight and value numbers to show how heavy the knapsack is currently\n"
    			+ "and how much remaining weight is left to be filled in the next step.\n"
    			+ "'Current' selection means the already chosen items at the currently active (and highlighted) node of the recursion graph shown on the right.\n"
    			+ "'Finished' selection means the algorithm reached it's recursion anchor, hence finishing a knapsack selection and returning the value for comparison.\n"
    			+ "Each step into the recursion will get highlighted in the graph and pseudo-code (also diplayed on the right) to help better understand how the algorithm\n"
    			+ "builds it's selections and compares them to ultimately decide the optimal result.\n"
    			+ "The result will be highlighted in the finished selections together with the graphs root at the end of execution.";
    }

    public String getDescription(){
        return "The knapsack problem describes a situation in which you have a container (e.g. knapsack) which can carry a certain amount of weight and an option of items\n"
        		+ "from which you can choose from to put into your container. Each item also has a certain weight and value. The goal is to pick a set of items which\n"
        		+ "fills your container while equally providing the most cumulated value for you.\n"
        		+ "The algorithm shown here will create a binary decision tree while looking at each item in your options recursively. In each steps the algorithm calculates\n"
        		+ "a potential container load with and without the chosen item, ending with a collection of all possible choice configurations.\n"
        		+ "The algorithm ultimately picks the configuration with the highest total value and returns it.\n";
    }
    
    private String getResultDescription() {
    	return "As you can see, the algorithm built a decision tree recursively. Each leaf shows a valid\n"
    			+ "selection choice from which the result can be selected from.\n"
    			+ "The marked path shows the best choice bubbling from it's leaf up to the root, getting\n"
    			+ "compared to other choices at each passing node. The result arriving at the root is the\n"
    			+ "maximum accumulated value of chosen items within the given weight of our knapsack.\n"
    			+ "Note that for each added option item, the tree grows in depth with a potential of 2^n (n = depth) nodes.\n";
    }

    public String getCodeExample(){
        return "public int recKnapsack(int capacity, int value, List options) {\n"
        	+ " if options are empty {\n"
        	+ "  treturn value\n"
			+ " } else {\n"
			+ "  choose first item in options\n"
			+ "  if capacity <= item.weight {\n"
			+ "   with = recKnapsack(capacity - item.weight, value + item.value, options)\n"
			+ "   without = recKnapsack(capacity, value, options)\n"
			+ "   return max(with, without)\n"
			+ "  } else {\n"
			+ "   skip the item\n"
			+ "   return recKnapsack(capacity, value, options)\n"
			+ "  }\n"
			+ " }\n"
    		+ "}\n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }
    
    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
    	String[][] items = (String[][])primitives.get("Items");
    	if (items.length == 3) {
    		int sum = 0;
    		for (String[] item: items) {
    			sum += item.length;
    		}
    		if (sum == 5 * 3) {
    			return true;
    		}
    	}
    	return false;
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
}
