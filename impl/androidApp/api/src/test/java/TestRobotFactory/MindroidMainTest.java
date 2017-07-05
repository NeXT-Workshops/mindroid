package TestRobotFactory;

import org.mindroid.api.statemachine.IMindroidMain;
import org.mindroid.api.robot.control.IMotorControl;
import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;
import org.mindroid.impl.robot.*;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.statemachine.State;
import org.mindroid.impl.statemachine.Statemachine;
import org.mindroid.impl.statemachine.StatemachineCollection;

/**
 * Created by torben on 02.03.2017.
 */

public class MindroidMainTest implements IMindroidMain {


    IStatemachine sm = new Statemachine("main");
    RobotController robotController = Robot.getRobotController();
    MotorController motorController = robotController.getMotorController();
    BrickController brickController = robotController.getBrickController();
    SensorController sensorController = robotController.getSensorController();

    //RobotEvent collision = RuleFactory.createCollisionRule(EV3PortIds.S1,"<",20);
    //RobotEvent no_ground = RuleFactory.createBrightnessRule(EV3PortIds.S2,"<",0.6);

    //TODO HashMap<String,RobotEvent> events = Robot.getInstance().getRobotEvents();


    public void initStatemachine() throws StateAlreadyExists {

        IState state_forward = new State("Forward") {
            @Override
            public void run() {
                System.out.println(this.getName() + " isActive\n");
                //FORWARD
                motorController.setMotorDirection(EV3PortIDs.PORT_A,IMotorControl.MOTOR_FORWARD);
                motorController.setMotorDirection(EV3PortIDs.PORT_D,IMotorControl.MOTOR_FORWARD);

                motorController.setMotorSpeed(EV3PortIDs.PORT_A,50);
                motorController.setMotorSpeed(EV3PortIDs.PORT_D,50);
            }
        };

        IState state_backward = new State("backward") {
            @Override
            public void run() {
                System.out.println(this.getName() + " isActive\n");
                //BACKWARD
                motorController.setMotorDirection(EV3PortIDs.PORT_A,IMotorControl.MOTOR_BACKWARD);
                motorController.setMotorDirection(EV3PortIDs.PORT_D,IMotorControl.MOTOR_BACKWARD);

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

                motorController.setMotorSpeed(EV3PortIDs.PORT_A,50);
                motorController.setMotorSpeed(EV3PortIDs.PORT_D,50);
            }
        };

        //Set start states ------
        sm.addState(state_forward);
        sm.setStartState(state_forward);

        //Add States ------
        sm.addState(state_backward);
        sm.addState(state_turn);

        //TODO refactor Events
        //IUltrasonicSensorEvent wall_hit = new UltrasonicSensorEvent(IUltrasonicSensorEvent.UltrasonicEventType.COLLISION_WALL, ultrasonic_front);
        //IUltrasonicSensorEvent no_wall_hit = new UltrasonicSensorEvent(IUltrasonicSensorEvent.UltrasonicEventType.NO_WALL, ultrasonic_front);

        //TimeEvent delay_1sec_backward = new TimeEvent(1, state_backward);
        //TimeEvent delay_1sec_turn = new TimeEvent(1.3f, state_turn);

        //--- Transitionen
        //ITransition if_delay_1sec_backward = new Transition(delay_1sec_backward);
        //ITransition if_delay_1sec_turn = new Transition(delay_1sec_turn);

        // TODO
        //ITransition if_wall_hit = new Transition(wall_hit);
        //ITransition if_no_wall_hit = new Transition(no_wall_hit);

        //sm.addTransition(if_wall_hit, state_forward, state_backward); TODO uncomment
        //sm.addTransition(if_delay_1sec_backward, state_backward, state_turn);
        //sm.addTransition(if_delay_1sec_turn, state_turn, state_forward);
    }


    @Override
    public StatemachineCollection getStatemachineCollection() throws StateAlreadyExists {
        StatemachineCollection sc = new StatemachineCollection();
        sc.addStatemachine(sm);
        return sc;
    }
}
