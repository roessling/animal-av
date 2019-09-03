package algoanim.animalscript;

import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.vhdl.XOrGateGenerator;
import algoanim.primitives.vhdl.VHDLElement;

/**
 * @see algoanim.primitives.generators.SquareGenerator
 * @author Guido Roessling
 * @version 0.2 20110218
 */
public class AnimalXorGenerator extends AnimalVHDLElementGenerator 
implements XOrGateGenerator {

  /**
   * @param aLang
   *          the associated <code>Language</code> object.
   */
  public AnimalXorGenerator(Language aLang) {
    super(aLang);
  }


  @Override
  public void create(VHDLElement vhdlElement) {
    createRepresentationForGate(vhdlElement, "relxor");    
  }
}
