package com.playwright.screenplay;

import com.microsoft.playwright.*;
import com.playwright.screenplay.enums.BrowserName;

import static com.playwright.screenplay.enums.BrowserName.CHROMIUM;

public class BrowserManager {

    private BrowserName browserName = CHROMIUM;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    void setBrowserName(BrowserName browserName) {
        this.browserName = browserName;
    }

    BrowserName getBrowserName() {
        return this.browserName;
    }

    void setBrowser(Browser browser) {
        this.browser = browser;
    }

    void setBrowserContext(BrowserContext browserContext) {
        this.browserContext = browserContext;
    }

    void setPage(Page page) {
        this.page = page;
    }

    public Browser getBrowser() {
        return browser;
    }

    public BrowserContext getBrowserContext() {
        return browserContext;
    }

    public Page getPage() {
        return page;
    }

    public void closeBrowser() {
        browser.close();
    }

    // Start browser methods
    private Browser createBrowser(BrowserName browserName, BrowserType.LaunchOptions launchOptions) {
        Playwright playwright = Playwright.create();
        return switch (browserName) {
            case CHROME -> playwright.chromium()
                    .launch(launchOptions
                            .setChannel(BrowserName.CHROME.name));
            case MSEDGE -> playwright.chromium()
                    .launch(launchOptions
                            .setChannel(BrowserName.MSEDGE.name));
            case WEBKIT -> playwright.webkit()
                    .launch(launchOptions);
            case FIREFOX -> playwright.firefox()
                    .launch(launchOptions);
            default -> playwright.chromium()
                    .launch(launchOptions);
        };
    }

    void create(BrowserType.LaunchOptions launchOptions, Browser.NewContextOptions contextOptions) {
        setBrowser(createBrowser(this.browserName, launchOptions));
        setBrowserContext(getBrowser().newContext(contextOptions));
        setPage(getBrowserContext().newPage());
    }

    void createBrowser(BrowserType.LaunchOptions launchOptions) {
        setBrowser(createBrowser(this.browserName, launchOptions));
    }

    void createContextAndTab(Browser.NewContextOptions contextOptions) {
        setBrowserContext(getBrowser().newContext(contextOptions));
        setPage(getBrowserContext().newPage());
    }

}
