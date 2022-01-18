package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Jplay.*;

public class FocusTest {

    public final String formsUrl = "https://dantegrek.github.io/testautomation-playground/forms.html";

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
    public void focusTest(BrowserName browserName) {
        given()
                .contextConfig()
                .withIgnoreHTTPSErrors(true)
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(formsUrl);

        when()
                .focus("#notes")
                .expectThat()
                .selector("#notes")
                .isFocused();

        then()
                .focus("#common_sense")
                .expectThat()
                .selector("#common_sense")
                .isFocused()
                .and()
                .selector("#notes")
                .not().focused();
    }
}
