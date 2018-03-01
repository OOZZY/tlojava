package tlo.regex;

public class OptionalRegex extends AbstractRecursiveRegex {
  public OptionalRegex(Regex regex) {
    super(regex);
  }

  @Override
  public String toString() {
    return "Optional[" + regex.toString() + "]";
  }

  @Override
  public boolean match(String string) {
    if (string.isEmpty()) {
      return true;
    }

    return regex.match(string);
  }

  @Override
  public String unparse() {
    return RegexUtils.forceToElement(regex).unparse() + "?";
  }
}
