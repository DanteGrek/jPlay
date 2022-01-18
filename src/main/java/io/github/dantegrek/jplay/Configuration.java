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

    @Override
    public ITimeoutConfig withDefaultNavigationTimeout(double timeout) {
        this.defaultNavigationTimeout = timeout;
        return this;
    }

    @Override
    public ITimeoutConfig withDefaultTimeout(double timeout) {
        this.defaultWaitTimeout = timeout;
        return this;
    }

    @Override
    public ITimeoutConfig withExpectTimeout(double timeout) {
        this.exceptTimeout = timeout;
        return this;
    }

    @Override
    public IBrowserConfiguration withBrowser(BrowserName browserName) {
        this.browserName = browserName;
        return this;
    }

    @Override
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

    @Override
    public IBrowserConfiguration withBrowserArgs(List<String> args) {
        this.launchOptions.setArgs(args);
        return this;
    }

    @Override
    public IBrowserConfiguration withHeadless(boolean isHeadless) {
        this.launchOptions.setHeadless(isHeadless);
        return this;
    }

    @Override
    public IBrowserConfiguration withChromiumSandbox(boolean isWithSandbox) {
        this.launchOptions.setChromiumSandbox(isWithSandbox);
        return this;
    }

    @Override
    public IBrowserConfiguration withChromiumDevTools(boolean isDevTools) {
        this.launchOptions.setDevtools(isDevTools);
        return this;
    }

    @Override
    public IBrowserConfiguration withDownloadsPath(Path path) {
        this.launchOptions.setDownloadsPath(path);
        return this;
    }

    @Override
    public IBrowserConfiguration withEnvironmentVariables(Map<String, String> environmentVariables) {
        this.launchOptions.setEnv(environmentVariables);
        return this;
    }

    @Override
    public IBrowserConfiguration withExecutablePath(Path path) {
        this.launchOptions.setExecutablePath(path);
        return this;
    }

    @Override
    public IBrowserConfiguration withFirefoxUserPrefs(Map<String, Object> prefs) {
        this.launchOptions.setFirefoxUserPrefs(prefs);
        return this;
    }

    @Override
    public IBrowserConfiguration withIgnoreAllDefaultArgs(boolean isIgnoreAllDefaultArgs) {
        this.launchOptions.setIgnoreAllDefaultArgs(isIgnoreAllDefaultArgs);
        return this;
    }

    @Override
    public IBrowserConfiguration withIgnoreDefaultArgs(List<String> args) {
        this.launchOptions.setIgnoreDefaultArgs(args);
        return this;
    }

    @Override
    public IBrowserConfiguration withProxy(Proxy proxy) {
        this.launchOptions.setProxy(proxy);
        return this;
    }

    @Override
    public IBrowserConfiguration withSlowMo(double milliseconds) {
        this.launchOptions.setSlowMo(milliseconds);
        return this;
    }

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

    @Override
    public IContextConfiguration withAcceptDownloads(boolean acceptDownloads) {
        this.contextOptions.setAcceptDownloads(acceptDownloads);
        return this;
    }

    @Override
    public IContextConfiguration withBaseURL(String baseUrl) {
        this.contextOptions.setBaseURL(baseUrl);
        return this;
    }

    @Override
    public IContextConfiguration withBypassCSP(boolean bypassCSP) {
        this.contextOptions.setBypassCSP(bypassCSP);
        return this;
    }

    @Override
    public IContextConfiguration withColorScheme(ColorScheme colorScheme) {
        this.contextOptions.setColorScheme(colorScheme);
        return this;
    }

    @Override
    public IContextConfiguration withDeviceScaleFactor(double scaleFactor) {
        this.contextOptions.setDeviceScaleFactor(scaleFactor);
        return this;
    }

    @Override
    public IContextConfiguration withExtraHTTPHeaders(Map<String, String> httpHeaders) {
        this.contextOptions.setExtraHTTPHeaders(httpHeaders);
        return this;
    }

    @Override
    public IContextConfiguration withForcedColors(ForcedColors forcedColors) {
        this.contextOptions.setForcedColors(forcedColors);
        return this;
    }

    @Override
    public IContextConfiguration withGeolocation(Geolocation geolocation) {
        this.contextOptions.setGeolocation(geolocation);
        return this;
    }

    @Override
    public IContextConfiguration withGeolocation(double latitude, double longitude) {
        this.contextOptions.setGeolocation(latitude, longitude);
        return this;
    }

    @Override
    public IContextConfiguration withHasTouch(boolean hasTouch) {
        this.contextOptions.setHasTouch(hasTouch);
        return this;
    }

    @Override
    public IContextConfiguration withHttpCredentials(HttpCredentials httpCredentials) {
        this.contextOptions.setHttpCredentials(httpCredentials);
        return this;
    }

    @Override
    public IContextConfiguration withIgnoreHTTPSErrors(boolean ignoreHTTPSErrors) {
        this.contextOptions.setIgnoreHTTPSErrors(ignoreHTTPSErrors);
        return this;
    }

    @Override
    public IContextConfiguration withIsMobile(boolean isMobile) {
        this.contextOptions.setIsMobile(isMobile);
        return this;
    }

    @Override
    public IContextConfiguration withJavaScriptEnabled(boolean javaScriptEnabled) {
        this.contextOptions.setJavaScriptEnabled(javaScriptEnabled);
        return this;
    }

    @Override
    public IContextConfiguration withLocale(String locale) {
        this.contextOptions.setLocale(locale);
        return this;
    }

    @Override
    public IContextConfiguration withOffline(boolean offline) {
        this.contextOptions.setOffline(offline);
        return this;
    }

    @Override
    public IContextConfiguration withPermissions(List<String> permissions) {
        this.contextOptions.setPermissions(permissions);
        return this;
    }

    @Override
    public IContextConfiguration withContextProxy(Proxy proxy) {
        this.contextOptions.setProxy(proxy);
        return this;
    }

    @Override
    public IContextConfiguration withContextProxy(String server) {
        this.contextOptions.setProxy(server);
        return this;
    }

    @Override
    public IContextConfiguration withRecordHarPath(Path path) {
        this.contextOptions.setRecordHarPath(path);
        return this;
    }

    @Override
    public IContextConfiguration withRecordVideoDir(Path path) {
        this.contextOptions.setRecordVideoDir(path);
        return this;
    }

    @Override
    public IContextConfiguration withRecordVideoSize(RecordVideoSize recordVideoSize) {
        this.contextOptions.setRecordVideoSize(recordVideoSize);
        return this;
    }

    @Override
    public IContextConfiguration withRecordVideoSize(int width, int height) {
        this.contextOptions.setRecordVideoSize(width, height);
        return this;
    }

    @Override
    public IContextConfiguration setReducedMotion(ReducedMotion reducedMotion) {
        this.contextOptions.setReducedMotion(reducedMotion);
        return this;
    }

    @Override
    public IContextConfiguration withScreenSize(ScreenSize screenSize) {
        this.contextOptions.setScreenSize(screenSize);
        return this;
    }

    @Override
    public IContextConfiguration withScreenSize(int width, int height) {
        this.contextOptions.setScreenSize(width, height);
        return this;
    }

    @Override
    public IContextConfiguration withStorageState(String state) {
        this.contextOptions.setStorageState(state);
        return this;
    }

    @Override
    public IContextConfiguration withStorageStatePath(Path path) {
        this.contextOptions.setStorageStatePath(path);
        return this;
    }

    @Override
    public IContextConfiguration withStrictSelectors(boolean isStrict) {
        this.contextOptions.setStrictSelectors(isStrict);
        return this;
    }

    @Override
    public IContextConfiguration withTimezoneId(String timezoneId) {
        this.contextOptions.setTimezoneId(timezoneId);
        return this;
    }

    @Override
    public IContextConfiguration withUserAgent(String userAgent) {
        this.contextOptions.setUserAgent(userAgent);
        return this;
    }

    @Override
    public IContextConfiguration withViewportSize(ViewportSize viewportSize) {
        this.contextOptions.setViewportSize(viewportSize);
        return this;
    }

    @Override
    public IContextConfiguration withViewportSize(int width, int height) {
        this.contextOptions.setViewportSize(width, height);
        return this;
    }

    @Override
    public IBrowserConfiguration browserConfig() {
        return this;
    }

    @Override
    public IContextConfiguration contextConfig() {
        return this;
    }

    @Override
    public ITimeoutConfig timeoutConfig() {
        return this;
    }

    @Override
    public Actor finished() {
        return this.actor;
    }

    @Override
    public Actor configIsFinished() {
        return this.actor;
    }

    @Override
    public Actor and() {
        return this.actor;
    }

    @Override
    public Actor andActor() {
        return this.actor;
    }
}
