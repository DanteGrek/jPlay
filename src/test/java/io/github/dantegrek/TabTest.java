package io.github.dantegrek;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Actor.actor;
import static org.junit.jupiter.api.Assertions.*;

public class TabTest {

    private final String UNEXPECTED_AMOUNT_OF_PAGES = "Unexpected amount of pages.";
    private final String UNEXPECTED_EXCEPTION_MESSAGE = "Unexpected exception message.";

    @AfterEach
    public void closeBrowser() {
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

    @ParameterizedTest
    @MethodSource("browsers")
    public void openNewTabTest(BrowserName browserName) {
        BrowserContext context = actor()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .openNewTab()
                .currentPage()
                .context();
        assertEquals(2, context.pages().size(), UNEXPECTED_AMOUNT_OF_PAGES);
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void createContextAndTabTest(BrowserName browserName) {
        BrowserContext context = actor()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .createContextAndTab()
                .currentPage()
                .context();
        assertEquals(1, context.pages().size(), UNEXPECTED_AMOUNT_OF_PAGES);
    }

    @Test
    public void openNewTabWithoutContextTest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                actor()
                        .startPureBrowser()
                        .openNewTab()
        );

        assertEquals("You can not open new tab without context. " +
                        "Please use 'createContextAndTab()' instead of 'openNewTab()' " +
                        "or 'startBrowser()' instead of 'startPureBrowser()', it will create browser with tab.",
                exception.getMessage(),
                UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void switchTabByIndexTest(BrowserName browserName) {
        Page page1 = actor()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .currentPage();
        Page page2 = actor()
                .openNewTab()
                .switchTabByIndex(0)
                .currentPage();
        assertSame(page1, page2, "switchTabByIndex(int index) switched to unexpected tab.");
    }

    @Test
    public void switchTabWithToBigIndexTest() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                actor()
                        .startBrowser()
                        .switchTabByIndex(1)
        );

        assertEquals("Index 1 out of bounds for length 1", exception.getMessage(),
                UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void switchTabByTitleTest(BrowserName browserName) {
        String html =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<title>jPlay</title>" +
                        "</head>" +
                        "</html>";
        Page page1 = actor()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .setContent(html)
                .currentPage();
        Page page2 = actor()
                .openNewTab()
                .switchTabByTitle("jPlay")
                .currentPage();
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
                        .startBrowser()
                        .setContent(html)
                        .openNewTab()
                        .setContent(html)
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
                        .startBrowser()
                        .setContent(html)
                        .openNewTab()
                        .setContent(html)
                        .switchTabByTitle("jWright")
        );

        assertEquals("None of tabs in current context has title 'jWright'",
                exception.getMessage(), UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @Test
    public void closeTabTest() {
        BrowserContext context = actor()
                .startBrowser()
                .closeCurrentTab()
                .currentPage()
                .context();
        assertAll("Context",
                () -> assertNotNull(context, "Context pointer is null after closing last tab."),
                () -> assertEquals(0, context.pages().size(), "Expected 0 pages in context.")
        );
    }

    @Test
    public void tryToWorkWithClosedTabTest() {
        PlaywrightException exception = assertThrows(PlaywrightException.class, () -> {
            actor()
                    .startBrowser()
                    .closeCurrentTab()
                    .setContent("Hello world!");
        });

        assertTrue(exception.getMessage().contains("message='Navigation failed because page was closed!")
                , UNEXPECTED_EXCEPTION_MESSAGE);
    }
}
