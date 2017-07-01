package tlo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathTest {
  @Test
  public void factorialShouldReturnNFactorialWhenGivenN() {
    assertEquals(1, tlo.Math.factorial(0));
    assertEquals(1, tlo.Math.factorial(1));
    assertEquals(2, tlo.Math.factorial(2));
    assertEquals(6, tlo.Math.factorial(3));
    assertEquals(24, tlo.Math.factorial(4));
    assertEquals(120, tlo.Math.factorial(5));
    assertEquals(720, tlo.Math.factorial(6));
    assertEquals(5040, tlo.Math.factorial(7));
    assertEquals(40320, tlo.Math.factorial(8));
    assertEquals(362880, tlo.Math.factorial(9));
    assertEquals(3628800, tlo.Math.factorial(10));
  }
}
