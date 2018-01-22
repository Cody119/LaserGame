package com.game.main.engine.util;

import com.game.main.components.View;
import com.game.main.engine.GameObjectList;
import com.game.main.engine.objectInterfaces.IInstance;
import com.game.main.engine.objectInterfaces.IWorldObject;
import com.game.main.engine.runtimeRefrence.InstanceRef;

import java.awt.*;

/**
 * Created by SuperRainbowNinja on 21/01/2018.
 */
public enum Direction {
    RIGHT(1, 0, 0, 1),
    UP(0, -1, 1, 0),
    LEFT(-1, 0, 0, -1),
    DOWN(0, 1, -1, 0);

    int p11, p12, p21, p22;

    Direction(int one, int two, int three, int four) {
        p11 = one;
        p12 = two;
        p21 = three;
        p22 = four;
    }

    public Point getDirectionVector() {
        return new Point(p22, p21);
    }

    public IWorldObject raycastObject(View view, GameObjectList list, int xStart, int yStart, int speed, InstanceRef exclude) {
        int x = xStart, y = yStart;
        Point p = getDirectionVector();
        p.setLocation(p.x * speed, p.y * speed);
        while (view.viewRect.contains(x, y)) {
            IWorldObject object = list.pointCollidesObj(x, y, exclude);
            if (object != null) {
                return object;
            }
            x += p.x;
            y += p.y;
        }
        return null;
    }

    public Direction reverse() {
        switch (this) {
            case UP:
                return DOWN;
            case LEFT:
                return RIGHT;
            case DOWN:
                return UP;
            case RIGHT:
                return LEFT;
            default:
                return RIGHT;
        }
    }

    public static class Pair {
        public Point p;
        public IInstance i;

        public Pair(Point pIn, IInstance iInstance) {
            p = pIn;
            i = iInstance;
        }
    }

    public Pair raycastPointInst(View view, GameObjectList list, int xStart, int yStart, int speed, InstanceRef exclude) {
        int x = xStart, y = yStart;
        IInstance inst;
        Point p = getDirectionVector();
        Point speedVec = new Point(p.x * speed, p.y * speed);
        while (view.viewRect.contains(x, y)) {
            if ((inst = list.pointCollidesInst(x, y, exclude)) != null) {
                do {
                    x -= p.x;
                    y -= p.y;

                } while(list.pointCollides(x, y, exclude));
                return new Pair(new Point(x, y), inst);
            }
            x += speedVec.x;
            y += speedVec.y;
        }
        return new Pair(new Point(x, y), null);
    }

    public Point raycastPoint(View view, GameObjectList list, int xStart, int yStart, int speed, InstanceRef exclude) {
        int x = xStart, y = yStart;
        Point p = getDirectionVector();
        Point speedVec = new Point(p.x * speed, p.y * speed);
        while (view.viewRect.contains(x, y)) {
            if (list.pointCollides(x, y, exclude)) {
                do {
                    x -= p.x;
                    y -= p.y;

                } while(list.pointCollides(x, y, exclude));
                return new Point(x, y);
            }
            x += speedVec.x;
            y += speedVec.y;
        }
        return new Point(x, y);
    }

    public Point getRotation(int x, int y) {
        return new Point(p11*x + p12*y, p21*x + p22*y);
    }

    public Direction next() {
        switch (this) {
            case UP:
                return LEFT;
            case LEFT:
                return DOWN;
            case DOWN:
                return RIGHT;
            case RIGHT:
                return UP;
            default:
                return RIGHT;
        }
    }
}
