package com.game.main.gameObjects.LaserGame;

import com.game.main.engine.GameEngine;
import com.game.main.engine.util.Direction;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by SuperRainbowNinja on 21/01/2018.
 */
public class LaserMirror extends ReactLaser {
    public LaserMirror(GameEngine e, int x, int y, Direction rotIn) {
        super(e, x, y, rotIn);
        reset(e);
    }

    @Override
    public Laser[] getTargets(GameEngine e, Direction from) {
        return new Laser[0];
    }

    Point toStrait;
    Point toBent;

    @Override
    Laser[] reset(GameEngine e) {
        //if (lasersPointing[rot.ordinal()] != null && lasersPointing[rot.ordinal()].getState(rot.reverse()) != 0)
        ArrayList<Laser> retLasers = new ArrayList<>(2);
        {
            Point p = rot.next().getRotation(7, 0);
            Direction.Pair ret = rot.next().raycastPointInst(e.getMainView(), e.getGameObjects(), self.x + 7 + p.x, self.y + 7 + p.y, 8, ref);
            toStrait = ret.p;
            if (ret.i instanceof ReactLaser) {
                ((ReactLaser) ret.i).addSource(this, rot.next().reverse());
                retLasers.add((Laser) ret.i);
            }
        }
        //if (lasersPointing[rot.next().ordinal()] != null && lasersPointing[rot.next().ordinal()].getState(rot.next().reverse()) != 0)
        {
            Point p = rot.getRotation(7, 0);
            Direction.Pair ret = rot.raycastPointInst(e.getMainView(), e.getGameObjects(), self.x + 7 + p.x, self.y + 7 + p.y, 8, ref);
            toBent = ret.p;
            if (ret.i instanceof ReactLaser) {
                ((ReactLaser) ret.i).addSource(this, rot.reverse());
                retLasers.add((Laser) ret.i);
            }
        }
        //}
        return retLasers.toArray(new Laser[retLasers.size()]);
//        if (!lasersPointing[rot.ordinal()].isEmpty()) {
//            strait = false;
//            Point p = rot.next().getRotation(7, 0);
//            Direction.Pair ret = rot.next().raycastPointInst(e.getMainView(), e.getGameObjects(), self.x + 7 + p.x, self.y + 7 + p.y, 8, ref);
//            to = ret.p;
//            if (ret.i instanceof ReactLaser) {
//                ((ReactLaser) ret.i).addSource(this, rot.next().reverse());
//                return new Laser[]{((Laser) ret.i)};
//            } else {
//                return new Laser[0];
//            }
//            //e.getMainView().getDraw().drawLine(self.x + 7 + p.x, self.y + 7 + p.y, to.x, to.y);
//        } else if (!lasersPointing[rot.next().ordinal()].isEmpty()) {
//            Point p = rot.getRotation(7, 0);
//            Direction.Pair ret = rot.raycastPointInst(e.getMainView(), e.getGameObjects(), self.x + 7 + p.x, self.y + 7 + p.y, 8, ref);
//            to = ret.p;
//            //e.getMainView().getDraw().drawLine(self.x + 7 + p.x, self.y + 7 + p.y, to.x, to.y);
//            strait = true;
//            if (ret.i instanceof ReactLaser) {
//                ((ReactLaser) ret.i).addSource(this, rot.reverse());
//                return new Laser[]{((Laser) ret.i)};
//            } else {
//                return new Laser[0];
//            }
//        } else {
//            to = null;
//            return new Laser[0];
//        }
    }

    @Override
    public int getState(Direction from) {
        if (from == rot) {
            return getLaser(rot.next());
        } else if (from == rot.next()) {
            return getLaser(rot);
        } else {
            return 0;
        }
    }

    @Override
    public boolean pointCollides(int x, int y) {
        return self.contains(x, y);
    }

    @Override
    public void drawSelf(GameEngine e) {
        if (e.getMainView().isInView(self)){
            e.getMainView().setColor(Color.CYAN);

            Polygon template = LaserMirror.tris[rot.ordinal()];
            Polygon triangle = new Polygon(template.xpoints, template.ypoints, template.npoints);
            triangle.translate(self.x, self.y);

            e.getMainView().renderPolygon(triangle);

            if (toStrait != null && getLaser(rot.next()) != 0) {
                Point p = rot.getRotation(7, 0);
                e.getMainView().setColor(Color.RED);
                e.getMainView().renderLine(self.x + 7 + p.x, self.y + 7 + p.y, toBent.x, toBent.y);
            }
            if (toBent != null && getLaser(rot) != 0) {
                Point p = rot.next().getRotation(7, 0);
                e.getMainView().setColor(Color.RED);
                e.getMainView().renderLine(self.x + 7 + p.x, self.y + 7 + p.y, toStrait.x, toStrait.y);

            }
        }
    }

    private static final Polygon[] tris;

    static {
        tris = new Polygon[4];
        Polygon p = new Polygon();
        p.addPoint(0, W_H-1);
        p.addPoint(0, 0);
        p.addPoint(W_H-1, 0);
        tris[0] = p;

        p = new Polygon();
        p.addPoint(0, 0);
        p.addPoint(W_H-1, 0);
        p.addPoint(W_H-1, W_H-1);
        tris[1] = p;

        p = new Polygon();
        p.addPoint(W_H-1, 0);
        p.addPoint(W_H-1, W_H-1);
        p.addPoint(0, W_H-1);
        tris[2] = p;

        p = new Polygon();
        p.addPoint(W_H-1, W_H-1);
        p.addPoint(0, W_H-1);
        p.addPoint(0, 0);
        tris[3] = p;

    }
}
