package io.github.dantegrek.jplay;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import io.github.dantegrek.enums.BrowserName;
import io.github.dantegrek.interfaces.Device;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Configuration class is responsible for collecting launch and context options.
 */
public final class Configuration implements IBrowserConfiguration, IContextConfiguration, ITimeoutConfig {

    private Actor actor;
    private BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
    private Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
    private BrowserName browserName = BrowserName.CHROMIUM;
    private double defaultNavigationTimeout = 20000;
    private double defaultWaitTimeout = 20000;
    private double exceptTimeout = 5000;
    private boolean withTrace;
    private Path traceDir = Paths.get("target", "traces");
    private String traceNamePrefix;

    Configuration(Actor actor) {
        this.actor = actor;
    }

    /**
     * Getter
     *
     * @return browser options.
     */
    BrowserType.LaunchOptions getLaunchOptions() {
        return this.launchOptions;
    }

    /**
     * Getter
     *
     * @return context options
     */
    Browser.NewContextOptions getContextOptions() {
        return this.contextOptions;
    }

    /**
     * Getter
     *
     * @return enum value of browser name.
     */
    BrowserName getBrowserName() {
        return this.browserName;
    }

    /**
     * Getter
     *
     * @return double represents wait in milliseconds.
     */
    double getDefaultNavigationTimeout() {
        return this.defaultNavigationTimeout;
    }

    /**
     * Getter
     *
     * @return double represents value in milliseconds.
     */
    double getDefaultTimeout() {
        return this.defaultWaitTimeout;
    }

    /**
     * Getter
     *
     * @return double represents value in milliseconds.
     */
    double getExceptTimeout() {
        return this.exceptTimeout;
    }

    // Custom options

    /**
     * Maximum navigation time in milliseconds
     * This setting will change the default maximum navigation time for the following methods and related shortcuts:
     * <p>
     * page.goBack([options])
     * page.goForward([options])
     * page.navigate(url[, options])
     * page.reload([options])
     * page.setContent(html[, options])
     * page.waitForNavigation([options], callback)
     * page.waitForURL(url[, options])
     * </p>
     *
     * @param timeout wait on all navigation actions.
     * @return instance of Configuration
     */
    public ITimeoutConfig withDefaultNavigationTimeout(double timeout) {
        this.defaultNavigationTimeout = timeout;
        return this;
    }

    /**
     * @param timeout Maximum time in milliseconds
     *                This setting will change the default maximum time for all the methods accepting timeout option.
     * @return instance of Configuration
     */
    public ITimeoutConfig withDefaultTimeout(double timeout) {
        this.defaultWaitTimeout = timeout;
        return this;
    }

    /**
     * @param timeout Maximum time in milliseconds
     *                This setting will change the default maximum time for all asserts in thread.
     * @return instance of Configuration
     */
    public ITimeoutConfig withExpectTimeout(double timeout) {
        this.exceptTimeout = timeout;
        return this;
    }

    /**
     * Sets browser to be used by actor.
     *
     * @param browserName all supported browsers defined in enum {@link io.github.dantegrek.enums.BrowserName}
     * @return instance of Configuration
     */
    public IBrowserConfiguration withBrowser(BrowserName browserName) {
        this.browserName = browserName;
        return this;
    }

    /**
     * @param device Emulate device user agent, view port, device scale factor, sets is touch to true if browser support
     *               such argument, and sets is mobile to true.
     *               Predefined devices: {@link io.github.dantegrek.enums.Device}
     * @return instance of Configuration
     */
    public IContextConfiguration withDevice(Device device) {
        this.contextOptions.setUserAgent(device.getUserAgent());
        this.contextOptions.setViewportSize(device.getViewportWidth(), device.getViewportHeight());
        this.contextOptions.setDeviceScaleFactor(device.getDeviceScaleFactor());
        this.contextOptions.setHasTouch(device.hasTouch());
        // firefox does not support flag isMobile
        if (!this.browserName.equals(BrowserName.FIREFOX)) {
            this.contextOptions.setIsMobile(device.isMobile());
        }
        return this;
    }

    // Launch options

