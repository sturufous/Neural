package com.stuartmorse.neural.neuron;

import com.stuartmorse.neural.Voltage;

public abstract class Neuron {
	
	double externalSodiumPotential = 0;
	final int chainPosition;
	final Axon axon;
	Dendron dendron;

	/**
	 * @param dendrites
	 * @param nodes
	 * @param terminals
	 */
	Neuron(int dendrites, int nodes, int terminals, int chainPosition) {

		this.axon = new Axon(nodes, terminals);
		this.chainPosition = chainPosition;
	}

	/**
	 * @param synapse
	 * @return
	 */
	public double epspController(Synapse synapse) {
		
		boolean successful = false;

		// We want the dendron to base its epsp attempt on sodium potential,
		// receptor density and ligand concentration
		double potential = this.dendron.tryEpsp(synapse);
		if (potential >= Voltage.FIRING_THRESHOLD.getValue()) {
			successful = axon.fireEpsp(potential);
		}
		else
		{
			successful = false;
		}
		
		return potential;
	}
	
	/**
	 * @return
	 */
	double getExternalSodiumPotential() {
		return externalSodiumPotential;
	}
	
	/**
	 * @return
	 */
	Dendron getDendron() {
		return dendron;
	}

	/**
	 * @param level
	 */
	public void setExternalSodiumPotential(double level) {
		this.externalSodiumPotential = level;
	}
	
	public String toString() {
		
		return "Dendron: " + this.dendron.toString() + "Axon: "
				+ this.axon.toString() + ", Chain Position: " + chainPosition;
	}
	
	/**
	 * @param synapse
	 */
	public void setHeadSynapse(Synapse synapse) {
		this.dendron.setSynapse(synapse);
	}

	/**
	 * @param synapse
	 */
	public void setTailSynapse(Synapse synapse) {
		this.axon.setSynapse(synapse);
	}
	
	public Synapse getHeadSynapse() {
		return this.dendron.getSynapse();
	}
}
