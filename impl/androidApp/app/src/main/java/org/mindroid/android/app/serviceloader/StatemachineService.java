package org.mindroid.android.app.serviceloader;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import org.mindroid.api.statemachine.IMindroidMain;
import org.mindroid.impl.statemachine.StatemachineCollection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created by torben on 14.08.2017.
 */
public class StatemachineService {
    private static StatemachineService ourInstance = new StatemachineService();

    //ServiceLoader loads all classed containing and returning Statemachine Implementations.
    private ServiceLoader<IMindroidMain> statemachineLoader;

    private ArrayList<StatemachineCollection> scs = new ArrayList<>();

    private final String statemachineSourcePath = "org.mindroid.android.app.statemachinesimpl";

    private String[] foundClasses = {
            "org.mindroid.android.app.statemachinesimpl.SensorMonitoring",
            "org.mindroid.android.app.statemachinesimpl.MindroidLVL2",
            "org.mindroid.android.app.statemachinesimpl.MindroidLVL1"
    };

    private final String apkPath = "/data/app/";

    /** Has to be set by activity **/
    public static PackageManager packageManager;

    private StatemachineService() {
        findStatemachineImplementations();
    }

    private void findStatemachineImplementations(){
        //getClassURLs();
        //System.out.println("#########################################################");
        //Package aPackage = Package.getPackage(statemachineSourcePath);
        //System.out.println("Packagename: "+aPackage.getName());
        //TODO find classes dynamicially


        for (String classname : foundClasses) {
            IMindroidMain mindroid = loadMindroidMainClass(classname);
            if(mindroid != null) {
                addStatemachineCollection(mindroid.getStatemachineCollection());
            }
        }

    }
    /**
     *
     * @param classname e.g. "android.app.NotificationManager"
     * @return class implementing the interface IMindroidMain
     */
    private IMindroidMain loadMindroidMainClass(String classname){
        try {
            IMindroidMain mindroidMain = null;
            Class cls = Class.forName(classname);
            try {
                if(cls.newInstance() instanceof IMindroidMain){
                    mindroidMain = (IMindroidMain) cls.newInstance();
                }
            }catch(ClassCastException cce){
                //Not the class i was looking for
                return null;
            }

            if(mindroidMain != null){
                return mindroidMain;
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

    private void addStatemachineCollection(StatemachineCollection sc){
        scs.add(sc);
    }

    private void extractStatemachinesFromCollections(){
        if(statemachineLoader.iterator() != null) {
            Iterator<IMindroidMain> it = statemachineLoader.iterator();
            StatemachineCollection sc;

            while (it.hasNext()) {
                sc = it.next().getStatemachineCollection();
                scs.add(sc);
            }
        }
    }



    public ArrayList<StatemachineCollection> getStatemachineCollections() {
        return scs;
    }

    public String[] getStatemachineCollectionIDs(){
        int size=0;
        for (int i = 0; i < scs.size(); i++) {
            size += scs.get(i).getStatemachineKeySet().size();
        }

        String[] tmpIDs = new String[size];
        int index = 0;
        for (int i = 0; i < scs.size(); i++) {
            for(String id : scs.get(i).getStatemachineKeySet()){
                tmpIDs[index] = id;
                index++;
            }
        }

        //TODO check if it can be removed!
        String[] ids = new String[index];
        for (int i = 0; i < index; i++) {
            ids[i] = tmpIDs[i];
        }

        return ids;
    }

    public static StatemachineService getInstance() {
        return ourInstance;
    }

    public String getStatemachineSourcePath(){
        /*URL main = StatemachineService.class.getResource(StatemachineService.class.getCanonicalName());
        String path = main.getPath();
        System.out.println("PATH: "+path);*/
        return "org.mindroid.android.app.statemachinesimpl.";
    }

    private class StatemachineClassLoader extends ClassLoader{

    }
}
