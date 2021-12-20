package com.playwright.screenplay.unit.actor;

import com.microsoft.playwright.Browser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static com.playwright.screenplay.Actor.actor;
import static com.playwright.screenplay.unit.actor.actions.TestAction.testAction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContextTest {

    private final String UNEXPECTED_AMOUNT_OF_CONTEXTS = "Unexpected amount of contexts.";
    private final String UNEXPECTED_EXCEPTION_MESSAGE = "Unexpected exception message.";

    @AfterEach
    public void clearActor() {
        actor()
                .closeBrowser();
    }

    @Test
    public void startContextAndTabTest() {
        actor()
                .createBrowser();
        actor()
                .createContextAndTab();
        actor()
                .createContextAndTab();
        Browser browser = actor()
                .does(testAction())
                .getBrowser();
        assertEquals(2, browser.contexts().size(), UNEXPECTED_AMOUNT_OF_CONTEXTS);
    }

    @Test
    public void closeSecondContextAndTabTest() {
        actor()
                .createBrowser();
        actor()
                .createContextAndTab();
        actor()
                .createContextAndTab()
                .closeCurrentContext();
        Browser browser = actor()
                .does(testAction())
                .getBrowser();
        assertEquals(1, browser.contexts().size(), UNEXPECTED_AMOUNT_OF_CONTEXTS);
    }

    @Test
    public void closeContextAndTabTest() {
        actor()
                .create()
                .closeCurrentContext();
        Browser browser = actor()
                .does(testAction())
                .getBrowser();
        assertEquals(0, browser.contexts().size(), UNEXPECTED_AMOUNT_OF_CONTEXTS);
    }

    @Test
    public void switchContextWithNoContext() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            actor()
                    .createBrowser()
                    .switchContextByIndex(1);

        });
        assertEquals("Browser does not have contexts, please start one with 'createContextAndTab()' method" +
                        " or use method 'create()' to create browser with context and tab.",
                exception.getMessage(), UNEXPECTED_EXCEPTION_MESSAGE);
    }
}
