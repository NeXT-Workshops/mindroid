package org.mindroid.common.messages.sensor;


/**
 * Gets returned from the SensorEndpoint on Bricks-side, when the
 * Phone-side Sensor-Object connects the first time to the Endpoint.
 *
 * Shows when the Sensor is ready to use.
 *
 * @author Torben
 *
 */
public class HelloSensorMessage{
        private String msg;

        public HelloSensorMessage(){

        }

        public HelloSensorMessage(String msg){
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        @Override
        public String toString() {
            return "HelloSensorMessage [msg=" + msg + "]";
        }
}
