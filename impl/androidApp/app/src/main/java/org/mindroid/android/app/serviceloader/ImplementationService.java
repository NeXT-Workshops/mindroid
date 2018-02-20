package org.mindroid.android.app.serviceloader;

import org.mindroid.api.BasicAPI;
import org.mindroid.api.ImplementationIDCrawlerVisitor;

import java.util.ArrayList;
import java.util.List;

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

    private String[] foundClasses = {
            // Dev
            "org.mindroid.android.app.imperativeimpl.RectangleDriver",
            "org.mindroid.android.app.imperativeimpl.RectangleDriver3",
            "org.mindroid.android.app.imperativeimpl.RectangleDriver2",
            "org.mindroid.android.app.imperativeimpl.ImpSingleWallPingPong",
            "org.mindroid.android.app.imperativeimpl.ImpCoordWallPingPong",
            "org.mindroid.android.app.imperativeimpl.SpeedTestForBackward",
            "org.mindroid.android.app.imperativeimpl.SpeedTestTurns",
            "org.mindroid.android.app.imperativeimpl.ButtonTest",
            "org.mindroid.android.app.imperativeimpl.ImpSingleWallPingPong",
            "org.mindroid.android.app.imperativeimpl.TestMessageAccess",
            "org.mindroid.android.app.imperativeimpl.MessageTest",

            //DEV Statemachines
            "org.mindroid.android.app.statemachinesimpl.SensorMonitoring",
            "org.mindroid.android.app.statemachinesimpl.MindroidStatemachines"


            // Stubs for Workshop
            /*
            "org.mindroid.android.app.workshopimpl.HelloWorld",
            "org.mindroid.android.app.workshopimpl.HelloDate",
            "org.mindroid.android.app.workshopimpl.DriveSquare",
            "org.mindroid.android.app.workshopimpl.ParkingSensor",
            "org.mindroid.android.app.workshopimpl.ColourTest",
            "org.mindroid.android.app.workshopimpl.HelloWorldPingR",
            "org.mindroid.android.app.workshopimpl.HelloWorldPingB",
            "org.mindroid.android.app.workshopimpl.ImpSingleWallPingPong",
            "org.mindroid.android.app.workshopimpl.ImpCoordWallPingPong",
            "org.mindroid.android.app.workshopimpl.LawnMower",
            "org.mindroid.android.app.workshopimpl.Platooning",
            "org.mindroid.android.app.workshopimpl.Follow"
            */
            // Solutions for Workshop
            /*
            "org.mindroid.android.app.workshopSolutions.HelloWorld",
            "org.mindroid.android.app.workshopSolutions.HelloDate",
            "org.mindroid.android.app.workshopSolutions.DriveSquare",
            "org.mindroid.android.app.workshopSolutions.ParkingSensor",
            "org.mindroid.android.app.workshopSolutions.ColourTest",
            "org.mindroid.android.app.workshopSolutions.HelloWorldPingR",
            "org.mindroid.android.app.workshopSolutions.HelloWorldPingB",
            "org.mindroid.android.app.workshopSolutions.ImpSingleWallPingPong",
            "org.mindroid.android.app.workshopSolutions.ImpCoordWallPingPong",
            "org.mindroid.android.app.workshopSolutions.LawnMower",
            "org.mindroid.android.app.workshopSolutions.Platooning",
            "org.mindroid.android.app.workshopSolutions.Follow"
                 */
    };

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
