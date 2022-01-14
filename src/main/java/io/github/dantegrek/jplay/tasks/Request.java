package io.github.dantegrek.jplay.tasks;

import com.microsoft.playwright.Route;
import io.github.dantegrek.enums.NetworkErrorCode;
import io.github.dantegrek.enums.RestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Helps override of modify out coming requests from browser.
 */
public final class Request extends NetworkRoute<Request> {

    private NetworkErrorCode abortCode = null;
    private final Route.ResumeOptions options = new Route.ResumeOptions();
    private final List<String> removedHeaders = new ArrayList<>();

    private Map processHeaders(Map actualHeaders) {
        removedHeaders.forEach(header -> actualHeaders.remove(header));
        actualHeaders.putAll(this.headers);
        return actualHeaders;
    }

    /**
     * Syntax sugar creates instance of Request
     *
     * @return instance of Request
     */
    public static Request request() {
        return new Request();
    }

    /**
     * Redirects request to different url
     *
     * @param url to point request to.
     * @return instance of Request
     */
    public Request overrideUrl(String url) {
        this.options.setUrl(url);
        return this;
    }

    /**
     * Change REST method.
     *
     * @param method of REST protocol
     * @return instance of Request
     */
    public Request overrideMethod(RestMethod method) {
        this.options.setMethod(method.name());
        return this;
    }

    /**
     * Add additional header to request.
     *
     * @param header name
     * @param value  of header
     * @return instance of Request
     */
    public Request addHeader(String header, String value) {
        this.headers.put(header, value);
        return this;
    }

    /**
     * Removes header from request
     *
     * @param header to remove
     * @return instance of Request
     */
    public Request removeHeader(String header) {
        this.removedHeaders.add(header);
        return this;
    }

    /**
     * Change request body.
     *
     * @param payload in request
     * @return instance of Request
     */
    public Request overridePayload(String payload) {
        this.options.setPostData(payload);
        return this;
    }

    /**
     * Change request body.
     *
     * @param payload in request
     * @return instance of Request
     */
    public Request overridePayload(byte[] payload) {
        this.options.setPostData(payload);
        return this;
    }

    /**
     * Fails request.
     *
     * @return instance of Request
     */
    public Request abort() {
        this.abortCode = NetworkErrorCode.FAILED;
        return this;
    }

    /**
     * Brake request with specific error.
     *
     * @param code of specific error
     * @return instance of Request
     */
    public Request abort(NetworkErrorCode code) {
        this.abortCode = code;
        return this;
    }

    private Consumer<Route> abortConsumer(NetworkErrorCode abort) {
        return route -> {
            if (isRestMethodMatch(route)) {
                route.abort(abort.code);
            } else {
                route.resume();
            }
        };
    }

    private void abortRoute(String url, NetworkErrorCode abort) {
        if (isPageNotNull()) {
            this.page.route(url, abortConsumer(abort));
        } else if (isContextNotNull()) {
            this.context.route(url, abortConsumer(abort));
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    private void abortRoute(Predicate<String> urlPredicate, NetworkErrorCode abort) {
        if (isPageNotNull()) {
            this.page.route(urlPredicate, abortConsumer(abort));
        } else if (isContextNotNull()) {
            this.context.route(urlPredicate, abortConsumer(abort));
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    private Consumer<Route> routeConsumer(Route.ResumeOptions options) {
        return route -> {
            if (isRestMethodMatch(route)) {
                route.resume(options.setHeaders(processHeaders(route.request().headers())));
            } else {
                route.resume();
            }
        };
    }

    private void route(String url, Route.ResumeOptions options) {
        if (isPageNotNull()) {
            this.page.route(url, routeConsumer(options));
        } else if (isContextNotNull()) {
            this.context.route(url, routeConsumer(options));
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    private void route(Predicate<String> urlPredicate, Route.ResumeOptions options) {
        if (isPageNotNull()) {
            this.page.route(urlPredicate, routeConsumer(options));
        } else if (isContextNotNull()) {
            this.context.route(urlPredicate, routeConsumer(options));
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    private void urlCase(String url) {
        if (abortCode != null) {
            abortRoute(url, this.abortCode);
        } else {
            route(url, this.options);
        }
    }

    private void predicateCase(Predicate<String> predicate) {
        if (abortCode != null) {
            abortRoute(predicate, this.abortCode);
        } else {
            route(predicate, this.options);
        }
    }

    @Override
    public void perform() {
        if (url != null) {
            urlCase(url);
        } else if (urlPredicate != null) {
            predicateCase(urlPredicate);
        } else {
            throw new RuntimeException("You have to specify for which url this mock is, use 'forUrl()'.");
        }
    }
}
