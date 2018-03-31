package com.stuartmorse.neural.neuron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stuartmorse.neural.ChannelInteraction;
import com.stuartmorse.neural.Concentration;
import com.stuartmorse.neural.IonChannelType;
import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.Voltage;
import com.stuartmorse.neural.hormones.Androgen;
import com.stuartmorse.neural.hormones.Testosterone;
import com.stuartmorse.neural.ionchannel.IonChannel;
import com.stuartmorse.neural.receptor.AndrogenReceptor;
import com.stuartmorse.neural.receptor.DopamineD2Receptor;
import com.stuartmorse.neural.receptor.Receptor;
import com.stuartmorse.neural.receptor.TransductionReceptor;
import com.stuartmorse.neural.therapeutics.GabaPentin;
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
	private final Map<IonChannelType, ArrayList<IonChannel>> preSynaptic = new HashMap<>();
	private final Map<Class<? extends Therapeutic>, Double> therapeuticConcentrations = new HashMap<>();
	private final Map<Class<? extends Therapeutic>, Double> preSynapticConcentrations = new HashMap<>();
	private final Map<Class<? extends Therapeutic>, Double> preSynapticTherapies = new HashMap<>();
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
	
	public void addPreSynapticIonChannels(int count, IonChannelType channel, Class<? extends IonChannel> channelType) {
		try {
			ArrayList<IonChannel> ionChannels;
			boolean channelsExistForIonType = false;

			if (preSynaptic.containsKey(channel)) {
				ionChannels = preSynaptic.get(channel);
				channelsExistForIonType = true;
			} else {
				ionChannels = new ArrayList<IonChannel>();
			}

			for (int idx2 = 0; idx2 < count; idx2++) {
				ionChannels.add(channelType.newInstance());
			}

			if (!channelsExistForIonType) {
				preSynaptic.put(channel, ionChannels);
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
	public void transduceSignal(double spikeAmplitude) {

		List<IonChannel> ionChannels = new ArrayList<>();
		
		for (IonChannelType channelType : preSynaptic.keySet()) {
			
			ionChannels = preSynaptic.get(channelType);
			int ionChannelCount = ionChannels.size();

			try {
				processChannelBasedTherapeutics(ionChannels);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		double calciumFlow = calculateCalciumFlow(ionChannels);
		double dischargeLevel = (spikeAmplitude / Voltage.FIRING_THRESHOLD.getValue()) * calciumFlow;

		// The ligand concentration is relative to the number of vesicles and
		// spike amplitude. Attempt to propagate signal over synapse.
		for (LigandType ligand : vesicles.keySet()) {
			Set<SynapticVesicle> ligandVesicles = vesicles.get(ligand);
			ArrayList<Receptor> ligandReceptors = receptors.get(ligand);
			
			try {
				processReceptorBasedTherapeutics(ligandReceptors);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Use matching ligand concentration to distribute bindings across receptor set.
			int ligandReceptorCount = ligandReceptors.size();

			// BindingProbability contains the number of receptors to bind. This will ultimately
			// be affected by the concentration of any ligand-related antagonists.
			double ligandConcentration = ((double) ligandVesicles.size() / 10) * dischargeLevel;
			double numberToBind = ligandReceptorCount * ligandConcentration;
			
			// Limit the number of receptors to bind to the actual number available
			if (numberToBind > ligandReceptorCount) {
				numberToBind = ligandReceptorCount;
			}
			
			// Bind receptors based on concentration of endogenous ligand
			int receptorsBound = 0;
			for (Receptor receptor : ligandReceptors) {
				if (!receptor.isBlocked() && !receptor.isBound() && receptor instanceof TransductionReceptor) {
					receptor.setBound(true);
					receptorsBound++;
				}
				
				if (receptorsBound > numberToBind) {
					break;
				}
			}
		}
	}
	
	private double calculateCalciumFlow(List<IonChannel> ionChannels) {
		
		int count = 0;
		for (IonChannel channel : ionChannels) {
			if (!channel.isInhibited()) {
				count++;
			}
		};
		
		return (double) count / ionChannels.size();
	}
	
	/**
	 * Pre-process Agonists and Antagonists for the post-synaptic receptors. Agonists will bind with
	 * a proportion of receptors based on the concentration of the therapeutic. Likewise, antagonists
	 * will block a proportionate number of receptors.
	 * 
	 * @param ligandReceptors
	 */
	private void processReceptorBasedTherapeutics(ArrayList<Receptor> ligandReceptors) throws Exception {
		
		Map<Class<? extends Therapeutic>, ReceptorInteraction> therapeuticsMatchingThisReceptor = new HashMap<>();

		// Check to see if any therapeutic in the synapse applies to this kind of receptor
		Receptor receptor = ligandReceptors.get(0);
		for (Class <? extends Therapeutic> therapeutic : therapeuticConcentrations.keySet()) {
			Therapeutic theraInstance = therapeutic.newInstance();
			Map<Class, ReceptorInteraction> interactions = theraInstance.getReceptorInteractions();
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
				if (ligandReceptors.get(0) instanceof AndrogenReceptor) {
					expressDopamineD2MRNA();
				}
				break;
			case ANTAGONIST:
				antAgonizeReceptors(ligandReceptors, therapeuticConcentration);
				break;
			default:
				break;
			}
		}
	}

	private void agonizeReceptors(ArrayList<Receptor> ligandReceptors, double therapeuticConcentration) {
		
		int receptorsToBind = (int) (ligandReceptors.size() * therapeuticConcentration);
		for (int idx=0; idx < receptorsToBind; idx++) {
			ligandReceptors.get(idx).setBound(true);
		}
	}

	private void antAgonizeReceptors(ArrayList<Receptor> ligandReceptors, double therapeuticConcentration) {
		
		int receptorsToBlock = (int) (ligandReceptors.size() * therapeuticConcentration);
		for (int idx=0; idx < receptorsToBlock; idx++) {
			ligandReceptors.get(idx).setBlocked(true);
		}
	}
	
	private void expressDopamineD2MRNA() {
		// At this point some of the D2 receptors are bound, so we can ultimately do something 
		// with that information. For now, just bump up the receptor count.
		addReceptors(20, LigandType.DOPAMINE, DopamineD2Receptor.class);
	}
	
	private void processChannelBasedTherapeutics(List<IonChannel> channels) throws Exception {
		
		Map<Class<? extends Therapeutic>, ChannelInteraction> therapeuticsMatchingThisChannel = new HashMap<>();

		// Check to see if any therapeutic in the synapse applies to this kind of receptor
		IonChannel channel = channels.get(0);
		for (Class <? extends Therapeutic> therapeutic : preSynapticConcentrations.keySet()) {
			Therapeutic theraInstance = therapeutic.newInstance();
			Map<Class, ChannelInteraction> interactions = theraInstance.getChannelInteractions();
			ChannelInteraction interact = interactions.get(channel.getClass());
			
			if (interact != null) {
				therapeuticsMatchingThisChannel.put(therapeutic, interact);
			}
		}
		
		// Now that we know which therapeutics match this channel type, distribute the action
		for (Class<? extends Therapeutic> therapeutic : therapeuticsMatchingThisChannel.keySet()) {
			double therapeuticConcentration = preSynapticConcentrations.get(therapeutic);
			ChannelInteraction interaction = therapeuticsMatchingThisChannel.get(therapeutic);
			switch(interaction) {
			case INHIBITOR:
				inhibitChannels(channels, therapeuticConcentration);
				break;
			default:
				break;
			}
		}
	}
	
	private void inhibitChannels(List<IonChannel> channels, double therapeuticConcentration) {
		
		int channelsToInhibit = (int) (channels.size() * therapeuticConcentration);
		for (int idx=0; idx < channelsToInhibit; idx++) {
			channels.get(idx).setInhibited(true);
		}
	}

	/**
	 * @return
	 */
	Neuron getNext() {
		return next;
	}

	public void setPreSynapticConcentration(Class<? extends Therapeutic> therapeutic, double concentration) {
		
		preSynapticConcentrations.put(therapeutic, concentration);
	}
}
