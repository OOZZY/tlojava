package tlo.regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RegexIntegrationTest {
  @Test
  public void canParseThenMatch() {
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

  @Test
  public void canParseThenMatchWithCharacterClasses() {
    Regex regex = RegexUtils.parse("0[a-z]*9");
    assertTrue(regex.match("09"));
    assertTrue(regex.match("0a9"));
    assertTrue(regex.match("0z9"));
    assertTrue(regex.match("0az9"));
    assertFalse(regex.match("049"));

    regex = RegexUtils.parse("0[^a-z]*9");
    assertTrue(regex.match("09"));
    assertTrue(regex.match("0A9"));
    assertTrue(regex.match("0Z9"));
    assertTrue(regex.match("0AZ9"));
    assertTrue(regex.match("049"));
    assertFalse(regex.match("0a9"));
  }

  @Test
  public void canParseThenSimplifyThenUnparse() {
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
