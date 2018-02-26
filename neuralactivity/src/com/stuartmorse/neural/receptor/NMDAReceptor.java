package com.stuartmorse.neural.receptor;

import com.stuartmorse.neural.Voltage;

public class NMDAReceptor extends IonotropicReceptor implements GlutamateReceptor, TransductionReceptor {

	@Override
	public double getVoltageIncrement() {
		// TODO Auto-generated method stub
		return Voltage.GLUTAMATE_INCREMENT.getValue();
	}

}
