package org.mindroid.api;

public abstract class AbstractImplDBVisitor extends AbstractImplVisitor {

    public void addImplementation(BasicAPI impl){
        impl.accept(this);
    }

}
