package org.mindroid.android.app.serviceloader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mindroid.android.app.fragments.log.LoggerFragment;
import org.mindroid.api.BasicAPI;
import org.mindroid.api.ImplementationIDCrawlerVisitor;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImplementationService {

    public static final String DEMO = "Demo";

    //Collected implementations

    private HashMap<String, List<BasicAPI>> implementationSets = new HashMap<>();

    /** Collects the IDs of the APIs **/
    private HashMap<String, ImplementationIDCrawlerVisitor> idCollectorSet = new HashMap<>();

    private static ImplementationService ourInstance = new ImplementationService();

    public static ImplementationService getInstance() {
        return ourInstance;
    }

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

        classesStatemachine = parseStringArray("classesStatemachine", jsonObject);
        classesSolutions = parseStringArray("classesSolutions", jsonObject);
        classesStubs = parseStringArray("classesStubs", jsonObject);
        classesDev = parseStringArray("classesDev", jsonObject);
        //classesDemo = parseStringArray("classesDemo", jsonObject); TODO update jsonFinder

        setMap = makeMap();
        findImplementations();
    }
    private String[] parseStringArray(String key, JSONObject jsonObject){
        JSONArray classesJson = (JSONArray) jsonObject.get(key);

        String[] classNames = new String[classesJson.size()];
        for (int i = 0; i < classNames.length; i++) {
            classNames[i] = (String) classesJson.get(i);
        }

        return classNames;
    }

    private String[] classesDev;
    private String[] classesStatemachine;
    private String[] classesStubs;
    private String[] classesSolutions;
    private String[] classesDemo;

    private HashMap<String, String[]> setMap;// = makeMap();
    private String[] program_sets = {"Solutions", "Stubs", "Statemachine", "Dev"};//TODO DEMO};

    private HashMap<String, String[]> makeMap(){
        HashMap<String, String[]> map = new HashMap<>();
        map.put(program_sets[0], classesSolutions);
        map.put(program_sets[1], classesStubs);
        map.put(program_sets[2], classesStatemachine);
        map.put(program_sets[3], classesDev);
        //map.put(program_sets[4], classesDemo); TODO
        return map;
    }

    public String getDefaultSet() {
        return program_sets[1];
    }


    private void findImplementations(){
        for(String set : program_sets){
            List<BasicAPI> implementations = new ArrayList<>();
            ImplementationIDCrawlerVisitor idCollector = new ImplementationIDCrawlerVisitor();
            String[] foundClasses = setMap.get(set); //SettingsProvider.getInstance().getSelectedProgramSet());
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
        return implementationSets.get(setKey);
    }

    /**
     * Returns the IDs of the collected implementations
     * @return String[] of collected IDs
     */
    public String[] getImplementationIDs(String setKey){
        List<String> idList = idCollectorSet.get(setKey).getCollectedIDs();
        return idList.toArray(new String[idList.size()]);
    }

    public String[] getImplementationSets(){
        return program_sets;
    }

}
