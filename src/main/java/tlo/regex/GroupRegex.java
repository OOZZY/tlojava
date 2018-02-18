package tlo.regex;

public class GroupRegex extends AbstractRecursiveRegex {
  public GroupRegex(Regex regex) {
    super(regex);
  }

  @Override
  public boolean match(String string) {
    return regex.match(string);
  }

  @Override
  public String toString() {
    return "Group[" + regex.toString() + "]";
  }

  @Override
  public String toRegexString() {
    return "(" + regex.toRegexString() + ")";
  }
}
