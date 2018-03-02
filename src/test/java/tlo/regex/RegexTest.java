package tlo.regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static tlo.regex.RegexUtils.alt;
import static tlo.regex.RegexUtils.chr;
import static tlo.regex.RegexUtils.chrc;
import static tlo.regex.RegexUtils.chrr;
import static tlo.regex.RegexUtils.dot;
import static tlo.regex.RegexUtils.grp;
import static tlo.regex.RegexUtils.opt;
import static tlo.regex.RegexUtils.plus;
import static tlo.regex.RegexUtils.seq;
import static tlo.regex.RegexUtils.star;

import org.junit.Test;

public class RegexTest {
  @Test
  public void canMatchCharacters() {
    assertFalse(chr('a').match("b"));
  }

  @Test
  public void canMatchCharacterSequencesOfSize2() {
    SequenceRegex sequence = seq(chr('a'), chr('b'));
    assertFalse(sequence.match(""));
    assertFalse(sequence.match("a"));
    assertFalse(sequence.match("b"));
    assertTrue(sequence.match("ab"));
    assertFalse(sequence.match("ba"));
    assertFalse(sequence.match("abc"));
  }

  @Test
  public void canMatchCharacterSequencesOfSize3() {
    SequenceRegex sequence = seq(chr('a'), chr('b'), chr('c'));
    assertFalse(sequence.match(""));
    assertFalse(sequence.match("a"));
    assertFalse(sequence.match("b"));
    assertFalse(sequence.match("ab"));
    assertFalse(sequence.match("ba"));
    assertTrue(sequence.match("abc"));
  }

  @Test
  public void canMatchCharacterSequencesOfSize10() {
    SequenceRegex sequence = seq(chr('a'), chr('b'), chr('c'), chr('d'),
        chr('e'), chr('f'), chr('g'), chr('h'), chr('i'), chr('j'));
    assertFalse(sequence.match("abcdefghi"));
    assertTrue(sequence.match("abcdefghij"));
    assertFalse(sequence.match("abcdefghijk"));
  }

  @Test
  public void canUnparse() {
    GroupRegex group = grp(alt(chr('a'), chr('b')));
    PlusRegex plus = plus(grp(opt(chr('f'))));
    SequenceRegex sequence = seq(group, chr('c'), plus, star(dot()));
    assertEquals("(a|b)c(f?)+.*", sequence.unparse());
  }

  @Test
  public void canMatchStars() {
    StarRegex star = star(chr('a'));
    assertTrue(star.match(""));
    assertTrue(star.match("a"));
    assertTrue(star.match("aa"));
    assertTrue(star.match("aaa"));
    assertFalse(star.match("b"));
    assertFalse(star.match("ab"));
    assertFalse(star.match("abc"));
  }

  @Test
  public void canMatchOptionals() {
    OptionalRegex optional = opt(chr('a'));
    assertTrue(optional.match(""));
    assertTrue(optional.match("a"));
    assertFalse(optional.match("aa"));
    assertFalse(optional.match("b"));
    assertFalse(optional.match("ab"));
  }

  @Test
  public void canMatchPluses() {
    PlusRegex plus = plus(chr('a'));
    assertFalse(plus.match(""));
    assertTrue(plus.match("a"));
    assertTrue(plus.match("aa"));
    assertTrue(plus.match("aaa"));
    assertFalse(plus.match("b"));
    assertFalse(plus.match("ab"));
    assertFalse(plus.match("abc"));
  }

  @Test
  public void canMatchCharacterClasses() {
    CharRange[] lowercaseRange = { chrr('a', 'z') };
    CharClassRegex lowercaseRegex = chrc(false, lowercaseRange);
    assertTrue(lowercaseRegex.match("a"));
    assertTrue(lowercaseRegex.match("m"));
    assertTrue(lowercaseRegex.match("z"));
    assertFalse(lowercaseRegex.match("A"));
    assertFalse(lowercaseRegex.match("0"));

    CharClassRegex nonLowercaseRegex = chrc(true, lowercaseRange);
    assertFalse(nonLowercaseRegex.match("a"));
    assertFalse(nonLowercaseRegex.match("m"));
    assertFalse(nonLowercaseRegex.match("z"));
    assertTrue(nonLowercaseRegex.match("A"));
    assertTrue(nonLowercaseRegex.match("0"));
  }

  @Test
  public void canProperlyWrapSubRegexesInGroupsWhenUnparsing() {
    Regex[] ab = { chr('a'), chr('b') };
    Regex[] cd = { chr('c'), chr('d') };
    Regex[] ef = { chr('e'), chr('f') };
    Regex[] alternations = { alt(ab), alt(cd), alt(ef) };
    Regex[] sequences = { seq(ab), seq(cd), seq(ef) };

    assertEquals("(ab)*", star(seq(ab)).unparse());
    assertEquals("a|b|(c|d)|(e|f)", alt(alternations).unparse());
    assertEquals("ab|cd|ef", alt(sequences).unparse());
    assertEquals("(a|b)(c|d)(e|f)", seq(alternations).unparse());
    assertEquals("ab(cd)(ef)", seq(sequences).unparse());
  }

  @Test
  public void canUseHashCodeAndEquals() {
    assertEquals(dot().hashCode(), dot().hashCode());
    assertEquals(dot(), dot());

    assertEquals(star(dot()).hashCode(), star(dot()).hashCode());
    assertEquals(star(dot()), star(dot()));

    assertEquals(alt(dot(), dot()).hashCode(), alt(dot(), dot()).hashCode());
    assertEquals(alt(dot(), dot()), alt(dot(), dot()));

    assertNotEquals(star(dot()), plus(dot()));
    assertNotEquals(alt(dot(), dot()), seq(dot(), dot()));
  }

  @Test
  public void canSimplify() {
    assertSimplification(alt(chr('a'), chr('a')), chr('a'));
    assertSimplification(star(star(chr('a'))), star(chr('a')));
    assertSimplification(star(seq(star(chr('a')), star(chr('b')))),
        star(alt(chr('a'), chr('b'))));
    assertSimplification(grp(chr('a')), chr('a'));
  }

  private void assertSimplification(Regex unsimplified, Regex simplified) {
    assertEquals(simplified, unsimplified.simplify());
  }
}
