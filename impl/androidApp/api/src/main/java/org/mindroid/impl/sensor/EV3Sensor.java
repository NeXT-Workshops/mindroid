package org.mindroid.impl.sensor;


import java.util.ArrayList;
import java.util.List;

import org.mindroid.api.sensor.IEV3SensorEventListener;
import org.mindroid.common.messages.Sensors;
import org.mindroid.impl.endpoint.ClientEndpointImpl;

import com.esotericsoftware.kryonet.Connection;

import org.mindroid.common.messages.SensorMessages;
import org.mindroid.common.messages.SensorMessages.SensorMode_;
import org.mindroid.impl.ev3.EV3PortID;


//TODO Extend Listener Functionality: adding mode; value; and more?
public class EV3Sensor extends ClientEndpointImpl {


    private List<IEV3SensorEventListener> listeners = new ArrayList<>(2);
    //private final EV3SensorManager sensorManager;

    private SensorMode_ initialMode = null;
    private SensorMode_ currentMode = null;
    private SensorMode_ modeToWaitFor = null;

    private float value = -1;

    private boolean ready = false;

    private final EV3Sensor sensor = this;

    private Sensors sensorType;
    private EV3PortID brick_port;

    /**
     * @param ip
     * @param tcpPort
     * @param brickTimeout
     */
    public EV3Sensor(String ip, int tcpPort, int brickTimeout,Sensors sensorType,EV3PortID brick_port,SensorMessages.SensorMode_ mode) {
        super(ip, tcpPort, brickTimeout);
        this.sensorType = sensorType;
        this.brick_port = brick_port;
        this.initialMode = mode;
    }
/*
    @Override
	protected void registerMessages(Client client){
        SensorMessages.register(client);
        BrickMessages.register(client);      
	}
*/

    @Override
    public void received(Connection connection, final Object object) {
        //Sensor data updated

        Thread t = new Thread() {
            @Override
            public void run() {
                if (object instanceof SensorMessages.SensorEventMsg) {

                    float sample = ((SensorMessages.SensorEventMsg) object).getSample();
                    long timestamp = ((SensorMessages.SensorEventMsg) object).getTimestamp();
                    EV3SensorEvent sensorevent = new EV3SensorEvent(sensor, sample, timestamp, currentMode);

                    for (IEV3SensorEventListener listener : listeners) {
                        value = sensorevent.getSample();
                        listener.handleSensorEvent(brick_port,sensorevent);
                    }
                    return;
                }

                if (object.getClass() == SensorMessages.HelloSensorMessage.class) {
                    System.out.println(((SensorMessages.HelloSensorMessage) object).toString());
                    ready = true;
                    return;
                }

                //Status Message
                if (object.getClass() == SensorMessages.StatusMessage.class) {
                    System.out.println("EV3Sensor: " + ((SensorMessages.StatusMessage) object).getMsg()); //+"\n at port: "+((SensorMessages.SensorErrorMessage)object).getPort()
                    return;
                }

                //Error Message
                if (object.getClass() == SensorMessages.SensorErrorMessage.class) {
                    SensorMessages.SensorErrorMessage msg = (SensorMessages.SensorErrorMessage) object;
                    System.out.println("EV3Sensor: SensorErrorMessage: " + msg.getErrorMsg() + " at Port " + msg.getPort()); //+"\n at port: "+((SensorMessages.SensorErrorMessage)object).getPort()
                    return;
                }

                //Message that sensormode was changed successfully
                if (object.getClass() == SensorMessages.modeChangedSuccessfullyMsg.class) {
                    currentMode = ((SensorMessages.modeChangedSuccessfullyMsg) object).getMode();
                    System.out.println("EV3Sensor: current Mode is " + currentMode);
                    return;
                }
            }
        };
        t.start();
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        client.stop();
    }

    public void registerListener(IEV3SensorEventListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(IEV3SensorEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Changes the mode to the given argument
     *
     * @param newMode
     * @author Alex
     */
    public void changeSensorToMode(SensorMode_ newMode) {
        client.sendTCP(SensorMessages.changeModeTo(newMode));
    }

    public void disconnectFromEndpoint() {
        client.stop();
    }

    /**
     * True if the Sensor is ready to Use.
     *
     * @return
     */
    public boolean isReady() {
        return ready;
    }


    public float getValue() {
        return value;
    }

    public SensorMode_ getCurrentMode() {
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
}
