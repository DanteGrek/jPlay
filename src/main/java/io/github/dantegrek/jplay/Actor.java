package io.github.dantegrek.jplay;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.WaitUntilState;
import io.github.dantegrek.enums.Key;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

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
     * This method point user to timeout configuration chain.
     *
     * @return instance of ITimeoutConfig
     */
    public ITimeoutConfig timeoutConfig() {
        return configuration;
    }

    /**
     * This method point user to browser configuration chain.
     *
     * @return instance of IBrowserConfiguration
     */
    public IBrowserConfiguration browserConfig() {
        return configuration;
    }

    /**
     * This method point user to context configuration chain.
     *
     * @return instance of IContextConfiguration
     */
    public IContextConfiguration contextConfig() {
        return configuration;
    }

    /**
     * This method creates new and clear configuration instance.
     *
     * @return instance of Configuration
     */
    public Configuration cleanConfig() {
        return configuration = new Configuration(this);
    }

    //Browser methods

    /**
     * This method starts browser with new context and tab.
     *
     * @return instance of Actor
     */
    public Actor startBrowser() {
        this.getBrowserManager().startBrowserContextAndTab(this.configuration);
        return actor.get();
    }

    /**
     * This method start browser without context and tab.
     *
     * @return instance of Actor
     */
    public Actor startPureBrowser() {
        this.getBrowserManager().startBrowserOnly(this.configuration);
        return actor.get();
    }

    /**
     * This method closes browser with context and tab.
     *
     * @return instance of Actor
     */
    public Actor closeBrowser() {
        this.getBrowserManager().closeBrowser();
        this.getBrowserManager().setBrowser(null);
        this.getBrowserManager().setBrowserContext(null);
        this.getBrowserManager().setPage(null);
        this.getBrowserManager().setFrame(null);
        return this;
    }

    // Context methods

    /**
     * This method creates new context and tab. Can be used after 'startPureBrowser()'
     * or to create new incognito tab.
     *
     * @return instance of Actor
     */
    public Actor createContextAndTab() {
        this.getBrowserManager().createContextWithTab(this.configuration);
        return actor.get();
    }

    /**
     * This method closes current context with tab.
     *
     * @return instance of Actor
     */
    public Actor closeCurrentContext() {
        this.getBrowserManager().getBrowserContext().close();
        this.getBrowserManager().setFrame(null);
        return this;
    }

    /**
     * This method allow user to access all contexts.
     *
     * @return List of BrowserContexts.
     */
    private List<BrowserContext> getContextsFromBrowser() {
        List<BrowserContext> contexts = this.getBrowserManager().getBrowser().contexts();
        if (contexts.isEmpty()) {
            throw new RuntimeException("Browser does not have contexts, please start one using method " +
                    "'createContextAndTab()' or use 'startBrowser()' to create browser with context and tab.");
        }
        return contexts;
    }

    /**
     * This method switch focus between contexts.
     *
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
     *
     * @return instance of active tab.
     */
    public Page currentPage() {
        return this.getBrowserManager().getPage();
    }

    /**
     * This method returns wrapped Frame instance of current page if actor did not switch explicitly.
     *
     * @return Frame
     */
    public Frame currentFrame() {
        return this.getBrowserManager().getFrame() == null ?
                this.getBrowserManager().getPage().mainFrame() : this.getBrowserManager().getFrame();
    }

    /**
     * This method opens new tab in scope of the same context, such tab will have common localstorage,
     * session storage and cookies with previous one.
     *
     * @return instance of Actor
     */
    public Actor openNewTab() {
        BrowserContext context = this.getBrowserManager().getBrowserContext();
        if (context == null) {
            throw new RuntimeException("You can not open new tab without context. " +
                    "Please use 'createContextAndTab()' instead of 'openNewTab()' " +
                    "or 'startBrowser()' instead of 'startPureBrowser()', it will create browser with tab.");
        }
        this.getBrowserManager().setPage(context.newPage());
        return actor.get();
    }

    /**
     * This method closes tab in focus.
     *
     * @return instance of Actor
     */
    public Actor closeCurrentTab() {
        this.getBrowserManager().getPage().close();
        this.getBrowserManager().setFrame(null);
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
     *
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
     *
     * @param title of tab.
     * @return instance of Actor
     */
    public Actor switchTabByTitle(String title) {
        List<Page> pages = this.getPagesFromCurrentContext().stream()
                .filter(tab -> tab.title().equals(title)).collect(Collectors.toList());
        if (pages.size() > 1) {
            throw new RuntimeException("More then one tab in current context has title '" + title +
                    "', in such cases better to use switchTabByIndex(int index).");
        } else if (pages.size() == 0) {
            throw new RuntimeException("None of tabs in current context has title '" + title + "'");
        }
        this.getBrowserManager().setPage(pages.get(0));
        return actor.get();
    }

    /**
     * This method point all actors actions into Fame.
     *
     * @param selector should point on html tag 'iframe'
     * @return instance of Actor
     */
    public Actor switchOnFrame(String selector) {
        Frame frame = this.currentFrame().locator(selector).elementHandle().contentFrame();
        if (frame != null) {
            this.getBrowserManager().setFrame(frame);
        } else {
            throw new RuntimeException("Iframe was not fund by selector: '" + selector + "'");
        }
        return actor.get();
    }

    /**
     * This method always return actor to wrapped current page in Frame
     *
     * @return instance of Actor
     */
    public Actor switchOnMainFrame() {
        this.getBrowserManager().setFrame(null);
        return actor.get();
    }

    // Waits

    /**
     * Waits till network activity will be still 500 milliseconds.
     *
     * @return instance of Actor
     */
    public Actor waitTillNetworkIdle() {
        this.currentPage().waitForLoadState(LoadState.NETWORKIDLE);
        return this;
    }

    /**
     * Waits till HTML document will be loaded.
     *
     * @return instance of Actor
     */
    public Actor waitTillDocumentLoaded() {
        this.currentPage().waitForLoadState(LoadState.DOMCONTENTLOADED);
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
        this.currentPage()
                .navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    /**
     * Click back browser button and wait till document loaded.
     *
     * @return instance of Actor
     */
    public Actor goBack() {
        this.currentPage()
                .goBack(new Page.GoBackOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    /**
     * Click forward browser button and wait till document loaded.
     *
     * @return instance of Actor
     */
    public Actor goForward() {
        this.currentPage()
                .goForward(new Page.GoForwardOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        return this;
    }

    /**
     * Click browser button refresh tab.
     *
     * @return instance of Actor
     */
    public Actor reloadTab() {
        this.currentPage().reload(new Page.ReloadOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
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
        this.currentFrame().click(selector);
        return this;
    }

    /**
     * Right button click on element.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor rightClick(String selector) {
        this.currentFrame().click(selector, new Frame.ClickOptions().setButton(MouseButton.RIGHT));
        return this;
    }

    /**
     * Double button click on element.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor doubleClick(String selector) {
        this.currentFrame().dblclick(selector);
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
        this.currentFrame().dragAndDrop(sourceSelector, targetSelector);
        return this;
    }

    /**
     * Check checkbox.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor check(String selector) {
        this.currentFrame().check(selector);
        return this;
    }

    /**
     * Uncheck checkbox.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor uncheck(String selector) {
        this.currentFrame().uncheck(selector);
        return this;
    }

    /**
     * This method type chars one by one.
     *
     * @param selector css or xpath
     * @param text     value
     * @return instance of Actor
     */
    public Actor typeText(String selector, String text) {
        this.currentFrame().type(selector, text);
        return this;
    }

    /**
     * This method puts text in to field.
     *
     * @param selector css of xpath
     * @param text     value
     * @return instance of Actor
     */
    public Actor fillText(String selector, String text) {
        this.currentFrame().fill(selector, text);
        return this;
    }

    /**
     * This method taps an element matching selector by performing the following steps:
     * Find an element matching selector. If there is none, wait until a matching element is attached to the DOM.
     * Wait for actionability checks on the matched element, unless force option is set. If the element is detached during the checks, the whole action is retried.
     * Scroll the element into view if needed.
     * Use Page.touchscreen() to tap the center of the element, or the specified position.
     * Wait for initiated navigations to either succeed or fail, unless noWaitAfter option is set.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor tap(String selector) {
        this.currentFrame().tap(selector);
        return this;
    }

    /**
     * Mouse over.
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor hover(String selector) {
        this.currentFrame().hover(selector);
        return this;
    }

    /**
     * This method expects selector to point to an input element.
     * <p>
     * Sets the value of the file input to these file paths or files.
     * If some of the filePaths are relative paths, then they are resolved relative to
     * the current working directory. For empty array, clears the selected files.
     *
     * @param selector css or xpath
     * @param file     to upload
     * @return instance of Actor
     */
    public Actor uploadFile(String selector, Path file) {
        this.currentFrame().setInputFiles(selector, file);
        return this;
    }

    /**
     * This method expects selector to point to an input element.
     * <p>
     * Sets the value of the file input to these file paths or files.
     * If some of the filePaths are relative paths, then they are resolved relative to
     * the current working directory. For empty array, clears the selected files.
     *
     * @param selector css or xpath
     * @param files    to upload
     * @return instance of Actor
     */
    public Actor uploadFiles(String selector, List<Path> files) {
        this.currentFrame().setInputFiles(selector, files.toArray(Path[]::new));
        return this;
    }

    // Keyboard

    /**
     * Shortcut for Keyboard.down(key) and Keyboard.up(key).
     *
     * @param key Key enum
     * @return instance of Actor
     */
    public Actor key(Key key) {
        this.currentPage().keyboard().press(key.keyCode);
        return this;
    }

    /**
     * Shortcut for Keyboard.down(key) and Keyboard.up(key).
     *
     * @param key          Key enum
     * @param milliseconds dilay between down and up.
     * @return instance of Actor
     */
    public Actor keyWithDelay(Key key, double milliseconds) {
        this.currentPage().keyboard().press(key.keyCode, new Keyboard.PressOptions().setDelay(milliseconds));
        return this;
    }

    /**
     * Shortcut for Keyboard.down(key) and Keyboard.up(key).
     *
     * @param selector css pr xpath
     * @param key      Key enum
     * @return instance of Actor
     */
    public Actor key(String selector, Key key) {
        this.currentPage().press(selector, key.keyCode);
        return this;
    }

    /**
     * Dispatches a keydown event.
     *
     * @param key Key enum
     * @return instance of Actor
     */
    public Actor keyDown(Key key) {
        this.currentPage().keyboard().down(key.keyCode);
        return this;
    }

    /**
     * Dispatches a keyup event.
     *
     * @param key Key enum
     * @return instance of Actor
     */
    public Actor keyUp(Key key) {
        this.currentPage().keyboard().up(key.keyCode);
        return this;
    }

    /**
     * Emulate exotic characters from keyboard like "嗨"
     *
     * @param text any String
     * @return instance of Actor
     */
    public Actor insertText(String text) {
        this.currentPage().keyboard().insertText(text);
        return this;
    }

    // Dialog

    /**
     * Opens chain to handle dialogs like: alert, beforeunload, confirm or prompt.
     *
     * @return instance of dialog.
     */
    public DialogHandler dialog() {
        return new DialogHandler(this);
    }

    // Expect

    /**
     * Opens assert functionality.
     *
     * @return new instance of Expect
     */
    public Expect expectThat() {
        return new Expect(this).withTimeout(this.configuration.getExceptTimeout());
    }

    /**
     * Opens assert functionality.
     *
     * @return new instance of Expect
     */
    public Expect softExpectThat() {
        return new Expect(this).withSoftExpect(true).withTimeout(this.configuration.getExceptTimeout());
    }

    // Helpers

    /**
     * Set content in tab
     *
     * @param html document
     * @return instance of Actor
     */
    public Actor setContent(String html) {
        this.currentFrame().setContent(html);
        return this;
    }

    /**
     * Evaluate
     *
     * @param jsScript java script injection
     * @return instance of Actor
     */
    public Actor evaluate(String jsScript) {
        this.currentFrame().evaluate(jsScript);
        return this;
    }

    // Execute actions and tasks methods

    private <T extends Action> T executeAction(T action) {
        return action;
    }

    private Actor executeTask(Task task) {
        task.perform();
        return this;
    }

    // Execute syntax sugar

    /**
     * This method allows user to put his implementation in to invocation chain
     *
     * @param action instance of class which is child of Action class.
     * @param <T>    any type which is a child of Action class.
     * @return instance of Action
     */
    public <T extends Action> T attemptTo(T action) {
        return this.executeAction(action);
    }

    /**
     * This method perform users task.
     *
     * @param task instance of class which is child of Task class.
     * @return instance of Action
     */
    public Actor attemptTo(Task task) {
        return this.executeTask(task);
    }

    /**
     * This method allows user to put his implementation in to invocation chain
     *
     * @param action instance of class which is child of Action class.
     * @param <T>    any type which is a child of Action class.
     * @return instance of Action
     */
    public <T extends Action> T does(T action) {
        return this.executeAction(action);
    }

    /**
     * This method perform users task.
     *
     * @param task instance of class which is child of Task class.
     * @return instance of Action
     */
    public Actor does(Task task) {
        return this.executeTask(task);
    }

    /**
     * This method allows user to put his implementation in to invocation chain
     *
     * @param action instance of class which is child of Action class.
     * @param <T>    any type which is a child of Action class.
     * @return instance of Action
     */
    public <T extends Action> T expectThat(T action) {
        return this.executeAction(action);
    }

    /**
     * This method perform users task.
     *
     * @param task instance of class which is child of Task class.
     * @return instance of Action
     */
    public Actor expectThat(Task task) {
        return this.executeTask(task);
    }
}
