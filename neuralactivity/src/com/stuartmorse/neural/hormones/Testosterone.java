package com.stuartmorse.neural.hormones;

import com.stuartmorse.neural.receptor.AndrogenReceptor;
import com.stuartmorse.neural.therapeutics.ReceptorInteraction;
import com.stuartmorse.neural.therapeutics.Therapeutic;

public class Testosterone extends Therapeutic implements Androgen {

	public Testosterone() {
		
		receptorInteractions.put(AndrogenReceptor.class, ReceptorInteraction.AGONIST);
	}
}
