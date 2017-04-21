package org.mindroid.android.app.SchuelerProjekt;

import org.junit.Test;
import org.mindroid.api.communication.IMessenger;
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
import org.mindroid.impl.statemachine.constraints.*;
import org.mindroid.impl.statemachine.properties.MessageProperty;
import org.mindroid.impl.statemachine.properties.Seconds;
import org.mindroid.impl.statemachine.properties.Milliseconds;
import org.mindroid.impl.statemachine.properties.sensorproperties.Color;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;

import static org.mindroid.api.communication.IMessenger.SERVER_LOG;

/**
 * Created by torben on 02.03.2017.
 */

public class MindroidMain implements IMindroidMain {


    IStatemachine sm = new Statemachine("main");

    RobotController robotController = Robot.getRobotController();
    MotorController motorController = robotController.getMotorController();
    BrickController brickController = robotController.getBrickController();
    SensorController sensorController = robotController.getSensorController();
    IMessenger messenger = Robot.getRobotController().getMessenger();
    String myRobotID = Robot.getRobotController().getRobotID();



    @Override
    public IStatemachine getStatemachine() throws StateAlreadyExsists {
        initStatemachine();
        return sm;
    }


    public void initStatemachine() throws StateAlreadyExsists {
        lightshowBig();
    }


    public void lightshowBig() throws StateAlreadyExsists {
        final String cmd_red = "RED";
        final String cmd_yellow = "YELLOW";
        final String cmd_green = "GREEN";


        String player_1 = "Bobby";
        String player_2 = "Lea";

        final String other_player;

        //WHO AM I?
        if(myRobotID.equals(player_1)){
            other_player = player_2;
            final String myID = player_1;
        }else{
            other_player = player_1;
            final String myID = player_2;

        }
        final String player_dest = other_player;

        /** Waits for command to start with sending an command or wait**/
        IState state_idle = new State("Idle"){
            @Override
            public void run(){
                messenger.sendMessage(SERVER_LOG,"Who should sent the Command?");
                brickController.resetEV3StatusLight();
            }
        };

        IConstraint send_cmd = new OR(new EQ(new Color(Color.BLACK,EV3PortIDs.PORT_1)),new EQ(new Color(Color.BLACK,EV3PortIDs.PORT_4)));
        ITransition trans_send_cmd = new Transition(send_cmd);


        IState state_sending_command = new State("SendingCommand"){
            @Override
            public void run(){
                int color;
                messenger.sendMessage(SERVER_LOG,"Calculating light.. ");
                do {
                    color = (int)Math.round(Math.random() *1000 * 3);
                    switch(color){
                        case 1: messenger.sendMessage(player_dest,cmd_red); messenger.sendMessage(SERVER_LOG,"sent Command: "+cmd_red); break;
                        case 2: messenger.sendMessage(player_dest,cmd_yellow); messenger.sendMessage(SERVER_LOG,"sent Command: "+cmd_yellow); break;
                        case 3: messenger.sendMessage(player_dest,cmd_green); messenger.sendMessage(SERVER_LOG,"sent Command: "+cmd_green); break;
                    }
                }while(!(color >= 1 && color <= 3));
                messenger.sendMessage(SERVER_LOG,"Command sent to "+other_player);
            }
        };

        IConstraint light_red = new MsgReceived(new MessageProperty(cmd_red,other_player));
        IConstraint light_yellow = new MsgReceived(new MessageProperty(cmd_yellow,other_player));
        IConstraint light_green = new MsgReceived(new MessageProperty(cmd_green,other_player));

        ITransition trans_light_red = new Transition(light_red);
        ITransition trans_light_yellow = new Transition(light_yellow);
        ITransition trans_light_green = new Transition(light_green);


        State state_red = new State("Show Red Light"){
            @Override
            public void run(){
                brickController.setEV3StatusLight(EV3StatusLightColor.RED,EV3StatusLightInterval.ON);
                messenger.sendMessage(SERVER_LOG,"Watch "+myRobotID+"s Lightshow!");
            }
        };

        State state_yellow = new State("Show Yellow Light"){
            @Override
            public void run(){
                brickController.setEV3StatusLight(EV3StatusLightColor.YELLOW,EV3StatusLightInterval.ON);
                messenger.sendMessage(SERVER_LOG,"Watch "+myRobotID+"s Lightshow!");
            }
        };

        State state_green = new State("Show Green Light"){
            @Override
            public void run(){
                brickController.setEV3StatusLight(EV3StatusLightColor.GREEN,EV3StatusLightInterval.ON);
                messenger.sendMessage(SERVER_LOG,"Watch "+myRobotID+"s Lightshow!");
            }
        };

        IConstraint time_expired = new TimeExpired(new Seconds(4));
        Transition trans_end_lightshow = new Transition(time_expired);
        Transition trans_cmd_sent = new Transition(time_expired);

        sm.addState(state_idle);
        sm.setStartState(state_idle);
        sm.addState(state_sending_command);
        sm.addState(state_red);
        sm.addState(state_yellow);
        sm.addState(state_green);

        sm.addTransition(trans_send_cmd,state_idle,state_sending_command);
        sm.addTransition(trans_cmd_sent,state_sending_command,state_idle);

        sm.addTransition(trans_light_red,state_idle,state_red);
        sm.addTransition(trans_light_yellow,state_idle,state_yellow);
        sm.addTransition(trans_light_green,state_idle,state_green);

        sm.addTransition(trans_end_lightshow,state_red,state_idle);
        sm.addTransition(trans_end_lightshow,state_yellow,state_idle);
        sm.addTransition(trans_end_lightshow,state_green,state_idle);

    }

