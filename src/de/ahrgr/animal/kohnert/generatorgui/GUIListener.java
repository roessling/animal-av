/*
 * Created on 17.01.2005
 */
package de.ahrgr.animal.kohnert.generatorgui;

import java.io.File;

/**
 * @author ek
 */
public interface GUIListener {
    
    public void GUIEvent_Generate(File f);
    public void GUIEvent_Exit();
    
}