    /**
     * Additional arguments to pass to the browser instance.
     *
     * @param args The list of Chromium flags can be found "https://peter.sh/experiments/chromium-command-line-switches"
     * @return instance of Configuration
     */
    public IBrowserConfiguration withBrowserArgs(List<String> args) {
        this.launchOptions.setArgs(args);
        return this;
    }

    /**
     * Whether to run browser in headless mode. Defaults to true unless the devtools option is true.
     *
     * @param isHeadless boolean primitive.
     * @return instance of Configuration
     */
    public IBrowserConfiguration withHeadless(boolean isHeadless) {
        this.launchOptions.setHeadless(isHeadless);
        return this;
    }

    /**
     * Enable Chromium sandboxing. Defaults to false.
     *
     * @param isWithSandbox boolean primitive
     * @return instance if Configuration
     */
    public IBrowserConfiguration withChromiumSandbox(boolean isWithSandbox) {
        this.launchOptions.setChromiumSandbox(isWithSandbox);
        return this;
    }

    /**
     * Chromium-only Whether to auto-open a Developer Tools panel for each tab.
     * If this option is true, the headless option will be set false.
     *
     * @param isDevTools boolean primitive
     * @return instance of Configuration
     */
    public IBrowserConfiguration withChromiumDevTools(boolean isDevTools) {
        this.launchOptions.setDevtools(isDevTools);
        return this;
    }

    /**
     * If specified, accepted downloads are downloaded into this directory.
     * Otherwise, temporary directory is created and is deleted when browser is closed.
     * In either case, the downloads are deleted when the browser context they were created in is closed.
     *
     * @param path boolean primitive
     * @return instance of Configuration
     */
    public IBrowserConfiguration withDownloadsPath(Path path) {
        this.launchOptions.setDownloadsPath(path);
        return this;
    }

    /**
     * Specify environment variables that will be visible to the browser. Defaults to process.env.
     *
     * @param environmentVariables map of values where key should follow MAP_KEY pattern as name for constant in java.
     * @return instance of Configuration
     */
    public IBrowserConfiguration withEnvironmentVariables(Map<String, String> environmentVariables) {
        this.launchOptions.setEnv(environmentVariables);
        return this;
    }

    /**
     * Path to a browser executable to run instead of the bundled one.
     * If executablePath is a relative path, then it is resolved relative to the current working directory.
     * Note that Playwright only works with the bundled Chromium, Firefox or WebKit, use at your own risk.
     *
     * @param path you can use Paths.get("path/to/your/browser/executable/file.exe");
     * @return instance of Configuration
     */
    @Override
    public IBrowserConfiguration withExecutablePath(Path path) {
        this.launchOptions.setExecutablePath(path);
        return this;
    }

    /**
     * Firefox user preferences.
     *
     * @param prefs Learn more about the Firefox user preferences at "https://support.mozilla.org/en-US/kb/about-config-editor-firefox"
     * @return instance of Configuration
     */
    @Override
    public IBrowserConfiguration withFirefoxUserPrefs(Map<String, Object> prefs) {
        this.launchOptions.setFirefoxUserPrefs(prefs);
        return this;
    }

    /**
     * If true, Playwright does not pass its own configurations args and only uses the ones from args.
     *
     * @param isIgnoreAllDefaultArgs Dangerous option; use with care. Defaults to false.
     * @return instance of Configuration
     */
    @Override
    public IBrowserConfiguration withIgnoreAllDefaultArgs(boolean isIgnoreAllDefaultArgs) {
        this.launchOptions.setIgnoreAllDefaultArgs(isIgnoreAllDefaultArgs);
        return this;
    }

    /**
     * If true, Playwright does not pass its own configurations args and only uses the ones from args.
     *
     * @param args Dangerous option; use with care.
     * @return instance of Configuration
     */
    @Override
    public IBrowserConfiguration withIgnoreDefaultArgs(List<String> args) {
        this.launchOptions.setIgnoreDefaultArgs(args);
        return this;
    }

    /**
     * Network proxy settings.
     *
     * @param proxy {@link com.microsoft.playwright.options.Proxy}
     * @return instance of Configuration
     */
    @Override
    public IBrowserConfiguration withProxy(Proxy proxy) {
        this.launchOptions.setProxy(proxy);
        return this;
    }

