package algoanim.primitives.generators;

import algoanim.animalscript.AnimalGenerator;
import algoanim.animalscript.AnimalScript;
import animal.variables.Variable;
import animal.variables.VariableRoles;

public class AnimalVariablesGenerator extends AnimalGenerator implements
    VariablesGenerator {
  public AnimalVariablesGenerator(Language lang) {
    super(lang);
  }

  public void declare(String key) {
    declare(key, " ", VariableRoles.UNKNOWN);
  }

  public void declare(String key, String value) {
    declare(key, value, VariableRoles.UNKNOWN);
  }
  
  public void declare(String key, String value, VariableRoles role) {
    StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);

    str.append(VariablesGenerator.DECLARE).append(" ").append(key);

    if (value != null && value.length() > 0)
      str.append(" initvalue \"").append(value).append("\"");

    if (role != VariableRoles.UNKNOWN && role != null)
      str.append(" role \"").append(Variable.getRoleString(role)).append("\"");
    lang.addLine(str);
  }


  public void update(String key, String value) {
    update(key, value, null);
  }
  
  public void update(String key, String value, VariableRoles newRole) {
    StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);

    str.append(VariablesGenerator.SET).append(" ").append(key);
    str.append(" to \"").append(value).append("\"");
   
    if (newRole != null)
      str.append(" role \"").append(Variable.getRoleString(newRole)).append("\"");

    lang.addLine(str);
  }


  public void discard(String key) {
    StringBuilder str = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);

    str.append(VariablesGenerator.DISCARD + " " + key);

    lang.addLine(str);
  }


}
