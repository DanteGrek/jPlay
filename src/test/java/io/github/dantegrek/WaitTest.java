package io.github.dantegrek;

import com.microsoft.playwright.TimeoutError;
import io.github.dantegrek.screenplay.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WaitTest {

    private final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error message";

    @AfterEach
    public void clearActor() {
        Actor.actor()
                .closeBrowser()
                .cleanConfig();
    }

    @Test
    public void negativeNavigationWaitTimeoutTest() {
        TimeoutError error = assertThrows(TimeoutError.class, () ->
                Actor.actor()
                .config()
                .withDefaultNavigationTimeout(10)
                .andActor()
                .createBrowser()
                .navigateTo("https://www.google.com")
        );
        assertTrue(error.getMessage().contains("message='Timeout 10ms exceeded."), UNEXPECTED_ERROR_MESSAGE);
    }

    @Test
    public void positiveWaitTimeoutTest() {
        String html = """
                <!DOCTYPE html>
                <html>                  
                    <head>
                        <button>jPlay</button>
                    </head>         
                </html>
                """;

        Assertions.assertDoesNotThrow(() ->
                Actor.actor()
                        .config()
                        .withDefaultTimeout(1000)
                        .andActor()
                        .createBrowser()
                        .setContent(html)
                        .click("button")
        );
    }

    @Test
    public void negativeWaitTimeoutTest() {
        String html = """
                <!DOCTYPE html>
                <html>                  
                    <head>
                        <button>jPlay</button>
                    </head>         
                </html>
                """;

        TimeoutError error = assertThrows(TimeoutError.class, () ->
                Actor.actor()
                .config()
                .withDefaultTimeout(2)
                .andActor()
                .createBrowser()
                .setContent(html)
                .click("button")
        );
        assertTrue(error.getMessage().contains("message='Timeout 2ms exceeded."), UNEXPECTED_ERROR_MESSAGE);
    }
}
