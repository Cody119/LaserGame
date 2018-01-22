package com.game.main.gameObjects;

import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IInstance;
import com.game.main.engine.objectInterfaces.ITickObject;
import com.game.main.engine.util.WorldObject;

/**
 * Created by AND0053 on 30/05/2016.
 */
public class MotionObjectHandle extends WorldObject<MotionObject> implements ITickObject {
    private int[] fields;

    public MotionObjectHandle(int one, int two, int three, int four) {
        super();
        fields = new int[]{one, two ,three, four};
    }


    //This isnt the interface method, but it can be used if the type is know, its much safer
    public IInstance newInstance(GameEngine game, int x, int y, int width, int height) {
        return addInstance(new MotionObject(x, y, width, height));
    }

    @Override
    public boolean step(GameEngine g, long deltaTime) {
        for (MotionObject m : instances) {
            m.step(g, deltaTime);
        }
        return true;
    }
}
