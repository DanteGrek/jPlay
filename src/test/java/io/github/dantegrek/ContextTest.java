package io.github.dantegrek;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.PlaywrightException;
import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Actor.actor;
import static io.github.dantegrek.jplay.Jplay.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContextTest {

    private final String UNEXPECTED_AMOUNT_OF_CONTEXTS = "Unexpected amount of contexts.";
    private final String UNEXPECTED_EXCEPTION_MESSAGE = "Unexpected exception message.";

    @AfterEach
    public void closeBrowser() {
        actor()
                .closeBrowser()
                .cleanConfig();
    }

    public static Object[][] browsers() {
        return new Object[][]{
                {BrowserName.CHROMIUM},
                {BrowserName.WEBKIT},
                {BrowserName.FIREFOX}
        };
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void startContextAndTabTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startPureBrowser();
        when()
                .createContextAndTab();
        and()
                .createContextAndTab();
        Browser browser = actor()
                .currentPage()
                .context()
                .browser();

        assertEquals(2, browser.contexts().size(), UNEXPECTED_AMOUNT_OF_CONTEXTS);
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void closeSecondContextAndTabTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startPureBrowser();
        when()
                .createContextAndTab();
        and()
                .createContextAndTab()
                .closeCurrentContext();
        Browser browser = then()
                .currentPage()
                .context()
                .browser();

        assertEquals(1, browser.contexts().size(), UNEXPECTED_AMOUNT_OF_CONTEXTS);
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void closeContextAndTabTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .closeCurrentContext();
        Browser browser = actor()
                .currentPage()
                .context()
                .browser();
        assertEquals(0, browser.contexts().size(), UNEXPECTED_AMOUNT_OF_CONTEXTS);
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void switchContextWithoutAnyContextTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startPureBrowser();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            actor()

                    .switchContextByIndex(1);

        });
        assertEquals("Browser does not have contexts, please start one using method 'createContextAndTab()'" +
                        " or use 'startBrowser()' to create browser with context and tab.",
                exception.getMessage(), UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void switchContextWithToBigIndexTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startPureBrowser()
                .createContextAndTab();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            actor()
                    .switchContextByIndex(2);
        });
        assertEquals("Index 2 out of bounds for length 1",
                exception.getMessage(), UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void tryToOpenTabInClosedContext(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .closeCurrentContext();
        PlaywrightException exception = assertThrows(PlaywrightException.class, () ->
            actor()
                    .openNewTab()
        );
        assertTrue(exception.getMessage().contains("message='Target page, context or browser has been closed"),
                UNEXPECTED_EXCEPTION_MESSAGE);
    }
}
