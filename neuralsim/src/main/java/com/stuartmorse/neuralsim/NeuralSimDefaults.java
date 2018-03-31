package com.stuartmorse.neuralsim;

public class NeuralSimDefaults {

	private Double sodiumPotential;
	private Integer dendriteCount;
	private Integer nodeCount;
	private Integer terminalCount;
	private Integer receptorCount;
	private Integer cngIonChannelCount;
	private Integer vesicleCount;

	/**
	 * @return
	 */
	public double getSodiumPotential() {
		return sodiumPotential;
	}

	/**
	 * @param sodiumPotential
	 */
	public void setSodiumPotential(String sodiumPotential) {
		this.sodiumPotential = new Double(sodiumPotential);
	}

	/**
	 * @return
	 */
	public int getDendriteCount() {
		return dendriteCount;
	}

	/**
	 * @param dendriteCount
	 */
	public void setDendriteCount(String dendriteCount) {
		this.dendriteCount = new Integer(dendriteCount);
	}

	/**
	 * @return
	 */
	public int getNodeCount() {
		return nodeCount;
	}

	/**
	 * @param nodeCount
	 */
	public void setNodeCount(String nodeCount) {
		this.nodeCount = new Integer(nodeCount);
	}

	/**
	 * @return
	 */
	public int getTerminalCount() {
		return terminalCount;
	}

	/**
	 * @param terminalCount
	 */
	public void setTerminalCount(String terminalCount) {
		this.terminalCount = new Integer(terminalCount);
	}

	/**
	 * @return
	 */
	public int getReceptorCount() {
		return receptorCount;
	}

	/**
	 * @param receptorCount
	 */
	public void setReceptorCount(String receptorCount) {
		this.receptorCount = new Integer(receptorCount);
	}

	/**
	 * @return
	 */
	public int getCngIonChannelCount() {
		return cngIonChannelCount;
	}

	/**
	 * @param cngIonChannelCount
	 */
	public void setCngIonChannelCount(String cngIonChannelCount) {
		this.cngIonChannelCount = new Integer(cngIonChannelCount);
	}

	/**
	 * @return
	 */
	public int getVesicleCount() {
		return vesicleCount;
	}

	/**
	 * @param vesicleCount
	 */
	public void setVesicleCount(String vesicleCount) {
		this.vesicleCount = new Integer(vesicleCount);
	}
}
