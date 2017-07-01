package tlo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilTest {
  @Test
  public void printJavaVersionShouldReturnTheVersionOfJava() {
    assertEquals(System.getProperty("java.version"),
        tlo.Util.printJavaVersion());
  }
}
