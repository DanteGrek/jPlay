package io.github.dantegrek.jplay;

import com.microsoft.playwright.options.Proxy;
import io.github.dantegrek.enums.BrowserName;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface IBrowserConfiguration extends IConfigSwitcher {

    /**
     * Return you to IContextConfiguration
     *
     * @return instance of IContextConfiguration
     */
    IContextConfiguration contextConfig();

    /**
     * Return you to ITimeoutConfig
     *
     * @return instance of ITimeoutConfig
     */
    ITimeoutConfig timeoutConfig();

    /**
     * Sets browser to be used by actor.
     *
     * @param browserName all supported browsers defined in enum {@link io.github.dantegrek.enums.BrowserName}
     * @return instance of Configuration
     */
    IBrowserConfiguration withBrowser(BrowserName browserName);


    /**
     * Additional arguments to pass to the browser instance.
     *
     * @param args The list of Chromium flags can be found "https://peter.sh/experiments/chromium-command-line-switches"
     * @return instance of Configuration
     */
    IBrowserConfiguration withBrowserArgs(List<String> args);

    /**
     * Whether to run browser in headless mode. Defaults to true unless the devtools option is true.
     *
     * @param isHeadless boolean primitive.
     * @return instance of Configuration
     */
    IBrowserConfiguration withHeadless(boolean isHeadless);

    /**
     * Enable Chromium sandboxing. Defaults to false.
     *
     * @param isWithSandbox boolean primitive
     * @return instance if Configuration
     */
    IBrowserConfiguration withChromiumSandbox(boolean isWithSandbox);

    /**
     * Chromium-only Whether to auto-open a Developer Tools panel for each tab.
     * If this option is true, the headless option will be set false.
     *
     * @param isDevTools boolean primitive
     * @return instance of Configuration
     */
    IBrowserConfiguration withChromiumDevTools(boolean isDevTools);

    /**
     * If specified, accepted downloads are downloaded into this directory.
     * Otherwise, temporary directory is created and is deleted when browser is closed.
     * In either case, the downloads are deleted when the browser context they were created in is closed.
     *
     * @param path boolean primitive
     * @return instance of Configuration
     */
    IBrowserConfiguration withDownloadsPath(Path path);

    /**
     * Specify environment variables that will be visible to the browser. Defaults to process.env.
     *
     * @param environmentVariables map of values where key should follow MAP_KEY pattern as name for constant in java.
     * @return instance of Configuration
     */
    IBrowserConfiguration withEnvironmentVariables(Map<String, String> environmentVariables);

    /**
     * Path to a browser executable to run instead of the bundled one.
     * If executablePath is a relative path, then it is resolved relative to the current working directory.
     * Note that Playwright only works with the bundled Chromium, Firefox or WebKit, use at your own risk.
     *
     * @param path you can use Paths.get("path/to/your/browser/executable/file.exe");
     * @return instance of Configuration
     */
    IBrowserConfiguration withExecutablePath(Path path);

    /**
     * Firefox user preferences.
     *
     * @param prefs Learn more about the Firefox user preferences at "https://support.mozilla.org/en-US/kb/about-config-editor-firefox"
     * @return instance of Configuration
     */
    IBrowserConfiguration withFirefoxUserPrefs(Map<String, Object> prefs);

    /**
     * If true, Playwright does not pass its own configurations args and only uses the ones from args.
     *
     * @param isIgnoreAllDefaultArgs Dangerous option; use with care. Defaults to false.
     * @return instance of Configuration
     */
    IBrowserConfiguration withIgnoreAllDefaultArgs(boolean isIgnoreAllDefaultArgs);

    /**
     * If true, Playwright does not pass its own configurations args and only uses the ones from args.
     *
     * @param args Dangerous option; use with care.
     * @return instance of Configuration
     */
    IBrowserConfiguration withIgnoreDefaultArgs(List<String> args);

    /**
     * Network proxy settings.
     *
     * @param proxy {@link com.microsoft.playwright.options.Proxy}
     * @return instance of Configuration
     */
    IBrowserConfiguration withProxy(Proxy proxy);

    /**
     * Slows down Playwright operations by the specified amount of milliseconds.
     * Useful so that you can see what is going on.
     *
     * @param milliseconds to wait between all playwright actions
     * @return instance of Configuration
     */
    IBrowserConfiguration withSlowMo(double milliseconds);

    /**
     * Maximum time in milliseconds to wait for the browser instance to start.
     *
     * @param milliseconds Defaults to 30000 (30 seconds). Pass 0 to disable timeout.
     * @return instance of Configuration
     */
    IBrowserConfiguration withTimeoutWaitOnBrowserToStart(double milliseconds);
}
