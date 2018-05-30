package org.mindroid.android.app.serviceloader;

import org.mindroid.api.BasicAPI;
import org.mindroid.api.ImplementationIDCrawlerVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ImplementationService {

    //Collected implementations
    private List<BasicAPI> implementations = new ArrayList<>();

    /** Collects the IDs of the APIs **/
    private ImplementationIDCrawlerVisitor idCollector = new ImplementationIDCrawlerVisitor();

    private static ImplementationService ourInstance = new ImplementationService();

    public static ImplementationService getInstance() {
        return ourInstance;
    }

    private ImplementationService(){
        findImplementations();
    }


    private String[] classesDev = {
            "org.mindroid.android.app.programs.dev.RectangleDriver",
            "org.mindroid.android.app.programs.dev.MotorTest",
            "org.mindroid.android.app.programs.dev.SensorTestWallPingPong",
            "org.mindroid.android.app.programs.dev.TestBroadcasting",
            "org.mindroid.android.app.programs.dev.RectangleDriver3",
            "org.mindroid.android.app.programs.dev.RectangleDriver2",
            "org.mindroid.android.app.programs.dev.ImpSingleWallPingPong",
            "org.mindroid.android.app.programs.dev.ImpCoordWallPingPong",
            "org.mindroid.android.app.programs.dev.SpeedTestForBackward",
            "org.mindroid.android.app.programs.dev.SpeedTestTurns",
            "org.mindroid.android.app.programs.dev.ButtonTest",
            "org.mindroid.android.app.programs.dev.ImpSingleWallPingPong",
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

            "org.mindroid.android.app.programs.workshop.stubs.ImpSingleWallPingPong",
            "org.mindroid.android.app.programs.workshop.stubs.ImpCoordWallPingPong",

            "org.mindroid.android.app.programs.workshop.stubs.LawnMower",
            "org.mindroid.android.app.programs.workshop.stubs.Platooning_A",
            "org.mindroid.android.app.programs.workshop.stubs.Platooning_B"
    };
    private String[] classesSolutions = {

            "org.mindroid.android.app.programs.workshop.solutions.HelloWorld",
            "org.mindroid.android.app.programs.workshop.solutions.HelloDate",

            "org.mindroid.android.app.programs.workshop.solutions.DriveSquare",
            "org.mindroid.android.app.programs.workshop.solutions.ParkingSensor",
            "org.mindroid.android.app.programs.workshop.solutions.ColourTest",

            "org.mindroid.android.app.programs.workshop.solutions.HelloWorldPingR",
            "org.mindroid.android.app.programs.workshop.solutions.HelloWorldPingB",

            "org.mindroid.android.app.programs.workshop.solutions.ImpSingleWallPingPong",
            "org.mindroid.android.app.programs.workshop.solutions.ImpCoordWallPingPong",

            "org.mindroid.android.app.programs.workshop.solutions.LawnMower",
            "org.mindroid.android.app.programs.workshop.solutions.Platooning_A",
            "org.mindroid.android.app.programs.workshop.solutions.Platooning_B"
    };


    // Define here which set to be selected
    private String[] foundClasses = classesSolutions;
    //private String[] foundClasses = classesStubs;
    //private String[] foundClasses = classesStatemachine;
    //private String[] foundClasses = classesDev;



    private void findImplementations(){
        for (String classname : foundClasses) {
            BasicAPI implementation = loadBasicAPIClasses(classname);
            if(implementation != null) {
                addImplementation(implementation);
                collectID(implementation);
            }
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
    private void addImplementation(BasicAPI implementation){
        implementations.add(implementation);
    }

    /**
     * Collects the ID of the Implementation
     * @param implementation - to collect the ID(s) from
     */
    private void collectID(BasicAPI implementation){
        idCollector.collectID(implementation);
    }

    /**
     * Returns the list of found Imperative implementation IDs
     * @return ArrayList of IDs
     */
    public List<BasicAPI> getImplementations() {
        return implementations;
    }

    /**
     * Returns the IDs of the collected implementations
     * @return String[] of collected IDs
     */
    public String[] getImplementationIDs(){
        return idCollector.getCollectedIDs().toArray(new String[idCollector.getCollectedIDs().size()]);
    }
}
