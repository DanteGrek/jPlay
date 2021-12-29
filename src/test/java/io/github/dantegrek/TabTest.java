package io.github.dantegrek;

import io.github.dantegrek.actions.TestAction;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.github.dantegrek.screenplay.Actor.actor;
import static org.junit.jupiter.api.Assertions.*;

public class TabTest {

    private final String UNEXPECTED_AMOUNT_OF_PAGES = "Unexpected amount of pages.";
    private final String UNEXPECTED_EXCEPTION_MESSAGE = "Unexpected exception message.";

    @AfterEach
    public void closeBrowser() {
        actor()
                .closeBrowser();
    }

    @Test
    public void openNewTabTest() {
        BrowserContext context = actor()
                .createBrowser()
                .openNewTab()
                .does(TestAction.testAction())
                .getCurrentContext();
        assertEquals(2, context.pages().size(), UNEXPECTED_AMOUNT_OF_PAGES);
    }

    @Test
    public void createContextAndTabTest() {
        BrowserContext context = actor()
                .createBrowser()
                .createContextAndTab()
                .does(TestAction.testAction())
                .getCurrentContext();
        assertEquals(1, context.pages().size(), UNEXPECTED_AMOUNT_OF_PAGES);
    }

    @Test
    public void openNewTabWithoutContextTest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                actor()
                        .createPureBrowser()
                        .openNewTab()
        );

        assertEquals("You can not open new tab without context. " +
                        "Please use 'createContextAndTab()' instead of 'openNewTab()' " +
                        "or 'createBrowser()' instead of 'createPureBrowser()', it will create browser with tab.",
                exception.getMessage(),
                UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @Test
    public void switchTabByIndexTest() {
        Page page1 = actor()
                .createBrowser()
                .does(TestAction.testAction())
                .getPage();
        Page page2 = actor()
                .openNewTab()
                .switchTabByIndex(0)
                .does(TestAction.testAction())
                .getPage();
        assertSame(page1, page2, "switchTabByIndex(int index) switched to unexpected tab.");
    }

    @Test
    public void switchTabWithToBigIndexTest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                actor()
                        .createBrowser()
                        .switchTabByIndex(1)
        );

        assertEquals("Index 1 out of bounds for length 1", exception.getMessage(),
                UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @Test
    public void switchTabByTitleTest() {
        String html =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<title>jPlay</title>" +
                        "</head>" +
                        "</html>";
        Page page1 = actor()
                .createBrowser()
                .does(TestAction.testAction())
                .renderHtml(html)
                .getPage();
        Page page2 = actor()
                .openNewTab()
                .switchTabByTitle("jPlay")
                .does(TestAction.testAction())
                .getPage();
        assertSame(page1, page2, "switchTabByTitle(String title) switched to unexpected tab.");
    }

    @Test
    public void switchTabByDuplicatedTitleTest() {
        String html =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<title>jPlay</title>" +
                        "</head>" +
                        "</html>";
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                actor()
                        .createBrowser()
                        .does(TestAction.testAction())
                        .renderHtml(html)
                        .and()
                        .openNewTab()
                        .does(TestAction.testAction())
                        .renderHtml(html)
                        .then()
                        .switchTabByTitle("jPlay")
        );

        assertEquals("More then one tab in current context has title 'jPlay', " +
                        "in such cases better to use switchTabByIndex(int index).",
                exception.getMessage(), UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @Test
    public void switchTabByAbsentTitleTest() {
        String html =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<title>jPlay</title>" +
                        "</head>" +
                        "</html>";
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                actor()
                        .createBrowser()
                        .does(TestAction.testAction())
                        .renderHtml(html)
                        .and()
                        .openNewTab()
                        .does(TestAction.testAction())
                        .renderHtml(html)
                        .then()
                        .switchTabByTitle("jWright")
        );

        assertEquals("None of tabs in current context has title 'jWright'",
                exception.getMessage(), UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @Test
    public void closeTabTest() {
        BrowserContext context = actor()
                .createBrowser()
                .closeCurrentTab()
                .attemptTo(TestAction.testAction())
                .getCurrentContext();
        assertAll("Context",
                () -> assertNotNull(context, "Context pointer is null after closing last tab."),
                () -> assertEquals(0, context.pages().size(), "Expected 0 pages in context.")
        );
    }

    @Test
    public void tryToWorkWithClosedTabTest() {
        PlaywrightException exception = assertThrows(PlaywrightException.class, () -> {
            actor()
                    .createBrowser()
                    .closeCurrentTab()
                    .attemptTo(TestAction.testAction())
                    .renderHtml("Hello world!");
        });

        assertTrue(exception.getMessage().contains("message='Navigation failed because page was closed!")
                , UNEXPECTED_EXCEPTION_MESSAGE);
    }
}
