package org.mindroid.common.messages.sensor;


import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

public class SensorErrorMessage implements ILoggable {
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

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }

}
