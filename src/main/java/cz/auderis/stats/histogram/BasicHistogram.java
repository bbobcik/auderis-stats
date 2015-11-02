package cz.auderis.stats.histogram;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BasicHistogram implements Histogram<Object> {

	private final Map<Object, Long> bucketMap;
	private long nullBucket;
	private long samples;

	public BasicHistogram() {
		this.bucketMap = new HashMap<Object, Long>(16);
	}

	@Override
	public Set<Object> getClassificationKeys() {
		return bucketMap.keySet();
	}

	@Override
	public void addSample(Object sample) {
		if (null == sample) {
			++nullBucket;
		} else {
			Long count = bucketMap.get(sample);
			bucketMap.put(sample, 1L + ((null != count) ? count : 0L));
		}
		++samples;
	}

	@Override
	public long getSampleCount(Object sample) {
		if (null == sample) {
			return nullBucket;
		}
		final Long result = bucketMap.get(sample);
		return (null != result) ? result : 0L;
	}

	@Override
	public long getTotalSampleCount() {
		return samples;
	}

}
