package com.playwright.screenplay;


public final class Actor {

    private BrowserManager browserManager = new BrowserManager(this);

    private static ThreadLocal<Actor> actor = ThreadLocal.withInitial(() -> new Actor());

    private Actor() {}

    public static Actor actor() {
        return actor.get();
    }

    public static void removeActor() {
        actor.get().browserManager.getBrowser().close();
        actor.remove();
    }

    public BrowserManager config() {
        return browserManager;
    }


}
