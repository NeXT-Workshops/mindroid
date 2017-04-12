package mindroid.common.ev3.server;

import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

public interface BrickServer {	
	

	public void createServer();
	public void addListener(Listener listener);
	void registerMessages(EndPoint kryo);
}
