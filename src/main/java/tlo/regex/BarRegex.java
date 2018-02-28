package tlo.regex;

import java.util.List;
import java.util.stream.Collectors;

public class BarRegex extends AbstractRecursiveRegexList {
  public BarRegex(List<Regex> regexes) {
    super(regexes);
  }

  @Override
  public String toString() {
    return "Bar" + regexes.toString();
  }

  @Override
  public boolean match(String string) {
    return regexes.stream().anyMatch(regex -> regex.match(string));
  }

  @Override
  public String unparse() {
    return regexes.stream().map(Regex::unparse)
        .collect(Collectors.joining("|"));
  }
}
