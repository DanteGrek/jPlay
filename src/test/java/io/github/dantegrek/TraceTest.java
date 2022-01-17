package io.github.dantegrek;

import com.microsoft.playwright.PlaywrightException;
import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Jplay.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TraceTest {

    private final String playgroundUrl = "https://dantegrek.github.io/testautomation-playground";

    public static Object[][] browsers() {
        return new Object[][]{
                {BrowserName.CHROMIUM},
                {BrowserName.WEBKIT},
                {BrowserName.FIREFOX}
        };
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void recordTraceOnStartBrowserTest(BrowserName browserName) {
        given()
                .contextConfig()
                .withTrace(true)
                .withTraceNamePrefix("Record");

        when()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(playgroundUrl);

        then()
                .closeBrowser()
                .clearConfig();

        final File expectedTrace = Paths.get("target", "traces",
                "Record-" + browserName.name().toLowerCase() + "-trace.zip").toFile();

        assertTrue(expectedTrace.exists());

        expectedTrace.delete();
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void recordTraceOnStartBrowserAndCreateContextTest(BrowserName browserName) {
        given()
                .contextConfig()
                .withTrace(true);

        when()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(playgroundUrl);

        and()
                .createContextAndTab()
                .navigateTo(playgroundUrl);

        then()
                .contextConfig()
                .withTraceNamePrefix("Context1")
                .andActor()
                .closeCurrentContext()
                .contextConfig()
                .withTraceNamePrefix("Context2")
                .andActor()
                .switchContextByIndex(0)
                .closeBrowser()
                .clearConfig();

        final File expectedTrace = Paths.get("target", "traces",
                "Context1-" + browserName.name().toLowerCase() + "-trace.zip").toFile();
        final File expectedTrace2 = Paths.get("target", "traces",
                "Context2-" + browserName.name().toLowerCase() + "-trace.zip").toFile();

        assertTrue(expectedTrace.exists());
        assertTrue(expectedTrace2.exists());

        expectedTrace.delete();
        expectedTrace2.delete();
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void tracingChunkTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(playgroundUrl);

        when()
                .startTracingChunk("click and navigate")
                .click(".btn[href='expected_conditions.html']")
                .stopTracingChunk("expected-conditions");

        then()
                .closeBrowser()
                .clearConfig();

        final File expectedTrace = Paths.get("target", "traces",
                "expected-conditions-chunk-of-" + browserName.name().toLowerCase() + "-trace.zip").toFile();

        assertTrue(expectedTrace.exists());

        expectedTrace.delete();
    }

    @Test
    public void traceWasNotStartedTest() {
        given()
                .startBrowser()
                .contextConfig()
                .withTrace(true)
                .and()
                .navigateTo(playgroundUrl);

        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
                then()
                        .closeBrowser()
        );

        assertTrue(playwrightException.getMessage().contains("Must start tracing before stopping"));

        and()
                .clearConfig();
    }

    @Test
    public void traceChunkWasNotStartedTest() {
        given()
                .startBrowser()
                .navigateTo(playgroundUrl);

        PlaywrightException playwrightException = assertThrows(PlaywrightException.class, () ->
                when()
                        .click(".btn[href='expected_conditions.html']")
                        .stopTracingChunk("expected-conditions")
        );

        assertTrue(playwrightException.getMessage().contains("Must start tracing before stopping"));

        then()
                .closeBrowser()
                .clearConfig();
    }

}