    /**
     * Slows down Playwright operations by the specified amount of milliseconds.
     * Useful so that you can see what is going on.
     *
     * @param milliseconds to wait between all playwright actions
     * @return instance of Configuration
     */
    @Override
    public IBrowserConfiguration withSlowMo(double milliseconds) {
        this.launchOptions.setSlowMo(milliseconds);
        return this;
    }

    /**
     * Maximum time in milliseconds to wait for the browser instance to start.
     *
     * @param milliseconds Defaults to 30000 (30 seconds). Pass 0 to disable timeout.
     * @return instance of Configuration
     */
    @Override
    public IBrowserConfiguration withTimeoutWaitOnBrowserToStart(double milliseconds) {
        this.launchOptions.setTimeout(milliseconds);
        return this;
    }

    // Context options

    @Override
    public IContextConfiguration withTrace(boolean withTrace) {
        this.withTrace = withTrace;
        return this;
    }

    /**
     * If specified, traces are saved into this directory.
     *
     * @param path you can use Paths.get("path/to/your/trace.zip");
     * @return instance of Configuration
     */
    @Override
    public IContextConfiguration withTraceDir(Path path) {
        this.traceDir = path;
        return this;
    }

    @Override
    public IContextConfiguration withTraceNamePrefix(String name) {
        this.traceNamePrefix = name;
        return this;
    }

    /**
     * Returns true if trace should be recorded.
     *
     * @return boolean
     */
    boolean getWithTrace() {
        return this.withTrace;
    }

    /**
     * Returns path to trace dir or null if path was not specified.
     *
     * @return Path to trace dir.
     */
    Path getTraceDir() {
        return this.traceDir;
    }

    /**
     * Get name of trace form config.
     *
     * @return trace name as String.
     */
    String getTraceName() {
        return this.traceNamePrefix != null ? this.traceNamePrefix + "-" + getTraceNameSuffix() : getTraceNameSuffix();
    }

    String getTraceNameSuffix() {
        return String.format("%s-trace.zip", browserName.name().toLowerCase());
    }

    /**
     * Whether to automatically download all the attachments.
     *
     * @param acceptDownloads Defaults to false where all the downloads are canceled.
     * @return instance of Configuration
     */
    @Override
    public IContextConfiguration withAcceptDownloads(boolean acceptDownloads) {
        this.contextOptions.setAcceptDownloads(acceptDownloads);
        return this;
    }

    /**
     * When using Page.navigate(url[, options]), Page.route(url, handler[, options]), Page.waitForURL(url[, options]),
     * Page.waitForRequest(urlOrPredicate[, options], callback), or
     * Page.waitForResponse(urlOrPredicate[, options], callback)
     * it takes the base URL in consideration by using the URL() constructor for building the corresponding URL.
     * Examples:#
     * baseURL: http://localhost:3000 and navigating to /bar.html results in http://localhost:3000/bar.html
     *
     * @param baseUrl: http://localhost:3000/foo/ and navigating to ./bar.html results in http://localhost:3000/foo/bar.html
     * @return instance of Configuration
     */
    public IContextConfiguration withBaseURL(String baseUrl) {
        this.contextOptions.setBaseURL(baseUrl);
        return this;
    }

    /**
     * Toggles bypassing page's Content-Security-Policy.
     *
     * @param bypassCSP default false.
     * @return instance of Configuration
     */
    public IContextConfiguration withBypassCSP(boolean bypassCSP) {
        this.contextOptions.setBypassCSP(bypassCSP);
        return this;
    }

    /**
     * ColorScheme { LIGHT, DARK, NO_PREFERENCE } Emulates 'prefers-colors-scheme' media feature,
     * See "https://playwright.dev/java/docs/api/class-page#page-emulate-media" for more details. Defaults to 'light'.
     *
     * @param colorScheme supported values are 'light', 'dark', 'no-preference'.
     * @return instance of Configuration
     */
    public IContextConfiguration withColorScheme(ColorScheme colorScheme) {
        this.contextOptions.setColorScheme(colorScheme);
        return this;
    }

