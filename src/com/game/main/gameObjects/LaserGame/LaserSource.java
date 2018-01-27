package com.game.main.gameObjects.LaserGame;

import com.game.main.engine.GameEngine;
import com.game.main.engine.util.Direction;

import java.awt.*;

/**
 * Created by SuperRainbowNinja on 21/01/2018.
 */
public class LaserSource extends Laser {
    private Point to;

    public LaserSource(GameEngine e, int x, int y, Direction rotIn) {
        super(e, x, y, rotIn);
        reset(e);
    }

    @Override
    Laser[] reset(GameEngine e) {
        Point p = rot.getRotation(7, 0);
        Direction.Pair toIn = rot.raycastPointInst(e.getMainView(), e.getGameObjects(), self.x + 7 + p.x, self.y + 7 + p.y, 8, ref);
        if (toIn.i instanceof ReactLaser) {
            ((ReactLaser) toIn.i).addSource(this, rot.reverse());
            to = toIn.p;
            return new Laser[]{((Laser) toIn.i)};
        }
        to = toIn.p;
        return new Laser[0];
    }

    @Override
    public int getState(Direction from) {
        if (from == rot) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isSource() {
        return true;
    }

    @Override
    public boolean pointCollides(int x, int y) {
        return self.contains(x, y);
    }

    @Override
    public void drawSelf(GameEngine e) {
        if (e.getMainView().isInView(self)){
            e.getMainView().setColor(Color.GREEN);
            e.getMainView().renderGameRect(self.x, self.y, self.width - 1, self.height - 1, false);
            e.getMainView().setColor(Color.RED);
            Point p = rot.getRotation(7, 0);
            e.getMainView().renderGameRect(self.x + 7 + p.x, self.y + 7 + p.y, 2, 2, true);

            e.getMainView().renderLine(self.x + 7 + p.x, self.y + 7 + p.y, to.x, to.y);
        }
    }
}
