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
      regex = parseSequence(level + 1);
      this.logParsedRegex(methodName, level, regex);
      regexes.add(regex);
    }

    return regexes.size() < 2 ? regex : new AlternationRegex(regexes);
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
        regex = parseItem(level + 1);
        this.logParsedRegex(methodName, level, regex);
        regexes.add(regex);
      } catch (RegexParserException exception) {
        this.logExceptionMessage(methodName, level, "Error when parsing item",
            exception);
        break;
      }
    }

    return regexes.size() < 2 ? regex : new SequenceRegex(regexes);
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
      return new OptionalRegex(regex);
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

    Regex regex;
    if (currentChar(isCharacter('['))) {
      regex = parseCharClass(level + 1);
    } else {
      regex = parseCharacter(level + 1);
    }
    this.logParsedRegex(methodName, level, regex);
    return regex;
  }

  private Regex parseCharClass(int level) {
    String methodName = "parseCharClass";
    this.logParseCall(methodName, level);

    if (accept(isCharacter('['))) {
      boolean negated = false;
      if (accept(isCharacter('^'))) {
        negated = true;
      }

      List<CharRange> charRanges = parseCharClassItems(level + 1);
      this.logParsedObject(methodName, level, "char class items", charRanges);
      if (accept(isCharacter(']'))) {
        return new CharClassRegex(negated, charRanges);
      }
      throw new RegexParserException(
          String.format("Expected character ']' at position %s.", position));
    }
    throw new RegexParserException(
        String.format("Expected character '[' at position %s.", position));
  }

  private List<CharRange> parseCharClassItems(int level) {
    String methodName = "parseCharClassItems";
    this.logParseCall(methodName, level);

    CharRange charRange = parseCharClassItem(level + 1);
    this.logParsedObject(methodName, level, "char class item", charRange);

    List<CharRange> charRanges = new ArrayList<>();
    charRanges.add(charRange);

    while (true) {
      try {
        charRange = parseCharClassItem(level + 1);
        this.logParsedObject(methodName, level, "char class item", charRange);
        charRanges.add(charRange);
      } catch (RegexParserException exception) {
        this.logExceptionMessage(methodName, level,
            "Error when parsing character class item", exception);
        break;
      }
    }

    return charRanges;
  }

  private CharRange parseCharClassItem(int level) {
    String methodName = "parseCharClassItem";
    this.logParseCall(methodName, level);

    char first = parseCharClassCharacter(level + 1);
    this.logParsedObject(methodName, level, "char class character", first);

    if (accept(isCharacter('-'))) {
      char last = parseCharClassCharacter(level + 1);
      this.logParsedObject(methodName, level, "char class character", last);
      if (first > last) {
        throw new RegexParserException(String.format(
            "Expected character >= '%s' at position %s", first, position - 1));
      }
      return new CharRange(first, last);
    }

    return new CharRange(first, first);
  }

  private char parseCharClassCharacter(int level) {
    String methodName = "parseCharClassCharacter";
    this.logParseCall(methodName, level);

    if (accept(isCharacter('\\'))) {
      if (accept(isCharClassMetacharacter)) {
        return previousChar();
      }
      throw new RegexParserException(String.format(
          "Expected character class metacharacter at position %s.", position));
    }

    if (accept(isCharClassMetacharacter.negate())) {
      return previousChar();
    }
    throw new RegexParserException(String.format(
        "Expected character class non-metacharacter at position %s.",
        position));
  }

  private Regex parseCharacter(int level) {
    String methodName = "parseCharacter";
    this.logParseCall(methodName, level);

    if (accept(isCharacter('\\'))) {
      if (accept(isMetacharacter)) {
        return new CharRegex(previousChar());
      }
      throw new RegexParserException(
          String.format("Expected metacharacter at position %s.", position));
    }

    if (accept(isCharacter('.'))) {
      return new DotRegex();
    }

    if (accept(isMetacharacter.negate())) {
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

  private static final Predicate<Character> isCharClassMetacharacter = RegexParser::isCharClassMetacharacter;

  public static boolean isCharClassMetacharacter(char ch) {
    return charClassMetacharacters.contains(ch);
  }

  private static final Predicate<Character> isMetacharacter = RegexParser::isMetacharacter;

  public static boolean isMetacharacter(char ch) {
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

  private void logParsedObject(String methodName, int level, String objectType,
      Object object) {
    if (logger.isTraceEnabled()) {
      logger.trace("{}{}: {}: {}", createSpaces(level), methodName, objectType,
          object);
    }
  }
}
