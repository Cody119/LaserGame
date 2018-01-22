package com.game.main.engine.script;

import com.game.main.engine.GameEngine;
import com.game.main.engine.exceptions.IllegalCommandArgumentException;
import com.game.main.engine.objectInterfaces.IGameObject;
import com.game.main.engine.objectInterfaces.IIOHandle;
import com.game.main.engine.runtimeRefrence.ObjectRef;

/**
 * Created by AND0053 on 3/06/2016.
 */
public class StandardLibrary {
    public static void loadLibrary(ICommandHandle scriptEngine) {
        scriptEngine.addCommand("print",
                (s, args) -> {
                    for (Object o : args) {
                        s.getGameEngine().getIoHandle().printString(o.toString());
                    }
                    return null;
                }
        );
        scriptEngine.addCommand("set",
                (s, args) -> {
                    if (args.length == 2) {
                        s.setVar(args[0].toString(), args[1]);
                    } else {
                        throw new IllegalCommandArgumentException();
                    }
                    return null;
                }
        );
        scriptEngine.addCommand("load",
                (s, args) -> {
                    if (args.length == 2) {
                        String ref = s.getGameEngine().getModuleManager().loadModule(args[0].toString(), args[1].toString());
                        GameEngine g = s.getGameEngine();
                        IGameObject gmObj = g.getModuleManager().getModuleAsGameObject(ref);
                        if (gmObj == null) {
                            g.getIoHandle().printString("Could not get object");
                            return null;
                        } else {
                            return g.getGameObjects().addObject(gmObj);
                        }
                    } else {
                        throw new IllegalCommandArgumentException();
                    }
                }
        );
        scriptEngine.addCommand("loadModule",
                (s, args) -> {
                    if (args.length == 2) {
                        return s.getGameEngine().getModuleManager().loadModule(args[0].toString(), args[1].toString());
                    } else {
                        throw new IllegalCommandArgumentException();
                    }
                }
        );
        scriptEngine.addCommand("addObject",
                (s, args) -> {
                    GameEngine g = s.getGameEngine();
                    IGameObject gmObj;

                    if (args.length > 1 && args[0] instanceof String) {
                        gmObj = g.getModuleManager().getModuleAsGameObject((String)args[0]);
                    } else {
                        throw new IllegalCommandArgumentException();
                    }

                    if (args.length > 2) {
                        return g.getGameObjects().addObject(gmObj, args[1].toString());
                    } else {
                        return g.getGameObjects().addObject(gmObj);
                    }
                }
        );
        scriptEngine.addCommand("setIOHandle",
                (s, args) -> {
                    if (args.length == 1 && args[0] instanceof ObjectRef) {
                        GameEngine g = s.getGameEngine();
                        Object ioH = g.getGameObjects().getObject((ObjectRef) args[0]);
                        if (ioH instanceof IIOHandle) {
                            g.setIoHandle((IIOHandle) ioH);
                        } else {
                            g.getIoHandle().printString("Supplied object is not an IIOHandle");
                        }
                    } else {
                        throw new IllegalCommandArgumentException();
                    }
                    return null;
                }
        );
        scriptEngine.addCommand("setDefaultIOHandle",
                (s, args) -> {
                    s.getGameEngine().setIoHandle();
                    return null;
                }
        );
        scriptEngine.addCommand("getObject",
                (s, args) -> {
                    if (args.length == 1 && args[0] instanceof String) {
                        ObjectRef tmp = s.getGameEngine().getGameObjects().getObject((String) args[0]);
                        if (tmp != null) {
                            return tmp;
                        } else {
                            s.getGameEngine().getIoHandle().printString(args[0] + " is not a registered game object");
                            return null;
                        }
                    } else {
                        throw new IllegalCommandArgumentException();
                    }
                }
        );
    }
}
