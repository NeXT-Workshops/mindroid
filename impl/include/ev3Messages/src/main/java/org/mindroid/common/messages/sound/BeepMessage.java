package org.mindroid.common.messages.sound;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

/**
 * Message to create a Beep on the Brick
 * @author Torben
 */
public class BeepMessage implements ILoggable {

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }

    public static class Beeptype{
        public static final int SINGLE_BEEP = 1;
        public static final int DOUBLE_BEEP = 2;
        public static final int BEEP_SEQUENCE_DOWNWARDS = 3;
        public static final int BEEP_SEQUENCE_UPWARDS = 4;
        public static final int LOW_BUZZ = 5;
    }

    private int beep;


    public BeepMessage(){

    }

    /**
     *
     * @param beeptype value of {@Link Beeptype}
     */
    public BeepMessage(int beeptype){
        this.beep = beeptype;
    }

    public int getBeep() {
        return beep;
    }

    @Override
    public String toString() {
        return "BeepMessage{" +
                "beep=" + beep +
                '}';
    }


}