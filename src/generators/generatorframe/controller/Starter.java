package generators.generatorframe.controller;

import generators.framework.GeneratorType;
import generators.generatorframe.loading.GeneratorLoader;
import generators.generatorframe.store.FilterInfo;
import generators.generatorframe.store.GetInfos;
import generators.generatorframe.store.SearchLoader;
import generators.generatorframe.view.GeneratorFrame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JFrame;

import animal.exchange.AnimationImporter;
import animal.main.Animal;
import animal.main.Animation;

/**
 * 
 * @author Nora Wester
 *
 */
public class Starter {
	
	GeneratorFrame view;
	Animal animal;
	private static GeneratorLoader gL;
	static HashMap<String, Vector<String>> contentAuthors;
	
	static int generatorNumber;
	
	public static void loadGenerator(){
	  gL = new GeneratorLoader();
    contentAuthors = gL.getContentAuthors();
    
    generatorNumber = gL.getTotalNumber();
	}
	
	protected static GeneratorLoader getLoader() {
	  return Starter.gL;
	}
	public Starter(Animal animal){
		
		this.animal = animal;
		
//		GeneratorLoader gL = new GeneratorLoader();
//		contentAuthors = gL.getContentAuthors();
//		
//		int generatorNumber = gL.getTotalNumber();
		
		view = new GeneratorFrame(generatorNumber, this, getCategory(), Animal.getCurrentLocale());;
		
		SearchLoader.getInstance().addObserver(view);
		FilterInfo.getInstance().addObserver(view);
		GetInfos.getInstance().addObserver(view);
		System.err.println(generatorNumber);
		
	}
	
	public Object getContentAuthors() {
		StringBuilder sb = new StringBuilder(2048);
		sb.append("\n<h2>Animation Generator Authors</h2>\n\n<dl>");
		Object[] keys = contentAuthors.keySet().toArray();
		Arrays.sort(keys);
		for (Object key : keys) {
			Vector<String> currentVector = contentAuthors.get(key);
			if (currentVector != null && currentVector.size() > 0) {
				sb.append("\n<dt>").append(key).append("</dt>\n<dd><ul>");
				Object[] entries = currentVector.toArray();
				Arrays.sort(entries);
				for (Object entry : entries)
					sb.append(entry);
				sb.append("\n</ul>\n</dd>");
			}
		}
		sb.append("\n</dl>\n");
		return sb.toString();
	}
	
	public void setAnimation(String content){
		
		String format = "animation/animalscript";
		if(animal != null){
			AnimationImporter animationImporter = AnimationImporter.getImporterFor(format);
			Animation animation = animationImporter.importAnimationFrom(null, content);
			animal.setAnimation(animation);
      animal.getAnimation().resetChange();
		}
	}
	
	public void setVisible(boolean setting){
		view.setVisible(setting);
	}
	
	private String[] getCategory(){
		int[] category = GeneratorType.getTypes();
		String[] data = new String[category.length];
		for(int i=0; i<data.length; i++){
			data[i] = GeneratorType.getStringForType(category[i]);
		}
		
		return data;
	}
	
	public void setGeneratorLocale(Locale target){
		view.setGeneratorLocale(target);
	}
	
	public JFrame getFrame(){
	  return view;
	}

  /**
   * zooms the window in
   * 
   * @param zoomIn
   *          if true window is zoomed in, if false window is zoomed out
   */
  public void zoom(boolean zoomIn) {

    view.zoom(zoomIn);
  }
}
