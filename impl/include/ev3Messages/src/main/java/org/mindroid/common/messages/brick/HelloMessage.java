package org.mindroid.common.messages.brick;

/**
 * Hello message gets send by the Brick when connection is established
 * and the Brick is ready to receive commands.
 *
 * @author Torben Unzicker
 */
public class HelloMessage{
    final String msg;

    public HelloMessage(){
        this("");
    }

    HelloMessage(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "HelloMessage [msg=" + msg + "]";
    }


}