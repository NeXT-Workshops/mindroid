package org.mindroid.impl.statemachine;

import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.ITimeEvent;

public class TimeEvent implements ITimeEvent{
	
	float delay = -1;
	
	IState owner = null;
	
	/**
	 * Delay (sec): Time after the Event gets fired.
	 * 
	 * @param delay - in milliseconds
	 */
	public TimeEvent(float delay, IState owner){
		this.delay = delay;
		this.owner = owner;
	}


	@Override
	public float getDelay() {
		return delay;
	}

	@Override
	public IState getOwner(){
		return owner;
	}

	@Override
	public String toString() {
		return "TimeEvent{" +
				"delay=" + delay +
				", owner=" + owner.getName() +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TimeEvent timeEvent = (TimeEvent) o;

		if (Float.compare(timeEvent.delay, delay) != 0) return false;
		return owner != null ? owner.equals(timeEvent.owner) : timeEvent.owner == null;

	}

	@Override
	public int hashCode() {
		int result = (delay != +0.0f ? Float.floatToIntBits(delay) : 0);
		result = 31 * result + (owner != null ? owner.hashCode() : 0);
		return result;
	}
}
