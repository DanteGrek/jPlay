package io.github.dantegrek.screenplay;

import com.microsoft.playwright.Page;

/**
 * This class represents base level of abstraction
 * witch is used in Actor class to insert Actor instance and base helper methods.
 */
abstract class AbstractActivity {
    /**
     *
     */
    protected Actor actor;
    /**
     *
     */
    protected Page p;

    void setActor(Actor actor) {
        this.actor = actor;
        this.p = this.actor.currentPage();
    }

}
