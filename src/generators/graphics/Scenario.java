package generators.graphics;


import java.util.ArrayList;

import algoanim.primitives.generators.Language;
import algoanim.util.Timing;

public class Scenario{
	
	public Language language;
	private boolean updateScene;
	private SourceCodeField srcField;
	
	public SourceCodeField getSourceCodeField()
	{
		return this.srcField;
	}
	public Language getLanguage()
	{
		return this.language;
	}
	
	private ArrayList<ScenarioObject> scenarioObjects;
	
	public Scenario(Language lang, SourceCodeField srcField)
	{
		this.scenarioObjects = new ArrayList<ScenarioObject>();
		this.language = lang;
		this.srcField = srcField;
	}
	
	public void hideAllPrimitives(){
		updateScene = true;
		this.language.hideAllPrimitives();
	}
	
	public void addScenarioObject(ScenarioObject scObj)
	{
		this.scenarioObjects.add(scObj);
	}
	public void removeScenarioObject(ScenarioObject scObj)
	{
		scObj.hidePrimitives(null);
		this.scenarioObjects.remove(scObj);
	}
	
	public void nextStep(String title, Timing animationTime)
	{
		this.updateScene(animationTime);
		if (animationTime != null && animationTime.getDelay() > 0)
		{
			this.language.nextStep(title); // t.getDelay()*100
		} else {
			this.language.nextStep(title);
		}
		this.srcField.didRefresh();
		for(ScenarioObject scObj : this.scenarioObjects){
			scObj.didRefresh();
		}
	}
	
	/**
	 * updates the scene.
	 * 
	 * With this method is really simple the hide all object except one special like the sourcecode object.
	 * 
	 * @param t
	 */
	public void updateScene(Timing t)
	{    		
		if ((updateScene && srcField.isVisible()) || (srcField.needUpdate() && srcField.isVisible()))
		{
			srcField.showPrimitives(t);
		} else if(srcField.needUpdate() && !srcField.isVisible()) {
			srcField.hidePrimitives(t);
		}
		srcField.setNeedUpdate(false);
		
		for(ScenarioObject obj : this.scenarioObjects)
		{
			if ((updateScene && obj.isVisible()) || (obj.needUpdate() && obj.isVisible()))
			{
				obj.showPrimitives(t);
			} else if(obj.needUpdate() && !obj.isVisible()) {
				obj.hidePrimitives(t);
			}
			obj.setNeedUpdate(false);
		}
		this.updateScene = false;
	}
}

