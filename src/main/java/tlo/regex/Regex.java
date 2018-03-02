package tlo.regex;

public interface Regex {
  public boolean match(String string);

  public String unparse();

  public default Regex simplify() {
    return this;
  }
}
