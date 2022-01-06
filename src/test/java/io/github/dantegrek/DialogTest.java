package io.github.dantegrek;

import com.microsoft.playwright.PlaywrightException;
import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Jplay.*;
import static org.junit.jupiter.api.Assertions.*;

public class DialogTest {

    private final String pathToIndexHtml = "file:" + Paths.get("src", "test", "resources", "playground/index.html").toFile().getAbsolutePath();

    @AfterEach
    public void afterEach() {
        then()
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
    public void expectThatMessageEqualAndDismissJsDialogTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startBrowser()
                .navigateTo(pathToIndexHtml);
        when()
                .dialog()
                .expectThatMessageEqualAndDismiss("Are you here?")
                .evaluate("window.result = confirm('Are you here?');");
        then()
                .dialog()
                .expectThatMessageEqualAndDismiss("false")
                .evaluate("alert(window.result);");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void expectThatMessageEqualAndDismissJsDialogThrowsAssertTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startBrowser()
                .navigateTo(pathToIndexHtml);

        AssertionFailedError assertionFailedError = assertThrows(AssertionFailedError.class, () ->
                when()
                        .dialog()
                        .expectThatMessageEqualAndDismiss("Are we there?")
                        .evaluate("window.result = confirm('Are you here?');")
        );

        assertEquals("Unexpected dialog message:\nActual: Are you here?\nExpected: Are we there?",
                assertionFailedError.getMessage());
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void expectThatMessageEqualAndAcceptJsDialogTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startBrowser()
                .navigateTo(pathToIndexHtml);
        when()
                .dialog()
                .expectThatMessageEqualAndAccept("Are you here?")
                .evaluate("window.result = confirm('Are you here?');");
        then()
                .dialog()
                .expectThatMessageEqualAndDismiss("true")
                .evaluate("alert(window.result);");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void acceptOnceJsConfirmDialogTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startBrowser()
                .navigateTo(pathToIndexHtml);
        when()
                .dialog()
                .acceptConfirmOnce()
                .evaluate("window.result = confirm('Are you here?');");
        then()
                .dialog()
                .expectThatMessageEqualAndDismiss("true");
        user()
                .evaluate("alert(window.result);")
                .evaluate("window.result = confirm('Are you here?');");
        and()
                .dialog()
                .expectThatMessageEqualAndDismiss("false")
                .evaluate("alert(window.result);");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void acceptAllJsConfirmDialogsTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startBrowser()
                .navigateTo(pathToIndexHtml);
        when()
                .dialog()
                .acceptAllConfirms()
                .evaluate("window.result = confirm('Are you here?');");
        then()
                .dialog()
                .expectThatMessageEqualAndDismiss("true");
        user()
                .evaluate("alert(window.result);")
                .evaluate("window.result = confirm('Are you here?');");
        and()
                .dialog()
                .expectThatMessageEqualAndDismiss("true")
                .evaluate("alert(window.result);");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void acceptOnceJsPromptDialogTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startBrowser()
                .navigateTo(pathToIndexHtml);
        when()
                .dialog()
                .acceptPromptOnce("Yes")
                .evaluate("window.result = prompt('Are you here?');");
        then()
                .dialog()
                .expectThatMessageEqualAndDismiss("Yes");
        user()
                .evaluate("alert(window.result);")
                .evaluate("window.result = prompt('Are you here?');");
        and()
                .dialog()
                .expectThatMessageEqualAndDismiss("null")
                .evaluate("alert(window.result);");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void acceptAllJsPromptDialogsTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startBrowser()
                .navigateTo(pathToIndexHtml);
        when()
                .dialog()
                .acceptAllPrompts("Yes")
                .evaluate("window.result = prompt('Are you here?');");
        then()
                .dialog()
                .expectThatMessageEqualAndDismiss("Yes");
        user()
                .evaluate("alert(window.result);")
                .evaluate("window.result = prompt('Are you here?');");
        and()
                .dialog()
                .expectThatMessageEqualAndDismiss("Yes")
                .evaluate("alert(window.result);");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void twoDialogHandlersTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName);
        and()
                .startBrowser()
                .navigateTo(pathToIndexHtml);

        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
                when()
                        .dialog()
                        .acceptAllPrompts("Yes")
                        .dialog()
                        .acceptAllPrompts()
                        .evaluate("window.result = prompt('Are you here?');")
        );

        final String expectedMessage = "Cannot accept dialog which is already handled!";
        assertTrue(playwrightException.getMessage().contains(expectedMessage),
                String.format("Error does not contain expected message:\nError: %s\nShould contain: %s",
                        playwrightException.getMessage(),
                        expectedMessage));
    }

}
