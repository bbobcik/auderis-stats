package cz.auderis.stats.histogram;

public interface HistogramClassifier<T, U> {

	U classify(T sampleValue);

	Histogram<U> createHistogram(Class<T> sampleClass);

}
