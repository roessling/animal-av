package generators.helpers;

import java.awt.Color;
import java.util.LinkedList;

import algoanim.animalscript.AnimalGroupGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;


public class Disk {

	private Language lang;
	private RectProperties rectProps;
	private TextProperties textProps;
	
	private Rect rectObj;
	private Text textObj;
	
	private Group disk;
	
	int label;
	
	public Disk(Language l, DiskProperties dp, int lbl){
		lang = l;
		label = lbl;
		rectProps = dp.getRectProperties();
		textProps = dp.getTextProperties();
		createDisk();
	}

	private void createDisk() {
		// TODO Auto-generated method stub
		int l = 10;
		int w = 15;
		
		LinkedList<Primitive> prms = new LinkedList<Primitive>();
		
		int xp = 640 + 60 - (label + 2) * l / 2;
		
		rectObj = lang.newRect(new Coordinates(xp ,405), 
				 new Coordinates( xp + (label + 2) * l, 405+w), "box", null, rectProps);
		
		textObj = lang.newText(new Offset(-1,- w/2, rectObj, AnimalScript.DIRECTION_C), 
								String.valueOf(label), "Disk", null, textProps);
		textObj.hide();
		// trick
		textObj = lang.newText(new Offset(-1,- w/2, rectObj, AnimalScript.DIRECTION_C), 
				String.valueOf(label), "Disk", null, textProps);
		
		prms.add(rectObj);
		prms.add(textObj);
		disk = new Group(new AnimalGroupGenerator(lang), prms, null);
	}
	
	
	public void moveBy(int dx, int dy, Timing duration){
		disk.moveBy(null, dx, dy, null, duration);
	}
	
	public void moveVia(Primitive via, Timing duration) {
		try {
			disk.moveVia(null, null, via, null, duration);
		} catch (IllegalDirectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Node getUpperLeft(){
		return rectObj.getUpperLeft();
	}
		
	
	public void changeLabelColor(Color color){
		textObj.changeColor(null, color, null, null);
	}
	
	private void changeFillColor(Color color){
		rectObj.changeColor("FillColor", color, null, null);	
	}
	
	public void highlight(){
		changeFillColor(Color.CYAN);
	}
	
	public void unhighlight(){
		changeFillColor((Color)rectProps.get(AnimationPropertiesKeys.FILL_PROPERTY));
	}
	
	public void hide(){
		disk.hide();
	}
	
	public void show(){
		disk.show();
	}
}
