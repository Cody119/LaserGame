package com.game.main.engine.util;

import com.game.main.engine.objectInterfaces.ReferenceField;
import com.game.main.engine.runtimeRefrence.ObjectRef;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by AND0053 on 31/05/2016.
 *
 * Reflection assistance when it comes to finding methods/constructors
 *
 * TODO need to look into generic types, they seem to cause a problem i.e. List<T>.class == List<S>.class which is bad?
 * After spending a hour or so on the problem i could not find a way to get a generics type param at run time, suspect its impossible
 * Alot of the comparison stuff is left to the standared libary, which after looking through i dont think it makes a differnce
 */
public abstract class ArgumentUtil {
    public static void printConstructorPramList(Class[][] constructorList) {
        for (int i = 0; i < constructorList.length; i++) {
            String tmp = "Con " + i + ": ";
            for (int j = 0; j < constructorList[i].length; j++) {
                tmp += constructorList[i][j].toString() + " ";
            }
            System.out.println(tmp);
        }
    }

    //This returns true if a class is a generic type
    //Unfortunatly this does not help in figuring out wat the generic argument is
    public static boolean isClassGeneric(Class clazz) {
        return clazz.getClass().getTypeParameters().length != 0;
    }

    public static Class[] getParamList(Object[] args) {
        //boolean default val is false
        return getParamList(args, new boolean[args.length]);
    }
    //if notPrimitive[i] == true then args[i] will not be turned into its primitive (if its a primitive wrapper)
    //note this is not made for performance reason (the speed gains are pretty minimal), it is made to indicate
    //that a primitive wrapper type should not be converted
    public static Class[] getParamList(Object[] args, boolean[] notPrimitive) {
        Class[] tmp = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            tmp[i] = notPrimitive[i] ? args[i].getClass() : tryPrimitiveConversion(args[i].getClass());
        }
        return tmp;
    }

    public static Class[] getParamListKeepAll(Object[] args) {
        Class[] tmp = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            tmp[i] = args[i].getClass();
        }
        return tmp;
    }

    //Does not work for generics
    public static boolean argTypeCheck(Object[] args, Class[] types) {
        if (args.length != types.length)
            return false;
        Class[] argTypes = getParamList(args);

        for (int i = 0; i < types.length; i++) {
            if (!(types[i].isAssignableFrom(argTypes[i]))) {
                    return false;
            }
        }

        return true;
    }

    public static Field getRefField(Class clazzIn) {
        Class clazzCur = clazzIn;
        Field[] fields = clazzCur.getDeclaredFields();
        Annotation a;
        while (true) {
            for (Field f : fields) {
                a = f.getAnnotation(ReferenceField.class);
                if (a != null) {
                    if (ObjectRef.class.isAssignableFrom(f.getType())) {
                        f.setAccessible(true);
                        return f;
                    }
                }
            }
            if ((clazzCur = clazzCur.getSuperclass()) == null) {
                return null;
            }
            fields = clazzCur.getDeclaredFields();
        }
    }

    public static Class tryPrimitiveConversion(Class primitiveWrapper) {
        if (primitiveWrapper.isPrimitive()) return primitiveWrapper;

        if (primitiveWrapper.equals(Integer.class)) return int.class;
        if (primitiveWrapper.equals(Double.class)) return double.class;
        if (primitiveWrapper.equals(Byte.class)) return byte.class;
        if (primitiveWrapper.equals(Short.class)) return short.class;
        if (primitiveWrapper.equals(Character.class)) return char.class;
        if (primitiveWrapper.equals(Long.class)) return long.class;
        if (primitiveWrapper.equals(Float.class)) return float.class;

        return primitiveWrapper;
    }

    public static Constructor findConstructor(Class clazz, Class[] conArgs) {
        Constructor constructor;
        try {
            constructor = clazz.getConstructor(conArgs);
        } catch (NoSuchMethodException e) {
            return null;
        }
        return constructor;
    }

    public static Constructor findConstructor(Class clazz, Object[] params) {
        Constructor constructor = findConstructor(clazz, getParamList(params));
        if (constructor == null) {
            constructor = findConstructor(clazz, getParamListKeepAll(params));
        }
        return constructor;
    }

    public static Object tryInstantiateClass(Class clazz, Object[] params) {
        Constructor constructor = findConstructor(clazz, params);
        if (constructor == null) {
            return null;
        }
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            //From what i understand this shouldn't happen
            throw new RuntimeException("Something is wrong with this method");
        }
    }

    public static Method findMethod(Class clazz, String name, Class[] conArgs) {
        Method method;
        try {
            method = clazz.getMethod(name, conArgs);
        } catch (NoSuchMethodException e) {
            return null;
        }
        return method;
    }

    public static Method findMethod(Class clazz, String name, Object[] params) {
        Method method = findMethod(clazz, name, getParamListKeepAll(params));
        if (method == null) {
            method = findMethod(clazz, name, getParamList(params));
        }
        return method;
    }


    /** --------------------------------------------------------------------------------------------------------
      * These methods are used to get the index within the array from class.getMethods and class.getConstructors
      * Not 100% sure where this methods are gonna be used but i think they will be
      * --------------------------------------------------------------------------------------------------------
     */

    public static int findConstructorIndex(Class clazz, Class[] conArgs) {
        Constructor[] constructors = clazz.getConstructors();
        Constructor constructor;
        try {
            constructor = clazz.getConstructor(conArgs);
        } catch (NoSuchMethodException e) {
            return -1;
        }
        for (int i = 0; i < constructors.length; i++) {
            if (constructor.equals(constructors[i]))
                return i;
        }
        //Very proffesional, if this happens then i am very confused
        throw new RuntimeException("WTF");
    }

    //Try both variations
    public static int findConstructorIndex(Class clazz, Object[] params) {
        int index = findConstructorIndex(clazz, getParamList(params));
        if (index == -1) {
            index = findConstructorIndex(clazz, getParamListKeepAll(params));
        }
        return index;
    }

    public static int getZeroArgConstructorIndex(Class clazz) {
        return findConstructorIndex(clazz, new Class[0]);
    }

    /*
     * This is old stuff from an earlier version, changed mostly due to generics being hard
     * But also cause it was messy

    @Deprecated
    public static Class[][] buildConstructorParamList(Class classIn) {
        Constructor[] constructors = classIn.getConstructors();
        Class[][] ret = new Class[constructors.length][];
        for (int i = 0; i < constructors.length; i++) {
            ret[i] = constructors[i].getParameterTypes();
        }
        return ret;
    }

    @Deprecated
    public static int findConstructorIndex(Class[][] constructorList, Class[] specificC) {
        for (int i = 0; i < constructorList.length; i++) {
            if (compareConstructors(constructorList[i], specificC))
                return i;
        }
        return -1;
    }

    //This method assumes the primitive types only occur in list1, its faster this way
    @Deprecated
    public static boolean compareConstructors(Class[] list1, Class[] list2) {
        if (list1.length != list2.length)
            return false;

        for (int i = 0; i < list1.length; i++) {
            if (!(list1[i].equals(list2[i]))) {
                //Could still be primitive

                //class one is a primitive, so this could still be the same
                Class one = list1[i];
                Class two = list2[i];
                //Did i miss any primitives?
                if (!(

                        //Double and ints are probs gonna be the most common, but the order dosent really matter
                        one == Integer.TYPE && two == Integer.class
                                || one == Double.TYPE && two == Double.class
                                || one == Byte.TYPE && two == Byte.class
                                || one == Character.TYPE && two == Character.class
                                || one == Short.TYPE && two == Short.class
                                || one == Long.TYPE && two == Long.class
                                || one == Float.TYPE && two == Float.class
                )) {
                    return false;
                }
            }
        }

        return true;
    }

    @Deprecated
    public static int getZeroArgConstructor(Class[][] constructorList) {
        return findConstructorIndex(constructorList, new Class[0]);
    }
    */
}
