package com.game.main.gameObjects;

import com.game.main.components.View;
import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IDrawableObject;
import com.game.main.engine.objectInterfaces.IGameObject;
import com.game.main.engine.objectInterfaces.IIOHandle;
import com.game.main.engine.objectInterfaces.ReferenceField;
import com.game.main.engine.runtimeRefrence.ObjectRef;
import com.game.main.engine.util.StringRef;

import java.util.ArrayList;

/**
 * Created by AND0053 on 30/05/2016.
 */
public class Log implements IGameObject, IDrawableObject, IIOHandle {
    public static final int DEFAULT_XPAD = 5;
    public static final int DEFAULT_YPAD = 5;
    public static final int DEFAULT_YSPACE = 0;

    private ArrayList<String> log;
    //This one is used as an easy way to display whatever is currently being typed
    public StringRef textBuf;
    public StringRef getTextBuf() {
        return textBuf;
    }
    public void setTextBuf(StringRef textBuf) {
        this.textBuf = textBuf;
    }

    private int xPad, yPad, ySpace;

    public Log(int xPad, int yPad, int ySpace) {
        log = new ArrayList<>();
        this.xPad = xPad;
        this.yPad = yPad;
        this.ySpace = ySpace;
        textBuf = null;
    }

    public Log() {
        this(DEFAULT_XPAD, DEFAULT_YPAD, DEFAULT_YSPACE);
    }

    public String pushStringRef() {
        if (textBuf != null) {
            printString(textBuf.getVal());
            textBuf = null;
            return log.get(log.size() - 1);
        }
        return null;
    }

    @Override
    public void loadComponent(GameEngine g) {}

    @Override
    public void unloadComponent(GameEngine g) {

    }

    @Override
    public void printString(String s) {
        log.add(s);
    }

    @Override
    public void startTyping(GameEngine g) {
        setTextBuf(g.getKeyBoard().startTyping());
    }

    @Override
    public boolean isTyping() {
        return textBuf != null;
    }

    @Override
    public void typeTick(GameEngine g) {
        if (!g.getKeyBoard().isTyping()) {
            g.pushProcString(pushStringRef());
        }
    }

    @Override
    public void drawSelf(GameEngine engine) {
        View v = engine.getMainView();
        int fH = v.getFontHeight();
        int vH = v.viewRect.height;
        int lS = log.size();
        for (int i = 0; i < lS; i++) {
            v.drawGUIText(log.get(i), xPad,
                    vH
                    - (fH + ySpace)*(lS - 1 - i) //this bit is a bit wierd and hard to explain
                    - (fH + ySpace) //This part here accounts for the textBuf being drawn
                    - yPad //y padding
                    //this expression could be simplified to
                    //vH - (fH + ySpace)*(lS - i) - yPad
                    //But in doing so this already hard to understand equation would be harder to understand
                    //i.e. leave it in for readability
            );
        }
        if (textBuf != null)
            v.drawGUIText(textBuf.getVal(), xPad, vH - yPad);
    }

    @ReferenceField
    private ObjectRef ref = null;

    @Override
    public void loadObject(GameEngine e) {

    }


    @Override
    public ObjectRef getObjectRef() {
        return ref;
    }
}
