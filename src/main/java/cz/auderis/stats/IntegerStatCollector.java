package cz.auderis.stats;

public class IntegerStatCollector extends AbstractNumericStatCollector {
	private static final long serialVersionUID = 20151101L;

	protected int minimum;
	protected int maximum;

	public IntegerStatCollector() {
		this(null, false);
	}

	public IntegerStatCollector(boolean advancedStatsEnabled) {
		this(null, advancedStatsEnabled);
	}

	public IntegerStatCollector(String id, boolean advancedStatsEnabled) {
		super(id, advancedStatsEnabled);
		minimum = Integer.MAX_VALUE;
		maximum = Integer.MIN_VALUE;
	}

	public void addSample(int sampleValue) {
		super.addSample(sampleValue);
		if (sampleValue < minimum) {
			minimum = sampleValue;
		}
		if (sampleValue > maximum) {
			maximum = sampleValue;
		}
	}

	public int getMinimum() {
		return minimum;
	}

	public int getMaximum() {
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
