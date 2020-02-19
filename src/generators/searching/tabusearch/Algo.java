package generators.searching.tabusearch;

import java.awt.Color;
import java.util.ArrayList;

import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Timing;

/**
 * This class contains the methods that are called by the algorithm in the
 * TabuSearch class. The concept is to keep the actual code clean and easily
 * readable in the TabuSearch class; in this class the methods are filled with
 * the actual content and the needed AlgoAnim transformations.
 * 
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 *
 */
public class Algo {

	/**
	 * Reference on tabu search instance
	 */
	public static TabuSearchGenerator tabu;

	/**
	 * Creates a randomly chosen roundtrip to begin with.
	 */
	public static void getRandomSolution() {

		// step
		tabu.lang.nextStep("Initialisierung");

		// get random permutation
		ArrayList<Integer> tour = new ArrayList<Integer>();
		for (int i = 1; i < Input.getNumberOfNodes(); i++)
			tour.add(i);
		java.util.Collections.shuffle(tour);

		// create and show current list
		tabu.currentSolList.put(0, Helper.nodeLabels[0], null, null);
		for (int i = 0; i < tour.size(); i++)
			tabu.currentSolList.put(i + 1, Helper.nodeLabels[tour.get(i)], null, null);
		tabu.currentSolList.show();

		// create and show current costs
		int costs = Helper.getSolutionCosts(tabu.currentSolList);
		tabu.currentSolCost.setText("Kosten: " + costs, null, null);
		tabu.currentSolCost.show();

		// create and show current graph
		tabu.currentSolGraph.show();
		Helper.showEdges(tabu.currentSolGraph, tabu.currentSolList);

		// highlight current label, list and code line 0
		tabu.currentSolLabel.changeColor(null,
				(Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		tabu.currentSolList.highlightCell(0, tabu.currentSolList.getLength() - 1, null, null);
		tabu.tabuCode.highlight(0);

	}

	/**
	 * Stores the current solution as the global best solution (to this point).
	 * 
	 * @param firstCall
	 *            Indicates whether the method is called in the beginning or later
	 *            in the for-loop (some minor changes have to be considered there)
	 */
	public static void setBestToCurrentSol(boolean firstCall) {

		// step
		tabu.lang.nextStep();

		// cleanup
		tabu.currentSolLabel.changeColor(null, Color.black, null, null);

		// create and show best list
		for (int i = 0; i < Input.getNumberOfNodes(); i++)
			tabu.bestSolList.put(i, tabu.currentSolList.getData(i), null, null);
		tabu.bestSolList.show();

		// create and show best costs
		tabu.bestSolCost.setText(tabu.currentSolCost.getText(), null, null);
		tabu.bestSolCost.show();

		// create and show best graph
		Helper.showEdges(tabu.bestSolGraph, tabu.bestSolList);
		tabu.bestSolGraph.show();

		// unhighlight best sol cost
		tabu.bestSolCost.changeColor(null, Color.black, null, null);

		// highlight best label, list and code line 1 (or line 7 if not first call)
		tabu.bestSolLabel.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY),
				null, null);
		tabu.bestSolList.highlightCell(0, tabu.bestSolList.getLength() - 1, null, null);
		if (firstCall) {
			tabu.tabuCode.unhighlight(0);
			tabu.tabuCode.highlight(1);
		} else {
			tabu.tabuCode.unhighlight(6);
			tabu.tabuCode.highlight(7);
		}
	}

