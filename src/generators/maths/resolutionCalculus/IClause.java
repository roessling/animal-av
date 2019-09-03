package generators.maths.resolutionCalculus;

import java.util.List;

public interface IClause {

	public void addLiteral(String literal);

	public boolean isUnsatisfiable();

	public List<Clause> resolveWith(Clause other, IResolveListener listener);

	/**
	 * Any class implementing this interface must be or at least embedded an
	 * instance of {@link Clause}
	 * 
	 * @return said instance
	 */
	public Clause getEmbeddedClause();

	public int literalCount();

	public String[] toRepresentation();

}
