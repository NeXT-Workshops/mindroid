package mindroid.common.ev3.endpoints;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import com.esotericsoftware.minlog.Log;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.PortException;
import mindroid.common.ev3.endpoints.motors.ev3.*;

public class MotorEndpoint extends Listener{
	
	public static final long UPDATETIME = 50;

	protected Connection conn;
	AbstractMotor motor;
	
	public MotorEndpoint(AbstractMotor motor){
		this.motor = motor;
		checkMotorState();
	}

	private void checkMotorState(){
		Runnable run = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(UPDATETIME);
					} catch (InterruptedException e) {
						//System.err.println("MotorEndpoint - Thread could not sleep.");
						e.printStackTrace();
					}
					if(conn != null && conn.isConnected() && motor != null){
						switch(motor.getMotortype()){
							case LargeRegulatedMotor:  conn.sendTCP(((LargeRegulatedMotor)motor).getMotorState()); break;
							case MediumRegulatedMotor: conn.sendTCP(((MediumRegulatedMotor)motor).getMotorState()); break;
							case UnregulatedMotor: conn.sendTCP(((EV3UnregulatedMotor)motor).getMotorState()); break;
							default: break;
						}
					}

				}
			}
		};
		new Thread(run).start();
	}
	
	@Override
	public void connected(Connection connection) {
		try {
			conn = connection;
		} catch (PortException e) {
			connection.close();
			e.printStackTrace();
			throw new RuntimeException("Motor port " + motor.getMotorPort().getName() + " already in use.");
		}
	}

	@Override
	public void received(Connection connection, Object msg){		
		if(motor != null && conn != null && motor instanceof MotorMessageListener) {
			//System.out.println("Motor.handleMotorMessage(): "+ msg.toString());
			((MotorMessageListener) motor).handleMotorMessage(msg);
		}
	}

	@Override
	public void disconnected(Connection connection) {
		//System.out.println("MotorEndpoint - Connection disconnected: "+connection.toString());
		motor.getMotor().stop();
		motor.getMotor().flt();
		conn = null;
	}

	public AbstractMotor getMotor() {
		return motor;
	}

	public void setMotor(AbstractMotor motor) {
		this.motor = motor;
	}
}
