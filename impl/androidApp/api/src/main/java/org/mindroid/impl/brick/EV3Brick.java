package org.mindroid.impl.brick;

import java.io.IOException;

import org.mindroid.api.brick.Brick;
import org.mindroid.api.ev3.EV3StatusLightColor;
import org.mindroid.api.ev3.EV3StatusLightInterval;
import org.mindroid.api.robot.control.IBrickControl;
import org.mindroid.common.messages.brick.BrickMessagesFactory;
import org.mindroid.common.messages.led.StatusLightMessageFactory;
import org.mindroid.common.messages.sound.BeepMessage;
import org.mindroid.common.messages.sound.SoundMessageFactory;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.motor.EV3MotorManager;
import org.mindroid.impl.sensor.EV3SensorManager;


/**
 *
 * Brick controller class.
 * Used to control the brick.
 */
public class EV3Brick extends Brick {
    /** Manager classes for endpoints */
    final EV3MotorManager motorManager;
    final EV3SensorManager sensorManager;
    private EV3Display display;

    private final EV3BrickEndpoint brickEndpoint;

	/**
	 *
	 * @param brickEndpoint - brick Endpoint capsulates the connection to the brick.
	 */
	public EV3Brick (EV3BrickEndpoint brickEndpoint) {
		this.brickEndpoint = brickEndpoint;

        motorManager 	= new EV3MotorManager(brickEndpoint);
        sensorManager 	= new EV3SensorManager(brickEndpoint);

		BrickButtonProvider.getInstance().addButton(new EV3Button(Button.ENTER));
		BrickButtonProvider.getInstance().addButton(new EV3Button(Button.LEFT));
		BrickButtonProvider.getInstance().addButton(new EV3Button(Button.RIGHT));
		BrickButtonProvider.getInstance().addButton(new EV3Button(Button.UP));
		BrickButtonProvider.getInstance().addButton(new EV3Button(Button.DOWN));

		display = new EV3Display();

		/** Add Listeners to Client **/
		brickEndpoint.client.addListener(sensorManager);
		brickEndpoint.client.addListener(motorManager);
		brickEndpoint.client.addListener(display);

		//Set Client to IMotor-/Sensormanager
		sensorManager.setBrickClient(brickEndpoint.client);
		motorManager.setBrickClient(brickEndpoint.client);
    }


    public boolean connect() {
		try {
			return brickEndpoint.connect();
		} catch (IOException e) {
			ErrorHandlerManager.getInstance().handleError(e,this.getClass(),e.getMessage());
		}
		return false;
	}

	/**
	 * Disconnects all open Connections to the Brick!
	 */
	public void disconnect(){
		brickEndpoint.disconnect();
		getSensorManager().disconnectSensors();
		getMotorManager().disconnectMotors();
	}

	/**
	 * returns true if connection to Brick is established
	 * @return
	 */
    public boolean isConnected(){
    	return brickEndpoint.isConnected();
    }
    
    /**
     * Returns true if connection to Brick is established and also ready to receive Commands
     * @return
     */
    public boolean isBrickReady(){
    	return brickEndpoint.isBrickReady();
    }

	@Override
	public void resetBrickState() {
    	//Resets the Bricks Display,LED,SoundVolume
		brickEndpoint.sendTCPMessage(BrickMessagesFactory.createResetBrickMsg());
	}

	public EV3MotorManager getMotorManager() {
		return motorManager;
	}

	public EV3SensorManager getSensorManager() {
		return sensorManager;
	}

	private EV3Display getDisplay() {
		return display;
	}

	public void setDisplay(EV3Display display) {
		this.display = display;
	}

	public void setConnectionInformation(String ip, int tcpPort){
		brickEndpoint.EV3Brick_IP = ip;
		brickEndpoint.EV3Brick_PORT = tcpPort;
	}

	public String getConnectionInformation(){
		return new StringBuffer().append(brickEndpoint.EV3Brick_IP).append(":").append(brickEndpoint.EV3Brick_PORT).toString();
	}




	//-------------- Status Light Operations ----------------
	@Override
	public void setEV3StatusLight(EV3StatusLightColor color, EV3StatusLightInterval interval) {
		if(isBrickReady()){
			brickEndpoint.sendTCPMessage(StatusLightMessageFactory.createSetStatusLightMessage((color.getValue()+3*interval.getValue())));
		}else{
			logError(new Exception("Brick is not ready yet. Check Brick connection!"));
		}
	}

	@Override
	public void setLEDOff() {
		if(isBrickReady()){
			brickEndpoint.sendTCPMessage(StatusLightMessageFactory.createSetStatusLightMessage(0));
		}else{
			logError(new Exception("Brick is not ready yet. Check Brick connection!"));
		}
	}

	private void logError(Exception e) {
		ErrorHandlerManager.getInstance().handleError(e,EV3BrickEndpoint.class,e.getMessage());
	}

	//-------------- SOUND Operations ----------------
	public void setVolume(int volume){
		if(isBrickReady()){
			brickEndpoint.sendTCPMessage(SoundMessageFactory.createVolumeMessage(volume));
		}else{
			logError(new Exception("Brick is not ready yet. Check Brick connection!"));
		}
	}

	public void singleBeep(){
		if(isBrickReady()){
			brickEndpoint.sendTCPMessage(SoundMessageFactory.createBeepMessage(BeepMessage.Beeptype.SINGLE_BEEP));
		}else{
			logError(new Exception("Brick is not ready yet. Check Brick connection!"));
		}
	}

	public void doubleBeep() {
		if(isBrickReady()){
			brickEndpoint.sendTCPMessage(SoundMessageFactory.createBeepMessage(BeepMessage.Beeptype.DOUBLE_BEEP));
		}else{
			logError(new Exception("Brick is not ready yet. Check Brick connection!"));
		}
	}

	public void buzz() {
		if(isBrickReady()){
			brickEndpoint.sendTCPMessage(SoundMessageFactory.createBeepMessage(BeepMessage.Beeptype.LOW_BUZZ));
		}else{
			logError(new Exception("Brick is not ready yet. Check Brick connection!"));
		}
	}

	public void beepSequenceDown() {
		if(isBrickReady()){
			brickEndpoint.sendTCPMessage(SoundMessageFactory.createBeepMessage(BeepMessage.Beeptype.BEEP_SEQUENCE_DOWNWARDS));
		}else{
			logError(new Exception("Brick is not ready yet. Check Brick connection!"));
		}
	}
	public void beepSequenceUp() {
		if(isBrickReady()){
			brickEndpoint.sendTCPMessage(SoundMessageFactory.createBeepMessage(BeepMessage.Beeptype.BEEP_SEQUENCE_UPWARDS));
		}else{
			logError(new Exception("Brick is not ready yet. Check Brick connection!"));
		}
	}

	@Override
	public void clearDisplay() {
		getDisplay().clearDisplay();
	}

	@Override
	public void drawString(String str,Textsize textsize, int posX, int posY) {
		getDisplay().drawString(str,textsize,posX,posY);
	}

	@Override
	public void drawImage(String str) {
		getDisplay().drawImage(str);
	}

}