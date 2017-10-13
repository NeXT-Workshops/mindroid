package org.mindroid.impl.imperative;

import org.mindroid.api.ImperativeAPI;

import java.util.HashMap;

/**
 * Manages the Imperative Implementations
 */
public class ImperativeImplManager {
    private static ImperativeImplManager ourInstance = new ImperativeImplManager();

    public static ImperativeImplManager getInstance() {
        return ourInstance;
    }

    // ---- Robot Behavior implementations ----
    private HashMap<String,ImperativeAPI> imperativeImplementations = new HashMap<String,ImperativeAPI>();

    private ImperativeImplManager() {

    }

    public void addImperativeImplementation(ImperativeAPI imperativeImplementation) {
        this.imperativeImplementations.put(imperativeImplementation.getImplementationID(),imperativeImplementation);
    }

    /**
     * Starts executing the Implementation with the given ID
     * @param id - unique id identifiying the imperative implementation to start
     */
    public void startImperativeImplementation(String id){
        final ImperativeAPI implementation = imperativeImplementations.get(id);

        Runnable runImpl = new Runnable(){
            @Override
            public void run() {
                implementation.start();
            }
        };
        new Thread(runImpl).start();

    }

    /**
     * Stops executing the Implementation with the given ID
     * @param id - unique id identifiying the imperative implementation to stop
     */
    public void stopImperativeImplementation(String id){
        ImperativeAPI implementation = imperativeImplementations.get(id);
        implementation.stop();
    }

    public String[] getImperativeImplIDs(){
        return imperativeImplementations.keySet().toArray(new String[imperativeImplementations.size()]);
    }
}
