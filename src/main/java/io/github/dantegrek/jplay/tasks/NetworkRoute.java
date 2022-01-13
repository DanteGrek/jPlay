package io.github.dantegrek.jplay.tasks;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import io.github.dantegrek.enums.RestMethod;
import io.github.dantegrek.jplay.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

abstract class NetworkRoute<T extends NetworkRoute<T>> extends Task {

    /**
     *
     */
    protected Page page = null;
    /**
     *
     */
    protected BrowserContext context = null;
    /**
     *
     */
    protected String url = null;
    /**
     *
     */
    protected Predicate<String> urlPredicate = null;
    /**
     *
     */
    protected RestMethod restMethod = null;
    /**
     *
     */
    protected final Map<String, String> headers = new HashMap();

    private void checkIfPageOrContextStarted() {
        if (actor.currentPage() == null) {
            throw new RuntimeException("You have to start context and page before creating network route.");
        }
    }

    /**
     * Throw Runtime exception with on current page on context message.
     */
    protected void throwRuntimeExceptionFromRoute() {
        throw new RuntimeException("You have to specify 'onCurrentPage()' or 'onCurrentContext()'.");
    }

    /**
     * Checks if REST method was set
     * @param route instance of Route
     * @return boolean
     */
    protected boolean isRestMethodMatch(Route route) {
        return this.restMethod == null || route.request().method().equals(this.restMethod.name());
    }

    /**
     * Checks if set page
     * @return boolean
     */
    protected boolean isPageNotNull() {
        return this.page != null;
    }

    /**
     * Checks if set context
     * @return boolean
     */
    protected boolean isContextNotNull() {
        return this.context != null;
    }

    /**
     * Set url as glob pattern.
     *
     * @param globPattern to put Mock or Request on
     * @return instance of Mock.
     */
    public T forUrl(String globPattern) {
        if(this.urlPredicate != null){
            throw new RuntimeException("You already set url predicate for this task.");
        }
        this.url = globPattern;
        return (T) this;
    }

    /**
     * Set url as glob pattern.
     *
     * @param globPatternName should be uniq key you can use later ro remember or remove Mock or Request.
     * @param globPattern to put Mock or Request on
     * @return instance of Mock.
     */
    public T forUrl(String globPatternName, String globPattern) {
        if(this.urlPredicate != null){
            throw new RuntimeException("You already set url predicate for this task.");
        }
        this.url = globPattern;
        this.actor.remember(globPatternName, this.url);
        return (T) this;
    }

    /**
     * Set url predicate, but you will not be able to remove Mock or Request based on this predicate.
     * To have ability remove Mock or request use 'forUrl(String predicateName, Predicate urlPredicate)'.
     * Because remove methods works based on predicateName.
     *
     * @param urlPredicate string predicate which returns true or false
     * @return instance if Mock or Request, depends on caller class.
     */
    public T forUrl(Predicate<String> urlPredicate) {
        if(this.url != null){
            throw new RuntimeException("You already set url for this task.");
        }
        this.urlPredicate = urlPredicate;
        return (T) this;
    }

    /**
     * Set url predicate and store this predicate to remove it later by name.
     *
     * @param predicateName should be uniq key you can use later ro remember or remove Mock or Request.
     * @param urlPredicate string predicate which returns true or false
     * @return instance if Mock or Request, depends on caller class.
     */
    public T forUrl(String predicateName, Predicate<String> urlPredicate) {
        if(this.url != null){
            throw new RuntimeException("You already set url for this task.");
        }
        this.urlPredicate = urlPredicate;
        this.actor.remember(predicateName, this.urlPredicate);
        return (T) this;
    }

    /**
     * Points Mock or Request on to REST method
     * @param restMethod of Mock or Request
     * @return instance of Mock or Request
     */
    public T forMethod(RestMethod restMethod) {
        this.restMethod = restMethod;
        return (T) this;
    }

    /**
     * Set mock on current page/tab requests.
     *
     * @return instance of Mock or Request.
     */
    public T onCurrentPage() {
        checkIfPageOrContextStarted();
        if (this.context != null) {
            throw new RuntimeException("You already specified 'onCurrentContext'.");
        }
        this.page = actor.currentPage();
        return (T) this;
    }

    /**
     * Set mock on all requests from pages/tabs in current browser context.
     *
     * @return instance of Mock or Request.
     */
    public T onCurrentContext() {
        checkIfPageOrContextStarted();
        if (this.page != null) {
            throw new RuntimeException("You already specified 'onCurrentPage'.");
        }
        this.context = actor.currentPage().context();
        return (T) this;
    }
}
