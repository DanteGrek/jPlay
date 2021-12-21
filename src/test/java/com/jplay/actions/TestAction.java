package com.jplay.actions;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ViewportSize;
import com.jplay.screenplay.Action;

public class TestAction extends Action {

    public Page getPage() {
        return this.page;
    }

    public BrowserContext getCurrentContext() {
        return this.page.context();
    }

    public Browser getBrowser() {
        return this.page.context().browser();
    }

    public TestAction renderHtml(String html) {
        this.page.setContent(html);
        return this;
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
