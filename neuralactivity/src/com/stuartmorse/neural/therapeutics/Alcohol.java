package com.stuartmorse.neural.therapeutics;

import com.stuartmorse.neural.receptor.GABAAReceptor;
import com.stuartmorse.neural.receptor.NMDAReceptor;

public class Alcohol extends Therapeutic implements GabaAgonist, NMDAAntagonist {

	public Alcohol() {
		
		receptorInteractions.put(GABAAReceptor.class, ReceptorInteraction.AGONIST);
		receptorInteractions.put(NMDAReceptor.class, ReceptorInteraction.ANTAGONIST);
	}
}
