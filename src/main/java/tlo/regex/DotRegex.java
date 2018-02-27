package tlo.regex;

public class DotRegex implements Regex {
  @Override
  public String toString() {
    return "Dot";
  }

  @Override
  public boolean match(String string) {
    return string.length() == 1;
  }

  @Override
  public String toRegexString() {
    return ".";
  }
}
