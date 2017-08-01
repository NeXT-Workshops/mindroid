package mindroid.common.ev3.server;

import java.io.IOException;
import java.util.Vector;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import com.esotericsoftware.minlog.Log;
import org.mindroid.common.messages.MessageRegistrar;
import org.mindroid.common.messages.NetworkPortConfig;

/**
 * Just a Server with an open port running on the Brick.
 *
 * E.g. Server for SensorEndpoints(Listener)
 *
 */
public class BrickServerImpl implements BrickServer {
	
	protected Server server;
	int tcpPort;
	
	Vector<Connection> clients = new Vector<Connection>();

	private boolean areMessagesRegistered = false;
	
	public BrickServerImpl(int tcpPort){
		this.tcpPort = tcpPort;
		createServer();
		registerMessages(server);
	}

	@Override
	public void addListener(Listener listener){
		server.addListener(listener);
		//listener.registerMessages(server);
	}

	
	
	@Override
	public void createServer() {
		try {
			server = new Server();
			server.start();
			server.bind(tcpPort,tcpPort-NetworkPortConfig.UDP_OFFSET);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void registerMessages(EndPoint kryo) {
		if(!areMessagesRegistered) {
			MessageRegistrar.register(kryo);
			areMessagesRegistered = true;
		}
	}
	
}
