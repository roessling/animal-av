package htdptl.ast;



public interface IVisitor {
	void visit(Expression expression);	
	void visit(Leaf leaf);	
}
