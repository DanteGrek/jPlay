package io.github.dantegrek.enums;

/**
 * This enum contains all supported browsers.
 */
public enum BrowserName {
    /**
     * Google Chrome
     */
    CHROME("chrome"),
    /**
     * Chromium
     */
    CHROMIUM("chromium"),
    /**
     * Microsoft EDGE
     */
    MSEDGE("msedge"),
    /**
     * Webkit used in safari and in plenty of mobile apps.
     */
    WEBKIT("webkit"),
    /**
     * Firefox
     */
    FIREFOX("firefox");
    /**
     * browser name as String used in playwright
     */
    public final String name;

    BrowserName(String name) {
        this.name = name;
    }
}
