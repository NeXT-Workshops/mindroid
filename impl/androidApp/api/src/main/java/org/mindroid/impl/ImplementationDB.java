package org.mindroid.impl;

import org.mindroid.api.AbstractImplDBVisitor;
import org.mindroid.api.BasicAPI;
import org.mindroid.api.ImperativeAPI;
import org.mindroid.api.StatemachineAPI;
import org.mindroid.api.statemachine.IStatemachine;
import org.mindroid.impl.statemachine.StatemachineCollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplementationDB extends AbstractImplDBVisitor {

    private Map<String,BasicAPI> implementations = new HashMap<String,BasicAPI>();

    /**
     * Gets called when {@link #addImplementation(BasicAPI)} of the Super-class is called.
     *
     * @param api - implementation to add
     */
    @Override
    public void visit(ImperativeAPI api) {
        addImperativeAPI(api);
    }

    /**
     * Gets called when {@link #addImplementation(BasicAPI)} of the Super-class is called.
     *
     * @param api - implementation to add
     */
    @Override
    public void visit(StatemachineAPI api) {
        addStatemachineAPI(api);
    }

    /**
     * Adds an ImperativeAPI Implementation
     * @param api - Instance of an ImperativeAPI containing a implementation
     */
    private void addImperativeAPI(ImperativeAPI api){
        implementations.put(api.getImplementationID(),api);
    }

    /**
     *  Add the collection of statemachines contained in its Class to the Implementation.
     *  As the API given to this method can contain multiple statemachines with different IDs in its collection, each group identified by its ID will be wrapped into
     *  a new StatemachineAPI only containing this group of statemachines identified by a single id.
     *  It is necessary to get the correct Statemachines later on at the Executor.
     *
     * @param api - Statemachine API
     */
    private void addStatemachineAPI(StatemachineAPI api){
        StatemachineCollection sc = api.getStatemachineCollection();
        for(String ID : sc.getStatemachines().keySet()){
            //Rewrapp Group of Statemachines identified by their ID
            StatemachineAPI tmpSM = new StatemachineAPI();
            tmpSM.statemachineCollection.addParallelStatemachines(ID,sc.getStatemachines().get(ID));

            //Put the new wrapped API to the DB
            implementations.put(ID,tmpSM);
        }
    }

    /**
     *
     * @param id - id of implementation
     * @return API - can be ImperativeAPI - or Statemachines API containing a collection of Statemachines with the given ID
     */
    public BasicAPI getImplementation(String id){
        return implementations.get(id);
    }

    public boolean contains(String id){
        return implementations.containsKey(id);
    }

    public String[] getImplementationIDs(){
        return implementations.keySet().toArray(new String[implementations.keySet().size()]);
    }
}
