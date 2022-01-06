package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Jplay.*;

public class MouseActionsTest {

    private final String pathToMouseActionsHtml = "file:" + Paths.get("src", "test", "resources", "playground/mouse_events.html").toFile().getAbsolutePath();

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
    public void clickTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToMouseActionsHtml);
        user()
                .click("#click_area");
        then()
                .expectThat()
                .selector("#click_type")
                .hasText("Click");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void doubleClickTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToMouseActionsHtml);
        user()
                .doubleClick("#click_area");
        then()
                .expectThat()
                .selector("#click_type")
                .hasText("Double-Click");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void rightClickTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToMouseActionsHtml);
        user()
                .rightClick("#click_area");
        then()
                .expectThat()
                .selector("#click_type")
                .hasText("Right-Click");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void hoverTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToMouseActionsHtml);
        user()
                .hover("button.dropbtn");
        then()
                .expectThat()
                .selector("#dd_java")
                .isVisible();
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void dragAndDropTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToMouseActionsHtml);
        user()
                .dragAndDrop("#drag_source", "#drop_target");
        then()
                .expectThat()
                .selector("#drop_target")
                .hasText("Drop is successful!");
    }
}
