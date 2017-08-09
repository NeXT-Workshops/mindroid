package org.mindroid.impl.test;

import org.mindroid.api.LVL1API;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.api.statemachine.ITransition;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.StateAlreadyExistsException;
import org.mindroid.common.messages.NetworkPortConfig;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.robot.RobotFactory;
import org.mindroid.impl.statemachine.State;
import org.mindroid.impl.statemachine.Statemachine;
import org.mindroid.impl.statemachine.StatemachineCollection;
import org.mindroid.impl.statemachine.Transition;
import org.mindroid.impl.statemachine.constraints.*;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;

import java.io.IOException;

public class TestPCClient{

    public static String brickIP = "10.0.1.1";
    public static String msgServerIP = "127.0.0.1";
    public static String robotID = "Testrobot 1";

    public static IStatemachine sm;

    public static void main(String args[]){
        new TestPCClientRobot();
    }

    private static class TestPCClientRobot extends LVL1API{
        IRobotCommandCenter commandCenter;

        public TestPCClientRobot(){
            try {
                System.out.println("[TestRobot:PC-Client] making robot");
                initRobot();
                System.out.println("[TestRobot:PC-Client] Connecting to brick");
                commandCenter.connectToBrick();

                while(!commandCenter.isConnected()){
                    Thread.sleep(100);
                    System.out.println("[TestRobot:PC-Client] connecting..");
                }

                System.out.println("[TestRobot:PC-Client] Initializing configuration");

                commandCenter.initializeConfiguration();
                Thread.sleep(10000);
                System.out.println("[TestRobot:PC-Client] initialized!");

                commandCenter.startStatemachine(TestPCClient.sm.getID());

                Thread.sleep(20000);

                commandCenter.stopStatemachine(TestPCClient.sm.getID());

                Thread.sleep(5000);

                commandCenter.startStatemachine(TestPCClient.sm.getID());

                Thread.sleep(20000);

                commandCenter.stopStatemachine(TestPCClient.sm.getID());


            } catch (StateAlreadyExistsException stateAlreadyExists) {
                stateAlreadyExists.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (PortIsAlreadyInUseException e) {
                e.printStackTrace();
            } catch (BrickIsNotReadyException e) {
                e.printStackTrace();
            }
        }

        public void initRobot() throws StateAlreadyExistsException {
            TestPCClient.sm = lightshowSmall();
            StatemachineCollection statemachineCollection = new StatemachineCollection();
            statemachineCollection.addStatemachine(sm.getID(),sm);

            RobotFactory roFactory = new RobotFactory();
            System.out.println("## App.Robot.makeRobot() got called ");
            //Config
            roFactory.setRobotConfig(new RobotTestConfig());
            roFactory.setBrickIP(brickIP);
            roFactory.setBrickTCPPort(NetworkPortConfig.BRICK_PORT);
            roFactory.setMSGServerIP(msgServerIP);
            roFactory.setMSGServerTCPPort(NetworkPortConfig.SERVER_PORT);
            roFactory.setRobotServerPort(NetworkPortConfig.ROBOT_SERVER_PORT);
            roFactory.setRobotID(robotID);

            //Add Statemachines
            roFactory.addStatemachine(statemachineCollection);

            //Create Robot
            commandCenter = roFactory.createRobot();
        }



        public IStatemachine lightshowSmall() throws StateAlreadyExistsException {
            IStatemachine sm = new Statemachine("lightshowSmall");



            /** Waits for command to start with sending an command or wait**/
            IState state_idle = new State("Idle"){
                @Override
                public void run(){
                    brickController.setEV3StatusLight(EV3StatusLightColor.YELLOW,EV3StatusLightInterval.ON);
                }
            };

            IState state_red = new State("state red"){
                @Override
                public void run(){
                    brickController.setEV3StatusLight(EV3StatusLightColor.RED,EV3StatusLightInterval.ON);
                }
            };

            IConstraint distance_collision = new LT(0.15f, new Distance(EV3PortIDs.PORT_2));
            ITransition trans_collision = new Transition(distance_collision);

            IConstraint distance_no_collision = new GT(0.20f, new Distance(EV3PortIDs.PORT_2));
            ITransition trans_no_collision = new Transition(distance_collision);


            sm.addState(state_idle);
            sm.setStartState(state_idle);

            sm.addState(state_red);

            sm.addTransition(trans_collision,state_idle,state_red);
            sm.addTransition(trans_no_collision,state_red,state_idle);


            return sm;
        }
    }


}
