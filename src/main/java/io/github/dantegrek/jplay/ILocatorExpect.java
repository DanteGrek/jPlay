package io.github.dantegrek.jplay;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents all expects for locator
 */
public interface ILocatorExpect {


    /**
     * Syntax sugar.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect is();

    /**
     * Syntax sugar.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect has();

    /**
     * Syntax sugar.
     *
     * @return instance of ILocatorExpect
     */
    Expect and();

    /**
     * Syntax sugar.
     *
     * @return instance of Actor
     */
    Actor andActor();

    /**
     * This method reverse next check in chain.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect not();

    /**
     * This method performs all checks.
     *
     * @return instance of Actor
     */
    Actor checkAll();

    /**
     * This method performs all checks.
     *
     * @return instance of Actor
     */
    Actor verifyAll();

    /**
     * Ensures the Locator points to a visible DOM node.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect isVisible();

    /**
     * Ensures the Locator points to a visible DOM node.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect visible();

    /**
     * Ensures the Locator points to a hidden DOM node, which is the opposite of visible.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect isHidden();

    /**
     * Ensures the Locator points to a hidden DOM node, which is the opposite of visible.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hidden();

    /**
     * Ensures the Locator points to a checked input.
     *
     * @return instance if ILocatorExpect
     */
    ILocatorExpect isChecked();

    /**
     * Ensures the Locator points to a checked input.
     *
     * @return instance if ILocatorExpect
     */
    ILocatorExpect checked();

