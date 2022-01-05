package io.github.dantegrek;

import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Jplay.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MouseActionsTest {

    private final String pathToMouseActionsHtml = "file:" + Paths.get("src", "test", "resources", "playground/mouse_events.html").toFile().getAbsolutePath();


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
                .navigateTo(pathToMouseActionsHtml);
    }

    @AfterEach
    public void afterEach() {
        given()
                .closeCurrentContext();
    }

    @Test
    public void clickTest() {
        user()
                .click("#click_area")
                .expectThat()
                .selector("#click_type")
                .hasText("Click");
    }

    @Test
    public void doubleClickTest() {
        user()
                .doubleClick("#click_area")
                .expectThat()
                .selector("#click_type")
                .hasText("Double-Click");
    }

    @Test
    public void rightClickTest() {
        user()
                .rightClick("#click_area")
                .expectThat()
                .selector("#click_type")
                .hasText("Right-Click");
    }

    @Test
    public void hoverTest() {
        user()
                .hover("button.dropbtn")
                .expectThat()
                .selector("#dd_java")
                .isVisible();
    }

    @Test
    public void dragAndDropTest() {
        user()
                .dragAndDrop("#drag_source", "#drop_target")
                .expectThat()
                .selector("#drop_target")
                .hasText("Drop is successful!");
    }
}
