package generators.maths.resolutionCalculus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Clause implements IClause {

	protected final HashSet<String> literals = new HashSet<>();

	public Clause() {
	}

	public Clause(String... literal) {
		for (String s : literal)
			addLiteral(s);
	}

	public void addLiteral(String literal) {
		literals.add(literal);
	}

	public boolean isUnsatisfiable() {
		return literals.size() == 0;
	}

	public List<Clause> resolveWith(Clause other, IResolveListener listener) {
		ArrayList<Clause> res = new ArrayList<>(Math.max(literals.size(), other.literals.size()));

		literals.forEach((literal) -> {
			String negated = literal.charAt(0) == '!' ? literal.substring(1, literal.length()) : "!".concat(literal);
			if (other.literals.contains(negated)) {
				Clause resolvent = new Clause();
				literals.forEach((l) -> {
					if (!l.equals(literal))
						resolvent.addLiteral(l);
				});
				other.literals.forEach((l) -> {
					if (!l.equals(negated))
						resolvent.addLiteral(l);
				});
				res.add(resolvent);
				listener.onResolve(literal, negated, resolvent);
			}
		});

		return res;
	}

	@Override
	public Clause getEmbeddedClause() {
		return this;
	}

	@Override
	public int literalCount() {
		return literals.size();
	}

	public String[] toRepresentation() {
		if (isUnsatisfiable()) {
			return new String[] { " " };
		}
		return literals.toArray(new String[literals.size()]);
	}

	@Override
	public String toString() {
		return Arrays.toString(toRepresentation());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Clause) {
			return literals.equals(((Clause) obj).literals);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return literals.hashCode();
	}

}
