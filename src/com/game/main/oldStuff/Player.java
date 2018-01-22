package com.game.main.oldStuff;

/**
 * Created by AND0053 on 12/04/2015.
 * Temporary Object -------- needs to be replaced with a more intergrated version (see ToDoList)
 */

public class Player {// implements IGameObject {
    /*
    public Rectangle self; //change to private
    public Color color;

    private int vSpeed;
    private int hSpeed;

    public static final int hLimit = 2;
    public static final int vLimit = 20;

    public Player(int x, int y){
        self = new Rectangle(x, y, 15, 15);
        color = Color.BLACK;
        vSpeed = 0;
        hSpeed = 0;
    }

    @Override
    public void drawSelf(View v) {
        if (v.isInView(self)){
            v.setColor(color);
            v.renderGameRect(self.x, self.y, self.width + 1, self.height + 1);
        }
    }

    @Override
    public boolean pointCollidesEx(int x, int y, IGameObject exclude) {
        if (self.contains(x, y))
            return true;
        return false;
    }

    @Override
    public boolean pointCollides(int x, int y) {
        if (self.contains(x, y))
            return true;
        return false;
    }

    public void step(IGameObject w, KeyBoard k) {
        if (k.keyCheckPressed(KeyEvent.VK_UP) &&
                (w.pointCollidesEx(self.x, self.y + self.height + 1, this) ||
                        w.pointCollidesEx(self.x + self.width, self.y + self.height + 1, this))){
            vSpeed = -10;
        }

        if (vSpeed < vLimit )
            vSpeed++;
        boolean test = true;
        for (int i = vSpeed; i != 0; i += (i/Math.abs(i))*-1) {
            if (!(w.pointCollidesEx(self.x + self.width, self.y + self.height + i, this) || w.pointCollidesEx(self.x, self.y + self.height + i, this) ||
                w.pointCollidesEx(self.x, self.y + i, this) || w.pointCollidesEx(self.x + self.width, self.y + i, this))) {
                self.y += i;
                test = false;
                break;
            }
        }
        if (test){
            vSpeed = 0;
        }

        test = true;
        if (k.keyCheck(KeyEvent.VK_RIGHT)){
            if (hLimit > hSpeed)
                hSpeed+=1;
            test = false;
        }
        if (k.keyCheck(KeyEvent.VK_LEFT)){
            if (-1 * hLimit < hSpeed)
            hSpeed-=1;
            test = false;
        }
        if (test && hSpeed != 0){
            hSpeed += hSpeed/Math.abs(hSpeed)*-1;
        }

        test = true;
        for (int i = hSpeed; i != 0; i += (i/Math.abs(i))*-1) {
            if (!(w.pointCollidesEx(self.x + self.width + i, self.y + self.height, this) || w.pointCollidesEx(self.x + i, self.y + self.height, this) ||
                    w.pointCollidesEx(self.x + i, self.y, this) || w.pointCollidesEx(self.x + self.width + i, self.y, this))) {
                self.x += i;
                test = false;
                break;
            }
        }
        if (test){
            hSpeed = 0;
        }
    }
    */
}
