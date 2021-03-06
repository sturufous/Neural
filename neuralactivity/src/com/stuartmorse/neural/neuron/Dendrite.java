package com.stuartmorse.neural.neuron;

import java.util.ArrayList;
import java.util.Map;

import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.Voltage;
import com.stuartmorse.neural.receptor.Receptor;

/**
 * @author Stuart Morse
 *
 */
public class Dendrite {

	private Synapse synapse;
	public Dendrite(Dendron myDendron) {

	}

	/**
	 * Receptors are stored in a map that is keyed by ligand type. An ArrayList of receptors is 
	 * associated with each ligand type. This method calculates the total flow potential from a
	 * synapse into the dendrite based on whether the receptor is bound (activated) and its voltage
	 * contribution. Receptors that stimulate (e.g. NMDA and Nicotinic) add to the voltage within
	 * the dendron. Receptors that inhibit (e.g. GABA) subtract from the voltage.
	 * 
	 * @return
	 */
	double getFlowPotential() {

		double flowPotential = Voltage.NILL_POTENTIAL.getValue();
		Map<LigandType, ArrayList<Receptor>> receptors = synapse.getReceptors();

		for (LigandType ligand : synapse.getReceptors().keySet()) {
			for (Receptor receptor : receptors.get(ligand)) {
				if (receptor.isBound())
					flowPotential += receptor.getVoltageIncrement();
			}
		}

		return flowPotential;

	}

	/**
	 * @return
	 */
	public Synapse getSynapse() {
		return synapse;
	}

	/**
	 * @param synapse
	 */
	public void setSynapse(Synapse synapse) {
		this.synapse = synapse;
	}
}
