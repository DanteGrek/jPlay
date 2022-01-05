package io.github.dantegrek.enums;

/**
 * All browser jplay can start.
 */
public enum BrowserName {
    /**
     * Google Chrome name
     */
    CHROME("chrome"),
    /**
     * Chromium name
     */
    CHROMIUM("chromium"),
    /**
     * Microsoft EDGE name
     */
    MSEDGE("msedge"),
    /**
     * Webkit name
     */
    WEBKIT("webkit"),
    /**
     * Firefox name
     */
    FIREFOX("firefox");

    /**
     * Key string with browser name for playwright
     */
    public final String name;

    BrowserName(String name) {
        this.name = name;
    }
}
