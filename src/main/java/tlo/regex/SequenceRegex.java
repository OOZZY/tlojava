package tlo.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SequenceRegex extends AbstractRecursiveRegexList {
  public SequenceRegex(List<Regex> regexes) {
    super(regexes);
  }

  @Override
  public String toString() {
    return "Sequence" + regexes.toString();
  }

  @Override
  public boolean match(String string) {
    if (regexes.size() == 2) {
      return RegexUtils.matchAnySplit(regexes.get(0), regexes.get(1), string);
    }

    SequenceRegex allExceptLast = new SequenceRegex(
        regexes.subList(0, regexes.size() - 1));
    Regex last = regexes.get(regexes.size() - 1);
    return RegexUtils.matchAnySplit(allExceptLast, last, string);
  }

  @Override
  public String unparse() {
    List<Regex> sequenceAndItems = new ArrayList<>();
    for (int i = 0; i < regexes.size(); ++i) {
      if (i == 0) {
        sequenceAndItems.add(RegexUtils.forceToSequence(regexes.get(i)));
      } else {
        sequenceAndItems.add(RegexUtils.forceToItem(regexes.get(i)));
      }
    }

    return sequenceAndItems.stream().map(Regex::unparse)
        .collect(Collectors.joining(""));
  }

  @Override
  public Regex simplify() {
    List<Regex> simplifiedRegexes = regexes.stream().map(Regex::simplify)
        .collect(Collectors.toList());
    return new SequenceRegex(simplifiedRegexes);
  }
}
