package com.stuartmorse.neural.therapeutics;

import com.stuartmorse.neural.receptor.GABAAReceptor;
import com.stuartmorse.neural.receptor.NMDAReceptor;

public class Alcohol extends Therapeutic implements GabaAgonist, NMDAAntagonist {

	public Alcohol() {
		
		interactions.put(GABAAReceptor.class, ReceptorInteraction.AGONIST);
		interactions.put(NMDAReceptor.class, ReceptorInteraction.ANTAGONIST);
	}
}
