package com.game.main.engine.objectInterfaces;

import com.game.main.engine.GameEngine;
import com.game.main.engine.util.StringRef;

/**
 * Created by AND0053 on 2/06/2016.
 *
 */
public interface IIOHandle extends IEngineComponent{
    void printString(String s);
    //This is an event which says the user has requested to start typing
    void startTyping(GameEngine g);
    boolean isTyping();
    //Called every tick while isTyping is true
    void typeTick(GameEngine g);
}
