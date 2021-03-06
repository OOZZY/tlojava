package tlo.regex;

public class PlusRegex extends AbstractRecursiveRegex {
  public PlusRegex(Regex regex) {
    super(regex);
  }

  @Override
  public String toString() {
    return "Plus[" + regex.toString() + "]";
  }

  @Override
  public boolean match(String string) {
    if (string.isEmpty()) {
      return regex.match(string);
    }

    return RegexUtils.matchAnySplit(regex, new StarRegex(regex), string);
  }

  @Override
  public String unparse() {
    return RegexUtils.forceToElement(regex).unparse() + "+";
  }

  @Override
  public Regex simplify() {
    return new PlusRegex(regex.simplify());
  }
}
