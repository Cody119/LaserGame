package com.game.main.engine.runtimeRefrence;

/**
 * Created by AND0053 on 1/06/2016.
 *
 * Should
 */
public class InstanceRef {
    public final ObjectRef obj;
    public final long id;
    public final int ref;
    public InstanceRef(int index, long instanceID, ObjectRef obj) {
        this.obj = obj;
        id = instanceID;
        ref = index;
    }
}
