package org.mindroid.common.messages;

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

public interface IEV3MessageVisitor {
    void visit(BeepMessage beepMessage);

    void visit(SoundVolumeMessage soundVolumeMessag);

    void visit(ChangeSensorModeMessage changeSensorModeMessage);

    void visit(HelloSensorMessage helloSensorMessage);

    void visit(ModeChangedSuccessfullyMessage modeChangedSuccessfullyMessage);

    void visit(SensorErrorMessage sensorErrorMessage);

    void visit(SensorEventMessage sensorEventMessage);

    void visit(SensorStatusMessage sensorStatusMessage);

    void visit(SynchronizedOperationMessage synchronizedOperationMessage);

    void visit(SynchronizedMotorGroupCreatedMessage synchronizedMotorGroupCreatedMessage);

    void visit(SyncedMotorOpCompleteMessage syncedMotorOpCompleteMessage);

    void visit(AccelerationMessage accelerationMessage);

    void visit(BackwardMessage backwardMessage);

    void visit(FltMessage fltMessage);

    void visit(ForwardMessage forwardMessage);

    void visit(MotorStateMessage motorStateMessage);

    void visit(RotateMessage rotateMessage);

    void visit(RotateToMessage rotateToMessage);

    void visit(SetMotorSpeedMessage setMotorSpeedMessage);

    void visit(StopMessage stopMessage);

    void visit(SetStatusLightMessage setStatusLightMessage);

    void visit(ClearDisplayMessage clearDisplayMessage);

    void visit(DrawStringMessage drawStringMessage);

    void visit(HelloDisplayMessage helloDisplayMessage);

    void visit(ButtonMessage buttonMessage);

    void visit(CreateMotorMessage createMotorMessage);

    void visit(CreateSensorMessage createSensorMessage);

    void visit(EndpointCreatedMessage endpointCreatedMessage);

    void visit(HelloMessage helloMessage);

    void visit(ResetBrickMessage resetBrickMessage);

    void visit(CreateSynchronizedMotorsMessage createSynchronizedMotorsMessage);
}
