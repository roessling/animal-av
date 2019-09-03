package generators.graphics;

import algoanim.util.Timing;


abstract public class ScenarioObject{
	
	private boolean needUpdate = false;
	private boolean isVisible = false;
	
	public void setNeedUpdate(boolean u)
	{
		this.needUpdate = u;
	}
	public void setIsVisible(boolean u)
	{
		this.isVisible = u;
	}
	
	public boolean needUpdate(){
		return this.needUpdate;
	}
	public boolean isVisible(){
		return this.isVisible;
	}
	
	public ScenarioObject()
	{
		this.setIsVisible(true);
		this.setNeedUpdate(false);
	}
	
	public void show()
	{
		needUpdate = true;
		isVisible = true;
	}
	public void hide()
	{
		needUpdate = true;
		isVisible = false;
	}
	
	abstract public void showPrimitives(Timing t);
	abstract public void hidePrimitives(Timing t);
	abstract public void didRefresh();
}

