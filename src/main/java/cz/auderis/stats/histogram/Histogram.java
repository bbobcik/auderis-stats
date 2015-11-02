package cz.auderis.stats.histogram;

import java.util.Set;

public interface Histogram<U> {

	Set<U> getClassificationKeys();

	void addSample(U sample);

	long getSampleCount(U sample);

	long getTotalSampleCount();

}
