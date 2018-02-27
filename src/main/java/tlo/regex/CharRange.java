package tlo.regex;

public class CharRange {
  private char first;
  private char last;

  public CharRange(char first, char last) {
    assert first <= last;
    this.first = first;
    this.last = last;
  }

  public char getFirst() {
    return first;
  }

  public void setFirst(char first) {
    assert first <= this.last;
    this.first = first;
  }

  public char getLast() {
    return last;
  }

  public void setLast(char last) {
    assert this.first <= last;
    this.last = last;
  }

  @Override
  public String toString() {
    if (first == last) {
      return "CharRange['" + first + "']";
    }
    return "CharRange['" + first + "'-'" + last + "']";
  }

  public boolean contains(char ch) {
    return this.first <= ch && ch <= this.last;
  }
}
