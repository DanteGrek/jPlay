package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Jplay.*;

public class UploadFileTest {

    private final String uploadFileName = "fileToUpload.txt";
    private final String secondUploadFileName = "secondFileToUpload.txt";
    private final String pathToFormHtml = "https://dantegrek.github.io/testautomation-playground/forms.html";//"file:" + Paths.get("src", "test", "resources", "playground/forms.html").toFile().getAbsolutePath();
    private final Path uploadFilePath = Paths.get("src", "test", "resources", uploadFileName);
    private final Path uploadSecondFilePath = Paths.get("src", "test", "resources", secondUploadFileName);

    @AfterEach
    public void closeContext() {
        then()
                .closeBrowser()
                .cleanConfig();
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
    public void uploadFileTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToFormHtml);
        when()
                .uploadFile("#upload_cv", uploadFilePath);
        then()
                .softExpectThat()
                .selector("#validate_cv")
                .isVisible()
                .and()
                .hasText(uploadFileName)
                .checkAll();
    }

    @ParameterizedTest
    @MethodSource("browsers")
    public void uploadMultipleFilesTest(BrowserName browserName) {
        given()
                .browserConfig()
                .withBrowser(browserName)
                .and()
                .startBrowser()
                .navigateTo(pathToFormHtml);
        when()
                .uploadFiles("#upload_files", uploadFilePath, uploadSecondFilePath);
        then()
                .softExpectThat()
                .selector("#validate_files")
                .isVisible()
                .and()
                .containsText(uploadFileName)
                .and()
                .containsText(secondUploadFileName)
                .checkAll();
    }
}
