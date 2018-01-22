package com.game.main.gameObjects;

import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IInstance;
import com.game.main.engine.util.WorldObject;

/**
 * Created by AND0053 on 30/05/2016.
 */
public class VoxelFieldHandle extends WorldObject<VoxelField> {

    public VoxelFieldHandle() {
        super();
    }

    //This isnt the interface method, but it can be used if the type is know and it is safe
    public IInstance newInstance(GameEngine game, int xSize, int ySize, int scale) {
        return addInstance(new VoxelField(xSize, ySize, scale));
    }
}
