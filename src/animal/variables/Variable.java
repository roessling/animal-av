package animal.variables;

public class Variable
{
	private String name;
	private String value;
	private int initStep;
	private VariableRoles role;

	public Variable(String name) {
		this(name, "", 0, VariableRoles.UNKNOWN);
	}
	public Variable(String name, String value) {
		this(name, value, 0, VariableRoles.UNKNOWN);
	}

  public Variable(String name, String value, int initStep) {
    this(name, value, initStep, VariableRoles.UNKNOWN);
  }
  
	public Variable(String varName, String varValue, int varInitStep, VariableRoles varRole) {
		name = varName;
		value = varValue;
		initStep = varInitStep;
		if (varRole != null)
		  role = varRole;
		else
		  role = VariableRoles.UNKNOWN;
	}

	public int getInitStep() {
		return initStep;
	}
	public String getName() {
		return name;
	}

	public VariableRoles getRole() {
	  return role;
	}
	
	public void setRole(VariableRoles newRole) {
	  role = newRole;
	  if (newRole == null)
	    role = VariableRoles.UNKNOWN;
	}

	public static VariableRoles getRoleFromRoleString(String roleString) {
	  if (roleString == null)
	    return VariableRoles.UNKNOWN;
	  if (roleString.equalsIgnoreCase("Stepper"))
	    return VariableRoles.STEPPER;
    if (roleString.equalsIgnoreCase("Temporary"))
      return VariableRoles.TEMPORARY;
    if (roleString.equalsIgnoreCase("Organizer"))
      return VariableRoles.ORGANIZER;
    if (roleString.equalsIgnoreCase("Fixed Value"))
      return VariableRoles.FIXED_VALUE;
    if (roleString.equalsIgnoreCase("Most-wanted holder"))
      return VariableRoles.MOST_WANTED_HOLDER;
    if (roleString.equalsIgnoreCase("Most-recent holder"))
      return VariableRoles.MOST_RECENT_HOLDER;
    if (roleString.equalsIgnoreCase("One-way flag"))
      return VariableRoles.ONE_WAY_FLAG;
    if (roleString.equalsIgnoreCase("Follower"))
      return VariableRoles.FOLLOWER;
    if (roleString.equalsIgnoreCase("Gatherer"))
      return VariableRoles.GATHERER;
    if (roleString.equalsIgnoreCase("Container"))
      return VariableRoles.CONTAINER;
    if (roleString.equalsIgnoreCase("Walker"))
      return VariableRoles.WALKER;
    return VariableRoles.UNKNOWN;
	}

	public static String getRoleString(VariableRoles role) {
	   switch(role) {
//     case UNKNOWN: return "Unknown";
     case STEPPER: return "Stepper";
     case TEMPORARY: return "Temporary";
     case ORGANIZER: return "Organizer";
     case FIXED_VALUE: return "Fixed Value";
     case MOST_WANTED_HOLDER: return "Most-wanted holder";
     case MOST_RECENT_HOLDER: return "Most-recent holder";
     case ONE_WAY_FLAG: return "One-way flag";
     case FOLLOWER: return "Follower";
     case GATHERER: return "Gatherer";
     case CONTAINER: return "Container";
     case WALKER: return "Walker";
     default: return "Unknown";
   }
	}
	
	public String getRoleString() {
	  return Variable.getRoleString(role);
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public String toString() {
	  StringBuilder sb = new StringBuilder(48);
	  sb.append("<Variable: name=").append(name).append("; value=");
	  sb.append(value).append("; initStep=").append(initStep);
	  sb.append("; role: ").append(getRoleString()).append(">");
	  return sb.toString();
	}
}
