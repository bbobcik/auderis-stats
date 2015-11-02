package cz.auderis.stats.histogram;

public abstract class AbstractHistogramClassifier<U> implements HistogramClassifier<U, Object> {

	@Override
	public Histogram<Object> createHistogram(Class<U> sampleClass) {
		return new BasicHistogram();
	}

}
