package com.stuartmorse.neural.therapeutics;

import java.util.HashMap;
import java.util.Map;

public abstract class Therapeutic {
	
	public Map<Class, ReceptorInteraction> interactions = new HashMap<Class, ReceptorInteraction>();
	
	public Map<Class, ReceptorInteraction> getInteractions() {
		return interactions;
	}
	
}
