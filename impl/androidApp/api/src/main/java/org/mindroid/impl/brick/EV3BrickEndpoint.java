package org.mindroid.impl.brick;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.mindroid.common.messages.MessageRegistrar;
import org.mindroid.common.messages.NetworkPortConfig;
import org.mindroid.common.messages.brick.ButtonMessage;
import org.mindroid.common.messages.brick.HelloMessage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class EV3BrickEndpoint extends Listener {

    public final static int BRICK_TIMEOUT = 10000;
    protected final Client client;
    public String EV3Brick_IP;
    public int EV3Brick_PORT;

    private Connection conn = null;
    private boolean areMessagesRegistered = false;

    /** Brick is ready for commands - will be set true when hello-msg from brick is received!**/
    private boolean readyForCommands = false;

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public EV3BrickEndpoint(final String ev3Brick_IP, int ev3Brick_PORT){
        this.EV3Brick_IP = ev3Brick_IP;
        this.EV3Brick_PORT = ev3Brick_PORT;

        client = new Client();
        client.start();
        new Thread(client).start(); //Neccessary to keep connection alive!

        /** Add Listeners to Client **/
        client.addListener(this);

        registerMessages(client);
    }

    /**
     * Registers Messages at the Kryoconnection, if not done so already.
     * @param client
     */
    private void registerMessages(Client client) {
        if (!areMessagesRegistered){
            MessageRegistrar.register(client);
            areMessagesRegistered = true;
        }
    }

    protected boolean connect() throws IOException {
        client.setKeepAliveTCP(10000);
        client.connect(BRICK_TIMEOUT, EV3Brick_IP, EV3Brick_PORT,EV3Brick_PORT- NetworkPortConfig.UDP_OFFSET);
        return client.isConnected();
    }

    /**
     * Disconnects all open Connections to the Brick!
     */
    protected void disconnect(){
        client.close();
    }

    /**
     * returns true if connection to Brick is established
     * @return returns true if the client is connected
     */
    public boolean isConnected(){
        return client.isConnected();
    }

    /**
     * Returns true if connection to Brick is established and also ready to receive Commands
     * @return true if the brick is ready else false
     */
    public boolean isBrickReady(){
        return (readyForCommands && isConnected());
    }


    @Override
    public void received(Connection connection, Object object){
        LOGGER.log(Level.INFO,"Received Message: "+object.toString());


        /** First answer from the Brick if the Connection is established **/
        if(object.getClass() == HelloMessage.class){
            readyForCommands = true;
            conn = connection;
            System.out.println("Local-EV3Brick: "+ object.toString());
        }

        if(object.getClass() == ButtonMessage.class){
            ButtonMessage msg = (ButtonMessage) object;
            EV3Button button;
            button = BrickButtonProvider.getInstance().getButton(BrickButtonProvider.getMappedID(msg.getButtonID()));
            if(button != null){
                if(msg.getButtonAction() == ButtonAction.RELEASED.getValue()){
                    button.setIsPressed(false);
                    return;
                }

                if(msg.getButtonAction() == ButtonAction.PRESSED.getValue()){
                    button.setIsPressed(true);
                    return;
                }
            }
        }

    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        client.close();
        readyForCommands = false;
    }

    protected void sendTCPMessage(Object msg){
        conn.sendTCP(msg);
    }
}
