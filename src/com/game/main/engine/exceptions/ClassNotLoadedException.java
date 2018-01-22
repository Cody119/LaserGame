package com.game.main.engine.exceptions;

/**
 * Created by AND0053 on 9/06/2016.
 *
 * This is essentially class not found, but the ancestor helps with error handling (mostly its neater)
 */
public class ClassNotLoadedException extends GameEngineException {
    public ClassNotLoadedException(String msg) {
        super(msg);
    }
    public ClassNotLoadedException(ClassNotFoundException msg) {
        super("Failed to load module - " + msg.getMessage());
    }

}
