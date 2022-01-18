package io.github.dantegrek;

import com.microsoft.playwright.*;
import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Jplay.*;
import static io.github.dantegrek.jplay.Jplay.then;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StrictModeTest {

//    public final String formsUrl = "https://dantegrek.github.io/testautomation-playground/forms.html";
//
//    @AfterEach
//    public void afterEach() {
//        then()
//                .closeBrowser()
//                .clearConfig();
//    }
//
//    public static Object[][] browsers() {
//        return new Object[][]{
//                {BrowserName.CHROMIUM},
//                {BrowserName.WEBKIT},
//                {BrowserName.FIREFOX}
//        };
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void clickStrictModeSingleElementTest(BrowserName browserName) {
//        given()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl);
//
//        when()
//                .useStrict(true)
//                .click("#notes");
//
//        then()
//                .expectThat()
//                .selector("#notes")
//                .count(1);
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void clickStrictModeMultipleElementsTest(BrowserName browserName) {
//        given()
//                .timeoutConfig()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl)
//                .useStrict(true);
//
//        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
//                when()
//                        .click(".form-check-input")
//        );
//
//        assertTrue(playwrightException.getMessage().contains("strict mode violation"));
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void doubleClickStrictModeSingleElementTest(BrowserName browserName) {
//        given()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl);
//
//        when()
//                .useStrict(true)
//                .doubleClick("#notes");
//
//        then()
//                .expectThat()
//                .selector("#notes")
//                .count(1);
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void doubleClickStrictModeMultipleElementsTest(BrowserName browserName) {
//        given()
//                .timeoutConfig()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl)
//                .useStrict(true);
//
//        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
//                when()
//                        .doubleClick(".form-check-input")
//        );
//
//        assertTrue(playwrightException.getMessage().contains("strict mode violation"));
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void fillTextStrictModeSingleElementTest(BrowserName browserName) {
//        given()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl);
//
//        when()
//                .useStrict(true)
//                .fillText("#notes", "strict");
//
//        then()
//                .expectThat()
//                .selector("#notes")
//                .count(1);
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void fillTextStrictModeMultipleElementsTest(BrowserName browserName) {
//        given()
//                .timeoutConfig()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl)
//                .useStrict(true);
//
//        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
//                when()
//                        .fillText(".form-check-input", "strict")
//        );
//
//        assertTrue(playwrightException.getMessage().contains("strict mode violation"));
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void typeTextStrictModeSingleElementTest(BrowserName browserName) {
//        given()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl);
//
//        when()
//                .useStrict(true)
//                .typeText("#notes", "strict");
//
//        then()
//                .expectThat()
//                .selector("#notes")
//                .count(1);
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void typeTextStrictModeMultipleElementsTest(BrowserName browserName) {
//        given()
//                .timeoutConfig()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl)
//                .useStrict(true);
//
//        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
//                when()
//                        .typeText(".form-check-input", "strict")
//        );
//
//        assertTrue(playwrightException.getMessage().contains("strict mode violation"));
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void hoverStrictModeSingleElementTest(BrowserName browserName) {
//        given()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl);
//
//        when()
//                .useStrict(true)
//                .hover("#notes");
//
//        then()
//                .expectThat()
//                .selector("#notes")
//                .count(1);
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void hoverStrictModeMultipleElementsTest(BrowserName browserName) {
//        given()
//                .timeoutConfig()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl)
//                .useStrict(true);
//
//        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
//                when()
//                        .hover(".form-check-input")
//        );
//
//        assertTrue(playwrightException.getMessage().contains("strict mode violation"));
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void checkUncheckStrictModeSingleElementTest(BrowserName browserName) {
//        given()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl);
//
//        when()
//                .useStrict(true)
//                .check("#check_java")
//                .uncheck("#check_java");
//
//        then()
//                .expectThat()
//                .selector("#check_java")
//                .count(1);
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void checkUncheckStrictModeMultipleElementsTest(BrowserName browserName) {
//        given()
//                .timeoutConfig()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl)
//                .useStrict(true);
//
//        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
//                when()
//                        .check(".form-check-input")
//        );
//
//        and()
//                .useStrict(false)
//                .check(".form-check-input")
//                .useStrict(true);
//
//        PlaywrightException playwrightException2 = assertThrows(PlaywrightException.class, () ->
//                when()
//                        .uncheck(".form-check-input")
//        );
//
//        assertTrue(playwrightException.getMessage().contains("strict mode violation"));
//        assertTrue(playwrightException2.getMessage().contains("strict mode violation"));
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void selectStrictModeSingleElementTest(BrowserName browserName) {
//        given()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl);
//
//        when()
//                .useStrict(true)
//                .selectByText("#select_tool", "Selenium");
//
//        then()
//                .expectThat()
//                .selector("#select_tool")
//                .count(1);
//    }
//
//    @ParameterizedTest
//    @MethodSource("browsers")
//    public void selectStrictModeMultipleElementsTest(BrowserName browserName) {
//        given()
//                .timeoutConfig()
//                .browserConfig()
//                .withBrowser(browserName)
//                .and()
//                .startBrowser()
//                .navigateTo(formsUrl)
//                .useStrict(true);
//
//        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
//                when()
//                        .selectByText("select", "Java")
//        );
//
//        assertTrue(playwrightException.getMessage().contains("strict mode violation"));
//    }
}
