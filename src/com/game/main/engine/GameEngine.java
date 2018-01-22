package com.game.main.engine;

import com.game.main.engine.objectInterfaces.IDrawableObject;
import com.game.main.engine.objectInterfaces.IGameObject;
import com.game.main.engine.objectInterfaces.IIOHandle;
import com.game.main.engine.objectInterfaces.ReferenceField;
import com.game.main.engine.script.DefaultCommandHandle;
import com.game.main.engine.script.ICommandHandle;
import com.game.main.engine.util.Direction;
import com.game.main.engine.util.GameObject;
import com.game.main.gameObjects.*;
import com.game.main.components.View;
import com.game.main.engine.modules.Module;
import com.game.main.engine.modules.ModuleManager;
import com.game.main.engine.runtimeRefrence.ObjectRef;
import com.game.main.gameObjects.LaserGame.LaserGridHandle;
import com.game.main.input.IMouseGetter;
import com.game.main.input.KeyBoard;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by AND0053 on 12/04/2015.
 *
 * TODO look at the whole instance object system and maybe revise it a bit?
 *
 * TODO General todo list:
 *  - Get some more code for drawing, like sprites and stuff
 *  - Collisons need a rework
 *  - Keyboard input is pretty dodgy
 *
 */
public class GameEngine implements Runnable {
    //Assorted functionality variables
    private boolean isRunning;
    private long frameRate;
    private int stepCount;
    private Thread gameLoop;

    //IO and some other stuff
    private KeyBoard mainInput; //prehaps all these objects should be grouped
    public KeyBoard getKeyBoard() {return mainInput;}
    private View mainView;
    public View getMainView() {return mainView;}
    private GameObjectList gameObjects;
    public GameObjectList getGameObjects() {return gameObjects;}
    private ModuleManager moduleManager;
    public ModuleManager getModuleManager() {return moduleManager;}

    private ICommandHandle commandHandle;
    public ICommandHandle getCommandHandle() {return commandHandle;}
    public void setCommandHandle(ICommandHandle i) {
        if (commandHandle != null)
            if (commandHandle == i) {
                return;
            } else {
                commandHandle.unloadComponent(this);
            }
        commandHandle = i;
        i.loadComponent(this);
    }


    private IIOHandle ioHandle;
    public IIOHandle getIoHandle() {return ioHandle;}
    //standered io handle is private, so these are provided
    public boolean isStandardIOHandle() {return ioHandle instanceof StandardIOHandle;}
    public boolean isStandardIOHandle(IIOHandle h) {return h instanceof StandardIOHandle;}
    public void setIoHandle() {
        if (!isStandardIOHandle())
            setIoHandle(new StandardIOHandle());
    }
    public void setIoHandle(IIOHandle i) {
        if (ioHandle != null)
            if (ioHandle == i) {
                return;
            } else {
                ioHandle.unloadComponent(this);
            }
        ioHandle = i;
        i.loadComponent(this);
    }

    //these two are final as they are using synchronise

    //all the strings to be processed by the command handler
    private final ArrayList<String> procStr;
    public void pushProcString(String s) {
        synchronized (procStr) {
            procStr.add(s);
        }
    }
    //This is for threads to request a task to be complete on the main thread
    //the tasks are all executed at the start of the tick event
    private final ArrayList<ITask> tasks;
    public void addTask(ITask t) {
        synchronized (tasks) {
            tasks.add(t);
        }
    }

    //Game variables
    private Direction dir = Direction.RIGHT;

    //Game objects
    private VoxelField voxelField;
    private MotionObject p1;
    private LaserGridHandle laser;

    //Constants
    public static final long STEP_LIMIT = 20;
    public static final int SCALE = 16;
    interface IDrawableGame extends IDrawableObject, IGameObject {}


