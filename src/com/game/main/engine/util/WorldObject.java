package com.game.main.engine.util;

import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IInstance;
import com.game.main.engine.objectInterfaces.IWorldObject;
import com.game.main.engine.runtimeRefrence.InstanceRef;

/**
 * Created by AND0053 on 30/05/2016.
 */
public abstract class WorldObject<T extends IInstance> extends GameObject<T> implements IWorldObject {
    public WorldObject() {
        super();
    }

    @Override
    public boolean pointCollides(int x, int y, InstanceRef exclude) {
        return pointCollidesObj(x, y, exclude) != null;
    }

    @Override
    public IWorldObject pointCollidesObj(int x, int y, InstanceRef exclude) {
        for (T o : instances) {
            if (o.getInstanceRef() != exclude && o.pointCollides(x,y)) {
                return this;
            }
        }
        return null;
    }

    @Override
    public IInstance pointCollidesInst(int x, int y, InstanceRef exclude) {
        for (T o : instances) {
            if (o.getInstanceRef() != exclude && o.pointCollides(x,y)) {
                return o;
            }
        }
        return null;
    }

    @Override
    public void drawSelf(GameEngine engine) {
        for (T o : instances) {
            o.drawSelf(engine);
        }
    }
}
