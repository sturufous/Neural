package com.stuartmorse.neural;

public enum Voltage {

	NILL_POTENTIAL(0.0), 
	FIRING_THRESHOLD(0.07), 
	RESTING_POTENTIAL(0.04), 
	GABAA_INCREMENT(-0.01), 
	MUSCARINIC_INCREMENT(0.01), 
	NICOTINIC_INCREMENT(0.01), 
	DOPAMINE_INCREMENT(0.01), 
	GLUTAMATE_INCREMENT(0.01), 
	DEFAULT_SODIUM_POTENTIAL(0.08);

	private double value;

	Voltage(double value) {
		this.value = value;
	};

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
