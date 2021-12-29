package io.github.dantegrek.screenplay;

import com.microsoft.playwright.*;
import io.github.dantegrek.enums.BrowserName;

/**
 * This class is browser, context and page keeper.
 */
public class BrowserManager {

    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    void setBrowser(Browser browser) {
        this.browser = browser;
    }

    void setBrowserContext(BrowserContext browserContext) {
        this.browserContext = browserContext;
    }

    void setPage(Page page) {
        this.page = page;
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
    private Browser createBrowser(BrowserName browserName, BrowserType.LaunchOptions launchOptions) {
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

    void create(Configuration configuration) {
        setBrowser(createBrowser(configuration.getBrowserName(), configuration.getLaunchOptions()));
        setBrowserContext(getBrowser().newContext(configuration.getContextOptions()));
        Page page = getBrowserContext().newPage();
        page.setDefaultNavigationTimeout(configuration.getDefaultNavigationTimeout());
        page.setDefaultTimeout(configuration.getDefaultTimeout());
        setPage(page);
    }

    void createBrowser(Configuration configuration) {
        setBrowser(createBrowser(configuration.getBrowserName(), configuration.getLaunchOptions()));
    }

    void createContextAndTab(Configuration configuration) {
        setBrowserContext(getBrowser().newContext(configuration.getContextOptions()));
        Page page = getBrowserContext().newPage();
        page.setDefaultNavigationTimeout(configuration.getDefaultNavigationTimeout());
        page.setDefaultTimeout(configuration.getDefaultTimeout());
        setPage(page);
    }

}
