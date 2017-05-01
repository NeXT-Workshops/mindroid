package statemachine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mindroid.api.statemachine.*;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.StateAlreadyExsists;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.robot.context.RobotContextStateListener;
import org.mindroid.impl.robot.context.RobotContextStateManager;
import org.mindroid.impl.statemachine.*;
import org.mindroid.impl.statemachine.constraints.EQ;
import org.mindroid.impl.statemachine.constraints.LT;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;

/**
 * Statemachine Testclass
 * 
 * @author Torben
 *
 */
public class TestStateMachineManger {
	
	IStatemachine sm;
	IState start;
	IState end;
	
	StatemachineManager smm = StatemachineManager.getInstance();
	String statemachineID = "main";

	IConstraint color_red;
	IConstraint collision;

	String state_name_start = "start";
	String state_name_end = "end";


	@Before
	public void initStatemachine() throws StateAlreadyExsists {
		start = new State(state_name_start){
			@Override
			public void run(){
				//System.out.println("Start-State is running");
			}
		};
		
		end = new State(state_name_end){
			@Override
			public void run(){
				//System.out.println("end-State is running");
			}
		};

		color_red = new EQ(new Color(Color.RED, EV3PortIDs.PORT_1));
		collision = new LT(new Distance(4f, EV3PortIDs.PORT_1));

		this.sm = new Statemachine(statemachineID);
		this.sm.addState(start);
		this.sm.addState(end);

		this.sm.addTransition(new Transition(color_red),start,end);
		this.sm.addTransition(new Transition(collision),end,start);

		this.sm.setStartState(start);
	}

	@Test
	public void testConstraint(){
		smm.addStatemachine(sm);
		smm.startStatemachine(statemachineID);

		//----- State 'start' should be switched to state 'end"
		smm.handleSatisfiedConstraint(statemachineID,color_red);

		assertTrue(smm.getCurrentState(statemachineID).getName().equals(state_name_end));
		assertTrue(end.isActive());
		assertFalse(start.isActive());

	//----- Nothing should change, because no transition with this constraint exists
		smm.handleSatisfiedConstraint(statemachineID,color_red);

	assertTrue(smm.getCurrentState(statemachineID).getName().equals(state_name_end));
	assertTrue(end.isActive());
	assertFalse(start.isActive());

	//----- State 'end' should be switched to state 'start'
		smm.handleSatisfiedConstraint(statemachineID,collision);

	assertTrue(smm.getCurrentState(statemachineID).getName().equals(state_name_start));
	assertTrue(start.isActive());
	assertFalse(end.isActive());
}

	@Test
	public void testStopAndResetStatemachine(){
		//TODO Impl
	}

	@Test
	public void testTimeEventDeletionStatemachine(){
		smm.addStatemachine(sm);
		smm.startStatemachine(statemachineID);



		//----- State 'start' should be switched to state 'end"
		smm.handleSatisfiedConstraint(statemachineID,color_red);

		assertTrue(smm.getCurrentState(statemachineID).getName().equals(state_name_end));
		assertTrue(end.isActive());
		assertFalse(start.isActive());

		//----- Nothing should change, because no transition with this constraint exists
		smm.handleSatisfiedConstraint(statemachineID,color_red);

		assertTrue(RobotContextStateManager.getInstance().takeSnapshot().getTimeEvents().size() == 0);
		RobotContextStateListener.getInstance().handleTimeEvent(new TimeEvent(200f,start));


		assertTrue(smm.getCurrentState(statemachineID).getName().equals(state_name_end));
		assertTrue(end.isActive());
		assertFalse(start.isActive());
		assertTrue(RobotContextStateManager.getInstance().takeSnapshot().getTimeEvents().size() == 1);

		//----- State 'end' should be switched to state 'start'
		smm.handleSatisfiedConstraint(statemachineID,collision);

		assertTrue(smm.getCurrentState(statemachineID).getName().equals(state_name_start));
		assertTrue(start.isActive());
		assertFalse(end.isActive());
		assertTrue(RobotContextStateManager.getInstance().takeSnapshot().getTimeEvents().size() == 0);
	}

	
}
