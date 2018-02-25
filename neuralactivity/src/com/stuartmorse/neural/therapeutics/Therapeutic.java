package com.stuartmorse.neural.therapeutics;

import java.util.HashMap;
import java.util.Map;

import com.stuartmorse.neural.ChannelInteraction;

public abstract class Therapeutic {
	
	public Map<Class, ReceptorInteraction> receptorInteractions = new HashMap<>();
	public Map<Class, ChannelInteraction> channelInteractions = new HashMap<>();
	
	public Map<Class, ReceptorInteraction> getReceptorInteractions() {
		return receptorInteractions;
	}
	
	public Map<Class, ChannelInteraction> getChannelInteractions() {
		return channelInteractions;
	}
}
