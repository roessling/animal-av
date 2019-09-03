package animal.exchange.animalscript2;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import animal.animator.Animator;
import animal.animator.SetText;

public class SetTextExporter extends TimedAnimatorExporter
{
  // /**
  // * Export this object in ASCII format to the PrintWriter passed in.
  // *
  // * @param animator the current Move object
  // */
  // public String getExportString(Animator animator) {
  // StringBuilder sb = new StringBuilder(200);
  // SetText setText = (SetText)animator;
  //
  // // 1. append the space to the output
  // sb.append(exportUsedObjects(setText.getObjectNums()));
  // // sb.append(exportUsedObjects(new int[] { move.getMoveBaseNum() },
  // false));
  // String objectIDString = exportObjectIDs(animator);
  // // String movePathIDString = exportObjectIDs(move.getMoveBaseNum());
  // sb.append("setText of");
  // sb.append(objectIDString);
  // sb.append(" to ");
  // sb.append(setText.getValue());
  //
  // // 2. write out the info contained in the ancestor
  // // note: this ends without a space
  // sb.append(super.getExportString(animator));
  // return sb.toString();
  // }
  @Override
  public void export(Language lang, Animator animator) {
    if (!(animator instanceof SetText))
        return;
    //TODO Set text not yet supported by the API...
    SetText setText = (SetText)animator;
    System.out.println(setText);
//    Primitive moveBase = PTGraphicObjectExporter.hasBeenExported.get(setText.get());
//    if (moveBase == null) {
//      System.err.println("Have to export id " +setText.getMoveBaseNum());
////      PTGraphicObject moveBase = move.getMoveBaseNum()
//    }    
    int[] oids = animator.getObjectNums();
    for (int oid : oids) {
      if (oid > -1) {
        Primitive currentPrimitive = PTGraphicObjectExporter.hasBeenExported.get(oid);
        if (currentPrimitive != null)
          System.err.println("SetText not @ API yet");
//           currentPrimitive.setText(setText.getValue(),
//              createTiming(true, setText),
//              createTiming(false, setText));
         else
          System.err.println("did not get information for ID " +oid);
      }
    }
  }


}
