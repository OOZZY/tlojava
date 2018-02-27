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
  public boolean match(String string) {
    return string.length() == 1 && string.charAt(0) == character;
  }

  @Override
  public String toRegexString() {
    if (RegexParser.isMetaCharacter(character)) {
      return "\\" + Character.valueOf(character).toString();
    }
    return Character.valueOf(character).toString();
  }
}
