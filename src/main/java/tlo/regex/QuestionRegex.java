package tlo.regex;

public class QuestionRegex extends AbstractRecursiveRegex {
  public QuestionRegex(Regex regex) {
    super(regex);
  }

  @Override
  public boolean match(String string) {
    if (string.isEmpty()) {
      return true;
    }

    return regex.match(string);
  }

  @Override
  public String toString() {
    return "Question[" + regex.toString() + "]";
  }

  @Override
  public String toRegexString() {
    return regex.toRegexString() + "?";
  }
}
