package generators.generatorframe.controller;

import generators.generatorframe.store.GetInfos;

/**
 * 
 * @author Nora Wester
 *
 */

public class GraphPanelListener {

	GetInfos info;
	
	public GraphPanelListener(){
		info = GetInfos.getInstance();
	}
	
	public void graphSet(String text) {
		// TODO Auto-generated method stub
		info.setGraph(text);
	}

}
