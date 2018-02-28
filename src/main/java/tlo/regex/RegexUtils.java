package tlo.regex;

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

  static Regex forceToSequence(Regex regex) {
    if (regex.getClass() == BarRegex.class) {
      return new GroupRegex(regex);
    }
    return regex;
  }

  static Regex forceToItem(Regex regex) {
    if (regex.getClass() == SequenceRegex.class) {
      return new GroupRegex(regex);
    }
    return forceToSequence(regex);
  }

  static Regex forceToElement(Regex regex) {
    if (regex.getClass() == StarRegex.class
        || regex.getClass() == QuestionRegex.class
        || regex.getClass() == PlusRegex.class) {
      return new GroupRegex(regex);
    }
    return forceToItem(regex);
  }
}
