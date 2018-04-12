package org.mindroid.common.messages.sound;

import org.mindroid.common.messages.IEV3MessageVisitor;
import org.mindroid.common.messages.ILoggable;

/**
 * Message to set the Volume of the Brick
 */
public class SoundVolumeMessage implements ILoggable {

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

    @Override
    public void accept(IEV3MessageVisitor visitor) {
        visitor.visit(this);
    }
}
