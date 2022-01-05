package io.github.dantegrek;

import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static io.github.dantegrek.enums.Key.*;
import static io.github.dantegrek.jplay.Jplay.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class KeyboardActionsTest {

    private final String pathToKeyboardActionsHtml = "file:" + Paths.get("src", "test", "resources", "playground/keyboard_events.html").toFile().getAbsolutePath();

    @BeforeAll
    public void beforeAll() {
        given()
                .startPureBrowser();
    }

    @AfterAll
    public void afterAll() {
        then()
                .closeBrowser();
    }

    @BeforeEach
    public void beforeEach() {
        given()
                .createContextAndTab()
                .navigateTo(pathToKeyboardActionsHtml);
    }

    @AfterEach
    public void afterEach() {
        given()
                .closeCurrentContext();
    }

    @Test
    public void keyTest() {
        when()
                .click("#area")
                .key(DIGIT_0)
                .expectThat()
                .selector("#key")
                .hasText("0");
    }

    @Test
    public void keyWithSelectorTest() {
        when()
                .key("#area", F1)
                .expectThat()
                .selector("#key")
                .hasText("F1");
    }

    @Test
    public void keyDownKeyUpTest() {
        when()
                .click("#area")
                .keyDown(SHIFT)
                .expectThat()
                .selector("#key")
                .hasText("Shift");
        and()
                .key(KEY_A)
                .keyUp(SHIFT)
                .expectThat()
                .selector("#key")
                .hasText("A");
        and()
                .key(KEY_A)
                .expectThat()
                .selector("#key")
                .hasText("a");
    }

    @Test
    public void insertTextTest() {
        when()
                .click("#area")
                .insertText("嗨")
                .expectThat()
                .selector("#area")
                .hasValue("嗨");
    }

}
