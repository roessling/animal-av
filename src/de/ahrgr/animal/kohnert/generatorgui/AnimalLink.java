/*
 * Created on 09.02.2005
 */
package de.ahrgr.animal.kohnert.generatorgui;

import java.io.File;

import de.ahrgr.animal.kohnert.asugen.Generator;

/**
 * @author ek
 */
public class AnimalLink implements Runnable{
    // AnimalLink funktioniert zur zeit nicht wegen Versionsproblemen
    
    
    protected boolean animalstarted = false;
    //protected static Animal instance = null;
    protected Thread t = null;
    protected File filetoload = null;
    protected File file = null;
    
    public AnimalLink() { this(null); }
       
    public AnimalLink(File aFile) {
     /*   filetoload = file;
        if(instance == null) {
            (new Thread(this)).start();
            try {
                synchronized(this) {this.wait(5000);}
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            instance = Animal.get();
            animalstarted = true;
        }
        if(filetoload != null) loadFile(filetoload);*/
    }
    
    public void start() {
        /*if(t == null) {
            t = new Thread(this);           
            t.start();
        }*/
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
//        String[] g = {};
  //      animal.main.Animal.main(g);
    }
    
    public void loadFile(File f) {
      /*  while(!animalstarted) {
            synchronized(this) {
                try {
                    this.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }                
            }
        }
        if(f.equals(file)) {
            instance.reloadFile();            
        } else {
            file = f;
            instance.importAnimation(f.toString(), "animation/animalscript");            
        }
        instance.showAnimationWindow();
        Animal.getAnimationWindow(true).toFront();*/
    }
    
    public void loadAnimation(Generator g) {
  /*      ByteArrayOutputStream buf = new ByteArrayOutputStream(100000);
        Writer w = new OutputStreamWriter(buf);
        g.generateScript(w);
        ByteArrayInputStream in = new ByteArrayInputStream(buf.toByteArray());
        instance.hide();
        instance.importAnimation(in, "tmp.asu", "animation/animalscript");
        instance.showAnimationWindow();
        Animal.getAnimationWindow(true).toFront();*/
    }

}
