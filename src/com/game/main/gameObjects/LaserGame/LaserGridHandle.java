package com.game.main.gameObjects.LaserGame;

import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IInstance;
import com.game.main.engine.objectInterfaces.ITickObject;
import com.game.main.engine.util.Direction;
import com.game.main.engine.util.WorldObject;
import com.game.main.engine.util.Queue;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by SuperRainbowNinja on 21/01/2018.
 */
public class LaserGridHandle extends WorldObject<Laser> implements ITickObject {

    public ArrayList<LaserSource> laserSources = new ArrayList<>();

    public LaserGridHandle() {
        super();
    }

    //This isn't the interface method, but it can be used if the type is know, its much safer
    public IInstance newInstance(GameEngine game, int x, int y, Direction rot, int type) {
        IInstance ret = addInstance(
                type == 0 ?
                    new LaserSource(game, coToGrid(x), coToGrid(y), rot) :
                    new LaserMirror(game, coToGrid(x), coToGrid(y), rot)
        );
        if (type == 0) {
            laserSources.add(((LaserSource) ret));
        }
        reset(game);
        return ret;
    }

    public void reset(GameEngine game) {
        for (Laser l : instances) {
            l.reset(game);
        }
//        Queue<Laser> lasers = new Queue<>();
//
//        for (Laser l : laserSources) {
//            lasers.add(l);
//        }
//
//        Laser l;
//        while ((l = lasers.remove()) != null) {
//            if (l.done()) {
//                continue;
//            } else {
//                l.touch();
//            }
//            Laser redos[] = l.reset(game);
//            lasers.addAll(Arrays.asList(redos));
//        }
    }

    @Override
    public boolean step(GameEngine g, long deltaTime) {
        for (Laser m : instances) {
            m.step(g, deltaTime);
        }
        return true;
    }

    public int coToGrid(int val){ //converts a cartesian co into a grid co
        return Laser.W_H*(int)Math.floor((double)val/Laser.W_H);
    }

    public Point coToGrid(int x, int y){ //converts a cartesian point into a grid point
        return new Point(coToGrid(x), coToGrid(y));
    }

    public Point coToGrid(Point p){ //converts a cartesian point into a grid point
        return new Point(coToGrid(p.x), coToGrid(p.y));
    }
}
