package com.playwright.screenplay.unit.actor;

import com.microsoft.playwright.options.ViewportSize;
import com.playwright.screenplay.enums.BrowserName;
import com.playwright.screenplay.interfaces.Device;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


import static com.playwright.screenplay.Actor.actor;
import static com.playwright.screenplay.enums.BrowserName.*;
import static com.playwright.screenplay.enums.Devices.IPHONE_12;
import static com.playwright.screenplay.unit.actor.actions.TestAction.testAction;
import static org.junit.jupiter.api.Assertions.*;

public class StartBrowserTest {

    @AfterEach
    public void clearActor() {
        actor()
                .closeBrowser()
                .cleanConfig();
    }

    public static Object[][] browsers() {
        return new Object[][]{
                {CHROMIUM, "HeadlessChrome"},
                {CHROME, "Chrome"},
                {MSEDGE, "Edg/"},
                {WEBKIT, "Safari"},
                {FIREFOX, "Firefox"}
        };
    }

    public static Object[][] devices() {
        return new Object[][]{
                {CHROMIUM, IPHONE_12},
                {CHROME, IPHONE_12},
                {MSEDGE, IPHONE_12},
                {WEBKIT, IPHONE_12},
                {FIREFOX, IPHONE_12}
        };
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void startBrowserTest(BrowserName browserName, String expectedUserAgent) {
        String userAgent = actor()
                .config()
                .withBrowser(browserName)
                .configIsFinished()
                .create()
                .does(testAction())
                .getUserAgent();
        assertTrue(userAgent.contains(expectedUserAgent),
                "User agent of " + browserName.name + " does not contain " + expectedUserAgent);
    }

    @ParameterizedTest
    @MethodSource("devices")
    public void startDeviceTest(BrowserName browserName, Device device) {
        String userAgent = actor()
                .config()
                .withBrowser(browserName)
                .withDevice(device)
                .configIsFinished()
                .create()
                .does(testAction())
                .getUserAgent();
        ViewportSize viewportSize = actor()
                .does(testAction())
                .getViewportSize();
        int deviceScaleFactor = actor()
                .does(testAction())
                .getDeviceScaleFactor();
        assertAll(String.format("Device '%s'", device.getDeviceName()),
                () -> assertEquals(device.getUserAgent(), userAgent, "user agent"),
                () -> assertEquals(device.getViewportWidth(), viewportSize.width, "viewport width is different."),
                () -> assertEquals(device.getViewportHeight(), viewportSize.height, "viewport height is different."),
                () -> assertEquals(device.getDeviceScaleFactor(), deviceScaleFactor, "device scale factor is different."));
    }
}
