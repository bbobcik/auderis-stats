package cz.auderis.stats;

public class LongStatCollector extends AbstractNumericStatCollector {
	private static final long serialVersionUID = 20151101L;

	protected long minimum;
	protected long maximum;

	public LongStatCollector() {
		this(null, false);
	}

	public LongStatCollector(boolean advancedStatsEnabled) {
		this(null, advancedStatsEnabled);
	}

	public LongStatCollector(String id, boolean advancedStatsEnabled) {
		super(id, advancedStatsEnabled);
		minimum = Integer.MAX_VALUE;
		maximum = Integer.MIN_VALUE;
	}

	public void addSample(long sampleValue) {
		super.addSample(sampleValue);
		if (sampleValue < minimum) {
			minimum = sampleValue;
		}
		if (sampleValue > maximum) {
			maximum = sampleValue;
		}
	}

	public long getMinimum() {
		return minimum;
	}

	public long getMaximum() {
		return maximum;
	}

	@Override
	protected void appendParams(StringBuilder str, boolean hasContentsBefore) {
		if (hasContentsBefore) {
			str.append(", ");
		}
		final boolean hasData = (0L != samples);
		if (hasData) {
			str.append("min=").append(minimum);
			str.append(", max=").append(maximum);
		}
		super.appendParams(str, hasData || hasContentsBefore);
	}

}
