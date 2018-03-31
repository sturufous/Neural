package com.stuartmorse.neural.neuron;

import com.stuartmorse.neural.Concentration;
import com.stuartmorse.neural.Voltage;

/**
 * @author Stuart Morse
 *
 */
public class MetabotropicDendron extends Dendron {

	/**
	 * @param dendriteCount
	 * @param myNeuron
	 */
	MetabotropicDendron(int dendriteCount, Neuron myNeuron) {
		super(dendriteCount, myNeuron);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.stuartmorse.neuron.Dendron#tryEpsp(com.stuartmorse.neuron.Synapse)
	 */
	protected double tryEpsp(Synapse synapse) {

		double potential = Voltage.NILL_POTENTIAL.getValue();
		double nucleotideConcentration = Concentration.NILL_CONCENTRATION.getValue();
		
		// TODO Modify for multiple ligand types.
		double totalFlowPotential = Voltage.NILL_POTENTIAL.getValue();

		// Returns the voltage resulting from activation of all bound receptors
		for (Dendrite dendrite : dendrites) {
			totalFlowPotential += dendrite.getFlowPotential();
		}

		// Nucleotide concentration is a function of totalFlowPotential so no need to include
		// it when calculating potential here. Only need to consider how many GNG Ion Channels
		// there are to enable Sodium Ion flow.
		
		double sodiumPotential = myNeuron.getExternalSodiumPotential();
		potential = totalFlowPotential
				* (sodiumPotential
						* (cngIonChannelCount * 0.001))
				+ Voltage.RESTING_POTENTIAL.getValue();

		reportOutcome(potential);

		return potential;
	}
}