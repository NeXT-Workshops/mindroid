package org.mindroid.api;


import java.util.ArrayList;
import java.util.List;

public class ImplementationIDCrawlerVisitor extends AbstractImplVisitor {

    private List<String> collectedIDs;

    public ImplementationIDCrawlerVisitor(){
        collectedIDs = new ArrayList<String>();
    }

    public List<String> getCollectedIDs(){
        return collectedIDs;
    }

    public void collectID(BasicAPI implementation){
        implementation.accept(this);
    }


    @Override
    public void visit(ImperativeAPI api) {

        if(!collectedIDs.contains(api.getImplementationID())) {
            collectedIDs.add(api.getImplementationID());
        }
    }

    @Override
    public void visit(StatemachineAPI api) {
        for(String id : api.getStatemachineCollection().getStatemachines().keySet()){
            if(!collectedIDs.contains(id)) {
                collectedIDs.add(id);
            }
        }
    }

}
