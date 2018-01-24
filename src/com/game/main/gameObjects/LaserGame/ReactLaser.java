package com.game.main.gameObjects.LaserGame;

import com.game.main.engine.GameEngine;
import com.game.main.engine.util.Direction;

/**
 * Created by SuperRainbowNinja on 23/01/2018.
 */
abstract public class ReactLaser extends Laser {
    public ReactLaser(GameEngine e, int x, int y, Direction rotIn) {
        super(e, x, y, rotIn);

        lasersPointing = new Laser[4];
        for (int i = 0; i < lasersPointing.length; i++) {
            lasersPointing[i] = null;
        }
    }

    protected Laser[] lasersPointing;

    public void addSource(Laser laser, Direction from) {
        lasersPointing[from.ordinal()] = laser;
    }

    public int getLaser(Direction from) {
        return lasersPointing[from.ordinal()] != null ? lasersPointing[from.ordinal()].getState(from.reverse()) : 0;
    }

    abstract public Laser[] getTargets(GameEngine e, Direction from);
}
