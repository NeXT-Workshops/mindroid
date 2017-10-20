package mindroid.common.ev3.app;


import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import org.mindroid.common.messages.brick.ButtonMessage;
import org.mindroid.common.messages.brick.ButtonMessageFactory;

/**
 * Observers the Brick-Hardware buttons.
 * Sends message when pressed or released
 */
public class ButtonObserver extends Listener implements KeyListener {

    private Connection conn;

    public ButtonObserver(){
        Button.ESCAPE.addKeyListener(this);
        Button.ENTER.addKeyListener(this);

        Button.LEFT.addKeyListener(this);
        Button.RIGHT.addKeyListener(this);

        Button.UP.addKeyListener(this);
        Button.DOWN.addKeyListener(this);
    }

    /**
     * Sends a Button message
     * @param buttonMessage - msg to send
     */
    private void sendMessage(ButtonMessage buttonMessage){
        if(conn != null && conn.isConnected()){
            conn.sendTCP(buttonMessage);
        }
    }

    // ----------- LEJOS KEY EVENT LISTENER METHODS

    @Override
    public void keyPressed(Key key) {
        ButtonMessage msg = null;
        switch(key.getId()){
            //case Button.ID_ESCAPE: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_ESCAPE,ButtonMessage.ACTION_BUTTON_PRESSED); break;
            case Button.ID_ENTER: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_ENTER,ButtonMessage.ACTION_BUTTON_PRESSED); break;
            case Button.ID_LEFT: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_LEFT,ButtonMessage.ACTION_BUTTON_PRESSED); break;
            case Button.ID_RIGHT: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_RIGHT,ButtonMessage.ACTION_BUTTON_PRESSED); break;
            case Button.ID_UP: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_UP,ButtonMessage.ACTION_BUTTON_PRESSED); break;
            case Button.ID_DOWN: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_DOWN,ButtonMessage.ACTION_BUTTON_PRESSED); break;
            default: msg = null;
        }

        if(msg != null){
            sendMessage(msg);
        }
    }

    @Override
    public void keyReleased(Key key) {
        ButtonMessage msg = null;
        switch(key.getId()){
            //case Button.ID_ESCAPE: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_ESCAPE,ButtonMessage.ACTION_BUTTON_RELEASED); break;
            case Button.ID_ENTER: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_ENTER,ButtonMessage.ACTION_BUTTON_RELEASED); break;
            case Button.ID_LEFT: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_LEFT,ButtonMessage.ACTION_BUTTON_RELEASED); break;
            case Button.ID_RIGHT: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_RIGHT,ButtonMessage.ACTION_BUTTON_RELEASED); break;
            case Button.ID_UP: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_UP,ButtonMessage.ACTION_BUTTON_RELEASED); break;
            case Button.ID_DOWN: msg = ButtonMessageFactory.createButtonMessage(ButtonMessage.ID_BUTTON_DOWN,ButtonMessage.ACTION_BUTTON_RELEASED); break;
            default: msg = null;
        }

        if(msg != null){
            sendMessage(msg);
        }
    }

    //---------- KRYO LISTENER METHODS
    @Override
    public void connected(Connection conn){
        this.conn = conn;
    }

    @Override
    public void disconnected(Connection conn){
        this.conn = null;
    }


}
