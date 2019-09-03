package generators.helpers.tsigaridas;

public class Schlange<T> {

  private Element<T> firstElement;

  private Element<T> lastElement;

  public void push(T el) {
    Element<T> e = new Element<T>(el);
    if (isEmpty()) {
      lastElement = e;
      firstElement = e;
    } else {
      lastElement.setNext(e);
      lastElement = e;
    }
  }

  public T peek() {
    if (firstElement != null) {
      return firstElement.getContent();
    }
    System.err.println(" Q u e u e   i s   a l r e a d y   e m p t y ! ! ! ");
    return null;
  }

  public boolean isEmpty() {
    if (firstElement == null) {
      return true;
    } else {
      return false;
    }
  }

  public T pop() {
    if (!isEmpty()) {
      T temp = peek();
      firstElement = firstElement.getNext();
      return temp;
    } else {
      System.err.println(" Q u e u e   i s   a l r e a d y   e m p t y ! ! ! ");
      return null;
    }
  }
}
