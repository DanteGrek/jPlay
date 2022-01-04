package io.github.dantegrek.jplay;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PageAssertions;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.github.dantegrek.jplay.JsStrings.JS_PSEUDO_ELEMENT_PROPERTY;

/**
 * This class represents JS style asserts.
 */
public final class Expect {

    private Actor actor;
    private double timeout = 5000;
    private boolean isExpectSoft;
    private List<AssertionFailedError> failedAsserts = new ArrayList<>();
    private Locator locator;
    private LocatorAssertions locatorAssertions;
    private PageAssertions pageAssertions;

    Expect(Actor actor) {
        this.actor = actor;
    }

    Expect withTimeout(double timeout) {
        this.timeout = timeout;
        return this;
    }

    Expect withSoftExpect(boolean isExpectSoft) {
        this.isExpectSoft = isExpectSoft;
        return this;
    }

    private void executeLocatorAssert(IExpect expect) {
        if (this.locatorAssertions == null) {
            throw new RuntimeException("Locator of selector was not specified. Please use .selector(...), " +
                    ".locator(...) or .locators(...) to specify elements under assert.");
        }
        if (isExpectSoft) {
            try {
                expect.doAssert();
            } catch (AssertionFailedError e) {
                failedAsserts.add(e);
            }
        } else {
            expect.doAssert();
        }
        this.locatorAssertions = assertThat(locator);
    }

    private void executePageAssert(IExpect expect) {
        if (this.pageAssertions == null) {
            this.initPageAssertions();
        }
        if (isExpectSoft) {
            try {
                expect.doAssert();
            } catch (AssertionFailedError e) {
                failedAsserts.add(e);
            }
        } else {
            expect.doAssert();
        }
    }

    private void executePseudoElementAssert(IExpect expect) {
        if (isExpectSoft) {
            try {
                expect.doAssert();
            } catch (AssertionFailedError e) {
                failedAsserts.add(e);
            }
        } else {
            expect.doAssert();
        }
    }

    /**
     * Syntax sugar.
     *
     * @return instance of Expect
     */
    public Expect is() {
        return this;
    }

    /**
     * Syntax sugar.
     *
     * @return instance of Expect
     */
    public Expect has() {
        return this;
    }

    /**
     * Syntax sugar.
     *
     * @return instance of Expect
     */
    public Expect and() {
        return this;
    }

    /**
     * Syntax sugar.
     *
     * @return instance of Actor
     */
    public Actor andActor() {
        return this.actor;
    }

    /**
     * This method reverse next check in chain.
     *
     * @return instance of Expect
     */
    public Expect not() {
        if (this.locatorAssertions != null) {
            this.locatorAssertions = this.locatorAssertions.not();
        }
        if (this.pageAssertions != null) {
            this.pageAssertions = this.pageAssertions.not();
        }
        return this;
    }

    /**
     * This method performs all checks.
     *
     * @return instance of Actor
     */
    public Actor checkAll() {
        if (!failedAsserts.isEmpty()) {
            List<String> errorMessages = failedAsserts.stream()
                    .map(error -> error.getMessage() + "\n").collect(Collectors.toList());
            throw new AssertionFailedError(errorMessages.toString());
        }
        return this.actor;
    }

    /**
     * This method performs all checks.
     *
     * @return instance of Actor
     */
    public Actor verifyAll() {
        this.checkAll();
        return this.actor;
    }

    // Init locator assertions

    /**
     * This method point all next checks in chain on this web element.
     *
     * @param selector css or xpath
     * @return instance of Expect
     */
    public Expect selector(String selector) {
        this.locator = this.actor.currentFrame().locator(selector);
        this.locatorAssertions = assertThat(this.locator);
        return this;
    }

    /**
     * This method point all next checks in chain on this web element.
     *
     * @param locator can be created with actor().currentFrame().locator(selector);
     * @return instance of Expect
     */
    public Expect locator(Locator locator) {
        this.locator = locator;
        this.locatorAssertions = assertThat(this.locator);
        return this;
    }

    /**
     * This method point all next checks in chain on this web element. (Syntax sugar)
     *
     * @param locator can be created with actor().currentFrame().locator(selector);
     * @return instance of Expect
     */
    public Expect locators(Locator locator) {
        return this.locator(locator);
    }

