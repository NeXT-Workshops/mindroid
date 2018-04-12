package org.mindroid.common.messages.brick;


import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;
import org.mindroid.common.messages.hardware.Motors;

public class CreateMotorMessage implements ILoggable {
    String port;
    Motors motorType;
    int networkPort;


    public CreateMotorMessage(){

    }


    public String getPort() {
        return port;
    }


    public void setPort(String port) {
        this.port = port;
    }


    public Motors getMotorType() {
        return motorType;
    }


    public void setMotorType(Motors motorType) {
        this.motorType = motorType;
    }


    public int getNetworkPort() {
        return networkPort;
    }


    public void setNetworkPort(int networkPort) {
        this.networkPort = networkPort;
    }

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}