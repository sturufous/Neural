package com.stuartmorse.neural.receptor;

import com.stuartmorse.neural.Voltage;

public class DopamineD2Receptor extends MetabotropicReceptor implements CatecholamineReceptor {

	@Override
	public double getVoltageIncrement() {
		// TODO Auto-generated method stub
		return Voltage.DOPAMINE_INCREMENT.getValue();
	}
}