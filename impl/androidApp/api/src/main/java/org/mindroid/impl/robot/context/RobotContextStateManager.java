package org.mindroid.impl.robot.context;

import org.mindroid.api.IClockListener;
import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.robot.context.IRobotContextStateEvaluator;
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

    private IRobotContextState robotContextState_Source;
    private List<IRobotContextStateEvaluator> evaluators;

    private Timer clk_Timer;

    private static final long clocks_per_second = 20;

    private RobotContextStateManager(){
        clk_Timer = new Timer(true);
        clk_Timer.schedule(new Task_handleCLK(this),100, 1000/clocks_per_second);
        //clk_Timer.schedule(new TimerTa);
        robotContextState_Source = RobotContextStateListener.getInstance();
        evaluators = new ArrayList<IRobotContextStateEvaluator>(1);
    }


    private static IRobotContextStateManager ourInstance = new RobotContextStateManager();

    public static IRobotContextStateManager getInstance() {
        return ourInstance;
    }


    public IRobotContextState takeSnapshot(){
        RobotContextStateListener robotContextState = new RobotContextStateListener();

        robotContextState.setSensor_output_S1(robotContextState_Source.getSensorEvent(EV3PortIDs.PORT_1));
        robotContextState.setSensor_output_S2(robotContextState_Source.getSensorEvent(EV3PortIDs.PORT_2));
        robotContextState.setSensor_output_S3(robotContextState_Source.getSensorEvent(EV3PortIDs.PORT_3));
        robotContextState.setSensor_output_S4(robotContextState_Source.getSensorEvent(EV3PortIDs.PORT_4));

        robotContextState.setReceivedTimeEvents(robotContextState_Source.getTimeEvents());
        robotContextState.setReceivedMessages(robotContextState_Source.getMessages());

        //System.out.println("Took a snapshot! "+robotContextState.toString());

        return robotContextState;
    }

    @Override
    public void handleCLK(){
        IRobotContextState context = takeSnapshot();
        for(IRobotContextStateEvaluator evaluator: evaluators) {
            evaluator.evaluateConstraints(context);
        }
    }

    @Override
    public void addRobotContextStateEvaluator(IRobotContextStateEvaluator evaluator){
        evaluators.add(evaluator);
    }

    @Override
    public void cleanContextState(){
        robotContextState_Source.getTimeEvents().clear();
        //TODO: robotContextState_Source.getMessages().clearConfiguration();
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
