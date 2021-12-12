package com.playwright.screenplay;


public final class Actor {

    private BrowserManager browserManager = new BrowserManager(this);

    private static ThreadLocal<Actor> actor = ThreadLocal.withInitial(() -> new Actor());

    private Actor() {}

    public static Actor actor() {
        return actor.get();
    }

    public void removeActor() {
        actor.remove();
    }

    public BrowserManager config() {
        return browserManager;
    }

    public Actor closeBrowser() {
        this.browserManager.getBrowser().close();
        return this;
    }

}
