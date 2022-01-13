package io.github.dantegrek.jplay.tasks;

import com.microsoft.playwright.Route;
import io.github.dantegrek.enums.NetworkErrorCode;
import io.github.dantegrek.enums.RestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class Request extends NetworkRoute<Request> {

    private NetworkErrorCode abortCode = null;
    private final Route.ResumeOptions options = new Route.ResumeOptions();
    private final List<String> removedHeaders = new ArrayList<>();

    public Map processHeaders(Map actualHeaders) {
        removedHeaders.forEach(header -> actualHeaders.remove(header));
        actualHeaders.putAll(this.headers);
        return actualHeaders;
    }

    public static Request request() {
        return new Request();
    }

    public Request overrideUrl(String url) {
        this.options.setUrl(url);
        return this;
    }

    public Request overrideMethod(RestMethod method) {
        this.options.setMethod(method.name());
        return this;
    }

    public Request addHeader(String header, String value) {
        this.headers.put(header, value);
        return this;
    }

    public Request removeHeader(String header) {
        this.removedHeaders.add(header);
        return this;
    }

    public Request overridePayload(String payload) {
        this.options.setPostData(payload);
        return this;
    }

    public Request overridePayload(byte[] payload) {
        this.options.setPostData(payload);
        return this;
    }

    public Request abort() {
        this.abortCode = NetworkErrorCode.FAILED;
        return this;
    }

    public Request abort(NetworkErrorCode code) {
        this.abortCode = code;
        return this;
    }

    private void abortRoute(String url, NetworkErrorCode abort) {
        if (isPageNotNull()) {
            this.page.route(url, route -> {
                if (isRestMethodMatch(route)) {
                    route.abort(abort.code);
                } else {
                    route.resume();
                }
            });
        } else if (isContextNotNull()) {
            this.context.route(url, route -> {
                if (isRestMethodMatch(route)) {
                    route.abort(abort.code);
                } else {
                    route.resume();
                }
            });
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    private void abortRoute(Predicate<String> urlPredicate, NetworkErrorCode abort) {
        if (isPageNotNull()) {
            this.page.route(urlPredicate, route -> {
                if (isRestMethodMatch(route)) {
                    route.abort(abort.code);
                } else {
                    route.resume();
                }
            });
        } else if (isContextNotNull()) {
            this.context.route(urlPredicate, route -> {
                if (isRestMethodMatch(route)) {
                    route.abort(abort.code);
                } else {
                    route.resume();
                }
            });
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    private void route(String url, Route.ResumeOptions options) {
        if (isPageNotNull()) {
            this.page.route(url, route -> {
                if (isRestMethodMatch(route)) {

                    route.resume(options.setHeaders(processHeaders(route.request().headers())));
                } else {
                    route.resume();
                }
            });
        } else if (isContextNotNull()) {
            this.context.route(url, route -> {
                if (isRestMethodMatch(route)) {
                    route.resume(options.setHeaders(processHeaders(route.request().headers())));
                } else {
                    route.resume();
                }
            });
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    private void route(Predicate<String> urlPredicate, Route.ResumeOptions options) {
        if (isPageNotNull()) {
            this.page.route(urlPredicate, route -> {
                if (isRestMethodMatch(route)) {
                    route.resume(options.setHeaders(processHeaders(route.request().headers())));
                } else {
                    route.resume();
                }
            });
        } else if (isContextNotNull()) {
            this.context.route(urlPredicate, route -> {
                if (isRestMethodMatch(route)) {
                    route.resume(options.setHeaders(processHeaders(route.request().headers())));
                } else {
                    route.resume();
                }
            });
        } else {
            throwRuntimeExceptionFromRoute();
        }
    }

    @Override
    public void perform() {
        if (url != null) {
            if (abortCode != null) {
                abortRoute(url, this.abortCode);
                return;
            }
            route(url, this.options);
        } else if (urlPredicate != null) {
            if (abortCode != null) {
                abortRoute(urlPredicate, this.abortCode);
                return;
            }
            route(urlPredicate, this.options);
        } else {
            throw new RuntimeException("You have to specify for which url this mock is, use 'forUrl()'.");
        }
    }
}
