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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((charRanges == null) ? 0 : charRanges.hashCode());
    result = prime * result + (negated ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CharClassRegex other = (CharClassRegex) obj;
    if (charRanges == null) {
      if (other.charRanges != null) {
        return false;
      }
    } else if (!charRanges.equals(other.charRanges)) {
      return false;
    }
    if (negated != other.negated) {
      return false;
    }
    return true;
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
