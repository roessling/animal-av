/*
 * Created on 14.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.StringTokenizer;

import animal.main.Animal;

/**
 * @author guido
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class LanguageMenuController implements ActionListener {
  /**
   * The animal instance of which we will be working!
   */
  private Animal animal;
  /**
   * generate a controller for the "Edit" menu
   *
   * @param animalInstance the current instance of Animal
   */
  public LanguageMenuController(Animal animalInstance) {
    super();

    // store the reference to the Animal instance
    animal = animalInstance;
  }

  /**
   * handle the events thrown from the "File" menu
   *
   * @param event the event describing the underlying action
   */
  public void actionPerformed(ActionEvent event) {
    // retrieve the action command used	
    String command = event.getActionCommand();

    // add a StringTokenizer that helps parsing the element
    StringTokenizer stok = new StringTokenizer(command, " _");

    // retrieve the base language  code (e.g., "en", "de", "es")
    String baseCode = stok.nextToken();
    
    // retrieve the country information
    String countryCode = null;
    if (stok.hasMoreElements())
    	countryCode = stok.nextToken();
  
    // the locale is consists of language code + country code ("en", "US")
    Locale targetLocale = new Locale(baseCode, countryCode);
    
    animal.setAnimalLocale(targetLocale);
  }
}
