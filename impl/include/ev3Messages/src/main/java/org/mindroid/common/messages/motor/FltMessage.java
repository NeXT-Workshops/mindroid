package org.mindroid.common.messages.motor;

public class FltMessage {

    private boolean immediateRetrun = false;

    public FltMessage(){

    }

    public FltMessage(boolean immediateRetrun) {
        this.immediateRetrun = immediateRetrun;
    }

    public boolean isImmediateRetrun() {
        return immediateRetrun;
    }
}
