package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Jplay.*;

public class MouseActionsTest {

    private final String mouseActionsUrl = "https://dantegrek.github.io/testautomation-playground/mouse_events.html";

    @AfterEach
    public void afterEach() {
        then()
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
    public void clickTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(mouseActionsUrl);
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
                .navigateTo(mouseActionsUrl);
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
                .navigateTo(mouseActionsUrl);
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
                .navigateTo(mouseActionsUrl);
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
                .navigateTo(mouseActionsUrl);
        user()
                .dragAndDrop("#drag_source", "#drop_target");
        then()
                .expectThat()
                .selector("#drop_target")
                .hasText("Drop is successful!");
    }
}