    /**
     * @param scaleFactor Specify device scale factor (can be thought of as dpr). Defaults to 1.
     * @return instance of Configuration
     */
    public IContextConfiguration withDeviceScaleFactor(double scaleFactor) {
        this.contextOptions.setDeviceScaleFactor(scaleFactor);
        return this;
    }

    /**
     * @param httpHeaders An object containing additional HTTP headers to be sent with every request.
     * @return instance of Configuration
     */
    public IContextConfiguration withExtraHTTPHeaders(Map<String, String> httpHeaders) {
        this.contextOptions.setExtraHTTPHeaders(httpHeaders);
        return this;
    }

    /**
     * @param forcedColors ForcedColors { ACTIVE, NONE } Emulates 'forced-colors' media feature,
     *                     supported values are 'active', 'none'.
     *                     See "https://playwright.dev/java/docs/api/class-page#page-emulate-media" for more details. Defaults to 'none'.
     * @return instance of Configuration
     */
    public IContextConfiguration withForcedColors(ForcedColors forcedColors) {
        this.contextOptions.setForcedColors(forcedColors);
        return this;
    }

    /**
     * @param geolocation setLatitude double Latitude between -90 and 90.
     *                    setLongitude double Longitude between -180 and 180.
     *                    setAccuracy double Non-negative accuracy value. Defaults to 0.
     * @return instance of Configuration
     */
    public IContextConfiguration withGeolocation(Geolocation geolocation) {
        this.contextOptions.setGeolocation(geolocation);
        return this;
    }

    /**
     * @param latitude  setLatitude double Latitude between -90 and 90.
     * @param longitude setLongitude double Longitude between -180 and 180.
     * @return instance of Configuration
     */
    public IContextConfiguration withGeolocation(double latitude, double longitude) {
        this.contextOptions.setGeolocation(latitude, longitude);
        return this;
    }

    /**
     * @param hasTouch Specifies if viewport supports touch events. Defaults to false.
     * @return instance of Configuration
     */
    public IContextConfiguration withHasTouch(boolean hasTouch) {
        this.contextOptions.setHasTouch(hasTouch);
        return this;
    }

    /**
     * @param httpCredentials Credentials for "https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication"
     * @return instance of Configuration
     */
    public IContextConfiguration withHttpCredentials(HttpCredentials httpCredentials) {
        this.contextOptions.setHttpCredentials(httpCredentials);
        return this;
    }

    /**
     * @param ignoreHTTPSErrors Whether to ignore HTTPS errors when sending network requests. Defaults to false.
     * @return instance of Configuration
     */
    public IContextConfiguration withIgnoreHTTPSErrors(boolean ignoreHTTPSErrors) {
        this.contextOptions.setIgnoreHTTPSErrors(ignoreHTTPSErrors);
        return this;
    }

    /**
     * @param isMobile Whether the meta viewport tag is taken into account and touch events are enabled.
     *                 Defaults to false. Not supported in Firefox.
     * @return instance of Configuration
     */
    public IContextConfiguration withIsMobile(boolean isMobile) {
        this.contextOptions.setIsMobile(isMobile);
        return this;
    }

    /**
     * @param javaScriptEnabled Whether or not to enable JavaScript in the context. Defaults to true.
     * @return instance of Configuration
     */
    public IContextConfiguration withJavaScriptEnabled(boolean javaScriptEnabled) {
        this.contextOptions.setJavaScriptEnabled(javaScriptEnabled);
        return this;
    }

    /**
     * @param locale Specify user locale, for example en-GB, de-DE, etc. Locale will affect navigator.language value,
     *               Accept-Language request header value as well as number and date formatting rules.
     * @return instance of Configuration
     */
    public IContextConfiguration withLocale(String locale) {
        this.contextOptions.setLocale(locale);
        return this;
    }

    /**
     * @param offline Whether to emulate network being offline. Defaults to false.
     * @return instance of Configuration
     */
    public IContextConfiguration withOffline(boolean offline) {
        this.contextOptions.setOffline(offline);
        return this;
    }

