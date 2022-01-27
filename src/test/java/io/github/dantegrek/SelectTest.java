package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Jplay.*;



public class SelectTest {

    private final String formsUrl = "https://dantegrek.github.io/testautomation-playground/forms.html";

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
    public void selectSingleValueAttributeTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(formsUrl)
                .waitTillNetworkIdle();
        when()
                .selectByValue("#select_tool", "sel");

        then()
                .expectThat()
                .selector("#select_tool_validate")
                .isVisible()
                .hasText("sel");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void selectMultipleValueAttributeTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(formsUrl)
                .waitTillNetworkIdle();
        when()
                .selectByValue("#select_lang", "java", "javascript");

        then()
                .expectThat()
                .selector("#select_lang_validate")
                .isVisible()
                .hasText("java,javascript");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void selectByLabelTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(formsUrl)
                .waitTillNetworkIdle();
        when()
                .selectByText("#select_tool", "Selenium");

        then()
                .expectThat()
                .selector("#select_tool_validate")
                .isVisible()
                .hasText("sel");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void selectByLabelsTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(formsUrl)
                .waitTillNetworkIdle();
        when()
                .selectByText("#select_lang", "Java", "JavaScript");

        then()
                .expectThat()
                .selector("#select_lang_validate")
                .isVisible()
                .hasText("java,javascript");
    }

}
