package io.github.dantegrek.interfaces;

import io.github.dantegrek.screenplay.Actor;

public interface IAction {
    public Actor then();

    public Actor and();
}
