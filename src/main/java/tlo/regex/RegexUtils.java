package tlo.regex;

import java.util.Arrays;

public class RegexUtils {
  static boolean matchAnySplit(Regex regex1, Regex regex2, String string) {
    if (string.isEmpty()) {
      return matchSplit(regex1, regex2, "", "");
    }

    if (matchSplit(regex1, regex2, "", string)) {
      return true;
    }

    if (matchAnySplitWithNonEmptyFirstPart(regex1, regex2, string)) {
      return true;
    }

    return false;
  }

  static boolean matchAnySplitWithNonEmptyFirstPart(Regex regex1, Regex regex2,
      String string) {
    for (int i = 1; i < string.length(); ++i) {
      String part1 = string.substring(0, i);
      String part2 = string.substring(i, string.length());
      if (matchSplit(regex1, regex2, part1, part2)) {
        return true;
      }
    }

    if (matchSplit(regex1, regex2, string, "")) {
      return true;
    }

    return false;
  }

  private static boolean matchSplit(Regex regex1, Regex regex2, String part1,
      String part2) {
    return regex1.match(part1) && regex2.match(part2);
  }

  static String createSpaces(int level) {
    if (level < 0) {
      return "";
    }

    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < level; ++i) {
      stringBuilder.append("  ");
    }
    return stringBuilder.toString();
  }

  public static Regex parse(String pattern) {
    RegexParser parser = new RegexParser();
    return parser.parse(pattern);
  }

  static Regex forceToElement(Regex regex) {
    if (regex.getClass() == StarRegex.class
        || regex.getClass() == OptionalRegex.class
        || regex.getClass() == PlusRegex.class) {
      return new GroupRegex(regex);
    }
    return forceToItem(regex);
  }

  static Regex forceToItem(Regex regex) {
    if (regex.getClass() == SequenceRegex.class) {
      return new GroupRegex(regex);
    }
    return forceToSequence(regex);
  }

  static Regex forceToSequence(Regex regex) {
    if (regex.getClass() == AlternationRegex.class) {
      return new GroupRegex(regex);
    }
    return regex;
  }

  static DotRegex dot() {
    return new DotRegex();
  }

  static CharRegex chr(char ch) {
    return new CharRegex(ch);
  }

  static CharRange chrr(char ch) {
    return new CharRange(ch, ch);
  }

  static CharRange chrr(char first, char last) {
    return new CharRange(first, last);
  }

  static CharClassRegex chrc(boolean negated, CharRange... charRanges) {
    return new CharClassRegex(negated, Arrays.asList(charRanges));
  }

  static GroupRegex grp(Regex regex) {
    return new GroupRegex(regex);
  }

  static StarRegex star(Regex regex) {
    return new StarRegex(regex);
  }

  static OptionalRegex opt(Regex regex) {
    return new OptionalRegex(regex);
  }

  static PlusRegex plus(Regex regex) {
    return new PlusRegex(regex);
  }

  static SequenceRegex seq(Regex... regexes) {
    return new SequenceRegex(Arrays.asList(regexes));
  }

  static AlternationRegex alt(Regex... regexes) {
    return new AlternationRegex(Arrays.asList(regexes));
  }
}
