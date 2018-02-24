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
import com.stuartmorse.neural.therapeutics.ReceptorInteraction;
import com.stuartmorse.neural.therapeutics.Therapeutic;

/**
 * @author Stuart Morse
 *
 */
public class Synapse {

	private final HashMap<LigandType, Double> ligandConcentrations = new HashMap<>();
	private final Map<LigandType, Set<SynapticVesicle>> vesicles = new HashMap<>();
	private final Map<LigandType, ArrayList<Receptor>> receptors = new HashMap<>();
	private final Map<Class<? extends Therapeutic>, Double> therapeuticConcentrations = new HashMap<>();
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
	 * @return
	 */
	public double getTherapeuticConcentration(Class <? extends Therapeutic> therapeutic) {
		
		if(ligandConcentrations.containsKey(therapeutic)) {
			return ligandConcentrations.get(therapeutic);
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
	 * @param concentration In milligrams per kilogram
	 */
	public void setTherapeuticConcentration(Class<? extends Therapeutic> therapeutic, double concentration) {
		therapeuticConcentrations.put(therapeutic, new Double(concentration));
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

		double dischargeLevel = spikeAmplitude / Voltage.FIRING_THRESHOLD.getValue();

		// The ligand concentration is relative to the number of vesicles and
		// spike amplitude. Attempt to propagate signal over synapse.
		for (LigandType ligand : vesicles.keySet()) {
			Set<SynapticVesicle> ligandVesicles = vesicles.get(ligand);
			setLigandConcentration(ligand, ((double) ligandVesicles.size() / 10) * dischargeLevel);

			// Use matching ligand concentration to distribute bindings across receptor set.
			ArrayList<Receptor> ligandReceptors = receptors.get(ligand);
			int ligandReceptorCount = ligandReceptors.size();
			double ligandConcentration = getLigandConcentration(ligand);

			// BindingProbability contains the number of receptors to bind. This will ultimately
			// be affected by the concentration of any ligand-related antagonists.
			double numberToBind = ligandReceptorCount * ligandConcentration;
			
			try {
				processTherapeutics(ligandReceptors, ligandReceptorCount);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Limit the number of receptors to bind to the actual number available
			if (numberToBind > ligandReceptorCount) {
				numberToBind = ligandReceptorCount;
			}

			// Bind receptors based on concentration of endogenous ligand
			for (int idx=0; idx < (int) numberToBind; idx++) {
				Receptor receptor = ligandReceptors.get(idx);
				if (!receptor.isBlocked()) {
					receptor.setBound(true);
				} else {
					receptor.setBound(false);
				}
			}
		}
	}
	
	/**
	 * Pre-process Agonists and Antagonists for the post-synaptic receptors. Agonists will bind with
	 * a proportion of receptors based on the concentration of the therapeutic. Likewise, antagonists
	 * will block a proportionate number of receptors.
	 * 
	 * @param ligandReceptors
	 */
	private void processTherapeutics(ArrayList<Receptor> ligandReceptors, int ligandReceptorCount) throws Exception {
		
		Map<Class<? extends Therapeutic>, ReceptorInteraction> therapeuticsMatchingThisReceptor = new HashMap<>();

		// Check to see if any therapeutic in the synapse applies to this kind of receptor
		Receptor receptor = ligandReceptors.get(0);
		for (Class <? extends Therapeutic> therapeutic : therapeuticConcentrations.keySet()) {
			Therapeutic theraInstance = therapeutic.newInstance();
			Map<Class, ReceptorInteraction> interactions = theraInstance.getInteractions();
			ReceptorInteraction interact = interactions.get(receptor.getClass());
			
			if (interact != null) {
				therapeuticsMatchingThisReceptor.put(therapeutic, interact);
			}
		}
		
		// Now that we know which therapeutics match this receptor type, distribute the action
		for (Class<? extends Therapeutic> therapeutic : therapeuticsMatchingThisReceptor.keySet()) {
			double therapeuticConcentration = therapeuticConcentrations.get(therapeutic);
			ReceptorInteraction interaction = therapeuticsMatchingThisReceptor.get(therapeutic);
			switch(interaction) {
			case AGONIST:
				agonizeReceptors(ligandReceptors, therapeuticConcentration);
				break;
			case ANTAGONIST:
				antAgonizeReceptors(ligandReceptors, therapeuticConcentration);
				break;
			}
		}
	}

	private void agonizeReceptors(ArrayList<Receptor> ligandReceptors, double therapeuticConcentration) {
		
		
	}

	private void antAgonizeReceptors(ArrayList<Receptor> ligandReceptors, double therapeuticConcentration) {
		
		int receptorsToBlock = (int) (ligandReceptors.size() * therapeuticConcentration);
		for (int idx=0; idx < receptorsToBlock; idx++) {
			ligandReceptors.get(idx).setBlocked(true);
		}
	}
	
	/**
	 * @return
	 */
	Neuron getNext() {
		return next;
	}
}
