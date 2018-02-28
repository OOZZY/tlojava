package tlo.regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class RegexParserTest {
  private static final Logger logger = LogManager
      .getLogger(RegexParserTest.class);

  @Test
  public void testParsing() {
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
  public void testParsingCharacterClasses() {
    RegexParser parser = new RegexParser();
    if (logger.isDebugEnabled()) {
      logger.debug(parser.parse("[a]"));
      logger.debug(parser.parse("[a0]"));
      logger.debug(parser.parse("[a0-9]"));
      logger.debug(parser.parse("[a-z0]"));
      logger.debug(parser.parse("[a-z0-9]"));
      logger.debug(parser.parse("[^a]"));
      logger.debug(parser.parse("[^a0]"));
      logger.debug(parser.parse("[^a0-9]"));
      logger.debug(parser.parse("[^a-z0]"));
      logger.debug(parser.parse("[^a-z0-9]"));
      logger.debug(parser.parse("[\\^]"));
      logger.debug(parser.parse("[^\\^]"));
    }
  }

  public void assertParseSucceeds(RegexParser parser, String pattern) {
    Regex regex = parser.parse(pattern);
    assertEquals(regex.unparse(), pattern);
    if (logger.isDebugEnabled()) {
      logger.debug(regex);
      logger.debug(regex.unparse());
    }
  }

  @Test
  public void testParsingFailure() {
    RegexParser parser = new RegexParser();
    assertParseFails(parser, "");
    assertParseFails(parser, "(a");
    assertParseFails(parser, "\\a");
    assertParseFails(parser, "*");
    assertParseFails(parser, "a|");
  }

  @Test
  public void testParsingCharacterClassesFailure() {
    RegexParser parser = new RegexParser();
    assertParseFails(parser, "[^a");
    assertParseFails(parser, "[a");
    assertParseFails(parser, "[z-a]");
    assertParseFails(parser, "[\\a]");
    assertParseFails(parser, "[-]");
  }

  public void assertParseFails(RegexParser parser, String pattern) {
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

  @Test
  public void testMatching() {
    Regex regex = RegexUtils.parse("(a|b)c(f?)+.*");
    assertFalse(regex.match(""));
    assertFalse(regex.match("a"));
    assertTrue(regex.match("ac"));
    assertTrue(regex.match("bc"));
    assertTrue(regex.match("acf"));
    assertTrue(regex.match("bcf"));
    assertTrue(regex.match("acf2"));
    assertTrue(regex.match("bcf4"));
    assertTrue(regex.match("acff2"));
    assertTrue(regex.match("bcff4"));
    assertTrue(regex.match("ac2"));
    assertTrue(regex.match("bc4"));
  }
}
