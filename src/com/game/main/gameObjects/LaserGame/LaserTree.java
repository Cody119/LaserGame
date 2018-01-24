package com.game.main.gameObjects.LaserGame;

import com.game.main.engine.GameEngine;

import java.util.ArrayList;

/**
 * Created by SuperRainbowNinja on 23/01/2018.
 */
public class LaserTree {
    public Laser[] sources;

    public LaserTree(ArrayList<Laser> lasers, GameEngine engine) {
        Node[] codes = new Node[lasers.size()];
        for (int i = 0; i < codes.length; i++) {
            codes[i] = new Node(lasers.get(i));
        }

    }

    public class Node {
        private ArrayList<Node> sources = new ArrayList<>();
        private Laser thisLaser;

        public Node(Laser laser) {
            thisLaser = laser;
        }
    }

}
