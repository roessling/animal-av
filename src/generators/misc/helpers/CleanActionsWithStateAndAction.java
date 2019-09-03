package generators.misc.helpers;

public abstract class CleanActionsWithStateAndAction implements CleanActions {
	int state;
	int action;

	public CleanActionsWithStateAndAction(int s, int a) {
		state = s;
		action = a;
	}
}