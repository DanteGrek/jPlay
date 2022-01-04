package io.github.dantegrek.jplay;

import com.microsoft.playwright.Dialog;

/**
 * Dialogs are dismissed automatically, unless there is a listener: acceptAll or acceptOnce
 */
public final class DialogHandler {

    private Actor actor;

    DialogHandler(Actor actor) {
        this.actor = actor;
    }

    public Actor acceptOnce() {
        this.actor.currentPage().onceDialog(Dialog::accept);
        return this.actor;
    }

    public Actor acceptOnce(String text) {
        this.actor.currentPage().onceDialog(dialog -> dialog.accept(text));
        return this.actor;
    }

    public Actor acceptAll() {
        this.actor.currentPage().onDialog(Dialog::accept);
        return this.actor;
    }

    public Actor acceptAll(String text) {
        this.actor.currentPage().onDialog(dialog -> dialog.accept(text));
        return this.actor;
    }

}
