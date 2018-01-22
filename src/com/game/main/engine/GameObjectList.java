package com.game.main.engine;

import com.game.main.engine.exceptions.UnmakableObjectException;
import com.game.main.engine.objectInterfaces.*;
import com.game.main.engine.runtimeRefrence.InstanceRef;
import com.game.main.engine.runtimeRefrence.ObjectRef;
import com.game.main.engine.util.ArgumentUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by AND0053 on 30/05/2016.
 *
 * Maybe increase this classes role and make it responsible for an entire gameEngine world
 * Would need to be renamed (not like thats an issue) but might be a good idea
 *
 */
public class GameObjectList {
    private AtomicLong curId;
    public Long getNextId() {
        return curId.incrementAndGet();
    }
    private GameEngine gameEngine;
    private ArrayList<IGameObject> gameObjects;
    private ArrayList<ITickObject> tickObjects;
    private ArrayList<IDrawableObject> drawableObjects;
    private ArrayList<IWorldObject> worldObjects;
    private ArrayList<IInstantiableObject> instantiableObjects;

    private HashMap<String, ObjectRef> objects;

    public GameObjectList(GameEngine g) {
        curId = new AtomicLong(1);
        gameEngine = g;
        gameObjects = new ArrayList<>();
        tickObjects = new ArrayList<>();
        drawableObjects = new ArrayList<>();
        worldObjects = new ArrayList<>();
        instantiableObjects = new ArrayList<>();
        objects = new HashMap<>();
    }

    //Dunno if this helps much
    private class RefConstructor {
        private int gameObjectRef;
        private int tickObjectRef = -1;
        private int drawableObjectRef = -1;
        private int worldObjectRef = -1;
        private int instantiableObjectRef = -1;
        public RefConstructor(int gameRef) {
            gameObjectRef = gameRef;
        }
        public void setTickObjectRef(int tickObjectRef) {
            this.tickObjectRef = tickObjectRef;
        }
        public void setDrawableObjectRef(int drawableObjectRef) {
            this.drawableObjectRef = drawableObjectRef;
        }
        public void setWorldObjectRef(int worldObjectRef) {
            this.worldObjectRef = worldObjectRef;
        }
        public void setInstantiableObjectRef(int instantiableObjectRef) {
            this.instantiableObjectRef = instantiableObjectRef;
        }

        public ObjectRef makeObjectRef() {
            return new ObjectRef(GameObjectList.this, gameObjectRef, tickObjectRef, drawableObjectRef, worldObjectRef, instantiableObjectRef);
        }
    }

    public ObjectRef addObject(IGameObject object)  throws UnmakableObjectException {
        return addObject(object, object.getClass().getName());
    }

    public ObjectRef addObject(IGameObject object, String name) throws UnmakableObjectException {
        Field objectRef = ArgumentUtil.getRefField(object.getClass());
        if (objectRef == null) {
            //gameEngine.getIoHandle().printString("Class " + object.getClass().getName() + " does not have a refrence field");
            throw new UnmakableObjectException("Class " + object.getClass().getName() + " does not have a refrence field");
        }

        if (objects.get(name) != null) {
            //gameEngine.getIoHandle().printString("Object of class " + object.getClass().getName() + " is already registered");
            //TODO make new exception for this case
            throw new UnmakableObjectException("Object of class " + object.getClass().getName() + " is already registered");
        }

        gameObjects.add(object);
        RefConstructor rC = new RefConstructor(gameObjects.size() - 1);

        if (object instanceof ITickObject) {
            tickObjects.add((ITickObject) object);
            rC.setTickObjectRef(tickObjects.size() - 1);
        }

        if (object instanceof IWorldObject) {
            worldObjects.add((IWorldObject) object);
            rC.setWorldObjectRef(worldObjects.size() - 1);
        }

        if (object instanceof IDrawableObject) {
            drawableObjects.add((IDrawableObject) object);
            rC.setDrawableObjectRef(drawableObjects.size() - 1);
        }

        if (object instanceof IInstantiableObject) {
            instantiableObjects.add((IInstantiableObject) object);
            rC.setInstantiableObjectRef(instantiableObjects.size() - 1);
        }
        try {
            objectRef.set(object, rC.makeObjectRef());
        } catch (IllegalAccessException e) {
            //gameEngine.getIoHandle().printString("Could not access " + object.getClass().getName() + " reference field");
            throw new UnmakableObjectException("Could not access " + object.getClass().getName() + " reference field");
        }
        //TODO move this?
        gameEngine.getIoHandle().printString("Added " + object.getClass().getName() + " to the game world");
        ObjectRef ret = object.getObjectRef();
        objects.put(name, ret);
        return ret;
    }

    public ObjectRef getObject(String name) {
        return objects.get(name);
    }

    public void step(long deltaTime) {
        for (ITickObject o : tickObjects) {
            o.step(gameEngine, deltaTime);
        }
    }

    public void draw() {
        for (IDrawableObject o : drawableObjects) {
            o.drawSelf(gameEngine);
        }
    }

    //tmp method
    public InstanceRef newInstance(ObjectRef obj, Object ... args) {
        //Need to check some stuff
        return instantiableObjects.get(obj.instantiableObjectRef).newInstance(gameEngine, getNextId(), args);
    }

    public IInstance getInstance(InstanceRef r) {
        return instantiableObjects.get(r.obj.instantiableObjectRef).getInstance(r);
    }

    public IGameObject getObject(ObjectRef r) {
        return gameObjects.get(r.gameObjectRef);
    }

    public boolean pointCollides(int  x, int y, InstanceRef exclude) {
        return pointCollidesObj(x, y, exclude) != null;
    }

    public IWorldObject pointCollidesObj(int  x, int y, InstanceRef exclude) {
        for (IWorldObject o : worldObjects) {
            IWorldObject t = o.pointCollidesObj(x, y, exclude);
            if (t != null) return t;
        }
        return null;
    }

    public IInstance pointCollidesInst(int  x, int y, InstanceRef exclude) {
        for (IWorldObject o : worldObjects) {
            IInstance t = o.pointCollidesInst(x, y, exclude);
            if (t != null) return t;
        }
        return null;
    }

    public IWorldObject pointCollidesObj(int  x, int y, ObjectRef exclude) {
        for (IWorldObject o : worldObjects) {
            if (exclude != o.getObjectRef()) {
                IWorldObject t = o.pointCollidesObj(x, y, null);
                if (t != null) return t;
            }
        }
        return null;
    }
}
