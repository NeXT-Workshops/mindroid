package org.mindroid.common.messages.sound;

/**
 * Message to set the Volume of the Brick
 */
public class SoundVolumeMessage{

    private int volume = 0;

    public SoundVolumeMessage(){

    }

    public SoundVolumeMessage(int volume){
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
