package org.mindroid.common.messages.sensor;


import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

/**
 * Gets returned from the SensorEndpoint on Bricks-side, when the
 * Phone-side Sensor-Object connects the first time to the Endpoint.
 *
 * Shows when the Sensor is ready to use.
 *
 * @author Torben
 *
 */
public class HelloSensorMessage implements ILoggable {
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

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
