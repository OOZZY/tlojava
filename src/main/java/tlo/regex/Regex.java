package tlo.regex;

public interface Regex {
  public boolean match(String string);

  public String toRegexString();
}
