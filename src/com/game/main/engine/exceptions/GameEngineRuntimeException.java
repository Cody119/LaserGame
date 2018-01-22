package com.game.main.engine.exceptions;

/**
 * Created by AND0053 on 9/06/2016.
 *
 * Common exception for all the exceptions used by the engine
 */
public class GameEngineRuntimeException extends RuntimeException {
    public GameEngineRuntimeException(String msg) {
        super(msg);
    }
    public GameEngineRuntimeException() {
        super();
    }
}
