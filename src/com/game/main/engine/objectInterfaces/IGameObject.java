package com.game.main.engine.objectInterfaces;

import com.game.main.engine.GameEngine;
import com.game.main.engine.runtimeRefrence.ObjectRef;


/**
 * Created by AND0053 on 15/04/2015.
 */

public interface IGameObject {
    //boolean pointCollides(int x, int y); //checks if a point collides with the object
    //boolean pointCollidesObj(int x ,int y, IGameObject exclude); //same as above, but will ignore the specified object
    //void step(long deltaTime, KeyBoard mainInput)
    //boolean instazible();
    //int getInstance();

    void loadObject(GameEngine e);

    //The object refrence is set using reflectio
    //A class must contatin a field with the @ReferenceField anotation in order to be loaded correctly
    ObjectRef getObjectRef();
}