    /**
     * Ensures the Locator points to a disabled element.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect isDisabled();

    /**
     * Ensures the Locator points to a disabled element.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect disabled();

    /**
     * Ensures the Locator points to an enabled element.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect isEnabled();

    /**
     * Ensures the Locator points to an enabled element.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect enabled();

    /**
     * Ensures the Locator points to an editable element.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect isEditable();

    /**
     * Ensures the Locator points to an editable element.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect editable();

    /**
     * Ensures the Locator points to a focused DOM node.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect isFocused();

    /**
     * Ensures the Locator points to a focused DOM node.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect focused();

    // Text checks

    /**
     * Check if locator has text in text content.
     *
     * @param expectedText expected text.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasText(String expectedText);

    /**
     * Check if locator has text in text content.
     *
     * @param expectedText expected text.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect text(String expectedText);

    /**
     * Check if locator has a pattern in text content.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasTextPattern(Pattern pattern);

    /**
     * Check if locator has a pattern in text content.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect textPattern(Pattern pattern);

    /**
     * Check if list of locators has an array of text strings in text content.
     *
     * @param expectedTexts array of expected text strings.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasTexts(String[] expectedTexts);

    /**
     * Check if list of locators has an array of text strings in text content.
     *
     * @param expectedTexts array of expected text strings.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect texts(String[] expectedTexts);

    /**
     * Check if list of locators has list of text strings in text content.
     *
     * @param expectedTexts List of expected text strings.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasTexts(List<String> expectedTexts);

    /**
     * Check if list of locators has list of text strings in text content.
     *
     * @param expectedTexts List of expected text strings.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect texts(List<String> expectedTexts);

    /**
     * Check if list of locators has an array of text strings which follow patterns.
     *
     * @param patterns array of expected text patterns.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasTextPatterns(Pattern[] patterns);

    /**
     * Check if list of locators has an array of text strings which follow patterns.
     *
     * @param patterns array of expected text patterns.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect textPatterns(Pattern[] patterns);

    /**
     * Check if list of locators follow text patterns in text content.
     *
     * @param patterns List of expected text patterns.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasTextPatterns(List<Pattern> patterns);

    /**
     * Check if list of locators follow text patterns in text content.
     *
     * @param patterns List of expected text patterns.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect textPatterns(List<Pattern> patterns);

    /**
     * Check if locator has inner text.
     *
     * @param expectedInnerText expected inner text.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasInnerText(String expectedInnerText);

    /**
     * Check if locator has inner text.
     *
     * @param expectedInnerText expected inner text.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect innerText(String expectedInnerText);

    /**
     * Check if locator has inner text pattern.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasInnerTextPattern(Pattern pattern);

    /**
     * Check if locator has inner text pattern.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect innerTextPattern(Pattern pattern);

    /**
     * Check if array of locators have expected inner texts.
     *
     * @param expectedInnerTexts array of expected inner texts.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasInnerTexts(String[] expectedInnerTexts);

    /**
     * Check if array of locators have expected inner texts.
     *
     * @param expectedInnerTexts array of expected inner texts.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect innerTexts(String[] expectedInnerTexts);

    /**
     * Check if list of locators have expected inner texts.
     *
     * @param expectedInnerTexts list of expected inner texts.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasInnerTexts(List<String> expectedInnerTexts);

    /**
     * Check if list of locators have expected inner texts.
     *
     * @param expectedInnerTexts list of expected inner texts.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect innerTexts(List<String> expectedInnerTexts);

    /**
     * Check if array test in each locator from list follow expected text patterns.
     *
     * @param patterns array of expected inner texts. Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasInnerTextPatterns(Pattern[] patterns);

    /**
     * Check if inner test in each locator from array follow expected text patterns.
     *
     * @param patterns array of expected inner texts. Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect innerTextPatterns(Pattern[] patterns);

    /**
     * Check if inner test in each locator from list follow expected text patterns.
     *
     * @param patterns list of expected inner texts. Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasInnerTextPatterns(List<Pattern> patterns);

    /**
     * Check if inner test in each locator from list follow expected text patterns.
     *
     * @param patterns list of expected inner texts. Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect innerTextPatterns(List<Pattern> patterns);


    /**
     * Check if locator contains text in text content.
     *
     * @param expectedText expected text.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsText(String expectedText);

    /**
     * Check if locator contains a pattern in text content.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsTextPattern(Pattern pattern);

    /**
     * Check if list of locators contains an array of text strings in text content.
     *
     * @param expectedTexts array of expected text strings.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsTexts(String[] expectedTexts);

    /**
     * Check if list of locators contains list of text strings in text content.
     *
     * @param expectedTexts List of expected text strings.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsTexts(List<String> expectedTexts);

    /**
     * Check if list of locators contains an array of text strings which follow patterns.
     *
     * @param patterns array of expected text patterns.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsTextPatterns(Pattern[] patterns);

    /**
     * Check if list of locators contains text patterns in text content.
     *
     * @param patterns List of expected text patterns.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsTextPatterns(List<Pattern> patterns);

    /**
     * Check if locator contains inner text.
     *
     * @param expectedInnerText expected inner text.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsInnerText(String expectedInnerText);

    /**
     * Check if locator contains inner text pattern.
     *
     * @param pattern you should use Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsInnerTextPattern(Pattern pattern);

    /**
     * Check if array of locators contains expected inner texts.
     *
     * @param expectedInnerTexts array of expected inner texts.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsInnerTexts(String[] expectedInnerTexts);

    /**
     * Check if list of locators contains expected inner texts.
     *
     * @param expectedInnerTexts list of expected inner texts.
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsInnerTexts(List<String> expectedInnerTexts);

    /**
     * Check if array test in each locator from list contains expected text patterns.
     *
     * @param patterns array of expected inner texts. Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsInnerTextPatterns(Pattern[] patterns);

    /**
     * Check if inner test in each locator from list contains expected text patterns.
     *
     * @param patterns list of expected inner texts. Pattern.compile("any regex")
     * @return instance of ILocatorExpect
     */
    ILocatorExpect containsInnerTextPatterns(List<Pattern> patterns);

    /**
     * Ensures the Locator points to an element with the given input value. You can use regular expressions for the value as well.
     *
     * @param expectedValue expected result
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasValue(String expectedValue);

    /**
     * Ensures the Locator points to an element with the given input value. You can use regular expressions for the value as well.
     *
     * @param expectedValue expected result
     * @return instance of ILocatorExpect
     */
    ILocatorExpect value(String expectedValue);

    /**
     * Ensures the Locator points to an element with the given input value. You can use regular expressions for the value as well.
     *
     * @param pattern of expected result
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasValuePattern(Pattern pattern);

    /**
     * Ensures the Locator points to an element with the given input value. You can use regular expressions for the value as well.
     *
     * @param pattern of expected result
     * @return instance of ILocatorExpect
     */
    ILocatorExpect valuePattern(Pattern pattern);

