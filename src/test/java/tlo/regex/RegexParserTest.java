package tlo.regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class RegexParserTest {
  private static final Logger logger = LogManager
      .getLogger(RegexParserTest.class);

  @Test
  public void parserParsesValidRegexes() {
    RegexParser parser = new RegexParser();
    assertParseSucceeds(parser, "a");
    assertParseSucceeds(parser, "a*");
    assertParseSucceeds(parser, "(a)");
    assertParseSucceeds(parser, "a|b");
    assertParseSucceeds(parser, "(a|b)c(f?)+.*");
    assertParseSucceeds(parser, "((\\+)*)?");
    assertParseSucceeds(parser, "((\\\\+)*)?");
  }

  @Test
  public void parserParsesValidRegexesWithCharacterClasses() {
    RegexParser parser = new RegexParser();
    assertParseSucceeds(parser, "[a]");
    assertParseSucceeds(parser, "[a0]");
    assertParseSucceeds(parser, "[a0-9]");
    assertParseSucceeds(parser, "[a-z0]");
    assertParseSucceeds(parser, "[a-z0-9]");
    assertParseSucceeds(parser, "[^a]");
    assertParseSucceeds(parser, "[^a0]");
    assertParseSucceeds(parser, "[^a0-9]");
    assertParseSucceeds(parser, "[^a-z0]");
    assertParseSucceeds(parser, "[^a-z0-9]");
    assertParseSucceeds(parser, "[\\^]");
    assertParseSucceeds(parser, "[^\\^]");
  }

  private void assertParseSucceeds(RegexParser parser, String pattern) {
    Regex regex = parser.parse(pattern);
    assertEquals(pattern, regex.unparse());
    if (logger.isDebugEnabled()) {
      logger.debug("\"{}\" parses to: {}", pattern, regex);
    }
  }

  @Test
  public void parsesFailsForInvalidRegexes() {
    RegexParser parser = new RegexParser();
    assertParseFails(parser, "");
    assertParseFails(parser, "(a");
    assertParseFails(parser, "\\a");
    assertParseFails(parser, "*");
    assertParseFails(parser, "a|");
  }

  @Test
  public void parsesFailsForRegexesWithInvalidCharacterClasses() {
    RegexParser parser = new RegexParser();
    assertParseFails(parser, "[^a");
    assertParseFails(parser, "[a");
    assertParseFails(parser, "[z-a]");
    assertParseFails(parser, "[\\a]");
    assertParseFails(parser, "[-]");
  }

  private void assertParseFails(RegexParser parser, String pattern) {
    try {
      parser.parse(pattern);
      fail();
    } catch (Exception exception) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error message when parsing \"{}\": {}", pattern,
            exception.getMessage());
      }
      return;
    }
  }
}
