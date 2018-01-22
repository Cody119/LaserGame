package com.game.main.input;

import com.game.main.components.View;
import com.game.main.engine.util.StringRef;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by AND0053 on 12/04/2015.
 *
 * TODO cpy paste
 */
public class KeyBoard implements KeyListener {
    public enum TypingState { NOT_TYPING, TYPING, START_TYPING}
    private TypingState typingState;
    public boolean isTyping() {return typingState != TypingState.NOT_TYPING;}

    private IMouseGetter mouseHandle;

    private StringRef typBuf;
    public StringRef getTypBuf() {return typBuf;}

    private boolean[] keyPressed;
    private boolean[] keyReleased;
    private boolean[] keyDown;

    public static final int NO_OF_KEYS = 127;
    //surley this already exsists somewhere
    public static final char NEW_LINE = '\n';

    public KeyBoard(IMouseGetter mouse){
        mouseHandle = mouse;
        typBuf = null;
        typingState = TypingState.NOT_TYPING;
        keyPressed = new boolean[NO_OF_KEYS];
        keyReleased = new boolean[NO_OF_KEYS];
        keyDown = new boolean[NO_OF_KEYS];
        wipKeyStates();
    }

    public boolean keyCheckPressed(int key){
        return keyPressed[key];
    }

    public boolean keyCheckReleased(int key){
        return keyReleased[key];
    }

    public boolean keyCheck(int key){
        return keyDown[key];
    }

    public void wipKeyStates(){
        for (int i = 0; i < keyDown.length; i++){
            keyDown[i] = false;
        }
        wipPressRelease();
    }

    public void wipPressRelease(){
        for (int i = 0; i < keyPressed.length; i++){
            keyPressed[i] = false;
        }
        for (int i = 0; i < keyReleased.length; i++){
            keyReleased[i] = false;
        }
    }

    public Point getMousePos(View v){ //temporary, will move
        Point comp = v.getLocation();
        Point mouse = mouseHandle.getMouse();
        return new Point(
                mouse.x - comp.x + v.viewRect.x,
                mouse.y - comp.y + v.viewRect.y
        );
    }

    public StringRef startTyping() {
        if (typingState == TypingState.NOT_TYPING) {
            typingState = TypingState.START_TYPING;
            return (typBuf = new StringRef());
        }
        return null;
    }

    public static boolean isSymbol(char c) {
        return (c >= 32 && c <= 126);
    }

    //There's this weird thing when holding control the charecters turn out wrong, e.g. ctrl + m is the newline char
    //I dont know y but for now all none symbol char's are ignored
    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if (typingState == TypingState.NOT_TYPING) return;

        if (typingState == TypingState.START_TYPING) {
            wipKeyStates();
            typingState = TypingState.TYPING;
        }
        if (keyEvent.getKeyChar() == NEW_LINE) {
            typingState = TypingState.NOT_TYPING;
            typBuf = null;

        } /*else if (keyEvent.getKeyChar() == (char)22) {
            try {
                System.out.println("paste?");
                typBuf.setVal(typBuf.getVal() + ((String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)));
            } catch (Exception e) {
                //Do nothing
            }
        }*/ else if (keyEvent.getKeyChar() == '\b' && typBuf.getVal().length() > 0) {
            typBuf.setVal(typBuf.getVal().substring(0, typBuf.getVal().length() - 1));
        } else if (isSymbol(keyEvent.getKeyChar())){
            typBuf.setVal(typBuf.getVal() + keyEvent.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (typingState != TypingState.NOT_TYPING) return;

        int key = keyEvent.getKeyCode();
        if (key >= 0 && key < NO_OF_KEYS && !keyDown[key]) {
                keyPressed[key] = true;
                keyDown[key] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (typingState != TypingState.NOT_TYPING) return;

        int key = keyEvent.getKeyCode();
        if (key >= 0 && key < NO_OF_KEYS && keyDown[key]) {
                keyReleased[key] = true;
                keyDown[key] = false;
        }
    }
}