    public GameEngine(View mainView, IMouseGetter mouseGetter, boolean startGame) {
        frameRate = 0;
        isRunning = false;
        mainInput = new KeyBoard(mouseGetter);
        procStr = new ArrayList<>();
        tasks = new ArrayList<>();

        this.mainView = mainView;
        this.mainView.addKeyListener(mainInput);
        moduleManager = new ModuleManager(Module.defaultPath, this);

        //objects = new GameObjectHandle(2);
        //tickObjects = new TickObjectHandle(1);

        //The rest of this is unique to this game
        //p1 = new MotionObject(0, 0, 16, 16);

        //voxelField stuff
        //voxelField = new VoxelField(this.mainView.getWidth() / SCALE, this.mainView.getHeight() / SCALE, SCALE);

        gameObjects = new GameObjectList(this);

        Log tmp = new Log();
        setIoHandle(tmp);
        try {

            gameObjects.addObject(tmp);

            MotionObjectHandle tmpM = new MotionObjectHandle(0, 0, 16, 16);
            ObjectRef motion = gameObjects.addObject(tmpM);
            VoxelFieldHandle tmpV = new VoxelFieldHandle();
            ObjectRef field = gameObjects.addObject(tmpV);

            gameObjects.addObject(new ViewMover());


            //This are using the reflection constuctors, there slower but i don't wanna break them so keep using them
            //p1 = (MotionObject)gameObjects.getInstance(gameObjects.newInstance(motion, 0, 0, 16, 16));
            //p1 = (MotionObject)tmpM.newInstance(this, 0, 0, 16, 16);
            //voxelField = (VoxelField)gameObjects.getInstance(
            //        gameObjects.newInstance(field, this.mainView.getWidth() / SCALE, this.mainView.getHeight() / SCALE, SCALE)
            //);
            laser = new LaserGridHandle();
            gameObjects.addObject(laser);
            laser.newInstance(this, 50, 50, Direction.RIGHT, 0);
            //laser.newInstance(this, 100, 50, Direction.LEFT, 0);

            gameObjects.addObject(new IDrawableGame() {
                @Override
                public void drawSelf(GameEngine engine) {
                    Point p = new Point(20 + 10 * dir.getDirectionVector().x, 20 + 10 * dir.getDirectionVector().y);
                    engine.getMainView().setColor(Color.ORANGE);
                    engine.getMainView().getDraw().drawLine(20, 20, p.x, p.y);
                }

                @ReferenceField
                private ObjectRef selfRef;

                @Override
                public void loadObject(GameEngine e) {}

                @Override
                public ObjectRef getObjectRef() {
                    return selfRef;
                }
            });

        } catch (Exception e) {}

        setCommandHandle(new DefaultCommandHandle());

        //voxelField = (VoxelField)tmpV.newInstance(this, this.mainView.getWidth() / SCALE, this.mainView.getHeight() / SCALE, SCALE);
/*
        for (int i = 0; i < voxelField.size.width; i++) {
            voxelField.setPosState(i, 7, true);
        }
        for (int i = 0; i < voxelField.size.width; i++) {
            voxelField.setPosState(i, 8, true);
        }
        for (int i = 0; i < voxelField.size.width; i++) {
            voxelField.setPosState(i, 9, true);
        }

        voxelField.setPosState(1, 0, true);
        voxelField.setPosState(3, 0, true);
        voxelField.setPosState(1, 3, true);
        voxelField.setPosState(3, 3, true);
        voxelField.setPosState(2, 3, true);
        voxelField.setPosState(0, 2, true);
        voxelField.setPosState(4, 2, true);
*/


        //throw all the objects into the game object handle
        //objects.allObjects[0] = voxelField;
        //objects.allObjects[1] = p1;
        //tickObjects.allObjects[0] = p1;

        /*
        gameObjects.addObject(moduleManager.getModuleAsGameObject(
                moduleManager.loadModule("com.game.main.gameObjects.TestObject", "com.game.main.gameObjects")
        ));
        */

        mainView.backgroundColor = Color.BLACK;

        if (startGame)
            start();
        commandHandle.runCommand("loadModule com.game.main.gameObjects.TestObject com.game.main.gameObjects");
    }

    public void start() {
        mainView.intVolatileImage();
        mainView.requestFocusInWindow();

        isRunning = true;
        gameLoop = new Thread(this);
        gameLoop.start();

    }

