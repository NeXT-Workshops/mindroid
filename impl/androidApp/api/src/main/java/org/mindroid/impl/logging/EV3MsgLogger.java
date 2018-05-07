package org.mindroid.impl.logging;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.brick.*;
import org.mindroid.common.messages.display.ClearDisplayMessage;
import org.mindroid.common.messages.display.DrawStringMessage;
import org.mindroid.common.messages.display.HelloDisplayMessage;
import org.mindroid.common.messages.led.SetStatusLightMessage;
import org.mindroid.common.messages.motor.*;
import org.mindroid.common.messages.motor.synchronization.*;
import org.mindroid.common.messages.sensor.*;
import org.mindroid.common.messages.sound.BeepMessage;
import org.mindroid.common.messages.sound.SoundVolumeMessage;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class EV3MsgLogger extends Handler implements IEV3MessageVisitor {

    private final Logger logger;
    private String preLogText = "";
    private static int msgID = 0;
    /**
     *
     * @param logger - logger to log the rcvd message
     * @param preLogText str concatinated before the log message
     */
    public EV3MsgLogger(Logger logger, String preLogText){
        this.logger = logger;
        this.preLogText = preLogText;
        logger.addHandler(this);
    }

    public EV3MsgLogger(Logger logger){
        this.logger = logger;
    }

    /**
     * Returns the preLogText. Adds the MSG ID ot the in front of it.
     * @return [MSG_ID] + PreLogText
     */
    public String getPreLogText() {
        return "["+(msgID++)+"] "+preLogText;
    }

    @Override
    public void visit(BeepMessage beepMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("BeepMessage: beepType = ").append(beepMessage.getBeep()).toString());
    }

    @Override
    public void visit(SoundVolumeMessage soundVolumeMessag) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("SoundVolumeMessage: volume = ").append(soundVolumeMessag.getVolume()).toString());
    }

    @Override
    public void visit(ChangeSensorModeMessage changeSensorModeMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("ChangeSensorModeMessage: new mode = ").append(changeSensorModeMessage.getNewMode()).toString());
    }

    @Override
    public void visit(HelloSensorMessage helloSensorMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("HelloSensorMessage: msg = ").append(helloSensorMessage.getMsg()).toString());
    }

    @Override
    public void visit(ModeChangedSuccessfullyMessage modeChangedSuccessfullyMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("ModeChangedSuccessfullyMessage: mode = ").append(modeChangedSuccessfullyMessage.getMode()).toString());
    }

    @Override
    public void visit(SensorErrorMessage sensorErrorMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("SensorErrorMessage: error = ").append(sensorErrorMessage.getErrorMsg()).toString());
    }

    @Override
    public void visit(SensorEventMessage sensorEventMessage) {
        /*StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText()).append("SensorEventMessage: ")
            .append("mode= ")
            .append(sensorEventMessage.getSensormode())
            .append("sample = ")
            .append(getFloatArrContent(sensorEventMessage.getSample()));
        */

        // NOT LOGGED
        // logger.log(Level.FINEST,logMsg.toString());
    }

    @Override
    public void visit(SensorStatusMessage sensorStatusMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("SensorStatusMessage: msg = ").append(sensorStatusMessage.getMsg()).toString());
    }

    @Override
    public void visit(SynchronizedOperationMessage synchronizedOperationMessage) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText())
                .append("SynchronizedOperationMessage: msg = ")
                .append(getSyncedOpArrContent(synchronizedOperationMessage.getOperations()));
        logger.log(Level.INFO,logMsg.toString());
    }

    @Override
    public void visit(SynchronizedMotorGroupCreatedMessage synchronizedMotorGroupCreatedMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("SynchronizedMotorGroupCreatedMessage: success=").append(synchronizedMotorGroupCreatedMessage.isSuccess()).toString());
    }

    @Override
    public void visit(SyncedMotorOpCompleteMessage syncedMotorOpCompleteMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("syncedMotorOpCompleteMessage: Synced Motor-Op complete").toString());
    }

    @Override
    public void visit(AccelerationMessage accelerationMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("AccelerationMessage: acceleration = ").append(accelerationMessage.getAcceleration()).toString());
    }

    @Override
    public void visit(BackwardMessage backwardMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("BackwardMessage").toString());
    }

    @Override
    public void visit(FltMessage fltMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("FltMessage").toString());
    }

    @Override
    public void visit(ForwardMessage forwardMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("ForwardMessage").toString());
    }

    @Override
    public void visit(MotorStateMessage motorStateMessage) {
        // NOT LOGGED

        //StringBuilder logMsg = new StringBuilder();
        //logger.log(Level.INFO,logMsg.append(getPreLogText()).append("MotorStateMessage: motorStateMsg = ").append(motorStateMessage.getMotorState().toString()).toString());
    }

    @Override
    public void visit(RotateMessage rotateMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("RotateMessage: angle = ").append(rotateMessage.getAngle()).toString());
    }

    @Override
    public void visit(RotateToMessage rotateToMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("RotateToMessage: rotateToAngle = ").append(rotateToMessage.getAngle()).toString());
    }

    @Override
    public void visit(SetMotorSpeedMessage setMotorSpeedMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("SetMotorSpeedMessage: motorSpeed = ").append(setMotorSpeedMessage.getSpeed()).toString());
    }

    @Override
    public void visit(StopMessage stopMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("StopMessage").toString());
    }

    @Override
    public void visit(SetStatusLightMessage setStatusLightMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("SetStatusLightMessage: statusLight = ").append(setStatusLightMessage.getVal()).toString());
    }

    @Override
    public void visit(ClearDisplayMessage clearDisplayMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("ClearDisplayMessage").toString());
    }

    @Override
    public void visit(DrawStringMessage drawStringMessage) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText())
                .append("DrawStringMessage: str = ").append(drawStringMessage.getStr())
                .append(", x = ").append(drawStringMessage.getX())
                .append(", y = ").append(drawStringMessage.getY())
                .append(", size = ").append(drawStringMessage.getTextsize());
        logger.log(Level.INFO,logMsg.toString());
    }

    @Override
    public void visit(HelloDisplayMessage helloDisplayMessage) {
        StringBuilder logMsg = new StringBuilder();
        logger.log(Level.INFO,logMsg.append(getPreLogText()).append("ClearDisplayMessage").toString());
    }

    @Override
    public void visit(ButtonMessage buttonMessage) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText())
                .append("ButtonMessage: btnID = ").append(buttonMessage.getButtonID())
                .append(", action = ").append(buttonMessage.getButtonAction());
        logger.log(Level.INFO,logMsg.toString());
    }

    @Override
    public void visit(CreateMotorMessage createMotorMessage) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText())
                .append("CreateMotorMessage: port = ").append(createMotorMessage.getPort())
                .append(", motorType = ").append(createMotorMessage.getMotorType());
        logger.log(Level.INFO,logMsg.toString());
    }

    @Override
    public void visit(CreateSensorMessage createSensorMessage) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText())
                .append("CreateSensorMessage: port = ").append(createSensorMessage.getPort())
                .append(", sensorType = ").append(createSensorMessage.getSensorType());
        logger.log(Level.INFO,logMsg.toString());
    }

    @Override
    public void visit(EndpointCreatedMessage endpointCreatedMessage) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText())
                .append("EndpointCreatedMessage: port = ").append(endpointCreatedMessage.getPort())
                .append(", msg = ").append(endpointCreatedMessage.getMsg());
        logger.log(Level.INFO,logMsg.toString());
    }

    @Override
    public void visit(HelloMessage helloMessage) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText())
                .append("HelloMessage: msg = ").append(helloMessage.getMsg());
        logger.log(Level.INFO,logMsg.toString());
    }

    @Override
    public void visit(ResetBrickMessage resetBrickMessage) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText())
                .append("ResetBrickMessage");
        logger.log(Level.INFO,logMsg.toString());
    }

    @Override
    public void visit(CreateSynchronizedMotorsMessage createSynchronizedMotorsMessage) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(getPreLogText())
                .append("CreateSynchronizedMotorsMessage");
        logger.log(Level.INFO,logMsg.toString());
    }

    private String getFloatArrContent(float[] arr){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]).append(",");
        }
        sb.append("}");
        return sb.toString();
    }

    private String getSyncedOpArrContent(SynchronizedMotorOperation[] arr){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i].getOptype()).append(",");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void publish(LogRecord record) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}