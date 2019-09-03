package generators.framework;

import javax.swing.tree.DefaultMutableTreeNode;

public class GeneratorTreeNode extends DefaultMutableTreeNode {
  /**
   * 
   */
  private static final long serialVersionUID = 5455405594903703245L;
  private transient Generator generator;
  public GeneratorTreeNode() {
    super();
  }
  public GeneratorTreeNode(Generator theGenerator) {
    super(theGenerator);
    generator = theGenerator;
  }
  
  public GeneratorTreeNode(Generator theGenerator, boolean allowsChildren) {
    super(theGenerator, allowsChildren);
    generator = theGenerator;
  }

  public Generator getGenerator() {
    return generator;
  }
  
  public String toString() {
    if (generator == null)
      return "null";
    return generator.getName();
  }
}
