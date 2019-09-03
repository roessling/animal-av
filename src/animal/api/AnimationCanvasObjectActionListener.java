package animal.api;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.main.AnimationCanvas;
import generators.framework.Generator;
import generators.framework.InteractiveGenerator;
import generators.framework.InteractivityJob;
import generators.generatorframe.store.GetInfos;

/**
 * reacts to mouse pressed and released events.
 */
public class AnimationCanvasObjectActionListener extends MouseAdapter {
  
	AnimationCanvas canvas = null;

	public AnimationCanvasObjectActionListener(AnimationCanvas theCanvas) {
		canvas = theCanvas;
	}
	
  private LinkedList<PTGraphicObject> getAllPTGraphicObjectOnPoint(GraphicVector objects, Point p, int tollerance) {
    LinkedList<PTGraphicObject> listWithObject = new LinkedList<PTGraphicObject>();
    // iterate from top to bottom.
    for (int a = objects.getSize() - 1; a >= 0; a--) {
      GraphicVectorEntry gve = objects.elementAt(a);
      if (gve != null) {
        PTGraphicObject go = gve.go;
        Rectangle recCanvas = canvas.getRealBoundingBox(go);
        Rectangle recCanvasWithTollerance = new Rectangle(Math.max(recCanvas.x-tollerance, 0), Math.max(recCanvas.y-tollerance, 0), recCanvas.width+tollerance*2, recCanvas.height+tollerance*2);
        if(recCanvasWithTollerance.contains(p)) {
          listWithObject.add(go);
        }
      }
    }
    return listWithObject;
  }
	
  private boolean clickerListenerOn = true;
	public void mouseClicked(MouseEvent evt) {
    if(SwingUtilities.isRightMouseButton(evt) && clickerListenerOn) {
      Generator generator = GetInfos.getInstance().getLatestStartetGenerator();
      InteractiveGenerator iGenerator = null;
      if(generator!=null && generator instanceof InteractiveGenerator) {
        iGenerator = (InteractiveGenerator) generator;
      }
      if(iGenerator!=null && iGenerator.isInteractive() && iGenerator.getInteractivityJobList()!=null) {
//        System.out.println("Is interactive Generator");
        
        // Get interactions in currentStep
        int currentLanguageStep = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).getStep();
        List<InteractivityJob> iListCurrentStep = iGenerator.getInteractivityJobList().stream().
            filter(iJob -> ((iJob.getInteractivityStep()+1==currentLanguageStep || (iJob.getInteractivityStep()+1<currentLanguageStep && iJob.isInteractivityFromStepUntilLastStep()))
                && (!iJob.isInteractivityOnlyAtAnimationEnd() ||
                    currentLanguageStep==AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).getAnimationState().getLastStep())))
            .collect(Collectors.toList());
        
        LinkedList<PTGraphicObject> listWithObject = getAllPTGraphicObjectOnPoint(canvas.getObjects(), evt.getPoint(), 5);
        if(listWithObject!=null) {
          boolean showOneInteractivity = false;
          final JPopupMenu popupMenu = new JPopupMenu();
          HashMap<String, LinkedList<InteractivityJob>> hashMapNameToIJ = new HashMap<String, LinkedList<InteractivityJob>>();
          for(int i = 0 ; i<listWithObject.size() ; i++) {
            PTGraphicObject ptgo = listWithObject.get(i);
            String ptgoName = ptgo.getObjectName();
            
            for(InteractivityJob iJob : iListCurrentStep) {
              if(iJob.getInteractivityPrimitive().getName().equals(ptgoName)) {
                String key = iJob.getInteractivityObjectName() == null ? ptgoName : iJob.getInteractivityObjectName();
                if(!hashMapNameToIJ.containsKey(key)) {
                  hashMapNameToIJ.put(key, new LinkedList<InteractivityJob>());
                }
                hashMapNameToIJ.get(key).add(iJob);
              }else if(iGenerator.getIRectName()!=null && ptgoName.equals(iGenerator.getIRectName())) {
                String key = iJob.getInteractivityObjectName() == null ? ptgoName : iJob.getInteractivityObjectName();
                if(!hashMapNameToIJ.containsKey(key)) {
                  hashMapNameToIJ.put(key, new LinkedList<InteractivityJob>());
                }
                hashMapNameToIJ.get(key).add(iJob);
              }
            }
          }
          
          for(Map.Entry<String, LinkedList<InteractivityJob>> entry : hashMapNameToIJ.entrySet()) {
            String key = entry.getKey();
            LinkedList<InteractivityJob> value = entry.getValue();
            
            final JMenu subMenu = new JMenu(key);
            for(InteractivityJob iJob : value) {
              JMenuItem item = new JMenuItem(iJob.getInteractivityActionName());
              item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  iJob.doAction();
                }
              });
              subMenu.add(item);
              showOneInteractivity = true;
            }
            popupMenu.add(subMenu);
          }
          
          if(showOneInteractivity) {
            popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
          }
        }
      }
    }
	}
}
