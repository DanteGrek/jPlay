package io.github.dantegrek.jplay;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.github.dantegrek.enums.Key;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.github.dantegrek.jplay.JsStrings.JS_PSEUDO_ELEMENT_CONTENT;

/**
 * This is main class in project. Ir represents actor which act with system under test.
 */
public final class Actor {

    private Configuration configuration = new Configuration(this);
    private BrowserManager browserManager = new BrowserManager();
    private Memory memory = new Memory();
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

    // Memory

    /**
     * Access memory object, Map which allows you to store test data for current instance of Actor.
     *
     * @return instance of Memory
     */
    public Memory memory() {
        return this.memory;
    }

    /**
     * Remembers value by key you can recall later in thread.
     *
     * @param key   to value
     * @param value you want to remember.
     * @return instance of Actor
     */
    public Actor remember(Object key, Object value) {
        this.memory.remember(key, value);
        return this;
    }

    /**
     * Recalls value by key and cast it to V.
     *
     * @param key to value.
     * @param <T> type of value method should return.
     * @return value after cast to valueType
     */
    public <T> T recall(Object key) {
        return this.memory.recall(key);
    }

    /**
     * Recalls value by key.
     * Checks if value is an instance of valueType or interface implementation and then cast to value type.
     *
     * @param key       to value.
     * @param valueType expected value class or implemented interface.
     * @param <T>       not required for this implementation.
     * @return value after cast to valueType
     */
    public <T> T recall(Object key, Class<T> valueType) {
        return this.memory.recallInstanceOf(key, valueType);
    }

    /**
     * Removes key and value from memory.
     *
     * @param key to value
     * @return instance of Actor
     */
    public Actor forget(Object key) {
        this.memory.forget(key);
        return this;
    }

    /**
     * Removes all key value pairs from memory.
     *
     * @return instance of Actor
     */
    public Actor clearMemory() {
        this.memory.clear();
        return this;
    }

    // Browser manager methods

