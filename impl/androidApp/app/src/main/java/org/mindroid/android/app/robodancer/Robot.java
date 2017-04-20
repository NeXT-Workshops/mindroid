package org.mindroid.android.app.robodancer;



import android.os.AsyncTask;

import org.mindroid.android.app.SchuelerProjekt.MindroidMain;
import org.mindroid.android.app.SchuelerProjekt.RobotHardwareConfiguration;
import org.mindroid.android.app.acitivites.MainActivity;
import org.mindroid.android.app.acitivites.SettingsActivity;
import org.mindroid.api.robot.IRobodancerConfig;
import org.mindroid.api.robot.IRobotFactory;
import org.mindroid.api.robot.control.IRobotCommandCenter;
import org.mindroid.api.statemachine.IMindroidMain;

import java.io.IOException;

import org.mindroid.api.statemachine.exception.StateAlreadyExsists;
import org.mindroid.impl.robot.RobotFactory;

/**
 * Created by mindroid on 09.12.16.
 */
public class Robot {

    public boolean isConnectedToBrick = false;
    public boolean isConfigurationBuilt = false;

    public boolean isRunning = false;

    private MainActivity main_activity;

    IRobotCommandCenter commandCenter;


    public Robot(MainActivity mainActivity){
        this.main_activity = mainActivity;
    }

    /**
     * TODO Refactor
     */
    public void makeRobot() throws StateAlreadyExsists {
        IRobotFactory roFactory = new RobotFactory();
        IRobodancerConfig config = new RobotHardwareConfiguration(); //TODO Dependency/Linked to SchuelerProjekt
        IMindroidMain mindroid = new MindroidMain(); //TODO Dependency/Linked to SchuelerProjekt

        //Config
        roFactory.setRobotConfig(config);
        roFactory.setBrickIP(Settings.getInstance().ev3IP);
        roFactory.setBrickTCPPort(Settings.getInstance().ev3TCPPort);
        roFactory.setMSGServerIP(Settings.getInstance().serverIP);
        roFactory.setMSGServerTCPPort(Settings.getInstance().serverTCPPort);
        roFactory.setRobotServerPort(Settings.getInstance().robotServerPort);
        roFactory.setRobotID(Settings.getInstance().robotID);

        //Statemachine
        roFactory.addStatemachine(mindroid.getStatemachine());

        //Create Robot
        commandCenter = roFactory.createRobot();
    }

    /**
     *
     * Der EV3 muss auf USB eingestellt werden und eine statische IP zugewiesen bekommen, sowie
     * die Subnetz Maske 255.255.0.0. Den PC-Ausgang des Bricks mit dem Rechner verbinden.
     * ACHTUNG: Bei uns musste für die Verbindung von EV3 und Smartphone die Subnetzmaske wieder auf
     * automatic gestellt werden!!
     *
     */
    public void connectToBrick(){
        new ConnectToBrickTask().execute("Not important string"); //String is not important
    }

    /**
     * Configurates the Robot.
     * Make sure the connection to the brick is established first! Use connectToBrick();
     */
    public void configurateRobot(){
        new InitRobotConfiguration().execute("Not important string"); //String is not important
    }

    /**
     * Run Statemachines of the Robot
     */
    public void startRobot(){
        if(isConnectedToBrick && isConfigurationBuilt) {
            new StartStopRobotTask().execute(true);
        }
        isRunning = false;
    }

    /**
     *
     */
    public void stopRobot(){
        new StartStopRobotTask().execute(false);
    }

    private void checkState(){
        Runnable task = new Runnable(){
            @Override
            public void run(){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                isConnectedToBrick = commandCenter.isConnected();
            }
        };
    }


    public boolean isConnectedToBrick() {
        return isConnectedToBrick;
    }

    public void setConnectedToBrick(boolean connectedToBrick) {
        isConnectedToBrick = connectedToBrick;
    }

    public boolean isConfigurationBuilt() {
        return isConfigurationBuilt;
    }

    public void setConfigurationBuilt(boolean configurationBuilt) {
        isConfigurationBuilt = configurationBuilt;
    }



    // ---------------------------------- ASYNC TASKS ------------------------------------- //
    private class ConnectToBrickTask extends AsyncTask<String,Integer,Boolean> {

