package io.github.dantegrek;

import com.microsoft.playwright.TimeoutError;
import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Actor.actor;
import static io.github.dantegrek.jplay.Jplay.given;
import static io.github.dantegrek.jplay.Jplay.when;
import static org.junit.jupiter.api.Assertions.*;

public class WaitTest {

    private final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error message";

    @AfterEach
    public void clearActor() {
        actor()
                .closeBrowser()
                .clearConfig();
    }

    public static Object[][] browsers() {
        return new Object[][]{
                {BrowserName.CHROMIUM},
                {BrowserName.WEBKIT},
                {BrowserName.FIREFOX}
        };
    }

    @Test
    public void negativeNavigationWaitTimeoutTest() {
        TimeoutError error = assertThrows(TimeoutError.class, () ->
                actor()
                        .timeoutConfig()
                        .withDefaultNavigationTimeout(10)
                        .andActor()
                        .startBrowser()
                        .navigateTo("https://www.google.com")
        );
        assertTrue(error.getMessage().contains("message='Timeout 10ms exceeded."), UNEXPECTED_ERROR_MESSAGE);
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void positiveWaitTimeoutTest(BrowserName browserName) {
        String html =
                "<!DOCTYPE html>" +
                "<html>" +
                    "<head>" +
                        "<button>jPlay</button>" +
                    "</head>" +
                "</html>";

        given()
                .timeoutConfig()
                .withDefaultTimeout(1000)
                .browserConfig()
                .withBrowser(browserName)
                .andActor()
                .startBrowser()
                .setContent(html);
        Assertions.assertDoesNotThrow(() ->
                when()
                        .click("button")
        );
    }

    @Test
    public void negativeWaitTimeoutTest() {
        String html =
                "<!DOCTYPE html>" +
                "<html>" +
                    "<head>" +
                        "<button>jPlay</button>" +
                    "</head>" +
                "</html>";

        TimeoutError error = assertThrows(TimeoutError.class, () ->
                actor()
                        .timeoutConfig()
                        .withDefaultTimeout(2)
                        .andActor()
                        .startBrowser()
                        .setContent(html)
                        .click("button")
        );
        assertTrue(error.getMessage().contains("message='Timeout 2ms exceeded."), UNEXPECTED_ERROR_MESSAGE);
    }
}
