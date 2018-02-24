package com.stuartmorse.neural.therapeutics;

import com.stuartmorse.neural.receptor.GABAAReceptor;

public class GabaPentin extends Therapeutic implements VGCalciumIonChannelInhibitor {
	
	public GabaPentin() {
		
		interactions.put(GABAAReceptor.class, ReceptorInteraction.AGONIST);
	}

}
