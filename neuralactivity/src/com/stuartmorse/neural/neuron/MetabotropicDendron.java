package com.stuartmorse.neural.neuron;

import com.stuartmorse.neural.Concentration;
import com.stuartmorse.neural.LigandType;
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
		double ligandConcentration = synapse.getLigandConcentration(LigandType.ACETYLCHOLINE);
		double totalFlowPotential = Voltage.NILL_POTENTIAL.getValue();

		for (Dendrite dendrite : dendrites) {
			totalFlowPotential += dendrite.getFlowPotential();
		}

		nucleotideConcentration = (totalFlowPotential * ligandConcentration);
		potential = nucleotideConcentration
				* (myNeuron.getExternalSodiumPotential()
						* (cngIonChannelCount * 0.001))
				+ Voltage.RESTING_POTENTIAL.getValue();

		reportOutcome(potential);

		return potential;
	}
}