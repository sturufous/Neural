package com.stuartmorse.neural.neuron;

/**
 * @author Stuart Morse
 *
 */
public class AxonTerminal {
	
	private Synapse synapse;
	
	public AxonTerminal() {
		super();
	}
	
	Synapse getSynapse() {
		return this.synapse;
	}

	void setSynapse(Synapse synapse) {
		this.synapse = synapse;
	}
}
