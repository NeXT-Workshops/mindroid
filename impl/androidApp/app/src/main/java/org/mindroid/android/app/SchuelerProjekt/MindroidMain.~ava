package org.mindroid.android.app.SchuelerProjekt;

import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.api.statemachine.IMindroidMain;
import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.api.statemachine.ITransition;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.StateAlreadyExsists;
import org.mindroid.impl.robot.Robot;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.robot.BrickController;
import org.mindroid.impl.robot.MotorController;
import org.mindroid.impl.robot.RobotController;
import org.mindroid.impl.robot.SensorController;
import org.mindroid.impl.statemachine.State;
import org.mindroid.impl.statemachine.Statemachine;
import org.mindroid.impl.statemachine.Transition;
import org.mindroid.impl.statemachine.constraints.AND;
import org.mindroid.impl.statemachine.constraints.GT;
import org.mindroid.impl.statemachine.constraints.LT;
import org.mindroid.impl.statemachine.constraints.TimeExpired;
import org.mindroid.impl.statemachine.properties.Seconds;
import org.mindroid.impl.statemachine.properties.Milliseconds;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;

/**
 * Created by torben on 02.03.2017.
 */

public class MindroidMain implements IMindroidMain {


    IStatemachine sm = new Statemachine("main");

    RobotController robotController = Robot.getRobotController();
    MotorController motorController = robotController.getMotorController();
    BrickController brickController = robotController.getBrickController();
    SensorController sensorController = robotController.getSensorController();

    IConstraint collision = new LT(new Distance(0.30f,EV3PortIDs.PORT_2));


    @Override
    public IStatemachine getStatemachine() throws StateAlreadyExsists {
        initStatemachine();
        return sm;
    }


    public void initStatemachine() throws StateAlreadyExsists {


        IState state_forward = new State("Forward") {
            @Override
            public void run() {
                System.out.println(this.getName() + " isActive\n");
                //FORWARD
                motorController.setMotorDirection(EV3PortIDs.PORT_A,IMotorControl.MOTOR_FORWARD);
                motorController.setMotorDirection(EV3PortIDs.PORT_D,IMotorControl.MOTOR_FORWARD);

                brickController.setEV3StatusLight(EV3StatusLightColor.GREEN, EV3StatusLightInterval.ON);


                motorController.setMotorSpeed(EV3PortIDs.PORT_A,50);
                motorController.setMotorSpeed(EV3PortIDs.PORT_D,50);
            }
        };

        IState state_time_test = new State("TestTimeEventState") {
            @Override
            public void run() {
                System.out.println(this.getName() + " isActive\n");
                //FORWARD
                motorController.stop(EV3PortIDs.PORT_A);
                motorController.stop(EV3PortIDs.PORT_D);

                brickController.resetEV3StatusLight();
            }
        };

        IState state_backward = new State("backward") {
            @Override
            public void run() {
                System.out.println(this.getName() + " isActive\n");
                //BACKWARD
                motorController.setMotorDirection(EV3PortIDs.PORT_A,IMotorControl.MOTOR_BACKWARD);
                motorController.setMotorDirection(EV3PortIDs.PORT_D,IMotorControl.MOTOR_BACKWARD);

                brickController.setEV3StatusLight(EV3StatusLightColor.RED, EV3StatusLightInterval.BLINKING);

                motorController.setMotorSpeed(EV3PortIDs.PORT_A,50);
                motorController.setMotorSpeed(EV3PortIDs.PORT_D,50);
            }
        };

        IState state_turn = new State("turn") {
            @Override
            public void run() {
                System.out.println(this.getName() + " isActive\n");
                //TURN LEFT
                motorController.setMotorDirection(EV3PortIDs.PORT_A,IMotorControl.MOTOR_BACKWARD);
                motorController.setMotorDirection(EV3PortIDs.PORT_D,IMotorControl.MOTOR_FORWARD);

                brickController.setEV3StatusLight(EV3StatusLightColor.YELLOW, EV3StatusLightInterval.BLINKING);

                motorController.setMotorSpeed(EV3PortIDs.PORT_A,50);
                motorController.setMotorSpeed(EV3PortIDs.PORT_D,50);
            }
        };

        //Set start states ------
        sm.addState(state_forward);
        sm.setStartState(state_forward);

        //Add States ------
        sm.addState(state_time_test);
        sm.addState(state_backward);
        sm.addState(state_turn);

        IConstraint distance_collision = new LT(new Distance(0.10f, EV3PortIDs.PORT_2));
        IConstraint time_driving_backward = new TimeExpired(new Milliseconds(1200));

        IConstraint time_180turn = new TimeExpired(new Milliseconds(1300));

        IConstraint time_stop = new TimeExpired(new Seconds(5));


        //--- Transitionen
        ITransition collision = new Transition(distance_collision);
        ITransition drive_backwards = new Transition(time_driving_backward);
        ITransition done_turn_180 = new Transition(time_180turn);
        ITransition stop = new Transition(time_stop);


        sm.addTransition(collision,state_forward,state_backward);
        sm.addTransition(drive_backwards, state_backward, state_turn);
        sm.addTransition(done_turn_180, state_turn, state_forward);
        sm.addTransition(stop,state_forward,state_time_test);
    }


}
