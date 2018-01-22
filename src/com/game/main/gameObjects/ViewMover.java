package com.game.main.gameObjects;

import com.game.main.components.View;
import com.game.main.engine.GameEngine;
import com.game.main.engine.objectInterfaces.IGameObject;
import com.game.main.engine.objectInterfaces.ITickObject;
import com.game.main.engine.objectInterfaces.ReferenceField;
import com.game.main.engine.runtimeRefrence.ObjectRef;
import com.game.main.input.KeyBoard;

/**
 * Created by AND0053 on 6/06/2016.
 */
public class ViewMover implements ITickObject, IGameObject {
    @ReferenceField
    private ObjectRef ref;

    @Override
    public void loadObject(GameEngine e) {}

    @Override
    public ObjectRef getObjectRef() {
        return ref;
    }

    @Override
    public boolean step(GameEngine g, long deltaTime) {
        View mainView = g.getMainView();
        KeyBoard mainInput = g.getKeyBoard();
        if (mainInput.keyCheck('W')) {
            mainView.viewRect.y--;
        }
        if (mainInput.keyCheck('S')) {
            mainView.viewRect.y++;
        }
        if (mainInput.keyCheck('D')) {
            mainView.viewRect.x++;
        }
        if (mainInput.keyCheck('A')) {
            mainView.viewRect.x--;
        }
        return true;
    }
}
