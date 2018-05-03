package org.mindroid.impl.sensor;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.common.messages.ILoggable;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.sensor.*;
import org.mindroid.impl.endpoint.ClientEndpointImpl;

import com.esotericsoftware.kryonet.Connection;

import org.mindroid.impl.ev3.EV3PortID;
import org.mindroid.impl.logging.APILoggerManager;
import org.mindroid.impl.logging.EV3MsgLogger;


//TODO Extend Listener Functionality: adding mode; value; and more?
public class EV3SensorEndpoint extends ClientEndpointImpl implements IEV3SensorEndpoint {


    private List<IEV3SensorEventListener> listeners = new ArrayList<>(2);
    //private final EV3SensorManager sensorManager;

    private Sensormode initialMode = null;
    private Sensormode currentMode = null;
    private Sensormode modeToWaitFor = null;

    //True, if the first sensor event is received from the brick and its sensormode is != null
    private boolean isFirstSensEventReceived = false;

    private final EV3SensorEndpoint sensor = this;

    private Sensors sensorType;

    private EV3PortID brick_port;

    // Gets set (true) when the creation on Brick site failed.
    private boolean hasCreationFailed = false;

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private final EV3MsgLogger msgRcvdLogger;
    private final EV3MsgLogger msgSendLogger;


    /**
     *
     * @param ip ip of the brick
     * @param tcpPort port of the sensorendpoint at the brick
     * @param brickTimeout brick timeout
     * @param sensorType sensor type
     * @param brick_port port of the sensor at the brick
     * @param mode sensormode
     */
    public EV3SensorEndpoint(String ip, int tcpPort, int brickTimeout, Sensors sensorType, EV3PortID brick_port, Sensormode mode) {
        super(ip, tcpPort, brickTimeout);
        this.sensorType = sensorType;
        this.brick_port = brick_port;
        this.initialMode = mode;

        //Init Loggers
        APILoggerManager.getInstance().registerLogger(LOGGER);
        msgRcvdLogger = new EV3MsgLogger(LOGGER,"Received FROM Port "+brick_port.getLabel()+" : ");
        msgSendLogger = new EV3MsgLogger(LOGGER,"Send TO Port "+brick_port.getLabel()+" : ");
    }

    @Override
    public void received(Connection connection, final Object object) {
        //Log msg
        if(object instanceof ILoggable){
            ((ILoggable) object).accept(msgRcvdLogger);
        }


        Thread t = new Thread() {
            @Override
            public void run() {
                if (object instanceof SensorEventMessage) {
                    SensorEventMessage eventMsg = (SensorEventMessage) object;
                    EV3SensorEvent sensorevent = new EV3SensorEvent(sensor, eventMsg.getSample(), eventMsg.getTimestamp(), eventMsg.getSensormode());

                    handleSensorEvent(sensorevent);
                    return;
                }

                if (object.getClass() == HelloSensorMessage.class) {
                    System.out.println(((HelloSensorMessage) object).toString());
                    return;
                }

                //Status Message
                if (object.getClass() == SensorStatusMessage.class) {
                    System.out.println("EV3SensorEndpoint: " + ((SensorStatusMessage) object).getMsg()); //+"\n at port: "+((SensorMessages.SensorErrorMessage)object).getPort()
                    return;
                }

                //Error Message
                if (object.getClass() == SensorErrorMessage.class) {
                    SensorErrorMessage msg = (SensorErrorMessage) object;
                    System.out.println("EV3SensorEndpoint: SensorErrorMessage: " + msg.getErrorMsg() + " at Port " + msg.getPort()); //+"\n at port: "+((SensorMessages.SensorErrorMessage)object).getPort()
                    return;
                }

                //Message that sensormode was changed successfully
                if (object.getClass() == ModeChangedSuccessfullyMessage.class) {
                    currentMode = ((ModeChangedSuccessfullyMessage) object).getMode();
                    System.out.println("EV3SensorEndpoint: current Mode is " + currentMode);
                    return;
                }
            }
        };
        t.start();
    }

    @Override
    public void handleSensorEvent(EV3SensorEvent sensorevent) {
        for (IEV3SensorEventListener listener : listeners) {
            //Only handle events which have a valid (not null) sensor mode
            if(sensorevent.getSensorMode() != null) {
                //value = sensorevent.getSample();
                listener.handleSensorEvent(brick_port, sensorevent);
                isFirstSensEventReceived = true;
            }
        }
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        client.stop();
    }

    public void registerListener(IEV3SensorEventListener listener) {
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    /**
     * Unregister the listener
     *
     * @param listener to unregister
     */
    public void unregisterListener(IEV3SensorEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Changes the mode to the given argument
     *
     * @param newMode - the new mode of the sensor
     * @author Alex
     */
    public void changeSensorToMode(Sensormode newMode) {
        ILoggable msg = SensorMessageFactory.changeModeTo(newMode);
        //Log msg
        msg.accept(msgSendLogger);

        client.sendTCP(msg);
    }

    public void disconnectFromEndpoint() {
        client.stop();
    }

    /**
     * True if the Sensor received its first sensor event and the sensormode of the received event is != null.
     *
     * @return true if the first sensor data was received else false
     */
    public boolean isFirstSensEventReceived() {
        return isFirstSensEventReceived;
    }


    public Sensormode getCurrentMode() {
        return currentMode;
    }

    public Sensors getSensorType() {
        return sensorType;
    }

    public void setSensorType(Sensors sensorType) {
        this.sensorType = sensorType;
    }

    public void initSensorMode() {
        if(initialMode != null){
            changeSensorToMode(initialMode);
        }
    }

    public boolean hasCreationFailed() {
        return hasCreationFailed;
    }

    public void setHasCreationFailed(boolean hasCreationFailed) {
        this.hasCreationFailed = hasCreationFailed;
    }
}