    public void stop() {
        isRunning = false;
        ioHandle.unloadComponent(this);
        try {
            //this probs shouldn't be interupted
            gameLoop.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        stepCount = 0;
        long timeTmp, deltaTime;
        long time = System.currentTimeMillis();

        while (isRunning) {
            timeTmp = System.currentTimeMillis();
            deltaTime = timeTmp - time;


            if (STEP_LIMIT <= deltaTime) {
                stepCount++;

                time = timeTmp;
                tick(deltaTime);
                render();

                if (mainView.hasFocus())
                    mainInput.wipPressRelease();
                else
                    mainInput.wipKeyStates();
                frameRate = 1000 / deltaTime;
            }
        }
    }

    public void tick(long deltaTime) {
        //this is all game specific
        if (ioHandle.isTyping()) {
            ioHandle.typeTick(this);
        } else if (mainInput.keyCheckReleased('O')) {
            ioHandle.startTyping(this);
        }

        for (int i = 0; i < tasks.size(); i++) {
            ITask tmp = tasks.remove(tasks.size() - 1);
            tmp.invoke(this);
            //ioHandle.printString(tmp);
        }

        for (int i = 0; i < procStr.size(); i++) {
            String tmp = procStr.remove(procStr.size() - 1);
            commandHandle.runCommand(tmp);
            //ioHandle.printString(tmp);
        }
/*
        if (mainInput.keyCheckPressed('P')) {
            Point mouse = voxelField.coToGrid(mainInput.getMousePos(mainView));
            if (voxelField.pointExists(mouse)) {
                voxelField.setPosState(mouse.x, mouse.y, !voxelField.getPosState(mouse.x, mouse.y));
                ioHandle.printString("(" + mouse.x + ", " + mouse.y + ", " + voxelField.getPosState(mouse.x, mouse.y) + ")");
            }
        }
*/

        if (mainInput.keyCheckPressed('E')) {
            dir = dir.next();
        }

        if (mainInput.keyCheckPressed('1')) {
            Point p = mainInput.getMousePos(mainView);
            if (!gameObjects.pointCollides(p.x, p.y, null)) {
                laser.newInstance(this, p.x, p.y, dir, 0);
            }
        }
        if (mainInput.keyCheckPressed('2')) {
            Point p = mainInput.getMousePos(mainView);
            if (!gameObjects.pointCollides(p.x, p.y, null)) {
                laser.newInstance(this, p.x, p.y, dir, 1);
            }
        }
/*
        if (mainInput.keyCheckPressed(KeyEvent.VK_UP)) {
            p1.addVForce(-10);
        }
        if (mainInput.keyCheck(KeyEvent.VK_LEFT)) {
            p1.addHForce(-0.8);
        }
        if (mainInput.keyCheck(KeyEvent.VK_RIGHT)) {
            p1.addHForce(0.8);
        }
*/
        gameObjects.step(deltaTime);
        //p1.step(voxelField, mainView, mainInput);
    }

    public void render() {
        mainView.getSurfaceGraphics();

        mainView.fillGraphics();

        mainView.setColor(Color.BLACK);
        mainView.drawGUIText(String.valueOf(frameRate), mainView.viewRect.width - 15, 10);
        mainView.drawGUIText(String.valueOf(stepCount), mainView.viewRect.width - 60, 10);

        gameObjects.draw();

        mainView.drawToScreen();
        mainView.dumpGraphics();
    }

    private class StandardIOHandle implements IIOHandle {
        private Scanner scan;
        private Runnable t;
        private Thread thread;
        private boolean running = true;
        private boolean startTyping = false;
        private boolean isTyping = false;
        private String s;

        public StandardIOHandle() {
            scan = new Scanner(System.in);
            t = () -> {
                while (running) {
                    if (startTyping) {
                        isTyping = true;
                        startTyping = false;
                        System.out.print(">>> ");
                        s = scan.nextLine();
                        pushProcString(s);
                        isTyping = false;
                    }
                }
            };
        }

        @Override
        public void loadComponent(GameEngine g) {
            thread = new Thread(t);
            thread.start();
        }

        @Override
        public void unloadComponent(GameEngine g) {
            //I could be wrong but im prty sure this object wont be collected by the gc
            //until the thread finishes, as the lambda function it is running does have a refrence to it
            //(prty sure that counts? oh well it might crash if it dosent)
            running = false;
        }

        @Override
        public void printString(String s) {
            System.out.println(s);
        }

        @Override
        public void startTyping(GameEngine g) {
            startTyping = true;
        }

        @Override
        public boolean isTyping() {
            return isTyping;
        }

        @Override
        public void typeTick(GameEngine g) {

        }
    }
}
