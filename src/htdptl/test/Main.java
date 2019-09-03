package htdptl.test;

import htdptl.exceptions.NoExpressionsException;
import htdptl.facade.Facade;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import algoanim.exceptions.IllegalDirectionException;

public class Main {

	public static void main(String[] args) throws IllegalDirectionException,
			IOException, NoExpressionsException {

		HashMap<String, String> expressions = new HashMap<String, String>();

		String definitions = "(define (my-max a b) (if (> a b) a b))\n";
		definitions += "(define (list-length lst) (if (empty? lst) 0 (+ 1 (list-length (rest lst)))))\n";
		definitions += "(define (f a b) (* (+ b a) (- a b)))\n";

		expressions.put("Map", "(map my-max (list 1 2 3) (list 3 2 1))");
		expressions.put("MapSquare", "(map sqr (list 1 2 3))");

		expressions
				.put(
						"AreaOfRing",
						"(define (area-of-disk r) (* 3.14 (* r r))) \n"
								+ "(area-of-disk 5) \n "
								+ "(define (area-of-ring outer inner) (- (area-of-disk outer) (area-of-disk inner))) \n"
								+ "(area-of-ring 5 3)");

		expressions.put("foldr", "(+ 2 (foldr - 0 (list 1 2 3 4)))\n");
		expressions.put("foldl", "(+ 2 (foldl - 0 (list 1 2 3 4)))\n");
		expressions.put("listlength_and_Map",
				"(list-length (map my-max (list 1 2 3) (list 4 5 6)))");
		expressions.put("fold_with_f", "(foldl f 0 (list 1 2 3 4))");

		for (Iterator<String> iterator = expressions.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			Facade facade = new Facade();
			facade.input(definitions + expressions.get(key));
			System.out.println(key);
			File file = new File("D:/Eigene Dateien/" + key + ".asu");
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(facade.getScriptCode());
			bw.close();
		}

	}

}
