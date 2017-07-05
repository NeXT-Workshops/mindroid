package org.mindroid.impl.statemachine;

import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.IStatemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Torbe on 06.05.2017.
 */
public class StatemachineCollection {

    private HashMap<String,ArrayList<IStatemachine>> statemachines;

    public StatemachineCollection(){
        statemachines = new HashMap<String,ArrayList<IStatemachine>>();
    }

    /**
     *
     * Adds a Single running Statemachine to the Collection.
     * Statemachines with the same id will be overwritten.
     *
     * @param statemachine
     */
    public void addStatemachine(IStatemachine statemachine){
        //TODO may throw a warning if statemachine/group with the same id already exists and will be overwritten
        ArrayList<IStatemachine> statemachines = new ArrayList<IStatemachine>(1);
        statemachines.add(statemachine);
        this.statemachines.put(statemachine.getID(),statemachines);
    }

    /**
     * Adds parallel running Statemachine to the Collection.
     * Calling this method multiple times with the same id will add the given Statemachines to the already existing ones.
     *
     * @param groupID
     * @param statemachines
     */
    public void addParallelStatemachines(String groupID,IStatemachine... statemachines){
        if(this.statemachines.containsKey(groupID)){
            ArrayList<IStatemachine> existingSMs = this.statemachines.get(groupID);

            for (IStatemachine newSM : statemachines) {
                if(!this.statemachines.get(groupID).contains(newSM)){
                    this.statemachines.get(groupID).add(newSM);
                }
            }
            //Testprintout
            System.out.println("## StatemachineCollection.addParallelStatemachines(..): Collection: ");
            for (int i = 0; i < this.statemachines.get(groupID).size(); i++) {
                System.out.println("## "+i+". "+this.statemachines.get(groupID).get(i));
            }

        }else {
            ArrayList<IStatemachine> collection = new ArrayList<IStatemachine>(statemachines.length);
            this.statemachines.put(groupID, collection);
            addParallelStatemachines(groupID,statemachines);
        }
    }

    /**
     * Adds parallel running Statemachine to the Collection.
     * Calling this method multiple times with the same id will add the given Statemachines to the already existing ones.
     *
     * @param groupID
     * @param statemachines
     */
    public void addParallelStatemachines(String groupID,ArrayList<IStatemachine> statemachines){
        IStatemachine[] arrayOfStatemachines = new  IStatemachine[statemachines.size()];
        for (int i = 0; i < statemachines.size(); i++) {
            arrayOfStatemachines[i] = statemachines.get(i);
        }
        addParallelStatemachines(groupID,arrayOfStatemachines);
    }

    public ArrayList<IStatemachine> getStatemachineList(String id){
        return statemachines.get(id);
    }

    public Set<String> getStatemachineKeySet(){
        return this.statemachines.keySet();
    }
}
