package io.github.dantegrek.actions;

import io.github.dantegrek.jplay.Action;

import java.util.List;

import static io.github.dantegrek.enums.Key.ENTER;

public class GoogleSearch extends Action {

    public static GoogleSearch googleSearch(){
        return new GoogleSearch();
    }

    private String searchSelector = "input[type='text']:visible";
    private String searchResultsSelector = "a>h3:visible";

    public GoogleSearch search(String searchQuery) {
        actor.typeText(searchSelector, searchQuery);
        actor.waitTillNavigationFinished(() -> actor.press(ENTER));
        return this;
    }

    public GoogleSearch verifyIfSearchResultsContainsText(String expectedText) {
        List<String> actualSearchResults = actor.currentFrame().locator(searchResultsSelector).allTextContents();
        System.out.println(actualSearchResults);
        return this;
    }
}