        StringBuffer sb = new StringBuffer();


        @Override
        protected void onPreExecute(){
            sb.append("connecting to "+Settings.getInstance().ev3IP+":"+Settings.getInstance().ev3TCPPort+"\n");

            if(isConnectedToBrick){
                this.cancel(true);
            }else{
                main_activity.showProgressDialog("Connecting to Brick","connecting to "+Settings.getInstance().ev3IP+":"+Settings.getInstance().ev3TCPPort+"\n");
            }
        }


        protected Boolean doInBackground(String... str) {
            try {
                commandCenter.connectToBrick();
            } catch (IOException e) {
                sb.append("\n--------------------\n");
                sb.append("Exception: \n").append(e.toString());
                sb.append("\ncaused by: \n").append(e.getCause());
                sb.append("\n--------------------\n");
                main_activity.dismissCurrentProgressDialog();
                main_activity.showAlertDialog("Error",sb.toString());
                e.printStackTrace();

                return false;
            } catch(Exception e){
                main_activity.dismissCurrentProgressDialog();
                main_activity.showAlertDialog("Error",""+e);
                e.printStackTrace();
            }

            boolean result = false;
            try{

                result = commandCenter.isConnected();;
            }catch(Exception e){
                main_activity.dismissCurrentProgressDialog();
                main_activity.showAlertDialog("Error",e.getMessage()+"\n"+e.getCause());
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                sb.append("\nCONNECTION ESTABLISHED!");
            }else{
                sb.append("\nCONNECTION FAILED!");
                main_activity.showAlertDialog("Error","Connection failed!");
            }

            isConnectedToBrick = result;

            main_activity.dismissCurrentProgressDialog();

        }
    }

    /**--------------------------------------------- ASYNC TASK INITIALIZE ROBOT CONFIGURATION ---------------------------------------------**/
    private class InitRobotConfiguration extends AsyncTask<String,Integer,Boolean> {

        StringBuffer sb = new StringBuffer();

        @Override
        protected void onPreExecute(){
            sb.append("Initializing Robot Configuration.. \n");

            if(!isConnectedToBrick){
                this.cancel(true);
            }else{
                main_activity.showProgressDialog("Initializing Robot Configuration",sb.toString());
            }
        }


        protected Boolean doInBackground(String... str) {
            boolean result = false;

            try{

                result = commandCenter.initializeConfiguration();;
            }catch(Exception e){
                main_activity.dismissCurrentProgressDialog();
                main_activity.showAlertDialog("Error",""+e);
                e.printStackTrace();
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Boolean result) {

            if(result){
                sb.append("\nConfiguration complete!");
            }else{
                sb.append("\nConfiguration failed!");

                main_activity.showAlertDialog("Error","Configuration failed!");
            }

            isConfigurationBuilt = result;

            main_activity.dismissCurrentProgressDialog();
        }
    }

    /**--------------------------------------------- ASYNC TASK START/STOP ROBOTS STATEMACHINE ---------------------------------------------**/
    private class StartStopRobotTask extends AsyncTask<Boolean,Integer,Boolean> {

        StringBuffer sb = new StringBuffer();

        @Override
        protected void onPreExecute(){
            if(!isConnectedToBrick && !isConfigurationBuilt){
                this.cancel(true);
            }else{
                main_activity.showProgressDialog("Starting Robot","starting..");
            }
        }

        /**
         *
         * @param start = true => start robot ; start = false => stop robot
         * @return
         */
        protected Boolean doInBackground(Boolean... start) {
            if(start.length>0) {
                if (start[0]) { //True => Start robot, else it should stop the Robot
                    try {
                        commandCenter.startStatemachine("main");//TODO use ID

                        return true;
                    }catch(Exception e){
                        main_activity.dismissCurrentProgressDialog();
                        main_activity.showAlertDialog("Error",""+e);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        commandCenter.stopStatemachine("main");//TODO use ID

                        return false;
                    }catch(Exception e){
                        main_activity.dismissCurrentProgressDialog();
                        main_activity.showAlertDialog("Error",""+e);
                        e.printStackTrace();
                    }
                }
            }else{
                return false;
            }
            return false;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Boolean result) {
            main_activity.dismissCurrentProgressDialog();
            isRunning = result;
        }


    }
}
