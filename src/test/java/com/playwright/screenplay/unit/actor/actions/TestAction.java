package com.playwright.screenplay.unit.actor.actions;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.options.ViewportSize;
import com.playwright.screenplay.Action;

public class TestAction extends Action {

    public Browser getBrowser() {
        return this.page.context().browser();
    }

    public String getUserAgent() {
        return (String) this.page.evaluate("window.navigator.userAgent");
    }

    public ViewportSize getViewportSize() {
        return this.page.viewportSize();
    }

    public int getDeviceScaleFactor() {
        return (int) this.page.evaluate("window.devicePixelRatio");
    }

    public static TestAction testAction() {
        return new TestAction();
    }
}
