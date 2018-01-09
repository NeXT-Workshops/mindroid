package org.mindroid.api.robot.control;

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
     * Connects the Messenger-client to the Message Server.
     * @param msgServerIP - ip of the message server
     * @param msgServerTCPPort - port of the message server
     */
    boolean connectMessenger(String msgServerIP, int msgServerTCPPort);

    /**
     * Disconnects the Messenger-Client from the server.
     */
    void disconnectMessenger();


    //public IRobotState getRobotState(); TODO: May return the current State of the robot

    // ------ Controlling of the Statemachine Engine ------
    /**
     * Start a statemachine with the given ID.
     * @param id - unique id identifiying the statemachine to run
     */
    void startStatemachine(String id);

    /**
     * Stop a statemachine with the given ID.
     * @param id - unique id identifiying the statemachine to stop
     */
    void stopStatemachine(String id);

    /**
     * Get all registered Statemachines by ther IDs
     * @return String of the Statemachine IDs
     */
    String[] getStatemachines();

    // ------ Controlling the Imperative Engine ------

    /**
     * Starts executing the Implementation with the given ID
     * @param id - unique id identifiying the imperative implementation to start
     */
    void startImperativeImplemenatation(String id);

    /**
     * Stops executing the Implementation with the given ID
     * @param id - unique id identifiying the imperative implementation to stop
     */
    void stopImperativeImplementation(String id);

    /**
     * Returns all registered Imperative Implementations IDs
     * @return Array of IDs of registered Imperative Implementations
     */
    String[] getImperativeImplementations();

}
