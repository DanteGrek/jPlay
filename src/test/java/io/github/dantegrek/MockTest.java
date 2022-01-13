package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import io.github.dantegrek.enums.RestMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Jplay.*;
import static io.github.dantegrek.jplay.tasks.Mock.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MockTest {

    private final String playgroundUrl = "https://dantegrek.github.io/testautomation-playground";
    private final Path filePathToPayload = Paths.get("src", "test", "resources", "test.html");

    @AfterEach
    public void afterEach() {
        then()
                .closeBrowser()
                .clearConfig()
                .clearMemory();
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
    public void addRemoveMockOnPageTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser();
        when()
                .set(mock()
                        .forUrl("**/testautomation-playground")
                        .onCurrentPage()
                        .withContentType("text/html")
                        .withBody("<html><h1>HELLO FROM MOCK!</h1></html>"));
        and()
                .navigateTo(playgroundUrl);
        then()
                .expectThat()
                .selector("h1")
                .isVisible()
                .hasText("HELLO FROM MOCK!");
        and()
                .removeMockFromPageForUrl("**/testautomation-playground")
                .navigateTo(playgroundUrl)
                .expectThat()
                .selector("h1")
                .hasText("The Playground");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void addMockForRestMethodTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser();
        when()
                .set(mock()
                        .forUrl("**/testautomation-playground")
                        .onCurrentPage()
                        .forMethod(RestMethod.POST)
                        .withContentType("text/html")
                        .withBody("<html><h1>HELLO FROM MOCK!</h1></html>"));
        and()
                .navigateTo(playgroundUrl);
        then()
                .expectThat()
                .selector("h1")
                .hasText("The Playground");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void addRemoveMockWithPredicateOnPageTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser();
        when()
                .set(mock()
                        .forUrl("mock 1", url -> url.endsWith("playground"))
                        .onCurrentPage()
                        .withContentType("text/html")
                        .withBody("<html><h1>HELLO FROM MOCK!</h1></html>"));
        and()
                .navigateTo(playgroundUrl);
        then()
                .expectThat()
                .selector("h1")
                .isVisible()
                .hasText("HELLO FROM MOCK!");
        and()
                .removeRoutFromPageForUrlByName("mock 1")
                .navigateTo(playgroundUrl)
                .expectThat()
                .selector("h1")
                .hasText("The Playground");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void addRemoveMockOnContextTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser();
        when()
                .set(mock()
                        .forUrl("**/testautomation-playground")
                        .onCurrentContext()
                        .withContentType("text/html")
                        .withBody("<html><h1>HELLO FROM MOCK!</h1></html>"));
        and()
                .navigateTo(playgroundUrl);
        then()
                .expectThat()
                .selector("h1")
                .isVisible()
                .hasText("HELLO FROM MOCK!");
        and()
                .removeMockFromContextForUrl("**/testautomation-playground")
                .navigateTo(playgroundUrl)
                .expectThat()
                .selector("h1")
                .hasText("The Playground");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void addMockOnPageRemoveMockOnContextDoesNoHasEffectTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser();
        when()
                .set(mock()
                        .forUrl("**/testautomation-playground")
                        .onCurrentPage()
                        .withContentType("text/html")
                        .withBody("<html><h1>HELLO FROM MOCK!</h1></html>"));
        and()
                .navigateTo(playgroundUrl);
        then()
                .expectThat()
                .selector("h1")
                .isVisible()
                .hasText("HELLO FROM MOCK!");
        and()
                .removeMockFromContextForUrl("**/testautomation-playground")
                .navigateTo(playgroundUrl)
                .expectThat()
                .selector("h1")
                .hasText("HELLO FROM MOCK!");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void addMockOnContextRemoveMockOnPageDoesNoHasEffectTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser();
        when()
                .set(mock()
                        .forUrl("**/testautomation-playground")
                        .onCurrentContext()
                        .withContentType("text/html")
                        .withBody("<html><h1>HELLO FROM MOCK!</h1></html>"));
        and()
                .navigateTo(playgroundUrl);
        then()
                .expectThat()
                .selector("h1")
                .isVisible()
                .hasText("HELLO FROM MOCK!");
        and()
                .removeMockFromPageForUrl("**/testautomation-playground")
                .navigateTo(playgroundUrl)
                .expectThat()
                .selector("h1")
                .hasText("HELLO FROM MOCK!");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void addMockWithPayloadAsFileEffectTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser();
        when()
                .set(mock()
                        .forUrl("**/testautomation-playground")
                        .onCurrentContext()
                        .withContentType("text/html")
                        .withPathToPayload(filePathToPayload));
        and()
                .navigateTo(playgroundUrl);
        then()
                .expectThat()
                .selector("h1")
                .hasText("Jplay!");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void addOneTimeMockTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser();
        when()
                .set(mock()
                        .forUrl("**/testautomation-playground")
                        .onCurrentPage()
                        .withContentType("text/html")
                        .withBody("<html><h1>HELLO FROM MOCK!</h1></html>")
                        .useTimes(1));
        and()
                .navigateTo(playgroundUrl);
        then()
                .expectThat()
                .selector("h1")
                .isVisible()
                .hasText("HELLO FROM MOCK!");
        and()
                .navigateTo(playgroundUrl)
                .expectThat()
                .selector("h1")
                .hasText("The Playground");
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void addTwoTimeMockTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser();
        when()
                .set(mock()
                        .forUrl("**/testautomation-playground")
                        .onCurrentPage()
                        .withContentType("text/html")
                        .withBody("<html><h1>HELLO FROM MOCK!</h1></html>")
                        .useTimes(2));
        and()
                .navigateTo(playgroundUrl);
        then()
                .expectThat()
                .selector("h1")
                .isVisible()
                .hasText("HELLO FROM MOCK!");
        and()
                .navigateTo(playgroundUrl)
                .expectThat()
                .selector("h1")
                .hasText("HELLO FROM MOCK!");
        and()
                .navigateTo(playgroundUrl)
                .expectThat()
                .selector("h1")
                .hasText("The Playground");
    }

    @Test
    public void zeroTimeMockTest() {
        given()
                .browserConfig()
                .and()
                .startBrowser();

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
        when()
                .set(mock()
                        .forUrl("**/testautomation-playground")
                        .onCurrentPage()
                        .withContentType("text/html")
                        .withBody("<html><h1>HELLO FROM MOCK!</h1></html>")
                        .useTimes(0))
        );

        assertEquals("Minimum 1 time is allowed.", runtimeException.getMessage());
    }

    @Test
    public void addTheSameMockOnContextAndPageTest() {
        given()
                .startBrowser();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                when()
                        .set(mock()
                                .forUrl("**/testautomation-playground")
                                .onCurrentContext()
                                .onCurrentPage())
        );

        assertEquals("You already specified 'onCurrentContext'.", runtimeException.getMessage());
    }

    @Test
    public void addTheSameMockOnPageAndContextTest() {
        given()
                .startBrowser();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                when()
                        .set(mock()
                                .forUrl("**/testautomation-playground")
                                .onCurrentPage()
                                .onCurrentContext())
        );

        assertEquals("You already specified 'onCurrentPage'.", runtimeException.getMessage());
    }

    @Test
    public void addMockWithoutContextOrPageTest() {
        given()
                .startBrowser();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                when()
                        .set(mock()
                                .forUrl("**/testautomation-playground")
                                .withContentType("text/html")
                                .withBody("<html><h1>HELLO FROM MOCK!</h1></html>"))
        );

        assertEquals("You have to specify 'onCurrentPage()' or 'onCurrentContext()'.", runtimeException.getMessage());
    }

    @Test
    public void addMockWithoutStartedContextAndPageTest() {
        given()
                .startPureBrowser();

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                when()
                        .set(mock()
                                .forUrl("**/testautomation-playground")
                                .onCurrentContext()
                                .withContentType("text/html")
                                .withBody("<html><h1>HELLO FROM MOCK!</h1></html>"))
        );

        assertEquals("You have to start context and page before creating network route.", runtimeException.getMessage());
    }

    @Test
    public void addMockWithoutUrlTest() {
        given()
                .startBrowser();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                when()
                        .set(mock()
                                .onCurrentPage()
                                .withContentType("text/html")
                                .withBody("<html><h1>HELLO FROM MOCK!</h1></html>"))
        );

        assertEquals("You have to specify for which url this mock is, use 'forUrl()'.", runtimeException.getMessage());
    }
}
