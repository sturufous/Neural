package com.stuartmorse.neural;

public enum Concentration {

	NILL_CONCENTRATION(0.0);

	private double value;

	Concentration(double value) {
		this.value = value;
	};

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
