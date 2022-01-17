package io.github.dantegrek;

import com.microsoft.playwright.options.ViewportSize;
import io.github.dantegrek.enums.BrowserName;
import io.github.dantegrek.actions.TestAction;
import io.github.dantegrek.enums.Device;
import io.github.dantegrek.jplay.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


import static org.junit.jupiter.api.Assertions.*;

public class StartBrowserTest {

    @AfterEach
    public void clearActor() {
        Actor.actor()
                .closeBrowser()
                .clearConfig();
    }

    public static Object[][] browsers() {
        return new Object[][]{
                {BrowserName.CHROMIUM, "HeadlessChrome"},
                {BrowserName.CHROME, "Chrome"},
                {BrowserName.MSEDGE, "Edg/"},
                {BrowserName.WEBKIT, "Safari"},
                {BrowserName.FIREFOX, "Firefox"}
        };
    }

    public static Object[][] devices() {
        return new Object[][]{
                {BrowserName.CHROMIUM, Device.IPHONE_12},
                {BrowserName.CHROME, Device.IPHONE_12},
                {BrowserName.MSEDGE, Device.IPHONE_12},
                {BrowserName.WEBKIT, Device.IPHONE_12},
                {BrowserName.FIREFOX, Device.IPHONE_12}
        };
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void startBrowserTest(BrowserName browserName, String expectedUserAgent) {
        String userAgent = Actor.actor()
                .browserConfig()
                .withBrowser(browserName)
                .configIsFinished()
                .startBrowser()
                .does(TestAction.testAction())
                .getUserAgent();
        assertTrue(userAgent.contains(expectedUserAgent),
                "User agent of " + browserName.name + " does not contain " + expectedUserAgent);
    }

    @ParameterizedTest
    @MethodSource("devices")
    public void startDeviceTest(BrowserName browserName, io.github.dantegrek.interfaces.Device device) {
        String userAgent = Actor.actor()
                .browserConfig()
                .withBrowser(browserName)
                .contextConfig()
                .withDevice(device)
                .configIsFinished()
                .startBrowser()
                .does(TestAction.testAction())
                .getUserAgent();
        ViewportSize viewportSize = Actor.actor()
                .does(TestAction.testAction())
                .getViewportSize();
        int deviceScaleFactor = Actor.actor()
                .does(TestAction.testAction())
                .getDeviceScaleFactor();
        assertAll(String.format("Device '%s'", device.getDeviceName()),
                () -> assertEquals(device.getUserAgent(), userAgent, "user agent"),
                () -> assertEquals(device.getViewportWidth(), viewportSize.width, "viewport width is different."),
                () -> assertEquals(device.getViewportHeight(), viewportSize.height, "viewport height is different."),
                () -> assertEquals(device.getDeviceScaleFactor(), deviceScaleFactor, "device scale factor is different."));
    }
}
