package tlo.regex;

public class StarRegex extends AbstractRecursiveRegex {
  public StarRegex(Regex regex) {
    super(regex);
  }

  @Override
  public String toString() {
    return "Star[" + regex.toString() + "]";
  }

  @Override
  public boolean match(String string) {
    if (string.isEmpty()) {
      return true;
    }

    return RegexUtils.matchAnySplitWithNonEmptyFirstPart(regex, this, string);
  }

  @Override
  public String unparse() {
    return regex.unparse() + "*";
  }
}