    /**
     * @param permissions A list of permissions to grant to all pages in this context.
     *                    A permission or an array of permissions to grant. Permissions can be one of the following values:
     *                    'geolocation'
     *                    'midi'
     *                    'midi-sysex' (system-exclusive midi)
     *                    'notifications'
     *                    'push'
     *                    'camera'
     *                    'microphone'
     *                    'background-sync'
     *                    'ambient-light-sensor'
     *                    'accelerometer'
     *                    'gyroscope'
     *                    'magnetometer'
     *                    'accessibility-events'
     *                    'clipboard-read'
     *                    'clipboard-write'
     *                    'payment-handler'
     * @return instance of Configuration
     */
    public IContextConfiguration withPermissions(List<String> permissions) {
        this.contextOptions.setPermissions(permissions);
        return this;
    }

    /**
     * @param proxy Network proxy settings to use with this context.
     *              setServer String Proxy to be used for all requests. HTTP and SOCKS proxies are supported,
     *              for example http://myproxy.com:3128 or socks5://myproxy.com:3128. Short form myproxy.com:3128 is considered an HTTP proxy.
     *              setBypass String Optional comma-separated domains to bypass proxy, for example ".com, chromium.org, .domain.com".
     *              setUsername String Optional username to use if HTTP proxy requires authentication.
     *              setPassword String Optional password to use if HTTP proxy requires authentication.
     * @return instance of Configuration
     */
    public IContextConfiguration setContextProxy(Proxy proxy) {
        this.contextOptions.setProxy(proxy);
        return this;
    }

    /**
     * @param server Network proxy settings to use with this context.
     *               setServer String Proxy to be used for all requests. HTTP and SOCKS proxies are supported,
     *               for example http://myproxy.com:3128 or socks5://myproxy.com:3128. Short form myproxy.com:3128 is considered an HTTP proxy.
     * @return instance of Configuration
     */
    public IContextConfiguration setContextProxy(String server) {
        this.contextOptions.setProxy(server);
        return this;
    }

    /**
     * @param path Enables HAR recording for all pages into the specified HAR file on the filesystem.
     *             If not specified, the HAR is not recorded. Make sure to call BrowserContext.close() for the HAR to be saved.
     *             use with Paths.get("path/to/your/log.har");
     * @return instance of Configuration
     */
    public IContextConfiguration withRecordHarPath(Path path) {
        this.contextOptions.setRecordHarPath(path);
        return this;
    }

    /**
     * @param path Enables video recording for all pages into the specified directory.
     *             If not specified videos are not recorded. Make sure to call BrowserContext.close() for videos to be saved.#
     *             use with Paths.get("path/to/your/videoDirectory");
     * @return instance of Configuration
     */
    public IContextConfiguration withRecordVideoDir(Path path) {
        this.contextOptions.setRecordVideoDir(path);
        return this;
    }

    /**
     * @param recordVideoSize RecordVideoSize Dimensions of the recorded videos.
     *                        If not specified the size will be equal to viewport scaled down to fit into 800x800.
     *                        If viewport is not configured explicitly the video size defaults to 800x450.
     *                        Actual picture of each page will be scaled down if necessary to fit the specified size.
     * @return instance of Configuration
     */
    public IContextConfiguration withRecordVideoSize(RecordVideoSize recordVideoSize) {
        this.contextOptions.setRecordVideoSize(recordVideoSize);
        return this;
    }

    /**
     * RecordVideoSize Dimensions of the recorded videos.
     * If not specified the size will be equal to viewport scaled down to fit into 800x800.
     * If viewport is not configured explicitly the video size defaults to 800x450.
     * Actual picture of each page will be scaled down if necessary to fit the specified size.
     *
     * @param width  setWidth int Video frame width.
     * @param height setHeight int Video frame height.
     * @return instance of Configuration
     */
    public IContextConfiguration withRecordVideoSize(int width, int height) {
        this.contextOptions.setRecordVideoSize(width, height);
        return this;
    }

    /**
     * @param reducedMotion ReducedMotion { REDUCE, NO_PREFERENCE }
     *                      Emulates 'prefers-reduced-motion' media feature, supported values are 'reduce', 'no-preference'.
     * @return instance of Configuration
     */
    public IContextConfiguration setReducedMotion(ReducedMotion reducedMotion) {
        this.contextOptions.setReducedMotion(reducedMotion);
        return this;
    }

