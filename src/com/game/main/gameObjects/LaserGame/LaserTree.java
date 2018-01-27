package com.game.main.gameObjects.LaserGame;

import com.game.main.engine.GameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SuperRainbowNinja on 23/01/2018.
 */
public class LaserTree {
    public Node[] nodes;
    public Node[] sources;

    public LaserTree(Laser[] lasers, GameEngine engine) {
        nodes = new Node[lasers.length];
        ArrayList<Laser> sourceLasers = new ArrayList<>();
        for (Laser laser : lasers) {
            if (laser.isSource()) {
                sourceLasers.add(laser);
            }
        }

        ArrayList<Node> newNodes = buildTree(sourceLasers.toArray(new Laser[sourceLasers.size()]), engine, new boolean[nodes.length]);
        sources = newNodes.toArray(new Node[newNodes.size()]);
//        for (int i = 0; i < codes.length; i++) {
//            codes[i] = new Node(lasers[i], lasers[i].getTargets(engine));
//        }

    }

    private ArrayList<Node> buildTree(Laser[] lasers, GameEngine engine, boolean[] flags) {
        if (lasers.length == 0)
            return new ArrayList<>();

        ArrayList<Node> newNodes = new ArrayList<>();

        for (Laser laser : lasers) {
            if (!flags[laser.ref.ref]) {
                flags[laser.ref.ref] = true;
                Laser[] toAdd = laser.getTargets(engine);
                nodes[laser.ref.ref] = new Node(laser, buildTree(toAdd, engine, flags));
            }
            newNodes.add(nodes[laser.ref.ref]);
        }
        return newNodes;
    }


    static class Node {
        private Node[] targets;
        private Laser thisLaser;

        Node(Laser laser, List<Node> targetsIn) {
            thisLaser = laser;
            targets = targetsIn.toArray(new Node[targetsIn.size()]);
        }
    }

}
