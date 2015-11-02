package cz.auderis.stats;

import java.io.Serializable;

public abstract class AbstractStatistic implements Serializable {
	private static final long serialVersionUID = 20151101L;

	private String id;

	protected AbstractStatistic() {
		super();
	}

	protected AbstractStatistic(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	protected void setId(String id) {
		this.id = id;
	}

	protected abstract void appendParams(StringBuilder str, boolean hasContentsBefore);

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder(128);
		if ((null == id) || id.isEmpty()) {
			str.append("Stat");
		} else {
			str.append(id);
		}
		str.append('{');
		appendParams(str, false);
		str.append('}');
		return str.toString();
	}

}
