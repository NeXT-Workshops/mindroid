package org.mindroid.impl.endpoint;

import java.io.IOException;

import org.mindroid.api.endpoint.ClientEndpoint;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.mindroid.common.messages.MessageRegistrar;
import org.mindroid.common.messages.NetworkPortConfig;

public abstract class ClientEndpointImpl extends Listener implements ClientEndpoint {

    public Client client;
    protected String ip;
    protected int tcpPort;
    protected int brickTimeout;
    
	boolean clientReady = false;;

	private boolean areMessagesRegistered = false;
    
	public ClientEndpointImpl(String ip,int tcpPort, int brickTimeout) {
		this.ip =ip;
		this.tcpPort = tcpPort;
		this.brickTimeout = brickTimeout;

		client = new Client();
		client.start();
		new Thread(client).start();

		client.addListener(this);


		registerMessages(client);
	}
	
	/**
	 * Connect the Client with a Server
	 */
	@Override
	public void connect() {
        try {
        	client.setKeepAliveTCP(10000);
            client.connect(this.brickTimeout, this.ip, this.tcpPort,this.tcpPort-NetworkPortConfig.UDP_OFFSET); 
        } catch (IOException e) {
            System.err.println("[ClientEndpointImpl:connect()] Connection timed out!");
            e.printStackTrace();
            //TODO Properly error handling
        	//throw new EV3Exception("You must have entered a wrong IP.");
        }

        if(client.isConnected()){
        	setClientReady(true);
        }	
	}

    @Override
    public void disconnect(){
        client.close();
    }

    @Override
    public abstract void received (Connection connection, Object object);

	private void registerMessages(Client client) {
		if (!areMessagesRegistered){
			MessageRegistrar.register(client);
			areMessagesRegistered = true;
		}
	}

	@Override
	public void stop(){
		client.stop();
	}
	
	@Override
	public boolean isClientReady() {
		return clientReady;
	}

	public void setClientReady(boolean clientReady) {
		this.clientReady = clientReady;
	}

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        setClientReady(false);
        stop();
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ClientEndpointImpl that = (ClientEndpointImpl) o;

		if (tcpPort != that.tcpPort) return false;
		if (brickTimeout != that.brickTimeout) return false;
		if (client != null ? !client.equals(that.client) : that.client != null) return false;
		return ip != null ? ip.equals(that.ip) : that.ip == null;

	}

	@Override
	public int hashCode() {
		int result = client != null ? client.hashCode() : 0;
		result = 31 * result + (ip != null ? ip.hashCode() : 0);
		result = 31 * result + tcpPort;
		result = 31 * result + brickTimeout;
		return result;
	}

	@Override
	public String toString() {
		return "ClientEndpointImpl{" +
				"client=" + client +
				", ip='" + ip + '\'' +
				", tcpPort=" + tcpPort +
				", brickTimeout=" + brickTimeout +
				", clientReady=" + clientReady +
				", areMessagesRegistered=" + areMessagesRegistered +
				'}';
	}
}
