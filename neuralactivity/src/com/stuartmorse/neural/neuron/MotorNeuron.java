package com.stuartmorse.neural.neuron;

/**
 * @author Stuart Morse
 *
 */
public class MotorNeuron extends Neuron {
	
	/**
	 * @param dendrites
	 * @param nodes
	 * @param terminals
	 */
	public MotorNeuron(int dendrites, int nodes, int terminals, int chainPosition) {
		
		super(dendrites, nodes, terminals, chainPosition);
		this.dendron = new IonotropicDendron(dendrites, this);
	}
}
