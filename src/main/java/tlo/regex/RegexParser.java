package tlo.regex;

import static tlo.regex.RegexUtils.createSpaces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegexParser {
  private static final Logger logger = LogManager.getLogger(RegexParser.class);
  private static final Set<Character> metacharacters;
  private static final Set<Character> charClassMetacharacters;

  static {
    metacharacters = new HashSet<>();
    metacharacters.add('|');
    metacharacters.add('*');
    metacharacters.add('(');
    metacharacters.add(')');
    metacharacters.add('?');
    metacharacters.add('+');
    metacharacters.add('.');
    metacharacters.add('\\');
    metacharacters.add('[');

    charClassMetacharacters = new HashSet<>();
    charClassMetacharacters.add('^');
    charClassMetacharacters.add('-');
    charClassMetacharacters.add('\\');
    charClassMetacharacters.add(']');
  }

  private String pattern;
  private int position;

  public Regex parse(String pattern) {
    if (pattern.isEmpty()) {
      throw new RegexParserException("Given regex pattern is empty.");
    }

    this.pattern = pattern;
    this.position = 0;
    return this.parseRegex(0);
  }

  private Regex parseRegex(int level) {
    String methodName = "parseRegex";
    this.logParseCall(methodName, level);

    Regex regex = parseSequence(level + 1);
    this.logParsedRegex(methodName, level, regex);

    List<Regex> regexes = new ArrayList<>();
    regexes.add(regex);

    while (accept(isCharacter('|'))) {
      Regex nextRegex = parseSequence(level + 1);
      this.logParsedRegex(methodName, level, nextRegex);
      regexes.add(nextRegex);
    }

    if (regexes.size() < 2) {
      return regex;
    }
    return new BarRegex(regexes);
  }

  private Regex parseSequence(int level) {
    String methodName = "parseSequence";
    this.logParseCall(methodName, level);

    Regex regex = parseItem(level + 1);
    this.logParsedRegex(methodName, level, regex);

    List<Regex> regexes = new ArrayList<>();
    regexes.add(regex);

    while (true) {
      try {
        Regex nextRegex = parseItem(level + 1);
        this.logParsedRegex(methodName, level, nextRegex);
        regexes.add(nextRegex);
      } catch (RegexParserException exception) {
        this.logExceptionMessage(methodName, level, "Error when parsing item",
            exception);
        break;
      }
    }

    if (regexes.size() < 2) {
      return regex;
    }
    return new SequenceRegex(regexes);
  }

  private Regex parseItem(int level) {
    String methodName = "parseItem";
    this.logParseCall(methodName, level);

    Regex regex = parseElement(level + 1);
    this.logParsedRegex(methodName, level, regex);

    if (accept(isCharacter('*'))) {
      return new StarRegex(regex);
    }

    if (accept(isCharacter('?'))) {
      return new QuestionRegex(regex);
    }

    if (accept(isCharacter('+'))) {
      return new PlusRegex(regex);
    }

    return regex;
  }

  private Regex parseElement(int level) {
    String methodName = "parseElement";
    this.logParseCall(methodName, level);

    if (accept(isCharacter('('))) {
      Regex regex = parseRegex(level + 1);
      this.logParsedRegex(methodName, level, regex);
      if (accept(isCharacter(')'))) {
        return new GroupRegex(regex);
      }
      throw new RegexParserException(
          String.format("Expected character ')' at position %s.", position));
    }

    Regex regex = parseCharacter(level + 1);
    this.logParsedRegex(methodName, level, regex);
    return regex;
  }

  private Regex parseCharacter(int level) {
    String methodName = "parseCharacter";
    this.logParseCall(methodName, level);

    if (accept(isCharacter('\\'))) {
      if (accept(isMetaCharacter)) {
        return new CharRegex(previousChar());
      }
      throw new RegexParserException(
          String.format("Expected metacharacter at position %s.", position));
    }

    if (accept(isCharacter('.'))) {
      return new DotRegex();
    }

    if (accept(isMetaCharacter.negate())) {
      return new CharRegex(previousChar());
    }
    throw new RegexParserException(
        String.format("Expected non-metacharacter at position %s.", position));
  }

  private boolean accept(Predicate<Character> predicate) {
    if (currentChar(predicate)) {
      advance();
      return true;
    }
    return false;
  }

  private boolean currentChar(Predicate<Character> predicate) {
    if (position >= pattern.length()) {
      return false;
    }

    if (predicate.test(currentChar())) {
      return true;
    }
    return false;
  }

  private char currentChar() {
    return pattern.charAt(position);
  }

  private char previousChar() {
    return pattern.charAt(position - 1);
  }

  private void advance() {
    position++;
  }

  private static Predicate<Character> isCharacter(char ch) {
    return arg -> arg == ch;
  }

  private static final Predicate<Character> isMetaCharacter = RegexParser::isMetaCharacter;

  public static boolean isMetaCharacter(char ch) {
    return metacharacters.contains(ch);
  }

  private void logParseCall(String methodName, int level) {
    if (logger.isTraceEnabled()) {
      logger.trace("{}{}: position: {}, remaining: \"{}\"", createSpaces(level),
          methodName, position, pattern.substring(position, pattern.length()));
    }
  }

  private void logParsedRegex(String methodName, int level, Regex regex) {
    if (logger.isTraceEnabled()) {
      logger.trace("{}{}: regex: {}", createSpaces(level), methodName, regex);
    }
  }

  private void logExceptionMessage(String methodName, int level, String context,
      Throwable exception) {
    if (logger.isTraceEnabled()) {
      logger.trace("{}{}: {}: {}", createSpaces(level), methodName, context,
          exception.getMessage());
    }
  }
}
