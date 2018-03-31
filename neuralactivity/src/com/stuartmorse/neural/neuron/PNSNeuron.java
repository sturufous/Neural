package com.stuartmorse.neural.neuron;

import com.stuartmorse.neural.ionchannel.IonChannel;

/**
 * @author Stuart Morse
 *
 */
public final class PNSNeuron extends Neuron {

	/**
	 * @param dendrites
	 * @param nodes
	 * @param terminals
	 */
	public PNSNeuron(int dendrites, int nodes, int terminals, int chainPosition) {
		
		super(dendrites, nodes, terminals, chainPosition);
		this.dendron = new MetabotropicDendron(dendrites, this);
	}

	/**
	 * @param level
	 */
	public void setExternalSodiumPotential(double level) {
		this.externalSodiumPotential = level;
	}
	
	/**
	 * @param count
	 * @param channelType
	 */
	public void addCNGIonChannels(int count, Class<? extends IonChannel> channelType) {
		this.dendron.addCNGIonChannels(count, channelType);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stuartmorse.neuron.Neuron#getExternalSodiumPotential()
	 */
	double getExternalSodiumPotential() {
		return this.externalSodiumPotential;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stuartmorse.neural.neuron.Neuron#getDendron()
	 */
	Dendron getDendron() {
		return dendron;
	}

	/**
	 * @param synapse
	 */
	public void setHeadSynapse(Synapse synapse) {
		this.dendron.setSynapse(synapse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stuartmorse.neuron.Neuron#setTailSynapse(com.stuartmorse.neuron.
	 * Synapse)
	 */
	public void setTailSynapse(Synapse synapse) {
		this.axon.setSynapse(synapse);
	}
}

