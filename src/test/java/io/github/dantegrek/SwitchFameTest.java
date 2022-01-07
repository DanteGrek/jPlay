package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Jplay.*;

public class SwitchFameTest {

    private static final String pathToFramesHtml = "file:" + Paths.get("src", "test", "resources", "playground/frames.html").toFile().getAbsolutePath();

    @AfterEach
    public void closeContext() {
        then()
                .closeBrowser()
                .cleanConfig();
    }

    public static Object[][] browsers() {
        return new Object[][]{
                {BrowserName.CHROMIUM/*, pathToFramesHtml*/},
                {BrowserName.WEBKIT/*, pathToFramesHtml*/},
                {BrowserName.FIREFOX/*, "https://dantegrek.github.io/testautomation-playground/frames.html"*/}
        };
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void switchOnFrameTest(BrowserName browserName, String url) {
        given()
                .timeoutConfig()
                .withExpectTimeout(1000)
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToFramesHtml);
        when()
                .switchOnFrame("#frame1")
                .click("#click_me_1");
        then()
                .expectThat()
                .selector("#click_me_1")
                .hasText("Clicked");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void switchOnMainFrame(BrowserName browserName, String url) {
        given()
                .timeoutConfig()
                .withExpectTimeout(1000)
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToFramesHtml);
        when()
                .switchOnFrame("#frame1")
                .switchOnFrame("#frame2")
                .click("#click_me_2");
        then()
                .expectThat()
                .selector("#click_me_2")
                .hasText("Clicked")
                .andActor()
                .switchOnMainFrame()
                .expectThat()
                .selector("#frame2")
                .is().count(0)
                .and().selector("#frame1")
                .isVisible();
    }
}
