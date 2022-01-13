package io.github.dantegrek.jplay.tasks;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Helps mock responses to browser.
 */
public final class Mock extends NetworkRoute<Mock> {

    private final Route.FulfillOptions routeFullFillOptions = new Route.FulfillOptions();
    private int times;

    /**
     * Syntax sugar, creates instance of Mock
     * @return instance of Mock
     */
    public static Mock mock() {
        return new Mock();
    }

    /**
     * Set Mock to work particular amount of times.
     * @param times to be executed.
     * @return instance of Mock
     */
    public Mock useTimes(int times) {
        if (times < 1) {
            throw new RuntimeException("Minimum 1 time is allowed.");
        }
        this.times = times;
        return this;
    }

    // Full fill options

    /**
     * Response status code, defaults to 200.
     *
     * @param statusCode of response
     * @return instance of Mock.
     */
    public Mock withStatusCode(int statusCode) {
        this.routeFullFillOptions.setStatus(statusCode);
        return this;
    }

    /**
     * If set, equals to setting Content-Type response header.
     *
     * @param contentType value.
     * @return instance of Mock.
     */
    public Mock withContentType(String contentType) {
        this.routeFullFillOptions.setContentType(contentType);
        return this;
    }

    /**
     * Response headers. Header values will be converted to a string.
     *
     * @param headers as a Map of Strings
     * @return instance of Mock.
     */
    public Mock withHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * Adds header to Mock
     * @param headerName key
     * @param headerValue value
     * @return instance of Mock
     */
    public Mock withHeader(String headerName, String headerValue) {
        this.headers.put(headerName, headerValue);
        return this;
    }

    /**
     * Optional response body as text.
     *
     * @param body as String
     * @return instance of Mock.
     */
    public Mock withBody(String body) {
        this.routeFullFillOptions.setBody(body);
        return this;
    }

    /**
     * Optional response body as raw bytes.
     *
     * @param body as byte array.
     * @return instance of Mock.
     */
    public Mock withBody(byte[] body) {
        this.routeFullFillOptions.setBodyBytes(body);
        return this;
    }

    /**
     * File path to respond with. The content type will be inferred from file extension.
     * If path is a relative path, then it is resolved relative to the current working directory.
     *
     * @param path to file with payload
     * @return instance of Mock.
     */
    public Mock withPathToPayload(Path path) {
        this.routeFullFillOptions.setPath(path);
        return this;
    }

    // Routs

    private Page.RouteOptions timesForPageRoute() {
        Page.RouteOptions options = null;
        if (times > 0) {
            options = new Page.RouteOptions().setTimes(times);
        }
        return options;
    }

    private BrowserContext.RouteOptions timesForContextRoute() {
        BrowserContext.RouteOptions options = null;
        if (times > 0) {
            options = new BrowserContext.RouteOptions().setTimes(times);
        }
        return options;
    }

    private void route(String url, Route.FulfillOptions fulfillOptions) {
        if (isPageNotNull()) {
            this.page.route(url, route -> {
                if (isRestMethodMatch(route)) {
                    route.fulfill(fulfillOptions);
                } else {
                    route.resume();
                }
            }, timesForPageRoute());
        } else if (isContextNotNull()) {
            this.context.route(url, route -> {
                if (isRestMethodMatch(route)) {
                    route.fulfill(fulfillOptions);
                } else {
                    route.resume();
                }
            }, timesForContextRoute());
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    private void route(Predicate<String> urlPredicate, Route.FulfillOptions fulfillOptions) {
        if (isPageNotNull()) {
            this.page.route(urlPredicate, route -> {
                if (isRestMethodMatch(route)) {
                    route.fulfill(fulfillOptions);
                } else {
                    route.resume();
                }
            }, timesForPageRoute());
        } else if (isContextNotNull()) {
            this.context.route(urlPredicate, route -> {
                if (isRestMethodMatch(route)) {
                    route.fulfill(fulfillOptions);
                } else {
                    route.resume();
                }
            }, timesForContextRoute());
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    @Override
    public void perform() {
        Route.FulfillOptions fulfillOptions = routeFullFillOptions.setHeaders(this.headers);
        if (this.url != null) {
            route(this.url, fulfillOptions);
        } else if (this.urlPredicate != null) {
            route(this.urlPredicate, fulfillOptions);
        } else {
            throw new RuntimeException("You have to specify for which url this mock is, use 'forUrl()'.");
        }
    }

}
