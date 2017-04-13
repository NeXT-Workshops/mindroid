package statemachine.constraints;

import org.junit.Test;
import org.mindroid.api.robot.context.IRobotContextState;
import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.api.statemachine.constraints.AbstractLogicOperator;
import org.mindroid.api.statemachine.constraints.IComparator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.Sensors;
import org.mindroid.impl.robot.context.RobotContextStateListener;
import org.mindroid.impl.ev3.EV3PortIDs;
import org.mindroid.impl.robot.context.StartCondition;
import org.mindroid.impl.sensor.EV3Sensor;
import org.mindroid.impl.sensor.EV3SensorEvent;
import org.mindroid.impl.statemachine.State;
import org.mindroid.impl.statemachine.TimeEvent;
import org.mindroid.impl.statemachine.constraints.*;
import org.mindroid.impl.statemachine.properties.Milliseconds;
import org.mindroid.impl.statemachine.properties.Seconds;
import org.mindroid.impl.statemachine.properties.sensorproperties.Distance;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by torben on 24.03.2017.
 */
public class TestConstraints {

    @Test
    public void testLT(){
        float test_distance = 20;
        float test_value_true = 15;
        float test_value_flase = 25;

        IRobotContextState contextState = new RobotContextStateListener();
        EV3Sensor testSensor = new EV3Sensor("-",1,1, Sensors.EV3UltrasonicSensor,EV3PortIDs.PORT_1, SensorMessages.SensorMode_.DISTANCE);
        IEV3SensorEventListener rcs = (IEV3SensorEventListener) contextState;
        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor,test_value_true,0, SensorMessages.SensorMode_.DISTANCE));

        Distance distance = new Distance(test_distance, EV3PortIDs.PORT_1);
        IConstraint lowerThan = new LT(distance);

        assertTrue(((IComparator) lowerThan).evaluate(contextState));

        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor,test_value_flase,0, SensorMessages.SensorMode_.DISTANCE));
        assertFalse(((IComparator) lowerThan).evaluate(contextState));

        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor,test_value_flase,0, SensorMessages.SensorMode_.LISTEN));
        assertFalse(((IComparator) lowerThan).evaluate(contextState));

    }

    @Test
    public void testGT(){
        float test_distance = 20;
        float test_value_false = 15;
        float test_value_true = 25;

        IRobotContextState contextState = new RobotContextStateListener();
        EV3Sensor testSensor = new EV3Sensor("-",1,1, Sensors.EV3UltrasonicSensor,EV3PortIDs.PORT_1, SensorMessages.SensorMode_.DISTANCE);
        IEV3SensorEventListener rcs = (IEV3SensorEventListener) contextState;
        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor,test_value_true,0, SensorMessages.SensorMode_.DISTANCE));

        Distance distance = new Distance(test_distance, EV3PortIDs.PORT_1);
        IConstraint gt = new GT(distance);

        assertTrue(((IComparator) gt).evaluate(contextState));

        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor,test_value_false,0, SensorMessages.SensorMode_.DISTANCE));
        assertFalse(((IComparator) gt).evaluate(contextState));
    }

    @Test
    public void testEQ(){
        float test_distance = 20;
        float test_value_false = 15;
        float test_value_true = 20;

        IRobotContextState contextState = new RobotContextStateListener();
        EV3Sensor testSensor = new EV3Sensor("-",1,1, Sensors.EV3UltrasonicSensor,EV3PortIDs.PORT_1, SensorMessages.SensorMode_.DISTANCE);
        IEV3SensorEventListener rcs = (IEV3SensorEventListener) contextState;
        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor,test_value_true,0, SensorMessages.SensorMode_.DISTANCE));

        Distance distance = new Distance(test_distance, EV3PortIDs.PORT_1);
        IConstraint eq = new EQ(distance);

        assertTrue(((IComparator) eq).evaluate(contextState));

        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor,test_value_false,0, SensorMessages.SensorMode_.DISTANCE));
        assertFalse(((IComparator) eq).evaluate(contextState));
    }

    @Test
    public void testOR(){
        float test_distance = 20;
        float test_value_s1 = 20;
        float test_value_s2 = 19;

        EV3Sensor testSensor_1 = new EV3Sensor("-",1,1, Sensors.EV3UltrasonicSensor,EV3PortIDs.PORT_1, SensorMessages.SensorMode_.DISTANCE);
        EV3Sensor testSensor_2 = new EV3Sensor("-",1,1, Sensors.EV3UltrasonicSensor,EV3PortIDs.PORT_2, SensorMessages.SensorMode_.DISTANCE);

        IRobotContextState contextState = new RobotContextStateListener();

        IEV3SensorEventListener rcs = (IEV3SensorEventListener) contextState;
        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor_1,test_value_s1,0, SensorMessages.SensorMode_.DISTANCE));
        rcs.handleSensorEvent(EV3PortIDs.PORT_2,new EV3SensorEvent(testSensor_2,test_value_s2,0, SensorMessages.SensorMode_.DISTANCE));

        Distance distance_s1 = new Distance(test_distance, EV3PortIDs.PORT_1);
        IConstraint eq_s1 = new EQ(distance_s1);

        Distance distance_s2 = new Distance(test_distance, EV3PortIDs.PORT_2);
        IConstraint eq_s2 = new EQ(distance_s2);
        IConstraint or = new OR(eq_s1,eq_s2);
        boolean result_s1 = ((IComparator) eq_s1).evaluate(contextState);
        boolean result_s2 = ((IComparator) eq_s2).evaluate(contextState);

        assertTrue(result_s1);
        assertFalse(result_s2);
        assertTrue(((AbstractLogicOperator)or).evaluate(result_s1,result_s2));


        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor_1,test_distance+2,0, SensorMessages.SensorMode_.DISTANCE));
        rcs.handleSensorEvent(EV3PortIDs.PORT_2,new EV3SensorEvent(testSensor_2,test_distance-2,0, SensorMessages.SensorMode_.DISTANCE));
        result_s1 = ((IComparator) eq_s1).evaluate(contextState);
        result_s2 = ((IComparator) eq_s2).evaluate(contextState);
        assertFalse(result_s1);
        assertFalse(result_s2);
        assertFalse(((AbstractLogicOperator)or).evaluate(result_s1,result_s2));
    }

    @Test
    public void testAND(){
        float test_distance = 20;
        float test_value_s1 = 20;
        float test_value_s2 = 19;

        EV3Sensor testSensor_1 = new EV3Sensor("-",1,1, Sensors.EV3UltrasonicSensor,EV3PortIDs.PORT_1, SensorMessages.SensorMode_.DISTANCE);
        EV3Sensor testSensor_2 = new EV3Sensor("-",1,1, Sensors.EV3UltrasonicSensor,EV3PortIDs.PORT_2, SensorMessages.SensorMode_.DISTANCE);

        IRobotContextState contextState = new RobotContextStateListener();

        IEV3SensorEventListener rcs = (IEV3SensorEventListener) contextState;
        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor_1,test_value_s1,0, SensorMessages.SensorMode_.DISTANCE));
        rcs.handleSensorEvent(EV3PortIDs.PORT_2,new EV3SensorEvent(testSensor_2,test_value_s2,0, SensorMessages.SensorMode_.DISTANCE));

        Distance distance_s1 = new Distance(test_distance, EV3PortIDs.PORT_1);
        IConstraint eq_s1 = new EQ(distance_s1);

        Distance distance_s2 = new Distance(test_distance, EV3PortIDs.PORT_2);
        IConstraint eq_s2 = new EQ(distance_s2);
        IConstraint and = new AND(eq_s1,eq_s2);
        boolean result_s1 = ((IComparator) eq_s1).evaluate(contextState);
        boolean result_s2 = ((IComparator) eq_s2).evaluate(contextState);

        assertTrue(result_s1);
        assertFalse(result_s2);
        assertFalse(((AbstractLogicOperator)and).evaluate(result_s1,result_s2));


        rcs.handleSensorEvent(EV3PortIDs.PORT_1,new EV3SensorEvent(testSensor_1,test_distance,0, SensorMessages.SensorMode_.DISTANCE));
        rcs.handleSensorEvent(EV3PortIDs.PORT_2,new EV3SensorEvent(testSensor_2,test_distance,0, SensorMessages.SensorMode_.DISTANCE));
        result_s1 = ((IComparator) eq_s1).evaluate(contextState);
        result_s2 = ((IComparator) eq_s2).evaluate(contextState);
        assertTrue(result_s1);
        assertTrue(result_s2);
        assertTrue(((AbstractLogicOperator)and).evaluate(result_s1,result_s2));
    }

    @Test
    public void testTimeExpiredTrue_Seconds() throws InterruptedException {
        float time_val_ms = 2000;
        long time_val_sec = 2;

        State testState = new State("test");
        testState.activate();

        RobotContextStateListener rcs = new RobotContextStateListener();
        StartCondition.getInstance().setStateActiveTime(1);

        Seconds sec_prop = new Seconds(time_val_sec);
        sec_prop.setSource(testState);

        TimeExpired time_sec = new TimeExpired(sec_prop);
        TimeEvent time_event = new TimeEvent(time_val_ms,testState);
        rcs.handleTimeEvent(time_event);

        Thread.sleep(2200);

        assertTrue(time_sec.evaluate(rcs));

    }

    @Test
    public void testTimeExpiredFalse_Seconds() throws InterruptedException {
        float time_val_ms = 2000;
        long time_val_sec = 2;
        RobotContextStateListener rcs = new RobotContextStateListener();
        StartCondition.getInstance().setStateActiveTime(1);
        State testState = new State("test");
        testState.activate();

        Seconds sec_prop = new Seconds(time_val_sec);
        TimeExpired time_sec = new TimeExpired(sec_prop);
        TimeEvent time_event = new TimeEvent(time_val_ms,testState);

        rcs.getTimeEvents().clear();
        rcs.handleTimeEvent(time_event);

        StartCondition.getInstance().setStateActiveTime(1);
        Thread.sleep(1000);

        assertFalse(time_sec.evaluate(rcs));
    }

    @Test
    public void testTimeExpiredTrue_Millis() throws InterruptedException {
        long time_val_ms = 2000;
        long time_val_sec = 2;

        State testState = new State("test");
        testState.activate();

        RobotContextStateListener rcs = new RobotContextStateListener();
        StartCondition.getInstance().setStateActiveTime(1);

        Milliseconds sec_prop = new Milliseconds(time_val_ms);
        sec_prop.setSource(testState);

        TimeExpired time_sec = new TimeExpired(sec_prop);
        TimeEvent time_event = new TimeEvent(time_val_ms,testState);
        rcs.handleTimeEvent(time_event);

        Thread.sleep(2200);

        assertTrue(time_sec.evaluate(rcs));

    }

    @Test
    public void testTimeExpiredFalse_Millis() throws InterruptedException {
        long time_val_ms = 2000;
        long time_val_sec = 2;
        RobotContextStateListener rcs = new RobotContextStateListener();
        StartCondition.getInstance().setStateActiveTime(1);
        State testState = new State("test");
        testState.activate();

        Milliseconds sec_prop = new Milliseconds(time_val_ms);
        TimeExpired time_sec = new TimeExpired(sec_prop);
        TimeEvent time_event = new TimeEvent(time_val_ms,testState);

        rcs.getTimeEvents().clear();
        rcs.handleTimeEvent(time_event);

        StartCondition.getInstance().setStateActiveTime(1);
        Thread.sleep(1000);

        assertFalse(time_sec.evaluate(rcs));
    }
}
