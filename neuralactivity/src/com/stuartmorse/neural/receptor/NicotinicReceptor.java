package com.stuartmorse.neural.receptor;

import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.Voltage;

/**
 * @author Stuart Morse
 *
 */
public class NicotinicReceptor extends IonotropicReceptor implements AcetylCholineReceptor {

	private static final LigandType ligand = LigandType.ACETYLCHOLINE;

	/**
	 * @return
	 */
	static LigandType getLigandType() {
		return ligand;
	}

	/**
	 * @return
	 */
	public double getVoltageIncrement() {
		return Voltage.NICOTINIC_INCREMENT.getValue();
	}
}