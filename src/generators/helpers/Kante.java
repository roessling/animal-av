package generators.helpers;
import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
/**
 * @author Atilla Yalcin(1240034) , Mustafa Tuerkmen(1094578)
 * @version 1.0 2008-07-28
 *
 */
public class Kante {
public Polyline line;

public Kante(Knoten k1, Knoten k2,Language l){
	Coordinates[] cline= new Coordinates[2];
	cline[0]=k1.from;cline[1]=k2.to;
	line=l.newPolyline(cline, "l"+cline.hashCode(), null);
}


}
