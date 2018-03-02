package tlo.regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
    AlternationRegex alternation = new AlternationRegex(
        Arrays.asList(new CharRegex('a'), new CharRegex('b')));
    GroupRegex group1 = new GroupRegex(alternation);

    CharRegex character = new CharRegex('c');

    OptionalRegex optional = new OptionalRegex(new CharRegex('f'));
    GroupRegex group2 = new GroupRegex(optional);
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
  public void testOptional() {
    OptionalRegex optional = new OptionalRegex(new CharRegex('a'));
    assertTrue(optional.match(""));
    assertTrue(optional.match("a"));
    assertFalse(optional.match("aa"));
    assertFalse(optional.match("b"));
    assertFalse(optional.match("ab"));
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

  @Test
  public void testUnparsing() {
    List<Regex> ab = Arrays.asList(new CharRegex('a'), new CharRegex('b'));
    List<Regex> cd = Arrays.asList(new CharRegex('c'), new CharRegex('d'));
    List<Regex> ef = Arrays.asList(new CharRegex('e'), new CharRegex('f'));
    AlternationRegex aOrB = new AlternationRegex(ab);
    AlternationRegex cOrD = new AlternationRegex(cd);
    AlternationRegex eOrF = new AlternationRegex(ef);
    SequenceRegex aThenB = new SequenceRegex(ab);
    SequenceRegex cThenD = new SequenceRegex(cd);
    SequenceRegex eThenF = new SequenceRegex(ef);
    List<Regex> alternationList = Arrays.asList(aOrB, cOrD, eOrF);
    List<Regex> sequenceList = Arrays.asList(aThenB, cThenD, eThenF);

    Regex trickyToUnparse = new StarRegex(aThenB);
    assertEquals("(ab)*", trickyToUnparse.unparse());

    Regex alternationOfAlternations = new AlternationRegex(alternationList);
    assertEquals("a|b|(c|d)|(e|f)", alternationOfAlternations.unparse());

    Regex alternationOfSequences = new AlternationRegex(sequenceList);
    assertEquals("ab|cd|ef", alternationOfSequences.unparse());

    Regex sequenceOfAlternations = new SequenceRegex(alternationList);
    assertEquals("(a|b)(c|d)(e|f)", sequenceOfAlternations.unparse());

    Regex sequenceOfSequences = new SequenceRegex(sequenceList);
    assertEquals("ab(cd)(ef)", sequenceOfSequences.unparse());
  }

  @Test
  public void testHashCodeAndEquals() {
    DotRegex dot1 = new DotRegex();
    DotRegex dot2 = new DotRegex();
    assertEquals(dot1.hashCode(), dot2.hashCode());
    assertEquals(dot1, dot2);

    StarRegex dotStar1 = new StarRegex(dot1);
    StarRegex dotStar2 = new StarRegex(dot2);
    assertEquals(dotStar1.hashCode(), dotStar2.hashCode());
    assertEquals(dotStar1, dotStar2);

    AlternationRegex dotOrDot1 = new AlternationRegex(
        Arrays.asList(dot1, dot2));
    AlternationRegex dotOrDot2 = new AlternationRegex(
        Arrays.asList(dot2, dot1));
    assertEquals(dotOrDot1.hashCode(), dotOrDot2.hashCode());
    assertEquals(dotOrDot1, dotOrDot2);

    PlusRegex dotPlus = new PlusRegex(dot1);
    assertNotEquals(dotStar1, dotPlus);

    SequenceRegex dotThenDot = new SequenceRegex(Arrays.asList(dot1, dot2));
    assertNotEquals(dotOrDot1, dotThenDot);
  }

  @Test
  public void testSimplification() {
    assertSimplification("a|a", "a");
    assertSimplification("(a*)*", "a*");
    assertSimplification("(a*b*)*", "(a|b)*");
    assertSimplification("(a)", "a");
  }

  private void assertSimplification(String unsimplified, String simplified) {
    Regex regex = RegexUtils.parse(unsimplified);
    assertEquals(simplified, regex.simplify().unparse());
  }
}
