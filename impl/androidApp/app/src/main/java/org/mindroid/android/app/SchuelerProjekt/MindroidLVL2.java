package org.mindroid.android.app.SchuelerProjekt;

import org.mindroid.api.LVL2API;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;

/**
 * Created by Torbe on 03.05.2017.
 * Easiest programming level.
 */
public class MindroidLVL2 extends LVL2API {


    public MindroidLVL2() throws StateAlreadyExists {

    }

    @Override
    public void run() {
        //Example Wall-Ping-Pong
        //messenger.sendMessage(IMessenger.SERVER_LOG,"Imperative Implementation is running!");
        forward();
        while(!isCollisionDetected()){

        }
        //backward 5cm
        backward(5);

        //turn around
        turnLeft(180);

        //Back to start
        forward();

        led.green();
        led.blinking();

        while(!hasLineDetected()){

        }
        //At start again
        stop();

    }


}
