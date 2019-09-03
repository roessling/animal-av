package generators.misc.helpers;

public abstract class CleanActionsWithState implements CleanActions {
	int state;

	public CleanActionsWithState(int s) {
		state = s;
	}
}