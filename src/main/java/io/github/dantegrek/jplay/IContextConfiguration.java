package io.github.dantegrek.jplay;

import com.microsoft.playwright.options.*;
import io.github.dantegrek.interfaces.Device;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface IContextConfiguration extends IConfigSwitcher {

    /**
     * Return you to IBrowserConfiguration
     *
     * @return instance of IBrowserConfiguration
     */
    IBrowserConfiguration browserConfig();

    /**
     * Return you to ITimeoutConfig
     *
     * @return instance of ITimeoutConfig
     */
    ITimeoutConfig timeoutConfig();

    /**
     * @param device Emulate device user agent, view port, device scale factor, sets is touch to true if browser support
     *               such argument, and sets is mobile to true.
     *               Predefined devices: {@link io.github.dantegrek.enums.Device}
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withDevice(Device device);

    /**
     * Whether to automatically download all the attachments.
     *
     * @param acceptDownloads Defaults to false where all the downloads are canceled.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withAcceptDownloads(boolean acceptDownloads);

    /**
     * When using Page.navigate(url[, options]), Page.route(url, handler[, options]), Page.waitForURL(url[, options]),
     * Page.waitForRequest(urlOrPredicate[, options], callback), or
     * Page.waitForResponse(urlOrPredicate[, options], callback)
     * it takes the base URL in consideration by using the URL() constructor for building the corresponding URL.
     * Examples:#
     * baseURL: http://localhost:3000 and navigating to /bar.html results in http://localhost:3000/bar.html
     *
     * @param baseUrl: http://localhost:3000/foo/ and navigating to ./bar.html results in http://localhost:3000/foo/bar.html
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withBaseURL(String baseUrl);

    /**
     * Toggles bypassing page's Content-Security-Policy.
     *
     * @param bypassCSP default false.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withBypassCSP(boolean bypassCSP);

    /**
     * Set color schema.
     *
     * ColorScheme { LIGHT, DARK, NO_PREFERENCE } Emulates 'prefers-colors-scheme' media feature,
     * See "https://playwright.dev/java/docs/api/class-page#page-emulate-media" for more details. Defaults to 'light'.
     *
     * @param colorScheme supported values are 'light', 'dark', 'no-preference'.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withColorScheme(ColorScheme colorScheme);

    /**
     * Set device scale factor.
     *
     * @param scaleFactor Specify device scale factor (can be thought of as dpr). Defaults to 1.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withDeviceScaleFactor(double scaleFactor);

    /**
     * Add extra headers.
     *
     * @param httpHeaders An object containing additional HTTP headers to be sent with every request.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withExtraHTTPHeaders(Map<String, String> httpHeaders);

    /**
     * Set colors
     *
     * @param forcedColors ForcedColors { ACTIVE, NONE } Emulates 'forced-colors' media feature,
     *                     supported values are 'active', 'none'.
     *                     See "https://playwright.dev/java/docs/api/class-page#page-emulate-media" for more details. Defaults to 'none'.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withForcedColors(ForcedColors forcedColors);

    /**
     * Set geolocation.
     *
     * @param geolocation setLatitude double Latitude between -90 and 90.
     *                    setLongitude double Longitude between -180 and 180.
     *                    setAccuracy double Non-negative accuracy value. Defaults to 0.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withGeolocation(Geolocation geolocation);

    /**
     * Set geolocation.
     *
     * @param latitude  setLatitude double Latitude between -90 and 90.
     * @param longitude setLongitude double Longitude between -180 and 180.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withGeolocation(double latitude, double longitude);

    /**
     * Set flag hasTouch.
     *
     * @param hasTouch Specifies if viewport supports touch events. Defaults to false.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withHasTouch(boolean hasTouch);

    /**
     * Set default http credentials.
     *
     * @param httpCredentials Credentials for "https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication"
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withHttpCredentials(HttpCredentials httpCredentials);

    /**
     * Ignore https errors.
     *
     * @param ignoreHTTPSErrors Whether to ignore HTTPS errors when sending network requests. Defaults to false.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withIgnoreHTTPSErrors(boolean ignoreHTTPSErrors);

    /**
     * Set flag is mobile.
     *
     * @param isMobile Whether the meta viewport tag is taken into account and touch events are enabled.
     *                 Defaults to false. Not supported in Firefox.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withIsMobile(boolean isMobile);

    /**
     * Allow or disable javascript in browser.
     *
     * @param javaScriptEnabled Whether or not to enable JavaScript in the context. Defaults to true.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withJavaScriptEnabled(boolean javaScriptEnabled);

    /**
     * Set localization.
     *
     * @param locale Specify user locale, for example en-GB, de-DE, etc. Locale will affect navigator.language value,
     *               Accept-Language request header value as well as number and date formatting rules.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withLocale(String locale);

    /**
     * Turn off network.
     *
     * @param offline Whether to emulate network being offline. Defaults to false.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withOffline(boolean offline);

    /**
     * Permissions for browser.
     *
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
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withPermissions(List<String> permissions);

    /**
     * Proxy config for browser.
     *
     * @param proxy Network proxy settings to use with this context.
     *              setServer String Proxy to be used for all requests. HTTP and SOCKS proxies are supported,
     *              for example http://myproxy.com:3128 or socks5://myproxy.com:3128. Short form myproxy.com:3128 is considered an HTTP proxy.
     *              setBypass String Optional comma-separated domains to bypass proxy, for example ".com, chromium.org, .domain.com".
     *              setUsername String Optional username to use if HTTP proxy requires authentication.
     *              setPassword String Optional password to use if HTTP proxy requires authentication.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration setContextProxy(Proxy proxy);

    /**
     * Proxy url for browser.
     *
     * @param server Network proxy settings to use with this context.
     *               setServer String Proxy to be used for all requests. HTTP and SOCKS proxies are supported,
     *               for example http://myproxy.com:3128 or socks5://myproxy.com:3128. Short form myproxy.com:3128 is considered an HTTP proxy.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration setContextProxy(String server);

    /**
     * Records har file when specified.
     *
     * @param path Enables HAR recording for all pages into the specified HAR file on the filesystem.
     *             If not specified, the HAR is not recorded. Make sure to call BrowserContext.close() for the HAR to be saved.
     *             use with Paths.get("path/to/your/log.har");
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withRecordHarPath(Path path);

    /**
     * Records video when specified.
     *
     * @param path Enables video recording for all pages into the specified directory.
     *             If not specified videos are not recorded. Make sure to call BrowserContext.close() for videos to be saved.#
     *             use with Paths.get("path/to/your/videoDirectory");
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withRecordVideoDir(Path path);

    /**
     * Set resolution for video recording.
     *
     * @param recordVideoSize RecordVideoSize Dimensions of the recorded videos.
     *                        If not specified the size will be equal to viewport scaled down to fit into 800x800.
     *                        If viewport is not configured explicitly the video size defaults to 800x450.
     *                        Actual picture of each page will be scaled down if necessary to fit the specified size.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withRecordVideoSize(RecordVideoSize recordVideoSize);

    /**
     * RecordVideoSize Dimensions of the recorded videos.
     * If not specified the size will be equal to viewport scaled down to fit into 800x800.
     * If viewport is not configured explicitly the video size defaults to 800x450.
     * Actual picture of each page will be scaled down if necessary to fit the specified size.
     *
     * @param width  setWidth int Video frame width.
     * @param height setHeight int Video frame height.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withRecordVideoSize(int width, int height);

    /**
     * @param reducedMotion ReducedMotion { REDUCE, NO_PREFERENCE }
     *                      Emulates 'prefers-reduced-motion' media feature, supported values are 'reduce', 'no-preference'.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration setReducedMotion(ReducedMotion reducedMotion);

    /**
     * @param screenSize Emulates consistent window screen size available inside web page via window.screen.
     *                   Is only used when the viewport is set.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withScreenSize(ScreenSize screenSize);

    /**
     * Emulates consistent window screen size available inside web page via window.screen.
     * Is only used when the viewport is set.
     *
     * @param width  int value
     * @param height int value
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withScreenSize(int width, int height);

    /**
     * @param state Populates context with given storage state.
     *              This option can be used to initialize context with logged-in information obtained via BrowserContext.storageState([options]).
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withStorageState(String state);

    /**
     * @param path Populates context with given storage state.
     *             This option can be used to initialize context with logged-in information obtained via BrowserContext.storageState([options]).
     *             Path to the file with saved storage state.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withStorageStatePath(Path path);

    /**
     * @param isStrict It specified, enables strict selectors mode for this context.
     *                 In the strict selectors mode all operations on selectors that imply single target DOM element
     *                 will throw when more than one element matches the selector. See Locator to learn more about the strict mode.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withStrictSelectors(boolean isStrict);

    /**
     * @param timezoneId Changes the timezone of the context.
     *                   See ICU's metaZones.txt for a list of supported timezone IDs.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withTimezoneId(String timezoneId);

    /**
     * @param userAgent Specific user agent to use in this context.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withUserAgent(String userAgent);

    /**
     * @param viewportSize Emulates consistent viewport for each page. Defaults to an 1280x720 viewport. null disables the default viewport.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withViewportSize(ViewportSize viewportSize);

    /**
     * Emulates consistent viewport for each page. Defaults to an 1280x720 viewport. null disables the default viewport.
     *
     * @param width  setWidth int page width in pixels.
     * @param height setHeight int page height in pixels.
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withViewportSize(int width, int height);

    /**
     * If specified, traces will be recorded.
     *
     * @param withTrace boolean
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withTrace(boolean withTrace);

    /**
     * If specified, traces are saved into this directory.
     *
     * @param path you can use Paths.get("path/to/your/trace.zip");
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withTraceDir(Path path);

    /**
     * If specified, trace file name prefix will be set.
     *
     * @param name of trace file
     * @return instance of IContextConfiguration
     */
    IContextConfiguration withTraceNamePrefix(String name);
}
