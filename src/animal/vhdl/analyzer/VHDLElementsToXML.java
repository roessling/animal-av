package animal.vhdl.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import animal.vhdl.graphics.PTVHDLElement;

public class VHDLElementsToXML {
  Document     myDocument;
  XMLOutputter outputter;
  String       fileName;

  public VHDLElementsToXML(ArrayList<PTVHDLElement> elements) {
    Element rootElement = null;
    for (int i = 0; i < elements.size(); i++) {
      if (elements.get(i).getEntityType() == 0) {
        fileName = elements.get(i).getObjectName();
        rootElement = new Element(fileName);
        myDocument = new Document(rootElement);
      }

    }

    for (int i = 0; i < elements.size(); i++) {
      if (elements.get(i) != null) {
        Element gate = new Element(elements.get(i).getObjectName());
        gate.setAttribute(new Attribute("Color", ""
            + elements.get(i).getColor()));
        gate.setAttribute(new Attribute("FillColor", ""
            + elements.get(i).getFillColor()));
        gate.setAttribute(new Attribute("IsFilled", ""
            + elements.get(i).isFilled()));
        gate.setAttribute(new Attribute("EntityType", ""
            + elements.get(i).getEntityType()));
        gate.setAttribute(new Attribute("CodeNr", elements.get(i)
            .getCodeLineNumberBegin()
            + "-" + elements.get(i).getCodeLineNumberEnd()));
        rootElement.addContent(gate);
        for (int j = 0; j < elements.get(i).getInputPins().size(); j++) {
          Element port = new Element("Input");
          port.setAttribute(new Attribute("name", ""
              + elements.get(i).getInputPins().get(j).getPinName()));
          port.setAttribute(new Attribute("value", ""
              + elements.get(i).getInputPins().get(j).getPinValue()));
          gate.addContent(port);
        }
        for (int j = 0; j < elements.get(i).getOutputPins().size(); j++) {
          Element port = new Element("Output");
          port.setAttribute(new Attribute("name", ""
              + elements.get(i).getOutputPins().get(j).getPinName()));
          port.setAttribute(new Attribute("value", ""
              + elements.get(i).getOutputPins().get(j).getPinValue()));
          gate.addContent(port);

        }
        if (elements.get(i).getControlPins() != null
            && elements.get(i).getControlPins().size() != 0) {

          for (int j = 0; j < elements.get(i).getControlPins().size(); j++) {
            Element port = new Element("Control");
            port.setAttribute(new Attribute("name", ""
                + elements.get(i).getControlPins().get(j).getPinName()));
            port.setAttribute(new Attribute("value", ""
                + elements.get(i).getControlPins().get(j).getPinValue()));
            if (elements.get(i).getControlPins().get(j).getPinName().equals(
                "cSD")
                || elements.get(i).getControlPins().get(j).getPinName().equals(
                    "cRD")
                || elements.get(i).getControlPins().get(j).getPinName().equals(
                    "cClk")
                || elements.get(i).getControlPins().get(j).getPinName().equals(
                    "cCE")) {

            } else
              gate.addContent(port);
          }
        }
        if (elements.get(i).getInoutPins() != null
            && elements.get(i).getInoutPins().size() != 0) {
          for (int j = 0; j < elements.get(i).getInoutPins().size(); j++) {
            Element port = new Element("Control");
            port.setAttribute(new Attribute("name", ""
                + elements.get(i).getInoutPins().get(j).getPinName()));
            port.setAttribute(new Attribute("value", ""
                + elements.get(i).getInoutPins().get(j).getPinValue()));
            gate.addContent(port);
          }
        }
      }

    }
    try {
      outputter = new XMLOutputter();
      FileWriter writer = new FileWriter("example" + File.separator + fileName
          + ".xml");
      // FileWriter writer = new
      // FileWriter("VHDL"+File.separator+"VHDLXML"+File.separator+fileName+".xml");
      outputter.output(myDocument, writer);
      writer.close();
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }

  }
}
