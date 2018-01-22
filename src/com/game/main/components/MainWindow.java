package com.game.main.components;

import com.game.main.engine.GameEngine;
import com.game.main.input.IMouseGetter;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * Created by AND0053 on 12/04/2015.
 *
 * TODO Figure out if this is a separate thread or not
 * TODO switch to swing?
 */
public class MainWindow extends Frame{

    public final String TITLE;
    public View mainView;
    public GameEngine mainGame;

    public MainWindow(String name){
        super();
        mainView = new View(new Dimension(800, 600));

        TITLE = name;
        setTitle(TITLE);
        add(mainView);
        pack();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
                                    public void windowClosing(WindowEvent we) {
                                        mainGame.stop();
                                        dispose();
                                    }
                                }
        );
        mainGame = new GameEngine(
                mainView,
                //Lambda function used to get the mouse location
                () -> {
                    Point tmp = MouseInfo.getPointerInfo().getLocation();
                    Point loc = getLocation();
                    return new Point(tmp.x - loc.x, tmp.y - loc.y);
                },
                true
        );
    }
}
