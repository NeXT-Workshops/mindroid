package org.mindroid.common.messages.brick;


import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class EndpointCreatedMessage implements ILoggable {
    String msg;
    boolean success = false;
    String port;
    boolean sensor;
    boolean motor;

    public EndpointCreatedMessage(){

    }

    public EndpointCreatedMessage(boolean success, String port, String msg,boolean isSensor,boolean isMotor ){
        this.msg = msg;
        this.success = success;
        this.port = port;
        this.sensor = isSensor;
        this.motor = isMotor;
    }
    public String getMsg() {
        return msg;
    }

    public boolean isSuccess(){
        return success;
    }

    public String getPort() {
        return port;
    }

    public boolean isSensor() {
        return sensor;
    }

    public boolean isMotor() {
        return motor;
    }

    @Override
    public String toString() {
        return "EndpointCreatedMessage [msg=" + msg + ", success=" + success + ", port=" + port + ", sensor="
                + sensor + ", motor=" + motor + "]";
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}