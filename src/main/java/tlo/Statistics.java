package tlo;

public class Statistics {
  private double size;
  private double sum;
  private double mean;
  private double min;
  private double max;
  private double range;
  private double sumsquares;
  private double meansquares;
  private double variance;
  private double stddeviation;

  public Statistics() {
    size = 0;
    sum = 0;
    mean = 0;
    min = 0;
    max = 0;
    range = 0;
    sumsquares = 0;
    meansquares = 0;
    variance = 0;
    stddeviation = 0;
  }

  public void add(double num) {
    size++;
    if (size == 1) {
      sum = num;
      mean = num;
      min = num;
      max = num;
      range = 0;
      sumsquares = num * num;
      meansquares = sumsquares;
      variance = 0;
      stddeviation = 0;
    } else {
      sum += num;
      mean = sum / size;
      if (min > num) {
        min = num;
      }
      if (max < num) {
        max = num;
      }
      range = max - min;
      sumsquares += num * num;
      meansquares = sumsquares / size;
      variance = meansquares - mean * mean;
      stddeviation = java.lang.Math.sqrt(variance);
    }
  }

  public double size() {
    return size;
  }

  public double sum() {
    return sum;
  }

  public double mean() {
    return mean;
  }

  public double min() {
    return min;
  }

  public double max() {
    return max;
  }

  public double range() {
    return range;
  }

  public double variance() {
    return variance;
  }

  public double stddeviation() {
    return stddeviation;
  }
}
