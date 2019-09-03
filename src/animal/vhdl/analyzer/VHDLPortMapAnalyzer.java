package animal.vhdl.analyzer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import animal.vhdl.graphics.PTPin;
import animal.vhdl.graphics.PTVHDLElement;

public class VHDLPortMapAnalyzer extends VHDLBaseAnalyzer {
  private static int CODE_NUMBER;

  protected static PTVHDLElement insertPortMapEntity(String line, int codeNr) {
    String regEx;
    Pattern p;
    Matcher m;
    boolean rs;
    PTVHDLElement mapEntity = null;
    regEx = ":.+(port|PORT)";
    p = Pattern.compile(regEx);
    m = p.matcher(line);
    rs = m.find();
    if (rs) {
      String name = m.group().replaceAll("(:|port|PORT)", "").trim();
      for (PTVHDLElement entity : VHDLAnalyzer.getElements()) {
        if (entity.getObjectName().equals(name)) {
          // String className=entity.getClass().getName();
          // ***************************************************************************************//
          // ***************************************************************************************//
          // ***************************************************************************************//
          mapEntity = (PTVHDLElement) entity.clone();
          regEx = "\\(.+\\)";
          p = Pattern.compile(regEx);
          m = p.matcher(line);
          rs = m.find();
          if (rs)
            mapEntity = setPortMap(mapEntity, m.group());
          mapEntity.setEntityType((byte) 2);
          mapEntity.setCodeLineNumberBegin(codeNr);
          mapEntity.setCodeLineNumberEnd(codeNr);
          if (mapEntity.getClass().getName() == "VHDL.animal.graphics.PTEntity") {
            mapEntity.setObjectName(mapEntity.getObjectName() + CODE_NUMBER);
            CODE_NUMBER++;
          }
          break;
        }
      }
    }
    return mapEntity;
  }

  private static PTVHDLElement setPortMap(PTVHDLElement mapEntity,
      String portList) {
    String[][] portTable = spiltportList(portList);
    ArrayList<PTPin> inputList = new ArrayList<PTPin>(0);
    ArrayList<PTPin> outputList = new ArrayList<PTPin>(0);
    ArrayList<PTPin> inoutputList = new ArrayList<PTPin>(0);
    if (mapEntity.getInputPins() != null) {
      for (PTPin input : mapEntity.getInputPins()) {
        for (int i = 0; i < portTable[0].length; i++) {
          if (input.getPinName().equals(portTable[0][i])) {
            inputList.add(new PTPin(portTable[1][i], true));
            inputList.get(inputList.size() - 1).setLastNode(10, 0);
            break;
          }
        }
      }
      if (inputList.size() != 0)
        mapEntity.setInputPins(inputList);
    }
    if (mapEntity.getOutputPins() != null) {
      for (PTPin output : mapEntity.getOutputPins()) {
        for (int i = 0; i < portTable[0].length; i++) {
          if (output.getPinName().equals(portTable[0][i])) {
            outputList.add(new PTPin(portTable[1][i], false));
            outputList.get(outputList.size() - 1).setLastNode(10, 0);
            break;
          }
        }
      }
      if (outputList.size() != 0)
        mapEntity.setOutputPins(outputList);
    }
    if (mapEntity.getInoutPins() != null) {
      for (PTPin inoutput : mapEntity.getInoutPins()) {
        for (int i = 0; i < portTable[0].length; i++) {
          if (inoutput.getPinName().equals(portTable[0][i])) {
            inoutputList.add(new PTPin(portTable[1][i], true));
            inoutputList.get(inoutputList.size() - 1).setFirstNode(10, 0);
            break;
          }
        }
      }
      if (inoutputList.size() != 0)
        mapEntity.setInoutPins(inoutputList);
    }

    return mapEntity;
  }

  private static String[][] spiltportList(String portList) {
    String[][] portTable = null;
    String workOnThis = portList;
    workOnThis = workOnThis.replaceAll("(\\(|\\))", ""); // delete ()
    workOnThis = workOnThis.replaceAll("\\s*=>", "=>"); // delete empty in the front
                                                    // of =>
    workOnThis = workOnThis.replaceAll("=>\\s*", "=> "); // insert empty in the
                                                     // behind of =>
    String[] temp = workOnThis.split("( |,|=>)");
    ArrayList<String> delEmpty = new ArrayList<String>(0);
    for (int i = 0; i < temp.length; i++)
      if (!temp[i].equals(""))
        delEmpty.add(temp[i]);
    portTable = new String[2][delEmpty.size() / 2];
    for (int i = 0; i < delEmpty.size() / 2; i++) {
      portTable[0][i] = delEmpty.get(i * 2);
      portTable[1][i] = delEmpty.get(i * 2 + 1);
    }
    return portTable;
  }
}