    /**
     * @param screenSize Emulates consistent window screen size available inside web page via window.screen.
     *                   Is only used when the viewport is set.
     * @return instance of Configuration
     */
    public IContextConfiguration withScreenSize(ScreenSize screenSize) {
        this.contextOptions.setScreenSize(screenSize);
        return this;
    }

    /**
     * Emulates consistent window screen size available inside web page via window.screen.
     * Is only used when the viewport is set.
     *
     * @param width  int value
     * @param height int value
     * @return instance of Configuration
     */
    public IContextConfiguration withScreenSize(int width, int height) {
        this.contextOptions.setScreenSize(width, height);
        return this;
    }

    /**
     * @param state Populates context with given storage state.
     *              This option can be used to initialize context with logged-in information obtained via BrowserContext.storageState([options]).
     * @return instance of Configuration
     */
    public IContextConfiguration withStorageState(String state) {
        this.contextOptions.setStorageState(state);
        return this;
    }

    /**
     * @param path Populates context with given storage state.
     *             This option can be used to initialize context with logged-in information obtained via BrowserContext.storageState([options]).
     *             Path to the file with saved storage state.
     * @return instance of Configuration
     */
    public IContextConfiguration withStorageStatePath(Path path) {
        this.contextOptions.setStorageStatePath(path);
        return this;
    }

    /**
     * @param isStrict It specified, enables strict selectors mode for this context.
     *                 In the strict selectors mode all operations on selectors that imply single target DOM element
     *                 will throw when more than one element matches the selector. See Locator to learn more about the strict mode.
     * @return instance of Configuration
     */
    public IContextConfiguration withStrictSelectors(boolean isStrict) {
        this.contextOptions.setStrictSelectors(isStrict);
        return this;
    }

    /**
     * @param timezoneId Changes the timezone of the context.
     *                   See ICU's metaZones.txt for a list of supported timezone IDs.
     * @return instance of Configuration
     */
    public IContextConfiguration withTimezoneId(String timezoneId) {
        this.contextOptions.setTimezoneId(timezoneId);
        return this;
    }

    /**
     * @param userAgent Specific user agent to use in this context.
     * @return instance of Configuration
     */
    public IContextConfiguration withUserAgent(String userAgent) {
        this.contextOptions.setUserAgent(userAgent);
        return this;
    }

    /**
     * @param viewportSize Emulates consistent viewport for each page. Defaults to an 1280x720 viewport. null disables the default viewport.
     * @return instance of Configuration
     */
    public IContextConfiguration withViewportSize(ViewportSize viewportSize) {
        this.contextOptions.setViewportSize(viewportSize);
        return this;
    }

    /**
     * Emulates consistent viewport for each page. Defaults to an 1280x720 viewport. null disables the default viewport.
     *
     * @param width  setWidth int page width in pixels.
     * @param height setHeight int page height in pixels.
     * @return instance of Configuration
     */
    public IContextConfiguration withViewportSize(int width, int height) {
        this.contextOptions.setViewportSize(width, height);
        return this;
    }

    /**
     * Return you to IBrowserConfiguration
     *
     * @return instance of IBrowserConfiguration
     */
    public IBrowserConfiguration browserConfig() {
        return this;
    }

    /**
     * Return you to IContextConfiguration
     *
     * @return instance of IContextConfiguration
     */
    public IContextConfiguration contextConfig() {
        return this;
    }

    /**
     * Return you to ITimeoutConfig
     *
     * @return instance of ITimeoutConfig
     */
    public ITimeoutConfig timeoutConfig() {
        return this;
    }

    /**
     * Return you to actor invocation chain.
     *
     * @return instance of Actor
     */
    public Actor finished() {
        return this.actor;
    }

    /**
     * Return you to actor invocation chain.
     *
     * @return instance of Actor
     */
    public Actor configIsFinished() {
        return this.actor;
    }

    /**
     * Return you to actor invocation chain.
     *
     * @return instance of Actor
     */
    public Actor and() {
        return this.actor;
    }

    /**
     * Return you to actor invocation chain.
     *
     * @return instance of Actor
     */
    public Actor andActor() {
        return this.actor;
    }
}
