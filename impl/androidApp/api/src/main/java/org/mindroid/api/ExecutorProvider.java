package org.mindroid.api;

import org.mindroid.impl.errorhandling.ErrorHandlerManager;
import org.mindroid.impl.imperative.ImperativeImplExecutor;
import org.mindroid.impl.statemachine.StatemachineExecutor;

public class ExecutorProvider extends AbstractImplVisitor{

    private IExecutor currentExecutor;

    //Executor for Imperative Implementations
    private ImperativeImplExecutor impExecutor = new ImperativeImplExecutor();

    //Executor for Statemachine Implementations
    private StatemachineExecutor smExecutor = new StatemachineExecutor();

    /**
     * Returns the proper Executor neccessary to execute the Implementation
     * @param executable - can be any type of BasicAPI
     * @return
     */
    public IExecutor getExecutor(BasicAPI executable){
        //Visit api. -> will set the current Executor which will be returned
        executable.accept(this);
        return currentExecutor;
    }

    @Override
    public void visit(ImperativeAPI api) {
        //TODO make sure thet currently no statemachine is running
        impExecutor.setImplementation(api);
        currentExecutor = impExecutor;
    }

    @Override
    public void visit(StatemachineAPI api) {
        //Check if the given API only contains one single set of one or more parallel running statemachines.
        if(api.statemachineCollection.getStatemachines().keySet().isEmpty() || api.statemachineCollection.getStatemachines().keySet().size() > 1){
            Exception e = new IllegalArgumentException("It is not allowed to provide an StatemachineAPI to this method containing more than one Group of Statemachines!");
            ErrorHandlerManager.getInstance().handleError(e,ExecutorProvider.class,e.getMessage());
            return;
        }

        //Extract ID - It is expected that there is only one group of Statemachines contained in the API.
        String ID =  api.statemachineCollection.getStatemachines().keySet().iterator().next();
        //Set Implementation
        smExecutor.setImplementation(api.statemachineCollection.getStatemachines().get(ID));
        //Set executor
        currentExecutor = smExecutor;
    }
}
