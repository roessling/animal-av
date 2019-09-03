package algoanim.animalscript;

import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.vhdl.TFlipflopGenerator;
import algoanim.primitives.vhdl.VHDLElement;

/**
 * @see algoanim.primitives.generators.SquareGenerator
 * @author Guido Roessling
 * @version 0.2 20110218
 */
public class AnimalTFlipflopGenerator extends AnimalVHDLElementGenerator 
implements TFlipflopGenerator {

  /**
   * @param aLang
   *          the associated <code>Language</code> object.
   */
  public AnimalTFlipflopGenerator(Language aLang) {
    super(aLang);
  }


  @Override
  public void create(VHDLElement vhdlElement) {
    createRepresentationForGate(vhdlElement, "relt");    
  }
}
