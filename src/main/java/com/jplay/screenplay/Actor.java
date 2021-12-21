package com.jplay.screenplay;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

import java.util.List;

public class Actor {

    private Configuration configuration = new Configuration(this);
    private BrowserManager browserManager = new BrowserManager();

    private static ThreadLocal<Actor> actor = ThreadLocal.withInitial(() -> new Actor());

    private Actor() {
    }

    public static Actor actor() {
        return actor.get();
    }

    // Browser manager methods

    BrowserManager getBrowserManager() {
        return browserManager;
    }

    // Config methods

    public Configuration config() {
        return configuration;
    }

    public Configuration cleanConfig() {
        return configuration = new Configuration(this);
    }

    //Browser methods

    public Actor createBrowser() {
        this.getBrowserManager().create(this.configuration.getLaunchOptions(), this.configuration.getContextOptions());
        return actor.get();
    }

    public Actor createPureBrowser() {
        this.getBrowserManager().createBrowser(this.configuration.getLaunchOptions());
        return actor.get();
    }

    public Actor closeBrowser() {
        this.getBrowserManager().closeBrowser();
        this.getBrowserManager().setBrowser(null);
        this.getBrowserManager().setBrowserContext(null);
        this.getBrowserManager().setPage(null);
        return this;
    }

    // Context methods

    public Actor createContextAndTab() {
        this.getBrowserManager().createContextAndTab(this.configuration.getContextOptions());
        return actor.get();
    }

    public Actor closeCurrentContext() {
        this.getBrowserManager().getBrowserContext().close();
        return this;
    }

    private List<BrowserContext> getContextsFromBrowser() {
        List<BrowserContext> contexts = this.getBrowserManager().getBrowser().contexts();
        if (contexts.isEmpty()) {
            throw new RuntimeException("Browser does not have contexts, please start one using method " +
                    "'createContextAndTab()' or use 'createBrowser()' to create browser with context and tab.");
        }
        return contexts;
    }

    public Actor switchContextByIndex(int index) {
        BrowserContext context = this.getContextsFromBrowser().get(index);
        this.getBrowserManager().setBrowserContext(context);
        return this;
    }

    // Page methods

    public Actor openNewTab() {
        BrowserContext context = this.getBrowserManager().getBrowserContext();
        if (context == null) {
            throw new RuntimeException("You can not open new tab without context. " +
                    "Please use 'createContextAndTab()' instead of 'openNewTab()' " +
                    "or 'createBrowser()' instead of 'createPureBrowser()', it will create browser with tab.");
        }
        this.getBrowserManager().setPage(context.newPage());
        return actor.get();
    }

    public Actor closeCurrentTab() {
        this.getBrowserManager().getPage().close();
        return actor.get();
    }

    private List<Page> getPagesFromCurrentContext() {
        List<Page> pages = this.getBrowserManager().getPage().context().pages();
        if (pages.isEmpty()) {
            throw new RuntimeException("Current context does not have pages, " +
                    "please start one with method 'openNewTab()' or change current context with 'switchContextByIndex()'" +
                    "or create new context with tab using 'createContextAndTab()'.");
        }
        return pages;
    }

    public Actor switchTabByIndex(int index) {
        Page page = this.getPagesFromCurrentContext().get(index);
        this.getBrowserManager().setPage(page);
        page.bringToFront();
        return actor.get();
    }

    public Actor switchTabByTitle(String title) {
        List<Page> pages = this.getPagesFromCurrentContext().stream()
                .filter(tab -> tab.title().equals(title)).toList();
        if (pages.size() > 1) {
            throw new RuntimeException("More then one tab in current context has title '" + title +
                    "', in such cases better to use switchTabByIndex(int index).");
        } else if (pages.size() == 0) {
            throw new RuntimeException("None of tabs in current context has title '" + title +"'");
        }
        this.getBrowserManager().setPage(pages.get(0));
        return actor.get();
    }

    // Actions

    public Actor navigateTo(String url) {
        this.getBrowserManager().getPage()
                .navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    // Execute actions and tasks methods

    protected final <T extends Action> T executeAction(T action) {
        action.setActor(this);
        action.setPage(this.browserManager.getPage());
        return action;
    }

    protected final Actor executeTask(Task task) {
        task.setActor(this);
        task.setPage(this.browserManager.getPage());
        task.perform();
        return this;
    }

    // Execute syntax sugar
    public <T extends Action> T attemptTo(T action) {
        return this.executeAction(action);
    }

    public Actor attemptTo(Task task) {
        return this.executeTask(task);
    }

    public <T extends Action> T does(T action) {
        return this.executeAction(action);
    }

    public Actor does(Task task) {
        return this.executeTask(task);
    }

    public <T extends Action> T expectThat(T action) {
        return this.executeAction(action);
    }

    public Actor expectThat(Task task) {
        return this.executeTask(task);
    }
}
