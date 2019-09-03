package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Point;

import animal.vhdl.graphics.PTGate;
import animal.vhdl.graphics.PTPin;

public abstract class PTVHDLExporter extends PTGraphicObjectExporter {
  public String exportCommonFeatures(PTGate gate, 
      String concreteTypeString) {
    StringBuilder sb = new StringBuilder(372);

    // write out the information of the super object
    sb.append(gate.getFileVersion());
    sb.append(" object ");
    sb.append(gate.getNum(false));
    
    // write this object's information
    Point center = gate.getStartNode();
    sb.append(concreteTypeString).append(" at (");

    sb.append(center.x).append(",").append(center.y);
    sb.append(") width ").append(gate.getWidth());
    sb.append(" height ").append(gate.getHeight());
    sb.append(" name \"").append(gate.getObjectName()).append('\"');
    sb.append(" nrInputPins ").append(gate.getInputPins().size());
    sb.append(" input {");
    for (PTPin pin : gate.getInputPins()) {
      sb.append(" inPin \"").append(pin.getPinName()).append('\"');
      if (pin.getPinValue() != ' ')
        sb.append(" value \"").append(pin.getPinValue()).append('\"');
    }
    sb.append(" } output \"");
    PTPin pin = gate.getOutputPins().get(0);
    sb.append(pin.getPinName()).append('\"');
    if (pin.getPinValue() != ' ')
      sb.append(" value ").append(pin.getPinValue());

    Color color = gate.getColor();
    sb.append(" color (").append(color.getRed()).append(",");
    sb.append(color.getGreen()).append(",").append(color.getBlue());
    sb.append(") fillColor (");
    color = gate.getFillColor();
    sb.append(color.getRed()).append(",").append(color.getGreen());
    sb.append(",").append(color.getBlue()).append(")");
    sb.append(getAdditionalExportInformation(gate));
    sb.append(" depth ").append(gate.getDepth());
    return sb.toString();
  }
  
  public String getAdditionalExportInformation(PTGate gate) {
    return "";
  }
}
