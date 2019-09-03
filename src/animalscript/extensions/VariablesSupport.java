package animalscript.extensions;

import java.io.IOException;
import java.util.Hashtable;

import algoanim.primitives.generators.VariablesGenerator;
import animal.animator.VariableDeclaration;
import animal.animator.VariableDiscard;
import animal.animator.VariableUpdate;
import animal.misc.ParseSupport;
import animal.variables.Variable;
import animalscript.core.AnimalParseSupport;
import animalscript.core.AnimalScriptInterface;
import animalscript.core.BasicParser;

public class VariablesSupport extends BasicParser implements
    AnimalScriptInterface {
  public VariablesSupport() {
    handledKeywords = new Hashtable<String, Object>();

    handledKeywords.put(VariablesGenerator.DECLARE, "parseVariableDeclare");
    handledKeywords.put(VariablesGenerator.SET, "parseVariableSet");
    handledKeywords.put(VariablesGenerator.DISCARD, "parseVariableDiscard");
  }

  public boolean generateNewStep(String currentCommand) {
    return !sameStep; // aus ArraySupport abgeschaut
  }

  public void parseVariableDeclare() throws IOException {
    // var_declare i [initValue val]
    int initStep = AnimalParseSupport.getCurrentStep();
    String value = "", role = null;
    ParseSupport.parseWord(stok, "variable key");

    String name = AnimalParseSupport.parseText(stok, "variable name");
    if (ParseSupport.parseOptionalWord(stok, "variable initial value",
        "initValue")) {
      value = AnimalParseSupport.parseText(stok, "initial value for variable");
    }
    
    if (ParseSupport.parseOptionalWord(stok, "variable initial role", "role")) {
      role = AnimalParseSupport.parseText(stok, "initial variable role");
    }

    VariableDeclaration varDec = new VariableDeclaration(initStep, name, value, 
        Variable.getRoleFromRoleString(role));

    BasicParser.addAnimatorToAnimation(varDec, anim);
  }

  public void parseVariableSet() throws IOException {
    // var_set "varname" to "val"
    int initStep = AnimalParseSupport.getCurrentStep();
    ParseSupport.parseWord(stok, "variable key");
    String name = AnimalParseSupport.parseText(stok, "variable name");

    ParseSupport.parseWord(stok, "variable value", "initValue");
    String value = AnimalParseSupport.parseText(stok, "value for variable");
    
    String role = null;
    if (ParseSupport.parseOptionalWord(stok, "variable role update", "role"))
      role = ParseSupport.parseText(stok, "variable role update");
    
    VariableUpdate varUpd = new VariableUpdate(initStep, name, value,
        Variable.getRoleFromRoleString(role));

    BasicParser.addAnimatorToAnimation(varUpd, anim);
  }

  public void parseVariableDiscard() throws IOException {
    // var_discard "varname"

    ParseSupport.parseWord(stok, "variable key");
    String varName = AnimalParseSupport.parseText(stok, "variable name");

    VariableDiscard varDisc = new VariableDiscard(AnimalParseSupport
        .getCurrentStep(), varName);
    BasicParser.addAnimatorToAnimation(varDisc, anim);
  }
}
