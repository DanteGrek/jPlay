package io.github.dantegrek.tasks;

import com.microsoft.playwright.Locator;
import io.github.dantegrek.jplay.Task;

public class UserSee extends Task {

    private Locator locator;
    private String expectedText;

    public UserSee locator(String selector) {
        this.locator = findBy(selector);
        return this;
    }

    public UserSee withText(String text) {
        this.expectedText = text;
        return this;
    }

    @Override
    public void perform() {
        this.actor.softExpectThat()
                .locator(this.locator)
                .isVisible()
                .hasText(this.expectedText)
                .checkAll();
    }

    public static UserSee userSee() {
        return new UserSee();
    }
}
