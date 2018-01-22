package com.game.main.engine.exceptions;

/**
 * Created by AND0053 on 9/06/2016.
 */
public class IllegalCommandArgumentException extends GameEngineRuntimeException {
    public IllegalCommandArgumentException(String msg) {
        super(msg);
    }
    public IllegalCommandArgumentException() {
        super();
    }
}