    public void testSM() throws StateAlreadyExsists {
        IState state_A = new State("STATE A");
        IState state_B = new State("STATE B");

        Transition transition = new Transition(new TimeExpired(new Seconds(3))){
            @Override
            public void run(){
                System.out.println("Executing overwritten transition run method.");
                messenger.sendMessage(SERVER_LOG,"Executing tranistionmethod");
            }
        };

        sm.addState(state_A);
        sm.setStartState(state_A);
        sm.addState(state_B);

        sm.addTransition(transition,state_A,state_B);
    }


    public void lightshowSmall() throws StateAlreadyExsists {
        final String cmd_red = "RED";
        final String cmd_yellow = "YELLOW";
        final String cmd_green = "GREEN";


        String player_1 = "Bobby";
        String player_2 = "Lea";

        final String other_player;

        //WHO AM I?
        if(myRobotID.equals(player_1)){
            other_player = player_2;
            final String myID = player_1;
        }else{
            other_player = player_1;
            final String myID = player_2;

        }
        final String player_src = other_player;
        final String player_dest = other_player;

        /** Waits for command to start with sending an command or wait**/
        IState state_idle = new State("Idle"){
            @Override
            public void run(){
                messenger.sendMessage(SERVER_LOG,"Who should sent the Command?");
            }
        };

        IConstraint send_cmd = new OR(new EQ(new Color(Color.BLACK,EV3PortIDs.PORT_1)),new EQ(new Color(Color.BLACK,EV3PortIDs.PORT_4)));

        Transition trans_send_first_cmd = new Transition(send_cmd){
            @Override
            public void run(){
                int color;
                System.out.println("Running transition --> 'trans_send_first_cmd' --> Calculating light..");
                messenger.sendMessage(SERVER_LOG,"Calculating light.. ");
                do {
                    color = (int)Math.round(Math.random() *1000 * 3);
                    switch(color){
                        case 1: messenger.sendMessage(player_dest,cmd_red); break;
                        case 2: messenger.sendMessage(player_dest,cmd_yellow); break;
                        case 3: messenger.sendMessage(player_dest,cmd_green); break;
                    }
                }while(!(color >= 1 && color <= 3));
                messenger.sendMessage(SERVER_LOG,"Command sent to "+other_player);
            }
        };

        IState state_wait_for_cmd = new State("waiting_for_cmd");

        IState state_lightshow = new State("lightshow");

        IConstraint light_red = new MsgReceived(new MessageProperty(cmd_red,other_player));
        IConstraint light_yellow = new MsgReceived(new MessageProperty(cmd_yellow,other_player));
        IConstraint light_green = new MsgReceived(new MessageProperty(cmd_green,other_player));

        Transition trans_red = new Transition(light_red){
            @Override
            public void run(){
                brickController.setEV3StatusLight(EV3StatusLightColor.RED,EV3StatusLightInterval.ON);
                messenger.sendMessage(SERVER_LOG,"Watch "+myRobotID+"s Lightshow!");
            }
        };

        Transition trans_yellow = new Transition(light_yellow){
            @Override
            public void run(){
                brickController.setEV3StatusLight(EV3StatusLightColor.YELLOW,EV3StatusLightInterval.ON);
                messenger.sendMessage(SERVER_LOG,"Watch "+myRobotID+"s Lightshow!");
            }
        };

        Transition trans_green = new Transition(light_green){
            @Override
            public void run(){
                brickController.setEV3StatusLight(EV3StatusLightColor.GREEN,EV3StatusLightInterval.ON);
                messenger.sendMessage(SERVER_LOG,"Watch "+myRobotID+"s Lightshow!");
            }
        };

        IConstraint time_expired = new TimeExpired(new Seconds(4));
        Transition trans_end_lightshow = new Transition(time_expired){
            @Override
            public void run(){
                int color;
                do {
                    color = (int)Math.round(Math.random() *1000 * 3);
                    switch(color){
                        case 1: messenger.sendMessage(player_dest,cmd_red); break;
                        case 2: messenger.sendMessage(player_dest,cmd_yellow); break;
                        case 3: messenger.sendMessage(player_dest,cmd_green); break;
                    }
                }while(!(color >= 1 && color <= 3));
                messenger.sendMessage(SERVER_LOG,"Command sent to "+other_player);
            }
        };


        sm.addState(state_idle);
        sm.setStartState(state_idle);

        sm.addState(state_wait_for_cmd);
        sm.addState(state_lightshow);

        sm.addTransition(trans_send_first_cmd,state_idle,state_wait_for_cmd);
        sm.addTransition(trans_red,state_idle,state_lightshow);
        sm.addTransition(trans_yellow,state_idle,state_lightshow);
        sm.addTransition(trans_green,state_idle,state_lightshow);

        sm.addTransition(trans_end_lightshow,state_lightshow,state_wait_for_cmd);

        sm.addTransition(trans_red,state_wait_for_cmd,state_lightshow);
        sm.addTransition(trans_yellow,state_wait_for_cmd,state_lightshow);
        sm.addTransition(trans_green,state_wait_for_cmd,state_lightshow);


    }
    /*
    public void wallPingPong() throws StateAlreadyExsists {
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

        IState state_time_test = new State("NothingFound :(") {
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
    }*/

}
