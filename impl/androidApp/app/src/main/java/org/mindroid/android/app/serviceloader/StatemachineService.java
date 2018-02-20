package org.mindroid.android.app.serviceloader;

import android.content.pm.PackageManager;
import org.mindroid.api.StatemachineAPI;
import org.mindroid.api.statemachine.IMindroidMain;
import org.mindroid.impl.statemachine.StatemachineCollection;

import java.util.*;

/**
 * Created by torben on 14.08.2017.
 */
//TODO Merge StatemachineIimpl and ImperativeImpl service class, only look for BasicAPI, the rest will be done in the background
public class StatemachineService {
    private static StatemachineService ourInstance = new StatemachineService();

    private ArrayList<StatemachineAPI> statemachineAPIs = new ArrayList<>();

    private String[] foundClasses = {
            "org.mindroid.android.app.statemachinesimpl.SensorMonitoring",
            "org.mindroid.android.app.statemachinesimpl.MindroidStatemachines"
    };


    /** Has to be set by activity **/
    public static PackageManager packageManager;

    private StatemachineService() {
        findStatemachineImplementations();
    }

    private void findStatemachineImplementations(){
        //TODO find classes dynamicially and by itsself without defining them in a foundClasses array?!

        for (String classname : foundClasses) {
            StatemachineAPI smAPI = loadMindroidMainClass(classname);
            if(smAPI != null) {
                addStatemachineAPI(smAPI);
            }
        }

    }

    /**
     *
     * @param classname e.g. "android.app.NotificationManager"
     * @return class implementing the interface IMindroidMain
     */
    private StatemachineAPI loadMindroidMainClass(String classname){
        try {
            StatemachineAPI smAPI = null;
            Class cls = Class.forName(classname);
            try {
                if(cls.newInstance() instanceof StatemachineAPI){
                    smAPI = (StatemachineAPI) cls.newInstance();
                }
            }catch(ClassCastException cce){
                //Not the class i was looking for
                return null;
            }

            if(smAPI != null){
                return smAPI;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Class not found!
        } catch (Exception e) {
            // Unknown exception
            e.printStackTrace();
        }
        return null;
    }

    private void addStatemachineAPI(StatemachineAPI smAPI){
        statemachineAPIs.add(smAPI);
    }


    public ArrayList<StatemachineAPI> getStatemachineAPIs() {
        return statemachineAPIs;
    }

    public ArrayList<String> getStatemachineCollectionIDs(){
        ArrayList<String> ids = new ArrayList<String>();

        for (int i = 0; i < statemachineAPIs.size(); i++) {
            for(String id : statemachineAPIs.get(i).getStatemachineCollection().getStatemachines().keySet()){
                ids.add(id);
            }
        }

        return ids;
    }

    public static StatemachineService getInstance() {
        return ourInstance;
    }

}
