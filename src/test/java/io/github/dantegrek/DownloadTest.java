package io.github.dantegrek;

import com.microsoft.playwright.Download;
import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.github.dantegrek.jplay.Jplay.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DownloadTest {

    public final String jPlayUrl = "https://github.com/DanteGrek/jPlay";

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
    public void downloadFileTest(BrowserName browserName) {
        given()
                .contextConfig()
                .withAcceptDownloads(true)
                .and()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(jPlayUrl);

        Download download = when()
                .click("get-repo")
                .clickAndWaitTillDownload("text=Download");

        assertNotNull(download);
    }
}
