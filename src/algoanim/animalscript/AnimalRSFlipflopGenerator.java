package algoanim.animalscript;

import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.vhdl.RSFlipflopGenerator;
import algoanim.primitives.vhdl.VHDLElement;

/**
 * @see algoanim.primitives.generators.SquareGenerator
 * @author Guido Roessling
 * @version 0.2 20110218
 */
public class AnimalRSFlipflopGenerator extends AnimalVHDLElementGenerator 
implements RSFlipflopGenerator {

  /**
   * @param aLang
   *          the associated <code>Language</code> object.
   */
  public AnimalRSFlipflopGenerator(Language aLang) {
    super(aLang);
  }


  @Override
  public void create(VHDLElement vhdlElement) {
    createRepresentationForGate(vhdlElement, "relrs");    
  }
}
