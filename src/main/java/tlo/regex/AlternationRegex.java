package tlo.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlternationRegex extends AbstractRecursiveRegexList {
  public AlternationRegex(List<Regex> regexes) {
    super(regexes);
  }

  @Override
  public String toString() {
    return "Alternation" + regexes.toString();
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

  @Override
  public Regex simplify() {
    List<Regex> simplifiedRegexes = regexes.stream().map(Regex::simplify)
        .collect(Collectors.toList());
    return createSimplifiedAlternationRegex(simplifiedRegexes);
  }

  static Regex createSimplifiedAlternationRegex(List<Regex> regexes) {
    AbstractRecursiveRegexList.expectMoreThanOneRegex(regexes);

    List<Regex> distinctRegexes = regexes.stream().distinct()
        .collect(Collectors.toList());
    if (distinctRegexes.size() < 2) {
      return distinctRegexes.get(0);
    } else {
      return new AlternationRegex(distinctRegexes);
    }
  }
}
