package com.stuartmorse.neural.receptor;

import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.Voltage;

/**
 * @author Stuart Morse
 *
 */
public class GABAAReceptor extends IonotropicReceptor implements GABAReceptor {

	private static final LigandType ligand = LigandType.GABA;

	/**
	 * @return
	 */
	static LigandType getLigandType() {
		return ligand;
	}

	/**
	 * @return
	 */
	@Override
	public double getVoltageIncrement() {
		return Voltage.GABAA_INCREMENT.getValue();
	}
}
