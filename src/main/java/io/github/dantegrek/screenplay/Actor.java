package io.github.dantegrek.screenplay;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.WaitUntilState;

import java.util.List;

/**
 * This is main class in project. Ir represents actor which act with system under test.
 */
public final class Actor {

    private Configuration configuration = new Configuration(this);
    private BrowserManager browserManager = new BrowserManager();

    private static ThreadLocal<Actor> actor = ThreadLocal.withInitial(() -> new Actor());

    private Actor() {
    }

    /**
     * This static function allows you to access actor in different methods in your project without
     * creating pointer for this object. It simply cals thread local collection.
     *
     * @return instance of Actor if already exists and creates new if not.
     */
    public static Actor actor() {
        return actor.get();
    }

    // Browser manager methods

    BrowserManager getBrowserManager() {
        return browserManager;
    }

    // Config methods

    /**
     * This method point user to configuration chain.
     *
     * @return instance of Configuration
     */
    public Configuration config() {
        return configuration;
    }

    /**
     * This method creates new and clear configuration instance.
     * @return instance of Configuration
     */
    public Configuration cleanConfig() {
        return configuration = new Configuration(this);
    }

    //Browser methods

    /**
     * This method starts browser with new context and tab.
     * @return instance of Actor
     */
    public Actor createBrowser() {
        this.getBrowserManager().create(this.configuration);
        return actor.get();
    }

    /**
     * This method start browser without context and tab.
     * @return instance of Actor
     */
    public Actor createPureBrowser() {
        this.getBrowserManager().createBrowser(this.configuration);
        return actor.get();
    }

    /**
     * This method closes browser with context and tab.
     * @return instance of Actor
     */
    public Actor closeBrowser() {
        this.getBrowserManager().closeBrowser();
        this.getBrowserManager().setBrowser(null);
        this.getBrowserManager().setBrowserContext(null);
        this.getBrowserManager().setPage(null);
        return this;
    }

    // Context methods

    /**
     * This method creates new context and tab. Can be used after 'createPureBrowser()'
     * or to create new incognito tab.
     * @return instance of Actor
     */
    public Actor createContextAndTab() {
        this.getBrowserManager().createContextAndTab(this.configuration);
        return actor.get();
    }

    /**
     * This method closes current context with tab.
     * @return instance of Actor
     */
    public Actor closeCurrentContext() {
        this.getBrowserManager().getBrowserContext().close();
        return this;
    }

    /**
     * This method allow user to access all contexts.
     * @return List of BrowserContexts.
     */
    private List<BrowserContext> getContextsFromBrowser() {
        List<BrowserContext> contexts = this.getBrowserManager().getBrowser().contexts();
        if (contexts.isEmpty()) {
            throw new RuntimeException("Browser does not have contexts, please start one using method " +
                    "'createContextAndTab()' or use 'createBrowser()' to create browser with context and tab.");
        }
        return contexts;
    }

    /**
     * This method switch focus between contexts.
     * @param index of context in list.
     * @return instance of Actor
     */
    public Actor switchContextByIndex(int index) {
        BrowserContext context = this.getContextsFromBrowser().get(index);
        this.getBrowserManager().setBrowserContext(context);
        return this;
    }

    // Page methods

    /**
     * This method get access to current page/tab.
     * @return instance of active tab.
     */
    public Page currentPage() {
        return this.getBrowserManager().getPage();
    }

    /**
     * This method opens new tab in scope of the same context, such tab will have common localstorage,
     * session storage and cookies with previous one.
     * @return instance of Actor
     */
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

    /**
     * This method closes tab in focus.
     * @return instance of Actor
     */
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

    /**
     * This method switch tab in current context.
     * @param index of tab in current context
     * @return instance of Actor
     */
    public Actor switchTabByIndex(int index) {
        Page page = this.getPagesFromCurrentContext().get(index);
        this.getBrowserManager().setPage(page);
        page.bringToFront();
        return actor.get();
    }

    /**
     * This method switch tab in current context.
     * @param title of tab.
     * @return instance of Actor
     */
    public Actor switchTabByTitle(String title) {
        List<Page> pages = this.getPagesFromCurrentContext().stream()
                .filter(tab -> tab.title().equals(title)).toList();
        if (pages.size() > 1) {
            throw new RuntimeException("More then one tab in current context has title '" + title +
                    "', in such cases better to use switchTabByIndex(int index).");
        } else if (pages.size() == 0) {
            throw new RuntimeException("None of tabs in current context has title '" + title + "'");
        }
        this.getBrowserManager().setPage(pages.get(0));
        return actor.get();
    }

