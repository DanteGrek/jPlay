package io.github.dantegrek;

import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Jplay.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DialogTest {

    private final String pathToAlertHtml = "file:" + Paths.get("src", "test", "resources", "playground/Alerts.html").toFile().getAbsolutePath();

    @BeforeAll
    public void beforeAll() {
        given()
                .startPureBrowser();
    }

    @AfterAll
    public void afterAll() {
        then()
                .closeBrowser()
                .cleanConfig();
    }

    @BeforeEach
    public void beforeEach() {
        given().createContextAndTab().navigateTo(pathToAlertHtml);
    }

    @AfterEach
    public void afterEach() {
        then().closeCurrentContext();
    }

    @Test
    public void acceptJsAlertTest() {
        when()
                .dialog()
                .acceptOnce()
                .click("button[onclick='jsAlert()']")
                .expectThat()
                .selector("#result")
                .hasText("You successfully clicked an alert");
    }

    @Test
    public void acceptOnceJsConfirmTest() {
        when()
                .dialog()
                .acceptOnce()
                .click("button[onclick='jsConfirm()']")
                .expectThat()
                .selector("#result")
                .hasText("You clicked: Ok")
                .andActor()
                .click("button[onclick='jsConfirm()']")
                .expectThat()
                .selector("#result")
                .hasText("You clicked: Cancel");
    }

    @Test
    public void acceptAllJsConfirmTest() {
        when()
                .dialog()
                .acceptAll()
                .click("button[onclick='jsConfirm()']")
                .expectThat()
                .selector("#result")
                .hasText("You clicked: Ok")
                .andActor()
                .click("button[onclick='jsConfirm()']")
                .expectThat()
                .selector("#result")
                .hasText("You clicked: Ok");
    }

    @Test
    public void acceptOnceJsPromptTest() {
        when()
                .dialog()
                .acceptOnce("jPlay")
                .click("button[onclick='jsPrompt()']")
                .expectThat()
                .selector("#result")
                .hasText("You entered: jPlay")
                .andActor()
                .click("button[onclick='jsPrompt()']")
                .expectThat()
                .selector("#result")
                .hasText("You entered: null");
    }

    @Test
    public void acceptAllJsPromptTest() {
        when()
                .dialog()
                .acceptAll("jPlay")
                .click("button[onclick='jsPrompt()']")
                .expectThat()
                .selector("#result")
                .hasText("You entered: jPlay")
                .andActor()
                .click("button[onclick='jsPrompt()']")
                .expectThat()
                .selector("#result")
                .hasText("You entered: jPlay");
    }
}
