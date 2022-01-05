package io.github.dantegrek;

import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Jplay.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DialogTest {

    private final String pathToIndexHtml = "file:" + Paths.get("src", "test", "resources", "playground/index.html").toFile().getAbsolutePath();

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
        given().createContextAndTab().navigateTo(pathToIndexHtml);
    }

    @AfterEach
    public void afterEach() {
        then().closeCurrentContext();
    }

    @Test
    public void expectThatMessageEqualAndDismissJsDialogTest() {
        when()
                .dialog()
                .expectThatMessageEqualAndDismiss("Are you here?")
                .evaluate("window.result = confirm('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("false")
                .evaluate("alert(window.result);");
    }

    @Test
    public void expectThatMessageEqualAndAcceptJsDialogTest() {
        when()
                .dialog()
                .expectThatMessageEqualAndAccept("Are you here?")
                .evaluate("window.result = confirm('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("true")
                .evaluate("alert(window.result);");
    }

    @Test
    public void acceptOnceJsConfirmDialogTest() {
        when()
                .dialog()
                .acceptConfirmOnce()
                .evaluate("window.result = confirm('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("true")
                .evaluate("alert(window.result);")
                .evaluate("window.result = confirm('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("false")
                .evaluate("alert(window.result);");
    }

    @Test
    public void acceptAllJsConfirmDialogsTest() {
        when()
                .dialog()
                .acceptAllConfirms()
                .evaluate("window.result = confirm('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("true")
                .evaluate("alert(window.result);")
                .evaluate("window.result = confirm('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("true")
                .evaluate("alert(window.result);");
    }

    @Test
    public void acceptOnceJsPromptDialogTest() {
        when()
                .dialog()
                .acceptPromptOnce("Yes")
                .evaluate("window.result = prompt('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("Yes")
                .evaluate("alert(window.result);")
                .evaluate("window.result = prompt('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("null")
                .evaluate("alert(window.result);");
    }

    @Test
    public void acceptAllJsPromptDialogsTest() {
        when()
                .dialog()
                .acceptAllPrompts("Yes")
                .evaluate("window.result = prompt('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("Yes")
                .evaluate("alert(window.result);")
                .evaluate("window.result = prompt('Are you here?');")
                .dialog()
                .expectThatMessageEqualAndDismiss("Yes")
                .evaluate("alert(window.result);");
    }

}
