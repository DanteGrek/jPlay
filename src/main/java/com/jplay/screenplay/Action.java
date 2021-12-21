package com.jplay.screenplay;

import com.jplay.interfaces.IAction;

public abstract class Action extends AbstractActivity implements IAction {

    @Override
    public Actor and() {
        return actor;
    }

    @Override
    public Actor then() {
        return actor;
    }

}
