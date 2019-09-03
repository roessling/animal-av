package htdptl.stepper;

import htdptl.ast.AST;
import htdptl.exceptions.StepException;
import htdptl.parser.Parser;

public class MapDefiner {

	private AST redex;

	/**
	 * Is called if the redex is a map call. A definition of map with
	 * corresponding formal parameters (i.e. number of lists) is put into the
	 * definitions map
	 * 
	 * @throws StepException
	 * @throws StepException
	 * 
	 */
	public MapDefiner(Stepper stepper) throws StepException {

		redex = stepper.getRedex();

		// check if all lists are equal in length
		int length = redex.getChild(1).numChildren();
		for (int i = 2; i < redex.numChildren(); i++) {
			if (length != redex.getChild(i).numChildren()) {
				throw new StepException(
						"Map was called with lists of different length!");
			}
		}
		
		
		String map;
		// (define (map f list) (if (empty? list) empty (cons (f (first list)) (map f (rest list)))))
		if (redex.numChildren()==2) {
			 map = "(define (map f list) (if (empty? list) empty (cons (f (first list)) (map f (rest list)))))";
		}
		// (define
		// (map f list)
		// (if (empty? list1) empty
		// (cons 
		// (f (first list1) (first list2) ... ) 
		// (map f (rest list1) (rest list2) ... ))))
		else {
			map = "(define (map f ";
			map = append(map, "", "") + ") ";

			map += "(if (empty? list1) empty (cons (f ";
			map = append(map, "(first ", ")") + ") ";

			map += "(map f ";
			map = append(map, "(rest ", ")") + "))))";
		}

		stepper.defineSymbol(Parser.parse(map));

	}

	private String append(String map, String prefix, String suffix) {
	  String myMap = map;
		for (int i = 1; i < redex.getChildren().size(); i++) {
		  myMap += prefix + "list" + i + suffix;
			if (i + 1 < redex.getChildren().size()) {
			  myMap += " ";
			}
		}
		return myMap;
	}

}
