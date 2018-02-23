package com.stuartmorse.neural.neuron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.stuartmorse.neural.Concentration;
import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.Voltage;
import com.stuartmorse.neural.receptor.Receptor;

/**
 * @author Stuart Morse
 *
 */
public class Synapse {

	private final HashMap<LigandType, Double> ligandConcentrations = new HashMap<>();
	private final Map<LigandType, Set<SynapticVesicle>> vesicles = new HashMap<>();
	private final Map<LigandType, ArrayList<Receptor>> receptors = new HashMap<>();
	private final Neuron prev;
	private final Neuron next;

	/**
	 * @param prev
	 * @param next
	 * @param chainPosition
	 */
	public Synapse(Neuron prev, Neuron next) {
		this.prev = prev;
		this.next = next;
	}

	/**
	 * @param ligand
	 * @return
	 */
	public double getLigandConcentration(LigandType ligand) {
		
		if(ligandConcentrations.containsKey(ligand)) {
			return ligandConcentrations.get(ligand);
		}
		else {
			return Concentration.NILL_CONCENTRATION.getValue();
		}
	}

	/**
	 * @param ligand
	 * @param concentration
	 */
	public void setLigandConcentration(LigandType ligand, double concentration) {
		ligandConcentrations.put(ligand, new Double(concentration));
	}

	/**
	 * @param ligand
	 * @param count
	 */
	public void addSynapticVesicles(LigandType ligand, int count) {
		
		Set<SynapticVesicle> ligandVesicles;
		boolean vesiclesExistForLigand = false;

		if (vesicles.containsKey(ligand)) {
			ligandVesicles = vesicles.get(ligand);
			vesiclesExistForLigand = true;
		} else {
			ligandVesicles = new HashSet<SynapticVesicle>();
		}
			
		for (int idx = 0; idx < count; idx++) {
			ligandVesicles.add(new SynapticVesicle(ligand));
		}

		if (!vesiclesExistForLigand) {
			vesicles.put(ligand, ligandVesicles);
		}
	}

	/**
	 * @return
	 */
	int getReceptorCount() {

		return receptors.size();
	}

	/**
	 * @return
	 */
	Map<LigandType, ArrayList<Receptor>> getReceptors() {

		return receptors;
	}

	/**
	 * @param count
	 * @param receptorType
	 */
	public void addReceptors(int count, LigandType ligand, Class<? extends Receptor> receptorType) {
		try {
			ArrayList<Receptor> ligandReceptors;
			boolean receptorsExistForLigand = false;

			if (receptors.containsKey(ligand)) {
				ligandReceptors = receptors.get(ligand);
				receptorsExistForLigand = true;
			} else {
				ligandReceptors = new ArrayList<Receptor>();
			}

			for (int idx2 = 0; idx2 < count; idx2++) {
				ligandReceptors.add(receptorType.newInstance());
			}

			if (!receptorsExistForLigand) {
				receptors.put(ligand, ligandReceptors);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param spikeAmplitude
	 */
	public void fuseVesicles(double spikeAmplitude) {

		double dischargeLevel = 0.07 / Voltage.FIRING_THRESHOLD.getValue();

		// The ligand concentration is relative to the number of vesicles and
		// spike amplitude. Attempt to propagate signal over synapse.
		for (LigandType ligand : vesicles.keySet()) {
			Set<SynapticVesicle> ligandVesicles = vesicles.get(ligand);
			setLigandConcentration(ligand, ((double) ligandVesicles.size() / 10) * dischargeLevel);

			// Use matching ligand concentration to distribute
			// bindings across receptor set.
			ArrayList<Receptor> ligandReceptors = receptors.get(ligand);
			int ligandReceptorCount = ligandReceptors.size();
			double ligandConcentration = getLigandConcentration(ligand);

			// BindingProbability contains the number of receptors to bind
			double numberToBind = ligandReceptorCount * ligandConcentration;

			// Limit the number of receptors to bind to the actual number
			// available
			if (numberToBind > ligandReceptorCount) {
				numberToBind = ligandReceptorCount;
			}

			for (int idx = 0; idx < (int) numberToBind; idx++) {
				ligandReceptors.get(idx).setBound(true);
			}
		}
	}

	/**
	 * @return
	 */
	Neuron getNext() {
		return next;
	}
}
