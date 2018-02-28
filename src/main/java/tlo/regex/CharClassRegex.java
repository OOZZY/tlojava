package tlo.regex;

import java.util.List;
import java.util.stream.Collectors;

public class CharClassRegex implements Regex {
  private boolean negated;
  private List<CharRange> charRanges;

  public CharClassRegex(boolean negated, List<CharRange> charRanges) {
    this.negated = negated;
    this.charRanges = charRanges;
  }

  public boolean isNegated() {
    return negated;
  }

  public void setNegated(boolean negated) {
    this.negated = negated;
  }

  public List<CharRange> getCharRanges() {
    return charRanges;
  }

  public void setCharRanges(List<CharRange> charRanges) {
    this.charRanges = charRanges;
  }

  @Override
  public String toString() {
    return "CharClass[" + negated + ", " + charRanges + "]";
  }

  @Override
  public boolean match(String string) {
    if (string.length() != 1) {
      return false;
    }

    boolean isInACharRange = this.charRanges.stream()
        .anyMatch(charRange -> charRange.contains(string.charAt(0)));

    return this.negated ? !isInACharRange : isInACharRange;
  }

  @Override
  public String unparse() {
    String unparsedCharRanges = charRanges.stream().map(CharRange::unparse)
        .collect(Collectors.joining(""));
    return "[" + (this.negated ? "^" : "") + unparsedCharRanges + "]";
  }
}