	/**
	 * Generates all neighbored roundtrips for the current one.
	 */
	public static void getNeighborhood() {

		// step
		tabu.lang.nextStep();

		// cleanup
		tabu.bestSolLabel.changeColor(null, Color.black, null, null);
		tabu.currentSolList.unhighlightCell(0, tabu.currentSolList.getLength() - 1, null, null);
		tabu.bestSolList.unhighlightCell(0, tabu.bestSolList.getLength() - 1, null, null);
		tabu.tabuListLabel.changeColor(null, Color.black, null, null);
		tabu.tabuList.unhighlightElem(0, tabu.tabuList.getLength() - 1, null, null);
		if (tabu.currentNeighborLists != null)
			for (StringArray neighborList : tabu.currentNeighborLists) {
				neighborList.hide();
				neighborList.hide(Timing.INSTANTEOUS);
			}
		if (tabu.currentNeighborCosts != null)
			for (Text neighborCost : tabu.currentNeighborCosts)
				neighborCost.hide();
		if (tabu.currentNeighborSwaps != null)
			for (Text neighborSwap : tabu.currentNeighborSwaps)
				neighborSwap.hide();

		// highlight code line 2 (for loop)
		for (int i = 0; i < tabu.tabuCode.length(); i++)
			tabu.tabuCode.unhighlight(i);
		Helper.updateCode();
		tabu.tabuCode.highlight(2);

		// step
		tabu.lang.nextStep(++Helper.iterationCount + ". Iterationsschritt");

		// generate and show neighbors
		Helper.generateNeighborhood();

		for (StringArray n : tabu.currentNeighborLists) {
			n.show();
		}

		// highlight neighborhood label and code line 3
		tabu.neighborhoodLabel.changeColor(null,
				(Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		tabu.tabuCode.unhighlight(2);
		tabu.tabuCode.highlight(3);
	}

	/**
	 * Checks the current tabu list and removes all occurring neighbors in the
	 * current neighborhood from further consideration.
	 */
	public static void removeTabus() {

		// step
		tabu.lang.nextStep();

		// cleanup
		tabu.neighborhoodLabel.changeColor(null, Color.black, null, null);

		// highlight tabu list label and elements
		tabu.tabuListLabel.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY),
				null, null);
		tabu.tabuList.setHighlightTextColor(0, tabu.tabuList.getLength() - 1,
				(Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		tabu.tabuList.highlightElem(0, tabu.tabuList.getLength() - 1, null, null);

		// highlight neighbors that are affected by tabu list
		for (int i = 0; i < tabu.tabuList.getLength(); i++) {

			// if there is a match in neighbor list for this tabu
			Neighbor n = Helper.findTabuNeighbor(tabu.tabuList.getData(i));
			if (n != null) {

				// highlight this neighbor's list and swap
				n.list.setHighlightFillColor(0, n.list.getLength() - 1, Color.red, null, null);
				n.list.highlightCell(0, n.list.getLength() - 1, null, null);
				n.swapText.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY),
						null, null);

				// remove this neighbor from consideration
				tabu.currentNeighbors.remove(n);
			}
		}

		// highlight code line 4
		tabu.tabuCode.unhighlight(3);
		tabu.tabuCode.highlight(4);
	}

	/**
	 * Selects the best roundtrip of the remaining neighbors in the neighborhood
	 * (after tabus are deleted).
	 */
	public static void getBestNeighbor() {

		// step
		tabu.lang.nextStep();

		// cleanup
		tabu.tabuListLabel.changeColor(null, Color.black, null, null);
		tabu.tabuList.unhighlightElem(0, tabu.tabuList.getLength() - 1, null, null);
		for (Text swap : tabu.currentNeighborSwaps)
			swap.changeColor(null, Color.black, null, null);

		// get best of remaining neighbors
		Neighbor best = Helper.getBestNeighbor();

		// set current graph as previous graph
		Helper.showEdges(tabu.prevSolGraph, tabu.currentSolList);
		tabu.prevSolGraph.show();

		// create and set best neighbor graph as new current graph
		Helper.showEdges(tabu.currentSolGraph, best.list);

		// update previous solution list and cost
		for (int i = 0; i < tabu.currentSolList.getLength(); i++)
			tabu.prevSolList.put(i, tabu.currentSolList.getData(i), null, null);
		tabu.prevSolCost.setText(tabu.currentSolCost.getText(), null, null);
		tabu.prevSolList.show();
		tabu.prevSolCost.show();

		// update current solution list and cost
		for (int i = 0; i < best.list.getLength(); i++)
			tabu.currentSolList.put(i, best.list.getData(i), null, null);
		tabu.currentSolCost.setText(best.costText.getText(), null, null);

		// highlight best neighbor list and cost
		best.list.highlightCell(0, best.list.getLength() - 1, null, null);
		best.costText.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
				null);

		// highlight current solution label, list and cost
		tabu.currentSolLabel.changeColor(null,
				(Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		tabu.currentSolList.highlightCell(0, tabu.currentSolList.getLength() - 1, null, null);
		tabu.currentSolCost.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY),
				null, null);

