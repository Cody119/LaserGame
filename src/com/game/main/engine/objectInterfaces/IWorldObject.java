package com.game.main.engine.objectInterfaces;


import com.game.main.engine.runtimeRefrence.InstanceRef;

/**
 * Created by AND0053 on 30/05/2016.
 *
 * Are there any world objects that arnt drawn?
 * Not sure if i should force it to be Instantiable
 */
public interface IWorldObject extends IDrawableObject, IInstantiableObject, IGameObject {
    //double of int? not sure
    boolean pointCollides(int  x, int y, InstanceRef exclude);
    //not sure if objects should be refrenced directly?
    //IWorldObject pointCollidesObj(int  x, int y, IWorldObject exclude);
    IWorldObject pointCollidesObj(int  x, int y, InstanceRef exclude);

    IInstance pointCollidesInst(int x, int y, InstanceRef exclude);
}
