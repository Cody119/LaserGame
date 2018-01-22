package com.game.main.engine.script;

import com.game.main.engine.GameEngine;
import com.game.main.engine.exceptions.GameEngineException;
import com.game.main.engine.exceptions.GameEngineRuntimeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by AND0053 on 3/06/2016.
 *
 */
public class DefaultCommandHandle implements ICommandHandle {
    public static final String ERROR_MSG = "ERROR: ";
    public static final String RETURN_VAR = "";
    public static final String RETURN_VAL_DEFAULT = "";
    public static final char VAR_CHAR = '$';

    //I dont know if i should be using a hash map or wat?
    HashMap<String, ICommand> functions;
    HashMap<String, Object> variables;
    GameEngine gameEngine;
    EngineScript engineScriptState;


    public DefaultCommandHandle() {
    }

    Object[] resolveArgs(String[] args) {
        Object[] ret = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            ret[i] = resolveArg(args[i]);
        }
        return ret;
    }

    Object resolveArg(String s) {
        if (!s.isEmpty() && s.charAt(0) == VAR_CHAR) {
            return variables.get(s.substring(1));
        }
        return s;
    }

    @Override
    public GameEngine getGameEngine() {
        return gameEngine;
    }

    @Override
    public boolean addCommand(String name, ICommand f) {
        if (functions.containsKey(name)) {
            return false;
        } else {
            functions.put(name, f);
            return true;
        }
    }

    @Override
    public void runCommand(String[] s) {
        if (s.length < 1) return;
        Object tmp;
        ICommand func;
        Object[] statement = null;

        if (s[0].isEmpty())  {
            gameEngine.getIoHandle().printString(ERROR_MSG + "No function name specified");
            return;
        }
        if (s[0].charAt(0) == VAR_CHAR) {
            //TODO write engine script
            return;
        }

        try {
            statement = resolveArgs(s);
            if ((func = functions.get(statement[0].toString())) != null) {
                tmp = func.invoke(this, Arrays.copyOfRange(statement, 1, statement.length));
                variables.put(RETURN_VAR, tmp);
            }
        } catch (IllegalArgumentException e) {
            //statement[0] should be defined, if resolve statement fails it throws a Undefined variable exception
            gameEngine.getIoHandle().printString(ERROR_MSG + "Attempted to call " + statement[0].toString() + " with wrong arguments");
        } catch (GameEngineRuntimeException | GameEngineException e) {
            gameEngine.getIoHandle().printString(ERROR_MSG + "\"" + e.getMessage() + "\"");
        }
    }

    public void executeFile(String file) {
        executeFile(new File(file));
    }

    public void executeFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                runCommand(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getVar(String s) {
        return variables.get(s);
    }

    @Override
    public void setVar(String name, Object val) {
        if (variables.containsKey(name)) {
            variables.replace(name, val);
        } else {
            variables.put(name, val);
        }
    }

    @Override
    public Object getLastReturn() {
        return getVar(RETURN_VAR);
    }

    @Override
    public void loadComponent(GameEngine g) {
        functions = new HashMap<>();
        variables = new HashMap<>();
        gameEngine = g;
        //this variable holds the return val for any scripts
        variables.put(RETURN_VAR, RETURN_VAL_DEFAULT);
        StandardLibrary.loadLibrary(this);
    }

    @Override
    public void unloadComponent(GameEngine g) {

    }
}
