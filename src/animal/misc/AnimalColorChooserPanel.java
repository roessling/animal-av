package animal.misc;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;

import translator.AnimalTranslator;

public class AnimalColorChooserPanel 
  extends AbstractColorChooserPanel
  implements ItemListener {

  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7326010372262725615L;

private static int NOT_FOUND = -1;

  JComboBox<String> comboBox;
  String[] labels = ColorChoice.getAllColorNames();
 
  // Change combo box to match color, if possible
  private void setColor(Color newColor) {
    int position = findColorPosition(newColor);
    comboBox.setSelectedIndex(position);
  }

  // Given a label, find the position of the label in the list
  private int findColorLabel(Object label) {
    String stringLabel = label.toString();
    int position = NOT_FOUND;
    for (int i=0, n=labels.length; i<n; i++) {
      if (stringLabel.equals(labels[i])) {
        position=i;
        break;
      }
    }
    return position;
  }

  // Given a color, find the position whose color matches
  // This could result in a position different from original if two are equal
  // Since color is same, this is considered to be okay
  private int findColorPosition(Color color) {
    String colorName = ColorChoice.getColorName(color);
    return findColorLabel(colorName);
  }

  public void itemStateChanged(ItemEvent itemEvent) {
    int state = itemEvent.getStateChange();
    if (state == ItemEvent.SELECTED) {
      int position = findColorLabel(itemEvent.getItem());
      // last position is bad(not selectable)
      if ((position != NOT_FOUND) &&(position != labels.length-1)) {
        ColorSelectionModel selectionModel = getColorSelectionModel();
        selectionModel.setSelectedColor(ColorChoice.getColor(labels[position]));
      }
    }
  }

  public String getDisplayName() {
    return AnimalTranslator.translateMessage("animalColors");
  }

  public Icon getSmallDisplayIcon() {
    return new ColoredSquare(Color.blue);
  }

  public Icon getLargeDisplayIcon() {
    return new ColoredSquare(Color.green);
  }
       
  protected void buildChooser() {
    comboBox = new JComboBox<String>(labels);
    comboBox.addItemListener(this);
    add(comboBox);
  }

  public void updateChooser() {
    Color color = getColorFromModel();
    setColor(color);
  }
}


