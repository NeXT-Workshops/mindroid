package org.mindroid.android.app.serviceloader;

import org.mindroid.api.ImperativeAPI;

import java.util.ArrayList;

public class ImperativeImplService {
    private static ImperativeImplService ourInstance = new ImperativeImplService();

    public static ImperativeImplService getInstance() {
        return ourInstance;
    }

    private ArrayList<ImperativeAPI> imperativeImplCollection = new ArrayList<>();

    // Array with Implementations to load
    private String[] foundClasses = {
            // Dev
            /*
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
            "org.mindroid.android.app.imperativeimpl.MessageTest"
            */

            
            // Stubs for Workshop
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

    private ImperativeImplService() {
        findStatemachineImplementations();
    }


    private void findStatemachineImplementations(){
        //getClassURLs();
        //System.out.println("#########################################################");
        //Package aPackage = Package.getPackage(statemachineSourcePath);
        //System.out.println("Packagename: "+aPackage.getName());
        //TODO find classes dynamicially and add to foundClasses-array

        for (String classname : foundClasses) {
            ImperativeAPI imperativeImpl = loadImperativeAPIClass(classname);
            if(imperativeImpl != null) {
                addImperativeImpl(imperativeImpl);
            }
        }

    }



    /**
     *
     * @param classname e.g. "android.app.NotificationManager"
     * @return class implementing the ImperativeAPI
     */
    private ImperativeAPI loadImperativeAPIClass(String classname){
        try {
            ImperativeAPI imperativeImpl = null;
            Class cls = Class.forName(classname);
            try {
                if(cls.newInstance() instanceof ImperativeAPI){
                    imperativeImpl = (ImperativeAPI) cls.newInstance();
                }
            }catch(ClassCastException cce){
                //Not the class i was looking for
                return null;
            }

            if(imperativeImpl != null){
                return imperativeImpl;
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
     * @param imperativeImplID - ID of an imperative Implementation
     */
    private void addImperativeImpl(ImperativeAPI imperativeImplID){
        imperativeImplCollection.add(imperativeImplID);
    }

    /**
     * Returns the list of found Imperative implementation IDs
     * @return ArrayList of IDs
     */
    public ArrayList<ImperativeAPI> getImperativeImplCollection() {
        return imperativeImplCollection;
    }

    public ArrayList<String> getImperativeImplIDs(){
        ArrayList<String> imperativeIDs = new ArrayList<String>(getImperativeImplCollection().size());
        for (int i = 0; i < getImperativeImplCollection().size(); i++) {
            if(getImperativeImplCollection().get(i).getImplementationID() != null) {
                imperativeIDs.add(getImperativeImplCollection().get(i).getImplementationID());
            }
        }
        return imperativeIDs;
    }
}
