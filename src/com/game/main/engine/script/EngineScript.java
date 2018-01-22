package com.game.main.engine.script;

import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IGameObject;
import com.game.main.engine.objectInterfaces.ReferenceField;
import com.game.main.engine.runtimeRefrence.ObjectRef;

/**
 * Created by AND0053 on 4/06/2016.
 */
public class EngineScript implements IGameObject{

    @ReferenceField
    ObjectRef ref = null;

    @Override
    public void loadObject(GameEngine e) {

    }


    @Override
    public ObjectRef getObjectRef() {
        return ref;
    }
}
