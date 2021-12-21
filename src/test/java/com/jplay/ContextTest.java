package com.jplay;

import com.jplay.actions.TestAction;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.PlaywrightException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static com.jplay.screenplay.Actor.actor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContextTest {

    private final String UNEXPECTED_AMOUNT_OF_CONTEXTS = "Unexpected amount of contexts.";
    private final String UNEXPECTED_EXCEPTION_MESSAGE = "Unexpected exception message.";

    @AfterEach
    public void closeBrowser() {
        actor()
                .closeBrowser();
    }

    @Test
    public void startContextAndTabTest() {
        actor()
                .createPureBrowser();
        actor()
                .createContextAndTab();
        actor()
                .createContextAndTab();
        Browser browser = actor()
                .does(TestAction.testAction())
                .getBrowser();
        assertEquals(2, browser.contexts().size(), UNEXPECTED_AMOUNT_OF_CONTEXTS);
    }

    @Test
    public void closeSecondContextAndTabTest() {
        actor()
                .createPureBrowser();
        actor()
                .createContextAndTab();
        actor()
                .createContextAndTab()
                .closeCurrentContext();
        Browser browser = actor()
                .does(TestAction.testAction())
                .getBrowser();
        assertEquals(1, browser.contexts().size(), UNEXPECTED_AMOUNT_OF_CONTEXTS);
    }

    @Test
    public void closeContextAndTabTest() {
        actor()
                .createBrowser()
                .closeCurrentContext();
        Browser browser = actor()
                .does(TestAction.testAction())
                .getBrowser();
        assertEquals(0, browser.contexts().size(), UNEXPECTED_AMOUNT_OF_CONTEXTS);
    }

    @Test
    public void switchContextWithoutAnyContextTest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            actor()
                    .createPureBrowser()
                    .switchContextByIndex(1);

        });
        assertEquals("Browser does not have contexts, please start one using method 'createContextAndTab()'" +
                        " or use 'createBrowser()' to create browser with context and tab.",
                exception.getMessage(), UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @Test
    public void switchContextWithToBigIndexTest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            actor()
                    .createPureBrowser()
                    .createContextAndTab()
                    .switchContextByIndex(2);
        });
        assertEquals("Index 2 out of bounds for length 1",
                exception.getMessage(), UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @Test
    public void tryToOpenTabInClosedContext() {
        PlaywrightException exception = assertThrows(PlaywrightException.class, () ->
            actor()
                    .createBrowser()
                    .closeCurrentContext()
                    .openNewTab()
        );
        assertTrue(exception.getMessage().contains("message='Target page, context or browser has been closed"),
                UNEXPECTED_EXCEPTION_MESSAGE);
    }
}
