package com.stuartmorse.neural.therapeutics;

import com.stuartmorse.neural.receptor.GABAAReceptor;
import com.stuartmorse.neural.receptor.NMDAReceptor;

public class NMethylDAspartate extends Therapeutic implements NMDAAgonist {
	
	public NMethylDAspartate() {
		
		receptorInteractions.put(NMDAReceptor.class, ReceptorInteraction.AGONIST);
	}
}
