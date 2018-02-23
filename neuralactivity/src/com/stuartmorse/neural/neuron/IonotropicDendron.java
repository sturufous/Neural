package com.stuartmorse.neural.neuron;

import com.stuartmorse.neural.Concentration;
import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.Voltage;

/**
 * @author Stuart Morse
 *
 */
public class IonotropicDendron extends Dendron {

	/**
	 * @param dendriteCount
	 * @param myNeuron
	 */
	IonotropicDendron(int dendriteCount, Neuron myNeuron) {
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
		double ligandConcentration = Concentration.NILL_CONCENTRATION.getValue();
		double totalFlowPotential = Voltage.NILL_POTENTIAL.getValue();
		
		for(Dendrite dendrite : dendrites) {
			totalFlowPotential += dendrite.getFlowPotential();
		}

		ligandConcentration = synapse.getLigandConcentration(LigandType.ACETYLCHOLINE);
		potential = ((totalFlowPotential * ligandConcentration) * myNeuron.getExternalSodiumPotential())
				+ Voltage.RESTING_POTENTIAL.getValue();

		reportOutcome(potential);

		return potential;
	}
}
