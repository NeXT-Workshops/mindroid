package org.mindroid.impl.robot.context;

import org.mindroid.api.IClockListener;
import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.robot.context.IConstraintEvaluator;
import org.mindroid.api.robot.context.IRobotContextStateManager;
import org.mindroid.impl.ev3.EV3PortIDs;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by torben on 11.03.2017.
 */
public class RobotContextStateManager implements IRobotContextStateManager,IClockListener {

    private IRobotContextState robotContextStateSource;
    private List<IConstraintEvaluator> evaluators;

    private Timer clk_Timer;

    /** taking a Snapshot and constraint evaluation per second **/
    private static final long clocksPerSecond = 20;

    private RobotContextStateManager(){
        //clk_Timer.schedule(new TimerTa);
        robotContextStateSource = RobotContextState.getInstance();
        evaluators = new ArrayList<IConstraintEvaluator>(1);
        clk_Timer = new Timer(true);
        clk_Timer.schedule(new Task_handleCLK(this),100, 1000/ clocksPerSecond);
    }


    private static IRobotContextStateManager ourInstance = new RobotContextStateManager();

    public static IRobotContextStateManager getInstance() {
        return ourInstance;
    }


    public synchronized IRobotContextState takeSnapshot(){
        RobotContextState robotContextState = new RobotContextState();

        robotContextState.setSensor_output_S1(robotContextStateSource.getSensorEvent(EV3PortIDs.PORT_1));
        robotContextState.setSensor_output_S2(robotContextStateSource.getSensorEvent(EV3PortIDs.PORT_2));
        robotContextState.setSensor_output_S3(robotContextStateSource.getSensorEvent(EV3PortIDs.PORT_3));
        robotContextState.setSensor_output_S4(robotContextStateSource.getSensorEvent(EV3PortIDs.PORT_4));

        robotContextState.setReceivedTimeEvents(robotContextStateSource.getTimeEvents());
        robotContextState.setReceivedMessages(robotContextStateSource.getMessages());

        //System.out.println("Took a snapshot! "+robotContextState.getMessages()+" ## "+robotContextState.getTimeEvents());

        return robotContextState;
    }

    @Override
    public synchronized void handleCLK(){
        //System.out.println("[RobotContextSateManager:handleCLK] handle CLK");
        Runnable evaluateRobotState = new Runnable(){
            @Override
            public void run(){
                IRobotContextState context = takeSnapshot();
                for(IConstraintEvaluator contextListener: evaluators) {
                    contextListener.handleRobotContextState(context);
                }
            }
        };
        new Thread(evaluateRobotState).start();
    }

    @Override
    public void registerRobotContextStateListener(IConstraintEvaluator evaluator){
        evaluators.add(evaluator);
    }

    @Override
    public synchronized void cleanContextState(){
        robotContextStateSource.getTimeEvents().clear();
        robotContextStateSource.getMessages().clear();
    }

    @Override
    public void setGyroSensorStartCondition() {
        //The addPosition methods checks if the event is a valid GyroSensor event or if its null.
        StartCondition.getInstance().addPosition(EV3PortIDs.PORT_1, robotContextStateSource.getSensorEvent(EV3PortIDs.PORT_1));
        StartCondition.getInstance().addPosition(EV3PortIDs.PORT_2, robotContextStateSource.getSensorEvent(EV3PortIDs.PORT_2));
        StartCondition.getInstance().addPosition(EV3PortIDs.PORT_3, robotContextStateSource.getSensorEvent(EV3PortIDs.PORT_3));
        StartCondition.getInstance().addPosition(EV3PortIDs.PORT_4, robotContextStateSource.getSensorEvent(EV3PortIDs.PORT_4));
    }


    // ------------------------ Class: Task_handleCLK

    private class Task_handleCLK extends TimerTask{

        IClockListener listener;

        public Task_handleCLK(IClockListener clkListener){
            this.listener = clkListener;
        }

        @Override
        public void run() {

            listener.handleCLK();
        }
    }
}
