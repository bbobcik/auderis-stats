package cz.auderis.stats;

public class BooleanStatCollector extends AbstractNumericStatCollector {
	private static final long serialVersionUID = 20151101L;

	protected boolean recentValue;
	protected long trueCount;
	protected long currentStreakLength;
	protected final LongStatCollector trueStreakStat;
	protected final LongStatCollector falseStreakStat;

	public BooleanStatCollector() {
		this(null);
	}

	public BooleanStatCollector(String id) {
		super(id, false);
		final LongStatCollector trueStreak;
		final LongStatCollector falseStreak;
		if (null == id) {
			trueStreak = new LongStatCollector(null, false);
			falseStreak = new LongStatCollector(null, false);
		} else {
			final String prefix = id + '.';
			trueStreak = new LongStatCollector(prefix + "trueStreak", false);
			falseStreak = new LongStatCollector(prefix + "falseStreak", false);
		}
		this.trueStreakStat = trueStreak;
		this.falseStreakStat = falseStreak;
	}

	public void addSample(boolean sampleValue) {
		super.addSample(sampleValue ? 1.0 : 0.0);
		if (sampleValue) {
			++trueCount;
		}
		if (sampleValue == recentValue) {
			++currentStreakLength;
		} else {
			if (0L != currentStreakLength) {
				final LongStatCollector streakStat = recentValue ? trueStreakStat : falseStreakStat;
				streakStat.addSample(currentStreakLength);
			}
			recentValue = sampleValue;
			currentStreakLength = 1L;
		}
	}

	@Override
	public void shutdown() {
		super.shutdown();
		final LongStatCollector streakStat = recentValue ? trueStreakStat : falseStreakStat;
		streakStat.addSample(currentStreakLength);
		currentStreakLength = 0L;
	}

	@Override
	protected void setId(String newId) {
		if (null == newId) {
			trueStreakStat.setId(null);
			falseStreakStat.setId(null);
		} else {
			final String prefix = newId + '.';
			trueStreakStat.setId(prefix + "trueStreak");
			falseStreakStat.setId(prefix + "falseStreak");
		}
	}

	public long getCountOfTrue() {
		return trueCount;
	}

	public LongStatCollector getStreakStatistics(boolean sampleValue) {
		return sampleValue ? trueStreakStat : falseStreakStat;
	}

	@Override
	protected void appendParams(StringBuilder str, boolean hasContentsBefore) {
		if (hasContentsBefore) {
			str.append(", ");
		}
		final boolean hasData = (0L != samples);
		if (hasData) {
			str.append("n(T)=").append(trueCount);
			str.append(", n(F)=").append(samples - trueCount);
			str.append(", n(T)/n=").append(trueCount / samples);
		}
		super.appendParams(str, hasData || hasContentsBefore);
		if (hasData) {
			str.append(", maxLen(T)=").append(trueStreakStat.getMaximum());
			str.append(", maxLen(F)=").append(falseStreakStat.getMaximum());
			str.append(", avgLen(T)=").append(trueStreakStat.getMean());
			str.append(", avgLen(F)=").append(falseStreakStat.getMean());
		}
	}

}
