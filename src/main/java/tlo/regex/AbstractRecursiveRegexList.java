package tlo.regex;

import java.util.List;

public abstract class AbstractRecursiveRegexList implements Regex {
  protected List<Regex> regexes;

  public AbstractRecursiveRegexList(List<Regex> regexes) {
    expectMoreThanOneRegex(regexes);
    this.regexes = regexes;
  }

  static void expectMoreThanOneRegex(List<Regex> regexes) {
    if (regexes.size() < 2) {
      throw new IllegalArgumentException("Regex list has less than 2 regexes.");
    }
  }

  public List<Regex> getRegexes() {
    return regexes;
  }

  public void setRegexes(List<Regex> regexes) {
    this.regexes = regexes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((regexes == null) ? 0 : regexes.hashCode());
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
    AbstractRecursiveRegexList other = (AbstractRecursiveRegexList) obj;
    if (regexes == null) {
      if (other.regexes != null) {
        return false;
      }
    } else if (!regexes.equals(other.regexes)) {
      return false;
    }
    return true;
  }
}
