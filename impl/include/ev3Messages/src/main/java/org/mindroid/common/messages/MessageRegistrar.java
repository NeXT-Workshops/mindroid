package org.mindroid.common.messages;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Contains method to register all necessary Classes on a kryo-connection to communicate with the EV3-brick.
 * 
 * @author mindroid
 *
 */
public class MessageRegistrar {

	
	public static void register(EndPoint endPoint){
		Kryo kryo = endPoint.getKryo();
		
		//BrickMessages	
		kryo.register(BrickMessages.class);
		kryo.register(BrickMessages.CreateMotorMessage.class);
		kryo.register(BrickMessages.CreateSensorMessage.class);
		kryo.register(BrickMessages.CreateDisplayMessage.class);
		kryo.register(BrickMessages.HelloMessage.class);
		kryo.register(BrickMessages.EndpointCreatedMessage.class);

		//DisplayMessageFactory
		kryo.register(DisplayMessageFactory.DrawStringMsg.class);
		kryo.register(DisplayMessageFactory.HelloDisplay.class);
		kryo.register(DisplayMessageFactory.ClearDisplayMsg.class);

		//RegulatedMotorMessages
		kryo.register(RegulatedMotorMessages.BackwardMsg.class);
		kryo.register(RegulatedMotorMessages.ForwardMsg.class);
		kryo.register(RegulatedMotorMessages.SetSpeedMsg.class);
		kryo.register(RegulatedMotorMessages.StopMsg.class);
		kryo.register(RegulatedMotorMessages.RotateMessage.class);
		kryo.register(RegulatedMotorMessages.RotateToMessage.class);
		kryo.register(RegulatedMotorMessages.MotorState.class);

		//UnregulatedMotorMessages
		kryo.register(UnregulatedMotorMessages.BackwardMsg.class);
		kryo.register(UnregulatedMotorMessages.ForwardMsg.class);
		kryo.register(UnregulatedMotorMessages.SetPowerMsg.class);
		kryo.register(UnregulatedMotorMessages.StopMsg.class);
		kryo.register(UnregulatedMotorMessages.MotorState.class);
	
		//SensorMessages
		kryo.register(SensorMessages.SensorEventMsg.class);
		kryo.register(SensorMessages.ChangeSensorModeMsg.class);
		kryo.register(SensorMessages.SensorMode_.class);
		kryo.register(SensorMessages.modeChangedSuccessfullyMsg.class);
		kryo.register(SensorMessages.SensorErrorMessage.class);
		kryo.register(SensorMessages.HelloSensorMessage.class);
		kryo.register(SensorMessages.StatusMessage.class);
	
		//StatusLightMessages
		kryo.register(StatusLightMessages.SetStatusLightMsg.class);

		//Sound Message
		kryo.register(SoundMessageFactory.BeepMessage.class);
		kryo.register(SoundMessageFactory.Beeptype.class);
		kryo.register(SoundMessageFactory.SoundVolumeMessage.class);

		/** ----- Used classes by Message-classes ----- **/
		kryo.register(Sensors.class);
		kryo.register(Motors.class);


		//kryo.register(SensorPort.class);
		//kryo.register(MotorPort.class);
		//kryo.register(Port.class);
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
