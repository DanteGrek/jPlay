package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

import java.nio.file.Paths;

import static io.github.dantegrek.actions.TestAction.testAction;
import static io.github.dantegrek.jplay.Actor.actor;
import static io.github.dantegrek.jplay.Jplay.given;
import static io.github.dantegrek.jplay.Jplay.then;
import static org.junit.jupiter.api.Assertions.*;

public class PseudoElementApiTest {

    private final String pathToAdvancedHtml = "file:" + Paths.get("src", "test", "resources", "playground/advanced.html").toFile().getAbsolutePath();

    @AfterEach
    public void closeContext() {
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
    public void expectVisiblePseudoElementTest(BrowserName browserName) {
        given()
                .timeoutConfig()
                .withExpectTimeout(1000)
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToAdvancedHtml);
        actor()
                .softExpectThat()
                .visiblePseudoElementHasPropertyWithValue(".star-rating", "::after", "display", "inline-block")
                .visiblePseudoElementHasNotProperty(".star-rating", "::after", "display1")
                .visiblePseudoElementPropertyContains(".star-rating", "::after", "content", "*")
                .visiblePseudoElementPropertyNotContains(".star-rating", "::after", "content", "jplay")
                .checkAll();
    }

    @Test
    public void negativeExpectVisiblePseudoElementTest() {
        given()
                .timeoutConfig()
                .withExpectTimeout(1000)
                .and()
                .startBrowser()
                .navigateTo(pathToAdvancedHtml);
        AssertionFailedError assertionFailedError = assertThrows(AssertionFailedError.class, () ->
                actor()
                        .softExpectThat()
                        .visiblePseudoElementHasPropertyWithValue(".star-rating", "after", "display", "inline-block1")
                        .visiblePseudoElementHasNotProperty(".star-rating", "after", "display")
                        .visiblePseudoElementPropertyContains(".star-rating", "after", "content", "jplay")
                        .visiblePseudoElementPropertyNotContains(".star-rating", "after", "content", "*")
                        .checkAll()
        );
        assertAll("Error messages.",
                () -> assertionFailedError.getMessage().contains("Pseudo element property 'display' not match.\n" +
                        "Expected: inline-block1\n" +
                        "Actual: inline-block\n"),
                () -> assertionFailedError.getMessage().contains("Pseudo element property 'display' exists or not empty.\n" +
                        "Value: inline-block\n"),
                () -> assertionFailedError.getMessage().contains("Pseudo element property 'content' not contains:\n"),
                () -> assertionFailedError.getMessage().contains("Pseudo element property 'content' contains:\n"));
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void getContentFromPseudoElementTest(BrowserName browserName) {
        given()
                .timeoutConfig()
                .withExpectTimeout(1000)
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToAdvancedHtml);
        String rating = testAction()
                .getRating();
        actor()
                .fillText("#txt_rating", rating)
                .click("#check_rating");
        then()
                .expectThat()
                .selector("#validate_rating")
                .isVisible()
                .and()
                .hasText("Well done!");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void negativeGetContentFromPseudoElementTest(BrowserName browserName) {
        given()
                .timeoutConfig()
                .withExpectTimeout(1000)
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToAdvancedHtml);

        String content = testAction()
                .getNotExistingPseudoElement();

        assertTrue(content.equals("none") || content.isEmpty());
    }
}
