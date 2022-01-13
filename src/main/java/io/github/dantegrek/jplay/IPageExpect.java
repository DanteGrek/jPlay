package io.github.dantegrek.jplay;

import java.util.regex.Pattern;

/**
 * All page assert methods
 */
public interface IPageExpect {

    /**
     * Syntax sugar.
     *
     * @return instance of Actor
     */
    Actor actor();

    /**
     * This method reverse next check in chain.
     *
     * @return instance of IPageExpect
     */
    IPageExpect pageNot();

    /**
     * Ensures the page is navigated to the given URL.
     *
     * @param url string or regex
     * @return instance of IPageExpect
     */
    IPageExpect url(String url);

    /**
     * Ensures the page is navigated to the given URL.
     *
     * @param pattern use as Pattern.compile(...);
     * @return instance of IPageExpect
     */
    IPageExpect url(Pattern pattern);

    /**
     * Ensures the page has the given title.
     *
     * @param title string
     * @return instance of IPageExpect
     */
    IPageExpect title(String title);

    /**
     * Ensures the page has the given title.
     *
     * @param pattern Pattern.compile(...);
     * @return instance of IPageExpect
     */
    IPageExpect title(Pattern pattern);
}
