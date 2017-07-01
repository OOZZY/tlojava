package tlo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StatisticsTest {
  private static final double EPSILON = tlo.Math.DOUBLE_EPSILON;

  private tlo.Statistics statistics;

  @Before
  public void setUp() {
    statistics = new tlo.Statistics();
  }

  @Test
  public void statisticsConstructorShouldInitializeStatistics() {
    assertEquals(0, statistics.size(), EPSILON);
    assertEquals(0, statistics.sum(), EPSILON);
    assertEquals(0, statistics.mean(), EPSILON);
    assertEquals(0, statistics.min(), EPSILON);
    assertEquals(0, statistics.max(), EPSILON);
    assertEquals(0, statistics.range(), EPSILON);
    assertEquals(0, statistics.variance(), EPSILON);
    assertEquals(0, statistics.stddeviation(), EPSILON);
  }

  @Test
  public void statisticsAddShouldUpdateStatisticsOverOneCall() {
    statistics.add(50);
    assertEquals(1, statistics.size(), EPSILON);
    assertEquals(50, statistics.sum(), EPSILON);
    assertEquals(50, statistics.mean(), EPSILON);
    assertEquals(50, statistics.min(), EPSILON);
    assertEquals(50, statistics.max(), EPSILON);
    assertEquals(0, statistics.range(), EPSILON);
    assertEquals(0, statistics.variance(), EPSILON);
    assertEquals(0, statistics.stddeviation(), EPSILON);
  }

  @Test
  public void statisticsAddShouldUpdateStatisticsOverMultipleCalls() {
    statistics.add(50);
    statistics.add(100);
    assertEquals(2, statistics.size(), EPSILON);
    assertEquals(150, statistics.sum(), EPSILON);
    assertEquals(75, statistics.mean(), EPSILON);
    assertEquals(50, statistics.min(), EPSILON);
    assertEquals(100, statistics.max(), EPSILON);
    assertEquals(50, statistics.range(), EPSILON);
    assertEquals(625, statistics.variance(), EPSILON);
    assertEquals(25, statistics.stddeviation(), EPSILON);
  }
}
