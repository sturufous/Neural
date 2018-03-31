package com.stuartmorse.neuralsim.minibeans;

public class IonPotential {
	
	String ionType;
	double potential = 0.0;
	
	public double getPotential() {
		return potential;
	}
	
	public void setPotential(double potential) {
		this.potential = potential;
	}
	
	public String getIonType() {
		return ionType;
	}
	
	public void setIonType(String ionType) {
		this.ionType = ionType;
	}

}
