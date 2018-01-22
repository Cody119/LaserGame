package com.game.main.engine.script;

import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IEngineComponent;
import com.game.main.engine.util.Lex;

/**
 * Created by AND0053 on 3/06/2016.
 */
public interface ICommandHandle extends IEngineComponent{
    GameEngine getGameEngine();
    //return false if name is taken
    boolean addCommand(String name, ICommand f);
    void runCommand(String[] s);
    default void runCommand(String s) {
        runCommand(Lex.lex(s));
    }

    Object getVar(String s);
    void setVar(String name, Object val);
    //not sure about this one, seems a little to not flexible
    Object getLastReturn();
}
