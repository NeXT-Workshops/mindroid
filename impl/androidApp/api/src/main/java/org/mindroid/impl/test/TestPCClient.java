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
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperation;
import org.mindroid.common.messages.motor.synchronization.SynchronizedMotorOperationFactory;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;
import org.mindroid.impl.robot.RobotFactory;
import org.mindroid.impl.statemachine.State;
import org.mindroid.impl.statemachine.Statemachine;
import org.mindroid.impl.statemachine.StatemachineCollection;
import org.mindroid.impl.statemachine.Transition;
import org.mindroid.impl.statemachine.constraints.*;
import org.mindroid.impl.statemachine.properties.Milliseconds;
import org.mindroid.impl.statemachine.properties.Seconds;
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

    private static class TestPCClientRobot extends LVL1API {
        IRobotCommandCenter commandCenter;

        public TestPCClientRobot() {
            try {
                System.out.println("[TestRobot:PC-Client] making robot");
                initRobot();
                System.out.println("[TestRobot:PC-Client] Connecting to brick");
                commandCenter.connectToBrick();

                while (!commandCenter.isConnected()) {
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

        private void initRobot() throws StateAlreadyExistsException {
            TestPCClient.sm = syncdMotorTest();
            StatemachineCollection statemachineCollection = new StatemachineCollection();
            statemachineCollection.addStatemachine(sm.getID(), sm);

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


        public IStatemachine syncdMotorTest(){
            IStatemachine sm = new Statemachine("SyncedMotorsTest");

            IState state_forward = new State("Forward") {
                @Override
                public void run() {
                    System.out.println(this.getName() + " isActive\n");

                    SynchronizedMotorOperation rotate = SynchronizedMotorOperationFactory.createRotateOperation(720);
                    SynchronizedMotorOperation forward = SynchronizedMotorOperationFactory.createForwardOperation();
                    SynchronizedMotorOperation noOperation = SynchronizedMotorOperationFactory.createNoOperation();

                    //FORWARD
                    motorProvider.getSynchronizedMotors().executeSynchronizedOperation(forward,noOperation,noOperation,forward);

                    brickController.setEV3StatusLight(EV3StatusLightColor.GREEN, EV3StatusLightInterval.ON);
                }
            };
            sm.addState(state_forward);
            sm.setStartState(state_forward);

            return sm;
        }

        public IStatemachine wallPingPong() {
            IStatemachine sm = new Statemachine("SingleWallPingPong");

            IState state_forward = new State("Forward") {
                @Override
                public void run() {
                    System.out.println(this.getName() + " isActive\n");
                    //FORWARD
                    motorProvider.getMotor(EV3PortIDs.PORT_A).forward();
                    motorProvider.getMotor(EV3PortIDs.PORT_D).forward();

                    brickController.setEV3StatusLight(EV3StatusLightColor.GREEN, EV3StatusLightInterval.ON);


                    motorProvider.getMotor(EV3PortIDs.PORT_A).setSpeed( 50);
                    motorProvider.getMotor(EV3PortIDs.PORT_D).setSpeed(  50);
                }
            };

            IState state_time_test = new State("NothingFound :(") {
                @Override
                public void run() {
                    System.out.println(this.getName() + " isActive\n");
                    //FORWARD
                    motorProvider.getMotor(EV3PortIDs.PORT_A).stop();
                    motorProvider.getMotor(EV3PortIDs.PORT_D).stop();

                    brickController.resetEV3StatusLight();
                }
            };


            IState state_backward = new State("backward") {
                @Override
                public void run() {
                    System.out.println(this.getName() + " isActive\n");
                    //BACKWARD
                    motorProvider.getMotor(EV3PortIDs.PORT_A).backward();
                    motorProvider.getMotor(EV3PortIDs.PORT_D).backward();

                    brickController.setEV3StatusLight(EV3StatusLightColor.RED, EV3StatusLightInterval.BLINKING);

                    motorProvider.getMotor(EV3PortIDs.PORT_A).setSpeed(50);
                    motorProvider.getMotor(EV3PortIDs.PORT_D).setSpeed(50);
                }
            };

            IState state_turn = new State("turn") {
                @Override
                public void run() {
                    System.out.println(this.getName() + " isActive\n");
                    //TURN LEFT
                    motorProvider.getMotor(EV3PortIDs.PORT_A).backward();
                    motorProvider.getMotor(EV3PortIDs.PORT_D).forward();

                    brickController.setEV3StatusLight(EV3StatusLightColor.YELLOW, EV3StatusLightInterval.BLINKING);

                    motorProvider.getMotor(EV3PortIDs.PORT_A).setSpeed(50);
                    motorProvider.getMotor(EV3PortIDs.PORT_D).setSpeed(50);
                }
            };

            //Set start states ------
            sm.addState(state_forward);
            sm.setStartState(state_forward);

            //Add States ------
            sm.addState(state_time_test);
            sm.addState(state_backward);
            sm.addState(state_turn);

            IConstraint distance_collision = new LT(0.10f, new Distance(EV3PortIDs.PORT_2));
            IConstraint time_driving_backward = new TimeExpired(new Milliseconds(1200));

            IConstraint time_180turn = new TimeExpired(new Milliseconds(1300));

            IConstraint time_stop = new TimeExpired(new Seconds(25));


            //--- Transitionen
            ITransition collision = new Transition(distance_collision);
            ITransition drive_backwards = new Transition(time_driving_backward);
            ITransition done_turn_180 = new Transition(time_180turn);
            ITransition stop = new Transition(time_stop);


            sm.addTransition(collision, state_forward, state_backward);
            sm.addTransition(drive_backwards, state_backward, state_turn);
            sm.addTransition(done_turn_180, state_turn, state_forward);
            sm.addTransition(stop, state_forward, state_time_test);

            return sm;
        }

    }
}