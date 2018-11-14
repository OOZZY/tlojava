package tlo.regex;

public class CharRegex implements Regex {
  private char character;

  public CharRegex(char character) {
    this.character = character;
  }

  public char getCharacter() {
    return character;
  }

  public void setCharacter(char character) {
    this.character = character;
  }

  @Override
  public String toString() {
    return "Char[" + character + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + character;
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
    CharRegex other = (CharRegex) obj;
    if (character != other.character) {
      return false;
    }
    return true;
  }

  @Override
  public boolean match(String string) {
    return string.length() == 1 && string.charAt(0) == character;
  }

  @Override
  public String unparse() {
    if (RegexParser.isMetacharacter(character)) {
      return "\\" + Character.valueOf(character).toString();
    }
    return Character.valueOf(character).toString();
  }
}
