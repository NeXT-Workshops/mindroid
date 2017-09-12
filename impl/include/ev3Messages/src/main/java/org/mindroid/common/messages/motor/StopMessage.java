package org.mindroid.common.messages.motor;

public class StopMessage {

    boolean immidiateReturn;

    public StopMessage(){

    }

    public StopMessage(boolean immidiateReturn){
        this.immidiateReturn = immidiateReturn;
    }

    public boolean isImmidiateReturn() {
        return immidiateReturn;
    }
}
