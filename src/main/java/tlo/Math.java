package tlo;

public class Math {
  public static final double DOUBLE_BASE = 2;
  public static final double DOUBLE_PRECISION = 53;
  public static final double DOUBLE_EPSILON = java.lang.Math.pow(DOUBLE_BASE,
      -(DOUBLE_PRECISION - 1));

  public static long factorial(long n) {
    long product = 1;
    for (long i = 2; i <= n; ++i) {
      product *= i;
    }
    return product;
  }
}
