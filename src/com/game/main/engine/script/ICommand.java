package com.game.main.engine.script;

import com.game.main.engine.exceptions.GameEngineException;
import com.game.main.engine.exceptions.GameEngineRuntimeException;

/**
 * Created by AND0053 on 3/06/2016.
 */

@FunctionalInterface
public interface ICommand {
    Object invoke(ICommandHandle s, Object ... params) throws GameEngineRuntimeException, GameEngineException;
}
