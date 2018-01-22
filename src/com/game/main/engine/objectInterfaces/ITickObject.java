package com.game.main.engine.objectInterfaces;

import com.game.main.engine.GameEngine;

/**
 * Created by AND0053 on 19/04/2015.
 */
public interface ITickObject {
    boolean step(GameEngine g, long deltaTime);
}
