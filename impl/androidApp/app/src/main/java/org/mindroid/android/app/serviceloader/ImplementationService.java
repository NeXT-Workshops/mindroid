package org.mindroid.android.app.serviceloader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mindroid.api.BasicAPI;
import org.mindroid.api.ImplementationIDCrawlerVisitor;
import org.mindroid.impl.errorhandling.ErrorHandlerManager;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImplementationService {

    public static final String DEMO = "demo";

    //Collected implementations

    private HashMap<String, List<BasicAPI>> implementationSets = new HashMap<>();

    /** Collects the IDs of the APIs **/
    private HashMap<String, ImplementationIDCrawlerVisitor> idCollectorSet = new HashMap<>();

    private static ImplementationService ourInstance = new ImplementationService();

    public static ImplementationService getInstance() {
        return ourInstance;
    }

    //realID,String[]
    private HashMap<String, String[]> setMap;// = makeMap();

    //MAPS internal Set IDs to displayed Set IDs
    //displayedID, realID
    private HashMap<String, String> displayedIDMap;// = makeMap();

    private ImplementationService(){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader("/sdcard/Mindroid/programs.json"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (jsonObject == null){
            ErrorHandlerManager.getInstance().handleError(new NullPointerException(), ImplementationService.class, "Please push programs.json again. Use Script \"find and push\"");
        }else {
            String[] IDs = (String[]) jsonObject.keySet().toArray(new String[jsonObject.keySet().size()]);
            ArrayList<String> programSets = new ArrayList<String>();
            setMap = new HashMap<>();
            displayedIDMap = new HashMap<>();

            String prefixWorkshop = "WORKSHOP_";
            String prefixDev = "DEV_";


            for (String id : IDs) {
                if (id.contains(prefixWorkshop)) {
                    String displayedID = id.replace(prefixWorkshop, "");
                    displayedIDMap.put(displayedID, id);
                    setMap.put(id, parseStringArray(id, jsonObject));
                } else if (id.contains(prefixDev)) {
                    String displayedID = id.replace(prefixDev, "");
                    displayedIDMap.put(displayedID, id);
                    setMap.put(id, parseStringArray(id, jsonObject));
                } else {
                    //NOTHING
                }
            }

            findImplementations();
        }
    }
    public static String[] parseStringArray(String key, JSONObject jsonObject){
        JSONArray classesJson = (JSONArray) jsonObject.get(key);

        String[] classNames = new String[classesJson.size()];
        for (int i = 0; i < classNames.length; i++) {
            classNames[i] = (String) classesJson.get(i);
        }

        return classNames;
    }


    public String getDefaultSet() {
        return "stubs";
    }


    private void findImplementations(){
        for(String set : setMap.keySet()){
            List<BasicAPI> implementations = new ArrayList<>();
            ImplementationIDCrawlerVisitor idCollector = new ImplementationIDCrawlerVisitor();
            System.out.println("################ "+set);
            String[] foundClasses = setMap.get(set); //SettingsProvider.getInstance().getSelectedProgramSet());
            System.out.println("################ "+setMap.get(set));
            for (String classname : foundClasses) {
                BasicAPI implementation = loadBasicAPIClasses(classname);
                if (implementation != null) {
                    implementations.add(implementation);
                    idCollector.collectID(implementation);
                }
            }
            implementationSets.put(set, implementations);
            idCollectorSet.put(set, idCollector);
        }
    }

    /**
     *
     * @param classname e.g. "android.app.NotificationManager"
     * @return class implementing the ImperativeAPI
     */
    private BasicAPI loadBasicAPIClasses(String classname){
        try {
            BasicAPI implementation = null;
            Class cls = Class.forName(classname);
            try {
                if(cls.newInstance() instanceof BasicAPI){
                    implementation = (BasicAPI) cls.newInstance();
                }
            }catch(ClassCastException cce){
                //Not the class i was looking for
                return null;
            }

            if(implementation != null){
                return implementation;
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

    /**
     * Add a Imperative Implementation to the collection.
     * @param implementation - ID of an imperative Implementation
     */
    private void addImplementation(String set, BasicAPI implementation){
        implementationSets.get(set).add(implementation);
    }

    /**
     * Collects the ID of the Implementation
     * @param implementation - to collect the ID(s) from
     */

    /**
     * Returns the list of found Imperative implementation IDs
     * @return ArrayList of IDs
     */
    public List<BasicAPI> getImplementations(String setKey) {
        return implementationSets.get(displayedIDMap.get(setKey));
    }

    /**
     * Returns the IDs of the collected implementations
     * @return String[] of collected IDs
     */
    public String[] getImplementationIDs(String setKey){
        System.out.println("################ getImplementationIDsSetKey: " +setKey );
        System.out.println("################ getImplementationIDsSetKey MAPPING: " +displayedIDMap.get(setKey) );
        System.out.println(idCollectorSet.toString());
        List<String> idList = idCollectorSet.get(displayedIDMap.get(setKey)).getCollectedIDs();
        return idList.toArray(new String[idList.size()]);
    }

    public String[] getImplementationSets(){
        return displayedIDMap.keySet().toArray(new String[displayedIDMap.keySet().size()]);
    }

}
