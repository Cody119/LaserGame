package com.game.main.engine.util;

import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IGameObject;
import com.game.main.engine.objectInterfaces.IInstance;
import com.game.main.engine.objectInterfaces.IInstantiableObject;
import com.game.main.engine.objectInterfaces.ReferenceField;
import com.game.main.engine.runtimeRefrence.InstanceRef;
import com.game.main.engine.runtimeRefrence.ObjectRef;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

/**
 * Created by AND0053 on 30/05/2016.
 *
 * Rename, its missleading
 */
public abstract class GameObject<T extends IInstance> implements IGameObject, IInstantiableObject {
    @ReferenceField
    protected ObjectRef selfRef;

    protected ArrayList<T> instances;
    protected final Class<T> instanceClass;
    //protected final Class[][] constructors;

    public GameObject() {
        instances = new ArrayList<>();
        //This only works because getGenericSuperClass gets the super type, so this class needs a child class to use this
        instanceClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        //constructors = ArgumentUtil.buildConstructorParamList(instanceClass);

    }

    protected T addInstance(T instance) {
        return addInstance(instance, getObjectRef().parent.getNextId());
    }

    protected T addInstance(T instance, long id) {
        if (instance == null) throw new NullPointerException();
        //redo this last bit
        instance.setInstanceRef(new InstanceRef(instances.size(), id, getObjectRef()));
        instances.add(instance);
        return instance; //convenience?
    }
    //TODO second list to track empty spaces
    protected void destroyInstance(T instance) {
        instances.remove(instance.getInstanceRef().ref);
    }

    @Override
    public IInstance getInstance(InstanceRef r) {
        return instances.get(r.ref);
    }

    @Override
    public InstanceRef newInstance(GameEngine game, long id, Object ... args) throws IllegalArgumentException {
        Object tmp = ArgumentUtil.tryInstantiateClass(instanceClass, args);
        if (tmp == null) {
            throw new IllegalArgumentException();
        }

        //We get the constructor from instance class, the class of type T therefore this is of type T
        return addInstance((T) tmp, id).getInstanceRef();
    }

    /*
    @Override
    public IInstance newInstance(GameEngine game, Object ... args) {
        Object tmp;
        try {

            tmp = instanceClass.getConstructors()[ArgumentUtil.findConstructorIndex(constructors, ArgumentUtil.getParamList(args))].newInstance(args);
        } catch (IllegalArgumentException e) {
            try {
                System.out.println("Specific constructor did not exsist, trying zero arg");
                tmp =  instanceClass.getConstructor().newInstance();
            } catch (Exception e1) {
                System.out.println(instanceClass + " did not initalise properly, override newInstance in " + this.getClass());
                return null;
            }
        } catch (Exception e) {
            System.out.println(instanceClass + " did not initalise properly, override newInstance in " + this.getClass());
            return null;
        }
        if (tmp.getClass().equals(instanceClass))
    }
    */

    @Override
    public void loadObject(GameEngine e) {}

    @Override
    public ObjectRef getObjectRef() {
        return selfRef;
    }
}
