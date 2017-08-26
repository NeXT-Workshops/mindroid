package org.mindroid.common.messages;

/**
 * Created by Torbe on 01.06.2017.
 */
public class SoundMessageFactory {

    public static class Beeptype{
        public static final int SINGLE_BEEP = 1;
        public static final int DOUBLE_BEEP = 2;
        public static final int BEEP_SEQUENCE_DOWNWARDS = 3;
        public static final int BEEP_SEQUENCE_UPWARDS = 4;
        public static final int LOW_BUZZ = 5;
    }

    /**
     * Creates a message that instructs the EV3 to beep according to the given type
     * @param type the type of beep according to {@link Beeptype}
     * @return the prepared message
     */
    public static BeepMessage createBeepMessage(int type){
        return new BeepMessage(type);
    }

    /**
     * Creates a message that instucts the EV3 to change its volume
     * @param volume volume level (0-100)
     * @return the prepared message
     */
    public static SoundVolumeMessage createVolumeMessage(int volume){
        return new SoundVolumeMessage(volume);
    }


    /**
     * Message to create a Beep on the Brick
     */
    public static class BeepMessage{
        private int beep = -1;

        public BeepMessage(){

        }

        private BeepMessage(int beeptype){
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


    /**
     * Message to set the Volume of the Brick
     */
    public static class SoundVolumeMessage{

        private int volume = 0;

        public SoundVolumeMessage(){

        }

        private SoundVolumeMessage(int volume){
            if(volume < 0){
                this.volume = 0;
            }else if(volume > 100 ){
                this.volume = 100;
            }else {
                this.volume = volume;
            }
        }

        public int getVolume() {
            return volume;
        }

        @Override
        public String toString() {
            return "SoundVolumeMessage{" +
                    "volume=" + volume +
                    '}';
        }
    }

}
