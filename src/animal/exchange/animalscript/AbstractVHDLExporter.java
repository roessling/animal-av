package animal.exchange.animalscript;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import animal.misc.ColorChoice;
import animal.vhdl.graphics.PTFlipFlop;
import animal.vhdl.graphics.PTGate;
import animal.vhdl.graphics.PTMulti;
import animal.vhdl.graphics.PTPin;
import animal.vhdl.graphics.PTVHDLElement;

public class AbstractVHDLExporter extends PTGraphicObjectExporter {
  
  protected StringBuilder startExport(PTVHDLElement vhdlElement, 
      String typeName, boolean usesMultiIn, boolean usesMultiOut) {
    // write out the information of the super object
    StringBuilder sb = new StringBuilder(400);
    sb.append(typeName).append(" \"").append(vhdlElement.getObjectName()).append("\" (");

    Point node = vhdlElement.getStartNode();
    sb.append((int) node.getX()).append(", ").append((int) node.getY());
    sb.append(") (").append(vhdlElement.getWidth()).append(", ");
    sb.append(vhdlElement.getHeight()).append(")");

    // input and output pin names and values
    exportPins(vhdlElement.getInputPins(), "input", usesMultiIn, sb);
    exportPins(vhdlElement.getOutputPins(), "output", usesMultiOut, sb);
    return sb;
  }
  
  protected void exportPins(ArrayList<PTPin> pins, String tag, boolean useMulti,
      StringBuilder sb) {
    if (useMulti) {
      for (PTPin inputPin: pins) {
        sb.append(' ').append(tag).append(' ').append(inputPin.getPinName());
        if (inputPin.getPinValue() != ' ')
          sb.append(" value ").append(inputPin.getPinValue());
      }
    } else {
      PTPin inputPin = pins.get(0);
      sb.append(' ').append(tag).append(' ').append(inputPin.getPinName());
      if (inputPin.getPinValue() != ' ')
        sb.append(" value ").append(inputPin.getPinValue());
    }
  }
  
  protected void finishExport(PTVHDLElement vhdlElement, 
      StringBuilder sb) {

    // write this object's information
    Color color = vhdlElement.getColor();
    sb.append(" color ").append(ColorChoice.getColorName(color));

    sb.append(" depth ").append(vhdlElement.getDepth());

    if (vhdlElement.isFilled()) {
      sb.append(" filled");
      sb.append(" fillColor ").append(
          ColorChoice.getColorName(vhdlElement.getFillColor()));
    }
    hasBeenExported.put(vhdlElement, vhdlElement.getObjectName());
  }
  
  protected String getCommonGateExportString(PTGate gate, String typeName) {
    // write out the information of the super object
    if (getExportStatus(gate))
      return "# previously exported: '" + gate.getNum(false) + "/"
          + gate.getObjectName();
    StringBuilder sb = startExport(gate, typeName, true, true);
      
    finishExport(gate, sb);
    return sb.toString();
  }
  
  protected String getCommonFlipFlopExportString(PTFlipFlop flipFlop, String typeName, 
      boolean usesMultiIn, boolean usesMultiOut) {
    if (getExportStatus(flipFlop))
      return "# previously exported: '" + flipFlop.getNum(false) + "/"
          + flipFlop.getObjectName();
    StringBuilder sb = startExport(flipFlop, typeName, 
        usesMultiIn, usesMultiOut);
    System.err.println("FlipFlop type: " +flipFlop.getClass().getName());
    System.err.println("hasAsynSR (clk, ce)? " +flipFlop.hasAsynSR());
    System.err.println("hasSynControl (sd, rd)? " +flipFlop.hasSynControl());

    // show control port or not
    ArrayList<PTPin> controlPins = flipFlop.getControlPins();
    PTPin currentPin = null;
    if (flipFlop.hasSynControl()) { // was async!
      currentPin = controlPins.get(2);
      sb.append(" clk ").append(currentPin.getPinName());
      if (currentPin.getPinValue() != ' ')
        sb.append(" value ").append(currentPin.getPinValue());
      
      currentPin = controlPins.get(3);
      sb.append(" ce ").append(currentPin.getPinName());
      if (currentPin.getPinValue() != ' ')
        sb.append(" value ").append(currentPin.getPinValue());
    }
    
    if (flipFlop.hasAsynSR()) { // was: hasSyncControl()
      currentPin = controlPins.get(0);
      sb.append(" sd ").append(currentPin.getPinName());
      if (currentPin.getPinValue() != ' ')
        sb.append(" value ").append(currentPin.getPinValue());
      
      currentPin = controlPins.get(1);
      sb.append(" rd ").append(currentPin.getPinName());
      if (currentPin.getPinValue() != ' ')
        sb.append(" value ").append(currentPin.getPinValue());
    }
    finishExport(flipFlop, sb);
    return sb.toString();
  }
  
  protected String getCommonMultiplexExportString(PTMulti multi, String typeName) {
    // write out the information of the super object
    if (getExportStatus(multi))
      return "# previously exported: '" + multi.getNum(false) + "/"
          + multi.getObjectName();
    StringBuilder sb = startExport(multi, typeName, true, true);

    // export in/out pins
    ArrayList<PTPin> pins = multi.getInoutPins();
    if (pins != null)
      exportPins(multi.getInoutPins(), "inoutput", true, sb);
    
    // export control pins
    pins = multi.getControlPins();
    if (pins != null)
      exportPins(pins, "control", true, sb);
    finishExport(multi, sb);
    return sb.toString();
  }
}
