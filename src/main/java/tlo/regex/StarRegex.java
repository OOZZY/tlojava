package tlo.regex;

import java.util.List;
import java.util.stream.Collectors;

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
    return RegexUtils.forceToElement(regex).unparse() + "*";
  }

  @Override
  public Regex simplify() {
    return createSimplifiedStarRegex(regex.simplify());
  }

  private static Regex createSimplifiedStarRegex(Regex regex) {
    if (regex.getClass() == StarRegex.class) {
      StarRegex star = (StarRegex) regex;
      return createSimplifiedStarRegex(star.getRegex());
    }

    if (regex.getClass() == SequenceRegex.class) {
      SequenceRegex sequence = (SequenceRegex) regex;
      List<Regex> regexes = sequence.getRegexes();
      boolean allRegexesAreStars = regexes.stream()
          .allMatch(item -> item.getClass() == StarRegex.class);

      if (allRegexesAreStars) {
        List<Regex> unstarredRegexes = regexes.stream().map(item -> {
          StarRegex star = (StarRegex) item;
          return star.getRegex();
        }).collect(Collectors.toList());

        return createSimplifiedStarRegex(AlternationRegex
            .createSimplifiedAlternationRegex(unstarredRegexes));
      }
    }

    return new StarRegex(regex);
  }
}
