package com.stuartmorse.neuralsim;

import java.io.Serializable;

public class StuTest implements Serializable {

	public String msg1;
	public double num1;
	
	public StuTest(String msg1, double num1) {
		this.msg1 = msg1;
		this.num1 = num1;
	}
	
	public StuTest() {
		super();
	}

	public String getMsg1() {
		return msg1;
	}

	public void setMsg1(String msg1) {
		this.msg1 = msg1;
	}

	public double getNum1() {
		return num1;
	}

	public void setNum1(double num1) {
		this.num1 = num1;
	}

	public String toString() {
		return msg1;
	}
}
