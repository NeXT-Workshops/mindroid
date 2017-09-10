package org.mindroid.common.messages.sound;

/**
 * Created by Torbe on 01.06.2017.
 */
public class SoundMessageFactory {



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




}
