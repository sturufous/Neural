package com.stuartmorse.neural.therapeutics;

import com.stuartmorse.neural.ChannelInteraction;
import com.stuartmorse.neural.ionchannel.VGCalciumIonChannel;
import com.stuartmorse.neural.receptor.GABAAReceptor;

public class GabaPentin extends Therapeutic implements VGCalciumIonChannelInhibitor {
	
	public GabaPentin() {
		
		channelInteractions.put(VGCalciumIonChannel.class, ChannelInteraction.INHIBITOR);
	}

}
