package org.mindroid.api;

import org.mindroid.api.ImperativeAPI;
import org.mindroid.api.StatemachineAPI;

public abstract class AbstractImplVisitor {

    abstract public void visit(ImperativeAPI api);

    abstract public void visit(StatemachineAPI api);
}
