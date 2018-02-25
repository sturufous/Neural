package com.stuartmorse.neural.neuron;

import java.util.LinkedList;
import java.util.List;

public class Axon {
	
	private final int nodeCount;
	private final int terminalCount;
	private final List<NodeOfRanvier> nodes = new LinkedList<>();
	private final List<AxonTerminal> terminals = new LinkedList<>();
	
	/**
	 * @param nodeCount
	 * @param terminalCount
	 */
	public Axon(int nodeCount, int terminalCount) {
		
		this.nodeCount = nodeCount;
		this.terminalCount = terminalCount;
		
		for(int idx=0; idx < nodeCount; idx++) {
			nodes.add(new NodeOfRanvier(20));
		}

		for(int idx=0; idx < terminalCount; idx++) {
			terminals.add(new AxonTerminal());
		}
	}
	
	/**
	 * @param spikeAmplitude
	 * @return
	 */
	boolean fireEpsp(double spikeAmplitude) {
		
		int nodesPropagated = 0;
		
		// Propagate the epsp from first to last node
		for (NodeOfRanvier node : nodes) {
			if (node.propagate() == true) {
				nodesPropagated++;
			}
			else {
				break;
			}
 		}
		
		// Currently only one terminal for development
		if (nodesPropagated == this.nodeCount) {
			for(AxonTerminal terminal : terminals) {
				Synapse tailSynapse = terminal.getSynapse();
				// Null here indicates end of neuron chain
				if (tailSynapse != null) {
					tailSynapse.transduceSignal(spikeAmplitude);
					Neuron myNeuron = tailSynapse.getNext();
					if (myNeuron != null) {
						myNeuron.epspController(tailSynapse);
					}
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * @param synapse
	 */
	public void setSynapse(Synapse synapse) {
		terminals.get(0).setSynapse(synapse);
	}

	public String toString() {
		
		return new String(
				"Nodes: " + nodeCount + " Terminals: " + terminalCount);
	}

}
