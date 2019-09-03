package generators.helpers.tsigaridas;

public class Element<T> {

  private Element<T> next;

  private T          content;

  Element(T content) {

    this.content = content;

  }

  public void setNext(Element<T> nextElement) {

    this.next = nextElement;

  }

  public T getContent() {

    return content;

  }

  public Element<T> getNext() {

    return next;

  }

}
