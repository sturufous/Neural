package com.stuartmorse.neural.receptor;

import com.stuartmorse.neural.exceptions.ReceptorStateException;

/**
 * @author Stuart Morse
 *
 */
public abstract class Receptor {

	private boolean bound = false;
	private boolean blocked = false;

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

	/**
	 * @return
	 */
	public boolean isBlocked() {
		return blocked;
	};

	/**
	 * @param bound
	 */
	public void setBlocked(boolean bound) {
		this.blocked = bound;
	}

}
