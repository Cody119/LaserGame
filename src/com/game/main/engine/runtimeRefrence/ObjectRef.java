package com.game.main.engine.runtimeRefrence;

import com.game.main.engine.GameObjectList;

/**
 * Created by AND0053 on 1/06/2016.
 */
public class ObjectRef {
    public final GameObjectList parent;
    public final int gameObjectRef;
    public final int tickObjectRef;
    public final int drawableObjectRef;
    public final int worldObjectRef;
    public final int instantiableObjectRef;
    public ObjectRef(GameObjectList parentIn, int gR, int tR, int dR, int wR, int iR) {
        parent = parentIn;
        gameObjectRef = gR;
        tickObjectRef = tR;
        drawableObjectRef = dR;
        worldObjectRef = wR;
        instantiableObjectRef = iR;
    }
}
