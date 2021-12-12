package com.playwright.screenplay;

import com.playwright.screenplay.enums.BrowserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.playwright.screenplay.Actor.actor;
import static com.playwright.screenplay.enums.BrowserName.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartBrowserTest {

    public static Object[][] browsers() {
        return new Object[][]{
                {CHROMIUM}, {CHROME}, {MSEDGE}, {WEBKIT}, {FIREFOX}
        };
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void startBrowserTest(BrowserName browserName) {
        actor()
                .config()
                .withBrowser(browserName)
                .create();
        assertTrue(true);
    }

    @AfterEach
    public void clearActor() {
        actor()
                .closeBrowser()
                .removeActor();
    }
}
