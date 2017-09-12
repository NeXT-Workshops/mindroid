package org.mindroid.common.messages;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import org.mindroid.common.messages.brick.*;
import org.mindroid.common.messages.display.*;
import org.mindroid.common.messages.hardware.Motors;
import org.mindroid.common.messages.hardware.Sensormode;
import org.mindroid.common.messages.hardware.Sensors;
import org.mindroid.common.messages.led.SetStatusLightMessage;
import org.mindroid.common.messages.motor.*;
import org.mindroid.common.messages.motor.synchronization.*;
import org.mindroid.common.messages.sensor.*;
import org.mindroid.common.messages.sound.BeepMessage;
import org.mindroid.common.messages.sound.SoundVolumeMessage;

/**
 * Contains method to register all necessary Classes on a kryo-connection to communicate with the EV3-brick.
 * 
 * @author mindroid
 *
 */
public class MessageRegistrar {

	
	public static void register(EndPoint endPoint){
		Kryo kryo = endPoint.getKryo();
		
		//Brick Messages
		kryo.register(CreateMotorMessage.class);
		kryo.register(CreateSensorMessage.class);
		kryo.register(CreateDisplayMessage.class);
		kryo.register(HelloMessage.class);
		kryo.register(EndpointCreatedMessage.class);

		//Display Message
		kryo.register(DrawStringMessage.class);
		kryo.register(HelloDisplayMessage.class);
		kryo.register(ClearDisplayMessage.class);

		//Regulated Motor Messages
		kryo.register(BackwardMessage.class);
		kryo.register(ForwardMessage.class);
		kryo.register(SetMotorSpeedMessage.class);
		kryo.register(StopMessage.class);
		kryo.register(RotateMessage.class);
		kryo.register(RotateToMessage.class);
		kryo.register(MotorState.class);
		kryo.register(MotorStateMessage.class);

		//Synchronized Motor Group Messages
		kryo.register(OperationType.class);
		kryo.register(SynchronizedMotorOperation.class);
		kryo.register(SynchronizedOperationMessage.class);
		kryo.register(CreateSynchronizedMotorsMessage.class);
		kryo.register(SynchronizedMotorGroupCreatedMessage.class);
	
		//Sensor Messages
		kryo.register(SensorEventMessage.class);
		kryo.register(ChangeSensorModeMessage.class);
		kryo.register(Sensormode.class);
		kryo.register(ModeChangedSuccessfullyMessage.class);
		kryo.register(SensorErrorMessage.class);
		kryo.register(HelloSensorMessage.class);
		kryo.register(SensorStatusMessage.class);
	
		//Status Light Messages
		kryo.register(SetStatusLightMessage.class);

		//Sound Message
		kryo.register(BeepMessage.Beeptype.class);
		kryo.register(BeepMessage.class);
		kryo.register(SoundVolumeMessage.class);

		/** ----- Used classes by Message-classes ----- **/
		kryo.register(Sensors.class);
		kryo.register(Motors.class);

		kryo.register(NetworkPortConfig.class);

		//Used Primitives
		kryo.register(String.class);
		
		kryo.register(Boolean.class);
		kryo.register(boolean.class);
		
		kryo.register(float.class);
		kryo.register(float[].class);
		
		kryo.register(Integer.class);
		kryo.register(int.class);
		kryo.register(int[].class);

		kryo.register(long.class);
		kryo.register(long[].class);
	}
}
