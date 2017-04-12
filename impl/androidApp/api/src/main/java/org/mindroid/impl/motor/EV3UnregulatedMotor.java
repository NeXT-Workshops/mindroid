package org.mindroid.impl.motor;



import org.mindroid.api.motor.UnregulatedMotor;
import org.mindroid.impl.endpoint.ClientEndpointImpl;

import com.esotericsoftware.kryonet.Connection;

import org.mindroid.common.messages.UnregulatedMotorMessages;
import org.mindroid.common.messages.UnregulatedMotorMessages.MotorState;


public class EV3UnregulatedMotor extends ClientEndpointImpl implements UnregulatedMotor {

    private boolean isMoving = false;
    private int power = -1;
    private int tachoCount = -1;
    
    
    EV3UnregulatedMotor(String ip,int tcpPort, int brickTimeout) {
    	super(ip,tcpPort,brickTimeout);       
    }
/*  
	@Override
	protected void registerMessages(Client client) {
        UnregulatedMotorMessages.register(client);
        BrickMessages.register(client);	
	}
*/

    @Override
    public void forward() {
		System.out.println("Local-EV3UnregulatedMotor: forward got called!");
        if (client.isConnected()){
        	client.sendTCP(UnregulatedMotorMessages.forward());
        }
    }

    @Override
    public void backward() {
		System.out.println("Local-EV3UnregulatedMotor: backward got called!");
        if (client.isConnected()){
            client.sendTCP(UnregulatedMotorMessages.backward());
        }
            
    }

    @Override
    public void setSpeed(int power) {
    	if(power > MAX_POWER){
    		power = MAX_POWER;
    	}else if(power < MIN_POWER){
    		power = MIN_POWER;
    	}
    	    	
    	if (client.isConnected()){
            client.sendTCP(UnregulatedMotorMessages.setPower(power));
    	}
            
    }

    @Override
    public void stop() {
        if (client.isConnected()){
            client.sendTCP(UnregulatedMotorMessages.stop());
        }
    }

	@Override
	public void received(Connection connection, Object object) {
		if(object.getClass() == UnregulatedMotorMessages.MotorState.class){
			MotorState ms = (UnregulatedMotorMessages.MotorState) object;
			power = ms.getPower();
			tachoCount = ms.getTachoCount();
			isMoving = ms.isMoving();
		}
		
	}

	@Override
	public boolean isMoving() {
		return isMoving;
	}
	@Override
	public int getPower() {
		return power;
	}
	@Override
	public int getTachoCount() {
		return tachoCount;
	}

	@Override
	public String toString() {
		return "EV3UnregulatedMotor [isMoving=" + isMoving + ", power=" + power + ", tachoCount=" + tachoCount + "]";
	}
	}
