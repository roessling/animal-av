/*
 * Created on 17.12.2004
 */
package de.ahrgr.animal.kohnert.asugen.petri;

import java.util.ArrayList;
import java.util.Iterator;

import de.ahrgr.animal.kohnert.asugen.AnimalObject;
import de.ahrgr.animal.kohnert.asugen.AnimalScriptWriter;
import de.ahrgr.animal.kohnert.asugen.Circle;
import de.ahrgr.animal.kohnert.asugen.EKColor;
import de.ahrgr.animal.kohnert.asugen.EKNode;
import de.ahrgr.animal.kohnert.asugen.PolyLine;
import de.ahrgr.animal.kohnert.asugen.Rectangle;
import de.ahrgr.animal.kohnert.asugen.Text;

/**
 * @author ek
 */
public class Transition extends AnimalObject{

    protected ArrayList<Link> vorbereich;
    protected ArrayList<Link> nachbereich;
    protected Rectangle rect;
    protected Text text;
    
    /**
     * @param sw
     */
    public Transition(AnimalScriptWriter sw, EKNode aPosition, String aText) {
        super(sw);
        position = aPosition;
        rect = new Rectangle(sw, aPosition.createOffset(-5,-20),
            position.createOffset(5,20));
        rect.setFillColor(EKColor.BLUE);
        text = new Text(sw, rect.createOffset(-10,-10, NW), aText);
        vorbereich = new ArrayList<Link>();
        nachbereich = new ArrayList<Link>();
    }
    
    public void addNachbereich(Stelle s) {
       Link l = new Link();
       l.stelle = s;
       l.line = new PolyLine(scriptwriter);
       l.line.addNode(rect.createOffset(0,0,AnimalObject.E));
       l.line.addNode(s.createOffset(0,0, AnimalObject.W));
       l.line.setArrow(PolyLine.ARROW_FORWARD);
       nachbereich.add(l);
       if(registered) l.line.register();
    }
    
    public void addVorbereich(Stelle s) {
       Link l = new Link();
       l.stelle = s;
       l.line = new PolyLine(scriptwriter);      
       l.line.addNode(s.createOffset(0,0, AnimalObject.E));
       l.line.addNode(rect.createOffset(0,0,AnimalObject.W));
       l.line.setArrow(PolyLine.ARROW_FORWARD);
       vorbereich.add(l);
       if(registered) l.line.register();
    }    
    
    public void setColor(EKColor c) {
        rect.setColor(c);        
    }
    
    public void setFillColor(EKColor c) {
        rect.setFillColor(c);
    }

    /* (non-Javadoc)
     * @see animalobjects.AnimalObject#register()
     */
    public void register() {
        if(registered) return;
        rect.register();
        text.register();
        Iterator<Link> i = vorbereich.iterator();
        while(i.hasNext()) {
            Link l = i.next();
            l.stelle.register();
            l.line.register();
        }
        i = nachbereich.iterator();
        while(i.hasNext()) {
            Link l = i.next();
            l.stelle.register();
            l.line.register();
        }        
        registered = true;        
    }
    
    public void animiereVorbereich() {
        // alle Stellen vom vorbereich zum Transitionsrechteck bewegen
        Iterator<Link> i = vorbereich.iterator();
        while(i.hasNext()) {
            Link l = i.next();
            if(l.animStelle == null) {
                l.animStelle = new Circle(scriptwriter, 
                   l.stelle.createOffset(-10, -10, AnimalObject.C), 10);
                l.animStelle.setFillColor(EKColor.BLACK);
                l.animStelle.register();
            } else {
                l.animStelle.setHidden(false);
            }
            l.animStelle.moveTo(rect.createOffset(-10, -10, AnimalObject.C)); 
        }
    }
    
    public void animiereNachbereich() {
        //alle ausgenden Stellen bewegen
        Iterator<Link> i = nachbereich.iterator();
        while(i.hasNext()) {
            Link l = i.next();
            if(l.animStelle == null) {
                l.animStelle = new Circle(scriptwriter, 
                    rect.createOffset(-10, -10, AnimalObject.C), 10);
                l.animStelle.setFillColor(EKColor.BLACK);
                l.animStelle.register();
            } else {
                l.animStelle.setHidden(false);
            }
            l.animStelle.moveTo(l.stelle.createOffset(-10, -10, AnimalObject.C)); 
        }
    }    
    
    public void resetVorbereichAnimation() {
        // alle Animationsobjekte auf Startposition und verstecken
        Iterator<Link> i = vorbereich.iterator();
        while(i.hasNext()) {
            Link l = i.next();
            if(l.animStelle != null) {
                l.animStelle.setPosition(l.stelle.createOffset(-10, -10, AnimalObject.C));
                l.animStelle.setHidden(true);
            }             
        }
        
    }
    
    public void resetNachbereichAnimation() {
        // alle Animationsobjekte auf Startposition und verstecken
        Iterator<Link> i = nachbereich.iterator();
        while(i.hasNext()) {
            Link l = i.next();
            if(l.animStelle != null) {
                l.animStelle.setPosition(rect.createOffset(-10, -10, AnimalObject.C));
                l.animStelle.setHidden(true);
            }             
        }
        
    }    


    class Link {
        Circle animStelle = null;
        PolyLine line;
        Stelle stelle;
    }
}
