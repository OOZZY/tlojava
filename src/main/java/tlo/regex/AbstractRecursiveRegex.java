package tlo.regex;

public abstract class AbstractRecursiveRegex implements Regex {
  protected Regex regex;

  public AbstractRecursiveRegex(Regex regex) {
    this.regex = regex;
  }

  public Regex getRegex() {
    return regex;
  }

  public void setRegex(Regex regex) {
    this.regex = regex;
  }
}
