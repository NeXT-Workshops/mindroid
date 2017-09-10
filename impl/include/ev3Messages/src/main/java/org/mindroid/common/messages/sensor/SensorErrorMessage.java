package org.mindroid.common.messages.sensor;


public class SensorErrorMessage{
    private String errorMsg;
    private String port;

    public SensorErrorMessage(){

    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "SensorErrorMessage [errorMsg=" + errorMsg + ", port=" + port + "]";
    }



}
