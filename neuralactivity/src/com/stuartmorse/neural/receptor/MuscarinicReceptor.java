package com.stuartmorse.neural.receptor;

import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.Voltage;

/**
 * @author Stuart Morse
 *
 */
public class MuscarinicReceptor extends MetabotropicReceptor implements AcetylCholineReceptor {

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
		return Voltage.MUSCARINIC_INCREMENT.getValue();
	}
}
