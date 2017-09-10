package org.mindroid.common.messages.brick;


public class EndpointCreatedMessage{
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
}