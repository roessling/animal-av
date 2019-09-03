/*
 * Created on 18.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.searching.bruteforce;

import generators.framework.Generator;
import generators.framework.properties.AnimationPropertiesContainer;

import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class BruteForceStringSearching extends
    AbstractBruteForceStringSearching implements Generator {
	
	private StringArray searchString = null;
	private Text searchText = null;

  public BruteForceStringSearching(String aResourceName,
      Locale aLocale) {
    super(aResourceName, aLocale);
  }

  /**
   * generates the animation
   * 
   * @param props
   *          the properties given by the animation viewer
   * @param prims
   *          the primitive objects as given by the animation viewer
   * @return the String output for the animation
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
	  
	  setUpDefaultElements(props, prims, "array", "code", "code", 0, 20);
	  String valueToSearchFor = (String) prims.get("value");
	  lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	  
	  // int resultIndex =
	  search(valueToSearchFor);
	  wrapUpAnimation();
	  lang.finalizeGeneration();
	  return lang.getAnimationCode();
  }

  protected int search(String valueToSearchFor) {
	  ArrayMarker lMarker = null, rMarker = null;
	  int length = valueToSearchFor.length();
	  int position = 0, counter = 0;
	  boolean check = false;
	  String[] searchFor = new String[length];
	  
	  for(int i=0; i<length; i++) {
		  searchFor[i] = valueToSearchFor.substring(i, i+1).toUpperCase(locale);
	  }
	  
	  searchText = lang.newText(new Coordinates(500, 180), "Suchstring:", 
				"#A", null, (TextProperties)primitiveProps.get("title"));
	  searchString = lang.newStringArray(new Coordinates(650, 180),
			  searchFor, "searchString", null, array.getProperties());
	    
	  lang.nextStep();
	  
	  // initialize marker
	  lMarker = installArrayMarker("lMarker", array, 0);
	  incrementNrAssignments();
	  rMarker = installArrayMarker("rMarker", searchString, 0);
	  incrementNrAssignments();
      lang.nextStep();
	    
	  // highlight first step
	  code.highlight("line1");
	  lang.nextStep();
	  
	  code.unhighlight("line1");
	  
	  FillInBlanksQuestionModel often = new FillInBlanksQuestionModel("howOften");
	  often.setPrompt("Wie oft geht der Algorithmus zu Schritt 7?");
      lang.addFIBQuestion(often);
      lang.nextStep();
      
	  while(lMarker.getPosition() < array.getLength() - 1 
			  && rMarker.getPosition() < searchString.getLength() - 1) {
		  incrementNrComparisons(2);
		  code.highlight("line2");
		  lang.nextStep();
		  
		  code.toggleHighlight("line2", "line3");
		  array.highlightCell(position, lMarker.getPosition(), null, null);
		  searchString.highlightCell(0, rMarker.getPosition(), null, null);
		  lang.nextStep();
		  
		  // Sind Zeichen gleich, dann zu 7
		  incrementNrComparisons(1);
		  if(array.getData(lMarker.getPosition()).equals(searchString.getData(rMarker.getPosition()))) {
			  incrementNrComparisons(1);
			  if(!check) {
				  position = lMarker.getPosition();
				  incrementNrAssignments();
				  check = true;
				  incrementNrAssignments();
			  }
			  
			  code.toggleHighlight("line3", "line31");
			  lang.nextStep();
			  
			  code.toggleHighlight("line31", "line7");
			  counter++;
			  lang.nextStep();
			  
			  lMarker.increment(null, DEFAULT_TIMING);
			  incrementNrAssignments();
			  rMarker.increment(null, DEFAULT_TIMING);
			  incrementNrAssignments();
			  lang.nextStep();
			  
			  code.toggleHighlight("line7", "line8");
			  lang.nextStep();
			  
			  code.unhighlight("line8");
		  } else {
			  code.toggleHighlight("line3", "line4");
			  lang.nextStep();
			  
			  lMarker.move(position + 1, null, DEFAULT_TIMING);
			  incrementNrAssignments();
			  position = lMarker.getPosition();
			  incrementNrAssignments();
			  check = false;
			  incrementNrAssignments();
			  array.unhighlightCell(0, array.getLength() -1, null, null);
			  array.highlightCell(position, lMarker.getPosition(), null, null);
			  lang.nextStep();
			  
			  code.toggleHighlight("line4", "line5");
			  lang.nextStep();
			  
			  rMarker.move(0, null, DEFAULT_TIMING);
			  incrementNrAssignments();
			  searchString.unhighlightCell(0, searchString.getLength() -1, null, null);
			  searchString.highlightCell(0, rMarker.getPosition(), null, null);
			  lang.nextStep();
			  
			  code.toggleHighlight("line5", "line6");
			  lang.nextStep();
			  
			  code.unhighlight("line6");
		  }
	  }
	  often.addAnswer("" + counter, 3, "Der Algorithmus geht insgesamt " + counter + " mal zu Schritt 7");
	  code.highlight("line2");
	  lang.nextStep();
	  
	  code.toggleHighlight("line2", "line21");
	  array.highlightCell(position, lMarker.getPosition(), null, null);
	  searchString.highlightCell(0, rMarker.getPosition(), null, null);
	  lang.nextStep();
	  
	  code.toggleHighlight("line21", "line9");
	  lang.nextStep();
	  
	  HtmlDocumentationModel link = new HtmlDocumentationModel("link");
	  link.setLinkAddress("http://de.wikipedia.org/wiki/String-Matching-Algorithmus");
	  lang.addDocumentationLink(link);
	  lang.nextStep();
	    
	  incrementNrComparisons(1);
	  if(rMarker.getPosition() == searchString.getLength() - 1) {
		  code.toggleHighlight("line9", "line91");
		  lang.nextStep();
		  
		  searchText.hide();
		  searchString.hide();
		  return position;
	  } else {
		  code.toggleHighlight("line9", "line92");
		  lang.nextStep();
		  
		  searchText.hide();
		  searchString.hide();
		  return -1;
	  }
  }
}