package org.mindroid.android.app.SchuelerProjekt;

import org.mindroid.api.LVL2API;
import org.mindroid.api.communication.IMessenger;
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
        if(messenger!=null) {
            messenger.sendMessage(IMessenger.SERVER_LOG, "Imperative Implementation is running!");
        }

        while (true) {
            forward();
            while (!isCollisionDetected()) {
                delay(300);
            }
            backward();
            delay(1200);
            turnLeftTime(1600);
        }

    }






    private void backward(int val){
//TODO
    }


}
