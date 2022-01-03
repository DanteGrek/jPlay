package io.github.dantegrek;

import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static io.github.dantegrek.jplay.Actor.actor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UploadFileTest {

    private final String uploadFileName = "fileToUpload.txt";
    private final String secondUploadFileName = "secondFileToUpload.txt";
    private final String pathToFormHtml = "file:" + Paths.get("src", "test", "resources", "playground/forms.html").toFile().getAbsolutePath();
    private final Path uploadFilePath = Paths.get("src", "test", "resources", uploadFileName);
    private final Path uploadSecondFilePath = Paths.get("src", "test", "resources", secondUploadFileName);

    @BeforeAll
    public void startBrowser() {
        actor()
                .config()
                .configIsFinished()
                .startPureBrowser();
    }

    @AfterAll
    public void closeBrowser() {
        actor()
                .closeBrowser()
                .cleanConfig();
    }

    @BeforeEach
    public void openTab() {
        actor()
                .createContextAndTab();
    }

    @AfterEach
    public void closeContext() {
        actor()
                .closeCurrentContext();
    }

    @Test
    public void uploadFileTest() {
        actor()
                .navigateTo(pathToFormHtml)
                .uploadFile("#upload_cv", uploadFilePath)
                .softExpectThat()
                .selector("#validate_cv")
                .isVisible()
                .and()
                .hasText(uploadFileName)
                .checkAll();
    }

    @Test
    public void uploadMultipleFilesTest() {
        actor()
                .navigateTo(pathToFormHtml)
                .uploadFiles("#upload_files", List.of(uploadFilePath, uploadSecondFilePath))
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
