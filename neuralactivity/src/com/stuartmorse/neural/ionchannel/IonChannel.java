package com.stuartmorse.neural.ionchannel;

/**
 * @author Stuart Morse
 *
 */
public abstract class IonChannel {

	private boolean inhibited = false;
	
	/**
	 * @return
	 */
	public boolean isInhibited() {
		return inhibited;
	};

	/**
	 * @param bound
	 */
	public void setInhibited(boolean inhibited) {
		this.inhibited = inhibited;
	}
}
