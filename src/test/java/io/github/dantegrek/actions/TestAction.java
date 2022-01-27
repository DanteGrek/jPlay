package io.github.dantegrek.actions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ViewportSize;
import io.github.dantegrek.jplay.Action;

public class TestAction extends Action {

    public String getUserAgent() {
        return (String) this.actor.currentPage().evaluate("window.navigator.userAgent");
    }

    public ViewportSize getViewportSize() {
        return this.actor.currentPage().viewportSize();
    }

    public int getDeviceScaleFactor() {
        return (int) this.actor.currentPage().evaluate("window.devicePixelRatio");
    }

    public static TestAction testAction() {
        return new TestAction();
    }

    public String getRating() {
       return this.actor.getPseudoElementContent(":light(.star-rating)", "::after");
    }

    public String getNotExistingPseudoElement() {
        return this.actor.getPseudoElementContent(":light(#check_rating)", "::before");
    }
}
