package htdptl.test;

import htdptl.exceptions.StepException;
import htdptl.parser.Parser;
import htdptl.stepHandlers.CondHandler;
import htdptl.stepHandlers.IfHandler;
import htdptl.stepper.Stepper;
import htdptl.visitors.VisitorUtil;

public class StepperTest {

	public static void main(String[] args) throws StepException {

		Stepper stepper = new Stepper();

		stepper.addHandler("if", new IfHandler());
		stepper.addHandler("cond", new CondHandler());
		
		
		// stepper.defineSymbol("(define (my-max a b) (if (< a b) a b))\n");
		stepper.defineSymbol(Parser.parse("(define (list-length lst) (if (empty? lst) 0 (+ 1 (list-length (rest lst)))))\n"));
		// stepper.defineSymbol("(define (f a b) (* (+ b a) (- a b)))\n");
		// stepper.setExpression("(my-max 1 2)");
		// stepper.setExpression("(list-length (map + (list 1 2 3) (list 1 2 3)))");
		stepper.setExpression(Parser.parse("(+ 1 (list-length (list 1 2 3)))"));

		// stepper.setExpression("(cond [(= 2 1) 1] [(< 1 2) 3] [else 4])");

		// foldl f 0 (list 1 2 3 4))");

		try {

			int i = 0;
			while (!stepper.isDone()) {
				System.out.println(VisitorUtil.toCode(stepper.getAST()));
				System.out.println(VisitorUtil.toCode(stepper.getRedex()));
				if (stepper.getCurrentDefinition()!=null) {
					System.out.println(stepper.getCurrentDefinition());
				}
				System.out.println();
				stepper.step();

				i++;
			}
			System.out.println(VisitorUtil.toCode(stepper.getAST()));
			System.out.println("i=" +i);
		} catch (StepException e) {
			e.printStackTrace();
		}

	}

}