		// highlight prev solution label
		tabu.prevSolLabel.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY),
				null, null);

		// highlight code line 5
		tabu.tabuCode.unhighlight(4);
		tabu.tabuCode.highlight(5);
	}

	/**
	 * Checks if the current roundtrip is better than the currently stored global
	 * best one.
	 * 
	 * @return true if current solution is better than global best, false otherwise
	 */
	public static boolean currentBetterThanBest() {

		// step
		tabu.lang.nextStep();

		// cleanup
		tabu.currentSolLabel.changeColor(null, Color.black, null, null);
		tabu.prevSolLabel.changeColor(null, Color.black, null, null);
		Neighbor best = Helper.getBestNeighbor();
		best.costText.changeColor(null, Color.black, null, null);

		// highlight best sol cost
		tabu.bestSolCost.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY),
				null, null);

		// highlight code line 6
		tabu.tabuCode.unhighlight(5);
		tabu.tabuCode.highlight(6);

		// return true if current is better than global best, else return false
		if (Helper.currentIsBetterThanGlobalBest())
			return true;
		return false;
	}

	/**
	 * Puts swap of last chosen solution on tabu list.
	 */
	public static void updateTabuList() {

		// step
		tabu.lang.nextStep();

		// cleanup
		tabu.currentSolList.unhighlightCell(0, tabu.currentSolList.getLength() - 1, null, null);
		tabu.bestSolList.unhighlightCell(0, tabu.bestSolList.getLength() - 1, null, null);
		tabu.currentSolCost.changeColor(null, Color.black, null, null);
		tabu.bestSolCost.changeColor(null, Color.black, null, null);
		tabu.bestSolLabel.changeColor(null, Color.black, null, null);

		// add best neighbor swap to tabulist queue
		Neighbor best = Helper.getBestNeighbor();
		String swapReverse = Helper.reverseSwap(best.swap);
		if (!tabu.tabuListQueue.offer(swapReverse)) {
			tabu.tabuListQueue.poll();
			tabu.tabuListQueue.offer(swapReverse);
		}

		// update tabu list
		int cnt = 0;
		for (String s : tabu.tabuListQueue)
			tabu.tabuList.put(cnt++, s, null, null);

		// highlight best neighbor swap
		best.swapText.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
				null);

		// highlight tabu list label and elements
		tabu.tabuListLabel.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY),
				null, null);
		tabu.tabuList.setHighlightTextColor(0, tabu.tabuList.getLength() - 1,
				(Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		tabu.tabuList.highlightElem(0, tabu.tabuList.getLength() - 1, null, null);

		// highlight code line 9
		tabu.tabuCode.unhighlight(6);
		tabu.tabuCode.unhighlight(7);
		tabu.tabuCode.highlight(8);
	}

	/**
	 * Highlights the globally best found solution after the end of the for-loop.
	 */
	public static void returnResult() {
		// step
		tabu.lang.nextStep();

		// cleanup
		tabu.bestSolLabel.changeColor(null, Color.black, null, null);
		tabu.currentSolList.unhighlightCell(0, tabu.currentSolList.getLength() - 1, null, null);
		tabu.bestSolList.unhighlightCell(0, tabu.bestSolList.getLength() - 1, null, null);
		tabu.tabuListLabel.changeColor(null, Color.black, null, null);
		tabu.tabuList.unhighlightElem(0, tabu.tabuList.getLength() - 1, null, null);
		if (tabu.currentNeighborLists != null)
			for (StringArray neighborList : tabu.currentNeighborLists) {
				neighborList.hide();
				neighborList.hide(Timing.INSTANTEOUS);
			}
		if (tabu.currentNeighborCosts != null)
			for (Text neighborCost : tabu.currentNeighborCosts)
				neighborCost.hide();
		if (tabu.currentNeighborSwaps != null)
			for (Text neighborSwap : tabu.currentNeighborSwaps)
				neighborSwap.hide();

		// highlight code line 2 (for loop)
		for (int i = 0; i < tabu.tabuCode.length(); i++)
			tabu.tabuCode.unhighlight(i);
		Helper.updateCode();
		tabu.tabuCode.highlight(2);

		// step
		tabu.lang.nextStep();

		// highlight code line 10
		tabu.tabuCode.unhighlight(2);
		tabu.tabuCode.highlight(10);

		// highlight best sol label and list
		tabu.bestSolLabel.changeColor(null, (Color) Input.getLabelProps().get(AnimationPropertiesKeys.COLOR_PROPERTY),
				null, null);
		tabu.bestSolList.highlightCell(0, tabu.bestSolList.getLength() - 1, null, null);

		// step
		tabu.lang.nextStep("Ergebnis");

		// unhighlight code
		tabu.tabuCode.unhighlight(10);

		// unhighlight best sol label and list
		tabu.bestSolLabel.changeColor(null, Color.BLACK, null, null);
		tabu.bestSolList.unhighlightCell(0, tabu.bestSolList.getLength() - 1, null, null);
	}
}
