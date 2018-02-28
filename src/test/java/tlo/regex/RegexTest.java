package tlo.regex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class RegexTest {
  private static final Logger logger = LogManager.getLogger(RegexTest.class);

  @Test
  public void testCharacter() {
    CharRegex character = new CharRegex('a');
    assertFalse(character.match("b"));
  }

  @Test
  public void testCharacterSequenceSize2() {
    SequenceRegex sequence = new SequenceRegex(
        Arrays.asList(new CharRegex('a'), new CharRegex('b')));
    assertFalse(sequence.match(""));
    assertFalse(sequence.match("a"));
    assertFalse(sequence.match("b"));
    assertTrue(sequence.match("ab"));
    assertFalse(sequence.match("ba"));
    assertFalse(sequence.match("abc"));
  }

  @Test
  public void testCharacterSequenceSize3() {
    SequenceRegex sequence = new SequenceRegex(Arrays.asList(new CharRegex('a'),
        new CharRegex('b'), new CharRegex('c')));
    assertFalse(sequence.match(""));
    assertFalse(sequence.match("a"));
    assertFalse(sequence.match("b"));
    assertFalse(sequence.match("ab"));
    assertFalse(sequence.match("ba"));
    assertTrue(sequence.match("abc"));
  }

  @Test
  public void testCharacterSequenceSize10() {
    SequenceRegex sequence = new SequenceRegex(Arrays.asList(new CharRegex('a'),
        new CharRegex('b'), new CharRegex('c'), new CharRegex('d'),
        new CharRegex('e'), new CharRegex('f'), new CharRegex('g'),
        new CharRegex('h'), new CharRegex('i'), new CharRegex('j')));
    assertFalse(sequence.match("abcdefghi"));
    assertTrue(sequence.match("abcdefghij"));
    assertFalse(sequence.match("abcdefghijk"));
  }

  @Test
  public void testToRegexString() {
    BarRegex bar = new BarRegex(
        Arrays.asList(new CharRegex('a'), new CharRegex('b')));
    GroupRegex group1 = new GroupRegex(bar);

    CharRegex character = new CharRegex('c');

    QuestionRegex question = new QuestionRegex(new CharRegex('f'));
    GroupRegex group2 = new GroupRegex(question);
    PlusRegex plus = new PlusRegex(group2);

    DotRegex dot = new DotRegex();
    StarRegex star = new StarRegex(dot);

    SequenceRegex sequence = new SequenceRegex(
        Arrays.asList(group1, character, plus, star));
    logger.debug(sequence);
    logger.debug(sequence.unparse());
  }

  @Test
  public void testStar() {
    StarRegex star = new StarRegex(new CharRegex('a'));
    assertTrue(star.match(""));
    assertTrue(star.match("a"));
    assertTrue(star.match("aa"));
    assertTrue(star.match("aaa"));
    assertFalse(star.match("b"));
    assertFalse(star.match("ab"));
    assertFalse(star.match("abc"));
  }

  @Test
  public void testQuestion() {
    QuestionRegex question = new QuestionRegex(new CharRegex('a'));
    assertTrue(question.match(""));
    assertTrue(question.match("a"));
    assertFalse(question.match("aa"));
    assertFalse(question.match("b"));
    assertFalse(question.match("ab"));
  }

  @Test
  public void testPlus() {
    PlusRegex plus = new PlusRegex(new CharRegex('a'));
    assertFalse(plus.match(""));
    assertTrue(plus.match("a"));
    assertTrue(plus.match("aa"));
    assertTrue(plus.match("aaa"));
    assertFalse(plus.match("b"));
    assertFalse(plus.match("ab"));
    assertFalse(plus.match("abc"));
  }

  @Test
  public void testCharacterClass() {
    List<CharRange> lowercaseRange = Arrays.asList(new CharRange('a', 'z'));
    CharClassRegex lowercaseRegex = new CharClassRegex(false, lowercaseRange);
    assertTrue(lowercaseRegex.match("a"));
    assertTrue(lowercaseRegex.match("m"));
    assertTrue(lowercaseRegex.match("z"));
    assertFalse(lowercaseRegex.match("A"));
    assertFalse(lowercaseRegex.match("0"));

    CharClassRegex nonLowercaseRegex = new CharClassRegex(true, lowercaseRange);
    assertFalse(nonLowercaseRegex.match("a"));
    assertFalse(nonLowercaseRegex.match("m"));
    assertFalse(nonLowercaseRegex.match("z"));
    assertTrue(nonLowercaseRegex.match("A"));
    assertTrue(nonLowercaseRegex.match("0"));
  }
}
