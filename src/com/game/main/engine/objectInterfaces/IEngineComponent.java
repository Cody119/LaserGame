package com.game.main.engine.objectInterfaces;

import com.game.main.engine.GameEngine;

/**
 * Created by AND0053 on 3/06/2016.
 */
public interface IEngineComponent {
    void loadComponent(GameEngine g);
    void unloadComponent(GameEngine g);
}
