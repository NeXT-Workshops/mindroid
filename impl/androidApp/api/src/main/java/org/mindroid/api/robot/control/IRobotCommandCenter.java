package org.mindroid.api.robot.control;

import org.mindroid.api.BasicAPI;
import org.mindroid.api.IImplStateListener;
import org.mindroid.impl.exceptions.BrickIsNotReadyException;
import org.mindroid.impl.exceptions.PortIsAlreadyInUseException;

import java.io.IOException;

/**
 * Created by torben on 02.03.2017.
 */

public interface IRobotCommandCenter {

    // ------ Setup Controlling ------

    /**
     * Connect the Phone to the Brick
     * @throws IOException - If connection error occurs
     */
    void connectToBrick() throws IOException;

    /**
     * Disconnect Phone from the Brick.
     */
    void disconnectFromBrick();

    /**
     * Returns the Connection-State of the Phone to Brick Connection
     * @return true if connection is established
     */
    boolean isConnectedToBrick();

    /**
     * Initializes the Configuration of your Robot.
     * Brick gets the Information of the Configuration of the Robot and set the Endpoints up.
     * @return true if the initializeation is complete
     * @throws BrickIsNotReadyException - thrown if an error on the Brick Connection occurred
     * @throws PortIsAlreadyInUseException - If you tried to initialize a brick-Hardware port twice.
     */
    boolean initializeConfiguration() throws BrickIsNotReadyException, PortIsAlreadyInUseException; //TODO initializeConfiguration()

    /**
     * Aborts the configuration process
     */
    void abortConfiguration();

    /**
     * Returns if the Configuration is complete
     * @return true - if Configuration has finished and is complete.
     */
    boolean isConfigurated();

    /**
     * Returns true if the messenger-client is connected to the Message server.
     * @return true if connected
     */
    boolean isMessengerConnected();

    /**
     *
     * Connects the Messenger-client to the Message Server.
     * @param msgServerIP - ip of the message server
     * @param msgServerTCPPort - port of the message server
     *
     * @return true if connection was successful
     */
    boolean connectMessenger(String msgServerIP, int msgServerTCPPort);

    /**
     * Disconnects the Messenger-Client from the server.
     */
    void disconnectMessenger();

    /**
     * Add an implementation to the CommandCenter
     *
     * @param implementation - an implementation
     */
    void addImplementation(BasicAPI implementation);

    /**
     * Starts an Implementation Imperative/Statemachine identified by the given ID.
     *
     * @param id of the implementation
     * @param IImplStateListener gets information about the current state of the implementation, provided by the executor {@link org.mindroid.api.IExecutor}
     */
    void startImplementation(String id, IImplStateListener IImplStateListener);

    /**
     * Stops the currently Running implementation
     */
    void stopImplementation();
}
