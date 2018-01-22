package com.game.main.gameObjects;

import com.game.main.engine.GameEngine;
import com.game.main.engine.GameObjectList;
import com.game.main.engine.objectInterfaces.IInstance;
import com.game.main.engine.runtimeRefrence.InstanceRef;

import java.awt.*;

/**
 * Created by AND0053 on 20/04/2015.
 */
public class MotionObject implements IInstance {
    public Rectangle self;

    private double vSpeed;
    private double hSpeed;
    private double vFriction;
    private double hFriction;
    private InstanceRef ref;

    public static final int hLimit = 20;
    public static final int vLimit = 20;

    public MotionObject(){
        this(0, 0, 16, 16);
    }

    public MotionObject(int x, int y, int width, int height){
        self = new Rectangle(x, y, width - 1, height - 1);
        vSpeed = 0;
        hSpeed = 0;
        hFriction = 0.1;
        vFriction = 0.1;
    }

    public boolean step(GameEngine engine, long deltaTime) {
        final GameObjectList gameObjects = engine.getGameObjects();

        //Speed limiter
        if (vSpeed > vLimit)
            vSpeed = vLimit;
        if (vSpeed < -1*vLimit)
            vSpeed = -1*vLimit;
        if (hSpeed > hLimit)
            hSpeed = hLimit;
        if (hSpeed < -1*hLimit)
            hSpeed = -1*hLimit;

        if (vSpeed < vLimit )
            vSpeed++;


        boolean test = true;

        int count = vSpeed > 0 ? (int)Math.ceil(vSpeed) : (int)Math.floor(vSpeed);

        int x1 = self.x;
        int y1 = count < 0 ? self.y : self.y + self.height;
        int x2 = self.x + self.width;
        int y2 = y1;

        for (int i = count; i != 0; i += (i/Math.abs(i))*-1) {
            if (!(gameObjects.pointCollides(x1, y1 + i, ref) || gameObjects.pointCollides(x2, y2 + i, ref))) {
                self.y += i;
                test = false;
                break;
            }
        }
        if (test){
            vSpeed = 0;
            hFriction = 0.2;
        } else {
            hFriction = 0.1;
        }

        if (hSpeed != 0){
            if (hSpeed > -0.4 && hSpeed < 0.4 && test)
                hSpeed = 0;
            hSpeed += hSpeed*-1*hFriction;
        }

        test = true;
        count = hSpeed > 0 ? (int)Math.ceil(hSpeed) : (int)Math.floor(hSpeed);

        for (int i = count; i != 0; i += (i/Math.abs(i))*-1) {
            if (!(
                    gameObjects.pointCollides(self.x + self.width + i, self.y + self.height, ref)
                    || gameObjects.pointCollides(self.x + i, self.y + self.height, ref)
                    || gameObjects.pointCollides(self.x + i, self.y, ref)
                    || gameObjects.pointCollides(self.x + self.width + i, self.y, ref))
            ) {
                self.x += i;
                test = false;
                break;
            }
        }
        if (test){
            hSpeed = 0;
        }
        vSpeed += vSpeed*-1*vFriction;


        return true;
    }

    public boolean addHForce(double f){
        hSpeed += f;
        return true;
    }

    public boolean addVForce(double f){
        vSpeed += f;
        return true;
    }

    @Override
    public void drawSelf(GameEngine e) {
        if (e.getMainView().isInView(self)){
            e.getMainView().setColor(Color.ORANGE);
            e.getMainView().renderGameRect(self.x + 1, self.y + 1, self.width, self.height, false);
        }
    }

    @Override
    public boolean pointCollides(int x, int y) {
        return self.contains(x, y);
    }

    public void setInstanceRef(InstanceRef r) {
        ref = r;
    }

    @Override
    public InstanceRef getInstanceRef() {
        return ref;
    }
}
