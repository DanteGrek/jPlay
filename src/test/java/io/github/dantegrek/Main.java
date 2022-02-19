package io.github.dantegrek;


import static io.github.dantegrek.actions.GoogleSearch.googleSearch;
import static io.github.dantegrek.jplay.Actor.actor;

public class Main {


    public static void main(String[] args) {
        actor()
                .browserConfig()
                .withHeadless(false)
                .andActor()
                .startBrowser()
                .navigateTo("https://google.com")
                .attemptTo(googleSearch())
                .search("Playwright")
                .verifyIfSearchResultsContainsText("Playwright");
        actor()
                .closeBrowser();
    }
}
