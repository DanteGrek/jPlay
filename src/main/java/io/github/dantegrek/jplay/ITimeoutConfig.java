package io.github.dantegrek.jplay;

public interface ITimeoutConfig extends IConfigSwitcher {
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
    ITimeoutConfig withDefaultNavigationTimeout(double timeout);

    /**
     * @param timeout Maximum time in milliseconds
     * This setting will change the default maximum time for all the methods accepting timeout option.
     * @return instance of Configuration
     */
    ITimeoutConfig withDefaultTimeout(double timeout);

    /**
     * @param timeout Maximum time in milliseconds
     * This setting will change the default maximum time for all asserts in thread.
     * @return instance of Configuration
     */
    ITimeoutConfig withExpectTimeout(double timeout);
}
