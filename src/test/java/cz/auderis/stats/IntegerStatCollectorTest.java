package cz.auderis.stats;

import cz.auderis.test.category.UnitTest;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class IntegerStatCollectorTest {

	private static final double ERROR_TOLERANCE = 1e-10;

	private IntegerStatCollector stat = new IntegerStatCollector();

	@Test
	@Category({ UnitTest.class })
	public void shouldCorrectlyCountSamples() throws Exception {
		for (int i = 1; i <= 100; ++i) {
			stat.addSample(i);
			assertThat(stat.getSampleCount(), is((long) i));
		}
	}

	@Test
	@Category({ UnitTest.class })
	public void shouldCalculateMinimumAndMaximum() throws Exception {
		for (int i = 2; i <= 50; ++i) {
			stat.addSample(i);
		}
		stat.addSample(1);
		for (int i = 100; i >= 51; --i) {
			stat.addSample(i);
		}
		assertThat(stat.getMinimum(), is(1));
		assertThat(stat.getMaximum(), is(100));
	}

	@Test
	@Category({ UnitTest.class })
	@Parameters({ "false", "true" })
	public void shouldCalculateMeanAndVarianceOfConstantSequence(boolean advancedStat) throws Exception {
		final IntegerStatCollector stat2 = new IntegerStatCollector(advancedStat);
		for (int i = 1; i <= 100; ++i) {
			stat2.addSample(10);
		}
		assertThat("mean", stat2.getMean(), closeTo(10.0, ERROR_TOLERANCE));
		assertThat("variance", stat2.getVariance(), closeTo(0.0, ERROR_TOLERANCE));
	}

	@Test
	@Category({ UnitTest.class })
	public void shouldCalculateSkewnessAndKurtosisOfConstantSequence() throws Exception {
		final IntegerStatCollector stat2 = new IntegerStatCollector(true);
		for (int i = 1; i <= 100; ++i) {
			stat2.addSample(10);
		}
		assertThat("skewness", stat2.getSkewness(), is(Double.NaN));
		assertThat("kurtosis", stat2.getKurtosis(), is(Double.NaN));
	}

	@Test
	@Category({ UnitTest.class })
	@Parameters({ "false", "true" })
	public void shouldCalculateMeanAndVarianceOfOscillatingSequence(boolean advancedStat) throws Exception {
		final IntegerStatCollector stat2 = new IntegerStatCollector(advancedStat);
		for (int i = 1; i <= 50; ++i) {
			stat2.addSample(10);
			stat2.addSample(-10);
		}
		assertThat("mean", stat2.getMean(), closeTo(0.0, ERROR_TOLERANCE));
		assertThat("variance", stat2.getVariance(), closeTo(101.01010101010101, ERROR_TOLERANCE));
	}

	@Test
	@Category({ UnitTest.class })
	public void shouldCalculateSkewnessAndKurtosisOfOscillatingSequence() throws Exception {
		final IntegerStatCollector stat2 = new IntegerStatCollector(true);
		for (int i = 1; i <= 50; ++i) {
			stat2.addSample(10);
			stat2.addSample(-10);
		}
		assertThat("skewness", stat2.getSkewness(), closeTo(0.0, ERROR_TOLERANCE));
		assertThat("kurtosis", stat2.getKurtosis(), closeTo(-2.0, ERROR_TOLERANCE));
	}

	@Test
	@Category({ UnitTest.class })
	public void shouldCalculateStatsOfDataSequence() throws Exception {
		// Given (data from R: faithful$waiting)
		final int[] faithfulWaiting = {
				79, 54, 74, 62, 85, 55, 88, 85, 51, 85,
				54, 84, 78, 47, 83, 52, 62, 84, 52, 79,
				51, 47, 78, 69, 74, 83, 55, 76, 78, 79,
				73, 77, 66, 80, 74, 52, 48, 80, 59, 90,
				80, 58, 84, 58, 73, 83, 64, 53, 82, 59,
				75, 90, 54, 80, 54, 83, 71, 64, 77, 81,
				59, 84, 48, 82, 60, 92
		};
		final IntegerStatCollector stat2 = new IntegerStatCollector(true);
		// When
		for (final int value : faithfulWaiting) {
			stat2.addSample(value);
		}
		// Then
		assertThat("samples", stat2.getSampleCount(), is((long) faithfulWaiting.length));
		assertThat("mean", stat2.getMean(), closeTo(70.151515151515, ERROR_TOLERANCE));
		assertThat("variance", stat2.getVariance(), closeTo(179.4228438228438, ERROR_TOLERANCE));
		assertThat("skewness", stat2.getSkewness(), closeTo(-0.2838771, 0.01));
		assertThat("kurtosis", stat2.getKurtosis(), closeTo(-1.36, 0.01));
	}

}