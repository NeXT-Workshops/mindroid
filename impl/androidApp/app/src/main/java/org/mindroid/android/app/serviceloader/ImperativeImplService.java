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
            "org.mindroid.android.app.imperativeimpl.RectangleDriver",
            "org.mindroid.android.app.imperativeimpl.RectangleDriver3",
            "org.mindroid.android.app.imperativeimpl.RectangleDriver2",
            "org.mindroid.android.app.imperativeimpl.ImpSingleWallPingPong",
            "org.mindroid.android.app.imperativeimpl.SpeedTestForBackward",
            "org.mindroid.android.app.imperativeimpl.SpeedTestTurns",
            "org.mindroid.android.app.imperativeimpl.ImpSingleWallPingPong",
            "org.mindroid.android.app.imperativeimpl.TestMessageAccess"
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