    /**
     * Ensures the Locator points to an empty editable element or to a DOM node that has no text.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect isEmpty();

    /**
     * Ensures the Locator points to an empty editable element or to a DOM node that has no text.
     *
     * @return instance of ILocatorExpect
     */
    ILocatorExpect empty();

    // Css and properties checks

    /**
     * Ensures the Locator points to an element with given attribute.
     *
     * @param name  of attribute
     * @param value expected value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasAttribute(String name, String value);

    /**
     * Ensures the Locator points to an element with given attribute.
     *
     * @param name  of attribute
     * @param value expected value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect attribute(String name, String value);

    /**
     * Ensures the Locator points to an element with given attribute.
     *
     * @param name    of attribute
     * @param pattern of expected value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasAttributePattern(String name, Pattern pattern);

    /**
     * Ensures the Locator points to an element with given attribute.
     *
     * @param name    of attribute
     * @param pattern of expected value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect attributePattern(String name, Pattern pattern);

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param clazz css class name
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasClass(String clazz);

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param classes css class names
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasAllClasses(String[] classes);

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param classes css class names
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasAllClasses(List<String> classes);

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param pattern css class name pattern, use: Pattern.compile("any regex");
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasClassPatterns(Pattern pattern);

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param patterns css class name patterns
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasAllClassPatterns(Pattern[] patterns);

    /**
     * Ensures the Locator points to an element with given CSS class.
     *
     * @param patterns css class name patterns
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasAllClassPatterns(List<Pattern> patterns);

    /**
     * Ensures the Locator resolves to an element with the given computed CSS style.
     *
     * @param name  CSS property name
     * @param value CSS property value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasCss(String name, String value);

    /**
     * Ensures the Locator resolves to an element with the given computed CSS style.
     *
     * @param name  CSS property name
     * @param value CSS property value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect css(String name, String value);

    /**
     * Ensures the Locator resolves to an element with the given computed CSS style.
     *
     * @param name    CSS property name
     * @param pattern CSS property pattern to match
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasCss(String name, Pattern pattern);

    /**
     * Ensures the Locator resolves to an element with the given computed CSS style.
     *
     * @param name    CSS property name
     * @param pattern CSS property pattern to match
     * @return instance of ILocatorExpect
     */
    ILocatorExpect css(String name, Pattern pattern);

    /**
     * Ensures the Locator points to an element with given JavaScript property. Note that this property can be of a primitive type as well as a plain serializable JavaScript object.
     *
     * @param name  JS property name
     * @param value JS property value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasJsProperty(String name, String value);

    /**
     * Ensures the Locator points to an element with given JavaScript property. Note that this property can be of a primitive type as well as a plain serializable JavaScript object.
     *
     * @param name  JS property name
     * @param value JS property value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect jsProperty(String name, String value);

    /**
     * Ensures the Locator points to an element with given JavaScript property. Note that this property can be of a primitive type as well as a plain serializable JavaScript object.
     *
     * @param name    JS property name
     * @param pattern JS property value pattern
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasJsProperty(String name, Pattern pattern);

    /**
     * Ensures the Locator points to an element with given JavaScript property. Note that this property can be of a primitive type as well as a plain serializable JavaScript object.
     *
     * @param name    JS property name
     * @param pattern JS property value pattern
     * @return instance of ILocatorExpect
     */
    ILocatorExpect jsProperty(String name, Pattern pattern);

    /**
     * Ensures the Locator points to an element with the given DOM Node ID.
     *
     * @param id String value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect hasId(String id);

    /**
     * Ensures the Locator points to an element with the given DOM Node ID.
     *
     * @param id String value
     * @return instance of ILocatorExpect
     */
    ILocatorExpect id(String id);

    // Count checks

    /**
     * Ensures the Locator resolves to an exact number of DOM nodes.
     *
     * @param count of elements in list
     * @return instance of ILocatorExpect
     */
    public ILocatorExpect hasCount(int count);

    /**
     * Ensures the Locator resolves to an exact number of DOM nodes.
     *
     * @param count of elements in list
     * @return instance of ILocatorExpect
     */
    ILocatorExpect count(int count);
}
