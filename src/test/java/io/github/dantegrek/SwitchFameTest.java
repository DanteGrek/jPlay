package io.github.dantegrek;

import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static io.github.dantegrek.jplay.Actor.actor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SwitchFameTest {

    private final String pathToFormHtml = "file:" + Paths.get("src", "test", "resources", "playground/frames.html").toFile().getAbsolutePath();


    @BeforeAll
    public void startBrowser() {
        actor()
                .config()
                .withExpectTimeout(1000)
                .and()
                .startPureBrowser();
    }

    @AfterAll
    public void closeBrowser() {
        actor()
                .closeBrowser();
    }

    @BeforeEach
    public void beforeTest() {
        actor()
                .createContextAndTab()
                .navigateTo(pathToFormHtml);
    }

    @AfterEach
    public void closeContext() {
        actor()
                .closeCurrentContext();
    }

    @Test
    public void switchOnFrameTest() {
        actor()
                .switchOnFrame("#frame1")
                .click("#click_me_1")
                .expectThat()
                .selector("#click_me_1")
                .hasText("Clicked");
    }

    @Test
    public void switchOnMainFrame() {
        actor()
                .switchOnFrame("#frame1")
                .switchOnFrame("#frame2")
                .click("#click_me_2")
                .expectThat().selector("#click_me_2").hasText("Clicked")
                .andActor().switchOnMainFrame()
                .expectThat().selector("#frame2")
                .is().count(0)
                .and().selector("#frame1").isVisible();
    }
}
