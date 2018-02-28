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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + first;
    result = prime * result + last;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CharRange other = (CharRange) obj;
    if (first != other.first) {
      return false;
    }
    if (last != other.last) {
      return false;
    }
    return true;
  }

  public boolean contains(char ch) {
    return this.first <= ch && ch <= this.last;
  }

  public String unparse() {
    if (first == last) {
      return unparse(first);
    }
    return unparse(first) + "-" + unparse(last);
  }

  private static String unparse(char ch) {
    if (RegexParser.isCharClassMetacharacter(ch)) {
      return "\\" + Character.valueOf(ch).toString();
    }
    return Character.valueOf(ch).toString();
  }
}
