/*
 * LinkStateRouting.java
 * Justin Hopp, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.network;

import java.awt.Color;
import java.awt.Font;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.Graph;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import translator.Translator;

public class LinkStateRouting implements ValidatingGenerator {
    private Language lang;
    private int Runden;
    private Graph graph;
    private Translator translator;
    private Locale language;
    private TextProperties textprop;
    private MatrixProperties matrixprop;
    
    public LinkStateRouting(Locale langua){
      translator = new Translator("resources/LinkStateRouting", langua);
      language = langua;
    }

    public void init(){
        lang = new AnimalScript("Link State Routing", "Justin Hopp", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Runden = (Integer)primitives.get("Runden");
        graph = (Graph)primitives.get("graph");      
        textprop = (TextProperties)props.getPropertiesByName("text");
        matrixprop = (MatrixProperties)props.getPropertiesByName("matrix");
        
        LinkStateRoutingInit(graph, Runden);
        
        return lang.toString();
    }
    
    private void LinkStateRoutingInit(Graph graph, int rounds) throws LineNotExistsException {
      
        TextProperties textPropsUeb = new TextProperties();
        textPropsUeb.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font(((Font) textprop.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName(),Font.BOLD,24));
        Text ueberschrift = lang.newText(new Coordinates(0,0), "Link State Routing", "überschrift", null, textPropsUeb);
        Text tb = lang.newText(new Offset(0, 20,"überschrift","SW"), translator.translateMessage("Bschrbng"),"tb", null, textprop);
        Text tb1 = lang.newText(new Offset(0, 5,"tb","SW"), translator.translateMessage("Bschrbng1"),"tb1", null, textprop);
        Text tb2 = lang.newText(new Offset(0, 5,"tb1","SW"), translator.translateMessage("Bschrbng2"),"tb2", null, textprop);
        Text tb3 = lang.newText(new Offset(0, 5,"tb2","SW"), translator.translateMessage("Bschrbng3"),"tb3", null, textprop);
        
        
        lang.nextStep(translator.translateMessage("einfhrung"));
        tb.hide();
        tb1.hide();
        tb2.hide();
        tb3.hide();

    	  GraphProperties graphProps = graph.getProperties();
	      graphProps.setName("graph");
	      graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0,0,0));
	      graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(51,153,255));
	      graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(255,153,0));
	      graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY , new Color(0,0,0));
	      graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, new Color(0,0,0));
	      graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
	      
	      String[] names = new String[graph.getNodes().length];
	      for(int i=0;i<graph.getNodes().length;i++){
	        names[i] = graph.getNodeLabel(i);
	      }
	      Graph graphDisplay = lang.newGraph("graph", graph.getAdjacencyMatrix(), graph.getNodes(), names, null, graphProps);
	      
	      ueberschrift.hide();
	      @SuppressWarnings("unused")
        Text ueberschrift1 = lang.newText(new Offset(0,-80,"graph","NW"), "Link State Routing", "überschrift", null, textPropsUeb);
	      
	      MatrixProperties gridProperties = matrixprop;
	      
	      /* Hardcode Properties
	      gridProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
	      gridProperties.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "left");
	      gridProperties.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, new Color(0,0,0));
	      gridProperties.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, new Color(0,0,0));
	      gridProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(255,153,0));*/
	      
	      int[][] edges = graph.getAdjacencyMatrix();

	      //Kanten in beide Richtungen angeben
	      for(int i=0;i<edges.length;i++){
          for(int j=0;j<edges[i].length;j++){
            if(edges[i][j] != 0){
              edges[j][i] = edges[i][j];
            }
          }
	      }
	    //Wieviele Kanten gibt es?
        int numberEdges = 0;
        for(int i=0;i<edges.length;i++){
          for(int j=0;j<edges[i].length;j++){
            if(edges[i][j] != 0){
              numberEdges++;
            }
          }
        }
        numberEdges = (numberEdges / 2);
	      //Daten die am Anfang in der Tabelle stehen
	      int nodeCount = graph.getNodes().length;
	      String[][] data = new String[numberEdges+1][nodeCount];
	      for(int i=0;i<nodeCount;i++){
	        data[0][i] = names[i];
	      }
	      for(int i=1;i<numberEdges+1;i++){
	        for(int j=0;j<nodeCount;j++){
	          data[i][j] = "0";
	        }
	      }
	      StringMatrix sm = lang.newStringMatrix(new Offset(70,0,"graph","NE"), data, "tb1", null, gridProperties);   
	      
	      Text t = lang.newText(new Offset(0,50,"graph","SW"), translator.translateMessage("runde") +" 0", "textpanel", null, textprop);
	      Text t1 = lang.newText(new Offset(0,10,"textpanel","SW"), "", "textpanel1", null, textprop);
	      
	      LinkStateRoutingProcess(numberEdges, nodeCount, rounds, graph.getAdjacencyMatrix(), graphDisplay, sm, t, t1);
    }
    
    //Welcher Id hat der Knoten mit dem Label X
    private int getNumberOfNode(int nodeCount, char label){
      for(int i=0;i<nodeCount;i++){
        if(graph.getNodeLabel(i).toCharArray()[0] == label){
          return i;
        }
      }
      return 0;
    }
    
    private void LinkStateRoutingProcess(int numberEdges, int nodeCount, int rounds, int[][] arrayEdge, Graph graph, StringMatrix sm, Text t, Text t1){
      String[][] wissen = new String[nodeCount][numberEdges];
      String[][] packages = new String[nodeCount][numberEdges];
      String[][] tmppackages = new String[nodeCount][numberEdges];
      
      int round = 0;
      int counter = 0;
      boolean wissenVoll = true;
      
      //Null setzen der Arrays
      for(int i=0;i<nodeCount;i++){
        for(int j=0;j<numberEdges;j++){
          wissen[i][j] = "nothing";
          packages[i][j] = "nothing";
          tmppackages[i][j] = "nothing";
        }
      }      

      //Am Anfang bekannte Kanten der einzelnen Knoten
      for(int i=0;i<nodeCount;i++){
        for(int j=0;j<nodeCount;j++){
          if(arrayEdge[i][j] != 0){
            wissen[i][counter] = "" + graph.getNodeLabel(i) + "," + arrayEdge[i][j] + "," + graph.getNodeLabel(j);
            packages[i][counter] = "" + graph.getNodeLabel(i) + "," + arrayEdge[i][j] + "," + graph.getNodeLabel(j);
            counter++;
          }
        }
        counter = 0;
      }
      
      //Anfangszeichnung
      lang.nextStep();
      for(int i=0;i<nodeCount;i++){
        for(int j=0;j<numberEdges;j++){
          if(wissen[i][j].compareTo("nothing") != 0){
            sm.put(j+1, i, wissen[i][j], null, null);
            sm.highlightCell(j+1, i, null, null);
          }
        }
      }
      lang.nextStep(translator.translateMessage("initialisierung"));
      //demarkieren der Elemente
      for(int i=0;i<nodeCount;i++){
        for(int j=0;j<numberEdges;j++){
          if(wissen[i][j].compareTo("nothing") != 0){
            sm.unhighlightCell(j+1, i, null, null);
          }
        }
      }
      //Prüfen ob alle Knoten alles Wissen, für eine Abbruchbedingung
      for(int i=0;i<nodeCount;i++){
        for(int j=0;j<numberEdges;j++){
          if(wissen[i][j].compareTo("nothing") == 0){
            wissenVoll = false;
            break;
          }
        }
      }
      
      String output = "";
      boolean[] outputNodes = new boolean[nodeCount];
      for(int i=0;i<nodeCount;i++){
        outputNodes[i] = false;
      }
      
      boolean packageAlreadyIn = false;
      boolean alreadyIn = false;
      round++;
      
      while(round <= rounds && !wissenVoll){        
        //packages weiterleiten
        //erst schauen wo packages sind und dann zu welchen Knoten diese geschickt werden können
        for(int i=0;i<nodeCount;i++){
          for(int j=0;j<numberEdges;j++){
            if(packages[i][j].compareTo("nothing") != 0){
              for(int k=0;k<nodeCount;k++){
                if(arrayEdge[i][k] != 0){
                  for(int l=0;l<numberEdges;l++){
                    //prüfen ob ähnliche Packages schon vorhanden sind
                    if(packages[i][j].compareTo(tmppackages[k][l]) == 0 ||
                        (packages[i][j].charAt(0) == tmppackages[k][l].charAt(tmppackages[k][l].length()-1) &&
                        packages[i][j].charAt(packages[i][j].length()-1) == tmppackages[k][l].charAt(0))){
                      packageAlreadyIn = true;
                    }
                    if(tmppackages[k][l].compareTo("nothing") == 0 && !packageAlreadyIn){
                      tmppackages[k][l] = packages[i][j];
                      break;
                    }
                  }
                  packageAlreadyIn = false;
                }
              }
            }
          }
        }                
        
        //packages in Wissensarray eintragen und alles dabei markieren
        for(int i=0;i<nodeCount;i++){
          output = translator.translateMessage("Paket1")+" " + graph.getNodeLabel(i) + " "+translator.translateMessage("Paket2")+": ";
          for(int j=0;j<numberEdges;j++){
            if(tmppackages[i][j].compareTo("nothing") != 0){
              if(getNumberOfNode(graph.getNodes().length,tmppackages[i][j].charAt(0)) != i){
                graph.highlightNode(getNumberOfNode(graph.getNodes().length,tmppackages[i][j].charAt(0)), null, null);
              }              
              for(int l=0;l<numberEdges;l++){
                  outputNodes[getNumberOfNode(graph.getNodes().length,tmppackages[i][j].charAt(0))] = true;
                  if(wissen[i][l].compareTo(tmppackages[i][j]) == 0 || 
                      (wissen[i][l].charAt(0) == tmppackages[i][j].charAt(tmppackages[i][j].length()-1) &&
                       wissen[i][l].charAt(wissen[i][l].length()-1) == tmppackages[i][j].charAt(0))){
                    alreadyIn = true;
                  }
                  if(wissen[i][l].compareTo("nothing") == 0 && !alreadyIn){
                    wissen[i][l] = tmppackages[i][j];
                    sm.put(l+1, i, wissen[i][l], null, null);//geändert
                    sm.highlightCell(l+1, i, null, null); 
                    alreadyIn = true;
                  }
                }
                alreadyIn = false;              
            }
          }
          t.setText(translator.translateMessage("runde")+": "+round, null, null);
          for(int o=0;o<nodeCount;o++){
            if(outputNodes[o]){
              output += graph.getNodeLabel(o)+" ";
            }
          }
          t1.setText(output, null, null);
          lang.nextStep();
          output = "";
          //macht markierungen rückgängig
          for(int c=0;c<nodeCount;c++){
            for(int h=0;h<numberEdges+1;h++){
              sm.unhighlightCell(h, c, null, null);
            }
            graph.unhighlightNode(c, null, null);
            outputNodes[c] = false;
          }          
        }
        
        //Packages säubern
        for(int i=0;i<nodeCount;i++){
          for(int j=0;j<numberEdges;j++){
            packages[i][j] = tmppackages[i][j];
            tmppackages[i][j] = "nothing";
          }
        }
        
        //Wissensprüfung wie oben
        wissenVoll = true;
        for(int i=0;i<nodeCount;i++){
          for(int j=0;j<numberEdges;j++){
            if(wissen[i][j].compareTo("nothing") == 0){ ////////auslagern
              wissenVoll = false;
              break;
            }
          }
        }
        //Table of Contents + Runde fertig
        t.setText(translator.translateMessage("runde")+" "+round+" "+translator.translateMessage("fertig"), null, null);
        t1.hide();
        lang.nextStep(translator.translateMessage("runde")+" "+round+" "+translator.translateMessage("fertig"));
        t1.show();
        
        round++;      
        
      }
      t1.hide();
      //Text für das Ende
      if(wissenVoll){
        t.setText(translator.translateMessage("protokollBeendetWissen")+" "+(round-1)+" "+translator.translateMessage("protokollBeendetWissen1"), null, null);
      }
      else if(round==rounds+1){
        t.setText(translator.translateMessage("protokollBeendetRunden")+"("+(round-1)+") "+translator.translateMessage("protokollBeendetRunden1"), null, null);
      }
      
    }

    public String getName() {
        return "Link State Routing";
    }

    public String getAlgorithmName() {
        return "Link State Routing";
    }

    public String getAnimationAuthor() {
        return "Justin Hopp";
    }

    public String getDescription(){
        return translator.translateMessage("Bschrbng")+" "+translator.translateMessage("Bschrbng1")+" "+translator.translateMessage("Bschrbng2")+" "+translator.translateMessage("Bschrbng3");
    }

    public String getCodeExample(){
        return translator.translateMessage("cdExmpl1")
 +"\n"
 +translator.translateMessage("cdExmpl2")
 +"\n"
 +translator.translateMessage("cdExmpl3")
 +"\n"
 +translator.translateMessage("cdExmpl4")
 +"\n"
 +translator.translateMessage("cdExmpl5");
    }

    public String getFileExtension(){
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
    public boolean validateInput(AnimationPropertiesContainer arg0,
        Hashtable<String, Object> arg1) throws IllegalArgumentException {
      
      int runden = (Integer) arg1.get("Runden");
      if(runden>=0) return true;
      else throw new IllegalArgumentException(translator.translateMessage("error"));
    }

}