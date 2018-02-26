package com.stuartmorse.neural.neuron;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.stuartmorse.neural.Voltage;
import com.stuartmorse.neural.ionchannel.IonChannel;

/**
 * @author Stuart Morse
 *
 */
public abstract class Dendron {
	
	private List<IonChannel> cngIonChannels = null;
	private final int dendriteCount;

	protected final List<Dendrite> dendrites = new LinkedList<>();
	protected Neuron myNeuron;
	protected int cngIonChannelCount = 0;

	/**
	 * @param dendriteCount
	 * @param myNeuron
	 */
	Dendron(int dendriteCount, Neuron myNeuron) {
		
		this.myNeuron = myNeuron;
		this.dendriteCount = dendriteCount;
		
		for(int idx=0; idx < dendriteCount; idx++) {
			dendrites.add(new Dendrite(this));
		}
	}
	
	protected abstract double tryEpsp(Synapse synapse);

	/**
	 * @param synapse
	 */
	void setSynapse(Synapse synapse) {
		this.dendrites.get(0).setSynapse(synapse);
	}

	/**
	 * @param count
	 * @param channelType
	 */
	public void addCNGIonChannels(int count, Class<? extends IonChannel> channelType) {
		cngIonChannels = new ArrayList<IonChannel>();
		for(int idx=0; idx<count; idx++) {
			try {
				cngIonChannels.add(channelType.newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Question: Where are the CNGIs? Dendrite or Dendron? Assuming Dendron.
		cngIonChannelCount = count;
	}
	
	/**
	 * @param value
	 * @param places
	 * @return
	 */
	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	protected void reportOutcome(double potential) {

		StringBuilder message = new StringBuilder("");

		if (potential >= Voltage.FIRING_THRESHOLD.getValue()) {
			message.append("EPSP Successful");
		} else {
			message.append("EPSP Failed");
		}
		message.append(" chain position: " + myNeuron.chainPosition + ", potential: " + round(potential, 3) + " Volts");
		System.out.println(message.toString());
	}
	
	public Synapse getSynapse() {
		return this.dendrites.get(0).getSynapse();
	}

	public String toString() {
		
		int receptorCount = myNeuron.getHeadSynapse().getReceptorCount();
		return new String("Dendrites: " + dendriteCount + " Receptors: " + receptorCount + "\n");
	}

}
