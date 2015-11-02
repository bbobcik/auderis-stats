package cz.auderis.stats;

public abstract class AbstractNumericStatCollector extends AbstractStatistic {
	private static final long serialVersionUID = 20151101L;

	private final boolean collectingHigherMoments;
	protected long samples;
	protected double runningMean;
	protected double runningVarianceAccumulator;
	protected double runningMoment3Accumulator;
	protected double runningMoment4Accumulator;

	public AbstractNumericStatCollector(boolean advancedStatsEnabled) {
		super();
		this.collectingHigherMoments = advancedStatsEnabled;
	}

	public AbstractNumericStatCollector(String id, boolean advancedStatsEnabled) {
		super(id);
		this.collectingHigherMoments = advancedStatsEnabled;
	}

	public long getSampleCount() {
		return samples;
	}

	public double getMean() {
		return (samples != 0L) ? runningMean : Double.NaN;
	}

	public double getVariance() {
		return (samples > 1L) ? (runningVarianceAccumulator / (samples - 1L)) : Double.NaN;
	}

	public double getStdDev() {
		return Math.sqrt(getVariance());
	}

	public boolean isAdvancedStatisticsEnabled() {
		return collectingHigherMoments;
	}

	public double getSkewness() {
		if (!collectingHigherMoments) {
			throw new IllegalStateException("advanced stats not available");
		} else if (samples == 0L) {
			return Double.NaN;
		}
		final double m2Factor = Math.pow(runningVarianceAccumulator, 1.5);
		return runningMoment3Accumulator * Math.sqrt(samples) / m2Factor;
	}

	public double getKurtosis() {
		if (!collectingHigherMoments) {
			throw new IllegalStateException("advanced stats not available");
		} else if (samples == 0L) {
			return Double.NaN;
		}
		final double m2Squared = runningVarianceAccumulator * runningVarianceAccumulator;
		return runningMoment4Accumulator * samples / m2Squared - 3.0;
	}

	public void shutdown() {
		// Does nothing
	}

	protected void addSample(double sampleValue) {
		++samples;
		final double oldDelta = sampleValue - runningMean;
		if (collectingHigherMoments) {
			final double weightedDelta = oldDelta / samples;
			final double squareWeightedDelta = weightedDelta * weightedDelta;
			final double term = oldDelta * weightedDelta * (samples - 1L);
			runningMean += weightedDelta;
			runningMoment4Accumulator += term * squareWeightedDelta * (samples*samples - 3.0*samples + 3.0)
							+ 6.0 * squareWeightedDelta * runningVarianceAccumulator
							- 4.0 * weightedDelta * runningMoment3Accumulator;
			runningMoment3Accumulator += term * weightedDelta * (samples - 2.0)
							- 3.0 * weightedDelta * runningVarianceAccumulator;
			runningVarianceAccumulator += term;
		} else {
			runningMean += oldDelta / samples;
			final double newDelta = sampleValue - runningMean;
			runningVarianceAccumulator += oldDelta * newDelta;
		}
	}

	@Override
	protected void appendParams(StringBuilder str, boolean hasContentsBefore) {
		if (hasContentsBefore) {
			str.append(", ");
		}
		str.append("n=").append(samples);
		if (samples > 1L) {
			str.append(", avg=").append(runningMean);
			str.append(", stDev=").append(getStdDev());
		}
	}

}
