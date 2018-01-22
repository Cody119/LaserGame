package com.game.main;

import com.game.main.components.MainWindow;


/**
 * Created by and0053 on 24/03/2015.
 */
public class test {
    public static void main(String[] args) throws Exception{

        MainWindow window = new MainWindow("Name");
        window.mainGame.addTask((g) -> {
            g.getIoHandle().printString("Hello world from the main thread");
        });

    }
}