package org.mindroid.common.messages;

public interface ILoggable {

    void accept(IEV3MessageVisitor visitor);

}
