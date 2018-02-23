package com.stuartmorse.neural.receptor;

import com.stuartmorse.neural.LigandType;
import com.stuartmorse.neural.receptor.MetabotropicReceptor;
import com.stuartmorse.neural.Voltage;

public class AndrogenReceptor extends MetabotropicReceptor {

	private static final LigandType ligand = LigandType.TESTOSTERONE;

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
		return Voltage.NILL_POTENTIAL.getValue();
	}

}
