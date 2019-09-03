package htdptl.test;

import htdptl.exceptions.NoExpressionsException;
import htdptl.exceptions.TraceTooLargeException;
import htdptl.facade.Facade;

import java.io.IOException;

import algoanim.exceptions.IllegalDirectionException;


public class FacadeTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws IllegalDirectionException 
	 * @throws NoExpressionsException 
	 * @throws TraceTooLargeException 
	 */
	public static void main(String[] args) throws IllegalDirectionException, IOException, NoExpressionsException, TraceTooLargeException {
		
		//String expression = "(+ (* 4 3 (+ 2 2)) (+ 3 3))";
		//String expression = "(rest (cons 2 empty))";
		//String expression = "(list-length (list 1 2 3))";		
		
		
		String expression = "(define (list-length lst) (if (empty? lst) 0 (+ 1 (list-length (rest lst)))))\n";
		expression += "(define (f a b) (* (+ b a) (- a b)))\n";
		expression += "(define (my-max a b) (if (> a b) a b))\n";
		// expression += "(define-struct point (x y))\n";
		
		// expression += "(list-length (map my-max (list 1 2 3) (list 4 5 6)))\n";
		//expression += "(list-length empty)\n";
		//expression += "(list-length (list 1))\n";
		//expression += "(list-length (list 1 2 3))\n";
		expression += "(foldl max 0 (list 1 2 3))";
		
		// expression += "(+ 1 (list-length (list 1.0 2.0 3.0)))";
		// expression += "(foldl - 0 (list 1 2 3))\n";
		// expression += "(+ 2 (foldr - 0 (list 1 2 3)))\n";
		// expression += "(rest (map my-max (list 1 2 3) (list 3 2 1)))";
		// expression += "(+ 3 (my-max 3 0))";
		
		// expression += "(foldl + 0 (list 1 2 3 4))";
		// expression += "(cond [(= 2 1) 1] [(< 1 2) 3] [else 4])";
		// expression += "(point-x (make-point 3 4))";
			
		
		//String expression = "(foo 1 2 3)";		


		// String expression = "(+ 1 (my-max 1 2))";
		
		Facade facade = new Facade();
		facade.input(expression);
		facade.animate();
		System.out.println(facade.getScriptCode());
	}

	
	
}
