package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Jplay.*;

public class SwitchFrameTest {

    private static final String framesUrl = "https://dantegrek.github.io/testautomation-playground/frames.html";

    @AfterEach
    public void closeContext() {
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
    public void switchOnFrameTest(BrowserName browserName) {
        given()
                .contextConfig()
                .withIgnoreHTTPSErrors(true)
                .timeoutConfig()
                .withExpectTimeout(1000)
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(framesUrl);
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
    public void switchOnMainFrame(BrowserName browserName) {
        given()
                .contextConfig()
                .withIgnoreHTTPSErrors(true)
                .timeoutConfig()
                .withExpectTimeout(1000)
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(framesUrl);
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
                .and()
                .selector("#frame1")
                .isVisible();
    }
}
