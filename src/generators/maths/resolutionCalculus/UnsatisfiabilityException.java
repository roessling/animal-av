package generators.maths.resolutionCalculus;

public class UnsatisfiabilityException extends Exception {

	private static final long serialVersionUID = 1L;

	private final AnimationDecoratedClause cause1;
	private final AnimationDecoratedClause cause2;
	private final AnimationDecoratedClause emptyClauseProve;

	public UnsatisfiabilityException(AnimationDecoratedClause cause1, AnimationDecoratedClause cause2,
			AnimationDecoratedClause emptyClauseProve) {
		this.cause1 = cause1;
		this.cause2 = cause2;
		this.emptyClauseProve = emptyClauseProve;
	}

	public AnimationDecoratedClause getCause1() {
		return cause1;
	}

	public AnimationDecoratedClause getCause2() {
		return cause2;
	}

	public AnimationDecoratedClause getEmptyClauseProve() {
		return emptyClauseProve;
	}

}
