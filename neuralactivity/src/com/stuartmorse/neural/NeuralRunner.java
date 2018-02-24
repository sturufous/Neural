package com.stuartmorse.neural;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.stuartmorse.neural.ionchannel.CAMPIonChannel;
import com.stuartmorse.neural.neuron.Neuron;
import com.stuartmorse.neural.neuron.PNSNeuron;
import com.stuartmorse.neural.neuron.Synapse;
import com.stuartmorse.neural.receptor.NMDAReceptor;
import com.stuartmorse.neural.therapeutics.Alcohol;
import com.stuartmorse.neural.therapeutics.GabaPentin;

/**
 * @author Stuart Morse 2018
 *
 */
public class NeuralRunner {
	
	private static final int CHAIN_LENGTH = 7;
	private static List<PNSNeuron> neuronChain = new LinkedList<>();
	private static Neuron prev = null;

	/**
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
				synapse.addReceptors(60, LigandType.GLUTAMATE, NMDAReceptor.class);
				//synapse.addReceptors(9, LigandType.GABA, GABAAReceptor.class);
				synapse.setTherapeuticConcentration(Alcohol.class, 0.16);
				synapse.addSynapticVesicles(LigandType.GLUTAMATE, 8);
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

		initialSynapse.setTherapeuticConcentration(Alcohol.class, 0.1);
		initialSynapse.setTherapeuticConcentration(GabaPentin.class, 0.25);
		initialSynapse.addSynapticVesicles(LigandType.GLUTAMATE, 8);
		// initialSynapse.addSynapticVesicles(LigandType.GABA, 13);

		initialSynapse.addReceptors(60, LigandType.GLUTAMATE, NMDAReceptor.class);
		head.addCNGIonChannels(960, CAMPIonChannel.class);
		//initialSynapse.addReceptors(20, LigandType.GABA, GABAAReceptor.class);

		initialSynapse.fuseVesicles(Voltage.FIRING_THRESHOLD.getValue());
		head.setHeadSynapse(initialSynapse);
		internalVoltage = head.epspController(initialSynapse);
	}
}
