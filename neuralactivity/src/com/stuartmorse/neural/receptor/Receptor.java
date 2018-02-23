package com.stuartmorse.neural.receptor;

/**
 * @author Stuart Morse
 *
 */
public abstract class Receptor {

	private boolean bound = false;

	/**
	 * @return
	 */
	public abstract double getVoltageIncrement();

	/**
	 * @return
	 */
	public boolean isBound() {
		return bound;
	};

	/**
	 * @param bound
	 */
	public void setBound(boolean bound) {
		this.bound = bound;
	}

}
