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
    protected Locator findByText(String text) {
        return actor.currentFrame().locator(String.format("text=%s", text));
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

    /**
     * This method helps extract content from pseudo-element
     * @param cssSelector css selector which points on element with ::after or ::before
     * @param pseudoElement after, before or any another pseudo-element.
     * @return value of attribute content as String.
     */
    protected String getPseudoElementContent(String cssSelector, String pseudoElement) {
        findBy(cssSelector).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String content = actor.currentFrame().evaluate(String.format(JS_PSEUDO_ELEMENT_CONTENT, cssSelector, pseudoElement)).toString();
        if(content.equals("none") || content.isEmpty()){
            return content;
        }
        // JS returns string as object and when Java do toString()
        // it wraps js string into java String that is why we need to rid off first and last characters in next line
        // they always will be "..."
        return content.substring(1, content.length() - 1);
    }
}
