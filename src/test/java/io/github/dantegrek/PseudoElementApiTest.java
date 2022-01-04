package io.github.dantegrek;

import org.junit.jupiter.api.*;
import org.opentest4j.AssertionFailedError;

import java.nio.file.Paths;

import static io.github.dantegrek.actions.TestAction.testAction;
import static io.github.dantegrek.jplay.Actor.actor;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PseudoElementApiTest {

    private final String pathToAdvancedHtml = "file:" + Paths.get("src", "test", "resources", "playground/advanced.html").toFile().getAbsolutePath();


    @BeforeAll
    public void startBrowser() {
        actor()
                .timeoutConfig()
                .withExpectTimeout(1000)
                .and()
                .startPureBrowser();
    }

    @AfterAll
    public void closeBrowser() {
        actor()
                .closeBrowser()
                .cleanConfig();
    }

    @BeforeEach
    public void beforeTest() {
        actor()
                .createContextAndTab()
                .navigateTo(pathToAdvancedHtml);
    }

    @AfterEach
    public void closeContext() {
        actor()
                .closeCurrentContext();
    }

    @Test
    public void expectVisiblePseudoElementTest() {
        actor()
                .softExpectThat()
                .visiblePseudoElementHasPropertyWithValue(".star-rating", "after", "display", "inline-block")
                .visiblePseudoElementHasNotProperty(".star-rating", "after", "display1")
                .visiblePseudoElementPropertyContains(".star-rating", "after", "content", "*")
                .visiblePseudoElementPropertyNotContains(".star-rating", "after", "content", "jplay")
                .checkAll();
    }

    @Test
    public void negativeExpectVisiblePseudoElementTest() {
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

    @Test
    public void getContentFromPseudoElementTest() {
        String rating = testAction()
                .getRating();
        actor()
                .fillText("#txt_rating", rating)
                .click("#check_rating")
                .expectThat()
                .selector("#validate_rating")
                .isVisible()
                .and()
                .hasText("Well done!");
    }

    @Test
    public void negativeGetContentFromPseudoElementTest() {
        AssertionFailedError assertionFailedError = assertThrows(AssertionFailedError.class, () ->
                testAction()
                .getNegativeRating()
        );
        assertEquals("Selector '#check_rating' with pseudo-element 'before' did not found any content. JS returned 'none'.",
                assertionFailedError.getMessage());
    }
}
