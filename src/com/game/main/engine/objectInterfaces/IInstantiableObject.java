package com.game.main.engine.objectInterfaces;

import com.game.main.engine.GameEngine;
import com.game.main.engine.runtimeRefrence.InstanceRef;

/**
 * Created by AND0053 on 30/05/2016.
 *
 * Still need to figure out how this whole thing will wotrk
 */
public interface IInstantiableObject {
    //Im not sure about the exeption thrown in this method, although i think unchecked is good
    InstanceRef newInstance(GameEngine game, long id, Object... args) throws IllegalArgumentException;
    IInstance getInstance(InstanceRef r);

    //IInstance newInstance(GameEngine game, Object[] args);

    //still not sure how to gameObjectRef an instance
    //IInstance getInstance();
}
