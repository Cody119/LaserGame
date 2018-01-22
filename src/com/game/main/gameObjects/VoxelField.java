package com.game.main.gameObjects;

import com.game.main.components.View;
import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.*;
import com.game.main.engine.runtimeRefrence.InstanceRef;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by AND0053 on 12/04/2015.
 */

public class VoxelField implements IInstance {
    private boolean[][] state; //the state is whether the point exists of not
    private int[][] data; //this is the data stored at the point
    private InstanceRef ref;

    @FunctionalInterface
    public interface DrawFunction {
        void draw(VoxelField o, View v, int x, int y);
    }

    private DrawFunction drawFunction;

    private int scale;
    public Dimension size;

    public VoxelField(int xSize, int ySize, int scale) {
        this(xSize, ySize, scale, (data, v, x, y) -> {
            int localScale = data.getScale();
            v.setColor(Color.GREEN);
            v.renderGameRect((x * localScale), (y * localScale), localScale, localScale, false);
        });
    }

    public VoxelField(int xSize, int ySize, int scale, DrawFunction drawFunctionIn){
        newArray(xSize, ySize);
        size = new Dimension(xSize, ySize);
        this.scale = scale;
        drawFunction = drawFunctionIn;
    }

    //Accessors and mutators
    public int getScale(){
        return scale;
    }

    public boolean setScale(int scale){
        if (scale > 0){
            this.scale = scale;
            return true;
        }
        return false;
    }

    public boolean getPosState(int x, int y){
        if (pointExists(x, y))
            return state[x][y];
        else
            throw new IndexOutOfBoundsException("Not on the Grid");
    }

    public boolean getPosState(Point p){
        if (pointExists(p))
            return state[p.x][p.y];
        else
            throw new IndexOutOfBoundsException("Not on the Grid");

    }

    public boolean setPosState(int x, int y, boolean val){
        if (pointExists(x, y)){
            state[x][y] = val;
            return true;
        }
        return false;
    }

    public boolean setPosState(Point p, boolean val){
        if (pointExists(p)){
            state[p.x][p.y] = val;
            return true;
        }
        return false;
    }

    public void setAllState(boolean val){
        for (int i = 0; i < size.width; i++){
            for (int j = 0; j < size.height; j++){
                state[i][j] = val;
            }
        }
    }

    public void setArrayState(boolean[] use, int xStart, int yStart){
        for (int i = 0; i < use.length; i++){
            if (pointExists(i, yStart))
                setPosState(i + xStart, yStart, use[i]);
        }
    }

    public void setArrayState(boolean[][] use, int xStart, int yStart){
        for (int x = 0; x < use.length; x++){
            for (int y = 0; y < use[x].length; y++) {
                if (pointExists(x, y))
                    setPosState(x + xStart, y + yStart, use[x][y]);
            }
        }
    }

    public int getPosData(int x, int y){
        if (pointExists(x, y))
            return data[x][y];
        else
            throw new IndexOutOfBoundsException("Not on the Grid");
    }

    public int getPosData(Point p){
        if (pointExists(p))
            return data[p.x][p.y];
        else
            throw new IndexOutOfBoundsException("Not on the Grid");
    }

    public boolean setPosData(int x, int y, int val){
        if (pointExists(x, y)){
            data[x][y] = val;
            return true;
        }
        return false;
    }

    public boolean setPosData(Point p, int val){
        if (pointExists(p)){
            data[p.x][p.y] = val;
            return true;
        }
        return false;
    }

    public void setAllData(int val){
        for (int i = 0; i < size.width; i++){
            for (int j = 0; j < size.height; j++){
                data[i][j] = val;
            }
        }
    }

    public void setArrayData(int[] use, int xStart, int yStart){
        for (int i = 0; i < use.length; i++){
            if (pointExists(i, yStart))
                setPosData(i + xStart, yStart, use[i]);
        }
    }

    public void setArrayData(int[][] use, int xStart, int yStart){
        for (int x = 0; x < use.length; x++){
            for (int y = 0; y < use[x].length; y++) {
                if (pointExists(x, y))
                    setPosData(x + xStart, y + yStart, use[x][y]);
            }
        }
    }

    public boolean pointExists(Point g){ //checks if the point is on the grid
        return g.x >= 0 && g.y >= 0 && g.x < size.width && g.y < size.height;
    }

    public boolean pointExists(int x, int y){ //checks if the point is on the grid
        return x >= 0 && y >= 0 && x < size.width && y < size.height;
    }

    //other stuff

    public int coToGrid(int val){ //converts a cartesian co into a grid co
        return (int)Math.floor((double)val/scale);
    }

    public Point coToGrid(int x, int y){ //converts a cartesian point into a grid point
        return new Point(coToGrid(x), coToGrid(y));
    }

    public Point coToGrid(Point p){ //converts a cartesian point into a grid point
        return new Point(coToGrid(p.x), coToGrid(p.y));
    }

    public boolean newArray(int xSize, int ySize){ //remakes the arrays
        if (xSize > 0 && ySize > 0){
            state = new boolean[xSize][ySize];
            data = new int[xSize][ySize];
            return true;
        }
        return false;
    }

    public Point nextGridPoint(Point g){ //finds the closest existing grid point to the grid point supplied
        Point val = g;
        if (pointExists(val))
            return val;
        else {
            if (val.x < 0)
                val.x = 0;
            if (val.y < 0)
                val.y = 0;
            if (val.x >= size.width)
                val.x = size.width - 1;
            if (val.y >= size.height - 1)
                val.y = size.height - 1;
            return val;
        }
    }

    //Implemented methods
    @Override
    public boolean pointCollides(int  x, int y) {
        Point p = coToGrid(x, y);
        if (pointExists(p)){
            if (getPosState(p.x, p.y)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawSelf(GameEngine engine) { //draws only the part that is on the screen
        final View v = engine.getMainView();

        Point start = nextGridPoint(coToGrid(v.viewRect.getLocation()));
        Point end = nextGridPoint(coToGrid(v.viewRect.x + v.viewRect.width,
                v.viewRect.y + v.viewRect.height));
        v.setColor(Color.black);

        for (int x = start.x; x <= end.x; x++) {
            for (int y = start.y; y <= end.y; y++) {
                if (getPosState(x, y)) {
                    drawFunction.draw(this, v, x, y);
                }
            }
        }
    }

    @Override
    public void setInstanceRef(InstanceRef r) {
        ref = r;
    }

    @Override
    public InstanceRef getInstanceRef() {
        return ref;
    }
}