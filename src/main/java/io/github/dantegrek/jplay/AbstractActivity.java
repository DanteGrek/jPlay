package io.github.dantegrek.jplay;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.opentest4j.AssertionFailedError;

import static io.github.dantegrek.jplay.JsStrings.JS_PSEUDO_ELEMENT_CONTENT;

/**
 * This class represents base level of abstraction
 * witch is used in Actor class to insert Actor instance and base helper methods.
 */
abstract class AbstractActivity {
    /**
     * Internal pointer on actor.
     */
    protected Actor actor = Actor.actor();

    /**
     * This method returns locator object which is tied to current page/frame.
     *
     * @param selector css or xpath
     * @return Locator object
     */
    protected Locator findBy(String selector) {
        return actor.currentFrame().locator(selector);
    }

    /**
     * This method returns locator object which is tied to current page/frame.
     *
     * @param text element should contain
     * @return Locator object
     */
    protected Locator findByContainsText(String text) {
        return actor.currentFrame().locator(String.format("text=%s", text));
    }

    /**
     * This method returns locator object which is tied to current page/frame.
     *
     * @param text element should contain
     * @return Locator object
     */
    protected Locator findByText(String text) {
        return actor.currentFrame().locator(String.format(":text(%s)", text));
    }

    /**
     * This method returns locator object which is tied to current page/frame.
     *
     * @param ancestor selector of expected ancestor
     * @param child    child element which ancestor should have.
     * @return Locator object
     */
    protected Locator findAncestorWithChild(String ancestor, String child) {
        return actor.currentFrame().locator(String.format("%s:has(%s)", ancestor, child));
    }
}
