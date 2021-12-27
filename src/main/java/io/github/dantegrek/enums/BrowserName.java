package io.github.dantegrek.enums;

public enum BrowserName {
    CHROME("chrome"),
    CHROMIUM("chromium"),
    MSEDGE("msedge"),
    WEBKIT("webkit"),
    FIREFOX("firefox");

    public final String name;

    BrowserName(String name) {
        this.name = name;
    }
}
