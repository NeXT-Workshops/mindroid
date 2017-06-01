package org.mindroid.common.messages;

/**
 * Created by Torbe on 01.06.2017.
 */
public class SoundMessageFactory {

    //Beeptypes
    public enum Beeptype{
        SINGLE_BEEP,
        DOUBLE_BEEP,
        BEEP_SEQUENCE_DOWNWARDS,
        BEEP_SEQUENCE_UPWARDS,
        LOW_BUZZ;
    }


    public static BeepMessage createBeepMessage(Beeptype type){
        return new BeepMessage(type);
    }



    public static class BeepMessage{



        private Beeptype beep = null;

        private BeepMessage(Beeptype beeptype){
            this.beep = beeptype;
        }

        public Beeptype getBeep() {
            return beep;
        }
    }

}
