package generators.maths.resolutionCalculus;

public interface IResolveListener {

	public static IResolveListener doNotListen = (a, b, d) -> {
	};

	public void onResolve(String causingLiteral1, String causingLiteral2, Clause resolvent);

}