    private BrowserManager getBrowserManager() {
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
    public Actor clearConfig() {
        this.configuration = new Configuration(this);
        this.isStrict = false;
        return this;
    }

    private void startTraceIfSpecified() {
        if (this.configuration.getWithTrace()) {
            this.getBrowserManager()
                    .getBrowserContext()
                    .tracing()
                    .start(new Tracing.StartOptions()
                            .setScreenshots(true)
                            .setSnapshots(true));
        }
    }

    private void stopAndSaveTrace() {
        if (this.configuration.getWithTrace()) {
            this.getBrowserManager()
                    .getBrowserContext()
                    .tracing()
                    .stop(new Tracing.StopOptions().setPath(
                            this.configuration.getTraceDir().resolve(this.configuration.getTraceName())));
        }
    }

    /**
     * Start tracing chunk
     *
     * @return instance of Actor
     */
    public Actor startTraceChunk() {
        contextConfig().withTrace(true);
        startTraceIfSpecified();
        this.getBrowserManager()
                .getBrowserContext().tracing().startChunk();
        return this;
    }

    /**
     * Start tracing chunk
     *
     * @param title of trace.
     * @return instance of Actor
     */
    public Actor startTracingChunk(String title) {
        contextConfig().withTrace(true);
        startTraceIfSpecified();
        this.getBrowserManager()
                .getBrowserContext().tracing().startChunk(new Tracing.StartChunkOptions().setTitle(title));
        return this;
    }

    /**
     * Stop tracing chunk
     *
     * @param prefix of trace name, ${prefix}-chunk-of-chromium-trace.zip
     * @return instance of Actor
     */
    public Actor stopTracingChunk(String prefix) {
        this.getBrowserManager()
                .getBrowserContext()
                .tracing()
                .stop(new Tracing.StopOptions().setPath(
                        this.configuration.getTraceDir()
                                .resolve(prefix + "-chunk-of-" + this.configuration.getTraceNameSuffix())));
        contextConfig().withTrace(false);
        return this;
    }

    //Browser methods

    /**
     * This method starts browser with new context and tab.
     *
     * @return instance of Actor
     */
    public Actor startBrowser() {
        this.getBrowserManager().startBrowserContextAndTab(this.configuration);
        this.startTraceIfSpecified();
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
        this.stopAndSaveTrace();
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
        this.startTraceIfSpecified();
        return actor.get();
    }

    /**
     * This method closes current context with tab.
     *
     * @return instance of Actor
     */
    public Actor closeCurrentContext() {
        this.stopAndSaveTrace();
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
        this.currentFrame().waitForLoadState(LoadState.NETWORKIDLE);
        return this;
    }

    /**
     * Waits till HTML document will be loaded.
     *
     * @return instance of Actor
     */
    public Actor waitTillDocumentLoaded() {
        this.currentFrame().waitForLoadState(LoadState.DOMCONTENTLOADED);
        return this;
    }

    /**
     * Waits till HTML document will be loaded.
     *
     * @return instance of Actor
     */
    public Actor waitTillNavigationFinished(Runnable navigationTrigger) {
        this.currentFrame().waitForNavigation(new Frame.WaitForNavigationOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED), navigationTrigger);
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
        this.currentFrame()
                .navigate(url, new Frame.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
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

    private boolean isStrict = false;

    /**
     * Use strict make all user actions like clicks, hover... expects that selector resolves to single element otherwise
     * it will fail.
     *
     * @param isStrict boolean
     * @return instance of Actor
     */
    public Actor useStrict(boolean isStrict) {
        this.isStrict = isStrict;
        return this;
    }

    /**
     * Focus on web element.
     *
     * @param selector css of xpath
     * @return instance of Actor
     */
    public Actor focus(String selector) {
        this.currentFrame().focus(selector, new Frame.FocusOptions().setStrict(isStrict));
        return this;
    }

    /**
     * Click on element.
     *
     * @param selector css or xpath.
     * @return instance of Actor
     */
    public Actor click(String selector) {
        this.currentFrame().click(selector, new Frame.ClickOptions().setStrict(isStrict));
        return this;
    }

    /**
     * Right button click on element.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor rightClick(String selector) {
        this.currentFrame().click(selector, new Frame.ClickOptions().setButton(MouseButton.RIGHT).setStrict(isStrict));
        return this;
    }

    /**
     * Double button click on element.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor doubleClick(String selector) {
        this.currentFrame().dblclick(selector, new Frame.DblclickOptions().setStrict(isStrict));
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
        this.currentFrame().dragAndDrop(sourceSelector, targetSelector,
                new Frame.DragAndDropOptions().setStrict(isStrict));
        return this;
    }

    /**
     * Check checkbox.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor check(String selector) {
        this.currentFrame().check(selector, new Frame.CheckOptions().setStrict(isStrict));
        return this;
    }

    /**
     * Uncheck checkbox.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor uncheck(String selector) {
        this.currentFrame().uncheck(selector, new Frame.UncheckOptions().setStrict(isStrict));
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
        this.currentFrame().type(selector, text, new Frame.TypeOptions().setStrict(isStrict));
        return this;
    }

    /**
     * This method type chars one by one with delay.
     *
     * @param selector     css or xpath
     * @param text         value
     * @param delayTimeout between each char
     * @return instance of Actor
     */
    public Actor typeTextWithDelay(String selector, String text, double delayTimeout) {
        this.currentFrame().type(selector, text, new Frame.TypeOptions().setDelay(delayTimeout).setStrict(isStrict));
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
        this.currentFrame().fill(selector, text, new Frame.FillOptions().setStrict(isStrict));
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
        this.currentFrame().tap(selector, new Frame.TapOptions().setStrict(isStrict));
        return this;
    }

    /**
     * Mouse over.
     *
     * @param selector css or xpath
     * @return instance of Actor
     */
    public Actor hover(String selector) {
        this.currentFrame().hover(selector, new Frame.HoverOptions().setStrict(isStrict));
        return this;
    }

    /**
     * Single selection matching the value attribute.
     *
     * @param selector css or xpath
     * @param value    to be selected
     * @return instance of Actor
     */
    public Actor selectByValue(String selector, String value) {
        this.currentFrame().selectOption(selector, value, new Frame.SelectOptionOptions().setStrict(isStrict));
        return this;
    }

    /**
     * Multiple selected items
     *
     * @param selector css or xpath
     * @param values   to be selected
     * @return instance of Actor
     */
    public Actor selectByValue(String selector, String... values) {
        this.currentFrame().selectOption(selector, values, new Frame.SelectOptionOptions().setStrict(isStrict));
        return this;
    }

    /**
     * Single selection matching the label
     *
     * @param selector css or xpath
     * @param label    to be selected
     * @return instance of Actor
     */
    public Actor selectByText(String selector, String label) {
        this.currentFrame().selectOption(selector,
                new SelectOption().setLabel(label),
                new Frame.SelectOptionOptions().setStrict(isStrict));
        return this;
    }

    /**
     * Multiple selection matching the labels
     *
     * @param selector css or xpath
     * @param labels   to be selected
     * @return instance of Actor
     */
    public Actor selectByText(String selector, String... labels) {
        this.currentFrame().selectOption(selector,
                List.of(labels).stream().map(label -> new SelectOption().setLabel(label)).toArray(SelectOption[]::new),
                new Frame.SelectOptionOptions().setStrict(isStrict));
        return this;
    }

    /**
     * This method wait till upload file input visible, then scroll into view if needed and sets value.
     * Sets the value of the file input to these file paths or files.
     * If some filePaths are relative paths, then they are resolved relative to
     * the current working directory. For empty array, clears the selected files.
     *
     * @param selector css or xpath
     * @param file     to upload
     * @return instance of Actor
     */
    public Actor uploadFile(String selector, Path file) {
        return this.uploadFile(selector, false, file);
    }

    /**
     * This method wait till upload file input visible, then scroll into view if needed and sets value.
     * Sets the value of the file input to these file paths or files.
     * If some filePaths are relative paths, then they are resolved relative to
     * the current working directory. For empty array, clears the selected files.
     *
     * @param selector    css or xpath
     * @param hiddenInput if false wait till input visible and scroll if needed, true wait till input attached to the dom.
     * @param file        to upload
     * @return instance of Actor
     */
    public Actor uploadFile(String selector, boolean hiddenInput, Path file) {
        Locator locator = this.currentFrame().locator(selector);
        if (!hiddenInput) {
            locator.scrollIntoViewIfNeeded();
        }
        locator.setInputFiles(file);
        return this;
    }

    /**
     * This method wait till upload file input visible, then scroll into view if needed and sets value.
     * Sets the value of the file input to these file paths or files.
     * If some filePaths are relative paths, then they are resolved relative to
     * the current working directory. For empty array, clears the selected files.
     *
     * @param selector css or xpath
     * @param files    to upload
     * @return instance of Actor
     */
    public Actor uploadFiles(String selector, List<Path> files) {
        return this.uploadFiles(selector, false, files);
    }

    /**
     * This method wait till upload file input visible, then scroll into view if needed and sets value.
     * Sets the value of the file input to these file paths or files.
     * If some of the filePaths are relative paths, then they are resolved relative to
     * the current working directory. For empty array, clears the selected files.
     *
     * @param selector    css or xpath
     * @param hiddenInput if false wait till input visible and scroll if needed, true wait till input attached to the dom.
     * @param files       to upload
     * @return instance of Actor
     */
    public Actor uploadFiles(String selector, boolean hiddenInput, List<Path> files) {
        Locator locator = this.currentFrame().locator(selector);
        if (!hiddenInput) {
            locator.scrollIntoViewIfNeeded();
        }
        locator.setInputFiles(files.toArray(Path[]::new));
        return this;
    }

    /**
     * This method wait till upload file input visible, then scroll into view if needed and sets value.
     * Sets the value of the file input to these file paths or files.
     * If some of the filePaths are relative paths, then they are resolved relative to
     * the current working directory. For empty array, clears the selected files.
     *
     * @param selector css or xpath
     * @param files    to upload
     * @return instance of Actor
     */
    public Actor uploadFiles(String selector, Path... files) {
        return this.uploadFiles(selector, false, files);
    }

    /**
     * This method wait till upload file input visible, then scroll into view if needed and sets value.
     * Sets the value of the file input to these file paths or files.
     * If some of the filePaths are relative paths, then they are resolved relative to
     * the current working directory. For empty array, clears the selected files.
     *
     * @param selector    css or xpath
     * @param hiddenInput if false wait till input visible and scroll if needed, true wait till input attached to the dom.
     * @param files       to upload
     * @return instance of Actor
     */
    public Actor uploadFiles(String selector, boolean hiddenInput, Path... files) {
        Locator locator = this.currentFrame().locator(selector);
        if (!hiddenInput) {
            locator.scrollIntoViewIfNeeded();
        }
        locator.setInputFiles(files);
        return this;
    }

    /**
     * Click on button and wait till file will be downloaded.
     *
     * @param selector to element which triggers download file.
     * @return instance of Download.
     */
    public Download clickAndWaitTillDownload(String selector) {
        return this.currentPage().waitForDownload(
                () -> currentFrame().click(selector, new Frame.ClickOptions().setStrict(isStrict)));
    }

    /**
     * Click on button and wait till file will be downloaded.
     *
     * @param selector to element which triggers download file.
     * @param timeout  to wait on downloaded file.
     * @return instance of Download.
     */
    public Download clickAndWaitTillDownload(String selector, double timeout) {
        return this.currentPage().waitForDownload(new Page.WaitForDownloadOptions().setTimeout(timeout),
                () -> currentFrame().click(selector, new Frame.ClickOptions().setStrict(isStrict)));
    }

    /**
     * This method helps extract content from pseudo-element
     *
     * @param selector      css selector which points on element with ::after or ::before
     * @param pseudoElement after, before or any another pseudo-element.
     * @return value of attribute content as String.
     */
    public String getPseudoElementContent(String selector, String pseudoElement) {
        Locator locator = this.currentFrame().locator(selector);
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String content = locator.evaluate(String.format(JS_PSEUDO_ELEMENT_CONTENT, pseudoElement)).toString();
        if (content.equals("none") || content.isEmpty()) {
            return content;
        }
        // JS returns string as object and when Java do toString()
        // it wraps js string into java String that is why we need to rid off first and last characters in next line
        // they always will be "..."
        return content.substring(1, content.length() - 1);
    }

    // Keyboard

    /**
     * Shortcut for Keyboard.down(key) and Keyboard.up(key).
     *
     * @param key Key enum
     * @return instance of Actor
     */
    public Actor press(Key key) {
        this.currentPage().keyboard().press(key.keyCode);
        return this;
    }

    /**
     * Shortcut for Keyboard.down(key) and Keyboard.up(key).
     *
     * @param key Key enum
     * @return instance of Actor
     */
    public Actor pressKey(Key key) {
        this.press(key);
        return this;
    }

    /**
     * Shortcut for Keyboard.down(key) and Keyboard.up(key).
     *
     * @param key          Key enum
     * @param milliseconds dilay between down and up.
     * @return instance of Actor
     */
    public Actor pressKeyWithDelay(Key key, double milliseconds) {
        this.currentPage().keyboard().press(key.keyCode, new Keyboard.PressOptions().setDelay(milliseconds));
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

    /**
     * Evaluate
     *
     * @param jsScript java script injection
     * @param object   argument will be passed to JS
     * @return instance of Actor
     */
    public Actor evaluate(String jsScript, Object object) {
        this.currentFrame().evaluate(jsScript, object);
        return this;
    }

    // Mock

    private void checkPageNotNullBeforeRemoveMock() {
        if (this.currentPage() == null) {
            throw new RuntimeException("You are trying to remove mock but page and context were closed. " +
                    "If you closed them or browser you do not need to remove mock.");
        }
    }

    /**
     * Removes Mock or Request from page by url
     *
     * @param url which was mocked or overridden.
     * @return instance of Actor.
     */
    public Actor removeMockFromPageForUrl(String url) {
        checkPageNotNullBeforeRemoveMock();
        this.currentPage().unroute(url);
        return this;
    }

    /**
     * Removes Mock or Request from context by url
     *
     * @param url which was mocked or overridden.
     * @return instance of Actor.
     */
    public Actor removeMockFromContextForUrl(String url) {
        checkPageNotNullBeforeRemoveMock();
        this.currentPage().context().unroute(url);
        return this;
    }

    /**
     * Removes Mock or Request from context by predicate or url name
     *
     * @param name which was mocked or overridden.
     * @return instance of Actor.
     */
    public Actor removeRoutFromPageForUrlByName(String name) {
        checkPageNotNullBeforeRemoveMock();
        this.currentPage().unroute(this.<Predicate<String>>recall(name));
        return this;
    }

    /**
     * Removes Mock or Request from context by predicate name
     *
     * @param name which was mocked or overridden.
     * @return instance of Actor.
     */
    public Actor removeRoutFromContextForUrlByName(String name) {
        checkPageNotNullBeforeRemoveMock();
        this.currentPage().context().unroute(this.<Predicate<String>>recall(name));
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

    /**
     * This method perform users task.
     *
     * @param task instance of class which is child of Task class.
     * @return instance of Action
     */
    public Actor set(Task task) {
        return this.executeTask(task);
    }
}
