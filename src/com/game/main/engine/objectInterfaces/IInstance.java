package com.game.main.engine.objectInterfaces;


import com.game.main.engine.runtimeRefrence.InstanceRef;

/**
 * Created by AND0053 on 30/05/2016.
 *
 * Should be implemented by any and all instances of game objects
 */
public interface IInstance extends IDrawableObject {
    boolean pointCollides(int  x, int y);
    void setInstanceRef(InstanceRef r);
    InstanceRef getInstanceRef();
}
