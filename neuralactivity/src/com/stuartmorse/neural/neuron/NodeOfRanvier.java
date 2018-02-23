package com.stuartmorse.neural.neuron;

import java.util.HashSet;
import java.util.Set;

import com.stuartmorse.neural.ionchannel.VGSodiumIonChannel;
import com.stuartmorse.neural.ionchannel.VoltageGatedIonChannel;

/**
 * @author Stuart Morse
 *
 */
public class NodeOfRanvier {

	private static final int CHANNEL_THRESHOLD = 20;

	private final Set<VoltageGatedIonChannel> channels = new HashSet<>();
	private final int channelCount;

	/**
	 * @param channelCount
	 */
	public NodeOfRanvier(int channelCount) {
		
		this.channelCount = channelCount;
		for(int idx=0; idx < channelCount; idx++) {
			channels.add(new VGSodiumIonChannel());
		}
	}
	
	/**
	 * @return
	 */
	boolean propagate() {
		if (channelCount >= CHANNEL_THRESHOLD) {
			return true;
		} else {
			return false;
		}
	}
}
