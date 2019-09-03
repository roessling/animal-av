import java.util.Vector;

import animal.animator.ColorChanger;
import animal.animator.DepthChanger;
import animal.animator.Highlight;
import animal.animator.HighlightEdge;
import animal.animator.Move;
import animal.animator.PropertyChanger;
import animal.animator.Put;
import animal.animator.Rotate;
import animal.animator.Scale;
import animal.animator.SetFont;
import animal.animator.SetText;
import animal.animator.Swap;
import animal.animator.TimedAnimator;
import animal.animator.TimedShow;
import animal.graphics.PTArc;
import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraph;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTIntArray;
import animal.graphics.PTNode;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.graphics.PTStringArray;
import animal.graphics.PTText;
import animal.handler.GraphicObjectHandler;
import animal.main.Animal;

/*
 * Created on 17.05.2007 by Guido Roessling (roessling@acm.org>
 */

public class FunctionTester {
	public FunctionTester() {
		Animal animal = Animal.get();
		animal.init();
		Vector<PTGraphicObject> ptgos = getGraphicalObjects();
		Vector<TimedAnimator> animators = getAnimators();
		for (TimedAnimator animator : animators) {
			for (PTGraphicObject ptgo : ptgos) {
				printMethods(testFunctions(ptgo, animator), ptgo, animator);
			}
		}
	}

	/**
	 * @return
	 */
	private Vector<PTGraphicObject> getGraphicalObjects() {
		Vector<PTGraphicObject> ptgos = new Vector<PTGraphicObject>(20);
		ptgos.add(new PTArc());
		ptgos.add(new PTBoxPointer());
		ptgos.add(new PTGraph());
		ptgos.add(new PTIntArray(new int[] {1,2,3,4,5}));
		ptgos.add(new PTNode());
		ptgos.add(new PTPoint());
		ptgos.add(new PTPolyline());
		ptgos.add(new PTStringArray(new String[] {"a","b","c","d","e"}));
		ptgos.add(new PTText());
		return ptgos;
	}
	
	private Vector<TimedAnimator> getAnimators() {
		Vector<TimedAnimator> animators = new Vector<TimedAnimator>(30);
		animators.add(new ColorChanger());
		animators.add(new DepthChanger());
		animators.add(new Highlight());
		animators.add(new HighlightEdge());
		animators.add(new Move());
		animators.add(new PropertyChanger());
		animators.add(new Put());
		animators.add(new Rotate());
		animators.add(new Scale());
		animators.add(new SetFont());
		animators.add(new SetText());
		animators.add(new Swap());
		animators.add(new TimedShow());
		return animators;
	}
	
	private String[] testFunctions(PTGraphicObject ptgo, 
			TimedAnimator animator) {
		GraphicObjectHandler handler = ptgo.getHandler();
		Vector<String> methodNames = handler.getMethods(ptgo, 
				animator.getProperty(0.5));
		String[] methodNameArray = new String[methodNames.size()]; 
		methodNames.copyInto(methodNameArray);
		return methodNameArray;
	}
	
	private void printMethods(String[] methodNames, PTGraphicObject ptgo,
			TimedAnimator animator) {
		System.err.println("Method for type " +ptgo.getType() +", animator "
				+animator.getAnimatorName());
		for (String elem : methodNames) 
			System.err.println(elem);
	}
	
	
	public static void main(String[] args) {
		new FunctionTester();
	}
}
