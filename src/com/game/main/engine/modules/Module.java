package com.game.main.engine.modules;

import com.game.main.engine.exceptions.ClassNotLoadedException;

import java.io.*;

/**
 * Created by AND0053 on 30/05/2016.
 *
 */
public class Module {
    public static String defaultPath = "D:\\Users\\and0053\\MyFiles\\MyStuff\\Programing\\Java\\IntelliJ\\2DProgrameGame\\out\\production\\2DProgrameGame\\";
    public final String path;
    //Should try and make these three final, they shouldn't change
    public final String packageName;
    private Class moduleClass;
    public Class getModuleClass() {return moduleClass;}

    //
    private final ModuleLoader classLoader;

    private ModuleState state;
    public ModuleState getState() {return state;}
    public enum ModuleState {VALID, LOADING, INVALID}

    public Module(String bName, String packageName) throws ClassNotLoadedException {
        this(bName, packageName, defaultPath);
    }

    public Module(String bName, String packageName, String path) throws ClassNotLoadedException {
        this.path = path;
        this.packageName = packageName;
        classLoader = new ModuleLoader();

        state = ModuleState.LOADING; //dont think this is needed
        try {
            moduleClass = classLoader.loadClass(bName, true);
        } catch (ClassNotFoundException e) {
            state = ModuleState.INVALID;
            throw new ClassNotLoadedException(e);
        }
        state = ModuleState.VALID;

    }

    class ModuleLoader extends ClassLoader {
        public ModuleLoader() {
            super();
        }

        public boolean classIsLoaded(String bName) {
            return super.findLoadedClass(bName) != null;
        }


        public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
            //This check needs to be done, because defineClass will call this class loaders "loadClass" method
            //for all classes that this class is derived from e.g. java.lang.Object ect, which will fail to be loaded
            //obviously a problem, so we can either check that its not a java class, or check if it is our class
            //the later is easier but less flexible, so probs need to fix, have a look into online solutions?
            Class clazz;

            if (!name.startsWith(packageName)) {
                clazz = getParent().loadClass(name);
                return clazz;
            }

            byte[] in;
            try {
                in = getClassData(name);
            } catch (IOException e) {
                throw new ClassNotFoundException("File not found for " + name);
            }

            //TODO handle class format error?
            clazz = defineClass(name, in, 0, in.length);
            if (resolve)
                resolveClass(clazz);
            return clazz;
        }

        private byte[] getClassData(String name) throws IOException {
            //apparently getResourceAsStream(name) can/should be used here (call with system class loader)
            //but it dosen't work as excpected?
            //System.out.println(path + name.replace('.', File.separatorChar) + ".class");
            InputStream stream = new FileInputStream(path + name.replace('.', File.separatorChar) + ".class");
            int size = stream.available();
            byte buff[] = new byte[size];
            DataInputStream in = new DataInputStream(stream);
            in.readFully(buff);
            in.close();
            return buff;
        }

    }
}
