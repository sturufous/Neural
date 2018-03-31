package com.stuartmorse.neural;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.stuartmorse.neural.ionchannel.CAMPIonChannel;
import com.stuartmorse.neural.ionchannel.VGCalciumIonChannel;
import com.stuartmorse.neural.neuron.Neuron;
import com.stuartmorse.neural.neuron.PNSNeuron;
import com.stuartmorse.neural.neuron.Synapse;
import com.stuartmorse.neural.receptor.DopamineD2Receptor;
import com.stuartmorse.neural.therapeutics.GabaPentin;

/**
 * @author Stuart Morse 2018
 *
 */
public class NeuralRunner {
	
	private static final int CHAIN_LENGTH = 10;
	private static List<PNSNeuron> neuronChain = new LinkedList<>();
	private static Neuron prev = null;

	/**
	//synapse.addReceptors(50, LigandType.TESTOSTERONE, AndrogenReceptor.class);
	//synapse.setTherapeuticConcentration(Testosterone.class, 0.5);
	//synapse.addSynapticVesicles(LigandType.TESTOSTERONE, 8);
	 * @param args
	 */
	public static void main(String[] args) {
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		// Create chain of neurons
		for (int idx=0; idx < CHAIN_LENGTH; idx++) {
			PNSNeuron next = new PNSNeuron(1, 8, 1, idx);
			
			next.setExternalSodiumPotential(Voltage.DEFAULT_SODIUM_POTENTIAL.getValue());
			neuronChain.add(next);
			// Skip the head. Create initial synapse for head in neuralBeat().
			if (prev != null) {
				Synapse synapse = new Synapse(prev, next);
				synapse.addReceptors(70, LigandType.DOPAMINE, DopamineD2Receptor.class);
				synapse.addSynapticVesicles(LigandType.DOPAMINE, 10);
				//synapse.setPreSynapticConcentration(GabaPentin.class, 0.35);
				
				synapse.addPreSynapticIonChannels(100, IonChannelType.VGCA_ION_CHANNEL, VGCalciumIonChannel.class);
				next.addCNGIonChannels(960, CAMPIonChannel.class);
				prev.setTailSynapse(synapse);
				next.setHeadSynapse(synapse);
			}
			prev = next;
		}

		neuralBeat();
		
		/*
		 * final Runnable beeper = new Runnable() {
		 * 
		 * public void run() { neuralBeat(); } };
		 * 
		 * final ScheduledFuture<?> beeperHandle = scheduler
		 * .scheduleAtFixedRate(beeper, 1, 5, TimeUnit.SECONDS);
		 * scheduler.schedule(new Runnable() { public void run() {
		 * beeperHandle.cancel(true); } }, 120, TimeUnit.SECONDS);
		 */

	}

	public static void neuralBeat() {

		double internalVoltage;

		PNSNeuron head = neuronChain.get(0);
		head.setExternalSodiumPotential(Voltage.DEFAULT_SODIUM_POTENTIAL.getValue());

		// Create dummy synapse to get the ball rolling
		Synapse initialSynapse = new Synapse(null, head);

		initialSynapse.addSynapticVesicles(LigandType.DOPAMINE, 10);
		initialSynapse.addPreSynapticIonChannels(100, IonChannelType.VGCA_ION_CHANNEL, VGCalciumIonChannel.class);
		initialSynapse.addReceptors(60, LigandType.DOPAMINE, DopamineD2Receptor.class);
		
		head.addCNGIonChannels(960, CAMPIonChannel.class);
		//initialSynapse.setPreSynapticConcentration(GabaPentin.class, 0.40);

		initialSynapse.transduceSignal(Voltage.FIRING_THRESHOLD.getValue());
		head.setHeadSynapse(initialSynapse);
		internalVoltage = head.epspController(initialSynapse);
	}
}
