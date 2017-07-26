package org.mindroid.common.messages;

/**
 * Valid and implemented Motors
 * 
 * @author Torben
 *
 */ //TODO Rename to MotorType
public enum Motors {
	UnregulatedMotor("Unregulated Motor"),
	MediumRegulatedMotor("Medium Regulated Motor"),
	LargeRegulatedMotor("Large Regulated Motor");

	String name = "";

	Motors(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
