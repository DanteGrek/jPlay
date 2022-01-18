package io.github.dantegrek;

import com.microsoft.playwright.PlaywrightException;
import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Jplay.*;
import static io.github.dantegrek.jplay.tasks.Request.request;
import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {

    @AfterEach
    public void afterEach() {
        then()
                .closeBrowser();
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
    public void overrideRequestUrlTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and().startBrowser();
        when()
                .set(request()
                        .forUrl(url -> url.endsWith("expected_conditions.html"))
                        .onCurrentPage()
                        .overrideUrl("https://dantegrek.github.io/testautomation-playground/keyboard_events.html")
                );

        then()
                .navigateTo("https://dantegrek.github.io/testautomation-playground")
                .click(".btn[href='expected_conditions.html']")
                .waitTillNetworkIdle()
                .expectThat()
                .selector("h2")
                .has().text("Keyboard Actions");

    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void abortRequestByUrlPredicateTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and().startBrowser();
        when()
                .set(request()
                        .forUrl(url -> url.endsWith("playground"))
                        .onCurrentContext()
                        .abort()
                );

        assertThrows(PlaywrightException.class, () ->
                then()
                        .navigateTo("https://dantegrek.github.io/testautomation-playground")
        );
    }
}
