package com.jplay.actions;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ViewportSize;
import com.jplay.screenplay.Action;

public class TestAction extends Action {

    public Page getPage() {
        return this.actor.currentPage();
    }

    public BrowserContext getCurrentContext() {
        return this.actor.currentPage().context();
    }

    public Browser getBrowser() {
        return this.actor.currentPage().context().browser();
    }

    public TestAction renderHtml(String html) {
        this.actor.currentPage().setContent(html);
        return this;
    }

    public String getUserAgent() {
        return (String) this.actor.currentPage().evaluate("window.navigator.userAgent");
    }

    public ViewportSize getViewportSize() {
        return this.actor.currentPage().viewportSize();
    }

    public int getDeviceScaleFactor() {
        return (int) this.actor.currentPage().evaluate("window.devicePixelRatio");
    }

    public static TestAction testAction() {
        return new TestAction();
    }
}
