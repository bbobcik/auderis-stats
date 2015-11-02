package cz.auderis.stats.histogram;

public class EnumHistogramClassifier<T extends Enum<T>> implements HistogramClassifier<T, T> {

	@Override
	public T classify(T sampleValue) {
		return sampleValue;
	}

	@Override
	public Histogram<T> createHistogram(Class<T> sampleClass) {
		if (null == sampleClass) {
			throw new NullPointerException();
		} else if (!sampleClass.isEnum()) {
			throw new IllegalArgumentException("not an enum class");
		}
		return new EnumHistogram<T>(sampleClass);
	}

}
