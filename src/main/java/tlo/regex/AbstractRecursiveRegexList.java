package tlo.regex;

import java.util.List;

public abstract class AbstractRecursiveRegexList implements Regex {
  protected List<Regex> regexes;

  public AbstractRecursiveRegexList(List<Regex> regexes) {
    if (regexes.size() < 2) {
      throw new IllegalArgumentException("Regex list has less than 2 regexes.");
    }
    this.regexes = regexes;
  }

  public List<Regex> getRegexes() {
    return regexes;
  }

  public void setRegexes(List<Regex> regexes) {
    this.regexes = regexes;
  }
}
