package animal.editor;

public interface MethodSelectionListener {
  public void addMethodSelection(String newMethodName);
  public boolean hasMethodSelection(String methodName);
  public void removeMethodSelection(String methodName);
  public void selectMethodSelection(String methodName);
}
