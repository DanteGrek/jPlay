package com.playwright.screenplay;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.playwright.screenplay.enums.BrowserName;

class BrowserManager {

    private Actor actor;
    private Playwright playwright = Playwright.create();
    private BrowserName browserName;
    private String deviceName;
    private BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
    private Browser browser;

    public BrowserManager(Actor actor) {
        this.actor = actor;
    }

    public Browser getBrowser() {
        return browser;
    }

    public BrowserManager withBrowser(BrowserName browserName) {
        this.browserName = browserName;
        return this;
    }

    public BrowserManager withDevice(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public BrowserManager withOptions(BrowserType.LaunchOptions launchOptions) {
        this.launchOptions = launchOptions;
        return this;
    }

    public Actor create() {
        switch (browserName){
            case CHROME -> browser = playwright.chromium()
                    .launch(this.launchOptions
                            .setChannel(BrowserName.CHROME.name));
            case MSEDGE -> browser = playwright.chromium()
                    .launch(this.launchOptions
                            .setChannel(BrowserName.MSEDGE.name));
            case WEBKIT -> browser = playwright.webkit()
                    .launch(this.launchOptions);
            case FIREFOX -> browser = playwright.firefox()
                    .launch(this.launchOptions);
            default -> browser = playwright.chromium()
                    .launch(this.launchOptions);
        }
        return this.actor;
    }
}