    // Waits

    /**
     * Waits till network activity will be still 500 milliseconds.
     *
     * @return instance of Actor
     */
    public Actor waitTillNetworkIdle() {
        this.getBrowserManager().getPage().waitForLoadState(LoadState.NETWORKIDLE);
        return this;
    }

    /**
     * Waits till HTML document will be loaded.
     *
     * @return instance of Actor
     */
    public Actor waitTillDocumentLoaded() {
        this.getBrowserManager().getPage().waitForLoadState(LoadState.DOMCONTENTLOADED);
        return this;
    }

    // Navigations

    /**
     * Navigates to url and wait till document loaded.
     *
     * @param url String.
     * @return instance of Actor
     */
    public Actor navigateTo(String url) {
        this.getBrowserManager().getPage()
                .navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    /**
     * Click back browser button and wait till document loaded.
     *
     * @return instance of Actor
     */
    public Actor goBack() {
        this.getBrowserManager().getPage()
                .goBack(new Page.GoBackOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    /**
     * Click forward browser button and wait till document loaded.
     *
     * @return instance of Actor
     */
    public Actor goForward() {
        this.getBrowserManager().getPage()
                .goForward(new Page.GoForwardOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    // Actions

    /**
     * Click on element.
     *
     * @param selector css or xpath.
     * @return instance of Actor
     */
    public Actor click(String selector) {
        this.getBrowserManager().getPage().click(selector);
        return this;
    }

    /**
     * Right button click on element.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor rightClick(String selector) {
        this.getBrowserManager().getPage().click(selector, new Page.ClickOptions().setButton(MouseButton.RIGHT));
        return this;
    }

    /**
     * Double button click on element.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor doubleClick(String selector) {
        this.getBrowserManager().getPage().dblclick(selector);
        return this;
    }

    /**
     * Drag and drop element to another element.
     *
     * @param sourceSelector css or xpath
     * @param targetSelector css of xpath
     * @return instance of Actor
     */
    public Actor dragAndDrop(String sourceSelector, String targetSelector) {
        this.getBrowserManager().getPage().dragAndDrop(sourceSelector, targetSelector);
        return this;
    }

    /**
     * Check checkbox.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor check(String selector) {
        this.getBrowserManager().getPage().check(selector);
        return this;
    }

    /**
     * Uncheck checkbox.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor uncheck(String selector) {
        this.getBrowserManager().getPage().uncheck(selector);
        return this;
    }

    // Helpers

    /**
     * Set content in tab
     *
     * @param html document
     * @return instance of Actor
     */
    public Actor setContent(String html) {
        this.getBrowserManager().getPage().setContent(html);
        return this;
    }

    // Execute actions and tasks methods

    private <T extends Action> T executeAction(T action) {
        action.setActor(this);
        return action;
    }

    private Actor executeTask(Task task) {
        task.setActor(this);
        task.perform();
        return this;
    }

    // Execute syntax sugar

    /**
     * This method allows user to put his implementation in to invocation chain
     * @param action instance of class which is child of Action class.
     * @param <T> any type which is a child of Action class.
     * @return instance of Action
     */
    public <T extends Action> T attemptTo(T action) {
        return this.executeAction(action);
    }

    /**
     * This method perform users task.
     * @param task instance of class which is child of Task class.
     * @return instance of Action
     */
    public Actor attemptTo(Task task) {
        return this.executeTask(task);
    }

    /**
     * This method allows user to put his implementation in to invocation chain
     * @param action instance of class which is child of Action class.
     * @param <T> any type which is a child of Action class.
     * @return instance of Action
     */
    public <T extends Action> T does(T action) {
        return this.executeAction(action);
    }

    /**
     * This method perform users task.
     * @param task instance of class which is child of Task class.
     * @return instance of Action
     */
    public Actor does(Task task) {
        return this.executeTask(task);
    }

    /**
     * This method allows user to put his implementation in to invocation chain
     * @param action instance of class which is child of Action class.
     * @param <T> any type which is a child of Action class.
     * @return instance of Action
     */
    public <T extends Action> T expectThat(T action) {
        return this.executeAction(action);
    }

    /**
     * This method perform users task.
     * @param task instance of class which is child of Task class.
     * @return instance of Action
     */
    public Actor expectThat(Task task) {
        return this.executeTask(task);
    }
}
