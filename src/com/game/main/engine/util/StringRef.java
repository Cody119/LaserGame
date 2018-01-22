package com.game.main.engine.util;

/**
 * Created by AND0053 on 30/05/2016.
 *
 * A shared string
 */
public class StringRef {
    private String val;

    public StringRef() {
        val = "";
    }

    public StringRef(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val != null ? val : "";
    }
}
