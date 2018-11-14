package tlo.regex;

public class GroupRegex extends AbstractRecursiveRegex {
  public GroupRegex(Regex regex) {
    super(regex);
  }

  @Override
  public String toString() {
    return "Group[" + regex.toString() + "]";
  }

  @Override
  public boolean match(String string) {
    return regex.match(string);
  }

  @Override
  public String unparse() {
    return "(" + regex.unparse() + ")";
  }

  @Override
  public Regex simplify() {
    return regex.simplify();
  }
}
