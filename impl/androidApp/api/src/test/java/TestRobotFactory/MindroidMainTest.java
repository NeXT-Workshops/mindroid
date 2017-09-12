package TestRobotFactory;

import org.mindroid.api.statemachine.IMindroidMain;
import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;
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
    MotorProvider motorProvider = robotController.getMotorProvider();
    BrickController brickController = robotController.getBrickController();
    SensorController sensorController = robotController.getSensorController();

    //RobotEvent collision = RuleFactory.createCollisionRule(EV3PortIds.S1,"<",20);
    //RobotEvent no_ground = RuleFactory.createBrightnessRule(EV3PortIds.S2,"<",0.6);

    //TODO HashMap<String,RobotEvent> events = Robot.getInstance().getRobotEvents();


    public void initStatemachine(){

        IState state_forward = new State("Forward") {
            @Override
            public void run() {
                System.out.println(this.getName() + " isActive\n");
                //FORWARD
                motorProvider.setMotorDirection(EV3PortIDs.PORT_A,MotorDirection.FORWARD);
                motorProvider.setMotorDirection(EV3PortIDs.PORT_D,MotorDirection.FORWARD);

                motorProvider.setMotorSpeed(EV3PortIDs.PORT_A,50);
                motorProvider.setMotorSpeed(EV3PortIDs.PORT_D,50);
            }
        };

        IState state_backward = new State("backward") {
            @Override
            public void run() {
                System.out.println(this.getName() + " isActive\n");
                //BACKWARD
                motorProvider.setMotorDirection(EV3PortIDs.PORT_A,MotorDirection.BACKWARD);
                motorProvider.setMotorDirection(EV3PortIDs.PORT_D,MotorDirection.BACKWARD);

                motorProvider.setMotorSpeed(EV3PortIDs.PORT_A,50);
                motorProvider.setMotorSpeed(EV3PortIDs.PORT_D,50);
            }
        };

        IState state_turn = new State("turn") {
            @Override
            public void run() {
                System.out.println(this.getName() + " isActive\n");
                //TURN LEFT
                motorProvider.setMotorDirection(EV3PortIDs.PORT_A,MotorDirection.BACKWARD);
                motorProvider.setMotorDirection(EV3PortIDs.PORT_D,MotorDirection.FORWARD);

                motorProvider.setMotorSpeed(EV3PortIDs.PORT_A,50);
                motorProvider.setMotorSpeed(EV3PortIDs.PORT_D,50);
            }
        };

        //Set start states ------
        sm.addState(state_forward);
        sm.setStartState(state_forward);

        //Add States ------
        sm.addState(state_backward);
        sm.addState(state_turn);

    }


    @Override
    public StatemachineCollection getStatemachineCollection() {
        StatemachineCollection sc = new StatemachineCollection();
        sc.addStatemachine("main",sm);
        return sc;
    }
}
