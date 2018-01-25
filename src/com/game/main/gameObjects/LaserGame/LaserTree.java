package com.game.main.gameObjects.LaserGame;

import com.game.main.engine.GameEngine;

import java.util.ArrayList;

/**
 * Created by SuperRainbowNinja on 23/01/2018.
 */
public class LaserTree {
    public Laser[] sources;

    public LaserTree(Laser[] lasers, GameEngine engine) {
        Node[] codes = new Node[lasers.length];
        for (int i = 0; i < codes.length; i++) {
            codes[i] = new Node(lasers[i], lasers[i].getTargets(engine));
        }

    }

    public static Node[] buildTree(Laser[] lasers, GameEngine engine, boolean[] flags) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < lasers.length; i++) {
            if (flags[lasers[i].ref.ref]) {

            }
        }
        return nodes;
    }


    public class Node {
        private Node[] sources;
        private Laser thisLaser;

        public Node(Laser laser, Laser[] targets) {
            thisLaser = laser;
        }
    }

}
