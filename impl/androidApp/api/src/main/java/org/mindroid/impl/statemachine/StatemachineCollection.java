package org.mindroid.impl.statemachine;

import org.mindroid.api.statemachine.IStatemachine;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Torbe on 06.05.2017.
 */
public class StatemachineCollection {

    private HashMap<String,IStatemachine[]> statemachines;

    public StatemachineCollection(){
        statemachines = new HashMap<String,IStatemachine[]>();
    }

    public void addStatemachine(IStatemachine statemachine){
        //TODO may throw a warning if statemachine/group with the same id already exists
        this.statemachines.put(statemachine.getID(),new IStatemachine[]{statemachine});
    }

    public void addParallelStatemachines(String groupID,IStatemachine... statemachines){
        if(this.statemachines.containsKey(groupID)){
            IStatemachine[] existingSMs = this.statemachines.get(groupID);
            IStatemachine[] newCollection = new Statemachine[existingSMs.length+statemachines.length];
            int cnt = 0;
            for (IStatemachine existingSM : existingSMs) {
                newCollection[cnt] = existingSM;
                cnt++;
            }
            for (IStatemachine newSms : statemachines) {
                newCollection[cnt] = newSms;
                cnt++;
            }
            this.statemachines.put(groupID, newCollection);
        }else {
            this.statemachines.put(groupID, statemachines);
        }
    }

    public IStatemachine[] getStatemachineSet(String id){
        return statemachines.get(id);
    }

    public Set<String> getStatemachineKeySet(){
        return this.statemachines.keySet();
    }
}
