package com.game.main.engine.modules;

import com.game.main.engine.GameEngine;
import com.game.main.engine.exceptions.ClassNotLoadedException;
import com.game.main.engine.exceptions.UnmakableObjectException;
import com.game.main.engine.objectInterfaces.IGameObject;
import com.game.main.engine.runtimeRefrence.ObjectRef;
import com.game.main.engine.util.ArgumentUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AND0053 on 2/06/2016.
 */
public class ModuleManager {
    private String defaultPath;
    private GameEngine gameEngine;
    private HashMap<String, Module> modules;

    public ModuleManager(String path, GameEngine g) {
        defaultPath = path;
        modules = new HashMap<>();
        gameEngine = g;
    }

    public String loadModule(String bName, String packageName) throws ClassNotLoadedException {
        modules.put(bName, new Module(bName, packageName , defaultPath));
        //gameEngine.getIoHandle().printString("Loaded " + tmp.getModuleClass().getName() + " correctly");
        return bName;
    }

    public IGameObject getModuleAsGameObject(String name) throws UnmakableObjectException {
        Class clazz = modules.get(name).getModuleClass();
        if (IGameObject.class.isAssignableFrom(clazz)) {
            Object ret = ArgumentUtil.tryInstantiateClass(clazz, new Object[0]);
            if (ret == null && (ret = ArgumentUtil.tryInstantiateClass(clazz, new Class[]{GameEngine.class})) == null) {
                throw new UnmakableObjectException(clazz.getName() + " does not have a zero arg or single param (of type GameEngine) public constructor");
            }
            return (IGameObject)ret;
        } else {
            throw new UnmakableObjectException(clazz.getName() + " is not a IGameObject");
        }
    }
}