    // Init page assertions

    private Expect initPageAssertions() {
        if (this.actor.currentPage() == null) {
            throw new RuntimeException("You can not do 'expect' without page/tab. Please use startBrowser() " +
                    "or startPureBrowser().createContextAndTab() before.");
        }
        this.pageAssertions = assertThat(this.actor.currentPage());
        return this;
    }

    /**
     * This method inits page asserts and allows use page().has().not().title(...)
     *
     * @return instance of Expect
     */
    public Expect page() {
        return this.initPageAssertions();
    }

    /**
     * This method inits page asserts and allows use tab().has().not().url(...)
     *
     * @return instance of Expect
     */
    public Expect tab() {
        return this.initPageAssertions();
    }

    // Element state checks

    /**
     * Ensures the Locator points to a visible DOM node.
     *
     * @return instance of Expect
     */
    public Expect isVisible() {
        this.executeLocatorAssert(() -> locatorAssertions.isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to a visible DOM node.
     *
     * @return instance of Expect
     */
    public Expect visible() {
        return this.isVisible();
    }

    /**
     * Ensures the Locator points to a hidden DOM node, which is the opposite of visible.
     *
     * @return instance of Expect
     */
    public Expect isHidden() {
        this.executeLocatorAssert(() -> locatorAssertions.isHidden(new LocatorAssertions.IsHiddenOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to a hidden DOM node, which is the opposite of visible.
     *
     * @return instance of Expect
     */
    public Expect hidden() {
        return isHidden();
    }

    /**
     * Ensures the Locator points to a checked input.
     *
     * @return instance if Expect
     */
    public Expect isChecked() {
        this.executeLocatorAssert(() -> locatorAssertions.isChecked(new LocatorAssertions.IsCheckedOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to a checked input.
     *
     * @return instance if Expect
     */
    public Expect checked() {
        return this.isChecked();
    }

    /**
     * Ensures the Locator points to a disabled element.
     *
     * @return instance of Expect
     */
    public Expect isDisabled() {
        this.executeLocatorAssert(() -> locatorAssertions.isDisabled(new LocatorAssertions.IsDisabledOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to a disabled element.
     *
     * @return instance of Expect
     */
    public Expect disabled() {
        return this.isDisabled();
    }

    /**
     * Ensures the Locator points to an enabled element.
     *
     * @return instance of Expect
     */
    public Expect isEnabled() {
        this.executeLocatorAssert(() -> locatorAssertions.isEnabled(new LocatorAssertions.IsEnabledOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an enabled element.
     *
     * @return instance of Expect
     */
    public Expect enabled() {
        return this.isEnabled();
    }

    /**
     * Ensures the Locator points to an editable element.
     *
     * @return instance of Expect
     */
    public Expect isEditable() {
        this.executeLocatorAssert(() -> locatorAssertions.isEditable(new LocatorAssertions.IsEditableOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an editable element.
     *
     * @return instance of Expect
     */
    public Expect editable() {
        return this.isEditable();
    }

    /**
     * Ensures the Locator points to a focused DOM node.
     *
     * @return instance of Expect
     */
    public Expect isFocused() {
        this.executeLocatorAssert(() -> locatorAssertions.isFocused(new LocatorAssertions.IsFocusedOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to a focused DOM node.
     *
     * @return instance of Expect
     */
    public Expect focused() {
        return isFocused();
    }

    // Text checks

    /**
     * Check if locator has text in text content.
     *
     * @param expectedText expected text.
     * @return instance of Expect
     */
    public Expect hasText(String expectedText) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(expectedText, new LocatorAssertions.HasTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if locator has text in text content.
     *
     * @param expectedText expected text.
     * @return instance of Expect
     */
    public Expect text(String expectedText) {
        return this.hasText(expectedText);
    }

    /**
     * Check if locator has a pattern in text content.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect hasTextPattern(Pattern pattern) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(pattern, new LocatorAssertions.HasTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if locator has a pattern in text content.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect textPattern(Pattern pattern) {
        return this.hasTextPattern(pattern);
    }

    /**
     * Check if list of locators has an array of text strings in text content.
     *
     * @param expectedTexts array of expected text strings.
     * @return instance of Expect
     */
    public Expect hasTexts(String[] expectedTexts) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(expectedTexts, new LocatorAssertions.HasTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if list of locators has an array of text strings in text content.
     *
     * @param expectedTexts array of expected text strings.
     * @return instance of Expect
     */
    public Expect texts(String[] expectedTexts) {
        return this.hasTexts(expectedTexts);
    }

    /**
     * Check if list of locators has list of text strings in text content.
     *
     * @param expectedTexts List of expected text strings.
     * @return instance of Expect
     */
    public Expect hasTexts(List<String> expectedTexts) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(expectedTexts.toArray(String[]::new), new LocatorAssertions.HasTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if list of locators has list of text strings in text content.
     *
     * @param expectedTexts List of expected text strings.
     * @return instance of Expect
     */
    public Expect texts(List<String> expectedTexts) {
        return this.hasTexts(expectedTexts);
    }

    /**
     * Check if list of locators has an array of text strings which follow patterns.
     *
     * @param patterns array of expected text patterns.
     * @return instance of Expect
     */
    public Expect hasTextPatterns(Pattern[] patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(patterns, new LocatorAssertions.HasTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if list of locators has an array of text strings which follow patterns.
     *
     * @param patterns array of expected text patterns.
     * @return instance of Expect
     */
    public Expect textPatterns(Pattern[] patterns) {
        return this.hasTextPatterns(patterns);
    }

    /**
     * Check if list of locators follow text patterns in text content.
     *
     * @param patterns List of expected text patterns.
     * @return instance of Expect
     */
    public Expect hasTextPatterns(List<Pattern> patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(patterns.toArray(String[]::new), new LocatorAssertions.HasTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if list of locators follow text patterns in text content.
     *
     * @param patterns List of expected text patterns.
     * @return instance of Expect
     */
    public Expect textPatterns(List<Pattern> patterns) {
        return this.hasTextPatterns(patterns);
    }

    /**
     * Check if locator has inner text.
     *
     * @param expectedInnerText expected inner text.
     * @return instance of Expect
     */
    public Expect hasInnerText(String expectedInnerText) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(expectedInnerText,
                new LocatorAssertions.HasTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if locator has inner text.
     *
     * @param expectedInnerText expected inner text.
     * @return instance of Expect
     */
    public Expect innerText(String expectedInnerText) {
        return this.hasInnerText(expectedInnerText);
    }

    /**
     * Check if locator has inner text pattern.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect hasInnerTextPattern(Pattern pattern) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(pattern,
                new LocatorAssertions.HasTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if locator has inner text pattern.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect innerTextPattern(Pattern pattern) {
        return this.hasInnerTextPattern(pattern);
    }

    /**
     * Check if array of locators have expected inner texts.
     *
     * @param expectedInnerTexts array of expected inner texts.
     * @return instance of Expect
     */
    public Expect hasInnerTexts(String[] expectedInnerTexts) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(expectedInnerTexts,
                new LocatorAssertions.HasTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if array of locators have expected inner texts.
     *
     * @param expectedInnerTexts array of expected inner texts.
     * @return instance of Expect
     */
    public Expect innerTexts(String[] expectedInnerTexts) {
        return this.hasInnerTexts(expectedInnerTexts);
    }

    /**
     * Check if list of locators have expected inner texts.
     *
     * @param expectedInnerTexts list of expected inner texts.
     * @return instance of Expect
     */
    public Expect hasInnerTexts(List<String> expectedInnerTexts) {
        this.executeLocatorAssert(new IExpect() {
            @Override
            public void doAssert() {
                locatorAssertions.hasText(expectedInnerTexts.toArray(String[]::new),
                        new LocatorAssertions.HasTextOptions().setUseInnerText(true).setTimeout(timeout));
            }
        });
        return this;
    }

    /**
     * Check if list of locators have expected inner texts.
     *
     * @param expectedInnerTexts list of expected inner texts.
     * @return instance of Expect
     */
    public Expect innerTexts(List<String> expectedInnerTexts) {
        return this.hasInnerTexts(expectedInnerTexts);
    }

    /**
     * Check if array test in each locator from list follow expected text patterns.
     *
     * @param patterns array of expected inner texts. Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect hasInnerTextPatterns(Pattern[] patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(patterns,
                new LocatorAssertions.HasTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if inner test in each locator from array follow expected text patterns.
     *
     * @param patterns array of expected inner texts. Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect innerTextPatterns(Pattern[] patterns) {
        return this.hasInnerTextPatterns(patterns);
    }

    /**
     * Check if inner test in each locator from list follow expected text patterns.
     *
     * @param patterns list of expected inner texts. Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect hasInnerTextPatterns(List<Pattern> patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.hasText(patterns.toArray(String[]::new),
                new LocatorAssertions.HasTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if inner test in each locator from list follow expected text patterns.
     *
     * @param patterns list of expected inner texts. Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect innerTextPatterns(List<Pattern> patterns) {
        return this.hasInnerTextPatterns(patterns);
    }


    /**
     * Check if locator contains text in text content.
     *
     * @param expectedText expected text.
     * @return instance of Expect
     */
    public Expect containsText(String expectedText) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(expectedText, new LocatorAssertions.ContainsTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if locator contains a pattern in text content.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect containsTextPattern(Pattern pattern) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(pattern, new LocatorAssertions.ContainsTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if list of locators contains an array of text strings in text content.
     *
     * @param expectedTexts array of expected text strings.
     * @return instance of Expect
     */
    public Expect containsTexts(String[] expectedTexts) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(expectedTexts, new LocatorAssertions.ContainsTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if list of locators contains list of text strings in text content.
     *
     * @param expectedTexts List of expected text strings.
     * @return instance of Expect
     */
    public Expect containsTexts(List<String> expectedTexts) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(expectedTexts.toArray(String[]::new), new LocatorAssertions.ContainsTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if list of locators contains an array of text strings which follow patterns.
     *
     * @param patterns array of expected text patterns.
     * @return instance of Expect
     */
    public Expect containsTextPatterns(Pattern[] patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(patterns, new LocatorAssertions.ContainsTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if list of locators contains text patterns in text content.
     *
     * @param patterns List of expected text patterns.
     * @return instance of Expect
     */
    public Expect containsTextPatterns(List<Pattern> patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(patterns.toArray(String[]::new), new LocatorAssertions.ContainsTextOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Check if locator contains inner text.
     *
     * @param expectedInnerText expected inner text.
     * @return instance of Expect
     */
    public Expect containsInnerText(String expectedInnerText) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(expectedInnerText,
                new LocatorAssertions.ContainsTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if locator contains inner text pattern.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect containsInnerTextPattern(Pattern pattern) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(pattern,
                new LocatorAssertions.ContainsTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if array of locators contains expected inner texts.
     *
     * @param expectedInnerTexts array of expected inner texts.
     * @return instance of Expect
     */
    public Expect containsInnerTexts(String[] expectedInnerTexts) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(expectedInnerTexts,
                new LocatorAssertions.ContainsTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if list of locators contains expected inner texts.
     *
     * @param expectedInnerTexts list of expected inner texts.
     * @return instance of Expect
     */
    public Expect containsInnerTexts(List<String> expectedInnerTexts) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(expectedInnerTexts.toArray(String[]::new),
                new LocatorAssertions.ContainsTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if array test in each locator from list contains expected text patterns.
     *
     * @param patterns array of expected inner texts. Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect containsInnerTextPatterns(Pattern[] patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(patterns,
                new LocatorAssertions.ContainsTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Check if inner test in each locator from list contains expected text patterns.
     *
     * @param patterns list of expected inner texts. Pattern.compile("any regex")
     * @return instance of Expect
     */
    public Expect containsInnerTextPatterns(List<Pattern> patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.containsText(patterns.toArray(String[]::new),
                new LocatorAssertions.ContainsTextOptions().setUseInnerText(true).setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with the given input value. You can use regular expressions for the value as well.
     *
     * @param expectedValue expected result
     * @return instance of Expect
     */
    public Expect hasValue(String expectedValue) {
        this.executeLocatorAssert(() -> locatorAssertions.hasValue(expectedValue, new LocatorAssertions.HasValueOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with the given input value. You can use regular expressions for the value as well.
     *
     * @param expectedValue expected result
     * @return instance of Expect
     */
    public Expect value(String expectedValue) {
        return this.hasValue(expectedValue);
    }

    /**
     * Ensures the Locator points to an element with the given input value. You can use regular expressions for the value as well.
     *
     * @param pattern of expected result
     * @return instance of Expect
     */
    public Expect hasValuePattern(Pattern pattern) {
        this.executeLocatorAssert(() -> locatorAssertions.hasValue(pattern, new LocatorAssertions.HasValueOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with the given input value. You can use regular expressions for the value as well.
     *
     * @param pattern of expected result
     * @return instance of Expect
     */
    public Expect valuePattern(Pattern pattern) {
        return this.hasValuePattern(pattern);
    }

    /**
     * Ensures the Locator points to an empty editable element or to a DOM node that has no text.
     *
     * @return instance of Expect
     */
    public Expect isEmpty() {
        this.executeLocatorAssert(() -> locatorAssertions.isEmpty(new LocatorAssertions.IsEmptyOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an empty editable element or to a DOM node that has no text.
     *
     * @return instance of Expect
     */
    public Expect empty() {
        return this.isEmpty();
    }

    // Css and properties checks

    /**
     * Ensures the Locator points to an element with given attribute.
     *
     * @param name  of attribute
     * @param value expected value
     * @return instance of Expect
     */
    public Expect hasAttribute(String name, String value) {
        this.executeLocatorAssert(() -> locatorAssertions.hasAttribute(name, value, new LocatorAssertions.HasAttributeOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with given attribute.
     *
     * @param name  of attribute
     * @param value expected value
     * @return instance of Expect
     */
    public Expect attribute(String name, String value) {
        return this.hasAttribute(name, value);
    }

    /**
     * Ensures the Locator points to an element with given attribute.
     *
     * @param name of attribute
     * @param pattern of expected value
     * @return instance of Expect
     */
    public Expect hasAttributePattern(String name, Pattern pattern) {
        this.executeLocatorAssert(() -> locatorAssertions.hasAttribute(name, pattern, new LocatorAssertions.HasAttributeOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with given attribute.
     * @param name of attribute
     * @param pattern of expected value
     * @return instance of Expect
     */
    public Expect attributePattern(String name, Pattern pattern) {
        return this.hasAttributePattern(name, pattern);
    }

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param clazz css class name
     * @return instance of Expect
     */
    public Expect hasClass(String clazz) {
        this.executeLocatorAssert(() -> locatorAssertions.hasClass(clazz, new LocatorAssertions.HasClassOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param classes css class names
     * @return instance of Expect
     */
    public Expect hasAllClasses(String[] classes) {
        this.executeLocatorAssert(() -> locatorAssertions.hasClass(classes, new LocatorAssertions.HasClassOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param classes css class names
     * @return instance of Expect
     */
    public Expect hasAllClasses(List<String> classes) {
        this.executeLocatorAssert(() -> locatorAssertions.hasClass(classes.toArray(String[]::new), new LocatorAssertions.HasClassOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param pattern css class name pattern, use: Pattern.compile("any regex");
     * @return instance of Expect
     */
    public Expect hasClassPatterns(Pattern pattern) {
        this.executeLocatorAssert(() -> locatorAssertions.hasClass(pattern, new LocatorAssertions.HasClassOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param patterns css class name patterns
     * @return instance of Expect
     */
    public Expect hasAllClassPatterns(Pattern[] patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.hasClass(patterns, new LocatorAssertions.HasClassOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param patterns css class name patterns
     * @return instance of Expect
     */
    public Expect hasAllClassPatterns(List<Pattern> patterns) {
        this.executeLocatorAssert(() -> locatorAssertions.hasClass(patterns.toArray(Pattern[]::new), new LocatorAssertions.HasClassOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator resolves to an element with the given computed CSS style.
     *
     * @param name  CSS property name
     * @param value CSS property value
     * @return instance of Expect
     */
    public Expect hasCss(String name, String value) {
        this.executeLocatorAssert(() -> locatorAssertions.hasCSS(name, value, new LocatorAssertions.HasCSSOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator resolves to an element with the given computed CSS style.
     *
     * @param name  CSS property name
     * @param value CSS property value
     * @return instance of Expect
     */
    public Expect css(String name, String value) {
        return this.hasCss(name, value);
    }

    /**
     * Ensures the Locator resolves to an element with the given computed CSS style.
     *
     * @param name    CSS property name
     * @param pattern CSS property pattern to match
     * @return instance of Expect
     */
    public Expect hasCss(String name, Pattern pattern) {
        this.executeLocatorAssert(() -> locatorAssertions.hasCSS(name, pattern, new LocatorAssertions.HasCSSOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator resolves to an element with the given computed CSS style.
     *
     * @param name    CSS property name
     * @param pattern CSS property pattern to match
     * @return instance of Expect
     */
    public Expect css(String name, Pattern pattern) {
        return this.hasCss(name, pattern);
    }

    /**
     * Ensures the Locator points to an element with given JavaScript property. Note that this property can be of a primitive type as well as a plain serializable JavaScript object.
     *
     * @param name  JS property name
     * @param value JS property value
     * @return instance of Expect
     */
    public Expect hasJsProperty(String name, String value) {
        this.executeLocatorAssert(() -> locatorAssertions.hasJSProperty(name, value, new LocatorAssertions.HasJSPropertyOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with given JavaScript property. Note that this property can be of a primitive type as well as a plain serializable JavaScript object.
     *
     * @param name  JS property name
     * @param value JS property value
     * @return instance of Expect
     */
    public Expect jsProperty(String name, String value) {
        return this.hasJsProperty(name, value);
    }

    /**
     * Ensures the Locator points to an element with given JavaScript property. Note that this property can be of a primitive type as well as a plain serializable JavaScript object.
     *
     * @param name    JS property name
     * @param pattern JS property value pattern
     * @return instance of Expect
     */
    public Expect hasJsProperty(String name, Pattern pattern) {
        this.executeLocatorAssert(() -> locatorAssertions.hasJSProperty(name, pattern, new LocatorAssertions.HasJSPropertyOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with given JavaScript property. Note that this property can be of a primitive type as well as a plain serializable JavaScript object.
     *
     * @param name    JS property name
     * @param pattern JS property value pattern
     * @return instance of Expect
     */
    public Expect jsProperty(String name, Pattern pattern) {
        return this.hasJsProperty(name, pattern);
    }

    /**
     * Ensures the Locator points to an element with the given DOM Node ID.
     *
     * @param id String value
     * @return instance of Expect
     */
    public Expect hasId(String id) {
        this.executeLocatorAssert(() -> locatorAssertions.hasId(id, new LocatorAssertions.HasIdOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator points to an element with the given DOM Node ID.
     *
     * @param id String value
     * @return instance of Expect
     */
    public Expect id(String id) {
        return this.hasId(id);
    }

    // Count checks

    /**
     * Ensures the Locator resolves to an exact number of DOM nodes.
     *
     * @param count of elements in list
     * @return instance of Expect
     */
    public Expect hasCount(int count) {
        this.executeLocatorAssert(() -> locatorAssertions.hasCount(count, new LocatorAssertions.HasCountOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the Locator resolves to an exact number of DOM nodes.
     *
     * @param count of elements in list
     * @return instance of Expect
     */
    public Expect count(int count) {
        return this.hasCount(count);
    }

    // Pseudo elements asserts

    private void assertThatPropertyIsEmpty(String property, String propertyName) {
        if (!property.isEmpty()) {
            throw new AssertionFailedError(String.format("Pseudo element property '%s' exists or not empty.\nValue: %s", propertyName, property));
        }
    }

    private void assertThatPropertyValueEqual(String propertyName, String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionFailedError(String.format("Pseudo element property '%s' not match.\nExpected: %s\nActual: %s", propertyName, expected, actual));
        }
    }

    private void assertThatPropertyContains(String propertyName, String expected, String actual) {
        if (!actual.contains(expected)) {
            throw new AssertionFailedError(String.format("Pseudo element property '%s' not contains:\nValue: %s\nShould contain: %s", propertyName, actual, expected));
        }
    }

    private void assertThatPropertyNotContains(String propertyName, String expected, String actual) {
        if (actual.contains(expected)) {
            throw new AssertionFailedError(String.format("Pseudo element property '%s' contains:\nValue: %s\nShould not contain: %s", propertyName, actual, expected));
        }
    }

    /**
     * Ensures the cssSelector is visible and pseudo-element has property with value.
     *
     * @param cssSelector   css selector which points on element with ::after or ::before
     * @param pseudoElement after, before or any another pseudo-element.
     * @param property      property name
     * @param value         expected property value
     * @return instance of Expect
     */
    public Expect visiblePseudoElementHasPropertyWithValue(String cssSelector, String pseudoElement, String property, String value) {
        this.executePseudoElementAssert(() -> {
            assertThat(actor.currentFrame().locator(cssSelector)).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(timeout));
            assertThatPropertyValueEqual(property, value, (String) actor.currentFrame().evaluate(
                    String.format(JS_PSEUDO_ELEMENT_PROPERTY, cssSelector, pseudoElement, property)));
        });
        return this;
    }

    /**
     * Ensures the cssSelector is visible and pseudo-element do not have property.
     *
     * @param cssSelector   css selector which points on element with ::after or ::before
     * @param pseudoElement after, before or any another pseudo-element.
     * @param property      property name
     * @return instance of Expect
     */
    public Expect visiblePseudoElementHasNotProperty(String cssSelector, String pseudoElement, String property) {
        this.executePseudoElementAssert(() -> {
            assertThat(actor.currentFrame().locator(cssSelector)).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(timeout));
            assertThatPropertyIsEmpty((String) actor.currentFrame().evaluate(
                    String.format(JS_PSEUDO_ELEMENT_PROPERTY, cssSelector, pseudoElement, property)), property);
        });
        return this;
    }

    /**
     * Ensures the cssSelector is visible and pseudo-element has property which contains value.
     *
     * @param cssSelector   css selector which points on element with ::after or ::before
     * @param pseudoElement after, before or any another pseudo-element.
     * @param property      property name
     * @param value         expected property value
     * @return instance of Expect
     */
    public Expect visiblePseudoElementPropertyContains(String cssSelector, String pseudoElement, String property, String value) {
        this.executePseudoElementAssert(() -> {
            assertThat(actor.currentFrame().locator(cssSelector)).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(timeout));
            assertThatPropertyContains(property, value, (String) actor.currentFrame().evaluate(
                    String.format(JS_PSEUDO_ELEMENT_PROPERTY, cssSelector, pseudoElement, property)));
        });
        return this;
    }

    /**
     * Ensures the cssSelector is visible and pseudo-element has property which not contains value.
     *
     * @param cssSelector   css selector which points on element with ::after or ::before
     * @param pseudoElement after, before or any another pseudo-element.
     * @param property      property name
     * @param value         expected property value
     * @return instance of Expect
     */
    public Expect visiblePseudoElementPropertyNotContains(String cssSelector, String pseudoElement, String property, String value) {
        this.executePseudoElementAssert(() -> {
            assertThat(actor.currentFrame().locator(cssSelector)).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(timeout));
            assertThatPropertyNotContains(property, value, (String) actor.currentFrame().evaluate(
                    String.format(JS_PSEUDO_ELEMENT_PROPERTY, cssSelector, pseudoElement, property)));
        });
        return this;
    }

    // Page asserts

    /**
     * Ensures the page is navigated to the given URL.
     *
     * @param url string or regex
     * @return instance of Expect
     */
    public Expect url(String url) {
        this.executePageAssert(() -> pageAssertions.hasURL(url, new PageAssertions.HasURLOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the page is navigated to the given URL.
     *
     * @param pattern use as Pattern.compile(...);
     * @return instance of Expect
     */
    public Expect url(Pattern pattern) {
        this.executePageAssert(() -> pageAssertions.hasURL(pattern, new PageAssertions.HasURLOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the page has the given title.
     *
     * @param title string
     * @return instance of Expect
     */
    public Expect title(String title) {
        this.executePageAssert(() -> pageAssertions.hasTitle(title, new PageAssertions.HasTitleOptions().setTimeout(timeout)));
        return this;
    }

    /**
     * Ensures the page has the given title.
     *
     * @param pattern Pattern.compile(...);
     * @return instance of Expect
     */
    public Expect title(Pattern pattern) {
        this.executePageAssert(() -> pageAssertions.hasTitle(pattern, new PageAssertions.HasTitleOptions().setTimeout(timeout)));
        return this;
    }
}
