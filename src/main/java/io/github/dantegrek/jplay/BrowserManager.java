package io.github.dantegrek.jplay;

import com.microsoft.playwright.*;
import io.github.dantegrek.enums.BrowserName;

/**
 * This class is browser, context and page keeper.
 */
class BrowserManager {

    private Browser browser;
    private BrowserContext browserContext;
    private Page page;
    private Frame frame;

    void setBrowser(Browser browser) {
        this.browser = browser;
    }

    void setBrowserContext(BrowserContext browserContext) {
        this.browserContext = browserContext;
    }

    void setPage(Page page) {
        this.page = page;
    }

    void setFrame(Frame frame) {
        this.frame = frame;
    }

    Frame getFrame() {
        return this.frame;
    }

    Browser getBrowser() {
        return browser;
    }

    BrowserContext getBrowserContext() {
        return browserContext;
    }

    Page getPage() {
        return page;
    }

    void closeBrowser() {
        browser.close();
    }

    // Start browser methods
    private Browser startBrowserOnly(BrowserName browserName, BrowserType.LaunchOptions launchOptions) {
        Playwright playwright = Playwright.create();
        switch (browserName) {
            case CHROME:
                return playwright.chromium()
                        .launch(launchOptions
                                .setChannel(BrowserName.CHROME.name));
            case MSEDGE:
                return playwright.chromium()
                        .launch(launchOptions
                                .setChannel(BrowserName.MSEDGE.name));
            case WEBKIT:
                return playwright.webkit()
                        .launch(launchOptions);
            case FIREFOX:
                return playwright.firefox()
                        .launch(launchOptions);
            default:
                return playwright.chromium()
                        .launch(launchOptions);
        }
    }

    void startBrowserOnly(Configuration configuration) {
        setBrowser(startBrowserOnly(configuration.getBrowserName(), configuration.getLaunchOptions()));
    }

    void startBrowserContextAndTab(Configuration configuration) {
        setBrowser(startBrowserOnly(configuration.getBrowserName(), configuration.getLaunchOptions()));
        setBrowserContext(getBrowser().newContext(configuration.getContextOptions()));
        setPage(getBrowserContext().newPage());
        getPage().setDefaultNavigationTimeout(configuration.getDefaultNavigationTimeout());
        getPage().setDefaultTimeout(configuration.getDefaultTimeout());
    }

    void createContextWithTab(Configuration configuration) {
        setBrowserContext(getBrowser().newContext(configuration.getContextOptions()));
        setPage(getBrowserContext().newPage());
        getPage().setDefaultNavigationTimeout(configuration.getDefaultNavigationTimeout());
        getPage().setDefaultTimeout(configuration.getDefaultTimeout());
    }

}
