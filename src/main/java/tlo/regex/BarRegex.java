package tlo.regex;

import java.util.ArrayList;
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
    List<Regex> regexAndSequences = new ArrayList<>();
    for (int i = 0; i < regexes.size(); ++i) {
      if (i == 0) {
        regexAndSequences.add(regexes.get(i));
      } else {
        regexAndSequences.add(RegexUtils.forceToSequence(regexes.get(i)));
      }
    }

    return regexAndSequences.stream().map(Regex::unparse)
        .collect(Collectors.joining("|"));
  }
}
