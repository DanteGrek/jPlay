package io.github.dantegrek;

import io.github.dantegrek.enums.BrowserName;
import io.github.dantegrek.enums.Device;
import io.github.dantegrek.enums.Key;

import static io.github.dantegrek.jplay.Actor.actor;
import static io.github.dantegrek.jplay.Jplay.*;

public class Main {

    public static void main(String[] args) {
        jPlay()
                .browserConfig()
                .withBrowser(BrowserName.CHROME)
                .withHeadless(false)
                .contextConfig()
                .withDevice(Device.IPHONE_12)
                .and()
                .startBrowser()
                .navigateTo("https://google.com");

        user()
                .fillText("input:visible", "maven jplay")
                .key(Key.ENTER);

        actor()
                .expectThat()
                .selector("text=jplay")
                .isVisible();
    }
}
