package com.game.main.engine;

/**
 * Created by AND0053 on 6/06/2016.
 */

@FunctionalInterface
public interface ITask {
    void invoke(GameEngine g);

    //Simple implementation for executing a bunch of commands
    class CommandTask implements ITask {
        String[] commands;

        public CommandTask(String[] commands) {
            this.commands = commands;
        }

        @Override
        public void invoke(GameEngine g) {
            for (String s : commands) {
                g.getCommandHandle().runCommand(s);
            }
        }
    }
}
