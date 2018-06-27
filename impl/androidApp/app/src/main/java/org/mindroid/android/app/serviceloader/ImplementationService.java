package org.mindroid.android.app.serviceloader;

import org.mindroid.android.app.robodancer.SettingsProvider;
import org.mindroid.api.BasicAPI;
import org.mindroid.api.ImplementationIDCrawlerVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ImplementationService {

    //Collected implementations
    //private List<BasicAPI> implementations = new ArrayList<>();
    private HashMap<String, List<BasicAPI>> implementationSets = new HashMap<>();

    /** Collects the IDs of the APIs **/
    //private ImplementationIDCrawlerVisitor idCollector = new ImplementationIDCrawlerVisitor();
    private HashMap<String, ImplementationIDCrawlerVisitor> idCollectorSet = new HashMap<>();

    private static ImplementationService ourInstance = new ImplementationService();

    public static ImplementationService getInstance() {
        return ourInstance;
    }

    private ImplementationService(){
        setMap = makeMap();
        findImplementations();
    }


    private String[] classesDev = {
            "org.mindroid.android.app.programs.dev.RectangleDriver",
            "org.mindroid.android.app.programs.dev.MotorTest",
            "org.mindroid.android.app.programs.dev.SensorTestWallPingPong",
            "org.mindroid.android.app.programs.dev.TestBroadcasting",
            "org.mindroid.android.app.programs.dev.RectangleDriver3",
            "org.mindroid.android.app.programs.dev.RectangleDriver2",
            "org.mindroid.android.app.programs.dev.SingleWallPingPong",
            "org.mindroid.android.app.programs.dev.CoordWallPingPong",
            "org.mindroid.android.app.programs.dev.SpeedTestForBackward",
            "org.mindroid.android.app.programs.dev.SpeedTestTurns",
            "org.mindroid.android.app.programs.dev.ButtonTest",
            "org.mindroid.android.app.programs.dev.SingleWallPingPong",
            "org.mindroid.android.app.programs.dev.TestMessageAccess",
            "org.mindroid.android.app.programs.dev.MessageTest",
            "org.mindroid.android.app.programs.dev.SimpleForward"
    };

    private String[] classesStatemachine = {
            "org.mindroid.android.app.programs.dev.statemachinesimpl.SensorMonitoring",
            "org.mindroid.android.app.programs.dev.statemachinesimpl.MindroidStatemachines"
    };

    private String[] classesStubs = {
            "org.mindroid.android.app.programs.workshop.stubs.HelloWorld",
            "org.mindroid.android.app.programs.workshop.stubs.HelloDate",

            "org.mindroid.android.app.programs.workshop.stubs.DriveSquare",
            "org.mindroid.android.app.programs.workshop.stubs.ParkingSensor",
            "org.mindroid.android.app.programs.workshop.stubs.ColourTest",

            "org.mindroid.android.app.programs.workshop.stubs.HelloWorldPingR",
            "org.mindroid.android.app.programs.workshop.stubs.HelloWorldPingB",

            "org.mindroid.android.app.programs.workshop.stubs.SingleWallPingPong",
            "org.mindroid.android.app.programs.workshop.stubs.CoordWallPingPong",

            "org.mindroid.android.app.programs.workshop.stubs.LawnMower",
            "org.mindroid.android.app.programs.workshop.stubs.Platooning",
            "org.mindroid.android.app.programs.workshop.stubs.Follow"
    };

    private String[] classesSolutions = {
            "org.mindroid.android.app.programs.workshop.solutions.HelloWorld",
            "org.mindroid.android.app.programs.workshop.solutions.HelloDate",

            "org.mindroid.android.app.programs.workshop.solutions.DriveSquare",
            "org.mindroid.android.app.programs.workshop.solutions.ParkingSensor",
            "org.mindroid.android.app.programs.workshop.solutions.ColourTest",

            "org.mindroid.android.app.programs.workshop.solutions.HelloWorldPingA",
            "org.mindroid.android.app.programs.workshop.solutions.HelloWorldPingB",

            "org.mindroid.android.app.programs.workshop.solutions.SingleWallPingPong",
            "org.mindroid.android.app.programs.workshop.solutions.CoordWallPingPong",
            "org.mindroid.android.app.programs.workshop.solutions.CoordWallPingPongA",
            "org.mindroid.android.app.programs.workshop.solutions.CoordWallPingPongB",

            "org.mindroid.android.app.programs.workshop.solutions.LawnMower",
            "org.mindroid.android.app.programs.workshop.solutions.Platooning",
            "org.mindroid.android.app.programs.workshop.solutions.PlatooningFollower",
            "org.mindroid.android.app.programs.workshop.solutions.PlatooningLeader",
            "org.mindroid.android.app.programs.workshop.solutions.Follow"
    };

    private HashMap<String, String[]> setMap;// = makeMap();

    private String[] program_sets = {"Solutions", "Stubs", "Statemachine", "Dev"};

    private HashMap<String, String[]> makeMap(){
        HashMap<String, String[]> map = new HashMap<>();
        map.put(program_sets[0], classesSolutions);
        map.put(program_sets[1], classesStubs);
        map.put(program_sets[2], classesStatemachine);
        map.put(program_sets[3], classesDev);
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

                    //addImplementation(set, implementation);
                    //collectID(implementation);
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
