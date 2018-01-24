package com.game.main.gameObjects.LaserGame;

import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IInstance;
import com.game.main.engine.runtimeRefrence.InstanceRef;
import com.game.main.engine.util.Direction;

import java.awt.*;

/**
 * Created by SuperRainbowNinja on 21/01/2018.
 */
abstract public class Laser implements IInstance {
    protected Rectangle self;
    protected InstanceRef ref;
    protected Direction rot;

    public static final int W_H = 16;

    public Laser(GameEngine e, int x, int y, Direction rotIn){
        self = new Rectangle(x, y, W_H-1, W_H-1);
        rot = rotIn;
    }

    @Override
    public void setInstanceRef(InstanceRef r) {
        ref = r;
    }

    @Override
    public InstanceRef getInstanceRef() {
        return ref;
    }



    public void step(GameEngine engine, long deltaTime) {}

    abstract Laser[] reset(GameEngine e);

    //protected boolean set = 1;
    abstract public int getState(Direction from);

    public Laser[] getTargets(GameEngine e) {
        Point p = rot.getRotation(7, 0);
        Direction.Pair toIn = rot.raycastPointInst(e.getMainView(), e.getGameObjects(), self.x + 7 + p.x, self.y + 7 + p.y, 8, ref);
        if (toIn.i instanceof ReactLaser) {
            return ((ReactLaser) toIn.i).getTargets(e, rot);
        }
        return new Laser[0];
    }

}
