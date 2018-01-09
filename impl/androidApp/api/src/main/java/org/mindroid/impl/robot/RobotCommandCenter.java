package org.mindroid.impl.robot;


import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;

import java.io.IOException;

/**
 *
 * Interface to Smartphone/app
 *
 * Created by torben on 02.03.2017.
 */
public class RobotCommandCenter implements IRobotCommandCenter {

    /**
     * The Robot Object this RommandCenter is observing
     */
    protected final Robot robot;

    /**
     * True if the Configuration (sensors/motors) succeed.
     */
    private boolean isConfigurated = false;

    public final static String ERROR_INITIALIZATION = "INITIALIZATION ERROR";

    public RobotCommandCenter(Robot robot){
        this.robot = robot;
    }

    @Override
    public void startStatemachine(String id) {
        robot.getStatemachineManager().startStatemachines(id);
    }

    @Override
    public void stopStatemachine(String id) {
        robot.getStatemachineManager().stopStatemachines(id);
    }

    @Override
    public String[] getStatemachines() {
        String[] IDsOfStatemachines = (String[]) robot.getStatemachineManager().getStatemachineIDs().toArray();
        return IDsOfStatemachines;
    }

    @Override
    public void startImperativeImplemenatation(String id) {
        robot.getImperativeImplManager().startImperativeImplementation(id);
    }

    @Override
    public void stopImperativeImplementation(String id) {
        robot.getImperativeImplManager().stopImperativeImplementation(id);
    }

    @Override
    public String[] getImperativeImplementations() {
        return robot.getImperativeImplManager().getImperativeImplIDs();
    }

    @Override
    public void connectToBrick() throws IOException {
        long timeout = 20000;
        boolean isConnected = robot.getBrick().connect();

        if(isConnected) {
            //TODO check if this is still necessary:
            long start_timer = System.currentTimeMillis();

            while (!robot.getBrick().isBrickReady()) {
                if (timeout < System.currentTimeMillis() - start_timer) {
                    Exception e = new IOException("RobotCommandCenter: Brick is not ready (timeout)!");
                    ErrorHandlerManager.getInstance().handleError(e, RobotCommandCenter.class, e.toString());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ErrorHandlerManager.getInstance().handleError(e, RobotCommandCenter.class, e.toString());
                }
            }
        }
    }

    @Override
    public boolean isConnectedToBrick() {
        if(robot.getRobotConfigurator() != null && robot.getRobotConfigurator().getBrick() != null) {
            return robot.getRobotConfigurator().getBrick().isConnected();
        }else{
            return false;
        }
    }

    @Override
    public boolean initializeConfiguration() throws BrickIsNotReadyException {
        if(robot.isSimulated()){
            //Simulation is enabled - no connection to brick or sensors
            isConfigurated = true;
        }else{
            isConfigurated = robot.getRobotConfigurator().initializeConfiguration();
            if(!isConfigurated) {
                ErrorHandlerManager.getInstance().handleError(new Exception("Initialization of a Sensor/motor Failed"), this.getClass(), ERROR_INITIALIZATION);
            }
        }

        return isConfigurated ;
    }

    @Override
    public void abortConfiguration() {
        robot.getRobotConfigurator().interruptConfigurationProcess();
    }

    @Override
    public boolean isConfigurated() {
        return isConnectedToBrick() && isConfigurated;
    }

    @Override
    public synchronized boolean isMessengerConnected() {
        if(robot.getMessenger() != null) {
            return robot.getMessenger().isConnected();
        }else{
            return false;
        }
    }

    @Override
    public synchronized boolean connectMessenger(String msgServerIP, int msgServerTCPPort) {
        if(this.robot.getMessenger() != null) {
            return this.robot.getMessenger().connect(msgServerIP,msgServerTCPPort);
        }else{
            ErrorHandlerManager.getInstance().handleError(new Exception("[RobotCommandCenter:connectMessenger] Robot is null"),RobotCommandCenter.class,"[RobotCommandCenter:connectMessenger]  Robot is null");
            return false;
        }
    }

    @Override
    public synchronized void disconnectMessenger() {
        robot.getMessenger().disconnect();
    }

    @Override
    public void disconnectFromBrick() {
        robot.getRobotConfigurator().getBrick().disconnect();
    }
}
