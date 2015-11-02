package cz.auderis.stats.histogram;

import java.util.EnumSet;
import java.util.Set;

public class EnumHistogram<U extends Enum<U>> implements Histogram<U> {

	private final Class<U> enumClass;
	private final long[] buckets;
	private long samples;
	private long nullBucket;

	public EnumHistogram(Class<U> enumClass) {
		if (null == enumClass) {
			throw new NullPointerException();
		}
		assert enumClass.isEnum();
		this.enumClass = enumClass;
		final U[] enumConstants = enumClass.getEnumConstants();
		assert (null != enumConstants) && (enumConstants.length > 0);
		this.buckets = new long[enumConstants.length];
	}

	@Override
	public Set<U> getClassificationKeys() {
		return EnumSet.allOf(enumClass);
	}

	@Override
	public void addSample(U sample) {
		if (null != sample) {
			final int index = sample.ordinal();
			++buckets[index];
		} else {
			++nullBucket;
		}
		++samples;
	}

	@Override
	public long getSampleCount(U sample) {
		final long result;
		if (null != sample) {
			final int index = sample.ordinal();
			result = buckets[index];
		} else {
			result = nullBucket;
		}
		return result;
	}

	@Override
	public long getTotalSampleCount() {
		return samples;
	}

}
